package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.ExamFormat
import org.junit.Assert.assertEquals
import org.junit.Test

class ExamCatalogTest {
    @Test
    fun thptIsVisibleOnlyForVietnameseLocale() {
        val formats = listOf(
            format("thpt", "THPT"),
            format("thpt_2", "THPT"),
            format("ielts", "IELTS"),
        )

        assertEquals(listOf("thpt", "thpt_2", "ielts"), ExamCatalog.visibleToLocale(formats, "vi").map { it.id })
        assertEquals(listOf("ielts"), ExamCatalog.visibleToLocale(formats, "en-US").map { it.id })
        assertEquals(listOf("ielts"), ExamCatalog.visibleToLocale(formats, "es").map { it.id })
    }

    private fun format(id: String, family: String) = ExamFormat(
        id = id,
        label = id,
        family = family,
        description = "",
        questionCount = 40,
        passages = null,
        timeLimitMinutes = 50,
        source = null,
    )
}
