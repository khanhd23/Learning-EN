package com.yourbrand.englishlearn.screens

import android.view.Gravity
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.content.Topic
import com.yourbrand.englishlearn.content.Word
import com.yourbrand.englishlearn.ui.*
import com.yourbrand.englishlearn.ui.views.Bar
import com.yourbrand.englishlearn.ui.views.ProgressRing

/** S04 — Từ vựng hub: Chủ đề · Cấp độ · Của tôi. */
class VocabScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val tab = Tab.VOCAB
    override val allowsBanner = true
    override val petMode = PetMode.FLOATING
    private var page = 0

    override fun headerActions(bar: LinearLayout) = HubHeader.build(this, bar, str(R.string.tab_vocab))

    override fun build(body: LinearLayout) {
        val c = ctx
        body.addView(segmented(listOf(str(R.string.by_topic), str(R.string.by_level), str(R.string.mine)), page) { page = it; refresh() }.margins(c, top = 4))
        when (page) {
            0 -> topics(body)
            1 -> levels(body)
            else -> mine(body)
        }
    }

    private fun known(w: Word) = services.store.item(w.key)?.mastered == true

    private fun topics(body: LinearLayout) {
        val c = ctx
        val content = services.content
        body.addView(Kit.text(c, str(R.string.vocab_total, content.words.size, content.words.sumOf { it.senses.size }), R.style.Text_Caption).margins(c, top = 12))
        // Two cards per row; on a fresh open the first rows appear at once and the rest fill in.
        val grid = Kit.vbox(c)
        body.addView(grid)
        val topics = content.topics
        // Cards are added one at a time (a pair per row): one card is a small enough slice of
        // work to fit inside a frame's time budget while the rest of the grid fills in.
        var row: LinearLayout? = null
        val addCard = { i: Int ->
            if (i % 2 == 0) { row = Kit.hbox(c) { gravity = Gravity.TOP }.margins(c, top = 12); grid.addView(row) }
            row!!.addView(topicCard(topics[i]).apply { layoutParams = lp(0, WRAP_CONTENT, 1f).apply { if (i % 2 == 1) marginStart = c.dpi(12) } })
            if (i == topics.lastIndex && i % 2 == 0) row!!.addView(android.view.View(c).apply { layoutParams = lp(0, 1, 1f).apply { marginStart = c.dpi(12) } })
        }
        // A topic row (two cards) is ~150dp; the header, tabs and count line take ~200dp.
        if (firstBuild) addInFrames(grid, topics.size, now = 2 * visibleCount(c, itemDp = 150, aboveDp = 200), startDelayMs = AFTER_TRANSITION_MS, add = addCard)
        else for (i in topics.indices) addCard(i)
        wordLists(body)
    }

    private fun wordLists(body: LinearLayout) {
        val c = ctx
        val content = services.content
        if (content.wordLists.isEmpty()) return
        body.addView(Kit.section(c, str(R.string.word_lists)))
        body.addView(Kit.text(c, str(R.string.word_lists_desc), R.style.Text_Caption))
        val grid = Kit.vbox(c).margins(c, top = 4)
        body.addView(grid)
        content.wordLists.forEach { definition ->
            val words = content.wordsByList[definition.id].orEmpty()
            val ready = words.count { it.tier == "silver" }
            grid.addView(Kit.clickableCard(c, 14, 8, onClick = { activity.open(WordListScreen(activity, listId = definition.id)) }) {
                addView(Kit.row(c, Kit.emojiTile(c, definition.emoji, c.col(R.color.surface_variant)),
                    definition.label, str(R.string.n_of_m_ready, ready, words.size), Kit.chevron(c)))
            })
        }
    }

    private fun topicCard(t: Topic): LinearLayout {
        val c = ctx
        val words = services.content.wordsByTopic[t.id].orEmpty()
        val known = words.count { known(it) }
        return Kit.clickableCard(c, 14, 0, Hues.container(c, t.hue), onClick = { activity.open(WordListScreen(activity, topicId = t.id)) }) {
            minimumHeight = c.dpi(138)
            val top = Kit.hbox(c)
            top.addView(Kit.text(c, t.icon, sizeSp = 26f).apply { layoutParams = lp(0, WRAP_CONTENT, 1f) })
            val ringBox = FrameLayout(c).apply { layoutParams = LinearLayout.LayoutParams(c.dpi(40), c.dpi(40)) }
            val ring = ProgressRing(c).apply { stroke = c.dp(5f); color = Hues.color(c, t.hue); trackColor = c.col(R.color.surface) }
            ringBox.addView(ring, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
            ringBox.addView(Kit.text(c, "${known * 100 / words.size.coerceAtLeast(1)}%", R.style.Text_Caption, sizeSp = 10f).apply { gravity = Gravity.CENTER }, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER))
            top.addView(ringBox)
            ring.post { ring.set(known / words.size.toFloat().coerceAtLeast(1f)) }
            addView(top)
            addView(Kit.ellipsize(Kit.text(c, t.name, R.style.Text_BodyStrong), 2).margins(c, top = 10))
            addView(Kit.text(c, str(R.string.words_known, known, words.size), R.style.Text_Caption).margins(c, top = 2))
            services.content.stories.firstOrNull { it.topic == t.id && words.any { w -> services.store.item(w.key)?.seen ?: 0 > 0 } }?.let { story ->
                addView(Kit.secondary(c, story.title, 14) { activity.open(StoryScreen(activity, story)) }.margins(c, top = 10))
            }
            contentDescription = "${t.name}, ${str(R.string.words_known, known, words.size)}"
        }
    }

    private fun levels(body: LinearLayout) {
        val c = ctx
        val words = services.content.words
        (1..5).forEach { lvl ->
            val list = words.filter { it.level == lvl }
            val known = list.count { known(it) }
            body.addView(Kit.clickableCard(c, 16, 12, onClick = { if (services.content.isLevelAvailable(lvl)) activity.open(WordListScreen(activity, level = lvl)) }) {
                val r = Kit.hbox(c)
                r.addView(Kit.text(c, str(R.string.level_n, lvl), R.style.Text_Title).apply { layoutParams = lp(0, WRAP_CONTENT, 1f) })
                addView(r)
                if (!services.content.isLevelAvailable(lvl)) {
                    r.addView(Kit.text(c, str(R.string.coming_soon), R.style.Text_Caption))
                } else {
                    r.addView(Kit.text(c, str(R.string.words_known, known, list.size), R.style.Text_Caption))
                    addView(Kit.text(c, str(when (lvl) { 1 -> R.string.level_desc_1; 2 -> R.string.level_desc_2; 3 -> R.string.level_desc_3; 4 -> R.string.level_desc_4; else -> R.string.level_desc_5 }), R.style.Text_Caption).margins(c, top = 2))
                    addView(Bar(c).apply { color = Hues.color(c, lvl * 2); layoutParams = lp(h = c.dpi(8)).apply { topMargin = c.dpi(12) }; post { set(known / list.size.toFloat().coerceAtLeast(1f)) } })
                }
            })
        }
    }

    private fun mine(body: LinearLayout) {
        val c = ctx
        val st = services.store.allItems().filter { it.key.startsWith("w:") }
        val groups = listOf(
            Triple(R.string.mine_saved, "🔖", st.filter { it.saved }),
            Triple(R.string.mine_wrong, "⚠️", st.filter { it.wrong > 0 && !it.mastered }.sortedByDescending { it.wrongRate }),
            Triple(R.string.mine_learning, "🌱", st.filter { it.seen > 0 && !it.mastered }),
            Triple(R.string.mine_known, "⭐", st.filter { it.mastered }),
        )
        if (st.isEmpty()) {
            body.addView(Kit.empty(c, "📚", str(R.string.mine_empty), str(R.string.start_learning)) { page = 0; refresh() })
            return
        }
        groups.forEach { (label, emoji, list) ->
            body.addView(Kit.clickableCard(c, 6, 12, onClick = { activity.open(WordListScreen(activity, keys = list.map { it.key }, title = str(label))) }) {
                addView(Kit.row(c, Kit.emojiTile(c, emoji, c.col(R.color.surface_variant)), str(label), str(R.string.n_words, list.size), Kit.chevron(c)))
            })
        }
        // Confusables are reachable from "Hay sai" too.
        body.addView(Kit.section(c, str(R.string.confusables)))
        val flow = Kit.flow(c)
        services.content.confusables.forEach { cf -> flow.addView(Kit.chip(c, cf.words.joinToString(" / ")) { activity.open(ConfusableScreen(activity, cf.id)) }) }
        body.addView(flow)
    }
}

/** iOS-style segmented control. */
fun ScrollScreen.segmented(labels: List<String>, selected: Int, onSelect: (Int) -> Unit): LinearLayout {
    val c = activity
    val track = Kit.hbox(c) {
        background = c.rounded(c.col(R.color.surface_variant), 14f)
        val p = c.dpi(4); setPadding(p, p, p, p)
    }
    labels.forEachIndexed { i, label ->
        track.addView(android.widget.TextView(c).apply {
            text = label; gravity = Gravity.CENTER; textSize = 14f
            val on = i == selected
            background = if (on) c.rounded(c.col(R.color.surface), 11f) else null
            elevation = if (on) c.dp(1f) else 0f
            setTextColor(c.col(if (on) R.color.on_surface else R.color.muted))
            typeface = android.graphics.Typeface.create("sans-serif-medium", if (on) android.graphics.Typeface.BOLD else android.graphics.Typeface.NORMAL)
            layoutParams = lp(0, c.dpi(40), 1f)
            isSelected = on
            setOnClickListener { if (i != selected) { haptic(); onSelect(i) } }
        })
    }
    return track
}
