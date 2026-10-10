# TOEIC Reading Parts 5–7 guidance

## Changes

- Part 5 now explains the workflow for checking structure, word form, tense,
  prepositions, meaning, and collocation, with a short elimination tip.
- Part 6 now explains paragraph flow, the sentence before/after the blank,
  reference words, grammar, and collocation.
- Part 7 now explains previewing document metadata and questions, locating
  details, recognizing paraphrases, and requiring textual evidence for inference.
- Part 7 practice is available by passage type (`p7_single`, `p7_double`,
  `p7_triple`) and each type has its own method tip.
- English and Vietnamese generated tip packs contain `p5_method`, `p6_method`,
  `p7_method`, `p7_single`, `p7_double`, and `p7_triple`.
- No new UI string key or official exam content was added.

## Checks

- `python tools/gen_content.py`: passed; existing TOEIC counts unchanged
  (P5 306, P6 40 passages/160 items, P7 54 passages/162 items).
- `python tools/validate_content.py`: `errors=0`, `warnings=244`.
- `python tools/audit_content.py`: `entries=8674`, `silver=7562`, `bronze=1112`,
  `errors=2709` (same baseline findings).
- `python tools/locale.py check --all`: passed for `vi`.
- `python -m unittest discover -s tools/tests`: 47 tests passed.
- `./gradlew.bat check testDebugUnitTest assembleDebug`: passed; `BUILD SUCCESSFUL`.
