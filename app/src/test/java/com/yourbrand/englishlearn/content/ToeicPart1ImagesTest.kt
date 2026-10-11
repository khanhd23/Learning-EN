package com.yourbrand.englishlearn.content

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class ToeicPart1ImagesTest {
    @Test
    fun everyPartOneSceneHasItsOwnImageResource() {
        val ids = (1..5).flatMap { set -> (1..6).map { item -> "p1_original_set${set}_${item.toString().padStart(3, '0')}" } }
        val resources = ids.map { ToeicPart1Images.resourceFor(it) }

        assertEquals(30, resources.count { it != null })
        assertEquals(30, resources.filterNotNull().toSet().size)
        assertNull(ToeicPart1Images.resourceFor("unknown_group"))
    }
}
