package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.Content
import com.yourbrand.englishlearn.content.Question
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TensePracticeTest {
    private val tenseIds = listOf(
        "present_simple", "present_continuous", "present_perfect", "present_perfect_continuous",
        "past_simple", "past_continuous", "past_perfect", "past_perfect_continuous",
        "future", "future_continuous", "future_perfect", "future_perfect_continuous",
    )

    @Test
    fun practiceHasAtLeastTenQuestionsPerExerciseTypeAndCoversEveryTense() {
        val questions = tenseIds.flatMap { id -> listOf(recognition(id), correction(id, "E04"), correction(id, "E05"), correction(id, "E12")) }
        val content = Content(
            emptyList(), emptyList(), emptyList(), emptyList(), questions,
            emptyList(), emptyList(), emptyList(), emptyMap(), emptyMap(),
        )
        val session = SessionBuilder(content, { null }, { emptyList() }, { emptyList() }, random = Random(7))
            .tensePractice("12 tenses", n = 42)

        assertEquals(42, session.exercises.size)
        assertEquals(SessionKind.TENSES, session.kind)
        val byPoint = session.exercises.groupBy { it.key.substringAfter(':').substringBeforeLast('_') }
        assertEquals(tenseIds.toSet(), byPoint.keys)
        val byType = session.exercises.groupingBy { it.kind }.eachCount()
        listOf(Kind.E02, Kind.E04, Kind.E05, Kind.E12).forEach { type ->
            assertTrue("$type should have at least ten questions", (byType[type] ?: 0) >= 10)
        }
    }

    private fun recognition(id: String) = Question(
        id = "${id}_recognition", type = "E02", fmt = "grammar", qtype = null, gp = id,
        level = 2, topic = null, skills = listOf("tense"), trap = emptyList(),
        stem = "They ___ outside.", options = listOf("work", "worked", "are working", "were working"),
        answer = 3, fix = null, translation = null, explanation = "Choose the tense that fits the time signal.",
    )

    private fun correction(id: String, type: String) = Question(
        id = "${id}_${type.lowercase()}", type = type, fmt = "grammar", qtype = null, gp = id,
        level = 2, topic = null, skills = listOf("tense"), trap = emptyList(),
        stem = if (type == "E04") "[They] [were] [work] [outside]." else "They worked outside.",
        options = if (type == "E04") emptyList() else listOf("They worked outside.", "They work outside.", "Outside worked they.", "They working outside."),
        answer = if (type == "E04") 2 else 0,
        fix = "working", translation = null, explanation = "Use the correct verb form.",
    )
}
