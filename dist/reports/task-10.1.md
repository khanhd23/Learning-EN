# Task 10.1: sound practice integration

## Changes

- Added a Listening & pronunciation card to the Exam/Practice hub, opening the existing sound session.
- Added at most one dictation or minimal-pair exercise to review and topic sessions after three completed sessions.
- Routed locale sound focus tips through the existing approval gate (`status.json` kind `sound`).
- Added owner-provided `ins_dictation`, `ins_minimal_pair`, `sound_tip`, and `play_slow` usage. Slow replay uses a TTS rate of 0.7.
- Dictation now aligns word sequences with edit-distance backtracking. Missing, extra, and swapped words no longer shift every later comparison; the learner sentence and expected sentence are shown with error highlighting.
- Dictation feedback uses the locale example translation when the sentence ID has an approved translation in the locale word pack.

No new UI strings were needed.

## Verification

- `python tools/validate_content.py` — passed (`errors=0`, 313 existing warnings).
- `python -m unittest discover -s tools/tests` — passed (21 tests).
- `./gradlew check assembleDebug --no-daemon` — passed.
- Added unit tests for missing, extra, and swapped sentence words, plus the existing case/punctuation and typo behavior.

The existing content audit still reports the repository baseline (`errors=6133`); this task changed no content or audit inputs.

## Scope

Only Kotlin code, tests, and this report were changed. `content/**`, `tools/authoring/**`, `strings.xml`, and `config/blocked_senses.txt` were not modified.
