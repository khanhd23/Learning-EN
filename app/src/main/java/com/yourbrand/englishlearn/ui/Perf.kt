package com.yourbrand.englishlearn.ui

import android.util.Log
import android.view.Choreographer
import com.yourbrand.englishlearn.BuildConfig

/** Debug-only timing log (tag "EngPerf"). */
object Perf {
    fun ms(from: Long, to: Long) = "%.1fms".format((to - from) / 1e6)
    fun log(msg: String) { if (BuildConfig.PERF_LOG) Log.d("EngPerf", msg) }

    /** Logs every frame gap for [durationMs] after a navigation, to see which frames stall. */
    fun watchFrames(label: String, durationMs: Long = 900) {
        if (!BuildConfig.PERF_LOG) return
        val start = System.nanoTime()
        var last = 0L
        val gaps = StringBuilder()
        Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
            override fun doFrame(t: Long) {
                if (last != 0L) gaps.append(((t - last) / 1_000_000)).append(' ')
                last = t
                if ((System.nanoTime() - start) / 1_000_000 < durationMs) Choreographer.getInstance().postFrameCallback(this)
                else log("frames after $label (ms between frames): $gaps")
            }
        })
    }
}
