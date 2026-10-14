# Manual semantic review — batch 010

## Scope

I read the next 25 contract and legal senses against their English examples. I rewrote seven
source definitions, including the receipt meaning, the neutral idea-sharing meaning of
`exchange_s2`, the agreement meaning of `terms_s1`, and the legal-use meanings of `renew_s1`,
`regulation_s1`, `lawyer_s1`, and `valid_s1`.

Hindi received a fresh gloss and example review for all 25 senses. The review keeps the three
`party` meanings distinct, separates `contract_s1` from `contract_s2`, and aligns `permit_s1`
and `permit_s2` as noun and verb. Source hashes were refreshed for every changed source sense
in all five locales.

## Reviewed senses

`receipt_of_s1`, `exchange_s1`, `exchange_s2`, `representative_s1`, `contract_s1`,
`contract_s2`, `agreement_s1`, `terms_s1`, `sign_s1`, `sign_s2`, `negotiate_s1`, `expire_s1`,
`renew_s1`, `comply_with_s1`, `regulation_s1`, `lawyer_s1`, `legal_s1`, `clause_s1`,
`valid_s1`, `permit_s1`, `permit_s2`, `policy_s1`, `party_s1`, `party_s2`, `party_s3`.

## Counts before/after

- English source senses: 8,837 / 8,837.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 7.
- Locale wording changes: hi 25.
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
