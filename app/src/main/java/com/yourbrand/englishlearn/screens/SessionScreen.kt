package com.yourbrand.englishlearn.screens

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.SystemClock
import android.text.InputType
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.yourbrand.englishlearn.BuildConfig
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.core.Sfx
import com.yourbrand.englishlearn.learning.Exercise
import com.yourbrand.englishlearn.learning.Grader
import com.yourbrand.englishlearn.learning.Kind
import com.yourbrand.englishlearn.learning.Scheduler
import com.yourbrand.englishlearn.learning.Session
import com.yourbrand.englishlearn.learning.SessionKind
import com.yourbrand.englishlearn.pet.Mood
import com.yourbrand.englishlearn.pet.PetView
import com.yourbrand.englishlearn.ui.*
import com.yourbrand.englishlearn.ui.views.SegmentedProgress

/** Result of one answered exercise (for the result screen and review list). */
data class Answer(val ex: Exercise, val correct: Boolean, val given: String?, val xp: Int)

/**
 * S16 — shared by every practice mode. Layout: top bar (close, segmented progress, mini pet, chip) →
 * prompt → answer area → "Kiểm tra"/"Tiếp tục" + slide-up feedback panel. No banner, no pet bubbles.
 */
class SessionScreen(activity: MainActivity, private val session: Session) : Screen(activity) {
    override val petMode = PetMode.MINI

    private val queue = session.exercises.toMutableList()
    private val requeued = HashSet<String>()
    private var index = 0
    private val answers = ArrayList<Answer>()
    private var combo = 0
    private var bestCombo = 0
    private var wrongRun = 0
    private var cheerBubble: TextView? = null
    private var fastStreak = 0
    private var totalXp = 0
    private var shownAt = 0L
    private var startedAt = SystemClock.elapsedRealtime()
    private var hintUsed = false
    private var lastWrong = false
    private var finished = false

    private lateinit var progress: SegmentedProgress
    private lateinit var miniPet: PetView
    private lateinit var chip: TextView
    private lateinit var content: LinearLayout
    private lateinit var scroll: ScrollView
    private lateinit var action: TextView
    private lateinit var hintBtn: TextView
    private lateinit var panel: LinearLayout
    private lateinit var bottomBar: LinearLayout

    /** Current input state, set by the renderer of each exercise type. */
    private var check: (() -> Unit)? = null
    private var onHint: (() -> Unit)? = null
    private val hintRunnable = Runnable { if (!answered) { hintBtn.show(onHint != null); hintBtn.enter(distanceDp = 6f) } }
    private var answered = false

    // Speed round
    private var speedLeftMs = 60_000L
    private val speedTick = object : Runnable {
        override fun run() {
            if (finished) return
            speedLeftMs -= 250
            chip.text = str(R.string.seconds_left, (speedLeftMs / 1000).coerceAtLeast(0))
            if (speedLeftMs <= 10_000 && !ctx.reduceMotion) chip.animate().scaleX(1.1f).scaleY(1.1f).setDuration(120).withEndAction { chip.animate().scaleX(1f).scaleY(1f).setDuration(120).start() }.start()
            if (speedLeftMs <= 0) finish() else chip.postDelayed(this, 250)
        }
    }
    private val timerTick = object : Runnable {
        override fun run() {
            if (finished) return
            val s = ((SystemClock.elapsedRealtime() - startedAt) / 1000).toInt()
            chip.text = "%d:%02d".format(s / 60, s % 60)
            chip.postDelayed(this, 1000)
        }
    }

