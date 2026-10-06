# Task 10: Dictation and minimal-pair listening

## Implemented

- Added parsing for the owner-delivered `content/en/sound.json` asset without changing the content data.
- Added sound exercise models and factories:
  - E18 dictation: text-to-speech playback, replay, sentence entry, per-word grading, and correction output.
  - E19 minimal pairs: text-to-speech playback and two-choice identification.
- Added sentence grading with case-insensitive matching, NFC normalization, punctuation tolerance, wrong-word positions, and the existing one-typo `ALMOST` behavior for long words.
- Added sound-session construction from the delivered dictation and minimal-pair data.
- Reused existing strings (`type_here`, `ins_listen`, and `listen`); no new string keys are required.

## Delivered data read

| Dataset | Count |
| --- | ---: |
| Pronunciation focuses | 20 |
| Minimal pairs | 42 |
| Dictation sentences | 132 |
| Shadowing sentences | 132 |

## Tests and builds

- `python tools/validate_content.py` — passed (`errors=0`; existing warnings remain).
- `python -m unittest discover -s tools/tests` — passed.
- `./gradlew check assembleDebug assembleBenchmark --no-daemon` — passed.
- Added unit coverage for sentence case/punctuation tolerance, wrong-word reporting, and long-word typo grading.

APK outputs from the benchmark build:

- Debug APK: 13,190,547 bytes.
- Benchmark APK: 6,941,238 bytes.

## Screenshot

![Task 10 benchmark screenshot](../../build/task10-home.png)

The screenshot was captured from the installed benchmark APK on the test device. ADB input injection is blocked by the device security policy, so automated navigation into an E18/E19 exercise was unavailable; the exercise grading paths are covered by unit tests and the full benchmark build.

## Scope check

Only Kotlin code, tests, and this report were changed. No files under `content/`, `tools/authoring/`, or `app/src/main/res/values*` were modified.
