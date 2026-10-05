package com.yourbrand.englishlearn.learning

import kotlin.random.Random

/** Learning state of one item ("w:<word>", "q:<question>", "p:<passage>:<blank>"). */
data class ItemState(
    val key: String,
    var box: Int = 0,
    var due: Long = 0L,
    var seen: Int = 0,
    var correct: Int = 0,
    var wrong: Int = 0,
    var streak: Int = 0,
    var last: Long = 0L,
    var saved: Boolean = false,
    /** In the mistake book until answered correctly twice in a row. */
    var mistake: Boolean = false,
    var lastWrongAt: Long = 0L,
    /** Day key + count of XP-earning correct answers that day (anti-farming). */
    var rewardDay: Long = -1,
    var rewardCount: Int = 0,
) {
    val isNew: Boolean get() = seen == 0
    val mastered: Boolean get() = box >= Scheduler.MASTER_BOX && streak >= 2
    val wrongRate: Float get() = if (seen == 0) 0f else wrong / seen.toFloat()
}

/**
 * Box schedule (SKILL.md 9.2): box1 = 10 min · box2 = 1 d · box3 = 3 d · box4 = 7 d · box5 = 16 d · box6 = 35 d.
 * Correct → +1; wrong → max(1, box − 2); ±10% jitter on the interval.
 */
object Scheduler {
    const val MAX_BOX = 6
    const val MASTER_BOX = 5
    private const val MIN = 60_000L
    private const val DAY = 86_400_000L
    private val intervals = longArrayOf(0, 10 * MIN, DAY, 3 * DAY, 7 * DAY, 16 * DAY, 35 * DAY)

    fun interval(box: Int): Long = intervals[box.coerceIn(1, MAX_BOX)]

    fun apply(s: ItemState, correct: Boolean, now: Long, random: Random = Random.Default) {
        s.seen++
        s.last = now
        if (correct) {
            s.correct++
            s.streak++
            s.box = (s.box + 1).coerceAtMost(MAX_BOX)
            if (s.mistake && s.streak >= 2) s.mistake = false
        } else {
            s.wrong++
            s.streak = 0
            s.box = (s.box - 2).coerceAtLeast(1)
            s.mistake = true
            s.lastWrongAt = now
        }
        val base = interval(s.box)
        val jitter = 1.0 + (random.nextDouble() * 0.2 - 0.1)
        s.due = now + (base * jitter).toLong()
    }

    /** Flashcard self-rating: 0 Lại · 1 Khó · 2 Được · 3 Dễ. */
    fun rate(s: ItemState, rating: Int, now: Long, random: Random = Random.Default) {
        when (rating) {
            0 -> apply(s, false, now, random)
            1 -> { apply(s, true, now, random); s.box = (s.box - 1).coerceAtLeast(1); s.due = now + interval(s.box) }
            2 -> apply(s, true, now, random)
            else -> { apply(s, true, now, random); s.box = (s.box + 1).coerceAtMost(MAX_BOX); s.due = now + interval(s.box) }
        }
    }

    fun isDue(s: ItemState, now: Long) = s.seen > 0 && s.due <= now
}

/** Ranks error tags by smoothed wrong-rate (wrong + 1) / (attempts + 3), min 5 attempts (SKILL.md 9.4). */
object WeaknessAnalyzer {
    data class TagStat(val tag: String, val attempts: Int, val wrong: Int) {
        val score: Float get() = (wrong + 1f) / (attempts + 3f)
        val accuracy: Int get() = if (attempts == 0) 0 else ((attempts - wrong) * 100 / attempts)
    }

    fun rank(stats: Collection<TagStat>, minAttempts: Int = 5): List<TagStat> =
        stats.filter { it.attempts >= minAttempts && it.wrong > 0 }.sortedByDescending { it.score }
}
