# IELTS full-reading practice data

## Scope

Added three original IELTS-style full Reading practice papers. Each paper has three complete passages and 40 questions, split 13 + 13 + 14 so the existing whole-passage exam engine never cuts a passage apart. The configured practice time is 60 minutes.

These are original study papers, not official IELTS papers. They use four-option questions because that is the question type supported by the current app exam runner. They do not estimate an IELTS band score. The IELTS URL in the format config is used only as format context; no official passage or question text was copied.

## Coverage and review

| Paper | Passages | Questions | Question IDs | Passage overlap with old IELTS mini bank |
|---|---:|---:|---:|---:|
| IELTS Academic full reading practice 1 | 3 | 40 | 40 | 0 |
| IELTS Academic full reading practice 2 | 3 | 40 | 40 | 0 |
| IELTS Academic full reading practice 3 | 3 | 40 | 40 | 0 |
| **Total** | **9** | **120** | **120 unique** | **0** |

Manual content review checked each passage against its 13/13/14 questions, each marked answer against the passage, each distractor for being distinct and not the answer, and every English explanation against the Vietnamese explanation. The generated answer positions are distributed across all four options: 34 / 29 / 30 / 27. Existing five 10-question IELTS mini-tests remain unchanged.

No new UI strings were needed. The format description clearly identifies the papers as original practice and not official or band-scored material.

## Verification

- `python tools/gen_content.py` — passed; rebuilt the exam banks from authoring data.
- `python tools/validate_content.py` — `errors=0` (244 existing warnings).
- `python -m unittest discover -s tools/tests` — passed, including 67 tests.
- `./gradlew.bat check testDebugUnitTest assembleDebug --console=plain -q` — passed.

The generated files are `content/en/exams/ielts_full.json` and `content/i18n/vi/exams/ielts_full.json`; the source is `tools/authoring/exam_ielts_full.json` and the builder is `tools/build_exam_banks.py`.
