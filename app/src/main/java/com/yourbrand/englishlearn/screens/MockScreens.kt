package com.yourbrand.englishlearn.screens

import android.graphics.Typeface
import android.os.SystemClock
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
import com.yourbrand.englishlearn.services
import com.yourbrand.englishlearn.learning.Exercise
import com.yourbrand.englishlearn.learning.MockTest
import com.yourbrand.englishlearn.ui.*
import com.yourbrand.englishlearn.ui.views.Bar
import org.json.JSONArray
import org.json.JSONObject

/** S14 — mock test. No feedback until submission, no banner, no pet bubbles. */
class MockScreen(activity: MainActivity, private val test: MockTest) : Screen(activity) {
    override val petMode = PetMode.HIDDEN
    private val exercises: List<Exercise?> by lazy { val b = services.builder(); test.keys.map { b.exerciseFor(it) } }
    private lateinit var counter: TextView
    private lateinit var timer: TextView
    private lateinit var content: LinearLayout
    private lateinit var scroll: ScrollView
    private lateinit var flagBtn: ImageView
    private lateinit var prev: TextView
    private lateinit var next: TextView
    private var tickBase = SystemClock.elapsedRealtime()
    private var submitted = false

    companion object {
        fun resume(activity: MainActivity) {
            val json = activity.services.settings.mockInProgress ?: return
            val t = runCatching { MockTest.fromJson(json) }.getOrNull() ?: run { activity.services.settings.mockInProgress = null; return }
            if (activity.navigator.current is MockScreen) return
            activity.open(MockScreen(activity, t))
        }
    }

