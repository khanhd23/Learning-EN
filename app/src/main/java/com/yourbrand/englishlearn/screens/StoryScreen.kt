package com.yourbrand.englishlearn.screens

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.pet.PetItems
import com.yourbrand.englishlearn.content.Story
import com.yourbrand.englishlearn.ui.*
import java.util.Locale

/** Offline story reader. Stories are optional assets and are already filtered by ContentParser. */
class StoryScreen(activity: MainActivity, private val story: Story) : Screen(activity) {
    override val petMode = PetMode.MINI
    private lateinit var body: LinearLayout
    private var questionIndex = 0
    private var correct = 0
    private var currentSentence = -1
    private val sentenceViews = ArrayList<TextView>()
    private var translationVisible = services.settings.storyShowTranslation
    private var playingSlow = false

    override fun onCreateView(parent: ViewGroup): View {
        val root = Kit.vbox(ctx)
        val header = Kit.hbox(ctx) { gravity = Gravity.CENTER_VERTICAL; setPadding(ctx.dpi(4), ctx.dpi(4), ctx.dpi(8), ctx.dpi(4)) }
        header.addView(ImageView(ctx).apply { setImageResource(R.drawable.ic_arrow_back); tintRes(R.color.on_surface); setPadding(ctx.dpi(12), ctx.dpi(12), ctx.dpi(12), ctx.dpi(12)); contentDescription = str(R.string.back); layoutParams = LinearLayout.LayoutParams(ctx.dpi(48), ctx.dpi(48)); setOnClickListener { activity.onBackPressedDispatcher.onBackPressed() } })
        header.addView(Kit.ellipsize(Kit.text(ctx, story.title, R.style.Text_Title)).apply { layoutParams = lp(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f) })
        root.addView(header)
        body = Kit.vbox(ctx, 20)
        root.addView(android.widget.ScrollView(ctx).apply { addView(body) }, lp(0, 0, 1f))
        renderReader()
        return root
    }

    private fun renderReader() {
        body.removeAllViews(); sentenceViews.clear(); currentSentence = -1
        val actions = Kit.hbox(ctx) { gravity = Gravity.CENTER_VERTICAL }
        story.translation?.let { actions.addView(Kit.chip(ctx, str(R.string.show_translation), translationVisible) { translationVisible = !translationVisible; services.settings.storyShowTranslation = translationVisible; renderReader() }) }
        actions.addView(Kit.secondary(ctx, str(R.string.listen), 8) { playAll(false) })
        actions.addView(Kit.secondary(ctx, str(R.string.play_slow), 8) { playAll(true) }.apply { layoutParams = (layoutParams as LinearLayout.LayoutParams).apply { marginStart = ctx.dpi(6) } })
        body.addView(actions)
        storySentences().forEachIndexed { i, sentence ->
            val view = highlightedSentence(sentence, i).margins(ctx, top = 10)
            sentenceViews += view; body.addView(view)
        }
        if (translationVisible && !story.translation.isNullOrBlank()) body.addView(Kit.text(ctx, story.translation, R.style.Text_Caption).margins(ctx, top = 12))
        body.addView(Kit.primary(ctx, str(R.string.continue_label), 18) { if (story.questions.isEmpty()) finishStory() else showQuestion() }.margins(ctx, top = 18))
    }

    private fun storySentences(): List<String> = story.text.split(Regex("(?<=[.!?])\\s+")).filter { it.isNotBlank() }

