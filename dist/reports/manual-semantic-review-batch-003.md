# Manual semantic review — batch 003

## Scope

I read the next 25 workplace and employment senses against their examples and parts of speech.
I rewrote 24 definitions. The most significant correction was `position_s2`: its old definition
described a military tactical location while its example teaches a way of sitting, so the source
now matches the example. I also clarified the employment meanings of `salary`, `wage`,
`employee`, `employer`, `personnel`, `vacancy` and `workforce`.

The Hindi records for all 24 changed senses were rewritten to match the corrected meanings and
examples. Other locales were checked and their source hashes were refreshed; no additional
Spanish, Portuguese, Indonesian or Vietnamese wording needed changing in this pass.

## Reviewed senses

`promote_s2`, `resign_s1`, `retire_s1`, `employee_s1`, `employer_s1`, `position_s1`,
`position_s2`, `position_s3`, `training_s1`, `training_s2`, `performance_s1`, `evaluate_s1`,
`probation_s1`, `full_time_s1`, `part_time_s1`, `personnel_s1`, `reference_s1`, `reference_s2`,
`vacancy_s1`, `workforce_s1`, `apply_for_s1`, `leave_s1`, `leave_s2`, `incentive_s1`,
`account_s1`.

## Counts before/after

- English source senses: 8,674 / 8,674.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 24.
- Hindi wording changes: 24.
- Status hashes refreshed for all five locales on the changed source senses.

## Checks

- `python -X utf-8 tools/audit_content.py`: 0 errors.
- `python -X utf-8 tools/validate_content.py`: 0 errors, 0 warnings.
- `python -X utf-8 tools/locale.py check --all`: all locales passed.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: 73 passed.
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Remaining review

The complete 8,674-sense corpus still requires additional manual batches. This report covers
only this 25-sense pass.
