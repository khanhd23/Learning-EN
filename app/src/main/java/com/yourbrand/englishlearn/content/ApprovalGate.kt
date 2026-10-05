package com.yourbrand.englishlearn.content

import java.security.MessageDigest
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
    val selectable: Boolean,
    val availableLevels: Set<Int>,
)

object ApprovalGate {
    private const val THRESHOLD = 0.95

    fun forEnglish(words: JSONObject, grammar: JSONObject): LocaleApproval =
        evaluate(JSONObject(), words, grammar, JSONObject())

    fun evaluate(status: JSONObject, words: JSONObject, grammar: JSONObject, questions: JSONObject): LocaleApproval {
        val entries = status.optJSONObject("entries") ?: JSONObject()
        fun rows(kind: String) = entries.optJSONObject(kind) ?: JSONObject()
        fun approved(kind: String, key: String, source: JSONObject? = null): Boolean {
            val row = rows(kind).optJSONObject(key) ?: return false
            if (row.optString("s") != "approved") return false
            val recorded = row.optString("src")
            return source == null || recorded.isBlank() || recorded == sourceHash(source)
        }
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

        fun ratio(total: List<String>, approved: Set<String>) =
            if (total.isEmpty()) 0.0 else total.count { it in approved } / total.size.toDouble()
        val approvedWords = keys("words", wordSources)
        val approvedGrammar = keys("grammar", grammarSources)
        val approvedQuestions = keys("questions", questionSources)
        val approvedPassages = keys("passages", passageSources)
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
            selectable = uiApproved && 1 in available,
            availableLevels = available,
        )
    }

    fun sourceHash(source: JSONObject): String = sha256(canonical(source)).take(12)

    private fun sha256(value: String): String = MessageDigest.getInstance("SHA-256")
        .digest(value.toByteArray(Charsets.UTF_8)).joinToString("") { "%02x".format(it) }

    /** Matches tools/locale.py canonical(): UTF-8, sorted object keys, compact separators. */
    private fun canonical(value: Any?): String = when (value) {
        null, JSONObject.NULL -> "null"
        is JSONObject -> value.keys().asSequence().toList().sorted().joinToString(prefix = "{", postfix = "}", separator = ",") {
            JSONObject.quote(it) + ":" + canonical(value.get(it))
        }
        is JSONArray -> (0 until value.length()).joinToString(prefix = "[", postfix = "]", separator = ",") { canonical(value.get(it)) }
        is String -> JSONObject.quote(value)
        is Boolean, is Number -> value.toString()
        else -> JSONObject.quote(value.toString())
    }
}
