package com.yourbrand.englishlearn

import android.app.Application
import android.content.Context
import android.os.StrictMode
import androidx.appcompat.app.AppCompatDelegate
import com.yourbrand.englishlearn.ads.AdsManager
import com.yourbrand.englishlearn.content.Content
import com.yourbrand.englishlearn.content.ContentRepository
import com.yourbrand.englishlearn.content.ContentDb
import com.yourbrand.englishlearn.core.Settings
import com.yourbrand.englishlearn.core.AppLocale
import com.yourbrand.englishlearn.core.Sfx
import com.yourbrand.englishlearn.core.Tts
import com.yourbrand.englishlearn.learning.Exercise
import com.yourbrand.englishlearn.learning.LearningStore
import com.yourbrand.englishlearn.learning.Scheduler
import com.yourbrand.englishlearn.learning.SessionBuilder
import com.yourbrand.englishlearn.learning.WeaknessAnalyzer
import com.yourbrand.englishlearn.learning.WeekKey
import com.yourbrand.englishlearn.pet.PetItems
import com.yourbrand.englishlearn.pet.BubbleScheduler
import com.yourbrand.englishlearn.pet.PetConfig
import com.yourbrand.englishlearn.pet.PetEngine
import com.yourbrand.englishlearn.pet.PetState
import org.json.JSONObject
import java.util.Calendar
import kotlin.concurrent.thread

/** Manual service locator (no DI framework). */
class Services(private val app: Application) {
    val settings = Settings(app)
    val contentRepo = ContentRepository(app)
    val contentDb = ContentDb(app)
    val store = LearningStore(app)
    val ads by lazy { AdsManager(app) }
    val sfx = Sfx(app) { settings.sounds }
    val tts by lazy { Tts(app) { settings.slowTts } }
    val bubbles = BubbleScheduler(LearningStore::dayKey)

    val content: Content get() = contentRepo.get(settings.contentLocale)

    private val petPrefs = app.getSharedPreferences("pet_v1", Context.MODE_PRIVATE).also { com.yourbrand.englishlearn.core.DataRevision.watch(it) }
    val pet: PetEngine by lazy {
        val state = petPrefs.getString("state", null)?.let { runCatching { PetState.fromJson(JSONObject(it)) }.getOrNull() } ?: PetState()
        PetEngine(state, petConfig(), LearningStore::dayKey, ::hourOf)
    }

    private fun petConfig(): PetConfig = runCatching {
        val o = JSONObject(app.assets.open("content/app_config.json").bufferedReader().use { it.readText() }).getJSONObject("pet")
        val costs = o.getJSONArray("stageCost").let { a -> List(a.length()) { a.getInt(it) } }
        PetConfig(costs, o.optInt("dailyXpCap", 200), o.optInt("graceHours", 24), o.optInt("dailyDecayPercentOfStage", 15), o.optInt("mealHungerDrop", 40))
    }.getOrElse { PetConfig() }

    fun savePet() { petPrefs.edit().putString("state", pet.state.toJson().toString()).apply() }

    fun builder() = SessionBuilder(content, store::item, store::allItems, ::weakTags, { settings.sessionsDone })

    fun weakTags(): List<String> = WeaknessAnalyzer.rank(store.tagStats()).map { it.tag }.filter { !it.startsWith("topic_") && !it.startsWith("pos_") }

    // ---- progress helpers ------------------------------------------------------------------

    fun today(now: Long = System.currentTimeMillis()) = LearningStore.dayKey(now)
    fun todayXp(): Int = store.day(today()).xp
    fun goalReached(): Boolean = todayXp() >= settings.dailyGoalXp

    fun ensureWeekly(now: Long = System.currentTimeMillis()) {
        settings.ensureWeekly(WeekKey.mondayOf(today(now)))
    }

    fun recordSpeakingAttempt(now: Long = System.currentTimeMillis()) {
        ensureWeekly(now)
        val d = today(now)
        settings.weeklySpeakingDays = settings.weeklySpeakingDays + d
    }

    fun recordStoryFinished(now: Long = System.currentTimeMillis()) {
        ensureWeekly(now)
        settings.weeklyStories++
    }

    /** Pays each completed weekly quest once. Returns newly awarded item ids. */
    fun claimWeeklyRewards(now: Long = System.currentTimeMillis()): List<String> {
        ensureWeekly(now)
        val rewards = ArrayList<String>()
        val counts = listOf(
            settings.weeklyNewWords to 20,
            settings.weeklyDailyChallenges to 3,
            settings.weeklySpeakingDays.size to 2,
            settings.weeklyStories to 2,
        )
        counts.forEachIndexed { bit, pair ->
            if (pair.first >= pair.second && settings.weeklyPaidMask and (1 shl bit) == 0) {
                settings.weeklyPaidMask = settings.weeklyPaidMask or (1 shl bit)
                val item = PetItems.all.filter { it.id !in pet.state.owned }.minByOrNull { it.price }
                if (item != null) { pet.state.owned += item.id; rewards += item.id }
                else pet.state.coins += 10
            }
        }
        if (rewards.isNotEmpty()) savePet()
        return rewards
    }

