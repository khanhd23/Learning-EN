# Manual semantic review — batch 036

Date: 2026-10-14
Scope: source sense indices 875–899, inclusive (25 senses), covering body vocabulary and appearance.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 definitions in concise learner language.
- Removed malformed citation text from `good_looking_s1` and corrected the technical wording of `brain_s1`, `sneeze_s1`, and `heart_s2`.
- Rechecked Hindi glosses/examples and refined the affected entries while preserving stable IDs.
- Refreshed source fingerprints for all 25 senses in five locale status files.

## Reviewed IDs

`stomach_s1`, `tooth_s1`, `finger_s1`, `hair_s1`, `tall_s1`, `slim_s1`, `curly_s1`, `good_looking_s1`, `cute_s1`, `skin_s1`, `blink_s1`, `yawn_s1`, `sneeze_s1`, `height_s1`, `weight_s1`, `brain_s1`, `heart_s1`, `heart_s2`, `ankle_s1`, `wrist_s1`, `eyebrow_s1`, `chin_s1`, `thumb_s1`, `appearance_s1`, `pet_s1`

## Verification

- English source senses reviewed: 900 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
