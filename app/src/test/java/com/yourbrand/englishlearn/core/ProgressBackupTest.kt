package com.yourbrand.englishlearn.core

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Test

class ProgressBackupTest {
    @Test
    fun roundTripKeepsFormatAndSummary() {
        val learning = JSONObject()
            .put("items", JSONArray().put(JSONObject().put("key", "w:permit")).put(JSONObject().put("key", "q:q1")))
            .put("history", JSONArray().put(JSONObject().put("kind", "SESSION")))
        val root = ProgressBackup.create(1234L, JSONObject().put("weeklyNewWords", 20), learning, JSONObject().put("stage", 4))

        val restored = ProgressBackup.parse(root.toString())
        val summary = ProgressBackup.summary(restored)
        assertEquals(ProgressBackup.FORMAT, restored.getString("format"))
        assertEquals(ProgressBackup.CURRENT_VERSION, restored.getInt("version"))
        assertEquals(1, summary.wordCount)
        assertEquals(1, summary.testCount)
        assertEquals(4, summary.petStage)
    }

    @Test
    fun olderV1FileWithOptionalSectionsMissingIsAccepted() {
        val root = JSONObject()
            .put("format", ProgressBackup.FORMAT)
            .put("version", 1)
            .put("settings", JSONObject())
            .put("learning", JSONObject().put("items", JSONArray()))
            .put("pet", JSONObject().put("stage", 1))

        assertEquals(0, ProgressBackup.summary(ProgressBackup.parse(root.toString())).wordCount)
    }

    @Test
    fun newerVersionIsRejectedBeforeRestore() {
        val root = JSONObject()
            .put("format", ProgressBackup.FORMAT)
            .put("version", ProgressBackup.CURRENT_VERSION + 1)
            .put("settings", JSONObject()).put("learning", JSONObject()).put("pet", JSONObject())

        assertThrows(ProgressBackup.NewerBackupException::class.java) { ProgressBackup.parse(root.toString()) }
    }
}
