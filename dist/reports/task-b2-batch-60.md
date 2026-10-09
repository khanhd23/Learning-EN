# B2 CEFR A1-B1 - batch 60

Date: 2026-10-09

## Scope

Added 25 CEFR A1-B1 entries covering spelling variants, academic language, work,
environment, and direction vocabulary:

`kilogramme`, `left-hand`, `marvelous`, `organized`, `olympic`, `accidentally`,
`advert`, `afterwards`, `barman`, `categorise`, `characterise`, `civilisation`,
`counselling`, `defence`, `disc`, `dude`, `easy-going`, `eco`, `ecstasy`,
`encyclopaedia`, `enquiry`, `face-to-face`, `fallen`, `farther`, `farthest`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after the final generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example for a complete sentence, the intended sense, and the target word or inflected form.
- Checked every Vietnamese gloss and example for accurate meaning, natural wording, and preserved diacritics.
- Checked comparative direction words `farther` and `farthest`, and the compound forms `left-hand`, `easy-going`, and `face-to-face`.
- Checked the spelling variants `kilogramme`, `marvelous`, `organized`, `categorise`, `characterise`, `civilisation`, `counselling`, `defence`, and `encyclopaedia` as real words rather than abbreviations.
- Kept `olympic` limited to the sports topic and removed the unrelated body/arts topics from `left-hand` and `olympic`.
- Reworded the Vietnamese gloss for `characterise` to the natural “mô tả đặc điểm”.
- Expanded the `disc` Vietnamese gloss to “một chiếc đĩa” so validation retained the baseline warning count without changing the meaning.
- Confirmed all 25 generated entries are silver after the final regeneration.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,107 | 7,132 |
| Audit silver entries | 5,879 | 5,904 |
| CEFR A1-B1 missing | 122 | 97 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b60.master.tsv 60` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b60.txt 60` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=7132`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch60.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b60.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, topics, and silver tiers checked).
- JSON delta QA - pass; exactly 25 new English entries and 25 new Vietnamese senses; existing entries unchanged.
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings; no new warning from this batch).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5904 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 97`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 97 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
