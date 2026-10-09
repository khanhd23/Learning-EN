# B2 CEFR A1-B1 - batch 50

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR B1 entries:

`rebuild`, `recite`, `reclaim`, `recording`, `recycled`, `refreshments`, `regain`,
`relaxing`, `retell`, `retrospect`, `reunification`, `reunify`, `riddle`, `roadside`,
`rotten`, `rudely`, `runaway`, `runway`, `sadness`, `scenic`, `seawater`, `secretly`,
`self-service`, `setting`, `settler`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example contains the target word in the intended sense, has a complete sentence, and is suitable for a learner.
- Checked every Vietnamese gloss and example for meaning, natural wording, and preserved diacritics.
- Checked the related word pairs `reunification`/`reunify` and `setting`/`recording` for distinct noun/verb or meaning use.
- Reworked `reclaim`, `recording`, `reunify`, and `self-service` so the definition, gloss, and example agree naturally.
- Found and fixed a normalization edge case for the hyphenated `self-service`: the editor sense ID must be `self_service_s1`; after regeneration it is silver rather than a template bronze entry.
- Confirmed all 25 generated entries are silver after the final regeneration.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,857 | 6,882 |
| Audit silver entries | 5,629 | 5,654 |
| CEFR A1-B1 missing | 372 | 347 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b50.master.tsv 50` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b50.txt 50` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=6882`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch50.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b50.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, and topics checked; `self-service` silver).
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5654 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 347`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 347 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
