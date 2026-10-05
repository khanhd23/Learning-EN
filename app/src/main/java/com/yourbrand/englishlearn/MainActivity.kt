package com.yourbrand.englishlearn

import android.animation.ValueAnimator
import android.graphics.Path
import android.graphics.PathMeasure
import android.graphics.Rect
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.yourbrand.englishlearn.learning.LearningStore
import com.yourbrand.englishlearn.learning.Session
import com.yourbrand.englishlearn.pet.Mood
import com.yourbrand.englishlearn.pet.PetFloatingView
import com.yourbrand.englishlearn.pet.PetMoodResolver
import com.yourbrand.englishlearn.pet.PetView
import com.yourbrand.englishlearn.screens.*
import com.yourbrand.englishlearn.ui.*
import com.yourbrand.englishlearn.ui.views.ParticleView

class MainActivity : AppCompatActivity() {
    lateinit var navigator: Navigator
        private set
    lateinit var overlay: FrameLayout
        private set
    lateinit var petView: PetFloatingView
        private set
    lateinit var particles: ParticleView
        private set
    private lateinit var rootFrame: FrameLayout
    private lateinit var screenContainer: FrameLayout
    private lateinit var bannerContainer: FrameLayout
    private lateinit var bottomNav: LinearLayout
    private val navItems = LinkedHashMap<Tab, Triple<LinearLayout, ImageView, TextView>>()
    val sheets = mutableListOf<BottomSheet>()

    private var currentTab = Tab.TODAY
    private var bannerLoaded = false
    var bottomInset = 0
        private set
    private var justFinishedSession = false
    private var greetPending = false
    private var resumedAt = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        buildRoot()
        setContentView(rootFrame)

        ViewCompat.setOnApplyWindowInsetsListener(rootFrame) { _, insets ->
            val bars = insets.getInsets(WindowInsetsCompat.Type.systemBars() or WindowInsetsCompat.Type.displayCutout())
            val ime = insets.isVisible(WindowInsetsCompat.Type.ime())
            screenContainer.updatePadding(top = bars.top, left = bars.left, right = bars.right)
            bottomInset = bars.bottom
            petView.setKeyboard(ime)
            updateChrome()
            insets
        }

        navigator = Navigator(screenContainer) { updateChrome() }
        onBackPressedDispatcher.addCallback(this) { handleBack() }

        if (!services.settings.onboarded) navigator.reset(OnboardingScreen(this))
        else selectTab(savedInstanceState?.getString("tab")?.let { runCatching { Tab.valueOf(it) }.getOrNull() } ?: Tab.TODAY)
        // Resume a mock test after process death.
        if (savedInstanceState != null && services.settings.mockInProgress != null && services.settings.onboarded) MockScreen.resume(this)
        if (savedInstanceState == null && BuildConfig.DEBUG) DemoMode.handle(this, intent)

