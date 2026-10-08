# Task D — TOEIC bank batch 6

## Scope

- 30 additional original Part 5 items on meetings, sales, communication, and workplace usage.
- 4 additional original Part 6 passages × 4 blanks.

Totals after generation: 303 Part 5 items and 32 Part 6 passages in the legacy source corpus.

## Manual review after generation

I manually re-solved all 30 generated Part 5 items after option shuffling and read all 16 new
Part 6 blanks in their full messages. I checked infinitives/gerunds, conditionals, passive forms,
fixed prepositions, connectors, and word forms. I verified the answer keys and both explanation
layers. The generated distractors were natural and no ambiguity or duplicate option remained.

## Checks

- `python tools/validate_content.py` — `errors=0` (245 existing warnings).
- `python -m unittest discover -s tools/tests` — 30 tests passed; locale `xx` errors=0.
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.
