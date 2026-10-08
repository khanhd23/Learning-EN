package com.yourbrand.englishlearn.core

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

/** Keeps the learner-content locale and the AppCompat UI locale in sync. */
object AppLocale {
    fun languageTag(contentLocale: String): String = when (contentLocale.trim().replace('_', '-')) {
        "" -> "en"
        else -> contentLocale.trim().replace('_', '-')
    }

    /** True when applying [contentLocale] would change the current per-app locale. */
    fun needsApply(currentTag: String, contentLocale: String): Boolean =
        currentTag.trim().replace('_', '-').lowercase() != languageTag(contentLocale).lowercase()

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
        val desired = languageTag(contentLocale)
        val current = AppCompatDelegate.getApplicationLocales()
        val currentTag = if (current.isEmpty) "" else current[0]?.toLanguageTag().orEmpty()
        if (!needsApply(currentTag, desired)) return
        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(desired))
    }
}
