# B2 CEFR A1-B1 - batch 49

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR B1 entries:

`piety`, `poisonous`, `politely`, `pollutant`, `positively`, `possessive`, `prefix`,
`prepared`, `preposition`, `preservation`, `priest`, `proclaim`, `procrastination`,
`prohibition`, `pronoun`, `prosperity`, `provided`, `publicly`, `pullover`, `punish`,
`punishment`, `purify`, `radiation`, `railroad`, `ration`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example contains the target word in the intended sense, has a complete sentence, and is suitable for a learner.
- Checked every Vietnamese gloss and example for meaning, natural wording, and preserved diacritics.
- Checked the grammar group (`possessive`, `prefix`, `preposition`, `provided`, `pronoun`) for accurate explanations and examples.
- Checked the environment/science group (`pollutant`, `preservation`, `purify`, `radiation`) for distinct meanings and appropriate contexts.
- Rewrote the `pronoun` example with quotation marks around the words being analyzed, so the grammar explanation is natural and unambiguous.
- Confirmed all 25 lemmas were absent before the batch and all 25 generated entries are silver afterward.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,832 | 6,857 |
| Audit silver entries | 5,604 | 5,629 |
| CEFR A1-B1 missing | 397 | 372 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b49.master.tsv 49` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b49.txt 49` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=6857`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch49.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b49.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, and topics checked).
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5629 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 372`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 372 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
