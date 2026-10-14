# Task 7 batch 43 — semantic gloss cleanup

## Scope

This continuation reviewed the remaining locale quality findings after the UTF-8 repair.
Vietnamese glosses were shortened semantically from their existing meanings, with explicit
word-level rewrites for long or ambiguous definitions. Hindi warnings were rewritten in Hindi
where the gloss had been left identical to the English definition.

## Result

- Vietnamese audit errors: 604 → 0.
- Vietnamese bronze entries: 599 → 0; silver entries: 8,075 → 8,674.
- Hindi validation warnings: 13 → 0.
- Vietnamese words remain 7,809 approved + 1,028 reviewed, with 0 draft.
- Hindi remains 10,618 reviewed entries, with 0 missing or draft.
- No approval metadata was added.

## Validation

- `python -X utf-8 tools/audit_content.py`: 0 errors.
- `python -X utf-8 tools/validate_content.py`: 0 errors, 0 warnings.
- `python -X utf-8 tools/locale.py check --all`: all locales passed with 0 errors.
- Unit tests and Gradle build: 73 tests passed; Gradle BUILD SUCCESSFUL.
