package com.yourbrand.englishlearn.core

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.os.Handler
import android.os.Looper
import java.util.Locale

/** Lazy system text-to-speech (en-US). No audio is bundled. */
class Tts(private val context: Context, private val slow: () -> Boolean) {
    private var tts: TextToSpeech? = null
    private var ready = false
    private var pending: Pair<String, Boolean>? = null
    private val main = Handler(Looper.getMainLooper())
    var onState: ((speaking: Boolean) -> Unit)? = null

    val available: Boolean get() = ready

    private fun init() {
        if (tts != null) return
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val r = tts?.setLanguage(Locale.US) ?: TextToSpeech.LANG_NOT_SUPPORTED
                ready = r != TextToSpeech.LANG_MISSING_DATA && r != TextToSpeech.LANG_NOT_SUPPORTED
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(id: String?) { main.post { onState?.invoke(true) } }
                    override fun onDone(id: String?) { main.post { onState?.invoke(false) } }
                    @Deprecated("Deprecated in Java") override fun onError(id: String?) { main.post { onState?.invoke(false) } }
                })
                pending?.let { (t, s) -> speak(t, s) }
                pending = null
            }
        }
    }

    fun warmUp() = init()

    fun speak(text: String, slower: Boolean = false) {
        init()
        if (!ready) { pending = text to slower; return }
        tts?.setSpeechRate(if (slower || slow()) 0.6f else 0.95f)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "u" + text.hashCode())
    }

    fun stop() { tts?.stop() }

    fun shutdown() { tts?.shutdown(); tts = null; ready = false }
}
