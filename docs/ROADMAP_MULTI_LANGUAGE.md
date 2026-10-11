# LingoMori multi-language expansion roadmap

Status: planning document

Decision date: 2026-10-11

Owner: Publisher and native-language reviewers

Engineering owner: Codex

## 1. Executive decision

The next localization is **Latin American Spanish (`es-419`, code `es`)**, with **Mexico as the first market to validate**.

This is a market pilot, not a Mexico-specific product fork. One Spanish content pack must serve the Spanish-speaking Latin American market. Country-specific differences belong in `content/i18n/market_profiles.json` and, only when necessary, in a `regional` field. The application ID, codebase, English source content, IDs, learning engine, and approval rules remain shared by every market.

The planned expansion order is:

1. Vietnamese (`vi`) — current base locale; continue fixing gaps and keeping it stable.
2. Latin American Spanish (`es-419`, stored as `es`) — Mexico pilot and regional expansion.
3. Brazilian Portuguese (`pt-BR`) — Brazil first.
4. Indonesian (`id`) — Indonesia first.
5. Hindi (`hi`) — India first, with explicit Hindi-market scope rather than assuming that every Indian learner is a Hindi speaker.
6. Additional locales only after the approval, QA, support, and measurement gates below are met.

The order is deliberately not a population ranking. It balances addressable demand, reach from one locale pack, translation and review cost, content risk, store discoverability, and the existing repository plan.

## 2. Why Mexico / `es-419` is the next pilot

The following signals are directional, not a forecast of installs or revenue:

| Candidate | 2024 population | EF EPI 2025 score | Digital signal | Main opportunity | Main risk |
|---|---:|---:|---|---|---|
| Mexico / `es-419` | 130.9 million | 440 | 110 million internet users and 127 million mobile connections reported for January 2025 | One Spanish pack can serve Mexico and other Latin American markets; very low English proficiency suggests a clear learning need | Spanish market is competitive; regional wording and store metadata need native review |
| Brazil / `pt-BR` | 212.0 million | 482 | Large single-language Android market | Large audience and one national Portuguese variant | Portuguese localization is substantial; Brazilian usage must be kept separate from European Portuguese |
| Indonesia / `id` | 283.5 million | 471 | 212 million internet users and 356 million mobile connections reported for January 2025 | Large mobile-first audience and a single national learner language | Lower purchasing power and strong local competition require careful pricing and retention testing |
| India / `hi` | 1.451 billion | 484 | Very large potential audience | Exceptional scale and English is important in education and work | India is multilingual; Hindi is not a safe proxy for the entire country, and content/support complexity is high |

The EF EPI score is used only as a demand signal. It measures the English skills of people who took EF's assessment and must not be interpreted as a census of the whole country. A lower score can indicate a stronger learning need, but it does not prove willingness to pay, retention, or store conversion.

Mexico is the best first expansion for LingoMori because it combines a high need signal with an efficient locale strategy: the first translation investment can be reused across a large Spanish-speaking region. This is an inference from the market and repository constraints, not a claim that Mexico is guaranteed to be the highest-revenue market.

If the owner prefers a single-country experiment after Spanish, Indonesia is the next recommended pilot. It has a large mobile audience, one primary learner-language pack, and a lower localization complexity than India.

## 3. Non-negotiable product rules

### 3.1 One app, one source of truth

- Keep one application ID: `com.khankstudio.lingomori`.
- Never create a country-specific APK, application ID, content fork, or duplicated exam bank.
- English remains the source of truth for IDs, English words, English examples, question structure, levels, topics, and grammar identifiers.
- A learner locale is a translation/adaptation pack, not a translated copy of another learner locale.
- Country targeting maps countries to a locale; countries are not themselves locale packs.

### 3.2 Approval gates are enforced in the build

A locale is selectable only when:

- its UI is 100% approved;
- Level 1 vocabulary is at least 95% approved;
- Level 1 grammar is at least 95% approved;
- `tools/locale.py check <locale>` passes;
- the English source hashes are current;
- no required locale field is missing, stale, or still draft;
- the APK contains only approved locale content.

