# Content B2 batch 70

## Scope and manual review

Added 25 useful CEFR B2 vocabulary entries: abandoned, abnormally, aboriginal,
abruptly, absentee, absurd, accessibility, achievable, adaptable, addict,
addicted, addiction, adequately, admiringly, admittedly, adopted, adoption,
adore, adventurous, adversely, adversity, affectionate, affectionately, ageless,
and aggressively.

I manually reviewed every generated entry for lemma, part of speech, IPA,
level, CEFR metadata, topic assignment, English definition, Vietnamese gloss,
English example, and Vietnamese example. I corrected the Vietnamese glosses for
aboriginal and affectionate during review. All 25 entries are silver, and no
template examples or unrelated topics were used.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,227 | 7,252 |
| Silver entries | 6,115 | 6,140 |
| Total bronze entries | 1,112 | 1,112 |
| CEFR B2 missing entries | 1,448 | 1,423 |
| CEFR B2 bronze entries | 203 | 203 |
| CEFR A1-B1 missing / bronze | 2 / 0 | 2 / 0 |
| TOEIC missing / bronze | 0 / 0 | 0 / 0 |

The two remaining A1-B1 source-list gaps are `handicapped` and `hearted`,
which remain excluded pending an owner decision because they are outdated or
normally used only inside compounds. This batch did not change them.

## Checks

- `check_editor_batch`: rows=25, problems=0
- `words_batch.py tiers`: not silver: 0
- `validate_content.py`: errors=0, warnings=236
- `audit_content.py`: entries=7252, silver=6140, bronze=1112, ngsl_missing=0, errors=2709; the pre-existing error count did not increase
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` errors=0
- `gradlew.bat testDebugUnitTest assembleDebug`: BUILD SUCCESSFUL
