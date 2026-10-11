package com.yourbrand.englishlearn.pet

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PetEngineTest {
    @Test fun streakShieldsAreCappedAtTwo() {
        val state = PetState(freezeTokens = 2)
        val engine = PetEngine(state, dayOf = { it }, hourOf = { 12f })
        engine.onWeeklyGoal(14)
        assertEquals(2, state.freezeTokens)
    }

    @Test fun weeklyBonusDoesNotConsumeTheDailyXpCap() {
        val state = PetState()
        val engine = PetEngine(state, PetConfig(stageCost = listOf(10_000)), dayOf = { it }, hourOf = { 12f })

        assertEquals(200, engine.addXp(200, 1L).gained)
        assertEquals(800, engine.addBonusXp(800, 1L).gained)
        assertEquals(1_000, state.totalXp)
        assertEquals(200, state.xpToday)
    }

    @Test fun maxStageTakesMoreThan100FullDaysIncludingWeeklyBonuses() {
        val config = PetConfig()
        val transitionXp = config.stageCost.dropLast(1).sum()
        val maximumWeeklyBonusesIn100Days = 15 * config.weeklyQuestXp.sum()

        assertTrue(transitionXp > 100 * config.dailyXpCap + maximumWeeklyBonusesIn100Days)
    }
}
