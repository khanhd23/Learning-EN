package com.yourbrand.englishlearn.core

import android.content.SharedPreferences

/**
 * Bumped whenever learner data, settings or pet state change. Screens compare it on return so an
 * unchanged screen is not torn down and rebuilt (that rebuild froze the back transition).
 */
object DataRevision {
    @Volatile var value = 0L
        private set

    fun bump() { value++ }

    // Held here so the preference listeners are not garbage-collected.
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { _, _ -> bump() }

    fun watch(prefs: SharedPreferences) = prefs.registerOnSharedPreferenceChangeListener(listener)
}
