package com.yourbrand.englishlearn.screens

import android.app.DatePickerDialog
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import android.widget.LinearLayout
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.learning.WeaknessAnalyzer
import com.yourbrand.englishlearn.ui.*
import com.yourbrand.englishlearn.ui.views.Bar
import com.yourbrand.englishlearn.ui.views.DayBars
import com.yourbrand.englishlearn.ui.views.Heatmap
import java.text.DateFormat
import java.util.Calendar
import java.util.Date

/** S19 — Tôi: progress, achievements, exam goal, entry to settings. */
class MeScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val tab = Tab.ME
    override val allowsBanner = true
    override val petMode = PetMode.FLOATING

    override fun headerActions(bar: LinearLayout) {
        HubHeader.build(this, bar, str(R.string.tab_me))
        bar.addView(ImageView(ctx).apply {
            setImageResource(R.drawable.ic_settings); tintRes(R.color.on_surface); setBackgroundResource(R.drawable.ripple_circle)
            val p = ctx.dpi(12); setPadding(p, p, p, p); contentDescription = str(R.string.settings)
            layoutParams = LinearLayout.LayoutParams(ctx.dpi(48), ctx.dpi(48))
            setOnClickListener { activity.open(SettingsScreen(activity)) }
        })
    }

    override fun build(body: LinearLayout) {
        val c = ctx
        val s = services
        val items = s.store.allItems()
        val wordsKnown = items.count { it.key.startsWith("w:") && it.mastered }
        val grammarDone = s.content.grammar.count { s.store.stars(it.id) > 0 }
        val streak = s.streak()

        // Overview
        val level = str(listOf(R.string.lvl_beginner, R.string.lvl_basic, R.string.lvl_inter, R.string.lvl_upper)[s.settings.level.coerceIn(0, 3)])
        body.addView(Kit.card(c, 16, 4) {
            val r = Kit.hbox(c)
            r.addView(Kit.chip(c, "🎓 $level", true).apply { (layoutParams as LinearLayout.LayoutParams).bottomMargin = 0 })
            r.addView(View(c).apply { layoutParams = lp(0, 1, 1f) })
            r.addView(Kit.text(c, "🔥 " + str(R.string.streak_days, streak), R.style.Text_BodyStrong).apply { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) })
            addView(r)
            // Weekly goal dots
            val dots = Kit.hbox(c).margins(c, top = 14)
            val names = listOf("T2", "T3", "T4", "T5", "T6", "T7", "CN")
            val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK).let { (it + 5) % 7 }
            s.weekDots().forEachIndexed { i, on ->
                val dayIdx = (today - 6 + i + 7) % 7
                dots.addView(Kit.vbox(c) {
                    gravity = Gravity.CENTER_HORIZONTAL
                    layoutParams = lp(0, WRAP_CONTENT, 1f)
                    addView(View(c).apply { background = c.rounded(c.col(if (on) R.color.accent else R.color.surface_variant), 100f); layoutParams = LinearLayout.LayoutParams(c.dpi(22), c.dpi(22)) })
                    addView(Kit.text(c, names[dayIdx], R.style.Text_Caption).apply { gravity = Gravity.CENTER; textSize = 11f })
                })
            }
            addView(dots)
            val stats = Kit.hbox(c).margins(c, top = 14)
            stats.addView(stat(wordsKnown.toString(), str(R.string.words_mastered)))
            stats.addView(stat(grammarDone.toString(), str(R.string.grammar_mastered)))
            stats.addView(stat(s.pet.state.totalXp.toString(), str(R.string.total_xp)))
            addView(stats)
        })

        // 14-day chart
        body.addView(Kit.section(c, str(R.string.chart_14)))
        val days = s.store.allDays()
        val todayKey = s.today()
        val values = IntArray(14) { i -> days[todayKey - 13 + i]?.xp ?: 0 }
        val labels = (0 until 14).map { i -> Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, i - 13) }.get(Calendar.DAY_OF_MONTH).toString() }
        val detail = Kit.text(c, "", R.style.Text_Caption)
        body.addView(Kit.card(c, 16, 4) {
            addView(detail)
            addView(DayBars(c).apply {
                goal = s.settings.dailyGoalXp
                layoutParams = lp(h = c.dpi(130)).apply { topMargin = c.dpi(8) }
                onSelect = { i ->
                    val d = days[todayKey - 13 + i]
                    detail.text = str(R.string.day_detail, labels[i], d?.xp ?: 0, (d?.seconds ?: 0) / 60, d?.answered ?: 0)
                }
                post { set(values, labels); onSelect?.invoke(13) }
            })
        })

        // Heatmap 8 weeks
        body.addView(Kit.section(c, str(R.string.heatmap)))
        body.addView(Kit.card(c, 16, 4) {
            addView(Heatmap(c).apply { set(IntArray(56) { i -> days[todayKey - 55 + i]?.answered ?: 0 }) })
        })

        // Weak spots
        val weak = WeaknessAnalyzer.rank(s.store.tagStats()).filter { TagNames.has(it.tag) }.take(5)
        body.addView(Kit.section(c, str(R.string.your_weak)))
        if (weak.isEmpty()) body.addView(Kit.text(c, str(R.string.weak_none), R.style.Text_Caption))
        else body.addView(Kit.card(c, 14, 4) {
            weak.forEach { t ->
                val r = Kit.hbox(c) { setPadding(0, c.dpi(6), 0, c.dpi(6)) }
                val col = Kit.vbox(c) { layoutParams = lp(0, WRAP_CONTENT, 1f) }
                col.addView(Kit.text(c, TagNames.label(c, t.tag) + "  ·  ${t.accuracy}%", R.style.Text_Body).apply { textSize = 14f })
                col.addView(Bar(c).apply { color = c.col(if (t.accuracy >= 70) R.color.accent else R.color.error); layoutParams = lp(h = c.dpi(6)).apply { topMargin = c.dpi(4) }; post { set(t.accuracy / 100f) } })
                r.addView(col)
                r.addView(Kit.chip(c, str(R.string.practice), false) { activity.startSession(s.builder().weak(str(R.string.practice_weak), System.currentTimeMillis())) }.apply { (layoutParams as LinearLayout.LayoutParams).apply { marginStart = c.dpi(12); bottomMargin = 0 } })
                addView(r)
            }
        })

        // Achievements
        body.addView(Kit.section(c, str(R.string.achievements)))
        val list = Achievements.compute(activity)
        val seen = activity.getSharedPreferences("ach", 0)
        var row: LinearLayout? = null
        list.forEachIndexed { i, a ->
            if (i % 3 == 0) { row = Kit.hbox(c) { gravity = Gravity.TOP }.margins(c, top = 10); body.addView(row) }
            val tile = Kit.card(c, 10, 0, c.col(if (a.unlocked) R.color.accent_container else R.color.surface_variant), null) {
                gravity = Gravity.CENTER_HORIZONTAL
                layoutParams = lp(0, WRAP_CONTENT, 1f).apply { if (i % 3 != 0) marginStart = c.dpi(10) }
                minimumHeight = c.dpi(112)
                addView(Kit.text(c, if (a.unlocked) a.emoji else "🔒", sizeSp = 28f).apply { gravity = Gravity.CENTER; alpha = if (a.unlocked) 1f else 0.5f })
                addView(Kit.ellipsize(Kit.text(c, a.title, R.style.Text_BodyStrong).apply { textSize = 12.5f; gravity = Gravity.CENTER }, 2).margins(c, top = 4))
                contentDescription = a.title + ", " + (if (a.unlocked) str(R.string.unlocked) else a.hint)
                setOnClickListener { activity.toast(if (a.unlocked) "${a.emoji} ${a.title}" else a.hint) }
            }
            row!!.addView(tile)
            if (a.unlocked && !seen.getBoolean(a.id, false)) {
                seen.edit().putBoolean(a.id, true).apply()
                tile.postDelayed({ tile.pop(0.6f); val loc = IntArray(2); tile.getLocationInWindow(loc); activity.particles.burst(loc[0] + tile.width / 2f, loc[1] + tile.height / 2f, 10) }, 400L + i * 60)
            }
        }
        if (list.size % 3 != 0) repeat(3 - list.size % 3) { row?.addView(View(c).apply { layoutParams = lp(0, 1, 1f).apply { marginStart = c.dpi(10) } }) }

        // Exam goal
        body.addView(Kit.section(c, str(R.string.exam_goal)))
        val goalFmt = s.settings.examGoal?.let { s.content.formatById(it)?.label } ?: str(R.string.not_set)
        val date = s.settings.examDate.takeIf { it > 0 }?.let { DateFormat.getDateInstance(DateFormat.MEDIUM).format(Date(it)) } ?: str(R.string.not_set)
        body.addView(Kit.card(c, 6, 4) {
            addView(Kit.row(c, Kit.emojiTile(c, "🎯", c.col(R.color.primary_container)), str(R.string.exam_type), goalFmt, Kit.chevron(c)) { pickExam() })
            addView(Kit.divider(c))
            addView(Kit.row(c, Kit.emojiTile(c, "📅", c.col(R.color.info_container)), str(R.string.exam_date), date, Kit.chevron(c)) { pickDate() })
        })
        body.addView(Kit.secondary(c, "⚙️  " + str(R.string.settings), 20) { activity.open(SettingsScreen(activity)) })
    }

    private fun stat(value: String, label: String) = Kit.vbox(ctx) {
        gravity = Gravity.CENTER_HORIZONTAL
        layoutParams = lp(0, WRAP_CONTENT, 1f)
        addView(Kit.text(ctx, value, R.style.Text_Title, ctx.col(R.color.primary), 22f).apply { gravity = Gravity.CENTER })
        addView(Kit.text(ctx, label, R.style.Text_Caption).apply { gravity = Gravity.CENTER; textSize = 12f })
    }

    private fun pickExam() {
        val sheet = BottomSheet(activity)
        sheet.show { box ->
            box.addView(Kit.text(ctx, str(R.string.exam_type), R.style.Text_Title))
            services.content.formats.forEach { f ->
                box.addView(Kit.row(ctx, null, f.label, f.family, if (services.settings.examGoal == f.id) Kit.icon(ctx, R.drawable.ic_check, ctx.col(R.color.primary)) else null) {
                    services.settings.examGoal = f.id; sheet.dismiss(); refresh()
                })
            }
        }
    }

    private fun pickDate() {
        val cal = Calendar.getInstance().apply { if (services.settings.examDate > 0) timeInMillis = services.settings.examDate else add(Calendar.MONTH, 2) }
        DatePickerDialog(activity, { _, y, m, d ->
            services.settings.examDate = Calendar.getInstance().apply { set(y, m, d, 9, 0, 0) }.timeInMillis
            refresh()
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).apply { datePicker.minDate = System.currentTimeMillis() }.show()
    }
}

