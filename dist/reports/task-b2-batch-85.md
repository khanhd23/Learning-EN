# B2 vocabulary batch 85

## Scope

Added 25 manually authored B2 entries through the owner authoring workflow: `emphatically`, `endeavor`, `endlessly`, `endorse`, `endurance`, `enduring`, `enforce`, `engulf`, `entertaining`, `equity`, `erect`, `erupt`, `eruption`, `essayist`, `essentially`, `estimated`, `eternally`, `ethic`, `evacuation`, `evidently`, `exactness`, `exaggerate`, `exaggeration`, `exalt`, and `examinee`.

Every entry was manually reviewed for its sense, part of speech, Vietnamese gloss, example pair, IPA, level, and controlled topics. The American spelling `endeavor` was selected; the British spelling `endeavour` was not added as a duplicate lesson.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,602 | 7,627 |
| Silver entries | 6,490 | 6,515 |
| Bronze entries | 1,112 | 1,112 |
| Audit errors | 2,709 | 2,709 |
| B2 missing | 1,073 | 1,048 |
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

