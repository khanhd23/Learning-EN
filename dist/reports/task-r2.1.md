# Task R2.1 — de-duplicated dependency licenses

## What changed

- Updated `tools/build_license_assets.py` to split Google `third_party_licenses.txt` catalogs into
  individual entries, group repeated standard license families, and keep one verbatim representative
  text with the artifacts and source labels that use it.
- AndroidX, Kotlin, and Guava Apache-2.0 entries now point to the existing shared offline
  `licenses/apache-2.0.txt` asset instead of copying that text into every dependency entry.
- Added a regression test proving identical license text is emitted once while both artifacts remain
  listed.
- No UI strings, content, authoring files, or hint text were changed. The 💡 hint rule remains:
  hints teach the solving method and do not remove options or reveal the answer before submission.

## Size

| Measure | Before R2.1 | After R2.1 |
|---|---:|---:|
| Generated `dependencies.txt` (uncompressed) | 4,065,245 B | 140,634 B |
| Change | — | -3,924,611 B (-96.54%) |

Release APK: `app/build/outputs/apk/release/app-release-unsigned.apk` — 7,373,258 B.

The required `unzip -lv` measurement reported:

```text
140634  Defl:N  36216  74%  ...  assets/licenses/dependencies.txt
```

Thus the uncompressed catalog is below the 150 KB target; its compressed APK entry is 36,216 B.

## Verification

- `python tools/validate_content.py` — pass, `errors=0` (253 existing warnings).
- `python -m unittest discover -s tools/tests` — pass, 29 tests.
- `./gradlew check assembleDebug` — pass.
- `./gradlew assembleRelease -PallowTestAds` — pass; produced the unsigned benchmark/review APK.

No push was performed.
