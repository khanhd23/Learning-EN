# Indonesian question explanations — batch 001 — 2026-10-14

## Scope

Translated the first source-ordered batch of Indonesian standalone question explanations, covering confusables, collocations, and early grammar questions. The explanation text was authored from each English explanation in `content/en/questions.json`; question stems, options, answers, IDs, and taught English forms remain unchanged.

The batch file contains 79 rows. Some rows were already reviewed in the Level 1 pilot and were refreshed with the same semantic standard. It added 51 newly reviewed question entries.

During the same review, 23 Indonesian entries with UTF-8 symbol corruption were repaired from the English source: five question explanations and 18 UI strings. This restored arrows, IPA symbols, ellipses, en dashes, middle dots, and quoted placeholders.

## Counts

| Locale | Question entries reviewed before | Newly reviewed | Reviewed after | Missing after | Approved added |
|---|---:|---:|---:|---:|---:|
| id | 112 / 890 | 51 | 163 / 890 | 727 | 0 |

Rows are recorded in `content/review/id/manual_questions_001_2026-10-14.csv` and `content/review/id/manual_encoding_repairs_2026-10-14.csv`. They are marked `reviewed` by the semantic reviewers used for this batch. No `approved` metadata was created.

## Validation

- `python tools/locale.py check --all` — passed for es, id, pt-BR, and vi.
- `python tools/validate_content.py` — `errors=0 warnings=0`.
- Indonesian question and UI packs no longer contain the identified replacement-character corruption; remaining question marks are intentional sentence punctuation.
