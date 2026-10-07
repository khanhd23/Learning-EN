package com.yourbrand.englishlearn.screens

import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.content.Story
import com.yourbrand.englishlearn.ui.*

/** Offline story reader. Stories are optional assets and are already filtered by ContentParser. */
class StoryScreen(activity: MainActivity, private val story: Story) : Screen(activity) {
    override val petMode = PetMode.MINI
    private lateinit var body: LinearLayout
    private var questionIndex = 0
    private var correct = 0

    override fun onCreateView(parent: ViewGroup): View {
        body = Kit.vbox(ctx, 20)
        val scroll = android.widget.ScrollView(ctx).apply { addView(body) }
        body.addView(Kit.text(ctx, story.title, R.style.Text_Display))
        body.addView(highlightedText().margins(ctx, top = 14))
        body.addView(Kit.primary(ctx, str(R.string.continue_label), 24) { if (story.questions.isEmpty()) finishStory() else showQuestion() }.margins(ctx, top = 18))
        return scroll
    }

    private fun highlightedText(): TextView {
        val text = story.text
        val out = SpannableStringBuilder(text)
        val words = story.targets.mapNotNull { id -> services.content.wordById[id] }
        words.forEach { word ->
            val regex = Regex("(?i)(?<![A-Za-z'])" + Regex.escape(word.lemma) + "(?![A-Za-z'])")
            regex.findAll(text).forEach { match ->
                out.setSpan(object : ClickableSpan() {
                    override fun onClick(widget: View) { showWord(word.id) }
                }, match.range.first, match.range.last + 1, 0)
            }
        }
        return Kit.text(ctx, out, R.style.Text_Body).apply {
            textSize = 19f
            movementMethod = LinkMovementMethod.getInstance()
            highlightColor = ctx.col(R.color.primary_container)
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
        if (questionIndex >= story.questions.size) { finishStory(); return }
        val q = story.questions[questionIndex]
        body.removeAllViews()
        body.addView(Kit.text(ctx, story.title, R.style.Text_Title))
        body.addView(Kit.text(ctx, q.question, R.style.Text_Question).margins(ctx, top = 18))
        // One answer per question: options lock after the first tap and show right/wrong.
        var answered = false
        val buttons = ArrayList<TextView>()
        q.options.forEachIndexed { i, option ->
            buttons += Kit.secondary(ctx, option, 10) {
                if (answered) return@secondary
                answered = true
                val ok = i == q.answer
                if (ok) correct++
                buttons.forEachIndexed { j, b ->
                    b.isEnabled = false
                    if (j == q.answer) b.background = ctx.rounded(ctx.col(R.color.success_container), 14f, ctx.col(R.color.success), 2f)
                    else if (j == i) b.background = ctx.rounded(ctx.col(R.color.error_container), 14f, ctx.col(R.color.error), 2f)
                }
                body.addView(Kit.text(ctx, q.explanation, R.style.Text_Body).margins(ctx, top = 12))
                body.addView(Kit.primary(ctx, str(R.string.continue_label), 16) { questionIndex++; showQuestion() })
            }.also { body.addView(it) }
        }
    }

    private fun finishStory() {
        services.creditXp((10 + correct * 2).coerceAtMost(20))
        services.pet.meal(System.currentTimeMillis(), services.goalReached())
        services.savePet()
        activity.navigator.pop()
    }
}
