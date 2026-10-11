package com.yourbrand.englishlearn.ads

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdPolicyTest {
    @Test
    fun longSessionQualifiesAfterTheInitialTwoSessions() {
        val state = AdState()
        val policy = AdPolicy(state, minGapMs = 0L, clock = { 1L })

        assertFalse(policy.onSessionFinished(false, false, false, questionCount = 5))
        assertFalse(policy.onSessionFinished(false, false, false, questionCount = 5))
        assertTrue(policy.onSessionFinished(false, false, false, questionCount = 20))
    }

    @Test
    fun threeShortSessionsQualifyAndShowingResetsTheShortSessionCounter() {
        val state = AdState()
        val policy = AdPolicy(state, minGapMs = 0L, clock = { 1L })

        assertFalse(policy.onSessionFinished(false, false, false, questionCount = 5))
        assertFalse(policy.onSessionFinished(false, false, false, questionCount = 5))
        assertTrue(policy.onSessionFinished(false, false, false, questionCount = 5))

        policy.onInterstitialShown()
        assertFalse(policy.onSessionFinished(false, false, false, questionCount = 5))
        assertFalse(policy.onSessionFinished(false, false, false, questionCount = 5))
        assertTrue(policy.onSessionFinished(false, false, false, questionCount = 5))
    }

    @Test
    fun wrongAnswersMockTestsAndPetSessionsNeverQualify() {
        val policy = AdPolicy(AdState(), minGapMs = 0L, clock = { 1L })

        repeat(3) { assertFalse(policy.onSessionFinished(true, false, false, questionCount = 20)) }
        repeat(3) { assertFalse(policy.onSessionFinished(false, true, false, questionCount = 20)) }
        repeat(3) { assertFalse(policy.onSessionFinished(false, false, true, questionCount = 20)) }
    }
}
