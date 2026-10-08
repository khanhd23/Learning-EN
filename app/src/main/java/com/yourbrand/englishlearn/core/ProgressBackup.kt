package com.yourbrand.englishlearn.core

import org.json.JSONObject

/** Versioned, self-contained progress file. It intentionally contains no content or account data. */
object ProgressBackup {
    const val FORMAT = "lingomori-progress"
    const val CURRENT_VERSION = 1

    data class Summary(val createdAt: Long, val wordCount: Int, val testCount: Int, val petStage: Int)

    fun create(createdAt: Long, settings: JSONObject, learning: JSONObject, pet: JSONObject): JSONObject =
        JSONObject()
            .put("format", FORMAT)
            .put("version", CURRENT_VERSION)
            .put("createdAt", createdAt)
            .put("settings", settings)
            .put("learning", learning)
            .put("pet", pet)

    fun parse(raw: String): JSONObject {
        val root = JSONObject(raw)
        if (root.optString("format") != FORMAT) throw IllegalArgumentException("format")
        val version = root.optInt("version", -1)
        if (version > CURRENT_VERSION) throw NewerBackupException
        if (version < 1 || !root.has("learning") || !root.has("pet") || !root.has("settings")) throw IllegalArgumentException("shape")
        return root
    }

    fun summary(root: JSONObject): Summary {
        val learning = root.getJSONObject("learning")
        val items = learning.optJSONArray("items")
        var words = 0
        if (items != null) for (i in 0 until items.length()) if (items.getJSONObject(i).optString("key").startsWith("w:")) words++
        return Summary(
            createdAt = root.optLong("createdAt", 0L),
            wordCount = words,
            testCount = learning.optJSONArray("history")?.length() ?: 0,
            petStage = root.getJSONObject("pet").optInt("stage", 1),
        )
    }

    object NewerBackupException : IllegalArgumentException("newer backup")
}
