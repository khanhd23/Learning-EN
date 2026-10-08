# Task D — TOEIC bank batch 5

## Scope

- 30 additional original Part 5 items covering travel, hospitality, facilities, and technology.
- 4 additional original Part 6 passages × 4 blanks.

Totals after generation: 272 Part 5 items and 28 Part 6 passages in the legacy source corpus.

## Manual review after generation

I manually checked all 30 generated Part 5 items and all 16 generated Part 6 blanks. I re-solved
the shuffled choices, checked the intended grammar/collocation point, and verified the English and
Vietnamese explanations. During review I replaced four misspelled Part 6 distractors with natural
English distractors, regenerated the content, and repeated the review. The final passages read
as coherent hotel, app, flight, and equipment notices.

## Checks

- `python tools/validate_content.py` — `errors=0` (245 existing warnings).
- `python -m unittest discover -s tools/tests` — 30 tests passed; locale `xx` errors=0.
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.
