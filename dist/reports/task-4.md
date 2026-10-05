# Task 4: Locale operations tool

## What changed

- Added `tools/locale.py` as the single locale operations CLI with `new`, `draft`, `export`, `import`, `approve`, `check`, and `report` commands.
- `new <locale>` creates the complete folder-layout scaffold with empty values and `missing` status records for the English IDs.
- `draft` reads `TRANSLATE_PROVIDER` and `TRANSLATE_API_KEY` from `secrets.properties`; `--provider none` performs no network request and only marks unchanged empty scaffold entries as `draft`. DeepL and Google Cloud use their official translation APIs. REWRITE confusable advice is never machine-drafted.
- `export` writes UTF-8 CSV batches of at most 300 rows with the required review columns. `import` accepts only known keys, records the reviewer and English source hash, and marks entries `reviewed`. `approve` promotes only `reviewed` entries.
- `check` delegates to the Task 3 locale rules, and `check --all` checks every folder locale. `report` writes `dist/locale-status.md` with missing/draft/reviewed/approved percentages by locale, level, and file, plus stale counts.
- Added provider settings to `secrets.properties.example`.
- Added the full-cycle test in `tools/tests/test_locale.py`, including a temporary `xx` locale.
- Added a standard-library import shim because the required `tools/locale.py` filename otherwise shadows Python's standard `locale` module when Gradle runs tools from that directory.

## Counts

The generated scaffold covers 46 topics, 5,499 word senses, 32 confusable sets, 38 grammar points, 641 questions, 8 passages, 550 UI keys, 9 pet moods, and 26 tips. The test cycle imports and approves one reviewed word entry; all other scaffold entries remain in their intended statuses.

## Verification

- `python tools/gen_content.py` passed.
- `python -m unittest discover -s tools/tests -v` passed: 9 tests.
- Full `xx` cycle passed: `new → draft --provider none → export → import → approve → check → report`.
- `python tools/locale.py check --all` passed for `vi`.
- `python tools/validate_content.py` passed with 0 errors.
- `gradlew check assembleDebug` passed.

The content audit still reports 7,792 pre-existing quality errors; Task 4 does not alter the audit or authoring content. No files under `tools/authoring/` were edited.
