# Task 20 — report a content mistake

## Changes

- Added a shared `MistakeReport` builder that produces the subject and body from the owner-provided
  resource templates and includes the item ID, screen name, and app version.
- Updated the exercise feedback panel to use the full report subject/body and the owner-provided
  `report_mistake` accessibility label.
- Updated WordDetail to show the owner-provided `report_mistake` label and include a complete body.
- Added `report_mistake` to every displayed item in the mock exam review screen.
- All three entry points use `ACTION_SENDTO` with `mailto:`, use `BuildConfig.SUPPORT_EMAIL`, and
  show `report_mistake_no_app` with the address when no email app can handle the intent.
- Added a unit test covering item ID, screen name, version, subject, and body construction.

No strings, content, authoring files, or configuration text files were changed.

## Verification

- `python tools/validate_content.py` — pass (`errors=0`; 253 existing warnings).
- `python -m unittest discover -s tools/tests` — pass (29 tests).
- `./gradlew testDebugUnitTest` — pass, including `MistakeReportTest`.
- `./gradlew check assembleDebug` — pass.

## Review notes

The report action opens the user's email app only; it does not make a network request, request a
permission, or store report data.
