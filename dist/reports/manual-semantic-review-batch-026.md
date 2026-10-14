# Manual semantic review — batch 026

Date: 2026-10-14
Scope: source sense indices 625–649, inclusive (25 senses), covering media and daily routines.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English source definitions where a shorter, clearer definition better represented the sense; refined `wake_up_s1` to `wake from sleep` after the audit correctly identified a verbatim WordNet definition.
- Rewrote 25 Hindi glosses and examples in context, preserving the English learning terms and stable sense IDs.
- Refreshed the source fingerprint for these 25 senses in all five locale status files.
- No source word, sense ID, example ID, or question ID was renamed or reordered.

## Reviewed IDs

`podcast_s1`, `announcement_s1`, `censor_s1`, `trending_s1`, `live_s2`, `live_s1`, `wake_up_s1`, `breakfast_s1`, `grocery_s1`, `laundry_s1`, `chore_s1`, `habit_s1`, `routine_s1`, `neighbor_s1`, `weekend_s1`, `hobby_s1`, `relax_s1`, `busy_s1`, `tired_s1`, `errand_s1`, `hang_out_s1`, `take_a_nap_s1`, `dishes_s1`, `fridge_s1`, `alarm_s1`

## Verification

- English source senses reviewed: 650 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`) after refining `wake_up_s1`.
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
