package com.yourbrand.englishlearn.screens

import android.graphics.Typeface
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.content.GrammarPoint
import com.yourbrand.englishlearn.core.GrammarRoadmap
import com.yourbrand.englishlearn.ui.*

/** S08 — grammar ladder, 5 levels easy → hard. */
class GrammarScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val tab = Tab.GRAMMAR
    // Use only MainActivity's fixed bottom banner; never inject ads into the roadmap.
    override val allowsBanner = true
    override val petMode = PetMode.FLOATING
    private var filter = 0 // 0 all · 1 TOEIC · 2 tricky
    private var roadmapId: String? = null
    private var cachedScrollY: Int? = null
    private var cachedRoadmapId: String? = null

    private val displayContent get() = if (services.contentReady) services.content else services.contentLight

    private fun visiblePoints(all: List<GrammarPoint>): List<GrammarPoint> = all.filter { gp -> when (filter) {
        1 -> gp.exam
        2 -> gp.mistakes.size >= 2 && gp.tags.any { it in setOf("tense", "conjunction", "agreement", "conditional", "gerund_infinitive", "pronoun") }
        else -> true
    } }

    private fun currentRoadmapId(all: List<GrammarPoint>): String? {
        val visible = visiblePoints(all)
        val stars = visible.associate { it.id to services.store.stars(it.id) }
        return GrammarRoadmap.currentId(visible.map { it.id }, stars, services.settings.grammarRoadmapId)
    }

    override fun onShown(firstTime: Boolean) {
        super.onShown(firstTime)
        // ScrollScreen restores/resets its position first. Restore Grammar's learning position
        // afterwards so returning to this tab never jumps back to the top.
        val id = if (services.contentLightReady) roadmapId ?: currentRoadmapId(displayContent.grammar) else null
        if (!firstTime && cachedScrollY != null && cachedRoadmapId == id) {
            scroll.scrollTo(0, cachedScrollY!!)
        } else {
            scroll.post { restoreLearningPosition() }
        }
    }

    override fun onHidden() {
        if (hasView) {
            cachedScrollY = scroll.scrollY
            cachedRoadmapId = roadmapId
        }
        super.onHidden()
    }

    /** Re-selects the saved roadmap node when the learner taps the Grammar tab again. */
    fun restoreLearningPosition() {
        if (!hasView) return
        val id = roadmapId ?: currentRoadmapId(displayContent.grammar) ?: return
        val point = displayContent.grammarById[id]
        val target = point?.let { body.findViewWithTag<View>("grammar-progress:${it.level}") }
            ?: body.findViewWithTag<View>("grammar-node:$id")
        if (target != null) {
            reveal(target)
        }
    }

    override fun headerActions(bar: LinearLayout) = HubHeader.build(this, bar, str(R.string.tab_grammar))

    enum class NodeState { LOCKED, AVAILABLE, IN_PROGRESS, MASTERED }

    fun stateOf(points: List<GrammarPoint>, i: Int): NodeState {
        val st = services.store
        val gp = points[i]
        if (st.stars(gp.id) > 0) return NodeState.MASTERED
        val unlocked = i == 0 || st.stars(points[i - 1].id) > 0
        if (!unlocked) return NodeState.LOCKED
        val started = displayContent.questionsByPoint[gp.id].orEmpty().any { (st.item(it.key)?.seen ?: 0) > 0 }
        return if (started) NodeState.IN_PROGRESS else NodeState.AVAILABLE
    }

    override fun build(body: LinearLayout) {
        if (showLightContentLoading(body)) return
        val c = ctx
        val chips = Kit.hbox(c)
        listOf(R.string.g_all, R.string.g_toeic, R.string.g_tricky).forEachIndexed { i, label -> chips.addView(Kit.chip(c, str(label), filter == i) { filter = i; refresh() }) }
        body.addView(Kit.hscroll(c, chips).margins(c, top = 4))
        body.addView(Kit.clickableCard(c, 8, 10, c.col(R.color.primary_container), onClick = {
            activity.startSessionAsync { services.builder().tensePractice("12 ${str(R.string.tag_tense)}") }
        }) {
            addView(Kit.row(c, Kit.emojiTile(c, "🕒", c.col(R.color.surface_variant)), "12 ${str(R.string.tag_tense)}", str(R.string.drills_sub, 42), Kit.chevron(c)))
        }.margins(c, top = 8, bottom = 4))

        val all = displayContent.grammar
        roadmapId = currentRoadmapId(all)
        if (firstBuild) {
            buildSmooth(body, c, all)
            return
        }
        (1..5).forEach { lvl ->
            val pts = all.filter { it.level == lvl }
            val visible = visiblePoints(pts)
            if (!displayContent.isLevelAvailable(lvl)) {
                body.addView(Kit.card(c, 14, 8, Hues.container(c, lvl * 2 - 2), null) {
                    addView(Kit.text(c, "${str(R.string.level_n, lvl)} · ${str(R.string.coming_soon)}", R.style.Text_Caption).apply {
                        gravity = Gravity.CENTER
                        layoutParams = lp(MATCH_PARENT, c.dpi(28))
                    })
                })
                return@forEach
            }
            if (visible.isEmpty()) return@forEach
            val mastered = pts.count { services.store.stars(it.id) > 0 }
            body.addView(levelProgress(c, lvl, mastered, pts.size))
            // Path of nodes (zig-zag) with connectors.
            visible.forEachIndexed { vi, gp ->
                val i = all.indexOf(gp)
                val state = stateOf(all, i)
                if (vi > 0) body.addView(connector(state != NodeState.LOCKED, vi))
                body.addView(node(gp, state, vi))
            }
        }
    }

    /** First-open ladder rendering is split by level and by frame; refreshes stay synchronous. */
    private fun buildSmooth(body: LinearLayout, c: android.content.Context, all: List<GrammarPoint>) {
        val filteredIds = visiblePoints(all).map { it.id }.toSet()
        fun next(levelIndex: Int) {
            if (levelIndex >= 5 || !body.isAttachedToWindow) return
            val lvl = levelIndex + 1
            val pts = all.withIndex().filter { it.value.level == lvl }
            val visible = pts.filter { (_, gp) -> gp.id in filteredIds }
            if (!displayContent.isLevelAvailable(lvl)) {
                body.addView(Kit.card(c, 14, 8, Hues.container(c, lvl * 2 - 2), null) {
                    addView(Kit.text(c, "${str(R.string.level_n, lvl)} - ${str(R.string.coming_soon)}", R.style.Text_Caption).apply {
                        gravity = Gravity.CENTER
                        layoutParams = lp(MATCH_PARENT, c.dpi(28))
                    })
                })
                body.postOnAnimation { next(levelIndex + 1) }
                return
            }
            if (visible.isEmpty()) { body.postOnAnimation { next(levelIndex + 1) }; return }
            val mastered = pts.count { services.store.stars(it.value.id) > 0 }
            body.addView(levelProgress(c, lvl, mastered, pts.size))
            fun addNode(i: Int) {
                val (index, gp) = visible[i]
                val state = stateOf(all, index)
                if (i > 0) body.addView(connector(state != NodeState.LOCKED, i))
                body.addView(node(gp, state, i))
                if (gp.id == roadmapId) {
                    body.post { restoreLearningPosition() }
                }
            }
            addInFrames(body, visible.size, now = if (levelIndex == 0) 3 else 1,
                startDelayMs = if (levelIndex == 0) AFTER_TRANSITION_MS else 0,
                onDone = { body.postOnAnimation { next(levelIndex + 1) } }, add = ::addNode)
        }
        next(0)
    }

    private fun levelProgress(c: android.content.Context, level: Int, mastered: Int, total: Int): View =
        Kit.card(c, 14, 8, Hues.container(c, level * 2 - 2), null).apply {
            tag = "grammar-progress:$level"
            addView(Kit.text(c, "$mastered/$total", R.style.Text_BodyStrong, Hues.color(c, level * 2 - 2)).apply {
                gravity = Gravity.CENTER
                layoutParams = lp(MATCH_PARENT, c.dpi(28))
                contentDescription = str(R.string.level_n, level) + ", " + text
            })
        }

    private fun offsetFor(i: Int): Float = when (i % 4) { 0 -> 0f; 1 -> 0.18f; 2 -> 0f; else -> -0.18f }

    private fun connector(active: Boolean, i: Int): View {
        val c = ctx
        return object : View(c) {
            val p = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply {
                style = android.graphics.Paint.Style.STROKE; strokeWidth = c.dp(4f); strokeCap = android.graphics.Paint.Cap.ROUND
                color = c.col(if (active) R.color.primary else R.color.outline)
                if (!active) pathEffect = android.graphics.DashPathEffect(floatArrayOf(c.dp(6f), c.dp(8f)), 0f)
            }
            override fun onDraw(canvas: android.graphics.Canvas) {
                val x1 = width / 2f + width * offsetFor(i - 1) * 0.9f
                val x2 = width / 2f + width * offsetFor(i) * 0.9f
                val path = android.graphics.Path().apply { moveTo(x1, 0f); cubicTo(x1, height * 0.6f, x2, height * 0.4f, x2, height.toFloat()) }
                canvas.drawPath(path, p)
            }
        }.apply { layoutParams = lp(h = c.dpi(26)) }
    }

    private fun node(gp: GrammarPoint, state: NodeState, i: Int): View {
        val c = ctx
        val frame = FrameLayout(c).apply {
            layoutParams = lp(h = WRAP_CONTENT)
            tag = "grammar-node:${gp.id}"
        }
        val box = Kit.vbox(c) { gravity = Gravity.CENTER_HORIZONTAL; layoutParams = FrameLayout.LayoutParams(c.dpi(220), WRAP_CONTENT, Gravity.CENTER_HORIZONTAL) }
        val circle = FrameLayout(c).apply {
            val (fill, stroke) = when (state) {
                NodeState.MASTERED -> R.color.accent to R.color.accent
                NodeState.IN_PROGRESS -> R.color.primary to R.color.primary
                NodeState.AVAILABLE -> R.color.surface to R.color.primary
                NodeState.LOCKED -> R.color.surface_variant to R.color.outline
            }
            background = c.rounded(c.col(fill), 100f, c.col(stroke), 3f, ripple = true)
            elevation = if (state == NodeState.LOCKED) 0f else c.dp(4f)
            layoutParams = LinearLayout.LayoutParams(c.dpi(68), c.dpi(68))
            isClickable = true
            contentDescription = gp.title + ", " + str(when (state) { NodeState.LOCKED -> R.string.node_locked; NodeState.MASTERED -> R.string.node_mastered; NodeState.IN_PROGRESS -> R.string.node_progress; else -> R.string.node_available })
        }
        val icon = when (state) {
            NodeState.LOCKED -> Kit.icon(c, R.drawable.ic_lock, c.col(R.color.muted), 26)
            NodeState.MASTERED -> Kit.icon(c, R.drawable.ic_star_filled, c.col(R.color.on_accent), 30)
            NodeState.IN_PROGRESS -> Kit.icon(c, R.drawable.ic_play_circle, c.col(R.color.on_primary), 30)
            NodeState.AVAILABLE -> Kit.icon(c, R.drawable.ic_play_circle, c.col(R.color.primary), 30)
        }
        circle.addView(icon, FrameLayout.LayoutParams(icon.layoutParams.width, icon.layoutParams.height, Gravity.CENTER))
        box.addView(circle)
        val stars = services.store.stars(gp.id)
        if (state == NodeState.MASTERED) box.addView(Kit.text(c, "★".repeat(stars) + "☆".repeat(3 - stars), R.style.Text_BodyStrong, c.col(R.color.accent)).apply { gravity = Gravity.CENTER })
        box.addView(Kit.ellipsize(Kit.text(c, gp.title, R.style.Text_BodyStrong).apply { gravity = Gravity.CENTER; textSize = 14f; alpha = if (state == NodeState.LOCKED) 0.6f else 1f }, 2).margins(c, top = 4))
        frame.addView(box)
        frame.post { box.translationX = frame.width * offsetFor(i) * 0.9f }
        circle.onTap {
            if (state == NodeState.LOCKED) {
                Dialogs.confirm(c, str(R.string.locked_title), str(R.string.locked_msg), str(R.string.skip_test), str(R.string.cancel)) {
                    activity.startSessionAsync { services.builder().skipTest(str(R.string.skip_test) + ": " + gp.title, gp.id) }
                }
            } else {
                services.settings.grammarRoadmapId = gp.id
                activity.open(GrammarLessonScreen(activity, gp.id))
            }
        }
        if (state == NodeState.IN_PROGRESS && !c.reduceMotion) circle.postDelayed({ circle.pop(0.92f) }, 400)
        return frame
    }
}

