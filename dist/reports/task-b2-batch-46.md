# B2 CEFR A1-B1 - batch 46

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR B1 entries:

`continually`, `continuously`, `involuntarily`, `ironing`, `ivory`, `jewel`, `juicy`,
`lawful`, `legally`, `lessen`, `liar`, `liberate`, `lifelong`, `lightly`, `limited`,
`limp`, `loaf`, `lottery`, `loudspeaker`, `lovingly`, `lung`, `magical`, `magnificent`,
`mankind`, `mash`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example contains the target word in the intended sense, has a complete sentence, and is suitable for a learner.
- Checked every Vietnamese gloss and example for meaning, natural wording, and preserved diacritics.
- Checked the related adverbs `continually` and `continuously` separately: repeated events with pauses versus no interruption.
- Checked the body/health group (`involuntarily`, `impair`-related usage, `limp`, `lung`) for clear, non-overlapping learner meanings.
- Rewrote the `magical` definition to one clear sense and replaced the `legally` example with a natural legal-use sentence.
- Confirmed all 25 lemmas were absent before the batch and all 25 generated entries are silver afterward.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,757 | 6,782 |
| Audit silver entries | 5,529 | 5,554 |
| CEFR A1-B1 missing | 472 | 447 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b46.master.tsv 46` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b46.txt 46` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=6782`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch46.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b46.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, and topics checked).
- `python tools/validate_content.py` - pass (`errors=0`; `246` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5554 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 447`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 447 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
