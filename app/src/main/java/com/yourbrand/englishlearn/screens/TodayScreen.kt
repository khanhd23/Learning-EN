package com.yourbrand.englishlearn.screens

import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.speech.SpeechRecognizer
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.Services
import com.yourbrand.englishlearn.learning.LearningStore
import com.yourbrand.englishlearn.learning.Scheduler
import com.yourbrand.englishlearn.learning.SessionKind
import com.yourbrand.englishlearn.pet.PetView
import com.yourbrand.englishlearn.ui.*
import com.yourbrand.englishlearn.ui.views.ProgressRing
import java.util.Calendar

/** S03 — Hôm nay (Home dashboard). */
class TodayScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val tab = Tab.TODAY
    override val allowsBanner = true
    override val petMode = PetMode.FLOATING

    override fun headerActions(bar: LinearLayout) = HubHeader.build(this, bar, greeting())

    private fun greeting(): String {
        val h = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return str(when { h < 11 -> R.string.greet_morning; h < 14 -> R.string.greet_noon; h < 18 -> R.string.greet_afternoon; else -> R.string.greet_evening })
    }

    override fun build(body: LinearLayout) {
        val c = ctx
        val s = services
        val now = System.currentTimeMillis()
        s.ensureWeekly(now)
        val goal = s.settings.dailyGoalXp
        val xp = s.todayXp()

        // 2. Daily goal card (hero)
        val hero = Kit.vbox(c, 20).apply {
            background = c.getDrawable(R.drawable.bg_hero)
            elevation = c.dp(4f)
            layoutParams = lp().apply { topMargin = c.dpi(8) }
        }
        val top = Kit.hbox(c)
        val texts = Kit.vbox(c) { layoutParams = lp(0, WRAP_CONTENT, 1f) }
        texts.addView(Kit.text(c, str(R.string.daily_goal), R.style.Text_Caption, c.col(R.color.hero_muted)))
        texts.addView(Kit.text(c, "$xp / $goal XP", R.style.Text_Display, c.col(R.color.hero_text), 28f))
        val left = (goal - xp).coerceAtLeast(0)
        texts.addView(Kit.text(c, if (left > 0) str(R.string.xp_left, left) else str(R.string.goal_done), R.style.Text_Body, c.col(R.color.hero_muted)).apply { textSize = 14f })
        top.addView(texts)
        val ringBox = FrameLayout(c).apply { layoutParams = LinearLayout.LayoutParams(c.dpi(88), c.dpi(88)) }
        val ring = ProgressRing(c).apply { color = c.col(R.color.accent); trackColor = 0x33FFFFFF; stroke = c.dp(9f) }
        ringBox.addView(ring, FrameLayout.LayoutParams(c.dpi(88), c.dpi(88)))
        ringBox.addView(Kit.text(c, if (xp >= goal) "🎉" else "🎯", sizeSp = 26f).apply { gravity = Gravity.CENTER }, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.CENTER))
        top.addView(ringBox)
        hero.addView(top)
        ring.post { ring.set(xp / goal.toFloat().coerceAtLeast(1f)) }
        hero.addView(TextView(c).apply {
            text = str(R.string.study_minutes, s.settings.dailyMinutes.coerceAtMost(5))
            gravity = Gravity.CENTER; textSize = 16f; setTypeface(typeface, android.graphics.Typeface.BOLD)
            setTextColor(c.col(R.color.primary_deep))
            setBackgroundResource(R.drawable.btn_on_hero)
            layoutParams = lp(h = c.dpi(52)).apply { topMargin = c.dpi(16) }
            tag = "petAvoid"
            onTap { startQuick() }
        })
        body.addView(hero)

        // Placement test offer (inside the first week, never forced)
        if (!s.settings.placementOffered && s.settings.sessionsDone in 2..10) {
            body.addView(Kit.clickableCard(c, 14, 12, c.col(R.color.info_container), onClick = {
                s.settings.placementOffered = true
                activity.open(PlacementScreen(activity))
            }) {
                addView(Kit.text(c, str(R.string.placement_title), R.style.Text_BodyStrong))
                addView(Kit.text(c, str(R.string.placement_sub), R.style.Text_Caption))
            })
        }

        // 3. Continue
        continueCard()?.let { body.addView(it) }

        // 4. Due reviews
        val dailyDone = s.settings.dailyChallengeDay == LearningStore.dayKey(now)
        body.addView(Kit.clickableCard(c, 16, 12, c.col(R.color.accent_container), onClick = {
            if (!dailyDone) {
                s.content.petLines["Q_DAILY_START"]?.randomOrNull()?.let { activity.petView.say(it.replace("{name}", s.pet.state.name), false) }
                activity.startSession(s.builder().daily(str(R.string.daily_challenge), now, s.settings.preferredTopics(), s.settings.userLevel))
            }
        }) {
            orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
            addView(Kit.emojiTile(c, "🎯", c.col(R.color.surface)))
            addView(Kit.vbox(c) {
                layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(14) }
                addView(Kit.text(c, str(R.string.daily_challenge), R.style.Text_BodyStrong))
                addView(Kit.text(c, if (dailyDone) str(R.string.daily_challenge_done) else str(R.string.daily_challenge_desc), R.style.Text_Caption))
            })
            addView(Kit.chevron(c))
        })
        val week = com.yourbrand.englishlearn.learning.WeekKey.mondayOf(s.today(now))
        val newWords = s.store.firstSeenCount(week).coerceAtMost(20)
        val dailyChallenges = s.settings.weeklyDailyChallenges.coerceAtMost(3)
        val speakingDays = s.settings.weeklySpeakingDays.size.coerceAtMost(2)
        val stories = s.settings.weeklyStories.coerceAtMost(2)
        body.addView(Kit.card(c, 16, 12) {
            addView(Kit.text(c, str(R.string.weekly_quests), R.style.Text_BodyStrong))
            weeklyRow(this, str(R.string.quest_new_words, 20) + "  " + str(R.string.quest_progress, newWords, 20), newWords >= 20)
            weeklyRow(this, str(R.string.quest_daily, 3) + "  " + str(R.string.quest_progress, dailyChallenges, 3), dailyChallenges >= 3)
            if (SpeechRecognizer.isRecognitionAvailable(c)) weeklyRow(this, str(R.string.quest_speaking, 2) + "  " + str(R.string.quest_progress, speakingDays, 2), speakingDays >= 2)
            weeklyRow(this, str(R.string.quest_stories, 2) + "  " + str(R.string.quest_progress, stories, 2), stories >= 2)
            addView(Kit.text(c, str(R.string.streak_shields, s.pet.state.freezeTokens), R.style.Text_Caption).margins(c, top = 4))
        })

        // 4. Due reviews
        val due = s.store.allItems().count { Scheduler.isDue(it, now) }
        body.addView(Kit.clickableCard(c, 16, 12, onClick = { activity.startSession(s.builder().review(str(R.string.review_now_title), now)) }) {
            orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
            addView(Kit.emojiTile(c, "🔁", c.col(R.color.info_container)))
            addView(Kit.vbox(c) {
                layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(14) }
                addView(Kit.text(c, str(R.string.due_today), R.style.Text_BodyStrong))
                addView(Kit.text(c, if (due > 0) str(R.string.due_count, due) else str(R.string.due_none), R.style.Text_Caption))
            })
            addView(Kit.badge(c, str(R.string.review_now), c.col(R.color.primary), c.col(R.color.on_primary)).apply { setPadding(c.dpi(14), c.dpi(8), c.dpi(14), c.dpi(8)); textSize = 13f })
        })

        // 5. Weak spots
        val weak = s.weakTags().take(3)
        if (weak.isNotEmpty()) {
            body.addView(Kit.section(c, str(R.string.weak_spots)))
            val flow = Kit.flow(c)
            weak.forEach { t -> flow.addView(Kit.chip(c, TagNames.label(c, t), false, c.col(R.color.error)) { activity.startSession(s.builder().weak(str(R.string.practice_weak), now)) }) }
            body.addView(flow)
            body.addView(Kit.secondary(c, str(R.string.practice_weak), 4) { activity.startSession(s.builder().weak(str(R.string.practice_weak), now)) })
        }

        // 6. Today's plan (from goals)
        body.addView(Kit.section(c, str(R.string.today_plan)))
        val plan = Kit.card(c, 6, 4)
        val todayKey = LearningStore.dayKey(now)
        val todayHist = s.store.history().filter { LearningStore.dayKey(it.at) == todayKey }
        val topic = topicForPlan()
        val list = selectedListForPlan()
        planRow(plan, str(R.string.plan_words, s.settings.newWordsPerSession * 2, list?.label ?: topic?.name ?: ""), todayHist.any { it.kind in listOf("QUICK", "TOPIC", "WORDS", "WELCOME", "MEAL") }) {
            if (list != null) {
                val words = s.content.wordsByList[list.id].orEmpty().filter { it.tier == "silver" }
                activity.startSession(s.builder().words(SessionKind.WORDS, list.label, words, com.yourbrand.englishlearn.learning.VocabMode.MIXED, newFirst = true))
            } else {
                val t = topic ?: return@planRow
                activity.startSession(s.builder().words(SessionKind.TOPIC, t.name, s.content.wordsByTopic[t.id].orEmpty(), com.yourbrand.englishlearn.learning.VocabMode.MIXED))
            }
        }
        val gp = nextGrammarPoint()
        planRow(plan, str(R.string.plan_grammar, gp?.title ?: ""), todayHist.any { it.kind == "GRAMMAR" }) { gp?.let { activity.open(GrammarLessonScreen(activity, it.id)) } }
        val examLabel = if ("toeic" in s.settings.goals) str(R.string.plan_part5) else str(R.string.plan_school)
        planRow(plan, examLabel, todayHist.any { it.kind == "EXAM_TYPE" || it.kind == "BUILDER" || it.kind == "MOCK" }) {
            val fmt = if ("toeic" in s.settings.goals) "toeic_p5" else "school"
            activity.startSession(s.builder().examType(examLabel, fmt, null, 3, 5))
        }
        body.addView(plan)

        // Word of the day (fun, changes daily)
        val wod = s.content.words.filter { it.level >= 2 && it.senses.size >= 2 }.let { list -> if (list.isEmpty()) null else list[Math.floorMod(todayKey, list.size.toLong()).toInt()] }
        if (wod != null) {
            body.addView(Kit.section(c, str(R.string.word_of_day)))
            body.addView(Kit.clickableCard(c, 16, 4, onClick = { activity.open(WordDetailScreen(activity, wod.id)) }) {
                val r = Kit.hbox(c)
                r.addView(Kit.text(c, wod.lemma, R.style.Text_Display).apply { layoutParams = lp(0, WRAP_CONTENT, 1f) })
                r.addView(ImageView(c).apply {
                    setImageResource(R.drawable.ic_volume); tintRes(R.color.primary)
                    background = c.rounded(c.col(R.color.primary_container), 100f, ripple = true)
                    val p = c.dpi(10); setPadding(p, p, p, p)
                    layoutParams = LinearLayout.LayoutParams(c.dpi(44), c.dpi(44))
                    contentDescription = str(R.string.listen)
                    setOnClickListener { s.tts.speak(wod.lemma) }
                })
                addView(r)
                addView(Kit.text(c, "/${wod.ipa}/", R.style.Text_Caption))
                wod.senses.take(3).forEach { sense -> addView(Kit.text(c, "• (${sense.pos}) ${sense.gloss}", R.style.Text_Body).apply { textSize = 15f }.margins(c, top = 4)) }
            })
        }

        // 60-second challenge
        body.addView(Kit.clickableCard(c, 16, 12, c.col(R.color.accent_container), onClick = { activity.startSession(s.builder().speed(str(R.string.speed_title), s.settings.userLevel)) }) {
            orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
            addView(Kit.emojiTile(c, "⚡", c.col(R.color.surface)))
            addView(Kit.vbox(c) {
                layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(14) }
                addView(Kit.text(c, str(R.string.speed_title), R.style.Text_BodyStrong))
                addView(Kit.text(c, str(R.string.speed_sub), R.style.Text_Caption))
            })
            addView(Kit.chevron(c))
        })

        // 7. Exam countdown
        val examDate = s.settings.examDate
        if (examDate > now) {
            val days = ((examDate - now) / 86_400_000L).toInt() + 1
            body.addView(Kit.card(c, 16, 12, c.col(R.color.info_container), null) {
                addView(Kit.text(c, str(R.string.exam_countdown, days), R.style.Text_Title))
                addView(Kit.text(c, str(R.string.exam_focus, s.weakTags().firstOrNull()?.let { TagNames.label(c, it) } ?: str(R.string.tag_pos)), R.style.Text_Caption).margins(c, top = 2))
            })
        }

        // 8. Pet teaser (when the floating pet is hidden)
        if (!s.settings.petVisible) {
            val st = s.pet.state
            body.addView(Kit.clickableCard(c, 12, 12, onClick = { activity.openPetHome() }) {
                orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
                addView(PetView(c).apply { setFrom(st); mood = activity.currentMood(); shadow = false; layoutParams = LinearLayout.LayoutParams(c.dpi(64), c.dpi(64)) })
                addView(Kit.vbox(c) {
                    layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(12) }
                    addView(Kit.text(c, str(R.string.pet_teaser, st.name, activity.moodWord(activity.currentMood())), R.style.Text_BodyStrong))
                    addView(Kit.text(c, str(R.string.open_pet_home), R.style.Text_Caption))
                })
                addView(Kit.chevron(c))
            })
        }
    }

    private fun planRow(parent: LinearLayout, text: String, done: Boolean, onClick: () -> Unit) {
        val c = ctx
        val check = ImageView(c).apply {
            setImageResource(if (done) R.drawable.ic_check else R.drawable.ic_chevron_right)
            tint(c.col(if (done) R.color.on_primary else R.color.muted))
            background = if (done) c.rounded(c.col(R.color.success), 100f) else c.rounded(c.col(R.color.surface_variant), 100f)
            val p = c.dpi(5); setPadding(p, p, p, p)
            layoutParams = LinearLayout.LayoutParams(c.dpi(28), c.dpi(28))
        }
        parent.addView(Kit.row(c, check, text, null, null, onClick).apply {
            if (done) (getChildAt(1) as LinearLayout).getChildAt(0).let { (it as TextView).paintFlags = it.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG; it.alpha = 0.6f }
        })
        if (done && !ctx.reduceMotion) { check.scaleX = 0f; check.scaleY = 0f; check.animate().scaleX(1f).scaleY(1f).setStartDelay(300).setDuration(200).start() }
    }

    private fun weeklyRow(parent: LinearLayout, text: String, done: Boolean) {
        val c = ctx
        val check = if (done) ImageView(c).apply {
            setImageResource(R.drawable.ic_check)
            tint(c.col(R.color.on_primary))
            background = c.rounded(c.col(R.color.success), 100f)
            val p = c.dpi(5); setPadding(p, p, p, p)
            layoutParams = LinearLayout.LayoutParams(c.dpi(28), c.dpi(28))
        } else null
        val row = Kit.row(c, null, text, trailing = check)
        parent.addView(row)
        if (done) {
            val title = (row.getChildAt(0) as LinearLayout).getChildAt(0) as TextView
            title.paintFlags = title.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            title.alpha = 0.6f
            if (!c.reduceMotion && check != null) {
                check.scaleX = 0f; check.scaleY = 0f
                check.animate().scaleX(1f).scaleY(1f).setStartDelay(300).setDuration(200).start()
            }
        }
    }

    private fun topicForPlan() = services.settings.preferredTopics().ifEmpty { services.content.topics.map { it.id } }
        .let { ids -> ids[Math.floorMod(services.today(), ids.size.toLong()).toInt()] }.let { services.content.topicById[it] }

    private fun selectedListForPlan() = when {
        "toeic" in services.settings.goals -> "toeic"
        "ielts" in services.settings.goals -> "ielts_academic"
        "work" in services.settings.goals -> "business"
        else -> null
    }?.let { id -> services.content.wordLists.firstOrNull { it.id == id } }

    private fun nextGrammarPoint() = services.content.grammar.firstOrNull { services.store.stars(it.id) == 0 }

    private fun continueCard(): View? {
        val c = ctx
        val s = services
        if (s.settings.mockInProgress != null) {
            return Kit.clickableCard(c, 16, 12, c.col(R.color.primary_container), onClick = { MockScreen.resume(activity) }) {
                addView(Kit.text(c, str(R.string.continue_label), R.style.Text_Caption))
                addView(Kit.text(c, str(R.string.continue_mock), R.style.Text_BodyStrong))
            }
        }
        val gp = s.content.grammar.firstOrNull { s.store.stars(it.id) == 0 && s.content.questionsByPoint[it.id].orEmpty().any { q -> s.store.item(q.key)?.seen ?: 0 > 0 } } ?: return null
        val qs = s.content.questionsByPoint[gp.id].orEmpty()
        val pct = qs.count { (s.store.item(it.key)?.correct ?: 0) > 0 } * 100 / qs.size.coerceAtLeast(1)
        return Kit.clickableCard(c, 16, 12, onClick = { activity.open(GrammarLessonScreen(activity, gp.id)) }) {
            addView(Kit.text(c, str(R.string.continue_label), R.style.Text_Caption))
            addView(Kit.text(c, str(R.string.continue_grammar, gp.title, pct), R.style.Text_BodyStrong))
            addView(com.yourbrand.englishlearn.ui.views.Bar(c).apply { layoutParams = lp(h = c.dpi(6)).apply { topMargin = c.dpi(10) }; post { set(pct / 100f) } })
        }
    }

    private fun startQuick() {
        val s = services
        val title = str(R.string.quick_session)
        activity.startSession(s.builder().quick(title, System.currentTimeMillis(), s.settings.preferredTopics(), s.settings.userLevel, 10))
    }
}

