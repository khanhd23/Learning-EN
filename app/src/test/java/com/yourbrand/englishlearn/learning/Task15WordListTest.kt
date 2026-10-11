package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.Content
import com.yourbrand.englishlearn.content.Example
import com.yourbrand.englishlearn.content.Sense
import com.yourbrand.englishlearn.content.Word
import com.yourbrand.englishlearn.content.WordListDefinition
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

    @Test
    fun newWordSamplingDoesNotUseAnUnstableRandomComparator() {
        val words = (0 until 500).map { word("word$it").copy(topics = if (it % 2 == 0) listOf("daily") else emptyList()) }
        val content = Content(emptyList(), words, emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyList(), emptyMap(), emptyMap())
        val builder = SessionBuilder(content, { null }, { emptyList() }, { emptyList() }, random = Random(11))

        val picked = builder.newWords(10, listOf("daily"), 1)

        assertEquals(10, picked.size)
        assertEquals(10, picked.map { it.id }.toSet().size)
    }

    @Test
    fun visibleExamListsResolveSourceCodesAndCefrLevels() {
        val words = listOf(
            word("toeic").copy(lists = listOf("tsl")),
            word("academic").copy(lists = listOf("nawl")),
            word("b1").copy(cefr = "B1"),
            word("other").copy(lists = listOf("bsl"), cefr = "A2"),
        )
        val definitions = listOf(
            WordListDefinition("toeic", "TOEIC", "TOEIC", "", sources = listOf("tsl"), cefr = emptyList(), credit = ""),
            WordListDefinition("ielts", "IELTS", "IELTS", "", sources = listOf("nawl"), cefr = emptyList(), credit = ""),
            WordListDefinition("vstep_b1", "VSTEP", "VSTEP B1", "", sources = emptyList(), cefr = listOf("B1"), credit = ""),
        )
        val content = Content(
            emptyList(), words, emptyList(), emptyList(), emptyList(), emptyList(), emptyList(),
            emptyList(), emptyMap(), emptyMap(), wordLists = definitions,
        )

        assertEquals(listOf("toeic"), content.wordsByList.getValue("toeic").map { it.id })
        assertEquals(listOf("academic"), content.wordsByList.getValue("ielts").map { it.id })
        assertEquals(listOf("b1"), content.wordsByList.getValue("vstep_b1").map { it.id })
    }

    private fun word(id: String) = Word(
        id = id, lemma = id, ipa = "", level = 1, topics = emptyList(),
        senses = listOf(Sense("${id}_s1", "n", "definition", "neutral", id, listOf(Example("${id}_e1", "Example.", null)))),
        family = emptyMap(), collocations = emptyList(), confusables = emptyList(), tip = null,
        tier = "silver",
    )
}
