# Task 5: UI localization

## What changed

- Made `app/src/main/res/values/strings.xml` the English fallback with short English UI labels and messages.
- Added `app/src/main/res/values-vi/strings.xml` and copied the previous default resource values there so Vietnamese resource lookup preserves the existing UI behavior.
- Added localized `values`/`values-vi` strings for the pet catalog, species names, and default pet names. `PetItems`, `PetHomeScreen`, and `OnboardingScreen` now resolve these through Android resources instead of hardcoded Vietnamese labels.
- Added localized pet-name suggestions to `arrays.xml` and `values-vi/arrays.xml`; the existing reminder array remains unchanged in the Vietnamese resource file.
- Added `build-ui <locale>` to `tools/locale.py`; it writes `values-<qualifier>/strings.xml` only when every UI key is approved, with `es → values-es` and `pt-BR → values-pt-rBR` mapping.
- Kept `android:supportsRtl="true"`; no layout uses `android:left` or `android:right`.
- Kept `localeFilters` restricted to `en` and the fully approved `vi` UI.

## Counts before / after

| UI asset | Before | After |
|---|---:|---:|
| Fallback string resources | 550 | 593 English keys |
| Vietnamese string resources | 0 separate file | 593 keys |
| Localized pet catalog/species/default-name strings | 0 | 43 |
| Pet-name suggestion arrays | hardcoded in Kotlin | 2 localized resource arrays |
| User-visible Vietnamese string literals in Kotlin | 43 catalog/name literals | 0 |

The 550 existing fallback entries were preserved in the Vietnamese resource file; the 43 additional keys support the resource-backed pet catalog and onboarding names.

## Verification

- `python tools/gen_content.py` passed before work and again after resource-key changes.
- Resource key parity: fallback, Vietnamese, and approved `vi/ui.json` each contain 593 keys.
- `python -m unittest discover -s tools/tests -v` passed: 12 tests, including `locale.py build-ui` coverage.
- `python tools/locale.py check --all` passed.
- `python tools/validate_content.py` passed with 0 errors.
- `gradlew check assembleDebug` passed.

The content audit remains at 7,792 pre-existing quality errors and was not changed. No files under `tools/authoring/` were edited. A device-level visual Vietnamese screenshot comparison remains a human QA follow-up; the resource fallback and string-key behavior are preserved in code and build checks.
