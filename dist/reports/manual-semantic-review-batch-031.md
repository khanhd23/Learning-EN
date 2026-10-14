# Manual semantic review — batch 031

Date: 2026-10-14
Scope: source sense indices 750–774, inclusive (25 senses), covering food and cooking.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English source definitions where a shorter, clearer definition better represented the sense.
- Corrected `boil_s1`, whose old definition described liquid vaporizing instead of cooking food in boiling water.
- Rewrote 25 Hindi glosses and examples in context, preserving the English learning terms and stable sense IDs.
- Refreshed the source fingerprint for these 25 senses in all five locale status files.
- No source word, sense ID, example ID, or question ID was renamed or reordered.

## Reviewed IDs

`fruit_s1`, `cook_s1`, `cook_s2`, `boil_s1`, `fry_s1`, `grill_s1`, `bake_s1`, `recipe_s1`, `taste_s1`, `taste_s2`, `sweet_s1`, `sweet_s2`, `sour_s1`, `bitter_s1`, `salty_s1`, `fresh_s1`, `snack_s1`, `meal_s1`, `hungry_s1`, `full_s1`, `leftovers_s1`, `chopsticks_s1`, `street_food_s1`, `takeaway_s1`, `flavor_s1`

## Verification

- English source senses reviewed: 775 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