    /** Consecutive days (ending today or yesterday) with at least one completed session. */
    fun streak(): Int {
        val days = store.allDays().filterValues { it.answered >= 5 }.keys
        var d = today()
        if (d !in days) d -= 1
        var n = 0
        while (d in days) { n++; d-- }
        return n
    }

    fun weekDots(): List<Boolean> {
        val days = store.allDays()
        val t = today()
        return (6 downTo 0).map { (days[t - it]?.answered ?: 0) >= 5 }
    }

    /**
     * Records one answer and returns the XP to credit (SKILL.md 11 multipliers: ×1.5 due, ×1.5 weak tag,
     * ×0.5 when the item was already rewarded today, 0 after 2 rewarded repeats per day, 0 for implausibly
     * fast random tapping, never negative).
     */
    fun recordAnswer(ex: Exercise, keys: List<String>, correct: Boolean, elapsedMs: Long, fastStreak: Int, now: Long = System.currentTimeMillis()): Int {
        val weak = weakTags().take(3).toSet()
        var xp = 0
        keys.forEach { key ->
            val s = store.itemOrNew(key)
            if (s.isNew && key.startsWith("w:")) {
                store.markFirstSeen(key, today(now))
                ensureWeekly(now)
                settings.weeklyNewWords++
            }
            val wasDue = Scheduler.isDue(s, now)
            Scheduler.apply(s, correct, now)
            if (correct) {
                val day = today(now)
                if (s.rewardDay != day) { s.rewardDay = day; s.rewardCount = 0 }
                if (s.rewardCount < 2 && !(elapsedMs < 800 && fastStreak >= 3)) {
                    var mult = 1.0
                    if (wasDue) mult *= 1.5
                    if (ex.tags.any { it in weak }) mult *= 1.5
                    if (s.rewardCount == 1) mult *= 0.5
                    xp += (ex.kind.baseXp * mult / keys.size.coerceAtLeast(1)).toInt().coerceAtLeast(1)
                    s.rewardCount++
                }
            }
            store.saveItem(s)
        }
        store.recordTags(ex.tags.filter { !it.startsWith("topic_") }, correct)
        claimWeeklyRewards(now)
        store.addToDay(today(now), xp = 0, answered = 1, correct = if (correct) 1 else 0)
        bubbles.lastAnswerAt = now
        return xp
    }

    /** Credits XP to the day and the pet. Returns the pet result (stage-ups, coins). */
    fun creditXp(xp: Int, now: Long = System.currentTimeMillis()): PetEngine.XpResult {
        val r = pet.addXp(xp, now)
        store.addToDay(today(now), xp = r.gained)
        savePet()
        return r
    }

    fun addStudyTime(seconds: Int) = store.addToDay(today(), seconds = seconds)

    companion object {
        fun hourOf(t: Long): Float {
            val c = Calendar.getInstance().apply { timeInMillis = t }
            return c.get(Calendar.HOUR_OF_DAY) + c.get(Calendar.MINUTE) / 60f
        }
    }
}

class EnglishApp : Application() {
    lateinit var services: Services
        private set

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            // Startup content/database prewarming is explicitly on `prewarm`; logging both disk
            // and network violations keeps accidental main-thread work visible in debug builds.
            StrictMode.setThreadPolicy(
                StrictMode.ThreadPolicy.Builder()
                    .detectDiskReads()
                    .detectDiskWrites()
                    .detectNetwork()
                    .penaltyLog()
                    .build()
            )
        }
        services = Services(this)
        val selectable = services.contentRepo.selectableLocales()
        val appLocaleTag = AppCompatDelegate.getApplicationLocales().let { locales ->
            if (locales.isEmpty) "" else locales[0]?.toLanguageTag().orEmpty()
        }
        if (appLocaleTag.isNotBlank()) {
            val systemLocale = AppLocale.resolve(services.settings.contentLocale, appLocaleTag, selectable)
            if (systemLocale != services.settings.contentLocale) services.settings.contentLocale = systemLocale
            services.settings.contentLocaleChosen = true
        } else if (!services.settings.contentLocaleChosen) {
            val deviceTag = resources.configuration.locales[0]?.toLanguageTag().orEmpty()
            services.settings.contentLocale = AppLocale.initialLocale(deviceTag, selectable)
        }
        AppLocale.apply(services.settings.contentLocale)
        AppCompatDelegate.setDefaultNightMode(services.settings.nightMode)
        // Parse content + load learning state while the first frame is drawn.
        thread(name = "prewarm", priority = Thread.NORM_PRIORITY - 1) {
            services.content
            services.store.load()
        }
    }
}

val Context.services: Services get() = (applicationContext as EnglishApp).services
