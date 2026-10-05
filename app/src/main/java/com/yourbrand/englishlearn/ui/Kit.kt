package com.yourbrand.englishlearn.ui

import android.app.Dialog
import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StyleRes
import androidx.core.widget.TextViewCompat
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R

fun lp(w: Int = MATCH_PARENT, h: Int = WRAP_CONTENT, weight: Float = 0f) = LinearLayout.LayoutParams(w, h, weight)

fun <T : View> T.margins(ctx: Context, top: Int = 0, bottom: Int = 0, start: Int = 0, end: Int = 0): T {
    val p = (layoutParams as? ViewGroup.MarginLayoutParams) ?: lp()
    p.topMargin = ctx.dpi(top); p.bottomMargin = ctx.dpi(bottom); p.marginStart = ctx.dpi(start); p.marginEnd = ctx.dpi(end)
    layoutParams = p
    return this
}

object Kit {
    fun vbox(ctx: Context, padDp: Int = 0, block: LinearLayout.() -> Unit = {}): LinearLayout = LinearLayout(ctx).apply {
        orientation = LinearLayout.VERTICAL
        val p = ctx.dpi(padDp); setPadding(p, p, p, p)
        layoutParams = lp()
        block()
    }

    fun hbox(ctx: Context, block: LinearLayout.() -> Unit = {}): LinearLayout = LinearLayout(ctx).apply {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        layoutParams = lp()
        block()
    }

    fun text(ctx: Context, value: CharSequence?, @StyleRes style: Int = R.style.Text_Body, color: Int? = null, sizeSp: Float? = null, bold: Boolean = false): TextView =
        TextView(ctx).apply {
            TextViewCompat.setTextAppearance(this, style)
            text = value
            color?.let { setTextColor(it) }
            sizeSp?.let { textSize = it }
            if (bold) setTypeface(typeface, Typeface.BOLD)
            layoutParams = lp()
        }

    fun icon(ctx: Context, @DrawableRes res: Int, tint: Int, sizeDp: Int = 24): ImageView = ImageView(ctx).apply {
        setImageResource(res)
        tint(tint)
        layoutParams = LinearLayout.LayoutParams(ctx.dpi(sizeDp), ctx.dpi(sizeDp))
        importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
    }

    /** Emoji in a soft rounded tile (topic icons, decor items). */
    fun emojiTile(ctx: Context, emoji: String, bg: Int, sizeDp: Int = 44, textSp: Float = 22f): TextView = TextView(ctx).apply {
        text = emoji
        textSize = textSp
        gravity = Gravity.CENTER
        background = ctx.rounded(bg, sizeDp * 0.32f)
        layoutParams = LinearLayout.LayoutParams(ctx.dpi(sizeDp), ctx.dpi(sizeDp))
        importantForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO
    }

    fun card(ctx: Context, padDp: Int = 16, topDp: Int = 12, fill: Int = ctx.col(R.color.surface), stroke: Int? = ctx.col(R.color.outline), block: LinearLayout.() -> Unit = {}): LinearLayout =
        vbox(ctx, padDp).apply {
            background = ctx.rounded(fill, 16f, stroke)
            layoutParams = lp().apply { topMargin = ctx.dpi(topDp) }
            block()
        }

    fun clickableCard(ctx: Context, padDp: Int = 16, topDp: Int = 12, fill: Int = ctx.col(R.color.surface), onClick: () -> Unit, block: LinearLayout.() -> Unit = {}): LinearLayout =
        card(ctx, padDp, topDp, fill).apply {
            foreground = ctx.rounded(0, 16f, ripple = true)
            isClickable = true; isFocusable = true
            onTap { onClick() }
            block()
        }

    fun primary(ctx: Context, label: String, topDp: Int = 16, onClick: () -> Unit): TextView = TextView(ctx).apply {
        TextViewCompat.setTextAppearance(this, R.style.Button_Primary)
        setBackgroundResource(R.drawable.btn_primary)
        setTextColor(ctx.getColorStateList(R.color.btn_primary_text))
        gravity = Gravity.CENTER
        text = label
        minHeight = ctx.dpi(52)
        setPadding(ctx.dpi(20), 0, ctx.dpi(20), 0)
        isClickable = true; isFocusable = true
        layoutParams = lp(h = ctx.dpi(52)).apply { topMargin = ctx.dpi(topDp) }
        onTap { if (isEnabled) onClick() }
    }

