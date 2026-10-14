# Manual semantic review — batch 037

Date: 2026-10-14
Scope: source sense indices 900–924, inclusive (25 senses), covering pets and animals.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 definitions in concise learner language.
- Corrected the malformed `cage_s1` wording, the mismatch between `wild_s1` and its example, and overly technical animal definitions.
- Refined affected Hindi glosses/examples while preserving stable IDs and English learning terms.
- Refreshed source fingerprints for all 25 senses in five locale status files.

## Reviewed IDs

`pet_s2`, `puppy_s1`, `kitten_s1`, `feed_s1`, `feed_s2`, `tail_s1`, `fur_s1`, `paw_s1`, `bark_s1`, `bite_s1`, `cage_s1`, `vet_s1`, `wild_s1`, `elephant_s1`, `tiger_s1`, `monkey_s1`, `bird_s1`, `butterfly_s1`, `dolphin_s1`, `turtle_s1`, `rabbit_s1`, `hamster_s1`, `adopt_s1`, `shelter_s1`, `cuddle_s1`

## Verification

- English source senses reviewed: 925 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
