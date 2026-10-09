# B2 CEFR A1-B1 - batch 37

Date: 2026-10-09

## Scope

Added 25 high-use CEFR A1-A2 entries that were still missing from the dictionary:

`april`, `august`, `december`, `february`, `january`, `july`, `june`, `september`,
`grandma`, `grandpa`, `daddy`, `cucumber`, `lettuce`, `peanut`, `spinach`,
`strawberry`, `freezer`, `saucepan`, `saucer`, `hairdryer`, `hairdresser`,
`handshake`, `mailbox`, `schoolmate`, `schoolteacher`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after generation.
- Checked each definition against the lemma and part of speech.
- Checked every IPA form, level, and topic assignment; all topics are real controlled topics.
- Checked every English example contains the target word and is a natural learner sentence.
- Checked every Vietnamese gloss and example for meaning, diacritics, and natural wording.
- Checked that no British/American duplicate or proper noun was added in this batch.
- No ambiguous entry or correction was found.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,532 | 6,557 |
| Audit silver entries | 5,304 | 5,329 |
| CEFR A1-B1 missing | 697 | 672 |
| CEFR A1-B1 bronze | 116 | 116 |

## Checks

- `python tools/gen_content.py` - pass.
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch37.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b37.txt` - pass (`not silver: 0`).
- `python tools/validate_content.py` - pass (`errors=0`; 245 existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5329 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 672`, `BRONZE 116`.
- Full Python and Android checks will be recorded after the batch commit.

## Remaining work

The CEFR A1-B1 source still contains 672 missing entries and 116 bronze entries. Future batches
must continue to select learner-relevant words and skip duplicates, abbreviations, proper nouns,
and source-only variants.
