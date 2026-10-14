# Manual semantic review — batch 022

Date: 2026-10-14
Scope: source sense indices 525–549, inclusive (25 senses), covering housing and education.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English source definitions where a shorter, clearer definition better represented the sense.
- Rewrote 25 Hindi glosses and examples in context, preserving the English learning terms and stable sense IDs.
- Refreshed the source fingerprint for these 25 senses in all five locale status files.
- No source word, sense ID, example ID, or question ID was renamed or reordered.

## Reviewed IDs

`roommate_s1`, `downtown_s1`, `downtown_s2`, `suburb_s1`, `own_s1`, `own_s2`, `construction_s1`, `brick_s1`, `ceiling_s1`, `household_s1`, `mortgage_rate_s1`, `student_s1`, `lesson_s1`, `homework_s1`, `exam_s1`, `degree_s1`, `degree_s2`, `graduate_s1`, `graduate_s2`, `scholarship_s1`, `tuition_s1`, `certificate_s1`, `course_s1`, `course_s2`, `lecture_s1`

## Verification

- English source senses reviewed: 550 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
