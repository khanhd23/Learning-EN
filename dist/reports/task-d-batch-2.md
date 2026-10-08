# Task D — TOEIC bank batch 2

## Scope

- 30 additional original Part 5 items.
- 4 additional original Part 6 workplace/service passages × 4 blanks.
- Each item has four options, a generated answer index, an English explanation, and a Vietnamese explanation.

Totals after generation: 177 Part 5 items and 16 Part 6 passages in the legacy source corpus.

## Manual review after generation

I inspected all 30 new Part 5 items and all 16 new Part 6 blanks after option shuffling. I
re-solved each item from its sentence context, checked the generated answer position, and checked
that the Vietnamese explanation teaches the grammar or vocabulary point. I also read each passage
as a complete message and checked the connector, tense, word form, and collocation blanks. No
duplicate option or ambiguous key was found.

## Checks

- `python tools/validate_content.py` — `errors=0` (245 existing warnings).
- `python -m unittest discover -s tools/tests` — 30 tests passed; locale `xx` errors=0.
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.

