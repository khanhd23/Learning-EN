package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.Content
import com.yourbrand.englishlearn.content.Question
import com.yourbrand.englishlearn.content.Word
import kotlin.random.Random

enum class SessionKind { QUICK, REVIEW, WEAK, TOPIC, LEVEL, WORDS, GRAMMAR, EXAM_TYPE, BUILDER, MISTAKES, WELCOME, MEAL, CONFUSABLE, SPEED, SKIP_TEST, PLACEMENT }

/** Vocabulary study mode chosen from the topic screen menu. */
enum class VocabMode { MIXED, FLASHCARD, QUIZ, MATCHING, SPELLING, LISTENING }

class Session(
    val kind: SessionKind,
    val title: String,
    val exercises: MutableList<Exercise>,
    /** Grammar point being practiced (stars are awarded on pass). */
    val gp: String? = null,
    /** E13 speed round: 60 s timer + combo multiplier, bonus-only XP. */
    val speed: Boolean = false,
    /** Timed practice-builder mode: explanations only at the end. */
    val timed: Boolean = false,
)

/**
 * Selection pipelines from SKILL.md 9.1. Pure Kotlin except for reading state from [LearningStore].
 */
class SessionBuilder(
    private val content: Content,
    private val state: (String) -> ItemState?,
    private val allStates: () -> List<ItemState>,
    private val weakTags: () -> List<String>,
    private val random: Random = Random.Default,
) {
    private val factory = ExerciseFactory(content, random)

    fun exerciseFor(key: String): Exercise? = when {
        key.startsWith("w:") -> content.wordById[key.removePrefix("w:")]?.let { factory.forWord(it, state(key)) }
        key.startsWith("q:") -> content.questionById[key.removePrefix("q:")]?.let { factory.question(it) }
        key.startsWith("p:") -> key.split(':').let { p -> content.passageById[p[1]]?.let { ps -> p[2].toIntOrNull()?.takeIf { it < ps.blanks.size }?.let { i -> factory.passageBlank(ps, i) } } }
        else -> null
    }

    private fun dueKeys(now: Long, limit: Int): List<String> =
        allStates().filter { Scheduler.isDue(it, now) }.sortedBy { it.due }.take(limit).map { it.key }

    /** Questions matching the user's weakest tags (≥ 5 attempts per tag to count). */
    private fun weakQuestionKeys(limit: Int, exclude: Set<String>): List<String> {
        val weak = weakTags().take(3).toSet()
        if (weak.isEmpty()) return emptyList()
        return content.questions.filter { q -> q.tags.any { it in weak } && q.key !in exclude && q.type != "E05" }
            .sortedWith(compareByDescending<Question> { state(it.key)?.wrongRate ?: 0f }.thenBy { state(it.key)?.due ?: 0L })
            .take(limit * 2).shuffled(random).take(limit).map { it.key }
    }

    /** New words, preferring topics that match the learner's goals and their level. */
    fun newWords(limit: Int, preferredTopics: List<String>, userLevel: Int, exclude: Set<String> = emptySet()): List<Word> {
        val fresh = content.lessonWords.filter { state(it.key)?.isNew != false && it.key !in exclude }
        val ranked = fresh.sortedBy { w ->
            val topicScore = if (preferredTopics.isEmpty() || w.topics.any { it in preferredTopics }) 0 else 3
            topicScore + kotlin.math.abs(w.level - userLevel) + random.nextDouble()
        }
        return ranked.take(limit)
    }

    /** "Học 5 phút" — 60% due · 25% weak · 15% new. */
    fun quick(title: String, now: Long, preferredTopics: List<String>, userLevel: Int, n: Int = 10): Session {
        val due = dueKeys(now, (n * 0.6).toInt())
        val weak = weakQuestionKeys((n * 0.25).toInt().coerceAtLeast(1), due.toSet())
        val taken = (due + weak).toMutableSet()
        val newCount = (n - taken.size).coerceAtLeast((n * 0.15).toInt())
        val newWords = newWords(newCount, preferredTopics, userLevel, taken)
        val ex = (due + weak).mapNotNull { exerciseFor(it) } + newWords.map { factory.forWord(it, null) } +
            grammarFill(n - due.size - weak.size - newWords.size, userLevel)
        return Session(SessionKind.QUICK, title, mix(ex.take(n)))
    }

    /** Fills a short session with grammar items at the learner's level when nothing else is available. */
    private fun grammarFill(count: Int, level: Int): List<Exercise> {
        if (count <= 0) return emptyList()
        return content.questions.filter { it.fmt == "grammar" && it.level <= level + 1 && state(it.key)?.isNew != false && it.type != "E05" }
            .shuffled(random).take(count).map { factory.question(it) }
    }

    fun review(title: String, now: Long, n: Int = 15): Session {
        val keys = dueKeys(now, n).ifEmpty {
            allStates().filter { it.seen > 0 }.sortedBy { it.last }.take(n).map { it.key }
        }
        return Session(SessionKind.REVIEW, title, mix(keys.mapNotNull { exerciseFor(it) }))
    }

    /** "Bữa ăn" for the pet: 5 quick items (due first, then recent, then easy new words). */
    fun meal(title: String, now: Long, userLevel: Int): Session {
        val keys = dueKeys(now, 5).toMutableList()
        if (keys.size < 5) keys += allStates().filter { it.seen > 0 && it.key !in keys && !it.key.startsWith("p:") }.sortedByDescending { it.last }.take(5 - keys.size).map { it.key }
        val ex = keys.mapNotNull { exerciseFor(it) }.toMutableList()
        if (ex.size < 5) ex += newWords(5 - ex.size, emptyList(), userLevel.coerceAtMost(2)).map { factory.meaning(it) }
        return Session(SessionKind.MEAL, title, mix(ex.take(5)))
    }

    fun welcome(title: String): Session {
        val words = content.lessonWords.filter { it.level == 1 && !it.lemma.contains(' ') }.shuffled(random).take(5)
        return Session(SessionKind.WELCOME, title, words.map { factory.meaning(it) }.toMutableList())
    }

    fun weak(title: String, now: Long, n: Int = 10): Session {
        val keys = weakQuestionKeys(n, emptySet())
        val ex = keys.mapNotNull { exerciseFor(it) }.toMutableList()
        if (ex.size < n) ex += allStates().filter { it.wrong > 0 && it.key !in keys }.sortedByDescending { it.wrongRate }.take(n - ex.size).mapNotNull { exerciseFor(it.key) }
        return Session(SessionKind.WEAK, title, mix(ex))
    }

    fun words(kind: SessionKind, title: String, words: List<Word>, mode: VocabMode, n: Int = 10): Session {
        val pick = words.sortedWith(compareBy<Word> { (state(it.key)?.box ?: 0) >= Scheduler.MASTER_BOX }
            .thenBy { if (state(it.key)?.let { s -> Scheduler.isDue(s, System.currentTimeMillis()) } == true) 0 else 1 }
            .thenBy { random.nextInt(1000) }).take(n)
        val ex: List<Exercise> = when (mode) {
            VocabMode.FLASHCARD -> pick.map { factory.flashcard(it) }
            VocabMode.QUIZ -> pick.map { if (random.nextBoolean()) factory.meaning(it) else factory.reverse(it) }
            VocabMode.LISTENING -> pick.map { factory.listening(it) }
            VocabMode.SPELLING -> pick.mapNotNull { factory.spelling(it) }.ifEmpty { pick.map { factory.reverse(it) } }
            VocabMode.MATCHING -> pick.chunked(4).mapNotNull { factory.matching(it) }.ifEmpty { pick.map { factory.meaning(it) } }
            VocabMode.MIXED -> {
                val list = pick.map { factory.forWord(it, state(it.key)) }.toMutableList()
                pick.filter { it.image != null }.shuffled(random).take(2).forEach { w ->
                    factory.picture(w, if (random.nextBoolean()) Kind.E16 else Kind.E17)?.let { list.add(it) }
                }
                if (pick.size >= 4) factory.matching(pick.shuffled(random).take(4))?.let { list.add(list.size / 2, it) }
                list
            }
        }
        return Session(kind, title, if (mode == VocabMode.MIXED) mix(ex) else ex.toMutableList())
    }

    /** Three quick exercises for a single word (word detail → "Luyện từ này"). */
    fun singleWord(title: String, w: Word): Session =
        Session(SessionKind.WORDS, title, listOfNotNull(factory.meaning(w), factory.cloze(w) ?: factory.reverse(w), factory.spelling(w) ?: factory.listening(w)).toMutableList())

    fun grammar(title: String, gp: String, n: Int = 10): Session {
        val qs = content.questionsByPoint[gp].orEmpty()
        // Progressive: recognize (cloze) → fix error → build sentence.
        val cloze = qs.filter { it.type == "E02" || it.type == "E01" || it.type == "E12" }.shuffled(random)
        val fix = qs.filter { it.type == "E04" }.shuffled(random)
        val order = qs.filter { it.type == "E05" }.shuffled(random)
        val picked = (cloze.take(n - 4) + fix.take(2) + order.take(2)).let { if (it.size < n) it + (cloze.drop(n - 4)).take(n - it.size) else it }
        return Session(SessionKind.GRAMMAR, title, picked.take(n).map { factory.question(it) }.toMutableList(), gp = gp)
    }

    /** 5-question skip test that unlocks a grammar node early. */
    fun skipTest(title: String, gp: String): Session {
        val qs = content.questionsByPoint[gp].orEmpty().filter { it.type == "E02" || it.type == "E04" }.shuffled(random).take(5)
        return Session(SessionKind.SKIP_TEST, title, qs.map { factory.question(it) }.toMutableList(), gp = gp)
    }

    fun confusable(title: String, setId: String): Session {
        val set = content.confusableById[setId]
        val qs = content.questions.filter { it.fmt == "vocab" && it.qtype == "confusable" && set != null && set.words.any { w -> it.options.any { o -> o.equals(w, true) } } }
        return Session(SessionKind.CONFUSABLE, title, qs.shuffled(random).map { factory.question(it) }.toMutableList())
    }

    /** Practice by exam question type (S11). difficulty: 0 easy · 1 medium · 2 hard · 3 mixed. */
    fun examType(title: String, fmt: String, qtype: String?, difficulty: Int, n: Int = 10): Session {
        val ex = if (fmt == "toeic_p6") {
            content.passages.flatMap { p -> p.blanks.indices.filter { qtype == null || p.blanks[it].qtype == qtype }.map { p to it } }
                .sortedBy { (p, i) -> if (state(p.key(i))?.isNew != false) 0 else 1 }.take(n * 2).shuffled(random).take(n)
                .sortedWith(compareBy({ it.first.id }, { it.second })).map { (p, i) -> factory.passageBlank(p, i) }
        } else {
            val pool = examPool(fmt).filter { qtype == null || it.qtype == qtype }
            val byLevel = when (difficulty) { 0 -> pool.filter { it.level <= 2 }; 1 -> pool.filter { it.level == 3 }; 2 -> pool.filter { it.level >= 4 }; else -> pool }
                .ifEmpty { pool }
            byLevel.sortedBy { if (state(it.key)?.isNew != false) 0 else 1 + random.nextInt(3) }.take(n).shuffled(random).map { factory.question(it) }
        }
        return Session(SessionKind.EXAM_TYPE, title, ex.toMutableList())
    }

    /** Items that belong to an exam format (school also reuses grammar find-error / transform items). */
    fun examPool(fmt: String): List<Question> = when (fmt) {
        "school" -> content.questions.filter { it.fmt == "school" || it.type == "E04" || it.type == "E12" }
        "toeic_p5" -> content.questions.filter { it.fmt == "toeic_p5" || (it.fmt == "grammar" && it.type == "E02" && it.qtype != null) }
        else -> content.questions.filter { it.fmt == fmt }
    }

    fun mistakes(title: String, tag: String?): Session {
        val keys = allStates().filter { it.mistake }.map { it.key }
        val ex = keys.mapNotNull { exerciseFor(it) }.filter { tag == null || tag in it.tags }
        return Session(SessionKind.MISTAKES, title, ex.shuffled(random).take(20).toMutableList())
    }

    /** E13 speed round: rapid multiple choice on known words and easy items. */
    fun speed(title: String, userLevel: Int): Session {
        val known = allStates().filter { it.key.startsWith("w:") && it.seen > 0 }.mapNotNull { content.wordById[it.key.removePrefix("w:")] }
        val words = (known + content.lessonWords.filter { it.level <= userLevel }).distinct().shuffled(random).take(40)
        return Session(SessionKind.SPEED, title, words.map { if (random.nextBoolean()) factory.meaning(it) else factory.reverse(it) }.toMutableList(), speed = true)
    }

    fun builder(title: String, result: PracticeBuilder.Result, count: Int, timed: Boolean): Session {
        val picked = result.pick(count, random) { state(it) }
        return Session(SessionKind.BUILDER, title, mix(picked.mapNotNull { exerciseFor(it) }), timed = timed)
    }

    /**
     * Ordering rules (SKILL.md 7): first item is an easy win, never the same type more than 2 in a row,
     * the last item medium.
     */
    fun mix(list: List<Exercise>): MutableList<Exercise> {
        if (list.size < 3) return list.toMutableList()
        val pool = list.shuffled(random).toMutableList()
        val first = pool.minBy { it.level }
        pool.remove(first)
        val out = mutableListOf(first)
        while (pool.isNotEmpty()) {
            val lastTwo = out.takeLast(2).map { it.kind }
            val idx = pool.indexOfFirst { !(lastTwo.size == 2 && lastTwo.all { k -> k == it.kind }) }.coerceAtLeast(0)
            out += pool.removeAt(idx)
        }
        return out
    }
}
