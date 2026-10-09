# B2 CEFR A1-B1 - batch 57

Date: 2026-10-09

## Scope

Added 25 CEFR A1-B1 entries covering core grammar, dates, numbers, transport, and
comparative vocabulary:

`closed`, `did`, `does`, `eighty`, `fifth`, `first-floor`, `had`, `has`, `his`,
`lorry`, `motorway`, `november`, `ok`, `petrol`, `seventy`, `used`, `was`, `were`,
`whose`, `your`, `yours`, `worse`, `worst`, `colourful`, `colour`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after the final generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example for a complete sentence, the intended grammatical role, and the target word.
- Checked every Vietnamese gloss and example for accurate meaning, natural wording, and preserved diacritics.
- Checked the grammar group `did`, `does`, `had`, `has`, `was`, and `were` against the subjects and tense described in each definition.
- Checked the possessive/determiner group `his`, `whose`, `your`, and `yours` for correct grammatical roles.
- Checked `fifth`, `eighty`, and `seventy` as exact number or ordinal meanings.
- Checked the British English transport terms `lorry`, `motorway`, and `petrol` as distinct learner vocabulary rather than abbreviations.
- Fixed the `yours` example after the editor checker found it had only four words; the final example has the required level-1 length.
- Confirmed all 25 generated entries are silver after the final regeneration.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,032 | 7,057 |
| Audit silver entries | 5,804 | 5,829 |
| CEFR A1-B1 missing | 197 | 172 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b57.master.tsv 57` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b57.txt 57` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=7057`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch57.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b57.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, topics, and silver tiers checked).
- JSON delta QA - pass; exactly 25 new English entries and 25 new Vietnamese senses; existing entries unchanged.
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5829 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 172`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 172 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
