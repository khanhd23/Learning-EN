# Manual semantic review — batch 001

## Scope

I read 25 consecutive English senses from the office and workplace section against their
part of speech and example sentence. I rewrote 17 definitions whose wording was too close to
legacy dictionary phrasing or did not explain the learner-facing sense clearly. The edits keep
all stable sense IDs and examples intact.

I then checked the corresponding Vietnamese, Spanish, Brazilian Portuguese, Indonesian and
Hindi records. Three locale groups received wording fixes: one Spanish example, two Brazilian
Portuguese entries, and 17 Hindi gloss/example pairs. The other translations matched the source
sense and were left unchanged. Source hashes were refreshed per entry after this review.

## Reviewed senses

`meeting_s1`, `schedule_s1`, `schedule_s2`, `colleague_s1`, `supervisor_s1`, `agenda_s1`,
`photocopy_s1`, `submit_s1`, `approve_s1`, `approve_s2`, `postpone_s1`, `assign_s1`,
`assign_s2`, `remind_s1`, `arrange_s1`, `attend_s2`, `efficient_s1`.

## Counts before/after

- English source senses: 8,674 / 8,674.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Locale records with wording changes: es 1, pt-BR 2, hi 17.
- Locale status hashes refreshed: vi, es, pt-BR, hi and id for each reviewed source sense.

## Checks

- `python -X utf-8 tools/audit_content.py`: 0 errors.
- `python -X utf-8 tools/validate_content.py`: 0 errors, 0 warnings.
- `python -X utf-8 tools/locale.py check --all`: all locales passed.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: 73 passed.
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Remaining review

The complete 8,674-sense corpus still requires additional manual batches. This report records
only this 25-sense pass and does not claim that the whole corpus has been human-reviewed.
