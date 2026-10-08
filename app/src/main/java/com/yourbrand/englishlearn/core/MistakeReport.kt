package com.yourbrand.englishlearn.core

import java.util.Locale

data class MistakeEmail(val subject: String, val body: String)

/** Builds the stable, user-editable email fields for a content report. */
object MistakeReport {
    fun build(itemId: String, screenName: String, versionName: String, subjectTemplate: String, bodyTemplate: String): MistakeEmail =
        MistakeEmail(
            String.format(Locale.ROOT, subjectTemplate, itemId),
            String.format(Locale.ROOT, bodyTemplate, itemId, screenName, versionName),
        )
}
