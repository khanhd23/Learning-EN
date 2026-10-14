# Indonesian passage explanations — 2026-10-14

## Scope

Completed the 40 Indonesian passage entries that were missing explanations. Each passage now has four Indonesian explanations, written from the English explanations in `content/en/questions.json`. English answers, blanks, and stable passage IDs remain unchanged.

## Counts

| Locale | Passage entries | Complete before | Complete after | Approved added |
|---|---:|---:|---:|---:|
| id | 40 | 0 | 40 | 0 |

The imported rows are recorded in `content/review/id/manual_passages_full_2026-10-14.csv` and marked `reviewed` by `codex-llm-semantic-id-passages-2026-10-14`. No `approved` metadata was created.

## Validation

- `python tools/locale.py check --all` — passed for es, id, pt-BR, and vi.
- `python tools/validate_content.py` — `errors=0 warnings=0`.
- `python tools/audit_content.py` — `errors=0`, `entries=8674`, `ngsl_missing=0`.

Indonesian still has 778 missing explanations for the standalone question bank. Those remain missing until each explanation receives semantic review.
