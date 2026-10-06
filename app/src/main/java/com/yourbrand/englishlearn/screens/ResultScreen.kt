package com.yourbrand.englishlearn.screens

import android.animation.ValueAnimator
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.core.Sfx
import com.yourbrand.englishlearn.learning.Exercise
import com.yourbrand.englishlearn.learning.Session
import com.yourbrand.englishlearn.learning.SessionKind
import com.yourbrand.englishlearn.notify.Reminders
import com.yourbrand.englishlearn.pet.Mood
import com.yourbrand.englishlearn.pet.PetView
import com.yourbrand.englishlearn.ui.*
import com.yourbrand.englishlearn.ui.views.ProgressRing

/** S17 — session result (≈ 2.2 s sequence, tap anywhere to skip to the final state). */
class ResultScreen(
    activity: MainActivity,
    private val session: Session,
    private val answers: List<Answer>,
    private var xp: Int,
    private val accuracy: Int,
    private val seconds: Int,
    private val bestCombo: Int,
) : ScrollScreen(activity) {
    override val petMode = PetMode.HIDDEN
    private var xpText: TextView? = null
    private var doubled = false
    private val anims = ArrayList<ValueAnimator>()

    override fun build(body: LinearLayout) {
        val c = ctx
        body.gravity = Gravity.CENTER_HORIZONTAL
        val stars = when { accuracy >= 90 -> 3; accuracy >= 70 -> 2; else -> 1 }
        body.addView(Kit.text(c, str(if (accuracy >= 80) R.string.result_great else R.string.result_done), R.style.Text_Display).apply { gravity = Gravity.CENTER }.margins(c, top = 12))
        body.addView(Kit.text(c, session.title, R.style.Text_Caption).apply { gravity = Gravity.CENTER })

        // Stars pop in sequence (150 ms stagger)
        val starRow = Kit.hbox(c) { gravity = Gravity.CENTER }.margins(c, top = 16)
        repeat(3) { i ->
            val iv = ImageView(c).apply {
                setImageResource(if (i < stars) R.drawable.ic_star_filled else R.drawable.ic_star_border)
                tint(c.col(if (i < stars) R.color.accent else R.color.outline))
                layoutParams = LinearLayout.LayoutParams(c.dpi(if (i == 1) 60 else 48), c.dpi(if (i == 1) 60 else 48)).apply { marginStart = c.dpi(4); marginEnd = c.dpi(4); if (i != 1) topMargin = c.dpi(10) }
            }
            starRow.addView(iv)
            if (!c.reduceMotion) { iv.scaleX = 0f; iv.scaleY = 0f; iv.animate().scaleX(1f).scaleY(1f).setStartDelay(250L + i * 150).setDuration(260).setInterpolator(android.view.animation.OvershootInterpolator(3f)).start() }
        }
        body.addView(starRow)

        // Accuracy ring + XP count-up + pet
        val stats = Kit.hbox(c) { gravity = Gravity.CENTER }.margins(c, top = 20)
        val ringBox = FrameLayout(c).apply { layoutParams = LinearLayout.LayoutParams(c.dpi(116), c.dpi(116)) }
        val ring = ProgressRing(c).apply { color = c.col(if (accuracy >= 80) R.color.success else if (accuracy >= 60) R.color.accent else R.color.error) }
        ringBox.addView(ring, FrameLayout.LayoutParams(c.dpi(116), c.dpi(116)))
        val ringLabel = Kit.vbox(c) { gravity = Gravity.CENTER }
        ringLabel.addView(Kit.text(c, "$accuracy%", R.style.Text_Display).apply { gravity = Gravity.CENTER })
        ringLabel.addView(Kit.text(c, str(R.string.accuracy), R.style.Text_Caption).apply { gravity = Gravity.CENTER })
        ringBox.addView(ringLabel, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER))
        stats.addView(ringBox)
        val right = Kit.vbox(c) { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { marginStart = c.dpi(24) } }
        xpText = Kit.text(c, "+0 XP", R.style.Text_Display, c.col(R.color.accent))
        right.addView(xpText)
        right.addView(Kit.text(c, str(R.string.result_meta, answers.distinctBy { it.ex.key }.size, seconds / 60, seconds % 60), R.style.Text_Caption))
        if (bestCombo >= 3) right.addView(Kit.text(c, str(R.string.best_combo, bestCombo), R.style.Text_Caption, c.col(R.color.accent)).margins(c, top = 2))
        stats.addView(right)
        body.addView(stats)
        ring.post { ring.set(accuracy / 100f) }
        countUp(xp)

        // Pet reaction
        val petCard = Kit.card(c, 14, 18).apply { orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL }
        val pet = PetView(c).apply { setFrom(services.pet.state); shadow = false; layoutParams = LinearLayout.LayoutParams(c.dpi(72), c.dpi(72)) }
        val full = services.goalReached()
        pet.mood = if (full) Mood.FULL else Mood.CHEER
        petCard.addView(pet)
        val petText = Kit.vbox(c) { layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(12) } }
        val name = services.pet.state.name
        petText.addView(Kit.text(c, str(if (full) R.string.pet_full_after else R.string.pet_ate, name), R.style.Text_BodyStrong))
        val goal = services.settings.dailyGoalXp
        petText.addView(Kit.text(c, str(R.string.goal_progress, services.todayXp().coerceAtMost(goal), goal), R.style.Text_Caption).margins(c, top = 2))
        petCard.addView(petText)
        petCard.isClickable = true
        petCard.setOnClickListener { activity.openPetHome() }
        body.addView(petCard)
        petCard.postDelayed({ pet.react(Mood.CHEER) }, 900)

        // Rewarded: double XP (optional, never blocking)
        if (services.ads.showsAds && xp > 0 && session.kind != SessionKind.SPEED) {
            val offer = Kit.clickableCard(c, 14, 10, c.col(R.color.accent_container), onClick = { doubleXp() }) {
                orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
                addView(Kit.text(c, "🎁", sizeSp = 24f).apply { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) })
                addView(Kit.vbox(c) {
                    layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(12) }
                    addView(Kit.text(c, str(R.string.double_xp), R.style.Text_BodyStrong))
                    addView(Kit.text(c, str(R.string.double_xp_sub), R.style.Text_Caption))
                })
            }
            offer.tag = "doubleOffer"
            body.addView(offer)
        }

        // New words mastered
        val mastered = answers.filter { it.correct && it.ex.key.startsWith("w:") }.mapNotNull { a -> services.store.item(a.ex.key)?.takeIf { it.mastered }?.let { a.ex.key.removePrefix("w:") } }.distinct()
            .mapNotNull { services.content.wordById[it] }
        if (mastered.isNotEmpty()) {
            body.addView(Kit.section(c, str(R.string.new_mastered)))
            val row = Kit.hbox(c)
            mastered.forEach { w ->
                row.addView(Kit.card(c, 12, 0, c.col(R.color.success_container), null) {
                    layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { marginEnd = c.dpi(10) }
                    addView(Kit.text(c, "⭐ " + w.lemma, R.style.Text_BodyStrong))
                    addView(Kit.text(c, w.gloss, R.style.Text_Caption))
                    setOnClickListener { activity.open(WordDetailScreen(activity, w.id)) }
                })
            }
            body.addView(Kit.hscroll(c, row))
        }

        // Need more practice: weak tags from wrong answers
        val weak = answers.filter { !it.correct }.flatMap { it.ex.tags }.filter { TagNames.has(it) }.groupingBy { it }.eachCount().entries.sortedByDescending { it.value }.take(3)
        if (weak.isNotEmpty()) {
            body.addView(Kit.section(c, str(R.string.need_practice)))
            val flow = Kit.flow(c)
            weak.forEach { (t, _) -> flow.addView(Kit.chip(c, TagNames.label(c, t), false, c.col(R.color.error)) { activity.startSession(services.builder().weak(str(R.string.practice_weak), System.currentTimeMillis())) }) }
            body.addView(flow)
        }

        // Review list (wrong answers first; all items for timed sessions)
        val review = if (session.timed) answers else answers.filter { !it.correct }
        if (review.isNotEmpty()) {
            body.addView(Kit.section(c, str(R.string.review_answers)))
            review.distinctBy { it.ex.key }.take(15).forEach { a -> body.addView(reviewRow(a)) }
        }

        // Actions
        body.addView(Kit.primary(c, str(R.string.study_more), 24) { again() })
        val wrong = answers.filter { !it.correct }.map { it.ex }.distinctBy { it.key }
        if (wrong.isNotEmpty()) body.addView(Kit.secondary(c, str(R.string.retry_wrong)) {
            activity.navigator.pop()
            activity.startSession(Session(SessionKind.MISTAKES, str(R.string.retry_wrong), wrong.mapNotNull { services.builder().exerciseFor(it.key) }.toMutableList()))
        })
        body.addView(Kit.secondary(c, str(R.string.go_home)) { activity.selectTab(Tab.TODAY) })

        // Effects
        if (accuracy >= 80) body.postDelayed({ activity.particles.confetti(60); services.sfx.play(Sfx.Sound.SUCCESS) }, 450)
        else body.postDelayed({ services.sfx.play(Sfx.Sound.POP) }, 400)
        body.setOnClickListener { skip() }

        // Stage up (after the result sequence), then optional prompts.
        val pending = services.pet.state.pendingStageUp
        if (pending > 0) body.postDelayed({ if (activity.navigator.current === this) activity.showStageUp(pending) }, 1600)
        else body.postDelayed({ maybePrompts() }, 1800)
    }

    private fun reviewRow(a: Answer): View {
        val c = ctx
        val (q, correct) = when (val e = a.ex) {
            is Exercise.Choice -> (if (e.passage != null) e.passage.title + " (${e.blank + 1})" else e.stem.ifBlank { e.speak ?: "" }).replace("[", "").replace("]", "") to e.options[e.answer]
            is Exercise.FindError -> e.stem.replace("[", "").replace("]", "") to e.fix
            is Exercise.WordOrder -> (e.translation ?: "") to e.sentence
            is Exercise.Spelling -> e.gloss to e.word.lemma
            is Exercise.Flashcard -> e.word.lemma to e.word.gloss
            is Exercise.Matching -> e.left.joinToString(", ") to e.right.joinToString(", ")
            is Exercise.PictureChoice -> e.words.joinToString(", ") { it.lemma } to e.words[e.answer].lemma
        }
        return Kit.card(c, 14, 8).apply {
            addView(Kit.text(c, q, R.style.Text_Body).apply { textSize = 15f })
            if (a.given != null && !a.correct) addView(Kit.text(c, "✗ " + a.given, R.style.Text_Caption, c.col(R.color.error)).margins(c, top = 4))
            addView(Kit.text(c, "✓ $correct", R.style.Text_BodyStrong, c.col(R.color.success)).margins(c, top = 2))
            val expl = (a.ex as? Exercise.Choice)?.explanation ?: (a.ex as? Exercise.FindError)?.explanation
            if (!expl.isNullOrBlank()) addView(Kit.text(c, expl, R.style.Text_Caption).margins(c, top = 4))
        }
    }

    private fun countUp(target: Int) {
        val tv = xpText ?: return
        if (ctx.reduceMotion) { tv.text = "+$target XP"; return }
        ValueAnimator.ofInt(0, target).apply {
            duration = 600; startDelay = 500
            addUpdateListener { tv.text = "+${it.animatedValue} XP" }
            start()
        }.also { anims += it }
    }

    private fun skip() {
        anims.forEach { it.end() }
        body.animate().cancel()
    }

    private fun doubleXp() {
        if (doubled) return
        val ads = services.ads
        val run = {
            ads.showRewarded(activity, onRewarded = {
                doubled = true
                val r = services.creditXp(xp)
                xp += r.gained
                xpText?.text = "+$xp XP"
                xpText?.pop()
                body.findViewWithTag<View>("doubleOffer")?.show(false)
                activity.toast(str(R.string.double_xp_done, r.gained))
                if (r.stageUp != null) activity.showStageUp(r.stageUp)
            })
        }
        if (ads.rewardedReady) run() else { activity.toast(str(R.string.ad_loading)); ads.preloadRewarded { run() } }
    }

    private fun again() {
        val b = services.builder()
        val now = System.currentTimeMillis()
        val next = when (session.kind) {
            SessionKind.GRAMMAR -> session.gp?.let { b.grammar(session.title, it) }
            SessionKind.SPEED -> b.speed(session.title, services.settings.userLevel)
            else -> b.quick(str(R.string.quick_session), now, services.settings.preferredTopics(), services.settings.userLevel)
        } ?: b.quick(str(R.string.quick_session), now, services.settings.preferredTopics(), services.settings.userLevel)
        activity.navigator.pop()
        activity.startSession(next)
    }

    /** Notification rationale after the first completed session; rate prompt rules (SKILL.md S23). */
    private fun maybePrompts() {
        if (activity.navigator.current !== this) return
        val s = services.settings
        if (!s.notifAsked && s.sessionsDone >= 1) {
            s.notifAsked = true
            Dialogs.confirm(ctx, str(R.string.notif_title), str(R.string.notif_msg, services.pet.state.name), str(R.string.notif_yes), str(R.string.not_now)) {
                Reminders.enable(activity, s.reminderHour)
            }
            return
        }
        val now = System.currentTimeMillis()
        if (s.goodSessions >= 3 && accuracy >= 80 && answers.lastOrNull()?.correct == true && now - s.rateAskedAt > 90L * 86_400_000L) {
            s.rateAskedAt = now
            Dialogs.confirm(ctx, str(R.string.rate_title), str(R.string.rate_msg), str(R.string.rate_yes), str(R.string.not_now)) {
                runCatching { activity.startActivity(android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse("https://play.google.com/store/apps/details?id=" + activity.packageName.removeSuffix(".debug")))) }
            }
        }
    }

    override fun onBack(): Boolean { activity.navigator.pop(); return true }
}
