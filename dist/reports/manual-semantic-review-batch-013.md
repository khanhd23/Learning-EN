# Manual semantic review — batch 013

## Scope

I read the next 25 trade and shopping senses against their English examples. I rewrote 13
source definitions, including the verb meaning of `delay_s2`, the practical meanings of
`efficiency_s1` and `specification_s1`, the country-to-country meaning of `trade_s2`, and the
corrected catalog, bulk, payment, sale, shopping mall, and brand-new definitions.

Hindi received a fresh gloss and example review for all 25 senses. The review separates
`trade_s1` from `trade_s2`, `sale_s1` from `sale_s2`, and `merchandise` from `goods`, while
making `vendor`, `catalog`, `brochure`, and `shopping_mall` understandable in context. Source
hashes were refreshed for every changed source sense in all five locales.

## Reviewed senses

`delay_s2`, `efficiency_s1`, `specification_s1`, `price_s1`, `cheap_s1`, `expensive_s1`,
`import_s1`, `export_s1`, `trade_s1`, `trade_s2`, `goods_s1`, `merchandise_s1`, `vendor_s1`,
`catalog_s1`, `bulk_s1`, `affordable_s1`, `payment_s1`, `install_s1`, `compare_s1`,
`brochure_s1`, `sale_s1`, `sale_s2`, `customer_service_s1`, `shopping_mall_s1`,
`brand_new_s1`.

## Counts before/after

- English source senses: 8,837 / 8,837.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 13.
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
