package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.Content
import kotlin.random.Random

/**
 * "Tạo bài luyện riêng" (S12): filter content by type, topic, skill, difficulty and source, then pick
 * a weighted random set (due and weak items boosted, no repeated stems).
 */
class PracticeBuilder(private val content: Content) {
    enum class Type { PART5, PART6, VOCAB, GRAMMAR, SCHOOL }
    enum class Source { NEW, WRONG, SAVED, ALL }

    data class Filters(
        val types: Set<Type> = setOf(Type.VOCAB, Type.GRAMMAR),
        val topics: Set<String> = emptySet(),
        val skills: Set<String> = emptySet(),
        val minLevel: Int = 1,
        val maxLevel: Int = 5,
        val source: Source = Source.ALL,
    )

    class Result(val keys: List<String>, private val weight: (String) -> Double) {
        val matchCount: Int get() = keys.size

        fun pick(n: Int, random: Random, state: (String) -> ItemState?): List<String> {
            // Weighted shuffle (Efraimidis–Spirakis): key = u^(1/w).
            return keys.map { k ->
                var w = weight(k)
                val s = state(k)
                if (s != null && Scheduler.isDue(s, System.currentTimeMillis())) w *= 2
                if (s != null && s.mistake) w *= 1.5
                k to Math.pow(random.nextDouble(), 1.0 / w.coerceAtLeast(0.01))
            }.sortedByDescending { it.second }.take(n).map { it.first }
        }
    }

    /** Skills offered as chips (label key → tag). */
    val skillTags = listOf("pos_transform", "tense", "preposition", "conjunction", "pronoun", "passive", "conditional",
        "relative_clause", "comparison", "article_quantifier", "gerund_infinitive", "agreement", "collocation", "confusable", "pronunciation", "stress")

    fun query(f: Filters, state: (String) -> ItemState?): Result {
        val keys = ArrayList<String>()
        fun sourceOk(key: String): Boolean {
            val s = state(key)
            return when (f.source) {
                Source.ALL -> true
                Source.NEW -> s == null || s.isNew
                Source.WRONG -> s != null && s.wrong > 0
                Source.SAVED -> s != null && s.saved
            }
        }
        if (Type.VOCAB in f.types) {
            content.lessonWords.filter { w ->
                w.level in f.minLevel..f.maxLevel && (f.topics.isEmpty() || w.topics.any { it in f.topics }) &&
                    (f.skills.isEmpty() || "vocab_core" in f.skills || "collocation" in f.skills && w.collocations.isNotEmpty())
            }.forEach { if (sourceOk(it.key)) keys += it.key }
        }
        val qTypes = buildSet {
            if (Type.PART5 in f.types) add("toeic_p5")
            if (Type.GRAMMAR in f.types) add("grammar")
            if (Type.VOCAB in f.types) add("vocab")
            if (Type.SCHOOL in f.types) add("school")
        }
        val stems = HashSet<String>()
        content.questions.filter { q ->
            q.fmt in qTypes && q.level in f.minLevel..f.maxLevel &&
                (f.topics.isEmpty() || q.topic == null && q.fmt != "toeic_p5" || q.topic in f.topics) &&
                (f.skills.isEmpty() || q.tags.any { it in f.skills })
        }.forEach { q -> if (sourceOk(q.key) && stems.add(q.stem)) keys += q.key }
        if (Type.PART6 in f.types) {
            content.passages.filter { p -> p.level in f.minLevel..f.maxLevel && (f.topics.isEmpty() || p.topic in f.topics) }
                .forEach { p -> p.blanks.indices.forEach { i -> if (sourceOk(p.key(i))) keys += p.key(i) } }
        }
        return Result(keys) { 1.0 }
    }
}

/** A mock / mini test (S13-S14). Answers are kept until submission; state survives process death via [toJson]. */
class MockTest(
    val formatId: String,
    val title: String,
    val keys: List<String>,
    val timeLimitSec: Int?,
    val answers: IntArray = IntArray(keys.size) { -1 },
    val flags: BooleanArray = BooleanArray(keys.size),
    var elapsedSec: Int = 0,
    var current: Int = 0,
) {
    val answeredCount: Int get() = answers.count { it >= 0 }

    fun toJson(): org.json.JSONObject = org.json.JSONObject()
        .put("fmt", formatId).put("title", title).put("keys", org.json.JSONArray(keys)).put("limit", timeLimitSec ?: -1)
        .put("answers", org.json.JSONArray(answers.toList())).put("flags", org.json.JSONArray(flags.toList()))
        .put("elapsed", elapsedSec).put("current", current)

    companion object {
        fun fromJson(o: org.json.JSONObject): MockTest {
            val k = o.getJSONArray("keys")
            val keys = List(k.length()) { k.getString(it) }
            val a = o.getJSONArray("answers")
            val f = o.getJSONArray("flags")
            return MockTest(o.getString("fmt"), o.getString("title"), keys, o.optInt("limit", -1).takeIf { it > 0 },
                IntArray(keys.size) { a.optInt(it, -1) }, BooleanArray(keys.size) { f.optBoolean(it) }, o.optInt("elapsed"), o.optInt("current"))
        }

        /**
         * Builds a test for [formatId]. Part 5 is stratified by question type; Part 6 uses whole passages;
         * mini tests take [count] items. Avoids items used in the last two tests when enough content exists.
         */
        fun build(content: Content, builder: SessionBuilder, formatId: String, title: String, count: Int?, recent: Set<String>, random: Random = Random.Default): MockTest {
            val format = content.formatById(formatId)
            val n = count ?: format?.questionCount ?: 20
            val keys: List<String> = if (formatId == "toeic_p6") {
                val passages = (format?.passages ?: 4).let { pc -> if (count != null) (count + 3) / 4 else pc }
                val fresh = content.passages.filter { p -> p.blanks.indices.none { p.key(it) in recent } }
                val chosen = (if (fresh.size >= passages) fresh else content.passages).shuffled(random).take(passages)
                chosen.flatMap { p -> p.blanks.indices.map { p.key(it) } }.take(n)
            } else {
                val pool = builder.examPool(formatId).filter { it.type != "E05" }
                val fresh = pool.filter { it.key !in recent }.let { if (it.size >= n) it else pool }
                // Stratify by question type so every type appears.
                val groups = fresh.groupBy { it.qtype ?: it.type }.mapValues { it.value.shuffled(random).toMutableList() }
                val out = ArrayList<String>()
                while (out.size < n && groups.values.any { it.isNotEmpty() }) {
                    for (g in groups.values.shuffled(random)) { if (out.size < n && g.isNotEmpty()) out += g.removeAt(0).key }
                }
                out.shuffled(random)
            }
            val limit = if (count == null) format?.timeLimitMinutes?.times(60) else null
            return MockTest(formatId, title, keys, limit)
        }
    }
}
