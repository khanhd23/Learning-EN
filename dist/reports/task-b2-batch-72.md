# Content B2 batch 72

## Scope and manual review

Added 25 useful CEFR B2 vocabulary entries: apostrophe, appreciative,
appropriately, archaeologist, arctic, aristocracy, armful, arrogant, articulate,
artificially, artistry, ash, aspiration, aspire, astonish, astonished,
astonishing, athletics, attached, attainable, attentively, attractiveness,
atypical, aubergine, and authoritative.

The American/general forms were kept where a duplicate variant was already
available or less useful (`archeologist` and `authorise` were not duplicated).
I manually reviewed every generated entry for lemma, part of speech, IPA,
level, CEFR metadata, topic assignment, definition, Vietnamese gloss, English
example, and Vietnamese example. I corrected the Vietnamese examples for
articulate, aspiration, and astonish during review. All 25 entries are silver.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,277 | 7,302 |
| Silver entries | 6,165 | 6,190 |
| Total bronze entries | 1,112 | 1,112 |
| CEFR B2 missing entries | 1,398 | 1,373 |
| CEFR B2 bronze entries | 203 | 203 |
| CEFR A1-B1 missing / bronze | 2 / 0 | 2 / 0 |
| TOEIC missing / bronze | 0 / 0 | 0 / 0 |

## Checks

- `check_editor_batch`: rows=25, problems=0
- `words_batch.py tiers`: not silver: 0
- `validate_content.py`: errors=0, warnings=236
- `audit_content.py`: entries=7302, silver=6190, bronze=1112, ngsl_missing=0, errors=2709; no new audit errors
- `word_gaps.py cefr B2`: MISSING 1373; BRONZE 203
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` errors=0
- `gradlew.bat testDebugUnitTest assembleDebug`: BUILD SUCCESSFUL
