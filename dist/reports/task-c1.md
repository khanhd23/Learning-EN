# C1 — THPT format

Added the `thpt` format entry to `config/exam_formats.json` for the 2025+ Vietnamese
high-school graduation English exam. The source is the official Quality Management Agency
(MOET) announcement:

<https://vqa.moet.gov.vn/vi/news/thong-bao/cau-truc-dinh-dang-de-thi-tot-nghiep-thpt-tu-nam-2025-74.html>

The announcement confirms 40 questions for Foreign Languages, 50 minutes, and that Foreign
Languages use one multiple-choice question type. The six section counts configured for the
bank are 6 + 6 + 5 + 5 + 8 + 10 = 40:

| Section | Type | Count |
|---|---|---:|
| Notice | group | 6 |
| Leaflet / advertisement | group | 6 |
| Sentence arrangement | mcq | 5 |
| Text completion | group | 5 |
| Reading 1 | group | 8 |
| Reading 2 | group | 10 |

The official announcement also links the MOET sample-paper folder. The `thpt` sections point
to the future `thpt` bank; until C2 supplies that bank, normal repository filtering keeps this
format unavailable rather than showing an incomplete exam.

## Verification

- `python -m json.tool config/exam_formats.json`: pass.
- `python tools/validate_content.py`: pass; errors=0 (247 existing warnings).
- `python -m unittest discover -s tools/tests`: pass (30 tests), including the new C1 format
  shape test.
- `./gradlew testDebugUnitTest assembleDebug`: pass.