Levels below the approval threshold remain unavailable and show the existing “Coming soon” behavior. No locale is marketed as fully supported while it is only a machine draft.

### 3.3 Content field policy

- `TRANSLATE`: glosses, example translations, grammar explanations, question explanations, UI, tips, and pet lines.
- `KEEP`: English words being taught, English example sentences, IDs, placeholders, symbols, and markup.
- `REWRITE`: learner-language notes, false-friend warnings, pronunciation guidance, and explanations that depend on the learner's first language. These must be written for the target L1, not mechanically translated from Vietnamese or another locale.
- Preserve every stable ID. Never silently delete a word, sense, example, topic, question, or grammar point to make the locale pass.
- Keep the target language's natural register consistent. Do not mix European Spanish into the Latin American base pack, and do not mix European Portuguese into `pt-BR`.

## 4. Locale and market model

### 4.1 Locale identifiers

Use the following canonical identifiers:

| Product name | Locale directory | Android qualifier | Initial market |
|---|---|---|---|
| English | `en` | `values` | Global fallback |
| Vietnamese | `vi` | `values-vi` | Vietnam |
| Latin American Spanish | `es` | `values-es` | Mexico, then Latin America |
| Brazilian Portuguese | `pt-BR` | `values-pt-rBR` | Brazil |
| Indonesian | `id` | `values-in` | Indonesia |
| Hindi | `hi` | `values-hi` | India Hindi-speaking users |

The Android qualifier and the content directory may use different syntax where Android requires a regional qualifier. The generator is the authority for the exact generated paths; do not hand-create duplicate resource trees.

### 4.2 Country profiles

`content/i18n/market_profiles.json` should map a market to a locale and optional variant metadata. The first profiles should be conceptually equivalent to:

```json
{
  "MX": { "locale": "es", "variant": "es-419" },
  "BR": { "locale": "pt-BR", "variant": "pt-BR" },
  "ID": { "locale": "id", "variant": "id" },
  "IN": { "locale": "hi", "variant": "hi" }
}
```

The exact file is created only through the assigned data task and the generator. This roadmap does not authorize hand-editing generated content.

### 4.3 Device and user selection

- First launch defaults to the device language only when that locale is selectable.
- Otherwise the app uses English and shows the language picker.
- Settings can change the UI language immediately without losing the learner's progress.
- Changing UI language must not duplicate learning records or reset the selected exam, level, or review schedule.
- Content visibility is filtered by approval status, not by the country guessed from the network.
- Do not use a VPN, IP address, SIM country, or advertising profile as the only source for language selection.

## 5. Delivery phases

### Phase 0 — Baseline and market readiness

Goal: make the Spanish pilot measurable and reversible before translating.

Tasks:

1. Freeze the English source snapshot and record its content hash.
2. Run `validate_content`, `audit_content`, and `locale.py check --all`; record the baseline rather than hiding legacy warnings.
3. Inventory the English source by file, level, topic, grammar point, exercise type, and word list.
4. Identify every user-visible string and every content field that a locale must provide.
5. Confirm that English fallback is complete and Vietnamese is unchanged.
6. Confirm that no store screenshot, icon, or feature graphic suggests that LingoMori is a government service, a children's-only product, or an official exam provider.
7. Create a Spanish reviewer brief with the target register: neutral Latin American Spanish, short UI labels, and English examples kept unchanged.
8. Define the support channel for Spanish testers before inviting them.

Exit criteria:

- source hash is recorded;
- baseline reports are archived;
- the locale pipeline can create an empty locale without affecting English or Vietnamese;
- the owner has identified at least one native Spanish reviewer and one backup reviewer.

### Phase 1 — Spanish Level 1 pilot

Goal: make a genuinely usable, reviewable Spanish Level 1 experience.

Workflow:

