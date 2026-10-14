package com.yourbrand.englishlearn.learning

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExamGuideFormatterTest {
    @Test
    fun splitsVietnameseMethodAndTip() {
        val blocks = ExamGuideFormatter.blocks("Cách làm: đọc câu hỏi trước. Mẹo: tìm từ khóa trong bài.")
        assertEquals(listOf("Cách làm", "Mẹo"), blocks.map { it.heading })
        assertEquals("đọc câu hỏi trước.", blocks[0].body)
        assertEquals("tìm từ khóa trong bài.", blocks[1].body)
    }

    @Test
    fun splitsEnglishMethodAndTip() {
        val blocks = ExamGuideFormatter.blocks("How: read the question first. Tip: find evidence in the text.")
        assertEquals(listOf("How", "Tip"), blocks.map { it.heading })
    }

    @Test
    fun readingBankUsesAllReadingPartGuides() {
        assertEquals(listOf("p5_method", "p6_method", "p7_method"), ExamGuideFormatter.readingBankTipKeys)
        assertTrue(ExamGuideFormatter.blocks("Cách làm: đọc câu. Mẹo: tìm bằng chứng.").size == 2)
    }
}
