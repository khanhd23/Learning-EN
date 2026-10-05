# Task 6.1: UI follows the chosen language

## Changes

- Added a shared `AppLocale` bridge from the app's content-locale IDs to AppCompat per-app UI locales.
- On first launch, the device language is selected when it matches a selectable locale (exact or base language); otherwise English is selected.
- Onboarding language cards persist the selected content locale and apply the UI locale immediately.
- Settings language selection persists the choice and applies the UI locale immediately.
- Added Android 13 per-app language metadata with the currently shipped English and Vietnamese locales; AppCompat handles persistence on older Android versions.

## Tests and verification

- Added `AppLocaleTest` for device-language selection, English fallback, and stored-locale tag normalization.
- Existing `ApprovalGateTest` and the new locale tests pass.
- `./gradlew testDebugUnitTest --tests '*AppLocaleTest' --tests '*ApprovalGateTest' assembleDebug`: pass (`BUILD SUCCESSFUL`).
- No strings, content, or `tools/authoring/` files were edited.

## Review notes

- Future approved locale packs should also be added to `res/xml/locales_config.xml` when they become selectable, so Android 13 system per-app language settings can expose them.
