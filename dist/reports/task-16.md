# Task 16 — Backup and restore learning data

## Implemented

- Settings now exposes “Back up progress” and “Restore progress” using the Android system file
  picker (`ACTION_CREATE_DOCUMENT` and `ACTION_OPEN_DOCUMENT`). No storage permission, account, or
  network access is used.
- The backup is one UTF-8 JSON document with format `lingomori-progress` and version `1`.
  It contains learning items, tags, day history, grammar stars, first-seen data, test history,
  settings (including weekly quest counters), and pet state.
- Restore parses and validates the document off the UI thread, summarizes the backup, asks for
  confirmation, then replaces the current progress only after the user confirms.
- Unknown/corrupt files show the owner-provided invalid-file message; files with a newer version
  show the owner-provided update message. Older v1 files with optional sections omitted remain
  readable through defaults.
- Existing owner strings were used; no string resources were changed.

## Verification

- `./gradlew testDebugUnitTest assembleDebug` — PASS.
- `ProgressBackupTest` covers round-trip summary, an older/minimal v1 file, and rejection of a
  newer version.
- `python tools/validate_content.py` — PASS, errors=0 (253 existing warnings).
- No files under `content/`, `tools/authoring/`, `config/`, or `res/values*` were changed.
