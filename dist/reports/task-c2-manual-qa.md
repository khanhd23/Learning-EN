# C2 THPT bank - manual QA

Manually reviewed all 200 THPT questions across Papers 1-5, including each passage,
arrangement order, option set, answer key, English explanation, and Vietnamese explanation.

Corrections found during the review:

- Paper 1: corrected the Repair Club inference key and wetland-view key; corrected the
  `its` reference wording; removed an unnecessary gendered distractor.
- Paper 2: corrected the first-aid question so the answer is the option about slowing
  bleeding.
- Paper 4: corrected the order keys for the art display, theatre announcement, and weather
  station announcement; aligned the Vietnamese explanations with those orders.
- Paper 5: corrected the order key for the language-club announcement.
- Vietnamese QA: replaced an English fragment in the wetland pronoun explanation and made
  the first-aid explanation more natural.

Final bank checks: 200 unique IDs, 200 unique stems, 200 Vietnamese explanations, six
sections filled in every paper, and no paper has an answer letter above 35%.

## Verification after manual QA

- `python -m json.tool content/en/exams/thpt.json`: pass.
- `python -m json.tool content/i18n/vi/exams/thpt.json`: pass.
- `python tools/validate_content.py`: pass; errors=0 (247 existing warnings).
- `python -m unittest discover -s tools/tests`: pass (30 tests).
- `./gradlew testDebugUnitTest assembleDebug`: pass.
