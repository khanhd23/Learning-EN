package com.yourbrand.englishlearn.pet

import org.json.JSONArray
import org.json.JSONObject

/** Tunables from config/app_config.json → "pet". */
data class PetConfig(
    val stageCost: List<Int> = listOf(100, 220, 380, 580, 820, 1100, 1450, 1850, 2300, 2800),
    val dailyXpCap: Int = 200,
    val graceHours: Int = 24,
    val dailyDecayPercent: Int = 15,
    val mealHungerDrop: Int = 40,
) {
    val maxStage: Int get() = stageCost.size
}

data class PetState(
    var species: String = "cat",
    var name: String = "Miu",
    var stage: Int = 1,
    var stageXp: Int = 0,
    var totalXp: Int = 0,
    var recoverableXp: Int = 0,
    var lastStudyAt: Long = 0L,
    var lastDecayDay: Long = -1L,
    var lastSeenAt: Long = 0L,
    var mealAt: Long = 0L,
    var hungerAtMeal: Int = 30,
    var xpDay: Long = -1L,
    var xpToday: Int = 0,
    var coins: Int = 0,
    var freezeTokens: Int = 0,
    var tokenWeek: Long = -1L,
    var pendingStageUp: Int = 0,
    val owned: MutableSet<String> = mutableSetOf(),
    val equipped: MutableMap<String, String> = mutableMapOf(),
    var created: Boolean = false,
) {
    fun toJson(): JSONObject = JSONObject()
        .put("species", species).put("name", name).put("stage", stage).put("stageXp", stageXp).put("totalXp", totalXp)
        .put("recoverable", recoverableXp).put("lastStudyAt", lastStudyAt).put("lastDecayDay", lastDecayDay).put("lastSeenAt", lastSeenAt)
        .put("mealAt", mealAt).put("hungerAtMeal", hungerAtMeal).put("xpDay", xpDay).put("xpToday", xpToday).put("coins", coins)
        .put("tokens", freezeTokens).put("tokenWeek", tokenWeek).put("pendingStageUp", pendingStageUp)
        .put("owned", JSONArray(owned.toList())).put("equipped", JSONObject(equipped as Map<*, *>)).put("created", created)

    companion object {
        fun fromJson(o: JSONObject): PetState = PetState(
            species = o.optString("species", "cat"), name = o.optString("name", "Miu"), stage = o.optInt("stage", 1),
            stageXp = o.optInt("stageXp"), totalXp = o.optInt("totalXp"), recoverableXp = o.optInt("recoverable"),
            lastStudyAt = o.optLong("lastStudyAt"), lastDecayDay = o.optLong("lastDecayDay", -1), lastSeenAt = o.optLong("lastSeenAt"),
            mealAt = o.optLong("mealAt"), hungerAtMeal = o.optInt("hungerAtMeal", 30), xpDay = o.optLong("xpDay", -1), xpToday = o.optInt("xpToday"),
            coins = o.optInt("coins"), freezeTokens = o.optInt("tokens"), tokenWeek = o.optLong("tokenWeek", -1), pendingStageUp = o.optInt("pendingStageUp"),
            owned = o.optJSONArray("owned")?.let { a -> MutableList(a.length()) { a.getString(it) }.toMutableSet() } ?: mutableSetOf(),
            equipped = o.optJSONObject("equipped")?.let { e -> e.keys().asSequence().associateWith { e.getString(it) }.toMutableMap() } ?: mutableMapOf(),
            created = o.optBoolean("created"),
        )
    }
}

/**
 * Growth comes only from real learning (SKILL.md 11). The pet never dies and never drops a stage:
 * missed days remove part of the current stage's progress into [PetState.recoverableXp], which a
 * free 5-question review (or an optional rewarded ad, or a freeze token) restores in full.
 *
 * All time math uses an injected clock and the local day function, so it is unit-testable.
 */
