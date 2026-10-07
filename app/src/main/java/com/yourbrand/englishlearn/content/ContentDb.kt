package com.yourbrand.englishlearn.content

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream
import java.text.Normalizer
import java.util.Locale
import com.yourbrand.englishlearn.ui.Perf

data class DbWordHit(val id: String, val lemma: String, val gloss: String, val level: Int, val tier: String)

/** Read-only access to the build-time content database. No content is rebuilt on the device. */
class ContentDb(private val context: Context) {
    private val file = File(context.noBackupFilesDir, "content.db")
    private var database: SQLiteDatabase? = null

    @Synchronized
    private fun db(): SQLiteDatabase {
        database?.takeIf { it.isOpen }?.let { return it }
        val started = System.nanoTime()
        val tmp = File(context.cacheDir, "content.db.tmp")
        val expected = context.assets.open("content/content.db.sha256").bufferedReader().use { it.readText().trim() }
        val hashFile = File(context.noBackupFilesDir, "content.db.sha256")
        val current = if (file.isFile && hashFile.isFile) hashFile.readText().trim() else null
        if (expected != current) {
            context.assets.open("content/content.db").use { input -> FileOutputStream(tmp).use { output -> input.copyTo(output) } }
            file.parentFile?.mkdirs()
            tmp.copyTo(file, overwrite = true)
            hashFile.writeText(expected)
        }
        tmp.delete()
        return SQLiteDatabase.openDatabase(file.path, null, SQLiteDatabase.OPEN_READONLY).also {
            database = it
            Perf.log("content db ready ${Perf.ms(started, System.nanoTime())} copied=${expected != current}")
        }
    }

    fun searchWords(locale: String, query: String, limit: Int = 30): List<DbWordHit> {
        val normalizedQuery = normalize(query.trim())
        val tokens = normalizedQuery.split("[^\\p{L}\\p{N}]+".toRegex()).filter { it.isNotBlank() }
        if (tokens.isEmpty()) return emptyList()
        // Space = AND in every FTS4 query syntax; the literal "AND" is a search term without the
        // enhanced syntax, which made multi-word queries ("tau dien ngam") find nothing.
        val match = tokens.joinToString(" ") { "$it*" }
        // A query typed with Vietnamese marks is a meaning search: rank gloss matches above English
        // words that merely start with the same letters ("đi" -> go before dictionary).
        val meaningFirst = normalizedQuery != query.trim().lowercase(Locale.ROOT)
        val glossWord = "(f.gloss LIKE ?1 || ' %' OR f.gloss LIKE '% ' || ?1 OR f.gloss LIKE '% ' || ?1 || ' %')"
        val middle = if (meaningFirst) "WHEN $glossWord THEN 4 WHEN instr(f.gloss, ?1) > 0 THEN 5 WHEN w.lemma_norm LIKE ?1 || '%' THEN 6"
        else "WHEN w.lemma_norm LIKE ?1 || '%' THEN 4 WHEN $glossWord THEN 5 WHEN instr(f.gloss, ?1) > 0 THEN 6"
        // With marks typed, "bàn" must not match the English word "ban": compare the lemma as typed.
        val exactLemma = if (meaningFirst) "WHEN lower(f.lemma) = ?5 THEN 0" else "WHEN w.lemma_norm = ?1 THEN 0"
        val started = System.nanoTime()
        val result = ArrayList<DbWordHit>()
        db().rawQuery(
            "SELECT f.word_id, f.lemma, f.gloss_raw, w.level, w.tier FROM word_fts f JOIN word w ON w.id = f.word_id " +
                "WHERE f.locale = ?2 AND w.adult = 0 AND word_fts MATCH ?3 " +
                "ORDER BY CASE $exactLemma WHEN lower(f.gloss_raw) = ?5 OR lower(f.gloss_raw) LIKE ?5 || ';%' OR lower(f.gloss_raw) LIKE ?5 || ',%' THEN 1 WHEN (' ' || replace(replace(replace(replace(lower(f.gloss_raw), ',', ' '), ';', ' '), '(', ' '), ')', ' ') || ' ') LIKE '% ' || ?5 || ' %' THEN 2 WHEN f.gloss = ?1 THEN 3 $middle ELSE 7 END, w.level, " +
                "CASE w.tier WHEN 'gold' THEN 0 WHEN 'silver' THEN 1 ELSE 2 END, " +
                "CASE WHEN w.ngsl_rank IS NULL THEN 2147483647 ELSE w.ngsl_rank END, w.lemma_norm LIMIT ?4",
            arrayOf(normalizedQuery, locale, match, limit.toString(), query.trim().lowercase(Locale.ROOT)),
        ).use { cursor ->
            while (cursor.moveToNext()) result += DbWordHit(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4))
        }
        Perf.log("searchWords query=${query.trim()} hits=${result.size} ${Perf.ms(started, System.nanoTime())}")
        return result
    }

    fun wordsByLevel(locale: String, level: Int, limit: Int = 200): List<DbWordHit> =
        queryWords(locale, "SELECT DISTINCT f.word_id, f.lemma, f.gloss_raw, w.level, w.tier FROM word_fts f JOIN word w ON w.id = f.word_id WHERE f.locale = ? AND w.adult = 0 AND w.level = ? ORDER BY w.tier, f.lemma LIMIT ?", arrayOf(locale, level.toString(), limit.toString()))

    fun wordsByTopic(locale: String, topicId: String, limit: Int = 200): List<DbWordHit> =
        queryWords(locale, "SELECT DISTINCT f.word_id, f.lemma, f.gloss_raw, w.level, w.tier FROM word_fts f JOIN word w ON w.id = f.word_id JOIN word_topic wt ON wt.word_id = w.id WHERE f.locale = ? AND w.adult = 0 AND wt.topic_id = ? ORDER BY w.level, f.lemma LIMIT ?", arrayOf(locale, topicId, limit.toString()))

    fun word(locale: String, id: String): DbWordHit? =
        queryWords(locale, "SELECT f.word_id, f.lemma, f.gloss_raw, w.level, w.tier FROM word_fts f JOIN word w ON w.id = f.word_id WHERE f.locale = ? AND w.adult = 0 AND f.word_id = ? LIMIT 1", arrayOf(locale, id)).firstOrNull()

    private fun queryWords(locale: String, sql: String, args: Array<String>): List<DbWordHit> {
        val result = ArrayList<DbWordHit>()
        db().rawQuery(sql, args).use { cursor ->
            while (cursor.moveToNext()) result += DbWordHit(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4))
        }
        return result
    }

    private fun readMeta(path: File, key: String): String? {
        SQLiteDatabase.openDatabase(path.path, null, SQLiteDatabase.OPEN_READONLY).use { db ->
            db.rawQuery("SELECT value FROM meta WHERE key = ?", arrayOf(key)).use { cursor -> return if (cursor.moveToFirst()) cursor.getString(0) else null }
        }
    }

    companion object {
        fun normalize(value: String): String {
            val decomposed = Normalizer.normalize(value.lowercase(Locale.ROOT), Normalizer.Form.NFD)
            return decomposed.filter { Character.getType(it) != Character.NON_SPACING_MARK.toInt() }.replace('\u0111', 'd')
        }
    }
}
