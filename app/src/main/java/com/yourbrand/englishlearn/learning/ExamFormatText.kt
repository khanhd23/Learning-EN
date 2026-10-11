package com.yourbrand.englishlearn.learning

import android.content.Context
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.content.ExamFormat
import com.yourbrand.englishlearn.content.ExamSection

/** Localized presentation text for format metadata kept in the exam catalog. */
object ExamFormatText {
    fun label(context: Context, format: ExamFormat): String =
        thptSetNumber(format.id)?.let { format.label }
            ?: labelRes(format.id)?.let(context::getString)
            ?: format.label

    fun description(context: Context, format: ExamFormat): String =
        descriptionRes(format.id)?.let(context::getString) ?: format.description

    fun sectionLabel(context: Context, section: ExamSection): String =
        sectionRes(section.id)?.let(context::getString) ?: section.label

    private fun labelRes(id: String): Int? = when (id) {
        "toeic_p5" -> R.string.exam_format_toeic_p5_title
        "toeic_p6" -> R.string.exam_format_toeic_p6_title
        "school" -> R.string.exam_format_school_title
        "thpt" -> R.string.exam_format_thpt_title
        else -> null
    }

    private fun descriptionRes(id: String): Int? = when {
        id == "toeic_p5" -> R.string.exam_format_toeic_p5_desc
        id == "toeic_p6" -> R.string.exam_format_toeic_p6_desc
        id == "school" -> R.string.exam_format_school_desc
        thptSetNumber(id) != null -> R.string.exam_format_thpt_desc
        id.startsWith("toeic_p7_practice_") -> R.string.exam_format_toeic_p7_desc
        id.startsWith("vstep_practice_") -> R.string.exam_format_vstep_desc
        else -> null
    }

    private fun sectionRes(id: String): Int? = when (id) {
        "notice" -> R.string.exam_section_notice
        "leaflet" -> R.string.exam_section_leaflet
        "arrange" -> R.string.exam_section_arrange
        "complete" -> R.string.exam_section_complete
        "reading1" -> R.string.exam_section_reading1
        "reading2" -> R.string.exam_section_reading2
        else -> null
    }

    private fun thptSetNumber(id: String): Int? = when {
        id == "thpt" -> 1
        id.startsWith("thpt_") -> id.substringAfter("thpt_").toIntOrNull()?.takeIf { it in 2..5 }
        else -> null
    }
}
