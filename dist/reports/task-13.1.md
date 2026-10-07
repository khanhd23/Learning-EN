# Task 13.1

Implemented the reviewed weekly-quest rules:

- Local weeks are keyed from Monday and reset their counters and paid flags on rollover.
- New words are recorded once at first exposure in a small SQLite `first_seen` table; daily challenges, speaking days, and finished stories have separate weekly counters.
- Daily challenge completion awards 10 coins.
- Each of the four quests pays once per week: 20 new words, 3 daily challenges, 2 speaking days, and 2 finished stories. The speaking quest is hidden when speech recognition is unavailable. Rewards use the cheapest unowned pet item, or 10 coins when all items are owned.
- Added a unit test covering the Sunday-to-Monday rollover boundary.

No new UI strings were required. Existing owner keys are used for quest labels, progress, and item rewards.

## Verification

- `./gradlew testDebugUnitTest --no-daemon` — PASS (31 tasks)
- `python tools/validate_content.py` — PASS (errors=0; existing warnings reported by the validator)
- `python -m unittest discover -s tools/tests` — PASS
- `./gradlew check assembleDebug --no-daemon` — PASS
