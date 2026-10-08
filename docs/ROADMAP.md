# EngPet roadmap — phase 2: be the most fun way to learn (and pass exams)

> **Current plan: `docs/ROADMAP_V1.md` (phase 3: ship v1.0, roles Publisher / Claude / Codex).**
> This file keeps the phase 2 specs (Tasks 9–15) that phase 3 refers to.

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

### Task 11 review (owner)

Pushed before review by mistake (9298f41). Accepted with owner fixes: words said wrongly were shown
green (only missing words were red) — the grader now reports `wrongExpected` and shadowing colours
those red (test added); the mic dialog used `mic_reason` as its title — new `mic_title`. Data
safety note added to `docs/RELEASE_CHECKLIST.md`.

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

### Task 12 review (owner) — accepted with owner fix; Task 12.1

Accepted (142d72a): reader, tappable target words (gloss, slow audio, save), questions with
explanations, XP + pet meal, story buttons on topic cards. Owner fix: answer buttons stayed active —
every extra tap added a point and another explanation; now one answer per question, right/wrong
coloured. **Task 12.1** (Codex):
1. **Translation toggle** (reuse `show_translation`): shows the locale `text` under the story;
   hidden by default; remembered like word detail.
2. **Listen to the story:** play-all button (reuse `listen`) reading sentence by sentence with the
   current sentence highlighted; tap a sentence to replay it; `play_slow` for slow speed.
3. Highlight targets in any form (plural, -s/-ed/-ing, irregular forms, y→i) — reuse the forms
   logic used for example highlights; underline + tinted background, not link blue.
4. Screen chrome like other pushed screens: back arrow + title bar; questions use `OptionCard`
   like sessions; show "n/m correct" at the end before returning.
5. A weekly quest "finish stories" (Task 13) counts `finishStory()`.

### Task 12.1 review (owner)

Accepted (1c40dfb): translation toggle, play-all/slow with sentence highlight and tap-to-replay,
inflected target highlights, title bar, OptionCard questions, result view. Owner fix: the result
reused `correct_answer_is` ("Correct answer: 2/3") — new key `story_score` ("2 of 3 correct").

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

### Task 13 review (owner) — accepted; logic fixes in Task 13.1

Accepted (69f63ad): daily challenge card + deterministic 5-item session, shield cap 2, pet lines.
Problems: "weekly" quests never reset (new words = lifetime count; daily challenges = lifetime
counter, so after 3 ever the quest is always done); the daily reward re-adds `toy_ball` to a Set, so
from day 2 the learner gets nothing; speaking and stories quests are missing. **Task 13.1**:
1. Week key = Monday-based local week. Per-week counters: new words first seen this week (record a
   first-seen day in `LearningStore` when an item stops being new), daily challenges, speaking days
   (any E20 attempt that day), stories finished. Reset when the week changes. Unit tests for the
   rollover.
2. Rewards: daily challenge → coins (e.g. 10); each weekly quest → the cheapest pet item the learner
   does not own yet, otherwise coins. Persist with `savePet()`; show `quest_reward` with the item
   name.
3. Quests: `quest_new_words` 20, `quest_daily` 3, `quest_speaking` 2 (hide if speech recognition is
   unavailable), `quest_stories` 2 (Task 12). Each pays once per week; show progress per quest.

### Task 14: Exam engine

Already in the app (do not rebuild): format picker, timer (`timeLimitSec`), resume, flags, result
screen with review filters, history, mistake book. Missing: exams made of several **sections**, and
exam-specific **banks** with shared passages. THPT (the owner is writing it under C4) needs both.

1. **Sections in `config/exam_formats.json`** (optional; a format without `sections` keeps working
   exactly as today). Shape:
   `"sections": [{"id": "s1", "label": "...", "itemType": "mcq" | "group", "count": 6,
   "exerciseTypes": ["E02"], "qtype": ["..."], "bank": "thpt"}]`. Read the counts from the
   config, never from code. A mock runs sections in order with one timer for the whole exam
   (`timeLimitMinutes` stays the exam total; `null` = untimed).
