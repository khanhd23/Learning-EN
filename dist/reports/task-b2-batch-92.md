# B2 vocabulary batch 92

## Scope

Added 25 manually authored B2 entries: `hyperbole`, `hyphen`, `iceman`, `icon`, `ideology`, `idolize`, `imitation`, `immerse`, `immoral`, `immortal`, `immortality`, `impartial`, `impatiently`, `impermanent`, `impolite`, `impolitely`, `impossibility`, `impractical`, `impressed`, `impressionism`, `impressionist`, `imprison`, `imprisonment`, `improbable`, and `improperly`.

Every entry was manually reviewed for meaning, part of speech, Vietnamese gloss, example pair, IPA, level, and controlled topic assignment. The dictionary remains shared across exam lists; no duplicate per-exam records were created.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,776 | 7,801 |
| Silver entries | 6,664 | 6,689 |
| Bronze entries | 1,112 | 1,112 |
| Audit errors | 2,709 | 2,709 |
| B2 missing | 899 | 874 |
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

