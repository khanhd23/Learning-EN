# B2 vocabulary batch 86

## Scope

Added 25 manually authored B2 entries through the owner authoring workflow: `excel`, `exceptionally`, `exclaim`, `exclusion`, `execute`, `exhausting`, `exhaustion`, `exile`, `existing`, `expected`, `expedition`, `explode`, `exploit`, `explosive`, `expressive`, `exquisite`, `extraordinarily`, `exultant`, `eyelash`, `eyelid`, `fabulous`, `facilities`, `falter`, `fascinated`, and `fascination`.

Every entry was manually reviewed for its definition, part of speech, Vietnamese gloss, example pair, IPA, level, and controlled topic assignment. These entries use the shared dictionary; there is no separate per-exam copy.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,627 | 7,652 |
| Silver entries | 6,515 | 6,540 |
| Bronze entries | 1,112 | 1,112 |
| Audit errors | 2,709 | 2,709 |
| B2 missing | 1,048 | 1,023 |
| B2 bronze | 203 | 203 |
| A1–B1 missing | 2 | 2 |
| TSL missing | 0 | 0 |
| Normalized duplicate lemmas | 0 | 0 |

## Checks

- `check_editor_batch.py`: 25 rows, 0 problems.
- `words_batch.py tiers`: 0 entries below silver.
- `validate_content.py`: 0 errors, 238 existing warnings.
- `audit_content.py`: 2,709 existing errors; no increase from the batch.
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` has 0 errors.
- `gradlew.bat testDebugUnitTest assembleDebug`: passed.