    fun secondary(ctx: Context, label: String, topDp: Int = 10, onClick: () -> Unit): TextView = TextView(ctx).apply {
        TextViewCompat.setTextAppearance(this, R.style.Button_Secondary)
        setBackgroundResource(R.drawable.btn_secondary)
        setTextColor(ctx.col(R.color.on_surface))
        gravity = Gravity.CENTER
        text = label
        setPadding(ctx.dpi(16), 0, ctx.dpi(16), 0)
        isClickable = true; isFocusable = true
        layoutParams = lp(h = ctx.dpi(52)).apply { topMargin = ctx.dpi(topDp) }
        onTap { onClick() }
    }

    /** Small rounded pill button / tag chip. */
    fun chip(ctx: Context, label: String, selected: Boolean = false, color: Int = ctx.col(R.color.primary), onClick: ((TextView) -> Unit)? = null): TextView =
        TextView(ctx).apply {
            text = label
            textSize = 14f
            gravity = Gravity.CENTER
            minHeight = ctx.dpi(36)
            setPadding(ctx.dpi(14), ctx.dpi(6), ctx.dpi(14), ctx.dpi(6))
            styleChip(this, selected, color)
            layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT).apply { marginEnd = ctx.dpi(8); bottomMargin = ctx.dpi(8) }
            if (onClick != null) { isClickable = true; onTap { onClick(this) } }
        }

    fun styleChip(tv: TextView, selected: Boolean, color: Int = tv.context.col(R.color.primary)) {
        val ctx = tv.context
        tv.isSelected = selected
        tv.background = if (selected) ctx.rounded(blend(color, ctx.col(R.color.surface), 0.82f), 100f, color, 1.5f, ripple = true)
        else ctx.rounded(ctx.col(R.color.surface), 100f, ctx.col(R.color.outline), 1f, ripple = true)
        tv.setTextColor(if (selected) color else ctx.col(R.color.on_surface))
        tv.typeface = Typeface.create("sans-serif-medium", if (selected) Typeface.BOLD else Typeface.NORMAL)
    }

    /** Wrapping row of chips. */
    fun flow(ctx: Context): FlowLayout = FlowLayout(ctx).apply { layoutParams = lp() }

    fun hscroll(ctx: Context, content: LinearLayout): HorizontalScrollView = HorizontalScrollView(ctx).apply {
        isHorizontalScrollBarEnabled = false
        clipToPadding = false
        overScrollMode = View.OVER_SCROLL_NEVER
        addView(content)
        layoutParams = lp()
    }

    fun section(ctx: Context, title: String, topDp: Int = 24, action: String? = null, onAction: (() -> Unit)? = null): LinearLayout =
        hbox(ctx) {
            layoutParams = lp().apply { topMargin = ctx.dpi(topDp); bottomMargin = ctx.dpi(4) }
            addView(text(ctx, title, R.style.Text_Section).apply {
                layoutParams = lp(0, WRAP_CONTENT, 1f)
                if (android.os.Build.VERSION.SDK_INT >= 28) isAccessibilityHeading = true
            })
            if (action != null) addView(text(ctx, action, R.style.Text_BodyStrong, ctx.col(R.color.primary), 14f).apply {
                layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
                setPadding(ctx.dpi(8), ctx.dpi(8), 0, ctx.dpi(8))
                onTap { onAction?.invoke() }
            })
        }

    /** Row with leading emoji/icon, title, subtitle and trailing view. */
    fun row(ctx: Context, lead: View?, title: CharSequence, subtitle: CharSequence? = null, trailing: View? = null, onClick: (() -> Unit)? = null): LinearLayout =
        hbox(ctx) {
            minimumHeight = ctx.dpi(56)
            setPadding(ctx.dpi(12), ctx.dpi(10), ctx.dpi(12), ctx.dpi(10))
            if (onClick != null) { background = ctx.rounded(0, 12f, ripple = true); isClickable = true; onTap { onClick() } }
            lead?.let { addView(it) }
            addView(vbox(ctx) {
                layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = if (lead != null) ctx.dpi(14) else 0 }
                addView(text(ctx, title, R.style.Text_BodyStrong))
                if (subtitle != null) addView(text(ctx, subtitle, R.style.Text_Caption).margins(ctx, top = 2))
            })
            trailing?.let { addView(it) }
        }

    fun chevron(ctx: Context) = icon(ctx, R.drawable.ic_chevron_right, ctx.col(R.color.muted), 22)

    fun divider(ctx: Context): View = View(ctx).apply {
        setBackgroundColor(ctx.col(R.color.outline))
        layoutParams = lp(h = 1).apply { marginStart = ctx.dpi(12); marginEnd = ctx.dpi(12) }
    }

    fun spacer(ctx: Context, dp: Int): View = View(ctx).apply { layoutParams = lp(h = ctx.dpi(dp)) }

    /** EmptyState: illustration (emoji) + one line + one action. */
    fun empty(ctx: Context, emoji: String, line: String, action: String?, onAction: (() -> Unit)?): LinearLayout = vbox(ctx, 24) {
        gravity = Gravity.CENTER_HORIZONTAL
        addView(text(ctx, emoji, sizeSp = 44f).apply { gravity = Gravity.CENTER })
        addView(text(ctx, line, R.style.Text_Body, ctx.col(R.color.muted)).apply { gravity = Gravity.CENTER }.margins(ctx, top = 8))
        if (action != null && onAction != null) addView(primary(ctx, action) { onAction() }.apply { layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, ctx.dpi(48)).apply { topMargin = ctx.dpi(16) } })
    }

    /** Count badge "12" etc. */
    fun badge(ctx: Context, value: String, fill: Int, textColor: Int): TextView = TextView(ctx).apply {
        text = value
        textSize = 12f
        setTypeface(typeface, Typeface.BOLD)
        setTextColor(textColor)
        gravity = Gravity.CENTER
        minWidth = ctx.dpi(24)
        setPadding(ctx.dpi(8), ctx.dpi(2), ctx.dpi(8), ctx.dpi(2))
        background = ctx.rounded(fill, 100f)
        layoutParams = LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    fun ellipsize(tv: TextView, lines: Int = 1): TextView { tv.maxLines = lines; tv.ellipsize = TextUtils.TruncateAt.END; return tv }
}

