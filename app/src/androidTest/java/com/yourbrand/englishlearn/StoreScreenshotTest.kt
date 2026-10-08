package com.yourbrand.englishlearn

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File
import java.io.FileOutputStream

/**
 * Store capture test. Run on a 1080x1920 device; pull the output directory to dist/screenshots.
 * The activity is drawn without system bars so screenshots contain no status-bar clutter.
 */
@RunWith(AndroidJUnit4::class)
class StoreScreenshotTest {
    @Test
    fun captureStoreScreens() {
        val app = ApplicationProvider.getApplicationContext<android.content.Context>()
        val out = File(app.getExternalFilesDir(null), "store-screenshots").apply { mkdirs() }
        listOf("en" to "light", "en" to "dark", "vi" to "light", "vi" to "dark").forEach { (locale, theme) ->
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(locale))
            AppCompatDelegate.setDefaultNightMode(if (theme == "dark") AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
            listOf("today", "picture", "explanation", "exam", "story", "pet").forEach { screen ->
                val intent = android.content.Intent(app, MainActivity::class.java)
                    .putExtra("demo_screen", screen)
                    .putExtra("demo_ads", "0")
                    .putExtra("demo_seed", "1")
                ActivityScenario.launch<MainActivity>(intent).use { scenario ->
                    Thread.sleep(500)
                    scenario.onActivity { activity ->
                        activity.window.decorView.systemUiVisibility =
                            View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        val view = activity.window.decorView
                        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
                        view.draw(Canvas(bitmap))
                        FileOutputStream(File(out, "task17_${locale}_${theme}_$screen.png")).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
                        bitmap.recycle()
                    }
                }
            }
        }
    }
}
