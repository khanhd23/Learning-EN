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
import android.view.View
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.random.Random

/**
 * The pet's home: background theme + furniture, drawn with Canvas. Night (19:00–06:00) dims the
 * room, shows the moon and lights up the lamp. The pet itself is a separate [PetView] on top.
 */
class RoomView(context: Context) : View(context) {
    var theme = "theme_cozy"; set(v) { field = v; invalidate() }
    var items: Map<String, String> = emptyMap(); set(v) { field = v; invalidate() }
    var night = false; set(v) { field = v; invalidate() }
    /** Preview a single item in the shop sheet. */
    var previewOnly: String? = null

    private val p = Paint(Paint.ANTI_ALIAS_FLAG)
    private val s = Paint(Paint.ANTI_ALIAS_FLAG).apply { style = Paint.Style.STROKE; strokeCap = Paint.Cap.ROUND }
    private val path = Path()
    private val r = RectF()
    private val starSeeds = List(24) { Random(it * 31 + 7).let { rnd -> Triple(rnd.nextFloat(), rnd.nextFloat(), rnd.nextFloat()) } }

    /** Floor line as a fraction of the height (the pet stands on it). */
    val floorFrac = 0.66f

    override fun onDraw(canvas: Canvas) {
        val w = width.toFloat(); val h = height.toFloat()
        if (w <= 0f) return
        val fy = h * floorFrac
        when (theme) {
            "theme_garden" -> garden(canvas, w, h, fy)
            "theme_beach" -> beach(canvas, w, h, fy)
            "theme_candy" -> candy(canvas, w, h, fy)
            "theme_forest" -> forest(canvas, w, h, fy)
            "theme_space" -> space(canvas, w, h, fy)
            "theme_castle" -> castle(canvas, w, h, fy)
            else -> cozy(canvas, w, h, fy)
        }
        val u = min(w, h) / 100f
        items[PetItems.WALL]?.let { wall(canvas, it, w * 0.27f, h * 0.24f, u) }
        items[PetItems.RUG]?.let { rug(canvas, it, w * 0.55f, fy + (h - fy) * 0.45f, w, u) }
        items[PetItems.LAMP]?.let { lamp(canvas, it, w * 0.09f, fy + (h - fy) * 0.2f, u) }
        items[PetItems.BED]?.let { bed(canvas, it, w * 0.22f, fy + (h - fy) * 0.55f, u) }
        items[PetItems.PLANT]?.let { plant(canvas, it, w * 0.9f, fy + (h - fy) * 0.35f, u) }
        items[PetItems.TOY]?.let { toy(canvas, it, w * 0.78f, fy + (h - fy) * 0.78f, u) }
        if (night) {
            p.color = 0x55101640
            canvas.drawRect(0f, 0f, w, h, p)
            items[PetItems.LAMP]?.let {
                p.shader = RadialGradient(w * 0.09f, fy - 18f * u, 40f * u, intArrayOf(0x66FFE08A, 0x00FFE08A), null, Shader.TileMode.CLAMP)
                canvas.drawCircle(w * 0.09f, fy - 18f * u, 40f * u, p)
                p.shader = null
            }
        }
    }

    // ---- backgrounds -----------------------------------------------------------------------

    private fun sky(canvas: Canvas, w: Float, h: Float, top: Int, bottom: Int) {
        p.shader = LinearGradient(0f, 0f, 0f, h, if (night) 0xFF1B2050.toInt() else top, if (night) 0xFF3A3A78.toInt() else bottom, Shader.TileMode.CLAMP)
        canvas.drawRect(0f, 0f, w, h, p)
        p.shader = null
    }

    private fun sunOrMoon(canvas: Canvas, x: Float, y: Float, rad: Float) {
        if (night) {
            p.color = 0xFFFFF4C2.toInt(); canvas.drawCircle(x, y, rad, p)
            p.color = 0xFF2A2F66.toInt(); canvas.drawCircle(x + rad * 0.45f, y - rad * 0.2f, rad * 0.85f, p)
            p.color = 0xFFFFFFFF.toInt()
            starSeeds.forEach { (a, b, c) -> p.alpha = (120 + 135 * c).toInt(); canvas.drawCircle(a * width, b * height * 0.5f, 1.2f + c * 1.5f, p) }
            p.alpha = 255
        } else {
            p.shader = RadialGradient(x, y, rad * 1.8f, intArrayOf(0x66FFE08A, 0x00FFE08A), null, Shader.TileMode.CLAMP)
            canvas.drawCircle(x, y, rad * 1.8f, p); p.shader = null
            p.color = 0xFFFFD54F.toInt(); canvas.drawCircle(x, y, rad, p)
        }
    }

