# B2 CEFR A1–B1 — batch 34 bronze upgrades

Date: 2026-10-09

This batch upgrades 50 existing CEFR A1–B1 bronze entries with reviewed definitions, IPA, examples, Vietnamese glosses, and Vietnamese examples.

## Manual review

- Read all 50 final upgraded entries after generation: definition, part of speech, IPA, English example, Vietnamese gloss, Vietnamese example, level, and topics.
- Corrected malformed imported IPA for `melt`, `vet`, `offensive`, `reading`, `warning`, `writing`, `acquaintance`, `being`, and `beginning`.
- Removed an invalid imported collocation template from `beast` through the correction file; the upgraded learner sense remains intact.
- Kept unchanged Vietnamese glosses only where the manual review confirmed they exactly matched the new definition, marked with `[keep-gloss]` for the checker.
- `check_editor_batch.py`: 50 rows, 0 problems.
- `words_batch.py tiers`: 0 entries remained below silver for this batch.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Generated word entries | 6,482 | 6,482 |
| Direct silver tier | 5,155 | 5,204 |
| Direct bronze tier | 1,327 | 1,278 |
| CEFR A1–B1 missing (`word_gaps.py cefr A1 A2 B1`) | 747 | 747 |
| CEFR A1–B1 bronze (`word_gaps.py cefr A1 A2 B1`) | 215 | 163 |

The B2 work is not complete: 747 CEFR A1–B1 entries remain missing and 163 remain bronze after this batch.

## Checks

- `python tools/gen_content.py` — passed (`topics=47 words=6482 confusables=32 grammar=38 questions=641 passages=8 petMoods=18`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch34.tsv` — passed (`rows=50 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b34.txt` — passed (`not silver: 0`).
- `python tools/validate_content.py` — passed (`errors=0`; existing warnings: 246).
- `python tools/audit_content.py` — completed; existing project-wide audit output reports `gold=0 silver=5204 bronze=1278 errors=3150`.
- `python tools/word_gaps.py cefr A1 A2 B1` — completed (`MISSING 747`, `BRONZE 163`).
- `python -m unittest discover -s tools/tests` — passed (30 tests; locale fixture reported 0 errors).
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.

