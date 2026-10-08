# Task D — TOEIC bank batch 3

## Scope

- 30 additional original Part 5 items focused on human resources, operations, and workplace grammar.
- 4 additional original Part 6 passages × 4 blanks.
- English explanations and Vietnamese teaching explanations were generated from the same authored source rows.

Totals after generation: 211 Part 5 items and 20 Part 6 passages in the legacy source corpus.

## Manual review after generation

I re-solved every new Part 5 item after option shuffling and read all 16 new Part 6 blanks in
context. The review covered agreement, passive voice, conditionals, prepositions, collocations,
word form, and connector logic. I verified that the Vietnamese explanation matches the English
reason and that each passage remains a coherent notice or business message. No answer-key,
duplicate-option, or ambiguous-context issue was found.

## Checks

- `python tools/validate_content.py` — `errors=0` (245 existing warnings).
- `python -m unittest discover -s tools/tests` — 30 tests passed; locale `xx` errors=0.
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.