    private fun cozy(canvas: Canvas, w: Float, h: Float, fy: Float) {
        p.color = 0xFFF9E3C8.toInt(); canvas.drawRect(0f, 0f, w, fy, p)
        p.color = 0xFFF3D3AE.toInt()
        for (i in 0..12) for (j in 0..6) canvas.drawCircle(i * w / 12f + (j % 2) * w / 24f, j * fy / 6f + 10f, 2.2f, p)
        // Window
        val wx = w * 0.56f; val wy = h * 0.1f; val ww = w * 0.3f; val wh = h * 0.3f
        canvas.save(); r.set(wx, wy, wx + ww, wy + wh); canvas.clipRect(r)
        p.shader = LinearGradient(0f, wy, 0f, wy + wh, if (night) 0xFF1B2050.toInt() else 0xFF8FD3FF.toInt(), if (night) 0xFF3A3A78.toInt() else 0xFFD6F0FF.toInt(), Shader.TileMode.CLAMP)
        canvas.drawRect(r, p); p.shader = null
        sunOrMoon(canvas, wx + ww * 0.7f, wy + wh * 0.3f, ww * 0.1f)
        if (!night) { p.color = Color.WHITE; canvas.drawOval(wx + ww * 0.1f, wy + wh * 0.55f, wx + ww * 0.45f, wy + wh * 0.72f, p) }
        canvas.restore()
        s.color = 0xFFB8865B.toInt(); s.strokeWidth = 5f
        canvas.drawRect(r, s); canvas.drawLine(wx + ww / 2, wy, wx + ww / 2, wy + wh, s); canvas.drawLine(wx, wy + wh / 2, wx + ww, wy + wh / 2, s)
        // Curtains
        p.color = 0xFFE8838F.toInt()
        path.reset(); path.moveTo(wx - 10f, wy - 6f); path.lineTo(wx + ww * 0.18f, wy - 6f); path.quadTo(wx + ww * 0.05f, wy + wh * 0.5f, wx + ww * 0.12f, wy + wh + 8f); path.lineTo(wx - 10f, wy + wh + 8f); path.close()
        canvas.drawPath(path, p)
        path.reset(); path.moveTo(wx + ww + 10f, wy - 6f); path.lineTo(wx + ww * 0.82f, wy - 6f); path.quadTo(wx + ww * 0.95f, wy + wh * 0.5f, wx + ww * 0.88f, wy + wh + 8f); path.lineTo(wx + ww + 10f, wy + wh + 8f); path.close()
        canvas.drawPath(path, p)
        // Floor planks
        p.shader = LinearGradient(0f, fy, 0f, h, 0xFFDDA672.toInt(), 0xFFC98B55.toInt(), Shader.TileMode.CLAMP)
        canvas.drawRect(0f, fy, w, h, p); p.shader = null
        s.color = 0x33000000; s.strokeWidth = 2f
        for (i in 1..4) { val y = fy + (h - fy) * i / 5f; canvas.drawLine(0f, y, w, y, s) }
        p.color = 0xFFB27A4B.toInt(); canvas.drawRect(0f, fy - 6f, w, fy, p)
    }

