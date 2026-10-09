# Content B2 batch 80

## Scope and manual review

Added 25 useful CEFR B2 vocabulary entries: critically, crossly, cruelly,
cruelty, crystallize, cube, cubism, cuff, culmination, culturally, curl,
cursor, cybercafe, cybercrime, cyberpet, cyberschool, cyberspace, cyclist,
daft, dandelion, daughter-in-law, dearly, decay, deceive, and deceptive.

I manually reviewed every generated entry for lemma, part of speech, IPA, level,
CEFR metadata, topic assignment, definition, Vietnamese gloss, English example,
and Vietnamese example. The obscure and dated `cyberaddict` source item was
not added as an independent lesson; `deceptive` was selected instead for
broader learner value. All 25 entries are silver.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,477 | 7,502 |
| Silver entries | 6,365 | 6,390 |
| Total bronze entries | 1,112 | 1,112 |
| CEFR B2 missing entries | 1,198 | 1,173 |
| CEFR B2 bronze entries | 203 | 203 |
| CEFR A1-B1 missing / bronze | 2 / 0 | 2 / 0 |
| TOEIC missing / bronze | 0 / 0 | 0 / 0 |

## Checks

- `check_editor_batch`: rows=25, problems=0
- `words_batch.py tiers`: not silver: 0
- `validate_content.py`: errors=0, warnings=236
- `audit_content.py`: entries=7502, silver=6390, bronze=1112, ngsl_missing=0, errors=2709; no new audit errors
- `word_gaps.py cefr B2`: MISSING 1173; BRONZE 203
- `python -m unittest discover -s tools/tests`: 32 tests passed; locale `xx` errors=0
- `gradlew.bat testDebugUnitTest assembleDebug`: BUILD SUCCESSFUL
