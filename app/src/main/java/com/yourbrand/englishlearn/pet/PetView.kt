package com.yourbrand.englishlearn.pet

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RadialGradient
import android.graphics.RectF
import android.graphics.Shader
import android.view.Choreographer
import android.view.View
import com.yourbrand.englishlearn.ui.reduceMotion
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * The pet, drawn entirely with Canvas (original design, no image assets).
 * - 3 species (cat, dog, dragon), 10 growth stages (size, proportions and features change)
 * - mood faces (idle, hi, hungry, starving, full, sleepy, sleeping, dozing, celebrate, cheer, oops, thinking)
 * - accessories from the pet shop (hat, glasses, neck)
 * - idle animation: breathing (2.4 s loop) + blink every 3–5 s; reactions: jump, tilt, wiggle.
 * Animations pause while the view is not visible or reduce-motion is on.
 */
class PetView(context: Context) : View(context), Choreographer.FrameCallback {
    var species = "cat"; set(v) { field = v; invalidate() }
    var stage = 1; set(v) { field = v.coerceIn(1, 10); invalidate() }
    var mood = Mood.IDLE; set(v) { field = v; invalidate() }
    /** Head-and-shoulders crop for the floating icon (legible at 48–72dp). */
    var crop = false; set(v) { field = v; invalidate() }
    var hat: String? = null; set(v) { field = v; invalidate() }
    var face: String? = null; set(v) { field = v; invalidate() }
    var neck: String? = null; set(v) { field = v; invalidate() }
    /** Draw a silhouette (locked gallery entries). */
    var silhouette = false; set(v) { field = v; invalidate() }
    /** Shadow under the feet (off for the floating icon). */
    var shadow = true

