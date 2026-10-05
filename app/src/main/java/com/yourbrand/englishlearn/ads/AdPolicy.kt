package com.yourbrand.englishlearn.ads

/** Persisted ad-frequency state. */
data class AdState(
    var sessionsFinished: Int = 0,
    var lastInterstitialAt: Long = 0L,
    var dayKey: Long = -1L,
    var dayCount: Int = 0,
)

/**
 * Interstitial rules (SKILL.md 13): only between sessions (before the result screen), ≥ 180 s apart,
 * ≤ 3 per day, never in the first 2 sessions, never right after a wrong answer, never from Pet Home or
 * a mock test, never on launch/exit. Banners and rewarded ads are not governed here.
 */
class AdPolicy(
    val state: AdState,
    private val minGapMs: Long = 180_000L,
    private val maxPerDay: Int = 3,
    private val skipFirstSessions: Int = 2,
    private val clock: () -> Long = System::currentTimeMillis,
) {
    fun onSessionFinished(lastAnswerWrong: Boolean, fromMock: Boolean, fromPetHome: Boolean): Boolean {
        state.sessionsFinished++
        val now = clock()
        return when {
            fromMock || fromPetHome -> false
            lastAnswerWrong -> false
            state.sessionsFinished <= skipFirstSessions -> false
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
    }

    private companion object { const val DAY = 86_400_000L }
}
