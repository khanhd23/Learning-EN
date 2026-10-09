# Content B2 batch 77

## Scope and manual review

Added 25 useful CEFR B2 vocabulary entries: cheerfully, cheerfulness,
chemotherapy, cherished, chimney, chunk, circuitry, citywide, clam,
classification, clearness, clog, clumsy, coaching, coalition, coastline,
cocktail, cocoa, coherence, coherent, coincide, coincidence, collocation,
colonial, and coma.

I manually reviewed every generated entry for lemma, part of speech, IPA, level,
CEFR metadata, topic assignment, definition, Vietnamese gloss, English example,
and Vietnamese example. During review, the `collocation` example was rewritten
so the phrase being taught is grammatical and explicit. All 25 entries are
silver.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,402 | 7,427 |
| Silver entries | 6,290 | 6,315 |
| Total bronze entries | 1,112 | 1,112 |
| CEFR B2 missing entries | 1,273 | 1,248 |
| CEFR B2 bronze entries | 203 | 203 |
| CEFR A1-B1 missing / bronze | 2 / 0 | 2 / 0 |
| TOEIC missing / bronze | 0 / 0 | 0 / 0 |

## Checks

- `check_editor_batch`: rows=25, problems=0
- `words_batch.py tiers`: not silver: 0
- `validate_content.py`: errors=0, warnings=236
- `audit_content.py`: entries=7427, silver=6315, bronze=1112, ngsl_missing=0, errors=2709; no new audit errors
- `word_gaps.py cefr B2`: MISSING 1248; BRONZE 203
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` errors=0
- `gradlew.bat testDebugUnitTest assembleDebug`: BUILD SUCCESSFUL
