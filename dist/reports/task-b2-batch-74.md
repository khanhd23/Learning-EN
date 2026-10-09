# Content B2 batch 74

## Scope and manual review

Added 25 useful CEFR B2 vocabulary entries: billion, biotechnology, birdcage,
birthplace, bitterly, bizarre, blindly, blindness, bloodstream, blossom, blur,
blurt, bombard, bombing, bony, bookish, bookmark, bossy, botany, bothered,
bothersome, bracket, brag, bravery, and breakable.

I manually reviewed every generated entry for lemma, part of speech, IPA, level,
CEFR metadata, topic assignment, definition, Vietnamese gloss, English example,
and Vietnamese example. The batch avoids the abbreviation `bio` and unrelated
duplicate variants. All 25 entries are silver, with no template examples.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,327 | 7,352 |
| Silver entries | 6,215 | 6,240 |
| Total bronze entries | 1,112 | 1,112 |
| CEFR B2 missing entries | 1,348 | 1,323 |
| CEFR B2 bronze entries | 203 | 203 |
| CEFR A1-B1 missing / bronze | 2 / 0 | 2 / 0 |
| TOEIC missing / bronze | 0 / 0 | 0 / 0 |

## Checks

- `check_editor_batch`: rows=25, problems=0
- `words_batch.py tiers`: not silver: 0
- `validate_content.py`: errors=0, warnings=236
- `audit_content.py`: entries=7352, silver=6240, bronze=1112, ngsl_missing=0, errors=2709; no new audit errors
- `word_gaps.py cefr B2`: MISSING 1323; BRONZE 203
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` errors=0
- `gradlew.bat testDebugUnitTest assembleDebug`: BUILD SUCCESSFUL