class PetEngine(
    val state: PetState,
    val config: PetConfig = PetConfig(),
    private val dayOf: (Long) -> Long,
    private val hourOf: (Long) -> Float,
) {
    data class XpResult(val gained: Int, val coins: Int, val stageUp: Int?)

    val stageCost: Int get() = config.stageCost[(state.stage - 1).coerceIn(0, config.maxStage - 1)]
    val isMaxStage: Boolean get() = state.stage >= config.maxStage
    val progress: Float get() = if (isMaxStage) 1f else state.stageXp / stageCost.toFloat()
    val isDozing: Boolean get() = state.recoverableXp > 0

    /** Lazy decay on open/resume. Returns the XP moved to "recoverable" (0 if none). */
    fun onOpen(now: Long): Int {
        val suspicious = state.lastSeenAt > 0 && now < state.lastSeenAt - 60 * 60_000L
        state.lastSeenAt = maxOf(state.lastSeenAt, now)
        if (suspicious || state.lastStudyAt == 0L) return 0
        if (now < state.lastStudyAt) return 0 // clock rolled back: treat elapsed as 0
        val elapsed = now - state.lastStudyAt
        if (elapsed < config.graceHours * 3_600_000L) return 0
        val today = dayOf(now)
        val lastStudyDay = dayOf(state.lastStudyAt)
        // Each FULL missed day after the grace period (yesterday studied → 0 missed).
        val firstMissed = maxOf(lastStudyDay + 1, state.lastDecayDay + 1)
        val missed = (today - firstMissed).toInt().coerceAtLeast(0)
        if (missed == 0) return 0
        var moved = 0
        repeat(missed) {
            val take = (stageCost * config.dailyDecayPercent / 100).coerceAtMost(state.stageXp)
            state.stageXp -= take
            state.recoverableXp += take
            moved += take
        }
        state.lastDecayDay = today - 1
        return moved
    }

    /**
     * Adds learning XP (already multiplied by the caller), capped per day. Coins: 1 per 2 XP + 30 per stage-up.
     */
    fun addXp(raw: Int, now: Long): XpResult {
        if (raw <= 0) return XpResult(0, 0, null)
        val day = dayOf(now)
        if (state.xpDay != day) { state.xpDay = day; state.xpToday = 0 }
        val gained = raw.coerceAtMost((config.dailyXpCap - state.xpToday).coerceAtLeast(0))
        state.xpToday += raw // count raw so the cap reflects effort, not just credited XP
        state.lastStudyAt = maxOf(state.lastStudyAt, now)
        state.lastDecayDay = maxOf(state.lastDecayDay, day - 1)
        var coins = (gained + 1) / 2
        var stageUp: Int? = null
        if (gained > 0) {
            state.totalXp += gained
            if (!isMaxStage) {
                state.stageXp += gained
                while (!isMaxStage && state.stageXp >= stageCost) {
                    state.stageXp -= stageCost
                    state.stage++
                    stageUp = state.stage
                    coins += 30
                }
                if (isMaxStage) state.stageXp = 0
            }
        }
        state.coins += coins
        if (stageUp != null) state.pendingStageUp = stageUp
        return XpResult(gained, coins, stageUp)
    }

    /** Cosmetic hunger 0..100: +1 per 30 awake minutes (07:00–22:00) since the last meal. */
    fun hunger(now: Long): Int {
        if (state.mealAt == 0L) return state.hungerAtMeal.coerceIn(0, 100)
        val awakeHalfHours = awakeMinutesBetween(state.mealAt, now) / 30
        return (state.hungerAtMeal + awakeHalfHours).coerceIn(0, 100)
    }

    private fun awakeMinutesBetween(from: Long, to: Long): Int {
        if (to <= from) return 0
        // Sample in 30-minute steps (cheap, at most ~2 weeks of steps since we cap at 100 anyway).
        var t = from
        var minutes = 0
        val step = 30 * 60_000L
        while (t < to && minutes < 100 * 30) {
            val h = hourOf(t)
            if (h >= 7f && h < 22f) minutes += 30
            t += step
        }
        return minutes
    }

    /** A completed session or ≥ 5 correct answers. Also wakes a dozing pet when [recover] is true. */
    fun meal(now: Long, goalReached: Boolean) {
        val h = hunger(now)
        state.hungerAtMeal = if (goalReached) minOf(h, 10) else h - minOf(h, config.mealHungerDrop)
        state.mealAt = now
    }

    /** Restores all decayed XP (free quick review, rewarded ad, all equivalent). */
    fun recover(): Int {
        val r = state.recoverableXp
        if (r == 0) return 0
        state.stageXp += r
        state.recoverableXp = 0
        while (!isMaxStage && state.stageXp >= stageCost) { state.stageXp -= stageCost; state.stage++ ; state.pendingStageUp = state.stage }
        return r
    }

    /** A freeze token cancels the latest missed day (restores one day's worth). */
    fun useFreezeToken(): Boolean {
        if (state.freezeTokens <= 0 || state.recoverableXp <= 0) return false
        state.freezeTokens--
        val back = (stageCost * config.dailyDecayPercent / 100).coerceAtMost(state.recoverableXp)
        state.recoverableXp -= back
        state.stageXp += back
        return true
    }

    /** One freeze token per completed weekly goal (week = 7-day block of local days). */
    fun onWeeklyGoal(day: Long) {
        val week = Math.floorDiv(day + 3, 7L)
        if (state.tokenWeek != week) { state.tokenWeek = week; state.freezeTokens = (state.freezeTokens + 1).coerceAtMost(2) }
    }

    fun buy(item: PetItem): Boolean {
        if (item.id in state.owned || item.price == 0) { state.owned += item.id; return true }
        if (state.coins < item.price || state.stage < item.minStage) return false
        state.coins -= item.price
        state.owned += item.id
        return true
    }

    fun equip(item: PetItem) {
        if (item.price > 0 && item.id !in state.owned) return
        if (state.equipped[item.slot] == item.id) state.equipped.remove(item.slot) else state.equipped[item.slot] = item.id
    }

    fun owns(item: PetItem) = item.price == 0 || item.id in state.owned
}