    private fun garden(canvas: Canvas, w: Float, h: Float, fy: Float) {
        sky(canvas, w, h, 0xFF8FD3FF.toInt(), 0xFFE2F5FF.toInt())
        sunOrMoon(canvas, w * 0.82f, h * 0.16f, min(w, h) * 0.07f)
        p.color = if (night) 0xFF2E5E3E.toInt() else 0xFF9BD67E.toInt()
        path.reset(); path.moveTo(0f, fy); path.quadTo(w * 0.25f, fy - h * 0.18f, w * 0.55f, fy - h * 0.04f); path.quadTo(w * 0.8f, fy - h * 0.16f, w, fy - h * 0.06f); path.lineTo(w, h); path.lineTo(0f, h); path.close()
        canvas.drawPath(path, p)
        // Fence
        p.color = 0xFFFFF4E4.toInt()
        val fenceY = fy - h * 0.08f
        var x = 0f
        while (x < w) { r.set(x + 4f, fenceY, x + 16f, fy + 6f); canvas.drawRoundRect(r, 4f, 4f, p); x += 26f }
        canvas.drawRect(0f, fenceY + 10f, w, fenceY + 16f, p)
        p.color = 0xFF7CC15F.toInt(); canvas.drawRect(0f, fy, w, h, p)
        val fc = intArrayOf(0xFFF7A1A8.toInt(), 0xFFFFD54F.toInt(), 0xFFB39DDB.toInt(), 0xFFFFFFFF.toInt())
        for (i in 0..9) {
            val fx = w * (0.05f + i * 0.1f); val fyy = fy + (h - fy) * (0.15f + (i % 3) * 0.3f)
            s.color = 0xFF4E8F3A.toInt(); s.strokeWidth = 2f; canvas.drawLine(fx, fyy, fx, fyy + 10f, s)
            p.color = fc[i % 4]; canvas.drawCircle(fx, fyy, 5f, p)
            p.color = 0xFFF5A623.toInt(); canvas.drawCircle(fx, fyy, 2f, p)
        }
    }

    private fun beach(canvas: Canvas, w: Float, h: Float, fy: Float) {
        sky(canvas, w, h, 0xFF7CCBFF.toInt(), 0xFFFFE6C2.toInt())
        sunOrMoon(canvas, w * 0.2f, h * 0.18f, min(w, h) * 0.08f)
        p.shader = LinearGradient(0f, fy - h * 0.14f, 0f, fy, if (night) 0xFF1E3A6E.toInt() else 0xFF3BA7E0.toInt(), if (night) 0xFF284B80.toInt() else 0xFF7FD4F0.toInt(), Shader.TileMode.CLAMP)
        canvas.drawRect(0f, fy - h * 0.14f, w, fy, p); p.shader = null
        s.color = 0x99FFFFFF.toInt(); s.strokeWidth = 2.5f
        for (i in 0..5) { val y = fy - h * 0.1f + i * 6f; canvas.drawLine(w * (0.1f + i * 0.13f), y, w * (0.18f + i * 0.13f), y, s) }
        p.color = 0xFFF3D9A4.toInt(); canvas.drawRect(0f, fy, w, h, p)
        p.color = 0xFFE9C98C.toInt()
        for (i in 0..14) canvas.drawCircle((i * 37 % 100) / 100f * w, fy + ((i * 53) % 100) / 100f * (h - fy), 2f, p)
        // Palm tree
        val px = w * 0.9f
        s.color = 0xFF9C6B3E.toInt(); s.strokeWidth = 9f
        path.reset(); path.moveTo(px, fy + 10f); path.quadTo(px - 12f, fy - h * 0.25f, px - 4f, fy - h * 0.42f); canvas.drawPath(path, s)
        p.color = 0xFF3FA35B.toInt()
        for (a in listOf(-160f, -120f, -60f, -20f, -90f)) {
            canvas.save(); canvas.rotate(a + 90f, px - 4f, fy - h * 0.42f)
            r.set(px - 4f - 9f, fy - h * 0.42f, px - 4f + 9f, fy - h * 0.42f + h * 0.17f); canvas.drawOval(r, p)
            canvas.restore()
        }
    }

