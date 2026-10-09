# Content B2 batch 73

## Scope and manual review

Added 25 useful CEFR B2 vocabulary entries: axis, backward, backyard,
bad-tempered, balanced, ballad, ballet, ballot, barbershop, barley, barn,
barometer, bathhouse, battlefield, beagle, beak, beautify, bellow, belongings,
benevolence, benevolent, bestow, bestseller, bewilder, and bewilderment.

The American/general forms were kept where source-list variants would duplicate
the lesson (`ax/axe` and `backward/backwards` were not both added). I manually
reviewed every generated entry for lemma, part of speech, IPA, level, CEFR
metadata, topic assignment, definition, Vietnamese gloss, English example, and
Vietnamese example. I corrected the Vietnamese examples for balanced and
battlefield during review. All 25 entries are silver.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,302 | 7,327 |
| Silver entries | 6,190 | 6,215 |
| Total bronze entries | 1,112 | 1,112 |
| CEFR B2 missing entries | 1,373 | 1,348 |
| CEFR B2 bronze entries | 203 | 203 |
| CEFR A1-B1 missing / bronze | 2 / 0 | 2 / 0 |
| TOEIC missing / bronze | 0 / 0 | 0 / 0 |

## Checks

- `check_editor_batch`: rows=25, problems=0
- `words_batch.py tiers`: not silver: 0
- `validate_content.py`: errors=0, warnings=236
- `audit_content.py`: entries=7327, silver=6215, bronze=1112, ngsl_missing=0, errors=2709; no new audit errors
- `word_gaps.py cefr B2`: MISSING 1348; BRONZE 203
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` errors=0
- `gradlew.bat testDebugUnitTest assembleDebug`: BUILD SUCCESSFUL
