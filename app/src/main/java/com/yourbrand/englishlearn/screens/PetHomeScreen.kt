package com.yourbrand.englishlearn.screens

import android.graphics.Typeface
import android.os.SystemClock
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.Services
import com.yourbrand.englishlearn.core.Sfx
import com.yourbrand.englishlearn.pet.Mood
import com.yourbrand.englishlearn.pet.PetItem
import com.yourbrand.englishlearn.pet.PetItems
import com.yourbrand.englishlearn.pet.PetView
import com.yourbrand.englishlearn.pet.RoomView
import com.yourbrand.englishlearn.pet.Species
import com.yourbrand.englishlearn.ui.*
import com.yourbrand.englishlearn.ui.views.Bar

/** S20 — Nhà thú cưng. No banner, no interstitials. */
class PetHomeScreen(activity: MainActivity) : Screen(activity) {
    override val petMode = PetMode.HIDDEN
    private lateinit var room: RoomView
    private lateinit var pet: PetView
    private lateinit var bubble: TextView
    private lateinit var panel: LinearLayout
    private lateinit var coinsText: TextView
    private var lastPet = 0L

    override fun onCreateView(parent: ViewGroup): View {
        val c = ctx
        val root = FrameLayout(c)
        val scroll = ScrollView(c).apply { isFillViewport = true }
        val col = Kit.vbox(c)
        // Scene (≈ 55 % of the height)
        val sceneH = (c.resources.displayMetrics.heightPixels * 0.5f).toInt().coerceAtLeast(c.dpi(300))
        val scene = FrameLayout(c).apply { layoutParams = lp(h = sceneH); clipChildren = false }
        room = RoomView(c)
        scene.addView(room, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        pet = PetView(c)
        val petSize = (sceneH * 0.62f).toInt()
        scene.addView(pet, FrameLayout.LayoutParams(petSize, petSize, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL).apply { bottomMargin = (sceneH * (1 - room.floorFrac) * 0.18f).toInt() })
        bubble = TextView(c).apply {
            textSize = 15f; setTextColor(c.col(R.color.on_surface)); maxWidth = c.dpi(240)
            setPadding(c.dpi(14), c.dpi(10), c.dpi(14), c.dpi(10))
            background = c.rounded(c.col(R.color.surface), 18f, c.col(R.color.outline))
            elevation = c.dp(6f); visibility = View.GONE
            accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_POLITE
        }
        scene.addView(bubble, FrameLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT, Gravity.TOP or Gravity.CENTER_HORIZONTAL).apply { topMargin = c.dpi(70) })
        // Top bar over the scene: back + coins
        val top = Kit.hbox(c) { setPadding(c.dpi(8), c.dpi(8) + 0, c.dpi(12), 0) }
        top.addView(ImageView(c).apply {
            setImageResource(R.drawable.ic_arrow_back); tintRes(R.color.on_surface)
            background = c.rounded(c.col(R.color.surface), 100f, ripple = true); elevation = c.dp(3f)
            val p = c.dpi(12); setPadding(p, p, p, p); contentDescription = str(R.string.back)
            layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48)); setOnClickListener { activity.navigator.pop() }
        })
        top.addView(View(c).apply { layoutParams = lp(0, 1, 1f) })
        coinsText = Kit.badge(c, "", c.col(R.color.surface), c.col(R.color.on_surface)).apply { textSize = 15f; elevation = c.dp(3f); setPadding(c.dpi(14), c.dpi(8), c.dpi(14), c.dpi(8)) }
        top.addView(coinsText)
        scene.addView(top, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.TOP))
        col.addView(scene)
        panel = Kit.vbox(c) { setPadding(c.dpi(16), c.dpi(4), c.dpi(16), c.dpi(32)) }
        col.addView(panel)
        scroll.addView(col)
        root.addView(scroll, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))

        pet.isClickable = true
        pet.contentDescription = str(R.string.pet_tap_cd)
        pet.setOnClickListener { petPet() }
        return root
    }

    override fun onShown(firstTime: Boolean) {
        render()
        val s = services
        val hour = Services.hourOf(System.currentTimeMillis())
        if (hour >= 22f || hour < 7f) say(services.content.petLines["SLEEPING_TAP"].orEmpty().randomOrNull())
        else activity.currentMood().let { m -> com.yourbrand.englishlearn.pet.PetMoodResolver.lineKey(m)?.let { say(s.content.petLines[it].orEmpty().randomOrNull()) } }
        if (firstTime && !ctx.reduceMotion) { pet.scaleX = 0.8f; pet.scaleY = 0.8f; pet.animate().scaleX(1f).scaleY(1f).setDuration(300).setInterpolator(android.view.animation.OvershootInterpolator(2f)).start() }
    }

    private fun say(text: String?) {
        if (text == null) return
        bubble.text = text.replace("{name}", services.pet.state.name)
        bubble.visibility = View.VISIBLE
        bubble.alpha = 0f; bubble.scaleX = 0.85f; bubble.scaleY = 0.85f
        bubble.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(150).start()
        bubble.removeCallbacks(hideBubble)
        bubble.postDelayed(hideBubble, 4000)
    }
    private val hideBubble = Runnable { bubble.animate().alpha(0f).setDuration(150).withEndAction { bubble.visibility = View.GONE }.start() }

    private fun petPet() {
        val now = SystemClock.elapsedRealtime()
        if (now - lastPet < 1000) return // max one reaction per second, no XP
        lastPet = now
        pet.wiggle()
        pet.haptic()
        services.sfx.play(Sfx.Sound.POP)
        val loc = IntArray(2); pet.getLocationInWindow(loc)
        activity.particles.burst(loc[0] + pet.width / 2f, loc[1] + pet.height * 0.3f, 8, 3)
        say(services.content.petLines["PETTED"].orEmpty().randomOrNull())
    }

    private fun render() {
        val c = ctx
        val s = services
        val engine = s.pet
        val st = engine.state
        val now = System.currentTimeMillis()
        val hour = Services.hourOf(now)
        room.theme = PetItems.equipped(st, PetItems.THEME)?.id ?: "theme_cozy"
        room.items = PetItems.slots.filter { it != PetItems.THEME && it != PetItems.HAT && it != PetItems.FACE && it != PetItems.NECK }
            .mapNotNull { slot -> PetItems.equipped(st, slot)?.let { slot to it.id } }.toMap()
        room.night = hour >= 19f || hour < 6f
        pet.setFrom(st)
        pet.mood = activity.currentMood()
        coinsText.text = "🪙 ${st.coins}"
        panel.removeAllViews()

        // Stage card
        val stageCard = Kit.card(c, 16, 12)
        val r = Kit.hbox(c)
        val nameBox = Kit.vbox(c) { layoutParams = lp(0, WRAP_CONTENT, 1f) }
        nameBox.addView(Kit.text(c, st.name + " ✏️", R.style.Text_Title).apply {
            setOnClickListener { rename() }
            setOnLongClickListener { rename(); true }
        })
        nameBox.addView(Kit.text(c, str(R.string.stage_of, st.stage, engine.config.maxStage) + " · " + Species.nameVi(st.species), R.style.Text_Caption))
        r.addView(nameBox)
        if (!engine.isMaxStage) r.addView(PetView(c).apply {
            species = st.species; stage = st.stage + 1; silhouette = true; shadow = false
            layoutParams = LinearLayout.LayoutParams(c.dpi(52), c.dpi(52))
            contentDescription = str(R.string.next_stage)
        })
        stageCard.addView(r)
        stageCard.addView(Bar(c).apply { color = c.col(R.color.accent); layoutParams = lp(h = c.dpi(10)).apply { topMargin = c.dpi(10) }; post { set(engine.progress) } })
        stageCard.addView(Kit.text(c, if (engine.isMaxStage) str(R.string.max_stage) else str(R.string.stage_xp, st.stageXp, engine.stageCost), R.style.Text_Caption).margins(c, top = 4))
        panel.addView(stageCard)

        // Recovery card (dozing) — the free option is always first and restores the same amount.
        if (engine.isDozing) {
            panel.addView(Kit.card(c, 16, 12, c.col(R.color.info_container), null) {
                addView(Kit.text(c, "😴 " + str(R.string.dozing_title, st.name), R.style.Text_BodyStrong))
                addView(Kit.text(c, str(R.string.dozing_sub, st.recoverableXp), R.style.Text_Caption).margins(c, top = 2))
                addView(Kit.primary(c, str(R.string.wake_free), 12) { feed() })
                if (s.ads.showsAds) addView(Kit.secondary(c, str(R.string.wake_ad)) { wakeWithAd() })
                if (st.freezeTokens > 0) addView(Kit.secondary(c, str(R.string.wake_token, st.freezeTokens)) { if (engine.useFreezeToken()) { s.savePet(); render(); activity.toast(str(R.string.token_used)) } })
            })
        }

        // Food bowl
        val hunger = engine.hunger(now)
        panel.addView(Kit.card(c, 16, 12) {
            orientation = LinearLayout.HORIZONTAL; gravity = Gravity.CENTER_VERTICAL
            addView(Kit.text(c, "🥣", sizeSp = 30f).apply { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT) })
            addView(Kit.vbox(c) {
                layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(12) }
                addView(Kit.text(c, str(if (hunger >= 60) R.string.bowl_hungry else if (hunger <= 15) R.string.bowl_full else R.string.bowl_ok, st.name), R.style.Text_BodyStrong))
                val goal = s.settings.dailyGoalXp
                addView(Kit.text(c, str(R.string.meal_today, s.todayXp().coerceAtMost(goal), goal), R.style.Text_Caption))
                addView(Bar(c).apply { color = c.col(R.color.success); layoutParams = lp(h = c.dpi(8)).apply { topMargin = c.dpi(6) }; post { set(1f - hunger / 100f) } })
            })
        })

        // Actions
        panel.addView(Kit.primary(c, "🍽️  " + str(R.string.feed), 16) { feed() })
        val row = Kit.hbox(c).margins(c, top = 10)
        row.addView(Kit.secondary(c, "🛋️ " + str(R.string.decorate), 0) { shop() }.apply { layoutParams = lp(0, c.dpi(52), 1f) })
        row.addView(Kit.secondary(c, "🖼️ " + str(R.string.gallery), 0) { gallery() }.apply { layoutParams = lp(0, c.dpi(52), 1f).apply { marginStart = c.dpi(10) } })
        panel.addView(row)
        panel.addView(Kit.text(c, str(R.string.pet_tip), R.style.Text_Caption).apply { gravity = Gravity.CENTER }.margins(c, top = 14))
        if (st.stage == 1 && st.totalXp < 30) panel.addView(Kit.secondary(c, str(R.string.change_species)) { changeSpecies() })
    }

    private fun feed() {
        val s = services
        activity.startSession(s.builder().meal(str(R.string.meal_title, s.pet.state.name), System.currentTimeMillis(), s.settings.userLevel))
    }

    private fun wakeWithAd() {
        val ads = services.ads
        val run = { ads.showRewarded(activity, onRewarded = { services.pet.recover(); services.savePet(); render(); pet.react(Mood.CELEBRATE); activity.toast(str(R.string.woke_up)) }) }
        if (ads.rewardedReady) run() else { activity.toast(str(R.string.ad_loading)); ads.preloadRewarded { run() } }
    }

    private fun rename() {
        val c = ctx
        Dialogs.custom(c) { box, d ->
            box.addView(Kit.text(c, str(R.string.rename), R.style.Text_Title))
            val input = EditText(c).apply { setText(services.pet.state.name); setSingleLine(); filters = arrayOf(android.text.InputFilter.LengthFilter(14)); layoutParams = lp().apply { topMargin = c.dpi(12) } }
            box.addView(input)
            box.addView(Kit.primary(c, str(R.string.save)) {
                val n = input.text.toString().trim()
                if (n.isNotEmpty()) { services.pet.state.name = n; services.savePet() }
                d.dismiss(); render()
            })
        }
    }

    private fun changeSpecies() {
        val c = ctx
        val sheet = BottomSheet(activity)
        sheet.show { box ->
            box.addView(Kit.text(c, str(R.string.change_species), R.style.Text_Title))
            val row = Kit.hbox(c).margins(c, top = 10)
            Species.all.forEach { sp ->
                row.addView(Kit.clickableCard(c, 8, 0, onClick = {
                    services.pet.state.species = sp; services.savePet(); sheet.dismiss(); render()
                }) {
                    gravity = Gravity.CENTER_HORIZONTAL
                    layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginEnd = c.dpi(8) }
                    addView(PetView(c).apply { species = sp; stage = 1; mood = Mood.HI; layoutParams = LinearLayout.LayoutParams(c.dpi(80), c.dpi(80)) })
                    addView(Kit.text(c, Species.nameVi(sp), R.style.Text_BodyStrong).apply { gravity = Gravity.CENTER })
                })
            }
            box.addView(row)
        }
    }

    /** Gallery of the 10 stages (locked ones as silhouettes). */
    private fun gallery() {
        val c = ctx
        val st = services.pet.state
        val sheet = BottomSheet(activity)
        sheet.show { box ->
            box.addView(Kit.text(c, str(R.string.gallery), R.style.Text_Title))
            box.addView(Kit.text(c, str(R.string.gallery_sub), R.style.Text_Caption).margins(c, bottom = 8))
            var row: LinearLayout? = null
            (1..10).forEach { n ->
                if ((n - 1) % 3 == 0) { row = Kit.hbox(c).margins(c, top = 8); box.addView(row) }
                row!!.addView(Kit.card(c, 6, 0, c.col(if (n <= st.stage) R.color.primary_container else R.color.surface_variant), null) {
                    gravity = Gravity.CENTER_HORIZONTAL
                    layoutParams = lp(0, WRAP_CONTENT, 1f).apply { if ((n - 1) % 3 != 0) marginStart = c.dpi(8) }
                    addView(PetView(c).apply { species = st.species; stage = n; silhouette = n > st.stage; mood = if (n == st.stage) Mood.HI else Mood.IDLE; layoutParams = LinearLayout.LayoutParams(c.dpi(84), c.dpi(84)) })
                    addView(Kit.text(c, str(R.string.stage_n, n), R.style.Text_Caption).apply { gravity = Gravity.CENTER })
                })
            }
            if (10 % 3 != 0) repeat(3 - 10 % 3) { row?.addView(View(c).apply { layoutParams = lp(0, 1, 1f).apply { marginStart = c.dpi(8) } }) }
        }
    }

    /** Decor & accessories shop — coins come only from learning. */
    private fun shop() {
        val c = ctx
        val sheet = BottomSheet(activity)
        var slot = PetItems.THEME
        lateinit var grid: LinearLayout
        lateinit var tabs: LinearLayout
        lateinit var coins: TextView
        fun slotName(s: String) = str(when (s) {
            PetItems.THEME -> R.string.slot_theme; PetItems.BED -> R.string.slot_bed; PetItems.RUG -> R.string.slot_rug; PetItems.PLANT -> R.string.slot_plant
            PetItems.LAMP -> R.string.slot_lamp; PetItems.WALL -> R.string.slot_wall; PetItems.TOY -> R.string.slot_toy; PetItems.HAT -> R.string.slot_hat
            PetItems.FACE -> R.string.slot_face; else -> R.string.slot_neck
        })
        fun fill() {
            val engine = services.pet
            val st = engine.state
            coins.text = "🪙 ${st.coins}"
            tabs.removeAllViews()
            PetItems.slots.forEach { s -> tabs.addView(Kit.chip(c, slotName(s), s == slot) { slot = s; fill() }) }
            grid.removeAllViews()
            var row: LinearLayout? = null
            val items = PetItems.inSlot(slot)
            items.forEachIndexed { i, item ->
                if (i % 3 == 0) { row = Kit.hbox(c) { gravity = Gravity.TOP }.margins(c, top = 8); grid.addView(row) }
                row!!.addView(itemTile(item, engine.owns(item), PetItems.equipped(st, item.slot)?.id == item.id, st.stage >= item.minStage).apply {
                    layoutParams = lp(0, WRAP_CONTENT, 1f).apply { if (i % 3 != 0) marginStart = c.dpi(8) }
                    setOnClickListener {
                        it.haptic()
                        when {
                            engine.owns(item) -> { engine.equip(item); services.savePet(); render(); fill(); services.sfx.play(Sfx.Sound.POP) }
                            st.stage < item.minStage -> activity.toast(str(R.string.need_stage, item.minStage))
                            st.coins < item.price -> activity.toast(str(R.string.need_coins, item.price - st.coins))
                            else -> Dialogs.confirm(c, item.emoji + " " + item.vi, str(R.string.buy_msg, item.price), str(R.string.buy), str(R.string.cancel)) {
                                if (engine.buy(item)) {
                                    engine.equip(item); services.savePet(); render(); fill()
                                    services.sfx.play(Sfx.Sound.SUCCESS)
                                    pet.react(Mood.CELEBRATE)
                                    activity.particles.burst(c.resources.displayMetrics.widthPixels / 2f, c.dpi(200).toFloat(), 14)
                                }
                            }
                        }
                    }
                })
            }
            if (items.size % 3 != 0) repeat(3 - items.size % 3) { row?.addView(View(c).apply { layoutParams = lp(0, 1, 1f).apply { marginStart = c.dpi(8) } }) }
        }
        sheet.show { box ->
            val head = Kit.hbox(c)
            head.addView(Kit.text(c, str(R.string.decorate), R.style.Text_Title).apply { layoutParams = lp(0, WRAP_CONTENT, 1f) })
            coins = Kit.badge(c, "", c.col(R.color.accent_container), c.col(R.color.on_surface)).apply { textSize = 14f }
            head.addView(coins)
            box.addView(head)
            box.addView(Kit.text(c, str(R.string.coins_hint), R.style.Text_Caption).margins(c, top = 2))
            tabs = Kit.hbox(c)
            box.addView(Kit.hscroll(c, tabs).margins(c, top = 10))
            grid = Kit.vbox(c)
            box.addView(grid)
            fill()
        }
    }

    private fun itemTile(item: PetItem, owned: Boolean, equipped: Boolean, unlocked: Boolean): LinearLayout {
        val c = ctx
        return Kit.card(c, 8, 0, c.col(if (equipped) R.color.primary_container else R.color.surface), c.col(if (equipped) R.color.primary else R.color.outline)) {
            gravity = Gravity.CENTER_HORIZONTAL
            minimumHeight = c.dpi(118)
            isClickable = true
            foreground = c.rounded(0, 16f, ripple = true)
            addView(Kit.text(c, item.emoji, sizeSp = 30f).apply { gravity = Gravity.CENTER; alpha = if (unlocked) 1f else 0.4f }.margins(c, top = 4))
            addView(Kit.ellipsize(Kit.text(c, item.vi, R.style.Text_BodyStrong).apply { textSize = 12.5f; gravity = Gravity.CENTER }, 2).margins(c, top = 4))
            val label = when {
                equipped -> "✓ " + str(R.string.in_use)
                owned -> str(R.string.owned)
                !unlocked -> "🔒 " + str(R.string.stage_n, item.minStage)
                else -> "🪙 ${item.price}"
            }
            addView(Kit.text(c, label, R.style.Text_Caption, c.col(if (equipped) R.color.primary else if (owned) R.color.success else R.color.on_surface)).apply { gravity = Gravity.CENTER; textSize = 12f; setTypeface(typeface, Typeface.BOLD) }.margins(c, top = 4))
            contentDescription = "${item.vi}, $label"
        }
    }
}
