package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.ExamBank
import com.yourbrand.englishlearn.content.ExamFormat
import com.yourbrand.englishlearn.content.ExamGroup
import com.yourbrand.englishlearn.content.ExamItem
import com.yourbrand.englishlearn.content.ExamSection
import kotlin.random.Random

data class PickedExamItem(
    val id: String,
    val section: String,
    val item: ExamItem,
    val group: ExamGroup? = null,
    val bank: String? = null,
)

/** Selects a sectioned exam from authored banks. Counts are always taken from the format. */
object SectionedExamBuilder {
    fun build(format: ExamFormat, bank: ExamBank, random: Random = Random.Default): List<PickedExamItem> =
        buildSections(format, random) { _, _ -> bank to null }

    /** Builds a format whose sections may draw from different banks without copying their content. */
    fun build(format: ExamFormat, banks: Map<String, ExamBank>, random: Random = Random.Default): List<PickedExamItem> =
        buildSections(format, random) { section, _ ->
            val bankId = requireNotNull(section.bank) { "section ${section.id} has no bank" }
            requireNotNull(banks[bankId]) { "missing bank $bankId" } to bankId
        }

    private fun buildSections(
        format: ExamFormat,
        random: Random,
        resolve: (ExamSection, String) -> Pair<ExamBank, String?>,
    ): List<PickedExamItem> {
        require(format.sections.isNotEmpty()) { "format has no sections" }
        return format.sections.flatMap { section ->
            val (bank, bankId) = resolve(section, section.id)
            val items = bank.items.filter { it.section == section.id && matches(it, section) }
            val groups = bank.groups.filter { it.section == section.id && belongsToSet(it.set, it.id, section.groupSet) }
            when (section.itemType) {
                "group" -> chooseGroups(section, groups, random, bankId)
                else -> items.shuffled(random).take(section.count).map { PickedExamItem(it.id, section.id, it, bank = bankId) }
            }
        }.also { picked ->
            format.sections.forEach { section -> require(picked.count { it.section == section.id } >= section.count) { "section ${section.id} cannot be filled" } }
        }
    }

    private fun matches(item: ExamItem, section: ExamSection): Boolean =
        (section.qtypes.isEmpty() || item.qtype in section.qtypes) &&
            belongsToSet(item.set, item.id, section.groupSet)

    /**
     * Some older generated banks encode the paper in stable IDs (for example p3_notice)
     * instead of repeating it in every object as a set field. Keep both encodings valid so
     * each configured paper can select a complete, deterministic bank without rewriting data.
     */
    private fun belongsToSet(explicitSet: String?, id: String, requestedSet: String?): Boolean {
        if (requestedSet == null) return true
        if (explicitSet == requestedSet) return true
        return id.startsWith("${requestedSet}_") || id.startsWith("p${requestedSet}_")
    }

    private fun chooseGroups(section: ExamSection, groups: List<ExamGroup>, random: Random, bankId: String?): List<PickedExamItem> {
        val shuffled = groups.shuffled(random)
        val chosen = exactGroups(shuffled, section.count) ?: error("section ${section.id} cannot be filled by whole groups")
        return chosen.flatMap { group -> group.items.map { PickedExamItem(it.id, section.id, it, group, bankId) } }
    }

    private fun exactGroups(groups: List<ExamGroup>, target: Int): List<ExamGroup>? {
        fun visit(index: Int, total: Int, out: List<ExamGroup>): List<ExamGroup>? {
            if (total == target) return out
            if (total > target || index == groups.size) return null
            return visit(index + 1, total + groups[index].items.size, out + groups[index]) ?: visit(index + 1, total, out)
        }
        return visit(0, 0, emptyList())
    }
}
