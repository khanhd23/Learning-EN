package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.content.Content
import com.yourbrand.englishlearn.content.Passage
import com.yourbrand.englishlearn.content.Question
import com.yourbrand.englishlearn.content.Word
import kotlin.random.Random

/** Builds runtime exercises from content. Every word gives many exercise types for free. */
class ExerciseFactory(private val content: Content, private val random: Random = Random.Default) {

    private fun wordTags(w: Word) = listOf("vocab_core", "pos_${w.pos}") + w.topics.take(1).map { "topic_$it" }

    /** Distractor words: same part of speech, similar level, different meaning. */
    private fun distractors(w: Word, n: Int = 3, pool: List<Word> = content.lessonWords): List<Word> {
        val glosses = HashSet<String>().apply { add(w.gloss.lowercase()) }
        val lemmas = HashSet<String>().apply { add(w.lemma.lowercase()) }
        val same = pool.filter { it.id != w.id && it.pos == w.pos }.shuffled(random)
            .sortedBy { kotlin.math.abs(it.level - w.level) + if (it.topics.any { t -> t in w.topics }) 0 else 1 }
        val any = pool.filter { it.id != w.id }.shuffled(random)
        val out = ArrayList<Word>()
        for (c in same.take(40) + any.take(40)) {
            if (out.size == n) break
            if (glosses.add(c.gloss.lowercase()) && lemmas.add(c.lemma.lowercase())) out += c
        }
        return out
    }

    private fun <T> withAnswer(correct: T, others: List<T>): Pair<List<T>, Int> {
        val all = (others + correct).shuffled(random)
        return all to all.indexOf(correct)
    }

    /** E01: English word → Vietnamese meaning. */
    fun meaning(w: Word): Exercise.Choice {
        val (opts, ans) = withAnswer(w.gloss, distractors(w).map { it.gloss })
        return Exercise.Choice(w.key, Kind.E01, wordTags(w), w.level, R.string.ins_meaning, w.lemma, opts, ans,
            explanation = explainWord(w), speak = w.lemma, wordId = w.id, hint = "(${w.pos}) ${w.ipa}")
    }

    /** E01 reversed: Vietnamese meaning → English word. */
    fun reverse(w: Word): Exercise.Choice {
        val (opts, ans) = withAnswer(w.lemma, distractors(w).map { it.lemma })
        return Exercise.Choice(w.key, Kind.E01, wordTags(w), w.level, R.string.ins_reverse, w.gloss, opts, ans,
            explanation = explainWord(w), speak = w.lemma, wordId = w.id)
    }

    /** E09 listening-lite: hear the word, pick its spelling. */
    fun listening(w: Word): Exercise.Choice {
        val (opts, ans) = withAnswer(w.lemma, distractors(w).map { it.lemma })
        return Exercise.Choice(w.key, Kind.E09, wordTags(w), w.level, R.string.ins_listen, "", opts, ans,
            explanation = explainWord(w), speak = w.lemma, audioOnly = true, wordId = w.id)
    }

    /** E02 from the word's own example sentence (only when the word appears verbatim). */
    fun cloze(w: Word): Exercise.Choice? {
        val ex = w.senses.first().examples.firstOrNull() ?: return null
        val rx = Regex("\\b" + Regex.escape(w.lemma) + "\\b", RegexOption.IGNORE_CASE)
        val m = rx.find(ex.text) ?: return null
        val stem = ex.text.replaceRange(m.range, "___")
        val (opts, ans) = withAnswer(w.lemma, distractors(w).map { it.lemma })
        return Exercise.Choice(w.key, Kind.E02, wordTags(w) + "collocation", w.level, R.string.ins_cloze, stem, opts, ans,
            explanation = explainWord(w) + (ex.vi?.let { "\n“$it”" } ?: ""), speak = ex.text, wordId = w.id)
    }

    fun flashcard(w: Word) = Exercise.Flashcard(w.key, wordTags(w), w.level, w)

    fun picture(w: Word, kind: Kind = Kind.E15): Exercise.PictureChoice? {
        // Off until Task 9.1 (ROADMAP): the current layouts print the answer next to each picture.
        if (!PICTURE_EXERCISES_READY) return null
        val others = content.lessonWords.filter { it.image != null && it.id != w.id && it.pos == w.pos }.shuffled(random).take(3)
        if (w.image == null || others.size < 3) return null
        val words = (others + w).shuffled(random)
        return Exercise.PictureChoice(w.key + ":" + kind.name, kind, wordTags(w) + "picture", w.level, words, words.indexOf(w), R.string.ins_choose,
            if (kind == Kind.E16) w.lemma else null)
    }

