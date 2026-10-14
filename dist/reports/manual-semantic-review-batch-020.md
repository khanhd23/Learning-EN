# Manual semantic review — batch 020

## Scope

I read the next 25 health senses against their English examples. I rewrote 15 source
definitions, correcting the patient, symptom, prescription, pharmacy, medicine, insurance,
recovery, injury, surgery, nurse, diet, exercise, stress, and cough meanings. The old
`stress_s2` definition also contained an unrelated pronunciation meaning and was narrowed to
the example's emphasis meaning.

Hindi received a fresh gloss and example review for all 25 senses. The review distinguishes the
two `patient` meanings, the two `exercise` meanings, and the two `stress` meanings, while
aligning treatment, recovery, injury, fever, emergency, check-up, allergy, cure, and vaccine
vocabulary. Source hashes were refreshed for every changed source sense in all five locales.

## Reviewed senses

`patient_s2`, `symptom_s1`, `prescription_s1`, `pharmacy_s1`, `medicine_s1`, `insurance_s1`,
`treatment_s1`, `recover_s1`, `injury_s1`, `headache_s1`, `fever_s1`, `clinic_s1`,
`surgery_s1`, `emergency_s1`, `nurse_s1`, `check_up_s1`, `allergic_s1`, `diet_s1`,
`exercise_s1`, `exercise_s2`, `stress_s1`, `stress_s2`, `cure_s1`, `cough_s1`, `vaccine_s1`.

## Counts before/after

- English source senses: 8,837 / 8,837.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 15.
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
