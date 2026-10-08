# LingoMori — full plan to release and beyond (handoff, 2026-10-08)

From now on **Codex does all remaining work**, code *and* content (words, questions, exam banks,
stories, pictures, UI text), following this file. Claude (owner) is away; the Publisher reviews
the reports and pushes. Older specs stay valid: `docs/ROADMAP_V1.md` (phase 3, Tasks 19–22),
`docs/ROADMAP.md` (Tasks 9–18 details), `docs/EXAM_DATA_PLAN.md` (exam data rules),
`docs/RELEASE_CHECKLIST.md`, `docs/store/*` (policy, listing, Play answers, test checklist).

## 0. How to work

- **One task = one commit** with a report `dist/reports/<task>.md`: what changed, numbers
  before/after, checks run and their results, anything left undone. **Never push.** The Publisher
  reads the report and pushes.
- Before every commit: `python tools/validate_content.py` (errors=0),
  `python -m unittest discover -s tools/tests`, `./gradlew testDebugUnitTest assembleDebug`.
  Content batches also need the checks in §2.
- Commit only files of your task. Do not reformat or re-save files you did not change (Windows:
  write text with `newline=""` / `"\n"` so line endings do not flip to CRLF).
- When unsure about a fact (exam format, a definition, a licence), stop and write the question in
  the report instead of guessing.

## 1. Where things stand (2026-10-08)

| Area | Now | Release target (v1.0) | Later |
|---|---|---|---|
| Words, lesson-ready (silver) | 4,345 of 5,746 | ≥ 4,900 | ≥ 7,000 |
| TOEIC (TSL) words | 469 missing, 75 bronze | 0 / 0 | — |
| CEFR A1–B1 words | 1,152 missing, 261 bronze | keep useful ones (§4 B2) | all |
| CEFR B2 words | 1,577 missing | — | VSTEP B2 |
| Questions | 641, all with English + Vietnamese explanations | +THPT, +TOEIC banks | — |
| THPT exam bank | 0 | 5 papers (200 items) | 20 papers |
| TOEIC P5/P6 bank on the engine | 0 | 300 P5 + 40 P6 texts | +Part 7 |
| Stories | 16 | 30 | 80 |
| Word pictures | 365 | 500 | 700 |
| Code tasks open | 20 (in progress), 21, 22 | done | 18 (TTS listening) |

Find gaps any time: `python tools/word_gaps.py tsl` and `python tools/word_gaps.py cefr A1 A2 B1`.

## 2. Rules that never change

**Licences and copyright** (details: `docs/EXAM_DATA_PLAN.md` §3–§4)
- Every word definition, example, question, passage, story and explanation is written new.
  Never copy from exam papers (MOET, ETS, Cambridge, VSTEP), prep books, Oxford/Longman/EVP lists,
  dictionaries' example sentences, or websites. Facts may come from public-domain (VOA's own
  texts) or CC BY-SA sources (Simple English Wikipedia) and must be credited per item.
- Allowed sources already in the repo: NGSL/NAWL/BSL/TSL (CC BY-SA 4.0), CEFR-J 1.5 (free with
  citation), Octanove C1/C2 (CC BY-SA 4.0), Noto Emoji (Apache-2.0), Fredoka (OFL, logo only),
  the Publisher's own panda artwork. Anything new needs its licence recorded in
  `content/LICENSES.md` first.

**Word entries** (enforced by `tools/check_editor_batch.py`)
- Definition: 1–12 words, own words (the checker rejects WordNet copies); a verb definition
  starts with "to …"; one clear sense per line, extra senses as `word_s2`, `word_s3`.
- IPA: General American, with stress marks; never the spelling itself.
- Example: a full sentence ending with . ! or ?; 5–14 words for levels 1–2, 6–14 for 3–5; it must
  contain the word (or its irregular form); no Vietnamese names or places in English text.
- Vietnamese gloss: short, natural, matching the definition; no "(thuộc)" style; mark
  `[keep]` only when the imported gloss is already right. Vietnamese example: a natural
  translation, not word for word.
- Topics: ids from `content/en/words.json` topics only; every word 1–3 topics, no `unsorted`.
- Level 1–5 by difficulty for a Vietnamese learner (CEFR A1≈1, A2≈2, B1≈3, B2≈4, C1+≈5).
- Words that are only adult/slang/offensive senses: do not add; if they exist, list them in
  `config/blocked_senses.txt`.

