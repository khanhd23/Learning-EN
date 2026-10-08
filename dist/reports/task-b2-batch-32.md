# B2 CEFR A1–B1 — batch 32

Date: 2026-10-09

This batch adds 52 learner-relevant CEFR A1–B1 entries from the remaining gap list. The entries cover school, family, food, health, transport, sports, technology, and everyday descriptions. Generated content was reviewed manually after the final `gen_content.py` run.

## Manual review

- Reviewed all 52 final generated entries: definition, part of speech, IPA, English example, Vietnamese gloss, Vietnamese example, level, and topic assignment.
- Kept the exact Vietnamese gloss `đậu Hà Lan` for `pea`; rewrote only the Vietnamese example as “Thêm một hạt đậu nhỏ...” because the checker rejects an untranslated place name appearing only in the translated example.
- `check_editor_batch.py`: 52 rows, 0 problems.
- `words_batch.py tiers`: 0 entries remained below silver for this batch.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Generated word entries | 6,376 | 6,428 |
| Direct silver tier | 5,049 | 5,101 |
| Direct bronze tier | 1,327 | 1,327 |
| CEFR A1–B1 missing (`word_gaps.py cefr A1 A2 B1`) | 853 | 801 |
| CEFR A1–B1 bronze (`word_gaps.py cefr A1 A2 B1`) | 215 | 215 |

The B2 work is not complete: 801 CEFR A1–B1 entries remain missing and 215 remain bronze after this batch.

## Checks

- `python tools/gen_content.py` — passed (`topics=47 words=6428 confusables=32 grammar=38 questions=641 passages=8 petMoods=18`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch32.tsv` — passed (`rows=52 problems=0`).
- `python tools/validate_content.py` — passed (`errors=0`; existing warnings: 248).
- `python tools/audit_content.py` — completed; existing project-wide audit output reports `gold=0 silver=5101 bronze=1327 errors=3251`.
- `python tools/word_gaps.py cefr A1 A2 B1` — completed (`MISSING 801`, `BRONZE 215`).
- `python -m unittest discover -s tools/tests` — passed (30 tests; locale fixture reported 0 errors).
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.

