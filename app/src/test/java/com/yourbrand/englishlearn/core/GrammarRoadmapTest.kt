package com.yourbrand.englishlearn.core

import org.junit.Assert.assertEquals
import org.junit.Test

class GrammarRoadmapTest {
    @Test
    fun firstUnfinishedNodeIsTheDefaultPosition() {
        assertEquals("g2", GrammarRoadmap.currentId(listOf("g1", "g2", "g3"), mapOf("g1" to 3), null))
    }

    @Test
    fun unfinishedSavedNodeIsRestoredUntilItIsMastered() {
        val ids = listOf("g1", "g2", "g3")
        assertEquals("g2", GrammarRoadmap.currentId(ids, mapOf("g1" to 3, "g2" to 0), "g2"))
        assertEquals("g3", GrammarRoadmap.currentId(ids, mapOf("g1" to 3, "g2" to 3), "g2"))
    }

    @Test
    fun completedRoadmapKeepsTheLastNodeAsLandingPoint() {
        val ids = listOf("g1", "g2")
        assertEquals("g2", GrammarRoadmap.currentId(ids, mapOf("g1" to 3, "g2" to 3), null))
    }
}
