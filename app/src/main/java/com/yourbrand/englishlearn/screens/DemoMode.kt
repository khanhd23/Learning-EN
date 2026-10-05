package com.yourbrand.englishlearn.screens

import android.content.Intent
import com.yourbrand.englishlearn.MainActivity
import com.yourbrand.englishlearn.learning.LearningStore
import com.yourbrand.englishlearn.learning.MockTest
import com.yourbrand.englishlearn.learning.Scheduler
import com.yourbrand.englishlearn.services
import com.yourbrand.englishlearn.ui.Tab

/**
 * Debug-only screenshot helper: adb shell am start -n <pkg>/.MainActivity --es demo_screen today
 * Screens: today, vocab, topic, word, grammar, lesson, exam, session, result, mock, me, pet, shop, onboarding.
 * Extras: demo_ads=0 hides ads, demo_seed=1 fills sample progress, demo_stage=N sets pet stage, demo_species, demo_theme.
 */
object DemoMode {
    fun handle(a: MainActivity, intent: Intent?) {
        val screen = intent?.getStringExtra("demo_screen") ?: return
        val s = a.services
        if (intent.getStringExtra("demo_ads") == "0") s.ads.demoNoAds = true
        if (screen == "onboarding") { s.settings.onboarded = false; a.navigator.reset(OnboardingScreen(a)); return }
        s.settings.onboarded = true
        if (intent.getStringExtra("demo_seed") == "1") seed(a)
        intent.getStringExtra("demo_stage")?.toIntOrNull()?.let { s.pet.state.stage = it.coerceIn(1, 10) }
        intent.getStringExtra("demo_species")?.let { s.pet.state.species = it }
        intent.getStringExtra("demo_theme")?.let { s.pet.state.equipped["theme"] = it; s.pet.state.owned += it }
        s.savePet()
        val b = s.builder()
        when (screen) {
            "today" -> a.selectTab(Tab.TODAY)
            "vocab" -> a.selectTab(Tab.VOCAB)
            "topic" -> { a.selectTab(Tab.VOCAB); a.open(WordListScreen(a, topicId = "office")) }
            "word" -> { a.selectTab(Tab.VOCAB); a.open(WordDetailScreen(a, "interest")) }
            "grammar" -> a.selectTab(Tab.GRAMMAR)
            "lesson" -> { a.selectTab(Tab.GRAMMAR); a.open(GrammarLessonScreen(a, "present_perfect")) }
            "exam" -> a.selectTab(Tab.EXAM)
            "me" -> a.selectTab(Tab.ME)
            "session" -> { a.selectTab(Tab.TODAY); a.startSession(b.examType("Part 5", "toeic_p5", "pos", 3)) }
            "passage" -> { a.selectTab(Tab.EXAM); a.startSession(b.examType("Part 6", "toeic_p6", null, 3)) }
            "mock" -> { a.selectTab(Tab.EXAM); a.open(MockScreen(a, MockTest.build(s.content, b, "toeic_p5", "Part 5", 10, emptySet()))) }
            "pet" -> { a.selectTab(Tab.TODAY); a.openPetHome() }
            "shop" -> { a.selectTab(Tab.TODAY); a.openPetHome() }
            "builder" -> { a.selectTab(Tab.EXAM); a.open(BuilderScreen(a)) }
            else -> a.selectTab(Tab.TODAY)
        }
    }

    /** Sample progress so dashboards are not empty in screenshots. */
    private fun seed(a: MainActivity) {
        val s = a.services
        val now = System.currentTimeMillis()
        val today = LearningStore.dayKey(now)
        if (s.store.allDays().isNotEmpty()) return
        val xpByDay = listOf(40, 65, 0, 80, 55, 90, 70, 30, 60, 75, 85, 50, 65, 45)
        xpByDay.forEachIndexed { i, xp -> s.store.addToDay(today - 13 + i, xp = xp, seconds = xp * 9, answered = xp / 4, correct = xp / 5) }
        s.content.words.take(160).forEachIndexed { i, w ->
            val st = s.store.itemOrNew(w.key)
            repeat(1 + i % 6) { Scheduler.apply(st, it != 1 || i % 4 != 0, now - 86_400_000L * 3) }
            st.due = now - (i % 3) * 3_600_000L
            s.store.saveItem(st)
        }
        s.content.grammar.take(9).forEachIndexed { i, g -> s.store.setStars(g.id, 1 + i % 3) }
        s.store.recordTags(listOf("preposition"), false); repeat(6) { s.store.recordTags(listOf("preposition", "tense"), it % 2 == 0) }
        s.pet.state.apply { stage = 4; stageXp = 300; totalXp = 1500; coins = 420; owned += listOf("theme_garden", "rug_rainbow", "plant_sunflower", "lamp_star", "toy_teddy", "hat_party", "wall_abc"); equipped.putAll(mapOf("theme" to "theme_garden", "rug" to "rug_rainbow", "plant" to "plant_sunflower", "lamp" to "lamp_star", "toy" to "toy_teddy", "wall" to "wall_abc")) ; lastStudyAt = now }
        s.savePet()
    }
}
