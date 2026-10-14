# Manual semantic review — batch 009

## Scope

I read the next 25 retail and customer service senses against their English examples. I
rewrote eight source definitions where the old text did not match the example: `inquiry_s1`,
the sequence and instruction meanings of `order_s3` and `order_s4`, the shop meaning of
`cashier_s1`, and the price meanings of `bargain_s1` and `guarantee_s1`.

Hindi received a fresh gloss and example review for all 25 senses. The review keeps
`order_s1` as a goods or food request, `order_s2` as the verb, `order_s3` as sequence, and
`order_s4` as an official instruction. It also corrected the retail/wholesale, customer/client,
and item/checkout distinctions. Source hashes were refreshed for every changed source sense in
all five locales.

## Reviewed senses

`in_stock_s1`, `satisfied_s1`, `replace_s1`, `inquiry_s1`, `quote_s1`, `quote_s2`, `order_s1`,
`order_s2`, `order_s3`, `order_s4`, `cashier_s1`, `salesperson_s1`, `retail_s1`,
`wholesale_s1`, `bargain_s1`, `apologize_s1`, `inconvenience_s1`, `available_s1`, `item_s1`,
`checkout_s1`, `guarantee_s1`, `handle_s1`, `handle_s2`, `courteous_s1`, `loyalty_card_s1`.

## Counts before/after

- English source senses: 8,837 / 8,837.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 8.
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
