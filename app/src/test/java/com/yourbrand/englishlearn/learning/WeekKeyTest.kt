package com.yourbrand.englishlearn.learning

import org.junit.Assert.assertEquals
import org.junit.Test

class WeekKeyTest {
    @Test fun mondayBoundaryStartsANewWeek() {
        val monday = WeekKey.mondayOf(100L)
        assertEquals(monday, WeekKey.mondayOf(monday + 6L))
        assertEquals(monday + 7L, WeekKey.mondayOf(monday + 7L))
    }
}