    private fun candy(canvas: Canvas, w: Float, h: Float, fy: Float) {
        p.color = 0xFFFFE1EC.toInt(); canvas.drawRect(0f, 0f, w, fy, p)
        p.color = 0xFFFFC9DC.toInt()
        var x = 0f; while (x < w) { canvas.drawRect(x, 0f, x + 14f, fy, p); x += 32f }
        // Checker floor
        val cell = (h - fy) / 4f
        var row = 0; var y = fy
        while (y < h) { var cx = 0f; var col = row % 2; while (cx < w) { p.color = if (col % 2 == 0) 0xFFFFFFFF.toInt() else 0xFFFFB3CF.toInt(); canvas.drawRect(cx, y, cx + cell, y + cell, p); cx += cell; col++ }; y += cell; row++ }
        // Lollipops
        for ((lx, c1) in listOf(w * 0.12f to 0xFF8A5CD6.toInt(), w * 0.88f to 0xFF3B7DD8.toInt())) {
            s.color = Color.WHITE; s.strokeWidth = 4f; canvas.drawLine(lx, fy - h * 0.06f, lx, fy - h * 0.3f, s)
            p.color = c1; canvas.drawCircle(lx, fy - h * 0.34f, min(w, h) * 0.07f, p)
            s.color = 0x88FFFFFF.toInt(); s.strokeWidth = 3f
            r.set(lx - min(w, h) * 0.045f, fy - h * 0.34f - min(w, h) * 0.045f, lx + min(w, h) * 0.045f, fy - h * 0.34f + min(w, h) * 0.045f)
            canvas.drawArc(r, 0f, 270f, false, s)
        }
        // Clouds of cotton candy
        p.color = 0xFFFFFFFF.toInt()
        canvas.drawOval(w * 0.35f, h * 0.08f, w * 0.55f, h * 0.16f, p); canvas.drawOval(w * 0.45f, h * 0.05f, w * 0.62f, h * 0.15f, p)
    }

    private fun forest(canvas: Canvas, w: Float, h: Float, fy: Float) {
        sky(canvas, w, h, 0xFFB9E8C9.toInt(), 0xFFE9F8EE.toInt())
        sunOrMoon(canvas, w * 0.5f, h * 0.12f, min(w, h) * 0.06f)
        val shades = intArrayOf(0xFF2F7D4E.toInt(), 0xFF3E9B60.toInt(), 0xFF2A6B45.toInt())
        for (i in 0..7) {
            val tx = w * (i / 7f); val th = h * (0.32f + (i % 3) * 0.08f)
            p.color = shades[i % 3]
            path.reset(); path.moveTo(tx - w * 0.09f, fy); path.lineTo(tx, fy - th); path.lineTo(tx + w * 0.09f, fy); path.close()
            canvas.drawPath(path, p)
        }
        p.color = 0xFF6BB36A.toInt(); canvas.drawRect(0f, fy, w, h, p)
        // Mushrooms
        for (mx in listOf(w * 0.08f, w * 0.93f)) {
            p.color = 0xFFFFF4E4.toInt(); canvas.drawRect(mx - 3f, fy + 6f, mx + 3f, fy + 18f, p)
            p.color = 0xFFE05555.toInt(); r.set(mx - 11f, fy - 2f, mx + 11f, fy + 14f); canvas.drawArc(r, 180f, 180f, true, p)
            p.color = Color.WHITE; canvas.drawCircle(mx - 4f, fy + 3f, 2f, p); canvas.drawCircle(mx + 5f, fy + 1f, 1.6f, p)
        }
    }

    private fun space(canvas: Canvas, w: Float, h: Float, fy: Float) {
        p.shader = LinearGradient(0f, 0f, 0f, h, 0xFF120E3A.toInt(), 0xFF3B2A7A.toInt(), Shader.TileMode.CLAMP)
        canvas.drawRect(0f, 0f, w, h, p); p.shader = null
        p.color = Color.WHITE
        starSeeds.forEach { (a, b, c) -> p.alpha = (100 + 155 * c).toInt(); canvas.drawCircle(a * w, b * fy, 1f + c * 1.8f, p) }
        p.alpha = 255
        // Planet with ring
        val px = w * 0.78f; val py = h * 0.2f; val pr = min(w, h) * 0.09f
        p.color = 0xFFF5A623.toInt(); canvas.drawCircle(px, py, pr, p)
        s.color = 0xFFFFD9A0.toInt(); s.strokeWidth = 4f
        r.set(px - pr * 1.7f, py - pr * 0.45f, px + pr * 1.7f, py + pr * 0.45f); canvas.drawOval(r, s)
        p.color = 0xFF5A4FA8.toInt(); canvas.drawRect(0f, fy, w, h, p)
        s.color = 0x337FB0F5; s.strokeWidth = 2f
        var x = 0f; while (x < w) { canvas.drawLine(x, fy, x - w * 0.15f, h, s); x += w / 8f }
        p.color = 0xFF7FB0F5.toInt(); canvas.drawRect(0f, fy - 4f, w, fy, p)
    }