/** Shared app-bar for the five hubs: title/greeting, streak badge, search icon (SKILL.md 1.2). */
object HubHeader {
    fun build(screen: ScrollScreen, bar: LinearLayout, title: String) {
        val c = screen.activity
        val s = c.services
        bar.setPadding(c.dpi(16), c.dpi(8), c.dpi(8), c.dpi(4))
        bar.addView(Kit.ellipsize(Kit.text(c, title, R.style.Text_Display)).apply { layoutParams = lp(0, WRAP_CONTENT, 1f) })
        val streak = s.streak()
        val badge = Kit.hbox(c) {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, c.dpi(36))
            background = c.rounded(c.col(R.color.accent_container), 100f)
            setPadding(c.dpi(10), 0, c.dpi(12), 0)
            contentDescription = c.getString(R.string.streak_cd, streak)
            addView(Kit.icon(c, R.drawable.ic_flame, c.col(if (streak > 0) R.color.accent else R.color.muted), 20))
            addView(Kit.text(c, streak.toString(), R.style.Text_BodyStrong).apply { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { marginStart = c.dpi(4) } })
            setOnClickListener { c.selectTab(Tab.ME) }
        }
        bar.addView(badge)
        bar.addView(ImageView(c).apply {
            setImageResource(R.drawable.ic_search); tintRes(R.color.on_surface)
            setBackgroundResource(R.drawable.ripple_circle)
            val p = c.dpi(12); setPadding(p, p, p, p)
            contentDescription = c.getString(R.string.search)
            layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48)).apply { marginStart = c.dpi(4) }
            setOnClickListener { c.open(SearchScreen(c)) }
        })
    }
}

private val android.content.Context.services: Services get() = (applicationContext as com.yourbrand.englishlearn.EnglishApp).services
