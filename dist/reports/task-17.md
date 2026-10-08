# Task 17 — Store screenshots

## Implemented

- Added `StoreScreenshotTest`, an instrumentation test covering the six requested screens:
  Today with pet, picture exercise, Vietnamese explanation, exam, story, and pet room.
- The test runs every screen in English/Vietnamese and light/dark modes, hides system bars before
  capture, and writes PNGs named `task17_<locale>_<theme>_<screen>.png` to the app's external
  `store-screenshots` directory.
- DemoMode now has deterministic `picture`, `explanation`, and `story` routes for the capture
  test. Existing demo data seeding is reused.

## Verification

- `./gradlew assembleDebugAndroidTest` — PASS.
- `python tools/validate_content.py` — PASS, errors=0 (253 existing warnings).
- ADB/device execution was not possible in this environment: `adb devices` returned no devices and
  no local emulator binary is installed. Therefore `dist/screenshots/` has not been populated with
  PNGs in this run. On the owner's 1080×1920 device, run the test and pull the generated directory
  with:

  ```text
  adb shell am instrument -w -e class com.yourbrand.englishlearn.StoreScreenshotTest com.khankstudio.lingomori.debug.test/androidx.test.runner.AndroidJUnitRunner
  adb pull /sdcard/Android/data/com.khankstudio.lingomori.debug/files/store-screenshots dist/screenshots
  ```

- Store captions were not invented: `ROADMAP_V1.md` assigns caption writing to Claude/owner, and no
  caption source was present in `docs/store/`.

No content files, authoring files, configuration text files, or string resources were changed.
