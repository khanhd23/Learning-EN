# C2 Paper 2 - THPT question bank

Added the second original THPT English practice paper to the shared English and Vietnamese
exam banks. Paper 2 contains 40 unique questions in the six configured sections:

| Section | Questions |
|---|---:|
| Notice | 6 |
| Leaflet / advertisement | 6 |
| Sentence arrangement | 5 |
| Text completion | 5 |
| Reading 1 | 8 |
| Reading 2 | 10 |
| **Total** | **40** |

Every Paper 2 item has four options, one checked answer key, an English explanation, and a
Vietnamese explanation. The answer-key distribution is A=10, B=9, C=10, D=11; no answer
letter is above 35%. The Vietnamese file contains explanations for all 40 new question IDs.
The bank now has 80 unique question stems across Papers 1 and 2. The passages, notices,
leaflet, and distractors are original practice content and contain no copied exam material.

## Verification

- `python -m json.tool content/en/exams/thpt.json`: pass.
- `python -m json.tool content/i18n/vi/exams/thpt.json`: pass.
- `python tools/validate_content.py`: pass; errors=0 (247 existing warnings).
- `python -m unittest discover -s tools/tests`: pass (30 tests).
- `./gradlew testDebugUnitTest assembleDebug`: pass.