/** Simple wrapping layout for chips (no extra dependency). */
class FlowLayout(context: Context) : ViewGroup(context) {
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val maxW = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        var x = 0; var y = 0; var rowH = 0
        for (i in 0 until childCount) {
            val c = getChildAt(i)
            if (c.visibility == GONE) continue
            measureChildWithMargins(c, widthMeasureSpec, 0, heightMeasureSpec, 0)
            val p = c.layoutParams as MarginLayoutParams
            val w = c.measuredWidth + p.leftMargin + p.rightMargin
            val h = c.measuredHeight + p.topMargin + p.bottomMargin
            if (x + w > maxW && x > 0) { x = 0; y += rowH; rowH = 0 }
            x += w; rowH = maxOf(rowH, h)
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), y + rowH + paddingTop + paddingBottom)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val maxW = r - l - paddingLeft - paddingRight
        val rtl = layoutDirection == LAYOUT_DIRECTION_RTL
        var x = 0; var y = 0; var rowH = 0
        for (i in 0 until childCount) {
            val c = getChildAt(i)
            if (c.visibility == GONE) continue
            val p = c.layoutParams as MarginLayoutParams
            val w = c.measuredWidth + p.leftMargin + p.rightMargin
            val h = c.measuredHeight + p.topMargin + p.bottomMargin
            if (x + w > maxW && x > 0) { x = 0; y += rowH; rowH = 0 }
            val left = if (rtl) r - l - paddingRight - x - w + p.leftMargin else paddingLeft + x + p.leftMargin
            c.layout(left, paddingTop + y + p.topMargin, left + c.measuredWidth, paddingTop + y + p.topMargin + c.measuredHeight)
            x += w; rowH = maxOf(rowH, h)
        }
    }

    override fun generateLayoutParams(attrs: android.util.AttributeSet?) = MarginLayoutParams(context, attrs)
    override fun generateDefaultLayoutParams() = MarginLayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    override fun generateLayoutParams(p: LayoutParams?) = MarginLayoutParams(p)
    override fun checkLayoutParams(p: LayoutParams?) = p is MarginLayoutParams
}

/**
 * Scrolling screen whose body is rebuilt in [build] whenever it becomes visible (fresh data).
 * Header: large title (tab roots) or a back bar (pushed screens), optional trailing actions.
 */