    private fun castle(canvas: Canvas, w: Float, h: Float, fy: Float) {
        p.color = 0xFFB9C2D6.toInt(); canvas.drawRect(0f, 0f, w, fy, p)
        s.color = 0x33485670; s.strokeWidth = 2f
        var y = 14f; var row = 0
        while (y < fy) { canvas.drawLine(0f, y, w, y, s); var x = if (row % 2 == 0) 0f else 20f; while (x < w) { canvas.drawLine(x, y - 14f, x, y, s); x += 40f }; y += 14f; row++ }
        // Banners
        for ((bx, c1) in listOf(w * 0.2f to 0xFFD64545.toInt(), w * 0.8f to 0xFF3B7DD8.toInt())) {
            p.color = c1
            path.reset(); path.moveTo(bx - 14f, h * 0.06f); path.lineTo(bx + 14f, h * 0.06f); path.lineTo(bx + 14f, h * 0.36f); path.lineTo(bx, h * 0.3f); path.lineTo(bx - 14f, h * 0.36f); path.close()
            canvas.drawPath(path, p)
            p.color = 0xFFFFD54F.toInt(); canvas.drawCircle(bx, h * 0.16f, 5f, p)
        }
        p.color = 0xFF8D97AE.toInt(); canvas.drawRect(0f, fy, w, h, p)
        p.color = 0xFFC0392B.toInt()
        path.reset(); path.moveTo(w * 0.4f, fy); path.lineTo(w * 0.6f, fy); path.lineTo(w * 0.72f, h); path.lineTo(w * 0.28f, h); path.close(); canvas.drawPath(path, p)
        p.color = 0xFFFFD54F.toInt(); canvas.drawRect(w * 0.4f, fy, w * 0.6f, fy + 3f, p)
    }

    // ---- furniture ---------------------------------------------------------------------

    private fun wall(canvas: Canvas, id: String, cx: Float, cy: Float, u: Float) {
        when (id) {
            "wall_abc" -> {
                p.color = 0xFFFFFFFF.toInt(); r.set(cx - 14f * u, cy - 10f * u, cx + 14f * u, cy + 10f * u); canvas.drawRoundRect(r, 2f * u, 2f * u, p)
                s.color = 0xFF9C6B3E.toInt(); s.strokeWidth = 1.6f * u; canvas.drawRoundRect(r, 2f * u, 2f * u, s)
                p.textSize = 8f * u; p.isFakeBoldText = true
                val cs = intArrayOf(0xFFD64545.toInt(), 0xFF2E9E5B.toInt(), 0xFF3B7DD8.toInt())
                "ABC".forEachIndexed { i, ch -> p.color = cs[i]; canvas.drawText(ch.toString(), cx - 11f * u + i * 8f * u, cy + 3f * u, p) }
                p.isFakeBoldText = false
            }
            "wall_clock" -> {
                p.color = 0xFFFFFFFF.toInt(); canvas.drawCircle(cx, cy, 9f * u, p)
                s.color = 0xFF8D5A35.toInt(); s.strokeWidth = 2f * u; canvas.drawCircle(cx, cy, 9f * u, s)
                val cal = java.util.Calendar.getInstance()
                val hr = (cal.get(java.util.Calendar.HOUR) + cal.get(java.util.Calendar.MINUTE) / 60f) / 12f * 2 * PI
                val mn = cal.get(java.util.Calendar.MINUTE) / 60f * 2 * PI
                s.strokeWidth = 1.4f * u; s.color = 0xFF2B2118.toInt()
                canvas.drawLine(cx, cy, cx + (sin(hr) * 4.5f * u).toFloat(), cy - (cos(hr) * 4.5f * u).toFloat(), s)
                canvas.drawLine(cx, cy, cx + (sin(mn) * 7f * u).toFloat(), cy - (cos(mn) * 7f * u).toFloat(), s)
            }
            "wall_map" -> {
                p.color = 0xFFBFE3F7.toInt(); r.set(cx - 16f * u, cy - 10f * u, cx + 16f * u, cy + 10f * u); canvas.drawRect(r, p)
                p.color = 0xFF7CC15F.toInt()
                canvas.drawOval(cx - 13f * u, cy - 7f * u, cx - 3f * u, cy + 2f * u, p)
                canvas.drawOval(cx - 2f * u, cy - 6f * u, cx + 7f * u, cy + 6f * u, p)
                canvas.drawOval(cx + 8f * u, cy + 1f * u, cx + 14f * u, cy + 7f * u, p)
                s.color = 0xFF8D5A35.toInt(); s.strokeWidth = 1.8f * u; canvas.drawRect(r, s)
            }
        }
    }

