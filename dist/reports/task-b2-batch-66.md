# Content B2 batch 66

## Scope and manual review

Upgraded 25 existing CEFR A1-B1 entries from bronze to silver:
deadly, deaf, despair, distinctly, divine, drown, dusty, earnest, engineering,
eternal, faint, faithful, farming, feast, festive, filling, fond, forgive,
fortnight, fuss, glimpse, glorious, gorgeous, gratitude, and gravy.

I manually reviewed every definition, part of speech, IPA, Vietnamese gloss,
English example, Vietnamese example, topic, and tier. I replaced a WordNet
definition for dusty, removed the imported template collocation for faithful,
and expanded the short fortnight example. All 25 entries now have learner-ready
content and silver tier.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,227 | 7,227 |
| Silver entries | 6,025 | 6,050 |
| Total bronze entries | 1,202 | 1,177 |
| CEFR A1-B1 bronze entries | 90 | 65 |

## Checks

- check_editor_batch: rows=25 problems=0
- words_batch tiers: not silver: 0
- validate_content: errors=0 warnings=244
- audit_content: entries=7227 gold=0 silver=6050 bronze=1177 ngsl_missing=0 errors=2881
- unittest discover: 32 tests passed; locale xx errors=0
- gradlew.bat check assembleDebug: BUILD SUCCESSFUL
