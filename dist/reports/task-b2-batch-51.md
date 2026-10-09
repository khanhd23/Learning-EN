# B2 CEFR A1-B1 - batch 51

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR B1 entries:

`severely`, `shadowy`, `shameful`, `sheriff`, `shiny`, `shiver`, `shocked`, `signpost`,
`simultaneously`, `smoker`, `socially`, `someplace`, `soothe`, `sophomore`, `southeast`,
`southwest`, `sparkle`, `spectacular`, `spiral`, `sportsmanship`, `stall`, `storyteller`,
`strangely`, `stressed`, `stressful`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example contains the target word in the intended sense, has a complete sentence, and is suitable for a learner.
- Checked every Vietnamese gloss and example for meaning, natural wording, and preserved diacritics.
- Checked the emotion pair `stressed`/`stressful` and the direction pair `southeast`/`southwest` for distinct word classes and meanings.
- Checked US-specific `sheriff` and `sophomore` in clear, neutral educational contexts.
- Rewrote `sportsmanship` to the natural definition “fair and respectful behavior in sports”.
- Rewrote `simultaneously` after the editor checker detected a copied WordNet definition; the final definition is original and the entry is silver.
- Confirmed all 25 generated entries are silver after the final regeneration.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,882 | 6,907 |
| Audit silver entries | 5,654 | 5,679 |
| CEFR A1-B1 missing | 347 | 322 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b51.master.tsv 51` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b51.txt 51` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=6907`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch51.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b51.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, and topics checked).
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5679 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 322`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 322 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