    private fun rug(canvas: Canvas, id: String, cx: Float, cy: Float, w: Float, u: Float) {
        val rw = min(w * 0.3f, 40f * u); val rh = 7f * u
        when (id) {
            "rug_rainbow" -> {
                val cs = intArrayOf(0xFFD64545.toInt(), 0xFFF5A623.toInt(), 0xFFFFD54F.toInt(), 0xFF2E9E5B.toInt(), 0xFF3B7DD8.toInt(), 0xFF8A5CD6.toInt())
                cs.forEachIndexed { i, c1 -> p.color = c1; val k = 1f - i * 0.14f; canvas.drawOval(cx - rw * k, cy - rh * k, cx + rw * k, cy + rh * k, p) }
            }
            "rug_paw" -> {
                p.color = 0xFF8FB8D8.toInt(); canvas.drawOval(cx - rw, cy - rh, cx + rw, cy + rh, p)
                p.color = 0xFFFFFFFF.toInt(); p.alpha = 180
                for (dx in listOf(-0.5f, 0.1f, 0.6f)) { canvas.drawCircle(cx + rw * dx, cy, 2.4f * u, p); canvas.drawCircle(cx + rw * dx - 2f * u, cy - 2.6f * u, 1f * u, p); canvas.drawCircle(cx + rw * dx + 2f * u, cy - 2.6f * u, 1f * u, p) }
                p.alpha = 255
            }
            else -> {
                p.color = 0xFFE8838F.toInt(); canvas.drawOval(cx - rw, cy - rh, cx + rw, cy + rh, p)
                s.color = 0x88FFFFFF.toInt(); s.strokeWidth = 1.5f * u; canvas.drawOval(cx - rw * 0.8f, cy - rh * 0.7f, cx + rw * 0.8f, cy + rh * 0.7f, s)
            }
        }
    }

    private fun bed(canvas: Canvas, id: String, cx: Float, cy: Float, u: Float) {
        when (id) {
            "bed_cushion" -> {
                p.color = 0xFF8A5CD6.toInt(); r.set(cx - 15f * u, cy - 6f * u, cx + 15f * u, cy + 5f * u); canvas.drawRoundRect(r, 6f * u, 6f * u, p)
                p.color = 0xFFB39DDB.toInt(); r.set(cx - 12f * u, cy - 8f * u, cx + 12f * u, cy + 1f * u); canvas.drawRoundRect(r, 5f * u, 5f * u, p)
            }
            "bed_cloud" -> {
                p.color = 0xFFFFFFFF.toInt()
                for (dx in listOf(-10f, -3f, 5f, 11f)) canvas.drawCircle(cx + dx * u, cy - 3f * u + (if (dx.toInt() % 2 == 0) -2f else 1f) * u, 7f * u, p)
                p.color = 0xFFDDEBFF.toInt(); r.set(cx - 16f * u, cy - 2f * u, cx + 16f * u, cy + 5f * u); canvas.drawRoundRect(r, 4f * u, 4f * u, p)
            }
            "bed_royal" -> {
                p.color = 0xFFC0392B.toInt(); r.set(cx - 17f * u, cy - 6f * u, cx + 17f * u, cy + 5f * u); canvas.drawRoundRect(r, 3f * u, 3f * u, p)
                p.color = 0xFFFFD54F.toInt(); canvas.drawRect(cx - 18f * u, cy + 3f * u, cx + 18f * u, cy + 6f * u, p)
                p.color = 0xFFFFFFFF.toInt(); r.set(cx - 14f * u, cy - 9f * u, cx - 4f * u, cy - 3f * u); canvas.drawRoundRect(r, 3f * u, 3f * u, p)
            }
            else -> { // basket
                p.color = 0xFFC98B55.toInt(); r.set(cx - 15f * u, cy - 4f * u, cx + 15f * u, cy + 6f * u); canvas.drawRoundRect(r, 5f * u, 5f * u, p)
                s.color = 0x55000000; s.strokeWidth = 1.2f * u
                for (i in -3..3) canvas.drawLine(cx + i * 4f * u, cy - 3f * u, cx + i * 4f * u, cy + 5f * u, s)
                p.color = 0xFFF9E3C8.toInt(); canvas.drawOval(cx - 12f * u, cy - 7f * u, cx + 12f * u, cy - 1f * u, p)
            }
        }
    }

