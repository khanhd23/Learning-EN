# Content B2 batch 67

## Scope and manual review

Upgraded 26 existing CEFR A1-B1 entries from bronze to silver:
cultivate, hanging, hasty, haven, heal, healing, hearing, hearty, heavenly,
holy, hopeless, humanity, illuminate, imaginary, imitate, jar, kindness,
lasting, leading, lodge, maple, marked, mend, mischief, miserable, and misery.

I manually reviewed every definition, part of speech, IPA, Vietnamese gloss,
English example, Vietnamese example, topic, and tier. I corrected the IPA for
mend and refined the topic assignments for hasty and hearty after review. All
26 entries now have learner-ready content and silver tier.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,227 | 7,227 |
| Silver entries | 6,050 | 6,076 |
| Total bronze entries | 1,177 | 1,151 |
| CEFR A1-B1 bronze entries | 65 | 39 |

## Checks

- check_editor_batch: rows=26 problems=0
- words_batch tiers: not silver: 0
- validate_content: errors=0 warnings=241
- audit_content: entries=7227 gold=0 silver=6076 bronze=1151 ngsl_missing=0 errors=2809
- unittest discover: 32 tests passed; locale xx errors=0
- gradlew.bat check assembleDebug: BUILD SUCCESSFUL
