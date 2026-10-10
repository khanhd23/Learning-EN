package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.ExamFormat
import org.junit.Assert.assertEquals
import org.junit.Test

class ExamCatalogTest {
    @Test
    fun groupsRelatedExamFormatsInPreferredOrder() {
        val formats = listOf(
            format("ielts_1", "IELTS Academic practice"),
            format("ket_1", "Cambridge A2 Key"),
            format("toeic_1", "TOEIC-style practice"),
            format("toeic_2", "TOEIC"),
            format("pet_1", "Cambridge B1 Preliminary"),
            format("vstep_1", "VSTEP"),
        )

        val grouped = ExamCatalog.families(formats)

        assertEquals(listOf("TOEIC", "IELTS", "VSTEP", "KET", "PET"), grouped.map { it.first })
        assertEquals(listOf("toeic_1", "toeic_2"), grouped.first { it.first == "TOEIC" }.second.map { it.id })
        assertEquals(listOf("ielts_1"), grouped.first { it.first == "IELTS" }.second.map { it.id })
    }

    @Test
    fun unknownFamiliesStaySeparateAndKeepInputOrder() {
        val formats = listOf(
            format("school_1", "School practice"),
            format("school_2", "School mock"),
        )

        val grouped = ExamCatalog.families(formats)

        assertEquals(listOf("School practice", "School mock"), grouped.map { it.first })
        assertEquals(listOf("school_1", "school_2"), grouped.flatMap { it.second }.map { it.id })
    }

    private fun format(id: String, family: String) = ExamFormat(
        id = id,
        label = id,
        family = family,
        description = "",
        questionCount = null,
        passages = null,
        timeLimitMinutes = null,
        source = null,
    )
}