    private fun lamp(canvas: Canvas, id: String, cx: Float, base: Float, u: Float) {
        when (id) {
            "lamp_star" -> {
                s.color = 0xFF8D5A35.toInt(); s.strokeWidth = 2f * u; canvas.drawLine(cx, base, cx, base - 22f * u, s)
                star(canvas, cx, base - 27f * u, 7f * u, if (night) 0xFFFFE08A.toInt() else 0xFFFFD54F.toInt())
            }
            "lamp_lava" -> {
                p.color = 0xFF5A4FA8.toInt(); r.set(cx - 5f * u, base - 4f * u, cx + 5f * u, base); canvas.drawRect(r, p)
                p.color = 0x88B39DDB.toInt(); r.set(cx - 4f * u, base - 22f * u, cx + 4f * u, base - 4f * u); canvas.drawRoundRect(r, 4f * u, 4f * u, p)
                p.color = 0xFFF5A623.toInt(); canvas.drawCircle(cx, base - 10f * u, 2.6f * u, p); canvas.drawCircle(cx + 1f * u, base - 17f * u, 2f * u, p)
            }
            else -> {
                s.color = 0xFF5D4B3A.toInt(); s.strokeWidth = 2f * u; canvas.drawLine(cx, base, cx, base - 26f * u, s)
                p.color = 0xFF5D4B3A.toInt(); canvas.drawOval(cx - 6f * u, base - 2f * u, cx + 6f * u, base + 1.5f * u, p)
                p.color = if (night) 0xFFFFE08A.toInt() else 0xFFF6D59A.toInt()
                path.reset(); path.moveTo(cx - 5f * u, base - 34f * u); path.lineTo(cx + 5f * u, base - 34f * u); path.lineTo(cx + 9f * u, base - 25f * u); path.lineTo(cx - 9f * u, base - 25f * u); path.close()
                canvas.drawPath(path, p)
            }
        }
    }

    private fun plant(canvas: Canvas, id: String, cx: Float, base: Float, u: Float) {
        p.color = 0xFFC86B48.toInt()
        path.reset(); path.moveTo(cx - 6f * u, base - 8f * u); path.lineTo(cx + 6f * u, base - 8f * u); path.lineTo(cx + 4.5f * u, base); path.lineTo(cx - 4.5f * u, base); path.close()
        canvas.drawPath(path, p)
        when (id) {
            "plant_cactus" -> {
                p.color = 0xFF4E9A5E.toInt(); r.set(cx - 3.5f * u, base - 24f * u, cx + 3.5f * u, base - 7f * u); canvas.drawRoundRect(r, 3.5f * u, 3.5f * u, p)
                r.set(cx + 2f * u, base - 19f * u, cx + 7f * u, base - 15f * u); canvas.drawRoundRect(r, 2f * u, 2f * u, p)
                r.set(cx + 4.5f * u, base - 23f * u, cx + 7f * u, base - 16f * u); canvas.drawRoundRect(r, 1.5f * u, 1.5f * u, p)
                p.color = 0xFFF7A1A8.toInt(); canvas.drawCircle(cx, base - 25f * u, 2f * u, p)
            }
            "plant_sunflower" -> {
                s.color = 0xFF4E9A5E.toInt(); s.strokeWidth = 1.8f * u; canvas.drawLine(cx, base - 8f * u, cx, base - 28f * u, s)
                p.color = 0xFFFFC83D.toInt()
                for (i in 0 until 10) { val a = i / 10f * 2 * PI; canvas.drawCircle(cx + (cos(a) * 5f * u).toFloat(), base - 30f * u + (sin(a) * 5f * u).toFloat(), 2.6f * u, p) }
                p.color = 0xFF7A4A21.toInt(); canvas.drawCircle(cx, base - 30f * u, 3.6f * u, p)
            }
            else -> { // monstera
                p.color = 0xFF2E8B57.toInt()
                for ((dx, dy, rot) in listOf(Triple(-6f, -20f, -30f), Triple(5f, -22f, 25f), Triple(0f, -28f, 0f), Triple(-9f, -12f, -60f), Triple(8f, -13f, 60f))) {
                    canvas.save(); canvas.rotate(rot, cx + dx * u, base + dy * u)
                    canvas.drawOval(cx + dx * u - 4f * u, base + dy * u - 7f * u, cx + dx * u + 4f * u, base + dy * u + 7f * u, p)
                    canvas.restore()
                }
            }
        }
    }

