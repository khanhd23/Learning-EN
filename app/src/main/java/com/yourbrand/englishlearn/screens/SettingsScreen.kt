package com.yourbrand.englishlearn.screens

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.yourbrand.englishlearn.BuildConfig
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.content.DbWordHit
import com.yourbrand.englishlearn.core.AppLocale
import com.yourbrand.englishlearn.notify.Reminders
import com.yourbrand.englishlearn.pet.PetState
import com.yourbrand.englishlearn.ui.*
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors

/** S21 — Cài đặt. */
class SettingsScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val petMode = PetMode.HIDDEN
    override val barTitle: String get() = str(R.string.settings)

    override fun build(body: LinearLayout) {
        val c = ctx
        val s = services.settings

        group(body, R.string.set_learning) { g ->
            val locales = services.contentRepo.shippableLocales()
            if (locales.size > 1) {
                val labels = locales.map { localeLabel(it) }
                choice(g, R.string.set_language, labels, locales.indexOf(services.contentRepo.effectiveLocale(s.contentLocale)).coerceAtLeast(0)) {
                    s.contentLocale = locales[it]
                    s.contentLocaleChosen = true
                    AppLocale.apply(s.contentLocale)
                }
            }
            choice(g, R.string.set_goal, listOf(5, 10, 15).map { str(R.string.minutes_n, it) }, listOf(5, 10, 15).indexOf(s.dailyMinutes)) { s.dailyMinutes = listOf(5, 10, 15)[it] }
            choice(g, R.string.set_new_words, listOf("3", "5", "8"), listOf(3, 5, 8).indexOf(s.newWordsPerSession)) { s.newWordsPerSession = listOf(3, 5, 8)[it] }
            choice(g, R.string.set_level, listOf(R.string.lvl_beginner, R.string.lvl_basic, R.string.lvl_inter, R.string.lvl_upper).map { str(it) }, s.level) { s.level = it }
            toggle(g, R.string.set_translation, s.showTranslation) { s.showTranslation = it }
            toggle(g, R.string.set_slow_tts, s.slowTts) { s.slowTts = it }
            toggle(g, R.string.set_haptics, s.haptics) { s.haptics = it }
            toggle(g, R.string.set_sounds, s.sounds) { s.sounds = it }
        }

        group(body, R.string.set_pet) { g ->
            toggle(g, R.string.set_show_pet, s.petVisible) { s.petVisible = it; activity.updateChrome() }
            toggle(g, R.string.set_bubbles, s.petBubbles) { s.petBubbles = it }
            choice(g, R.string.set_pet_size, listOf("S", "M", "L"), s.petSize) { s.petSize = it; activity.petView.applySize() }
            action(g, R.string.set_reset_pos) { activity.petView.resetPosition(); activity.toast(str(R.string.done)) }
            action(g, R.string.set_open_pet) { activity.openPetHome() }
        }

        group(body, R.string.set_notifications) { g ->
            toggle(g, R.string.set_reminder, s.reminderOn) { on -> if (on) Reminders.enable(activity, s.reminderHour) else Reminders.disable(c); refresh() }
            if (s.reminderOn) choice(g, R.string.set_reminder_time, listOf(8, 12, 18, 20, 21).map { "%02d:00".format(it) }, listOf(8, 12, 18, 20, 21).indexOf(s.reminderHour)) {
                s.reminderHour = listOf(8, 12, 18, 20, 21)[it]; Reminders.schedule(c, s.reminderHour)
            }
            g.addView(Kit.text(c, str(R.string.quiet_hours), R.style.Text_Caption).apply { setPadding(c.dpi(12), 0, c.dpi(12), c.dpi(8)) })
        }

        group(body, R.string.set_appearance) { g ->
            choice(g, R.string.set_theme, listOf(R.string.theme_system, R.string.theme_light, R.string.theme_dark).map { str(it) }, s.theme) {
                s.theme = it; AppCompatDelegate.setDefaultNightMode(s.nightMode)
            }
            choice(g, R.string.set_text_size, listOf("100%", "125%", "150%"), s.textSize) { s.textSize = it }
            toggle(g, R.string.set_reduce_motion, s.reduceMotion) { s.reduceMotion = it }
        }

        group(body, R.string.set_privacy) { g ->
            if (services.ads.privacyOptionsRequired) action(g, R.string.set_ad_privacy) { services.ads.showPrivacyOptions(activity) }
            action(g, R.string.privacy_policy) {
                if (BuildConfig.PRIVACY_URL.isNotBlank()) runCatching { activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(BuildConfig.PRIVACY_URL))) }
                else activity.open(TextScreen(activity, str(R.string.privacy_policy), str(R.string.privacy_text)))
            }
        }

        group(body, R.string.set_data) { g ->
            g.addView(Kit.text(c, str(R.string.backup_info), R.style.Text_Caption).apply { setPadding(c.dpi(12), c.dpi(8), c.dpi(12), c.dpi(8)) })
            action(g, R.string.reset_progress, destructive = true) {
                Dialogs.confirm(c, str(R.string.reset_title), str(R.string.reset_msg), str(R.string.reset_confirm), destructive = true) {
                    services.store.reset()
                    val keep = services.pet.state
                    val fresh = PetState(species = keep.species, name = keep.name, created = true)
                    services.pet.state.apply {
                        stage = fresh.stage; stageXp = 0; totalXp = 0; recoverableXp = 0; coins = 0; owned.clear(); equipped.clear(); lastStudyAt = 0; pendingStageUp = 0
                    }
                    services.savePet()
                    activity.toast(str(R.string.reset_done))
                    activity.selectTab(Tab.TODAY)
                }
            }
        }

        group(body, R.string.set_about) { g ->
            action(g, R.string.about_disclaimer) { activity.open(TextScreen(activity, str(R.string.about_disclaimer), str(R.string.disclaimer_full))) }
            action(g, R.string.licenses) {
                val contentLicenses = c.assets.open("content/LICENSES.md").bufferedReader(Charsets.UTF_8).use { it.readText() }
                val imageNotice = runCatching { c.assets.open("content/third_party_notices.txt").bufferedReader(Charsets.UTF_8).use { it.readText() } }.getOrDefault("")
                activity.open(TextScreen(activity, str(R.string.licenses), str(R.string.licenses_text) + "\n\n" + contentLicenses + if (imageNotice.isBlank()) "" else "\n\n" + imageNotice))
            }
            action(g, R.string.contact) {
                runCatching { activity.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${BuildConfig.SUPPORT_EMAIL}?subject=" + Uri.encode(str(R.string.app_short_name) + " feedback")))) }
            }
            g.addView(Kit.text(c, str(R.string.version, BuildConfig.VERSION_NAME), R.style.Text_Caption).apply { setPadding(c.dpi(12), c.dpi(8), c.dpi(12), c.dpi(12)) })
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

    private fun group(body: LinearLayout, title: Int, block: (LinearLayout) -> Unit) {
        body.addView(Kit.text(ctx, str(title), R.style.Text_BodyStrong, ctx.col(R.color.primary)).margins(ctx, top = 20, bottom = 6))
        val card = Kit.card(ctx, 4, 0)
        block(card)
        body.addView(card)
    }

    private fun toggle(g: LinearLayout, label: Int, value: Boolean, onChange: (Boolean) -> Unit) {
        val c = ctx
        val sw = SwitchCompat(c).apply { isChecked = value; isClickable = false; isFocusable = false; importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO }
        val row = Kit.row(c, null, str(label), null, sw)
        row.isClickable = true
        row.background = c.rounded(0, 12f, ripple = true)
        row.contentDescription = str(label)
        row.setOnClickListener { it.haptic(); sw.isChecked = !sw.isChecked; onChange(sw.isChecked) }
        g.addView(row)
    }

    private fun choice(g: LinearLayout, label: Int, options: List<String>, selected: Int, onPick: (Int) -> Unit) {
        val c = ctx
        val box = Kit.vbox(c) { setPadding(c.dpi(12), c.dpi(10), c.dpi(12), c.dpi(4)) }
        box.addView(Kit.text(c, str(label), R.style.Text_BodyStrong))
        val flow = Kit.flow(c).margins(c, top = 8)
        val chips = ArrayList<TextView>()
        options.forEachIndexed { i, o -> chips += Kit.chip(c, o, i == selected) { onPick(i); chips.forEachIndexed { j, v -> Kit.styleChip(v, j == i) } }.also { flow.addView(it) } }
        box.addView(flow)
        g.addView(box)
    }

    private fun action(g: LinearLayout, label: Int, destructive: Boolean = false, onClick: () -> Unit) {
        val c = ctx
        g.addView(Kit.row(c, null, str(label), null, Kit.chevron(c), onClick).apply {
            if (destructive) ((getChildAt(0) as LinearLayout).getChildAt(0) as TextView).setTextColor(c.col(R.color.error))
        })
    }
}

/** Plain text page (disclaimer, licenses, offline privacy policy). */
class TextScreen(activity: MainActivity, private val title: String, private val text: String) : ScrollScreen(activity) {
    override val barTitle: String get() = title
    override fun build(body: LinearLayout) { body.addView(Kit.text(ctx, text, R.style.Text_Body).margins(ctx, top = 8)) }
}

/** S22 — search words, grammar points and topics. */
class SearchScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val petMode = PetMode.HIDDEN
    private var query = ""
    private lateinit var input: android.widget.EditText
    private lateinit var results: LinearLayout
    private val mainHandler = Handler(Looper.getMainLooper())
    private val searchExecutor = Executors.newSingleThreadExecutor { task -> Thread(task, "content-search").apply { isDaemon = true } }
    private var searchVersion = 0L
    private var pendingSearch: Runnable? = null

    override fun buildHeader() {
        val c = ctx
        header.addView(android.widget.ImageView(c).apply {
            setImageResource(R.drawable.ic_arrow_back); tintRes(R.color.on_surface); setBackgroundResource(R.drawable.ripple_circle)
            val p = c.dpi(12); setPadding(p, p, p, p); contentDescription = str(R.string.back)
            layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48)); setOnClickListener { activity.navigator.pop() }
        })
        input = android.widget.EditText(c).apply {
            hint = str(R.string.search_hint)
            setSingleLine()
            textSize = 17f
            background = c.rounded(c.col(R.color.surface), 14f, c.col(R.color.outline))
            setPadding(c.dpi(14), c.dpi(10), c.dpi(14), c.dpi(10))
            layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginEnd = c.dpi(8) }
            imeOptions = android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
            addTextChangedListener(object : android.text.TextWatcher {
                override fun afterTextChanged(e: android.text.Editable?) { query = e?.toString().orEmpty(); scheduleSearch() }
                override fun beforeTextChanged(s: CharSequence?, a: Int, b: Int, d: Int) {}
                override fun onTextChanged(s: CharSequence?, a: Int, b: Int, d: Int) {}
            })
        }
        header.addView(input)
        input.post { input.requestFocus(); (c.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as android.view.inputmethod.InputMethodManager).showSoftInput(input, 0) }
    }

    override fun build(body: LinearLayout) {
        results = Kit.vbox(ctx)
        body.addView(results)
        renderEmptyQuery()
    }

    private fun norm(s: String) = java.text.Normalizer.normalize(s.lowercase(), java.text.Normalizer.Form.NFD).replace(Regex("\\p{M}+"), "").replace('đ', 'd')

    private fun remember(q: String) { if (q.isNotBlank()) services.settings.recentSearches = (listOf(q) + services.settings.recentSearches.filter { it != q }) }

    private fun scheduleSearch() {
        if (!::results.isInitialized) return
        searchVersion++
        pendingSearch?.let(mainHandler::removeCallbacks)
        val version = searchVersion
        val rawQuery = query
        if (rawQuery.trim().isEmpty()) { renderEmptyQuery(); return }
        val locale = services.settings.contentLocale
        pendingSearch = Runnable {
            searchExecutor.submit {
                val words = services.contentDb.searchWords(locale, rawQuery)
                mainHandler.post { if (version == searchVersion && query == rawQuery) renderResults(rawQuery, words) }
            }
        }
        mainHandler.postDelayed(pendingSearch!!, 120L)
    }

    override fun onDestroy() {
        pendingSearch?.let(mainHandler::removeCallbacks)
        searchExecutor.shutdownNow()
        super.onDestroy()
    }

    private fun renderEmptyQuery() {
        val c = ctx
        if (!::results.isInitialized) return
        results.removeAllViews()
        val content = services.content
        val recent = services.settings.recentSearches
        if (recent.isNotEmpty()) {
            results.addView(Kit.section(c, str(R.string.recent_searches), 8))
            val f = Kit.flow(c); recent.forEach { r -> f.addView(Kit.chip(c, r) { input.setText(r); input.setSelection(r.length) }) }
            results.addView(f)
        }
        results.addView(Kit.section(c, str(R.string.suggest_topics)))
        val f = Kit.flow(c)
        content.topics.take(12).forEach { t -> f.addView(Kit.chip(c, t.icon + " " + t.name, false, Hues.color(c, t.hue)) { activity.open(WordListScreen(activity, topicId = t.id)) }) }
        results.addView(f)
    }

    private fun renderResults(rawQuery: String, hits: List<DbWordHit>) {
        val c = ctx
        if (!::results.isInitialized) return
        results.removeAllViews()
        val content = services.content
        val words = hits.mapNotNull { hit -> content.wordById[hit.id] }
        val q = norm(rawQuery.trim())
        val gps = content.grammar.filter { norm(it.title).contains(q) || norm(it.formula).contains(q) }
        val topics = content.topics.filter { norm(it.name).contains(q) }
        if (words.isEmpty() && gps.isEmpty() && topics.isEmpty()) { results.addView(Kit.empty(c, "🔍", str(R.string.no_results), null, null)); return }
        if (words.isNotEmpty()) {
            results.addView(Kit.section(c, str(R.string.words) + " (${words.size})", 8))
            val card = Kit.card(c, 4, 4)
            words.forEachIndexed { i, w ->
                if (i > 0) card.addView(Kit.divider(c))
                card.addView(Kit.row(c, null, "${w.lemma}  ·  ${w.pos}", w.senses.joinToString("; ") { it.gloss }, Kit.chevron(c)) { remember(query.trim()); activity.open(WordDetailScreen(activity, w.id)) })
            }
            results.addView(card)
        }
        if (gps.isNotEmpty()) {
            results.addView(Kit.section(c, str(R.string.grammar_points)))
            val card = Kit.card(c, 4, 4)
            gps.forEach { g -> card.addView(Kit.row(c, null, g.title, g.formula, Kit.chevron(c)) { remember(query.trim()); activity.open(GrammarLessonScreen(activity, g.id)) }) }
            results.addView(card)
        }
        if (topics.isNotEmpty()) {
            results.addView(Kit.section(c, str(R.string.topics)))
            val f = Kit.flow(c)
            topics.forEach { t -> f.addView(Kit.chip(c, t.icon + " " + t.name) { activity.open(WordListScreen(activity, topicId = t.id)) }) }
            results.addView(f)
        }
    }
}