```text
python tools/locale.py new es
python tools/locale.py draft es --level 1
# Write and review es/l1_notes.json and all Spanish REWRITE fields.
python tools/locale.py export es --level 1
# A native reviewer reviews the exported batches.
python tools/locale.py import es review/es-reviewed.csv --reviewer <name>
python tools/locale.py approve es --level 1
python tools/locale.py check es
python tools/locale.py report
```

The pilot must include:

- approved UI strings for onboarding, navigation, learning, exams, settings, backup/restore, ads consent, and error states;
- Level 1 topics and topic names;
- Level 1 words, glosses, example translations, and pronunciation support;
- Level 1 grammar explanations and exercise feedback;
- exam instructions and explanations that are shown to the learner;
- Spanish pronunciation notes that address Spanish-speaker transfer issues;
- false-friend guidance such as `actually/actualmente`, `embarrassed/embarazada`, `library/librería`, `sensible/sensible`, `assist/asistir`, `carpet/carpeta`, `exit/éxito`, `realize/realizar`, and `attend/atender`;
- a consistent treatment of dropped subjects, “I have 20 years”, adjective position, articles, double negatives, `people are`, general nouns, and `make/do`;
- localized pet and reward text where the feature is shipped, even though pet screens are not used as Store screenshots;
- no Vietnamese wording or Vietnamese-specific explanations leaking into `es`.

The Spanish pack must use neutral Latin American wording. Mexico-specific terms belong in a regional profile only when they improve comprehension without making the shared pack unnatural.

### Phase 2 — Native review and approval

Goal: turn a draft into a locale that can be shipped.

Review process:

1. Export small batches grouped by field class and level.
2. Have the native reviewer check meaning against the English definition and example, not against a machine translation.
3. Have a second reviewer sample all REWRITE fields and every high-risk item: false friends, pronunciation, grammar, answer explanations, and exam instructions.
4. Import review results with reviewer identity and review date.
5. Resolve conflicts explicitly; never overwrite a reviewed value with a new machine draft.
6. Approve only the exact entries that have been reviewed.
7. Run a stale-source check after every English content update.

Required review evidence:

- reviewer name and language expertise;
- batch IDs and date;
- unresolved questions and owner decisions;
- examples of changes made after review;
- counts by level, file, and status;
- a manual sample of at least 50 word-to-gloss/example pairs and all high-risk REWRITE entries.

### Phase 3 — Product QA and Spanish Store Listing

Goal: verify that Spanish is a learner experience, not only a translated database.

Product checks:

- onboarding language picker and first-launch device-language default;
- immediate language switching from onboarding and Settings;
- English teaching text remains English where it is supposed to be learned;
- Spanish gloss and explanation appear in the correct locale;
- approved-only filtering works in vocabulary, grammar, stories, picture exercises, and exams;
- unavailable levels show “Coming soon” rather than empty or mixed-language screens;
- search ranks Spanish glosses correctly, including accented queries;
- backup/restore keeps locale preference and learning progress separate;
- TTS uses the English voice for English teaching material and does not replace English examples with Spanish;
- no layout overflow from Spanish word length, accents, punctuation, or plural forms;
- no pet screen is used in the Store screenshot set for this market unless the owner explicitly approves it.

Store work for `es`:

- add Spanish title, short description, and full description through Manage translations;
- upload only reviewed Spanish screenshots, with no claims of official exam or government affiliation;
- use the same icon and feature graphic unless the owner approves a localized alternative;
- keep legal, privacy, support, and source statements accurate and link to working pages;
- do not use automatic translation for policy or legal text without human review;
- verify the listing on a Spanish-language device before submitting.

### Phase 4 — Mexico soft launch and measurement

Goal: validate the market without committing to a full multi-country marketing launch.

Launch sequence:

