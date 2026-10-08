package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.ExamBank
import com.yourbrand.englishlearn.content.ExamBankParser
import com.yourbrand.englishlearn.content.ExamFormat
import com.yourbrand.englishlearn.content.ExamGroup
import com.yourbrand.englishlearn.content.ExamItem
import com.yourbrand.englishlearn.content.ExamSection
import com.yourbrand.englishlearn.content.Content
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
            ExamSection("s1", "Items", "mcq", 2, listOf("E02"), emptyList(), "test"),
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

    @Test fun sectionedMockBuildAnswerAndResume() {
        val format = ExamFormat("test", "Test", "TEST", "", null, null, 10, null, listOf(
            ExamSection("s1", "Items", "mcq", 2, emptyList(), emptyList(), "test"),
            ExamSection("s2", "Passage", "group", 2, emptyList(), emptyList(), "test"),
        ))
        val content = Content(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), listOf(format), emptyList(), emptyMap(), emptyMap(), examBanks = mapOf("test" to bank))
        val builder = SessionBuilder(content, { null }, { emptyList() }, { emptyList() })
        val mock = MockTest.build(content, builder, "test", "Test", null, emptySet(), kotlin.random.Random(1))
        mock.keys.forEachIndexed { i, key -> mock.answers[i] = (builder.exerciseFor(key) as Exercise.Choice).answer }
        assertEquals(mock.keys.size, mock.answers.count { it >= 0 })
        assertEquals(4, mock.keys.indices.count { i ->
            mock.answers[i] == (builder.exerciseFor(mock.keys[i]) as Exercise.Choice).answer
        })
        val resumed = MockTest.fromJson(mock.toJson())
        assertEquals(mock.keys, resumed.keys)
        assertEquals(mock.sectionIds, resumed.sectionIds)
        assertEquals(4, resumed.keys.size)
    }

    @Test fun localizedExamOverridesRequireApproval() {
        val localized = """
            {"s1_i1":{"stem":"Approved translated stem","expl":"Approved translated explanation"},
             "s1_i2":{"stem":"Unapproved translated stem","expl":"Must not be used"},
             "g1":{"passage":"Approved translated passage","title":"Approved translated title"}}
        """.trimIndent()
        val merged = ExamBankParser.merge(bank, localized, setOf("s1_i1", "g1"))
        assertEquals("Approved translated stem", merged.items.first().stem)
        assertEquals("Choose B", merged.items[1].stem)
        assertEquals("Approved translated passage", merged.groups.first().passage)
        assertEquals("Approved translated title", merged.groups.first().title)
    }

    @Test fun toeicPracticeReadsPartFiveAndPartSixFromTheSectionedBank() {
        val toeic = ExamBank(
            listOf(ExamItem("p5_1", "p5", 2, "Choose", listOf("A", "B", "C", "D"), 0, "P5 explanation")),
            listOf(ExamGroup("p6_1", "p6", "A shared passage {1}.", "Notice", listOf(
                ExamItem("p6_1_1", "p6", 3, "Choose", listOf("A", "B", "C", "D"), 1, "P6 explanation"),
            )))
        )
        val content = Content(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyMap(), emptyMap(), examBanks = mapOf("toeic" to toeic))
        val builder = SessionBuilder(content, { null }, { emptyList() }, { emptyList() })
        val p5 = builder.examType("Part 5", "toeic", "p5", 3, 1).exercises.single() as Exercise.Choice
        val p6 = builder.examType("Part 6", "toeic", "p6", 3, 1).exercises.single() as Exercise.Choice
        assertEquals("exam:toeic:p5_1", p5.key)
        assertEquals("exam:toeic:p6_1_1", p6.key)
        assertEquals("A shared passage {1}.", p6.sharedPassage)
    }
}
