# Manual semantic review — batch 016

## Scope

I read the next 25 hotel and restaurant senses against their English examples. I corrected the
corrupt `check_out_s2` source example (`caf�`) and replaced the country-specific `dish_s1`
example with a clean, neutral noodle-dish example so non-Vietnamese locales do not inherit an
L1 reference. I rewrote seven source definitions, including `check_out_s2`, `ingredient_s1`,
`amenity_s1`, `appetizer_s1`, `reserve_s1`, and `cater_s1`.

Hindi received a fresh gloss and example review for all 25 senses. The review separates the two
`check out` meanings, the two `dish` meanings, the two `tip` meanings, and the two `serve`
meanings, while aligning hotel amenities, housekeeping, suites, complimentary items, and
restaurant vocabulary. Both corrected source examples were synchronized across all five
locales, and source hashes were refreshed.

## Reviewed senses

`vacant_s1`, `check_out_s1`, `check_out_s2`, `front_desk_s1`, `menu_s1`, `dish_s1`, `dish_s2`,
`waiter_s1`, `chef_s1`, `ingredient_s1`, `vegetarian_s1`, `tip_s1`, `tip_s2`, `serve_s1`,
`serve_s2`, `amenity_s1`, `housekeeping_s1`, `suite_s1`, `complimentary_s1`, `cuisine_s1`,
`appetizer_s1`, `dessert_s1`, `beverage_s1`, `reserve_s1`, `cater_s1`.

## Counts before/after

- English source senses: 8,837 / 8,837.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 7.
- Source examples rewritten: 2 (`check_out_s2`, `dish_s1`).
- Locale wording changes: vi 2, es 2, pt-BR 2, id 2, hi 25.
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
