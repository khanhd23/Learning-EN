# B2 CEFR A1–B1 — batch 29

Date: 2026-10-08

This batch adds 51 learner-relevant CEFR A1–B1 entries from the remaining gap list. The entries cover school, daily life, travel, work, health, science, food, and home vocabulary. Generated content was reviewed manually after `gen_content.py` ran.

## Manual review

- Reviewed all 51 generated entries: definition, part of speech, IPA, English example, Vietnamese gloss, Vietnamese example, level, and topic assignment.
- Removed `bookshop` before generation because the existing American-English entry `bookstore` already covers the same learner meaning.
- Corrected the `aged` gloss from an incorrect imported draft to `cao tuổi` before the final generation.
- Excluded duplicate variants already represented in the app, including `aeroplane`/`airplane` and `behaviour`/`behavior`.
- `check_editor_batch.py`: 51 rows, 0 problems.
- `words_batch.py tiers`: 0 entries remained below silver for this batch.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Generated word entries | 6,215 | 6,266 |
| Direct silver tier | 4,889 | 4,939 |
| Direct bronze tier | 1,326 | 1,327 |
| CEFR A1–B1 missing (`word_gaps.py cefr A1 A2 B1`) | 1,014 | 963 |
| CEFR A1–B1 bronze (`word_gaps.py cefr A1 A2 B1`) | 215 | 215 |

The B2 work is not complete: 963 CEFR A1–B1 entries remain missing and 215 remain bronze after this batch.

## Checks

- `python tools/gen_content.py` — passed (`topics=47 words=6266 confusables=32 grammar=38 questions=641 passages=8 petMoods=18`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch29.tsv` — passed (`rows=51 problems=0`).
- `python tools/validate_content.py` — passed (`errors=0`; existing warnings: 247).
- `python tools/audit_content.py` — completed; existing project-wide audit output reports `gold=0 silver=4939 bronze=1327 errors=3251`.
- `python -m unittest discover -s tools/tests` — passed (30 tests; locale fixture reported 0 errors).
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.
- `git diff --check` — passed.

