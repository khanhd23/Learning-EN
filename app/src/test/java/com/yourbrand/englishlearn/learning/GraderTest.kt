package com.yourbrand.englishlearn.learning

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GraderTest {
    @Test fun sentenceGradeIgnoresCaseAndPunctuationPerWord() {
        val result = Grader.gradeSentence("the QUICK fox jumps.", "The quick fox jumps!")
        assertEquals(Grader.Result.CORRECT, result.result)
        assertTrue(result.wrongWords.isEmpty())
    }

    @Test fun sentenceGradeReportsWrongWordPosition() {
        val result = Grader.gradeSentence("The slow fox jumps", "The quick fox jumps")
        assertEquals(Grader.Result.WRONG, result.result)
        assertEquals(listOf(1), result.wrongWords)
    }

    @Test fun sentenceGradeAllowsOneLongWordTypo() {
        val result = Grader.gradeSentence("The restauranx is known", "The restaurant is known")
        assertEquals(Grader.Result.ALMOST, result.result)
    }
}
