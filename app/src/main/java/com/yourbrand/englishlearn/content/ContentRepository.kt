package com.yourbrand.englishlearn.content

import android.content.Context
import org.json.JSONException
import org.json.JSONArray
import org.json.JSONObject

/** Everything the app teaches, parsed once from assets/content (org.json, no reflection). */
class Content(
    val topics: List<Topic>,
    val words: List<Word>,
    val confusables: List<Confusable>,
    val grammar: List<GrammarPoint>,
    val questions: List<Question>,
    val passages: List<Passage>,
    val formats: List<ExamFormat>,
    val comingSoon: List<ComingSoon>,
    val petLines: Map<String, List<String>>,
    val tips: Map<String, String>,
) {
    /** All entries, including bronze dictionary-only imports. */
    val allWords: List<Word> get() = words
    /** Lesson and practice candidates; bronze entries remain searchable only. */
    val lessonWords: List<Word> = words.filter { it.tier == "silver" || it.tier == "gold" }
    val wordById = words.associateBy { it.id }
    val topicById = topics.associateBy { it.id }
    val grammarById = grammar.associateBy { it.id }
    val questionById = questions.associateBy { it.id }
    val passageById = passages.associateBy { it.id }
    val confusableById = confusables.associateBy { it.id }
    val wordsByTopic: Map<String, List<Word>> = topics.associate { t -> t.id to words.filter { t.id in it.topics } }
    val questionsByPoint: Map<String, List<Question>> = questions.filter { it.gp != null }.groupBy { it.gp!! }

    fun formatById(id: String) = formats.firstOrNull { it.id == id }
}

object ContentParser {
    private fun JSONArray?.strings(): List<String> = if (this == null) emptyList() else List(length()) { getString(it) }
    private fun JSONObject.strOrNull(k: String): String? = if (has(k) && !isNull(k)) optString(k).takeIf { it.isNotEmpty() } else null
    private inline fun <T> JSONArray?.map(f: (JSONObject) -> T): List<T> = if (this == null) emptyList() else List(length()) { f(getJSONObject(it)) }

    fun parse(words: String, grammar: String, questions: String, locale: LocalePackFiles, formats: String): Content {
        val viTopics = JSONObject(locale.topics)
        val viWords = JSONObject(locale.words)
        val viConf = JSONObject(locale.confusables)
        val viGrammar = JSONObject(locale.grammar)
        val localeQuestions = JSONObject(locale.questions)
        val viQ = localeQuestions.optJSONObject("q") ?: JSONObject()
        val viQTranslation = localeQuestions.optJSONObject("q_translation") ?: JSONObject()
        val viQFix = localeQuestions.optJSONObject("q_fix") ?: JSONObject()
        val viQNotes = localeQuestions.optJSONObject("q_notes") ?: JSONObject()
        val viP = localeQuestions.optJSONObject("passages") ?: JSONObject()

        val w = JSONObject(words)
        val topics = w.getJSONArray("topics").map { Topic(it.getString("id"), viTopics.optString(it.getString("id")), it.getString("icon"), it.getInt("hue")) }
        val wordList = w.getJSONArray("words").map { o ->
            val id = o.getString("id")
            val senses = o.getJSONArray("senses").let { arr ->
                List(arr.length()) { i ->
                    val s = arr.getJSONObject(i)
                    val sv = viWords.optJSONObject(s.getString("id")) ?: JSONObject()
                    val exVi = sv.optJSONObject("ex") ?: JSONObject()
                    Sense(s.getString("id"), s.getString("pos"), s.optString("def"), s.optString("register", "neutral"), sv.optString("g"), s.getJSONArray("ex").map { e ->
                        Example(e.getString("id"), e.getString("text"), exVi.strOrNull(e.getString("id")), e.strOrNull("hl"))
                    })
                }
            }
            val firstLocaleSense = o.getJSONArray("senses").optJSONObject(0)?.let { viWords.optJSONObject(it.getString("id")) }
            val fam = o.optJSONObject("family")
            Word(
                id = id, lemma = o.getString("lemma"), ipa = o.optString("ipa"), level = o.optInt("level", 1),
                topics = o.optJSONArray("topics").strings(), senses = senses,
                family = fam?.keys()?.asSequence()?.associateWith { fam.getString(it) }.orEmpty(),
                collocations = o.optJSONArray("coll").strings(), confusables = o.optJSONArray("conf").strings(),
                tip = firstLocaleSense?.strOrNull("tip"),
                tier = o.optString("tier", "bronze"),
                forms = o.optJSONObject("forms")?.keys()?.asSequence()?.associateWith { o.getJSONObject("forms").getString(it) }.orEmpty(),
                grammarIds = o.optJSONArray("grammarIds").strings(),
            )
        }
        val conf = w.getJSONArray("confusables").map { o ->
            val cv = viConf.optJSONObject(o.getString("id")) ?: JSONObject()
            val notes = cv.optJSONObject("notes")
            Confusable(
                o.getString("id"), o.getJSONArray("words").strings(),
                o.optJSONArray("wordIds").let { a -> if (a == null) emptyList() else List(a.length()) { a.optString(it).takeIf { s -> s.isNotEmpty() && s != "null" } } },
                cv.optString("tip"), notes?.keys()?.asSequence()?.associateWith { notes.getString(it) }.orEmpty(),
            )
        }

        val grammarList = JSONObject(grammar).getJSONArray("points").map { o ->
            val gv = viGrammar.getJSONObject(o.getString("id"))
            val exVi = gv.optJSONArray("examples").strings()
            val mVi = gv.optJSONArray("mistakes").strings()
            GrammarPoint(
                id = o.getString("id"), level = o.getInt("level"), title = gv.getString("title"), formula = o.optString("formula"),
                whenToUse = gv.optString("when"), body = gv.optString("body"), signals = o.optJSONArray("signals").strings(),
                examples = o.getJSONArray("examples").map { it }.mapIndexed { i, e -> GExample(e.getString("en"), exVi.getOrElse(i) { "" }, e.optString("hl")) },
                mistakes = o.getJSONArray("mistakes").map { it }.mapIndexed { i, m -> Mistake(m.getString("wrong"), m.getString("right"), mVi.getOrElse(i) { "" }) },
                tags = o.optJSONArray("tags").strings(), exam = o.optBoolean("exam"),
            )
        }

        val q = JSONObject(questions)
        val questionList = q.getJSONArray("questions").map { o ->
            val id = o.getString("id")
            Question(
                id = id, type = o.getString("type"), fmt = o.optString("fmt", "grammar"), qtype = o.strOrNull("qtype"),
                gp = o.strOrNull("gp"), level = o.optInt("level", 2), topic = o.strOrNull("topic"),
                skills = o.optJSONArray("skills").strings(), trap = o.optJSONArray("trap").strings(),
                stem = o.optString("stem"), options = o.optJSONArray("opts").strings(), answer = o.optInt("ans", 0),
                fix = viQFix.strOrNull(id) ?: o.strOrNull("fix"),
                translation = viQTranslation.strOrNull(id) ?: o.strOrNull("vi"),
                notes = viQNotes.strOrNull(id), explanation = viQ.optString(id),
            )
        }
        val passageList = q.getJSONArray("passages").map { o ->
            val pv = viP.optJSONObject(o.getString("id"))?.optJSONArray("blanks").strings()
            Passage(
                o.getString("id"), o.getString("title"), o.getString("text"), o.strOrNull("topic"), o.optInt("level", 3),
                o.getJSONArray("blanks").map { it }.mapIndexed { i, b -> Blank(b.getJSONArray("opts").strings(), b.getInt("ans"), b.optString("qtype"), pv.getOrElse(i) { "" }) },
            )
        }

        val f = JSONObject(formats)
        val formatList = f.getJSONArray("formats").map { o ->
            ExamFormat(
                o.getString("id"), o.getString("label"), o.optString("family"), o.optString("description"),
                if (o.isNull("questionCount")) null else o.optInt("questionCount"),
                if (!o.has("passages") || o.isNull("passages")) null else o.optInt("passages"),
                if (o.isNull("timeLimitMinutes")) null else o.optInt("timeLimitMinutes"),
                o.strOrNull("source"),
            )
        }
        val soon = f.optJSONArray("comingSoon").map { ComingSoon(it.getString("id"), it.getString("label")) }

        val petObj = JSONObject(locale.pet)
        val pet = petObj.keys().asSequence().associateWith { petObj.getJSONArray(it).strings() }
        val tipsObj = JSONObject(locale.tips)
        val tips = tipsObj.keys().asSequence().associateWith { tipsObj.getString(it) }

        return Content(topics, wordList, conf, grammarList, questionList, passageList, formatList, soon, pet, tips)
    }
}

