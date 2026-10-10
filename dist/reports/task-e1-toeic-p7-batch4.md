# Task E1 — three non-repeating TOEIC-style Part 7 practices

## Result

Added the fourth original authoring batch and split the complete Part 7 bank
into three fixed practice sets. Each set contains 18 whole passage groups and
54 questions. The app exposes the sets as `Practice 1`, `Practice 2`, and
`Practice 3`; the section builder filters by the set before selecting groups.

| Set | Passage groups | Questions | Group range | Shared question IDs |
|---|---:|---:|---|---:|
| 1 | 18 | 54 | 001–018 | 0 |
| 2 | 18 | 54 | 019–036 | 0 |
| 3 | 18 | 54 | 037–054 | 0 |

The complete bank now has 54 passage groups and 162 unique questions. Groups
remain intact, so a practice paper never splits a multi-document passage.

## Content and manual review

- Added 34 groups / 102 questions in `tools/authoring/exam_p7_batch4.json`.
- Every new passage, question, option, answer, English explanation, and
  Vietnamese explanation was written as original material for this project.
- Manually checked all 102 new answer keys against their passages, including
  dates, times, quantities, locations, conditions, and requested actions.
- Checked distractors for direct contradiction or unsupported information and
  checked each Vietnamese explanation against the keyed answer.
- Automated checks found 162 distinct item IDs, no duplicate stems in the
  expanded bank, and zero intersections between the three sets.
- The ETS URL in the format config is used only as a public format reference;
  this bank is labelled TOEIC-style practice and is not an official TOEIC
  test.

## Verification

- `python tools/build_toeic_bank.py` — passed; P5 306, P6 40 passages / 160
  items, P7 54 passages / 162 items.
- `python tools/validate_content.py` — passed with `errors=0`; 244 existing
  Vietnamese gloss-length warnings remain.
- `python -m unittest tools.tests.test_exam_p7 -v` — 4 tests passed.
- `python -m unittest discover -s tools/tests` — 36 tests passed.
- `./gradlew.bat testDebugUnitTest assembleDebug` — `BUILD SUCCESSFUL` in 42s.
- `git -c core.whitespace=cr-at-eol diff --check` — passed (the repository's
  generated JSON files use CRLF line endings).

## Remaining limitation

The three no-repeat guarantees apply to the three labelled fixed practice
sets. Repeating the same labelled set later is expected to reuse that set's
questions; it is not presented as a new no-repeat paper.
