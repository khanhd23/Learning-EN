# E2 — three complete original Part 2-style listening papers

## Scope

This revision replaces the five short selectable papers with three complete
Part 2-style practices of 25 questions each. The questions and responses are
newly written for this repository. They do not reproduce ETS questions, answer
keys, scripts, or audio, and the app does not claim to be an official TOEIC
test. The ETS URL in `config/exam_formats.json` is a format reference only.

## Data and manual review

The new authored source is `tools/authoring/exam_p2_batch2.json`; the existing
30 Part 2 groups remain in `tools/authoring/exam_p2_batch1.json` so their IDs
are not deleted. The generated banks are
`content/en/exams/toeic_p2.json` and
`content/i18n/vi/exams/toeic_p2.json`.

| selectable paper | question groups | questions | spoken sentences |
|---|---:|---:|---:|
| Practice 1 | 25 | 25 | 100 |
| Practice 2 | 25 | 25 | 100 |
| Practice 3 | 25 | 25 | 100 |
| **Selectable total** | **75** | **75** | **300** |

The generated bank contains 87 groups: the 75 selectable questions plus the
12 previously authored groups from the two old six-question sets. Sets 4 and 5
are retained for stable IDs but have no format card and cannot be selected by
the app.

I manually reviewed all 75 new questions and responses, plus the retained
groups: every question has exactly three distinct, grammatical responses; the
answer matches the question type; the English and Vietnamese explanations match
the answer; and no question ID or prompt is duplicated. Each selectable paper
uses the balanced A–B–C pattern repeated through question 25. The content is
original workplace, travel, service, study, and community dialogue material,
not copied or lightly paraphrased from a test or preparation book.

## App behavior

- Three selectable format cards are defined as `toeic_p2_practice_1` through
  `toeic_p2_practice_3`, each with 25 questions.
- The mock-test path selects the requested 25-question set without mixing
  papers. Practice by type stays within that selected set.
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

- `python tools/gen_content.py`: passed; generated TOEIC P2 bank contains 87
  groups, including three selectable 25-question papers.
- `python tools/validate_content.py`: passed with `errors=0` and the existing
  244 Vietnamese gloss-length warnings.
- `python tools/locale.py check --all`: passed (`vi`, errors=0).
- `python -m unittest discover -s tools/tests`: passed, 44 tests.
- `./gradlew.bat check testDebugUnitTest assembleDebug`: passed; build finished
  successfully in 1m 52s.
- Manual generated-bank QA: three selectable sets × 25, 75 selectable items,
  three responses per item, unique IDs, valid answer indexes, and the intended
  answer-position sequence all passed.
- The Gradle `auditContent` task still reports the repository's pre-existing
  word-data audit total (`errors=2709`, including catch-all-topic and example
  quality issues); this batch adds no vocabulary records and does not change
  that baseline.

## Licensing and policy review

All Part 2 questions, response sentences, and explanations are original. No
third-party audio or exam text was copied. The bank is labelled as original
TOEIC-style practice, not an official exam or score, and retains only an ETS
format-reference URL.
