package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.ExamFormat
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExamPartCatalogTest {
    @Test
    fun toeicPartsOnlyContainFormatsForThatPart() {
        val formats = listOf(
            format("toeic_p1_practice_1"), format("toeic_p1_practice_2"), format("toeic_p1_practice_3"),
            format("toeic_p1_practice_4"), format("toeic_p1_practice_5"),
            format("toeic_p2_practice_1"), format("toeic_p2_practice_2"), format("toeic_p2_practice_3"),
            format("toeic_p3_examples"), format("toeic_p3_practice_2"), format("toeic_p3_practice_3"),
            format("toeic_p4_examples"), format("toeic_p4_practice_2"), format("toeic_p4_practice_3"),
            format("toeic_p5"), format("toeic_p6"),
            format("toeic_p7_practice_1"), format("toeic_p7_practice_2"), format("toeic_p7_practice_3"),
            format("toeic_reading_1"), format("toeic_reading_2"), format("toeic_reading_3"), format("toeic"),
        )

        val parts = ExamPartCatalog.toeic(formats)

        assertEquals(listOf("p1", "p2", "p3", "p4", "p5", "p6", "p7", "reading_bank"), parts.map { it.key })
        assertEquals(listOf(5, 3, 3, 3, 3, 3, 3, 1), parts.map { it.formats.size })
        assertEquals(ExamPartMode.LISTENING, parts.first().mode)
        assertTrue(parts.first { it.key == "p5" }.formats.none { it.id.startsWith("toeic_p7") })
        assertEquals(listOf("p5", "p5", "p5"), parts.first { it.key == "p5" }.sets.map { it.sectionId })
        assertEquals(listOf("p6", "p6", "p6"), parts.first { it.key == "p6" }.sets.map { it.sectionId })
    }

    private fun format(id: String) = ExamFormat(
        id = id,
        label = id,
        family = "TOEIC",
        description = "",
        questionCount = null,
        passages = null,
        timeLimitMinutes = null,
        source = null,
    )
}
