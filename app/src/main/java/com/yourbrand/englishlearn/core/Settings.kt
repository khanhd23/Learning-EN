package com.yourbrand.englishlearn.core

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import org.json.JSONArray
import org.json.JSONObject

/** Small key-value settings (SharedPreferences; included in Auto Backup). */
class Settings(context: Context) {
    companion object {
        /** Daily study time choices (onboarding and settings). */
        val DAILY_MINUTES = listOf(15, 30, 60)
    }

    private val p = context.getSharedPreferences("settings_v1", Context.MODE_PRIVATE).also { DataRevision.watch(it) }

    private fun bool(k: String, d: Boolean) = p.getBoolean(k, d)
    private fun int(k: String, d: Int) = p.getInt(k, d)
    private fun put(k: String, v: Boolean) = p.edit().putBoolean(k, v).apply()
    private fun put(k: String, v: Int) = p.edit().putInt(k, v).apply()
    private fun put(k: String, v: Long) = p.edit().putLong(k, v).apply()
    private fun put(k: String, v: String?) = p.edit().putString(k, v).apply()

    var onboarded: Boolean get() = bool("onboarded", false); set(v) = put("onboarded", v)
    /** Goal ids: toeic, ielts, talk, work, travel, basics. */
    var goals: Set<String> get() = p.getStringSet("goals", emptySet()) ?: emptySet(); set(v) = p.edit().putStringSet("goals", v).apply()
    /** 0 beginner · 1 basic · 2 intermediate · 3 upper. */
    var level: Int get() = int("level", 1); set(v) = put("level", v)
    /** Daily study time, one of [DAILY_MINUTES]; older 5/10-minute choices read as 15. */
    var dailyMinutes: Int get() = int("dailyMinutes", 15).let { m -> DAILY_MINUTES.firstOrNull { it >= m } ?: DAILY_MINUTES.last() }; set(v) = put("dailyMinutes", v)
    var newWordsPerSession: Int get() = int("newWords", 5); set(v) = put("newWords", v)
    var showTranslation: Boolean get() = bool("showVi", true); set(v) = put("showVi", v)
    var storyShowTranslation: Boolean get() = bool("storyShowTranslation", false); set(v) = put("storyShowTranslation", v)
    /** Learner-facing content locale. Unsupported or incomplete packs safely fall back to vi. */
    var contentLocale: String get() = p.getString("contentLocale", "vi") ?: "vi"; set(v) = put("contentLocale", v)
    var contentLocaleChosen: Boolean get() = bool("contentLocaleChosen", false); set(v) = put("contentLocaleChosen", v)
    var slowTts: Boolean get() = bool("slowTts", false); set(v) = put("slowTts", v)
    var haptics: Boolean get() = bool("haptics", true); set(v) = put("haptics", v)
    var sounds: Boolean get() = bool("sounds", true); set(v) = put("sounds", v)
    var petVisible: Boolean get() = bool("petVisible", true); set(v) = put("petVisible", v)
    var petBubbles: Boolean get() = bool("petBubbles", true); set(v) = put("petBubbles", v)
    /** 0 S 48dp · 1 M 56dp · 2 L 72dp. */
    var petSize: Int get() = int("petSize", 1); set(v) = put("petSize", v)
    /** 0 system · 1 light · 2 dark. */
    var theme: Int get() = int("theme", 0); set(v) = put("theme", v)
    /** 0 = 1.0 · 1 = 1.25 · 2 = 1.5 (question text). */
    var textSize: Int get() = int("textSize", 0); set(v) = put("textSize", v)
    var reduceMotion: Boolean get() = bool("reduceMotion", false); set(v) = put("reduceMotion", v)
    var reminderOn: Boolean get() = bool("reminder", false); set(v) = put("reminder", v)
    var reminderHour: Int get() = int("reminderHour", 20); set(v) = put("reminderHour", v)
    var notifAsked: Boolean get() = bool("notifAsked", false); set(v) = put("notifAsked", v)
    var examGoal: String? get() = p.getString("examGoal", null); set(v) = put("examGoal", v)
    var examDate: Long get() = p.getLong("examDate", 0L); set(v) = put("examDate", v)
    var sessionsDone: Int get() = int("sessionsDone", 0); set(v) = put("sessionsDone", v)
    var goodSessions: Int get() = int("goodSessions", 0); set(v) = put("goodSessions", v)
    var rateAskedAt: Long get() = p.getLong("rateAskedAt", 0L); set(v) = put("rateAskedAt", v)
    var lastOpenAt: Long get() = p.getLong("lastOpenAt", 0L); set(v) = put("lastOpenAt", v)
    var placementOffered: Boolean get() = bool("placementOffered", false); set(v) = put("placementOffered", v)
    var dailyChallengeDay: Long get() = p.getLong("dailyChallengeDay", -1L); set(v) = put("dailyChallengeDay", v)
    var dailyChallengesCompleted: Int get() = int("dailyChallengesCompleted", 0); set(v) = put("dailyChallengesCompleted", v)
    var weeklyQuestRewardWeek: Long get() = p.getLong("weeklyQuestRewardWeek", -1L); set(v) = put(v = v, k = "weeklyQuestRewardWeek")
    var weeklyQuestWeek: Long get() = p.getLong("weeklyQuestWeek", -1L); set(v) = put(v = v, k = "weeklyQuestWeek")
    var weeklyNewWords: Int get() = int("weeklyNewWords", 0); set(v) = put("weeklyNewWords", v)
    var weeklyDailyChallenges: Int get() = int("weeklyDailyChallenges", 0); set(v) = put("weeklyDailyChallenges", v)
    var weeklySpeakingDays: Set<Long>
        get() = (p.getStringSet("weeklySpeakingDays", emptySet()) ?: emptySet()).mapNotNull { it.toLongOrNull() }.toSet()
        set(v) = p.edit().putStringSet("weeklySpeakingDays", v.map(Long::toString).toSet()).apply()
    var weeklyStories: Int get() = int("weeklyStories", 0); set(v) = put("weeklyStories", v)
    var weeklyPaidMask: Int get() = int("weeklyPaidMask", 0); set(v) = put("weeklyPaidMask", v)

