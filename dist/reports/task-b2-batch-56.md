# B2 CEFR A1-B1 - batch 56

Date: 2026-10-09

## Scope

Added 25 foundational CEFR A1 entries:

`am`, `are`, `best`, `doing`, `eighteen`, `fifteen`, `fourteen`, `hers`, `him`,
`is`, `my`, `nineteen`, `ninety`, `ours`, `saw`, `seventeen`, `sixteen`, `thanks`,
`their`, `these`, `thirteen`, `thirty`, `those`, `twelve`, `twenty`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after the final generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example for a complete sentence, the intended grammatical role, and the target word.
- Checked every Vietnamese gloss and example for accurate meaning, natural wording, and preserved diacritics.
- Checked the `be` forms `am`, `are`, and `is` with their correct subjects, and the past-form entry `saw` as the past of `see`.
- Checked the possessive/pronoun group `hers`, `him`, `my`, `ours`, and `their`, including the distinction between determiners and pronouns.
- Checked the demonstratives `these` and `those` for nearby versus farther plural references.
- Checked all eleven number entries for exact values and natural counting examples.
- Corrected the IPA for `best` from a spelling-like value to `bɛst`; the final entry is silver.
- Confirmed all 25 generated entries are silver after the final regeneration.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,007 | 7,032 |
| Audit silver entries | 5,779 | 5,804 |
| CEFR A1-B1 missing | 222 | 197 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b56.master.tsv 56` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b56.txt 56` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=7032`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch56.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b56.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, topics, and silver tiers checked).
- JSON delta QA - pass; exactly 25 new English entries and 25 new Vietnamese senses; existing entries unchanged.
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5804 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 197`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 197 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