class ContentRepository(private val context: Context) {
    private val cached = mutableMapOf<String, Content>()

    private fun asset(name: String) = context.assets.open("content/$name").bufferedReader().use { it.readText() }

    private fun localePack(locale: String): String {
        val candidate = locale.trim().replace('_', '-')
        if (candidate.isBlank() || candidate == "vi") return "i18n/vi"
        val candidates = listOf(candidate, candidate.substringBefore('-'))
        for (code in candidates.distinct()) {
            val path = "i18n/$code"
            runCatching {
                val meta = JSONObject(asset("$path/status.json"))
                val complete = meta.optString("status") == "complete" && !meta.optBoolean("todo", true)
                if (complete) return path
            }
        }
        return "i18n/vi"
    }

    /** Locale packs that are actually bundled in this build and pass the release gate. */
    fun shippableLocales(): List<String> = synchronized(this) {
        context.assets.list("content/i18n").orEmpty().asSequence()
            .filter { it != "market_profiles.json" && context.assets.list("content/i18n/$it").orEmpty().contains("status.json") }
            .filter { it == "vi" || localePack(it) == "i18n/$it" }
            .distinct()
            .sortedWith(compareBy<String> { if (it == "vi") 0 else 1 }.thenBy { it })
            .toList()
    }

    fun effectiveLocale(locale: String): String = localePack(locale).removePrefix("i18n/")

    /** Parses lazily (first call happens on a background prewarm thread at startup). */
    fun get(locale: String = "vi"): Content = synchronized(this) {
        val key = localePack(locale)
        cached[key] ?: ContentParser.parse(
            asset("en/words.json"),
            asset("en/grammar.json"),
            asset("en/questions.json"),
            localeFiles(key),
            asset("exam_formats.json"),
        ).also { cached[key] = it }
    }

    /** True only for a locale pack that has passed the content-release gate. */
    fun isShippable(locale: String): Boolean = locale == "vi" || localePack(locale) != "i18n/vi"

    private fun localeFiles(path: String) = LocalePackFiles(
        topics = asset("$path/topics.json"),
        words = asset("$path/words.json"),
        confusables = asset("$path/confusables.json"),
        grammar = asset("$path/grammar.json"),
        questions = asset("$path/questions.json"),
        pet = asset("$path/pet.json"),
        tips = asset("$path/tips.json"),
    )
}
