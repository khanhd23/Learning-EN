package com.yourbrand.englishlearn.core

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import kotlin.concurrent.thread
import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.sin

/**
 * Tiny synthesized sound effects (0 KB of audio assets). Plays only when enabled and the ringer is
 * in normal mode, at a low volume (SKILL.md 10 sound policy).
 */
class Sfx(private val context: Context, private val enabled: () -> Boolean) {
    enum class Sound { TAP, CORRECT, WRONG, ALMOST, COMBO, SUCCESS, LEVEL_UP, POP }

    private val rate = 22050
    private val cache = HashMap<Sound, ShortArray>()

    private fun tone(notes: List<Pair<Double, Int>>, volume: Double = 0.18, decay: Double = 6.0): ShortArray {
        val total = notes.sumOf { it.second } * rate / 1000
        val out = ShortArray(total)
        var offset = 0
        for ((freq, ms) in notes) {
            val n = ms * rate / 1000
            for (i in 0 until n) {
                val t = i / rate.toDouble()
                val env = exp(-decay * t) * minOf(1.0, i / (rate * 0.005))
                val s = sin(2 * PI * freq * t) * 0.8 + sin(4 * PI * freq * t) * 0.2
                out[offset + i] = (s * env * volume * Short.MAX_VALUE).toInt().toShort()
            }
            offset += n
        }
        return out
    }

    private fun pcm(s: Sound): ShortArray = cache.getOrPut(s) {
        when (s) {
            Sound.TAP -> tone(listOf(1200.0 to 30), 0.08, 40.0)
            Sound.CORRECT -> tone(listOf(880.0 to 90, 1318.5 to 220), 0.16, 7.0)
            Sound.WRONG -> tone(listOf(330.0 to 120, 247.0 to 200), 0.13, 8.0)
            Sound.ALMOST -> tone(listOf(660.0 to 110, 784.0 to 160), 0.13, 8.0)
            Sound.COMBO -> tone(listOf(880.0 to 70, 1108.7 to 70, 1318.5 to 180), 0.15, 8.0)
            Sound.SUCCESS -> tone(listOf(523.3 to 120, 659.3 to 120, 784.0 to 120, 1046.5 to 360), 0.16, 4.0)
            Sound.LEVEL_UP -> tone(listOf(523.3 to 100, 659.3 to 100, 784.0 to 100, 1046.5 to 100, 1318.5 to 450), 0.17, 3.0)
            Sound.POP -> tone(listOf(1500.0 to 60), 0.1, 30.0)
        }
    }

    fun play(s: Sound) {
        if (!enabled()) return
        val am = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        if (am.ringerMode != AudioManager.RINGER_MODE_NORMAL) return
        thread(name = "sfx", priority = Thread.NORM_PRIORITY) {
            runCatching {
                val data = pcm(s)
                val track = AudioTrack.Builder()
                    .setAudioAttributes(AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build())
                    .setAudioFormat(AudioFormat.Builder().setEncoding(AudioFormat.ENCODING_PCM_16BIT).setSampleRate(rate).setChannelMask(AudioFormat.CHANNEL_OUT_MONO).build())
                    .setBufferSizeInBytes(data.size * 2)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()
                track.write(data, 0, data.size)
                track.play()
                Thread.sleep(data.size * 1000L / rate + 50)
                track.release()
            }
        }
    }
}