    private val p = Paint(Paint.ANTI_ALIAS_FLAG)
    private val stroke = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeCap = Paint.Cap.ROUND; strokeJoin = Paint.Join.ROUND }
    private val path = Path()
    private val rect = RectF()

    // Animation state
    private var t = 0f
    private var lastNs = 0L
    private var running = false
    private var nextBlink = 2f
    private var blinkUntil = 0f
    private var jumpStart = -10f
    private var tiltStart = -10f
    private var wiggleStart = -10f
    private var chewStart = -10f

    fun setFrom(state: PetState) {
        species = state.species
        stage = state.stage
        hat = state.equipped[PetItems.HAT]
        face = state.equipped[PetItems.FACE]
        neck = state.equipped[PetItems.NECK]
    }

    /** Short reaction animations (CHEER jump 250 ms, OOPS tilt 200 ms, pet wiggle, chewing). */
    fun react(kind: Mood) {
        when (kind) {
            Mood.CHEER, Mood.CELEBRATE, Mood.HI -> jumpStart = t
            Mood.OOPS -> tiltStart = t
            Mood.THINKING -> wiggleStart = t
            else -> {}
        }
        ensureRunning()
        invalidate()
    }

    fun chew() { chewStart = t; ensureRunning() }
    fun wiggle() { wiggleStart = t; ensureRunning() }

    // ---- animation loop ------------------------------------------------------------------

    private fun ensureRunning() {
        if (!isAttachedToWindow || !isShown || context.reduceMotion) return
        if (running) {
            // A reaction while idling: wake the loop now instead of after the idle delay.
            Choreographer.getInstance().removeFrameCallback(this)
            Choreographer.getInstance().postFrameCallback(this)
            return
        }
        running = true; lastNs = 0L
        Choreographer.getInstance().postFrameCallback(this)
    }

    /** True while a short reaction or a blink is playing; those run at full frame rate. */
    private fun busy() = t - jumpStart < 0.5f || t - tiltStart < 0.45f || t - wiggleStart < 0.65f ||
        t - chewStart < 1.25f || t < blinkUntil + 0.02f

    override fun doFrame(frameTimeNanos: Long) {
        if (!isAttachedToWindow || !isShown || windowVisibility != VISIBLE) { running = false; return }
        val dt = if (lastNs == 0L) 0.016f else ((frameTimeNanos - lastNs) / 1e9f).coerceAtMost(0.05f)
        lastNs = frameTimeNanos
        t += dt
        if (t > nextBlink) { blinkUntil = t + 0.12f; nextBlink = t + 3f + Random.nextFloat() * 2f }
        invalidate()
        // Idle breathing does not need 90–120 fps. Redrawing the pet every vsync kept the whole
        // window rendering nonstop and made scrolling and transitions stutter. Idle runs at ~20 fps.
        if (busy()) Choreographer.getInstance().postFrameCallback(this)
        else Choreographer.getInstance().postFrameCallbackDelayed(this, IDLE_FRAME_MS)
    }

    private companion object { const val IDLE_FRAME_MS = 50L }

    override fun onAttachedToWindow() { super.onAttachedToWindow(); ensureRunning() }
    override fun onDetachedFromWindow() { Choreographer.getInstance().removeFrameCallback(this); running = false; super.onDetachedFromWindow() }
    override fun onVisibilityAggregated(isVisible: Boolean) {
        super.onVisibilityAggregated(isVisible)
        if (isVisible) ensureRunning() else { Choreographer.getInstance().removeFrameCallback(this); running = false }
    }
    override fun onWindowFocusChanged(hasWindowFocus: Boolean) { super.onWindowFocusChanged(hasWindowFocus); if (hasWindowFocus) ensureRunning() }

    // ---- palette -------------------------------------------------------------------------

    private class Palette(val main: Int, val dark: Int, val belly: Int, val inner: Int, val accent: Int)

    private fun palette(): Palette {
        val s = (stage - 1) / 9f
        return when (species) {
            "panda" -> Palette(0xFFFFFDF9.toInt(), 0xFF2A2A2E.toInt(), 0xFFF3EEE6.toInt(), 0xFFF5A3AE.toInt(), 0xFF3FAF6A.toInt())
            "dragon" -> Palette(mix(0xFF8ED9A8.toInt(), 0xFF4FC383.toInt(), s), 0xFF2F8F5E.toInt(), 0xFFF3F8C8.toInt(), 0xFFF7D06B.toInt(), 0xFFF5A623.toInt())
            else -> Palette(mix(0xFFFFC768.toInt(), 0xFFFFA93A.toInt(), s), 0xFFE07F1F.toInt(), 0xFFFFEED3.toInt(), 0xFFF7A1A8.toInt(), 0xFFD6487E.toInt())
        }
    }

    private fun mix(a: Int, b: Int, f: Float): Int {
        fun ch(s: Int) = (((a shr s) and 0xFF) + ((((b shr s) and 0xFF) - ((a shr s) and 0xFF)) * f)).toInt()
        return Color.argb(255, ch(16), ch(8), ch(0))
    }

    private fun c(color: Int): Int = if (silhouette) 0xFFB9AE9F.toInt() else color

    // ---- drawing -------------------------------------------------------------------------

    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat(); val h = height.toFloat()
        if (w <= 0 || h <= 0) return
        val s = (stage - 1) / 9f
        // Unit: the pet is drawn in a 100×100 box.
        val u = min(w, h) / 100f
        val growth = 0.74f + 0.26f * s
        val breath = if (context.reduceMotion) 0f else sin(t * 2f * PI.toFloat() / 2.4f)
        val sleepingSlow = mood == Mood.SLEEPING || mood == Mood.DOZING
        val breathAmp = if (sleepingSlow) 0.035f else 0.022f
        val jump = phase(jumpStart, 0.45f).let { if (it in 0f..1f) -sin(it * PI.toFloat()) * 14f else 0f }
        val tilt = phase(tiltStart, 0.4f).let { if (it in 0f..1f) sin(it * PI.toFloat() * 2) * 10f else 0f }
        val wig = phase(wiggleStart, 0.6f).let { if (it in 0f..1f) sin(it * PI.toFloat() * 6) * 6f * (1 - it) else 0f }

        canvas.save()
        if (crop) {
            // Head-and-shoulders crop: zoom on the head.
            canvas.translate(w / 2, h / 2)
            canvas.scale(u * 1.55f, u * 1.55f)
            canvas.translate(-50f, -40f)
        } else {
            canvas.translate(w / 2 - 50f * u, h / 2 - 50f * u)
            canvas.scale(u, u)
        }

        // Aura for the final stage.
        if (stage >= 10 && !silhouette && !crop) {
            p.shader = RadialGradient(50f, 55f, 48f, intArrayOf(0x66FFD54F, 0x00FFD54F), null, Shader.TileMode.CLAMP)
            canvas.drawCircle(50f, 55f, 48f, p)
            p.shader = null
        }

        // Ground shadow
        if (shadow && !crop) {
            p.color = 0x22000000
            val sw = 26f * growth * (1f - jump / 40f)
            canvas.drawOval(50f - sw, 90f, 50f + sw, 96f, p)
        }

        canvas.translate(0f, jump)
        canvas.rotate(tilt + wig, 50f, 80f)
        // Scale around the feet so growth keeps the pet grounded.
        canvas.scale(growth, growth, 50f, 92f)

        val pal = palette()
        val headR = 27f - 4f * s
        val bodyW = 24f + 7f * s
        val bodyH = 17f + 8f * s
        val bodyCy = 92f - bodyH
        val headCy = bodyCy - bodyH * 0.55f - headR * 0.55f
        val fullBelly = mood == Mood.FULL

        // Tail + wings behind the body.
        drawTail(canvas, pal, bodyCy, bodyW, s)
        if (species == "dragon" && stage >= 3) drawWings(canvas, pal, bodyCy, bodyW, s)

        // Body (breathing)
        canvas.save()
        canvas.scale(1f + breath * breathAmp * 0.6f, 1f + breath * breathAmp, 50f, 92f)
        p.color = c(pal.main)
        val bw = bodyW * if (fullBelly) 1.12f else 1f
        rect.set(50f - bw, bodyCy - bodyH, 50f + bw, 92f)
        canvas.drawOval(rect, p)
        // Belly
        p.color = c(pal.belly)
        rect.set(50f - bw * 0.62f, bodyCy - bodyH * 0.55f, 50f + bw * 0.62f, 90f)
        canvas.drawOval(rect, p)
        if (species == "dragon" && !silhouette) {
            stroke.color = 0x33000000; stroke.strokeWidth = 0.8f
            for (i in 0..2) { val y = bodyCy - bodyH * 0.2f + i * bodyH * 0.35f; canvas.drawLine(50f - bw * 0.35f, y, 50f + bw * 0.35f, y, stroke) }
        }
        // Feet (black for the panda)
        p.color = c(if (species == "panda") pal.dark else pal.main)
        canvas.drawOval(50f - bw * 0.72f, 86f, 50f - bw * 0.18f, 94f, p)
        canvas.drawOval(50f + bw * 0.18f, 86f, 50f + bw * 0.72f, 94f, p)
        if (!silhouette) {
            p.color = c(pal.inner)
            canvas.drawCircle(50f - bw * 0.45f, 91f, 1.4f, p)
            canvas.drawCircle(50f + bw * 0.45f, 91f, 1.4f, p)
        }
        // Little arms (black for the panda)
        p.color = c(if (species == "panda") pal.dark else pal.main)
        canvas.drawOval(50f - bw * 0.95f, bodyCy - bodyH * 0.35f, 50f - bw * 0.55f, bodyCy + bodyH * 0.25f, p)
        canvas.drawOval(50f + bw * 0.55f, bodyCy - bodyH * 0.35f, 50f + bw * 0.95f, bodyCy + bodyH * 0.25f, p)
        canvas.restore()

        // Stage-7+ star charm (when no neck item)
        if (stage >= 7 && neck == null && !silhouette) drawStar(canvas, 50f, bodyCy - bodyH * 0.55f, 4.2f, 0xFFFFD54F.toInt())
        if (neck != null && !silhouette) drawNeck(canvas, neck!!, headCy + headR * 0.92f, bodyW)

        // Head (gentle counter-breath)
        canvas.save()
        canvas.translate(0f, -breath * 0.8f)
        drawEarsBack(canvas, pal, headCy, headR, s)
        p.color = c(pal.main)
        rect.set(50f - headR * 1.08f, headCy - headR, 50f + headR * 1.08f, headCy + headR * 0.92f)
        canvas.drawOval(rect, p)
        drawEarsFront(canvas, pal, headCy, headR, s)
        if (!silhouette) {
            drawMarkings(canvas, pal, headCy, headR)
            // Muzzle
            p.color = c(pal.belly)
            rect.set(50f - headR * 0.55f, headCy + headR * 0.05f, 50f + headR * 0.55f, headCy + headR * 0.75f)
            canvas.drawOval(rect, p)
            drawFace(canvas, pal, headCy, headR)
            face?.let { drawGlasses(canvas, it, headCy, headR) }
        }
        val hatId = hat ?: if (stage >= 10 && !silhouette) "hat_crown" else null
        if (hatId != null && !silhouette) drawHat(canvas, hatId, headCy - headR * 0.92f, headR)
        canvas.restore()

        canvas.restore()

        // Floating zzz for sleeping / dozing (outside the pet transform)
        if ((mood == Mood.SLEEPING || mood == Mood.DOZING) && !silhouette) drawZzz(canvas, w, h, u)
    }

    private fun phase(start: Float, dur: Float) = (t - start) / dur

    private fun drawTail(canvas: Canvas, pal: Palette, bodyCy: Float, bodyW: Float, s: Float) {
        val wag = if (context.reduceMotion) 0f else sin(t * (if (mood == Mood.CHEER || mood == Mood.CELEBRATE || mood == Mood.HI) 14f else 3f)) * 6f
        stroke.color = c(pal.main)
        when (species) {
            "panda" -> {
                // Short round tail.
                p.color = c(pal.main)
                canvas.drawCircle(50f + bodyW * 0.88f, bodyCy + 4f + wag * 0.15f, 4.5f, p)
            }
            "dragon" -> {
                p.color = c(pal.main)
                path.reset()
                path.moveTo(50f + bodyW * 0.6f, 88f)
                path.quadTo(50f + bodyW * 1.5f, 92f + wag * 0.3f, 50f + bodyW * 1.7f, 78f + wag * 0.5f)
                path.lineTo(50f + bodyW * 1.45f, 80f)
                path.quadTo(50f + bodyW * 1.2f, 86f, 50f + bodyW * 0.6f, 80f)
                path.close()
                canvas.drawPath(path, p)
                p.color = c(pal.inner)
                path.reset()
                val tx = 50f + bodyW * 1.7f; val ty = 78f + wag * 0.5f
                path.moveTo(tx, ty - 5f); path.lineTo(tx + 5f, ty + 1f); path.lineTo(tx - 2f, ty + 3f); path.close()
                canvas.drawPath(path, p)
            }
            else -> {
                stroke.strokeWidth = 5.5f + 1.5f * s
                path.reset(); path.moveTo(50f + bodyW * 0.75f, 88f)
                path.cubicTo(50f + bodyW * 1.5f, 90f, 50f + bodyW * 1.55f + wag * 0.4f, 70f, 50f + bodyW * 1.2f + wag, 62f)
                canvas.drawPath(path, stroke)
                if (!silhouette) {
                    stroke.color = c(pal.dark); stroke.strokeWidth = 2.2f
                    canvas.drawPoint(50f + bodyW * 1.2f + wag, 62f, stroke)
                }
            }
        }
    }

    private fun drawWings(canvas: Canvas, pal: Palette, bodyCy: Float, bodyW: Float, s: Float) {
        val flap = if (context.reduceMotion) 0f else sin(t * 4f) * 4f
        val span = 10f + 14f * s
        p.color = c(mix(pal.main, pal.dark, 0.35f))
        for (side in listOf(-1f, 1f)) {
            path.reset()
            val bx = 50f + side * bodyW * 0.55f; val by = bodyCy - 6f
            path.moveTo(bx, by)
            path.quadTo(bx + side * span * 0.6f, by - span - flap, bx + side * span, by - span * 0.7f - flap)
            path.quadTo(bx + side * span * 0.8f, by - span * 0.2f, bx + side * span * 0.9f, by)
            path.quadTo(bx + side * span * 0.5f, by - 2f, bx, by + 4f)
            path.close()
            canvas.drawPath(path, p)
        }
    }

    private fun drawEarsBack(canvas: Canvas, pal: Palette, cy: Float, r: Float, s: Float) {
        if (species == "dragon") {
            // Horns
            p.color = c(pal.inner)
            val hl = 7f + 6f * s
            for (side in listOf(-1f, 1f)) {
                path.reset()
                val bx = 50f + side * r * 0.45f; val by = cy - r * 0.78f
                path.moveTo(bx - 3f, by + 2f); path.lineTo(bx + side * 3f, by - hl); path.lineTo(bx + 3f, by + 2f); path.close()
                canvas.drawPath(path, p)
            }
            // Head spikes for older dragons
            if (stage >= 4) {
                p.color = c(pal.dark)
                for (i in 0..2) {
                    val x = 50f - 4f + i * 4f
                    path.reset(); path.moveTo(x - 2f, cy - r * 0.95f); path.lineTo(x, cy - r * 0.95f - 4f); path.lineTo(x + 2f, cy - r * 0.95f); path.close()
                    canvas.drawPath(path, p)
                }
            }
        }
        if (species == "cat") {
            for (side in listOf(-1f, 1f)) {
                p.color = c(pal.main)
                path.reset()
                val bx = 50f + side * r * 0.62f; val by = cy - r * 0.55f
                path.moveTo(bx - side * r * 0.42f, by)
                path.lineTo(bx + side * r * 0.22f, by - r * 0.72f)
                path.lineTo(bx + side * r * 0.42f, by + r * 0.15f)
                path.close()
                canvas.drawPath(path, p)
                if (!silhouette) {
                    p.color = c(pal.inner)
                    path.reset()
                    path.moveTo(bx - side * r * 0.2f, by - r * 0.02f)
                    path.lineTo(bx + side * r * 0.17f, by - r * 0.5f)
                    path.lineTo(bx + side * r * 0.27f, by + r * 0.08f)
                    path.close()
                    canvas.drawPath(path, p)
                }
            }
        }
    }

    private fun drawEarsFront(canvas: Canvas, pal: Palette, cy: Float, r: Float, s: Float) {
        if (species == "panda") {
            // Round black ears, drawn over the head's top edge.
            for (side in listOf(-1f, 1f)) {
                p.color = c(pal.dark)
                canvas.drawCircle(50f + side * r * 0.74f, cy - r * 0.74f, r * 0.33f, p)
                if (!silhouette) {
                    p.color = c(0xFF4A4A50.toInt())
                    canvas.drawCircle(50f + side * r * 0.74f, cy - r * 0.74f, r * 0.15f, p)
                }
            }
        }
        if (species == "dragon") {
            p.color = c(mix(pal.main, pal.dark, 0.3f))
            for (side in listOf(-1f, 1f)) {
                path.reset()
                val bx = 50f + side * r * 1.0f; val by = cy - r * 0.1f
                path.moveTo(bx, by - 4f); path.lineTo(bx + side * 8f, by - 7f); path.lineTo(bx, by + 4f); path.close()
                canvas.drawPath(path, p)
            }
        }
    }

    private fun drawMarkings(canvas: Canvas, pal: Palette, cy: Float, r: Float) {
        when (species) {
            "cat" -> if (stage >= 3) {
                stroke.color = c(pal.dark); stroke.strokeWidth = 1.8f
                for (i in -1..1) canvas.drawLine(50f + i * 4.5f, cy - r * 0.88f, 50f + i * 3.5f, cy - r * 0.62f, stroke)
            }
            "panda" -> {
                // Black eye patches, tilted outwards, with a small white disc so the eyes stay visible.
                val ex = r * 0.42f; val ey = cy - r * 0.1f
                for (side in listOf(-1f, 1f)) {
                    canvas.save()
                    canvas.rotate(side * 28f, 50f + side * ex, ey)
                    p.color = c(pal.dark)
                    canvas.drawOval(50f + side * ex - r * 0.25f, ey - r * 0.33f, 50f + side * ex + r * 0.25f, ey + r * 0.33f, p)
                    canvas.restore()
                    p.color = Color.WHITE
                    canvas.drawCircle(50f + side * ex, ey, r * 0.22f, p)
                }
            }
        }
    }

    private fun drawFace(canvas: Canvas, pal: Palette, cy: Float, r: Float) {
        val ex = r * 0.42f
        val ey = cy - r * 0.1f
        val eyeR = r * (0.16f + (10 - stage) * 0.004f)
        val dark = 0xFF2B2118.toInt()
        val blinking = t < blinkUntil
        stroke.color = dark; stroke.strokeWidth = r * 0.075f
        p.color = dark

        fun arcEye(x: Float, up: Boolean) {
            path.reset()
            if (up) { path.moveTo(x - eyeR, ey + eyeR * 0.3f); path.quadTo(x, ey - eyeR * 1.1f, x + eyeR, ey + eyeR * 0.3f) }
            else { path.moveTo(x - eyeR, ey - eyeR * 0.2f); path.quadTo(x, ey + eyeR * 0.9f, x + eyeR, ey - eyeR * 0.2f) }
            canvas.drawPath(path, stroke)
        }

        fun openEye(x: Float, lookUp: Boolean = false, half: Boolean = false) {
            val dy = if (lookUp) -eyeR * 0.35f else 0f
            if (half) {
                rect.set(x - eyeR, ey - eyeR * 0.2f + dy, x + eyeR, ey + eyeR + dy)
                canvas.drawArc(rect, 0f, 180f, true, p)
                canvas.drawLine(x - eyeR * 1.1f, ey + dy - eyeR * 0.15f, x + eyeR * 1.1f, ey + dy - eyeR * 0.15f, stroke)
                return
            }
            rect.set(x - eyeR, ey - eyeR * 1.15f + dy, x + eyeR, ey + eyeR * 1.15f + dy)
            canvas.drawOval(rect, p)
            p.color = Color.WHITE
            canvas.drawCircle(x - eyeR * 0.3f, ey - eyeR * 0.45f + dy, eyeR * 0.38f, p)
            canvas.drawCircle(x + eyeR * 0.35f, ey + eyeR * 0.35f + dy, eyeR * 0.16f, p)
            p.color = dark
        }

        when (mood) {
            Mood.HI, Mood.CHEER, Mood.CELEBRATE, Mood.FULL -> { arcEye(50f - ex, true); arcEye(50f + ex, true) }
            Mood.SLEEPING -> { arcEye(50f - ex, false); arcEye(50f + ex, false) }
            Mood.DOZING, Mood.SLEEPY -> { openEye(50f - ex, half = true); openEye(50f + ex, half = true) }
            Mood.OOPS -> {
                for (side in listOf(-1f, 1f)) {
                    val x = 50f + side * ex
                    path.reset(); path.moveTo(x - side * eyeR, ey - eyeR * 0.8f); path.lineTo(x + side * eyeR * 0.6f, ey); path.lineTo(x - side * eyeR, ey + eyeR * 0.8f)
                    canvas.drawPath(path, stroke)
                }
            }
            Mood.THINKING -> { openEye(50f - ex, lookUp = true); openEye(50f + ex, lookUp = true) }
            else -> if (blinking) { arcEye(50f - ex, false); arcEye(50f + ex, false) } else { openEye(50f - ex); openEye(50f + ex) }
        }

        // Cheeks
        p.color = c(pal.inner); p.alpha = 150
        canvas.drawOval(50f - r * 0.82f, cy + r * 0.12f, 50f - r * 0.48f, cy + r * 0.32f, p)
        canvas.drawOval(50f + r * 0.48f, cy + r * 0.12f, 50f + r * 0.82f, cy + r * 0.32f, p)
        p.alpha = 255

        // Nose
        p.color = if (species == "panda") dark else c(mix(pal.inner, dark, 0.3f))
        canvas.drawOval(50f - r * 0.09f, cy + r * 0.16f, 50f + r * 0.09f, cy + r * 0.27f, p)

        // Mouth
        val my = cy + r * 0.36f
        stroke.strokeWidth = r * 0.06f
        val chew = phase(chewStart, 1.2f).let { if (it in 0f..1f) abs(sin(it * PI.toFloat() * 3)) else -1f }
        when {
            chew >= 0f -> { p.color = dark; canvas.drawOval(50f - r * 0.12f, my - r * 0.02f, 50f + r * 0.12f, my + r * 0.06f + chew * r * 0.12f, p) }
            mood == Mood.HUNGRY || mood == Mood.STARVING || mood == Mood.CHEER || mood == Mood.CELEBRATE || mood == Mood.HI -> {
                p.color = 0xFF8C3B3B.toInt()
                rect.set(50f - r * 0.16f, my - r * 0.04f, 50f + r * 0.16f, my + r * 0.24f)
                canvas.drawArc(rect, 0f, 180f, true, p)
                p.color = 0xFFF28C8C.toInt()
                rect.set(50f - r * 0.09f, my + r * 0.08f, 50f + r * 0.09f, my + r * 0.22f)
                canvas.drawOval(rect, p)
            }
            mood == Mood.OOPS || mood == Mood.STARVING -> {
                path.reset(); path.moveTo(50f - r * 0.14f, my + r * 0.06f); path.quadTo(50f, my - r * 0.06f, 50f + r * 0.14f, my + r * 0.06f)
                canvas.drawPath(path, stroke)
            }
            mood == Mood.SLEEPING || mood == Mood.DOZING -> { p.color = dark; canvas.drawCircle(50f, my + r * 0.04f, r * 0.05f, p) }
            species == "cat" -> {
                path.reset()
                path.moveTo(50f - r * 0.16f, my); path.quadTo(50f - r * 0.08f, my + r * 0.1f, 50f, my)
                path.quadTo(50f + r * 0.08f, my + r * 0.1f, 50f + r * 0.16f, my)
                canvas.drawPath(path, stroke)
            }
            else -> {
                path.reset(); path.moveTo(50f - r * 0.15f, my); path.quadTo(50f, my + r * 0.14f, 50f + r * 0.15f, my)
                canvas.drawPath(path, stroke)
            }
        }

        // Whiskers (cat)
        if (species == "cat") {
            stroke.color = 0x552B2118; stroke.strokeWidth = r * 0.03f
            for (side in listOf(-1f, 1f)) for (k in 0..1) {
                canvas.drawLine(50f + side * r * 0.5f, cy + r * (0.22f + k * 0.1f), 50f + side * r * 0.95f, cy + r * (0.16f + k * 0.16f), stroke)
            }
        }
        // Sweat drop for starving / oops
        if (mood == Mood.STARVING || mood == Mood.OOPS) {
            p.color = 0xFF7FB0F5.toInt()
            path.reset()
            val sx = 50f + r * 0.85f; val sy = cy - r * 0.5f
            path.moveTo(sx, sy - 4f); path.quadTo(sx + 3f, sy + 1f, sx, sy + 2.5f); path.quadTo(sx - 3f, sy + 1f, sx, sy - 4f)
            canvas.drawPath(path, p)
        }
    }

    private fun drawGlasses(canvas: Canvas, id: String, cy: Float, r: Float) {
        val ex = r * 0.42f; val ey = cy - r * 0.1f; val gr = r * 0.27f
        if (id == "face_star") {
            drawStar(canvas, 50f - ex, ey, gr * 1.2f, 0xFF2B2118.toInt())
            drawStar(canvas, 50f + ex, ey, gr * 1.2f, 0xFF2B2118.toInt())
            stroke.color = 0xFF2B2118.toInt(); stroke.strokeWidth = r * 0.06f
            canvas.drawLine(50f - ex + gr, ey, 50f + ex - gr, ey, stroke)
        } else {
            stroke.color = 0xFF3B2A1A.toInt(); stroke.strokeWidth = r * 0.065f
            canvas.drawCircle(50f - ex, ey, gr, stroke)
            canvas.drawCircle(50f + ex, ey, gr, stroke)
            canvas.drawLine(50f - ex + gr, ey, 50f + ex - gr, ey, stroke)
        }
    }

    private fun drawHat(canvas: Canvas, id: String, top: Float, r: Float) {
        when (id) {
            "hat_party" -> {
                path.reset(); path.moveTo(50f - r * 0.42f, top + 4f); path.lineTo(50f, top - r * 0.95f); path.lineTo(50f + r * 0.42f, top + 4f); path.close()
                p.shader = LinearGradient(50f - r * 0.4f, top, 50f + r * 0.4f, top - r, intArrayOf(0xFF8A5CD6.toInt(), 0xFFD6487E.toInt()), null, Shader.TileMode.CLAMP)
                canvas.drawPath(path, p); p.shader = null
                p.color = 0xFFFFD54F.toInt(); canvas.drawCircle(50f, top - r * 0.95f, r * 0.12f, p)
                for (i in 0..2) canvas.drawCircle(50f - r * 0.12f + i * r * 0.12f, top - r * 0.3f - i * r * 0.15f, r * 0.05f, p)
            }
            "hat_beanie" -> {
                p.color = 0xFF3B7DD8.toInt()
                rect.set(50f - r * 0.78f, top - r * 0.55f, 50f + r * 0.78f, top + r * 0.55f)
                canvas.drawArc(rect, 180f, 180f, true, p)
                p.color = 0xFF2C5FA8.toInt(); canvas.drawRect(50f - r * 0.8f, top - r * 0.02f, 50f + r * 0.8f, top + r * 0.16f, p)
                p.color = 0xFFF5F5F5.toInt(); canvas.drawCircle(50f, top - r * 0.58f, r * 0.15f, p)
            }
            "hat_flower" -> {
                val colors = intArrayOf(0xFFF7A1A8.toInt(), 0xFFFFD54F.toInt(), 0xFFFFFFFF.toInt(), 0xFFB39DDB.toInt(), 0xFFF7A1A8.toInt())
                for (i in 0..4) {
                    val x = 50f - r * 0.6f + i * r * 0.3f
                    val y = top + 2f - sin(i / 4f * PI.toFloat()) * r * 0.18f
                    p.color = colors[i]; canvas.drawCircle(x, y, r * 0.13f, p)
                    p.color = 0xFFF5A623.toInt(); canvas.drawCircle(x, y, r * 0.05f, p)
                }
            }
            "hat_grad" -> {
                p.color = 0xFF2B2118.toInt()
                rect.set(50f - r * 0.45f, top - r * 0.25f, 50f + r * 0.45f, top + r * 0.12f); canvas.drawRect(rect, p)
                path.reset(); path.moveTo(50f - r * 0.95f, top - r * 0.3f); path.lineTo(50f, top - r * 0.6f); path.lineTo(50f + r * 0.95f, top - r * 0.3f); path.lineTo(50f, top - r * 0.02f); path.close()
                canvas.drawPath(path, p)
                stroke.color = 0xFFFFD54F.toInt(); stroke.strokeWidth = r * 0.05f
                canvas.drawLine(50f, top - r * 0.3f, 50f + r * 0.7f, top - r * 0.2f, stroke)
                canvas.drawLine(50f + r * 0.7f, top - r * 0.2f, 50f + r * 0.72f, top + r * 0.2f, stroke)
            }
            "hat_crown" -> {
                p.color = 0xFFFFC83D.toInt()
                path.reset()
                path.moveTo(50f - r * 0.5f, top + 2f); path.lineTo(50f - r * 0.55f, top - r * 0.45f); path.lineTo(50f - r * 0.25f, top - r * 0.18f)
                path.lineTo(50f, top - r * 0.6f); path.lineTo(50f + r * 0.25f, top - r * 0.18f); path.lineTo(50f + r * 0.55f, top - r * 0.45f); path.lineTo(50f + r * 0.5f, top + 2f)
                path.close(); canvas.drawPath(path, p)
                p.color = 0xFFD6487E.toInt(); canvas.drawCircle(50f, top - r * 0.12f, r * 0.08f, p)
                p.color = 0xFF3B7DD8.toInt(); canvas.drawCircle(50f - r * 0.3f, top - r * 0.05f, r * 0.06f, p); canvas.drawCircle(50f + r * 0.3f, top - r * 0.05f, r * 0.06f, p)
            }
        }
    }

    private fun drawNeck(canvas: Canvas, id: String, y: Float, bodyW: Float) {
        when (id) {
            "neck_bow" -> {
                p.color = 0xFFD6487E.toInt()
                path.reset(); path.moveTo(50f, y); path.lineTo(50f - 9f, y - 5f); path.lineTo(50f - 9f, y + 5f); path.close(); canvas.drawPath(path, p)
                path.reset(); path.moveTo(50f, y); path.lineTo(50f + 9f, y - 5f); path.lineTo(50f + 9f, y + 5f); path.close(); canvas.drawPath(path, p)
                p.color = 0xFFB0305F.toInt(); canvas.drawCircle(50f, y, 2.4f, p)
            }
            "neck_scarf" -> {
                p.color = 0xFFD64545.toInt()
                rect.set(50f - bodyW * 0.75f, y - 3.5f, 50f + bodyW * 0.75f, y + 3.5f); canvas.drawRoundRect(rect, 3f, 3f, p)
                rect.set(50f + bodyW * 0.25f, y, 50f + bodyW * 0.5f, y + 13f); canvas.drawRoundRect(rect, 2f, 2f, p)
                p.color = 0xFFFFFFFF.toInt(); p.alpha = 120
                for (i in 0..3) canvas.drawRect(50f - bodyW * 0.6f + i * bodyW * 0.35f, y - 3.5f, 50f - bodyW * 0.6f + i * bodyW * 0.35f + 2f, y + 3.5f, p)
                p.alpha = 255
            }
            "neck_medal" -> {
                stroke.color = 0xFF3B7DD8.toInt(); stroke.strokeWidth = 2.2f
                canvas.drawLine(50f - 7f, y - 4f, 50f, y + 6f, stroke); canvas.drawLine(50f + 7f, y - 4f, 50f, y + 6f, stroke)
                p.color = 0xFFFFC83D.toInt(); canvas.drawCircle(50f, y + 9f, 4.5f, p)
                drawStar(canvas, 50f, y + 9f, 2.6f, 0xFFF59E0B.toInt())
            }
        }
    }

    private fun drawStar(canvas: Canvas, cx: Float, cy: Float, r: Float, color: Int) {
        path.reset()
        for (i in 0 until 10) {
            val rr = if (i % 2 == 0) r else r * 0.45f
            val a = PI / 5 * i - PI / 2
            val x = cx + (cos(a) * rr).toFloat(); val y = cy + (sin(a) * rr).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close()
        p.color = color
        canvas.drawPath(path, p)
    }

    private fun drawZzz(canvas: Canvas, w: Float, h: Float, u: Float) {
        p.color = if (mood == Mood.DOZING) 0xFF7FB0F5.toInt() else 0xFF8A7CC2.toInt()
        p.textSize = 9f * u * (if (crop) 1.6f else 1f)
        p.isFakeBoldText = true
        for (i in 0..2) {
            val ph = ((t * 0.5f + i / 3f) % 1f)
            p.alpha = (255 * sin(ph * PI.toFloat())).toInt().coerceIn(0, 255)
            canvas.drawText("z", w * 0.68f + ph * 14f * u, h * 0.32f - ph * 20f * u + i * 2f, p)
        }
        p.alpha = 255
        p.isFakeBoldText = false
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val w = MeasureSpec.getSize(widthMeasureSpec)
        val h = if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) w else MeasureSpec.getSize(heightMeasureSpec)
        setMeasuredDimension(w, max(h, 1))
    }
}
