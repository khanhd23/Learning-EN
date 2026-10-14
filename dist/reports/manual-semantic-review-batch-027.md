# Manual semantic review — batch 027

Date: 2026-10-14
Scope: source sense indices 650–674, inclusive (25 senses), covering daily interaction and analytical vocabulary.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English source definitions where a shorter, clearer definition better represented the sense, including the two meanings of `approach`.
- Rewrote 25 Hindi glosses and examples in context, preserving the English learning terms and stable sense IDs.
- Refreshed the source fingerprint for these 25 senses in all five locale status files.
- No source word, sense ID, example ID, or question ID was renamed or reordered.

## Reviewed IDs

`traffic_s1`, `on_time_s1`, `in_a_hurry_s1`, `get_along_with_s1`, `look_after_s1`, `run_out_of_s1`, `analyze_s1`, `theory_s1`, `hypothesis_s1`, `conclusion_s1`, `significant_s1`, `method_s1`, `approach_s1`, `approach_s2`, `factor_s1`, `affect_s1`, `effect_s1`, `define_s1`, `summary_s1`, `percentage_s1`, `chart_s1`, `experiment_s1`, `argument_s1`, `assume_s1`, `indicate_s1`

## Verification

- English source senses reviewed: 675 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
