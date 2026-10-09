# B2 CEFR A1-B1 - batch 54

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR A1-B1 entries:

`unbelievable`, `uncontrollable`, `uncover`, `underage`, `underneath`, `undo`,
`undress`, `unexpectedly`, `unfairly`, `unfit`, `unfold`, `unfortunate`,
`unfriendly`, `unheard`, `unify`, `uninterested`, `uninteresting`, `unlucky`,
`unpack`, `unpredictable`, `unrelated`, `untidy`, `unwanted`, `unwell`,
`up-to-date`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after the final generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example for a complete sentence, the intended sense, and the target word or inflected form.
- Checked every Vietnamese gloss and example for accurate meaning, natural wording, and preserved diacritics.
- Checked the contrast between `uninterested` (a person's lack of interest) and `uninteresting` (a thing that is not engaging).
- Checked the negative-prefix group `uncover`, `undo`, `unfold`, `unpack`, and `unify` for correct verb meanings.
- Removed topic assignments that were not directly supported by the word meaning or example (`undo`, `unheard`, and `unwanted`).
- Reworded Vietnamese examples for `underage`, `uninteresting`, and `unlucky` during manual review.
- Confirmed all 25 generated entries are silver after the final regeneration.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,957 | 6,982 |
| Audit silver entries | 5,729 | 5,754 |
| CEFR A1-B1 missing | 272 | 247 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b54.master.tsv 54` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b54.txt 54` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=6982`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch54.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b54.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, topics, and silver tiers checked).
- JSON delta QA - pass; exactly 25 new English entries and 25 new Vietnamese senses; existing entries unchanged.
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5754 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 247`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 247 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
