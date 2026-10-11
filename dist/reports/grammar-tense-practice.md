# 12-tense grammar practice

## Scope

The Grammar tab now exposes a `12 Verb tenses` practice card above the roadmap. It starts one asynchronous session after the learner has reviewed grammar and does not add a new visible-string key: it reuses the owner-provided `tag_tense` and `drills_sub` strings.

The review covers these twelve points:

- present simple
- present continuous
- present perfect
- present perfect continuous
- past simple
- past continuous
- past perfect
- past perfect continuous
- will / be going to (the existing future point)
- future continuous
- future perfect
- future perfect continuous

Each new point has original examples, two error corrections, six cloze questions, one transformation, and one word-order exercise. The session selector keeps all twelve tense points represented and selects at least ten questions of each exercise family (E02, E04, E05 and E12). That produces a 42-question minimum session: twelve coverage questions plus ten per family, with duplicate questions removed. Retry from the result screen returns to the same 12-tense session.

## Manual content review

Reviewed the generated English questions and answer indexes for all six newly authored points. The time signals match the intended tense (`since`/`for`, `while`/`when`, `by`, and future reference points); distractors are grammatical alternatives rather than duplicate answers; E04 indexes point to the marked error; E12 correct options preserve the intended meaning; and E05 sentences have a single natural ordering. The existing `future` point is intentionally retained as the simple-future / be-going-to group, so no duplicate point was introduced.

## Verification

- `python tools/gen_content.py`: passed; generated 44 grammar points and 890 questions.
- Tense audit: 12 points, 10 questions per newly added point, with E02/E04/E05/E12 represented in the source.
- Exercise-family audit: E02=85, E04=28, E05=20, E12=10 in the generated English question bank; the 12-tense session now selects at least 10 of each family.
- `python -m unittest discover -s tools/tests`: passed, 72 tests.
- `python tools/validate_content.py`: no new grammar/question errors. Current baseline remains `errors=1 warnings=241` because the pre-existing Vietnamese UI pack is missing five `about_*` keys; the new English explanations removed the 54 missing-source errors for this change.
- `./gradlew :app:testDebugUnitTest`: passed.
- `./gradlew :app:assembleDebug`: passed.
- `git diff --check`: existing generated/working-tree line-ending changes outside this feature still report whitespace; no content source was changed manually outside the authoring entries above.

No screenshot was added because this is a content/session change and no device preview was requested in this turn.
