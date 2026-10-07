package com.yourbrand.englishlearn.learning

/** Monday-based local week key. [day] is the local day key from [LearningStore.dayKey]. */
object WeekKey {
    fun mondayOf(day: Long): Long = day - Math.floorMod(day + 3L, 7L)
}
