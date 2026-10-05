---
name: english-learning-app
description: Design and build a lightweight, offline, ad-supported (AdMob) Android English-learning app in Kotlin (XML + ViewBinding), Vietnamese-first and expandable to many countries. Learning-first product (vocabulary, grammar ladder from easy to hard, exam-format practice such as TOEIC Reading Part 5/6, custom topic practice builder, mock tests) with a FLOATING PET COMPANION visible on every tab that shows speech bubbles (hungry, hi, full, sleepy, cheering) and opens a Pet Home when tapped. Contains detailed screen-by-screen specs, exercise catalogue, curriculum seeds, animation and correct/incorrect effects, state machines, content schemas, ad placement, performance budgets, QA and phased execution plan. Use whenever the task mentions English learning app screens, learning app UI/UX, floating mascot/pet overlay, vocabulary or grammar practice UX, exam-prep practice modes, practice builder by topic, correct/wrong feedback animations, or building the app in phases — even if the user does not say "skill".
---

# English Learning App (learning-first, floating pet companion) — Agent Skill

Works for Claude Code, Codex or any coding agent. Read this whole file before writing code, then follow the phases in section 17 and stop at each gate.

**This skill supersedes the earlier `english-pet-app` skill where they conflict.** Key change: the app is
**learning-first**. The pet is **not a tab and not the home screen**. It is a small **floating companion shown on every
tab**; it speaks short bubbles ("Đói quá", "Hi!", "No rồi", "Buồn ngủ"...) and **tapping it opens the Pet Home**.

Product promise: *"Ôn đúng chỗ yếu, học như chơi."*

---

## 0. Non-negotiable rules

1. **Learning first.** The main UI is lessons, practice and review. The pet motivates; it never blocks learning.
2. **Pet growth comes only from real learning** (correct answers, genuine reviews), with anti-farming caps (section 11).
3. **Offline-first.** No backend, no accounts. Only network use: AdMob, UMP, Play Billing, Play Asset Delivery.
4. **Original or properly licensed content only.** Record every external source in `content/LICENSES.md`. Never copy real exam items, prep books or other apps.
5. **Trademarks:** "TOEIC", "TOEFL", "IELTS" belong to others. Use descriptively only ("luyện theo định dạng TOEIC Part 5"), never in app name/icon, always show the independence disclaimer. **Never claim official score conversion**; show an "estimated level" labelled as an estimate.
6. **Exam format facts** (number of questions, time limits, part structure) must come from `config/exam_formats.json`, filled by a human from official public sources. If a value is `null`, label the mode generically ("Bài thi thử") and do not claim it mirrors a real test.
7. **No manipulative design:** no guilt messaging, no fake urgency, no blocking learning behind ads, always a free recovery path.
8. **Ads never interrupt answering**; test ad IDs only in debug/committed files.
9. **Hard size/performance budgets** (section 14) fail the build.
10. **Target audience 13+** in Play Console.
11. **No system overlay permission.** The floating pet is an in-app view (a child of the activity root), never `SYSTEM_ALERT_WINDOW`.
12. Do not add a dependency without justifying its size cost in the commit message.

---

## 1. Product concept and information architecture

### 1.1 Three ways to study (the user can enter any of them from Home)

1. **By content:** Vocabulary (topics, levels, word families, confusables, collocations) and Grammar (structure ladder easy → hard).
2. **By exam format:** Practice by part/question type (e.g., Part 5 POS questions), by topic, mini tests, mock tests.
3. **By weakness:** automatic "Luyện điểm yếu" built from the user's error tags.

### 1.2 Navigation map

Bottom navigation, 5 tabs (Vietnamese labels):

| Tab | Label | Purpose |
|---|---|---|
| 1 | **Hôm nay** | Home dashboard: today's plan, continue, due reviews, goal ring |
| 2 | **Từ vựng** | Topics, levels, my words, word detail |
| 3 | **Ngữ pháp** | Grammar ladder, grammar point lessons |
| 4 | **Luyện thi** | Exam formats, practice builder, mini/mock tests, mistake book |
| 5 | **Tôi** | Progress, achievements, settings, exam goal |

Not tabs: **Session screen**, **Result screen**, **Mock test**, **Word detail**, **Grammar lesson**, **Pet Home**, **Search**, **Settings**. Pet Home is opened by tapping the floating pet (or a card on Hôm nay).

Global elements: top app bar with streak/week badge and a search icon on tabs 1–4; **floating pet** (section 5) on tabs 1–5.

### 1.3 First-run flow (≤ 60 seconds, skippable)

Splash (≤ 600 ms, no logo animation) → **Onboarding** (3 short steps, all skippable) → **first question within seconds** (a 5-question "welcome" session). Placement test is offered later inside the first week, never forced.

---

## 2. Inputs and outputs

### Inputs
| Input | Path | Notes |
|---|---|---|
| App config | `config/app_config.json` | package id, flags, ad caps, pet params, budgets |
| Exam formats | `config/exam_formats.json` | part names, question counts, time limits — human-filled, `null` if unknown |
| Content (English) | `content/en/*.json` | words, families, confusables, grammar points, questions (section 12) |
| Localization | `content/i18n/vi.json`, `res/values/strings.xml` (vi default) | glosses, tips, explanations, pet lines |
| Pet art | `art/pets/<species>/` | layered vectors (section 5.6); Codex generates only **marked placeholders** if missing |
| Sources & licenses | `content/LICENSES.md` | required for every external list |
| Secrets | `secrets.properties` (git-ignored) | AdMob IDs, signing |

### Outputs
`app-release.aab`, `app-debug.apk`, `size-report.md`, `content-validation-report.md`, `content-coverage-report.md`, `listing/<locale>/`, `privacy-policy-<locale>.md`, `data-safety.md`, `qa-checklist.md`, `screens-checklist.md` (every screen in section 6 with done/not-done and screenshots).

---

## 3. Tech stack and architecture (size-driven)

- Kotlin, Gradle Kotlin DSL, version catalog, latest stable AGP/Kotlin. minSdk 24, targetSdk = Play's current requirement.
- **XML + ViewBinding + AppCompat.** No Jetpack Compose, no Lottie. Custom `View`s with `Canvas` for particles, charts and the pet.
- Single `MainActivity` hosting fragments via `FragmentManager` (no heavy navigation library). Root layout is a `FrameLayout`: `[content container][bottom nav][PetFloatingView][overlay layer for dialogs/level-up]`.
- Light MVVM (`ViewModel` + `StateFlow`), manual DI (`ServiceLocator`).
- Persistence: **DataStore** for settings/pet; **plain SQLite (`SQLiteOpenHelper`)** for per-item learning state; **no Room**.
- JSON: `org.json`. **No** Gson/Moshi/kotlinx.serialization.
- Images: VectorDrawable, AnimatedVectorDrawable, WebP. No PNG sprites, no custom fonts.
- Allowed SDKs: Google Mobile Ads, UMP, Play Billing, Play Asset Delivery. **Forbidden in v1:** Firebase, Crashlytics, third-party analytics, Glide/Coil, Retrofit/OkHttp.
- Auto Backup enabled for DB + DataStore (declare in Data safety).
- Pure-Kotlin cores with injected `Clock`: `Scheduler`, `SessionBuilder`, `PetEngine`, `PetMoodResolver`, `BubbleScheduler`, `AdPolicy`.

