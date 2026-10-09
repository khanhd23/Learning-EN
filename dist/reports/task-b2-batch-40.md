# B2 CEFR A1-B1 - batch 40

Date: 2026-10-09

## Scope

Reviewed 25 candidate CEFR A1-A2 words and added 24 learner-relevant entries:

`encouraging`, `equality`, `examiner`, `exhausted`, `experienced`, `forehead`,
`freshman`, `frozen`, `frustrated`, `gardening`, `goalkeeper`, `gorilla`, `guitarist`,
`gymnastics`, `honesty`, `hopeful`, `horizon`, `humorous`, `jogging`, `jug`, `magician`,
`messy`, `moisture`, `molecule`.

`jumper` was rejected during manual review because the dictionary already contains the
American equivalent `sweater`; it was removed before generation and commit.

## Manual educational review

- Read all 24 generated English entries and Vietnamese locale entries after generation.
- Checked every definition, part of speech, IPA, level, and controlled topic assignment.
- Checked every English example contains the target word in the intended meaning and is a natural learner sentence.
- Checked every Vietnamese gloss and example for meaning, diacritics, and natural wording.
- Checked potentially confusable meanings including `exhausted` versus ordinary tiredness,
  `frustrated` versus sadness, `hopeful` versus certainty, and the science terms `moisture`
  and `molecule`.
- Confirmed the duplicate `jumper` was not left in generated content.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,607 | 6,631 |
| Audit silver entries | 5,379 | 5,403 |
| CEFR A1-B1 missing | 622 | 598 |
| CEFR A1-B1 bronze | 116 | 116 |

## Checks

- `python tools/words_batch.py build tools/authoring/work/b40.txt 40` - pass.
- `python tools/gen_content.py` - pass.
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch40.tsv` - pass (`rows=24 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b40.txt` - pass (`not silver: 0`).
- `python tools/validate_content.py` - pass (`errors=0`; 245 existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5403 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 598`, `BRONZE 116`.
- Full Python and Android checks will be recorded after the batch commit.

## Remaining work

The CEFR A1-B1 source still contains 598 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
