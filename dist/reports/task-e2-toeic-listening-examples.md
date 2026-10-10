# TOEIC-style listening examples and tips

## Scope

This batch keeps the selectable Part 2 bank at exactly 75 questions: three selectable
practice papers with 25 questions each. The twelve legacy questions in hidden sets 4 and 5
remain in the generated bank only to preserve stable IDs and are not exposed by the config.

It also adds a small original preview for Parts 3 and 4 rather than a full-length paper:

| Section | Groups | Questions | Options per question |
|---|---:|---:|---:|
| Part 1 | 30 | 30 | 4 |
| Part 2 | 87 generated (75 selectable) | 87 | 3 |
| Part 3 examples | 5 | 10 | 4 |
| Part 4 examples | 5 | 10 | 4 |

## Content and review

- All new Part 3 and Part 4 scripts, questions, distractors, English explanations, and
  Vietnamese explanations are newly authored in `tools/authoring/exam_p3_examples.json` and
  `exam_p4_examples.json`. They use `original:exam-expansion-2026` provenance.
- I manually reviewed all 20 new questions against their scripts: the keyed answer is
  supported by the audio text, each distractor is distinct, and dates, times, locations,
  quantities, and requested actions agree with the script.
- The official ETS URL is used only as a format reference. No official question, script, or
  recording was copied, and no external audio is bundled.
- Part 1–4 method/tip text is available through `listen_p1`–`listen_p4` in the generated EN/VI
  tips packs. The format cards also describe how to approach each part and give a short tip.
- For a learner locale, the translated transcript remains visible while TTS reads the preserved
  English source script. Part 3/4 TTS reads the English script and the current question before
  reading the four options; the slow playback action remains available.
- No new `strings.xml` key was needed.

## Checks

- `python tools/gen_content.py`: passed; generated Part 3 = 5 groups/10 items and Part 4 =
  5 groups/10 items.
- `python tools/validate_content.py`: `errors=0`, `warnings=244` (existing Vietnamese gloss
  warnings).
- `python tools/audit_content.py`: `entries=8674`, `gold=0`, `silver=7562`, `bronze=1112`,
  `ngsl_missing=0`, `errors=2709` (unchanged baseline audit findings).
- `python -m unittest discover -s tools/tests`: 46 tests passed.
- `./gradlew.bat check testDebugUnitTest assembleDebug`: passed; `BUILD SUCCESSFUL`.

The new Part 3/4 content is intentionally an examples bank, not a complete TOEIC paper.
