# Task 19 — cold-start and heavy-screen performance

## Changes

- `TodayScreen` builds the hero, placement/continue content, daily challenge and weekly quests
  first. The lower cards that scan learning history or large content collections are posted after
  the first frame and are cancelled when the screen is rebuilt.
- `StoryScreen` caches sentence splitting and target-word regular expressions, then fills sentence
  views with `addInFrames`; translation and the continue button are added after the sentence batch.
- `WordListScreen` uses one synchronized `LearningStore.masteredCount` lookup for progress and uses
  a 1 ms per-frame fill budget for rows loaded after the first visible rows.
- `ScrollScreen` and the story reader now emit `EngPerf` build/setup timings for benchmark runs.

No strings, content files, authoring files, or configuration text files were changed by this task.

## Measurements

The owner baseline in `docs/ROADMAP_V1.md` is:

| Screen/event | Before |
|---|---:|
| Today cold build | 417–421 ms |
| Vocab first open | 149 ms |
| Story create | 83–100 ms |
| WordList frames after opening | 66–100 ms |

`assembleBenchmark` completed successfully and produced:

```text
app/build/outputs/apk/benchmark/app-benchmark.apk — 7,655,859 bytes
```

No Android device or AVD was available in this environment (`adb devices -l` returned no
devices), so post-change `EngPerf` timings and frame gaps could not be captured here. The app now
logs `TodayScreen build=…`, `StoryScreen reader setup=…`, and the existing frame-gap stream when
the benchmark build is run on the publisher's Xiaomi or an equivalent device. The target of no
frame over 50 ms after the slide-in therefore remains to be verified on hardware.

## Verification

- `python tools/validate_content.py` — pass (`errors=0`; 253 existing warnings).
- `python -m unittest discover -s tools/tests` — pass (29 tests).
- `./gradlew testDebugUnitTest` — pass.
- `./gradlew check assembleDebug assembleBenchmark` — pass.

## Review notes

The repository contained unrelated pre-existing owner changes in `content/**` and
`tools/authoring/**`; they were left untouched and are not included in the Task 19 commit.
