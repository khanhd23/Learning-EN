# Task E — pictures to 500

Date: 2026-10-09

## Delivered

- Expanded `tools/authoring/word_images.tsv` from 349 to exactly 500 unique word mappings.
- Added 151 new mappings for silver lesson words across animals, food, home, clothes, health,
  transport, technology, education, money, sports, and nature.
- Reused the pinned Noto Emoji SVGs where the object is clear and added owner-drawn SVGs for
  `cooker`, `kettle`, and `tablet` where the available emoji was not an accurate picture.
- No new UI string keys were needed.

## Build measurements

| Measure | Before | After |
|---|---:|---:|
| Word mappings | 349 | 500 |
| Generated WebP resources | 349 | 500 |
| Generated image bytes | 896,554 | 1,288,976 |
| Image budget | — | 1.23 MiB of 3 MiB |

Noto Emoji: `v2.051`; vendored SVG payload SHA-256:
`031b2586f15d28c6dc2cb8d366ba8720bdae4abe0e93e5d40544da019603b340`.
The generated notice points to `third_party/noto-emoji/LICENSE`.

## Manual educational QA

- Read all 151 new mappings against the English lemma, definition, topic, and silver tier.
- Confirmed every new row has a unique word ID, a source SVG, and an image that represents the
  concrete word or its close everyday category.
- Ran the image builder and confirmed `mapped=500` and `missing=[]`.
- Viewed representative final renders for an animal, dish, bathroom, screen, money symbol,
  mountain, mug, backpack, airplane, taxi, pasta, cooker, kettle, and tablet.
- Rejected and corrected the obvious mismatches found during inspection: `cooker` no longer uses
  a frying-pan emoji, `kettle` no longer uses a tea-cup emoji, `tablet` no longer uses a laptop,
  and the ambiguous screenshot mapping was replaced with the exact `pasta` image.
- No missing source, duplicate word ID, non-silver word, or image-budget violation remained.

## Checks

- Mapping QA script — passed (500 total; 151 new; all new words silver; all sources present).
- `python tools/build_images.py --root . --output build/manual-images-final6-20261009` — passed
  (`mapped=500`, `missing=[]`, `bytes=1,288,976`).
- `python tools/validate_content.py` — passed (`errors=0`; existing warnings: 245).
- `python -m unittest discover -s tools/tests` — passed (32 tests; locale `xx` errors=0).
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.
