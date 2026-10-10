package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.ExamFormat

/** Presentation grouping for the exam hub. Raw formats remain unchanged. */
object ExamCatalog {
    private val preferredOrder = listOf("TOEIC", "IELTS", "VSTEP", "THPT", "KET", "PET")

    fun groupKey(family: String): String = when {
        family.startsWith("TOEIC", ignoreCase = true) -> "TOEIC"
        family.startsWith("IELTS", ignoreCase = true) -> "IELTS"
        family.startsWith("Cambridge A2 Key", ignoreCase = true) -> "KET"
        family.startsWith("Cambridge B1 Preliminary", ignoreCase = true) -> "PET"
        else -> family
    }

    fun families(formats: List<ExamFormat>): List<Pair<String, List<ExamFormat>>> =
        formats.groupBy { groupKey(it.family) }
            .toList()
            .sortedWith(compareBy { preferredOrder.indexOf(it.first).let { index -> if (index < 0) Int.MAX_VALUE else index } })
}
