package com.yourbrand.englishlearn.ui

import android.graphics.Typeface
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan

object Spans {
    /** Renders "[x]" markup as underlined text (pronunciation items, synonym stems). */
    fun underline(src: String, color: Int? = null): CharSequence {
        val sb = SpannableStringBuilder()
        var i = 0
        while (i < src.length) {
            val open = src.indexOf('[', i)
            if (open < 0) { sb.append(src.substring(i)); break }
            val close = src.indexOf(']', open)
            if (close < 0) { sb.append(src.substring(i)); break }
            sb.append(src.substring(i, open))
            val start = sb.length
            sb.append(src.substring(open + 1, close))
            sb.setSpan(UnderlineSpan(), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sb.setSpan(StyleSpan(Typeface.BOLD), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            color?.let { sb.setSpan(ForegroundColorSpan(it), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }
            i = close + 1
        }
        return sb
    }

    /** Replaces "___" with [fill] (or a wide blank) and highlights it. */
    fun blank(stem: String, fill: String?, color: Int, bg: Int): CharSequence {
        val sb = SpannableStringBuilder()
        val idx = stem.indexOf("___")
        if (idx < 0) return underline(stem)
        sb.append(underline(stem.substring(0, idx)))
        val start = sb.length
        sb.append(if (fill != null) " $fill " else " ______ ")
        sb.setSpan(ForegroundColorSpan(color), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.setSpan(BackgroundColorSpan(bg), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.setSpan(StyleSpan(Typeface.BOLD), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        sb.append(underline(stem.substring(idx + 3)))
        return sb
    }

    /** Highlights [part] inside [text] (grammar examples). */
    fun highlight(text: String, part: String, color: Int, bg: Int): CharSequence {
        val sb = SpannableStringBuilder(text)
        if (part.isBlank()) return sb
        val i = text.indexOf(part, ignoreCase = true)
        if (i >= 0) {
            sb.setSpan(ForegroundColorSpan(color), i, i + part.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sb.setSpan(BackgroundColorSpan(bg), i, i + part.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            sb.setSpan(StyleSpan(Typeface.BOLD), i, i + part.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        return sb
    }

    fun bold(text: String): CharSequence = SpannableStringBuilder(text).apply { setSpan(StyleSpan(Typeface.BOLD), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE) }

    /**
     * Part 6 passage: blanks {1}..{n}. Answered blanks show their (correct) text, the current blank is
     * highlighted, later blanks show their number.
     */
    fun passage(text: String, filled: Map<Int, String>, current: Int, accent: Int, accentBg: Int, done: Int): CharSequence {
        val sb = SpannableStringBuilder()
        val rx = Regex("\\{(\\d+)\\}")
        var last = 0
        for (m in rx.findAll(text)) {
            sb.append(text.substring(last, m.range.first))
            val n = m.groupValues[1].toInt() - 1
            val start = sb.length
            when {
                filled.containsKey(n) -> {
                    sb.append(filled[n])
                    sb.setSpan(ForegroundColorSpan(done), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    sb.setSpan(StyleSpan(Typeface.BOLD), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                n == current -> {
                    sb.append("  (${n + 1}) ______  ")
                    sb.setSpan(ForegroundColorSpan(accent), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    sb.setSpan(BackgroundColorSpan(accentBg), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    sb.setSpan(StyleSpan(Typeface.BOLD), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                }
                else -> sb.append(" (${n + 1}) _____ ")
            }
            last = m.range.last + 1
        }
        sb.append(text.substring(last))
        return sb
    }
}
