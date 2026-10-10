# E1 — TOEIC-style Part 7 batch 2

## Delivered

- Added ten more original Part 7 groups, bringing the bank to 15 groups and
  45 questions total.
- Added single, double, and triple-document contexts covering hotel events,
  museums, courier collections, markets, staff training, college courses,
  repair services, theatre events, catering, and office recycling.
- Every new question has four options, one answer, an English explanation, and
  a Vietnamese explanation.
- The generator now loads all `exam_p7*.json` authoring batches in stable
  order, so later batches cannot silently replace earlier content.

## Manual educational review

I read every new English and Vietnamese passage and re-solved all 30 new
questions from the final option order. I checked dates, times, locations,
cross-document references, distractor grammar, and answer explanations. No
ambiguous answer or unsupported detail remained.

## Verification

- `python tools/gen_content.py`: pass; the dedicated bank step
  `python tools/build_toeic_bank.py` was also run to regenerate the Part 7
  artifacts from all authoring batches.
- `python tools/validate_content.py`: 0 errors; 244 existing warnings about
  Vietnamese word-gloss length.
- `python tools/locale.py check --all`: `vi` 0 errors; `xx` 0 errors.
- `python -m unittest discover -s tools/tests`: 34 tests passed.
- `./gradlew.bat check testDebugUnitTest assembleDebug`: pass.
- Gradle content audit: 8,674 entries, 2,709 existing findings; this batch
  changes no vocabulary or third-party source data.

## Coverage status

The bank now contains 45 original Part 7 questions. It remains a clearly
labelled preview and is not yet enough for the planned 54-question Part 7
section. More original groups are required before enabling the full section.
