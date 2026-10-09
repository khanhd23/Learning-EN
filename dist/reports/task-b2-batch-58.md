# B2 CEFR A1-B1 - batch 58

Date: 2026-10-09

## Scope

Added 25 CEFR A1-B1 entries covering spelling variants and useful everyday vocabulary:

`aeroplane`, `coke`, `e-mail`, `favourite`, `grey`, `jewellery`, `maths`, `me`,
`mommy`, `practise`, `programme`, `super`, `t-shirt`, `theatre`, `us`, `yoghurt`,
`apologise`, `behaviour`, `bookshop`, `centimetre`, `centre`, `cheque`, `chilli`,
`criticise`, `favourable`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after the final generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example for a complete sentence, the intended sense, and the target word or inflected form.
- Checked every Vietnamese gloss and example for accurate meaning, natural wording, and preserved diacritics.
- Checked the pronoun pair `me`/`us`, spelling variants such as `favourite`, `colour`, and `practise`, and the noun forms `e-mail`, `programme`, and `theatre`.
- Checked the food entries `coke`, `yoghurt`, and `chilli` in ordinary, non-promotional contexts.
- Fixed a generated sense-ID edge case for `t-shirt`: the underscore slug initially confused the batch helper, so the source sense was explicitly written as `t_shirt_s1`.
- Reworded the `super` example during manual review to make the English and Vietnamese natural.
- Expanded the `centimetre` Vietnamese gloss to “một xen-ti-mét” so validation retained the baseline warning count without sacrificing meaning.
- Confirmed all 25 generated entries are silver after the final regeneration.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,057 | 7,082 |
| Audit silver entries | 5,829 | 5,854 |
| CEFR A1-B1 missing | 172 | 147 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b58.master.tsv 58` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b58.txt 58` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=7082`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch58.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b58.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, topics, and silver tiers checked).
- JSON delta QA - pass; exactly 25 new English entries and 25 new Vietnamese senses; existing entries unchanged.
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings; no new warning from this batch).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5854 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 147`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 147 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
