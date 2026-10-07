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

**Owner data delivered (C2):** `tools/authoring/word_images.tsv` — 349 lesson words: 306 Noto
emoji (all verified to exist in Noto Emoji v2.051) + 43 owner SVGs in `tools/authoring/images/`
(generated by `draw_owner_images.py`; only `<svg>`/`<path>`, solid hex fills, ~21 KB total).
Builder must: (1) accept `-` as the codepoint separator (`1f9d1-200d-1f373`, the format stated in
the file header); (2) convert real Noto SVGs, which use `circle`, `ellipse`, `rect`, `polygon`,
`transform`, `linearGradient`/`radialGradient` (`fill="url(#…)"`) and `opacity` — either convert
these to VectorDrawable (gradients via `aapt:attr`) or pre-render to WebP (≤ 128 px), keeping the
3 MB budget; (3) fail the build if any mapped word has no image.

### Task 9 review (owner) — not done; Task 9.1

Pushed by mistake before review (ca0bd82); harmless because 0 images are built, so no picture
exercise can appear. Owner has now vendored the 306 Noto SVGs (`third_party/noto-emoji/svg/`,
`SOURCE.md`) and committed the 43 owner SVGs and instruction strings. Fix in **Task 9.1**:

1. **Exercises are wrong.** E15/E16/E17 all render four option cards that show the word *and* its
   picture, with no prompt — the answer is printed next to every picture. Required:
   - **E15 picture → word:** one large picture (≥ 120 dp) as the prompt; four word options (text
     only); string `ins_picture_word`.
   - **E16 sound → picture:** big play button (auto-play once); 2×2 grid of pictures only, no text
     until answered (then show each lemma under its picture); string `ins_listen_picture`.
   - **E17 match pictures:** four pictures and four words, tap-to-pair like the existing Matching
     exercise; string `ins_match_pictures`.
   Distractors: same topic first, then same pos; never two items with the same image.
2. **Images must render exactly.** The VectorDrawable converter rejects `ellipse`/`polygon`/
   gradients (3 of 10 sampled Noto files) and silently ignores `transform` and `opacity`, so even
   accepted files can draw wrong. Render every SVG (Noto and owner) to a 128 px WebP with resvg
   (`resvg-py`, pinned in `tools/requirements.txt`) into `res/drawable-nodpi`; keep the 3 MB budget.
3. Read Noto files from `third_party/noto-emoji/svg/`; accept `-` as the codepoint separator;
   **fail the build** if a mapped word has no image (no silent skips).
4. Licenses screen: credit Noto Emoji (Apache-2.0) from `third_party_notices.txt`.
5. Verify on the benchmark build with screenshots of E15/E16/E17 in the report.

### Task 9.1 review (owner)

Accepted (01f1fd1): E15/E16/E17 layouts match the spec, resvg → WebP renders Noto exactly, 349/349
images, 896 KB. Owner fix: release/benchmark APKs contained **0** word images — they are looked up
by name (`getIdentifier`), so the resource shrinker removed them; `res/raw/keep.xml` keeps
`@drawable/word_*` (benchmark APK 5.97 → 6.94 MB, 349 WebP). Small follow-up for later: the
distractor list is shuffled as a whole, so "same topic first" is lost — shuffle within each group.

### Task 10: Listen and type (dictation) + minimal pairs

- **E18 dictation:** play a sentence (TTS, slow replay button), learner types it; grade per word
  with tolerance for case/punctuation; show the diff.
- **E19 minimal pair:** play one word of a pair, choose which one was said.
- Data comes from `content/en/sound.json` (owner, C5). Tests for the grader.

**Owner data delivered (C5):** `content/en/sound.json` (from `tools/authoring/sound_en.json`):
20 sound focuses with tips, 42 minimal pairs, 132 dictation and 132 shadowing sentences
(`{id, text, level}`; `id` is the curated example id, so its translation is in the locale's
`words.json` `ex` map). Vietnamese tips: `content/i18n/vi/sound.json` (`focus` map), status kind
`sound`. Task 11 uses the `shadowing` list.

### Task 10 review (owner) — accepted with follow-up Task 10.1

E18/E19 models, grader and parsing are fine and harmless, but the feature is unreachable: nothing
calls `SessionBuilder.sound()` and sound items are never mixed into normal sessions. Fix in
**Task 10.1**:

1. **Entry point:** a card "Listening & pronunciation" (`sound_practice`, `sound_practice_desc`) on
   the Exam/Practice hub that starts `sound()`; also mix at most one E18 or E19 into daily review and
   topic sessions once the learner has finished 3 sessions.
2. **Strings:** E18 instruction `ins_dictation`; E19 instruction `ins_minimal_pair`; a "slow"
   replay button `play_slow` (TTS rate 0.7) on both. Do not use `type_here` as an instruction.
3. **Tips:** after an E19 answer show the focus tip (`sound_tip`, text from the locale
   `sound.json` `focus` map, English source in English mode). Load `content/i18n/<locale>/sound.json`
   through the approval gate (status kind `sound`).
4. **Dictation grading:** align words (edit distance / LCS), not by position — one missing word must
   not mark every later word wrong. Show the learner's sentence with wrong/missing words coloured
   and the correct sentence under it. Unit tests for missing, extra and swapped words.
