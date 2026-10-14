# Manual semantic review — batch 017

## Scope

I read the next 25 hotel, food, and event senses against their English examples. I corrected the
corrupt `delicious_s1` source example (`caf�`) and rewrote 10 definitions, including the food
meaning of `portion_s1`, the hosting distinctions among `host_s1` and `host_s3`, and the event
meanings of `venue_s1`, `presentation_s1`, `presentation_s2`, and `exhibition_s1`.

Hindi received a fresh gloss and example review for all 25 senses. The review distinguishes the
three `host` meanings, the two `presentation` meanings, and the two hotel/event list meanings
of `register`. The corrected café example and source hashes were synchronized across all five
locales.

## Reviewed senses

`room_service_s1`, `lobby_s1`, `spicy_s1`, `delicious_s1`, `portion_s1`, `host_s1`, `host_s2`,
`host_s3`, `recommend_s1`, `conference_s1`, `seminar_s1`, `workshop_s1`, `venue_s1`,
`register_s1`, `register_s2`, `participant_s1`, `speaker_s1`, `presentation_s1`,
`presentation_s2`, `organize_s1`, `exhibition_s1`, `trade_fair_s1`, `banquet_s1`,
`invitation_s1`, `ceremony_s1`.

## Counts before/after

- English source senses: 8,837 / 8,837.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 10.
- Source examples rewritten: 1 (`delicious_s1`).
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
