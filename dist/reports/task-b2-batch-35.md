# B2 CEFR A1–B1 — batch 35 bronze upgrades

Date: 2026-10-09

This batch upgrades 50 existing CEFR A1–B1 bronze entries covering function words, numbers, family, school, sport, health, food, and common verbs.

## Manual review

- Read all 50 final upgraded entries after generation: definition, part of speech, IPA, English example, Vietnamese gloss, Vietnamese example, level, and topics.
- Replaced malformed imported IPA for `living`, `running`, `drawing`, `following`, `purple`, `given`, `dam`, `interested`, `interesting`, and `swimming`.
- Rewrote the Vietnamese `bloom` example to avoid a false proper-name detection while keeping the meaning natural.
- Kept imported glosses for `forty` and `nationality` only after manual confirmation that they match the reviewed definitions, marked `[keep-gloss]`.
- `check_editor_batch.py`: 50 rows, 0 problems.
- `words_batch.py tiers`: 0 entries remained below silver for this batch.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Generated word entries | 6,482 | 6,482 |
| Direct silver tier (audit) | 5,204 | 5,254 |
| Direct bronze tier (audit) | 1,278 | 1,228 |
| CEFR A1–B1 missing (`word_gaps.py cefr A1 A2 B1`) | 747 | 747 |
| CEFR A1–B1 bronze | 163 | 114 |

The B2 work is not complete: 747 CEFR A1–B1 entries remain missing and 114 remain bronze after this batch.

## Checks

- `python tools/gen_content.py` — passed (`topics=47 words=6482 confusables=32 grammar=38 questions=641 passages=8 petMoods=18`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch35.tsv` — passed (`rows=50 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b35.txt` — passed (`not silver: 0`).
- `python tools/validate_content.py` — passed (`errors=0`; existing warnings: 245).
- `python tools/audit_content.py` — completed; existing project-wide audit output reports `gold=0 silver=5254 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` — completed (`MISSING 747`; CEFR bronze count from generated JSON: 114).
- `python -m unittest discover -s tools/tests` — passed (30 tests; locale fixture reported 0 errors).
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.

