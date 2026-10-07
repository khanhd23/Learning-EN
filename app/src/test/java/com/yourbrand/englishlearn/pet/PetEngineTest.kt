package com.yourbrand.englishlearn.pet

import org.junit.Assert.assertEquals
import org.junit.Test

class PetEngineTest {
    @Test fun streakShieldsAreCappedAtTwo() {
        val state = PetState(freezeTokens = 2)
        val engine = PetEngine(state, dayOf = { it }, hourOf = { 12f })
        engine.onWeeklyGoal(14)
        assertEquals(2, state.freezeTokens)
    }
}
