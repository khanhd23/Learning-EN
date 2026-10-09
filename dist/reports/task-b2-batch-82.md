# B2 vocabulary batch 82

## Scope

Added 25 manually authored B2 entries through the owner authoring workflow. The batch covers `amongst`, `anchorman`, `arch`, `balky`, `bidding`, `bikini`, `bio`, `bomber`, `bourbon`, `boxer`, `bulimia`, `bum`, `colon`, `countryman`, `cowardly`, `digest`, `dignify`, `dime`, `dip`, `diphtheria`, `diplomat`, `disappearance`, `disapproval`, `disapprove`, and `disciple`.

Each entry was manually checked for its sense, Vietnamese gloss, example pair, IPA, level, and controlled topic assignment. No duplicate lemma or spelling variant already present in the shared dictionary was added.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,527 | 7,552 |
| Silver entries | 6,415 | 6,440 |
| Bronze entries | 1,112 | 1,112 |
| Audit errors | 2,709 | 2,709 |
| B2 missing | 1,148 | 1,123 |
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

