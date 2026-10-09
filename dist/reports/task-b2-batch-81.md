# B2 vocabulary batch 81

## Scope

Added 25 manually authored B2 entries through the owner authoring workflow. The entries cover `decisive` through `dictator`; the American spelling `dehumanize` was used and the duplicate British spelling `dehumanise` was not added.

Every entry was reviewed for its definition, Vietnamese gloss, example sentence, Vietnamese example, part of speech, IPA, level, and topic assignment. Topics were assigned from the meaning and example, with no positional or default assignment.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,502 | 7,527 |
| Silver entries | 6,390 | 6,415 |
| Bronze entries | 1,112 | 1,112 |
| Audit errors | 2,709 | 2,709 |
| B2 missing | 1,173 | 1,148 |
| B2 bronze | 203 | 203 |
| A1–B1 missing | 2 | 2 |
| TSL missing | 0 | 0 |
| Normalized duplicate lemmas | 0 | 0 |

The two remaining A1–B1 gaps are intentionally excluded: `handicapped` is outdated terminology for the intended lesson, and `hearted` is normally used as part of compounds rather than as a standalone lesson.

## Checks

- `check_editor_batch.py`: 25 rows, 0 problems.
- `words_batch.py tiers`: 0 entries below silver.
- `validate_content.py`: 0 errors, 236 existing warnings.
- `audit_content.py`: 2,709 existing errors; no increase from the batch.
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` has 0 errors.
- `gradlew.bat testDebugUnitTest assembleDebug`: passed.

