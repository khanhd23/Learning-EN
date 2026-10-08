# B2 CEFR A1–B1 — batch 31

Date: 2026-10-09

This batch adds 59 learner-relevant CEFR A1–B1 entries from the remaining gap list. The entries cover school, food, travel, sports, health, relationships, arts, and daily life. Generated content was reviewed manually after the final `gen_content.py` run.

## Manual review

- Reviewed all 59 final generated entries: definition, part of speech, IPA, English example, Vietnamese gloss, Vietnamese example, level, and topic assignment.
- Corrected the Vietnamese meaning for `sailing` from “chèo thuyền buồm” to the accurate “đi thuyền buồm”, and regenerated before the final checks.
- Confirmed that no duplicate entry was retained from the batch source.
- `check_editor_batch.py`: 59 rows, 0 problems.
- `words_batch.py tiers`: 0 entries remained below silver for this batch.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Generated word entries | 6,317 | 6,376 |
| Direct silver tier | 4,990 | 5,049 |
| Direct bronze tier | 1,327 | 1,327 |
| CEFR A1–B1 missing (`word_gaps.py cefr A1 A2 B1`) | 912 | 853 |
| CEFR A1–B1 bronze (`word_gaps.py cefr A1 A2 B1`) | 215 | 215 |

The B2 work is not complete: 853 CEFR A1–B1 entries remain missing and 215 remain bronze after this batch.

## Checks

- `python tools/gen_content.py` — passed (`topics=47 words=6376 confusables=32 grammar=38 questions=641 passages=8 petMoods=18`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch31.tsv` — passed (`rows=59 problems=0`).
- `python tools/validate_content.py` — passed (`errors=0`; existing warnings: 248).
- `python tools/audit_content.py` — completed; existing project-wide audit output reports `gold=0 silver=5049 bronze=1327 errors=3251`.
- `python tools/word_gaps.py cefr A1 A2 B1` — completed (`MISSING 853`, `BRONZE 215`).
- `python -m unittest discover -s tools/tests` — passed (30 tests; locale fixture reported 0 errors).
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.

