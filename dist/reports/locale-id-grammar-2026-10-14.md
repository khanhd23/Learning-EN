# Indonesian grammar completion — 2026-10-14

## Scope

Completed the 36 Indonesian grammar entries that were still missing after the Level 1 pilot. The source for every translation is the corresponding entry in `content/en/grammar.json`; no learner locale was used as a translation source.

The translation was authored semantically for Indonesian learners. English forms, formulas, signals, taught examples, and stable grammar IDs remain source-controlled. The Indonesian fields cover the title, use case, explanation, translated examples, and Indonesian-specific error explanations.

## Counts

| Locale | Grammar reviewed before | Grammar reviewed after | Missing after | Approved added |
|---|---:|---:|---:|---:|
| id | 8 / 44 | 44 / 44 | 0 | 0 |

The 36 imported rows are recorded in `content/review/id/manual_grammar_full_2026-10-14.csv` and marked `reviewed` by `codex-llm-semantic-id-grammar-2026-10-14`. No `approved` metadata was created.

## Validation

- `python tools/locale.py check --all` — passed for es, id, pt-BR, and vi.
- `python tools/validate_content.py` — `errors=0 warnings=0`.
- `python tools/audit_content.py` — `errors=0`, `entries=8674`, `ngsl_missing=0`.

Indonesian still has 778 missing question explanations and 40 missing passage explanations. Those remain explicitly missing until they receive the same semantic review.