```
app/src/main/java/com/yourbrand/englishlearn/
 ├─ core/ (ServiceLocator, Clock, TextNormalizer, Haptics, Sfx)
 ├─ content/ (ContentRepository, PackManager, model/)
 ├─ learning/ (ItemStateStore, Scheduler, SessionBuilder, ExerciseFactory, Grader, WeaknessAnalyzer, PracticeBuilder, MockTestEngine)
 ├─ pet/ (PetEngine, PetState, PetMoodResolver, BubbleScheduler, PetFloatingView, PetHomeFragment, art/)
 ├─ fx/ (ParticleView, ConfettiEmitter, XpFlyAnimator, ShakeAnimator, StarBurstView, CountUpTextView)
 ├─ ads/ billing/ notify/ tts/
 └─ ui/ (today, vocab, grammar, exam, me, session, result, mock, search, settings, common)
```

---

## 4. Design system

### 4.1 Color tokens (light / dark)

| Token | Light | Dark |
|---|---|---|
| `bg` | `#FFF8F0` | `#14120F` |
| `surface` | `#FFFFFF` | `#1E1B17` |
| `surfaceVariant` | `#F6EBDD` | `#2A251F` |
| `onSurface` | `#2B2118` | `#F2E9DF` |
| `onSurfaceMuted` | `#6F6254` | `#B4A797` |
| `primary` | `#12857A` | `#4FD1C2` |
| `onPrimary` | `#FFFFFF` | `#06201C` |
| `accent` | `#F5A623` | `#FFC85C` |
| `success` / `successContainer` | `#2E9E5B` / `#E3F6EB` | `#58D68D` / `#143222` |
| `error` / `errorContainer` | `#D64545` / `#FDEAEA` | `#FF8A8A` / `#3A1818` |
| `info` / `infoContainer` | `#3B7DD8` / `#E6EFFB` | `#7FB0F5` / `#16263D` |
| `outline` | `#E4D6C3` | `#3A332A` |
Topic cards use 10 curated hues (each with container variant). All text/background pairs ≥ 4.5:1 (unit-tested).

### 4.2 Typography (system Roboto, full Vietnamese diacritics)
Title 24 bold · Section 18 semibold · Question 20 medium (user scale ×1.0/1.25/1.5) · Body 16 · Caption 13. **Line height ≥ 1.45** (1.5 for questions) so stacked diacritics are not clipped. All Vietnamese text normalized to **NFC**.

### 4.3 Layout
8dp grid; screen padding 16dp; card radius 16dp; primary button 52dp high, radius 14dp; touch targets ≥ 48dp; tablets ≥ 600dp: centered content max 640dp; edge-to-edge with insets; use `start/end` (RTL-ready).

### 4.4 Core components

| Component | Behavior |
|---|---|
| `PrimaryButton` / `SecondaryButton` | Press scale 0.97 (80 ms), disabled state 38% alpha, ripple |
| `OptionCard` (answer choice) | States: default · selected (primary outline 2dp) · correct (success fill + check) · wrong (error fill + cross) · dimmed · disabled. Left letter chip A–D. |
| `SegmentedProgress` | One segment per question, fills smoothly (200 ms), current segment pulses subtly |
| `XpChip` | "+5 XP", used for fly-to-pet animation |
| `StreakBadge` | Flame icon + number; weekly mode shows 7 dots |
| `TagChip` | Skill/trap tag (tap → tag practice) |
| `TopicCard` | Icon, name, progress ring, "N/M từ" |
| `LevelNode` | Grammar ladder node: locked/available/in-progress/mastered (stars 0–3) |
| `BottomSheet` | Draggable, scrim 40%, used for filters, palette, hints |
| `EmptyState` | Vector illustration + one line + one action |
| `Snackbar` | Bottom, above nav, 3 s |

### 4.5 Icon set (vector 24dp, stroke 2dp, rounded caps; ~45 icons)
Navigation: today, book(vocab), structure(grammar), target(exam), person. Actions: search, close, back, check, cross, bookmark(saved), bookmark-filled, speaker, speaker-slow, flag, hint(bulb), skip, refresh, settings, share(feedback), info, lock, star, star-filled, flame, clock, calendar, trophy, chart, filter, sort, list, grid, play, pause, mic(reserved), heart(reserved), coin(reserved), gift, bell, moon, sun, food-bowl, paw. Exam-type icons: sentence, paragraph, listening(reserved), reading(reserved).
Pet-related mini icons: `zzz`, `food_bubble`, `heart_pop`, `sparkle`, `sweat_drop`.

---

## 5. Floating pet companion (on every tab)

### 5.1 Component: `PetFloatingView`
- Child of the activity root `FrameLayout`, above fragment content and bottom nav, **below** dialogs, bottom sheets, full-screen ads, level-up overlay.
- **Sizes:** S 48dp / M 56dp (default) / L 72dp (Settings → "Kích thước thú cưng"). Touch target always ≥ 48dp.
- **Default anchor:** bottom-end, 12dp above the bottom navigation bar.
- **Draggable:** long-press (350 ms) then drag; snaps to the nearest horizontal edge on release (spring 250 ms); vertical clamped inside safe area and above the nav bar; position persisted per orientation. A single tap opens **Pet Home**.
- **Avoidance rules** (evaluated on layout change):
  - If it would overlap a view tagged `petAvoid` (primary CTA, banner ad container, input field), it slides to the opposite edge or up by the overlapped height.
  - Hidden while the soft keyboard is open.
  - On tablets, anchored to the content column edge, not the screen edge.
- **Session screen & mock test:** the floating view switches to **mini mode** (28–32dp icon beside the progress bar, not draggable, no text bubbles, only emoji-like reactions: ✓ cheer, oops, thinking). It never covers answer options.
- **Pet Home itself:** the floating view is hidden (the pet is already on screen).
- Settings: Show/Hide pet; "Chỉ biểu tượng (không lời thoại)"; size; reset position. Hiding the floating pet does not disable the pet system; Pet Home stays reachable from a card on Hôm nay.
- Rendering: layered vector drawable + a face overlay (section 5.6); idle animation (blink every 3–5 s, breathing 2.4 s loop) via `AnimatedVectorDrawable`; **pause all animations when the app is in background, the screen is off, or reduce-motion is on**. Use `translationX/Y`, never re-layout during drag.
- Accessibility: `contentDescription` = current mood in words ("Thú cưng đang đói, nhấn để mở nhà thú cưng"); bubbles are `liveRegion=polite`.

