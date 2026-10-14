# Manual semantic review — batch 021

Date: 2026-10-14
Scope: source sense indices 500–524, inclusive (25 senses), covering insurance claims and housing.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English source definitions where a shorter, clearer definition better represented the sense.
- Rewrote 25 Hindi glosses and examples in context, preserving the English learning terms and stable sense IDs.
- Refreshed the source fingerprint for these 25 senses in all five locale status files.
- No source word, sense ID, example ID, or question ID was renamed or reordered.

## Reviewed IDs

`claim_s2`, `claim_s1`, `coverage_s1`, `healthy_s1`, `well_being_s1`, `apartment_s1`, `rent_s1`, `rent_s2`, `landlord_s1`, `tenant_s1`, `lease_s1`, `real_estate_s1`, `neighborhood_s1`, `furniture_s1`, `renovate_s1`, `utilities_s1`, `move_s1`, `move_s2`, `spacious_s1`, `balcony_s1`, `garage_s1`, `location_s1`, `plumber_s1`, `leak_s1`, `residential_s1`

## Verification

- English source senses reviewed: 525 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