2. **Exam banks**: `content/en/exams/<bank>.json` plus `content/i18n/vi/exams/<bank>.json`
   (explanations and translations, status kind `exams`, same approval gate as `questions`). Shape:
   `{"items": [{"id", "section", "level", "stem", "opts", "ans", "expl"}],
   "groups": [{"id", "section", "passage", "title"?, "items": [ ...same item shape... ]}]}`.
   A `group` shows the passage once (scrollable, pinned above the question on the same screen,
   with a toggle to collapse it) and then its 4–8 items in order. Picking questions: whole groups
   for `group` sections, never a random part of a group. Include the bank files in the
   `generateContentAssets` copy list with the same exists-filter you used for `stories.json`.
3. **Result screen**: add a score for each section (`correct/total`) above the existing
   breakdowns. Mock history keeps the overall score; also save per-section scores in the history
   JSON. Old history rows (without sections) must still load.
4. **Validation**: `tools/validate_content.py` checks the banks: unique ids, `ans` in range, 4
   options, every `section` exists in the format that uses the bank, each `group` has 1+ items,
   every section's `count` can be filled. Unit tests for picking whole groups and for loading
   formats with and without sections.
5. **Placeholder**: until the owner's bank arrives, add a test-only bank in
   `app/src/test/resources` for the unit tests. Do not create `content/en/exams/thpt.json` and do
   not write any THPT facts, counts or questions yourself: the owner provides the format entry
   and the bank.

Done when: TOEIC P5/P6 and the school format work as before, a sectioned format from the test bank
runs end to end in unit tests, validate passes, `assembleDebug` and `testDebugUnitTest` pass.
Commit, do not push.

### Task 14 review (owner) — accepted as a base; wiring in Task 14.1

Accepted (2220c51): `ExamSection`, `ExamBankParser`, `SectionedExamBuilder` (whole groups, exact
fill), section scores on the result screen, optional bank copy in Gradle, bank checks in
`validate_content.py`, tests pass. Not usable in the app yet: nothing loads a bank,
`MockTest.build`/`ExamScreen` never call `SectionedExamBuilder`, there is no passage view, the vi
bank is never read, per-section scores are not saved in history. Bug: `matches()` tests
`section.exerciseTypes.any { it == "mcq" || it == "E01" }`, so a section with
`"exerciseTypes": ["E02"]` never gets an item.

### Task 14.1: Wire the exam engine into the app

1. `ContentRepository` loads every `en/exams/<bank>.json` that a format uses (lazy, once) and
   merges `i18n/<locale>/exams/<bank>.json` (`{id: {expl, stem?, passage?}}`, approved only, like
   `questions`). Missing bank = the format card is hidden, never a crash.
2. `MockTest.build`: if the format has `sections`, use `SectionedExamBuilder`; keys look like
   `exam:<bank>:<itemId>`; store `sectionIds`/`sectionLabels` (already in the JSON). Resume works.
3. Fix `matches()`: drop the `exerciseTypes` test for bank items (banks have no E-types); keep the
   `qtype` filter.
4. Passage view: for items with a group, show the passage in a card above the question, max 40 %
   of the screen height, scrollable, with `passage_show` / `passage_hide`. Do not repeat the
   passage on every item of the same group unless the learner opens it. Show `section_n` at the
   start of each section.
5. Review screen: show the passage above grouped items; explanation from the vi bank if present,
   else English `expl`.
6. History JSON: save `sections: [{id, correct, total}]`; old rows still load.
7. Test: a format with two sections from the test bank runs `build → answer → result` in a unit
   test; keys round-trip through `toJson/fromJson`.

Strings already added by the owner: `passage_show`, `passage_hide`, `section_n`.

### Task 14.1 / R4 review (owner) — accepted with owner fixes

