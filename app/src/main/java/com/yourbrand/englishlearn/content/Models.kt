package com.yourbrand.englishlearn.content

/** JSON files making up one learner locale pack under assets/content/i18n/<locale>/. */
data class LocalePackFiles(
    val topics: String,
    val words: String,
    val confusables: String,
    val grammar: String,
    val questions: String,
    val pet: String,
    val tips: String,
    val sound: String,
    val stories: String = "{}",
)

data class Topic(val id: String, val name: String, val icon: String, val hue: Int)

data class Example(val id: String, val text: String, val vi: String?, val highlight: String? = null)

data class Sense(
    val id: String,
    val pos: String,
    val definition: String,
    val register: String,
    val gloss: String,
    val examples: List<Example>,
)

data class Word(
    val id: String,
    val lemma: String,
    val ipa: String,
    val level: Int,
    val topics: List<String>,
    val senses: List<Sense>,
    val family: Map<String, String>,
    val collocations: List<String>,
    val confusables: List<String>,
    val tip: String?,
    val tier: String = "bronze",
    val forms: Map<String, String> = emptyMap(),
    val grammarIds: List<String> = emptyList(),
    val image: String? = null,
    val adult: Boolean = false,
) {
    val pos: String get() = senses.first().pos
    val gloss: String get() = senses.first().gloss
    val key: String get() = "w:$id"
    /** All meanings in one line, e.g. "(n) ngân sách · (v) lập ngân sách". */
    val allGlosses: String get() = senses.joinToString(" · ") { "(${it.pos}) ${it.gloss}" }
}

data class Confusable(val id: String, val words: List<String>, val wordIds: List<String?>, val tip: String, val notes: Map<String, String>)

data class GExample(val en: String, val vi: String, val highlight: String)
data class Mistake(val wrong: String, val right: String, val note: String)

data class GrammarPoint(
    val id: String,
    val level: Int,
    val title: String,
    val formula: String,
    val whenToUse: String,
    val body: String,
    val signals: List<String>,
    val examples: List<GExample>,
    val mistakes: List<Mistake>,
    val tags: List<String>,
    val exam: Boolean,
)

/**
 * One authored question. type: E01 (MC), E02 (cloze), E04 (find error), E05 (word order), E12 (transform).
 * fmt: grammar / vocab / toeic_p5 / school.
 */
data class Question(
    val id: String,
    val type: String,
    val fmt: String,
    val qtype: String?,
    val gp: String?,
    val level: Int,
    val topic: String?,
    val skills: List<String>,
    val trap: List<String>,
    val stem: String,
    val options: List<String>,
    val answer: Int,
    val fix: String?,
    val translation: String?,
    val notes: String? = null,
    val explanation: String,
) {
    val key: String get() = "q:$id"
    val tags: List<String> get() = (skills + trap + listOfNotNull(qtype)).distinct()
}

data class Blank(val options: List<String>, val answer: Int, val qtype: String, val explanation: String)

data class Passage(val id: String, val title: String, val text: String, val topic: String?, val level: Int, val blanks: List<Blank>) {
    fun key(blank: Int) = "p:$id:$blank"
}

data class ExamFormat(
    val id: String,
    val label: String,
    val family: String,
    val description: String,
    val questionCount: Int?,
    val passages: Int?,
    val timeLimitMinutes: Int?,
    val source: String?,
    val sections: List<ExamSection> = emptyList(),
)

data class ExamSection(
    val id: String,
    val label: String,
    val itemType: String,
    val count: Int,
    val exerciseTypes: List<String>,
    val qtypes: List<String>,
    val bank: String?,
)

data class ExamItem(val id: String, val section: String, val level: Int, val stem: String, val options: List<String>, val answer: Int, val explanation: String, val qtype: String? = null)
data class ExamGroup(val id: String, val section: String, val passage: String, val title: String?, val items: List<ExamItem>)
data class ExamBank(val items: List<ExamItem>, val groups: List<ExamGroup>)

data class ComingSoon(val id: String, val label: String)

data class SoundFocus(val id: String, val label: String, val tip: String)
data class MinimalPair(val id: String, val a: String, val b: String, val focus: String)
data class SoundSentence(val id: String, val text: String, val level: Int, val translation: String? = null)
data class SoundData(
    val focuses: List<SoundFocus> = emptyList(),
    val pairs: List<MinimalPair> = emptyList(),
    val dictation: List<SoundSentence> = emptyList(),
    val shadowing: List<SoundSentence> = emptyList(),
)

data class StoryQuestion(val question: String, val options: List<String>, val answer: Int, val explanation: String)
data class Story(
    val id: String,
    val topic: String,
    val level: Int,
    val title: String,
    val text: String,
    val translation: String? = null,
    val targets: List<String>,
    val questions: List<StoryQuestion>,
)