    private val tick = object : Runnable {
        override fun run() {
            if (submitted) return
            val now = SystemClock.elapsedRealtime()
            test.elapsedSec += ((now - tickBase) / 1000).toInt()
            tickBase += ((now - tickBase) / 1000) * 1000
            val limit = test.timeLimitSec
            if (limit != null) {
                val left = (limit - test.elapsedSec).coerceAtLeast(0)
                timer.text = "%d:%02d".format(left / 60, left % 60)
                val warn = left <= maxOf(60, limit / 10)
                timer.setTextColor(ctx.col(if (warn) R.color.warning else R.color.on_surface))
                if (left <= 10 && !ctx.reduceMotion) timer.animate().scaleX(1.1f).scaleY(1.1f).setDuration(250).withEndAction { timer.animate().scaleX(1f).scaleY(1f).setDuration(250).start() }.start()
                if (left == 0) { activity.toast(str(R.string.time_up)); submit(); return }
            } else timer.text = "%d:%02d".format(test.elapsedSec / 60, test.elapsedSec % 60)
            timer.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(parent: ViewGroup): View {
        val c = ctx
        val root = Kit.vbox(c).apply { layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT) }
        val top = Kit.hbox(c) { setPadding(c.dpi(4), c.dpi(4), c.dpi(12), c.dpi(4)); minimumHeight = c.dpi(56) }
        top.addView(ImageView(c).apply {
            setImageResource(R.drawable.ic_close); tintRes(R.color.on_surface); setBackgroundResource(R.drawable.ripple_circle)
            val p = c.dpi(12); setPadding(p, p, p, p); contentDescription = str(R.string.close)
            layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48)); setOnClickListener { askClose() }
        })
        counter = Kit.text(c, "", R.style.Text_Title).apply { layoutParams = lp(0, WRAP_CONTENT, 1f) }
        top.addView(counter)
        timer = Kit.badge(c, "0:00", c.col(R.color.surface_variant), c.col(R.color.on_surface)).apply { textSize = 15f; setPadding(c.dpi(12), c.dpi(6), c.dpi(12), c.dpi(6)) }
        top.addView(timer)
        root.addView(top)
        scroll = ScrollView(c).apply { layoutParams = lp(MATCH_PARENT, 0, 1f) }
        content = Kit.vbox(c) { setPadding(c.dpi(20), c.dpi(4), c.dpi(20), c.dpi(24)) }
        scroll.addView(content)
        root.addView(scroll)
        val bar = Kit.hbox(c) { setPadding(c.dpi(12), c.dpi(8), c.dpi(12), c.dpi(14)) }
        prev = Kit.secondary(c, "‹ " + str(R.string.prev), 0) { go(test.current - 1) }.apply { layoutParams = lp(0, c.dpi(52), 1f) }
        flagBtn = ImageView(c).apply {
            setImageResource(R.drawable.ic_flag); setBackgroundResource(R.drawable.ripple_circle)
            val p = c.dpi(14); setPadding(p, p, p, p); contentDescription = str(R.string.flag)
            layoutParams = LinearLayout.LayoutParams(c.dpi(52), c.dpi(52)).apply { marginStart = c.dpi(6) }
            setOnClickListener { test.flags[test.current] = !test.flags[test.current]; save(); paintFlag(); haptic() }
        }
        val palette = ImageView(c).apply {
            setImageResource(R.drawable.ic_grid); tintRes(R.color.on_surface); setBackgroundResource(R.drawable.ripple_circle)
            val p = c.dpi(14); setPadding(p, p, p, p); contentDescription = str(R.string.palette)
            layoutParams = LinearLayout.LayoutParams(c.dpi(52), c.dpi(52)).apply { marginEnd = c.dpi(6) }
            setOnClickListener { showPalette() }
        }
        next = Kit.primary(c, str(R.string.next) + " ›", 0) { if (test.current >= test.keys.lastIndex) confirmSubmit() else go(test.current + 1) }.apply { layoutParams = lp(0, c.dpi(52), 1f) }
        bar.addView(prev); bar.addView(flagBtn); bar.addView(palette); bar.addView(next)
        root.addView(bar)
        return root
    }

    override fun onShown(firstTime: Boolean) {
        if (firstTime) { save(); render(); tickBase = SystemClock.elapsedRealtime(); timer.post(tick) }
    }

    override fun onPause() { save() }
    override fun onResume() { tickBase = SystemClock.elapsedRealtime() }
    override fun onDestroy() { timer.removeCallbacks(tick) }
    override fun onBack(): Boolean { askClose(); return true }

    private fun save() { if (!submitted) services.settings.mockInProgress = test.toJson() }

    private fun askClose() {
        Dialogs.confirm(ctx, str(R.string.mock_leave_title), str(R.string.mock_leave_msg), str(R.string.quit_keep), str(R.string.mock_leave_save), onNegative = {
            save(); activity.navigator.pop()
        }) {}
    }

    private fun go(i: Int) {
        if (i !in test.keys.indices) return
        test.current = i
        save()
        render()
    }

    private fun paintFlag() { flagBtn.tintRes(if (test.flags[test.current]) R.color.accent else R.color.muted) }

    private fun render() {
        val c = ctx
        val i = test.current
        counter.text = "${i + 1}/${test.keys.size}"
        prev.isEnabled = i > 0; prev.alpha = if (i > 0) 1f else 0.4f
        next.text = if (i >= test.keys.lastIndex) str(R.string.submit) else str(R.string.next) + " ›"
        paintFlag()
        content.removeAllViews()
        scroll.scrollTo(0, 0)
        val ex = exercises[i] ?: run { content.addView(Kit.text(c, "—")); return }
        when (ex) {
            is Exercise.Choice -> {
                if (ex.passage != null) {
                    val filled = HashMap<Int, String>()
                    // In a mock the learner's own choices fill the other blanks.
                    test.keys.forEachIndexed { k, key -> val e = exercises[k] as? Exercise.Choice; if (e?.passage?.id == ex.passage.id && e.blank != ex.blank && test.answers[k] >= 0) filled[e.blank] = e.options[test.answers[k]] }
                    content.addView(Kit.text(c, ex.passage.title, R.style.Text_BodyStrong).margins(c, top = 4))
                    content.addView(Kit.card(c, 16, 10, c.col(R.color.surface_variant), null) {
                        addView(Kit.text(c, Spans.passage(ex.passage.text, filled, ex.blank, c.col(R.color.primary), c.col(R.color.primary_container), c.col(R.color.on_surface)), R.style.Text_Body).apply { textSize = 15f })
                    })
                } else {
                    content.addView(Kit.text(c, str(ex.instruction), R.style.Text_Caption))
                    if (ex.stem.isNotBlank()) content.addView(Kit.text(c, if (ex.stem.contains("___")) Spans.blank(ex.stem, ex.options.getOrNull(test.answers[i]), c.col(R.color.primary), c.col(R.color.primary_container)) else Spans.underline(ex.stem), R.style.Text_Question).margins(c, top = 8))
                }
                val cards = ex.options.mapIndexed { j, o -> OptionCard(c, j, if (ex.optionsMarked) Spans.underline(o) else o) }
                val box = Kit.vbox(c).margins(c, top = 14)
                cards.forEachIndexed { j, card ->
                    if (test.answers[i] == j) card.setState(OptionCard.State.SELECTED)
                    card.view.onTap {
                        test.answers[i] = j
                        cards.forEachIndexed { k, oc -> oc.setState(if (k == j) OptionCard.State.SELECTED else OptionCard.State.DEFAULT) }
                        save()
                        if (ex.stem.contains("___") && ex.passage == null) render()
                    }
                    box.addView(card.view)
                }
                content.addView(box)
            }
            is Exercise.FindError -> {
                content.addView(Kit.text(c, str(R.string.ins_find_error), R.style.Text_Caption))
                content.addView(Kit.text(c, Spans.underline(ex.stem), R.style.Text_Question).margins(c, top = 8))
                val parts = Regex("\\[([^\\]]+)\\]").findAll(ex.stem).map { it.groupValues[1] }.toList()
                val cards = parts.mapIndexed { j, p -> OptionCard(c, j, p) }
                val box = Kit.vbox(c).margins(c, top = 14)
                cards.forEachIndexed { j, card ->
                    if (test.answers[i] == j) card.setState(OptionCard.State.SELECTED)
                    card.view.onTap { test.answers[i] = j; cards.forEachIndexed { k, oc -> oc.setState(if (k == j) OptionCard.State.SELECTED else OptionCard.State.DEFAULT) }; save() }
                    box.addView(card.view)
                }
                content.addView(box)
            }
            else -> content.addView(Kit.text(c, "—"))
        }
        content.staggerChildren(25)
    }

    /** Palette sheet: grid of question numbers (answered / flagged / blank) + submit. */
    private fun showPalette() {
        val c = ctx
        val sheet = BottomSheet(activity)
        sheet.show { box ->
            box.addView(Kit.text(c, str(R.string.palette), R.style.Text_Title))
            box.addView(Kit.text(c, str(R.string.palette_legend, test.answeredCount, test.keys.size), R.style.Text_Caption).margins(c, top = 2, bottom = 8))
            val flow = Kit.flow(c)
            test.keys.indices.forEach { k ->
                val answered = test.answers[k] >= 0
                flow.addView(TextView(c).apply {
                    text = "${k + 1}"; gravity = Gravity.CENTER; textSize = 15f
                    setTypeface(typeface, Typeface.BOLD)
                    setTextColor(c.col(if (answered) R.color.on_primary else R.color.on_surface))
                    background = c.rounded(c.col(if (answered) R.color.primary else R.color.surface), 12f, c.col(if (test.flags[k]) R.color.accent else if (k == test.current) R.color.primary else R.color.outline), if (test.flags[k]) 3f else 1.5f, ripple = true)
                    layoutParams = ViewGroup.MarginLayoutParams(c.dpi(48), c.dpi(48)).apply { rightMargin = c.dpi(8); bottomMargin = c.dpi(8) }
                    setOnClickListener { sheet.dismiss(); go(k) }
                })
            }
            box.addView(flow)
            box.addView(Kit.primary(c, str(R.string.submit)) { sheet.dismiss(); confirmSubmit() })
        }
    }

    private fun confirmSubmit() {
        val left = test.keys.size - test.answeredCount
        if (left == 0) { submit(); return }
        Dialogs.confirm(ctx, str(R.string.submit_title), str(R.string.submit_left, left), str(R.string.submit), str(R.string.keep_working)) { submit() }
    }

    private fun submit() {
        if (submitted) return
        submitted = true
        timer.removeCallbacks(tick)
        services.settings.mockInProgress = null
        val now = System.currentTimeMillis()
        var correct = 0
        var xp = 0
        val detail = JSONArray()
        test.keys.forEachIndexed { k, key ->
            val ex = exercises[k] ?: return@forEachIndexed
            val ans = when (ex) { is Exercise.Choice -> ex.answer; is Exercise.FindError -> ex.answer; else -> -1 }
            val ok = test.answers[k] == ans
            if (ok) correct++
            if (test.answers[k] >= 0) xp += services.recordAnswer(ex, listOf(key), ok, 5000, 0, now)
            detail.put(JSONObject().put("k", key).put("a", test.answers[k]).put("f", test.flags[k]))
        }
        services.addStudyTime(test.elapsedSec)
        val r = services.creditXp(xp)
        if (test.answeredCount >= 5) { services.pet.meal(now, services.goalReached()); services.savePet() }
        services.settings.recentMockKeys = (test.keys + services.settings.recentMockKeys).take(test.keys.size * 2)
        val previous = services.store.history().lastOrNull { it.kind == "MOCK" && it.format == test.formatId && it.total == test.keys.size }
        services.store.addHistory("MOCK", test.title, test.formatId, test.keys.size, correct, test.elapsedSec, now, JSONObject().put("items", detail).put("xp", r.gained))
        activity.onSessionDone()
        // Interstitials never come from a mock test (policy); go straight to the result.
        services.ads.onSessionFinished(activity, lastAnswerWrong = false, fromMock = true, fromPetHome = false) {
            activity.navigator.replace(MockResultScreen(activity, test, exercises, correct, r.gained, previous?.percent))
        }
    }
}

