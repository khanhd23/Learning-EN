# Manual semantic review — batch 015

## Scope

I read the next 25 travel and accommodation senses against their English examples. I rewrote
eight source definitions, including the route-plan meaning of `itinerary_s1`, the train meaning
of `platform_s1`, the seat-passage meaning of `aisle_s1`, and the practical meanings of
`souvenir_s1`, `traffic_jam_s1`, `guide_s1`, `map_s1`, and `guest_s1`.

Hindi received a fresh gloss and example review for all 25 senses. The review distinguishes
`fare_s1` from `fare_s2`, `commute_s1` from `commute_s2`, and `guide_s1` from `guide_s2`, while
aligning the reservation, customs, accommodation, baggage claim, and hotel guest vocabulary.
Source hashes were refreshed for every changed source sense in all five locales.

## Reviewed senses

`reservation_s1`, `itinerary_s1`, `destination_s1`, `passport_s1`, `customs_s1`,
`round_trip_s1`, `fare_s1`, `fare_s2`, `commute_s1`, `commute_s2`, `platform_s1`, `aisle_s1`,
`souvenir_s1`, `sightseeing_s1`, `tourist_s1`, `journey_s1`, `traffic_jam_s1`, `check_in_s1`,
`abroad_s1`, `accommodation_s1`, `guide_s1`, `guide_s2`, `map_s1`, `baggage_claim_s1`,
`guest_s1`.

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
