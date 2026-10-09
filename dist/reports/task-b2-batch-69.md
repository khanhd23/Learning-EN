# Content B2 batch 69

## Scope and manual review

Upgraded the final 15 CEFR A1-B1 bronze entries to silver:
overwhelming, stricken, stubborn, stumble, tease, telegram, turning, useless,
vain, vivid, warrior, wax, whistle, wizard, and working.

I manually reviewed every definition, part of speech, IPA, Vietnamese gloss,
English example, Vietnamese example, topic, and tier. The senses cover
emotional, directional, media, household, sports, and technology uses without
using template sentences. All 15 entries are silver.

## Final CEFR A1-B1 status

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,227 | 7,227 |
| Silver entries | 6,100 | 6,115 |
| Total bronze entries | 1,127 | 1,112 |
| CEFR A1-B1 bronze entries | 15 | 0 |
| CEFR A1-B1 missing entries | 2 | 2 |
| TOEIC missing/bronze entries | 0 / 0 | 0 / 0 |

The only remaining A1-B1 source-list gaps are handicapped, an outdated term
that should not be taught as current learner vocabulary, and hearted, which is
normally used inside compounds rather than as an independent word. They remain
visible for owner review and were not silently invented.

## Checks

- check_editor_batch: rows=15 problems=0
- words_batch tiers: not silver: 0
- validate_content: errors=0 warnings=236
- audit_content: entries=7227 gold=0 silver=6115 bronze=1112 ngsl_missing=0 errors=2709
- word_gaps cefr A1 A2 B1: MISSING 2, BRONZE 0
- word_gaps tsl: MISSING 0, BRONZE 0
- unittest discover: 32 tests passed; locale xx errors=0
- gradlew.bat check assembleDebug: BUILD SUCCESSFUL
