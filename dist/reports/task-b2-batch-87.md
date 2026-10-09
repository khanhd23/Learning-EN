# B2 vocabulary batch 87

## Scope

Added 24 manually authored B2 entries through the owner authoring workflow: `fate`, `father-in-law`, `fearsome`, `feat`, `ferryboat`, `fiber`, `fictional`, `fighting`, `figurative`, `filial`, `financially`, `finely`, `fingernail`, `firearm`, `fireplace`, `firmness`, `fixed`, `flawless`, `flax`, `flee`, `flexibly`, `flip`, `fluency`, and `fondly`.

Each generated entry was manually reviewed for meaning, part of speech, Vietnamese gloss, example pair, IPA, level, and controlled topic assignment. The American spelling `fiber` was selected; the British spelling `fibre` was not added as a duplicate lesson.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,652 | 7,676 |
| Silver entries | 6,540 | 6,564 |
| Bronze entries | 1,112 | 1,112 |
| Audit errors | 2,709 | 2,709 |
| B2 missing | 1,023 | 999 |
| B2 bronze | 203 | 203 |
| A1–B1 missing | 2 | 2 |
| TSL missing | 0 | 0 |
| Normalized duplicate lemmas | 0 | 0 |

## Checks

- `check_editor_batch.py`: 24 rows, 0 problems.
- `words_batch.py tiers`: 0 entries below silver.
- `validate_content.py`: 0 errors, 239 existing warnings.
- `audit_content.py`: 2,709 existing errors; no increase from the batch.
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` has 0 errors.
- `gradlew.bat testDebugUnitTest assembleDebug`: passed.
