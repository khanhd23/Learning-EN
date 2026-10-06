package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.Passage
import com.yourbrand.englishlearn.content.Word

/** Exercise ids from SKILL.md section 7. */
enum class Kind(val baseXp: Int) {
    E01(4), E02(5), E03(6), E04(6), E05(6), E06(5), E07(3), E08(5), E09(4), E11(6), E12(6), E14(5), E15(5), E16(5), E17(6)
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
}

/** Typed-answer grading: NFC, trim, case-insensitive, trailing punctuation ignored, 1 typo allowed for words ≥ 7 letters. */
object Grader {
    enum class Result { CORRECT, ALMOST, WRONG }

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
