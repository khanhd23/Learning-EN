# Task D — TOEIC bank batch 4

## Scope

- 30 additional original Part 5 items on customer service, finance, refunds, and payment.
- 4 additional original Part 6 passages × 4 blanks.

Totals after generation: 242 Part 5 items and 24 Part 6 passages in the legacy source corpus.

## Manual review after generation

I manually solved all 30 new Part 5 questions from the generated option order and checked the
grammar point, distractors, answer key, and Vietnamese explanation. I also read all 16 new Part 6
blanks within their complete customer-service and finance messages. The review specifically
covered passive time clauses, tag questions, fixed prepositions, conditionals, collocations, and
word forms. No ambiguous answer or duplicate option was found.

## Checks

- `python tools/validate_content.py` — `errors=0` (245 existing warnings).
- `python -m unittest discover -s tools/tests` — 30 tests passed; locale `xx` errors=0.
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.
