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

    @Test fun mixedBankFormatKeepsSectionsAndKeysInTheirSourceBanks() {
        val p5 = ExamBank(listOf(
            ExamItem("p5_set1_a", "p5", 3, "P5 A", listOf("A", "B", "C", "D"), 0, "Explanation").copy(set = "1"),
            ExamItem("p5_set2_a", "p5", 3, "P5 B", listOf("A", "B", "C", "D"), 0, "Explanation").copy(set = "2"),
        ), emptyList())
        val p6 = ExamBank(emptyList(), listOf(ExamGroup("p6_set1", "p6", "Passage", "P6", listOf(
            ExamItem("p6_set1_a", "p6", 3, "P6 A", listOf("A", "B", "C", "D"), 0, "Explanation"),
            ExamItem("p6_set1_b", "p6", 3, "P6 B", listOf("A", "B", "C", "D"), 0, "Explanation"),
        ), "1")))
        val p7 = ExamBank(emptyList(), listOf(ExamGroup("p7_set1", "p7", "Passage", "P7", listOf(
            ExamItem("p7_set1_a", "p7", 3, "P7 A", listOf("A", "B", "C", "D"), 0, "Explanation", "p7_single"),
            ExamItem("p7_set1_b", "p7", 3, "P7 B", listOf("A", "B", "C", "D"), 0, "Explanation", "p7_single"),
        ), "1")))
        val format = ExamFormat("mixed", "Mixed", "TEST", "", 6, null, 75, null, listOf(
            ExamSection("p5", "Part 5", "mcq", 1, emptyList(), emptyList(), "p5", "1"),
            ExamSection("p6", "Part 6", "group", 2, emptyList(), emptyList(), "p6", "1"),
            ExamSection("p7", "Part 7", "group", 2, emptyList(), listOf("p7_single"), "p7", "1"),
        ))

        val picked = SectionedExamBuilder.build(format, mapOf("p5" to p5, "p6" to p6, "p7" to p7), kotlin.random.Random(7))
        assertEquals(5, picked.size)
        assertEquals(listOf("p5", "p6", "p6", "p7", "p7"), picked.map { it.bank })
        assertTrue(picked.none { it.item.id == "p5_set2_a" })

        val content = Content(
            emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(),
            listOf(format), emptyList(), emptyMap(), emptyMap(),
            examBanks = mapOf("p5" to p5, "p6" to p6, "p7" to p7),
        )
        val builder = SessionBuilder(content, { null }, { emptyList() }, { emptyList() })
        val mock = MockTest.build(content, builder, "mixed", "Mixed", null, emptySet(), kotlin.random.Random(7))
        assertEquals(5, mock.keys.size)
        assertTrue(mock.keys.all { it.startsWith("exam:p5:") || it.startsWith("exam:p6:") || it.startsWith("exam:p7:") })
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

    @Test fun partSevenMiniTestUsesTenQuestionsButKeepsPassageLookup() {
        val groups = (1..4).map { number ->
            ExamGroup("p7_$number", "p7", "Passage $number", "Notice", (1..3).map { question ->
                ExamItem("p7_${number}_q$question", "p7", 3, "Question $number-$question", listOf("A", "B", "C", "D"), 0, "Explanation", "p7_single")
            }, "1")
        }
        val format = ExamFormat("toeic_p7_practice_1", "Part 7", "TOEIC-style practice", "", 54, null, null, null,
            listOf(ExamSection("p7", "Part 7 practice", "group", 54, listOf("E01"), listOf("p7_single", "p7_double", "p7_triple"), "toeic_p7", "1")))
        val content = Content(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), listOf(format), emptyList(), emptyMap(), emptyMap(), examBanks = mapOf("toeic_p7" to ExamBank(emptyList(), groups)))
        val builder = SessionBuilder(content, { null }, { emptyList() }, { emptyList() })
        val mock = MockTest.build(content, builder, "toeic_p7_practice_1", "Part 7 sample", 10, emptySet(), kotlin.random.Random(1))
        assertEquals(10, mock.keys.size)
        assertEquals(10, mock.sectionIds.size)
        assertTrue(mock.keys.all { (builder.exerciseFor(it) as Exercise.Choice).sharedPassage?.startsWith("Passage ") == true })
    }

    @Test fun sectionPracticeUsesOnlyTheRequestedPartFromAFullReadingPaper() {
        val p5 = ExamBank(listOf(
            ExamItem("p5_1", "p5", 2, "Question 1", listOf("A", "B", "C", "D"), 0, "Explanation"),
            ExamItem("p5_2", "p5", 2, "Question 2", listOf("A", "B", "C", "D"), 1, "Explanation"),
        ), emptyList())
        val p6 = ExamBank(emptyList(), listOf(ExamGroup("p6_1", "p6", "Passage", "P6", listOf(
            ExamItem("p6_1_1", "p6", 2, "Question", listOf("A", "B", "C", "D"), 0, "Explanation"),
        ))))
        val format = ExamFormat("reading_1", "Reading 1", "TOEIC", "", 3, null, 75, null, listOf(
            ExamSection("p5", "Part 5", "mcq", 2, emptyList(), emptyList(), "p5"),
            ExamSection("p6", "Part 6", "group", 1, emptyList(), emptyList(), "p6"),
        ))
        val content = Content(
            emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), listOf(format), emptyList(), emptyMap(), emptyMap(),
            examBanks = mapOf("p5" to p5, "p6" to p6),
        )

        val mock = MockTest.buildSection(content, "reading_1", "p5", "Reading 1 · Part 5", kotlin.random.Random(1))

        assertEquals(setOf("exam:p5:p5_1", "exam:p5:p5_2"), mock.keys.toSet())
        assertEquals(listOf("p5", "p5"), mock.sectionIds)
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

    @Test fun partThreePracticeReadsEnglishConversationAndQuestion() {
        val p3 = ExamBank(emptyList(), listOf(ExamGroup("p3_1", "p3", "Bản dịch hội thoại", "Hội thoại", listOf(
            ExamItem("p3_1_q1", "p3", 2, "What are they discussing?", listOf("A", "B", "C", "D"), 0, "Explanation", "listen_p3")
        ), "examples", "Man: What are they discussing?")))
        val content = Content(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyMap(), emptyMap(), examBanks = mapOf("toeic_p3" to p3))
        val builder = SessionBuilder(content, { null }, { emptyList() }, { emptyList() })
        val exercise = builder.examType("Part 3", "toeic_p3_examples", null, 3, 1).exercises.single() as Exercise.Choice
        assertTrue(exercise.audioOptions)
        assertEquals("Man: What are they discussing? What are they discussing?", exercise.audioPrompt)
    }

    @Test fun partFourPracticeReadsEnglishTalkAndQuestion() {
        val p4 = ExamBank(emptyList(), listOf(ExamGroup("p4_1", "p4", "Bản dịch bài nói", "Bài nói", listOf(
            ExamItem("p4_1_q1", "p4", 2, "When will it begin?", listOf("A", "B", "C", "D"), 1, "Explanation", "listen_p4")
        ), "examples", "The talk begins at noon.")))
        val content = Content(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyMap(), emptyMap(), examBanks = mapOf("toeic_p4" to p4))
        val builder = SessionBuilder(content, { null }, { emptyList() }, { emptyList() })
        val exercise = builder.examType("Part 4", "toeic_p4_examples", null, 3, 1).exercises.single() as Exercise.Choice
        assertTrue(exercise.audioOptions)
        assertEquals("The talk begins at noon. When will it begin?", exercise.audioPrompt)
    }

    @Test fun partSevenPracticeCanFilterByPassageType() {
        val p7 = ExamBank(emptyList(), listOf(ExamGroup("p7_1", "p7", "A short notice", "Notice", listOf(
            ExamItem("p7_1_q1", "p7", 3, "What is announced?", listOf("A", "B", "C", "D"), 0, "Explanation", "p7_single")
        ), "1")))
        val content = Content(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyMap(), emptyMap(), examBanks = mapOf("toeic_p7" to p7))
        val builder = SessionBuilder(content, { null }, { emptyList() }, { emptyList() })
        val exercise = builder.examType("Part 7", "toeic_p7_practice_1", "p7_single", 3, 1).exercises.single() as Exercise.Choice
        assertEquals("exam:toeic_p7:p7_1_q1", exercise.key)
        assertEquals("A short notice", exercise.sharedPassage)
    }

    @Test fun openClozeExamItemBuildsAsTypedExerciseAndPersists() {
        val item = ExamItem("ket_p5_1", "part5", 2, "Gap 1: Write one word.", emptyList(), -1, "Use at.", "a2_part5", "at")
        val content = Content(emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyMap(), emptyMap(), examBanks = mapOf("ket" to ExamBank(listOf(item), emptyList())))
        val builder = SessionBuilder(content, { null }, { emptyList() }, { emptyList() })
        val exercise = builder.exerciseFor("exam:ket:ket_p5_1")
        assertTrue(exercise is Exercise.ExamText)
        assertEquals("at", (exercise as Exercise.ExamText).answer)

        val mock = MockTest("ket_reading_1", "KET", listOf(exercise.key), null, textAnswers = arrayOf("at"))
        val resumed = MockTest.fromJson(mock.toJson())
        assertEquals(1, resumed.answeredCount)
        assertEquals("at", resumed.textAnswers.single())
    }
}
