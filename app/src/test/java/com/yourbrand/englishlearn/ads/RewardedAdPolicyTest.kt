package com.yourbrand.englishlearn.ads

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RewardedAdPolicyTest {
    @Test
    fun rewardedAdsNeedTenMinutesAndStopAtFivePerDay() {
        var now = 1L
        val policy = RewardedAdPolicy(RewardedAdState(), minGapMs = 10L, clock = { now })

        assertTrue(policy.canShow())
        policy.onShown()
        assertFalse(policy.canShow())

        now = 11L
        repeat(4) {
            assertTrue(policy.canShow())
            policy.onShown()
            now += 10L
        }
        assertFalse(policy.canShow())
    }

    @Test
    fun aNewDayResetsTheDailyRewardedLimit() {
        var now = 1L
        val policy = RewardedAdPolicy(RewardedAdState(), minGapMs = 0L, clock = { now })

        repeat(5) { assertTrue(policy.canShow()); policy.onShown() }
        assertFalse(policy.canShow())

        now = 86_400_001L
        assertTrue(policy.canShow())
    }
}
