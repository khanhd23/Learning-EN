package com.yourbrand.englishlearn.screens

import android.view.Gravity
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.content.ExamFormat
import com.yourbrand.englishlearn.learning.MockTest
import com.yourbrand.englishlearn.learning.SessionKind
import com.yourbrand.englishlearn.ui.*
import java.text.DateFormat
import java.util.Date

/** S10 — Luyện thi hub. */
class ExamScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val tab = Tab.EXAM
    override val allowsBanner = true
    override val petMode = PetMode.FLOATING

    override fun headerActions(bar: LinearLayout) = HubHeader.build(this, bar, str(R.string.tab_exam))

    private var formatId: String
        get() = services.settings.examGoal?.takeIf { id -> services.content.formatById(id) != null } ?: if ("toeic" in services.settings.goals) "toeic_p5" else "school"
        set(v) { services.settings.examGoal = v }

    override fun build(body: LinearLayout) {
        val c = ctx
        val content = services.content
        val fmt = content.formatById(formatId) ?: content.formats.first()

        // 1. Choose exam / format
        body.addView(Kit.text(c, str(R.string.choose_format), R.style.Text_Caption).margins(c, top = 4))
        val row = Kit.hbox(c) { gravity = Gravity.TOP }
        content.formats.forEach { f ->
            val sel = f.id == fmt.id
            row.addView(Kit.clickableCard(c, 14, 8, if (sel) c.col(R.color.primary_container) else c.col(R.color.surface), onClick = { formatId = f.id; refresh() }) {
                layoutParams = LinearLayout.LayoutParams(c.dpi(178), WRAP_CONTENT).apply { marginEnd = c.dpi(10) }
                minimumHeight = c.dpi(118)
                if (sel) background = c.rounded(c.col(R.color.primary_container), 16f, c.col(R.color.primary), 2f)
                addView(Kit.text(c, f.family, R.style.Text_Caption, c.col(R.color.primary)))
                addView(Kit.text(c, f.label, R.style.Text_BodyStrong).margins(c, top = 2))
                addView(Kit.ellipsize(Kit.text(c, f.questionCount?.let { str(R.string.n_questions, it) } ?: str(R.string.generic_test), R.style.Text_Caption), 2).margins(c, top = 4))
            })
        }
        content.comingSoon.forEach { s ->
            row.addView(Kit.card(c, 14, 8, c.col(R.color.surface_variant), null) {
                layoutParams = LinearLayout.LayoutParams(c.dpi(150), WRAP_CONTENT).apply { marginEnd = c.dpi(10) }
                minimumHeight = c.dpi(118)
                alpha = 0.7f
                addView(Kit.text(c, s.label, R.style.Text_BodyStrong))
                addView(Kit.text(c, str(R.string.coming_soon), R.style.Text_Caption).margins(c, top = 4))
            })
        }
        body.addView(Kit.hscroll(c, row))
        body.addView(Kit.text(c, fmt.description, R.style.Text_Body).apply { textSize = 14f }.margins(c, top = 8))

        if (content.sound.dictation.isNotEmpty() || content.sound.pairs.isNotEmpty()) {
            body.addView(Kit.clickableCard(c, 6, 10, c.col(R.color.primary_container), onClick = {
                activity.startSession(services.builder().sound(str(R.string.sound_practice)))
            }) {
                addView(Kit.row(c, Kit.emojiTile(c, "🔊", c.col(R.color.primary_container)), str(R.string.sound_practice), str(R.string.sound_practice_desc), Kit.chevron(c)))
            })
        }

        // 2. Practice by question type
        body.addView(Kit.section(c, str(R.string.by_type)))
        body.addView(Kit.clickableCard(c, 6, 4, onClick = { activity.open(QuestionTypeScreen(activity, fmt.id)) }) {
            addView(Kit.row(c, Kit.emojiTile(c, "🧩", c.col(R.color.info_container)), str(R.string.by_type_title), str(R.string.by_type_sub), Kit.chevron(c)))
        })

        // 3. Practice builder
        body.addView(Kit.clickableCard(c, 6, 10, onClick = { activity.open(BuilderScreen(activity)) }) {
            addView(Kit.row(c, Kit.emojiTile(c, "🛠️", c.col(R.color.accent_container)), str(R.string.builder_title), str(R.string.builder_sub), Kit.chevron(c)))
        })

        // 4. Mini & mock tests
        body.addView(Kit.section(c, str(R.string.tests)))
        val hist = services.store.history().filter { it.kind == "MOCK" && it.format == fmt.id }
        testCard(body, "⏱️", str(R.string.mini_test, 10), fmt, 10, hist.filter { it.total == 10 })
        testCard(body, "📝", str(R.string.mini_test, 20), fmt, 20, hist.filter { it.total == 20 })
        testCard(body, "🏁", if (fmt.questionCount != null) str(R.string.part_test, fmt.label) else str(R.string.generic_test), fmt, null, hist.filter { it.total != 10 && it.total != 20 })

        // 5. Mistake book
        val mistakes = services.store.allItems().count { it.mistake }
        body.addView(Kit.section(c, str(R.string.mistake_book)))
        body.addView(Kit.clickableCard(c, 6, 4, onClick = { activity.open(MistakeBookScreen(activity)) }) {
            addView(Kit.row(c, Kit.emojiTile(c, "📕", c.col(R.color.error_container)), str(R.string.mistake_book), str(R.string.n_items, mistakes), Kit.chevron(c)))
        })

        // 6. History
        val recent = services.store.history().filter { it.kind == "MOCK" || it.kind == "EXAM_TYPE" || it.kind == "BUILDER" }.takeLast(10).reversed()
        if (recent.isNotEmpty()) {
            body.addView(Kit.section(c, str(R.string.history)))
            val card = Kit.card(c, 4, 4)
            val df = DateFormat.getDateInstance(DateFormat.SHORT)
            recent.forEachIndexed { i, h ->
                if (i > 0) card.addView(Kit.divider(c))
                card.addView(Kit.row(c, null, h.title, "${df.format(Date(h.at))} · ${h.seconds / 60}:${"%02d".format(h.seconds % 60)}",
                    Kit.badge(c, "${h.correct}/${h.total}", c.col(if (h.percent >= 80) R.color.success_container else if (h.percent >= 60) R.color.accent_container else R.color.error_container), c.col(R.color.on_surface))))
            }
            body.addView(card)
        }

        body.addView(Kit.text(c, str(R.string.disclaimer), R.style.Text_Caption).apply { textSize = 11f; gravity = Gravity.CENTER }.margins(c, top = 24))
    }

    private fun testCard(body: LinearLayout, emoji: String, title: String, fmt: ExamFormat, count: Int?, hist: List<com.yourbrand.englishlearn.learning.HistoryEntry>) {
        val c = ctx
        val n = count ?: fmt.questionCount ?: 20
        val last = hist.lastOrNull()?.let { "${it.correct}/${it.total}" } ?: "—"
        val best = hist.maxByOrNull { it.percent }?.let { "${it.correct}/${it.total}" } ?: "—"
        val time = fmt.timeLimitMinutes?.let { str(R.string.minutes_n, it) } ?: str(R.string.untimed)
        body.addView(Kit.clickableCard(c, 6, 10, onClick = { start(fmt, title, count) }) {
            addView(Kit.row(c, Kit.emojiTile(c, emoji, c.col(R.color.primary_container)), title, str(R.string.test_meta, n, time, last, best), Kit.chevron(c)))
        })
    }

    private fun start(fmt: ExamFormat, title: String, count: Int?) {
        if (services.settings.mockInProgress != null) {
            Dialogs.confirm(ctx, str(R.string.mock_in_progress), str(R.string.mock_in_progress_msg), str(R.string.continue_label), str(R.string.start_new),
                onNegative = { services.settings.mockInProgress = null; start(fmt, title, count) }) { MockScreen.resume(activity) }
            return
        }
        val test = MockTest.build(services.content, services.builder(), fmt.id, title, count, services.settings.recentMockKeys.toSet())
        if (test.keys.isEmpty()) { activity.toast(str(R.string.nothing_to_practice)); return }
        activity.open(MockScreen(activity, test))
    }
}

