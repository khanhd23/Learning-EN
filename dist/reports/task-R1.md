# Task R1: release readiness

## Changes

- Set the Google Mobile Ads global maximum content rating to `G` before SDK initialization. Child-directed treatment remains unspecified.
- Added offline Apache-2.0 and CC BY-SA 4.0 license assets. The Licenses screen keeps `content/LICENSES.md` first, then shows the bundled third-party license text without network access.
- Privacy policy now always opens the in-app policy text. When `privacyPolicyUrl` is non-empty in `config/app_config.json`, the page also offers a button to open that public URL; while empty, no public-link button is shown.
- Added an `adult` flag to the generated content database, derived from the owner-maintained `config/blocked_senses.txt`. Search, level/topic DB queries, direct DB word lookup, and in-app vocabulary lists exclude flagged entries.
- Bumped the content DB schema to version 2 and included the blocked-senses file in the content hash.
- Kept the existing `verifyReleaseAds` CI-style guard: release bundling fails when test or missing AdMob IDs are present and can proceed with real values from `secrets.properties`.

No new UI strings were needed.

## Verification

- `python tools/validate_content.py` — passed (`errors=0`, 313 existing warnings).
- `python -m unittest discover -s tools/tests` — passed (22 tests).
- `./gradlew check assembleDebug --no-daemon` — passed.
- `./gradlew verifyReleaseAds --no-daemon` — failed as intended because this workspace has no real AdMob IDs in `secrets.properties` (`appId`, `banner`, `interstitial`, `rewarded`).
- `./gradlew bundleRelease --no-daemon` reached `verifyReleaseAds` and was rejected by the same intentional guard. With the owner’s real IDs supplied, the existing dependency causes the release bundle to continue.

The existing content audit baseline remains `errors=6133`; this task changed no content data. No files under `content/`, `tools/authoring/`, `strings.xml`, or `config/blocked_senses.txt` were modified.
