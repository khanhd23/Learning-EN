package com.yourbrand.englishlearn.core

import org.junit.Assert.assertEquals
import org.junit.Test

class AppLocaleTest {
    @Test
    fun deviceLocaleUsesSelectableExactOrBaseLanguageAndFallsBackToEnglish() {
        val selectable = listOf("en", "vi", "pt-BR")

        assertEquals("vi", AppLocale.initialLocale("vi-VN", selectable))
        assertEquals("pt-BR", AppLocale.initialLocale("pt-BR", selectable))
        assertEquals("en", AppLocale.initialLocale("fr-FR", selectable))
    }

    @Test
    fun appLocaleTagsNormalizeStoredContentLocale() {
        assertEquals("en", AppLocale.languageTag("en"))
        assertEquals("pt-BR", AppLocale.languageTag("pt_BR"))
        assertEquals("en", AppLocale.languageTag(""))
    }
}
