package com.yourbrand.englishlearn.core

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.os.Handler
import android.os.Looper
import java.util.Locale

/** Lazy system text-to-speech (en-US). No audio is bundled. */
class Tts(private val context: Context, private val slow: () -> Boolean) {
    private data class SequenceRequest(val sentences: List<String>, val slower: Boolean, val gapMs: Long)

    private var tts: TextToSpeech? = null
    private var ready = false
    private var pending: Triple<String, Boolean, Float?>? = null
    private var pendingSequence: SequenceRequest? = null
    private var activeSequence: SequenceRequest? = null
    private var sequenceToken = 0
    private var sequenceIndex = -1
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
                    override fun onStart(id: String?) {
                        main.post {
                            onState?.invoke(true)
                            val parts = id?.split(':')
                            when {
                                parts?.firstOrNull() == "story" -> parts.getOrNull(1)?.toIntOrNull()?.let { onUtterance?.invoke(it) }
                                parts?.firstOrNull() == "sequence" && parts.getOrNull(1)?.toIntOrNull() == sequenceToken ->
                                    parts.getOrNull(2)?.toIntOrNull()?.let { onUtterance?.invoke(it) }
                            }
                        }
                    }
                    override fun onDone(id: String?) {
                        main.post {
                            val parts = id?.split(':')
                            if (parts?.firstOrNull() == "sequence" && parts.getOrNull(1)?.toIntOrNull() == sequenceToken) {
                                val request = activeSequence ?: return@post
                                val next = sequenceIndex + 1
                                if (next < request.sentences.size) {
                                    main.postDelayed({
                                        if (activeSequence === request && sequenceToken == parts.getOrNull(1)?.toIntOrNull()) speakSequencePart(request, next)
                                    }, request.gapMs)
                                } else {
                                    activeSequence = null
                                    sequenceIndex = -1
                                    onState?.invoke(false)
                                }
                            } else onState?.invoke(false)
                        }
                    }
                    @Deprecated("Deprecated in Java") override fun onError(id: String?) { main.post { onState?.invoke(false) } }
                })
                pending?.let { (t, s, rate) -> speakNow(t, s, rate) }
                pending = null
                pendingSequence?.let { request ->
                    pendingSequence = null
                    beginSequence(request)
                }
            }
        }
    }

    fun warmUp() = init()

    fun speak(text: String, slower: Boolean = false) {
        cancelSequence()
        init()
        if (!ready) { pending = Triple(text, slower, null); return }
        speakNow(text, slower, null)
    }

    fun speakSlow(text: String) {
        cancelSequence()
        init()
        if (!ready) { pending = Triple(text, false, 0.7f); return }
        speakNow(text, false, 0.7f)
    }

    fun speakSentences(sentences: List<String>, slower: Boolean = false, onSentence: (Int) -> Unit) {
        cancelSequence()
        init()
        onUtterance = onSentence
        if (!ready) { pending = Triple(sentences.joinToString(" "), slower, if (slower) 0.7f else null); return }
        tts?.setSpeechRate(if (slower) 0.7f else if (slow()) 0.6f else 0.95f)
        sentences.forEachIndexed { i, sentence -> tts?.speak(sentence, if (i == 0) TextToSpeech.QUEUE_FLUSH else TextToSpeech.QUEUE_ADD, null, "story:$i") }
    }

    /** Reads a short ordered audio lesson with a real pause between chunks. */
    fun speakSequence(sentences: List<String>, slower: Boolean = false, gapMs: Long = 1500L, onSentence: (Int) -> Unit = {}) {
        if (sentences.isEmpty()) return
        cancelSequence()
        onUtterance = onSentence
        val request = SequenceRequest(sentences, slower, gapMs.coerceAtLeast(0L))
        init()
        if (!ready) { pendingSequence = request; return }
        beginSequence(request)
    }

    private fun beginSequence(request: SequenceRequest) {
        sequenceToken++
        activeSequence = request
        sequenceIndex = -1
        tts?.setSpeechRate(if (request.slower) 0.7f else if (slow()) 0.6f else 0.95f)
        speakSequencePart(request, 0)
    }

    private fun speakSequencePart(request: SequenceRequest, index: Int) {
        if (activeSequence !== request || index !in request.sentences.indices) return
        sequenceIndex = index
        tts?.speak(request.sentences[index], TextToSpeech.QUEUE_FLUSH, null, "sequence:$sequenceToken:$index")
    }

    private fun cancelSequence() {
        activeSequence = null
        pendingSequence = null
        sequenceIndex = -1
        sequenceToken++
    }

    private fun speakNow(text: String, slower: Boolean, rate: Float?) {
        tts?.setSpeechRate(rate ?: if (slower || slow()) 0.6f else 0.95f)
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "u" + text.hashCode())
    }

    fun stop() { cancelSequence(); tts?.stop() }

    fun shutdown() { tts?.shutdown(); tts = null; ready = false }
}
