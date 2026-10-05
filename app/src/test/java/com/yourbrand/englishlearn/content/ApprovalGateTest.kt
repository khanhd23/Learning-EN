package com.yourbrand.englishlearn.content

import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class ApprovalGateTest {
    @Test
    fun shippedVietnamesePackPassesApprovalGate() {
        val root = sequenceOf(File("content"), File("../content")).first { it.isDirectory }
        val result = ApprovalGate.evaluate(
            JSONObject(File(root, "i18n/vi/status.json").readText()),
            JSONObject(File(root, "en/words.json").readText()),
            JSONObject(File(root, "en/grammar.json").readText()),
            JSONObject(File(root, "en/questions.json").readText()),
        )
        assertTrue(result.selectable)
        assertTrue(result.approvedWords.size > 5000)
        assertTrue(1 in result.availableLevels)
    }

    @Test
    fun fakeLocaleWithOnlyLevelOneApprovedIsSelectableAndLocksLaterLevels() {
        val words = JSONObject().put("words", JSONArray().apply {
            repeat(20) { put(JSONObject().put("level", 1).put("senses", JSONArray().put(JSONObject().put("id", "l1_$it")))) }
            repeat(20) { put(JSONObject().put("level", 2).put("senses", JSONArray().put(JSONObject().put("id", "l2_$it")))) }
        })
        val grammar = JSONObject().put("points", JSONArray().apply {
            repeat(2) { put(JSONObject().put("id", "g1_$it").put("level", 1)) }
            repeat(2) { put(JSONObject().put("id", "g2_$it").put("level", 2)) }
        })
        val questions = JSONObject().put("questions", JSONArray()).put("passages", JSONArray())
        val status = JSONObject().put("entries", JSONObject().apply {
            put("ui", approvedRows("ui_key"))
            put("words", approvedRows(*Array(20) { "l1_$it" }))
            put("grammar", approvedRows("g1_0", "g1_1"))
        })

        val result = ApprovalGate.evaluate(status, words, grammar, questions)

        assertTrue(result.selectable)
        assertEquals(setOf(1), result.availableLevels)
        assertEquals(20, result.approvedWords.size)
        assertEquals(setOf("g1_0", "g1_1"), result.approvedGrammar)
    }

    @Test
    fun approvedRowWithSlashInSourceStaysApproved() {
        // Regression: on Android, org.json escapes "/" so a runtime re-hash rejected every grammar
        // formula like "I am · He/She/It is". Staleness is checked at build time instead.
        val point = JSONObject().put("id", "be").put("level", 1).put("formula", "I am · He/She/It is")
        val grammar = JSONObject().put("points", JSONArray().put(point))
        val words = JSONObject().put("words", JSONArray().put(JSONObject().put("level", 1).put("senses", JSONArray().put(JSONObject().put("id", "s1")))))
        val status = JSONObject().put("entries", JSONObject().apply {
            put("ui", approvedRows("ui_key"))
            put("words", approvedRows("s1"))
            put("grammar", JSONObject().put("be", JSONObject().put("s", "approved").put("src", "hash-from-python")))
        })

        val result = ApprovalGate.evaluate(status, words, grammar, JSONObject())

        assertEquals(setOf("be"), result.approvedGrammar)
        assertTrue(result.selectable)
    }

    @Test
    fun draftRowIsNotApproved() {
        val words = JSONObject().put("words", JSONArray().put(JSONObject().put("level", 1).put("senses", JSONArray().put(JSONObject().put("id", "s1")))))
        val status = JSONObject().put("entries", JSONObject().put("words", JSONObject().put("s1", JSONObject().put("s", "draft"))))

        val result = ApprovalGate.evaluate(status, words, JSONObject().put("points", JSONArray()), JSONObject())

        assertFalse(result.selectable)
        assertTrue(result.approvedWords.isEmpty())
    }

    private fun approvedRows(vararg keys: String): JSONObject = JSONObject().apply {
        keys.forEach { put(it, JSONObject().put("s", "approved")) }
    }
}