    /** Starts a fresh Monday-based quest ledger without changing lifetime learning data. */
    fun ensureWeekly(week: Long) {
        if (weeklyQuestWeek == week) return
        weeklyQuestWeek = week
        weeklyNewWords = 0
        weeklyDailyChallenges = 0
        weeklySpeakingDays = emptySet()
        weeklyStories = 0
        weeklyPaidMask = 0
    }

    fun petPos(landscape: Boolean): Pair<Float, Float>? {
        val k = if (landscape) "petPosL" else "petPosP"
        val s = p.getString(k, null) ?: return null
        val parts = s.split(',')
        return parts[0].toFloat() to parts[1].toFloat()
    }

    fun setPetPos(landscape: Boolean, pos: Pair<Float, Float>?) =
        put(if (landscape) "petPosL" else "petPosP", pos?.let { "${it.first},${it.second}" })

    /** Mock test in progress (process-death safe). */
    var mockInProgress: JSONObject?
        get() = p.getString("mock", null)?.let { runCatching { JSONObject(it) }.getOrNull() }
        set(v) = put("mock", v?.toString())

    /** Keys used in the last 2 mock tests (avoid repeats). */
    var recentMockKeys: List<String>
        get() = p.getString("recentMock", null)?.let { s -> JSONArray(s).let { a -> List(a.length()) { a.getString(it) } } } ?: emptyList()
        set(v) = put("recentMock", JSONArray(v).toString())

    var builderPresets: JSONArray
        get() = p.getString("presets", null)?.let { runCatching { JSONArray(it) }.getOrNull() } ?: JSONArray()
        set(v) = put("presets", v.toString())

    var recentSearches: List<String>
        get() = p.getString("recentSearch", null)?.let { s -> JSONArray(s).let { a -> List(a.length()) { a.getString(it) } } } ?: emptyList()
        set(v) = put("recentSearch", JSONArray(v.take(8)).toString())

    var bubblesHiddenDay: Long get() = p.getLong("bubblesHiddenDay", -1); set(v) = put("bubblesHiddenDay", v)

    /** Daily XP goal; stays within the pet's daily XP cap (200) so 60 minutes is reachable. */
    val dailyGoalXp: Int get() = when (dailyMinutes) { 15 -> 60; 30 -> 120; else -> 200 }
    val userLevel: Int get() = (level + 1).coerceIn(1, 5)
    val questionScale: Float get() = when (textSize) { 1 -> 1.25f; 2 -> 1.5f; else -> 1f }

    val nightMode: Int
        get() = when (theme) {
            1 -> AppCompatDelegate.MODE_NIGHT_NO
            2 -> AppCompatDelegate.MODE_NIGHT_YES
            else -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
        }

    /** Topics that match the learner's goals (drives "new words" selection). */
    fun preferredTopics(): List<String> = goals.flatMap {
        when (it) {
            "toeic", "work" -> listOf("office", "hr", "finance", "accounting", "marketing", "sales", "legal", "logistics", "commerce", "events", "tech")
            "ielts" -> listOf("academic", "environment", "media", "education", "health", "tech")
            "talk" -> listOf("expressions", "daily", "feelings", "family", "food", "phrasal", "idioms", "social", "entertainment")
            "travel" -> listOf("travel", "hospitality", "city", "food", "weather", "expressions")
            "basics" -> listOf("daily", "family", "food", "body", "animals", "clothes", "weather", "city")
            else -> emptyList()
        }
    }.distinct()

    fun clearAll() = p.edit().clear().apply()
}
