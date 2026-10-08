package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.ExamBank
import com.yourbrand.englishlearn.content.ExamBankParser
import com.yourbrand.englishlearn.content.ExamFormat
import com.yourbrand.englishlearn.content.ExamGroup
import com.yourbrand.englishlearn.content.ExamItem
import com.yourbrand.englishlearn.content.ExamSection
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ExamEngineTest {
    private val bank = ExamBank(
        listOf(
            ExamItem("s1_i1", "s1", 2, "Choose A", listOf("A", "B", "C", "D"), 0, "A"),
            ExamItem("s1_i2", "s1", 2, "Choose B", listOf("A", "B", "C", "D"), 1, "B"),
        ),
        listOf(ExamGroup("g1", "s2", "Shared passage.", "Passage 1", listOf(
            ExamItem("s2_i1", "s2", 3, "One", listOf("A", "B", "C", "D"), 0, "A"),
            ExamItem("s2_i2", "s2", 3, "Two", listOf("A", "B", "C", "D"), 1, "B"),
       ))),
    )

    @Test fun sectionedExamUsesCountsAndWholeGroupsInOrder() {
        val format = ExamFormat("test", "Test", "TEST", "", null, null, 10, null, listOf(
            ExamSection("s1", "Items", "mcq", 2, listOf("E01"), emptyList(), "test"),
            ExamSection("s2", "Passage", "group", 2, emptyList(), emptyList(), "test"),
        ))
        val picked = SectionedExamBuilder.build(format, bank)
        assertEquals(listOf("s1", "s1", "s2", "s2"), picked.map { it.section })
        assertEquals(setOf("g1"), picked.drop(2).mapNotNull { it.group?.id }.toSet())
        assertEquals("Shared passage.", picked[2].group?.passage)
    }

    @Test fun groupSelectionDoesNotSplitAGroup() {
        val format = ExamFormat("test", "Test", "TEST", "", null, null, null, null, listOf(ExamSection("s2", "Passage", "group", 2, emptyList(), emptyList(), "test")))
        val picked = SectionedExamBuilder.build(format, bank)
        assertNotEquals(null, picked.first().group)
        assertEquals(2, picked.size)
    }

    @Test fun testBankFixtureParsesAndRuns() {
        val json = javaClass.classLoader!!.getResource("sectioned_exam_bank.json")!!.readText()
        val parsed = ExamBankParser.parse(json)
        assertEquals(2, SectionedExamBuilder.build(
            ExamFormat("test", "Test", "TEST", "", null, null, null, null, listOf(ExamSection("s1", "Items", "mcq", 2, listOf("E01"), emptyList(), "test"))),
            parsed,
        ).size)
    }
}
