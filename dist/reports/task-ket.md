# Task KET — A2 Key Reading practice bank

Date: 2026-10-10

## Scope

Added five original A2 Key Reading practice papers. This is deliberately the
Reading-only scope defined in `docs/EXAM_DATA_PLAN.md`; the app does not claim to
score Cambridge Writing, Listening or Speaking. The Cambridge format page was
used for format facts only:
https://www.cambridgeenglish.org/exams-and-tests/qualifications/key/format/

Each paper has 30 reading questions:

| Part | Task shape | Questions per paper | Papers | Total |
|---|---|---:|---:|---:|
| 1 | six short real-world texts, 3-choice | 6 | 5 | 30 |
| 2 | seven matching prompts over three short texts | 7 | 5 | 35 |
| 3 | one original long text, 3-choice | 5 | 5 | 25 |
| 4 | multiple-choice cloze, 3-choice | 6 | 5 | 30 |
| 5 | open cloze, one typed word | 6 | 5 | 30 |
| **Total** |  | **30** | **5** | **150** |

All passages, options, keys and explanations are newly written. No official
Cambridge sample or past-paper wording was copied. Vietnamese explanations and
passage translations are generated from the reviewed authoring source and are
present for all 150 items; group titles/passages add 25 localized entries.

## Code

- Added the generated `ket` bank and five sectioned format entries.
- Added `ExamItem.answerText` and `Exercise.ExamText` for genuine open-cloze
  input; open-cloze items have `opts=[]` and are not converted to multiple choice.
- Added typed-answer rendering, persistence, scoring, section scoring and result
  review for mock tests and normal practice sessions.
- Updated bank validation and parser tests.

## Manual content review

- Reviewed all 25 passages and 150 question records after generation.
- Checked every multiple-choice key against the passage and every open-cloze
  answer against the sentence grammar/collocation.
- Checked distractors are plausible but contradicted by, or unsupported by, the
  passage; no item uses “all of the above”.
- Checked paper sets are disjoint, item IDs are unique, and answer positions are
  spread across all three choices (120 multiple-choice items).

## Checks

- `python tools/gen_content.py` — passed; generated `ket` with 25 groups / 150 items.
- `python tools/validate_content.py` — passed (`errors=0`, 244 pre-existing warnings).
- `python tools/audit_content.py` — completed; baseline counts unchanged: 8,674 entries,
  7,562 silver, 1,112 bronze, 2,709 existing audit findings.
- `python tools/locale.py check --all` — passed.
- `python -m unittest discover -s tools/tests` — **54 tests passed**.
- `./gradlew.bat testDebugUnitTest` — passed.
- `./gradlew.bat assembleDebug` — passed.

No push was performed.
