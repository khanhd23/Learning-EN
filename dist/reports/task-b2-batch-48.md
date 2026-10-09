# B2 CEFR A1-B1 - batch 48

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR B1 entries:

`opening`, `oppress`, `oppression`, `oral`, `orbit`, `organism`, `outdoors`, `outlaw`,
`outweigh`, `overjoyed`, `overwhelm`, `overwork`, `oxygen`, `paddle`, `painful`,
`parachute`, `paradise`, `parental`, `patrol`, `peculiar`, `penniless`, `persuasive`,
`phantom`, `philosopher`, `pleasantly`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example contains the target word in the intended sense, has a complete sentence, and is suitable for a learner.
- Checked every Vietnamese gloss and example for meaning, natural wording, and preserved diacritics.
- Checked the science group (`orbit`, `organism`, `oxygen`) for precise, learner-appropriate meanings.
- Checked the society group (`oppress`, `oppression`, `outlaw`, `patrol`) for distinct meanings and neutral educational context.
- Changed `opening` to one clear sense, changed the oral-interview Vietnamese wording, and changed `paddle` to a verb example matching its part of speech.
- Confirmed all 25 lemmas were absent before the batch and all 25 generated entries are silver afterward.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,807 | 6,832 |
| Audit silver entries | 5,579 | 5,604 |
| CEFR A1-B1 missing | 422 | 397 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b48.master.tsv 48` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b48.txt 48` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=6832`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch48.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b48.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, and topics checked).
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5604 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 397`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 397 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
