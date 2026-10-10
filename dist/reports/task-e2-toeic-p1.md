# E2 — five original Part 1-style listening papers

## Scope

This batch adds five small, original TOEIC-style Part 1 listening practices. It
does not reproduce ETS questions, photographs, scripts, answer keys, or audio,
and it does not claim to be an official TOEIC test. The ETS URL in
`config/exam_formats.json` is format reference only; it is not a content source.

## Data

The authored source is `tools/authoring/exam_p1_batch1.json`; the generated
banks are `content/en/exams/toeic_p1.json` and
`content/i18n/vi/exams/toeic_p1.json`. Every record has
`source: original:exam-expansion-2026`, an English scene prompt, a Vietnamese
scene prompt, four distinct answer sentences, an English explanation, and a
Vietnamese explanation.

| paper | scene groups | questions | spoken answer sentences |
|---|---:|---:|---:|
| Practice 1 | 6 | 6 | 24 |
| Practice 2 | 6 | 6 | 24 |
| Practice 3 | 6 | 6 | 24 |
| Practice 4 | 6 | 6 | 24 |
| Practice 5 | 6 | 6 | 24 |
| **Total** | **30** | **30** | **120** |

The five sets are disjoint. Manual QA checked every scene, answer, distractor,
English explanation, and Vietnamese explanation. Each paper uses the balanced
answer-position pattern A–B–C–D–A–B; there are no duplicate scene prompts or
duplicate options inside a question.

## App behavior

- Five selectable format cards are defined as `toeic_p1_practice_1` through
  `toeic_p1_practice_5`.
- The mock-test path selects the requested six-group set without mixing papers.
- Practice by type also selects only the current paper's six questions.
- Part 1 items are tagged `listen_p1`. The device TTS reads the four answer
  sentences in order when the question opens, and the currently spoken option
  receives a blue playing state. `Play slowly` replays the same four sentences
  at the slower rate. The learner still chooses an option; audio does not
  reveal or remove the answer.
- No bundled audio or image asset was added. Playback uses the existing system
  Android TTS service, so voice availability remains device-dependent.
- No new UI string key was required; existing `ins_listen` and `play_slow`
  owner strings are reused.

## Validation and tests

- `python tools/gen_content.py`: passed; generated TOEIC P1 bank contains 30
  groups and 30 items.
- `python tools/validate_content.py`: passed with `errors=0` and the existing
  244 Vietnamese gloss-length warnings.
- `python tools/locale.py check --all`: passed (`vi`, errors=0).
- `python -m unittest discover -s tools/tests`: passed, 40 tests.
- `./gradlew.bat check testDebugUnitTest assembleDebug`: passed.
- `git diff --check` and JSON parsing checks: passed.

The Gradle `auditContent` task still reports the repository's pre-existing
word-data audit total (`errors=2709`, including catch-all-topic and example
quality issues); this batch adds no vocabulary records and does not change that
baseline.

## Licensing and review status

All Part 1 scenes, answer sentences, and explanations are newly written for
this repository. No third-party audio, image, or exam text was copied. The bank
is enabled as original TOEIC-style practice, not presented as an official
exam or score.
