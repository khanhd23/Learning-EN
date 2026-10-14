# Manual semantic review — batch 033

Date: 2026-10-14
Scope: source sense indices 800–824, inclusive (25 senses), covering weather, nature, and sports.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English definitions to align with the senses shown by their examples.
- Clarified the paired meanings of `match`, `lose`, `coach`, and `fan`.
- Rewrote 25 Hindi glosses and examples in context, preserving stable IDs and English learning terms.
- Refreshed source fingerprints for these 25 senses in all five locale status files.

## Reviewed IDs

`heatwave_s1`, `typhoon_s1`, `pour_s1`, `nature_s1`, `landscape_s1`, `earthquake_s1`, `fog_s1`, `melt_s1`, `football_s1`, `team_s1`, `match_s1`, `match_s2`, `score_s1`, `score_s2`, `win_s1`, `lose_s1`, `lose_s2`, `coach_s1`, `coach_s2`, `champion_s1`, `tournament_s1`, `referee_s1`, `stadium_s1`, `fan_s1`, `fan_s2`

## Verification

- English source senses reviewed: 825 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