### 5.2 Speech bubble
- Appears beside the pet (toward screen center), max **2 lines / ≤ 60 characters**, rounded card with tail, fade+scale in 150 ms, auto-hide after 4 s (6 s if font scale ≥ 1.3), fade out 150 ms.
- Tap bubble or pet → Pet Home. Swipe bubble away → dismiss. Long-press pet → quick menu: "Ẩn bong bóng hôm nay", "Ẩn thú cưng".
- **Throttling (`BubbleScheduler`):** at most 1 bubble per 20 minutes of foreground time; max 6 per day; never within 10 s after a user answer; never while a dialog/bottom sheet/ad is shown; never during sessions/mock tests; priority queue (below), dropping lower priority if throttled.
- Text variety: ≥ 8 variants per state in `i18n/vi.json`; never repeat the same line twice in a row; optional `{name}` (pet name) interpolation.
- Tone: warm, playful, **never guilt-tripping**. Forbidden: "Bé buồn vì bạn", "Bạn bỏ rơi bé", countdowns, alarming red.

### 5.3 Mood state machine (`PetMoodResolver`, pure Kotlin)

Inputs: `now`, `hungerLevel (0–100)`, `fullToday` (daily goal reached), `lastStudyAt`, local hour, `missedDays`, pending events.

| Mood (id) | Trigger | Priority | Floating icon | Example bubble (vi) |
|---|---|---|---|---|
| `HI` | First open of the day, or return after > 1 h away | 5 | Waving, happy eyes | "Hi! Hôm nay mình học gì nhỉ?" · "Chào bạn! Bé nhớ bạn quá!" |
| `HUNGRY` | `hungerLevel ≥ 60` and awake hours | 4 | Rubbing tummy, bowl | "Đói quá, bé muốn ăn từ mới!" · "Ôn 5 câu cho bé ăn nhé?" |
| `STARVING` (still gentle) | `hungerLevel ≥ 85` | 4 | Floppy ears, tiny sweat drop | "Bụng kêu ọc ọc rồi nè…" |
| `FULL` ("no") | Daily goal ≥ 100% **or** just finished a session and hunger ≤ 10 | 3 | Round belly, content face | "No rồi! Cảm ơn bạn nhiều!" · "No căng bụng luôn!" |
| `SLEEPY` | Local time 21:30–22:00 | 2 | Yawn, half-closed eyes | "Buồn ngủ quá… ôn nhanh vài câu rồi ngủ nhé?" |
| `SLEEPING` (night, cosmetic) | Local time 22:00–07:00 | 2 | Eyes closed, `zzz` | No bubble; tap → grumpy-cute: "Hừm… cho bé ngủ thêm xíu…" then opens Pet Home. **No penalty.** |
| `DOZING` (neglect, recoverable) | `missedDays ≥ 1` after grace (section 11) | 6 | Head drooping, `zzz` | "Bé ngủ gật rồi… ôn 5 câu để đánh thức bé nhé." |
| `CELEBRATE` | Stage up, goal reached, new streak milestone | 7 | Jumping, sparkles | "Hoan hô! Bé lớn lên rồi!" |
| `CHEER` / `OOPS` / `THINKING` | Session events (mini mode only) | — | Small reactions | (no text) |

Rules: the night `SLEEPING` mood is **clock-based and cosmetic** (no growth effects). `DOZING` is the neglect state tied to decay and the recovery flow. If both apply, `DOZING` wins for the icon; text mentions recovery.

### 5.4 Hunger model (cosmetic, drives mood only)
- `hungerLevel` rises 1 point per 30 minutes **of awake time** (07:00–22:00 local) since the last "meal" (completed session or ≥ 5 correct answers); max 100.
- A meal reduces hunger by `min(hunger, 40)`; completing the daily goal sets hunger ≤ 10.
- Hunger **never** reduces XP or stage. It only changes mood and bubble text.

### 5.5 Pet Home (full screen, see S20): the destination when the pet or bubble is tapped.

### 5.6 Pet art specification (layered, to keep size tiny)
Per species `<s>` and stage `<n>`:
- `pet_<s>_s<n>_body.xml` (vector body, ≤ 15 KB)
- Shared face overlays per species: `face_<s>_{idle,happy,hungry,starving,full,sleepy,sleeping,dozing,cheer,oops,thinking}.xml` (each ≤ 2 KB), anchored by named pivots `eye_l`, `eye_r`, `mouth`.
- Effect overlays: `fx_zzz`, `fx_bowl`, `fx_hearts`, `fx_sparkles`, `fx_sweat` (≤ 1 KB each).
- Floating icon uses a head-and-shoulders crop of the body for legibility at 48–72dp (`pet_<s>_s<n>_icon.xml`).
- Species are cosmetic (default 3: cat, dog, dragon). **Original art only**; do not imitate existing franchises.
- If art is missing the build uses neutral placeholders **labelled "PLACEHOLDER"** and `screens-checklist.md` lists the missing assets.

---

## 6. Screen specifications

Each screen lists: purpose · layout (top → bottom) · interactions · states · animations · ads.
Common states for every list screen: **empty** (EmptyState with action), **content**, **error** (only for pack-loading; retry button). Content is local, so there are no spinners except for on-demand pack downloads.

### S01 Splash
Solid `bg` color, small app glyph, ≤ 600 ms, no animation beyond a 150 ms fade. Starts parsing the content index on a background thread.

### S02 Onboarding (3 steps, each skippable, progress dots)
1. **Mục tiêu:** multi-select cards (Thi TOEIC, Thi IELTS, Giao tiếp, Công việc, Du lịch, Nền tảng). Stored as `goals[]`.
2. **Trình độ & thời gian:** segmented level picker (Mới bắt đầu / Cơ bản / Trung cấp / Khá) with "Làm bài test nhanh sau", daily time (5/10/15 phút).
3. **Chọn thú cưng:** 3 species cards with idle animation; name field (default suggestions); a sentence explaining "Bé sẽ đồng hành cùng bạn".
Ends with "Bắt đầu học" → welcome session (5 easy questions). No notification permission here.
Animations: step slide 250 ms; selected card scales 1.03 + check morph.

### S03 Hôm nay (Home dashboard)
Layout:
1. App bar: greeting ("Chào buổi sáng!"), `StreakBadge`, search icon.
2. **Daily goal card:** ring (XP or minutes), "Còn 40 XP nữa là đủ mục tiêu", primary **"Học 5 phút"** button.
3. **Tiếp tục** card: resumes unfinished session or last lesson ("Ngữ pháp: Thì hiện tại hoàn thành — 60%").
4. **Cần ôn hôm nay:** count of due items + "Ôn ngay" (spaced repetition session).
5. **Luyện điểm yếu:** top 3 weak tags as `TagChip`s + button.
6. **Lộ trình hôm nay:** 3 checklist items generated from goals (e.g., "10 từ mới chủ đề Văn phòng", "1 điểm ngữ pháp", "5 câu Part 5") with checkmarks that animate when done.
7. **Đếm ngược kỳ thi** (only if the user set an exam date): days left + suggested focus.
8. **Pet teaser card** (small, only when the floating pet is hidden): "Thú cưng của bạn đang đói" → Pet Home.
9. Banner ad (adaptive) at the bottom, inside content, above nav.
Animations: cards fade-up stagger 40 ms; goal ring animates from previous to new value 500 ms; checklist check draws in 200 ms.
Interactions: pull-to-refresh not needed.

