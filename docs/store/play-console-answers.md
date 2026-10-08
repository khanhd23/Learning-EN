# Play Console answers (draft by Claude, Publisher submits)

Check each answer against the build you upload. If a library is added or removed, update this file.

## App content → Privacy policy

URL of `docs/store/privacy-policy.md` once hosted (also put it in `config/app_config.json`
`privacyPolicyUrl`). Replace `{EFFECTIVE_DATE}`, `{DEVELOPER_NAME}`, `{CONTACT_EMAIL}` first.

## App content → Ads

Contains ads: **Yes**.

## App content → App access

All functionality is available without special access: **Yes** (no login).

## App content → Target audience and content

- Target age groups: **13–15, 16–17, 18 and over**. Do not tick any group under 13.
- "Could your app unintentionally appeal to children?" Answer honestly. If Play decides the pet
  appeals to children, follow the Families policy steps in `docs/RELEASE_CHECKLIST.md`.

## App content → Content rating (IARC questionnaire)

- Category: **Reference, News, or Educational**.
- Violence, sexuality, language, controlled substances, gambling: **No**. Dictionary search can
  show words with adult meanings, but lessons never use them and there is no explicit content.
- User interaction / user-generated content / sharing location / digital purchases: **No**.
- Ads: the app shows ads (rated G).

## App content → Data safety

Does the app collect or share any of the required user data types? **Yes** (through the Google
Mobile Ads SDK). Is all collected data encrypted in transit? **Yes**. Do you provide a way for
users to request that their data is deleted? **No** (the app keeps no data on our servers; learning
data is deleted by clearing app storage). If Play requires a deletion method, describe "Clear
storage / uninstall; advertising ID reset in Android settings".

| Data type | Collected | Shared | Optional? | Purposes |
|---|---|---|---|---|
| Location → Approximate location (from IP address) | Yes | Yes (Google, advertising) | No | Advertising or marketing; Analytics |
| Device or other IDs (advertising ID, app set ID) | Yes | Yes (Google, advertising) | No | Advertising or marketing; Analytics; Fraud prevention, security, and compliance |
| App activity → App interactions | Yes | Yes (Google, advertising) | No | Advertising or marketing; Analytics |
| App info and performance → Diagnostics | Yes | Yes (Google, advertising) | No | Analytics |
| Audio → Voice or sound recordings | **No** | No | — | Speech goes to the system speech service, not to the app's own collection; the app does not store or transmit audio |
| Personal info, financial info, messages, photos, contacts, calendar, files, web history | No | No | — | — |

Source for the AdMob rows: Google, "Prepare for Google Play's data disclosure requirements"
(developers.google.com/admob/android/privacy/play-data-disclosure), checked 2026-10-08.

## App content → Advertising ID

Uses advertising ID: **Yes**. Purpose: **Advertising or marketing; Analytics**.

## App content → Government apps / Financial features / Health

No / None / None.

## Store settings

- App category: **Education**.
- Contact email: the support email in `config/app_config.json` (must be the LingoMori address, not
  another app's).
- Tags: Education, Language learning.
