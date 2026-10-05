package com.yourbrand.englishlearn.content

import org.json.JSONObject
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File

class Task1A1ContentTest {
    private fun root(): File = sequenceOf(File("content"), File("../content"))
        .first { it.isDirectory }

    @Test
    fun localeRestoresMovedQuestionFields() {
        val base = root()
        val questions = JSONObject(File(base, "en/questions.json").readText())
        val viQuestions = JSONObject(File(base, "i18n/vi/questions.json").readText())
        val translations = viQuestions.optJSONObject("q_translation") ?: JSONObject()
        val fixes = viQuestions.optJSONObject("q_fix") ?: JSONObject()
        val items = questions.getJSONArray("questions")
        for (i in 0 until items.length()) {
            val question = items.getJSONObject(i)
            if (question.optString("type") == "E05") {
                assertTrue("Missing E05 translation: ${question.getString("id")}", translations.has(question.getString("id")))
            }
            if (question.optString("type") == "E04") {
                assertTrue("Missing E04 fix: ${question.getString("id")}", fixes.has(question.getString("id")))
            }
        }
    }
}