enum class Mood { IDLE, HI, HUNGRY, STARVING, FULL, SLEEPY, SLEEPING, DOZING, CELEBRATE, CHEER, OOPS, THINKING }

/** Mood state machine (SKILL.md 5.3). Pure function of its inputs. */
object PetMoodResolver {
    data class Input(
        val hourOfDay: Float,
        val hunger: Int,
        val fullToday: Boolean,
        val greet: Boolean,
        val dozing: Boolean,
        val celebrate: Boolean,
        val justFinishedSession: Boolean = false,
    )

    fun resolve(i: Input): Mood = when {
        i.celebrate -> Mood.CELEBRATE
        i.dozing -> Mood.DOZING
        i.hourOfDay >= 22f || i.hourOfDay < 7f -> Mood.SLEEPING
        i.greet -> Mood.HI
        i.fullToday || (i.justFinishedSession && i.hunger <= 10) -> Mood.FULL
        i.hunger >= 85 -> Mood.STARVING
        i.hunger >= 60 -> Mood.HUNGRY
        i.hourOfDay >= 21.5f -> Mood.SLEEPY
        else -> Mood.IDLE
    }

    /** Which pet line group a mood speaks from (null = no bubble). */
    fun lineKey(m: Mood): String? = when (m) {
        Mood.HI -> "HI"; Mood.HUNGRY -> "HUNGRY"; Mood.STARVING -> "STARVING"; Mood.FULL -> "FULL"
        Mood.SLEEPY -> "SLEEPY"; Mood.DOZING -> "DOZING"; Mood.CELEBRATE -> "CELEBRATE"; else -> null
    }
}

/**
 * Bubble throttling (SKILL.md 5.2): ≥ 20 min apart, ≤ 6 per day, never within 10 s after an answer,
 * never while blocked (session, dialog, ad), never the same line twice in a row.
 */
class BubbleScheduler(private val dayOf: (Long) -> Long) {
    var lastShownAt = 0L
    var day = -1L
    var countToday = 0
    var lastAnswerAt = 0L
    var hiddenDay = -1L
    private val lastLine = HashMap<String, Int>()

    fun canShow(now: Long, blocked: Boolean, priority: Int): Boolean {
        val d = dayOf(now)
        if (d != day) { day = d; countToday = 0 }
        return when {
            blocked -> false
            hiddenDay == d -> false
            now - lastAnswerAt < 10_000 -> false
            countToday >= 6 -> false
            // High-priority events (stage up, greeting) may skip the 20-minute spacing once.
            priority < 5 && lastShownAt > 0 && now - lastShownAt < 20 * 60_000L -> false
            else -> true
        }
    }

    fun onShown(now: Long) { lastShownAt = now; countToday++ }

    fun pick(key: String, lines: List<String>, random: kotlin.random.Random = kotlin.random.Random.Default): String? {
        if (lines.isEmpty()) return null
        val last = lastLine[key] ?: -1
        var i = random.nextInt(lines.size)
        if (lines.size > 1 && i == last) i = (i + 1) % lines.size
        lastLine[key] = i
        return lines[i]
    }
}
