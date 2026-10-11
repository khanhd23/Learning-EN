# Exam listening audit

Date: 2026-10-11

## Result

The existing TOEIC Part 3 and Part 4 banks already contain three complete sets. This change adds three complete listening-style papers for every other supported exam family that has an official listening component, plus three full 100-question TOEIC Listening papers assembled from the existing disjoint Part 1–4 sets.

All new scripts and questions are original practice content. They are not copied from official papers and the formats are labelled as practice rather than official tests.

| Family | Reading currently exposed | Listening after this change | Shape checked |
|---|---:|---:|---|
| TOEIC | 3 × 100 | 3 × 100 | 6 + 25 + 39 + 30 |
| IELTS Academic | 3 × 40 | 3 × 40 | 4 parts × 10 |
| VSTEP.3–5 | 3 × 40 | 3 × 35 | 8 + 12 + 15 |
| A2 Key | 5 × 30 reading items in the app's reading-only adaptation | 3 × 25 | 5 parts × 5 |
| B1 Preliminary | 3 × 32 | 3 × 25 | 4 parts; 7 + 6 + 6 + 6 |
| THPT | 5 × 40 | Not applicable | The supported national English format is reading/language use; no listening paper is added. |

The Cambridge and IELTS official papers use more than one listening task type. The current engine's answer component is multiple choice, so these new practice papers preserve the official part/question totals and listening progression but present original prompts as answer cards. This is intentionally stated in each format description; they must not be marketed as official papers or band/score predictors.

## Transcript protection

Listening items now carry a common `listening` tag. The active session and mock-test screen use that tag to hide `passage`/`audioPassage`; TOEIC Part 1 still shows its picture and only A/B/C/D labels. The recording is played through TTS, with replay and slower playback. The result review may show the script and evidence after submission.

TTS follows the format: TOEIC Part 2–4 reads the question/options after the recording, while
TOEIC Part 1 and the IELTS/VSTEP/Cambridge listening banks play only the recording during the
active attempt. Their printed questions/options remain available for the learner to answer and are
not exposed as an audio transcript before submission.

## Manual content checks

- Every new group has a non-empty script and explicit `audioPassage`.
- Every answer index points to exactly one option; option counts are 4 for IELTS/VSTEP/TOEIC and 3 for A2 Key/B1 Preliminary.
- IDs, scripts within each bank, and question stems within each group are unique.
- Set 1, 2 and 3 counts are checked against their format sections; TOEIC full papers reuse the existing disjoint Part 1–4 sets rather than copying questions.
- Spot-checked scripts from all four new banks: the answer value appears in the spoken script, and the displayed question asks for the same labelled detail.

## Verification

- `python -m unittest discover -s tools/tests`: **72 passed**.
- `python -m unittest tools.tests.test_exam_listening -v`: **4 passed**.
- `python tools/validate_content.py`: **1 pre-existing error**, the five missing Vietnamese `about_feature_*` UI keys; no new exam-bank errors. Existing warnings: 241.
- `./gradlew :app:testDebugUnitTest :app:assembleDebug`: **BUILD SUCCESSFUL**.

The IELTS listening source URL in `config/exam_formats.json` was normalized to the current official
IELTS format page. No official question text or recording transcript was copied.

