# Content B2 batch 75

## Scope and manual review

Added 25 useful CEFR B2 vocabulary entries: breakdown, breakup, breathtaking,
breezy, brewery, brilliantly, broadband, broadcaster, broadly, brother-in-law,
bruise, bumper, bun, burdensome, bureaucracy, burglary, burgle, burial, bustle,
bustling, buzz, calmly, cannonball, canon, and caption.

I manually reviewed every generated entry for lemma, part of speech, IPA, level,
CEFR metadata, topic assignment, definition, Vietnamese gloss, English example,
and Vietnamese example. The checker caught one overly long gloss for
`broadcaster`; I shortened it to `đài phát sóng`, regenerated the data, and
rechecked the complete batch. All 25 entries are silver.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,352 | 7,377 |
| Silver entries | 6,240 | 6,265 |
| Total bronze entries | 1,112 | 1,112 |
| CEFR B2 missing entries | 1,323 | 1,298 |
| CEFR B2 bronze entries | 203 | 203 |
| CEFR A1-B1 missing / bronze | 2 / 0 | 2 / 0 |
| TOEIC missing / bronze | 0 / 0 | 0 / 0 |

## Checks

- `check_editor_batch`: rows=25, problems=0
- `words_batch.py tiers`: not silver: 0
- `validate_content.py`: errors=0, warnings=236
- `audit_content.py`: entries=7377, silver=6265, bronze=1112, ngsl_missing=0, errors=2709; no new audit errors
- `word_gaps.py cefr B2`: MISSING 1298; BRONZE 203
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` errors=0
- `gradlew.bat testDebugUnitTest assembleDebug`: BUILD SUCCESSFUL
