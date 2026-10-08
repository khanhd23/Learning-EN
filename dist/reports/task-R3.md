# Task R3 — Release hardening

## Implemented

- Release lint tasks now explicitly depend on generated offline license assets, so R8/lint can
  validate the same asset graph that is packaged.
- Debug StrictMode now logs disk reads, disk writes, and network access on the main thread during
  startup. Content and learning-state prewarming remains on the existing `prewarm` worker.
- Added an app baseline profile covering `EnglishApp.onCreate`, `MainActivity.onCreate`, tab
  selection, and the first Words screen.
- Added a debug instrumentation smoke test covering the five tabs, topic/word detail, grammar
  lesson, mock test, and the offline licences screen. DemoMode now exposes settings/licences paths
  for that test.

## Verification

| Check | Result |
|---|---|
| `python tools/validate_content.py` | PASS — errors=0, warnings=253 (pre-existing) |
| `python -m unittest discover -s tools/tests` | PASS — 28 tests |
| `./gradlew testDebugUnitTest assembleDebug assembleDebugAndroidTest` | PASS |
| `./gradlew assembleBenchmark` | PASS |
| `./gradlew bundleRelease` | PASS — R8, lintVital and AAB packaging completed |
| Baseline profile merge | PASS — app startup and Words entries present |

## Size and timing

- Debug APK: 16,315,416 bytes.
- R8 benchmark APK: 7,761,586 bytes.
- Release AAB: 10,770,078 bytes.
- Cold-start timing and on-device smoke execution could not be collected: no ADB device or local
  emulator was available (`adb devices` returned no devices). The instrumentation APK compiles and
  is ready for the owner's connected-device run.
- The release AAB is currently unsigned because no `signing.*` values are configured in the local
  ignored `secrets.properties`. The existing release signing path will sign once the owner supplies
  the upload keystore values; do not upload this local unsigned artifact.

No content files, authoring files, configuration text files, or string resources were changed.
