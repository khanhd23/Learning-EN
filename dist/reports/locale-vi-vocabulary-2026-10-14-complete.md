# Vietnamese vocabulary completion — 2026-10-14

## Result

The Vietnamese word-sense pack now covers the full English source inventory.

- Before this continuation: 7,809 approved / 1,028 draft / 8,837 total.
- After this continuation: 7,809 approved / 1,028 reviewed / 0 draft / 8,837 total.
- Reviewed in this continuation: 1,028 senses from the English source, batches 001–018.
- Locale conversion coverage: 100% reviewed or approved.

## Method

Each remaining draft gloss and example was semantically reviewed against its matching
`content/en/` definition and example. Ambiguous, archaic, idiomatic, grammatical, and
technical senses were rewritten in Vietnamese according to their actual sense. The locale
import recorded each entry as reviewed with reviewer `codex-llm-semantic-vi-2026-10-14`.

The 1,028 entries remain `reviewed` rather than `approved`; approval is a separate owner
review step in the locale workflow.

## Validation

- `python -X utf-8 tools/validate_content.py`: 0 errors, 13 existing warnings for Hindi
  glosses identical to English source definitions.
- `python -X utf-8 tools/audit_content.py`: 604 source-content errors remain in the current
  English audit output; these are English source gloss length/equivalent/capitalization issues,
  not locale encoding failures.
- `python -X utf-8 tools/locale.py check --all`: all locales passed.
- Unit tests: 73 passed. Gradle `check assembleDebug`: passed.
