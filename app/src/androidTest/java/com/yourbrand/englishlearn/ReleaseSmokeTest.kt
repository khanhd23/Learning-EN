package com.yourbrand.englishlearn

import android.content.Intent
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Test
import org.junit.runner.RunWith

/** Debug-device smoke coverage for the navigation paths exercised before an R8 release. */
@RunWith(AndroidJUnit4::class)
class ReleaseSmokeTest {
    @Test
    fun demoScreensOpenWithoutCrash() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        listOf("today", "vocab", "grammar", "exam", "me", "topic", "word", "lesson", "mock", "licenses")
            .forEach { screen ->
                val intent = Intent(context, MainActivity::class.java)
                    .putExtra("demo_screen", screen)
                    .putExtra("demo_ads", "0")
                    .putExtra("demo_seed", "1")
                ActivityScenario.launch<MainActivity>(intent).use { scenario ->
                    scenario.onActivity { activity -> assertFalse("$screen finished", activity.isFinishing) }
                }
            }
    }
}
