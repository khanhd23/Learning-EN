package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.ExamFormat

enum class ExamPartMode { LISTENING, READING }

data class ExamPartSet(
    val format: ExamFormat,
    /** Non-null when a full reading paper is exposed as a part-only practice set. */
    val sectionId: String? = null,
)

data class ExamPart(
    val key: String,
    val mode: ExamPartMode,
    val sets: List<ExamPartSet>,
) {
    val formats: List<ExamFormat> get() = sets.map { it.format }
}

/** Groups TOEIC formats by the part they actually practise. */
object ExamPartCatalog {
    fun toeic(formats: List<ExamFormat>): List<ExamPart> = listOfNotNull(
        part(formats, "p1", ExamPartMode.LISTENING) { it.id.startsWith("toeic_p1_practice_") },
        part(formats, "p2", ExamPartMode.LISTENING) { it.id.startsWith("toeic_p2_practice_") },
        part(formats, "p3", ExamPartMode.LISTENING) { it.id == "toeic_p3_examples" },
        part(formats, "p4", ExamPartMode.LISTENING) { it.id == "toeic_p4_examples" },
        part(formats, "p5", ExamPartMode.READING) {
            it.id.startsWith("toeic_reading_")
        }.withSectionForFullReading("p5"),
        part(formats, "p6", ExamPartMode.READING) {
            it.id.startsWith("toeic_reading_")
        }.withSectionForFullReading("p6"),
        part(formats, "p7", ExamPartMode.READING) { it.id.startsWith("toeic_p7_practice_") },
        part(formats, "reading_bank", ExamPartMode.READING) { it.id == "toeic" },
    )

    private fun part(formats: List<ExamFormat>, key: String, mode: ExamPartMode, match: (ExamFormat) -> Boolean): ExamPart? =
        formats.filter(match).takeIf { it.isNotEmpty() }?.let { matches ->
            ExamPart(key, mode, matches.map { ExamPartSet(it) })
        }

    private fun ExamPart?.withSectionForFullReading(sectionId: String): ExamPart? = this?.let { part ->
        part.copy(sets = part.sets.map { set ->
            if (set.format.id.startsWith("toeic_reading_")) set.copy(sectionId = sectionId) else set
        })
    }
}
