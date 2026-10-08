# B2 CEFR A1–B1 — batch 36

Date: 2026-10-09

This batch adds 50 learner-relevant CEFR A1–B1 entries covering food, school, grammar, science, technology, transport, society, and common descriptions.

## Manual review

- Read all 50 final generated entries after generation: definition, part of speech, IPA, English example, Vietnamese gloss, Vietnamese example, level, and topics.
- Corrected the Vietnamese gloss/example for `right-hand` to the natural “tay phải, bên phải” wording.
- Corrected the Vietnamese gloss for `bureau` by removing the inaccurate “cục”, while retaining both dictionary senses.
- Excluded American/British duplicates already represented in the app, including `analyse`/`analyze`, `characterise`/`characterize`, and `civilisation`/`civilization`.
- `check_editor_batch.py`: 50 rows, 0 problems.
- `words_batch.py tiers`: 0 entries remained below silver for this batch.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Generated word entries | 6,482 | 6,532 |
| Direct silver tier (audit) | 5,254 | 5,304 |
| Direct bronze tier (audit) | 1,228 | 1,228 |
| CEFR A1–B1 missing (`word_gaps.py cefr A1 A2 B1`) | 747 | 697 |
| CEFR A1–B1 bronze | 114 | 114 |

The B2 work is not complete: 697 CEFR A1–B1 entries remain missing and 114 remain bronze after this batch.

## Checks

- `python tools/gen_content.py` — passed (`topics=47 words=6532 confusables=32 grammar=38 questions=641 passages=8 petMoods=18`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch36.tsv` — passed (`rows=50 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b36.txt` — passed (`not silver: 0`).
- `python tools/validate_content.py` — passed (`errors=0`; existing warnings: 245).
- `python tools/audit_content.py` — completed; existing project-wide audit output reports `gold=0 silver=5304 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` — completed (`MISSING 697`; CEFR bronze count from generated JSON: 114).
- `python -m unittest discover -s tools/tests` — passed (30 tests; locale fixture reported 0 errors).
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.

