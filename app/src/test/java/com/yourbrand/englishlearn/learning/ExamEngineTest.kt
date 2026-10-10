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
import org.junit.Assert.assertTrue
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

    @Test fun groupSetSelectionKeepsPracticePapersDisjoint() {
        fun group(id: String, set: String) = ExamGroup(id, "p7", "Passage $id", id, listOf(
            ExamItem("${id}_1", "p7", 3, "Question $id", listOf("A", "B", "C", "D"), 0, "Explanation"),
            ExamItem("${id}_2", "p7", 3, "Question $id 2", listOf("A", "B", "C", "D"), 1, "Explanation"),
        ), set)
        val papers = ExamBank(emptyList(), listOf(group("a1", "1"), group("a2", "1"), group("b1", "2"), group("b2", "2")))
        fun paper(set: String) = SectionedExamBuilder.build(
            ExamFormat("p7_$set", "Paper $set", "TEST", "", 4, null, null, null,
                listOf(ExamSection("p7", "Part 7", "group", 4, emptyList(), emptyList(), "p7", set))),
            papers,
        )
        assertEquals(setOf("a1_1", "a1_2", "a2_1", "a2_2"), paper("1").map { it.id }.toSet())
        assertEquals(setOf("b1_1", "b1_2", "b2_1", "b2_2"), paper("2").map { it.id }.toSet())
        assertEquals(emptySet<String>(), paper("1").map { it.id }.toSet() intersect paper("2").map { it.id }.toSet())
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

    @Test fun partOnePracticeMarksAllOptionsForSequentialTts() {
        val p1 = ExamBank(emptyList(), listOf(ExamGroup("p1_1", "p1", "Scene prompt", "Scene", listOf(
            ExamItem("p1_1_q1", "p1", 2, "Choose", listOf("A sentence", "B sentence", "C sentence", "D sentence"), 0, "Explanation", "listen_p1")
        ), "1")))
        val content = Content(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyMap(), emptyMap(), examBanks = mapOf("toeic_p1" to p1))
        val builder = SessionBuilder(content, { null }, { emptyList() }, { emptyList() })
        val exercise = builder.examType("Part 1", "toeic_p1_practice_1", null, 3, 1).exercises.single() as Exercise.Choice
        assertTrue(exercise.audioOptions)
        assertEquals(listOf("A sentence", "B sentence", "C sentence", "D sentence"), exercise.options)
    }

    @Test fun partTwoPracticeReadsPromptBeforeThreeResponses() {
        val p2 = ExamBank(emptyList(), listOf(ExamGroup("p2_1", "p2", "Where is the meeting?", "Question", listOf(
            ExamItem("p2_1_q1", "p2", 2, "Choose the best response.", listOf("In Room 2", "At noon", "Next week"), 0, "Explanation", "listen_p2")
        ), "1")))
        val content = Content(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyMap(), emptyMap(), examBanks = mapOf("toeic_p2" to p2))
        val builder = SessionBuilder(content, { null }, { emptyList() }, { emptyList() })
        val exercise = builder.examType("Part 2", "toeic_p2_practice_1", null, 3, 1).exercises.single() as Exercise.Choice
        assertTrue(exercise.audioOptions)
        assertEquals("Where is the meeting?", exercise.audioPrompt)
        assertEquals(3, exercise.options.size)
    }
}
