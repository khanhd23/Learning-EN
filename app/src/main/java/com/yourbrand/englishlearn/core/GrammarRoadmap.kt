package com.yourbrand.englishlearn.core

/** Chooses the grammar node that should be brought back into view. */
object GrammarRoadmap {
    /** Keep an unfinished selected node, otherwise advance to the first unfinished node. */
    fun currentId(orderedIds: List<String>, stars: Map<String, Int>, savedId: String?): String? {
        val saved = savedId?.takeIf { it in orderedIds && (stars[it] ?: 0) <= 0 }
        return saved
            ?: orderedIds.firstOrNull { (stars[it] ?: 0) <= 0 }
            ?: orderedIds.lastOrNull()
    }
}
