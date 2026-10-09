# Content B2 batch 76

## Scope and manual review

Added 25 useful CEFR B2 vocabulary entries: caravan, cardboard, cardigan,
carefree, caring, carnival, carpool, carve, cascade, casually, catalogue,
catalyst, catastrophe, catastrophic, categorization, cathedral, cellar,
cellist, cello, cemetery, centigrade, chaotic, charming, checkpoint, and cheeky.

I manually reviewed every generated entry for lemma, part of speech, IPA, level,
CEFR metadata, topic assignment, definition, Vietnamese gloss, English example,
and Vietnamese example. The batch uses one spelling of the categorization
variant to avoid duplicate lessons. During review, the `cascade` example was
changed to an unambiguous waterfall context. All 25 entries are silver.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,377 | 7,402 |
| Silver entries | 6,265 | 6,290 |
| Total bronze entries | 1,112 | 1,112 |
| CEFR B2 missing entries | 1,298 | 1,273 |
| CEFR B2 bronze entries | 203 | 203 |
| CEFR A1-B1 missing / bronze | 2 / 0 | 2 / 0 |
| TOEIC missing / bronze | 0 / 0 | 0 / 0 |

## Checks

- `check_editor_batch`: rows=25, problems=0
- `words_batch.py tiers`: not silver: 0
- `validate_content.py`: errors=0, warnings=236
- `audit_content.py`: entries=7402, silver=6290, bronze=1112, ngsl_missing=0, errors=2709; no new audit errors
- `word_gaps.py cefr B2`: MISSING 1273; BRONZE 203
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` errors=0
- `gradlew.bat testDebugUnitTest assembleDebug`: BUILD SUCCESSFUL
