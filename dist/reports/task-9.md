# Task 9: Picture learning

## Done

- Added `tools/build_images.py`, an optional build-time image pipeline. It reads the owner mapping when present, resolves pinned Noto Emoji filenames or owner SVGs, validates the SVG feature set, converts supported paths to Android VectorDrawables, emits `image_manifest.json` and a Noto third-party notice, and fails above the 3 MiB image budget.
- Added a five-word fixture with emoji and hand-drawn SVG inputs. Tests cover successful conversion, missing image data, unsupported SVG elements, and the 3 MiB budget.
- Added nullable `Word.image`, generated-manifest loading, generated resource wiring, and image display in word lists and word details.
- Added E15/E16/E17 picture-choice exercise kinds. The factory and mixed sessions select them only when a word has a generated image; the normal build therefore remains unchanged while image data is absent.
- No new user-visible string keys were needed.

## Build/data state

The owner mapping `tools/authoring/word_images.tsv` exists, but the required Noto SVG payload and owner SVG set are not available in this checkout. The normal build therefore generated:

- image resources: 0
- manifest entries: 0
- generated image payload: 0 bytes
- missing mappings: owner data is reported by the builder and is non-fatal by design

Noto Emoji metadata is vendored under `third_party/noto-emoji/` with version `v2.051`, Apache-2.0 license text, and a generated payload hash notice. No Noto SVG was copied because no owner mapping can be shipped without its corresponding source asset.

## Verification

- `python tools/validate_content.py`: pass, 0 errors, 313 existing warnings.
- `python -m unittest discover -s tools/tests`: pass, 21 tests.
- Image-builder tests: pass, 4 tests.
- `./gradlew check assembleDebug assembleBenchmark`: pass.
- Benchmark APK: 5,949,645 bytes. The previous Task 6.3.1 benchmark was 5,942,133 bytes; this implementation adds 7,512 bytes with no generated image payload.
- Smooth check: benchmark assembly and lint completed successfully. The image UI/exercises could not be exercised because the owner image sources are absent; ADB input injection is denied on the connected physical device.

`audit_content.py` still reports the repository's pre-existing owner-content errors; Task 9 did not edit content or authoring data.