/** S11 — practice by question type (with tips card and difficulty picker). */
class QuestionTypeScreen(activity: MainActivity, private val fmt: String) : ScrollScreen(activity) {
    override val petMode = PetMode.FLOATING
    override val barTitle: String get() = services.content.formatById(fmt)?.label ?: str(R.string.by_type_title)
    private var difficulty = 3

    override fun build(body: LinearLayout) {
        val c = ctx
        val chips = Kit.hbox(c)
        listOf(R.string.d_easy, R.string.d_medium, R.string.d_hard, R.string.d_mixed).forEachIndexed { i, l -> chips.addView(Kit.chip(c, str(l), difficulty == i) { difficulty = i; refresh() }) }
        body.addView(Kit.hscroll(c, chips).margins(c, top = 4))
        val types: List<Pair<String?, Int>> = if (fmt == "toeic_p6") {
            services.content.passages.flatMap { it.blanks }.groupBy { it.qtype }.map { it.key to it.value.size } + listOf<Pair<String?, Int>>(null to services.content.passages.sumOf { it.blanks.size })
        } else {
            val pool = services.builder().examPool(fmt).filter { it.type != "E05" }
            pool.groupBy { it.qtype ?: if (it.type == "E04") "error" else if (it.type == "E12") "transform" else "vocab" }.map { it.key to it.value.size }.sortedByDescending { it.second }
        }
        types.forEach { (qt, count) ->
            val keys = if (fmt == "toeic_p6") services.content.passages.flatMap { p -> p.blanks.indices.filter { qt == null || p.blanks[it].qtype == qt }.map { p.key(it) } }
            else services.builder().examPool(fmt).filter { q -> (q.qtype ?: if (q.type == "E04") "error" else if (q.type == "E12") "transform" else "vocab") == qt }.map { it.key }
            val st = keys.mapNotNull { services.store.item(it) }.filter { it.seen > 0 }
            val acc = if (st.isEmpty()) null else st.sumOf { it.correct } * 100 / st.sumOf { it.seen }.coerceAtLeast(1)
            val label = if (qt == null) str(R.string.all_types) else TagNames.label(c, qt)
            body.addView(Kit.card(c, 16, 12) {
                val r = Kit.hbox(c)
                r.addView(Kit.vbox(c) {
                    layoutParams = lp(0, WRAP_CONTENT, 1f)
                    addView(Kit.text(c, label, R.style.Text_BodyStrong))
                    addView(Kit.text(c, str(R.string.type_meta, count, acc?.let { "$it%" } ?: "—", st.count { it.mastered }), R.style.Text_Caption))
                })
                r.addView(Kit.primary(c, str(R.string.practice), 0) { tips(qt, label) }.apply { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, c.dpi(44)) })
                addView(r)
            })
        }
    }

    /** "Mẹo nhận diện" card, then a 10-question session. */
    private fun tips(qt: String?, label: String) {
        val c = ctx
        val tip = qt?.let { services.content.tips[it] }
        val start = { activity.startSession(services.builder().examType(label, fmt, qt, difficulty)) }
        if (tip == null) { start(); return }
        val sheet = BottomSheet(activity)
        sheet.show { box ->
            box.addView(Kit.text(c, "💡 " + str(R.string.tips_title), R.style.Text_Title))
            box.addView(Kit.text(c, label, R.style.Text_Caption).margins(c, top = 2))
            box.addView(Kit.text(c, tip, R.style.Text_Body).margins(c, top = 10))
            box.addView(Kit.primary(c, str(R.string.start_10)) { sheet.dismiss(); start() })
        }
    }
}

