# Indonesian question explanations — batch 008 — 2026-10-14

## Scope

Completed the remaining source-backed Indonesian question explanations. The batch covers applied grammar and workplace language, pronunciation of `-ed`, `-s`, and `-es`, vowel sounds, word stress, synonyms, and antonyms.

The explanations were authored from the English source in `content/en/questions.json`. IPA symbols, English forms being taught, question stems, options, answers, and stable IDs remain unchanged.

## Counts

| Locale | Reviewed before | Imported rows | Reviewed after | Source gaps remaining | Approved added |
|---|---:|---:|---:|---:|---:|
| id questions | 763 / 890 | 121 | 884 / 890 | 6 | 0 |

The imported rows are recorded in `content/review/id/manual_questions_008_2026-10-14.csv` and marked `reviewed` by `codex-llm-semantic-id-questions-008-2026-10-14`. No `approved` metadata was created.

The six remaining `missing` IDs are `q8c128ac18`, `q8f528a56b`, `q09323df7b`, `q35f64661e`, `qa266907c5`, and `q2c32606b6`. They are E05 word-order questions whose English source entries have no `expl` field. They remain empty rather than receiving invented explanations; the English source needs authoring before any locale can translate them.

## Validation

- `python tools/locale.py check --all` — passed for es, id, pt-BR, and vi.
- `python tools/validate_content.py` — `errors=0 warnings=0`.
- `python tools/audit_content.py` — `errors=0`, `entries=8674`, `ngsl_missing=0`.
