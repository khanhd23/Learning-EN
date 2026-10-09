# Content B2 batch 65

## Scope and manual review

Upgraded 26 existing CEFR A1-B1 entries from bronze to silver:
treasure, weep, abundance, academy, admiration, affection, aloud, annoyance,
annoyed, applaud, avenue, bare, bark, bathe, blessing, breeding, burning,
carelessness, carriage, cherish, circular, companion, concerned, conquer,
courage, and cruel.

For every entry I manually reviewed the definition, part of speech, IPA,
Vietnamese gloss, English example, Vietnamese example, and topic assignment.
The entries previously using imported or template definitions now have
learner-ready explanations and concrete examples. One short example for aloud
was expanded to meet the level-specific checker rule.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,227 | 7,227 |
| Silver entries | 5,999 | 6,025 |
| CEFR A1-B1 bronze entries | 116 | 90 |
| Total bronze entries | 1,228 | 1,202 |
| CEFR A1-B1 missing entries | 2 | 2 |

The remaining missing entries are handicapped (outdated terminology) and
hearted (normally used inside compounds rather than as an independent word);
they are documented for owner review rather than taught as current vocabulary.

## Checks

- check_editor_batch: rows=26 problems=0
- words_batch tiers: not silver: 0
- validate_content: errors=0 warnings=247
- audit_content: entries=7227 gold=0 silver=6025 bronze=1202 ngsl_missing=0 errors=2947
- unittest discover: 32 tests passed; locale xx errors=0
- gradlew.bat check assembleDebug: BUILD SUCCESSFUL
