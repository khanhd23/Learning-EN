# Manual semantic review — batch 014

## Scope

I read the next 25 shopping and travel senses against their English examples. I rewrote four
source definitions, including the health meaning of `fit_s2`, a clearer distinction between
`flight_s1` and `flight_s2`, and the aircraft/ship meaning of `boarding_pass_s1`.

Hindi received a fresh gloss and example review for all 25 senses. The review separates the
three `fit` meanings, the two `suit` meanings, the two `deal` meanings, and the two `book`
meanings, while aligning the travel vocabulary for passengers, luggage, departure, arrival,
delays, and cancellations. Source hashes were refreshed for every changed source sense in all
five locales.

## Reviewed senses

`second_hand_s1`, `try_on_s1`, `size_s1`, `fit_s1`, `fit_s2`, `fit_s3`, `suit_s1`, `suit_s2`,
`deal_s1`, `deal_s2`, `wholesaler_s1`, `e_commerce_s1`, `free_of_charge_s1`, `reasonable_s1`,
`flight_s1`, `flight_s2`, `passenger_s1`, `luggage_s1`, `boarding_pass_s1`, `departure_s1`,
`arrival_s1`, `delayed_s1`, `cancel_s1`, `book_s2`, `book_s1`.

## Counts before/after

- English source senses: 8,837 / 8,837.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 4.
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
