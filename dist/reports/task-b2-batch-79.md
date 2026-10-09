# Content B2 batch 79

## Scope and manual review

Added 25 useful CEFR B2 vocabulary entries: converse, conversely, conversion,
convinced, cooperative, copper, coral, cornerstone, cornet, corona, corporal,
correctness, corridor, corrupt, corruption, coziness, cottage, countrywide,
coward, craftsman, crater, crease, creatively, creek, and creepy.

I manually reviewed every generated entry for lemma, part of speech, IPA, level,
CEFR metadata, topic assignment, definition, Vietnamese gloss, English example,
and Vietnamese example. `corporal` uses the concrete military-rank sense so the
lesson example is clear. All 25 entries are silver.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,452 | 7,477 |
| Silver entries | 6,340 | 6,365 |
| Total bronze entries | 1,112 | 1,112 |
| CEFR B2 missing entries | 1,223 | 1,198 |
| CEFR B2 bronze entries | 203 | 203 |
| CEFR A1-B1 missing / bronze | 2 / 0 | 2 / 0 |
| TOEIC missing / bronze | 0 / 0 | 0 / 0 |

## Checks

- `check_editor_batch`: rows=25, problems=0
- `words_batch.py tiers`: not silver: 0
- `validate_content.py`: errors=0, warnings=236
- `audit_content.py`: entries=7477, silver=6365, bronze=1112, ngsl_missing=0, errors=2709; no new audit errors
- `word_gaps.py cefr B2`: MISSING 1198; BRONZE 203
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` errors=0
- `gradlew.bat testDebugUnitTest assembleDebug`: BUILD SUCCESSFUL
