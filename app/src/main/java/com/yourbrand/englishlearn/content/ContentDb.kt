package com.yourbrand.englishlearn.content

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import java.io.File
import java.io.FileOutputStream
import java.text.Normalizer
import java.util.Locale

data class DbWordHit(val id: String, val lemma: String, val gloss: String, val level: Int, val tier: String)

/** Read-only access to the build-time content database. No content is rebuilt on the device. */
class ContentDb(private val context: Context) {
    private val file = File(context.noBackupFilesDir, "content.db")
    private var database: SQLiteDatabase? = null

    @Synchronized
    private fun db(): SQLiteDatabase {
        database?.takeIf { it.isOpen }?.let { return it }
        val tmp = File(context.cacheDir, "content.db.tmp")
        context.assets.open("content/content.db").use { input -> FileOutputStream(tmp).use { output -> input.copyTo(output) } }
        val expected = readMeta(tmp, "content_hash")
        val current = if (file.isFile) runCatching { readMeta(file, "content_hash") }.getOrNull() else null
        if (expected != current) {
            file.parentFile?.mkdirs()
            tmp.copyTo(file, overwrite = true)
        }
        tmp.delete()
        return SQLiteDatabase.openDatabase(file.path, null, SQLiteDatabase.OPEN_READONLY).also { database = it }
    }

    fun searchWords(locale: String, query: String, limit: Int = 30): List<DbWordHit> {
        val tokens = normalize(query).split("[^\\p{L}\\p{N}]+".toRegex()).filter { it.isNotBlank() }
        if (tokens.isEmpty()) return emptyList()
        val match = tokens.joinToString(" AND ") { "$it*" }
        val candidates = ArrayList<DbWordHit>()
        db().rawQuery(
            "SELECT f.word_id, f.lemma, f.gloss, w.level, w.tier FROM word_fts f JOIN word w ON w.id = f.word_id " +
                "WHERE f.locale = ? AND word_fts MATCH ? LIMIT 200",
            arrayOf(locale, match),
        ).use { cursor ->
            while (cursor.moveToNext()) candidates += DbWordHit(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getString(4))
        }
        return candidates.sortedWith(compareBy<DbWordHit> {
            when { normalize(it.lemma) == normalize(query) -> 0; normalize(it.lemma).startsWith(normalize(query)) -> 1; else -> 2 }
        }.thenBy { it.level }.thenBy { if (it.tier == "silver" || it.tier == "gold") 0 else 1 }.thenBy { it.lemma }).take(limit)
    }

    fun wordsByLevel(locale: String, level: Int, limit: Int = 200): List<DbWordHit> =
        queryWords(locale, "SELECT DISTINCT f.word_id, f.lemma, f.gloss, w.level, w.tier FROM word_fts f JOIN word w ON w.id = f.word_id WHERE f.locale = ? AND w.level = ? ORDER BY w.tier, f.lemma LIMIT ?", arrayOf(locale, level.toString(), limit.toString()))

    fun wordsByTopic(locale: String, topicId: String, limit: Int = 200): List<DbWordHit> =
        queryWords(locale, "SELECT DISTINCT f.word_id, f.lemma, f.gloss, w.level, w.tier FROM word_fts f JOIN word w ON w.id = f.word_id JOIN word_topic wt ON wt.word_id = w.id WHERE f.locale = ? AND wt.topic_id = ? ORDER BY w.level, f.lemma LIMIT ?", arrayOf(locale, topicId, limit.toString()))

    fun word(locale: String, id: String): DbWordHit? =
        queryWords(locale, "SELECT f.word_id, f.lemma, f.gloss, w.level, w.tier FROM word_fts f JOIN word w ON w.id = f.word_id WHERE f.locale = ? AND f.word_id = ? LIMIT 1", arrayOf(locale, id)).firstOrNull()

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
            return decomposed.filter { Character.getType(it) != Character.NON_SPACING_MARK.toInt() }.replace('đ', 'd')
        }
    }
}
