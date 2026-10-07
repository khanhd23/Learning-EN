package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.Passage
import com.yourbrand.englishlearn.content.Word

/** Exercise ids from SKILL.md section 7. */
enum class Kind(val baseXp: Int) {
    E01(4), E02(5), E03(6), E04(6), E05(6), E06(5), E07(3), E08(5), E09(4), E11(6), E12(6), E14(5), E15(5), E16(5), E17(6), E18(6), E19(5), E20(6)
}

/** A single runtime exercise built from content by [ExerciseFactory]. */
sealed class Exercise {
    abstract val key: String
    abstract val kind: Kind
    abstract val tags: List<String>
    abstract val level: Int

    /** Multiple choice of any flavour (meaning, cloze, listening, transform, passage blank, pronunciation...). */
    data class Choice(
        override val key: String,
        override val kind: Kind,
        override val tags: List<String>,
        override val level: Int,
        /** String resource id of the instruction ("Chọn đáp án đúng"). */
        val instruction: Int,
        /** Main text. "___" marks the blank; "[x]" marks underlined parts. */
        val stem: String,
        val options: List<String>,
        val answer: Int,
        val explanation: String,
        /** Text read aloud by TTS when the speaker is tapped (or automatically for listening). */
        val speak: String? = null,
        /** Listening: hide the stem, play [speak] instead. */
        val audioOnly: Boolean = false,
        /** Options render with [x] underline markup (pronunciation / stress items). */
        val optionsMarked: Boolean = false,
        /** Part 6: passage shown above the options with this blank highlighted. */
        val passage: Passage? = null,
        val blank: Int = -1,
        val wordId: String? = null,
        val hint: String? = null,
    ) : Exercise()

    /** E04: tap the wrong part among four marked segments. */
    data class FindError(
        override val key: String,
        override val tags: List<String>,
        override val level: Int,
        val stem: String,
        val answer: Int,
        val fix: String,
        val explanation: String,
    ) : Exercise() { override val kind get() = Kind.E04 }

    /** E05: build the sentence from shuffled chips. */
    data class WordOrder(
        override val key: String,
        override val tags: List<String>,
        override val level: Int,
        val sentence: String,
        val chips: List<String>,
        val translation: String?,
    ) : Exercise() { override val kind get() = Kind.E05 }

    /** E06: match 4 words with their meanings. All words share one exercise; keys hold each word. */
    data class Matching(
        override val key: String,
        val keys: List<String>,
        override val tags: List<String>,
        override val level: Int,
        val left: List<String>,
        val right: List<String>,
        /** right index for each left index. */
        val solution: List<Int>,
    ) : Exercise() { override val kind get() = Kind.E06 }

    /** E07: flip card and self-rate. */
    data class Flashcard(
        override val key: String,
        override val tags: List<String>,
        override val level: Int,
        val word: Word,
    ) : Exercise() { override val kind get() = Kind.E07 }

    /** E08: type the word from its meaning. */
    data class Spelling(
        override val key: String,
        override val tags: List<String>,
        override val level: Int,
        val word: Word,
        val gloss: String,
        val example: String?,
    ) : Exercise() { override val kind get() = Kind.E08 }

    data class PictureChoice(
        override val key: String,
        override val kind: Kind,
        override val tags: List<String>,
        override val level: Int,
        val words: List<Word>,
        val answer: Int,
        val instruction: Int,
        val promptImage: String? = null,
        val showWordOptions: Boolean = false,
        val speak: String? = null,
    ) : Exercise()

    data class PictureMatch(
        override val key: String,
        override val tags: List<String>,
        override val level: Int,
        val keys: List<String>,
        val words: List<Word>,
        val solution: List<Int>,
    ) : Exercise() { override val kind get() = Kind.E17 }

    data class Dictation(
        override val key: String,
        override val tags: List<String>,
        override val level: Int,
        val sentence: String,
        val translation: String? = null,
    ) : Exercise() { override val kind get() = Kind.E18 }

    data class MinimalPairChoice(
        override val key: String,
        override val tags: List<String>,
        override val level: Int,
        val first: String,
        val second: String,
        val answer: Int,
        val focus: String,
    ) : Exercise() { override val kind get() = Kind.E19 }

