# Manual semantic review — batch 029

Date: 2026-10-14
Scope: source sense indices 700–724, inclusive (25 senses), covering relationships and feelings.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English source definitions where a shorter, clearer definition better represented the sense, including the two meanings of `close` and `trust`.
- Corrected the malformed source definition for `sad_s1`.
- Rewrote 25 Hindi glosses and examples in context, preserving the English learning terms and stable sense IDs.
- Refreshed the source fingerprint for these 25 senses in all five locale status files.
- No source word, sense ID, example ID, or question ID was renamed or reordered.

## Reviewed IDs

`relationship_s1`, `close_s1`, `close_s2`, `trust_s1`, `trust_s2`, `support_s1`, `support_s2`, `argue_s1`, `generation_s1`, `only_child_s1`, `twin_s1`, `take_after_s1`, `look_up_to_s1`, `crush_s1`, `date_s1`, `date_s2`, `best_friend_s1`, `happy_s1`, `sad_s1`, `angry_s1`, `excited_s1`, `bored_s1`, `nervous_s1`, `worried_s1`, `proud_s1`

## Verification

- English source senses reviewed: 725 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
