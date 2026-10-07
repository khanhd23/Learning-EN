# Task 13: Daily challenge and pet quests

## Changes

- Added a deterministic five-item daily challenge from due, weak, and new work; the day changes the exercise mix while keeping the item pool stable.
- Added the Daily challenge card above Review today and a weekly quest summary card.
- Completing the daily challenge awards an existing pet item, tracks daily completions, and unlocks a weekly reward after three daily challenges.
- Added owner pet lines for daily start/completion, quest completion, and streak-shield rewards.
- Capped streak shields at two and award one when the learner reaches a seven-day streak.
- Added a unit test proving the two-shield cap.

No new string keys were required. Existing owner keys and pet lines were used. No content, authoring, strings, or `config/*.txt` files were changed by this task.

## Verification

- `python tools/validate_content.py` — PASS (`errors=0`, 313 existing warnings).
- `python -m unittest discover -s tools/tests` — PASS (22 tests).
- `./gradlew assembleDebug --no-daemon` — PASS.
- `./gradlew check --no-daemon` remains blocked by the existing Android Lint `ExperimentalDetector` crash while analyzing `Exercise.kt`; Kotlin compilation and unit tests pass.