        rootFrame.post {
            services.ads.gatherConsent(this) { loadBanner() }
            services.ads.preloadRewarded()
        }
    }

    // ---- layout ------------------------------------------------------------------------------

    private fun buildRoot() {
        rootFrame = FrameLayout(this).apply { setBackgroundColor(col(R.color.bg)) }
        val column = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        screenContainer = FrameLayout(this).apply { clipChildren = false }
        column.addView(screenContainer, LinearLayout.LayoutParams(MATCH_PARENT, 0, 1f))
        // Banner sits inside the content column above the nav (never in sessions, lessons, Pet Home).
        bannerContainer = FrameLayout(this).apply {
            setBackgroundColor(col(R.color.bg))
            minimumHeight = dpi(50)
            setPadding(0, dpi(4), 0, dpi(6))
            visibility = View.GONE
            tag = "petAvoid"
        }
        column.addView(bannerContainer, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        bottomNav = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            setBackgroundResource(R.drawable.bg_bottom_nav)
            setPadding(0, dpi(6), 0, dpi(4))
        }
        column.addView(bottomNav, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
        rootFrame.addView(column, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        addNav(Tab.TODAY, R.drawable.ic_sun, R.string.tab_today)
        addNav(Tab.VOCAB, R.drawable.ic_book, R.string.tab_vocab)
        addNav(Tab.GRAMMAR, R.drawable.ic_structure, R.string.tab_grammar)
        addNav(Tab.EXAM, R.drawable.ic_target, R.string.tab_exam)
        addNav(Tab.ME, R.drawable.ic_person, R.string.tab_me)

        petView = PetFloatingView(this, onOpen = { openPetHome() }, onMenu = { petQuickMenu() })
        rootFrame.addView(petView)
        particles = ParticleView(this)
        rootFrame.addView(particles, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        overlay = FrameLayout(this)
        rootFrame.addView(overlay, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        // Keep the pet inside the visible content area (above banner + nav).
        screenContainer.addOnLayoutChangeListener { v, l, t, r, b, _, _, _, _ ->
            petView.setBounds(Rect(l, t + v.paddingTop, r, b))
        }
    }

    private fun addNav(tab: Tab, icon: Int, label: Int) {
        val item = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            minimumHeight = dpi(56)
            background = rounded(0, 16f, ripple = true)
            isClickable = true; isFocusable = true
            contentDescription = getString(label)
        }
        val pill = FrameLayout(this)
        val img = ImageView(this).apply { setImageResource(icon); importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO }
        pill.addView(img, FrameLayout.LayoutParams(dpi(22), dpi(22), Gravity.CENTER))
        item.addView(pill, LinearLayout.LayoutParams(dpi(56), dpi(30)))
        val tv = TextView(this).apply {
            setText(label); textSize = 11.5f; gravity = Gravity.CENTER; maxLines = 1
            ellipsize = android.text.TextUtils.TruncateAt.END
            includeFontPadding = true
        }
        item.addView(tv, LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT).apply { topMargin = dpi(2) })
        item.setOnClickListener {
            it.haptic()
            if (tab == currentTab && navigator.depth == 1) (navigator.current as? ScrollScreen)?.scrollToTop() else selectTab(tab)
        }
        bottomNav.addView(item, LinearLayout.LayoutParams(0, WRAP_CONTENT, 1f))
        navItems[tab] = Triple(item, img, tv)
        pill.tag = "pill"
    }

    fun selectTab(tab: Tab) {
        currentTab = tab
        navigator.reset(when (tab) {
            Tab.TODAY -> TodayScreen(this)
            Tab.VOCAB -> VocabScreen(this)
            Tab.GRAMMAR -> GrammarScreen(this)
            Tab.EXAM -> ExamScreen(this)
            Tab.ME -> MeScreen(this)
        })
    }

    fun open(screen: Screen) = navigator.push(screen)

    fun startSession(session: Session) {
        if (session.exercises.isEmpty()) { toast(getString(R.string.nothing_to_practice)); return }
        services.ads.preloadInterstitial()
        navigator.push(SessionScreen(this, session))
    }

    fun openPetHome() {
        if (navigator.current is PetHomeScreen) return
        navigator.push(PetHomeScreen(this))
    }

    private fun handleBack() {
        if (sheets.isNotEmpty()) { sheets.last().dismiss(); return }
        val s = navigator.current
        when {
            s?.onBack() == true -> Unit
            navigator.pop() -> Unit
            s !is OnboardingScreen && currentTab != Tab.TODAY -> selectTab(Tab.TODAY)
            else -> finish()
        }
    }

    /** Bottom bar, banner, pet mode and insets for the visible screen. */
    fun updateChrome() {
        if (!::navigator.isInitialized) return
        val screen = navigator.current ?: return
        val showNav = screen.tab != null
        screen.tab?.let { currentTab = it }
        bottomNav.show(showNav)
        val showBanner = bannerLoaded && screen.allowsBanner && services.ads.showsAds
        bannerContainer.show(showBanner)
        bottomNav.updatePadding(bottom = dpi(4) + bottomInset)
        screenContainer.updatePadding(bottom = if (!showNav && !showBanner) bottomInset else 0)
        bannerContainer.updatePadding(bottom = dpi(6) + if (!showNav) bottomInset else 0)
        navItems.forEach { (tab, v) ->
            val sel = tab == currentTab
            val pill = v.second.parent as FrameLayout
            pill.background = if (sel) getDrawable(R.drawable.bg_nav_indicator) else null
            v.second.tintRes(if (sel) R.color.primary else R.color.muted)
            v.third.setTextColor(col(if (sel) R.color.on_surface else R.color.muted))
            v.third.paint.isFakeBoldText = sel
            v.third.invalidate()
            v.first.isSelected = sel
        }
        petView.setScreenMode(screen.petMode == PetMode.FLOATING)
        refreshPet()
        // Avoid tagged views after layout settles.
        screenContainer.postDelayed({ checkAvoid() }, 300)
    }

    fun checkAvoid() {
        val rects = ArrayList<Rect>()
        fun walk(v: View) {
            if (v.visibility != View.VISIBLE) return
            if (v.tag == "petAvoid" && v.isShown) {
                val loc = IntArray(2); v.getLocationInWindow(loc)
                val rl = IntArray(2); rootFrame.getLocationInWindow(rl)
                rects += Rect(loc[0] - rl[0], loc[1] - rl[1], loc[0] - rl[0] + v.width, loc[1] - rl[1] + v.height)
            }
            if (v is ViewGroup) for (i in 0 until v.childCount) walk(v.getChildAt(i))
        }
        navigator.current?.view?.let { walk(it) }
        petView.avoid(rects)
    }

    // ---- pet ---------------------------------------------------------------------------------

    fun currentMood(now: Long = System.currentTimeMillis()): Mood {
        val pet = services.pet
        return PetMoodResolver.resolve(PetMoodResolver.Input(
            hourOfDay = Services.hourOf(now), hunger = pet.hunger(now), fullToday = services.goalReached(),
            greet = greetPending, dozing = pet.isDozing, celebrate = pet.state.pendingStageUp > 0 && false,
            justFinishedSession = justFinishedSession,
        ))
    }

    /** Re-applies pet look + mood; may show a throttled bubble. */
    fun refreshPet(forceBubble: Boolean = false) {
        val state = services.pet.state
        val mood = currentMood()
        petView.pet.setFrom(state)
        petView.pet.mood = mood
        petView.setDescription(getString(R.string.pet_cd, moodWord(mood)))
        if (!petView.isShowingPet) return
        val key = PetMoodResolver.lineKey(mood) ?: return
        val now = System.currentTimeMillis()
        val priority = when (mood) { Mood.CELEBRATE -> 7; Mood.DOZING -> 6; Mood.HI -> 5; Mood.HUNGRY, Mood.STARVING -> 4; Mood.FULL -> 3; else -> 2 }
        val b = services.bubbles
        b.hiddenDay = services.settings.bubblesHiddenDay
        val blocked = petView.isBlocked || sheets.isNotEmpty() || services.ads.isShowingFullScreen || !services.settings.petBubbles
        if (!forceBubble && !b.canShow(now, blocked, priority)) return
        if (forceBubble && blocked) return
        val lines = services.content.petLines[key].orEmpty()
        val line = b.pick(key, lines)?.replace("{name}", state.name) ?: return
        b.onShown(now)
        petView.say(line, iconOnly = false)
        petView.pet.react(mood)
        if (mood == Mood.HI) greetPending = false
        if (mood == Mood.FULL) justFinishedSession = false
    }

    fun moodWord(m: Mood): String = getString(when (m) {
        Mood.HUNGRY, Mood.STARVING -> R.string.mood_hungry
        Mood.FULL -> R.string.mood_full
        Mood.SLEEPY -> R.string.mood_sleepy
        Mood.SLEEPING -> R.string.mood_sleeping
        Mood.DOZING -> R.string.mood_dozing
        Mood.HI -> R.string.mood_hi
        else -> R.string.mood_happy
    })

    private fun petQuickMenu() {
        BottomSheet(this).show { box ->
            box.addView(Kit.text(this, getString(R.string.pet_menu_title, services.pet.state.name), R.style.Text_Title))
            box.addView(Kit.row(this, Kit.icon(this, R.drawable.ic_moon, col(R.color.muted)), getString(R.string.pet_hide_bubbles_today)) {
                services.settings.bubblesHiddenDay = LearningStore.dayKey(System.currentTimeMillis()); petView.hideBubbleNow(); sheets.lastOrNull()?.dismiss()
            }.margins(this, top = 8))
            box.addView(Kit.row(this, Kit.icon(this, R.drawable.ic_visibility, col(R.color.muted)), getString(R.string.pet_hide)) {
                services.settings.petVisible = false; sheets.lastOrNull()?.dismiss(); updateChrome(); toast(getString(R.string.pet_hidden_hint))
            })
            box.addView(Kit.row(this, Kit.icon(this, R.drawable.ic_refresh, col(R.color.muted)), getString(R.string.pet_reset_position)) {
                petView.resetPosition(); sheets.lastOrNull()?.dismiss()
            })
        }
    }

    /** Called after a session completes (before showing the result). */
    fun onSessionDone() { justFinishedSession = true }

    /** "+5 XP" chip flies along a curved path (450 ms) toward the pet (SKILL.md 10). */
    fun flyXp(fromView: View, xp: Int, target: View? = null) {
        if (xp <= 0) return
        val chip = Kit.badge(this, "+$xp XP", col(R.color.accent), col(R.color.on_accent)).apply { textSize = 13f }
        overlay.addView(chip, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT))
        val loc = IntArray(2); fromView.getLocationInWindow(loc)
        val rl = IntArray(2); rootFrame.getLocationInWindow(rl)
        val sx = loc[0] - rl[0] + fromView.width / 2f
        val sy = loc[1] - rl[1] + fromView.height / 2f
        val (tx, ty) = if (target != null) {
            val tl = IntArray(2); target.getLocationInWindow(tl)
            (tl[0] - rl[0] + target.width / 2f) to (tl[1] - rl[1] + target.height / 2f)
        } else petView.target()
        if (reduceMotion) { chip.x = sx; chip.y = sy; chip.animate().alpha(0f).setStartDelay(400).setDuration(100).withEndAction { overlay.removeView(chip) }.start(); return }
        val path = Path().apply { moveTo(sx, sy); quadTo((sx + tx) / 2 + dp(40f), minOf(sy, ty) - dp(80f), tx, ty) }
        val pm = PathMeasure(path, false)
        val pos = FloatArray(2)
        chip.post {
            ValueAnimator.ofFloat(0f, 1f).apply {
                duration = 450
                interpolator = DecelerateInterpolator(1.2f)
                addUpdateListener {
                    val f = it.animatedValue as Float
                    pm.getPosTan(pm.length * f, pos, null)
                    chip.x = pos[0] - chip.width / 2f; chip.y = pos[1] - chip.height / 2f
                    chip.scaleX = 1f - 0.4f * f; chip.scaleY = chip.scaleX
                }
                doOnEndCompat { overlay.removeView(chip) }
                start()
            }
        }
    }

    /** Full-screen stage-up celebration (tap to skip). */
    fun showStageUp(stage: Int) {
        services.pet.state.pendingStageUp = 0
        services.savePet()
        val scrim = FrameLayout(this).apply {
            setBackgroundColor(0xCC0B1420.toInt())
            isClickable = true
        }
        val rays = object : View(this) {
            var rot = 0f
            val paint = android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG).apply { color = 0x33FFD54F }
            override fun onDraw(c: android.graphics.Canvas) {
                val cx = width / 2f; val cy = height * 0.42f
                c.save(); c.rotate(rot, cx, cy)
                for (i in 0 until 12) {
                    c.rotate(30f, cx, cy)
                    val p = Path().apply { moveTo(cx, cy); lineTo(cx - dp(30f), cy - height); lineTo(cx + dp(30f), cy - height); close() }
                    c.drawPath(p, paint)
                }
                c.restore()
            }
        }
        scrim.addView(rays, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        val pet = PetView(this).apply { setFrom(services.pet.state); mood = Mood.CELEBRATE }
        scrim.addView(pet, FrameLayout.LayoutParams(dpi(220), dpi(220), Gravity.CENTER).apply { bottomMargin = dpi(90) })
        val title = Kit.text(this, getString(R.string.stage_up_title, stage), R.style.Text_Display, 0xFFFFFFFF.toInt(), 30f).apply { gravity = Gravity.CENTER }
        scrim.addView(title, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.CENTER).apply { topMargin = dpi(150) })
        val sub = Kit.text(this, getString(R.string.stage_up_sub, services.pet.state.name), R.style.Text_Body, 0xDDFFFFFF.toInt()).apply { gravity = Gravity.CENTER }
        scrim.addView(sub, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.CENTER).apply { topMargin = dpi(200) })
        overlay.addView(scrim, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        petView.setBlocked(true)
        services.sfx.play(com.yourbrand.englishlearn.core.Sfx.Sound.LEVEL_UP)
        scrim.haptic(android.view.HapticFeedbackConstants.LONG_PRESS)
        scrim.alpha = 0f
        scrim.animate().alpha(1f).setDuration(200).start()
        if (!reduceMotion) {
            pet.scaleX = 0.4f; pet.scaleY = 0.4f
            pet.animate().scaleX(1f).scaleY(1f).setDuration(500).setInterpolator(android.view.animation.OvershootInterpolator(2f)).start()
            ValueAnimator.ofFloat(0f, 360f).apply { duration = 12000; repeatCount = ValueAnimator.INFINITE; addUpdateListener { rays.rot = it.animatedValue as Float; rays.invalidate() }; start() }
                .also { anim -> scrim.tag = anim }
            pet.postDelayed({ pet.react(Mood.CELEBRATE) }, 500)
            scrim.post { particles.confetti(60) }
        }
        val close = {
            (scrim.tag as? ValueAnimator)?.cancel()
            scrim.animate().alpha(0f).setDuration(200).withEndAction { overlay.removeView(scrim); petView.setBlocked(false); refreshPet() }.start()
        }
        scrim.setOnClickListener { close() }
        scrim.postDelayed({ if (scrim.parent != null) close() }, 4000)
    }

    fun toast(msg: String) {
        val t = Kit.text(this, msg, R.style.Text_Body, col(R.color.surface)).apply {
            background = rounded(col(R.color.on_surface), 14f)
            setPadding(dpi(16), dpi(12), dpi(16), dpi(12))
            gravity = Gravity.CENTER
        }
        overlay.addView(t, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL).apply {
            bottomMargin = dpi(90) + bottomInset; leftMargin = dpi(24); rightMargin = dpi(24)
        })
        t.alpha = 0f; t.translationY = dp(10f)
        t.animate().alpha(1f).translationY(0f).setDuration(180).start()
        t.postDelayed({ t.animate().alpha(0f).setDuration(180).withEndAction { overlay.removeView(t) }.start() }, 3000)
    }

    // ---- ads ---------------------------------------------------------------------------------

    private fun loadBanner() {
        if (!services.ads.showsAds) return
        services.ads.loadBanner(this, bannerContainer) {
            if (!bannerLoaded) { bannerLoaded = true; updateChrome() }
        }
    }

    // ---- lifecycle ---------------------------------------------------------------------------

    override fun onResume() {
        super.onResume()
        services.ads.resumeBanner(bannerContainer)
        val now = System.currentTimeMillis()
        val s = services.settings
        // Greeting: first open of the day, or back after > 1 h away.
        if (LearningStore.dayKey(s.lastOpenAt) != LearningStore.dayKey(now) || now - s.lastOpenAt > 3_600_000L) greetPending = s.onboarded
        s.lastOpenAt = now
        resumedAt = now
        val moved = services.pet.onOpen(now)
        if (moved > 0 || services.pet.state.lastSeenAt == now) services.savePet()
        navigator.current?.onResume()
        rootFrame.postDelayed({ refreshPet() }, 900)
    }

    override fun onPause() {
        services.ads.pauseBanner(bannerContainer)
        navigator.current?.onPause()
        services.savePet()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("tab", currentTab.name)
    }

    override fun onDestroy() {
        services.ads.destroyBanner(bannerContainer)
        if (isFinishing) services.tts.stop()
        super.onDestroy()
    }
}

fun ValueAnimator.doOnEndCompat(block: () -> Unit) {
    addListener(object : android.animation.AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: android.animation.Animator) { block() }
    })
}
