package com.yourbrand.englishlearn.learning

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import org.json.JSONArray
import org.json.JSONObject
import java.util.Calendar
import java.util.concurrent.Executors

/** One finished session / test, shown in "Lịch sử bài làm" and the mock-test comparison. */
data class HistoryEntry(
    val id: Long,
    val kind: String,
    val title: String,
    val format: String?,
    val total: Int,
    val correct: Int,
    val seconds: Int,
    val at: Long,
    val detail: JSONObject,
) {
    val percent: Int get() = if (total == 0) 0 else correct * 100 / total
}

data class DayStat(val day: Long, var xp: Int = 0, var seconds: Int = 0, var answered: Int = 0, var correct: Int = 0)

/**
 * Per-item learning state in plain SQLite (no Room, SKILL.md 3). Everything is mirrored in memory;
 * writes go to a single background thread so the UI never waits on disk.
 */
class LearningStore(context: Context) : SQLiteOpenHelper(context, "learning.db", null, 2) {
    private val io = Executors.newSingleThreadExecutor { r -> Thread(r, "db").apply { priority = Thread.MIN_PRIORITY } }
    private val items = HashMap<String, ItemState>()
    private val tags = HashMap<String, WeaknessAnalyzer.TagStat>()
    private val days = HashMap<Long, DayStat>()
    private val grammarStars = HashMap<String, Int>()
    private val history = ArrayList<HistoryEntry>()
    private val firstSeen = HashMap<String, Long>()
    @Volatile private var loaded = false

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE item(k TEXT PRIMARY KEY, box INT, due INT, seen INT, correct INT, wrong INT, streak INT, last INT, saved INT, mistake INT, lastWrong INT, rDay INT, rCount INT)")
        db.execSQL("CREATE TABLE tag(t TEXT PRIMARY KEY, attempts INT, wrong INT)")
        db.execSQL("CREATE TABLE day(d INT PRIMARY KEY, xp INT, seconds INT, answered INT, correct INT)")
        db.execSQL("CREATE TABLE gp(id TEXT PRIMARY KEY, stars INT)")
        db.execSQL("CREATE TABLE history(id INTEGER PRIMARY KEY AUTOINCREMENT, kind TEXT, title TEXT, fmt TEXT, total INT, correct INT, seconds INT, at INT, detail TEXT)")
        db.execSQL("CREATE TABLE first_seen(k TEXT PRIMARY KEY, day INT)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) db.execSQL("CREATE TABLE IF NOT EXISTS first_seen(k TEXT PRIMARY KEY, day INT)")
    }

    @Synchronized
    fun load() {
        if (loaded) return
        val db = readableDatabase
        db.rawQuery("SELECT * FROM item", null).use { c ->
            while (c.moveToNext()) {
                val s = ItemState(c.getString(0), c.getInt(1), c.getLong(2), c.getInt(3), c.getInt(4), c.getInt(5), c.getInt(6), c.getLong(7),
                    c.getInt(8) == 1, c.getInt(9) == 1, c.getLong(10), c.getLong(11), c.getInt(12))
                items[s.key] = s
            }
        }
        db.rawQuery("SELECT * FROM tag", null).use { c -> while (c.moveToNext()) tags[c.getString(0)] = WeaknessAnalyzer.TagStat(c.getString(0), c.getInt(1), c.getInt(2)) }
        db.rawQuery("SELECT * FROM day", null).use { c -> while (c.moveToNext()) days[c.getLong(0)] = DayStat(c.getLong(0), c.getInt(1), c.getInt(2), c.getInt(3), c.getInt(4)) }
        db.rawQuery("SELECT * FROM gp", null).use { c -> while (c.moveToNext()) grammarStars[c.getString(0)] = c.getInt(1) }
        db.rawQuery("SELECT * FROM history ORDER BY at", null).use { c ->
            while (c.moveToNext()) history += HistoryEntry(c.getLong(0), c.getString(1), c.getString(2), c.getString(3), c.getInt(4), c.getInt(5), c.getInt(6), c.getLong(7),
                runCatching { JSONObject(c.getString(8)) }.getOrElse { JSONObject() })
        }
        db.rawQuery("SELECT k, day FROM first_seen", null).use { c -> while (c.moveToNext()) firstSeen[c.getString(0)] = c.getLong(1) }
        loaded = true
    }

    private fun ensure() { if (!loaded) load() }

    // ---- items ---------------------------------------------------------------------------

    @Synchronized fun item(key: String): ItemState? { ensure(); return items[key] }
    @Synchronized fun itemOrNew(key: String): ItemState { ensure(); return items.getOrPut(key) { ItemState(key) } }
    @Synchronized fun allItems(): List<ItemState> { ensure(); return items.values.toList() }

    @Synchronized fun markFirstSeen(key: String, day: Long): Boolean {
        ensure()
        if (key !in firstSeen) {
            firstSeen[key] = day
            io.execute { writableDatabase.execSQL("INSERT OR IGNORE INTO first_seen(k, day) VALUES(?,?)", arrayOf(key, day)) }
            return true
        }
        return false
    }

