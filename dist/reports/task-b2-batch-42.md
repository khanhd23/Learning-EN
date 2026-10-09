# B2 CEFR A1-B1 - batch 42

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR A2-B1 entries:

`foolish`, `forbidden`, `freely`, `friendliness`, `fright`, `frustration`, `genetics`,
`geography`, `gifted`, `giggle`, `glide`, `golfer`, `graceful`, `guilt`, `handball`,
`handkerchief`, `hardship`, `hatred`, `heel`, `helpless`, `hidden`, `hip`, `hop`,
`horrify`, `harness`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after generation.
- Checked every definition, part of speech, IPA, level, and controlled topic assignment.
- Checked every English example contains the target word in the intended meaning and is a natural learner sentence.
- Checked every Vietnamese gloss and example for meaning, diacritics, and natural wording.
- Checked the emotion words `fright`, `frustration`, `guilt`, `hatred`, and `helpless` for distinct meanings.
- Checked the science and school words `genetics`, `geography`, and `gifted` against their examples.
- Confirmed the physical terms `heel` and `hip` are not confused with nearby body parts.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,657 | 6,682 |
| Audit silver entries | 5,429 | 5,454 |
| CEFR A1-B1 missing | 572 | 547 |
| CEFR A1-B1 bronze | 116 | 116 |

## Checks

- `python tools/words_batch.py build tools/authoring/work/b42.txt 42` - pass.
- `python tools/gen_content.py` - pass.
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch42.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b42.txt` - pass (`not silver: 0`).
- `python tools/validate_content.py` - pass (`errors=0`; 245 existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5454 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 547`, `BRONZE 116`.
- Full Python and Android checks will be recorded after the batch commit.

## Remaining work

The CEFR A1-B1 source still contains 547 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
