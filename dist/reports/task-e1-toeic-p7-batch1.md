# E1 — TOEIC-style Part 7 batch 1

## Delivered

- Added five original Part 7 passage groups:
  - three single-passage groups;
  - one double-passage group;
  - one triple-passage group.
- Added 15 questions total, with four options, one answer, English explanation,
  and Vietnamese explanation for every question.
- Added English and Vietnamese passage/title overlays.
- Added `toeic_p7_preview` as a separate practice-preview format. It is not
  presented as a complete TOEIC Reading test and does not replace the current
  P5/P6 bank.
- Added generator-time provenance checks. Every source group is marked
  `original:exam-expansion-2026`; no external passage, question, answer key,
  audio, image, or exam screenshot was used.

## Manual educational review

I read all five passages in both languages and re-solved all 15 questions from
the final option order. I checked the answer key, distractor grammar and
plausibility, dates/times, cross-document references, and Vietnamese
explanations. No ambiguity or unsupported answer remained.

## Verification

- `python tools/gen_content.py`: pass.
- `python tools/validate_content.py`: 0 errors; 244 existing warnings about
  Vietnamese word-gloss length.
- `python tools/locale.py check --all`: `vi` 0 errors; `xx` 0 errors.
- `python -m unittest discover -s tools/tests`: 34 tests passed.
- `./gradlew.bat check testDebugUnitTest assembleDebug`: pass.
- Gradle content audit: 8,674 entries, 2,709 pre-existing audit findings; this
  batch adds no vocabulary entries or third-party source data.

## Remaining work

This is only the first reviewed preview batch. It is not enough for a full
54-question Part 7 section or a complete 100-question TOEIC Reading session.
Further original batches must be written and manually reviewed before claiming
full Part 7 coverage. The preview remains clearly labelled as original
format-practice content.
