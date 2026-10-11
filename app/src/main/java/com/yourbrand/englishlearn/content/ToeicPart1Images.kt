package com.yourbrand.englishlearn.content

/** Resource names for the original black-and-white TOEIC Part 1 scene illustrations. */
object ToeicPart1Images {
    private val byGroupId = mapOf(
        "p1_original_set1_001" to "toeic_p1_001",
        "p1_original_set1_002" to "toeic_p1_002",
        "p1_original_set1_003" to "toeic_p1_003",
        "p1_original_set1_004" to "toeic_p1_004",
        "p1_original_set1_005" to "toeic_p1_005",
        "p1_original_set1_006" to "toeic_p1_006",
        "p1_original_set2_001" to "toeic_p1_007",
        "p1_original_set2_002" to "toeic_p1_008",
        "p1_original_set2_003" to "toeic_p1_009",
        "p1_original_set2_004" to "toeic_p1_010",
        "p1_original_set2_005" to "toeic_p1_011",
        "p1_original_set2_006" to "toeic_p1_012",
        "p1_original_set3_001" to "toeic_p1_013",
        "p1_original_set3_002" to "toeic_p1_014",
        "p1_original_set3_003" to "toeic_p1_015",
        "p1_original_set3_004" to "toeic_p1_016",
        "p1_original_set3_005" to "toeic_p1_017",
        "p1_original_set3_006" to "toeic_p1_018",
        "p1_original_set4_001" to "toeic_p1_019",
        "p1_original_set4_002" to "toeic_p1_020",
        "p1_original_set4_003" to "toeic_p1_021",
        "p1_original_set4_004" to "toeic_p1_022",
        "p1_original_set4_005" to "toeic_p1_023",
        "p1_original_set4_006" to "toeic_p1_024",
        "p1_original_set5_001" to "toeic_p1_025",
        "p1_original_set5_002" to "toeic_p1_026",
        "p1_original_set5_003" to "toeic_p1_027",
        "p1_original_set5_004" to "toeic_p1_028",
        "p1_original_set5_005" to "toeic_p1_029",
        "p1_original_set5_006" to "toeic_p1_030",
    )

    fun resourceFor(groupId: String?): String? = groupId?.let(byGroupId::get)
}
