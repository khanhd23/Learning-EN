# TOEIC Part 3 and Part 4 — three complete practices

## Scope

Part 3 and Part 4 now expose three independent original listening practices each.
The first practice keeps the existing complete bank; two new complete practices were
added to each part.

| Part | Groups per practice | Questions per practice | Total groups | Total questions |
|---|---:|---:|---:|---:|
| Part 3 | 13 conversations | 39 | 39 | 117 |
| Part 4 | 10 talks | 30 | 30 | 90 |

Every group contains three questions and every question has four options. The six
formats use stable set IDs 1, 2, and 3, so the app can select a whole practice
without mixing groups from other practices.

## Content review

- The new scripts, stems, distractors, explanations, and answer keys are original
  practice material with original:exam-expansion-2026 provenance in the expansion
  generator.
- Each keyed answer was checked against an exact evidence span in its audio script.
- IDs are unique within each bank; question stems are unique across each complete
  Part 3/Part 4 bank.
- Part 3 passage lengths are 74–119 words; Part 4 passage lengths are 67–95 words.
- Answer positions are balanced independently in every practice:
  - Part 3: 10/10/10/9 across A/B/C/D.
  - Part 4: 8/8/7/7 across A/B/C/D.
- The ETS URL remains a format reference only; no official question, transcript, or
  recording was copied.

## App integration

- Added toeic_p3_practice_2, toeic_p3_practice_3,
  toeic_p4_practice_2, and toeic_p4_practice_3.
- Changed the existing Practice 1 group selectors to set 1.
- Updated ExamPartCatalog to show all three sets for each part.
- Added an idempotent expansion generator hook so a later content generation does
  not revert the six complete practices to the old preview bank.
- The debug APK contains the six formats and the 117/90 question banks.

## Checks

- Manual content audit: passed for IDs, group/question counts, option counts,
  answer evidence, duplicate stems, and per-practice answer distribution.
- python -m unittest discover -s tools/tests: 68 tests passed.
- :app:testDebugUnitTest: passed.
- :app:assembleDebug: passed.
- APK asset check: passed; toeic_p3.json has 39 groups/117 questions and
  toeic_p4.json has 30 groups/90 questions.
- python tools/validate_content.py: existing unrelated failure remains:
  vi/ui is missing about_feature_grammar, about_feature_practice,
  about_feature_words, about_features_title, and about_independent_text.
  No Part 3/Part 4 validation error was reported.
