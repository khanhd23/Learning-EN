# Task 2: Per-locale folder layout

## What changed

- Migrated Vietnamese from the legacy `content/i18n/vi.json` pack to `content/i18n/vi/`.
- Added the target files: `topics.json`, `words.json` keyed by stable sense ID, `confusables.json`, `grammar.json`, `questions.json`, `pet.json`, `tips.json`, `ui.json`, `l1_notes.json`, and `status.json`.
- `status.json` marks existing Vietnamese content `approved` with `by: "legacy-vi"` and a short source hash. The pack is complete and remains the only shippable locale.
- Added `tools/migrate_locale_layout.py` with UTF-8 writing and a lossless `--round-trip` check.
- Updated `ContentRepository`, locale models, content asset generation, validation, regression tests, and locale documentation for the folder layout.
- Removed the eight old empty scaffold files: `ar.json`, `fr.json`, `hi.json`, `id.json`, `ja.json`, `ko.json`, `pt-BR.json`, and `th.json`.
- No files in `tools/authoring/` were modified.

## Counts and preservation

| Content | Before (legacy pack) | After (folder pack) |
|---|---:|---:|
| Topics | 46 | 46 |
| Word entries | 5336 | 5336 |
| Localized sense entries | 5499 | 5499 |
| Confusable sets | 32 | 32 |
| Grammar points | 38 | 38 |
| Questions | 579 | 579 |
| Passage translations | 8 | 8 |
| Pet moods | 9 | 9 |
| Tips | 26 | 26 |
| Approved status records | — | 6299 |

The migration was run with `--round-trip`; reconstruction matched the legacy pack before the legacy file was removed. Spot checks covered 20 words, 5 grammar points, and 10 questions, including moved question translations/fixes.

## Verification

- `python tools/validate_content.py`: passed with 0 errors and 0 warnings.
- `python tools/audit_content.py`: `ngsl_missing=0`; no new migration errors.
- `gradlew.bat check assembleDebug`: `BUILD SUCCESSFUL`.
- Android unit tests, including the locale question-field regression test: passed.

Task 2 is ticked in `docs/localization/PLAN.md`. No push was performed.
