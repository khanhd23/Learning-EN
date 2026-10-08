# EngPet roadmap — phase 3: ship v1.0, then grow exam prep

Written 2026-10-08 by Claude after a project audit. Phase 2 details stay in `docs/ROADMAP.md`;
exam data rules are in `docs/EXAM_DATA_PLAN.md`; release rules in `docs/RELEASE_CHECKLIST.md`.

## Who does what (by strength)

| Who | Strength | Owns |
|---|---|---|
| **Publisher** (the human owner) | Accounts, money, legal identity, real phones, final decisions | Google Play and AdMob accounts, signing key, hosting, store forms, testers, device testing, go/no-go |
| **Claude** | English and Vietnamese content, data, exam formats, review, QA, documents | All words, questions, stories, pictures-as-data, UI text, store and policy text, Codex reviews, pushes |
| **Codex** | Kotlin/Python code and tests | Features, tools, build and release engineering; commits only, never pushes |

Rules that do not change: Codex writes code only; Claude writes all content and reviews every
Codex commit before pushing; only the Publisher creates accounts, signs, uploads and spends money.

## Where the project stands (audit)

Done: lessons with spaced repetition, 20 exercise types (picture, sound, dictation, shadowing,
stories), pet with quests, Vietnamese explanations, SQLite search, smooth rendering, ads with
consent and rating G, licences screen, exam screens (TOEIC P5/P6, school format), section engine
base (Task 14).

| Area | Now | Needed for v1.0 | Needed later |
|---|---|---|---|
| Lesson-ready words | 3,982 | 4,800 (all 1,250 TOEIC words) | 7,000+ (CEFR A1–B2) |
| Topics with < 40 words | 2 (phrasal verbs 30, life stages 31) | 0 | — |
| Word pictures | 365 | 500 | 700 |
| Stories | 16 | 30 | 80 |
| THPT bank | 0 | 5 papers (200 items) | 20 papers |
| TOEIC P5/P6 bank on the engine | general pool only | 300 P5 + 40 P6 texts | 600 + 80, then Part 7 |
| VSTEP / KET / PET banks | 0 | — | 20 papers each |
| Exam word lists in the app | config only | Task 15 | — |
| Full licence texts (Apache §4) | missing | required | — |
| Privacy policy URL | empty | required | — |
| Real AdMob IDs, signing key | missing (`secrets.properties` absent) | required | — |
| Learning data backup | Android auto-backup only | export/import file | — |
| Store listing, screenshots | none | required | — |

## Milestones

- **M1 — Internal test build** (target 2026-10-22): Tasks 14.1, 15, R2, R3 merged; 4,300 words;
  THPT format entry and first 2 papers; signed AAB on the internal track.
- **M2 — Closed test** (target 2026-11-05 start, 14 days): personal developer accounts created
  after Nov 2023 must run a closed test with at least 12 testers opted in for 14 days in a row
  before production access (check the exact numbers in Play Console). THPT 5 papers, TOEIC bank,
  4,800 words, store listing ready.
- **M3 — Production v1.0** (target late November 2026): fix closed-test feedback, rollout 20 % →
  100 %.
- **M4 — Exam growth** (Dec 2026 onward): THPT to 20 papers before the June exam season,
  TOEIC Part 7, VSTEP B1–B2, KET/PET, CEFR B2 words.

## Publisher tasks

