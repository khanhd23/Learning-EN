package com.yourbrand.englishlearn.screens

import android.content.Intent
import android.net.Uri
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.yourbrand.englishlearn.core.Settings
import com.yourbrand.englishlearn.BuildConfig
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.content.DbSearchResult
import com.yourbrand.englishlearn.content.DbTopicHit
import com.yourbrand.englishlearn.core.AppLocale
import com.yourbrand.englishlearn.notify.Reminders
import com.yourbrand.englishlearn.pet.PetState
import com.yourbrand.englishlearn.ui.*
import android.os.Handler
import android.os.Looper
import java.util.concurrent.Executors
import java.util.concurrent.Future

/** S21 — Cài đặt. */
class SettingsScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val petMode = PetMode.HIDDEN
    override val barTitle: String get() = str(R.string.settings)
    private val localeBindings = ArrayList<() -> Unit>()
    private var localeIndexRetry = 0

    override fun onShown(firstTime: Boolean) {
        super.onShown(firstTime)
        // UMP consent access may initialize a provider lazily and can take seconds on a weak
        // device. It is optional settings chrome, so discover it after the first frame.
        if (firstTime) view.postDelayed({
            if (hasView && activity.navigator.current === this && services.ads.privacyOptionsRequired) refresh()
        }, 1200L)
        if (firstTime) retryLocaleIndex()
    }

    override fun build(body: LinearLayout) {
        localeBindings.clear()
        val c = ctx
        val s = services.settings

        group(body, R.string.set_learning) { g ->
            val locales = services.contentRepo.selectableLocalesIfReady().orEmpty()
            if (locales.size > 1) {
                languageChoice(g, locales)
            }
            choice(g, R.string.set_goal, { Settings.DAILY_MINUTES.map { str(R.string.minutes_n, it) } }, Settings.DAILY_MINUTES.indexOf(s.dailyMinutes).coerceAtLeast(0)) { s.dailyMinutes = Settings.DAILY_MINUTES[it] }
            choice(g, R.string.set_new_words, { listOf("3", "5", "8") }, listOf(3, 5, 8).indexOf(s.newWordsPerSession)) { s.newWordsPerSession = listOf(3, 5, 8)[it] }
            choice(g, R.string.set_level, { listOf(R.string.lvl_beginner, R.string.lvl_basic, R.string.lvl_inter, R.string.lvl_upper).map { str(it) } }, s.level) { s.level = it }
            toggle(g, R.string.set_translation, s.showTranslation) { s.showTranslation = it }
            toggle(g, R.string.set_slow_tts, s.slowTts) { s.slowTts = it }
            toggle(g, R.string.set_haptics, s.haptics) { s.haptics = it }
            toggle(g, R.string.set_sounds, s.sounds) { s.sounds = it }
        }

        group(body, R.string.set_pet) { g ->
            toggle(g, R.string.set_show_pet, s.petVisible) { s.petVisible = it; activity.updateChrome() }
            toggle(g, R.string.set_bubbles, s.petBubbles) { s.petBubbles = it }
            choice(g, R.string.set_pet_size, { listOf("S", "M", "L") }, s.petSize) { s.petSize = it; activity.petView.applySize() }
            action(g, R.string.set_reset_pos) { activity.petView.resetPosition(); activity.toast(str(R.string.done)) }
            action(g, R.string.set_open_pet) { activity.openPetHome() }
        }

        group(body, R.string.set_notifications) { g ->
            toggle(g, R.string.set_reminder, s.reminderOn) { on -> if (on) Reminders.enable(activity, s.reminderHour) else Reminders.disable(c); refresh() }
            if (s.reminderOn) choice(g, R.string.set_reminder_time, { listOf(8, 12, 18, 20, 21).map { "%02d:00".format(it) } }, listOf(8, 12, 18, 20, 21).indexOf(s.reminderHour)) {
                s.reminderHour = listOf(8, 12, 18, 20, 21)[it]; Reminders.schedule(c, s.reminderHour)
            }
            val quietHours = Kit.text(c, str(R.string.quiet_hours), R.style.Text_Caption).apply { setPadding(c.dpi(12), 0, c.dpi(12), c.dpi(8)) }
            bindText(quietHours) { str(R.string.quiet_hours) }
            g.addView(quietHours)
        }

        group(body, R.string.set_appearance) { g ->
            choice(g, R.string.set_theme, { listOf(R.string.theme_system, R.string.theme_light, R.string.theme_dark).map { str(it) } }, s.theme) {
                s.theme = it; AppCompatDelegate.setDefaultNightMode(s.nightMode)
            }
            choice(g, R.string.set_text_size, { listOf("100%", "125%", "150%") }, s.textSize) { s.textSize = it }
            toggle(g, R.string.set_reduce_motion, s.reduceMotion) { s.reduceMotion = it }
        }

        group(body, R.string.set_privacy) { g ->
            action(g, R.string.privacy_policy) {
                activity.open(PrivacyPolicyScreen(activity, BuildConfig.PRIVACY_URL))
            }
        }

        group(body, R.string.set_data) { g ->
            val backupInfo = Kit.text(c, str(R.string.backup_info), R.style.Text_Caption).apply { setPadding(c.dpi(12), c.dpi(8), c.dpi(12), c.dpi(8)) }
            bindText(backupInfo) { str(R.string.backup_info) }
            g.addView(backupInfo)
            action(g, R.string.backup_title) { activity.startBackup() }
            action(g, R.string.restore_title) { activity.startRestore() }
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
            action(g, R.string.about_disclaimer) { activity.open(AboutScreen(activity)) }
            action(g, R.string.licenses) {
                openLicenses()
            }
            action(g, R.string.contact) {
                runCatching { activity.startActivity(Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:${BuildConfig.SUPPORT_EMAIL}?subject=" + Uri.encode(str(R.string.app_short_name) + " feedback")))) }
            }
            val version = Kit.text(c, str(R.string.version, BuildConfig.VERSION_NAME), R.style.Text_Caption).apply { setPadding(c.dpi(12), c.dpi(8), c.dpi(12), c.dpi(12)) }
            bindText(version) { str(R.string.version, BuildConfig.VERSION_NAME) }
            g.addView(version)
        }
    }

    private fun retryLocaleIndex() {
        if (!hasView || activity.navigator.current !== this || localeIndexRetry++ >= 30) return
        if (services.contentRepo.selectableLocalesIfReady().isNullOrEmpty()) {
            view.postDelayed({ retryLocaleIndex() }, 100L)
        } else refresh()
    }

    override fun onLocaleChanged(): Boolean {
        if (!hasView) return false
        val started = System.nanoTime()
        root.suppressLayout(true)
        val afterSuppress = System.nanoTime()
        var slowestNs = 0L
        try {
            refreshHeaderLocale()
            val afterHeader = System.nanoTime()
            localeBindings.forEach { binding ->
                val bindingStarted = System.nanoTime()
                binding()
                slowestNs = maxOf(slowestNs, System.nanoTime() - bindingStarted)
            }
            val afterBindings = System.nanoTime()
            Perf.log("SettingsScreen locale detail suppress=${Perf.ms(started, afterSuppress)} header=${Perf.ms(afterSuppress, afterHeader)} bindings=${Perf.ms(afterHeader, afterBindings)} slowest=${Perf.ms(0L, slowestNs)}")
        } finally {
            root.suppressLayout(false)
        }
        Perf.log("SettingsScreen locale bindings=${localeBindings.size} total=${Perf.ms(started, System.nanoTime())}")
        return true
    }

    private fun bindText(view: TextView, value: () -> CharSequence) {
        localeBindings += { view.text = value() }
    }

    /** Opens the complete offline license catalogue; used by the release smoke test too. */
    fun openLicenses() {
        val heading = str(R.string.licenses)
        val prefix = str(R.string.licenses_text)
        activity.openAsync {
            val c = ctx
            val contentLicenses = c.assets.open("content/LICENSES.md").bufferedReader(Charsets.UTF_8).use { it.readText() }
            val imageNotice = runCatching { c.assets.open("content/third_party_notices.txt").bufferedReader(Charsets.UTF_8).use { it.readText() } }.getOrDefault("")
            val apache = c.assets.open("licenses/apache-2.0.txt").bufferedReader(Charsets.UTF_8).use { it.readText() }
            val ccBySa = c.assets.open("licenses/cc-by-sa-4.0.txt").bufferedReader(Charsets.UTF_8).use { it.readText() }
            val dependencyLicenses = runCatching { c.assets.open("licenses/dependencies.txt").bufferedReader(Charsets.UTF_8).use { it.readText() } }.getOrDefault("")
            val text = prefix + "\n\n" + contentLicenses +
                if (imageNotice.isBlank()) "" else "\n\n$imageNotice" +
                "\n\nApache License 2.0 (AndroidX and Noto Emoji)\n\n$apache" +
                "\n\nCC BY-SA 4.0 summary\n\n$ccBySa" +
                if (dependencyLicenses.isBlank()) "" else "\n\n$dependencyLicenses"
            TextScreen(activity, heading, text)
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

    private fun localeFlag(locale: String): String = when (locale) {
        "en" -> "🇬🇧"
        "vi" -> "🇻🇳"
        "es" -> "🇪🇸"
        "pt-BR" -> "🇧🇷"
        "id" -> "🇮🇩"
        "hi" -> "🇮🇳"
        "ar" -> "🇸🇦"
        "ja" -> "🇯🇵"
        "ko" -> "🇰🇷"
        "th" -> "🇹🇭"
        "fr" -> "🇫🇷"
        else -> "🌐"
    }

    private fun languageChoice(g: LinearLayout, locales: List<String>) {
        val c = ctx
        val settings = services.settings
        val current = services.contentRepo.effectiveLocale(settings.contentLocale)
        val flag = Kit.emojiTile(c, localeFlag(current), R.color.surface_variant, 42, 21f)
        val row = Kit.row(
            c,
            flag,
            str(R.string.set_language),
            localeLabel(current),
            Kit.chevron(c),
        ) {
            val sheet = BottomSheet(activity)
            sheet.show { box ->
                box.addView(Kit.text(c, str(R.string.set_language), R.style.Text_Title).margins(c, bottom = 8))
                locales.forEach { locale ->
                    val selected = locale == services.contentRepo.effectiveLocale(settings.contentLocale)
                    val check = if (selected) Kit.icon(c, R.drawable.ic_check, c.col(R.color.primary), 22) else null
                    box.addView(Kit.row(
                        c,
                        Kit.emojiTile(c, localeFlag(locale), R.color.surface_variant, 42, 21f),
                        localeLabel(locale),
                        null,
                        check,
                    ) {
                        if (!selected) {
                            settings.contentLocale = locale
                            settings.contentLocaleChosen = true
                            sheet.dismiss(immediate = true)
                            // Match the onboarding selector: let the sheet removal be drawn
                            // before AppCompat dispatches the locale configuration callback.
                            // Applying in the same click frame makes the whole SettingsScreen
                            // rebuild before the user sees the selection take effect.
                            activity.window.decorView.post { AppLocale.apply(locale) }
                        } else {
                            sheet.dismiss()
                        }
                    })
                }
            }
        }
        val labels = row.getChildAt(1) as LinearLayout
        val title = labels.getChildAt(0) as TextView
        val subtitle = labels.getChildAt(1) as TextView
        localeBindings += {
            val active = services.contentRepo.effectiveLocale(settings.contentLocale)
            flag.text = localeFlag(active)
            title.text = str(R.string.set_language)
            subtitle.text = localeLabel(active)
        }
        g.addView(row)
    }

    private fun group(body: LinearLayout, title: Int, block: (LinearLayout) -> Unit) {
        val heading = Kit.text(ctx, str(title), R.style.Text_BodyStrong, ctx.col(R.color.primary)).margins(ctx, top = 20, bottom = 6)
        bindText(heading) { str(title) }
        body.addView(heading)
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
        val labels = row.getChildAt(0) as LinearLayout
        val title = labels.getChildAt(0) as TextView
        localeBindings += {
            title.text = str(label)
            row.contentDescription = str(label)
        }
        row.setOnClickListener { it.haptic(); sw.isChecked = !sw.isChecked; onChange(sw.isChecked) }
        g.addView(row)
    }

    private fun choice(g: LinearLayout, label: Int, options: () -> List<String>, selected: Int, onPick: (Int) -> Unit) {
        val c = ctx
        val box = Kit.vbox(c) { setPadding(c.dpi(12), c.dpi(10), c.dpi(12), c.dpi(4)) }
        val labelView = Kit.text(c, str(label), R.style.Text_BodyStrong)
        bindText(labelView) { str(label) }
        box.addView(labelView)
        val flow = Kit.flow(c).margins(c, top = 8)
        val chips = ArrayList<TextView>()
        options().forEachIndexed { i, o -> chips += Kit.chip(c, o, i == selected) { onPick(i); chips.forEachIndexed { j, v -> Kit.styleChip(v, j == i) } }.also { flow.addView(it) } }
        localeBindings += {
            val labels = options()
            chips.forEachIndexed { i, chip -> if (i < labels.size) chip.text = labels[i] }
        }
        box.addView(flow)
        g.addView(box)
    }

    private fun action(g: LinearLayout, label: Int, destructive: Boolean = false, onClick: () -> Unit) {
        val c = ctx
        g.addView(Kit.row(c, null, str(label), null, Kit.chevron(c), onClick).apply {
            val title = ((getChildAt(0) as LinearLayout).getChildAt(0) as TextView)
            bindText(title) { str(label) }
            if (destructive) title.setTextColor(c.col(R.color.error))
        })
    }
}

/** Friendly, structured introduction to the product and its independent status. */
class AboutScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val barTitle: String get() = str(R.string.about_disclaimer)

    override fun build(body: LinearLayout) {
        val c = ctx
        body.addView(Kit.card(c, 20, 8, c.col(R.color.primary_container), null) {
            addView(Kit.text(c, str(R.string.about_tagline), R.style.Text_Title, c.col(R.color.on_surface)))
            addView(Kit.text(c, str(R.string.about_intro), R.style.Text_Body).margins(c, top = 8))
        })

        body.addView(Kit.section(c, str(R.string.about_features_title)))
        body.addView(Kit.card(c, 16, 8) {
            listOf(R.string.about_feature_words, R.string.about_feature_grammar, R.string.about_feature_practice).forEach { id ->
                addView(Kit.text(c, "•  " + str(id), R.style.Text_Body).margins(c, top = 4, bottom = 4))
            }
        })

        body.addView(Kit.section(c, str(R.string.about_independent_title)))
        body.addView(Kit.card(c, 16, 8, c.col(R.color.surface_variant), null) {
            addView(Kit.text(c, str(R.string.about_independent_text), R.style.Text_Body))
        })
    }
}

/** Plain text page (disclaimer, licenses, offline privacy policy). */
class TextScreen(activity: MainActivity, private val title: String, private val text: String) : ScrollScreen(activity) {
    override val barTitle: String get() = title
    override fun build(body: LinearLayout) { body.addView(Kit.text(ctx, text, R.style.Text_Body).margins(ctx, top = 8)) }
}

class PrivacyPolicyScreen(activity: MainActivity, private val publicUrl: String) : ScrollScreen(activity) {
    override val barTitle: String get() = str(R.string.privacy_policy)

    override fun build(body: LinearLayout) {
        val c = ctx
        body.addView(Kit.card(c, 16, 8, c.col(R.color.primary_container), null) {
            addView(Kit.text(c, str(R.string.privacy_intro_title), R.style.Text_Title))
            addView(Kit.text(c, str(R.string.privacy_intro), R.style.Text_Body).margins(c, top = 8))
        })
        policyBlock(body, R.string.privacy_backup_title, R.string.privacy_backup)
        policyBlock(body, R.string.privacy_ads_title, R.string.privacy_ads)
        policyBlock(body, R.string.privacy_microphone_title, R.string.privacy_microphone)
        policyBlock(body, R.string.privacy_reminders_title, R.string.privacy_reminders)
        body.addView(Kit.text(c, str(R.string.privacy_link_hint), R.style.Text_Caption).margins(c, top = 10))
        if (publicUrl.isNotBlank()) body.addView(Kit.secondary(ctx, str(R.string.privacy_open_link), 12) {
            runCatching { activity.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(publicUrl))) }
        }.margins(ctx, top = 8))
    }

    private fun policyBlock(body: LinearLayout, title: Int, text: Int) {
        val c = ctx
        body.addView(Kit.card(c, 16, 12) {
            addView(Kit.text(c, str(title), R.style.Text_BodyStrong, c.col(R.color.primary)))
            addView(Kit.text(c, str(text), R.style.Text_Body).margins(c, top = 6))
        })
    }
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
    private var activeSearch: Future<*>? = null

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
        activeSearch?.cancel(true)
        val version = searchVersion
        val rawQuery = query
        if (rawQuery.trim().isEmpty()) { renderEmptyQuery(); return }
        val locale = services.settings.contentLocale
        pendingSearch = Runnable {
            activeSearch = searchExecutor.submit {
                val result = services.contentDb.search(locale, rawQuery)
                mainHandler.post { if (version == searchVersion && query == rawQuery) renderResults(result) }
            }
        }
        mainHandler.postDelayed(pendingSearch!!, 120L)
    }

    override fun onDestroy() {
        pendingSearch?.let(mainHandler::removeCallbacks)
        activeSearch?.cancel(true)
        searchExecutor.shutdownNow()
        super.onDestroy()
    }

    private fun renderEmptyQuery() {
        val c = ctx
        if (!::results.isInitialized) return
        results.removeAllViews()
        val recent = services.settings.recentSearches
        if (recent.isNotEmpty()) {
            results.addView(Kit.section(c, str(R.string.recent_searches), 8))
            val f = Kit.flow(c); recent.forEach { r -> f.addView(Kit.chip(c, r) { input.setText(r); input.setSelection(r.length) }) }
            results.addView(f)
        }
        results.addView(Kit.section(c, str(R.string.suggest_topics)))
        val f = Kit.flow(c)
        results.addView(f)
        val version = searchVersion
        val locale = services.settings.contentLocale
        activeSearch = searchExecutor.submit {
            val topics = runCatching { services.contentDb.suggestedTopics(locale) }.getOrDefault(emptyList())
            mainHandler.post {
                if (version != searchVersion || query.trim().isNotEmpty()) return@post
                f.removeAllViews()
                topics.forEach { addTopicChip(f, it) }
            }
        }
    }

    private fun addTopicChip(flow: android.view.ViewGroup, topic: DbTopicHit) {
        val c = ctx
        flow.addView(Kit.chip(c, topic.icon + " " + topic.name, false, Hues.color(c, topic.hue)) {
            activity.open(WordListScreen(activity, topicId = topic.id))
        })
    }

    private fun renderResults(result: DbSearchResult) {
        val c = ctx
        if (!::results.isInitialized) return
        results.removeAllViews()
        val words = result.words
        val gps = result.grammar
        val topics = result.topics
        if (words.isEmpty() && gps.isEmpty() && topics.isEmpty()) { results.addView(Kit.empty(c, "🔍", str(R.string.no_results), null, null)); return }
        if (words.isNotEmpty()) {
            results.addView(Kit.section(c, str(R.string.words) + " (${words.size})", 8))
            val card = Kit.card(c, 4, 4)
            words.forEachIndexed { i, w ->
                if (i > 0) card.addView(Kit.divider(c))
                card.addView(Kit.row(c, null, "${w.lemma}  \u00b7  ${w.pos}", w.gloss, Kit.chevron(c)) { remember(query.trim()); activity.open(WordDetailScreen(activity, w.id)) })
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
