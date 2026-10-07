# Task 11: Speaking (shadowing)

## Changes

- Added E20 shadowing exercises backed by the owner-provided `sound.json` `shadowing` list.
- Added a Speaking path to the existing Listening & pronunciation session.
- Added Android microphone permission handling. The app explains microphone use before the first request and does not request it earlier.
- Added `SpeechRecognizer` handling with English free-form recognition, sentence alignment through the existing case/punctuation-tolerant grader, per-word green/red feedback, and up to two retries after an unsuccessful attempt.
- E20 is removed from the queue without an error when recognition is unavailable or permission is denied.
- Added a unit test covering sentence alignment and punctuation tolerance used by shadowing.

No new user-visible string keys were required. No content, authoring, strings, or config files were changed.

## Verification

- `python tools/validate_content.py` — PASS (`errors=0`, 313 existing warnings).
- `python -m unittest discover -s tools/tests` — PASS (22 tests).
- `./gradlew check assembleDebug --no-daemon` — PASS.

The Gradle content-audit task still reports the repository's existing audit baseline (`errors=6133`); this task did not modify content or audit inputs.
