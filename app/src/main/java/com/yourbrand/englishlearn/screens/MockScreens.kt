package com.yourbrand.englishlearn.screens

import android.content.Intent
import android.graphics.Typeface
import android.os.SystemClock
import android.net.Uri
import android.text.InputType
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
import android.widget.EditText
import com.yourbrand.englishlearn.BuildConfig
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.core.MistakeReport
import com.yourbrand.englishlearn.services
import com.yourbrand.englishlearn.learning.Exercise
import com.yourbrand.englishlearn.learning.Grader
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
    private val openPassageGroups = mutableSetOf<String>()
    private val seenPassageGroups = mutableSetOf<String>()

    private data class SharedScene(val id: String?, val title: String?, val passage: String?, val image: String?)

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
        prev = Kit.secondary(c, "‹ " + str(R.string.prev), 0) { go(previousPageIndex(test.current)) }.apply { layoutParams = lp(0, c.dpi(52), 1f) }
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
        next = Kit.primary(c, str(R.string.next) + " ›", 0) {
            val target = nextPageIndex(test.current)
            if (target < 0) confirmSubmit() else go(target)
        }.apply { layoutParams = lp(0, c.dpi(52), 1f) }
        bar.addView(prev); bar.addView(flagBtn); bar.addView(palette); bar.addView(next)
        root.addView(bar)
        return root
    }

    override fun onShown(firstTime: Boolean) {
        if (firstTime) { save(); render(); tickBase = SystemClock.elapsedRealtime(); timer.post(tick) }
    }

    override fun onPause() { save() }
    override fun onResume() { tickBase = SystemClock.elapsedRealtime() }
    override fun onDestroy() { timer.removeCallbacks(tick); services.tts.stop() }
    override fun onBack(): Boolean { askClose(); return true }

    private fun save() { if (!submitted) services.settings.mockInProgress = test.toJson() }

    private fun askClose() {
        Dialogs.confirm(ctx, str(R.string.mock_leave_title), str(R.string.mock_leave_msg), str(R.string.quit_keep), str(R.string.mock_leave_save), onNegative = {
            save(); activity.navigator.pop()
        }) {}
    }

    private fun go(i: Int) {
        if (i !in test.keys.indices) return
        test.current = pageStart(i)
        save()
        render()
    }

    private fun paintFlag() { flagBtn.tintRes(if (test.flags[test.current]) R.color.accent else R.color.muted) }

    private fun isGroupedListening(index: Int): Boolean =
        exercises.getOrNull(index) is Exercise.Choice &&
            (exercises[index] as Exercise.Choice).tags.any { it == "listening" } &&
            (exercises[index] as Exercise.Choice).sharedGroupId != null

    private fun listeningGroupStart(index: Int): Int {
        if (!isGroupedListening(index)) return index
        val groupId = (exercises[index] as Exercise.Choice).sharedGroupId ?: return index
        var start = index
        while (start > 0 && isGroupedListening(start - 1) && (exercises[start - 1] as? Exercise.Choice)?.sharedGroupId == groupId) start--
        return start
    }

    private fun listeningGroupEnd(index: Int): Int {
        if (!isGroupedListening(index)) return index
        val groupId = (exercises[index] as Exercise.Choice).sharedGroupId ?: return index
        var end = index
        while (end + 1 < exercises.size && isGroupedListening(end + 1) && (exercises[end + 1] as? Exercise.Choice)?.sharedGroupId == groupId) end++
        return end
    }

    private fun pageStart(index: Int): Int = listeningGroupStart(index.coerceIn(0, test.keys.lastIndex))

    private fun previousPageIndex(index: Int): Int {
        val start = pageStart(index)
        if (start <= 0) return -1
        return pageStart(start - 1)
    }

    private fun nextPageIndex(index: Int): Int {
        val start = pageStart(index)
        val end = listeningGroupEnd(start)
        return if (end >= test.keys.lastIndex) -1 else pageStart(end + 1)
    }

    private fun toeicSceneImage(resourceName: String): ImageView? {
        val id = ctx.resources.getIdentifier(resourceName, "drawable", ctx.packageName)
        if (id == 0) return null
        return ImageView(ctx).apply {
            setImageResource(id)
            contentDescription = str(R.string.ins_picture_word)
            scaleType = ImageView.ScaleType.FIT_CENTER
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, ctx.dpi(220)).apply {
                bottomMargin = ctx.dpi(8)
            }
        }
    }

    private fun render() {
        val c = ctx
        val i = pageStart(test.current)
        test.current = i
        val groupEnd = listeningGroupEnd(i)
        counter.text = if (groupEnd > i) "${i + 1}-${groupEnd + 1}/${test.keys.size}" else "${i + 1}/${test.keys.size}"
        val previous = previousPageIndex(i)
        val following = nextPageIndex(i)
        prev.isEnabled = previous >= 0; prev.alpha = if (previous >= 0) 1f else 0.4f
        next.text = if (following < 0) str(R.string.submit) else str(R.string.next) + " ›"
        paintFlag()
        content.removeAllViews()
        scroll.scrollTo(0, 0)
        val ex = exercises[i] ?: run { content.addView(Kit.text(c, "—")); return }
        val sectionIndex = test.sectionLabels.indexOfFirst { it.first == test.sectionIds.getOrNull(i) }
        if (sectionIndex >= 0 && (i == 0 || test.sectionIds.getOrNull(i - 1) != test.sectionIds.getOrNull(i))) {
            content.addView(Kit.text(c, str(R.string.section_n, sectionIndex + 1, test.sectionLabels.size, test.sectionLabels[sectionIndex].second), R.style.Text_Caption).margins(c, top = 4, bottom = 8))
        }
        if (isGroupedListening(i)) {
            renderListeningGroup(i, groupEnd)
            content.staggerChildren(25)
            return
        }
        val shared = when (ex) {
            is Exercise.Choice -> SharedScene(ex.sharedGroupId, ex.sharedPassageTitle, ex.sharedPassage, ex.sharedImage)
            is Exercise.ExamText -> SharedScene(ex.sharedGroupId, ex.sharedPassageTitle, ex.sharedPassage, null)
            else -> null
        }
        val hideAudioTranscript = ex is Exercise.Choice && ex.tags.any { it == "listening" }
        if ((!hideAudioTranscript || shared?.image != null) && shared?.id != null && shared.passage != null) {
            val groupId = shared.id
            if (shared.image != null) {
                content.addView(Kit.card(c, 14, 10, c.col(R.color.surface_variant), null) {
                    val image = shared.image.let(::toeicSceneImage)
                    if (image != null) addView(image)
                    else if (!hideAudioTranscript) addView(Kit.text(c, (shared.title?.let { "$it\n\n" } ?: "") + shared.passage, R.style.Text_Body))
                }.margins(c, top = 6, bottom = 8))
            } else {
                // Open the first time the group appears; after that it stays as the learner left it
                // (re-rendering after an answer must not collapse it).
                if (seenPassageGroups.add(groupId)) openPassageGroups += groupId
                val open = groupId in openPassageGroups
                content.addView(Kit.secondary(c, str(if (open) R.string.passage_hide else R.string.passage_show), 0) {
                    if (open) openPassageGroups -= groupId else openPassageGroups += groupId
                    render()
                }.margins(c, top = 2))
                if (open) {
                    // Let the parent question scroll own the whole passage. A fixed-height nested
                    // scroll view hid long Part 7 passages behind a second scroll container.
                    content.addView(Kit.card(c, 14, 10, c.col(R.color.surface_variant), null) {
                        addView(Kit.text(c, (shared.title?.let { "$it\n\n" } ?: "") + shared.passage, R.style.Text_Body))
                    }.margins(c, top = 6, bottom = 8))
                }
            }
        }
        when (ex) {
            is Exercise.Choice -> {
                var stemView: TextView? = null
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
                    if (ex.stem.isNotBlank()) {
                        stemView = Kit.text(c, if (ex.stem.contains("___")) Spans.blank(ex.stem, ex.options.getOrNull(test.answers[i]), c.col(R.color.primary), c.col(R.color.primary_container)) else Spans.underline(ex.stem), R.style.Text_Question).margins(c, top = 8)
                        content.addView(stemView)
                    }
                }
                // TOEIC Listening Part 1 presents picture statements as audio only.
                // Keep the answer choices selectable, but never reveal their text before
                // the learner answers; this also covers the full-test MockScreen path.
                val hideAudioAnswerText = ex.hideAudioOptionText ||
                    ex.key.startsWith("exam:toeic_p1:") ||
                    ex.tags.any { it == "section_p1" || it == "listen_p1" }
                val cards = ex.options.mapIndexed { j, o ->
                    OptionCard(c, j, if (ex.optionsMarked) Spans.underline(o) else o, showText = !hideAudioAnswerText)
                }
                val box = Kit.vbox(c).margins(c, top = 14)
                cards.forEachIndexed { j, card ->
                    if (test.answers[i] == j) card.setState(OptionCard.State.SELECTED)
                    card.view.onTap {
                        test.answers[i] = j
                        cards.forEachIndexed { k, oc -> oc.setState(if (k == j) OptionCard.State.SELECTED else OptionCard.State.DEFAULT) }
                        if (ex.stem.contains("___") && ex.passage == null) {
                            stemView?.text = Spans.blank(ex.stem, ex.options[j], c.col(R.color.primary), c.col(R.color.primary_container))
                        }
                        save()
                    }
                    box.addView(card.view)
                }
                content.addView(box)
                if (ex.audioOptions) {
                    fun playOptions(slower: Boolean) {
                        val sentences = listOfNotNull(ex.audioPrompt) + ex.options
                        services.tts.speakSentences(sentences, slower) { index ->
                            val optionIndex = index - if (ex.audioPrompt != null) 1 else 0
                            if (test.answers[i] < 0) cards.forEachIndexed { j, card ->
                                card.setState(if (j == optionIndex) OptionCard.State.PLAYING else OptionCard.State.DEFAULT)
                            }
                        }
                    }
                    content.addView(Kit.secondary(c, str(R.string.play_slow), 4) { playOptions(true) }.margins(c, top = 2))
                    content.post { playOptions(false) }
                }
            }
            is Exercise.ExamText -> {
                content.addView(Kit.text(c, str(ex.instruction), R.style.Text_Caption))
                content.addView(Kit.text(c, ex.stem, R.style.Text_Question).margins(c, top = 8))
                content.addView(EditText(c).apply {
                    hint = str(R.string.type_here)
                    textSize = 19f
                    inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                    setSingleLine()
                    setPadding(c.dpi(16), c.dpi(14), c.dpi(16), c.dpi(14))
                    background = c.rounded(c.col(R.color.surface), 14f, c.col(R.color.outline), 1.5f)
                    layoutParams = lp().apply { topMargin = c.dpi(18) }
                    setText(test.textAnswers[i])
                    addTextChangedListener(object : android.text.TextWatcher {
                        override fun afterTextChanged(s: android.text.Editable?) { test.textAnswers[i] = s?.toString().orEmpty(); save() }
                        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                    })
                })
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

    /** Keep one recording and its questions on one answer page for every grouped listening part. */
    private fun renderListeningGroup(start: Int, end: Int) {
        val c = ctx
        val questions = (start..end).mapNotNull { exercises[it] as? Exercise.Choice }
        val questionViews = ArrayList<TextView>()
        val optionCards = ArrayList<List<OptionCard>>()
        content.addView(Kit.text(c, str(R.string.ins_listen), R.style.Text_Caption))
        content.addView(Kit.secondary(c, str(R.string.listen), 4) { playListeningGroup(start, end, false, questionViews, optionCards) }.margins(c, top = 8))
        content.addView(Kit.secondary(c, str(R.string.play_slow), 4) { playListeningGroup(start, end, true, questionViews, optionCards) }.margins(c, top = 2))
        questions.forEachIndexed { offset, ex ->
            val index = start + offset
            val q = Kit.text(c, "${index + 1}. ${ex.stem}", R.style.Text_Question).margins(c, top = 18)
            questionViews += q
            content.addView(q)
            val hideAudioAnswerText = ex.hideAudioOptionText || ex.tags.any { it == "section_p1" || it == "listen_p1" }
            val cards = ex.options.mapIndexed { j, option -> OptionCard(c, j, option, showText = !hideAudioAnswerText) }
            optionCards += cards
            val box = Kit.vbox(c).margins(c, top = 10)
            cards.forEachIndexed { j, card ->
                if (test.answers[index] == j) card.setState(OptionCard.State.SELECTED)
                card.view.onTap {
                    test.answers[index] = j
                    cards.forEachIndexed { k, other -> other.setState(if (k == j) OptionCard.State.SELECTED else OptionCard.State.DEFAULT) }
                    save()
                }
                box.addView(card.view)
            }
            content.addView(box)
        }
        content.post { playListeningGroup(start, end, false, questionViews, optionCards) }
    }

    private fun playListeningGroup(
        start: Int,
        end: Int,
        slower: Boolean,
        questionViews: List<TextView> = emptyList(),
        optionCards: List<List<OptionCard>> = emptyList(),
    ) {
        val first = exercises[start] as? Exercise.Choice ?: return
        val dialogue = first.sharedPassage?.takeIf { it.isNotBlank() } ?: first.audioPrompt.orEmpty()
        val questions = (start..end).mapNotNull { exercises[it] as? Exercise.Choice }
        // TOEIC Parts 2–4 are the only formats in this screen whose listening
        // flow reads the question/options after the recording. For the other
        // exams the questions are printed material; reading them through TTS
        // would no longer match the real test and can expose answer text as
        // audio before the learner answers. Part 1 is recording-only as well.
        val speakQuestions = questions.any { question ->
            question.tags.any { it == "section_p2" || it == "section_p3" || it == "section_p4" }
        }
        val chunks = if (!speakQuestions) {
            listOf(dialogue)
        } else {
            listOf(dialogue) + questions.mapIndexed { offset, ex ->
                val number = start + offset + 1
                "Question $number. ${ex.stem} " + ex.options.mapIndexed { j, option -> "${('A'.code + j).toChar()}. $option" }.joinToString(" ")
            }
        }
        services.tts.speakSequence(chunks, slower, gapMs = 1500L) { chunk ->
            val active = chunk - 1
            questionViews.forEachIndexed { i, view -> view.setTextColor(ctx.col(if (i == active) R.color.primary else R.color.on_surface)) }
            optionCards.forEachIndexed { i, cards ->
                val selected = test.answers[start + i]
                cards.forEachIndexed { j, card ->
                    card.setState(when {
                        i == active && selected < 0 -> OptionCard.State.PLAYING
                        j == selected -> OptionCard.State.SELECTED
                        else -> OptionCard.State.DEFAULT
                    })
                }
            }
        }
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
                val answered = test.answers[k] >= 0 || test.textAnswers.getOrNull(k).orEmpty().isNotBlank()
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
            val ok = when (ex) {
                is Exercise.ExamText -> Grader.grade(test.textAnswers[k], ex.answer) != Grader.Result.WRONG
                else -> test.answers[k] == ans
            }
            if (ok) correct++
            if (test.answers[k] >= 0 || test.textAnswers[k].isNotBlank()) xp += services.recordAnswer(ex, listOf(key), ok, 5000, 0, now)
            detail.put(JSONObject().put("k", key).put("a", test.answers[k]).put("text", test.textAnswers[k]).put("f", test.flags[k]))
        }
        val sectionDetail = JSONArray()
        test.sectionLabels.forEach { (id, _) ->
            val indices = test.sectionIds.mapIndexedNotNull { i, section -> i.takeIf { section == id } }
            sectionDetail.put(JSONObject().put("id", id).put("correct", indices.count { k ->
                when (val ex = exercises[k]) {
                    is Exercise.ExamText -> Grader.grade(test.textAnswers[k], ex.answer) != Grader.Result.WRONG
                    is Exercise.Choice -> test.answers[k] == ex.answer
                    is Exercise.FindError -> test.answers[k] == ex.answer
                    else -> false
                }
            }).put("total", indices.size))
        }
        services.addStudyTime(test.elapsedSec)
        val r = services.creditXp(xp)
        if (test.answeredCount >= 5) { services.pet.meal(now, services.goalReached()); services.savePet() }
        services.settings.recentMockKeys = (test.keys + services.settings.recentMockKeys).take(test.keys.size * 2)
        val previous = services.store.history().lastOrNull { it.kind == "MOCK" && it.format == test.formatId && it.total == test.keys.size }
        services.store.addHistory("MOCK", test.title, test.formatId, test.keys.size, correct, test.elapsedSec, now, JSONObject().put("items", detail).put("sections", sectionDetail).put("xp", r.gained))
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

    private data class SharedScene(val id: String?, val title: String?, val passage: String?, val image: String?)

    private fun toeicSceneImage(resourceName: String): ImageView? {
        val id = ctx.resources.getIdentifier(resourceName, "drawable", ctx.packageName)
        if (id == 0) return null
        return ImageView(ctx).apply {
            setImageResource(id)
            contentDescription = str(R.string.ins_picture_word)
            scaleType = ImageView.ScaleType.FIT_CENTER
            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, ctx.dpi(220)).apply {
                bottomMargin = ctx.dpi(8)
            }
        }
    }

    private fun answerOf(ex: Exercise?) = when (ex) { is Exercise.Choice -> ex.answer; is Exercise.FindError -> ex.answer; else -> -1 }
    private fun correctAt(index: Int): Boolean = when (val ex = exercises[index]) {
        is Exercise.ExamText -> Grader.grade(test.textAnswers[index], ex.answer) != Grader.Result.WRONG
        is Exercise.Choice -> test.answers[index] == ex.answer
        is Exercise.FindError -> test.answers[index] == ex.answer
        else -> false
    }
    private fun answeredAt(index: Int): Boolean = test.answers[index] >= 0 || test.textAnswers.getOrNull(index).orEmpty().isNotBlank()

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
                val sectionCorrect = indices.count(::correctAt)
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
            val ok = correctAt(k)
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

        body.addView(Kit.primary(c, str(R.string.practice_weak), 16) { activity.startSessionAsync { services.builder().weak(str(R.string.practice_weak), System.currentTimeMillis()) } })

        // Review each question
        body.addView(Kit.section(c, str(R.string.review_each)))
        val chips = Kit.hbox(c)
        listOf(R.string.rv_wrong, R.string.rv_flagged, R.string.f_all).forEachIndexed { i, l -> chips.addView(Kit.chip(c, str(l), reviewFilter == i) { reviewFilter = i; refresh() }) }
        body.addView(Kit.hscroll(c, chips))
        val evidenceColors = listOf(c.col(R.color.primary), c.col(R.color.accent), c.col(R.color.success))
        val evidenceBackgrounds = listOf(c.col(R.color.primary_container), c.col(R.color.accent_container), c.col(R.color.success_container))
        val shownListeningGroups = mutableSetOf<String>()
        exercises.forEachIndexed { k, ex ->
            ex ?: return@forEachIndexed
            val ans = answerOf(ex)
            val ok = correctAt(k)
            val show = when (reviewFilter) { 0 -> !ok; 1 -> test.flags[k]; else -> true }
            if (!show) return@forEachIndexed
            val shared = when (ex) {
                is Exercise.Choice -> SharedScene(ex.sharedGroupId, ex.sharedPassageTitle, ex.sharedPassage, ex.sharedImage)
                is Exercise.ExamText -> SharedScene(ex.sharedGroupId, ex.sharedPassageTitle, ex.sharedPassage, null)
                else -> null
            }
            val listeningReview = ex is Exercise.Choice && ex.tags.any { it == "listening" }
            val groupId = shared?.id
            val listeningGroupQuestions = if (listeningReview && groupId != null) {
                exercises.mapNotNull { it as? Exercise.Choice }.filter {
                    it.sharedGroupId == groupId && it.tags.any { tag -> tag == "listening" }
                }
            } else emptyList()
            val listeningQuestionIndex = if (listeningReview) {
                listeningGroupQuestions.indexOfFirst { it.key == ex.key }.coerceAtLeast(0)
            } else 0
            val showListeningTranscript = listeningReview && groupId?.let { shownListeningGroups.add(it) } == true
            val showRegularTranscript = !listeningReview && groupId != null && exercises.take(k).none {
                    when (it) {
                        is Exercise.Choice -> it.sharedGroupId == groupId
                        is Exercise.ExamText -> it.sharedGroupId == groupId
                        else -> false
                    }
                }
            if ((showListeningTranscript || showRegularTranscript) && shared?.id != null && shared.passage != null) {
                body.addView(Kit.card(c, 14, 10, c.col(R.color.surface_variant), null) {
                    val image = shared.image?.let(::toeicSceneImage)
                    if (image != null) addView(image)
                    else if (listeningReview) {
                        shared.title?.let { addView(Kit.text(c, it, R.style.Text_BodyStrong)) }
                        val evidenceParts = listeningGroupQuestions.mapNotNull { it.evidence?.takeIf(String::isNotBlank) }
                        if (evidenceParts.isNotEmpty()) {
                            val legend = Kit.hbox(c).margins(c, top = 6, bottom = 6)
                            evidenceParts.forEachIndexed { index, _ ->
                                val marker = Kit.text(c, "Q${index + 1}", R.style.Text_Caption, evidenceColors[index % evidenceColors.size]).apply {
                                    background = c.rounded(evidenceBackgrounds[index % evidenceBackgrounds.size], 8f)
                                    setPadding(c.dpi(8), c.dpi(4), c.dpi(8), c.dpi(4))
                                    layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { marginEnd = c.dpi(6) }
                                }
                                legend.addView(marker)
                            }
                            addView(legend)
                            addView(Kit.text(c, Spans.highlightParts(shared.passage, evidenceParts, evidenceColors, evidenceBackgrounds), R.style.Text_Body))
                        } else {
                            addView(Kit.text(c, shared.passage, R.style.Text_Body))
                        }
                    } else addView(Kit.text(c, (shared.title?.let { "$it\n\n" } ?: "") + shared.passage, R.style.Text_Body))
                })
            }
            val (stem, opts, expl) = when (ex) {
                is Exercise.Choice -> Triple(if (ex.passage != null) "${ex.passage.title} (${ex.blank + 1})" else ex.stem.replace("[", "").replace("]", ""), ex.options, ex.explanation)
                is Exercise.ExamText -> Triple(ex.stem, listOf(ex.answer), ex.explanation)
                is Exercise.FindError -> Triple(ex.stem.replace("[", "").replace("]", ""), Regex("\\[([^\\]]+)\\]").findAll(ex.stem).map { it.groupValues[1] }.toList(), ex.explanation + " → " + ex.fix)
                else -> Triple("", emptyList(), "")
            }
            body.addView(Kit.card(c, 14, 10) {
                addView(Kit.text(c, "${k + 1}. $stem", R.style.Text_Body).apply { textSize = 15f })
                val given = test.answers[k]
                if (ex is Exercise.ExamText) {
                    val typed = test.textAnswers[k]
                    if (typed.isNotBlank() && !ok) addView(Kit.text(c, "✗ $typed", R.style.Text_Caption, c.col(R.color.error)).margins(c, top = 4))
                    if (typed.isBlank()) addView(Kit.text(c, str(R.string.unanswered), R.style.Text_Caption, c.col(R.color.warning)).margins(c, top = 4))
                    addView(Kit.text(c, "✓ ${ex.answer}", R.style.Text_BodyStrong, c.col(R.color.success)).margins(c, top = 2))
                } else {
                    if (given >= 0 && !ok) addView(Kit.text(c, "✗ " + opts.getOrElse(given) { "" }.replace("[", "").replace("]", ""), R.style.Text_Caption, c.col(R.color.error)).margins(c, top = 4))
                    if (given < 0) addView(Kit.text(c, str(R.string.unanswered), R.style.Text_Caption, c.col(R.color.warning)).margins(c, top = 4))
                    addView(Kit.text(c, "✓ " + opts.getOrElse(ans) { "" }.replace("[", "").replace("]", ""), R.style.Text_BodyStrong, c.col(R.color.success)).margins(c, top = 2))
                }
                if (expl.isNotBlank()) addView(Kit.text(c, expl, R.style.Text_Caption).margins(c, top = 4))
                val evidence = (ex as? Exercise.Choice)?.evidence
                if (listeningReview && evidence?.isNotBlank() == true) {
                    addView(Kit.text(c, str(R.string.audio_evidence), R.style.Text_Caption).margins(c, top = 10))
                    val colorIndex = listeningQuestionIndex % evidenceColors.size
                    addView(Kit.text(c, Spans.highlight(evidence, evidence, evidenceColors[colorIndex], evidenceBackgrounds[colorIndex]), R.style.Text_Body).margins(c, top = 3))
                }
                addView(Kit.secondary(c, str(R.string.report_mistake), 8) { report(ex.key) }.margins(c, top = 8))
            })
        }
        body.addView(Kit.secondary(c, str(R.string.go_home), 20) { activity.selectTab(Tab.EXAM) })
        val pending = services.pet.state.pendingStageUp
        if (pending > 0) body.postDelayed({ if (activity.navigator.current === this) activity.showStageUp(pending) }, 1200)
    }

    private fun report(key: String) {
        val email = MistakeReport.build(key, "MockResultScreen", BuildConfig.VERSION_NAME, str(R.string.report_mistake_subject), str(R.string.report_mistake_body))
        val uri = Uri.parse("mailto:${BuildConfig.SUPPORT_EMAIL}?subject=" + Uri.encode(email.subject) + "&body=" + Uri.encode(email.body))
        runCatching { activity.startActivity(Intent(Intent.ACTION_SENDTO, uri)) }.onFailure { activity.toast(str(R.string.report_mistake_no_app, BuildConfig.SUPPORT_EMAIL)) }
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
