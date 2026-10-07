package com.yourbrand.englishlearn.learning

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class GraderTest {
    @Test fun shadowingUsesSentenceAlignmentAndPunctuationTolerance() {
        val result = Grader.gradeSentence("Please open the door.", "please open the door!")
        assertEquals(Grader.Result.CORRECT, result.result)
        assertTrue(result.diff.wrongActual.isEmpty())
        assertTrue(result.diff.missingExpected.isEmpty())
    }
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

    @Test fun sentenceGradeAlignsMissingWordWithoutShiftingLaterWords() {
        val result = Grader.gradeSentence("I like apples", "I really like apples")
        assertEquals(Grader.Result.WRONG, result.result)
        assertEquals(setOf(1), result.diff.missingExpected)
        assertTrue(result.diff.wrongActual.isEmpty())
    }

    @Test fun sentenceGradeMarksExtraWordWithoutShiftingLaterWords() {
        val result = Grader.gradeSentence("I really like apples", "I like apples")
        assertEquals(Grader.Result.WRONG, result.result)
        assertEquals(setOf(1), result.diff.extraActual)
        assertTrue(result.diff.wrongActual.isEmpty())
    }

    @Test fun sentenceGradeReportsSwappedWordsAsAnAlignedDifference() {
        val result = Grader.gradeSentence("blue red green", "red blue green")
        assertEquals(Grader.Result.WRONG, result.result)
        assertTrue(result.diff.wrongActual.isNotEmpty() || result.diff.extraActual.isNotEmpty() || result.diff.missingExpected.isNotEmpty())
        assertTrue(result.diff.actual.indexOf("green") !in result.diff.wrongActual)
    }
}
