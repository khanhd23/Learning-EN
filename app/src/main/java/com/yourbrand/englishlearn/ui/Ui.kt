package com.yourbrand.englishlearn.ui

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.RippleDrawable
import android.provider.Settings as SysSettings
import android.util.TypedValue
import android.view.HapticFeedbackConstants
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.services

fun Context.dp(v: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v, resources.displayMetrics)
fun Context.dpi(v: Int): Int = dp(v.toFloat()).toInt()
fun Context.sp(v: Float): Float = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, v, resources.displayMetrics)
fun Context.col(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

val Context.isNight: Boolean
    get() = (resources.configuration.uiMode and android.content.res.Configuration.UI_MODE_NIGHT_MASK) == android.content.res.Configuration.UI_MODE_NIGHT_YES

/** Reduce motion: system animator scale 0 or the in-app setting (SKILL.md 10). */
val Context.reduceMotion: Boolean
    get() = services.settings.reduceMotion ||
        SysSettings.Global.getFloat(contentResolver, SysSettings.Global.ANIMATOR_DURATION_SCALE, 1f) == 0f

fun ImageView.tintRes(@ColorRes id: Int) { imageTintList = ColorStateList.valueOf(context.col(id)) }
fun ImageView.tint(color: Int) { imageTintList = ColorStateList.valueOf(color) }
fun ImageView.setIcon(@DrawableRes icon: Int, @ColorRes tint: Int) { setImageResource(icon); tintRes(tint) }

fun Context.rounded(fill: Int, radiusDp: Float = 16f, stroke: Int? = null, strokeDp: Float = 1f, ripple: Boolean = false): Drawable {
    val shape = GradientDrawable().apply {
        cornerRadius = dp(radiusDp)
        setColor(fill)
        if (stroke != null) setStroke(dp(strokeDp).toInt().coerceAtLeast(1), stroke)
    }
    return if (ripple) RippleDrawable(ColorStateList.valueOf(col(R.color.ripple)), shape, null) else shape
}

fun Context.gradient(start: Int, end: Int, radiusDp: Float = 24f, angle: GradientDrawable.Orientation = GradientDrawable.Orientation.TL_BR): Drawable =
    GradientDrawable(angle, intArrayOf(start, end)).apply { cornerRadius = dp(radiusDp) }

/** Blends [c] toward [toward] by [t] (0..1). */
fun blend(c: Int, toward: Int, t: Float): Int {
    fun ch(s: Int) = (((c shr s) and 0xFF) + ((((toward shr s) and 0xFF) - ((c shr s) and 0xFF)) * t)).toInt().coerceIn(0, 255)
    return Color.argb(255, ch(16), ch(8), ch(0))
}

fun View.haptic(type: Int = HapticFeedbackConstants.CLOCK_TICK) {
    if (context.services.settings.haptics) performHapticFeedback(type)
}

fun View.hapticResult(success: Boolean) {
    if (!context.services.settings.haptics) return
    val type = if (android.os.Build.VERSION.SDK_INT >= 30) {
        if (success) HapticFeedbackConstants.CONFIRM else HapticFeedbackConstants.REJECT
    } else if (success) HapticFeedbackConstants.VIRTUAL_KEY else HapticFeedbackConstants.LONG_PRESS
    performHapticFeedback(type)
}

fun View.show(visible: Boolean) { visibility = if (visible) View.VISIBLE else View.GONE }

/** Fade + rise entrance (skipped with reduce motion). */
fun View.enter(delay: Long = 0, distanceDp: Float = 12f) {
    if (context.reduceMotion) { alpha = 0f; animate().alpha(1f).setStartDelay(delay).setDuration(100).start(); return }
    alpha = 0f
    translationY = context.dp(distanceDp)
    animate().alpha(1f).translationY(0f).setStartDelay(delay).setDuration(260).setInterpolator(DecelerateInterpolator(1.5f)).start()
}

fun View.pop(from: Float = 0.9f) {
    if (context.reduceMotion) return
    scaleX = from; scaleY = from
    animate().scaleX(1f).scaleY(1f).setDuration(240).setInterpolator(OvershootInterpolator(2.2f)).start()
}

/** Horizontal shake, 3 cycles, 280 ms, 8dp (incorrect answer). */
fun View.shake() {
    if (context.reduceMotion) return
    val a = context.dp(8f)
    ValueAnimator.ofFloat(0f, 1f).apply {
        duration = 280
        addUpdateListener { v ->
            val t = v.animatedFraction
            translationX = (kotlin.math.sin(t * Math.PI * 6) * a * (1 - t)).toFloat()
        }
        start()
    }
}

/** Soft 1.0 → 1.04 → 1.0 bump (correct answer). */
fun View.bump() {
    if (context.reduceMotion) return
    animate().scaleX(1.04f).scaleY(1.04f).setDuration(110).withEndAction {
        animate().scaleX(1f).scaleY(1f).setDuration(140).setInterpolator(OvershootInterpolator(3f)).start()
    }.start()
}

/** Press feedback: scale 0.97 for 80 ms (SKILL.md 4.4). */
fun View.pressable() {
    setOnTouchListener { v, e ->
        when (e.actionMasked) {
            MotionEvent.ACTION_DOWN -> if (v.isEnabled) v.animate().scaleX(0.97f).scaleY(0.97f).setDuration(80).start()
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> v.animate().scaleX(1f).scaleY(1f).setDuration(120).start()
        }
        false
    }
}

fun View.onTap(action: (View) -> Unit) {
    pressable()
    setOnClickListener { it.haptic(); action(it) }
}

fun ViewGroup.staggerChildren(step: Long = 40, max: Int = 8) {
    for (i in 0 until childCount) {
        val child = getChildAt(i)
        if (i < max) child.enter(delay = i * step) else child.alpha = 1f
    }
}

/** Topic colours: 10 curated hues with a container variant for light/dark (SKILL.md 4.1). */
object Hues {
    private val main = intArrayOf(0xFF12857A.toInt(), 0xFFE07A1F.toInt(), 0xFF3B7DD8.toInt(), 0xFF8A5CD6.toInt(), 0xFFD6487E.toInt(),
        0xFF2E9E5B.toInt(), 0xFFC98A0B.toInt(), 0xFFD64545.toInt(), 0xFF4A5BD4.toInt(), 0xFFA0673A.toInt())

    fun color(ctx: Context, hue: Int): Int {
        val c = main[Math.floorMod(hue, main.size)]
        return if (ctx.isNight) blend(c, Color.WHITE, 0.35f) else c
    }

    fun container(ctx: Context, hue: Int): Int {
        val c = main[Math.floorMod(hue, main.size)]
        return if (ctx.isNight) blend(c, 0xFF1E1B17.toInt(), 0.72f) else blend(c, Color.WHITE, 0.87f)
    }
}
