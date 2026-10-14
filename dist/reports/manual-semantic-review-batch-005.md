# Manual semantic review — batch 005

## Scope

I read the next 25 finance and accounting senses against their English examples. I rewrote
all 25 source definitions to make the part of speech, meaning, and level of detail precise.
I also rewrote the `statement_s2` source example because the previous sentence described a
press announcement rather than a statement used as evidence. Its five locale examples now
express the same evidence meaning.

Hindi received matching glosses and examples for all 25 senses. Vietnamese, Spanish,
Brazilian Portuguese, and Indonesian received the corrected `statement_s2` example. The
English target words remain identifiable in the learner examples, and the source hashes were
refreshed for every changed sense in all five locales.

## Reviewed senses

`transaction_s1`, `statement_s1`, `statement_s2`, `financial_s1`, `economy_s1`, `fund_s1`,
`fund_s2`, `stock_s1`, `stock_s2`, `cash_s1`, `cash_s2`, `credit_card_s1`, `fee_s1`,
`borrow_s1`, `lend_s1`, `invoice_s1`, `receipt_s1`, `tax_s1`, `audit_s1`, `accountant_s1`,
`calculate_s1`, `estimate_s1`, `estimate_s2`, `reimburse_s1`, `quarter_s1`.

## Counts before/after

- English source senses: 8,674 / 8,674.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 25.
- Source examples rewritten: 1 (`statement_s2`).
- Locale wording changes: vi 1, id 1, hi 25, es 1, pt-BR 1.
- Status hashes refreshed for all five locales on the 25 changed source senses.

## Checks

- `python -X utf-8 tools/audit_content.py`: 0 errors.
- `python -X utf-8 tools/validate_content.py`: 0 errors, 0 warnings.
- `python -X utf-8 tools/locale.py check --all`: all locales passed.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: 73 passed.
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Remaining review

The complete 8,674-sense corpus still requires additional manual batches. This report covers
only this 25-sense pass.
