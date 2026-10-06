# Task 9.1: picture learning review fixes

## Changes

- Replaced VectorDrawable conversion with `resvg-py==0.5.0` rasterization plus Pillow WebP output at 128×128 into `drawable-nodpi`.
- Reads Noto SVGs from `third_party/noto-emoji/svg/`, accepts hyphen-separated codepoints, preserves transforms/gradients/opacity through resvg, and fails when any mapping is missing or the generated pack exceeds 3 MiB.
- Fixed the build ordering so `generateContentAssets` runs before image generation and the image manifest/third-party notice survive into `assets/content`.
- E15 now shows one large picture and text-only word options.
- E16 now auto-plays the word once, shows a large replay button and a 2×2 picture-only grid; labels appear after answering.
- E17 now uses a picture column and a word column with tap-to-pair matching.
- Distractors prioritize shared topics, then part of speech, and are unique by image resource.
- Added Noto Emoji credit to the Settings → Licenses screen.
- Re-enabled picture exercises after the layouts were corrected.

## Counts and size

- Mapped words: 349.
- Missing images: 0.
- Generated WebP files: 349.
- Generated image payload: 895,952 bytes (under 3 MiB).
- Benchmark APK: 5,969,478 bytes.
- No new user-visible strings were added by Codex; the owner-provided `ins_picture_word`, `ins_listen_picture`, and `ins_match_pictures` keys are used.

## Verification

- `python tools/build_images.py --root . --output ...`: pass, 349 mapped, 0 missing.
- `python -m unittest discover -s tools/tests`: pass, 21 tests.
- `python tools/validate_content.py`: pass, 0 errors.
- `./gradlew check assembleDebug assembleBenchmark`: pass.
- Benchmark APK installed successfully on device `d8ad0782`; a home screenshot was captured at `build/task91-home.png`.
- E15/E16/E17 device screenshots could not be captured because the connected device rejects ADB input injection (`INJECT_EVENTS`); the benchmark build and resource packaging were verified, but interactive navigation remains owner/device-manual review.

The existing `audit_content.py` owner-content errors remain outside this code-only task.