| # | Task | Before | Notes |
|---|---|---|---|
| P1 | Create the Google Play developer account (25 USD, identity check) | M1 | Personal or organisation; organisation skips the 12-tester rule but needs a D-U-N-S number |
| P2 | Create the AdMob account and app, ad units (banner, interstitial, rewarded); put the IDs in `secrets.properties` (never commit) | M1 | Keep test IDs in debug |
| P3 | Create the upload keystore; fill `signing.*` in `secrets.properties`; back up the keystore and passwords in two safe places | M1 | Use Play App Signing; losing the upload key is recoverable, losing everything is not |
| P4a | Fix `supportEmail` in `config/app_config.json`: it is `support.permitprep@gmail.com` (another app's address); use an EngPet address | M1 | Same address goes in the privacy policy and Play listing |
| P4 | Host the privacy policy (Claude writes it) at a public URL, e.g. GitHub Pages or Google Sites; put the URL in `config/app_config.json` `privacyPolicyUrl` | M1 | Required for apps with ads |
| P5 | Choose the final app name and approve the icon (no exam names or logos) | M1 | Claude proposes 5 names with a trademark search |
| P6 | Fill Play Console forms: Data safety, content rating, target audience 13+, ads declaration (Claude drafts every answer) | M2 | |
| P7 | Recruit 12+ closed testers (friends, classmates, a Facebook group) and keep them for 14 days | M2 | Claude writes the invite message and a feedback form |
| P8 | Test each build on the phone with the checklist Claude sends; report what looks wrong with a screenshot | Every build | |
| P9 | Optional: ask a lawyer to confirm the exam-content rules in `EXAM_DATA_PLAN.md` §4 | M3 | |

## Claude tasks

| # | Task | Before |
|---|---|---|
| A1 | Words: the 808 missing TOEIC words and the 76 search-only ones (≈6 batches) | M2 |
| A2 | Words: CEFR A1–B1 missing words (THPT, KET, PET), then B2 (VSTEP); retire archaic group-C words | M4 |
| A3 | ✅ Topics: phrasal verbs 57, life stages 62 (batch 22); smallest topic now 42 | M1 |
| A4 | THPT: verify the format against the MOET sample paper, add the `sections` format entry, write papers 1–2 (M1) and 3–5 (M2), with vi explanations | M2 |
| A5 | TOEIC: move P5/P6 to a bank on the engine; 300 P5 items and 40 P6 texts | M2 |
| A6 | Stories to 30 (Level 1–3), pictures to 500 | M2 |
| A7 | ✅ `docs/store/privacy-policy.md` (+ `.vi.md`), `docs/store/play-console-answers.md`; in-app `privacy_text` updated | M1 |
| A8 | Store listing EN + VI: short and full description, feature list, trademark notice, 5 app-name options, screenshot captions | M2 |
| A9 | ✅ `docs/store/device-test-checklist.md`, `docs/store/tester-invite.md` (release notes per build later) | M1 |
| A10 | Review every Codex commit before push; keep this roadmap and the release checklist current | Always |

## Codex tasks

Specs for 14.1 and 15 are in `docs/ROADMAP.md`. New tasks below; take them in order.

### Task R2: Full licence texts

Add the Google `oss-licenses` plugin (or an equivalent offline screen) so every bundled library
shows its full licence text, and add the full Apache-2.0 text for Noto Emoji to the Licences
screen. No network. Report the APK size change.

### Task R3: Release hardening

1. `bundleRelease` produces a signed AAB when `signing.*` exists, and fails with a clear message
   when release AdMob IDs are missing (already partly done — verify).
2. R8: run the release build through every screen in an instrumented smoke test (open each tab,
   start and finish one lesson, open a mock test, open the licences screen). It must not crash.
3. Baseline profile for start-up and the Words tab (Macrobenchmark module) if it fits in the
   project without new network SDKs.
4. StrictMode in debug: no disk or network work on the main thread during start-up.
5. Report: APK/AAB size, cold start time on the benchmark build, crash-free smoke test.

### Task 16: Backup and restore learning data

Settings → "Back up progress" writes one JSON file (learning items, history, pet, settings,
quests) through the system file picker (`ACTION_CREATE_DOCUMENT`); "Restore" reads it
(`ACTION_OPEN_DOCUMENT`), shows what will be replaced, and asks to confirm. Version the file
format; reject files from a newer version with a clear message. No permissions, no network.
Unit tests for round trip and for an old-version file. Strings delivered: `backup_title`,
`backup_desc`, `backup_done`, `backup_failed`, `restore_title`, `restore_desc`,
`restore_confirm_title`, `restore_confirm_msg` (date, words, tests, pet level), `restore_replace`,
`restore_done`, `restore_bad_file`, `restore_newer_version`.

### Task 17: Store screenshots

An instrumented test that opens the 6 key screens (Today with pet, a picture exercise, a
Vietnamese explanation, the exam screen, a story, the pet room) with demo data (`DemoMode`) and
saves 1080×1920 PNGs in light and dark, English and Vietnamese, to `dist/screenshots/`. No status
bar clutter (demo mode clock). Claude writes the captions.

### Task 18 (after M3): Exam listening with TTS

Item type `listen_mcq` for banks: the stem is spoken by the device TTS (voice and speed from the
item, replay button, max 2 plays in timed mode), options shown as text. Used later for TOEIC
Part 2 style and Key listening Part 1 style. Owner provides the scripts.

## Order

Codex: 14.1 → 15 → R2 → R3 → 16 → 17 → (after M3) 18.
Claude: A3, A7, A9 first (they unblock M1), then A1/A4/A5 in parallel batches, then A6, A8.
Publisher: P1–P5 now (they take days to clear), P6–P7 at M2, P8 every build.
