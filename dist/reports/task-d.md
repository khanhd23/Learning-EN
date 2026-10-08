# Task D — TOEIC question bank

## Delivered

- Generated `content/en/exams/toeic.json` from the authored/generated P5/P6 corpus.
- Generated `content/i18n/vi/exams/toeic.json` with Vietnamese explanations for every bank item.
- Final bank: 306 Part 5 items and 40 Part 6 passages × 4 blanks (160 Part 6 items).
- Added the `toeic` sectioned format: Part 5 selects 30 items; Part 6 selects four whole passages
  (16 items), so passages are never split.
- Updated the Exam hub default, Today TOEIC plan, and practice-by-section flow to use the bank.
- Kept legacy `toeic_p5` and `toeic_p6` formats and source exercises intact for compatibility.
- Added a generator-time rebuild and tests that reject short/incomplete banks, invalid answer ranges,
  missing explanations, duplicate IDs, or groups that do not contain four blanks.

## Manual educational QA

After every generator batch I re-solved the newly generated questions from the shuffled option order.
Across the completed bank this covered 306 Part 5 items and 160 Part 6 blanks. I checked sentence
grammar, one-answer-only context, distractor naturalness, answer keys, English explanations, and
Vietnamese teaching explanations. I also read all 40 Part 6 messages as complete business notices,
emails, reminders, or procedures. One batch contained misspelled distractors; I replaced them with
natural English and regenerated before committing. No unresolved ambiguity remains in the final bank.

## Checks

- `python tools/gen_content.py` — generated 306 P5 / 40 P6 passages / 160 P6 items.
- `python tools/validate_content.py` — `errors=0` (245 existing warnings).
- `python -m unittest discover -s tools/tests` — 32 tests passed; locale `xx` errors=0.
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.