/** S18 — mock result: score, time, estimated level (clearly labelled), breakdowns, review. */
class MockResultScreen(
    activity: MainActivity,
    private val test: MockTest,
    private val exercises: List<Exercise?>,
    private val correct: Int,
    private val xp: Int,
    private val previousPercent: Int?,
) : ScrollScreen(activity) {
    override val petMode = PetMode.HIDDEN
    override val barTitle: String get() = str(R.string.mock_result)
    private var reviewFilter = 0 // 0 wrong · 1 flagged · 2 all

    private fun answerOf(ex: Exercise?) = when (ex) { is Exercise.Choice -> ex.answer; is Exercise.FindError -> ex.answer; else -> -1 }

    override fun build(body: LinearLayout) {
        val c = ctx
        val total = test.keys.size
        val pct = correct * 100 / total.coerceAtLeast(1)
        body.addView(Kit.card(c, 20, 4, c.col(R.color.primary_container), null) {
            gravity = Gravity.CENTER_HORIZONTAL
            addView(Kit.text(c, test.title, R.style.Text_Caption).apply { gravity = Gravity.CENTER })
            addView(Kit.text(c, "$correct / $total", R.style.Text_Display, sizeSp = 40f).apply { gravity = Gravity.CENTER })
            addView(Kit.text(c, str(R.string.time_used, test.elapsedSec / 60, test.elapsedSec % 60) + "  ·  +$xp XP", R.style.Text_Body).apply { gravity = Gravity.CENTER })
            if (previousPercent != null) {
                val diff = pct - previousPercent
                addView(Kit.text(c, (if (diff >= 0) "▲ " else "▼ ") + str(R.string.vs_previous, kotlin.math.abs(diff)), R.style.Text_BodyStrong, c.col(if (diff >= 0) R.color.success else R.color.error)).apply { gravity = Gravity.CENTER }.margins(c, top = 6))
            }
        })
        // Estimated level — explicitly NOT an official score.
        if (test.sectionIds.isNotEmpty() && test.sectionLabels.isNotEmpty()) {
            body.addView(Kit.section(c, str(R.string.by_skill)))
            test.sectionLabels.forEach { (id, label) ->
                val indices = test.sectionIds.mapIndexedNotNull { i, section -> i.takeIf { section == id } }
                val sectionCorrect = indices.count { test.answers[it] == answerOf(exercises[it]) }
                body.addView(Kit.text(c, "$label  $sectionCorrect/${indices.size}", R.style.Text_Body).margins(c, bottom = 6))
            }
        }
        val est = str(when { pct >= 90 -> R.string.est_5; pct >= 75 -> R.string.est_4; pct >= 55 -> R.string.est_3; pct >= 35 -> R.string.est_2; else -> R.string.est_1 })
        body.addView(Kit.card(c, 16, 12) {
            addView(Kit.text(c, str(R.string.est_level), R.style.Text_Caption))
            addView(Kit.text(c, est, R.style.Text_Title).margins(c, top = 2))
            addView(Kit.text(c, str(R.string.est_disclaimer), R.style.Text_Caption, c.col(R.color.warning)).margins(c, top = 4))
        })
        if (pct >= 80) body.postDelayed({ activity.particles.confetti(50) }, 300)

        // Breakdown by skill / tag
        val byTag = HashMap<String, IntArray>()
        val byType = HashMap<String, IntArray>()
        exercises.forEachIndexed { k, ex ->
            ex ?: return@forEachIndexed
            val ok = test.answers[k] == answerOf(ex)
            ex.tags.filter { TagNames.has(it) }.forEach { t -> byTag.getOrPut(t) { IntArray(2) }.let { it[0]++; if (ok) it[1]++ } }
            val type = str(when (ex) { is Exercise.FindError -> R.string.type_error; is Exercise.Choice -> if (ex.passage != null) R.string.type_passage else if (ex.optionsMarked) R.string.type_sound else R.string.type_choice; else -> R.string.type_choice })
            byType.getOrPut(type) { IntArray(2) }.let { it[0]++; if (ok) it[1]++ }
        }
        body.addView(Kit.section(c, str(R.string.by_skill)))
        val card = Kit.card(c, 14, 4)
        byTag.entries.sortedBy { it.value[1] / it.value[0].toFloat() }.take(8).forEach { (t, v) -> card.addView(barRow(TagNames.label(c, t), v[1], v[0])) }
        body.addView(card)
        body.addView(Kit.section(c, str(R.string.by_qtype)))
        val card2 = Kit.card(c, 14, 4)
        byType.forEach { (t, v) -> card2.addView(barRow(t, v[1], v[0])) }
        body.addView(card2)

        body.addView(Kit.primary(c, str(R.string.practice_weak), 16) { activity.startSession(services.builder().weak(str(R.string.practice_weak), System.currentTimeMillis())) })

        // Review each question
        body.addView(Kit.section(c, str(R.string.review_each)))
        val chips = Kit.hbox(c)
        listOf(R.string.rv_wrong, R.string.rv_flagged, R.string.f_all).forEachIndexed { i, l -> chips.addView(Kit.chip(c, str(l), reviewFilter == i) { reviewFilter = i; refresh() }) }
        body.addView(Kit.hscroll(c, chips))
        exercises.forEachIndexed { k, ex ->
            ex ?: return@forEachIndexed
            val ans = answerOf(ex)
            val ok = test.answers[k] == ans
            val show = when (reviewFilter) { 0 -> !ok; 1 -> test.flags[k]; else -> true }
            if (!show) return@forEachIndexed
            val (stem, opts, expl) = when (ex) {
                is Exercise.Choice -> Triple(if (ex.passage != null) "${ex.passage.title} (${ex.blank + 1})" else ex.stem.replace("[", "").replace("]", ""), ex.options, ex.explanation)
                is Exercise.FindError -> Triple(ex.stem.replace("[", "").replace("]", ""), Regex("\\[([^\\]]+)\\]").findAll(ex.stem).map { it.groupValues[1] }.toList(), ex.explanation + " → " + ex.fix)
                else -> Triple("", emptyList(), "")
            }
            body.addView(Kit.card(c, 14, 10) {
                addView(Kit.text(c, "${k + 1}. $stem", R.style.Text_Body).apply { textSize = 15f })
                val given = test.answers[k]
                if (given >= 0 && !ok) addView(Kit.text(c, "✗ " + opts.getOrElse(given) { "" }.replace("[", "").replace("]", ""), R.style.Text_Caption, c.col(R.color.error)).margins(c, top = 4))
                if (given < 0) addView(Kit.text(c, str(R.string.unanswered), R.style.Text_Caption, c.col(R.color.warning)).margins(c, top = 4))
                addView(Kit.text(c, "✓ " + opts.getOrElse(ans) { "" }.replace("[", "").replace("]", ""), R.style.Text_BodyStrong, c.col(R.color.success)).margins(c, top = 2))
                if (expl.isNotBlank()) addView(Kit.text(c, expl, R.style.Text_Caption).margins(c, top = 4))
            })
        }
        body.addView(Kit.secondary(c, str(R.string.go_home), 20) { activity.selectTab(Tab.EXAM) })
        val pending = services.pet.state.pendingStageUp
        if (pending > 0) body.postDelayed({ if (activity.navigator.current === this) activity.showStageUp(pending) }, 1200)
    }

    private fun barRow(label: String, ok: Int, total: Int): View {
        val c = ctx
        return Kit.vbox(c).apply {
            setPadding(c.dpi(4), c.dpi(6), c.dpi(4), c.dpi(6))
            val r = Kit.hbox(c)
            r.addView(Kit.text(c, label, R.style.Text_Body).apply { textSize = 14f; layoutParams = lp(0, WRAP_CONTENT, 1f) })
            r.addView(Kit.text(c, "$ok/$total", R.style.Text_BodyStrong).apply { textSize = 14f })
            addView(r)
            val pct = ok / total.toFloat().coerceAtLeast(1f)
            addView(Bar(c).apply {
                color = c.col(if (pct >= 0.8f) R.color.success else if (pct >= 0.6f) R.color.accent else R.color.error)
                layoutParams = lp(h = c.dpi(6)).apply { topMargin = c.dpi(4) }
                post { set(pct) }
            })
        }
    }

    override fun onBack(): Boolean { activity.navigator.pop(); return true }
}
