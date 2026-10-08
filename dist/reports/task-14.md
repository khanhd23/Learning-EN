# Task 14: Exam engine

Implemented the section/bank engine without inventing owner exam content:

- `ExamFormat` now reads optional ordered sections, counts, item type, exercise types, qtypes, and bank id. Formats without sections retain the existing behavior.
- Added a pure `ExamBankParser` and `SectionedExamBuilder`. Item sections select the configured count; group sections select whole groups and reject a bank that cannot fill the configured count.
- Added section metadata to resumable mock tests and section score rows to the result screen. Old saved mocks remain compatible because the fields are optional.
- The content asset task copies optional `content/en/exams/` and Vietnamese exam-bank folders only when the owner supplies them.
- `validate_content.py` validates optional banks: unique ids, four options, answer bounds, declared sections, non-empty groups, and fillable section counts.
- Added a small test-only bank fixture with one item section and one shared-passage group section. It is not shipped as THPT content.

The owner’s C4 data is not present yet (`content/en/exams/` and a sectioned format entry are absent), so no THPT facts, counts, translations, or production bank were created. Once C4 arrives, it can be placed in the documented paths and validated without code changes.

No strings, content source files, authoring files, or config files were changed. No new UI text was required.

## Verification

- `python tools/validate_content.py` — PASS (errors=0; existing warnings reported by the validator)
- `python -m unittest discover -s tools/tests` — PASS
- `./gradlew testDebugUnitTest --no-daemon` — PASS (31 tests)
- `./gradlew check assembleDebug --no-daemon` — PASS
