package com.yourbrand.englishlearn.content

import org.json.JSONObject

/** Parses the optional owner-authored exam bank format without requiring a bank at build time. */
object ExamBankParser {
    fun parse(json: String): ExamBank {
        val root = JSONObject(json)
        fun item(o: JSONObject, section: String = o.getString("section")) = ExamItem(
            o.getString("id"), section, o.optInt("level", 1), o.optString("stem"), o.getJSONArray("opts").let { a -> List(a.length()) { a.getString(it) } },
            o.getInt("ans"), o.optString("expl"), o.optString("qtype").takeIf { it.isNotBlank() },
        )
        val items = root.optJSONArray("items")?.let { a -> List(a.length()) { item(a.getJSONObject(it)) } } ?: emptyList()
        val groups = root.optJSONArray("groups")?.let { a -> List(a.length()) { i ->
            val o = a.getJSONObject(i)
            val section = o.getString("section")
            val groupItems = o.getJSONArray("items").let { values -> List(values.length()) { item(values.getJSONObject(it), section) } }
            ExamGroup(o.getString("id"), section, o.getString("passage"), o.optString("title").takeIf { it.isNotBlank() }, groupItems)
        } } ?: emptyList()
        return ExamBank(items, groups)
    }
}
