package com.yourbrand.englishlearn.ui

import android.content.Context
import com.yourbrand.englishlearn.R

/** Human labels for skill / trap / question-type tags (TagChip). */
object TagNames {
    private val map = mapOf(
        "pos_transform" to R.string.tag_pos, "pos" to R.string.tag_pos, "word_family" to R.string.tag_pos,
        "tense" to R.string.tag_tense, "preposition" to R.string.tag_preposition, "conjunction" to R.string.tag_conjunction,
        "conj_prep" to R.string.tag_conjunction, "pronoun" to R.string.tag_pronoun, "passive" to R.string.tag_passive,
        "conditional" to R.string.tag_conditional, "relative_clause" to R.string.tag_relative, "relative" to R.string.tag_relative,
        "comparison" to R.string.tag_comparison, "article_quantifier" to R.string.tag_quantifier, "quantifier" to R.string.tag_quantifier,
        "gerund_infinitive" to R.string.tag_gerund, "gerund" to R.string.tag_gerund, "agreement" to R.string.tag_agreement,
        "collocation" to R.string.tag_collocation, "confusable" to R.string.tag_confusable, "vocab_core" to R.string.tag_vocab,
        "vocab_workplace" to R.string.tag_vocab_work, "vocab" to R.string.tag_vocab_work, "pronunciation" to R.string.tag_pronunciation,
        "stress" to R.string.tag_stress, "wrong_pos" to R.string.trap_wrong_pos, "similar_meaning" to R.string.trap_similar,
        "wrong_preposition" to R.string.trap_preposition, "wrong_tense" to R.string.trap_tense, "spelling_lookalike" to R.string.trap_lookalike,
        "linking" to R.string.tag_linking, "discourse" to R.string.tag_linking, "participle" to R.string.tag_participle,
        "subjunctive" to R.string.tag_subjunctive, "inversion" to R.string.tag_inversion, "modal" to R.string.tag_modal,
        "noun_phrase" to R.string.tag_noun_phrase, "sentence" to R.string.tag_sentence, "synonym" to R.string.tag_synonym,
        "antonym" to R.string.tag_antonym, "reported_speech" to R.string.tag_reported, "wish" to R.string.tag_wish,
        "word_order" to R.string.tag_word_order, "spelling" to R.string.tag_spelling, "passage" to R.string.tag_passage,
        "cleft" to R.string.tag_cleft, "ellipsis" to R.string.tag_ellipsis, "hedging" to R.string.tag_hedging, "register" to R.string.tag_register,
        "error" to R.string.tag_error, "transform" to R.string.tag_transform,
    )

    fun has(tag: String) = tag in map
    fun label(ctx: Context, tag: String): String = map[tag]?.let { ctx.getString(it) } ?: tag.replace('_', ' ')
}
