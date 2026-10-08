package com.yourbrand.englishlearn.screens

import android.widget.LinearLayout
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.R
import com.yourbrand.englishlearn.content.SoundFocus
import com.yourbrand.englishlearn.ui.*

/** S18 — guided IPA focus lessons backed by the owner-authored sound data. */
class IpaScreen(activity: MainActivity) : ScrollScreen(activity) {
    override val petMode = PetMode.FLOATING
    override val barTitle: String get() = str(R.string.tag_pronunciation)

    override fun build(body: LinearLayout) {
        val c = ctx
        val sound = services.content.sound
        body.addView(Kit.text(c, str(R.string.tag_pronunciation), R.style.Text_Section).margins(c, top = 4))
        body.addView(Kit.text(c, str(R.string.sound_practice_desc), R.style.Text_Body).margins(c, top = 4))
        body.addView(Kit.primary(c, str(R.string.sound_practice), 14) {
            activity.startSession(services.builder().sound(str(R.string.sound_practice)))
        })

        sound.focuses.forEach { focus -> focusCard(body, focus) }
    }

    private fun focusCard(body: LinearLayout, focus: SoundFocus) {
        val c = ctx
        val pairs = services.content.sound.pairs.filter { it.focus == focus.id }.take(4)
        body.addView(Kit.card(c, 14, 12, c.col(R.color.surface), c.col(R.color.outline)) {
            addView(Kit.text(c, focus.label, R.style.Text_Title, c.col(R.color.primary)))
            addView(Kit.text(c, str(R.string.sound_tip, focus.tip), R.style.Text_Body).margins(c, top = 6))
            if (pairs.isNotEmpty()) {
                val samples = Kit.vbox(c).margins(c, top = 10)
                pairs.forEach { pair ->
                    val row = Kit.hbox(c)
                    row.addView(Kit.secondary(c, pair.a, 0) {
                        services.tts.speak(pair.a)
                    }.apply { layoutParams = lp(0, c.dpi(48), 1f) })
                    row.addView(Kit.secondary(c, pair.b, 0) {
                        services.tts.speak(pair.b)
                    }.apply { layoutParams = lp(0, c.dpi(48), 1f).apply { marginStart = c.dpi(8) } })
                    samples.addView(row)
                }
                addView(samples)
            }
        })
    }
}
