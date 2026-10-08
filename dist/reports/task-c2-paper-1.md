# C2 Paper 1 - THPT question bank

Added the first original THPT English practice paper to the English and Vietnamese exam
banks. The paper follows the C1 format and contains 40 unique questions:

| Section | Questions |
|---|---:|
| Notice | 6 |
| Leaflet / advertisement | 6 |
| Sentence arrangement | 5 |
| Text completion | 5 |
| Reading 1 | 8 |
| Reading 2 | 10 |
| **Total** | **40** |

All questions have four options, one answer key, an English explanation, and a Vietnamese
explanation. The answer-key distribution is A=11, B=10, C=9, D=10; no answer letter is
above 35%. The Vietnamese file contains explanations for all 40 question IDs. The passages,
notices, leaflet, and distractors are original practice content and contain no copied exam
material.

## Verification

- `python -m json.tool content/en/exams/thpt.json`: pass.
- `python -m json.tool content/i18n/vi/exams/thpt.json`: pass.
- `python tools/validate_content.py`: pass; errors=0 (247 existing warnings).
- `python -m unittest discover -s tools/tests`: pass (30 tests).
- `./gradlew testDebugUnitTest assembleDebug`: pass.
