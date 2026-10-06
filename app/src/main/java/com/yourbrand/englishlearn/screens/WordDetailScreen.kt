package com.yourbrand.englishlearn.screens

import android.content.Intent
import android.net.Uri
import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yourbrand.englishlearn.BuildConfig
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.content.Word
import com.yourbrand.englishlearn.ui.*
import com.yourbrand.englishlearn.ui.views.Bar

/** S06 — word detail with every meaning in context. */
class WordDetailScreen(activity: MainActivity, private val wordId: String) : ScrollScreen(activity) {
    override val petMode = PetMode.FLOATING
    private val word: Word? get() = services.content.wordById[wordId]
    override val barTitle: String get() = str(R.string.word_detail)
    private var showVi = services.settings.showTranslation

    override fun build(body: LinearLayout) {
        val c = ctx
        val w = word ?: return
        val st = services.store.item(w.key)

        // Lemma + IPA + speakers
        val head = Kit.hbox(c).margins(c, top = 4)
        val left = Kit.vbox(c) { layoutParams = lp(0, WRAP_CONTENT, 1f) }
        left.addView(Kit.text(c, w.lemma, R.style.Text_Display, sizeSp = 34f))
        left.addView(Kit.text(c, "/${w.ipa}/", R.style.Text_Body, c.col(R.color.muted)))
        head.addView(left)
        w.image?.let { name ->
            val id = c.resources.getIdentifier(name, "drawable", c.packageName)
            if (id != 0) head.addView(ImageView(c).apply {
                setImageResource(id)
                contentDescription = w.lemma
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                layoutParams = LinearLayout.LayoutParams(c.dpi(64), c.dpi(64)).apply { marginStart = c.dpi(8) }
            })
        }
        head.addView(speakerBtn(R.drawable.ic_volume, str(R.string.listen)) { services.tts.speak(w.lemma) })
        head.addView(speakerBtn(R.drawable.ic_timer, str(R.string.listen_slow)) { services.tts.speak(w.lemma, slower = true) }.apply { (layoutParams as LinearLayout.LayoutParams).marginStart = c.dpi(8) })
        body.addView(head)

        // Chips: level + topics
        val chips = Kit.flow(c).margins(c, top = 10)
        chips.addView(Kit.chip(c, str(R.string.level_n, w.level), true))
        w.topics.mapNotNull { services.content.topicById[it] }.forEach { t -> chips.addView(Kit.chip(c, t.icon + " " + t.name, false, Hues.color(c, t.hue)) { activity.open(WordListScreen(activity, topicId = t.id)) }) }
        body.addView(chips)

        // Meanings (each sense with its own example in context)
        body.addView(Kit.text(c, str(R.string.meanings, w.senses.size), R.style.Text_Section).margins(c, top = 16))
        val hasTranslation = w.senses.any { s -> s.examples.any { it.vi != null } }
        w.senses.forEachIndexed { i, s ->
            body.addView(Kit.card(c, 16, 10) {
                val r = Kit.hbox(c)
                r.addView(Kit.badge(c, "${i + 1}", c.col(R.color.primary_container), c.col(R.color.primary)))
                r.addView(Kit.text(c, posLabel(s.pos), R.style.Text_Caption, c.col(R.color.primary)).apply { layoutParams = LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(8) } })
                // Translation toggle sits inside the first meaning card, top right.
                if (i == 0 && hasTranslation) r.addView(Kit.chip(c, str(R.string.show_translation), showVi) { showVi = !showVi; services.settings.showTranslation = showVi; refresh() }
                    .apply { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) })
                addView(r)
                addView(Kit.text(c, s.gloss, R.style.Text_Title, sizeSp = 20f).margins(c, top = 6))
                s.examples.forEach { e ->
                    val ex = Kit.hbox(c) { gravity = Gravity.TOP }.margins(c, top = 10)
                    val tx = Kit.vbox(c) { layoutParams = lp(0, WRAP_CONTENT, 1f) }
                    tx.addView(Kit.text(c, Spans.highlight(e.text, w.lemma, c.col(R.color.primary), c.col(R.color.primary_container)), R.style.Text_Body))
                    if (showVi && e.vi != null) tx.addView(Kit.text(c, e.vi, R.style.Text_Caption).margins(c, top = 2))
                    ex.addView(tx)
                    ex.addView(ImageView(c).apply {
                        setImageResource(R.drawable.ic_volume); tintRes(R.color.muted)
                        setBackgroundResource(R.drawable.ripple_circle); val p = c.dpi(10); setPadding(p, p, p, p)
                        layoutParams = LinearLayout.LayoutParams(c.dpi(44), c.dpi(44))
                        contentDescription = str(R.string.listen)
                        setOnClickListener { services.tts.speak(e.text); animateSpeak(this) }
                    })
                    addView(ex)
                }
            })
        }

        // Word family
        if (w.family.isNotEmpty()) {
            body.addView(Kit.section(c, str(R.string.word_family)))
            val flow = Kit.flow(c)
            flow.addView(Kit.chip(c, "${w.lemma} (${w.pos})", true))
            w.family.forEach { (k, v) ->
                val pos = k.trimEnd('2')
                val target = services.content.words.firstOrNull { it.lemma.equals(v, true) }
                flow.addView(Kit.chip(c, "$v ($pos)", false, Hues.color(c, when (pos) { "noun" -> 2; "verb" -> 7; "adj" -> 5; else -> 3 })) { target?.let { activity.open(WordDetailScreen(activity, it.id)) } ?: services.tts.speak(v) })
            }
            body.addView(flow)
        }

        // Confusables
        if (w.confusables.isNotEmpty()) {
            body.addView(Kit.section(c, str(R.string.confused_with)))
            w.confusables.mapNotNull { services.content.confusableById[it] }.forEach { cf ->
                body.addView(Kit.clickableCard(c, 14, 8, c.col(R.color.warning_container), onClick = { activity.open(ConfusableScreen(activity, cf.id)) }) {
                    addView(Kit.text(c, cf.words.joinToString("  vs  "), R.style.Text_BodyStrong))
                    addView(Kit.ellipsize(Kit.text(c, cf.tip, R.style.Text_Caption), 2).margins(c, top = 2))
                })
            }
        }

        // Collocations
        if (w.collocations.isNotEmpty()) {
            body.addView(Kit.section(c, str(R.string.collocations)))
            val flow = Kit.flow(c)
            w.collocations.forEach { col -> flow.addView(Kit.chip(c, col) { services.tts.speak(col) }) }
            body.addView(flow)
        }

        // Tip
        w.tip?.let { tip ->
            body.addView(Kit.card(c, 16, 16, c.col(R.color.accent_container), null) {
                addView(Kit.text(c, "💡 " + str(R.string.memory_tip), R.style.Text_BodyStrong))
                addView(Kit.text(c, tip, R.style.Text_Body).margins(c, top = 4))
            })
        }

        // Mastery
        body.addView(Kit.section(c, str(R.string.mastery)))
        val box = st?.box ?: 0
        body.addView(Kit.card(c, 16, 4) {
            addView(Kit.text(c, str(when { st == null || st.isNew -> R.string.mastery_new; st.mastered -> R.string.mastery_known; else -> R.string.mastery_learning }), R.style.Text_BodyStrong))
            if (st != null && st.seen > 0) addView(Kit.text(c, str(R.string.mastery_stats, st.correct, st.seen), R.style.Text_Caption))
            addView(Bar(c).apply { layoutParams = lp(h = c.dpi(8)).apply { topMargin = c.dpi(10) }; post { set(box / 6f) } })
        })

        // Actions
        val saved = st?.saved == true
        val actions = Kit.hbox(c).margins(c, top = 20)
        actions.addView(Kit.secondary(c, if (saved) "🔖 " + str(R.string.saved) else "🔖 " + str(R.string.save), 0) { services.store.toggleSaved(w.key); refresh() }.apply { layoutParams = lp(0, c.dpi(52), 1f) })
        actions.addView(Kit.secondary(c, "🚩 " + str(R.string.report), 0) {
            val uri = Uri.parse("mailto:${BuildConfig.SUPPORT_EMAIL}?subject=" + Uri.encode(str(R.string.report_subject, w.key)))
            runCatching { activity.startActivity(Intent(Intent.ACTION_SENDTO, uri)) }
        }.apply { layoutParams = lp(0, c.dpi(52), 1f).apply { marginStart = c.dpi(10) } })
        body.addView(actions)
        body.addView(Kit.primary(c, str(R.string.practice_this_word), 12) { activity.startSession(services.builder().singleWord(w.lemma, w)) }.apply { tag = "petAvoid" })
    }

    private fun posLabel(pos: String) = str(when (pos) {
        "n" -> R.string.pos_n; "v" -> R.string.pos_v; "adj" -> R.string.pos_adj; "adv" -> R.string.pos_adv
        "prep" -> R.string.pos_prep; "conj" -> R.string.pos_conj; "idiom" -> R.string.pos_idiom; else -> R.string.pos_phr
    })

    private fun speakerBtn(icon: Int, cd: String, onClick: () -> Unit) = ImageView(ctx).apply {
        setImageResource(icon); tintRes(R.color.primary)
        background = ctx.rounded(ctx.col(R.color.primary_container), 100f, ripple = true)
        val p = ctx.dpi(12); setPadding(p, p, p, p)
        layoutParams = LinearLayout.LayoutParams(ctx.dpi(52), ctx.dpi(52))
        contentDescription = cd
        setOnClickListener { onClick(); animateSpeak(this) }
    }

    private fun animateSpeak(v: android.view.View) {
        if (ctx.reduceMotion) return
        v.animate().scaleX(1.15f).scaleY(1.15f).setDuration(150).withEndAction { v.animate().scaleX(1f).scaleY(1f).setDuration(200).start() }.start()
    }
}

