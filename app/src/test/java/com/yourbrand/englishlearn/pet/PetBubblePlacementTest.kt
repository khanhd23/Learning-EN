package com.yourbrand.englishlearn.pet

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PetBubblePlacementTest {
    private val screen = BubbleBox(0, 0, 1080, 1600)

    @Test fun prefersTheClearSideAndStaysInsideScreen() {
        val pet = BubbleBox(920, 900, 976, 956)
        val weeklyQuest = BubbleBox(20, 980, 1060, 1230)

        val result = PetBubblePlacement.find(screen, pet, 220, 80, listOf(weeklyQuest), 8)

        assertNotNull(result)
        assertTrue(screen.contains(result!!))
        assertFalse(result.intersects(pet))
        assertFalse(result.intersects(weeklyQuest))
        assertEquals(692, result.left)
    }

    @Test fun hidesWhenEveryAllowedPositionWouldCoverContent() {
        val pet = BubbleBox(920, 900, 976, 956)
        val protectedContent = listOf(
            BubbleBox(0, 980, 1080, 1600),
            BubbleBox(0, 0, 1080, 910),
        )

        val result = PetBubblePlacement.find(screen, pet, 220, 80, protectedContent, 8)

        assertTrue(result == null)
    }
}
