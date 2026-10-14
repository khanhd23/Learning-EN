# Indonesian question explanations — batch 002 — 2026-10-14

## Scope

Translated the next 100 source-ordered Indonesian question explanations, covering past simple, present continuous, comparisons, quantifiers, future forms, past continuous, modals, pronouns, and present perfect. Each explanation was written from its English source explanation in `content/en/questions.json`.

Question stems, options, answers, stable IDs, and taught English forms remain unchanged. The batch is stored in `content/review/id/manual_questions_002_2026-10-14.csv`.

## Counts

| Locale | Reviewed before | Imported rows | Reviewed after | Missing after | Approved added |
|---|---:|---:|---:|---:|---:|
| id questions | 163 / 890 | 100 | 263 / 890 | 627 | 0 |

Rows are marked `reviewed` by `codex-llm-semantic-id-questions-002-2026-10-14`. No `approved` metadata was created.

## Validation

- `python tools/locale.py check --all` — passed for es, id, pt-BR, and vi.
- `python tools/validate_content.py` — `errors=0 warnings=0`.
- `python tools/audit_content.py` — `errors=0`, `entries=8674`, `ngsl_missing=0`.
