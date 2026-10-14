# Manual semantic review — batch 019

## Scope

I read the next 25 technology and health-entry senses against their English examples. I
rewrote eight source definitions, correcting the computer meaning of `virus_s1`, the website
meaning of `website_s1`, the technical meanings of `technician_s1` and `device_s1`, the AI
definition, and the compatibility meaning of `compatible_s1`.

Hindi received a fresh gloss and example review for all 25 senses. The review distinguishes the
two `update` meanings, the two `virus` meanings, and the two `access` meanings, while aligning
screen, browser, attachment, artificial intelligence, network access, troubleshooting, and
the first doctor/patient vocabulary. Source hashes were refreshed for every changed source
sense in all five locales.

## Reviewed senses

`update_s1`, `update_s2`, `security_s1`, `virus_s1`, `virus_s2`, `screen_s1`, `screen_s2`,
`keyboard_s1`, `technician_s1`, `malfunction_s1`, `user_s1`, `website_s1`, `browser_s1`,
`attachment_s1`, `artificial_intelligence_s1`, `innovation_s1`, `digital_s1`, `access_s1`,
`access_s2`, `compatible_s1`, `troubleshoot_s1`, `wireless_s1`, `doctor_s1`,
`appointment_s1`, `patient_s1`.

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