### S04 Từ vựng (hub)
Top tabs: **Chủ đề · Cấp độ · Của tôi**.
- **Chủ đề:** 2-column `TopicCard` grid (≈ 20 topics, section 8.1), each with progress ring and "N/M từ thuộc". Sticky search field.
- **Cấp độ:** 5 level rows (Level 1 → 5) with progress bars; tapping opens the word list for that level.
- **Của tôi:** sub-chips *Đã lưu*, *Hay sai*, *Đang học*, *Đã thuộc*; each a word list.
Banner ad at bottom. Pet floats above banner.

### S05 Chủ đề / Danh sách từ (topic word list)
- Header: topic name, progress ring, **Học chủ đề** primary button (starts a session from this topic) and a mode menu (Flashcard, Trắc nghiệm, Nối cặp, Gõ từ, Nghe & chọn).
- Filter chips: *Mới · Đang học · Đã thuộc · Đã lưu*; sort by (A–Z, khó → dễ, hay sai).
- List item: lemma, POS tag, gloss (vi), mastery dots (0–5), speaker icon, bookmark icon. Tap → S06.
- Sticky bottom bar when ≥ 1 word selected (long-press to multi-select): "Luyện các từ đã chọn".

### S06 Chi tiết từ (Word detail)
Layout: big lemma + IPA + speaker (normal & slow); POS + level + topic chips; gloss (vi) large; **ví dụ** (2–3 sentences, tappable words, TTS per sentence, vi translation toggle); **Họ từ** (noun/verb/adj/adv forms as chips with POS color); **Dễ nhầm với** (confusable cards linking to S07); **Cụm từ đi kèm**; **Mẹo nhớ** (mnemonic card, vi); mastery bar; buttons: Lưu, Luyện từ này (3 quick exercises), Báo lỗi (opens `mailto:` with word id).
Animations: speaker icon waves while TTS speaks; mastery bar fill; chips fade in.

### S07 Từ dễ nhầm (Confusable set)
Side-by-side comparison cards (e.g., affect vs effect) with: meaning, POS, example, "mẹo phân biệt". Footer: **Luyện bộ này** (cloze items using both words). Accessible from S06 and from a section under Từ vựng → Của tôi → Hay sai.

### S08 Ngữ pháp (hub): the structure ladder, easy → hard
Vertical "path" of **5 levels** (section 8.2), each level a header with progress and a list of `LevelNode`s (grammar points). Node states: locked (only the *next* node after a mastered node is unlocked; any node can be unlocked early by passing a 5-question skip test) · available · in progress · mastered (1–3 stars).
Filters: *Tất cả · Dành cho TOEIC · Dễ nhầm*. Search. Banner ad at bottom.
Animations: when a node becomes mastered, a star burst and the path line to the next node "draws" in 400 ms.

### S09 Bài ngữ pháp (Grammar lesson), 4-step stepper
1. **Hiểu:** short explanation (≤ 80 words vi), formula card (e.g., `S + have/has + V3`), time markers/signals, "Khi nào dùng".
2. **Xem:** 3 annotated examples (key parts color-highlighted, tap to hear).
3. **Cẩn thận:** "Lỗi hay gặp" cards (traps; includes Vietnamese-interference tips), 2 contrast pairs (đúng/sai).
4. **Luyện:** 8–10 drills (progressive: recognize → complete → fix error → build sentence). Passing (≥ 80%) grants stars.
Bottom bar: Back / Next; progress dots; "Luyện thêm" after completion. No banner inside the lesson.

### S10 Luyện thi (hub)
Sections (vertical):
1. **Chọn kỳ thi / dạng:** horizontally scrollable cards from `exam_formats.json` (e.g., "Đọc hiểu — Part 5", "Part 6"; future packs shown as "Sắp ra mắt"/"Tải gói").
2. **Luyện theo dạng bài** → S11.
3. **Tạo bài luyện riêng** → S12 (practice builder).
4. **Bài thi nhỏ & thi thử** → S13.
5. **Sổ lỗi sai** → S15.
6. **Lịch sử bài làm** (last 10, with score, date, duration).
Banner ad at bottom of this hub only.

### S11 Luyện theo dạng bài (Question-type practice)
List of question types for the chosen part, each with count, accuracy, mastery, and a "Luyện" button. Part 5 example types (labels, content-driven): *Từ loại (POS)*, *Thì & dạng động từ*, *Giới từ*, *Liên từ vs giới từ*, *Đại từ*, *Từ vựng theo ngữ cảnh*, *So sánh*, *Mạo từ/Lượng từ*, *Cụm cố định*. Part 6 example types: *Điền từ trong đoạn*, *Điền câu vào chỗ trống*, *Từ nối*, *Thì xuyên suốt đoạn*.
Tapping a type opens a short **tips card** ("Mẹo nhận diện") then starts a 10-question session. Difficulty picker: Dễ / Vừa / Khó / Hỗn hợp.

### S12 Tạo bài luyện riêng (Practice builder), the "ôn theo chủ đề tôi muốn"
A single scrollable form with a live count at the bottom ("124 câu phù hợp"):
- **Dạng:** multi-select (Part 5, Part 6, Từ vựng, Ngữ pháp, Hỗn hợp).
- **Chủ đề:** multi-select chips (section 8.1) — e.g., Tài chính, Du lịch, Nhân sự.
- **Kỹ năng/điểm ngữ pháp:** multi-select tags (POS, tense, preposition…).
- **Độ khó:** range slider 1–5, or "Tự động theo trình độ".
- **Nguồn:** Mới / Đã sai / Đã lưu / Tất cả.
- **Số câu:** 10 / 20 / 30 / 50. **Chế độ:** Luyện tập (có giải thích ngay) / Tính giờ.
- Button **"Bắt đầu"**; secondary "Lưu mẫu này" (saves up to 5 presets, shown as chips on top).
If the filter matches < 5 items: inline message with a "Nới lỏng bộ lọc" button. Logic: `PracticeBuilder` (section 9).

### S13 Bài thi nhỏ & thi thử
Cards: **Mini test** (10 / 20 câu, auto-picked from the selected format), **Test từng Part** (length from `exam_formats.json`; if `null` show "Bài thi thử"), **Test tổng hợp** (when formats allow). Each card shows time (if defined), last score, best score. Start → S14.

### S14 Màn làm bài thi thử (Mock test)
- Top: close (confirm dialog), question counter "12/30", **timer** (if time limit defined; turns `warning` color and pulses at the last 10% / last 60 s).
- Body: question (cloze/text completion with passage scroll), options. **No feedback until submission.**
- Bottom: Prev / Next, **flag** icon, **palette button** → bottom sheet grid of question numbers (answered / flagged / blank) ; **Nộp bài** on the last question and in the palette sheet, with confirm ("Còn 3 câu chưa làm").
- No banner, no pet bubbles (mini mode only), no ads until finished.
- State saved on every answer so process death resumes.
- On time up: auto-submit with a short toast.