    data class Shadowing(
        override val key: String,
        override val tags: List<String>,
        override val level: Int,
        val sentence: String,
    ) : Exercise() { override val kind get() = Kind.E20 }
}

/** Typed-answer grading: NFC, trim, case-insensitive, trailing punctuation ignored, 1 typo allowed for words ≥ 7 letters. */
object Grader {
    enum class Result { CORRECT, ALMOST, WRONG }
    data class SentenceDiff(
        val actual: List<String>,
        val expected: List<String>,
        val wrongActual: Set<Int>,
        val extraActual: Set<Int>,
        val missingExpected: Set<Int>,
        val almostActual: Set<Int>,
    )
    data class SentenceResult(val result: Result, val wrongWords: List<Int>, val diff: SentenceDiff)

    fun normalize(s: String): String =
        java.text.Normalizer.normalize(s, java.text.Normalizer.Form.NFC).trim().lowercase()
            .replace('’', '\'').replace(Regex("[.!?,;:]+$"), "").replace(Regex("\\s+"), " ")

    fun grade(input: String, expected: String): Result {
        val a = normalize(input)
        val b = normalize(expected)
        if (a == b) return Result.CORRECT
        if (b.length >= 7 && distance(a, b) <= 1) return Result.ALMOST
        return Result.WRONG
    }

    /** Grades words using edit-distance alignment so one missing word does not shift every later word. */
    fun gradeSentence(input: String, expected: String): SentenceResult {
        val actual = input.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        val wanted = expected.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }
        val dp = Array(actual.size + 1) { IntArray(wanted.size + 1) }
        for (i in actual.indices) dp[i + 1][0] = i + 1
        for (j in wanted.indices) dp[0][j + 1] = j + 1
        for (i in actual.indices) for (j in wanted.indices) {
            val substitution = if (grade(actual[i], wanted[j]) == Result.CORRECT) 0 else 1
            dp[i + 1][j + 1] = minOf(dp[i][j + 1] + 1, dp[i + 1][j] + 1, dp[i][j] + substitution)
        }
        val wrong = linkedSetOf<Int>(); val extra = linkedSetOf<Int>(); val missing = linkedSetOf<Int>(); val almost = linkedSetOf<Int>()
        var i = actual.size; var j = wanted.size
        while (i > 0 || j > 0) {
            val same = i > 0 && j > 0 && grade(actual[i - 1], wanted[j - 1]) == Result.CORRECT && dp[i][j] == dp[i - 1][j - 1]
            val near = i > 0 && j > 0 && grade(actual[i - 1], wanted[j - 1]) == Result.ALMOST && dp[i][j] == dp[i - 1][j - 1] + 1
            if (same || near) { if (near) almost += i - 1; i--; j--; continue }
            if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) { wrong += i - 1; i--; j--; continue }
            if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) { extra += i - 1; i--; continue }
            missing += j - 1; j--
        }
        val diff = SentenceDiff(actual, wanted, wrong, extra, missing, almost)
        val allWrong = wrong + extra + missing
        return SentenceResult(if (allWrong.isNotEmpty()) Result.WRONG else if (almost.isNotEmpty()) Result.ALMOST else Result.CORRECT, wrong.toList() + extra, diff)
    }

    fun distance(a: String, b: String): Int {
        val dp = IntArray(b.length + 1) { it }
        for (i in 1..a.length) {
            var prev = dp[0]
            dp[0] = i
            for (j in 1..b.length) {
                val tmp = dp[j]
                dp[j] = minOf(dp[j] + 1, dp[j - 1] + 1, prev + if (a[i - 1] == b[j - 1]) 0 else 1)
                prev = tmp
            }
        }
        return dp[b.length]
    }

    /** Splits a sentence into word-order chips (punctuation stays attached to its word). */
    fun chips(sentence: String): List<String> = sentence.trim().split(Regex("\\s+"))

    fun sameSentence(built: List<String>, sentence: String) = normalize(built.joinToString(" ")) == normalize(sentence)
}
