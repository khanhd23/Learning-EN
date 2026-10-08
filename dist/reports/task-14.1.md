# Task 14.1: Wire the sectioned exam engine into the app

## Changes

- Loaded the exam banks referenced by sectioned formats from the English asset set and merged optional learner-locale overrides only when their status rows are approved.
- Hid a sectioned format when any referenced bank is missing, while leaving existing non-sectioned formats unchanged.
- Wired sectioned formats through `MockTest.build` and `SessionBuilder` with stable `exam:<bank>:<itemId>` keys and resumable section metadata.
- Removed the obsolete exercise-type gate from bank-item matching; bank sections continue to filter by qtype when configured.
- Added shared-passage display for grouped bank items with a bounded, scrollable passage card, section headings, and existing owner-provided passage strings.
- Added passage context to grouped review items and persisted per-section history rows without changing the old history format.
- Added tests for sectioned build/answer/result flow, resume key round-tripping, approved-only locale overrides, and whole-group selection.

No content files, authoring files, string resources, or new UI strings were changed.

## Verification

- `python tools/validate_content.py` — PASS (`errors=0`; 253 existing warnings).
- `./gradlew testDebugUnitTest --no-daemon` — PASS (25 tests).
- `./gradlew assembleDebug --no-daemon` — PASS.
- `git diff --check` — PASS.

## Notes

The current repository has no production sectioned exam bank yet; the test bank remains under `app/src/test/resources` and is not shipped.