    @Synchronized fun firstSeenCount(fromDay: Long): Int { ensure(); return firstSeen.values.count { it >= fromDay && it < fromDay + 7L } }

    fun saveItem(s: ItemState) {
        com.yourbrand.englishlearn.core.DataRevision.bump()
        val cv = ContentValues().apply {
            put("k", s.key); put("box", s.box); put("due", s.due); put("seen", s.seen); put("correct", s.correct); put("wrong", s.wrong)
            put("streak", s.streak); put("last", s.last); put("saved", if (s.saved) 1 else 0); put("mistake", if (s.mistake) 1 else 0)
            put("lastWrong", s.lastWrongAt); put("rDay", s.rewardDay); put("rCount", s.rewardCount)
        }
        io.execute { writableDatabase.insertWithOnConflict("item", null, cv, SQLiteDatabase.CONFLICT_REPLACE) }
    }

    @Synchronized fun toggleSaved(key: String): Boolean {
        val s = itemOrNew(key)
        s.saved = !s.saved
        saveItem(s)
        return s.saved
    }

    @Synchronized fun removeFromMistakes(key: String) {
        val s = item(key) ?: return
        s.mistake = false
        saveItem(s)
    }

    // ---- tags ----------------------------------------------------------------------------

    @Synchronized fun recordTags(tagList: Collection<String>, correct: Boolean) {
        com.yourbrand.englishlearn.core.DataRevision.bump()
        ensure()
        tagList.forEach { t ->
            val old = tags[t] ?: WeaknessAnalyzer.TagStat(t, 0, 0)
            val n = WeaknessAnalyzer.TagStat(t, old.attempts + 1, old.wrong + if (correct) 0 else 1)
            tags[t] = n
            io.execute { writableDatabase.execSQL("INSERT OR REPLACE INTO tag VALUES(?,?,?)", arrayOf(n.tag, n.attempts, n.wrong)) }
        }
    }

    @Synchronized fun tagStats(): List<WeaknessAnalyzer.TagStat> { ensure(); return tags.values.toList() }

    // ---- days ----------------------------------------------------------------------------

    @Synchronized fun day(key: Long): DayStat { ensure(); return days[key] ?: DayStat(key) }
    @Synchronized fun allDays(): Map<Long, DayStat> { ensure(); return HashMap(days) }

    @Synchronized fun addToDay(key: Long, xp: Int = 0, seconds: Int = 0, answered: Int = 0, correct: Int = 0) {
        com.yourbrand.englishlearn.core.DataRevision.bump()
        ensure()
        val d = days.getOrPut(key) { DayStat(key) }
        d.xp += xp; d.seconds += seconds; d.answered += answered; d.correct += correct
        val copy = d.copy()
        io.execute { writableDatabase.execSQL("INSERT OR REPLACE INTO day VALUES(?,?,?,?,?)", arrayOf(copy.day, copy.xp, copy.seconds, copy.answered, copy.correct)) }
    }

    // ---- grammar -------------------------------------------------------------------------

    @Synchronized fun stars(gp: String): Int { ensure(); return grammarStars[gp] ?: 0 }

    @Synchronized fun setStars(gp: String, stars: Int) {
        com.yourbrand.englishlearn.core.DataRevision.bump()
        ensure()
        if (stars <= (grammarStars[gp] ?: 0)) return
        grammarStars[gp] = stars
        io.execute { writableDatabase.execSQL("INSERT OR REPLACE INTO gp VALUES(?,?)", arrayOf(gp, stars)) }
    }

    // ---- history -------------------------------------------------------------------------

    @Synchronized fun history(): List<HistoryEntry> { ensure(); return history.toList() }

    @Synchronized fun addHistory(kind: String, title: String, format: String?, total: Int, correct: Int, seconds: Int, at: Long, detail: JSONObject) {
        com.yourbrand.englishlearn.core.DataRevision.bump()
        ensure()
        history += HistoryEntry(-1, kind, title, format, total, correct, seconds, at, detail)
        io.execute {
            writableDatabase.insert("history", null, ContentValues().apply {
                put("kind", kind); put("title", title); put("fmt", format); put("total", total); put("correct", correct)
                put("seconds", seconds); put("at", at); put("detail", detail.toString())
            })
        }
    }

    /** Wipes everything (Settings → Dữ liệu → Đặt lại). */
    @Synchronized fun reset() {
        com.yourbrand.englishlearn.core.DataRevision.bump()
        items.clear(); tags.clear(); days.clear(); grammarStars.clear(); history.clear(); firstSeen.clear()
        io.execute { listOf("item", "tag", "day", "gp", "history", "first_seen").forEach { writableDatabase.execSQL("DELETE FROM $it") } }
    }

    companion object {
        /** Local calendar day number (days since epoch in the device time zone). */
        fun dayKey(t: Long): Long {
            val c = Calendar.getInstance().apply { timeInMillis = t }
            val offset = c.get(Calendar.ZONE_OFFSET) + c.get(Calendar.DST_OFFSET)
            return Math.floorDiv(t + offset, 86_400_000L)
        }

        fun jsonList(values: List<String>) = JSONArray(values)
    }
}
