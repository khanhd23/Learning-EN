# Manual semantic review — batch 004

## Scope

I read the next 25 finance and banking senses against their examples. I rewrote 20 source
definitions, including the incorrect `balance_s2` definition that described an accounting
ledger while its example teaches balance between work and rest, and the incorrect
`exchange_rate_s1` wording that called the rate a charge. I also separated the money meaning
of `transfer_s1` from the unrelated job or location meaning.

Hindi received matching wording for all 20 changed senses. Vietnamese and Indonesian received
the targeted corrections for balance, transfer and exchange rate; Spanish and Brazilian
Portuguese were checked and needed no wording change. Source hashes were refreshed for every
changed sense in all five locales.

## Reviewed senses

`deposit_s1`, `deposit_s2`, `withdraw_s1`, `loan_s1`, `loan_s2`, `interest_s1`, `interest_s2`,
`interest_s3`, `balance_s1`, `balance_s2`, `transfer_s1`, `transfer_s2`, `branch_s1`,
`branch_s2`, `currency_s1`, `exchange_rate_s1`, `invest_s1`, `profit_s1`, `revenue_s1`,
`budget_s1`, `expense_s1`, `afford_s1`, `debt_s1`, `mortgage_s1`, `savings_s1`.

## Counts before/after

- English source senses: 8,674 / 8,674.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 20.
- Locale wording changes: vi 2, id 3, hi 20, es 0, pt-BR 0.
- Status hashes refreshed for all five locales on the changed source senses.

## Checks

- `python -X utf-8 tools/audit_content.py`: 0 errors.
- `python -X utf-8 tools/validate_content.py`: 0 errors, 0 warnings.
- `python -X utf-8 tools/locale.py check --all`: all locales passed.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: 73 passed.
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Remaining review

The complete 8,674-sense corpus still requires additional manual batches. This report covers
only this 25-sense pass.