/** S15 — mistake book grouped by tag. Items leave after 2 consecutive correct reviews. */
class MistakeBookScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val petMode = PetMode.FLOATING
    override val barTitle: String get() = str(R.string.mistake_book)
    private var days = 0 // 0 all · 7 · 30

    override fun build(body: LinearLayout) {
        val c = ctx
        val now = System.currentTimeMillis()
        val chips = Kit.hbox(c)
        listOf(0 to R.string.f_all, 7 to R.string.last_7, 30 to R.string.last_30).forEach { (d, l) -> chips.addView(Kit.chip(c, str(l), days == d) { days = d; refresh() }) }
        body.addView(Kit.hscroll(c, chips).margins(c, top = 4))
        val b = services.builder()
        val items = services.store.allItems().filter { it.mistake && (days == 0 || now - it.lastWrongAt < days * 86_400_000L) }
        if (items.isEmpty()) { body.addView(Kit.empty(c, "🎉", str(R.string.mistakes_empty), str(R.string.go_practice)) { activity.selectTab(Tab.EXAM) }); return }
        val exByKey = items.associate { it.key to b.exerciseFor(it.key) }
        val groups = items.flatMap { s -> (exByKey[s.key]?.tags.orEmpty().filter { TagNames.has(it) }.ifEmpty { listOf("vocab_core") }).map { it to s } }.groupBy({ it.first }, { it.second })
            .entries.sortedByDescending { it.value.size }
        body.addView(Kit.primary(c, str(R.string.redo_all, items.size), 12) { activity.startSession(b.mistakes(str(R.string.mistake_book), null)) })
        groups.forEach { (tag, list) ->
            var expanded = false
            val card = Kit.card(c, 6, 12)
            val header = Kit.row(c, Kit.badge(c, "${list.size}", c.col(R.color.error_container), c.col(R.color.error)), TagNames.label(c, tag), null, Kit.chevron(c))
            card.addView(header)
            val inner = Kit.vbox(c).apply { visibility = android.view.View.GONE }
            list.take(12).forEach { s ->
                val ex = exByKey[s.key] ?: return@forEach
                val text = when (ex) {
                    is com.yourbrand.englishlearn.learning.Exercise.Choice -> ex.stem.ifBlank { ex.options[ex.answer] }.replace("[", "").replace("]", "") + "  →  " + ex.options[ex.answer]
                    is com.yourbrand.englishlearn.learning.Exercise.FindError -> ex.stem.replace("[", "").replace("]", "") + "  →  " + ex.fix
                    is com.yourbrand.englishlearn.learning.Exercise.Spelling -> ex.gloss + "  →  " + ex.word.lemma
                    is com.yourbrand.englishlearn.learning.Exercise.Flashcard -> ex.word.lemma
                    is com.yourbrand.englishlearn.learning.Exercise.WordOrder -> ex.sentence
                    is com.yourbrand.englishlearn.learning.Exercise.Matching -> ex.left.joinToString()
                    is com.yourbrand.englishlearn.learning.Exercise.PictureChoice -> ex.words[ex.answer].lemma
                    is com.yourbrand.englishlearn.learning.Exercise.PictureMatch -> ex.words.joinToString(", ") { it.lemma }
                    is com.yourbrand.englishlearn.learning.Exercise.Dictation -> ex.sentence
                    is com.yourbrand.englishlearn.learning.Exercise.MinimalPairChoice -> if (ex.answer == 0) ex.first else ex.second
                }
                val r = Kit.hbox(c) { setPadding(c.dpi(12), c.dpi(6), c.dpi(4), c.dpi(6)) }
                r.addView(Kit.text(c, text, R.style.Text_Body).apply { textSize = 14f; layoutParams = lp(0, WRAP_CONTENT, 1f) })
                r.addView(android.widget.ImageView(c).apply {
                    setImageResource(R.drawable.ic_check); tintRes(R.color.success); setBackgroundResource(R.drawable.ripple_circle)
                    val p = c.dpi(12); setPadding(p, p, p, p); contentDescription = str(R.string.remove_mastered)
                    layoutParams = LinearLayout.LayoutParams(c.dpi(44), c.dpi(44))
                    setOnClickListener { services.store.removeFromMistakes(s.key); refresh() }
                })
                inner.addView(r)
            }
            inner.addView(Kit.secondary(c, str(R.string.redo_group)) { activity.startSession(b.mistakes(TagNames.label(c, tag), tag)) }.margins(c, top = 6, bottom = 8, start = 8, end = 8))
            card.addView(inner)
            header.setOnClickListener { expanded = !expanded; inner.show(expanded); (header.getChildAt(header.childCount - 1)).animate().rotation(if (expanded) 90f else 0f).setDuration(150).start() }
            body.addView(card)
        }
    }
}
