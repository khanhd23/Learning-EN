# Manual semantic review — batch 012

## Scope

I read the next 25 production and logistics senses against their English examples. I rewrote
eight source definitions, including the amount meaning of `output_s1`, the transport meaning of
`freight_s1`, the service-bundle meaning of `package_s2`, and the practical meanings of
`safety_s1`, `distribute_s1`, and `delay_s1`.

Hindi received a fresh gloss and example review for all 25 senses. The review separates
`package_s1` from `package_s2`, `track_s1` from `track_s2`, and `load_s1` from `load_s2`, while
making the equipment, container, freight, and warehouse vocabulary usable for learners. Source
hashes were refreshed for every changed source sense in all five locales.

## Reviewed senses

`equipment_s1`, `machinery_s1`, `quality_s1`, `defective_s1`, `output_s1`, `capacity_s1`,
`freight_s1`, `container_s1`, `deliver_on_time_s1`, `track_s1`, `track_s2`, `package_s1`,
`package_s2`, `load_s1`, `load_s2`, `raw_material_s1`, `process_s1`, `process_s2`,
`operate_s1`, `maintenance_s1`, `safety_s1`, `distribute_s1`, `shortage_s1`, `automate_s1`,
`delay_s1`.

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
