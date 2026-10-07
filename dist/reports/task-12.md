# Task 12: Story reader

## Changes

- Added story domain models and optional parsing for owner-authored `stories.json`.
- Added story approval-gate entries; locale stories are shown only when their story id is approved.
- Added an offline `StoryScreen` with target-word highlighting, tappable word cards (gloss, TTS, save), question flow, explanations, XP, and pet food.
- Added story entry buttons to topic cards after the learner has seen a word in that topic.
- Made the Gradle story asset copy optional so builds remain valid before generated story assets are available.

No strings, content, authoring, or `config/*.txt` files were changed. Existing string keys were reused. No new UI keys are required by the code; the story title/question/explanation text comes from owner data.

## Owner-data status

The owner source `tools/authoring/stories.json` and its generator changes were present as uncommitted workspace changes, but generated `content/en/stories.json` and locale output were not present. They were not modified or committed because this task forbids edits to `content/**` and `tools/authoring/**`. Until the owner runs the generator and supplies approved story rows, the app correctly builds without showing stories.

## Verification

- `python tools/validate_content.py` — PASS (`errors=0`, 313 existing warnings).
- `python -m unittest discover -s tools/tests` — PASS before the Task 12 code change (22 tests).
- `./gradlew assembleDebug --no-daemon` — Kotlin/resource/package stages PASS.
- `./gradlew check --no-daemon` — BLOCKED by an Android Lint tool crash in `ExperimentalDetector` while analyzing `Exercise.kt` (`Unexpected owner function: null`); this is an analyzer failure, not a source compilation error.

New string keys needed: none.
