# Task 15 — Exam word lists

## What changed

- Added generator-owned `lists` and optional `cefr` metadata to every English word. The
  generator matches normalized lemmas against the owner-provided NGSL, NAWL, BSL and TSL
  sources. CEFR matching supports slash-separated headwords, prefers a matching part of speech,
  and otherwise chooses the lowest available CEFR level.
- Moved the shared source-list loader out of `tag_master_packs.py` into `tools/word_lists.py`.
- Added CEFR/list fields and indexed list/CEFR storage to the generated SQLite content database,
  including the `word_list` relation table and schema version 4.
- Loaded `config/word_lists.json` into the app, added the Words-tab list cards and progressive list
  screen, displayed each list credit, and limited list lessons to silver words.
- List lessons select new words before due reviews. Today preselects TOEIC, IELTS Academic, or
  Business English for the matching onboarding goal.
- Added list/CEFR validation and unit coverage for CEFR POS precedence, fallback level selection,
  source-list parsing, and new-before-due lesson ordering.

## Generated metadata counts

| Field | Count |
|---|---:|
| Words in NGSL (`ngsl`) | 2,809 |
| Words in NAWL (`nawl`) | 284 |
| Words in BSL (`bsl`) | 527 |
| Words in TSL (`tsl`) | 441 |
| CEFR A1 | 875 |
| CEFR A2 | 896 |
| CEFR B1 | 1,310 |
| CEFR B2 | 872 |
| CEFR C1 | 133 |
| CEFR C2 | 66 |

List membership is allowed to overlap. Words without a CEFR match omit `cefr`; every word has a
`lists` field, including an empty list.

## Silver-count guard

- Before generator: 4,018 silver words.
- After generator: 4,018 silver words.
- The generated English word diff was checked after removing only `lists` and `cefr`: 0 other
  word changes. No `content/i18n/vi/status.json` change was produced.

## Verification

- `python tools/gen_content.py` — pass.
- `python tools/validate_content.py` — pass, `errors=0` (253 existing warnings).
- `python -m unittest discover -s tools/tests` — pass, 26 tests.
- `./gradlew testDebugUnitTest` — pass, 27 tests.
- `./gradlew assembleDebug` — pass.

No strings or authoring files were changed. No push was performed.