    private fun toy(canvas: Canvas, id: String, cx: Float, cy: Float, u: Float) {
        when (id) {
            "toy_ball" -> {
                p.color = 0xFFFFFFFF.toInt(); canvas.drawCircle(cx, cy - 5f * u, 5.5f * u, p)
                p.color = 0xFF2B2118.toInt(); canvas.drawCircle(cx, cy - 5f * u, 2f * u, p)
                s.color = 0xFF2B2118.toInt(); s.strokeWidth = 0.8f * u; canvas.drawCircle(cx, cy - 5f * u, 5.5f * u, s)
            }
            "toy_yarn" -> {
                p.color = 0xFFD6487E.toInt(); canvas.drawCircle(cx, cy - 5f * u, 5.5f * u, p)
                s.color = 0x88FFFFFF.toInt(); s.strokeWidth = 0.9f * u
                for (i in -2..2) canvas.drawLine(cx - 4f * u, cy - 5f * u + i * 2f * u, cx + 4f * u, cy - 6f * u + i * 2f * u, s)
                s.color = 0xFFD6487E.toInt(); s.strokeWidth = 1f * u
                path.reset(); path.moveTo(cx + 5f * u, cy - 3f * u); path.quadTo(cx + 10f * u, cy + 2f * u, cx + 14f * u, cy - 1f * u); canvas.drawPath(path, s)
            }
            "toy_teddy" -> {
                p.color = 0xFFB07A4A.toInt()
                canvas.drawCircle(cx, cy - 4f * u, 5f * u, p); canvas.drawCircle(cx, cy - 11f * u, 4f * u, p)
                canvas.drawCircle(cx - 3.4f * u, cy - 14f * u, 1.6f * u, p); canvas.drawCircle(cx + 3.4f * u, cy - 14f * u, 1.6f * u, p)
                p.color = 0xFFF3D9A4.toInt(); canvas.drawCircle(cx, cy - 10f * u, 1.8f * u, p)
                p.color = 0xFF2B2118.toInt(); canvas.drawCircle(cx - 1.5f * u, cy - 12f * u, 0.6f * u, p); canvas.drawCircle(cx + 1.5f * u, cy - 12f * u, 0.6f * u, p)
            }
            "toy_books" -> {
                val cs = intArrayOf(0xFF3B7DD8.toInt(), 0xFFF5A623.toInt(), 0xFF2E9E5B.toInt())
                cs.forEachIndexed { i, c1 -> p.color = c1; r.set(cx - 8f * u + i, cy - (i + 1) * 3.4f * u, cx + 8f * u - i, cy - i * 3.4f * u); canvas.drawRoundRect(r, 1f * u, 1f * u, p) }
            }
        }
    }

    private fun star(canvas: Canvas, cx: Float, cy: Float, rr: Float, color: Int) {
        path.reset()
        for (i in 0 until 10) {
            val rad = if (i % 2 == 0) rr else rr * 0.45f
            val a = PI / 5 * i - PI / 2
            val x = cx + (cos(a) * rad).toFloat(); val y = cy + (sin(a) * rad).toFloat()
            if (i == 0) path.moveTo(x, y) else path.lineTo(x, y)
        }
        path.close(); p.color = color; canvas.drawPath(path, p)
    }
}
