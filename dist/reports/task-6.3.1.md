# Task 6.3.1 — Content DB follow-ups

## Changes

- Added the generated `content/content.db.sha256` asset. `ContentDb` reads this 66-byte hash before opening the database and only copies the 6 MB database to `noBackupFilesDir` when the copied hash differs or is missing. The database is not read just to discover its hash on every launch.
- Moved search ordering into SQLite before `LIMIT`: exact normalized lemma, normalized lemma prefix, normalized gloss match, remaining match; then level, gold/silver/bronze tier, NGSL rank, and lemma.
- Added benchmark logging for database readiness and `searchWords` duration.
- Added builder tests for the hash sidecar and a 251-hit search fixture where the exact lemma is inserted after 200+ other matches and must still be first.

## Verification

- `python tools/validate_content.py` — passed: 0 errors, 320 warnings.
- `python -m unittest discover -s tools/tests` — passed: 17 tests.
- `./gradlew check assembleDebug assembleBenchmark` — passed.
- Benchmark APK contains both `assets/content/content.db` and `assets/content/content.db.sha256`.

## Benchmark

The existing benchmark APK before this change was 5,943,043 bytes. The rebuilt benchmark APK is 5,942,133 bytes, a reduction of 910 bytes. The generated DB is 6,045,696 bytes; the shipped hash sidecar is 66 bytes.

The first cold SQLite connection/query for `go` against the generated DB measured 67.794 ms on the build host; repeated fresh connections measured 4.25–4.45 ms. The connected physical device was locked and rejected ADB input injection, so an end-to-end UI cold-start/search measurement on that device could not be captured in this run. The app now emits `EngPerf` timings for `content db ready` and `searchWords`, so the device measurement can be captured on an unlocked benchmark device.

## Review note

The owner’s pre-existing changes in `docs/content/CONTENT_STANDARD.md`, `tools/authoring/entry_corrections.tsv`, and `tools/gen_content.py` were preserved and are not part of this task.
