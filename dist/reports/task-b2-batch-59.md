# B2 CEFR A1-B1 - batch 59

Date: 2026-10-09

## Scope

Added 25 CEFR A1-B1 entries covering grammar forms, family, work, transport, money,
academic language, and travel:

`been`, `done`, `favour`, `gramme`, `granddad`, `granny`, `honour`, `killer`,
`acknowledgement`, `analyse`, `kilometre`, `labour`, `licence`, `marvellous`,
`omelette`, `organise`, `organised`, `pence`, `realise`, `rumour`, `theirs`,
`towards`, `traveller`, `whom`, `murderer`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after the final generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example for a complete sentence, the intended sense, and the target word or inflected form.
- Checked every Vietnamese gloss and example for accurate meaning, natural wording, and preserved diacritics.
- Checked the grammar forms `been` and `done`, the spelling/usage pair `organise`/`organised`, and the formal pronoun `whom`.
- Checked the measurement and currency entries `gramme`, `kilometre`, and `pence` for exact values and British usage labels in the definitions.
- Checked `killer` and `murderer` in neutral detective/investigation examples without graphic content.
- Expanded the `gramme` Vietnamese gloss to “một gam” so validation retained the baseline warning count while preserving the correct meaning.
- Confirmed all 25 generated entries are silver after the final regeneration.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 7,082 | 7,107 |
| Audit silver entries | 5,854 | 5,879 |
| CEFR A1-B1 missing | 147 | 122 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b59.master.tsv 59` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b59.txt 59` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=7107`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch59.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b59.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, topics, and silver tiers checked).
- JSON delta QA - pass; exactly 25 new English entries and 25 new Vietnamese senses; existing entries unchanged.
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings; no new warning from this batch).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5879 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 122`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 122 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
