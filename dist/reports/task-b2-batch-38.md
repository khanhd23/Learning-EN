# B2 CEFR A1-B1 - batch 38

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR A1-A2 entries:

`greeting`, `handwriting`, `jungle`, `neighbourhood`, `nervousness`, `noticeboard`,
`nutritious`, `parking`, `postman`, `preschool`, `rainforest`, `recycling`, `safari`,
`salmon`, `scared`, `scary`, `scold`, `seaweed`, `snowboard`, `snowstorm`, `sunbathe`,
`swimmer`, `teammate`, `teamwork`, `vowel`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after generation.
- Checked the definition, part of speech, IPA, level, and controlled topic assignment for every entry.
- Checked every English example contains the target word and uses it in the intended meaning.
- Checked every Vietnamese gloss and example for meaning, diacritics, and natural wording.
- Corrected `sunbathe` to include relaxing or getting a tan, not only warming up.
- Corrected `vowel` to describe a speech sound without incorrectly limiting vowels to five letters.
- Confirmed that `scared` and `scary` teach different meanings and that no duplicate variant was added.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,557 | 6,582 |
| Audit silver entries | 5,329 | 5,354 |
| CEFR A1-B1 missing | 672 | 647 |
| CEFR A1-B1 bronze | 116 | 116 |

## Checks

- `python tools/words_batch.py build tools/authoring/work/b38.txt 38` - pass.
- `python tools/gen_content.py` - pass.
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch38.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b38.txt` - pass (`not silver: 0`).
- `python tools/validate_content.py` - pass (`errors=0`; 245 existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5354 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 647`, `BRONZE 116`.
- Full Python and Android checks will be recorded after the batch commit.

## Remaining work

The CEFR A1-B1 source still contains 647 missing entries and 116 bronze entries. Future batches
must continue to select learner-relevant words and skip duplicates, abbreviations, proper nouns,
and source-only variants.
