package com.yourbrand.englishlearn.content

import org.junit.Assert.assertEquals
import org.junit.Test

class ExamBankParserTest {
    @Test
    fun partSevenCanLocalizeExplanationWithoutReplacingEnglishPassage() {
        val base = ExamBankParser.parse(
            """{"items":[],"groups":[{"id":"g1","section":"p7","title":"English title","passage":"English passage","items":[{"id":"q1","section":"p7","stem":"English question","opts":["A","B"],"ans":0,"expl":"English explanation"}]}]}"""
        )
        val localized = """{"g1":{"title":"Tiêu đề tiếng Việt","passage":"Đoạn văn tiếng Việt"},"q1":{"stem":"Câu hỏi tiếng Việt","expl":"Giải thích tiếng Việt"}}"""

        val merged = ExamBankParser.merge(
            base,
            localized,
            setOf("g1", "q1"),
            translatePassages = false,
            translateGroupTitles = false,
            translateItemStems = false,
        )
        val group = merged.groups.single()

        assertEquals("English passage", group.passage)
        assertEquals("English title", group.title)
        assertEquals("English question", group.items.single().stem)
        assertEquals("Giải thích tiếng Việt", group.items.single().explanation)
    }
}