### S15 Sổ lỗi sai (Mistake book)
Grouped by **tag** (Từ loại, Thì…, Giới từ…) with counts; each group expands to items (stem, your answer vs correct, date). Actions: **Làm lại nhóm này**, remove item when mastered, filter by date. Items leave the book after 2 consecutive correct reviews.

### S16 Session screen (shared by all practice modes)
Layout:
- **Top bar:** close (confirm on > 30% progress), `SegmentedProgress`, mini pet icon (mode state), optional timer chip.
- **Prompt area:** instruction label ("Chọn đáp án đúng"), main content (varies by exercise, section 7), speaker button when audio is available.
- **Answer area:** per exercise.
- **Bottom action:** **Kiểm tra** (disabled until an answer is chosen) → after checking becomes **Tiếp tục**. Hint (💡) available once per question after 8 s idle: reveals POS clue or eliminates one option (costs 0 XP bonus for that item, not a penalty).
- **Feedback panel** (bottom sheet-like, slides up 220 ms): correct/incorrect title, correct answer, vi explanation (≤ 40 words), `TagChip`s for skill/trap, speaker for the sentence, **Lưu** (bookmark), **Báo lỗi**.
- Keyboard-based exercises: panel moves above the keyboard; pet hidden.
- Re-queue: wrong items reappear later in the same session (after 3–4 other items), once.
- No banner/pet bubbles in session.
Animations/effects: section 10.

### S17 Kết quả phiên (Session result)
Sequence (total ≈ 2.2 s, tap to skip to final state): header "Hoàn thành!"; **stars (1–3)** pop in sequentially (150 ms stagger); **accuracy ring** and **XP count-up** (600 ms); "Bé đã ăn no!" with the pet icon reacting (if goal reached, `FULL`); **Từ mới thuộc** carousel; **Cần luyện thêm** (top weak tags); buttons: **Học tiếp**, **Ôn lại câu sai**, **Về trang chủ**. Confetti if accuracy ≥ 80% (60 particles max). Interstitial is shown **before** this screen only when `AdPolicy` allows. Rewarded: "Nhận gấp đôi XP (xem quảng cáo)" optional, never blocking.

### S18 Kết quả bài thi thử (Mock result)
Score summary (correct/total, **time used**), **ước lượng trình độ** (clearly labelled "ước lượng, không phải điểm thi chính thức"), breakdown by skill/tag (bars), breakdown by question type, **Xem lại từng câu** (filter: sai / đã flag / tất cả) with explanations, "Luyện điểm yếu" CTA, and comparison to the previous attempt (↑↓).

### S19 Tôi (Progress & profile)
- **Tổng quan:** level estimate chip, streak, weekly goal dots, total mastered words/grammar points.
- **Biểu đồ 14 ngày** (Canvas bars: minutes or XP), tap a bar for details.
- **Lịch hoạt động** (heatmap of last 8 weeks).
- **Điểm yếu của bạn:** horizontal bars by tag with "Luyện" buttons.
- **Thành tựu:** grid of badges (locked/unlocked with a shine animation on unlock).
- **Mục tiêu thi:** exam type + date (drives the countdown on S03).
- Entry to **Cài đặt** (S21).

### S20 Nhà thú cưng (Pet Home)
Opened by tapping the floating pet/bubble or the teaser card. Not a tab.
Layout:
1. Scene area (≈ 55% height): stage-specific background, the pet at large size with mood animation, speech bubble area, day/night tint following local time (night = dim, `zzz`).
2. **Stage card:** name (editable via long-press → dialog), stage "Cấp 3 / 10", growth bar (XP in stage), tiny preview of next stage silhouette.
3. **No/đói meter:** a bowl icon with fill level based on `hungerLevel` and today's goal ring ("Bữa hôm nay: 2/3").
4. **Actions:**
   - **Cho ăn** → starts a 5-question quick review session (the "meal"). Primary button.
   - **Vuốt ve** (tap on the pet): heart-pop + happy sound, max 1 XP-free reaction per second.
   - **Thư viện hình dáng:** gallery of unlocked stages/backgrounds (locked ones as silhouettes).
5. **Khôi phục** card (only if `DOZING`): **Ôn nhanh 5 câu để đánh thức** (free) · **Xem quảng cáo để khôi phục ngay** (optional) · **Dùng thẻ đóng băng** (if owned). All options restore the same amount.
6. Close/back returns to the previous tab with the floating pet re-appearing by a small slide-in.
Animations: pet idle (breath/blink), feeding (food bowl bounce + chewing 3 cycles), stage-up (section 10), sleeping (breathing slow + floating `zzz`).
No banner here. Interstitials never open from here.

### S21 Cài đặt
Groups: **Học tập** (daily goal 5/10/15 phút, new words per day, show Vietnamese translation by default, TTS voice/speed, haptics, sound effects) · **Thú cưng** (show floating pet, bubbles on/off, size, reset position, rename, switch species only at stage 1) · **Thông báo** (opt-in reminder time, quiet hours) · **Giao diện** (theme, text size, reduce motion) · **Gói & mua** (Gỡ quảng cáo, Khôi phục mua hàng, Thẻ đóng băng) · **Quyền riêng tư** (UMP options, privacy policy) · **Dữ liệu** (backup info, reset progress with confirm) · **Về ứng dụng** (disclaimer, sources & licenses, version, contact/feedback).

### S22 Tìm kiếm
Search words/grammar points/topics; recent searches (local); results grouped by type; tapping a word → S06. Empty state suggests topics.

### S23 Dialogs & sheets (all use the same style)
Exit-session confirm · Hint sheet · Report-issue (mailto) · Notification permission rationale (shown only after the first completed session) · Reward-ad offer (clear label, "Không, cảm ơn") · Stage-up overlay (full-screen, skip on tap) · Placement-test prompt · Pack download sheet (size, progress, cancel) · Rate-app prompt (only after ≥ 3 sessions with ≥ 80% accuracy, once per 90 days, never after a wrong answer).

---

## 7. Exercise catalogue

All exercises are generated from the same content by `ExerciseFactory`. Every exercise defines: **prompt**, **input**, **grading**, **feedback**, **XP**.

