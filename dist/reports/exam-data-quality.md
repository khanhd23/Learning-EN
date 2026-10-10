# Exam-bank quality review

Date: 2026-10-10

This review adds a repeatable quality gate so future exam batches cannot silently introduce
duplicate IDs, duplicate passages, repeated stems inside one passage, invalid answer indexes, or
duplicate options. It also checks that the three full TOEIC Reading papers remain exactly 100
questions and disjoint.

## Current reading coverage

| Bank | Available original reading practice |
|---|---:|
| TOEIC Reading | 3 × 100 questions (30 P5 + 16 P6 + 54 P7) |
| THPT | 5 × 40 questions |
| VSTEP Reading | 3 × 40 questions |
| A2 Key Reading | 5 × 30 questions in the current reading-only bank |
| B1 Preliminary Reading | 3 × 32 questions |
| IELTS short reading | 5 × 10 questions |

The IELTS bank remains explicitly labelled short-reading practice. It is not presented as a full
IELTS Reading paper because the current bank and engine do not yet cover the complete task-type
mix. Listening, Writing and Speaking remain practice/format information rather than official
scored sections. No filler or duplicate content was added to pretend those sections are complete.

## Manual review results

- All English exam banks: unique group and item IDs.
- All groups: non-empty, unique passages within their bank.
- All items: non-empty stems and explanations; valid answer indexes; 3 or 4 choices as authored;
  no duplicate choices in an item.
- All group passages: no repeated stem within the same passage.
- TOEIC Reading Practices 1–3: exact 30/16/54 section counts, 100 items each, zero cross-paper
  item-ID overlap.
- The repeated P6 structural wording (“Choose the best answer for blank N”) is only reused across
  different passages; each passage and blank ID remains unique.

## Test result

- `python -m unittest discover -s tools/tests -v`: 64 tests passed.
- Existing content tests, locale-cycle tests and exam-bank tests all passed.
- `python tools/validate_content.py`: `errors=0`, `warnings=244`.

This gate is intentionally data-only and does not create another copy of any exam bank.
