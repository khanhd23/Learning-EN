package com.yourbrand.englishlearn.pet

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.TextView
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.services
import com.yourbrand.englishlearn.ui.col
import com.yourbrand.englishlearn.ui.dp
import com.yourbrand.englishlearn.ui.dpi
import com.yourbrand.englishlearn.ui.haptic
import com.yourbrand.englishlearn.ui.reduceMotion
import com.yourbrand.englishlearn.ui.rounded
import kotlin.math.abs
import kotlin.math.hypot

/**
 * Floating pet companion (SKILL.md 5.1): a child of the activity root, above screen content and the
 * bottom nav, below sheets/dialogs/overlays. Moves only with translationX/Y (no re-layout while idle
 * or dragging). Long-press (350 ms) + drag, snaps to the nearest edge; a tap opens Pet Home.
 */
@SuppressLint("ViewConstructor")
class PetFloatingView(context: Context, private val onOpen: () -> Unit, private val onMenu: () -> Unit) : FrameLayout(context) {
    val pet = PetView(context).apply { crop = true; shadow = false }
    private val disc = FrameLayout(context)
    val bubble = TextView(context)

    /** Area the pet may occupy (screen content minus bottom nav / banner), in root coordinates. */
    private val bounds = Rect()
    private var sizePx = 0
    private var blocked = false
    private var hiddenByScreen = true
    private var keyboard = false
    private var avoidOffset = 0f
    private var savedPos: Pair<Float, Float>? = null // fractions (x: 0 start / 1 end, y of bounds)
    private var hideBubble: Runnable? = null

    init {
        clipChildren = false
        clipToPadding = false
        disc.background = context.rounded(context.col(R.color.surface), 100f, context.col(R.color.outline), 1.5f)
        disc.elevation = context.dp(6f)
        disc.addView(pet, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT))
        addView(disc)
        bubble.apply {
            textSize = 14f
            setTextColor(context.col(R.color.on_surface))
            maxLines = 2
            maxWidth = context.dpi(220)
            setPadding(context.dpi(14), context.dpi(10), context.dpi(14), context.dpi(10))
            background = context.rounded(context.col(R.color.surface), 18f, context.col(R.color.outline), 1f)
            elevation = context.dp(8f)
            visibility = GONE
            accessibilityLiveRegion = View.ACCESSIBILITY_LIVE_REGION_POLITE
            setOnClickListener { hideBubbleNow(); onOpen() }
        }
        addView(bubble, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        isFocusable = false
        disc.isFocusable = true
        disc.importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_YES
        setupTouch()
        applySize()
    }

    fun applySize() {
        sizePx = context.dpi(when (context.services.settings.petSize) { 0 -> 48; 2 -> 72; else -> 56 })
        disc.layoutParams = LayoutParams(sizePx, sizePx)
        requestLayout()
        post { place(animate = false) }
    }

    /** Called by the activity whenever the visible content area changes. */
    fun setBounds(r: Rect) {
        if (r == bounds) return
        bounds.set(r)
        post { place(animate = false) }
    }

    fun setScreenMode(visible: Boolean) {
        hiddenByScreen = !visible
        refreshVisibility()
    }

    fun setBlocked(b: Boolean) { blocked = b; if (b) hideBubbleNow() }
    val isBlocked get() = blocked

    fun setKeyboard(open: Boolean) { keyboard = open; refreshVisibility() }

    private fun refreshVisibility() {
        val want = !hiddenByScreen && !keyboard && context.services.settings.petVisible
        if (want && visibility != VISIBLE) {
            visibility = VISIBLE
            if (!context.reduceMotion) { disc.translationX = (if (isAtEnd()) 1 else -1) * context.dp(30f); disc.alpha = 0f; disc.animate().translationX(0f).alpha(1f).setDuration(220).start() }
        } else if (!want && visibility == VISIBLE) {
            visibility = GONE
            hideBubbleNow()
        }
    }

    private fun isAtEnd() = (savedPos?.first ?: 1f) >= 0.5f

    fun setDescription(text: String) { disc.contentDescription = text }

    /**
     * The pet no longer moves by itself: jumping away from one button kept landing it on another.
     * It stays where it is (right side by default, or where the user dragged it).
     */
    @Suppress("UNUSED_PARAMETER")
    fun avoid(rects: List<Rect>) = Unit

    private fun place(animate: Boolean) {
        if (bounds.isEmpty || sizePx == 0) return
        val margin = context.dp(12f)
        val pos = savedPos ?: context.services.settings.petPos(isLandscape())
        savedPos = pos
        val fx = pos?.first ?: 1f
        val minY = bounds.top + margin
        val maxY = bounds.bottom - sizePx - margin
        val ty = ((pos?.second?.let { bounds.top + it * bounds.height() } ?: maxY.toFloat()) - avoidOffset).coerceIn(minY, maxY.toFloat().coerceAtLeast(minY))
        val tx = if (fx >= 0.5f) bounds.right - sizePx - margin else bounds.left + margin
        if (animate && !context.reduceMotion) disc.animate().x(tx).y(ty).setDuration(250).setInterpolator(OvershootInterpolator(1.2f)).start()
        else { disc.x = tx; disc.y = ty }
        positionBubble()
    }

    private fun isLandscape() = resources.configuration.orientation == android.content.res.Configuration.ORIENTATION_LANDSCAPE

    // ---- touch ---------------------------------------------------------------------------