| ID | Exercise | Input | Grading | Base XP | Used in |
|---|---|---|---|---|---|
| E01 | Multiple choice (meaning/usage) | tap option | exact index | 4 | vocab, grammar |
| E02 | **Cloze (Part 5 style)** | tap option, blank highlights and fills with the chosen word | exact index | 5 | exam, grammar |
| E03 | **Text completion (Part 6 style)** | passage with 2–4 blanks; tap blank → choose option sheet | per-blank | 6 per blank | exam |
| E04 | **Find the error** | tap the incorrect word/phrase in a sentence | tapped token = target | 6 | grammar, exam |
| E05 | Word order | tap word chips to build the sentence | normalized equality (alternative orders allowed via `acceptedOrders`) | 6 | grammar |
| E06 | Matching pairs | tap left/right to pair (word ↔ gloss, base ↔ derived form) | all pairs; errors shake only the tapped pair | 5 | vocab |
| E07 | Flashcard recall | flip card, self-rate **Lại / Khó / Được / Dễ** | rating maps to SRS | 3 | vocab |
| E08 | Spelling (typed) | text field, shows blanks with hint letters | normalized, 1 typo tolerance for words ≥ 7 letters (shows "gần đúng") | 5 | vocab |
| E09 | Listening-lite | hear a word (TTS) → choose spelling/meaning | exact | 4 | vocab |
| E10 | Dictation-lite | hear a short sentence → type it | token-level diff highlighting | 7 | vocab, grammar |
| E11 | Word formation | base word shown; choose/type correct form | exact | 6 | exam (word family) |
| E12 | Sentence transform (choose) | pick the sentence that keeps the meaning | exact | 6 | grammar |
| E13 | Speed round (60 s) | rapid MC | exact; combo multiplier | 3 per item, bonus-only XP | any |
| E14 | Confusable pick | two words side by side in a sentence | exact | 5 | vocab |

Grading details: normalize Unicode (NFC), trim, case-insensitive for typed input, strip trailing punctuation; for typed answers display a diff with the user's input. Time-based rules: no scoring on speed except E13.

Session mixing: ≥ 3 different exercise types per 10-item session; the first item is an easy win; the last is medium; never the same type more than 2 in a row.

---

## 8. Curriculum seeds (human-validated before shipping; Codex must mark `needs_review`)

### 8.1 Vocabulary topics (descriptive labels; v1 ~20)
Văn phòng & điều hành · Nhân sự & tuyển dụng · Tài chính & ngân hàng · Kế toán & thuế · Marketing & quảng cáo · Bán hàng & dịch vụ khách hàng · Hợp đồng & pháp lý · Sản xuất & kho vận · Mua sắm & thương mại · Du lịch & di chuyển · Khách sạn & nhà hàng · Sự kiện & hội nghị · Công nghệ & IT · Y tế & bảo hiểm · Bất động sản & nhà ở · Giáo dục & đào tạo · Môi trường & năng lượng · Truyền thông & báo chí · Đời sống hàng ngày · Học thuật cơ bản.
Level scale: Level 1–5 (≈ sơ cấp → nâng cao). **Do not claim equivalence** to CEFR/exam scores.

### 8.2 Grammar ladder (5 levels, easy → hard; each point has ≥ 20 items)
- **Level 1 — Nền tảng:** to be · present simple · articles a/an/the · plural nouns · basic prepositions (in/on/at) · there is/are · can/could · basic word order.
- **Level 2 — Cơ bản:** past simple · present continuous · comparatives/superlatives · countable/uncountable & quantifiers · will vs be going to · must/should/have to · possessives & pronoun cases.
- **Level 3 — Trung cấp:** present perfect (vs past simple) · passive voice · conditionals 0/1/2 · relative clauses (who/which/that) · gerund vs infinitive · conjunctions vs prepositions (although/despite…) · subject–verb agreement · word forms (noun/verb/adj/adv).
- **Level 4 — Khá:** past perfect · conditional 3 & mixed · reported speech · participle clauses · wish/if only · inversion (basic) · modal perfect (should have…) · linking & discourse markers.
- **Level 5 — Nâng cao:** complex noun phrases · subjunctive · cleft sentences · advanced inversion · ellipsis & substitution · nuance of modals/hedging · formal vs informal register.
**TOEIC-oriented overlay tag `exam_part5`:** POS identification, word formation, prepositions of time/place, tense markers, pronoun case, agreement, conjunction vs preposition, comparison, quantifiers.

### 8.3 Exam formats (`config/exam_formats.json`, human-filled)
```json
{
  "formats": [
    { "id": "toeic_r_p5", "label": "Đọc hiểu — Part 5", "exerciseTypes": ["E02","E11","E04"],
      "questionCount": null, "timeLimitMinutes": null, "optionsPerQuestion": 4, "source": "official-public-info-url-here" },
    { "id": "toeic_r_p6", "label": "Đọc hiểu — Part 6", "exerciseTypes": ["E03"],
      "questionCount": null, "timeLimitMinutes": null, "optionsPerQuestion": 4, "source": "…" }
  ]
}
```
Future formats (Part 7, Listening, IELTS, TOEFL) are separate **packs**; build extension points, ship none in v1.

---

## 9. Practice builder and session logic

### 9.1 Selection pipelines
- **Học nhanh (Today):** 60% due items · 25% weak-tag items (min 5 attempts per tag to count) · 15% new.
- **Luyện điểm yếu:** top-3 weak tags; items ordered by (wrong rate desc, due asc); difficulty ≈ user level − 0.5 to rebuild confidence.
- **Theo dạng bài (S11):** filter by `skills`/`examType`, difficulty selector; unseen first.
- **Practice builder (S12):** `PracticeBuilder.query(filters)` → candidate set → shuffle with weights (due and weak boosted) → take N, no repeated stems, ≥ 2 exercise types when the filter allows. Return `matchCount` live while the user edits filters (debounced 150 ms, indexed in-memory).
- **Mock tests:** stratified by `questionType` according to format config; no repeats from the user's last 2 mock tests when enough content exists.

### 9.2 Spaced repetition
Box schedule (tunable): box1 = same session + 10 min · box2 = 1 d · box3 = 3 d · box4 = 7 d · box5 = 16 d · box6 = 35 d (mastered when box ≥ 5 and last 2 correct). Correct → +1; wrong → max(1, box − 2) with `lapse` flag; ±10% jitter on `dueAt`.

### 9.3 Adaptive difficulty
Aim 70–85% accuracy. Rolling accuracy > 90% → raise level / harder distractors; < 60% → lower and show explanations first. Always explain wrong answers; never lock the user out.

### 9.4 Error tags
`skills`: `vocab_core`, `vocab_workplace`, `word_family`, `pos_transform`, `collocation`, `preposition`, `tense`, `conditional`, `agreement`, `comparison`, `relative_clause`, `confusable`, `conjunction`, `pronoun`, `article_quantifier`.
`trap`: `wrong_pos`, `similar_meaning`, `wrong_preposition`, `wrong_tense`, `spelling_lookalike`, `false_friend_vi` (Vietnamese-only, lives in the locale layer).
`WeaknessAnalyzer` ranks tags by smoothed wrong-rate: `(wrong + 1) / (attempts + 3)` with a minimum of 5 attempts.

---

## 10. Feedback, animation and effects

Global rules: durations 80–600 ms (celebrations up to 2.2 s, always skippable by tap); use `ViewPropertyAnimator`, `ValueAnimator`, `AnimatedVectorDrawable`, and one shared `ParticleView` (Canvas, object pool, ≤ 60 particles, no per-frame allocation). **Reduce motion** (system animator scale 0 or app setting): replace motion with instant state change + 100 ms alpha fade; confetti/shake disabled; haptics preserved unless disabled.

