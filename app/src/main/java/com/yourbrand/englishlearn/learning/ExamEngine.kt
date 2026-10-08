package com.yourbrand.englishlearn.learning

import com.yourbrand.englishlearn.content.ExamBank
import com.yourbrand.englishlearn.content.ExamFormat
import com.yourbrand.englishlearn.content.ExamGroup
import com.yourbrand.englishlearn.content.ExamItem
import kotlin.random.Random

data class PickedExamItem(val id: String, val section: String, val item: ExamItem, val group: ExamGroup? = null)

/** Selects a sectioned exam from authored banks. Counts are always taken from the format. */
object SectionedExamBuilder {
    fun build(format: ExamFormat, bank: ExamBank, random: Random = Random.Default): List<PickedExamItem> {
        require(format.sections.isNotEmpty()) { "format has no sections" }
        return format.sections.flatMap { section ->
            val items = bank.items.filter { it.section == section.id && matches(it, section) }
            val groups = bank.groups.filter { it.section == section.id }
            when (section.itemType) {
                "group" -> chooseGroups(section, groups, random)
                else -> items.shuffled(random).take(section.count).map { PickedExamItem(it.id, section.id, it) }
            }
        }.also { picked ->
            format.sections.forEach { section -> require(picked.count { it.section == section.id } >= section.count) { "section ${section.id} cannot be filled" } }
        }
    }

    private fun matches(item: ExamItem, section: com.yourbrand.englishlearn.content.ExamSection): Boolean =
        section.qtypes.isEmpty() || item.qtype in section.qtypes

    private fun chooseGroups(section: com.yourbrand.englishlearn.content.ExamSection, groups: List<ExamGroup>, random: Random): List<PickedExamItem> {
        val shuffled = groups.shuffled(random)
        val chosen = exactGroups(shuffled, section.count) ?: error("section ${section.id} cannot be filled by whole groups")
        return chosen.flatMap { group -> group.items.map { PickedExamItem(it.id, section.id, it, group) } }
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
