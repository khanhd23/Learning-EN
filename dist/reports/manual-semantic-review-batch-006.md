# Manual semantic review — batch 006

## Scope

I read the next 25 finance and accounting senses against their English examples. I corrected
17 source definitions where the wording was too broad, grammatically incomplete, or mismatched
with the example. The most serious correction was `figure_s2`: its old definition described a
human body while its example described an important person. I also replaced the old
`figure_s2` example mentioning Vietnamese art with a neutral local art example, then translated
that corrected meaning into every locale.

Hindi received a fresh gloss and example review for all 25 senses, including clearer
distinctions for `record_s1`/`record_s2`, `total_s1`/`total_s2`, `increase_s1`/`increase_s2`,
and `decrease_s1`/`decrease_s2`. Source hashes were refreshed for every changed source sense in
all five locales.

## Reviewed senses

`annual_s1`, `deduct_s1`, `figure_s1`, `figure_s2`, `figure_s3`, `spreadsheet_s1`, `asset_s1`,
`payroll_s1`, `record_s1`, `record_s2`, `outstanding_s1`, `overdue_s1`, `refund_s1`,
`charge_s1`, `charge_s2`, `charge_s3`, `total_s1`, `total_s2`, `increase_s1`, `increase_s2`,
`decrease_s1`, `decrease_s2`, `loss_s1`, `cost_s1`, `cost_s2`.

## Counts before/after

- English source senses: 8,837 / 8,837.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 17.
- Source examples rewritten: 1 (`figure_s2`).
- Locale wording changes: vi 1, id 1, hi 25, es 1, pt-BR 1.
- Status hashes refreshed for all five locales on the changed source senses.

## Checks

- `python -X utf-8 tools/audit_content.py`: 0 errors.
- `python -X utf-8 tools/validate_content.py`: 0 errors, 0 warnings.
- `python -X utf-8 tools/locale.py check --all`: all locales passed.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: 73 passed.
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Remaining review

The complete 8,837-sense corpus still requires additional manual batches. This report covers
only this 25-sense pass.