| Event | Visual | Sound / haptic | Pet reaction |
|---|---|---|---|
| Tap an option | Press scale 0.97 → selected outline | tiny tick (haptic `CLOCK_TICK`) | — |
| **Correct** | Option turns success green; check icon **morphs/draws in** (200 ms); card does a soft scale 1.0→1.04→1.0 (spring 220 ms); a green glow ring pulses once; feedback panel slides up; **"+5 XP" chip flies along a curved path (450 ms) toward the pet icon** and the XP bar ticks up; if combo ≥ 3, "x3" badge pops | soft "ding" (≤ 8 KB ogg), haptic `CONFIRM` | mini pet: `CHEER` (jump 250 ms) |
| **Incorrect** | Option turns soft red with cross icon; card **shakes** horizontally (3 cycles, 280 ms, amplitude 8dp); the correct option pulses green once and is highlighted; feedback panel explains | low soft "boop" (not a harsh buzzer), haptic `REJECT` | mini pet: `OOPS` (tilt 200 ms), no penalty |
| Almost-correct typed answer | Amber tint, diff highlight, "Gần đúng!" | gentle chime | `THINKING` |
| Combo (3/5/10 correct in a row) | Flame badge grows, brief sparkle burst | rising chime | cheer |
| Word mastered | Card flips (200 ms) to reveal a star + sparkles, appears in "Từ mới thuộc" | sparkle sound | cheer |
| Grammar node mastered | Star burst + path line draws to the next node (400 ms) | fanfare-lite | — |
| Session complete | Stars pop in sequence (150 ms stagger), accuracy ring sweeps, XP **counts up** (600 ms), confetti if ≥ 80% | success jingle (≤ 15 KB) | pet reacts (`FULL` if goal done) |
| **Stage up (pet)** | Dim scrim; pet glows (radial gradient pulse), light rays (Canvas) rotate, pet morphs to the new stage with a 300 ms cross-fade + scale pop; "Cấp {n}!" banner; confetti; tap to skip | fanfare, haptic `LONG_PRESS` | `CELEBRATE` |
| Streak milestone (3/7/30) | Flame ignites with scale + glow | chime | `CELEBRATE` bubble |
| Timer last 10 s (mock) | Timer text pulses (scale 1.0↔1.1, 500 ms) | none | — |
| Screen transitions | Tab switch: fade-through 200 ms; push: shared-axis (slide 24dp + fade) 250 ms; bottom sheets: 220 ms decelerate | — | — |
| Lists | Items fade-up with 40 ms stagger (first 8 only) | — | — |
| Progress rings/bars | Animate from previous to new value 400–500 ms, decelerate | — | — |
| Pet drag/snap | Lift scale 1.1 on long-press, spring snap 250 ms | haptic tick on pick-up | — |

Sound policy: **sound effects default ON at low volume only when the ringer is not silent/vibrate**; toggle in Settings; total SFX assets ≤ 120 KB; short Opus/OGG mono. Haptics follow the system setting and an in-app toggle.

Implementation notes: precompute `Path`s and `PathMeasure` for the XP flight; reuse `Paint`s; run particle updates in `Choreographer` frame callbacks only while visible; stop all animators in `onStop`.

---

## 11. Pet growth, decay and recovery (rules)

Consistent with the earlier design; values are tunable in `app_config.json`.
- **Stages:** 10; `stageCost` ≈ 100, 220, 380, 580, 820, 1100, 1450, 1850, 2300, 2800 XP.
- **XP from learning only.** Multipliers: ×1.5 for items due, ×1.5 for weak-tag items, ×0.5 for the same item already correct today; daily XP cap (default 200); max 2 rewarded repeats per item per day; no XP for implausibly fast answers (< 0.8 s) in patterns of random tapping; wrong answers 0 XP, never negative.
- **Grace period 24 h**, then lazy decay computed on app open/resume (no background services): each missed full day removes `dailyDecayPercentOfStage` (default 15%) of the **current stage's** cost from `stageProgressXp`, the lost amount is stored as `recoverableXp`.
- **Floor = start of the current stage.** The pet never drops to a previous stage and never dies.
- **Recovery (free path always exists):** (a) quick 5-question review wakes the pet and restores 100% of `recoverableXp`; (b) optional rewarded ad restores the same amount instantly; (c) freeze token cancels the latest missed-day decay. Earned: 1 token per completed weekly goal; extra tokens can be purchased.
- **Clock safety:** if `now < lastStudyAt` → treat elapsed as 0; track `SystemClock.elapsedRealtime` + boot count to detect large jumps; when suspicious, skip decay and award no bonus.
- Notifications: opt-in only, requested after the first completed session; ≤ 1/day; quiet hours 22:00–08:00; gentle copy.

---

## 12. Content schemas (condensed; write full `content/SCHEMAS.md`)

**Word** (`content/en/words.json`): `id, lemma, pos, ipa, level(1–5), topics[], freqBand, examples[{id,text}], family, confusableWith[], collocations[], tags[], audio, source`.
**Question**: `id, type (E02/E03/E04/E05/E11/E12/E14…), examFormat, questionType, stem or passage, options[], answer, skills[], trap[], level, topic, wordIds[], grammarPoint, acceptedOrders?, needs_review, source`.
**Grammar point**: `id, level, title key, formula, signals[], examples[], commonMistakes[], tags[], examOverlay?`.
**Localized layer** (`content/i18n/<locale>.json`): glosses, notes, mnemonics, example translations, explanations, tips, grammar bodies, pet lines (≥ 8 variants per mood), UI microcopy.
**Validator** (`tools/validate_content.py`, non-zero exit on error): unique ids; references resolve; exactly one correct answer; cloze has exactly one blank; length limits (stem ≤ 200, option ≤ 60, explanation ≤ 300); near-duplicate detection (trigram Jaccard > 0.85); NFC; every shipped word has a gloss and every question an explanation; balanced answer positions (20–30% each); per-topic and per-tag minimums; lists `needs_review` and `ai_draft`; **fails release if `ai_draft` remains**. AI may draft content, but every item needs human review.

v1 targets (tunable): 2,500–3,500 words · 250–400 confusable sets · 800–1,200 word families · 400–600 collocations · 40–60 grammar points × 20–30 items · 3,000–4,000 questions. **Depth over breadth.**

---

## 13. Ads and monetization

| Format | Where | Rules |
|---|---|---|
| Adaptive banner | Hôm nay, Từ vựng hub, Ngữ pháp hub, Luyện thi hub, Tôi | Placed inside content above the nav; **tagged `petAvoid`** so the pet moves away; none in Session, Mock, Lesson, Pet Home, Result |
| Interstitial | Between sessions (before S17) | ≥ 180 s apart, ≤ 3/day, never in the first 2 sessions, never on launch/exit, never after a wrong answer, never from Pet Home or Mock test |
| Rewarded | Pet recovery (optional), double XP on result, extra hint | Always user-initiated and clearly labelled; recovery always has a free equal option |
- UMP consent every launch; privacy options in Settings; AdMob initialized off the main thread after consent and after first frame; `AdPolicy` pure-Kotlin with unit tests; `verifyReleaseAds` fails release builds containing test IDs.
- IAP: **Gỡ quảng cáo** (non-consumable), **Thẻ đóng băng** (consumable); restore purchase in Settings; when ads are removed, AdMob is not initialized.
- Never promise revenue.

