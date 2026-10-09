# B2 vocabulary batch 84

## Scope

Added 25 manually authored B2 entries through the owner authoring workflow: `dowry`, `drapery`, `dreamy`, `drinkable`, `droop`, `dropout`, `duplication`, `dye`, `eagerly`, `earnings`, `eastward`, `ecologically`, `ecologist`, `ecstatic`, `educated`, `educator`, `elective`, `electronics`, `elegance`, `elegantly`, `elemental`, `elite`, `eloquence`, `eloquent`, and `emergence`.

Each entry was manually reviewed for meaning, part of speech, Vietnamese gloss, example pair, IPA, level, and controlled topics. The entries are added to the shared dictionary; exam lists do not receive separate duplicate records.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,577 | 7,602 |
| Silver entries | 6,465 | 6,490 |
| Bronze entries | 1,112 | 1,112 |
| Audit errors | 2,709 | 2,709 |
| B2 missing | 1,098 | 1,073 |
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

