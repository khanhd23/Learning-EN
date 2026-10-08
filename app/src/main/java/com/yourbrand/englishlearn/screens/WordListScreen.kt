package com.yourbrand.englishlearn.screens

import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.content.Word
import com.yourbrand.englishlearn.learning.SessionKind
import com.yourbrand.englishlearn.learning.VocabMode
import com.yourbrand.englishlearn.ui.*

/** S05 — word list for a topic, a level, or "my words". */
class WordListScreen(
    activity: MainActivity,
    private val topicId: String? = null,
    private val level: Int? = null,
    private val keys: List<String>? = null,
    private val title: String? = null,
    private val listId: String? = null,
) : ScrollScreen(activity) {
    override val petMode = PetMode.FLOATING
    private var filter = -1 // -1 all · 0 new · 1 learning · 2 known · 3 saved
    private var sort = 0 // 0 default · 1 A–Z · 2 hard→easy · 3 often wrong
    private var shown = PAGE
    private var loadMore: (() -> Unit)? = null
    private val selected = LinkedHashSet<String>()
    private var selectBar: LinearLayout? = null

    private val topic get() = topicId?.let { services.content.topicById[it] }
    private val wordList get() = listId?.let { id -> services.content.wordLists.firstOrNull { it.id == id } }
    override val barTitle: String get() = title ?: wordList?.let { "${it.emoji} ${it.label}" } ?: topic?.let { "${it.icon} ${it.name}" } ?: level?.let { str(R.string.level_n, it) } ?: ""

    private fun allWords(): List<Word> {
        val c = services.content
        return when {
            listId != null -> c.wordsByList[listId].orEmpty()
            topicId != null -> c.wordsByTopic[topicId].orEmpty()
            level != null -> c.words.filter { it.level == level }
            keys != null -> keys.mapNotNull { c.wordById[it.removePrefix("w:")] }
            else -> emptyList()
        }
    }

    override fun build(body: LinearLayout) {
        val c = ctx
        val tData = System.nanoTime()
        val words = allWords()
        val st = services.store
        val known = words.count { st.item(it.key)?.mastered == true }
        Perf.log("WordList data: ${words.size} words in ${Perf.ms(tData, System.nanoTime())}")

        // Header: progress + "Học chủ đề" + modes
        val head = Kit.card(c, 16, 4, topic?.let { Hues.container(c, it.hue) } ?: c.col(R.color.surface), null)
        head.addView(Kit.text(c, str(R.string.words_known, known, words.size), R.style.Text_BodyStrong))
        head.addView(com.yourbrand.englishlearn.ui.views.Bar(c).apply {
            topic?.let { color = Hues.color(c, it.hue) }
            trackColor = c.col(R.color.surface)
            layoutParams = lp(h = c.dpi(8)).apply { topMargin = c.dpi(10) }
            post { set(known / words.size.toFloat().coerceAtLeast(1f)) }
        })
        val btns = Kit.hbox(c).margins(c, top = 14)
        btns.addView(Kit.primary(c, str(if (listId != null) R.string.learn_list else R.string.learn_topic), 0) { study(VocabMode.MIXED, words) }.apply { layoutParams = lp(0, c.dpi(52), 1f); tag = "petAvoid" })
        btns.addView(Kit.secondary(c, "⋯", 0) { modeMenu(words) }.apply { layoutParams = LinearLayout.LayoutParams(c.dpi(64), c.dpi(52)).apply { marginStart = c.dpi(10) }; contentDescription = str(R.string.study_modes) })
        head.addView(btns)
        body.addView(head)

        // Filters + sort
        val chips = Kit.hbox(c)
        listOf(R.string.f_all to -1, R.string.f_new to 0, R.string.f_learning to 1, R.string.f_known to 2, R.string.f_saved to 3).forEach { (label, v) ->
            chips.addView(Kit.chip(c, str(label), filter == v) { filter = v; shown = 60; refresh() })
        }
        chips.addView(Kit.chip(c, "⇅ " + str(listOf(R.string.sort_default, R.string.sort_az, R.string.sort_hard, R.string.sort_wrong)[sort])) { sort = (sort + 1) % 4; refresh() })
        body.addView(Kit.hscroll(c, chips).margins(c, top = 14))

        var list = words.filter { w ->
            val s = st.item(w.key)
            when (filter) {
                0 -> s == null || s.isNew
                1 -> s != null && s.seen > 0 && !s.mastered
                2 -> s?.mastered == true
                3 -> s?.saved == true
                else -> true
            }
        }
        list = when (sort) {
            1 -> list.sortedBy { it.lemma.lowercase() }
            2 -> list.sortedByDescending { it.level }
            3 -> list.sortedByDescending { st.item(it.key)?.wrongRate ?: 0f }
            else -> list
        }
        if (list.isEmpty()) { body.addView(Kit.empty(c, "🔎", str(R.string.no_words_filter), null, null)); return }
        val card = Kit.card(c, 4, 6)
        fun add(i: Int) { if (i > 0) card.addView(Kit.divider(c)); card.addView(wordRow(list[i])) }
        // Show one page (20 words) first; more are appended as the user nears the end.
        var count = minOf(shown, list.size)
        // A word row is ~72dp; the progress card and filter chips take ~300dp.
        if (firstBuild) addInFrames(card, count, now = visibleCount(c, itemDp = 72, aboveDp = 300), startDelayMs = AFTER_TRANSITION_MS, add = ::add) else for (i in 0 until count) add(i)
        body.addView(card)
        val more = Kit.secondary(c, "") { loadMore?.invoke() }
        fun updateMore() { more.visibility = if (count < list.size) View.VISIBLE else View.GONE; more.text = str(R.string.show_more, list.size - count) }
        updateMore()
        body.addView(more)
        wordList?.credit?.takeIf { it.isNotBlank() }?.let { credit ->
            body.addView(Kit.text(c, str(R.string.list_source, credit), R.style.Text_Caption).margins(c, top = 18, bottom = 12))
        }
        var loading = false
        loadMore = {
            if (!loading && count < list.size) {
                // Append the next page a couple of rows per frame so scrolling never stalls.
                loading = true
                val start = count
                val end = minOf(count + PAGE, list.size)
                count = end; shown = maxOf(shown, count)
                updateMore()
                addInFrames(card, end - start, now = 1, onDone = { loading = false }) { add(start + it) }
            }
        }
        scroll.setOnScrollChangeListener { _, _, y, _, _ ->
            val content = scroll.getChildAt(0) ?: return@setOnScrollChangeListener
            if (content.bottom - (scroll.height + y) < c.dpi(600)) loadMore?.invoke()
        }
    }

    private fun wordRow(w: Word): View {
        val c = ctx
        val s = services.store.item(w.key)
        val row = Kit.hbox(c) {
            minimumHeight = c.dpi(64)
            setPadding(c.dpi(12), c.dpi(8), c.dpi(4), c.dpi(8))
            background = c.rounded(if (w.key in selected) c.col(R.color.primary_container) else 0, 12f, ripple = true)
            isClickable = true
        }
        w.image?.let { name ->
            val id = c.resources.getIdentifier(name, "drawable", c.packageName)
            if (id != 0) row.addView(ImageView(c).apply {
                setImageResource(id)
                contentDescription = w.lemma
                scaleType = ImageView.ScaleType.CENTER_INSIDE
                layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48)).apply { marginEnd = c.dpi(10) }
            })
        }
        val texts = Kit.vbox(c) { layoutParams = lp(0, WRAP_CONTENT, 1f) }
        val top = Kit.hbox(c)
        top.addView(Kit.text(c, w.lemma, R.style.Text_BodyStrong).apply { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) })
        top.addView(Kit.text(c, " " + w.senses.joinToString("/") { it.pos }.let { if (it.length > 12) w.pos else it }, R.style.Text_Caption, c.col(R.color.primary)).apply { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) })
        texts.addView(top)
        texts.addView(Kit.ellipsize(Kit.text(c, w.senses.joinToString("; ") { it.gloss }, R.style.Text_Caption)))
        // Mastery dots 0–5
        texts.addView(MasteryDots(c, (s?.box ?: 0).coerceAtMost(5)).margins(c, top = 4))
        row.addView(texts)
        row.addView(ImageView(c).apply {
            setImageDrawable(icon(R.drawable.ic_volume)); tintRes(R.color.primary)
            setBackgroundResource(R.drawable.ripple_circle); val p = c.dpi(12); setPadding(p, p, p, p)
            contentDescription = str(R.string.listen)
            layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48))
            setOnClickListener { services.tts.speak(w.lemma) }
        })
        row.addView(ImageView(c).apply {
            val saved = s?.saved == true
            setImageDrawable(icon(if (saved) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border)); tintRes(if (saved) R.color.accent else R.color.muted)
            setBackgroundResource(R.drawable.ripple_circle); val p = c.dpi(12); setPadding(p, p, p, p)
            contentDescription = str(R.string.save)
            layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48))
            setOnClickListener {
                val now = services.store.toggleSaved(w.key)
                setImageResource(if (now) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border); tintRes(if (now) R.color.accent else R.color.muted)
                pop()
            }
        })
        row.setOnClickListener {
            if (selected.isNotEmpty()) toggleSelect(w, row) else activity.open(WordDetailScreen(activity, w.id))
        }
        row.setOnLongClickListener { it.haptic(android.view.HapticFeedbackConstants.LONG_PRESS); toggleSelect(w, row); true }
        return row
    }

    // Icons are inflated once per screen and shared (each row used to inflate its own vectors).
    private val icons = HashMap<Int, android.graphics.drawable.Drawable.ConstantState?>()
    private fun icon(id: Int): android.graphics.drawable.Drawable? =
        icons.getOrPut(id) { ctx.getDrawable(id)?.constantState }?.newDrawable(ctx.resources)?.mutate()

    private companion object { const val PAGE = 20 }

    private fun toggleSelect(w: Word, row: View) {
        if (!selected.add(w.key)) selected.remove(w.key)
        row.background = ctx.rounded(if (w.key in selected) ctx.col(R.color.primary_container) else 0, 12f, ripple = true)
        updateSelectBar()
    }

    /** Sticky bottom bar when ≥ 1 word is selected: "Luyện các từ đã chọn". */
    private fun updateSelectBar() {
        val c = ctx
        if (selected.isEmpty()) { selectBar?.let { root.removeView(it) }; selectBar = null; return }
        val bar = selectBar ?: Kit.hbox(c) {
            setPadding(c.dpi(16), c.dpi(10), c.dpi(16), c.dpi(12))
            setBackgroundColor(c.col(R.color.surface))
            elevation = c.dp(8f)
        }.also { selectBar = it; root.addView(it) }
        bar.removeAllViews()
        bar.addView(Kit.text(c, str(R.string.n_selected, selected.size), R.style.Text_BodyStrong).apply { layoutParams = lp(0, WRAP_CONTENT, 1f) })
        bar.addView(Kit.secondary(c, str(R.string.clear), 0) { selected.clear(); updateSelectBar(); refresh() }.apply { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, c.dpi(48)).apply { marginEnd = c.dpi(8) } })
        bar.addView(Kit.primary(c, str(R.string.practice_selected), 0) {
            val words = selected.mapNotNull { services.content.wordById[it.removePrefix("w:")] }
            selected.clear(); updateSelectBar()
            activity.startSession(services.builder().words(SessionKind.WORDS, str(R.string.practice_selected), words, VocabMode.MIXED, words.size.coerceAtMost(20)))
        }.apply { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, c.dpi(48)) })
    }

    private fun modeMenu(words: List<Word>) {
        val c = ctx
        val sheet = BottomSheet(activity)
        sheet.show { box ->
            box.addView(Kit.text(c, str(R.string.study_modes), R.style.Text_Title))
            listOf(
                Triple("🃏", R.string.mode_flashcard, VocabMode.FLASHCARD),
                Triple("✅", R.string.mode_quiz, VocabMode.QUIZ),
                Triple("🔗", R.string.mode_matching, VocabMode.MATCHING),
                Triple("⌨️", R.string.mode_spelling, VocabMode.SPELLING),
                Triple("🎧", R.string.mode_listening, VocabMode.LISTENING),
            ).forEach { (emoji, label, mode) ->
                box.addView(Kit.row(c, Kit.emojiTile(c, emoji, c.col(R.color.surface_variant), 40, 20f), str(label), null, Kit.chevron(c)) { sheet.dismiss(); study(mode, words) }.margins(c, top = 4))
            }
        }
    }

    private fun study(mode: VocabMode, words: List<Word>) {
        val candidates = if (listId != null) words.filter { it.tier == "silver" } else words
        if (candidates.isEmpty()) return
        activity.startSession(services.builder().words(if (listId != null) SessionKind.WORDS else SessionKind.TOPIC, barTitle, candidates, mode, if (mode == VocabMode.MATCHING) 12 else 10, newFirst = listId != null))
    }
}

/** Mastery 0–5 as five dots drawn by one view (was five views with five drawables per row). */
private class MasteryDots(ctx: android.content.Context, private val filled: Int) : View(ctx) {
    private val on = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply { color = ctx.col(R.color.primary) }
    private val off = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply { color = ctx.col(R.color.surface_variant) }
    private val d = ctx.dp(7f)
    private val gap = ctx.dp(3f)

    init { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT); importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO }

    override fun onMeasure(w: Int, h: Int) = setMeasuredDimension((5 * d + 4 * gap).toInt() + 1, d.toInt() + 1)

    override fun onDraw(canvas: android.graphics.Canvas) {
        for (i in 0 until 5) canvas.drawCircle(d / 2 + i * (d + gap), d / 2, d / 2, if (i < filled) on else off)
    }
}
