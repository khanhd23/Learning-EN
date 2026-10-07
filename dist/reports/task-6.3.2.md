# Task 6.3.2

Implemented the reviewed accented-gloss search fix:

- Content DB schema version 3 now stores the original gloss with diacritics in `word_fts.gloss_raw`, while retaining the normalized FTS field for accent-insensitive matching.
- Search returns `gloss_raw` for display and ranks an exact raw-gloss match ahead of normalized gloss/prefix matches. Thus `cá` selects the `fish` gloss before `cà phê`.
- Added a builder regression test for raw-gloss preservation and accent-sensitive ordering; the existing >200-result SQL ranking test remains covered.

No UI strings, content, authoring files, or config text files were changed. No new strings were needed.

## Verification

- `python -m unittest discover -s tools/tests` — PASS (23 tests)
- `./gradlew testDebugUnitTest --no-daemon` — PASS
- `./gradlew generateContentAssets --rerun-tasks --no-daemon` — PASS; rebuilt schema 3 asset
- `python tools/validate_content.py` — PASS (errors=0; existing warnings reported by the validator)
- `./gradlew check assembleDebug --no-daemon` — PASS