/** S07 — side-by-side comparison of confusable words + "Luyện bộ này". */
class ConfusableScreen(activity: MainActivity, private val setId: String) : ScrollScreen(activity) {
    override val petMode = PetMode.FLOATING
    override val barTitle: String get() = str(R.string.confusables)

    override fun build(body: LinearLayout) {
        val c = ctx
        val cf = services.content.confusableById[setId] ?: return
        body.addView(Kit.text(c, cf.words.joinToString("  vs  "), R.style.Text_Display).margins(c, top = 4))
        val row = Kit.hbox(c) { gravity = Gravity.TOP }.margins(c, top = 12)
        cf.words.forEachIndexed { i, wd ->
            row.addView(Kit.card(c, 14, 0, Hues.container(c, i * 3 + 1), null) {
                layoutParams = lp(0, WRAP_CONTENT, 1f).apply { if (i > 0) marginStart = c.dpi(10) }
                addView(Kit.text(c, wd, R.style.Text_Title, Hues.color(c, i * 3 + 1)))
                cf.notes[wd]?.let { addView(Kit.text(c, it, R.style.Text_Body).apply { textSize = 15f }.margins(c, top = 6)) }
                val wid = cf.wordIds.getOrNull(i)
                if (wid != null) addView(Kit.text(c, str(R.string.see_word), R.style.Text_Caption, c.col(R.color.primary)).margins(c, top = 8).apply { setOnClickListener { activity.open(WordDetailScreen(activity, wid)) } })
            })
        }
        body.addView(row)
        body.addView(Kit.card(c, 16, 16, c.col(R.color.accent_container), null) {
            addView(Kit.text(c, "💡 " + str(R.string.tell_apart), R.style.Text_BodyStrong))
            addView(Kit.text(c, cf.tip, R.style.Text_Body).margins(c, top = 4))
        })
        val s = services.builder().confusable(cf.words.joinToString(" / "), cf.id)
        if (s.exercises.isNotEmpty()) body.addView(Kit.primary(c, str(R.string.practice_set, s.exercises.size), 20) { activity.startSession(services.builder().confusable(cf.words.joinToString(" / "), cf.id)) })
    }
}
