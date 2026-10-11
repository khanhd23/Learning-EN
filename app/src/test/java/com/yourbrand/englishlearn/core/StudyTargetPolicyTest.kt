package com.yourbrand.englishlearn.core

import org.junit.Assert.assertEquals
import org.junit.Test

class StudyTargetPolicyTest {
    @Test
    fun targetsScaleWithTheDailyPlan() {
        assertEquals(5, StudyTargetPolicy.dailyNewWords(15, 5))
        assertEquals(35, StudyTargetPolicy.weeklyNewWords(15, 5))
        assertEquals(8, StudyTargetPolicy.dailyNewWords(30, 5))
        assertEquals(56, StudyTargetPolicy.weeklyNewWords(30, 5))
        assertEquals(12, StudyTargetPolicy.dailyNewWords(60, 5))
        assertEquals(84, StudyTargetPolicy.weeklyNewWords(60, 5))
    }

    @Test
    fun learnerPreferenceCanRaiseButNotLowerThePlanMinimum() {
        assertEquals(5, StudyTargetPolicy.dailyNewWords(15, 3))
        assertEquals(8, StudyTargetPolicy.dailyNewWords(15, 8))
        assertEquals(12, StudyTargetPolicy.dailyNewWords(60, 8))
    }
}
