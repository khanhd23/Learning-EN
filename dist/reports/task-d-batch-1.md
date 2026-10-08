# Task D — TOEIC bank batch 1

## Scope

Added the first original bank batch through authoring modules and `tools/gen_content.py`:

- 30 new Part 5 items (word form, tense, preposition, connector, pronoun, and collocation).
- 4 new Part 6 passages × 4 blanks = 16 items.
- Every new item has four options, an English source explanation, and a Vietnamese teaching explanation.

The generated corpus now contains 148 Part 5 items and 12 Part 6 passages. The sectioned
`content/en/exams/toeic.json` bank is intentionally generated after all D batches are complete;
the existing `toeic_p5` and `toeic_p6` formats remain unchanged during the build-out.

## Manual review after generation

I inspected all 30 newly generated Part 5 items and all 16 newly generated Part 6 blanks after
`gen_content.py` shuffled their options. I checked the selected answer in its generated position,
sentence grammar, distractor plausibility, and that each explanation teaches the reason rather than
just repeating the answer. I also read each Part 6 passage as a complete workplace message and
checked that every blank has one contextually valid answer. No content correction was needed.

## Checks

- `python tools/validate_content.py` — `errors=0` (245 existing warnings).
- `python -m unittest discover -s tools/tests` — 30 tests passed; locale `xx` errors=0.
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.
