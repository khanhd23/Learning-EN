# B2 CEFR A1-B1 - batch 44

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR B1 entries:

`countless`, `crow`, `curiously`, `discomfort`, `downward`, `doze`, `drunk`, `dynasty`,
`emotionally`, `entertainer`, `eternity`, `excitedly`, `extensively`, `extinct`,
`extinction`, `fake`, `fantasy`, `fashionable`, `fluently`, `flute`, `fondness`, `geology`,
`ginger`, `glint`, `goddess`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example contains the target word in the intended sense, has a complete sentence, and is suitable for a learner.
- Checked every Vietnamese gloss and example for meaning, natural wording, and preserved diacritics.
- Checked the environment/science group (`crow`, `extinct`, `extinction`, `geology`, `ginger`, `glint`) for distinct meanings and appropriate examples.
- Checked the adverb group (`curiously`, `downward`, `emotionally`, `excitedly`, `extensively`, `fluently`) for correct English usage and natural Vietnamese wording.
- Rewrote four Vietnamese examples after review: `curiously`, `emotionally`, `extensively`, and `glint`.
- Confirmed all 25 lemmas were absent before the batch and all 25 generated entries are silver afterward.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,707 | 6,732 |
| Audit silver entries | 5,479 | 5,504 |
| CEFR A1-B1 missing | 522 | 497 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b44.master.tsv 44` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b44.txt 44` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=6732`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch44.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b44.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, and topics checked).
- `python tools/validate_content.py` - pass (`errors=0`; `246` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5504 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 497`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 497 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