1. Internal track: owner and engineering smoke test.
2. Closed test: a small Spanish-speaking reviewer group tests onboarding, vocabulary, grammar, exams, search, audio, and support reporting.
3. Fix release-blocking issues and upload the next version to the same appropriate track.
4. Keep a changelog of every Spanish-language issue and its resolution.
5. Submit the production-access request only when the account's Play testing requirement is satisfied; language count does not replace the tester requirement.
6. Roll out gradually and monitor crashes, ANRs, store conversion, first-session completion, search success, and support reports.

No market is declared successful from downloads alone. Continue only when learners complete real sessions and reviewers report that the explanations are natural and useful.

### Phase 5 — Portuguese, Indonesian, and Hindi

Each new locale repeats the Spanish workflow. Do not start all three at once.

#### `pt-BR` — Brazil

Start after the Spanish pilot has passed its review and release gates. Use Brazilian Portuguese throughout the base pack. Add Portuguese-specific REWRITE content for pronunciation and common transfer errors, and never reuse Spanish wording as the source.

Minimum scope: approved UI, Level 1 vocabulary and grammar, onboarding, search, exercise feedback, exam instructions, store listing, and a native reviewer report.

#### `id` — Indonesia

This is the preferred next single-country experiment if the owner wants to prioritize one country rather than the Spanish region. Keep the UI concise for small screens, test low-bandwidth behavior, and validate that English examples remain unchanged while Indonesian glosses and explanations are natural.

#### `hi` — India

Treat Hindi as a Hindi-speaking market, not as “all of India”. Before translation, confirm that the target audience, store metadata, support language, and reviewer group are Hindi-speaking. Later Indian expansion may need additional locales rather than a Hindi-only claim.

## 6. Reusable implementation checklist for every locale

### Source and generation

- [ ] English source hash recorded.
- [ ] `tools/locale.py new <locale>` completed.
- [ ] Generator output is reproducible.
- [ ] No manual edits to generated `content/**` files.
- [ ] No manual edits to `tools/authoring/**` unless the task explicitly assigns the content batch.
- [ ] No unofficial translation endpoint used.

### Content

- [ ] UI, topics, vocabulary, grammar, questions, stories, picture labels, and pet lines have coverage or an intentional “Coming soon” state.
- [ ] Level 1 vocabulary and grammar meet the 95% approval gate.
- [ ] All shipped entries are approved.
- [ ] English teaching text, IDs, placeholders, punctuation rules, and markup are preserved.
- [ ] L1-specific REWRITE fields were written by a native reviewer.
- [ ] No Vietnamese leakage or source-locale leakage.
- [ ] Search works with accents and without accidental cross-locale results.

### App behavior

- [ ] Locale is selectable only when the approval gate passes.
- [ ] Device-language default works when selectable; otherwise English is used.
- [ ] Settings changes the UI immediately and preserves progress.
- [ ] Levels below the gate show “Coming soon”.
- [ ] Ads, consent, backup, restore, TTS, and notifications do not expose the wrong locale.
- [ ] Long translations do not clip or cover controls.

### QA commands

Run the full set from the repository root:

```text
python tools/validate_content.py
python tools/audit_content.py
python tools/locale.py check --all
python -m unittest discover -s tools/tests
./gradlew testDebugUnitTest
./gradlew check
./gradlew assembleDebug
```

For a selectable locale, also run:

```text
python tools/locale.py check <locale>
./gradlew :app:connectedDebugAndroidTest
```

If an instrumented device is unavailable, record that limitation and perform the equivalent manual smoke test on a real device. Do not mark the locale production-ready from unit tests alone.

### Store acceptance

- [ ] Default listing is complete and accurate.
- [ ] Localized title, short description, full description, and screenshots are reviewed by a native speaker.
- [ ] Screenshots show the current app and do not imply government affiliation, official exam ownership, or child-only targeting.
- [ ] Feature graphics and icons are declared correctly when AI-generated or AI-edited.
- [ ] Privacy policy and support links work in the target market.
- [ ] Store language and in-app language are not claimed to be available beyond the approved build.

### 6.1 End-to-end locale conversion runbook

