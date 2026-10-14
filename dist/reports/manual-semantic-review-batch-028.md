# Manual semantic review — batch 028

Date: 2026-10-14
Scope: source sense indices 675–699, inclusive (25 senses), covering comparison, reasoning, and family relationships.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English source definitions where a shorter, clearer definition better represented the sense, including the two meanings of `relative`.
- Corrected the malformed source definition for `contrast_s1`.
- Rewrote 25 Hindi glosses and examples in context, preserving the English learning terms and stable sense IDs.
- Refreshed the source fingerprint for these 25 senses in all five locale status files.
- No source word, sense ID, example ID, or question ID was renamed or reordered.

## Reviewed IDs

`contrast_s1`, `previous_s1`, `approximately_s1`, `consequence_s1`, `crucial_s1`, `demonstrate_s1`, `interpret_s1`, `relevant_s1`, `whereas_s1`, `parents_s1`, `sibling_s1`, `cousin_s1`, `niece_s1`, `nephew_s1`, `grandparents_s1`, `husband_s1`, `wife_s1`, `relative_s1`, `relative_s2`, `raise_s1`, `raise_s2`, `bring_up_s1`, `get_married_s1`, `divorce_s1`, `engaged_s1`

## Verification

- English source senses reviewed: 700 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