Accepted 9179d42 (banks loaded and merged with approved vi overrides, `matches()` fixed, section
headers, passage card, per-section history) and 1c3918d (application id
`com.khankstudio.lingomori`, verified on a device). Owner fixes: the passage collapsed again when the
screen re-rendered after an answer (now opens once per group and keeps the learner's choice);
exam items used the `ins_meaning` instruction, now `ins_choose`. 46d910e (locale flicker) did not
stop the activity restart; replaced by the owner fix in 95ed924 (manifest handles locale changes,
views rebuilt in place).

### Review of Tasks 15, R2, R3, 16, 17 and extra commits (owner, 2026-10-08)

Accepted: 8b09dbe Task 15 (only `lists`/`cefr` added; silver unchanged at 4,018), 8425a68 R2,
2b03cc9 R3, 1193412 Task 16, 49a5f30 Task 17, 4c9f519 (weekly quest ticks), f99247e (girl/daughter
distractors), fa73533 + b335e76 (IPA on word rows, IPA screen). 50b94f3: 62 E05 explanations are
correct English; Vietnamese versions still missing (owner to write).

Owner fixes: c15ad4b turned 💡 into "show the explanation before answering" (gave the answer away)
and the picture hint showed the answer word. Rule from the publisher: **a hint teaches how to solve;
it never removes options and never reveals the answer.** Now: `Exercise.Choice.method` = question-type
tip (`tips`) or grammar formula; word items show a context sentence (answer blanked when it is the
answer); word order shows the meaning + method; explanations only after answering. Strings
`hint_generic`, `hint_context`, `hint_picture`, `hint_word_order`, `hint_word_order_meaning`.

Follow-up for Codex (**Task R2.1**): the R2 report said "+242 bytes", but
`assets/licenses/dependencies.txt` is 4.07 MB (431 KB compressed) in the release APK. De-duplicate
identical licence texts and keep one copy per licence with the list of artifacts that use it; target
< 150 KB uncompressed. Report the real compressed size from `unzip -lv`.

### Task 15: Exam word lists

Owner data delivered: `config/word_lists.json` (lists, labels, credits), CEFR sources in
`tools/sources/cefrj-vocabulary-profile-1.5.csv` and `octanove-vocabulary-profile-c1c2-1.0.csv`,
strings `word_lists`, `word_lists_desc`, `n_of_m_ready`, `learn_list`, `list_source`.

1. `tools/gen_content.py`: give every word `lists` (subset of `ngsl`, `nawl`, `bsl`, `tsl`) and
   `cefr` (A1–C2 or absent). Match by lemma (lowercase; CEFR headwords like `a.m./am` split on
   `/`); when CEFR has several rows for one lemma, prefer the row whose pos matches the word's pos,
   else the lowest level. Reuse `load_list` from `tools/tag_master_packs.py` (move it into a shared
   helper). Add both fields to `build_content_db.py` (indexed columns) and to `Models.kt`.
2. Words tab: a `word_lists` section (after topics) with one card per list from the config: emoji,
   label, `n_of_m_ready` (silver words in the list / all words in the list that exist in the
   dictionary). Tap opens a list screen like a topic screen (progressive rendering, same as topics)
   with a `learn_list` button that starts a lesson from silver words of that list only (new words
   first, then due reviews). Show `list_source` with the credit at the bottom.
3. Onboarding goal (if the learner picked an exam goal) pre-selects the matching list on Today.
4. Validate: every list id unique, every `sources` value is a known list, every `cefr` value is
   A1–C2. Unit test for the CEFR matching rules.

Done when validate, `testDebugUnitTest`, `assembleDebug` pass. Commit, do not push.

### Order

Task 6.3 review (owner) → 9 → 10 → 12 → 13 → 11 → 14 → 14.1 → 15. Owner work runs in parallel:
C1 now, C2 before Task 9 ships, C5 before 10, C3 before 12, C4 before 14.
