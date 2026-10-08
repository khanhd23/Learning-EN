package com.yourbrand.englishlearn.core

import org.junit.Assert.assertEquals
import org.junit.Test

class MistakeReportTest {
    @Test
    fun buildsSubjectAndBodyWithItemScreenAndVersion() {
        val report = MistakeReport.build(
            "q:grammar_01",
            "MockResultScreen",
            "1.4.0",
            "LingoMori: mistake in %1" + '$' + "s",
            "Item: %1" + '$' + "s\nScreen: %2" + '$' + "s\nApp version: %3" + '$' + "s",
        )

        assertEquals("LingoMori: mistake in q:grammar_01", report.subject)
        assertEquals("Item: q:grammar_01\nScreen: MockResultScreen\nApp version: 1.4.0", report.body)
    }
}
