package com.yourbrand.englishlearn.content

import org.json.JSONObject

/** Parses the optional owner-authored exam bank format without requiring a bank at build time. */
object ExamBankParser {
    fun parse(json: String): ExamBank {
        val root = JSONObject(json)
        fun item(o: JSONObject, section: String = o.getString("section")) = ExamItem(
            o.getString("id"), section, o.optInt("level", 1), o.optString("stem"), o.optJSONArray("opts")?.let { a -> List(a.length()) { a.getString(it) } } ?: emptyList(),
            o.optInt("ans", -1), o.optString("expl"), o.optString("qtype").takeIf { it.isNotBlank() },
            o.optString("answerText").takeIf { it.isNotBlank() },
            o.optString("set").takeIf { it.isNotBlank() },
        )
        val items = root.optJSONArray("items")?.let { a -> List(a.length()) { item(a.getJSONObject(it)) } } ?: emptyList()
        val groups = root.optJSONArray("groups")?.let { a -> List(a.length()) { i ->
            val o = a.getJSONObject(i)
            val section = o.getString("section")
            val groupItems = o.getJSONArray("items").let { values -> List(values.length()) { item(values.getJSONObject(it), section) } }
            val passage = o.getString("passage")
            ExamGroup(o.getString("id"), section, passage, o.optString("title").takeIf { it.isNotBlank() }, groupItems, o.optString("set").takeIf { it.isNotBlank() }, passage)
        } } ?: emptyList()
        return ExamBank(items, groups)
    }

    /** Applies approved locale overrides without allowing a locale to add or remove exam items. */
    fun merge(
        base: ExamBank,
        localizedJson: String?,
        approved: Set<String>,
        translatePassages: Boolean = true,
        translateGroupTitles: Boolean = true,
        translateItemStems: Boolean = true,
    ): ExamBank {
        if (localizedJson == null) return base
        val values = JSONObject(localizedJson)
        fun override(id: String, field: String, fallback: String): String =
            if (id in approved) values.optJSONObject(id)?.optString(field, fallback) ?: fallback else fallback
        fun item(item: ExamItem) = item.copy(
            stem = if (translateItemStems) override(item.id, "stem", item.stem) else item.stem,
            explanation = override(item.id, "expl", item.explanation),
        )
        return ExamBank(base.items.map(::item), base.groups.map { group ->
            group.copy(
                // Reading practice keeps the source passage in English. Localized titles and
                // explanations remain available without replacing the text being assessed.
                passage = if (translatePassages) override(group.id, "passage", group.passage) else group.passage,
                audioPassage = group.audioPassage ?: group.passage,
                title = if (translateGroupTitles) override(group.id, "title", group.title ?: "").takeIf { it.isNotBlank() } else group.title,
                items = group.items.map(::item),
            )
        })
    }
}
