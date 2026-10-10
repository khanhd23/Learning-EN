# IELTS Academic short-reading expansion

## Delivered

- Added five original IELTS Academic short-reading mini papers.
- Each paper has two original passages and five four-option questions per passage: 10 questions per paper, 50 questions total.
- Added English and Vietnamese generated banks, five selectable practice formats, and a bank-structure regression test.
- The app labels these as short-reading practice only. They are not official IELTS papers and do not estimate a band score; full IELTS Reading remains outside the current V1 scope.
- Format context reference: https://ielts.org/take-a-test/test-types/ielts-academic
- All passages, questions, options, answers, explanations, and Vietnamese translations were written as original app content; no official/sample paper text was copied.

## Manual content review

All 50 questions were reviewed after generation. For each question, the answer was checked against the passage, distractors were checked for being distinct and unsupported or incorrect, and the English explanation was checked against the answer. All 10 Vietnamese passages and 50 Vietnamese explanations were checked for matching meaning. The five papers have different passages and question IDs.

## Verification

- `python tools/gen_content.py` — passed; generated `ielts_passages=10 ielts_items=50`.
- `python tools/validate_content.py` — passed: `errors=0` (244 existing warnings).
- `python -m unittest discover -s tools/tests` — passed: 62 tests.
- `./gradlew.bat testDebugUnitTest --console=plain -q` — passed.
- `./gradlew.bat assembleDebug --console=plain -q` — passed.