/** S09 — grammar lesson, 4-step stepper: Hiểu · Xem · Cẩn thận · Luyện. No banner. */
class GrammarLessonScreen(activity: MainActivity, private val gpId: String) : Screen(activity) {
    override val petMode = PetMode.HIDDEN
    private var step = 0
    private lateinit var content: LinearLayout
    private lateinit var scroll: ScrollView
    private lateinit var dots: LinearLayout
    private lateinit var next: TextView
    private lateinit var back: TextView
    private val gp get() = services.content.grammarById[gpId]!!

    override fun onCreateView(parent: ViewGroup): View {
        val c = ctx
        val root = Kit.vbox(c).apply { layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT) }
        val top = Kit.hbox(c) { setPadding(c.dpi(4), c.dpi(4), c.dpi(16), c.dpi(4)); minimumHeight = c.dpi(56) }
        top.addView(ImageView(c).apply {
            setImageResource(R.drawable.ic_close); tintRes(R.color.on_surface); setBackgroundResource(R.drawable.ripple_circle)
            val p = c.dpi(12); setPadding(p, p, p, p); contentDescription = str(R.string.close)
            layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48)); setOnClickListener { activity.navigator.pop() }
        })
        top.addView(Kit.ellipsize(Kit.text(c, gp.title, R.style.Text_Title)).apply { layoutParams = lp(0, WRAP_CONTENT, 1f) })
        root.addView(top)
        dots = Kit.hbox(c) { gravity = Gravity.CENTER; setPadding(0, c.dpi(4), 0, c.dpi(8)) }
        root.addView(dots)
        scroll = ScrollView(c).apply { layoutParams = lp(MATCH_PARENT, 0, 1f) }
        content = Kit.vbox(c) { setPadding(c.dpi(20), c.dpi(8), c.dpi(20), c.dpi(24)) }
        scroll.addView(content)
        root.addView(scroll)
        val bar = Kit.hbox(c) { setPadding(c.dpi(20), c.dpi(8), c.dpi(20), c.dpi(16)) }
        back = Kit.secondary(c, str(R.string.back), 0) { if (step > 0) { step--; render() } }.apply { layoutParams = lp(0, c.dpi(52), 1f) }
        next = Kit.primary(c, str(R.string.next), 0) { onNext() }.apply { layoutParams = lp(0, c.dpi(52), 1.4f).apply { marginStart = c.dpi(10) } }
        bar.addView(back); bar.addView(next)
        root.addView(bar)
        return root
    }

    override fun onShown(firstTime: Boolean) { render() }

    private fun onNext() {
        if (step < 3) { step++; render(); return }
        activity.startSessionAsync { services.builder().grammar(gp.title, gp.id) }
    }

    private fun render() {
        val c = ctx
        dots.removeAllViews()
        val labels = listOf(R.string.step_understand, R.string.step_see, R.string.step_careful, R.string.step_practice)
        labels.forEachIndexed { i, l ->
            dots.addView(TextView(c).apply {
                text = str(l); textSize = 12f; gravity = Gravity.CENTER
                setTypeface(typeface, if (i == step) Typeface.BOLD else Typeface.NORMAL)
                setTextColor(c.col(if (i <= step) R.color.primary else R.color.muted))
                background = if (i == step) c.rounded(c.col(R.color.primary_container), 100f) else null
                setPadding(c.dpi(10), c.dpi(4), c.dpi(10), c.dpi(4))
                layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { marginStart = c.dpi(2); marginEnd = c.dpi(2) }
            })
        }
        back.isEnabled = step > 0
        back.alpha = if (step > 0) 1f else 0.4f
        next.text = str(if (step < 3) R.string.next else R.string.start_drills)
        content.removeAllViews()
        scroll.scrollTo(0, 0)
        val g = gp
        when (step) {
            0 -> {
                content.addView(Kit.text(c, str(R.string.when_to_use), R.style.Text_Caption))
                content.addView(Kit.text(c, g.whenToUse, R.style.Text_BodyStrong).margins(c, top = 2))
                content.addView(Kit.card(c, 16, 16, c.col(R.color.primary_container), null) {
                    addView(Kit.text(c, str(R.string.formula), R.style.Text_Caption, c.col(R.color.primary)))
                    addView(Kit.text(c, g.formula, R.style.Text_Title, c.col(R.color.on_surface)).apply { typeface = Typeface.MONOSPACE; textSize = 16f }.margins(c, top = 4))
                })
                content.addView(Kit.text(c, g.body, R.style.Text_Body).margins(c, top = 16))
                if (g.signals.isNotEmpty()) {
                    content.addView(Kit.text(c, str(R.string.signals), R.style.Text_BodyStrong).margins(c, top = 16))
                    val f = Kit.flow(c).margins(c, top = 6)
                    g.signals.forEach { f.addView(Kit.chip(c, it, false, c.col(R.color.info))) }
                    content.addView(f)
                }
            }
            1 -> {
                content.addView(Kit.text(c, str(R.string.examples_tap), R.style.Text_Caption))
                g.examples.forEach { e ->
                    content.addView(Kit.clickableCard(c, 16, 12, onClick = { services.tts.speak(e.en) }) {
                        addView(Kit.text(c, Spans.highlight(e.en, e.highlight, c.col(R.color.primary), c.col(R.color.primary_container)), R.style.Text_Question).apply { textSize = 18f })
                        addView(Kit.text(c, e.vi, R.style.Text_Caption).margins(c, top = 4))
                        addView(Kit.text(c, "🔊 " + str(R.string.listen), R.style.Text_Caption, c.col(R.color.primary)).margins(c, top = 6))
                    })
                }
            }
            2 -> {
                content.addView(Kit.text(c, str(R.string.common_mistakes), R.style.Text_Caption))
                g.mistakes.forEach { m ->
                    content.addView(Kit.card(c, 16, 12) {
                        addView(Kit.text(c, "✗  " + m.wrong, R.style.Text_Body, c.col(R.color.error)).apply { paintFlags = paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG })
                        addView(Kit.text(c, "✓  " + m.right, R.style.Text_BodyStrong, c.col(R.color.success)).margins(c, top = 4))
                        addView(Kit.text(c, m.note, R.style.Text_Caption).margins(c, top = 6))
                    })
                }
            }
            else -> {
                val n = services.content.questionsByPoint[g.id].orEmpty().size
                content.addView(Kit.vbox(c) {
                    gravity = Gravity.CENTER_HORIZONTAL
                    addView(Kit.text(c, "🏋️", sizeSp = 54f).apply { gravity = Gravity.CENTER }.margins(c, top = 24))
                    addView(Kit.text(c, str(R.string.drills_ready), R.style.Text_Title).apply { gravity = Gravity.CENTER }.margins(c, top = 8))
                    addView(Kit.text(c, str(R.string.drills_sub, minOf(10, n)), R.style.Text_Body, c.col(R.color.muted)).apply { gravity = Gravity.CENTER }.margins(c, top = 6))
                    val stars = services.store.stars(g.id)
                    if (stars > 0) addView(Kit.text(c, "★".repeat(stars) + "☆".repeat(3 - stars), R.style.Text_Display, c.col(R.color.accent)).apply { gravity = Gravity.CENTER }.margins(c, top = 12))
                })
            }
        }
        content.staggerChildren(40)
    }
}
