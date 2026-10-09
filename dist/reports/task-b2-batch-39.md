# B2 CEFR A1-B1 - batch 39

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR A1-A2 entries:

`daisy`, `debris`, `dustbin`, `duvet`, `earache`, `elbow`, `eyesight`, `farmland`,
`greenhouse`, `hostel`, `hut`, `jade`, `lamb`, `marble`, `mustard`, `pond`, `pork`,
`razor`, `rubbish`, `sandal`, `skeleton`, `wagon`, `webcam`, `wetland`, `workout`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after generation.
- Checked every definition, part of speech, IPA, level, and controlled topic assignment.
- Checked every English example contains the target word in the intended sense and is a natural learner sentence.
- Checked every Vietnamese gloss and example for meaning, diacritics, and natural wording.
- Corrected `lamb` so the gloss matches the animal sense rather than adding an unprovided meat sense.
- Corrected `sandal` to use a natural plural English example matching the Vietnamese translation.
- Confirmed the concrete objects and nature terms are unambiguous for future picture use.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,582 | 6,607 |
| Audit silver entries | 5,354 | 5,379 |
| CEFR A1-B1 missing | 647 | 622 |
| CEFR A1-B1 bronze | 116 | 116 |

## Checks

- `python tools/words_batch.py build tools/authoring/work/b39.txt 39` - pass.
- `python tools/gen_content.py` - pass.
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch39.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b39.txt` - pass (`not silver: 0`).
- `python tools/validate_content.py` - pass (`errors=0`; 245 existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5379 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 622`, `BRONZE 116`.
- Full Python and Android checks will be recorded after the batch commit.

## Remaining work

The CEFR A1-B1 source still contains 622 missing entries and 116 bronze entries. Future batches
must continue to select learner-relevant words and skip duplicates, abbreviations, proper nouns,
and source-only variants.
