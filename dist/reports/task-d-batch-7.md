# Task D — TOEIC bank completion batch

## Scope

- 3 final original Part 5 items.
- 8 final original Part 6 passages in this completion batch (4 in D7 and 4 in D8), each with 4 blanks.
- Final legacy source totals: 306 Part 5 items and 40 Part 6 passages.

## Manual review after generation

I re-solved the three generated Part 5 questions and all 32 generated Part 6 blanks after the
generator shuffled their options. I checked the final answer positions, grammar and collocation
logic, distractor quality, and the English/Vietnamese explanations. I read each of the eight
business messages from start to finish; all blanks have one contextually valid answer and the
messages remain coherent. The source contains more than the requested 300 Part 5 items and
exactly 40 Part 6 passages.

## Checks

- `python tools/validate_content.py` — `errors=0` (245 existing warnings).
- `python -m unittest discover -s tools/tests` — 30 tests passed; locale `xx` errors=0.
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.

