# Manual semantic review — batch 030

Date: 2026-10-14
Scope: source sense indices 725–749, inclusive (25 senses), covering personality, feelings, and food basics.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English source definitions where a shorter, clearer definition better represented the sense.
- Rewrote 25 Hindi glosses and examples in context, preserving the English learning terms and stable sense IDs.
- Refreshed the source fingerprint for these 25 senses in all five locale status files.
- No source word, sense ID, example ID, or question ID was renamed or reordered.

## Reviewed IDs

`embarrassed_s1`, `confident_s1`, `shy_s1`, `friendly_s1`, `honest_s1`, `lazy_s1`, `hard_working_s1`, `generous_s1`, `selfish_s1`, `stressed_out_s1`, `relieved_s1`, `disappointed_s1`, `grateful_s1`, `jealous_s1`, `lonely_s1`, `calm_s1`, `calm_s2`, `curious_s1`, `mood_s1`, `upset_s1`, `thrilled_s1`, `overwhelmed_s1`, `rice_s1`, `noodles_s1`, `vegetable_s1`

## Verification

- English source senses reviewed: 750 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
