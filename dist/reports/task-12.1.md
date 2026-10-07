# Task 12.1: Story reader review fixes

## Changes

- Added a story-only Translation toggle using the owner key `show_translation`; it is hidden by default and persisted independently.
- Added a standard pushed-screen title bar with a back arrow.
- Added play-all and slow-play controls. Stories are split into sentences, TTS reports the active sentence, the active sentence is highlighted, and tapping a sentence replays it.
- Target words now use authored forms plus common inflections (including plural, `-ed`, `-ing`, `y` → `-ies`/`-ied`, and irregular/form-map values). Matches are underlined and tinted; they are not rendered as link-blue text.
- Replaced story question buttons with the shared `OptionCard`, locking answers and showing right/wrong states.
- Added a result view using the existing `correct_answer_is` key to show `n/m` before exit.
- Preserved the existing story XP/pet reward path.

No new string keys were required. No content, authoring, strings, or `config/*.txt` files were changed.

## Verification

- `python tools/validate_content.py` — PASS (`errors=0`, 310 existing warnings).
- `python -m unittest discover -s tools/tests` — PASS (22 tests).
- `./gradlew check assembleDebug --no-daemon` — PASS.

The existing content audit baseline remains unchanged in scope; the Gradle audit task reports its repository baseline.
