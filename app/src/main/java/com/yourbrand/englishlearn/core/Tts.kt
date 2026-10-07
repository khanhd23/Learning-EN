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
    private var pending: Triple<String, Boolean, Float?>? = null
    private val main = Handler(Looper.getMainLooper())
    var onState: ((speaking: Boolean) -> Unit)? = null
    var onUtterance: ((index: Int) -> Unit)? = null

    val available: Boolean get() = ready

    private fun init() {
        if (tts != null) return
        tts = TextToSpeech(context.applicationContext) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val r = tts?.setLanguage(Locale.US) ?: TextToSpeech.LANG_NOT_SUPPORTED
                ready = r != TextToSpeech.LANG_MISSING_DATA && r != TextToSpeech.LANG_NOT_SUPPORTED
                tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                    override fun onStart(id: String?) { main.post { onState?.invoke(true); id?.removePrefix("story:")?.toIntOrNull()?.let { onUtterance?.invoke(it) } } }
                    override fun onDone(id: String?) { main.post { onState?.invoke(false) } }
                    @Deprecated("Deprecated in Java") override fun onError(id: String?) { main.post { onState?.invoke(false) } }
                })
                pending?.let { (t, s, rate) -> speakNow(t, s, rate) }
                pending = null
            }
        }
    }

    fun warmUp() = init()

    fun speak(text: String, slower: Boolean = false) {
        init()
        if (!ready) { pending = Triple(text, slower, null); return }
        speakNow(text, slower, null)
    }

    fun speakSlow(text: String) {
        init()
        if (!ready) { pending = Triple(text, false, 0.7f); return }
        speakNow(text, false, 0.7f)
    }

    fun speakSentences(sentences: List<String>, slower: Boolean = false, onSentence: (Int) -> Unit) {
        init()
        onUtterance = onSentence
        if (!ready) { pending = Triple(sentences.joinToString(" "), slower, if (slower) 0.7f else null); return }
        tts?.setSpeechRate(if (slower) 0.7f else if (slow()) 0.6f else 0.95f)
        sentences.forEachIndexed { i, sentence -> tts?.speak(sentence, if (i == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD, null, "story:$i") }
    }

    private fun speakNow(text: String, slower: Boolean, rate: Float?) {
        tts?.setSpeechRate(rate ?: if (slower || slow()) 0.6f else 0.95f)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "u" + text.hashCode())
    }

    fun stop() { tts?.stop() }

    fun shutdown() { tts?.shutdown(); tts = null; ready = false }
}
