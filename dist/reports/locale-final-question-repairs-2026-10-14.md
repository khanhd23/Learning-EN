# Final locale question repairs — 2026-10-14

## Scope

Filled the last six question entries that had no explanation in the English question source. The entries are E05 examples for past continuous, present perfect continuous, future continuous, past perfect continuous, future perfect, and future perfect continuous.

Spanish had placeholder text for these rows, while Indonesian was empty. Both were replaced with semantic explanations based on the corresponding English grammar point. Portuguese already contained meaningful explanations and was left unchanged. English stems, stable IDs, and taught forms remain unchanged.

## Counts

| Locale | Question entries reviewed before | Repaired | Reviewed after | Missing after | Approved added |
|---|---:|---:|---:|---:|---:|
| es | 890 / 890 | 6 | 890 / 890 | 0 | 0 |
| pt-BR | 890 / 890 | 0 | 890 / 890 | 0 | 0 |
| id | 884 / 890 | 6 | 890 / 890 | 0 | 0 |
| vi | 890 / 890 | 0 | 890 / 890 | 0 | 0 |

Review rows are recorded in `content/review/es/manual_final_question_repairs_2026-10-14.csv` and `content/review/id/manual_final_question_repairs_2026-10-14.csv`. No `approved` metadata was created.

## Validation

- `python tools/locale.py check --all` — passed for es, id, pt-BR, and vi.
- `python tools/validate_content.py` — `errors=0 warnings=0`.
- `python tools/audit_content.py` — run again with the final build checks.
