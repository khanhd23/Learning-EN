package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.Content
import com.yourbrand.englishlearn.content.Question
import com.yourbrand.englishlearn.content.Word
import kotlin.random.Random

enum class SessionKind { QUICK, REVIEW, WEAK, TOPIC, LEVEL, WORDS, GRAMMAR, TENSES, EXAM_TYPE, BUILDER, MISTAKES, WELCOME, MEAL, CONFUSABLE, SPEED, DAILY, SKIP_TEST, PLACEMENT }

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
    private val sessionsDone: () -> Int = { 0 },
    private val random: Random = Random.Default,
) {
    private val factory = ExerciseFactory(content, random)

    fun exerciseFor(key: String, wordPool: List<Word> = content.lessonWords): Exercise? = when {
        key.startsWith("w:") -> content.wordById[key.removePrefix("w:")]?.let { factory.forWord(it, state(key), wordPool) }
        key.startsWith("q:") -> content.questionById[key.removePrefix("q:")]?.let { factory.question(it) }
        key.startsWith("d:") -> content.sound.dictation.firstOrNull { it.id == key.removePrefix("d:") }?.let { factory.dictation(it) }
        key.startsWith("mp:") -> content.sound.pairs.firstOrNull { "mp:${it.id}" == key.substringBeforeLast(':') }?.let { factory.minimalPair(it) }
        key.startsWith("sh:") -> content.sound.shadowing.firstOrNull { it.id == key.removePrefix("sh:") }?.let { factory.shadowing(it) }
        key.startsWith("exam:") -> key.split(':').let { p ->
            if (p.size < 3) null else content.examBanks[p[1]]?.let { bank ->
                val item = (bank.items.firstOrNull { it.id == p[2] } ?: bank.groups.asSequence().flatMap { it.items.asSequence() }.firstOrNull { it.id == p[2] })
                val group = bank.groups.firstOrNull { group -> group.items.any { it.id == p[2] } }
                item?.let { factory.exam(p[1], it, group) }
            }
        }
        key.startsWith("p:") -> key.split(':').let { p -> content.passageById[p[1]]?.let { ps -> p[2].toIntOrNull()?.takeIf { it < ps.blanks.size }?.let { i -> factory.passageBlank(ps, i) } } }
        else -> null
    }

    private fun dueKeys(now: Long, limit: Int): List<String> =
        allStates().filter { Scheduler.isDue(it, now) }.sortedBy { it.due }.take(limit).map { it.key }

    /** Questions matching the user's weakest tags (≥ 5 attempts per tag to count). */
    private fun weakQuestionKeys(limit: Int, exclude: Set<String>): List<String> {
        val weak = weakTags().take(3).toSet()
        if (weak.isEmpty()) return emptyList()
        return content.questions.asSequence()
            .filter { q -> q.tags.any { it in weak } && q.key !in exclude && q.type != "E05" }
            .map { q -> q to state(q.key) }
            .sortedWith(compareByDescending<Pair<Question, ItemState?>> { it.second?.wrongRate ?: 0f }
                .thenBy { it.second?.due ?: 0L })
            .take(limit * 2).shuffled(random).take(limit).map { it.first.key }.toList()
    }

    /** New words, preferring topics that match the learner's goals and their level. */
    fun newWords(limit: Int, preferredTopics: List<String>, userLevel: Int, exclude: Set<String> = emptySet()): List<Word> {
        if (limit <= 0) return emptyList()
        val excludeIds = exclude.mapTo(HashSet()) { it.removePrefix("w:") }
        val preferred = sampleWords(content.lessonWords, limit, excludeIds) { word ->
            state(word.key)?.isNew != false &&
                word.level <= userLevel + 1 &&
                (preferredTopics.isEmpty() || word.topics.any { it in preferredTopics })
        }
        val used = HashSet<String>(excludeIds).apply { addAll(preferred.map { it.id }) }
        val fallback = sampleWords(content.lessonWords, limit - preferred.size, used) { word ->
            state(word.key)?.isNew != false && word.level <= userLevel + 1
        }
        return (preferred + fallback).take(limit)
    }

    /** "Học 5 phút" — 60% due · 25% weak · 15% new. */
    fun quick(title: String, now: Long, preferredTopics: List<String>, userLevel: Int, n: Int = 10): Session {
        val due = dueKeys(now, (n * 0.6).toInt())
        val weak = weakQuestionKeys((n * 0.25).toInt().coerceAtLeast(1), due.toSet())
        val taken = (due + weak).toMutableSet()
        val newCount = (n - taken.size).coerceAtLeast((n * 0.15).toInt())
        val newWords = newWords(newCount, preferredTopics, userLevel, taken)
        val dueWords = (due + weak).mapNotNull { key -> content.wordById[key.removePrefix("w:")] }
        val wordPool = (dueWords + newWords + speedWords(userLevel, 40)).distinctBy { it.id }
        val ex = (due + weak).mapNotNull { exerciseFor(it, wordPool) } + newWords.map { factory.forWord(it, null, wordPool) } +
            grammarFill(n - due.size - weak.size - newWords.size, userLevel)
        return Session(SessionKind.QUICK, title, mix(ex.take(n)))
    }

    /** Five deterministic daily items, combining due and weak work with a day-varying mix. */
    fun daily(title: String, now: Long, preferredTopics: List<String>, userLevel: Int): Session {
        val day = LearningStore.dayKey(now)
        val due = dueKeys(now, 5)
        val weak = weakQuestionKeys(5, due.toSet())
        val keys = (due + weak).distinct().take(5).toMutableList()
        if (keys.size < 5) keys += newWords(5 - keys.size, preferredTopics, userLevel, keys.toSet()).map { it.key }
        val exercises = keys.mapIndexedNotNull { i, key ->
            val base = exerciseFor(key) ?: return@mapIndexedNotNull null
            // The order and available base exercises vary by local day, while the item pool remains stable.
            if (i % 2 == Math.floorMod(day, 2L).toInt() && key.startsWith("w:")) {
                content.wordById[key.removePrefix("w:")]?.let { factory.forWord(it, state(key)) } ?: base
            } else base
        }
        return Session(SessionKind.DAILY, title, exercises.toMutableList())
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
        return Session(SessionKind.REVIEW, title, addOptionalSound(mix(keys.mapNotNull { exerciseFor(it) })))
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

    fun words(kind: SessionKind, title: String, words: List<Word>, mode: VocabMode, n: Int = 10, newFirst: Boolean = false): Session {
        val now = System.currentTimeMillis()
        val pick = if (newFirst) {
            val fresh = sampleWords(words, n) { state(it.key)?.isNew != false }
            val freshIds = fresh.mapTo(HashSet()) { it.id }
            val due = sampleWords(words, n - fresh.size, freshIds) { candidate ->
                val item = state(candidate.key)
                item != null && !item.isNew && Scheduler.isDue(item, now)
            }
            val used = HashSet<String>(freshIds).apply { addAll(due.map { it.id }) }
            val rest = sampleWords(words, n - fresh.size - due.size, used) { true }
            (fresh + due + rest).take(n)
        } else {
            // Never put a random value inside a comparator: it violates the comparator contract
            // on large lists and can crash TimSort. Bounded sampling also avoids sorting 100k words.
            val due = sampleWords(words, n, predicate = { state(it.key)?.let { s -> Scheduler.isDue(s, now) } == true })
            val used = due.mapTo(HashSet()) { it.id }
            val learning = sampleWords(words, n - due.size, used) { candidate ->
                val s = state(candidate.key)
                s != null && !s.mastered && !Scheduler.isDue(s, now)
            }
            val usedAll = HashSet(used).apply { addAll(learning.map { it.id }) }
            val rest = sampleWords(words, n - due.size - learning.size, usedAll) { true }
            (due + learning + rest).take(n)
        }
        val ex: List<Exercise> = when (mode) {
            VocabMode.FLASHCARD -> pick.map { factory.flashcard(it) }
            VocabMode.QUIZ -> pick.map { if (random.nextBoolean()) factory.meaning(it) else factory.reverse(it) }
            VocabMode.LISTENING -> pick.map { factory.listening(it) }
            VocabMode.SPELLING -> pick.mapNotNull { factory.spelling(it) }.ifEmpty { pick.map { factory.reverse(it) } }
            VocabMode.MATCHING -> pick.chunked(4).mapNotNull { factory.matching(it) }.ifEmpty { pick.map { factory.meaning(it) } }
            VocabMode.MIXED -> {
                val distractorPool = if (newFirst) pick else content.lessonWords
                val list = pick.map { factory.forWord(it, state(it.key), distractorPool) }.toMutableList()
                pick.filter { it.image != null }.shuffled(random).take(2).forEach { w ->
                    if (random.nextBoolean()) factory.picture(w, Kind.E16)?.let { list.add(it) }
                    else factory.pictureMatch(w, distractorPool)?.let { list.add(it) }
                }
                if (pick.size >= 4) factory.matching(pick.shuffled(random).take(4))?.let { list.add(list.size / 2, it) }
                list
            }
        }
        val result = if (mode == VocabMode.MIXED) mix(ex) else ex.toMutableList()
        return Session(kind, title, if (kind == SessionKind.TOPIC) addOptionalSound(result) else result)
    }

    /** Bounded random selection; avoids sorting or shuffling a potentially huge topic/list. */
    private fun sampleWords(source: List<Word>, limit: Int, exclude: Set<String> = emptySet(), predicate: (Word) -> Boolean): List<Word> {
        if (limit <= 0 || source.isEmpty()) return emptyList()
        val out = ArrayList<Word>(limit)
        val used = HashSet<String>(exclude)
        var attempts = 0
        while (out.size < limit && attempts++ < limit * 32) {
            val word = source[random.nextInt(source.size)]
            if (word.id !in used && predicate(word)) {
                used += word.id
                out += word
            }
        }
        if (out.size < limit) {
            source.asSequence()
                .filter { it.id !in used && predicate(it) }
                .take(limit - out.size)
                .forEach { used += it.id; out += it }
        }
        return out.shuffled(random)
    }

    /** Three quick exercises for a single word (word detail → "Luyện từ này"). */
    fun singleWord(title: String, w: Word): Session =
        Session(SessionKind.WORDS, title, listOfNotNull(factory.meaning(w), factory.cloze(w) ?: factory.reverse(w), factory.spelling(w) ?: factory.listening(w)).toMutableList())

    fun sound(title: String, n: Int = 10): Session {
        val dictation = content.sound.dictation.shuffled(random).take(n / 3).map { factory.dictation(it) }
        val pairs = content.sound.pairs.shuffled(random).take(n / 3).map { factory.minimalPair(it) }
        val shadowing = content.sound.shadowing.shuffled(random).take(n - dictation.size - pairs.size).map { factory.shadowing(it) }
        return Session(SessionKind.WORDS, title, mix(dictation + pairs + shadowing))
    }

    private fun addOptionalSound(exercises: MutableList<Exercise>): MutableList<Exercise> {
        if (sessionsDone() < 3) return exercises
        val extra = (content.sound.dictation.map { factory.dictation(it) } + content.sound.pairs.map { factory.minimalPair(it) }).randomOrNull(random) ?: return exercises
        return exercises.toMutableList().apply { add(random.nextInt(size + 1), extra) }
    }

    fun grammar(title: String, gp: String, n: Int = 10): Session {
        val qs = content.questionsByPoint[gp].orEmpty()
        // Progressive: recognize (cloze) → fix error → build sentence.
        val cloze = qs.filter { it.type == "E02" || it.type == "E01" || it.type == "E12" }.shuffled(random)
        val fix = qs.filter { it.type == "E04" }.shuffled(random)
        val order = qs.filter { it.type == "E05" }.shuffled(random)
        val picked = (cloze.take(n - 4) + fix.take(2) + order.take(2)).let { if (it.size < n) it + (cloze.drop(n - 4)).take(n - it.size) else it }
        return Session(SessionKind.GRAMMAR, title, picked.take(n).map { factory.question(it) }.toMutableList(), gp = gp)
    }

    /**
     * A balanced review of all twelve tense points, exposed from the Grammar tab.
     *
     * The session includes at least ten questions from every grammar exercise
     * family used by the tense bank (E02, E04, E05 and E12). Twelve coverage
     * questions are retained so every tense remains represented, therefore the
     * minimum session size is 42 rather than 40.
     */
    fun tensePractice(title: String, n: Int = 42): Session {
        val tenseIds = listOf(
            "present_simple", "present_continuous", "present_perfect", "present_perfect_continuous",
            "past_simple", "past_continuous", "past_perfect", "past_perfect_continuous",
            "future", "future_continuous", "future_perfect", "future_perfect_continuous",
        )
        val pool = tenseIds.flatMap { content.questionsByPoint[it].orEmpty() }
        val picked = linkedMapOf<String, Question>()
        val counts = mutableMapOf<String, Int>()
        fun add(question: Question) {
            if (picked.putIfAbsent(question.id, question) == null) {
                counts[question.type] = (counts[question.type] ?: 0) + 1
            }
        }

        // Keep every tense visible even when the exercise families are unevenly sized.
        tenseIds.forEach { id ->
            content.questionsByPoint[id].orEmpty()
                .firstOrNull { it.type == "E02" }
                ?.let(::add)
                ?: content.questionsByPoint[id].orEmpty().firstOrNull()?.let(::add)
        }

        // Fill each exercise family independently. The generated bank currently
        // has enough source questions for all four minimums.
        listOf("E02", "E04", "E05", "E12").forEach { type ->
            pool.filter { it.type == type }.shuffled(random).forEach { question ->
                if ((counts[type] ?: 0) < 10) add(question)
            }
        }

        // Keep the optional n parameter useful for larger sessions, but never
        // allow callers to create a session below the per-family requirement.
        val target = maxOf(n, 42)
        pool.filterNot { picked.containsKey(it.id) }.shuffled(random).forEach { question ->
            if (picked.size < target) add(question)
        }
        return Session(SessionKind.TENSES, title, picked.values.map { factory.question(it) }.toMutableList())
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
        val ex = if (fmt == "toeic") {
            val bank = content.examBanks["toeic"]
            val selected = if (qtype == "p6") {
                bank?.groups.orEmpty().flatMap { group -> group.items.map { it to group } }
            } else {
                bank?.items.orEmpty().filter { qtype == null || it.section == qtype }.map { it to null }
            }
            selected.filter { (item, _) -> when (difficulty) { 0 -> item.level <= 2; 1 -> item.level == 3; 2 -> item.level >= 4; else -> true } }
                .ifEmpty { selected }.shuffled(random).take(n).map { (item, group) -> factory.exam("toeic", item, group) }
        } else if (fmt == "toeic_p6") {
            content.passages.flatMap { p -> p.blanks.indices.filter { qtype == null || p.blanks[it].qtype == qtype }.map { p to it } }
                .sortedBy { (p, i) -> if (state(p.key(i))?.isNew != false) 0 else 1 }.take(n * 2).shuffled(random).take(n)
                .sortedWith(compareBy({ it.first.id }, { it.second })).map { (p, i) -> factory.passageBlank(p, i) }
        } else if (fmt.startsWith("toeic_p1_practice_")) {
            val set = fmt.substringAfterLast('_')
            val selected = content.examBanks["toeic_p1"]?.groups.orEmpty()
                .filter { it.set == set }
                .flatMap { group -> group.items.filter { qtype == null || it.qtype == qtype }.map { it to group } }
            selected.shuffled(random).take(n).map { (item, group) -> factory.exam("toeic_p1", item, group) }
        } else if (fmt.startsWith("toeic_p2_practice_")) {
            val set = fmt.substringAfterLast('_')
            val selected = content.examBanks["toeic_p2"]?.groups.orEmpty()
                .filter { it.set == set }
                .flatMap { group -> group.items.filter { qtype == null || it.qtype == qtype }.map { it to group } }
            selected.shuffled(random).take(n).map { (item, group) -> factory.exam("toeic_p2", item, group) }
        } else if (fmt == "toeic_p3_examples" || fmt == "toeic_p4_examples") {
            val bankId = if (fmt == "toeic_p3_examples") "toeic_p3" else "toeic_p4"
            val selected = content.examBanks[bankId]?.groups.orEmpty()
                .filter { it.set == "examples" }
                .flatMap { group -> group.items.filter { qtype == null || it.qtype == qtype }.map { it to group } }
            selected.shuffled(random).take(n).map { (item, group) -> factory.exam(bankId, item, group) }
        } else if (fmt.startsWith("toeic_p7_practice_")) {
            val set = fmt.substringAfterLast('_')
            val selected = content.examBanks["toeic_p7"]?.groups.orEmpty()
                .filter { it.set == set }
                .flatMap { group -> group.items.filter { qtype == null || it.qtype == qtype }.map { it to group } }
            selected.shuffled(random).take(n).map { (item, group) -> factory.exam("toeic_p7", item, group) }
        } else {
            val pool = examPool(fmt).filter { qtype == null || it.qtype == qtype }
            val byLevel = when (difficulty) { 0 -> pool.filter { it.level <= 2 }; 1 -> pool.filter { it.level == 3 }; 2 -> pool.filter { it.level >= 4 }; else -> pool }
                .ifEmpty { pool }
            // Shuffle before deterministic prioritisation; random comparator keys can be unstable.
            byLevel.shuffled(random).sortedBy { if (state(it.key)?.isNew != false) 0 else 1 }.take(n).map { factory.question(it) }
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

    /** E13 speed round: rapid multiple choice on easy items, without blocking on learning.db. */
    fun speed(title: String, userLevel: Int): Session {
        val words = speedWords(userLevel, 40)
        // The speed pool is deliberately small: distractors must not scan and shuffle the
        // complete vocabulary once per question while the user is waiting on the UI thread.
        return Session(SessionKind.SPEED, title, words.map {
            if (random.nextBoolean()) factory.meaning(it, words) else factory.reverse(it, words)
        }.toMutableList(), speed = true)
    }

    /** Selects a small speed-round pool without materialising/shuffling the whole vocabulary. */
    private fun speedWords(userLevel: Int, limit: Int): List<Word> {
        val selected = ArrayList<Word>(limit)
        val selectedIds = HashSet<String>(limit)

        // Do not load the learning database here: this method is called by a tap listener on the
        // main thread. A speed round needs only easy words, so bounded indexed sampling gives us
        // the same variety without scanning, allocating, or shuffling the complete vocabulary.
        val source = content.lessonWords
        var attempts = 0
        while (selected.size < limit && source.isNotEmpty() && attempts++ < limit * 32) {
            val word = source[random.nextInt(source.size)]
            if (word.level <= userLevel && selectedIds.add(word.id)) selected += word
        }
        if (selected.size < limit) {
            source.asSequence()
                .filter { it.level <= userLevel && selectedIds.add(it.id) }
                .take(limit - selected.size)
                .forEach { selected += it }
        }
        return selected.shuffled(random)
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
