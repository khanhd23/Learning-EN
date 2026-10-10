# PET / B1 Preliminary Reading expansion

## Delivered

- Added three original B1 Preliminary Reading practice papers.
- Each paper has 32 questions in six sections: Part 1 (5), Part 2 (5), Part 3 (5), Part 4 (5), Part 5 (6), and Part 6 (6).
- Total: 18 section groups and 96 questions.
- Parts 1–5 contain three-option items (78 questions total). Part 6 contains 18 original open-cloze items with typed answers.
- Added English and Vietnamese generated banks, three selectable exam formats, and a bank-structure regression test.
- The format reference is the official Cambridge page: https://www.cambridgeenglish.org/exams-and-tests/preliminary/exam-format/
- The passages, questions, options, answers, explanations, and Vietnamese translations were written as original app content; no official/sample paper text was copied.

## Manual content review

All 96 questions were reviewed after generation. For every item, the answer was checked against its notice, matching text, passage, or sentence context; distractors were checked for being distinct and non-answering; Part 6 answers were checked for grammar and collocation. All 18 Vietnamese passages and all 96 Vietnamese explanations were checked for matching meaning.

## Verification

- `python tools/gen_content.py` — passed; generated `pet_passages=18 pet_items=96`.
- `python tools/validate_content.py` — passed: `errors=0` (244 existing warnings).
- `python -m unittest discover -s tools/tests` — passed: 58 tests.
- `python tools/audit_content.py` — unchanged vocabulary audit baseline: `entries=8674 gold=0 silver=7562 bronze=1112 ngsl_missing=0 errors=2709`.
- `./gradlew.bat testDebugUnitTest --console=plain -q` — passed.
- `./gradlew.bat assembleDebug --console=plain -q` — passed.
