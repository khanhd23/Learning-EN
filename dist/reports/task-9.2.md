# Task 9.2: picture distractor ordering

## Changes

- Picture exercise distractors now shuffle the same-topic group independently, then shuffle and use same-part-of-speech fallbacks.
- Duplicate image resources are removed after the group ordering is established, so duplicate pictures cannot consume multiple answer slots.
- Applied the same ordering to E15/E16 and picture matching selection.
- Added unit tests covering topic priority and duplicate-image handling.

## Verification

- `python tools/validate_content.py` — passed (`errors=0`, 313 existing warnings).
- `python -m unittest discover -s tools/tests` — passed (21 tests).
- `./gradlew check assembleDebug --no-daemon` — passed.
- The existing content audit baseline remains `errors=6133`; no content or audit inputs were changed.

No new UI strings were needed.

## Scope

Only Kotlin code, tests, and this report were changed. `content/**`, `tools/authoring/**`, `strings.xml`, and `config/blocked_senses.txt` were not modified.
