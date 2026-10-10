# E1 — TOEIC-style Part 7 v1 completion

## Delivered

- Added the final five original passage groups for the v1 scope.
- Final Part 7 bank: 20 groups and 60 unique questions.
- Group types: single, double, and triple documents.
- Changed the format from a preview to `toeic_p7_practice`, which selects 54
  questions from whole groups. No passage is split across a practice session.
- The learner-facing description explicitly says that the material is original
  practice and not an official TOEIC test.

## Manual educational review

I read all five new passages in English and Vietnamese and re-solved all 15 new
questions from the final option order. I checked each answer against the
passage, all dates/times/locations, cross-document references, distractor
grammar, and Vietnamese explanations. The complete 60-question set has now
been reviewed in the same way across batches 1–3.

## Verification

- `python tools/build_toeic_bank.py`: generated 20 groups and 60 Part 7
  questions, alongside 306 Part 5 questions and 40 Part 6 passages.
- `python tools/validate_content.py`: 0 errors; 244 existing warnings about
  Vietnamese word-gloss length.
- `python tools/locale.py check --all`: `vi` 0 errors; `xx` 0 errors.
- `python -m unittest discover -s tools/tests`: 35 tests passed.
- `./gradlew.bat check testDebugUnitTest assembleDebug`: pass.
- Group-selection test confirms that 54 questions are reachable using whole
  groups; the 60-question bank gives variation between sessions.

## Scope note

This completes the v1 Part 7 practice bank, not the long-term roadmap target
of 150 original passage groups. Further expansion can add more reviewed
groups without changing the engine or copying any official material.