---

## 14. Performance and size budgets (enforced in CI)

| Metric | Target | Hard limit |
|---|---|---|
| Base download size (AAB estimate) | ≤ 12 MB | **25 MB** |
| Core content in base install | ≤ 3 MB | 6 MB |
| SFX total | ≤ 120 KB | 200 KB |
| Pet art per stage | ≤ 15 KB | 30 KB |
| Cold start to first frame | ≤ 800 ms | 1200 ms |
| Frames < 16 ms during session & animations | ≥ 95% | 90% |
| Floating pet overhead (idle) | < 1% CPU, 0 layout passes while idle | — |
| Memory | ≤ 100 MB | 150 MB |
Checklist: R8 full mode + shrinkResources; `localeFilters`; vectors/WebP only; optional audio/content packs through Play Asset Delivery; `bundletool get-size`; top-10 contributors in `size-report.md`; Baseline Profile; StrictMode in debug; defer ads/billing/TTS/consent initialization; `RecyclerView` + `DiffUtil`; flat layouts; batch SQLite writes; pause animations when invisible.

---

## 15. Accessibility and internationalization

- TalkBack labels for all controls (including pet state), logical focus order, 200% font scale safe, ≥ 48dp targets, no color-only feedback (icon + text), captions for sound cues, **reduce motion respected**.
- All UI strings in resources (Vietnamese default `values/`, English `values-en/`); no hard-coded or concatenated strings; plurals/format args; text expansion tolerant (≥ 40%); RTL-safe layouts.
- **Add a locale** with `tools/new_locale.py <code>`: scaffolds strings, `content/i18n/<code>.json` (every id present, `"todo": true`), listing and ASO sheet. Ship a locale only if UI is 100% translated and reviewed by a native speaker, ≥ 95% words glossed and 100% questions explained, listing localized, script/RTL rendering verified, and data (installs/retention/eCPM per country) justifies it.

---

## 16. Testing and QA

### Automated
- **Unit (JVM):** Scheduler; SessionBuilder (mix, no duplicates); PracticeBuilder (filters, counts, no-match handling); Grader (NFC, typo tolerance, diffs); WeaknessAnalyzer; **PetMoodResolver** (every row of section 5.3, priorities, night rules, hunger model); **BubbleScheduler** (throttle, daily cap, quiet during sessions/dialogs, no repeats); **PetEngine** (grace, decay, floor, no stage drop, recovery equivalence, tokens, clock rollback, XP caps); AdPolicy; color contrast.
- **Content:** `validateContent` wired into `check`.
- **Instrumented smoke:** onboarding → welcome session → result → pet XP updates → tap floating pet → Pet Home → "Cho ăn" → session → back.
- **UI tests:** floating pet stays above nav on all 5 tabs; avoids `petAvoid` views; hidden with keyboard; mini mode in session/mock; position persisted; drag + snap; hidden by setting.
- **Macrobenchmark:** cold start, session flow, pet drag (recommended).

### Manual checklist (`qa-checklist.md`)
- [ ] Fresh install offline: first question within seconds
- [ ] Vietnamese diacritics not clipped at 100/150/200% font scale
- [ ] Every screen S01–S23 implemented and screenshotted in `screens-checklist.md`
- [ ] Floating pet visible on all 5 tabs, never covers answers/banner/CTA; bubbles throttled; tap opens Pet Home
- [ ] Moods verified: hi, hungry, starving, full ("no"), sleepy, sleeping (night, no penalty), dozing (recoverable)
- [ ] Correct/incorrect effects, XP fly-to-pet, combo, stage-up, result sequence; reduce-motion variants
- [ ] Mock test: timer, palette, flag, resume after process death, auto-submit, result labelled as estimate
- [ ] Practice builder: live count, presets, no-match state
- [ ] Ads: none in session/mock/lesson/Pet Home; caps respected; UMP with EEA test geography
- [ ] Notification permission only after first session; quiet hours; off switch
- [ ] Clock tampering: no negative XP, no crash
- [ ] Backup/restore preserves progress
- [ ] Sizes within budget; no `needs_review`/`ai_draft`/test ad IDs in release

---

## 17. Execution plan for the agent (stop at each gate and report)

1. **Phase 1 – Foundation:** project, tokens, components (section 4), i18n scaffolding, content repository with a placeholder sample pack, validator. *Gate:* app runs, validator passes.
2. **Phase 2 – Learning core:** SQLite state, Scheduler, SessionBuilder, ExerciseFactory + exercises E01, E02, E04, E05, E06, E07, E08, Session screen (S16) and Result (S17), feedback effects v1. *Gate:* a full session works offline; tests green.
3. **Phase 3 – Content screens:** S03 Hôm nay, S04–S07 vocabulary, S08–S09 grammar, S22 search. *Gate:* all navigable with placeholder content.
4. **Phase 4 – Exam practice:** S10–S15, PracticeBuilder, MockTestEngine, exercises E03, E11, E12, E14, E13, E09, E10. *Gate:* builder and mock test tests green.
5. **Phase 5 – Floating pet and Pet Home:** `PetFloatingView`, `PetMoodResolver`, `BubbleScheduler`, `PetEngine`, Pet Home (S20), mini mode, art placeholders, XP fly-to-pet, stage-up. *Gate:* section 5 and 11 tests green; floating-pet UI tests pass.
6. **Phase 6 – Progress and settings:** S19, S21, achievements, notifications (opt-in), dialogs (S23). *Gate:* settings control pet visibility/size.
7. **Phase 7 – Monetization:** UMP, ads per section 13, billing. *Gate:* placement matrix verified with test IDs.
8. **Phase 8 – Polish and optimization:** animation pass (section 10) with reduce-motion variants, accessibility pass, R8/baseline profile, `size_report.py`, packs plumbing. *Gate:* budgets met.
9. **Phase 9 – Content (human-in-the-loop):** AI drafts → human review → validator and coverage reports per batch. *Gate:* v1 targets or an agreed smaller set pass validation.
10. **Phase 10 – Locale tooling:** `new_locale.py`, completeness report, listing generator. *Gate:* dummy locale builds and passes checks.

**Definition of Done (v1.0):** all screens implemented and checklist-verified, size and performance within budgets, content validated and human-reviewed, release has real ad IDs and no test IDs, listing/privacy/data-safety generated for `vi`, human sign-off on pet art and Vietnamese copy.

---

## 18. Ask the human only when blocked
One concise question at a time, only if the answer is not in the repo/config: package name/brand/support email · location of word lists and pet art · exam format values for `exam_formats.json` · real AdMob IDs and signing key. Otherwise state the assumption in one line and continue.

## 19. Out of scope for v1
Accounts, cloud sync, leaderboards/social, user-generated content, speaking scoring, Listening parts, Part 7, IELTS/TOEFL packs, subscriptions, pet shop/customization items, iOS/web.
