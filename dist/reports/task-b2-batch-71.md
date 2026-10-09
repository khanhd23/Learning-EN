# Content B2 batch 71

## Scope and manual review

Added 25 useful CEFR B2 vocabulary entries: adorn, agonize, aircrew,
alcoholism, allegation, alliance, alphabetical, altitude, aluminum, amazement,
amazingly, ambiguity, amplifier, amplify, anchorage, anchorperson, animate,
anonymous, antiaircraft, antibacterial, anticancer, anticipation, antipollution,
antistatic, and antivirus.

The American spellings `agonize` and `aluminum` were selected; the British
variants remain source-list gaps rather than duplicate lessons. I manually
reviewed every generated entry for lemma, part of speech, IPA, level, CEFR
metadata, topic assignment, definition, Vietnamese gloss, English example, and
Vietnamese example. I corrected the Vietnamese examples for amazement,
amplifier, and antivirus during review. All 25 entries are silver.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,252 | 7,277 |
| Silver entries | 6,140 | 6,165 |
| Total bronze entries | 1,112 | 1,112 |
| CEFR B2 missing entries | 1,423 | 1,398 |
| CEFR B2 bronze entries | 203 | 203 |
| CEFR A1-B1 missing / bronze | 2 / 0 | 2 / 0 |
| TOEIC missing / bronze | 0 / 0 | 0 / 0 |

## Checks

- `check_editor_batch`: rows=25, problems=0
- `words_batch.py tiers`: not silver: 0
- `validate_content.py`: errors=0, warnings=236
- `audit_content.py`: entries=7277, silver=6165, bronze=1112, ngsl_missing=0, errors=2709; no new audit errors
- `word_gaps.py cefr B2`: MISSING 1398; BRONZE 203
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` errors=0
- `gradlew.bat testDebugUnitTest assembleDebug`: BUILD SUCCESSFUL