Use this sequence for every new learner locale. Replace `<locale>` with the canonical locale
identifier, for example `es`, `pt-BR`, `id`, or `hi`. A step is not complete until its exit
condition is met; do not start the next phase by simply marking an incomplete pack as complete.

#### Step 0 - choose the scope and owner

1. Select one target locale and its first market. A market profile maps a country to a locale;
   it does not create a country-specific content fork.
2. Decide the first release scope: UI only, Level 1 learning, exams, stories, picture practice,
   pet text, or a combination. Anything outside the scope must remain unavailable or show the
   existing `Coming soon` state.
3. Name a native-language reviewer and a backup reviewer. Record the target register, spelling,
   punctuation, and any regional restrictions in the reviewer brief.
4. Confirm that all policy, privacy, support, and store links have an owner who can verify them.

**Exit condition:** the locale code, market mapping, scope, reviewer, and support process are
recorded before any translation batch is generated.

#### Step 1 - freeze and inventory the English source

1. Record the current English content hash and repository revision.
2. Run the baseline checks and save their output:

   ```text
   python tools/validate_content.py
   python tools/audit_content.py
   python tools/locale.py check --all
   ```

3. Inventory the source by file, level, topic, word list, grammar point, exercise type, exam,
   story, picture label, and user-visible UI string.
4. Classify every field as `TRANSLATE`, `KEEP`, or `REWRITE`. Confirm that stable IDs,
   placeholders, markup, English teaching text, and answer indexes will not change.

**Exit condition:** the source hash and baseline reports are saved, and the reviewer knows exactly
which fields will be translated, preserved, or written specifically for the target L1.

#### Step 2 - create the locale scaffold

Create the pack with the repository tool; do not copy or hand-edit another learner locale:

```text
python tools/locale.py new <locale>
python tools/locale.py report
```

Check that the scaffold contains the expected files, stable IDs, empty status entries, and no
Vietnamese or other source-locale leakage. Do not ship the scaffold and do not treat empty fields
as translations.

**Exit condition:** `content/i18n/<locale>/` exists, the scaffold shape matches the English
source, and all entries start in `missing` status.

#### Step 3 - prepare terminology and machine drafts

1. Build a glossary before translating: navigation labels, learning actions, grammar terms,
   exam names, score labels, reward terms, accessibility labels, and common product terms.
2. If machine translation is used, run it only offline through the provider configured in
   `secrets.properties`. Never use an unofficial scraping endpoint, and never call a translation
   service from the Android app.
3. Generate only a bounded batch, normally one file or level at a time:

   ```text
   python tools/locale.py draft <locale> --level 1 --provider <approved-provider>
   python tools/locale.py export <locale> --level 1 --status draft
   ```

4. Treat every machine result as `draft`. Do not change review metadata in bulk and do not
   translate `KEEP` fields such as English words, example sentences, IDs, placeholders, or answer
   choices.

**Exit condition:** each draft batch has a source hash, glossary version, provider/model record,
and an explicit list of entries awaiting human review.

#### Step 4 - write L1-dependent content and review the batch

1. Write `REWRITE` fields for the target learner language: false-friend warnings, pronunciation
   transfer notes, grammar contrasts, confusable advice, and `l1_notes`. Do not machine-translate
   these from Vietnamese or another locale.
2. The native reviewer checks each translated meaning against the English source, including:
   register, part of speech, example meaning, grammar accuracy, naturalness, and regional usage.
3. The second reviewer samples all high-risk entries and resolves disagreements with an owner
   decision. Review answers, explanations, exam instructions, TTS-visible text, and long strings
   more strictly than short UI labels.
4. Import reviewed rows with reviewer identity:

   ```text
   python tools/locale.py import <locale> content/review/<locale>/batch_001.csv --reviewer <name>
   python tools/locale.py export <locale> --status reviewed
   ```

**Exit condition:** every shipped-scope entry has a human-reviewed value or an intentional
`Coming soon` decision; unresolved questions are listed rather than silently guessed.

