# B2 CEFR A1–B1 — batch 30

Date: 2026-10-09

This batch adds 51 learner-relevant CEFR A1–B1 entries from the remaining gap list. The entries cover weather, relationships, school, daily life, food, clothing, health, travel, and work. Generated content was reviewed manually after the final `gen_content.py` run.

## Manual review

- Reviewed all 51 final generated entries: definition, part of speech, IPA, English example, Vietnamese gloss, Vietnamese example, level, and topic assignment.
- The first draft contained 52 candidates. `motorway` was removed before the final generation because the app already contains the equivalent `highway` entry; no duplicate was shipped.
- Confirmed the normalized generated id `make_up` correctly represents the source lemma `make-up`.
- `check_editor_batch.py`: 51 rows, 0 problems.
- `words_batch.py tiers`: 0 entries remained below silver for this batch.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Generated word entries | 6,266 | 6,317 |
| Direct silver tier | 4,939 | 4,990 |
| Direct bronze tier | 1,327 | 1,327 |
| CEFR A1–B1 missing (`word_gaps.py cefr A1 A2 B1`) | 963 | 912 |
| CEFR A1–B1 bronze (`word_gaps.py cefr A1 A2 B1`) | 215 | 215 |

The B2 work is not complete: 912 CEFR A1–B1 entries remain missing and 215 remain bronze after this batch.

## Checks

- `python tools/gen_content.py` — passed (`topics=47 words=6317 confusables=32 grammar=38 questions=641 passages=8 petMoods=18`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch30.tsv` — passed (`rows=51 problems=0`).
- `python tools/validate_content.py` — passed (`errors=0`; existing warnings: 248).
- `python tools/audit_content.py` — completed; existing project-wide audit output reports `gold=0 silver=4990 bronze=1327 errors=3251`.
- `python tools/word_gaps.py cefr A1 A2 B1` — completed (`MISSING 912`, `BRONZE 215`).
- `python -m unittest discover -s tools/tests` — passed (30 tests; locale fixture reported 0 errors).
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.

