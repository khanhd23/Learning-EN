# Content B2 batch 64

## Scope and manual review

Added 19 CEFR A1-B1 entries through the authoring workflow:
hiv, hiphop, ness, northeast, northeastern, northwest, northwestern,
noticeable, organisation, out-of-date, packing, paid, paralyse, paralyze,
phoenix, recognise, specialise, sports, summarise.

I manually reviewed every English definition, part of speech, example,
Vietnamese gloss, Vietnamese example, level, and topic. I also separated the
two paralyse/paralyze examples so the British and American spellings are not
duplicate exercises. All 19 entries are silver.

Two source-list items remain intentionally unadded: handicapped is an outdated
term that should not be taught as current learner vocabulary, and hearted is
normally used inside compounds rather than as an independent word. These
remain visible in the gap report for owner review.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,208 | 7,227 |
| Silver entries | 5,980 | 5,999 |
| CEFR A1-B1 entries still missing | 21 | 2 |
| Bronze entries | 1,228 | 1,228 |

## Checks

- check_editor_batch: rows=19 problems=0
- words_batch tiers: not silver: 0
- validate_content: errors=0 warnings=247
- audit_content: entries=7227 gold=0 silver=5999 bronze=1228 ngsl_missing=0 errors=3012
- unittest discover: 32 tests passed; locale xx errors=0
- gradlew.bat check assembleDebug: BUILD SUCCESSFUL