    private fun highlightedSentence(text: String, index: Int): TextView {
        val out = SpannableStringBuilder(text)
        val words = story.targets.mapNotNull { id -> services.content.wordById[id] }
        words.forEach { word ->
            val forms = buildSet {
                add(word.lemma); word.forms.values.forEach { add(it) }
                val base = word.lemma.lowercase(Locale.ROOT)
                add(base + "s"); add(base + "ed"); add(base + "ing")
                if (base.endsWith("y") && base.length > 1) { add(base.dropLast(1) + "ies"); add(base.dropLast(1) + "ied") }
                if (base.endsWith("e")) add(base.dropLast(1) + "ing")
            }.filter { it.isNotBlank() }.joinToString("|") { Regex.escape(it) }
            val regex = Regex("(?i)(?<![A-Za-z'])($forms)(?![A-Za-z'])")
            regex.findAll(text).forEach { match ->
                out.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) { showWord(word.id) }
                    override fun updateDrawState(ds: android.text.TextPaint) { ds.color = ctx.col(R.color.on_surface); ds.isUnderlineText = true }
                }, match.range.first, match.range.last + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                out.setSpan(BackgroundColorSpan(ctx.col(R.color.primary_container)), match.range.first, match.range.last + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                out.setSpan(ForegroundColorSpan(ctx.col(R.color.primary_deep)), match.range.first, match.range.last + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return Kit.text(ctx, out, R.style.Text_Body).apply {
            textSize = 19f; movementMethod = android.text.method.LinkMovementMethod.getInstance(); highlightColor = Color.TRANSPARENT
            setOnClickListener { services.tts.speak(text, slower = playingSlow) }
            setBackgroundColor(if (index == currentSentence) ctx.col(R.color.info_container) else Color.TRANSPARENT)
        }
    }

    private fun playAll(slow: Boolean) {
        playingSlow = slow
        val sentences = storySentences()
        services.tts.speakSentences(sentences, slow) { index ->
            currentSentence = index
            sentenceViews.forEachIndexed { i, view -> view.setBackgroundColor(if (i == index) ctx.col(R.color.info_container) else Color.TRANSPARENT) }
        }
    }

    private fun showWord(id: String) {
        val word = services.content.wordById[id] ?: return
        Dialogs.custom(ctx) { box, dialog ->
            box.addView(Kit.text(ctx, word.lemma, R.style.Text_Title))
            box.addView(Kit.text(ctx, word.allGlosses, R.style.Text_Body).margins(ctx, top = 8))
            box.addView(Kit.primary(ctx, str(R.string.play_slow), 20) { services.tts.speakSlow(word.lemma) }.margins(ctx, top = 12))
            box.addView(Kit.secondary(ctx, str(R.string.save), 20) { services.store.toggleSaved(word.key); dialog.dismiss() }.margins(ctx, top = 8))
            box.addView(Kit.secondary(ctx, str(R.string.close), 20) { dialog.dismiss() }.margins(ctx, top = 8))
        }
    }

    private fun showQuestion() {
        if (questionIndex >= story.questions.size) { showResult(); return }
        val q = story.questions[questionIndex]
        body.removeAllViews()
        body.addView(Kit.text(ctx, story.title, R.style.Text_Title))
        body.addView(Kit.text(ctx, q.question, R.style.Text_Question).margins(ctx, top = 18))
        // One answer per question: options lock after the first tap and show right/wrong.
        var answered = false
        val buttons = ArrayList<OptionCard>()
        q.options.forEachIndexed { i, option ->
            val card = OptionCard(ctx, i, option)
            buttons += card
            card.view.setOnClickListener {
                if (!answered) {
                    answered = true
                    val ok = i == q.answer
                    if (ok) correct++
                    buttons.forEachIndexed { j, b -> b.setState(when { j == q.answer -> OptionCard.State.CORRECT; j == i -> OptionCard.State.WRONG; else -> OptionCard.State.DIMMED }) }
                    body.addView(Kit.text(ctx, q.explanation, R.style.Text_Body).margins(ctx, top = 12))
                    body.addView(Kit.primary(ctx, str(R.string.continue_label), 16) { questionIndex++; showQuestion() })
                }
            }
            body.addView(card.view.margins(ctx, top = 10))
        }
    }

    private fun showResult() {
        body.removeAllViews()
        body.addView(Kit.text(ctx, story.title, R.style.Text_Title))
        body.addView(Kit.text(ctx, str(R.string.story_score, correct, story.questions.size), R.style.Text_Display).margins(ctx, top = 20))
        body.addView(Kit.primary(ctx, str(R.string.done), 18) { finishStory() }.margins(ctx, top = 18))
    }

    private fun finishStory() {
        services.creditXp((10 + correct * 2).coerceAtMost(20))
        val rewards = services.run { recordStoryFinished(); claimWeeklyRewards() }
        rewards.forEach { id -> PetItems.byId[id]?.let { activity.toast(str(R.string.quest_reward, str(it.nameRes))) } }
        services.pet.meal(System.currentTimeMillis(), services.goalReached())
        services.savePet()
        activity.navigator.pop()
    }
}