**UI text**
- English in `res/values/strings.xml`, Vietnamese in `res/values-vi/strings.xml`, same keys;
  escape `'` as `\'` and line breaks as `\n`; run `tools/gen_content.py` so
  `content/i18n/vi/ui.json` follows; validate must stay at errors=0.
- Wording for students 13+ and adults; never "kids", "bé", "trẻ em", "cho con".

**Learning design**
- 💡 hints teach how to solve (question-type tip, grammar formula, context sentence). A hint
  never removes options and never shows the explanation or the answer before answering.
- Explanations are shown after answering, in the learner's language (English source + vi).

**App quality**
- Smooth: build what is visible first (`addInFrames`, `visibleCount`), no frame over 50 ms after a
  transition (EngPerf log on the benchmark build).
- Ads: banner on hubs only, interstitial between sessions only, rewarded always optional, rating
  G, never auto-expanding/collapsible banners, never during a question.
- No new network SDKs, no new permissions, learning data stays on the device.

## 3. Phase A — code tasks (now)

Specs in `docs/ROADMAP_V1.md`.
- **Task 20** Report a content mistake (in progress).
- **Task 21** Pet speech bubble never hides content.
- **Task 22** Run the store screenshots (needs a device or emulator; if none, say so and stop).

Done when: all three committed with reports; tests and build pass.

## 4. Phase B — vocabulary (biggest content job)

Workflow (see the docstring of `tools/words_batch.py`): write a master file
`tools/authoring/work/bN.tsv` (new words) or a working file `work/bN.txt` (upgrades), run the
script, then `gen_content` → `check_editor_batch` (0 problems) → `gen_content` → `validate`
(errors=0) → `audit_content` → `words_batch.py tiers` (empty). Next batch number: **25**. About
150 words per batch, one commit per batch: "Content: batch N, …".

- **B1 TOEIC** — the 469 missing TSL words in TSL rank order (`word_gaps.py tsl`), then the 75
  bronze ones as upgrades (M/S lines with full definition, IPA, example, gloss). ≈4 batches.
  Done when `word_gaps.py tsl` shows 0 / 0 (skip list in the script excepted).
- **B2 CEFR A1–B1** — from `word_gaps.py cefr A1 A2 B1`: add the words a learner really meets
  (school, daily life, THPT reading); skip proper nouns, abbreviations, function-word variants
  already covered, British/American duplicates of an existing word (colour → keep colour only if
  colour is missing, otherwise add as a form). Upgrade the bronze ones. ≈8 batches.
  Target after B1+B2: silver ≥ 4,900 before release, ≥ 6,000 after.
- **B3 CEFR B2** (after release, for VSTEP) — same method.
- **B4 Archaic words**: words that are bronze, not in NGSL/NAWL/BSL/TSL/CEFR and rare
  (abeyance, aught…) stay search-only; do not spend time on them.

## 5. Phase C — THPT exam (release needs 5 papers)

**C1 Format (first, small).** Find the official MOET sample paper for the 2025+ English exam on
moet.gov.vn (or a government site quoting it). Verify: 40 items, 50 minutes, and the section
list. Current understanding (to verify, not to trust): notice gap-fill 6, leaflet/advert gap-fill
6, arrangement (dialogue/letter/paragraph) 5, text completion with sentences/phrases 5, reading
passage 8, reading passage 10. Add a format entry to `config/exam_formats.json`:
```json
{"id": "thpt", "label": "Đề thi tốt nghiệp THPT", "family": "THPT",
 "description": "40 câu trắc nghiệm, 50 phút: điền thông báo, tờ rơi, sắp xếp, điền đoạn, 2 bài đọc.",
 "questionCount": 40, "timeLimitMinutes": 50, "optionsPerQuestion": 4,
 "source": "<official URL>",
 "sections": [
   {"id": "notice", "label": "Thông báo", "itemType": "group", "count": 6, "bank": "thpt"},
   {"id": "leaflet", "label": "Tờ rơi, quảng cáo", "itemType": "group", "count": 6, "bank": "thpt"},
   {"id": "arrange", "label": "Sắp xếp câu", "itemType": "mcq", "count": 5, "bank": "thpt"},
   {"id": "complete", "label": "Điền vào đoạn văn", "itemType": "group", "count": 5, "bank": "thpt"},
   {"id": "reading1", "label": "Đọc hiểu 1", "itemType": "group", "count": 8, "bank": "thpt"},
   {"id": "reading2", "label": "Đọc hiểu 2", "itemType": "group", "count": 10, "bank": "thpt"}]}
```
Use the verified numbers and labels, not these if they differ. Put the URL in `source`.

