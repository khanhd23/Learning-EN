# B2 CEFR A1-B1 - batch 43

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR B1 entries:

`creator`, `crossing`, `damaged`, `dangerously`, `dating`, `defender`, `deficiency`,
`diving`, `divorced`, `duty-free`, `eagerness`, `ecological`, `economics`, `ecosystem`,
`electron`, `embarrassment`, `emperor`, `encouragement`, `endanger`, `endless`, `enjoyment`,
`enrich`, `environmentalist`, `essence`, `exploration`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example contains the target word in the intended sense, has a complete sentence, and is suitable for a learner.
- Checked every Vietnamese gloss and example for meaning, natural wording, and preserved diacritics.
- Checked the related environment/science group (`ecological`, `ecosystem`, `electron`, `endanger`, `environmentalist`, `exploration`) for distinct meanings and examples.
- Checked the relationship/emotion group (`dating`, `divorced`, `eagerness`, `embarrassment`, `encouragement`, `enjoyment`) for distinct meanings and age-appropriate examples.
- Changed the Vietnamese gloss for `electron` to `hạt electron`; it is a natural Vietnamese scientific term and avoids copying the English headword as the gloss.
- Confirmed all 25 lemmas were absent before the batch and all 25 generated entries are silver afterward.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,682 | 6,707 |
| Audit silver entries | 5,454 | 5,479 |
| CEFR A1-B1 missing | 547 | 522 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b43.master.tsv 43` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b43.txt 43` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=6707`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch43.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b43.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, and topics checked).
- `python tools/validate_content.py` - pass (`errors=0`; `246` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5479 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 522`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 522 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
