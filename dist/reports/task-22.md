# Task 22: Store screenshots

## Status

Blocked by the local environment: the screenshot test requires a connected Android device or an
emulator at 1080×1920 (or the closest), but none is available in this workspace.

- `adb devices -l`: no connected devices.
- Android emulator executable/AVD listing: emulator not found.
- `dist/screenshots/`: not created; no screenshots were fabricated.
- `StoreScreenshotTest`: not run because there is no instrumented-test target.

No application code, strings, content, or test files were changed for this task.

## Verification

The required repository checks remain green from the preceding task:

- `python tools/validate_content.py`: pass; errors=0.
- `python -m unittest discover -s tools/tests`: pass.
- `./gradlew testDebugUnitTest assembleDebug`: pass.

To finish Task 22, connect an Android device or start an emulator, run
`StoreScreenshotTest.captureStoreScreens`, then pull the test output into `dist/screenshots/` and
review the generated English/Vietnamese light/dark captures for cut text or empty data.
