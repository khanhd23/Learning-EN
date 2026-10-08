package com.yourbrand.englishlearn.screens

import android.text.InputFilter
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.yourbrand.englishlearn.core.Settings
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.core.AppLocale
import com.yourbrand.englishlearn.learning.Session
import com.yourbrand.englishlearn.learning.SessionKind
import com.yourbrand.englishlearn.pet.Mood
import com.yourbrand.englishlearn.pet.PetView
import com.yourbrand.englishlearn.pet.Species
import com.yourbrand.englishlearn.ui.*

/** S02 — 4 short skippable steps, then a 5-question welcome session (first question within seconds). */
class OnboardingScreen(activity: MainActivity) : Screen(activity) {
    override val petMode = PetMode.HIDDEN
    private var step = 0
    private val goals = linkedSetOf<String>()
    private var level = 1
    private var minutes = 15
    private var species = "cat"
    private var name = ""
    private var language = "en"
    private lateinit var content: LinearLayout
    private lateinit var dots: LinearLayout
    private lateinit var next: TextView

    override fun onCreateView(parent: ViewGroup): View {
        val c = ctx
        language = services.settings.contentLocale
        if (name.isEmpty()) name = c.getString(Species.defaultNameRes(species))
        val root = Kit.vbox(c).apply { layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT) }
        val top = Kit.hbox(c) { setPadding(c.dpi(20), c.dpi(12), c.dpi(8), 0) }
        dots = Kit.hbox(c) { layoutParams = lp(0, WRAP_CONTENT, 1f) }
        top.addView(dots)
        top.addView(Kit.text(c, str(R.string.skip), R.style.Text_BodyStrong, c.col(R.color.muted)).apply {
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, c.dpi(48)); gravity = Gravity.CENTER
            setPadding(c.dpi(12), 0, c.dpi(12), 0)
            onTap { if (step < 3) { step++; render() } else finish() }
        })
        root.addView(top)
        val scroll = ScrollView(c).apply { layoutParams = lp(MATCH_PARENT, 0, 1f) }
        content = Kit.vbox(c) { setPadding(c.dpi(20), c.dpi(8), c.dpi(20), c.dpi(20)) }
        scroll.addView(content)
        root.addView(scroll)
        next = Kit.primary(c, str(R.string.next), 0) { if (step < 3) { step++; render() } else finish() }.apply {
            layoutParams = lp(h = c.dpi(52)).apply { setMargins(c.dpi(20), c.dpi(8), c.dpi(20), c.dpi(20)) }
        }
        root.addView(next)
        return root
    }

    override fun onShown(firstTime: Boolean) { render() }

    override fun onBack(): Boolean { if (step > 0) { step--; render(); return true }; return false }

    private fun render() {
        val c = ctx
        dots.removeAllViews()
        repeat(4) { i -> dots.addView(View(c).apply { background = c.rounded(c.col(if (i <= step) R.color.primary else R.color.outline), 100f); layoutParams = LinearLayout.LayoutParams(c.dpi(if (i == step) 24 else 8), c.dpi(8)).apply { marginEnd = c.dpi(6) } }) }
        content.removeAllViews()
        next.text = str(if (step < 3) R.string.next else R.string.start_learning)
        when (step) {
            0 -> stepLanguage()
            1 -> stepGoals()
            2 -> stepLevel()
            else -> stepPet()
        }
        if (!c.reduceMotion) { content.translationX = c.dp(24f); content.alpha = 0f; content.animate().translationX(0f).alpha(1f).setDuration(250).start() }
    }

    private fun title(t: Int, sub: Int) {
        content.addView(Kit.text(ctx, str(t), R.style.Text_Display).margins(ctx, top = 12))
        content.addView(Kit.text(ctx, str(sub), R.style.Text_Body, ctx.col(R.color.muted)).margins(ctx, top = 4, bottom = 12))
    }

    private fun stepLanguage() {
        val c = ctx
        title(R.string.ob_language_title, R.string.ob_language_sub)
        val locales = services.contentRepo.selectableLocales()
        if (language !in locales) language = locales.firstOrNull() ?: "en"
        locales.forEach { locale ->
            val card = Kit.card(c, 14, 12) {
                isClickable = true
                addView(Kit.text(c, str(R.string.i_speak, localeLabel(locale)), R.style.Text_BodyStrong))
            }
            fun paint() { card.background = c.rounded(c.col(if (locale == language) R.color.primary_container else R.color.surface), 16f, c.col(if (locale == language) R.color.primary else R.color.outline), if (locale == language) 2f else 1f) }
            paint()
            card.onTap {
                language = locale
                services.settings.contentLocale = locale
                services.settings.contentLocaleChosen = true
                paint()
                // Paint the selected card first. AppCompat may recreate the activity while
                // applying the per-app locale; posting keeps the selection visible in the
                // current frame instead of flashing the old screen before the restart.
                card.post { if (language == locale) AppLocale.apply(locale) }
            }
            content.addView(card.margins(c, top = 10))
        }
    }

    private fun localeLabel(locale: String): String = when (locale) {
        "en" -> str(R.string.locale_en)
        "vi" -> str(R.string.locale_vi)
        "es" -> str(R.string.locale_es)
        "pt-BR" -> str(R.string.locale_pt_br)
        "id" -> str(R.string.locale_id)
        "hi" -> str(R.string.locale_hi)
        "ar" -> str(R.string.locale_ar)
        "ja" -> str(R.string.locale_ja)
        "ko" -> str(R.string.locale_ko)
        "th" -> str(R.string.locale_th)
        "fr" -> str(R.string.locale_fr)
        else -> locale
    }

    private fun stepGoals() {
        val c = ctx
        title(R.string.ob_goal_title, R.string.ob_goal_sub)
        val options = listOf("toeic" to ("📝" to R.string.goal_toeic), "ielts" to ("🎓" to R.string.goal_ielts), "talk" to ("💬" to R.string.goal_talk),
            "work" to ("💼" to R.string.goal_work), "travel" to ("✈️" to R.string.goal_travel), "basics" to ("🌱" to R.string.goal_basics))
        var row: LinearLayout? = null
        options.forEachIndexed { i, (id, pair) ->
            if (i % 2 == 0) { row = Kit.hbox(c).margins(c, top = 10); content.addView(row) }
            val card = Kit.card(c, 14, 0) {
                layoutParams = lp(0, WRAP_CONTENT, 1f).apply { if (i % 2 == 1) marginStart = c.dpi(10) }
                minimumHeight = c.dpi(96)
                isClickable = true
                addView(Kit.text(c, pair.first, sizeSp = 26f))
                addView(Kit.text(c, str(pair.second), R.style.Text_BodyStrong).margins(c, top = 6))
            }
            fun paint() { card.background = c.rounded(c.col(if (id in goals) R.color.primary_container else R.color.surface), 16f, c.col(if (id in goals) R.color.primary else R.color.outline), if (id in goals) 2f else 1f) }
            paint()
            card.onTap { if (!goals.add(id)) goals.remove(id); paint(); if (id in goals) card.pop(0.96f) }
            row!!.addView(card)
        }
    }

    private fun stepLevel() {
        val c = ctx
        title(R.string.ob_level_title, R.string.ob_level_sub)
        val levels = listOf(R.string.lvl_beginner, R.string.lvl_basic, R.string.lvl_inter, R.string.lvl_upper)
        val box = Kit.vbox(c)
        val views = levels.mapIndexed { i, l ->
            Kit.card(c, 16, 10) {
                isClickable = true
                addView(Kit.text(c, str(l), R.style.Text_BodyStrong))
                addView(Kit.text(c, str(listOf(R.string.lvl_beginner_d, R.string.lvl_basic_d, R.string.lvl_inter_d, R.string.lvl_upper_d)[i]), R.style.Text_Caption))
            }.also { box.addView(it) }
        }
        fun paint() { views.forEachIndexed { i, v -> v.background = c.rounded(c.col(if (i == level) R.color.primary_container else R.color.surface), 16f, c.col(if (i == level) R.color.primary else R.color.outline), if (i == level) 2f else 1f) } }
        views.forEachIndexed { i, v -> v.onTap { level = i; paint() } }
        paint()
        content.addView(box)
        content.addView(Kit.text(c, str(R.string.placement_later), R.style.Text_Caption).margins(c, top = 8))
        content.addView(Kit.text(c, str(R.string.ob_time), R.style.Text_BodyStrong).margins(c, top = 20))
        // Flow row, not a baseline-aligned hbox: the bold selected chip shifted the baseline and
        // the row clipped the chips' top and bottom edges.
        content.addView(Kit.flow(c).also { r ->
            val chips = ArrayList<TextView>()
            Settings.DAILY_MINUTES.forEach { m -> chips += Kit.chip(c, str(R.string.minutes_n, m), m == minutes) { minutes = m; chips.forEachIndexed { j, v -> Kit.styleChip(v, Settings.DAILY_MINUTES[j] == m) } }.also { r.addView(it) } }
        }.margins(c, top = 8))
    }

    private fun stepPet() {
        val c = ctx
        title(R.string.ob_pet_title, R.string.ob_pet_sub)
        val row = Kit.hbox(c) { gravity = Gravity.TOP }
        val cards = Species.all.mapIndexed { i, sp ->
            Kit.card(c, 8, 0) {
                gravity = Gravity.CENTER_HORIZONTAL
                layoutParams = lp(0, WRAP_CONTENT, 1f).apply { if (i > 0) marginStart = c.dpi(10) }
                isClickable = true
                addView(PetView(c).apply { species = sp; stage = 1; mood = Mood.HI; layoutParams = LinearLayout.LayoutParams(c.dpi(92), c.dpi(92)) })
                addView(Kit.text(c, c.getString(Species.nameRes(sp)), R.style.Text_BodyStrong).apply { gravity = Gravity.CENTER })
            }.also { row.addView(it) }
        }
        val nameInput = EditText(c).apply {
            setText(name); setSingleLine(); textSize = 18f
            filters = arrayOf(InputFilter.LengthFilter(14))
            background = c.rounded(c.col(R.color.surface), 14f, c.col(R.color.outline), 1.5f)
            setPadding(c.dpi(16), c.dpi(12), c.dpi(16), c.dpi(12))
            layoutParams = lp().apply { topMargin = c.dpi(8) }
            addTextChangedListener(object : android.text.TextWatcher {
                override fun afterTextChanged(s: android.text.Editable?) { name = s.toString().trim().ifEmpty { c.getString(Species.defaultNameRes(species)) } }
                override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, d: Int) {}
                override fun onTextChanged(s: CharSequence?, a: Int, b: Int, d: Int) {}
            })
        }
        fun paint() { cards.forEachIndexed { i, v -> v.background = c.rounded(c.col(if (Species.all[i] == species) R.color.primary_container else R.color.surface), 16f, c.col(if (Species.all[i] == species) R.color.primary else R.color.outline), if (Species.all[i] == species) 2f else 1f) } }
        cards.forEachIndexed { i, v -> v.onTap {
            val old = c.getString(Species.defaultNameRes(species))
            species = Species.all[i]
            if (name == old) { name = c.getString(Species.defaultNameRes(species)); nameInput.setText(name) }
            paint(); (v.getChildAt(0) as PetView).react(Mood.HI)
        } }
        paint()
        content.addView(row)
        content.addView(Kit.text(c, str(R.string.ob_pet_name), R.style.Text_BodyStrong).margins(c, top = 20))
        content.addView(nameInput)
        val sugg = Kit.flow(c).margins(c, top = 8)
        c.resources.getStringArray(R.array.pet_name_suggestions).forEach { n -> sugg.addView(Kit.chip(c, n) { nameInput.setText(n) }) }
        content.addView(sugg)
        content.addView(Kit.card(c, 14, 14, c.col(R.color.accent_container), null) {
            addView(Kit.text(c, str(R.string.ob_pet_explain), R.style.Text_Body).apply { textSize = 14f })
        })
    }

    private fun finish() {
        val s = services
        s.settings.contentLocale = language
        s.settings.contentLocaleChosen = true
        s.settings.goals = goals.ifEmpty { setOf("talk") }
        s.settings.level = level
        s.settings.dailyMinutes = minutes
        s.settings.onboarded = true
        s.pet.state.species = species
        s.pet.state.name = name
        s.pet.state.created = true
        s.savePet()
        activity.selectTab(Tab.TODAY)
        // Welcome session: 5 easy questions right away.
        activity.startSession(s.builder().welcome(str(R.string.welcome_session)))
    }
}

/** Placement test (offered in the first week, never forced): 3 items per level, sets the learner level. */
class PlacementScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val barTitle: String get() = str(R.string.placement_title)
    override fun build(body: LinearLayout) {
        val c = ctx
        body.addView(Kit.text(c, "🧭", sizeSp = 48f).apply { gravity = Gravity.CENTER }.margins(c, top = 20))
        body.addView(Kit.text(c, str(R.string.placement_intro), R.style.Text_Body).apply { gravity = Gravity.CENTER }.margins(c, top = 8))
        body.addView(Kit.primary(c, str(R.string.start), 24) {
            val b = services.builder()
            val f = com.yourbrand.englishlearn.learning.ExerciseFactory(services.content)
            val ex = (1..5).flatMap { lvl -> services.content.questions.filter { it.level == lvl && (it.type == "E02" || it.type == "E01") && it.fmt != "school" }.shuffled().take(3).map { f.question(it) } }
            activity.navigator.pop()
            activity.startSession(Session(SessionKind.PLACEMENT, str(R.string.placement_title), ex.toMutableList()))
            b.hashCode()
        })
    }
}