5. Explanation for E18 is the sentence's translation (locale `words.json` `ex` map), not the
   sentence repeated.

### Task R1: Release readiness (ads + licenses)  *(Codex, code only)*

See `docs/RELEASE_CHECKLIST.md`.
1. `RequestConfiguration`: `setMaxAdContentRating(MAX_AD_CONTENT_RATING_G)` for all ad requests;
   keep `tagForChildDirectedTreatment` unspecified (target audience 13+).
2. Licenses screen: add the full license texts (Apache-2.0 for AndroidX and Noto, CC BY-SA 4.0
   summary + URL) — bundle them as assets (no network); keep `content/LICENSES.md` on top.
3. Privacy policy: Settings → Privacy policy opens the in-app text and offers the public URL from
   `config/app_config.json` (`privacyPolicyUrl`, owner fills it; hide the link while empty).
4. Search must not return bronze senses tagged adult/vulgar (`sex`, `horny`, `naughty`, `cock`,
   `bloody`, `damn`, `hell`, `ass`…): add an `adult` flag in `build_content_db.py` from a list in
   `config/blocked_senses.txt` (owner maintains the list) and filter it in search and word lists.
5. Release AAB build check in CI-style script: `./gradlew bundleRelease` must fail with test ad IDs
   (already) and pass with real ones from `secrets.properties`.

### Task 9.2: Picture distractors  *(Codex, small)*

Shuffle within groups (same topic, then same part of speech) instead of shuffling the merged list,
so "same topic first" holds. Unit test.

### Task 10.1 / 9.2 / R1 review (owner)

Accepted (016332e, f3da03b, e92f33f): sound practice card + one optional sound item after 3
sessions, slow replay, focus tips through the approval gate, edit-distance dictation diff with
tests; distractors shuffled within groups; ad content rating G, offline licenses, privacy page,
`adult` filter from `config/blocked_senses.txt`. Owner fix: `assets/licenses/apache-2.0.txt` was
not the verbatim license (≈1,200 characters missing, e.g. the "Notwithstanding the above" sentence
in §5) — replaced with the official text from apache.org. **Legal texts must always be copied
byte-for-byte from the official source, never retyped or summarised.**

### Task 11: Speaking (shadowing)

- **E20 say it:** show and play a sentence, learner speaks; Android `SpeechRecognizer` (offline when
  available); compare words, colour each word green/red; never block a lesson if recognition is
  unavailable (skip the exercise, no error). Ask microphone permission only when first used.

**Owner data delivered for Task 11:** strings `ins_say_it`, `tap_to_speak`, `listening_now`,
`you_said`, `speak_again`, `mic_reason` (shown before the permission request); sentences:
`content/en/sound.json` → `shadowing`. Match words with the dictation aligner (case/punctuation
tolerant); E20 never blocks a session — skip it when recognition is unavailable or permission is
denied. Show the result per word (green/red) and allow two retries.

### Task 12: Story reader

- Format `content/en/stories.json` (+ locale packs through the existing approval gate).
- Reader screen: text with target words highlighted, tap a word → mini card (gloss, audio, save),
  play-all TTS with sentence highlight, then the 2–3 questions with explanations; XP and pet food.
- Stories unlock after the topic's first lesson; the topic screen shows them.

**Owner data delivered (C3):** `content/en/stories.json` (from `tools/authoring/stories.json`,
validated by `gen_content.py`): 16 Level 1–2 stories, one per common topic, 70–130 words, American
spelling, `targets` = lesson word ids that appear in the text, 2–3 questions each
(`q`, `options`, `answer`, `expl`). Vietnamese pack `content/i18n/vi/stories.json`:
`{id: {title, text, expl[]}}`, status kind `stories`. Show the translation only on demand (a
"Translation" toggle like word detail), never by default.

### Task 13: Daily challenge and pet quests

- Daily challenge: 5 mixed items from due + weak items, a different exercise mix each day, pet
  reward; streak freeze earned by 7-day streaks (max 2).
- Weekly pet quests (e.g. "learn 20 new words", "finish 2 stories", "3 days of speaking"); rewards
  are pet items. Lines from C6.

**Owner data delivered for Task 13:** strings `daily_challenge`, `daily_challenge_desc`,
`daily_challenge_done`, `weekly_quests`, `quest_new_words`, `quest_stories`, `quest_speaking`,
`quest_daily`, `quest_progress`, `quest_reward`, `streak_shields`, `streak_shield_info`; pet lines
`Q_DAILY_START`, `Q_DAILY_DONE`, `Q_QUEST_DONE`, `Q_FREEZE` (vi + English source). Quests this
version: new words, daily challenges, speaking days (stories quest only after Task 12 ships).
Rewards are existing pet shop items (no new art needed). Daily challenge card on Today, above
"Review today".

### Task 14: Exam engine

- Generic exam definition in `config/exam_formats.json`: sections, item types, counts, time per
  section, scoring/band tables — only owner-verified values; `null` stays "not officially
  defined". Timed mode, review screen with explanations, history per exam.
- First exam: THPT (once C4 delivers the bank). The existing TOEIC P5/P6 move onto the engine.

### Order

Task 6.3 review (owner) → 9 → 10 → 12 → 13 → 11 → 14. Owner work runs in parallel:
C1 now, C2 before Task 9 ships, C5 before 10, C3 before 12, C4 before 14.
