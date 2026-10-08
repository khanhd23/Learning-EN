# Task R2 — Full offline license texts

## What changed

- Added `tools/build_license_assets.py`, run during the Android build, to scan the resolved
  release runtime AAR/JAR artifacts and bundle their embedded license/notice text offline.
- Added Apache-2.0 fallback text for resolved AndroidX, Kotlin, and Guava artifacts whose Maven
  archive does not carry a copy. The text is taken byte-for-byte from the existing official
  `app/src/main/assets/licenses/apache-2.0.txt` asset.
- Added the generated `licenses/dependencies.txt` asset to the existing Settings → Licenses
  screen. The existing full Apache-2.0 text remains visible for AndroidX and Noto Emoji, and the
  existing CC BY-SA text remains available.
- Added tool tests for archive extraction and Apache fallback de-duplication.

The Google Ads and UMP artifacts contribute their embedded third-party license catalogs; the app
does not fetch license text or any other data at runtime.

## APK size

| Build | APK bytes |
|---|---:|
| Debug before R2 | 16,315,174 |
| Debug after R2 | 16,315,416 |
| Change | +242 bytes |

The generated uncompressed dependency catalog is 4,065,245 bytes and contains 13 de-duplicated
full license-text entries. The compressed APK contains `assets/licenses/dependencies.txt` alongside
the existing Apache and CC BY-SA assets.

## Verification

- `python tools/validate_content.py` — pass, `errors=0` (253 existing warnings).
- `python -m unittest discover -s tools/tests` — pass, 28 tests.
- `./gradlew testDebugUnitTest` — pass, 27 tests.
- `./gradlew assembleDebug` — pass.

No content, authoring files, or strings were changed. No network is used by the app for licenses,
and no push was performed.