#### Step 5 - approve a release slice

1. Run the locale checks and review the report by file and level:

   ```text
   python tools/locale.py check <locale>
   python tools/locale.py report
   ```

2. Approve only the exact reviewed slice, for example:

   ```text
   python tools/locale.py approve <locale> --level 1 --file words
   python tools/locale.py approve <locale> --level 1 --file grammar
   python tools/locale.py approve <locale> --file ui
   ```

3. Confirm the release gate: UI is fully approved, Level 1 vocabulary and grammar meet the
   required threshold, explanations and high-risk rewrites are reviewed, and no stale source
   hashes remain.
4. Generate locale Android resources only after UI approval:

   ```text
   python tools/locale.py build-ui <locale>
   ```

**Exit condition:** the approval manifest marks the intended locale slice selectable and the
generated UI resource is reproducible. A draft or partially approved locale must remain hidden.

#### Step 6 - validate the built product

Run the complete checks from the repository root:

```text
python tools/validate_content.py
python tools/audit_content.py
python tools/locale.py check --all
python -m unittest discover -s tools/tests
./gradlew testDebugUnitTest
./gradlew check
./gradlew assembleDebug
```

Then perform a real-device smoke test in the target locale:

- first-launch language selection and device-language fallback;
- language switching without losing progress or resetting exam state;
- vocabulary search with accents and no accidental cross-locale results;
- grammar, stories, pictures, audio, TTS, exams, rewards, ads/consent, backup, and restore;
- `Coming soon` behavior for content outside the approved scope;
- long translations, punctuation, plurals, RTL/script rendering, and small screens;
- English teaching material remains English where it is intentionally being taught.

**Exit condition:** all automated checks pass, the reviewer signs off the device smoke test, and
any limitation is documented in the locale report.

#### Step 7 - prepare the store and closed test

1. Prepare the localized title, short description, full description, screenshots, support text,
   privacy link, and source links. A native reviewer checks the entire listing.
2. Confirm that the listing does not imply government affiliation, official exam ownership, or
   support for content that is not in the approved build.
3. Upload the same signed application to the planned closed-test track, invite the target-language
   testers, and provide a support form or email in that language.
4. Track crashes, ANRs, fallback-to-English events, zero-result searches, confusing translations,
   audio failures, and user reports. Fix release blockers in a new batch and repeat review.

**Exit condition:** the closed test completes without release-blocking locale issues, all links
work, and the store listing matches the exact shipped scope.

#### Step 8 - release, monitor, and expand

1. Publish only after the owner makes the go/no-go decision and the locale remains approved in the
   build manifest.
2. Monitor language-specific support, retention, lesson completion, search success, crash-free
   sessions, and repeated switches back to English.
3. Keep a changelog of corrections and a backup of the reviewed locale pack.
4. For new English content, translate only the changed source IDs, run the stale-source check,
   review the new batch, and rebuild. Never retranslate the whole locale and overwrite reviewed
   work unnecessarily.
5. Start the next locale only when the current locale has a repeatable review process and is not
   delaying English or Vietnamese fixes.

**Definition of complete:** the locale is selectable from the app, all shipped entries are
approved, the APK contains no unapproved locale data, automated and device QA pass, the closed
test is complete, store/support links are verified, and the owner has approved release.

## 7. Measurement plan

Use Play Console and privacy-respecting in-app diagnostics. Do not add a new analytics SDK only to make this roadmap measurable without an owner decision.

### Product metrics

- store listing conversion by language and market;
- first-session completion;
- activation: completed onboarding plus first learning activity;
- Day 1, Day 7, and Day 30 retention;
- vocabulary and grammar completion by level;
- search success and zero-result queries;
- audio/TTS use and error rate;
- language-switch failures and fallback-to-English events;
- crash-free sessions, ANRs, and slow-start reports;
- support tickets grouped by translation, content, layout, audio, and policy.

### Decision gates

