# Task 7 batch 42 — locale semantic completion and UTF-8 repair

## Scope

This batch completed the remaining Vietnamese word review and repaired Hindi semantic locale
packs and review CSVs whose prior writes contained replacement question marks.

## Counts

- Vietnamese words: 7,809 approved + 1,028 reviewed before; 7,809 approved + 1,028 reviewed after; 0 draft.
- Hindi words: 8,837 reviewed after repair; 0 missing, 0 draft, 0 approved.
- Hindi full tracked locale: 10,618 reviewed; 0 missing, 0 draft, 0 approved.

No approval metadata was added.

## Validation

- `python -X utf-8 tools/validate_content.py`: 0 errors, 13 warnings.
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, vi, and xx.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: 73 passed.
- `gradlew.bat check assembleDebug --no-daemon`: passed.
- `python -X utf-8 tools/audit_content.py`: 604 English source-content errors remain in the audit output. They are recorded for a separate source cleanup task and were not changed in this batch.

The owner still needs to approve reviewed Vietnamese and Hindi entries before they can ship.
