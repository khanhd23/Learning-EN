# B2 CEFR A1-B1 - batch 41

Date: 2026-10-09

## Scope

Added 26 learner-relevant CEFR A2-B1 entries:

`daze`, `dazzle`, `delighted`, `delightful`, `depressed`, `depressing`, `deprive`,
`destructive`, `detective`, `devastating`, `devotion`, `dialogue`, `diameter`, `diaper`,
`diligence`, `diligent`, `dioxide`, `disability`, `disabled`, `disastrous`, `discourage`,
`discrimination`, `disgusting`, `dissolve`, `diver`, `diverse`.

## Manual educational review

- Read all 26 generated English entries and Vietnamese locale entries after generation.
- Checked every definition, part of speech, IPA, level, and controlled topic assignment.
- Checked every English example contains the target word in the intended meaning and is a natural learner sentence.
- Checked every Vietnamese gloss and example for meaning, diacritics, and natural wording.
- Checked the adjective pairs `depressed`/`depressing` and the noun/adjective pair
  `disability`/`disabled` for distinct teaching meanings.
- Corrected the `deprive` example to use the natural collocation `deprive students of the focus they need`.
- Corrected the Vietnamese translation of `devastating` to natural wording.
- Corrected the Vietnamese translation of `discourage` to natural wording.
- Removed `environment_energy` from `disastrous`; its meaning and example are general description,
  not an environmental sense.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,631 | 6,657 |
| Audit silver entries | 5,403 | 5,429 |
| CEFR A1-B1 missing | 598 | 572 |
| CEFR A1-B1 bronze | 116 | 116 |

## Checks

- `python tools/words_batch.py build tools/authoring/work/b41.txt 41` - pass.
- `python tools/gen_content.py` - pass.
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch41.tsv` - pass (`rows=26 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b41.txt` - pass (`not silver: 0`).
- `python tools/validate_content.py` - pass (`errors=0`; 245 existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5429 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 572`, `BRONZE 116`.
- Full Python and Android checks will be recorded after the batch commit.

## Remaining work

The CEFR A1-B1 source still contains 572 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
