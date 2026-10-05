package com.yourbrand.englishlearn.ui.views

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.ui.col
import com.yourbrand.englishlearn.ui.dp
import com.yourbrand.englishlearn.ui.reduceMotion
import com.yourbrand.englishlearn.ui.sp

/** Goal / accuracy ring, animates from the previous value (500 ms decelerate). */
class ProgressRing(context: Context) : View(context) {
    var stroke = context.dp(10f)
    var trackColor = context.col(R.color.surface_variant)
    var color = context.col(R.color.primary)
    var progress = 0f
        private set
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeCap = Paint.Cap.ROUND }
    private val rect = RectF()

    fun set(value: Float, animate: Boolean = true) {
        val target = value.coerceIn(0f, 1f)
        if (!animate || context.reduceMotion) { progress = target; invalidate(); return }
        ValueAnimator.ofFloat(progress, target).apply {
            duration = 500; interpolator = DecelerateInterpolator()
            addUpdateListener { progress = it.animatedValue as Float; invalidate() }
        }.start()
    }

    override fun onDraw(canvas: Canvas) {
        val s = stroke / 2
        rect.set(s, s, width - s, height - s)
        paint.strokeWidth = stroke
        paint.color = trackColor
        canvas.drawArc(rect, 0f, 360f, false, paint)
        if (progress > 0f) {
            paint.color = color
            canvas.drawArc(rect, -90f, 360f * progress, false, paint)
        }
    }
}

/** Thin rounded progress bar. */
class Bar(context: Context) : View(context) {
    var color = context.col(R.color.primary)
    var trackColor = context.col(R.color.surface_variant)
    private var value = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()

    fun set(v: Float, animate: Boolean = true) {
        val target = v.coerceIn(0f, 1f)
        if (!animate || context.reduceMotion) { value = target; invalidate(); return }
        ValueAnimator.ofFloat(value, target).apply {
            duration = 450; interpolator = DecelerateInterpolator()
            addUpdateListener { value = it.animatedValue as Float; invalidate() }
        }.start()
    }

    override fun onDraw(canvas: Canvas) {
        val r = height / 2f
        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        paint.color = trackColor
        canvas.drawRoundRect(rect, r, r, paint)
        if (value > 0f) {
            rect.right = (width * value).coerceAtLeast(height.toFloat())
            paint.color = color
            canvas.drawRoundRect(rect, r, r, paint)
        }
    }
}

/** One segment per question; fills smoothly (200 ms), the current segment pulses subtly. */
class SegmentedProgress(context: Context) : View(context) {
    private var states: IntArray = IntArray(0) // 0 pending, 1 correct, 2 wrong
    private var current = 0
    private var pulse = 0f
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()
    private val pending = context.col(R.color.surface_variant)
    private val ok = context.col(R.color.success)
    private val bad = context.col(R.color.error)
    private val cur = context.col(R.color.primary)
    private var anim: ValueAnimator? = null

    fun setup(count: Int) { states = IntArray(count); current = 0; invalidate(); startPulse() }

    fun mark(index: Int, correct: Boolean) { if (index in states.indices) states[index] = if (correct) 1 else 2; invalidate() }

    fun setCurrent(i: Int) { current = i; invalidate() }

    fun grow(count: Int) { if (count > states.size) { states = states.copyOf(count); invalidate() } }

    private fun startPulse() {
        if (context.reduceMotion || anim != null) return
        anim = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 1200; repeatCount = ValueAnimator.INFINITE; repeatMode = ValueAnimator.REVERSE
            addUpdateListener { pulse = it.animatedValue as Float; invalidate() }
            start()
        }
    }

    override fun onDetachedFromWindow() { anim?.cancel(); anim = null; super.onDetachedFromWindow() }
    override fun onAttachedToWindow() { super.onAttachedToWindow(); if (states.isNotEmpty()) startPulse() }

    override fun onDraw(canvas: Canvas) {
        val n = states.size.coerceAtLeast(1)
        val gap = context.dp(3f)
        val w = (width - gap * (n - 1)) / n
        val r = height / 2f
        for (i in 0 until n) {
            val x = i * (w + gap)
            rect.set(x, 0f, x + w, height.toFloat())
            paint.color = when { i < states.size && states[i] == 1 -> ok; i < states.size && states[i] == 2 -> bad; i == current -> cur; else -> pending }
            paint.alpha = if (i == current && states.getOrElse(i) { 0 } == 0) (150 + 105 * pulse).toInt() else 255
            canvas.drawRoundRect(rect, r, r, paint)
        }
    }
}

