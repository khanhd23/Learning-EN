package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.Content
import com.yourbrand.englishlearn.content.Example
import com.yourbrand.englishlearn.content.Sense
import com.yourbrand.englishlearn.content.Word
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test

class Task15WordListTest {
    @Test
    fun listLessonPutsNewWordsBeforeDueReviews() {
        val words = listOf(word("new"), word("due"))
        val due = ItemState("w:due", seen = 1, due = 0L)
        val content = Content(emptyList(), words, emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyMap(), emptyMap())
        val builder = SessionBuilder(content, { key -> if (key == due.key) due else null }, { listOf(due) }, { emptyList() }, random = Random(7))

        val session = builder.words(SessionKind.WORDS, "List", words, VocabMode.FLASHCARD, n = 2, newFirst = true)

        assertEquals(listOf("w:new", "w:due"), session.exercises.map { it.key })
    }

    private fun word(id: String) = Word(
        id = id, lemma = id, ipa = "", level = 1, topics = emptyList(),
        senses = listOf(Sense("${id}_s1", "n", "definition", "neutral", id, listOf(Example("${id}_e1", "Example.", null)))),
        family = emptyMap(), collocations = emptyList(), confusables = emptyList(), tip = null,
        tier = "silver",
    )
}
