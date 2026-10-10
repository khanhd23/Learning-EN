# E2 — five original Part 2-style listening papers

## Scope

This batch adds five small, original TOEIC-style Part 2 question-and-response
practices. It does not reproduce ETS questions, answer keys, scripts, or audio,
and it does not claim to be an official TOEIC test. The ETS URL in
`config/exam_formats.json` is format reference only; it is not a content source.

## Data

The authored source is `tools/authoring/exam_p2_batch1.json`; the generated
banks are `content/en/exams/toeic_p2.json` and
`content/i18n/vi/exams/toeic_p2.json`. Every record has
`source: original:exam-expansion-2026`, an English question, a Vietnamese
question, three distinct responses, and English/Vietnamese explanations.

| paper | question groups | questions | spoken sentences |
|---|---:|---:|---:|
| Practice 1 | 6 | 6 | 24 |
| Practice 2 | 6 | 6 | 24 |
| Practice 3 | 6 | 6 | 24 |
| Practice 4 | 6 | 6 | 24 |
| Practice 5 | 6 | 6 | 24 |
| **Total** | **30** | **30** | **120** |

Each paper is a disjoint six-question set. Manual review covered all 30
questions, all 90 response choices, all answer positions, and all explanations.
Every question has three distinct responses, the intended response matches the
question type and explanation, and each paper uses the balanced A–B–C–A–B–C
answer-position pattern. No duplicate question IDs, prompts, or response choices
were found within a question.

## App behavior

- Five selectable format cards are defined as `toeic_p2_practice_1` through
  `toeic_p2_practice_5`.
- The mock-test path selects the requested six-question set without mixing
  papers. Practice by type also stays within the selected set.
- Part 2 items use `listen_p2` and the existing listening exercise path. Device
  TTS reads the question first and then the three responses in order. The
  response currently being spoken receives the playing state, and `Play slowly`
  replays the same sequence at a slower rate.
- The learner still chooses a response; playback does not reveal, remove, or
  reorder an answer.
- No bundled audio was added. Playback uses the existing Android TTS service,
  so voice availability remains device-dependent.
- No new UI string key was required; existing owner strings `ins_listen` and
  `play_slow` are reused.

## Validation and tests

- `python tools/gen_content.py`: passed; generated TOEIC P2 bank contains 30
  groups and 30 items.
- `python tools/validate_content.py`: passed with `errors=0` and the existing
  244 Vietnamese gloss-length warnings.
- `python tools/locale.py check --all`: passed (`vi`, errors=0).
- `python -m unittest discover -s tools/tests`: passed, 44 tests.
- `./gradlew.bat check testDebugUnitTest assembleDebug`: passed; Gradle build
  finished successfully in 3m 18s.
- Generated-bank verification: 30/30 groups match the authored source; all five
  sets contain six `listen_p2` questions with three choices each.
- The Gradle `auditContent` task still reports the repository's pre-existing
  word-data audit total (`errors=2709`, including catch-all-topic and example
  quality issues); this batch adds no vocabulary records and does not change
  that baseline.

## Licensing and review status

All Part 2 questions, response sentences, and explanations are newly written
for this repository. No third-party audio or exam text was copied. The bank is
enabled as original TOEIC-style practice, not presented as an official exam or
score.