    fun spelling(w: Word): Exercise.Spelling? {
        if (w.lemma.contains(' ') || w.lemma.length > 14 || w.lemma.length < 3) return null
        val ex = w.senses.first().examples.firstOrNull()
        return Exercise.Spelling(w.key, wordTags(w) + "spelling", w.level, w, w.gloss, ex?.vi)
    }

    /** E06 with up to 4 words (word ↔ meaning). */
    fun matching(words: List<Word>): Exercise.Matching? {
        val ws = words.distinctBy { it.gloss.lowercase() }.take(4)
        if (ws.size < 3) return null
        val order = ws.indices.shuffled(random)
        val right = order.map { ws[it].gloss }
        val solution = ws.indices.map { i -> order.indexOf(i) }
        return Exercise.Matching("m:" + ws.joinToString(",") { it.id }, ws.map { it.key }, listOf("vocab_core"), ws.maxOf { it.level },
            ws.map { it.lemma }, right, solution)
    }

    fun explainWord(w: Word): String = buildString {
        append(w.lemma).append(" /").append(w.ipa).append("/  ")
        append(w.allGlosses)
    }

    /** Authored question → exercise. */
    fun question(q: Question): Exercise = when (q.type) {
        "E04" -> Exercise.FindError(q.key, q.tags, q.level, q.stem, q.answer, q.fix.orEmpty(), q.explanation)
        "E05" -> Exercise.WordOrder(q.key, q.tags, q.level, q.stem, Grader.chips(q.stem).shuffled(random).let { c ->
            // Never show the chips already in the right order.
            if (c.joinToString(" ") == q.stem && c.size > 1) c.reversed() else c
        }, q.translation)
        else -> {
            val instruction = when {
                q.qtype == "pronunciation" -> R.string.ins_pronunciation
                q.qtype == "stress" -> R.string.ins_stress
                q.qtype == "synonym" -> R.string.ins_synonym
                q.qtype == "antonym" -> R.string.ins_antonym
                q.type == "E12" -> R.string.ins_transform
                q.type == "E02" -> R.string.ins_cloze
                else -> R.string.ins_choose
            }
            val kind = when (q.type) { "E02" -> if (q.qtype == "pos") Kind.E11 else if (q.qtype == "confusable") Kind.E14 else Kind.E02; "E12" -> Kind.E12; else -> Kind.E01 }
            val marked = q.qtype == "pronunciation" || q.qtype == "stress"
            Exercise.Choice(q.key, kind, q.tags, q.level, instruction, q.stem, q.options, q.answer, q.explanation,
                speak = if (marked) null else q.stem.replace("___", q.options[q.answer]).replace("[", "").replace("]", "").ifBlank { null },
                optionsMarked = marked)
        }
    }

    fun passageBlank(p: Passage, i: Int): Exercise.Choice {
        val b = p.blanks[i]
        return Exercise.Choice(p.key(i), Kind.E02, listOf("passage", b.qtype), p.level,
            if (b.qtype == "sentence") R.string.ins_sentence else R.string.ins_passage, p.title, b.options, b.answer, b.explanation,
            passage = p, blank = i)
    }

    /** A varied set of exercises for one word, by how well it is known. */
    fun forWord(w: Word, state: ItemState?): Exercise {
        val box = state?.box ?: 0
        val options = buildList<() -> Exercise?> {
            if (box <= 1) { add { meaning(w) }; add { meaning(w) }; add { flashcard(w) } }
            if (box <= 1) add { picture(w, Kind.E15) }
            if (box >= 1) { add { reverse(w) }; add { listening(w) }; add { cloze(w) } }
            if (box >= 2) { add { spelling(w) }; add { cloze(w) } }
        }
        for (attempt in 0 until 4) options.random(random)()?.let { return it }
        return meaning(w)
    }
}

/** Picture exercises (E15–E17) stay disabled until their Task 9.1 layouts ship. */
const val PICTURE_EXERCISES_READY = false
