# Content B2 batch 61

## Scope

Added 25 missing CEFR A1-B1 vocabulary entries through the owner-authoring
workflow and regenerated the content output. No generated content was edited by
hand.

The batch covers: feverishly, film-maker, finding, flunk, furthest, gall, gaol,
gee, genetically, geographical, grouping, hand-held, harbour, haunt, hip-hop,
hurriedly, illegally, immigrate, including, inject, innermost, insane, lily,
max, and pacific.

## Manual review

I reviewed every new English definition, part of speech, example sentence,
Vietnamese gloss, Vietnamese example, level, and topic assignment against the
meaning of the entry. All 25 entries have one to three meaningful topics and
silver tier. The initial hearted candidate was rejected because it is normally
used as part of compounds; it was replaced with the independently usable
adverb feverishly.

Additional data-integrity checks confirmed 25 new English word IDs, zero
changes to existing English entries, and 25 new Vietnamese sense translations.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,132 | 7,157 |
| Silver entries | 5,904 | 5,929 |
| CEFR A1-B1 entries still missing | 97 | 72 |
| Bronze entries | 1,228 | 1,228 |
| TOEIC missing entries | 0 | 0 |

## Checks

- python tools/check_editor_batch.py tools/authoring/senses_editor_batch61.tsv: rows=25 problems=0
- python tools/words_batch.py tiers tools/authoring/work/b61.txt: not silver: 0
- python tools/validate_content.py: errors=0 warnings=247
- python tools/audit_content.py: entries=7157 gold=0 silver=5929 bronze=1228 ngsl_missing=0 errors=3012
- python -m unittest discover -s tools/tests: 32 tests passed; locale xx errors 0
- gradlew.bat check assembleDebug: BUILD SUCCESSFUL