**Continue the pilot** when the locale is technically stable, reviewers find no material meaning errors, and learners complete sessions without repeatedly switching back to English for explanations.

**Pause the pilot** when there are repeated translation errors in core lessons, missing approvals, high crash/ANR rates, broken links, or a store listing that implies unsupported official affiliation.

**Expand to the next locale** only after:

- one release has passed the full locale and device QA;
- all release-blocking feedback is closed or documented;
- native review coverage is repeatable;
- the owner has a support plan for the new language;
- the current locale's content pipeline is not delaying English or Vietnamese fixes.

## 8. Proposed timeline

The dates below are estimates and should move when review capacity or release testing changes.

| Period | Deliverable | Exit gate |
|---|---|---|
| Week 0 | Baseline, source hash, reviewer brief, Mexico market profile plan | Reproducible locale creation |
| Weeks 1–2 | Spanish draft and Level 1 translation batches | Draft exported; no unofficial endpoint |
| Weeks 2–3 | Native review and REWRITE fields | Reviewer evidence complete |
| Week 4 | Approval, build filtering, locale QA | UI 100%, Level 1 vocab/grammar ≥95% |
| Week 5 | Spanish Store Listing and device screenshots | Listing and links checked |
| Weeks 6–7 | Closed-test feedback and fixes | No release-blocking Spanish issues |
| Week 8 | Mexico soft launch decision | Continue, pause, or revise based on evidence |
| After Week 8 | `pt-BR`, then `id`, then `hi` | Previous locale remains maintainable |

## 9. Responsibilities

### Publisher

- choose the final market and budget;
- recruit native reviewers and testers;
- own Play Console, signing, policy declarations, support email, and go/no-go decisions;
- verify privacy, legal, and store claims;
- keep backup copies of release keys and accepted assets.

### Native reviewers

- review meaning, register, grammar explanations, pronunciation notes, false friends, and store text;
- identify unnatural or regionally confusing wording;
- approve only entries they personally checked;
- provide examples of corrections for later batches.

### Codex

- maintain the locale pipeline, approval gate, resource generation, filtering, tests, build, and release QA;
- never bypass review status or bundle unapproved content;
- report counts, failures, stale entries, and unresolved owner decisions;
- never push to the remote repository.

## 10. Open decisions before starting Spanish

- [ ] Confirm Mexico as the first Spanish market pilot.
- [ ] Name the primary and backup native Spanish reviewers.
- [ ] Confirm whether Spanish support is learner UI only at first or includes exams, stories, picture practice, and pet text.
- [ ] Confirm the Spanish support email response process.
- [ ] Confirm whether the first Spanish release is closed-test-only or also includes a production-access application.
- [ ] Confirm that all Spanish Store Listing links and policy text are working before upload.
- [ ] Confirm whether paid features, ads, and reward wording need market-specific review.
- [ ] Confirm the owner will review the final `es` content before approval.

## 11. References

- [EF English Proficiency Index 2025](https://www.ef.com/assetscdn/WIBIwq6RdJvcD9bc8RMd/cefcom-epi-site/reports/2025/ef-epi-2025-english.pdf) — directional proficiency signal and methodology caveat.
- [World Bank Population 2024](https://datacatalogfiles.worldbank.org/ddh-published/0038126/DR0046429/POP.pdf) — population comparison used for market sizing.
- [Digital 2025: Mexico](https://datareportal.com/reports/digital-2025-mexico) — internet and mobile indicators.
- [Digital 2025: Indonesia](https://datareportal.com/reports/digital-2025-indonesia) — internet and mobile indicators for the fallback pilot.
- [INEGI ENDUTIH 2025](https://www.inegi.org.mx/programas/endutih/2025/) — official Mexico connectivity and technology-use survey.
- [Google Play: Translate and localize your app](https://support.google.com/googleplay/android-developer/answer/9844778) — localized listing workflow.
- [Google Play: Add preview assets](https://support.google.com/googleplay/android-developer/answer/9866151) — screenshot and graphic-asset requirements.