/** 14-day bars (XP). Tap a bar to show its value. */
class DayBars(context: Context) : View(context) {
    private var values = IntArray(0)
    private var labels = emptyList<String>()
    private var selected = -1
    var goal = 0
    var onSelect: ((Int) -> Unit)? = null
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val text = Paint(Paint.ANTI_ALIAS_FLAG).apply { textSize = context.sp(10f); color = context.col(R.color.muted); textAlign = Paint.Align.CENTER }
    private val rect = RectF()
    private var reveal = 1f

    fun set(v: IntArray, l: List<String>) {
        values = v; labels = l; selected = v.lastIndex
        if (context.reduceMotion) { reveal = 1f; invalidate(); return }
        ValueAnimator.ofFloat(0f, 1f).apply { duration = 500; interpolator = DecelerateInterpolator(); addUpdateListener { reveal = it.animatedValue as Float; invalidate() } }.start()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        if (e.action == MotionEvent.ACTION_UP && values.isNotEmpty()) {
            selected = (e.x / (width / values.size.toFloat())).toInt().coerceIn(0, values.lastIndex)
            onSelect?.invoke(selected)
            invalidate()
            performClick()
        }
        return true
    }

    override fun performClick(): Boolean { super.performClick(); return true }

    override fun onDraw(canvas: Canvas) {
        if (values.isEmpty()) return
        val labelH = context.sp(16f)
        val chartH = height - labelH
        val max = maxOf(values.maxOrNull() ?: 0, goal, 10).toFloat()
        val slot = width / values.size.toFloat()
        val bw = (slot * 0.56f).coerceAtMost(context.dp(16f))
        values.forEachIndexed { i, v ->
            val cx = slot * i + slot / 2
            rect.set(cx - bw / 2, 0f, cx + bw / 2, chartH)
            paint.color = context.col(R.color.surface_variant)
            canvas.drawRoundRect(rect, bw / 2, bw / 2, paint)
            val h = chartH * (v / max) * reveal
            if (h > 0) {
                rect.set(cx - bw / 2, chartH - h, cx + bw / 2, chartH)
                paint.color = context.col(if (i == selected) R.color.accent else R.color.primary)
                canvas.drawRoundRect(rect, bw / 2, bw / 2, paint)
            }
            if (i % 2 == values.lastIndex % 2) canvas.drawText(labels.getOrElse(i) { "" }, cx, height - context.dp(2f), text)
        }
        if (goal > 0) {
            val y = chartH * (1 - goal / max)
            paint.color = context.col(R.color.accent); paint.strokeWidth = context.dp(1.5f); paint.alpha = 160
            canvas.drawLine(0f, y, width.toFloat(), y, paint)
            paint.alpha = 255
        }
    }
}

/** 8-week activity heatmap (7 rows × 8 columns). */
class Heatmap(context: Context) : View(context) {
    private var cells = IntArray(56)
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val rect = RectF()

    /** [values] oldest → newest, 56 days. */
    fun set(values: IntArray) { cells = values; invalidate() }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val cell = (w - context.dp(4f) * 7) / 8f
        setMeasuredDimension(w, (cell * 7 + context.dp(4f) * 6).toInt())
    }

    override fun onDraw(canvas: Canvas) {
        val gap = context.dp(4f)
        val cell = (width - gap * 7) / 8f
        val max = (cells.maxOrNull() ?: 0).coerceAtLeast(1)
        val base = context.col(R.color.primary)
        for (i in cells.indices) {
            val col = i / 7; val row = i % 7
            rect.set(col * (cell + gap), row * (cell + gap), col * (cell + gap) + cell, row * (cell + gap) + cell)
            val v = cells[i]
            paint.color = if (v == 0) context.col(R.color.surface_variant) else base
            paint.alpha = if (v == 0) 255 else (70 + 185 * v / max).coerceAtMost(255)
            canvas.drawRoundRect(rect, cell * 0.25f, cell * 0.25f, paint)
        }
    }
}
