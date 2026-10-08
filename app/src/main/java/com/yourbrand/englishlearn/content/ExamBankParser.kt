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

    /** Applies approved locale overrides without allowing a locale to add or remove exam items. */
    fun merge(base: ExamBank, localizedJson: String?, approved: Set<String>): ExamBank {
        if (localizedJson == null) return base
        val values = JSONObject(localizedJson)
        fun override(id: String, field: String, fallback: String): String =
            if (id in approved) values.optJSONObject(id)?.optString(field, fallback) ?: fallback else fallback
        fun item(item: ExamItem) = item.copy(
            stem = override(item.id, "stem", item.stem),
            explanation = override(item.id, "expl", item.explanation),
        )
        return ExamBank(base.items.map(::item), base.groups.map { group ->
            group.copy(
                passage = override(group.id, "passage", group.passage),
                title = override(group.id, "title", group.title ?: "").takeIf { it.isNotBlank() },
                items = group.items.map(::item),
            )
        })
    }
}
