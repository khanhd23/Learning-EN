package com.yourbrand.englishlearn.learning

import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PictureDistractorTest {
    @Test fun sameTopicDistractorsStayAheadOfPartOfSpeechFallbacks() {
        val result = orderedPictureDistractors(
            sameTopic = listOf("topic-a", "topic-b"),
            samePartOfSpeech = listOf("pos-a", "pos-b"),
            image = { it },
            random = Random(7),
            limit = 3,
        )
        assertEquals(3, result.size)
        assertEquals(setOf("topic-a", "topic-b"), result.take(2).toSet())
        assertTrue(result[2] == "pos-a" || result[2] == "pos-b")
    }

    @Test fun duplicateImagesDoNotConsumeASecondDistractorSlot() {
        val result = orderedPictureDistractors(
            sameTopic = listOf("topic-image"),
            samePartOfSpeech = listOf("same-image", "fallback"),
            image = { if (it == "same-image" || it == "topic-image") "shared" else it },
            random = Random(11),
            limit = 2,
        )
        assertEquals("topic-image", result.first())
        assertEquals(2, result.size)
        assertTrue("fallback" in result)
    }
}
