# Task 6.2: Language sync and English-source checks

## Changes

- `App.onCreate` now reads the AppCompat per-app language before applying the stored locale. A selectable system locale updates the stored content locale; an unavailable system locale leaves the stored choice unchanged.
- Added pure `AppLocale.resolve` coverage for selectable, unavailable, and empty system locale tags.
- `validate_content.py` now treats missing English grammar sources (`title`, `when`, `body`, and mistake `why`), missing question/passage explanations, missing topic names, and Vietnamese characters in those English-source fields as errors.
- Extended locale draft tests to verify grammar source fields and mistake explanations, question explanations, passage-blank explanations, and topic names are drafted from English only. Formula, signals, wrong/right sentences, question stems, and options are not sent to the provider.

## Counts and verification

- English source pack: 38 grammar points, 579 questions, 32 passages, and 50 topics checked by the new validator rules.
- `python tools/gen_content.py`: pass before implementation; no generated data changes remain.
- `python tools/validate_content.py`: pass, 0 errors, 337 warnings.
- `python tools/locale.py check --all`: pass for `vi`.
- `python -m unittest discover -s tools/tests`: 14 tests passed.
- `./gradlew check assembleDebug`: pass.

## Scope review

- No changes to `tools/authoring/*`, `content/**`, or `res/values*/strings.xml`.
- The Gradle audit still reports pre-existing content-quality errors; this task did not modify content or the audit rules.
