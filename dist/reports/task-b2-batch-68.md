# Content B2 batch 68

## Scope and manual review

Upgraded 24 existing CEFR A1-B1 entries from bronze to silver:
monster, nonsense, notorious, orderly, outward, owing, participle, penny,
prayer, quake, regarding, remembrance, respectable, ripe, rolling, sacred,
shame, shocking, sickness, sore, spiritual, spoil, sponge, and sticky.

I manually reviewed every definition, part of speech, IPA, Vietnamese gloss,
English example, Vietnamese example, topic, and tier. The grammar entry
participle was checked as a language-learning term, and regarding was checked
as a preposition. I also rewrote the owing example and narrowed respectable to
its descriptive topic. All 24 entries are silver.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,227 | 7,227 |
| Silver entries | 6,076 | 6,100 |
| Total bronze entries | 1,151 | 1,127 |
| CEFR A1-B1 bronze entries | 39 | 15 |

## Checks

- check_editor_batch: rows=24 problems=0
- words_batch tiers: not silver: 0
- validate_content: errors=0 warnings=237
- audit_content: entries=7227 gold=0 silver=6100 bronze=1127 ngsl_missing=0 errors=2747
- unittest discover: 32 tests passed; locale xx errors=0
- gradlew.bat check assembleDebug: BUILD SUCCESSFUL