**C2 Bank.** `content/en/exams/thpt.json` (shape in `docs/ROADMAP.md` Task 14) and
`content/i18n/vi/exams/thpt.json` (`{id: {expl}}`, Vietnamese explanation for every item).
Per paper: the six sections above. Write **5 papers = 200 items** first (one commit per paper).
- Passages: original, 120–250 words (notice/leaflet 60–120), topics from teen and school life,
  environment, technology, health, work, culture; level B1 (some A2/B2 items).
- Items: 4 options, one key, plausible distractors, no "all of the above"; arrangement items list
  the lines as a–e in the stem and orders as options ("b – d – a – e – c").
- English `expl` + Vietnamese explanation for every item, saying why the key is right and why the
  tempting distractor is wrong.
- Check: `validate_content.py` (bank checks from Task 14), every section fillable, no duplicate
  stems, answer keys spread over A–D (no letter above 35 % in a paper).
**C3** grow to 20 papers after release (before the June exam season).

## 6. Phase D — TOEIC bank on the engine

Move P5/P6 practice to `content/en/exams/toeic.json` with sections `p5` (mcq, 30) and `p6`
(group, 4 texts × 4). Write 300 P5 items and 40 P6 texts (original, business settings, TOEIC
style: grammar/vocabulary/word form/connectors). Vietnamese explanation for each. Keep the old
`toeic_p5`/`toeic_p6` formats working until the bank is complete, then point them to the bank.
Part 7 after release.

## 7. Phase E — stories and pictures

- **Stories** to 30 (Level 1–3): 80–120 words, 5–8 target words from one topic (only silver
  words), 2–3 questions with English + Vietnamese explanations, title and translation in
  Vietnamese. Source: `tools/authoring/stories.json` (same shape as the 16 existing stories;
  `tools/gen_content.py` validates targets against lesson words and fails on problems).
- **Pictures** to 500: map concrete nouns in `tools/authoring/word_images.tsv`
  (`word_id | emoji codepoints | svg name`): Noto Emoji when one fits exactly, otherwise a flat
  SVG drawn in `tools/authoring/images/` in the same style; build with `tools/build_images.py`.
  A picture must show the word unambiguously (no orange for grapefruit).

## 8. Phase F — release (Publisher + Codex)

1. Publisher: create the upload keystore, fill `signing.*` in `secrets.properties`, keep two
   backups (P3).
2. Codex: `./gradlew bundleRelease` (must pass `verifyReleaseAds`), report AAB size.
3. Publisher: Play Console → internal testing, upload the AAB; fill Data safety, content rating,
   target audience 13+, ads declaration from `docs/store/play-console-answers.md`; store listing
   from `docs/store/listing.md`, icon `docs/store/play-icon-512.png`, screenshots from Task 22.
4. Publisher: read the **pre-launch report** (Play runs the app on many phones); Codex fixes any
   crash or layout problem it shows.
5. Closed test: 12+ testers for 14 days (`docs/store/tester-invite.md`); Codex fixes feedback.
6. Production: staged rollout 20 % → 100 %.

## 9. Phase G — after release

Task 18 (TTS listening items), THPT to 20 papers, TOEIC Part 7, VSTEP B1/B2 reading banks,
Cambridge KET/PET, CEFR B2 words, more interface languages through the locale pipeline
(`docs/localization/PLAN.md`), MREC on result screens only if the Publisher asks.

## 10. Order for Codex

A (20 → 21 → 22) → B1 (TOEIC words) → C1 (THPT format) → C2 (THPT papers 1–5) → B2 (CEFR A1–B1,
alternate one word batch with one THPT paper) → D (TOEIC bank) → E (stories, pictures) → F step 2
when the Publisher has the keystore → G.

## 11. Publisher checklist (last)

- [ ] P3 keystore + `signing.*` (two backups)
- [ ] Play Console forms and listing (from `docs/store/`)
- [ ] Internal test upload, read the pre-launch report
- [ ] 12+ closed testers for 14 days
- [ ] Test each build with `docs/store/device-test-checklist.md`
- [ ] Optional: lawyer check of `docs/EXAM_DATA_PLAN.md` §4
