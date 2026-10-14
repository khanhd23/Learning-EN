# Hindi vocabulary semantic review: batches 80?81

Date: 2026-10-14
Locale: `hi`
Reviewer: `codex-llm-semantic-hi-2026-10-14`

## Result

Completed 120 additional Hindi word-sense translations from the English source of truth in `content/en/words.json`:

- Before: 4,921 `reviewed`, 3,916 `missing`
- After: 5,041 `reviewed`, 3,796 `missing`
- Added in this batch: 120
- `approved`: 0; approval remains a human review step
- Target: 5,000 reviewed senses reached

The glosses and examples were written as semantic Hindi translations per entry. English vocabulary being taught remains in the examples where required by the locale rules. No IDs were renamed or renumbered.

## Validation

- `python tools/validate_content.py`: passed (`errors=0`, `warnings=0`)
- `python tools/audit_content.py`: passed (`errors=0`)
- `python tools/locale.py check --all`: passed for `es`, `hi`, `id`, `pt-BR`, `vi`, and `xx`
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests)

## Human review

The 5,041 Hindi word senses are marked `reviewed` and still require owner approval before they can ship. The remaining 3,796 missing Hindi word senses are the next localization workload.
