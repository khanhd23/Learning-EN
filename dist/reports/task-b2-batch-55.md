# B2 CEFR A1-B1 - batch 55

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR A1-B1 entries:

`terrorism`, `turbulence`, `underpants`, `uplifted`, `viewpoint`, `violently`,
`virtual`, `visually`, `vividly`, `warmly`, `warmth`, `washbowl`, `wasteful`,
`well-dressed`, `westward`, `widespread`, `wildly`, `windscreen`, `windsurfing`,
`wither`, `worn`, `wrapping`, `written`, `yell`, `suicide`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after the final generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example for a complete sentence, the intended sense, and the target word or inflected form.
- Checked every Vietnamese gloss and example for accurate meaning, natural wording, and preserved diacritics.
- Checked the transport group `turbulence`, `windscreen`, and `windsurfing` in concrete travel contexts.
- Checked the adverb group `violently`, `visually`, `vividly`, `warmly`, and `wildly` for correct modification meaning.
- Checked `suicide` separately: the definition is factual and the example points only to confidential health support; there is no instructional or glorifying content.
- Reworded Vietnamese examples for `visually`, `washbowl`, and `written`, and removed the unrelated furniture topic from `wrapping`.
- Confirmed all 25 generated entries are silver after the final regeneration.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,982 | 7,007 |
| Audit silver entries | 5,754 | 5,779 |
| CEFR A1-B1 missing | 247 | 222 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b55.master.tsv 55` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b55.txt 55` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=7007`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch55.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b55.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, topics, and silver tiers checked).
- JSON delta QA - pass; exactly 25 new English entries and 25 new Vietnamese senses; existing entries unchanged.
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5779 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 222`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 222 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
