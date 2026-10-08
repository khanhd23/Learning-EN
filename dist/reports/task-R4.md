# Task R4: Rename the application package id

## Changes

- Changed `config/app_config.json` applicationId from `com.engpet.learn` to `com.khankstudio.lingomori`.
- Updated the localization plan's current applicationId reference.
- Kept the Kotlin namespace and package names unchanged, as allowed by the roadmap.
- No other source, content, string, or authoring files required an old applicationId update.

## Verification

- `python tools/validate_content.py` — PASS (`errors=0`; 253 existing warnings).
- `./gradlew testDebugUnitTest --no-daemon` — PASS (25 tests).
- `./gradlew assembleDebug --no-daemon` — PASS.
- Debug APK generated at `app/build/outputs/apk/debug/app-debug.apk`.
- ADB executable was found, but no device was connected, so installation and launcher-label verification could not be performed.

## Owner action

Connect a device or emulator and install the debug APK before the first upload. The debug package should resolve as `com.khankstudio.lingomori.debug`; benchmark keeps its `.bench` suffix.