    override fun onCreateView(parent: ViewGroup): View {
        val c = ctx
        val root = FrameLayout(c)
        val col = Kit.vbox(c).apply { layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT) }
        // Top bar
        val top = Kit.hbox(c) { setPadding(c.dpi(4), c.dpi(4), c.dpi(12), c.dpi(4)); minimumHeight = c.dpi(56) }
        top.addView(ImageView(c).apply {
            setImageResource(R.drawable.ic_close); tintRes(R.color.on_surface)
            setBackgroundResource(R.drawable.ripple_circle)
            val p = c.dpi(12); setPadding(p, p, p, p)
            contentDescription = str(R.string.close)
            layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48))
            setOnClickListener { askClose() }
        })
        progress = SegmentedProgress(c).apply { layoutParams = lp(0, c.dpi(10), 1f).apply { marginStart = c.dpi(4) }; setup(queue.size) }
        top.addView(progress)
        miniPet = PetView(c).apply {
            crop = true; shadow = false
            setFrom(services.pet.state); mood = Mood.IDLE
            layoutParams = LinearLayout.LayoutParams(c.dpi(34), c.dpi(34)).apply { marginStart = c.dpi(10) }
            importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
        }
        top.addView(miniPet)
        chip = Kit.badge(c, "", c.col(R.color.accent_container), c.col(R.color.on_surface)).apply {
            textSize = 13f
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { marginStart = c.dpi(8) }
            visibility = View.GONE
        }
        top.addView(chip)
        col.addView(top)

        scroll = ScrollView(c).apply { isFillViewport = true; layoutParams = lp(MATCH_PARENT, 0, 1f) }
        content = Kit.vbox(c).apply { setPadding(c.dpi(20), c.dpi(8), c.dpi(20), c.dpi(24)) }
        val center = FrameLayout(c)
        center.addView(content, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.CENTER_HORIZONTAL))
        center.addOnLayoutChangeListener { v, l, _, r, _, _, _, _, _ ->
            val maxW = c.dpi(640); val want = if (r - l > maxW) maxW else MATCH_PARENT
            val p = content.layoutParams as FrameLayout.LayoutParams
            if (p.width != want) { p.width = want; v.post { content.layoutParams = p } }
        }
        scroll.addView(center)
        col.addView(scroll)

        bottomBar = Kit.hbox(c) { setPadding(c.dpi(20), c.dpi(8), c.dpi(20), c.dpi(16)) }
        hintBtn = Kit.secondary(c, "💡") { onHint?.invoke(); hintBtn.show(false); hintUsed = true }.apply {
            layoutParams = LinearLayout.LayoutParams(c.dpi(56), c.dpi(52)).apply { marginEnd = c.dpi(10) }
            contentDescription = str(R.string.hint)
            visibility = View.GONE
        }
        bottomBar.addView(hintBtn)
        action = Kit.primary(c, str(R.string.check), 0) { onAction() }.apply { layoutParams = lp(0, c.dpi(52), 1f) }
        bottomBar.addView(action)
        col.addView(bottomBar)
        root.addView(col)

        panel = Kit.vbox(c, 20).apply {
            background = c.getDrawable(R.drawable.bg_sheet)
            elevation = c.dp(16f)
            visibility = View.GONE
            isClickable = true
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.BOTTOM)
        }
        root.addView(panel)

        if (session.speed) { chip.show(true); chip.text = str(R.string.seconds_left, 60); chip.postDelayed(speedTick, 250) }
        if (session.timed) { chip.show(true); chip.post(timerTick) }
        return root
    }

    override fun onShown(firstTime: Boolean) { if (firstTime) render() }

    override fun onBack(): Boolean { askClose(); return true }

    override fun onPause() { if (!finished) services.addStudyTime(((SystemClock.elapsedRealtime() - startedAt) / 1000).toInt()); startedAt = SystemClock.elapsedRealtime() }

    override fun onDestroy() {
        chip.removeCallbacks(speedTick); chip.removeCallbacks(timerTick); content.removeCallbacks(hintRunnable)
        cheerBubble?.let { activity.overlay.removeView(it) }; cheerBubble = null
        services.tts.stop()
    }

    private fun askClose() {
        if (finished) return
        if (index.toFloat() / queue.size.coerceAtLeast(1) <= 0.3f) { activity.navigator.pop(); return }
        Dialogs.confirm(ctx, str(R.string.quit_title), str(R.string.quit_msg), str(R.string.quit_keep), str(R.string.quit_leave), onNegative = {
            finished = true
            activity.navigator.pop()
        }) {}
    }

    // ---- rendering --------------------------------------------------------------------------

    private fun render() {
        if (index >= queue.size) { finish(); return }
        val ex = queue[index]
        answered = false
        hintUsed = false
        check = null; onHint = null
        hintBtn.show(false)
        content.removeCallbacks(hintRunnable)
        content.postDelayed(hintRunnable, 8000)
        panel.show(false)
        bottomBar.show(true)
        action.text = str(R.string.check)
        action.isEnabled = false
        progress.grow(queue.size)
        progress.setCurrent(index)
        miniPet.mood = Mood.IDLE
        content.removeAllViews()
        scroll.scrollTo(0, 0)
        shownAt = SystemClock.elapsedRealtime()
        when (ex) {
            is Exercise.Choice -> renderChoice(ex)
            is Exercise.FindError -> renderFindError(ex)
            is Exercise.WordOrder -> renderWordOrder(ex)
            is Exercise.Matching -> renderMatching(ex)
            is Exercise.Flashcard -> renderFlashcard(ex)
            is Exercise.Spelling -> renderSpelling(ex)
        }
        content.staggerChildren(30)
    }

    private fun instruction(res: Int): TextView = Kit.text(ctx, str(res), R.style.Text_Caption).apply { textSize = 14f }

    private fun questionText(text: CharSequence): TextView = Kit.text(ctx, text, R.style.Text_Question).apply {
        textSize = 20f * services.settings.questionScale
        setTextIsSelectable(false)
    }.margins(ctx, top = 10)

    private fun speaker(text: String, big: Boolean = false): ImageView = ImageView(ctx).apply {
        setImageResource(R.drawable.ic_volume); tintRes(R.color.primary)
        background = ctx.rounded(ctx.col(R.color.primary_container), 100f, ripple = true)
        val p = ctx.dpi(if (big) 22 else 10); setPadding(p, p, p, p)
        contentDescription = str(R.string.listen)
        layoutParams = LinearLayout.LayoutParams(ctx.dpi(if (big) 88 else 44), ctx.dpi(if (big) 88 else 44))
        setOnClickListener { speak(text, it) }
        setOnLongClickListener { services.tts.speak(text, slower = true); true }
    }

    private fun speak(text: String, v: View? = null) {
        services.tts.speak(text)
        if (v != null && !ctx.reduceMotion) v.animate().scaleX(1.12f).scaleY(1.12f).setDuration(140).withEndAction { v.animate().scaleX(1f).scaleY(1f).setDuration(160).start() }.start()
    }

    // -- Choice (E01/E02/E09/E11/E12/E14 + passages + pronunciation) --

    private fun renderChoice(ex: Exercise.Choice) {
        val c = ctx
        content.addView(instruction(ex.instruction))
        var stemView: TextView? = null
        when {
            ex.passage != null -> {
                content.addView(Kit.text(c, ex.passage.title, R.style.Text_BodyStrong).margins(c, top = 8))
                val filled = HashMap<Int, String>()
                for (i in 0 until ex.blank) { val b = ex.passage.blanks[i]; filled[i] = b.options[b.answer] }
                val passageCard = Kit.card(c, 16, 10, c.col(R.color.surface_variant), null)
                stemView = Kit.text(c, Spans.passage(ex.passage.text, filled, ex.blank, c.col(R.color.primary), c.col(R.color.primary_container), c.col(R.color.success)), R.style.Text_Body).apply {
                    textSize = 15f * services.settings.questionScale
                }
                passageCard.addView(stemView)
                content.addView(passageCard)
                content.addView(Kit.text(c, str(R.string.blank_n, ex.blank + 1), R.style.Text_Caption).margins(c, top = 12))
            }
            ex.audioOnly -> {
                val box = Kit.vbox(c) { gravity = Gravity.CENTER_HORIZONTAL; setPadding(0, c.dpi(20), 0, c.dpi(8)) }
                box.addView(speaker(ex.speak.orEmpty(), big = true))
                box.addView(Kit.text(c, str(R.string.tap_to_replay), R.style.Text_Caption).apply { gravity = Gravity.CENTER }.margins(c, top = 8))
                content.addView(box)
                content.post { speak(ex.speak.orEmpty()) }
            }
            ex.stem.isNotBlank() -> {
                // The top gap goes on the row: a top margin on a child of a CENTER_VERTICAL row shifts
                // it down by half the margin past the row's bottom and clipped the descenders (y, g).
                val row = Kit.hbox(c).margins(c, top = 10)
                stemView = questionText(if (ex.stem.contains("___")) Spans.blank(ex.stem, null, c.col(R.color.primary), c.col(R.color.primary_container)) else Spans.underline(ex.stem)).apply {
                    layoutParams = lp(0, WRAP_CONTENT, 1f)
                    if (ex.kind == Kind.E01 && ex.wordId != null && ex.instruction == R.string.ins_meaning) { textSize = 28f * services.settings.questionScale; setTypeface(typeface, Typeface.BOLD) }
                }
                row.addView(stemView)
                if (ex.speak != null && ex.wordId != null) row.addView(speaker(ex.speak).apply { (layoutParams as LinearLayout.LayoutParams).marginStart = c.dpi(8) })
                content.addView(row)
                if (ex.hint != null) content.addView(Kit.text(c, ex.hint, R.style.Text_Caption).margins(c, top = 2))
            }
        }
        val optionsBox = Kit.vbox(c).margins(c, top = 16)
        content.addView(optionsBox)
        var selected = -1
        val cards = ex.options.mapIndexed { i, opt ->
            OptionCard(c, i, if (ex.optionsMarked) Spans.underline(opt) else opt).also { card ->
                optionsBox.addView(card.view)
                card.view.onTap {
                    if (answered || !card.enabled) return@onTap
                    selected = i
                    services.sfx.play(Sfx.Sound.TAP)
                    cardsRef.forEachIndexed { j, oc -> oc.setState(if (j == i) OptionCard.State.SELECTED else OptionCard.State.DEFAULT) }
                    if (ex.stem.contains("___") && stemView != null && ex.passage == null) stemView.text = Spans.blank(ex.stem, opt, c.col(R.color.primary), c.col(R.color.primary_container))
                    action.isEnabled = true
                    if (session.speed) check?.invoke()
                }
            }
        }
        cardsRef = cards
        check = {
            val ok = selected == ex.answer
            cards.forEachIndexed { j, oc ->
                oc.setState(when { j == ex.answer -> OptionCard.State.CORRECT; j == selected -> OptionCard.State.WRONG; else -> OptionCard.State.DIMMED })
            }
            if (!ok) cards.getOrNull(selected)?.view?.shake() else cards[selected].view.bump()
            if (!ok) cards[ex.answer].pulse()
            if (ex.stem.contains("___") && stemView != null && ex.passage == null) stemView.text = Spans.blank(ex.stem, ex.options[ex.answer], c.col(if (ok) R.color.success else R.color.primary), c.col(if (ok) R.color.success_container else R.color.primary_container))
            onAnswered(ex, ok, ex.options.getOrNull(selected), cards[ex.answer].view, correctText = ex.options[ex.answer], explanation = ex.explanation, speakText = ex.speak)
        }
        onHint = {
            // Eliminate one wrong option (no penalty, only no bonus XP).
            cards.indices.filter { it != ex.answer && it != selected && cards[it].enabled }.randomOrNull()?.let { cards[it].setState(OptionCard.State.DISABLED) }
        }
    }

    private var cardsRef: List<OptionCard> = emptyList()

    // -- Find the error (E04) --

    private fun renderFindError(ex: Exercise.FindError) {
        val c = ctx
        content.addView(instruction(R.string.ins_find_error))
        val parts = Regex("\\[([^\\]]+)\\]").findAll(ex.stem).toList()
        val flow = Kit.flow(c).margins(c, top = 14)
        var last = 0
        val segs = ArrayList<TextView>()
        var selected = -1
        fun plain(s: String) { s.trim().split(Regex("\\s+")).filter { it.isNotEmpty() }.forEach { w -> flow.addView(Kit.text(c, w, R.style.Text_Question).apply {
            textSize = 19f * services.settings.questionScale
            layoutParams = ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { rightMargin = c.dpi(6); bottomMargin = c.dpi(10) }
        }) } }
        parts.forEachIndexed { i, m ->
            plain(ex.stem.substring(last, m.range.first))
            val seg = TextView(c).apply {
                text = m.groupValues[1]
                textSize = 19f * services.settings.questionScale
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(c.col(R.color.on_surface))
                setPadding(c.dpi(10), c.dpi(6), c.dpi(10), c.dpi(6))
                background = c.rounded(c.col(R.color.surface), 10f, c.col(R.color.outline), 1.5f, ripple = true)
                layoutParams = ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { rightMargin = c.dpi(6); bottomMargin = c.dpi(10) }
                contentDescription = "${'A' + i}: ${m.groupValues[1]}"
                onTap {
                    if (answered) return@onTap
                    selected = i
                    segs.forEachIndexed { j, t -> t.background = c.rounded(if (j == i) c.col(R.color.primary_container) else c.col(R.color.surface), 10f, c.col(if (j == i) R.color.primary else R.color.outline), if (j == i) 2f else 1.5f, ripple = true) }
                    action.isEnabled = true
                }
            }
            segs += seg
            flow.addView(seg)
            last = m.range.last + 1
        }
        plain(ex.stem.substring(last))
        content.addView(flow)
        val letters = Kit.text(c, str(R.string.tap_wrong_part), R.style.Text_Caption).margins(c, top = 4)
        content.addView(letters)
        check = {
            val ok = selected == ex.answer
            segs.forEachIndexed { j, t ->
                val (fill, stroke) = when { j == ex.answer -> R.color.error_container to R.color.error; j == selected -> R.color.surface_variant to R.color.outline; else -> R.color.surface to R.color.outline }
                t.background = c.rounded(c.col(fill), 10f, c.col(stroke), 2f)
            }
            if (ok) segs[ex.answer].bump() else segs.getOrNull(selected)?.shake()
            onAnswered(ex, ok, segs.getOrNull(selected)?.text?.toString(), segs[ex.answer], correctText = str(R.string.fix_to, segs[ex.answer].text, ex.fix), explanation = ex.explanation)
        }
        onHint = { letters.text = str(R.string.hint_find_error) }
    }

    // -- Word order (E05) --

    private fun renderWordOrder(ex: Exercise.WordOrder) {
        val c = ctx
        content.addView(instruction(R.string.ins_word_order))
        if (ex.translation != null) content.addView(questionText(ex.translation).apply { textSize = 18f * services.settings.questionScale })
        val answerArea = Kit.flow(c).apply {
            minimumHeight = c.dpi(110)
            setPadding(c.dpi(10), c.dpi(10), c.dpi(10), c.dpi(4))
            background = c.rounded(c.col(R.color.surface_variant), 14f)
        }.margins(c, top = 16)
        val pool = Kit.flow(c).margins(c, top = 18)
        content.addView(answerArea)
        content.addView(pool)
        val built = ArrayList<TextView>()
        fun chip(word: String): TextView = TextView(c).apply {
            text = word; textSize = 17f
            setTextColor(c.col(R.color.on_surface))
            setPadding(c.dpi(14), c.dpi(10), c.dpi(14), c.dpi(10))
            background = c.rounded(c.col(R.color.surface), 12f, c.col(R.color.outline), 1.5f, ripple = true)
            elevation = c.dp(1f)
            layoutParams = ViewGroup.MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { rightMargin = c.dpi(8); bottomMargin = c.dpi(8) }
        }
        ex.chips.forEach { w ->
            val inPool = chip(w)
            inPool.onTap {
                if (answered || inPool.alpha < 1f) return@onTap
                val placed = chip(w)
                placed.onTap {
                    if (answered) return@onTap
                    answerArea.removeView(placed); built.remove(placed); inPool.alpha = 1f; inPool.isEnabled = true
                    action.isEnabled = built.size == ex.chips.size
                }
                answerArea.addView(placed); built += placed
                placed.pop(0.8f)
                inPool.alpha = 0.25f
                services.sfx.play(Sfx.Sound.TAP)
                action.isEnabled = built.size == ex.chips.size
            }
            pool.addView(inPool)
        }
        check = {
            val ok = Grader.sameSentence(built.map { it.text.toString() }, ex.sentence)
            answerArea.background = c.rounded(c.col(if (ok) R.color.success_container else R.color.error_container), 14f, c.col(if (ok) R.color.success else R.color.error), 1.5f)
            if (ok) answerArea.bump() else answerArea.shake()
            onAnswered(ex, ok, built.joinToString(" ") { it.text }, answerArea, correctText = ex.sentence, explanation = "", speakText = ex.sentence)
        }
        onHint = {
            // Place the first correct chip.
            val first = Grader.chips(ex.sentence).firstOrNull()
            if (built.isEmpty() && first != null) (0 until pool.childCount).map { pool.getChildAt(it) as TextView }.firstOrNull { it.text == first && it.alpha == 1f }?.performClick()
        }
    }

    // -- Matching (E06) --

    private fun renderMatching(ex: Exercise.Matching) {
        val c = ctx
        content.addView(instruction(R.string.ins_matching))
        val row = Kit.hbox(c) { gravity = Gravity.TOP }.margins(c, top = 16)
        val left = Kit.vbox(c).apply { layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginEnd = c.dpi(6) } }
        val right = Kit.vbox(c).apply { layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(6) } }
        row.addView(left); row.addView(right)
        content.addView(row)
        var selLeft = -1; var selRight = -1
        val matched = BooleanArray(ex.left.size)
        val mistakes = BooleanArray(ex.left.size)
        lateinit var lViews: List<TextView>
        lateinit var rViews: List<TextView>
        fun tile(text: String) = TextView(c).apply {
            this.text = text; textSize = 15f; gravity = Gravity.CENTER
            minHeight = c.dpi(64)
            setPadding(c.dpi(8), c.dpi(8), c.dpi(8), c.dpi(8))
            setTextColor(c.col(R.color.on_surface))
            background = c.rounded(c.col(R.color.surface), 14f, c.col(R.color.outline), 1.5f, ripple = true)
            layoutParams = lp().apply { bottomMargin = c.dpi(10) }
        }
        fun paint() {
            lViews.forEachIndexed { i, v -> if (!matched[i]) v.background = c.rounded(c.col(if (i == selLeft) R.color.primary_container else R.color.surface), 14f, c.col(if (i == selLeft) R.color.primary else R.color.outline), if (i == selLeft) 2f else 1.5f, ripple = true) }
            rViews.forEachIndexed { i, v -> if (ex.solution.indexOf(i).let { l -> l < 0 || !matched[l] }) v.background = c.rounded(c.col(if (i == selRight) R.color.primary_container else R.color.surface), 14f, c.col(if (i == selRight) R.color.primary else R.color.outline), if (i == selRight) 2f else 1.5f, ripple = true) }
        }
        fun tryPair() {
            if (selLeft < 0 || selRight < 0) return
            val l = selLeft; val r = selRight
            selLeft = -1; selRight = -1
            if (ex.solution[l] == r) {
                matched[l] = true
                for (v in listOf(lViews[l], rViews[r])) { v.background = c.rounded(c.col(R.color.success_container), 14f, c.col(R.color.success), 1.5f); v.isEnabled = false; v.bump() }
                services.sfx.play(Sfx.Sound.CORRECT)
                if (l < ex.left.size) speak(ex.left[l])
                paint()
                if (matched.all { it }) {
                    val keysOk = ex.keys.filterIndexed { i, _ -> !mistakes[i] }
                    onMatchingDone(ex, keysOk, lViews[l])
                }
            } else {
                mistakes[l] = true
                services.sfx.play(Sfx.Sound.WRONG)
                lViews[l].shake(); rViews[r].shake()
                lViews[l].hapticResult(false)
                paint()
            }
        }
        lViews = ex.left.mapIndexed { i, t -> tile(t).also { v -> v.setTypeface(v.typeface, Typeface.BOLD); v.onTap { if (!matched[i]) { selLeft = i; paint(); tryPair() } }; left.addView(v) } }
        rViews = ex.right.mapIndexed { i, t -> tile(t).also { v -> v.onTap { selRight = i; paint(); tryPair() }; right.addView(v) } }
        bottomBar.show(false)
    }

    private fun onMatchingDone(ex: Exercise.Matching, okKeys: List<String>, from: View) {
        answered = true
        val now = System.currentTimeMillis()
        var xp = 0
        ex.keys.forEach { key ->
            val ok = key in okKeys
            xp += services.recordAnswer(ex, listOf(key), ok, 2000, 0, now)
        }
        val allOk = okKeys.size == ex.keys.size
        progress.mark(index, allOk)
        answers += Answer(ex, allOk, null, xp)
        creditXp(xp, from)
        combo = if (allOk) combo + 1 else 0
        miniPet.mood = if (allOk) Mood.CHEER else Mood.OOPS
        miniPet.react(miniPet.mood)
        cheer(allOk)
        content.postDelayed({ index++; render() }, 700)
    }

    // -- Flashcard (E07) --

    private fun renderFlashcard(ex: Exercise.Flashcard) {
        val c = ctx
        val w = ex.word
        content.addView(instruction(R.string.ins_flashcard))
        val card = Kit.card(c, 24, 16).apply {
            gravity = Gravity.CENTER_HORIZONTAL
            minimumHeight = c.dpi(260)
            elevation = c.dp(3f)
        }
        fun front() {
            card.removeAllViews()
            card.addView(Kit.text(c, w.lemma, R.style.Text_Display, sizeSp = 32f * services.settings.questionScale).apply { gravity = Gravity.CENTER }.margins(c, top = 30))
            card.addView(Kit.text(c, "/${w.ipa}/ · ${w.pos}", R.style.Text_Caption).apply { gravity = Gravity.CENTER }.margins(c, top = 6))
            card.addView(speaker(w.lemma).apply { (layoutParams as LinearLayout.LayoutParams).apply { gravity = Gravity.CENTER_HORIZONTAL; topMargin = c.dpi(18) } })
            card.addView(Kit.text(c, str(R.string.tap_to_flip), R.style.Text_Caption).apply { gravity = Gravity.CENTER }.margins(c, top = 18))
        }
        fun back() {
            card.removeAllViews()
            card.addView(Kit.text(c, w.lemma, R.style.Text_Title).apply { gravity = Gravity.CENTER })
            w.senses.take(3).forEach { s ->
                card.addView(Kit.text(c, "(${s.pos}) ${s.gloss}", R.style.Text_BodyStrong, c.col(R.color.primary), 18f).apply { gravity = Gravity.CENTER }.margins(c, top = 10))
                s.examples.firstOrNull()?.let { e ->
                    card.addView(Kit.text(c, e.text, R.style.Text_Body).apply { gravity = Gravity.CENTER; textSize = 15f }.margins(c, top = 4))
                    if (services.settings.showTranslation && e.vi != null) card.addView(Kit.text(c, e.vi, R.style.Text_Caption).apply { gravity = Gravity.CENTER })
                }
            }
        }
        front()
        content.addView(card)
        var flipped = false
        val rating = Kit.hbox(c).margins(c, top = 16)
        val labels = listOf(R.string.rate_again to R.color.error, R.string.rate_hard to R.color.warning, R.string.rate_good to R.color.primary, R.string.rate_easy to R.color.success)
        labels.forEachIndexed { i, (label, color) ->
            rating.addView(TextView(c).apply {
                text = str(label); gravity = Gravity.CENTER; textSize = 15f
                setTypeface(typeface, Typeface.BOLD)
                setTextColor(c.col(color))
                background = c.rounded(blend(c.col(color), c.col(R.color.surface), 0.85f), 14f, ripple = true)
                layoutParams = lp(0, c.dpi(52), 1f).apply { if (i > 0) marginStart = c.dpi(8) }
                onTap { if (!answered) rateFlashcard(ex, i, this) }
            })
        }
        rating.visibility = View.INVISIBLE
        content.addView(rating)
        card.isClickable = true
        card.setOnClickListener {
            if (flipped) return@setOnClickListener
            flipped = true
            it.haptic()
            if (c.reduceMotion) { back(); rating.visibility = View.VISIBLE; return@setOnClickListener }
            card.cameraDistance = c.dp(8000f)
            card.animate().rotationY(90f).setDuration(110).withEndAction {
                back(); card.rotationY = -90f
                card.animate().rotationY(0f).setDuration(110).start()
                rating.visibility = View.VISIBLE; rating.enter()
            }.start()
            speak(w.lemma)
        }
        bottomBar.show(false)
    }

    private fun rateFlashcard(ex: Exercise.Flashcard, rating: Int, from: View) {
        answered = true
        val now = System.currentTimeMillis()
        val ok = rating >= 2
        // Normal pipeline (XP, tags, box ±), then "Khó"/"Dễ" nudge the box one step.
        val xp = services.recordAnswer(ex, listOf(ex.key), ok, SystemClock.elapsedRealtime() - shownAt, fastStreak, now)
        if (rating == 1 || rating == 3) {
            val s = services.store.itemOrNew(ex.key)
            s.box = (s.box + if (rating == 3) 1 else -1).coerceIn(1, Scheduler.MAX_BOX)
            s.due = now + Scheduler.interval(s.box)
            services.store.saveItem(s)
        }
        progress.mark(index, ok)
        answers += Answer(ex, ok, null, xp)
        creditXp(xp, from)
        content.postDelayed({ index++; render() }, 250)
    }

    // -- Spelling (E08) --

    private fun renderSpelling(ex: Exercise.Spelling) {
        val c = ctx
        content.addView(instruction(R.string.ins_spelling))
        content.addView(questionText(ex.gloss).apply { textSize = 22f * services.settings.questionScale })
        val pattern = ex.word.lemma.mapIndexed { i, ch -> if (i == 0 || ch == '-' || ch == '\'') ch else '_' }.joinToString(" ")
        content.addView(Kit.text(c, pattern, R.style.Text_Title, c.col(R.color.muted)).apply { letterSpacing = 0.1f }.margins(c, top = 8))
        ex.example?.let { if (services.settings.showTranslation) content.addView(Kit.text(c, "“$it”", R.style.Text_Caption).margins(c, top = 8)) }
        val input = EditText(c).apply {
            hint = str(R.string.type_here)
            textSize = 20f
            inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            imeOptions = EditorInfo.IME_ACTION_DONE
            setSingleLine()
            setPadding(c.dpi(16), c.dpi(14), c.dpi(16), c.dpi(14))
            background = c.rounded(c.col(R.color.surface), 14f, c.col(R.color.outline), 1.5f)
            layoutParams = lp().apply { topMargin = c.dpi(18) }
            tag = "petAvoid"
            addTextChangedListener(object : android.text.TextWatcher {
                override fun afterTextChanged(s: android.text.Editable?) { action.isEnabled = !s.isNullOrBlank() && !answered }
                override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, d: Int) {}
                override fun onTextChanged(s: CharSequence?, a: Int, b: Int, d: Int) {}
            })
            setOnEditorActionListener { _, id, _ -> if (id == EditorInfo.IME_ACTION_DONE && action.isEnabled) { onAction(); true } else false }
        }
        content.addView(input)
        input.post { input.requestFocus(); (c.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager).showSoftInput(input, 0) }
        check = {
            val r = Grader.grade(input.text.toString(), ex.word.lemma)
            val ok = r != Grader.Result.WRONG
            input.isEnabled = false
            input.background = c.rounded(c.col(when (r) { Grader.Result.CORRECT -> R.color.success_container; Grader.Result.ALMOST -> R.color.warning_container; else -> R.color.error_container }), 14f,
                c.col(when (r) { Grader.Result.CORRECT -> R.color.success; Grader.Result.ALMOST -> R.color.warning; else -> R.color.error }), 1.5f)
            (c.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(input.windowToken, 0)
            if (r == Grader.Result.WRONG) input.shake() else input.bump()
            onAnswered(ex, ok, input.text.toString(), input, correctText = ex.word.lemma + " /" + ex.word.ipa + "/", explanation = ex.word.allGlosses,
                speakText = ex.word.lemma, almost = r == Grader.Result.ALMOST)
        }
        onHint = { if (input.text.isEmpty()) { input.setText(ex.word.lemma.take(2)); input.setSelection(input.text.length) } }
    }

    // ---- answering ----------------------------------------------------------------------------

    private fun onAction() {
        if (!answered) check?.invoke() else { index++; render() }
    }

    private fun onAnswered(ex: Exercise, correct: Boolean, given: String?, anchor: View, correctText: String, explanation: String, speakText: String? = null, almost: Boolean = false) {
        if (answered) return
        answered = true
        content.removeCallbacks(hintRunnable)
        hintBtn.show(false)
        val elapsed = SystemClock.elapsedRealtime() - shownAt
        fastStreak = if (elapsed < 800) fastStreak + 1 else 0
        lastWrong = !correct
        val now = System.currentTimeMillis()
        var xp = 0
        if (session.speed) {
            // E13: bonus-only XP with a combo multiplier, no schedule changes.
            if (correct) xp = (3 * (1 + combo / 5)).coerceAtMost(9)
            services.store.addToDay(services.today(now), answered = 1, correct = if (correct) 1 else 0)
        } else {
            xp = services.recordAnswer(ex, listOf(ex.key), correct, elapsed, fastStreak, now)
            if (hintUsed) xp /= 2
        }
        progress.mark(index, correct)
        answers += Answer(ex, correct, given, xp)
        // Combo
        if (correct) { combo++; bestCombo = maxOf(bestCombo, combo) } else combo = 0
        // Effects
        anchor.hapticResult(correct)
        services.sfx.play(when { almost -> Sfx.Sound.ALMOST; correct && combo in listOf(3, 5, 10) -> Sfx.Sound.COMBO; correct -> Sfx.Sound.CORRECT; else -> Sfx.Sound.WRONG })
        miniPet.mood = if (almost) Mood.THINKING else if (correct) Mood.CHEER else Mood.OOPS
        miniPet.react(miniPet.mood)
        if (correct && combo in listOf(3, 5, 10)) showCombo()
        cheer(correct)
        creditXp(xp, anchor)
        // Re-queue a wrong item once, 3–4 items later.
        if (!correct && !session.speed && ex.key !in requeued && ex !is Exercise.Matching) {
            requeued += ex.key
            val at = (index + 4).coerceAtMost(queue.size)
            services.builder().exerciseFor(ex.key)?.let { queue.add(at, it) } ?: queue.add(at, ex)
        }
        if (session.speed || session.timed) {
            content.postDelayed({ if (!finished) { index++; render() } }, if (session.speed) 350 else 650)
            return
        }
        showPanel(ex, correct, almost, correctText, explanation, speakText)
    }

    private fun creditXp(xp: Int, from: View) {
        if (xp <= 0) return
        totalXp += xp
        val r = services.creditXp(xp)
        activity.flyXp(from, xp, miniPet)
        if (r.stageUp != null) miniPet.setFrom(services.pet.state)
    }

    /**
     * The mini pet praises a streak (3/5/10 in a row) or a comeback after two misses, and
     * encourages after two misses in a row. Never during timed tests; respects the bubble setting.
     */
    private fun cheer(correct: Boolean) {
        val prevWrong = wrongRun
        wrongRun = if (correct) 0 else wrongRun + 1
        if (session.timed || !services.settings.petBubbles) return
        val key = when {
            correct && combo == 10 -> "S_COMBO10"
            correct && combo == 5 -> "S_COMBO5"
            correct && combo == 3 -> "S_COMBO3"
            correct && prevWrong >= 2 -> "S_COMEBACK"
            !correct && wrongRun == 2 -> "S_WRONG2"
            else -> return
        }
        val line = services.content.petLines[key].orEmpty().randomOrNull()?.replace("{name}", services.pet.state.name) ?: return
        // Let the combo badge finish first so the two never overlap.
        val delay = if (key.startsWith("S_COMBO")) 1100L else 150L
        miniPet.postDelayed({ if (!finished) showCheer(line) }, delay)
    }

    private fun showCheer(line: String) {
        val c = ctx
        cheerBubble?.let { activity.overlay.removeView(it) }
        val loc = IntArray(2); miniPet.getLocationInWindow(loc)
        val o = IntArray(2); activity.overlay.getLocationInWindow(o)
        val bubble = TextView(c).apply {
            text = line
            textSize = 14f
            setTextColor(c.col(R.color.on_surface))
            maxWidth = c.dpi(220)
            setPadding(c.dpi(12), c.dpi(8), c.dpi(12), c.dpi(8))
            background = c.rounded(c.col(R.color.surface), 16f, c.col(R.color.outline), 1f)
            elevation = c.dp(6f)
            accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_POLITE
            pivotY = 0f
        }
        val endGap = (activity.overlay.width - (loc[0] - o[0] + miniPet.width)).coerceAtLeast(c.dpi(8))
        activity.overlay.addView(bubble, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.TOP or Gravity.END).apply {
            topMargin = loc[1] - o[1] + miniPet.height + c.dpi(6)
            marginEnd = endGap - c.dpi(8)
        })
        cheerBubble = bubble
        bubble.pop(0.7f)
        bubble.postDelayed({
            bubble.animate().alpha(0f).setDuration(200).withEndAction {
                activity.overlay.removeView(bubble)
                if (cheerBubble === bubble) cheerBubble = null
            }.start()
        }, 2400)
    }

    private fun showCombo() {
        val c = ctx
        val badge = Kit.badge(c, "🔥 x$combo", c.col(R.color.accent), c.col(R.color.on_accent)).apply { textSize = 16f }
        activity.overlay.addView(badge, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.TOP or Gravity.CENTER_HORIZONTAL).apply { topMargin = c.dpi(90) })
        badge.pop(0.4f)
        val loc = IntArray(2); progress.getLocationInWindow(loc)
        activity.particles.burst(c.resources.displayMetrics.widthPixels / 2f, c.dpi(110).toFloat(), 14)
        badge.postDelayed({ badge.animate().alpha(0f).translationY(-c.dp(20f)).setDuration(250).withEndAction { activity.overlay.removeView(badge) }.start() }, 900)
    }

    private fun showPanel(ex: Exercise, correct: Boolean, almost: Boolean, correctText: String, explanation: String, speakText: String?) {
        val c = ctx
        panel.removeAllViews()
        val color = when { almost -> R.color.warning; correct -> R.color.success; else -> R.color.error }
        val container = when { almost -> R.color.warning_container; correct -> R.color.success_container; else -> R.color.error_container }
        panel.background = c.rounded(c.col(container), 24f).let { d -> (d as android.graphics.drawable.GradientDrawable).apply { cornerRadii = floatArrayOf(c.dp(24f), c.dp(24f), c.dp(24f), c.dp(24f), 0f, 0f, 0f, 0f) } }
        val head = Kit.hbox(c)
        head.addView(ImageView(c).apply {
            setImageResource(if (correct) R.drawable.ic_check else R.drawable.ic_close)
            tint(c.col(R.color.on_primary))
            background = c.rounded(c.col(color), 100f)
            val p = c.dpi(6); setPadding(p, p, p, p)
            layoutParams = LinearLayout.LayoutParams(c.dpi(34), c.dpi(34))
        })
        val title = when { almost -> R.string.fb_almost; correct -> listOf(R.string.fb_correct_1, R.string.fb_correct_2, R.string.fb_correct_3, R.string.fb_correct_4).random(); else -> R.string.fb_wrong }
        head.addView(Kit.text(c, str(title), R.style.Text_Title, c.col(color), 20f).apply { layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(12) } })
        if (speakText != null) head.addView(speaker(speakText))
        panel.addView(head)
        if (!correct || almost) panel.addView(Kit.text(c, str(R.string.correct_answer_is, correctText), R.style.Text_BodyStrong).margins(c, top = 10))
        if (explanation.isNotBlank()) panel.addView(Kit.text(c, explanation, R.style.Text_Body).apply { textSize = 15f }.margins(c, top = 6))
        val tags = ex.tags.filter { TagNames.has(it) }.take(3)
        val bottom = Kit.hbox(c).margins(c, top = 10)
        if (tags.isNotEmpty()) {
            val tagRow = Kit.flow(c).apply { layoutParams = lp(0, WRAP_CONTENT, 1f) }
            tags.forEach { t -> tagRow.addView(Kit.chip(c, TagNames.label(c, t), false) { activity.startSession(services.builder().mistakes(TagNames.label(c, t), t).takeIf { it.exercises.size >= 3 } ?: services.builder().weak(str(R.string.practice_weak), System.currentTimeMillis())) }.apply { textSize = 12f; minHeight = c.dpi(32) }) }
            bottom.addView(tagRow)
        } else bottom.addView(View(c).apply { layoutParams = lp(0, 1, 1f) })
        val saved = services.store.item(ex.key)?.saved == true
        bottom.addView(ImageView(c).apply {
            setImageResource(if (saved) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border); tintRes(R.color.on_surface)
            setBackgroundResource(R.drawable.ripple_circle); val p = c.dpi(12); setPadding(p, p, p, p)
            contentDescription = str(R.string.save)
            layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48))
            setOnClickListener { val now = services.store.toggleSaved(ex.key); setImageResource(if (now) R.drawable.ic_bookmark else R.drawable.ic_bookmark_border); haptic(); activity.toast(str(if (now) R.string.saved else R.string.unsaved)) }
        })
        bottom.addView(ImageView(c).apply {
            setImageResource(R.drawable.ic_flag); tintRes(R.color.on_surface)
            setBackgroundResource(R.drawable.ripple_circle); val p = c.dpi(12); setPadding(p, p, p, p)
            contentDescription = str(R.string.report)
            layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48))
            setOnClickListener { report(ex.key) }
        })
        panel.addView(bottom)
        panel.addView(Kit.primary(c, str(R.string.cont), 12) { index++; render() }.apply {
            if (!correct) background = c.rounded(c.col(R.color.error), 14f, ripple = true)
        })
        panel.setPadding(c.dpi(20), c.dpi(18), c.dpi(20), c.dpi(18) + activity.bottomInset)
        panel.visibility = View.VISIBLE
        bottomBar.visibility = View.INVISIBLE
        panel.post {
            if (c.reduceMotion) { panel.alpha = 0f; panel.animate().alpha(1f).setDuration(100).start(); return@post }
            panel.translationY = panel.height.toFloat()
            panel.animate().translationY(0f).setDuration(220).setInterpolator(android.view.animation.DecelerateInterpolator()).start()
            // Keep the answer visible above the panel.
            scroll.postDelayed({ scroll.smoothScrollBy(0, panel.height / 2) }, 220)
        }
        panel.announceForAccessibility(str(title))
    }

    private fun report(key: String) {
        val uri = Uri.parse("mailto:${BuildConfig.SUPPORT_EMAIL}?subject=" + Uri.encode(str(R.string.report_subject, key)) + "&body=" + Uri.encode(str(R.string.report_body, key)))
        runCatching { activity.startActivity(Intent(Intent.ACTION_SENDTO, uri)) }.onFailure { activity.toast(str(R.string.no_mail_app)) }
    }

    // ---- finish ------------------------------------------------------------------------------

    private fun finish() {
        if (finished) return
        finished = true
        chip.removeCallbacks(speedTick); chip.removeCallbacks(timerTick)
        val seconds = ((SystemClock.elapsedRealtime() - startedAt) / 1000).toInt()
        services.addStudyTime(seconds)
        val correct = answers.count { it.correct }
        val total = answers.size
        val now = System.currentTimeMillis()
        val firstTry = answers.distinctBy { it.ex.key }
        val accuracy = if (firstTry.isEmpty()) 0 else firstTry.count { it.correct } * 100 / firstTry.size
        // Pet meal: a completed session (or ≥ 5 correct answers).
        val goalBefore = services.goalReached()
        if (total >= 3 || correct >= 5) services.pet.meal(now, services.goalReached())
        if (session.kind == SessionKind.MEAL || session.kind == SessionKind.WELCOME || services.pet.isDozing && total >= 5) services.pet.recover()
        if (services.goalReached() && !goalBefore) services.pet.state.coins += 20
        // Weekly goal → freeze token
        if (services.weekDots().count { it } >= 5) services.pet.onWeeklyGoal(services.today())
        services.savePet()
        // Grammar stars: ≥ 80 % = 1★, ≥ 90 % = 2★, 100 % = 3★.
        session.gp?.let { gp ->
            val stars = when { accuracy >= 100 -> 3; accuracy >= 90 -> 2; accuracy >= 80 -> 1; else -> 0 }
            if (session.kind == SessionKind.SKIP_TEST && accuracy >= 80) services.store.setStars(gp, 1) else if (stars > 0) services.store.setStars(gp, stars)
        }
        val s = services.settings
        if (session.kind == SessionKind.PLACEMENT) {
            // Highest level answered ≥ 2/3 correctly (first try) → learner level.
            val byLevel = firstTry.groupBy { it.ex.level }
            val top = (1..5).lastOrNull { l -> byLevel[l]?.let { list -> list.count { it.correct } * 3 >= list.size * 2 } == true } ?: 1
            s.level = (top - 1).coerceIn(0, 3)
            s.placementOffered = true
        }
        s.sessionsDone = s.sessionsDone + 1
        if (accuracy >= 80) s.goodSessions = s.goodSessions + 1
        services.store.addHistory(session.kind.name, session.title, null, firstTry.size, firstTry.count { it.correct }, seconds, now,
            org.json.JSONObject().put("xp", totalXp).put("combo", bestCombo))
        activity.onSessionDone()
        val result = ResultScreen(activity, session, answers, totalXp, accuracy, seconds, bestCombo)
        services.ads.onSessionFinished(activity, lastWrong, fromMock = false, fromPetHome = session.kind == SessionKind.MEAL) {
            activity.navigator.replace(result)
        }
    }
}

