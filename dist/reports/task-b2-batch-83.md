# B2 vocabulary batch 83

## Scope

Added 25 manually authored B2 entries through the owner authoring workflow. The batch covers `discriminate`, `dishonesty`, `disillusion`, `disorderly`, `disorganized`, `dispensable`, `displace`, `displeasure`, `disqualify`, `disregard`, `dissatisfaction`, `dissatisfied`, `dissident`, `distortion`, `distressing`, `diversion`, `divert`, `divided`, `DNA`, `doorkeeper`, `dorm`, `dormitory`, `doubly`, `dove`, and `downfall`.

The data was manually reviewed for sense, part of speech, Vietnamese gloss, example pair, IPA, level, and topic. `disorganized` was used instead of the already equivalent British spelling `disorganised`. The DNA gloss and the adverb definition for `doubly` were corrected during review.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,552 | 7,577 |
| Silver entries | 6,440 | 6,465 |
| Bronze entries | 1,112 | 1,112 |
| Audit errors | 2,709 | 2,709 |
| B2 missing | 1,123 | 1,098 |
| B2 bronze | 203 | 203 |
| A1–B1 missing | 2 | 2 |
| TSL missing | 0 | 0 |
| Normalized duplicate lemmas | 0 | 0 |

## Checks

- `check_editor_batch.py`: 25 rows, 0 problems.
- `words_batch.py tiers`: 0 entries below silver.
- `validate_content.py`: 0 errors, 237 existing warnings.
- `audit_content.py`: 2,709 existing errors; no increase from the batch.
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` has 0 errors.
- `gradlew.bat testDebugUnitTest assembleDebug`: passed.

