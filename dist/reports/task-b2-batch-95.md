# B2 vocabulary batch 95

## Scope

Added 25 manually authored B2 entries: `ironically`, `irrational`, `irresistible`, `irresistibly`, `irresponsible`, `irritated`, `irritating`, `irritation`, `islander`, `italicize`, `itch`, `jealously`, `jeopardize`, `jeweler`, `judo`, `juror`, `karaoke`, `keeper`, `kerosene`, `keystroke`, `kneel`, `leaflet`, `leash`, `leek`, and `legend`.

Each entry was manually reviewed for meaning, part of speech, Vietnamese gloss, example pair, IPA, level, and controlled topic assignment. American spellings were used where the equivalent British spelling was already an alternative source candidate.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,851 | 7,876 |
| Silver entries | 6,739 | 6,764 |
| Bronze entries | 1,112 | 1,112 |
| Audit errors | 2,709 | 2,709 |
| B2 missing | 824 | 799 |
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

