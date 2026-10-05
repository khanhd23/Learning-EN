package com.yourbrand.englishlearn.core

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/** Keeps the learner-content locale and the AppCompat UI locale in sync. */
object AppLocale {
    fun languageTag(contentLocale: String): String = when (contentLocale.trim().replace('_', '-')) {
        "" -> "en"
        else -> contentLocale.trim().replace('_', '-')
    }

    fun initialLocale(deviceTag: String, selectable: Collection<String>): String {
        val normalized = deviceTag.trim().replace('_', '-').lowercase()
        val exact = selectable.firstOrNull { it.lowercase() == normalized }
        if (exact != null) return exact
        val language = normalized.substringBefore('-')
        return selectable.firstOrNull { it.lowercase() == language } ?: "en"
    }

    /** System per-app locale overrides the stored choice only when it maps to a shipped locale. */
    fun resolve(stored: String, systemTag: String, selectable: Collection<String>): String {
        val normalized = systemTag.trim().replace('_', '-').lowercase()
        if (normalized.isBlank()) return stored
        val exact = selectable.firstOrNull { it.lowercase() == normalized }
        if (exact != null) return exact
        val language = normalized.substringBefore('-')
        return selectable.firstOrNull { it.lowercase() == language } ?: stored
    }

    fun apply(contentLocale: String) {
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(languageTag(contentLocale)))
    }
}