abstract class ScrollScreen(activity: MainActivity) : Screen(activity) {
    protected lateinit var scroll: ScrollView
    protected lateinit var body: LinearLayout
    protected lateinit var header: LinearLayout
    protected lateinit var root: LinearLayout
    protected var footer: LinearLayout? = null

    /** Non-null shows a top bar with a back arrow (pushed screens). */
    protected open val barTitle: String? = null
    /** Bottom padding so the floating pet never covers the last item. */
    protected open val bottomPadDp: Int = 96

    override fun onCreateView(parent: ViewGroup): View {
        val c = ctx
        root = Kit.vbox(c).apply { layoutParams = ViewGroup.LayoutParams(MATCH_PARENT, MATCH_PARENT) }
        header = Kit.hbox(c) { setPadding(c.dpi(4), c.dpi(4), c.dpi(8), 0); minimumHeight = c.dpi(56) }
        root.addView(header)
        scroll = ScrollView(c).apply {
            isFillViewport = true
            clipToPadding = false
            overScrollMode = View.OVER_SCROLL_IF_CONTENT_SCROLLS
            layoutParams = lp(MATCH_PARENT, 0, 1f)
        }
        val center = FrameLayout(c)
        body = Kit.vbox(c).apply {
            val pad = c.dpi(16)
            setPadding(pad, c.dpi(4), pad, c.dpi(bottomPadDp))
            layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.CENTER_HORIZONTAL)
        }
        center.addView(body)
        // Tablets: centered content column, max 640dp (SKILL.md 4.3).
        center.addOnLayoutChangeListener { v, l, _, r, _, _, _, _, _ ->
            val maxW = c.dpi(640)
            val w = r - l
            val lpB = body.layoutParams as FrameLayout.LayoutParams
            val want = if (w > maxW) maxW else MATCH_PARENT
            if (lpB.width != want) { lpB.width = want; v.post { body.layoutParams = lpB } }
        }
        scroll.addView(center)
        root.addView(scroll)
        buildHeader()
        return root
    }

    protected open fun buildHeader() {
        val c = ctx
        val title = barTitle
        if (title != null) {
            header.addView(ImageView(c).apply {
                setImageResource(com.yourbrand.englishlearn.R.drawable.ic_arrow_back)
                tintRes(com.yourbrand.englishlearn.R.color.on_surface)
                setBackgroundResource(com.yourbrand.englishlearn.R.drawable.ripple_circle)
                val p = c.dpi(12); setPadding(p, p, p, p)
                contentDescription = c.getString(com.yourbrand.englishlearn.R.string.back)
                layoutParams = LinearLayout.LayoutParams(c.dpi(48), c.dpi(48))
                setOnClickListener { activity.onBackPressedDispatcher.onBackPressed() }
            })
            header.addView(Kit.ellipsize(Kit.text(c, title, com.yourbrand.englishlearn.R.style.Text_Title)).apply {
                layoutParams = lp(0, WRAP_CONTENT, 1f).apply { marginStart = c.dpi(4) }
            })
        }
        headerActions(header)
    }

    protected open fun headerActions(bar: LinearLayout) {}

    override fun onShown(firstTime: Boolean) {
        val y = scroll.scrollY
        body.removeAllViews()
        build(body)
        if (firstTime) { body.staggerChildren(40); scroll.post { scroll.scrollTo(0, 0) } } else scroll.post { scroll.scrollTo(0, y) }
    }

    protected abstract fun build(body: LinearLayout)

    fun scrollToTop() = scroll.smoothScrollTo(0, 0)

    /** Re-renders without the entrance animation (after a filter change). */
    fun refresh() {
        val y = scroll.scrollY
        body.removeAllViews()
        build(body)
        scroll.post { scroll.scrollTo(0, y) }
    }
}

object Dialogs {
    fun confirm(ctx: Context, title: String, message: String, positive: String, negative: String? = ctx.getString(R.string.cancel),
                destructive: Boolean = false, onNegative: () -> Unit = {}, onPositive: () -> Unit) {
        val d = Dialog(ctx, R.style.Dialog)
        val box = Kit.vbox(ctx, 24).apply { background = ctx.getDrawable(R.drawable.bg_dialog) }
        box.addView(Kit.text(ctx, title, R.style.Text_Title))
        box.addView(Kit.text(ctx, message, R.style.Text_Body, ctx.col(R.color.muted)).margins(ctx, top = 10))
        box.addView(Kit.primary(ctx, positive, 24) { d.dismiss(); onPositive() }.apply {
            if (destructive) background = ctx.rounded(ctx.col(R.color.error), 14f, ripple = true)
        })
        if (negative != null) box.addView(Kit.secondary(ctx, negative) { d.dismiss(); onNegative() })
        d.setContentView(box)
        d.window?.setLayout(minOf(ctx.resources.displayMetrics.widthPixels - ctx.dpi(48), ctx.dpi(420)), WRAP_CONTENT)
        d.show()
    }

