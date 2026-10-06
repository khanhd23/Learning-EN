# EngPet roadmap — phase 2: be the most fun way to learn (and pass exams)

Read `AGENTS.md` first. Roles do not change: **Codex writes code and tests only; the owner (Claude)
writes all content, data, images-as-data and UI text.** One commit per task, report in
`dist/reports/<task>.md`, never push. The localization plan (`docs/localization/PLAN.md`) still
applies to every new string and content item.

## Why people would switch to us

Competitors each do one thing: games and streaks (Duolingo), pronunciation (ELSA), video clips
(Cake), flashcards (Quizlet/Memrise), dictionaries (TFlat), kids' pictures (Monkey). Most explain
in English or in one language, need an account, and are heavy.

EngPet combines what none of them has together:

1. **A pet that lives on your learning** — fed by correct answers, reacts during lessons (done),
   grows, decorates; daily and weekly pet quests turn practice into care, not chores.
2. **Every answer explained in your language** — grammar, questions and words (done for vi/en;
   the locale pipeline adds more languages).
3. **One app for school and certificates** — THPT, TOEIC, IELTS, VSTEP, Cambridge, each with the
   real format, timing and scoring, built on the same vocabulary and grammar core.
4. **Many ways to learn the same word** — picture, sound, story, spelling, speaking, games — mixed
   by spaced repetition so it sticks (methods with evidence, not gimmicks).
5. **Light and offline** — no login, small APK, works on cheap phones (adaptive rendering done).

Success is measured, not guessed: day-1/day-7 retention, lessons per day, recall accuracy on review
items after 1/7/30 days. Every feature below logs the minimum needed for these (on device only).

## Principles for every feature

- **Effective first:** retrieval (the learner produces or chooses, never just reads), spacing,
  interleaving, dual coding (picture + sound + word), words met in context.
- **Fun second, but always:** short (≤ 5 min), instant feedback, the pet reacts, variety every
  session (never 10 identical exercises in a row).
- **Light:** no new network SDKs; images are vectors; total image budget ≤ 3 MB.
- **Smooth:** new screens use `addInFrames` / `visibleCount` and are checked on the `benchmark`
  build (`EngPerf` log: no frame > 50 ms after the slide-in).

---

## Owner tasks (Claude — content and data)

- **C1 Topics.** 2,399 of 5,336 words have no topic, so topics look thin (university: 3, cooking: 5).
  Assign topics to every lesson-ready word; merge `school` + `university` → `education`,
  `cooking` → `food_drink` ("Food & cooking"), `meetings` → `office`; add topics where real demand
  exists (kitchen, pets, fruit & vegetables, at the doctor, airport, social media, exam words).
  Target: every topic ≥ 40 lesson-ready words.
- **C2 Pictures.** Map every concrete noun (animals, food, furniture, clothes, body, transport,
  tools, places; ~700 words) to an image: an emoji from Noto Emoji (Apache-2.0) when one exists,
  otherwise an owner-drawn SVG in the same flat style (`tools/authoring/images/*.svg`). Data file:
  `tools/authoring/word_images.tsv` (`word_id | emoji codepoints | svg name`).
- **C3 Micro-stories.** Per topic and level: 80–120-word stories using 5–8 target words, with 2–3
  questions and explanations (English source + vi). Start with Level 1–2 topics.
- **C4 Exams.** Verified formats (official sources only) and question banks, one exam at a time:
  THPT first (largest Vietnamese audience, 100 % multiple choice), then TOEIC Part 7, VSTEP reading,
  IELTS academic vocabulary/reading, Cambridge KET/PET (with pictures).
- **C5 Sound.** Minimal-pair lists (ship/sheep, light/right, …), dictation sentences picked from
  curated examples, shadowing sentences per level.
- **C6 Pet lines** for every new mode and quest (vi + English source).

## Codex tasks (code only)

### Task 9: Picture learning

- `tools/build_images.py`: reads `word_images.tsv`; for emoji, takes the SVG from a pinned Noto
  Emoji release (vendored under `third_party/noto-emoji/` with its LICENSE; record version + hash);
  for owner SVGs, reads `tools/authoring/images/`. Converts all to Android VectorDrawable XML in a
  generated resource dir at build time (like content assets). Fails the build if the total exceeds
  3 MB or an SVG uses unsupported features. Writes a third-party notice entry for Noto.
- App: `Word.image` (resource name or null). Word list and word detail show the image.
- New exercises (registered in `Exercise`/`ExerciseFactory`, used by `SessionBuilder` for words
  with images): **E15 picture → choose word**, **E16 hear word → choose picture** (2×2 grid),
  **E17 match pictures to words**. Mixed into normal sessions, never more than 3 in a row.
- Tests: builder (size budget, missing image, bad SVG), factory picks only words with images.

### Task 10: Listen and type (dictation) + minimal pairs

- **E18 dictation:** play a sentence (TTS, slow replay button), learner types it; grade per word
  with tolerance for case/punctuation; show the diff.
- **E19 minimal pair:** play one word of a pair, choose which one was said.
- Data comes from `content/en/sound.json` (owner, C5). Tests for the grader.

### Task 11: Speaking (shadowing)

- **E20 say it:** show and play a sentence, learner speaks; Android `SpeechRecognizer` (offline when
  available); compare words, colour each word green/red; never block a lesson if recognition is
  unavailable (skip the exercise, no error). Ask microphone permission only when first used.

### Task 12: Story reader

- Format `content/en/stories.json` (+ locale packs through the existing approval gate).
- Reader screen: text with target words highlighted, tap a word → mini card (gloss, audio, save),
  play-all TTS with sentence highlight, then the 2–3 questions with explanations; XP and pet food.
- Stories unlock after the topic's first lesson; the topic screen shows them.

### Task 13: Daily challenge and pet quests

- Daily challenge: 5 mixed items from due + weak items, a different exercise mix each day, pet
  reward; streak freeze earned by 7-day streaks (max 2).
- Weekly pet quests (e.g. "learn 20 new words", "finish 2 stories", "3 days of speaking"); rewards
  are pet items. Lines from C6.

### Task 14: Exam engine

- Generic exam definition in `config/exam_formats.json`: sections, item types, counts, time per
  section, scoring/band tables — only owner-verified values; `null` stays "not officially
  defined". Timed mode, review screen with explanations, history per exam.
- First exam: THPT (once C4 delivers the bank). The existing TOEIC P5/P6 move onto the engine.

### Order

Task 6.3 review (owner) → 9 → 10 → 12 → 13 → 11 → 14. Owner work runs in parallel:
C1 now, C2 before Task 9 ships, C5 before 10, C3 before 12, C4 before 14.
