# Manual semantic review — batch 002

## Scope

I read the next 25 English senses in the workplace and recruitment sequence, checking each
definition against its part of speech and example. I rewrote 16 definitions, including removal
of a stray source citation from `temporary_s1`, separation of the telephone meaning of
`extension_s1` from an unrelated time-extension meaning, and clearer learner wording for pay,
recruitment, interviewing and promotion.

I checked all five locale records for every reviewed sense. Hindi received 25 gloss/example
repairs, while Vietnamese, Spanish and Indonesian each received the targeted correction needed
for the narrowed `extension_s1` meaning; Brazilian Portuguese required no wording change.
All source hashes were refreshed per entry after review.

## Reviewed senses

`urgent_s1`, `temporary_s1`, `productive_s1`, `stationery_s1`, `extension_s1`, `delegate_s1`,
`workload_s1`, `in_charge_of_s1`, `on_behalf_of_s1`, `headquarters_s1`, `reception_s1`,
`applicant_s1`, `candidate_s1`, `resume_s1`, `interview_s1`, `interview_s2`, `hire_s1`,
`recruit_s1`, `qualified_s1`, `experience_s1`, `salary_s1`, `wage_s1`, `benefit_s1`,
`benefit_s2`, `promote_s1`.

## Counts before/after

- English source senses: 8,674 / 8,674.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Locale wording changes: vi 1, es 1, id 2, hi 25, pt-BR 0.
- Status hashes refreshed for all five locales on the reviewed source senses.

## Checks

- `python -X utf-8 tools/audit_content.py`: 0 errors.
- `python -X utf-8 tools/validate_content.py`: 0 errors, 0 warnings.
- `python -X utf-8 tools/locale.py check --all`: all locales passed.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: 73 passed.
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Remaining review

The complete 8,674-sense corpus still requires additional manual batches. This report covers
only this 25-sense pass.
