# Manual semantic review — batch 024

Date: 2026-10-14
Scope: source sense indices 575–599, inclusive (25 senses), covering climate and environmental vocabulary.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English source definitions where a shorter, clearer definition better represented the sense.
- Rewrote 25 Hindi glosses and examples in context, preserving the English learning terms and stable sense IDs.
- Refreshed the source fingerprint for these 25 senses in all five locale status files.
- No source word, sense ID, example ID, or question ID was renamed or reordered.

## Reviewed IDs

`waste_s2`, `climate_change_s1`, `renewable_s1`, `energy_s1`, `solar_s1`, `emission_s1`, `conserve_s1`, `sustainable_s1`, `plastic_s1`, `flood_s1`, `drought_s1`, `wildlife_s1`, `species_s1`, `endangered_s1`, `fuel_s1`, `reduce_s1`, `protect_s1`, `global_warming_s1`, `eco_friendly_s1`, `resource_s1`, `electricity_s1`, `carbon_footprint_s1`, `disposable_s1`, `forest_s1`, `organic_s1`

## Verification

- English source senses reviewed: 600 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
