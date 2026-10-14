# Manual semantic review — batch 018

## Scope

I read the next 25 event and technology senses against their English examples. I rewrote eight
source definitions, including the event-booth meaning of `booth_s1`, a clean celebration meaning
for `celebrate_s1`, the two session meanings, and concise learner definitions for `software_s1`
and `device_s1`.

Hindi received a fresh gloss and example review for all 25 senses. The review distinguishes the
three `network` meanings, the two `session` meanings, and the two `award` meanings, while
aligning conference, exhibition, software, hardware, download, upload, password, and backup
vocabulary. Source hashes were refreshed for every changed source sense in all five locales.

## Reviewed senses

`keynote_s1`, `attendee_s1`, `booth_s1`, `catering_s1`, `anniversary_s1`, `celebrate_s1`,
`announce_s1`, `award_s1`, `award_s2`, `in_advance_s1`, `session_s1`, `session_s2`,
`venue_capacity_s1`, `software_s1`, `hardware_s1`, `download_s1`, `upload_s1`, `password_s1`,
`log_in_s1`, `device_s1`, `network_s1`, `network_s2`, `network_s3`, `data_s1`, `backup_s1`.

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
