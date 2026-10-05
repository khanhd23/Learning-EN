# Task 6: Approved-only content and language picker

## Changes

- Added a Kotlin approval gate that checks `approved` status rows and source hashes, then filters topics, word senses, confusables, grammar points, questions, passages, pet lines, and tips before the app exposes `Content`.
- A word is included only when sense 1 is approved; later senses are included only when their own status row is approved.
- Grammar points and questions are included only when their locale status row is approved. Stale approved rows are excluded.
- Locale selection now requires fully approved UI plus at least 95% approved Level 1 vocabulary and grammar. Unavailable locales fall back to English, never Vietnamese.
- Per-level approval gates expose only qualifying levels; other vocabulary and grammar levels display `Coming soon`.
- Added a four-step onboarding flow with an `I speak: ___` language step. The list contains only selectable locales, defaults to the current app/device locale when selectable, and saves the choice. Settings uses the same gated list.

## Counts and tests

- English core: 5,336 words and 38 grammar points before and after; no content files were edited.
- Vietnamese pack: approval gate passes; all current approved content remains available.
- Fake `xx` test locale: 20/20 Level 1 words and 2/2 Level 1 grammar points approved; Level 2 is unapproved and unavailable. The locale is selectable because Level 1 is at least 95% approved.
- `ApprovalGateTest`: pass, including stale approved-row rejection and the fake `xx` Level 1-only scenario.
- New string keys: none. The implementation reuses existing localized resources; the onboarding card renders the required `I speak: ___` label from the selected locale.
- Existing string values were not changed.

## Verification

- `python tools/gen_content.py`: pass before work began.
- `./gradlew testDebugUnitTest`: pass.
- Final `./gradlew check assembleDebug`: pass (`BUILD SUCCESSFUL`).

## Human review

The approval policy is code-driven; locale status remains the source of truth for future packs.