    @SuppressLint("ClickableViewAccessibility")
    private fun setupTouch() {
        val slop = ViewConfiguration.get(context).scaledTouchSlop
        var downX = 0f; var downY = 0f; var startX = 0f; var startY = 0f
        var dragging = false; var moved = false
        val longPress = Runnable {
            dragging = true
            disc.haptic(android.view.HapticFeedbackConstants.LONG_PRESS)
            disc.animate().scaleX(1.1f).scaleY(1.1f).setDuration(120).start()
            hideBubbleNow()
        }
        disc.setOnTouchListener { v, e ->
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    downX = e.rawX; downY = e.rawY; startX = v.x; startY = v.y
                    dragging = false; moved = false
                    v.postDelayed(longPress, 350)
                    true
                }
                MotionEvent.ACTION_MOVE -> {
                    val dx = e.rawX - downX; val dy = e.rawY - downY
                    if (!dragging && hypot(dx, dy) > slop) v.removeCallbacks(longPress)
                    if (dragging) {
                        if (hypot(dx, dy) > slop) moved = true
                        v.x = (startX + dx).coerceIn(bounds.left.toFloat(), (bounds.right - sizePx).toFloat())
                        v.y = (startY + dy).coerceIn(bounds.top.toFloat(), (bounds.bottom - sizePx).toFloat())
                        positionBubble()
                    }
                    true
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    v.removeCallbacks(longPress)
                    if (dragging) {
                        v.animate().scaleX(1f).scaleY(1f).setDuration(150).start()
                        if (moved) {
                            val fx = if (v.x + sizePx / 2f > (bounds.left + bounds.right) / 2f) 1f else 0f
                            val fy = ((v.y + avoidOffset - bounds.top) / bounds.height().coerceAtLeast(1)).coerceIn(0f, 1f)
                            savedPos = fx to fy
                            context.services.settings.setPetPos(isLandscape(), savedPos)
                            place(animate = true)
                        } else onMenu()
                    } else if (e.actionMasked == MotionEvent.ACTION_UP && abs(e.rawX - downX) < slop && abs(e.rawY - downY) < slop) {
                        v.performClick()
                        hideBubbleNow()
                        onOpen()
                    }
                    dragging = false
                    true
                }
                else -> false
            }
        }
        disc.setOnClickListener { onOpen() }
    }

    fun resetPosition() {
        savedPos = null
        context.services.settings.setPetPos(isLandscape(), null)
        place(animate = true)
    }

    // ---- bubble ----------------------------------------------------------------------------

    fun say(text: String, iconOnly: Boolean) {
        if (visibility != VISIBLE || blocked || iconOnly) { if (!iconOnly) return else { pet.react(Mood.HI); return } }
        bubble.text = text
        bubble.visibility = VISIBLE
        bubble.alpha = 0f
        bubble.scaleX = 0.85f; bubble.scaleY = 0.85f
        positionBubble()
        bubble.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(150).start()
        bubble.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_FOCUSED)
        hideBubble?.let { removeCallbacks(it) }
        val big = resources.configuration.fontScale >= 1.3f
        hideBubble = Runnable { hideBubbleNow() }.also { postDelayed(it, if (big) 6000 else 4000) }
        setupBubbleSwipe()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupBubbleSwipe() {
        var downX = 0f
        bubble.setOnTouchListener { v, e ->
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> { downX = e.rawX; false }
                MotionEvent.ACTION_MOVE -> { v.translationX = e.rawX - downX; true }
                MotionEvent.ACTION_UP -> {
                    if (abs(v.translationX) > v.width / 3f) { hideBubbleNow(); true }
                    else { v.animate().translationX(0f).setDuration(120).start(); abs(e.rawX - downX) > context.dpi(8) }
                }
                else -> false
            }
        }
    }

    fun hideBubbleNow() {
        hideBubble?.let { removeCallbacks(it) }
        if (bubble.visibility != VISIBLE) return
        bubble.animate().alpha(0f).setDuration(150).withEndAction { bubble.visibility = GONE; bubble.translationX = 0f }.start()
    }

    private fun positionBubble() {
        if (bubble.visibility != VISIBLE) return
        bubble.measure(MeasureSpec.makeMeasureSpec(context.dpi(220), MeasureSpec.AT_MOST), MeasureSpec.UNSPECIFIED)
        val bw = bubble.measuredWidth; val bh = bubble.measuredHeight
        val atEnd = disc.x + sizePx / 2f > (bounds.left + bounds.right) / 2f
        // Beside the pet, toward the screen centre.
        bubble.x = if (atEnd) disc.x - bw - context.dp(8f) else disc.x + sizePx + context.dp(8f)
        // Bounds can still be empty on the first insets pass; never coerce into an inverted range.
        val minY = bounds.top.toFloat()
        bubble.y = (disc.y + sizePx / 2f - bh / 2f).coerceIn(minY, (bounds.bottom - bh).toFloat().coerceAtLeast(minY))
        bubble.pivotX = if (atEnd) bw.toFloat() else 0f
        bubble.pivotY = bh / 2f
    }

    /** Centre of the pet in root coordinates (XP fly target). */
    fun target(): Pair<Float, Float> = (disc.x + sizePx / 2f) to (disc.y + sizePx / 2f)
    val isShowingPet get() = visibility == VISIBLE

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        if (changed) place(animate = false)
    }

    // The container itself never intercepts touches outside the pet / bubble.
    override fun onTouchEvent(event: MotionEvent?) = false

    init { layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT); foregroundGravity = Gravity.NO_GRAVITY }
}