/** Badges computed from progress (no server). */
object Achievements {
    data class A(val id: String, val emoji: String, val title: String, val hint: String, val unlocked: Boolean)

    fun compute(a: MainActivity): List<A> {
        val s = a.services
        val items = s.store.allItems()
        val known = items.count { it.key.startsWith("w:") && it.mastered }
        val streak = s.streak()
        val hist = s.store.history()
        val stars = s.content.grammar.count { s.store.stars(it.id) > 0 }
        val l1 = s.content.grammar.filter { it.level == 1 }.all { s.store.stars(it.id) > 0 }
        val pet = s.pet.state
        fun t(id: Int) = a.getString(id)
        return listOf(
            A("first", "🌱", t(R.string.ach_first), t(R.string.ach_first_h), hist.isNotEmpty()),
            A("streak3", "🔥", t(R.string.ach_streak3), t(R.string.ach_streak3_h), streak >= 3),
            A("streak7", "🏅", t(R.string.ach_streak7), t(R.string.ach_streak7_h), streak >= 7),
            A("streak30", "🏆", t(R.string.ach_streak30), t(R.string.ach_streak30_h), streak >= 30),
            A("words50", "📗", t(R.string.ach_w50), t(R.string.ach_w50_h), known >= 50),
            A("words200", "📚", t(R.string.ach_w200), t(R.string.ach_w200_h), known >= 200),
            A("words500", "🧠", t(R.string.ach_w500), t(R.string.ach_w500_h), known >= 500),
            A("grammar1", "⭐", t(R.string.ach_g1), t(R.string.ach_g1_h), stars >= 1),
            A("level1", "🪜", t(R.string.ach_l1), t(R.string.ach_l1_h), l1),
            A("mock80", "🎯", t(R.string.ach_mock), t(R.string.ach_mock_h), hist.any { it.kind == "MOCK" && it.percent >= 80 && it.total >= 10 }),
            A("combo10", "⚡", t(R.string.ach_combo), t(R.string.ach_combo_h), hist.any { it.detail.optInt("combo") >= 10 }),
            A("pet3", "🐣", t(R.string.ach_pet3), t(R.string.ach_pet3_h), pet.stage >= 3),
            A("pet5", "🦄", t(R.string.ach_pet5), t(R.string.ach_pet5_h), pet.stage >= 5),
            A("pet10", "👑", t(R.string.ach_pet10), t(R.string.ach_pet10_h), pet.stage >= 10),
            A("decor", "🏡", t(R.string.ach_decor), t(R.string.ach_decor_h), pet.owned.size >= 5),
        )
    }
}

private val android.content.Context.services get() = (applicationContext as com.yourbrand.englishlearn.EnglishApp).services
