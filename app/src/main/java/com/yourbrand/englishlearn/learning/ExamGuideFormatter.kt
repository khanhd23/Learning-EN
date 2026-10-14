package com.yourbrand.englishlearn.learning

/** Splits localized method text into readable blocks without changing its content. */
data class ExamGuideBlock(val heading: String, val body: String)

object ExamGuideFormatter {
    private val adviceMarkers = listOf("Mẹo:", "Tip:", "Consejo:", "Dica:")

    val readingBankTipKeys: List<String> = listOf("p5_method", "p6_method", "p7_method")

    fun blocks(raw: String): List<ExamGuideBlock> {
        val text = raw.trim()
        if (text.isEmpty()) return emptyList()
        val marker = adviceMarkers
            .mapNotNull { value -> text.indexOf(value).takeIf { it >= 0 }?.let { it to value } }
            .minByOrNull { it.first }
            ?: return listOf(ExamGuideBlock("", text))
        val method = text.substring(0, marker.first).trim()
        val methodColon = method.indexOf(':')
        val methodHeading = if (methodColon >= 0) method.substring(0, methodColon).trim() else ""
        val methodBody = if (methodColon >= 0) method.substring(methodColon + 1).trim() else method
        val adviceBody = text.substring(marker.first + marker.second.length).trim()
        return buildList {
            if (methodBody.isNotEmpty()) add(ExamGuideBlock(methodHeading, methodBody))
            if (adviceBody.isNotEmpty()) add(ExamGuideBlock(marker.second.removeSuffix(":"), adviceBody))
        }
    }
}
