# B2 vocabulary batch 90

## Scope

Added 25 manually authored B2 entries: `freak`, `funk`, `gang`, `generalize`, `generosity`, `generously`, `genuinely`, `geographic`, `gymnastic`, `habitant`, `hairy`, `handicraft`, `handout`, `handrail`, `harmless`, `harrow`, `hastily`, `hatching`, `haunting`, `heartwarming`, `helicopter`, `helper`, `hemp`, `heritage`, and `hilarious`.

Each entry was manually reviewed for meaning, part of speech, Vietnamese gloss, example pair, IPA, level, and controlled topic assignment. Variant spellings and unsuitable source candidates were not duplicated.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,726 | 7,751 |
| Silver entries | 6,614 | 6,639 |
| Bronze entries | 1,112 | 1,112 |
| Audit errors | 2,709 | 2,709 |
| B2 missing | 949 | 924 |
| B2 bronze | 203 | 203 |
| A1–B1 missing | 2 | 2 |
| TSL missing | 0 | 0 |
| Normalized duplicate lemmas | 0 | 0 |

## Checks

- `check_editor_batch.py`: 25 rows, 0 problems.
- `words_batch.py tiers`: 0 entries below silver.
- `validate_content.py`: 0 errors, 239 existing warnings.
- `audit_content.py`: 2,709 existing errors; no increase from the batch.
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` has 0 errors.
- `gradlew.bat testDebugUnitTest assembleDebug`: passed.

