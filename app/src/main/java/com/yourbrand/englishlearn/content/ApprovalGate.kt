package com.yourbrand.englishlearn.content

import org.json.JSONArray
import org.json.JSONObject

/** Release rules for learner locale packs. No locale data is trusted without an approved status row. */
data class LocaleApproval(
    val approvedTopics: Set<String>,
    val approvedWords: Set<String>,
    val approvedConfusables: Set<String>,
    val approvedGrammar: Set<String>,
    val approvedQuestions: Set<String>,
    val approvedPassages: Set<String>,
    val approvedPet: Set<String>,
    val approvedTips: Set<String>,
    val approvedSound: Set<String>,
    val approvedStories: Set<String> = emptySet(),
    val approvedExams: Set<String> = emptySet(),
    val selectable: Boolean,
    val availableLevels: Set<Int>,
)

object ApprovalGate {
    private const val THRESHOLD = 0.95

    fun forEnglish(words: JSONObject, grammar: JSONObject): LocaleApproval =
        evaluate(JSONObject(), words, grammar, JSONObject())

    fun evaluate(status: JSONObject, words: JSONObject, grammar: JSONObject, questions: JSONObject, stories: JSONObject = JSONObject()): LocaleApproval {
        val entries = status.optJSONObject("entries") ?: JSONObject()
        fun rows(kind: String) = entries.optJSONObject(kind) ?: JSONObject()
        // Staleness (`src` no longer matching the English source) is rejected at build time by
        // tools/validate_content.py, so a shipped pack never contains stale approved rows. The app
        // does not re-hash: Android's org.json escapes "/" differently from Python's json, which
        // would wrongly reject every entry containing a slash (most grammar formulas).
        @Suppress("UNUSED_PARAMETER")
        fun approved(kind: String, key: String, source: JSONObject? = null): Boolean =
            rows(kind).optJSONObject(key)?.optString("s") == "approved"
        fun keys(kind: String, sources: Map<String, JSONObject>? = null): Set<String> =
            (sources?.keys ?: rows(kind).keys().asSequence().toSet()).filterTo(linkedSetOf()) { key ->
                approved(kind, key, sources?.get(key))
            }

        val wordSources = linkedMapOf<String, JSONObject>()
        val wordsArray = words.optJSONArray("words") ?: JSONArray()
        val wordLevels = linkedMapOf<Int, MutableList<String>>()
        for (i in 0 until wordsArray.length()) {
            val word = wordsArray.getJSONObject(i)
            val level = word.optInt("level", 1)
            val senseArray = word.optJSONArray("senses") ?: JSONArray()
            val first = senseArray.optJSONObject(0)
            if (first != null) {
                val id = first.getString("id")
                wordSources[id] = first
                wordLevels.getOrPut(level) { mutableListOf() }.add(id)
            }
        }

        val grammarSources = linkedMapOf<String, JSONObject>()
        val grammarLevels = linkedMapOf<Int, MutableList<String>>()
        val grammarArray = grammar.optJSONArray("points") ?: JSONArray()
        for (i in 0 until grammarArray.length()) {
            val item = grammarArray.getJSONObject(i)
            val id = item.getString("id")
            grammarSources[id] = item
            grammarLevels.getOrPut(item.optInt("level", 1)) { mutableListOf() }.add(id)
        }

        val questionSources = linkedMapOf<String, JSONObject>()
        val questionArray = questions.optJSONArray("questions") ?: JSONArray()
        val questionLevels = linkedMapOf<Int, MutableList<String>>()
        for (i in 0 until questionArray.length()) {
            val item = questionArray.getJSONObject(i)
            val id = item.getString("id")
            questionSources[id] = item
            questionLevels.getOrPut(item.optInt("level", 1)) { mutableListOf() }.add(id)
        }
        val passageSources = linkedMapOf<String, JSONObject>()
        val passageArray = questions.optJSONArray("passages") ?: JSONArray()
        for (i in 0 until passageArray.length()) {
            val item = passageArray.getJSONObject(i)
            passageSources[item.getString("id")] = item
        }
        val storySources = linkedMapOf<String, JSONObject>()
        stories.optJSONArray("stories")?.let { array -> for (i in 0 until array.length()) { val item = array.getJSONObject(i); storySources[item.getString("id")] = item } }

        fun ratio(total: List<String>, approved: Set<String>) =
            if (total.isEmpty()) 0.0 else total.count { it in approved } / total.size.toDouble()
        val approvedWords = keys("words", wordSources)
        val approvedGrammar = keys("grammar", grammarSources)
        val approvedQuestions = keys("questions", questionSources)
        val approvedPassages = keys("passages", passageSources)
        val approvedStories = keys("stories", storySources)
        val approvedExams = rows("exams").keys().asSequence().filter { approved("exams", it) }.toSet()
        val available = (1..5).filter { level ->
            ratio(wordLevels[level].orEmpty(), approvedWords) >= THRESHOLD &&
                ratio(grammarLevels[level].orEmpty(), approvedGrammar) >= THRESHOLD
        }.toSet()
        val ui = rows("ui")
        val uiApproved = ui.length() > 0 && ui.keys().asSequence().all { key -> approved("ui", key) }
        return LocaleApproval(
            approvedTopics = keys("topics"), approvedWords = approvedWords,
            approvedConfusables = keys("confusables"), approvedGrammar = approvedGrammar,
            approvedQuestions = approvedQuestions, approvedPassages = approvedPassages,
            approvedPet = keys("pet"), approvedTips = keys("tips"),
            approvedSound = keys("sound"),
            approvedStories = approvedStories,
            approvedExams = approvedExams,
            selectable = uiApproved && 1 in available,
            availableLevels = available,
        )
    }

}