    /** Free-form dialog body. */
    fun custom(ctx: Context, build: (LinearLayout, Dialog) -> Unit): Dialog {
        val d = Dialog(ctx, R.style.Dialog)
        val box = Kit.vbox(ctx, 24).apply { background = ctx.getDrawable(R.drawable.bg_dialog) }
        build(box, d)
        d.setContentView(ScrollView(ctx).apply { addView(box) })
        d.window?.setLayout(minOf(ctx.resources.displayMetrics.widthPixels - ctx.dpi(48), ctx.dpi(420)), WRAP_CONTENT)
        d.show()
        return d
    }
}

/**
 * Bottom sheet on the activity overlay: draggable down to dismiss, scrim 40 %, 220 ms decelerate.
 */
class BottomSheet(private val activity: MainActivity) {
    private var scrim: View? = null
    private var panel: LinearLayout? = null
    var onDismiss: (() -> Unit)? = null
    val isShowing get() = panel != null

    fun show(build: (LinearLayout) -> Unit): BottomSheet {
        val c = activity
        val overlay = activity.overlay
        val s = View(c).apply {
            setBackgroundColor(c.col(R.color.scrim))
            alpha = 0f
            isClickable = true
            setOnClickListener { dismiss() }
        }
        overlay.addView(s, FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT))
        val p = Kit.vbox(c).apply {
            background = c.getDrawable(R.drawable.bg_sheet)
            setPadding(c.dpi(20), c.dpi(10), c.dpi(20), c.dpi(20) + activity.bottomInset)
            isClickable = true
            elevation = c.dp(12f)
        }
        p.addView(View(c).apply {
            background = c.rounded(c.col(R.color.outline), 100f)
            layoutParams = LinearLayout.LayoutParams(c.dpi(40), c.dpi(4)).apply { gravity = Gravity.CENTER_HORIZONTAL; bottomMargin = c.dpi(12) }
        })
        val content = Kit.vbox(c)
        build(content)
        val sc = ScrollView(c).apply { addView(content); isVerticalScrollBarEnabled = false }
        p.addView(sc, lp())
        val maxH = (c.resources.displayMetrics.heightPixels * 0.85f).toInt()
        overlay.addView(p, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT, Gravity.BOTTOM))
        p.post {
            if (p.height > maxH) p.layoutParams = (p.layoutParams as FrameLayout.LayoutParams).apply { height = maxH }
            p.translationY = p.height.toFloat()
            p.animate().translationY(0f).setDuration(220).setInterpolator(android.view.animation.DecelerateInterpolator()).start()
        }
        s.animate().alpha(1f).setDuration(220).start()
        // Drag down to dismiss (on the handle area / panel).
        var startY = 0f
        p.setOnTouchListener { v, e ->
            when (e.actionMasked) {
                android.view.MotionEvent.ACTION_DOWN -> { startY = e.rawY; true }
                android.view.MotionEvent.ACTION_MOVE -> { v.translationY = (e.rawY - startY).coerceAtLeast(0f); true }
                android.view.MotionEvent.ACTION_UP, android.view.MotionEvent.ACTION_CANCEL -> {
                    if (v.translationY > v.height * 0.25f) dismiss() else v.animate().translationY(0f).setDuration(150).start(); true
                }
                else -> false
            }
        }
        scrim = s; panel = p
        activity.sheets += this
        activity.petView.setBlocked(true)
        return this
    }

    fun dismiss() {
        val s = scrim ?: return
        val p = panel ?: return
        scrim = null; panel = null
        activity.sheets -= this
        if (activity.sheets.isEmpty()) activity.petView.setBlocked(false)
        s.animate().alpha(0f).setDuration(180).withEndAction { activity.overlay.removeView(s) }.start()
        p.animate().translationY(p.height.toFloat()).setDuration(180).withEndAction { activity.overlay.removeView(p) }.start()
        onDismiss?.invoke()
    }
}
