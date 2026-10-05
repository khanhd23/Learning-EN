# Task 3: Validation rules

## What changed

- Extended `tools/validate_content.py` with build-blocking checks for locale IDs, placeholders and markup, leftover `<keep>` tags, duplicate gloss fragments, Vietnamese leakage in non-`vi` locales, stale approved source hashes, and `ui.json`/`strings.xml` key parity.
- Added warning-only checks for untranslated glosses and unusually short or long glosses.
- Updated `tools/gen_content.py` so generated Vietnamese content refreshes `vi/status.json` hashes. Unchanged records retain their review metadata; changed source records become `approved` with `by: "owner"` and a new `src` hash. It also keeps the Vietnamese UI key set synchronized with `strings.xml`.
- Normalized repeated comma/semicolon gloss fragments during generation. The 18 regenerated entries were: `prevent_s1`, `root_s1`, `constantly_s1`, `bat_s1`, `soup_s1`, `depression_s1`, `derivative_s1`, `grease_s1`, `whence_s1`, `salute_s1`, `heating_s1`, `tenth_s1`, `dander_s1`, `wow_s1`, `accomplished_s1`, `crimp_s1`, `gorgeous_s1`, and `elicit_s1`.
- Added eight small failing fixtures in `tools/tests/test_validate_content.py`, including a generator status-refresh test.

## Counts before / after

| Check/data | Before | After |
|---|---:|---:|
| Folder-layout status: UI entries | 0 | 550 |
| Vietnamese word entries | 5,499 | 5,499 |
| Approved Vietnamese status entries with stale hashes | not checked by the old validator | 0 |
| Task 3 validation errors | not implemented | 0 |
| Task 3 validation warnings | not implemented | 368 |

The existing content audit remains at `7,792` errors; these are pre-existing content-quality issues from earlier tasks and are reported by the audit tool without failing the build.

## Verification

- `python tools/gen_content.py` passed.
- `python tools/validate_content.py` passed: `errors=0 warnings=368`.
- `python -m unittest discover -s tools/tests -v` passed: 8 tests.
- `gradlew check assembleDebug` passed.

Human review is still needed for the warning-only gloss length/English-identity findings and the pre-existing audit queue. No `tools/authoring/` content files were edited.
