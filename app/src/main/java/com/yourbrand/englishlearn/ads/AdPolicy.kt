package com.yourbrand.englishlearn.ads

/** Persisted ad-frequency state. */
data class AdState(
    var sessionsFinished: Int = 0,
    var lastInterstitialAt: Long = 0L,
    var dayKey: Long = -1L,
    var dayCount: Int = 0,
    var shortSessionsSinceAd: Int = 0,
)

/**
 * Interstitial rules (SKILL.md 13): only between sessions (before the result screen), ≥ 180 s apart,
 * ≤ 3 per day, never in the first 2 sessions, never right after a wrong answer, never from Pet Home or
 * a mock test, never on launch/exit. One long session (20+ questions) qualifies, or three short
 * sessions since the last interstitial. Banners and rewarded ads are not governed here.
 */
class AdPolicy(
    val state: AdState,
    private val minGapMs: Long = 180_000L,
    private val maxPerDay: Int = 3,
    private val skipFirstSessions: Int = 2,
    private val longSessionQuestions: Int = 20,
    private val shortSessionsPerAd: Int = 3,
    private val clock: () -> Long = System::currentTimeMillis,
) {
    fun onSessionFinished(lastAnswerWrong: Boolean, fromMock: Boolean, fromPetHome: Boolean, questionCount: Int = 0): Boolean {
        state.sessionsFinished++
        val now = clock()
        if (fromMock || fromPetHome || lastAnswerWrong) return false
        val longSession = questionCount >= longSessionQuestions
        if (!longSession) state.shortSessionsSinceAd++
        val studyThresholdReached = longSession || state.shortSessionsSinceAd >= shortSessionsPerAd
        return when {
            state.sessionsFinished <= skipFirstSessions -> false
            !studyThresholdReached -> false
            state.lastInterstitialAt > 0 && now - state.lastInterstitialAt < minGapMs -> false
            now / DAY == state.dayKey && state.dayCount >= maxPerDay -> false
            else -> true
        }
    }

    fun onInterstitialShown() {
        val now = clock()
        if (now / DAY != state.dayKey) { state.dayKey = now / DAY; state.dayCount = 0 }
        state.dayCount++
        state.lastInterstitialAt = now
        state.shortSessionsSinceAd = 0
    }

    private companion object { const val DAY = 86_400_000L }
}

/** Rewarded-ad frequency is shared by optional pet care, coin earning, and result bonuses. */
data class RewardedAdState(
    var dayKey: Long = -1L,
    var dayCount: Int = 0,
    var lastShownAt: Long = 0L,
)

class RewardedAdPolicy(
    val state: RewardedAdState,
    private val minGapMs: Long = 10 * 60_000L,
    private val maxPerDay: Int = 5,
    private val clock: () -> Long = System::currentTimeMillis,
) {
    fun canShow(): Boolean {
        val now = clock()
        val day = now / DAY
        if (day != state.dayKey) { state.dayKey = day; state.dayCount = 0 }
        return state.dayCount < maxPerDay && (state.lastShownAt == 0L || now - state.lastShownAt >= minGapMs)
    }

    fun onShown() {
        val now = clock()
        val day = now / DAY
        if (day != state.dayKey) { state.dayKey = day; state.dayCount = 0 }
        state.dayCount++
        state.lastShownAt = now
    }

    private companion object { const val DAY = 86_400_000L }
}
