package com.yourbrand.englishlearn.ui.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.view.Choreographer
import android.view.View
import com.yourbrand.englishlearn.ui.dp
import com.yourbrand.englishlearn.ui.reduceMotion
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

/**
 * One shared particle system (confetti, sparkles, hearts). Object pool of ≤ 60 particles, no per-frame
 * allocation; runs on Choreographer only while particles are alive.
 */
class ParticleView(context: Context) : View(context), Choreographer.FrameCallback {
    private class P {
        var alive = false
        var x = 0f; var y = 0f; var vx = 0f; var vy = 0f
        var rot = 0f; var vr = 0f; var size = 0f; var life = 0f; var maxLife = 1f
        var color = 0; var shape = 0
    }

    private val pool = Array(60) { P() }
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val star = Path()
    private val heart = Path()
    private var running = false
    private var lastNs = 0L
    private val colors = intArrayOf(0xFF12857A.toInt(), 0xFFF5A623.toInt(), 0xFFD6487E.toInt(), 0xFF3B7DD8.toInt(), 0xFF2E9E5B.toInt(), 0xFF8A5CD6.toInt())

    init {
        isClickable = false
        // Unit star and heart paths (scaled when drawing).
        for (i in 0 until 10) {
            val r = if (i % 2 == 0) 1f else 0.45f
            val a = Math.PI / 5 * i - Math.PI / 2
            val x = (cos(a) * r).toFloat(); val y = (sin(a) * r).toFloat()
            if (i == 0) star.moveTo(x, y) else star.lineTo(x, y)
        }
        star.close()
        heart.moveTo(0f, 0.35f)
        heart.cubicTo(-1f, -0.35f, -0.45f, -1.05f, 0f, -0.45f)
        heart.cubicTo(0.45f, -1.05f, 1f, -0.35f, 0f, 0.35f)
        heart.close()
    }

    private fun spawn(init: P.() -> Unit) {
        val p = pool.firstOrNull { !it.alive } ?: return
        p.alive = true; p.life = 0f; p.rot = 0f
        p.init()
    }

    /** Confetti burst from the top of the view (result ≥ 80 %, stage up). */
    fun confetti(count: Int = 60) {
        if (context.reduceMotion) return
        val w = width.toFloat().coerceAtLeast(1f)
        repeat(count.coerceAtMost(60)) {
            spawn {
                x = Random.nextFloat() * w; y = -context.dp(10f) - Random.nextFloat() * context.dp(80f)
                vx = (Random.nextFloat() - 0.5f) * context.dp(120f); vy = context.dp(160f) + Random.nextFloat() * context.dp(200f)
                vr = (Random.nextFloat() - 0.5f) * 720f; size = context.dp(5f) + Random.nextFloat() * context.dp(5f)
                maxLife = 2.2f; color = colors[Random.nextInt(colors.size)]; shape = Random.nextInt(3)
            }
        }
        start()
    }

    /** Small sparkle / heart burst at (cx, cy). shape 2 = star, 3 = heart. */
    fun burst(cx: Float, cy: Float, count: Int = 12, shapeKind: Int = 2, color: Int? = null) {
        if (context.reduceMotion) return
        repeat(count) { i ->
            val a = (Math.PI * 2 * i / count + Random.nextDouble() * 0.4).toFloat()
            val speed = context.dp(90f) + Random.nextFloat() * context.dp(80f)
            spawn {
                x = cx; y = cy; vx = cos(a) * speed; vy = sin(a) * speed - context.dp(40f)
                vr = (Random.nextFloat() - 0.5f) * 360f; size = context.dp(5f) + Random.nextFloat() * context.dp(4f)
                maxLife = 0.8f; this.color = color ?: colors[Random.nextInt(colors.size)]; shape = shapeKind
            }
        }
        start()
    }

    private fun start() {
        if (running) return
        running = true
        lastNs = 0L
        Choreographer.getInstance().postFrameCallback(this)
    }

    override fun doFrame(frameTimeNanos: Long) {
        val dt = if (lastNs == 0L) 0.016f else ((frameTimeNanos - lastNs) / 1e9f).coerceAtMost(0.05f)
        lastNs = frameTimeNanos
        var any = false
        val g = context.dp(260f)
        for (p in pool) {
            if (!p.alive) continue
            p.life += dt
            if (p.life >= p.maxLife || p.y > height + context.dp(30f)) { p.alive = false; continue }
            any = true
            p.vy += g * dt * (if (p.shape >= 2) 0.6f else 0.35f)
            p.vx *= 0.99f
            p.x += p.vx * dt; p.y += p.vy * dt; p.rot += p.vr * dt
        }
        invalidate()
        if (any && isAttachedToWindow) Choreographer.getInstance().postFrameCallback(this) else running = false
    }

    override fun onDetachedFromWindow() {
        Choreographer.getInstance().removeFrameCallback(this)
        running = false
        pool.forEach { it.alive = false }
        super.onDetachedFromWindow()
    }

    override fun onDraw(canvas: Canvas) {
        for (p in pool) {
            if (!p.alive) continue
            val fade = (1f - (p.life / p.maxLife)).coerceIn(0f, 1f)
            paint.color = p.color
            paint.alpha = (255 * minOf(1f, fade * 2f)).toInt()
            canvas.save()
            canvas.translate(p.x, p.y)
            canvas.rotate(p.rot)
            when (p.shape) {
                0 -> canvas.drawRect(-p.size / 2, -p.size / 4, p.size / 2, p.size / 4, paint)
                1 -> canvas.drawCircle(0f, 0f, p.size / 2.5f, paint)
                2 -> { canvas.scale(p.size, p.size); canvas.drawPath(star, paint) }
                else -> { canvas.scale(p.size * 1.2f, p.size * 1.2f); paint.color = Color.argb(paint.alpha, 0xF0, 0x5A, 0x7E); canvas.drawPath(heart, paint) }
            }
            canvas.restore()
        }
    }
}