/** Answer option with the states from SKILL.md 4.4 (default · selected · correct · wrong · dimmed · disabled). */
class OptionCard(private val ctx: android.content.Context, index: Int, text: CharSequence) {
    enum class State { DEFAULT, SELECTED, CORRECT, WRONG, DIMMED, DISABLED }

    val view: LinearLayout
    private val letter: TextView
    private val label: TextView
    private val mark: ImageView
    var enabled = true
        private set

    init {
        val c = ctx
        view = Kit.hbox(c) {
            minimumHeight = c.dpi(58)
            setPadding(c.dpi(12), c.dpi(10), c.dpi(14), c.dpi(10))
            layoutParams = lp().apply { bottomMargin = c.dpi(10) }
            isClickable = true; isFocusable = true
        }
        letter = TextView(c).apply {
            this.text = ('A' + index).toString(); gravity = Gravity.CENTER; textSize = 14f
            setTypeface(typeface, Typeface.BOLD)
            layoutParams = LinearLayout.LayoutParams(c.dpi(32), c.dpi(32))
        }
        label = Kit.text(c, text, R.style.Text_Body).apply {
            textSize = 17f * c.services.settings.questionScale
            layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(12) }
        }
        mark = ImageView(c).apply { layoutParams = LinearLayout.LayoutParams(c.dpi(24), c.dpi(24)); visibility = View.GONE }
        view.addView(letter); view.addView(label); view.addView(mark)
        view.contentDescription = "${'A' + index}. $text"
        setState(State.DEFAULT)
    }

    fun setState(s: State) {
        val c = ctx
        val (fill, stroke, strokeW) = when (s) {
            State.SELECTED -> Triple(R.color.primary_container, R.color.primary, 2f)
            State.CORRECT -> Triple(R.color.success_container, R.color.success, 2f)
            State.WRONG -> Triple(R.color.error_container, R.color.error, 2f)
            else -> Triple(R.color.surface, R.color.outline, 1.5f)
        }
        view.background = c.rounded(c.col(fill), 16f, c.col(stroke), strokeW, ripple = s == State.DEFAULT || s == State.SELECTED)
        letter.background = c.rounded(c.col(when (s) { State.SELECTED -> R.color.primary; State.CORRECT -> R.color.success; State.WRONG -> R.color.error; else -> R.color.surface_variant }), 100f)
        letter.setTextColor(c.col(if (s == State.SELECTED || s == State.CORRECT || s == State.WRONG) R.color.on_primary else R.color.on_surface))
        view.alpha = when (s) { State.DIMMED -> 0.6f; State.DISABLED -> 0.38f; else -> 1f }
        enabled = s != State.DISABLED
        mark.show(s == State.CORRECT || s == State.WRONG)
        if (s == State.CORRECT) { mark.setImageResource(R.drawable.ic_check); mark.tintRes(R.color.success); if (!c.reduceMotion) { mark.scaleX = 0f; mark.scaleY = 0f; mark.animate().scaleX(1f).scaleY(1f).setDuration(200).setInterpolator(android.view.animation.OvershootInterpolator(3f)).start() } }
        if (s == State.WRONG) { mark.setImageResource(R.drawable.ic_close); mark.tintRes(R.color.error) }
    }

    /** The correct option pulses green once after a wrong answer. */
    fun pulse() {
        if (ctx.reduceMotion) return
        view.animate().scaleX(1.03f).scaleY(1.03f).setDuration(160).withEndAction { view.animate().scaleX(1f).scaleY(1f).setDuration(200).start() }.start()
    }
}

private val android.content.Context.services get() = (applicationContext as com.yourbrand.englishlearn.EnglishApp).services
