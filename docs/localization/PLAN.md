# Multi-language plan for learners of English

Read `AGENTS.md` first. Do **one task per session**, in order. When a task is done, tick it below,
write `dist/reports/task-<N>.md`, then stop.

## Progress

- [x] Task 0: Freeze the broken Spanish draft
- [x] Task 1A: Schema upgrade (sense IDs, `def`, `tier`, `forms`, `hl`) + quarantine imports
- [x] Task 1A.1: Fix Task 1A regressions (question translations, placeholder/auto definitions)
- [x] Task 1B: Fill the NGSL gaps (718 missing lemmas, function words first)
- [ ] Task 1C: Rewrite Level 1–2 entries to the standard  *(owner/Claude, in parallel — see note)*
- [ ] Task 1D: Topics: replace catch-all topics with the controlled list  *(owner/Claude, in parallel)*
- [ ] Task 1E: Rewrite Level 3 entries; re-level by frequency band  *(owner/Claude, in parallel)*
- [ ] Task 1F: Grammar questions, confusables, families up to target  *(owner/Claude, in parallel)*
- [x] Task 2: Per-locale folder layout + review status
- [ ] Task 3: Validation rules
- [ ] Task 4: `tools/locale.py` operations tool
- [ ] Task 5: UI localization (English default, `values-xx`)
- [ ] Task 6: Show only approved content; language picker
- [ ] Task 7: Spanish pilot (Level 1)
- [ ] Task 8: Store listings per locale

---

## Background: why the current `es.json` is rejected

`tools/draft_translate_locale.py` machine-translated every string of `vi.json` into Spanish through an
unofficial Google endpoint. Problems found on 2026-10-05:

| Problem | Example in `es.json` |
|---|---|
| English being taught was translated away | `say/tell` tip → "decir + lo que se dice; decir + oyente"; `lately` → "tarde = recientemente; tarde = tarde" |
| vi→es pivot garbled meaning | grammar `be`: "Ser significa 'ser, ser, ser'" |
| Duplicate glosses (205 senses) | "horario, horario", "urgente, urgente" |
| Vietnamese-specific advice copied | 23 mentions of "vietnamita" |
| Placeholders translated | 16 × `{nombre}` instead of `{name}` |
| Wrong source senses (also in vi) | `bank` = only "Đắp bờ"; `fine` = only "Hay, khéo"; `sensible` = "Dễ nhận thấy" |

---

## Target layout

```
content/
  en/                       English core (words, grammar, questions, relations)
  i18n/
    market_profiles.json    country → locale (+ variant); unchanged idea
    vi/  es/  pt-BR/ ...    one folder per learner language
      ui.json               UI strings, keys = Android string names
      words.json            per sense: gloss + example translations
      topics.json           topic names
      grammar.json          per grammar point
      questions.json        answer explanations (questions + passage blanks)
      confusables.json      confusable-set tips (REWRITE)
      tips.json             exam strategy tips
      pet.json              pet speech lines
      l1_notes.json         L1-specific pitfalls (REWRITE)
      status.json           review status for every entry above
  review/<locale>/          CSV batches sent to / returned from reviewers
tools/locale.py             single CLI for all locale operations
```

### Field classes

| File / field | Class | Notes |
|---|---|---|
| `ui.json` values | TRANSLATE | keep `%1$s`, `%d`, `{name}` |
| `topics.json` | TRANSLATE | |
| `words.json` `g` (gloss) | TRANSLATE | from English `def` + example, never from another locale |
| `words.json` `ex.<id>` | TRANSLATE | translation of the English example |
| `words.json` `tip` | REWRITE | about this L1 only |
| `grammar.json` `title`, `when`, `body` | TRANSLATE | English forms stay in `<keep>` |
| `grammar.json` `examples[]` | TRANSLATE | translations of English examples |
| `grammar.json` `mistakes[]` | REWRITE | why *this* L1's speakers make the error |
| `questions.json` explanations | TRANSLATE | answer words stay English |
| `confusables.json` | REWRITE | English words stay English |
| `tips.json` | TRANSLATE | |
| `pet.json` | TRANSLATE | natural, playful; `{name}` untouched |
| `l1_notes.json` | REWRITE | |
| all IDs, English text, placeholders | KEEP | |

### Schemas

`content/en/words.json`: each sense gains a stable `id` and an English definition:

```json
{"id": "bank", "lemma": "bank", "senses": [
  {"id": "bank_s1", "pos": "n", "def": "a business that keeps and lends money",
   "ex": [{"id": "bank_s1", "text": "I need to go to the bank."}]}
]}
```

`content/i18n/<loc>/words.json`: keyed by sense ID:

```json
{"bank_s1": {"g": "banco", "ex": {"bank_s1": "Necesito ir al banco."},
             "tip": "", "regional": {"es-ES": ""}}}
```

`content/i18n/<loc>/l1_notes.json`:

```json
{
  "falseFriends": [
    {"id": "ff_actually", "en": "actually", "l1": "actualmente",
     "enMeans": "in fact", "l1Means": "currently", "senseIds": ["actually_s1"]}
  ],
  "pronunciation": [
    {"id": "pr_b_v", "sounds": ["b", "v"], "tip": "...", "examples": ["very", "berry"]}
  ],
  "grammar": [
    {"id": "gr_age", "grammarId": "be", "wrong": "I have 20 years.",
     "right": "I am 20 years old.", "why": "..."}
  ]
}
```

`content/i18n/<loc>/status.json`: one row per translatable entry:

```json
{"words:bank_s1": {"s": "approved", "by": "maria.g", "at": "2026-10-20", "src": "a1b2c3d4"}}
```

- `s`: `missing` | `draft` | `reviewed` | `approved`.
- `src`: short hash of the English source text when the entry was reviewed. If the English
  changes, `check` reports the entry as **stale** and the app treats it as not approved.

---

## Task 0: Freeze the broken Spanish draft

1. Rename `content/i18n/es.json` → `content/i18n/_rejected/es.mt-draft.json`. Keep it for reference only;
   no tool or build may read `_rejected/`.
2. Disable `tools/draft_translate_locale.py`: remove the unofficial endpoint and have the script exit
   with a message pointing to `tools/locale.py` (Task 4).
3. Make sure the build still bundles only `vi`.

**Done when:** the build and `validate_content.py` pass; `grep -r translate_a tools/` returns nothing.

## Task 1: Content foundation (do this before any new locale)

The standard is `docs/content/CONTENT_STANDARD.md`. The measuring tool is `tools/audit_content.py`
(writes `dist/content-audit.md` and `dist/content-audit-issues.jsonl`). Work through the issues file;
never edit the audit to make numbers look better.

Baseline audit on 2026-10-05:

| metric | now | target |
|---|---|---|
| entries passing the standard (ignoring the new `id`/`def` fields) | 1,009 / 4,618 | all lesson entries |
| — hand-authored entries passing | 1,000 / 1,341 | 1,341 |
| — vi.wiktionary imports passing | 9 / 3,277 | rewrite or demote to `bronze` |
| NGSL lemmas missing | 718 / 2,809 (incl. he, we, would, which, who, city, music, phone…) | 0 |
| dictionary-fragment examples ("to take silk") | 3,817 | 0 |
| vi glosses in dictionary style (capital + final period) | ~3,200 | 0 |
| inconsistent IPA (slashes, dots, invalid symbols) | 2,551 | 0 |
| words in catch-all topics (`open_things` 1,511, `open_descriptions` 673) | 2,486 | 0 |
| entries with > 1 sense | 149 | most polysemous NGSL top-2000 words |
| grammar points with ≥ 20 questions | 0 / 38 | all |
| confusable sets / word families | 32 / 230 | 250–400 / 800–1,200 |
| non-English strings in `content/en` (e.g. question `vi` field) | 114 | 0 |

Known wrong first senses from the import: `bank` (only "đắp bờ"), `fine` (only "hay, khéo"),
`sensible` ("dễ nhận thấy"), `silk` ("luật sư hoàng gia"), `put` ("đi, đi về phía"),
`follow` ("theo nghề"), `point` ("hướng về"), `national` ("dân tộc"), `supporting` ("phụ").

### Task 1A: Schema upgrade + quarantine

1. Add to every entry/sense the fields from the standard §3: `sense.id` (`<id>_s<n>`, reuse existing
   example IDs), `sense.def`, `sense.register`, `ex.hl`, `forms`, `tier`, `levelReason`, `grammarIds`.
   Generate them in `tools/gen_content.py` / authoring modules, not by hand-editing JSON.
2. Normalise IPA to the standard (strip slashes and dots, fix invalid symbols); entries that cannot be
   fixed automatically get an `ipa_format` issue, not a guess.
3. Normalise vi gloss style automatically where safe (lowercase start, drop final period). Do not cut
   meanings: overlong glosses stay as issues for 1C/1E.
4. Set `tier`: `bronze` for every entry that still fails the audit, `silver` otherwise. Nothing is `gold`
   until the owner checks it.
5. App: lesson/practice selection uses only `silver`/`gold` entries; `bronze` stays searchable in the
   dictionary with a "dictionary" label (`ContentRepository`, `SessionBuilder`, `PracticeBuilder`).
6. Move Vietnamese text out of `content/en` (e.g. question `vi` fields) into `content/i18n/vi.json`.
7. Wire `python tools/audit_content.py` into `gradlew check` (report only; not `--strict` yet).

**Done when:** the audit shows 0 `sense_id_missing` / `def_missing` / non-English-in-core; Level 1
lessons still have ≥ 600 usable words; app builds and runs.

### Task 1A.1: Fix Task 1A regressions

Owner review of Task 1A (2026-10-05) found:

1. **App bug:** Vietnamese question fields were moved from `content/en/questions.json` to
   `vi.json` (`q_translation` 62, `q_fix` 11, `q_notes` 4), but `ContentRepository.kt` still reads
   `o.strOrNull("vi")` / `o.strOrNull("fix")` from the English object. The 62 E05 word-order questions
   now show no Vietnamese prompt and 11 E04 find-the-error questions lost their Vietnamese fix text.
   Read them from the locale pack (`q_translation`, `q_fix`, `q_notes`), and add a unit test that every
   E05 question has a translation and every E04 question has a fix in the `vi` pack.
2. **Placeholder definitions:** 309 senses got template text instead of a definition
   ("a word or phrase used in English" ×139, "a person, place, thing, or idea" ×56, …). The audit now
   reports these as `def_placeholder` errors. Never write a placeholder: leave `def` empty (audit error
   `def_missing`) when no correct definition is known, or write a real one.
3. **Auto-matched WordNet definitions often describe the wrong sense**, and then contradict the vi gloss:
   `bank_s1` = "tip laterally" (gloss: bank), `post_s1` = guard post (gloss "bài đăng"),
   `fine_s1` = "an expression of agreement", `right_s1` = the side of the body,
   `silk_s1` = fabric while the gloss says "luật sư hoàng gia". Add `defSource` to every sense:
   `wordnet-auto` | `template` | `editor`. Only `editor` counts as checked (the audit warns
   `def_unchecked` otherwise). Task 1C/1E rewrite senses to `editor`.
4. For the NGSL top-1000 entries already in the core, fix now: make sense 1 the everyday meaning,
   with a matching `def` (`defSource: editor`) and vi gloss. List every change in the report.
5. `tools/audit_content.py` was edited in Task 1A (skip `.ipa` paths in the non-English scan) without
   being reported. The change is accepted; future edits to the audit or the standard are not allowed
   (AGENTS.md rule 9). Propose them in the report instead.

**Done when:** app shows E05 prompts and E04 fixes again (test passes); `def_placeholder` = 0;
every sense has `defSource`; NGSL top-1000 entries have an `editor` sense 1 that matches its gloss;
Level 1 silver/gold does not drop below 318 (the count after the placeholder rule was added).

**Owner review of the first attempt (2026-10-05): REJECTED, redo items 3–4.**
Items 1, 2 and 5 are accepted (question fields restored with tests; placeholders removed).
Rejected:

- `tools/gen_content.py` (the loop after "Task 1A.1 owner-reviewed first senses") sets
  `defSource = "editor"` on every NGSL top-1000 sense 1 without changing its text. 819 of 882
  "editor" definitions are verbatim WordNet. The owner did not review them. The audit now reports
  this as `editor_label_on_wordnet_text`.
- Many of those senses still describe the wrong meaning or contradict the gloss: `book_s1`
  "engage for a performance", `make_s1` "engage in", `take_s1` "carry out", `order_s1` = military
  command (gloss "đơn hàng"), `state_s1` = territory (gloss "trạng thái"), `present_s1` = time
  (gloss "có mặt"), `last_s1` (gloss "người cuối cùng"), `follow_s1` = "to practice a trade"
  (the known-wrong sense was kept as sense 1), `fine` gloss still "hay, khéo", `point` gloss still
  "hướng về", `national` gloss still "dân tộc", `kind`/`well`/`still` defs are long WordNet text.

Redo:

1. Delete the bulk-relabel loop. `defSource: editor` may only come from a per-entry curated data file
   (e.g. `tools/authoring/senses_editor.tsv`: `sense_id, def, vi_gloss, example, note`), one row per
   sense, written for this entry.
2. For every NGSL top-1000 entry, sense 1 must be the everyday meaning a beginner needs, in a learner
   definition you write yourself (≤ 12 words, simple words, never copied from WordNet), with a vi gloss
   that matches it. Examples of the expected result:
   `book_s1` "a set of printed pages you read" → "quyển sách" (verb "reserve" becomes sense 2);
   `fine_s1` "well, healthy, or acceptable" → "ổn, khỏe"; `follow_s1` "to go or come after someone"
   → "đi theo"; `order_s1` "a request for goods or food" → "đơn hàng, món gọi";
   `state_s1` "the condition someone or something is in" → "trạng thái, tình trạng".
3. Work in batches of 200 senses in NGSL rank order; one batch per session is fine. The report lists
   **every** changed sense (word, old def, new def, old gloss, new gloss). Untouched entries keep
   `defSource: wordnet-auto`.

**Done when (redo):** the bulk-relabel code is gone; `editor_label_on_wordnet_text` = 0; all NGSL
top-1000 sense 1 are `editor` with a written definition and matching gloss; the report table has one
row per changed sense.

**Owner review of batch 1 (NGSL 1–200, 2026-10-05): definitions and glosses ACCEPTED, with fixes.**
`editor_label_on_wordnet_text` = 0 and the definitions are learner-friendly. Fix before batch 2:

1. **Wrong sense 1 (choose the meaning a beginner meets most):** `book` must be the noun
   ("a set of printed pages you read" → "quyển sách"; the "reserve" verb becomes sense 2);
   `place` noun ("a particular area or position" → "nơi, chỗ"); `kind` noun ("a type or sort"
   → "loại, kiểu"; the adjective "tử tế" becomes sense 2); `live` verb ("to have your home
   somewhere" → "sống, ở"). Reorder the sense objects; keep IDs.
2. **Example must show the new definition.** The curated file has only def + gloss, so sense 1 kept
   its old example, which now often contradicts the definition or is a fragment:
   `interest` (bank interest vs "feeling of wanting to know"), `leave` ("maternity leave" vs verb),
   `company` (companionship), `mean` (statistical mean), `child` ("sin is the child of idleness"),
   `feel` ("soft to the feel"), `lot` ("choose by lot"), `number` ("broken number"),
   `turn` (noun example for a verb def), `a` ("a Shakespeare"), `of`, `to`, `at` ("at Haiduong"),
   `all` ("all Vietnam"), and wrong grammar: `they` ("He prefer…"), `seem` ("You seem surprise").
   Add an `example` column to the curated file (as specified above: `sense_id, def, vi_gloss,
   example, note`) and write a standard example (CONTENT_STANDARD §4) for every curated sense 1,
   with its vi translation. Keep the old example only if it already meets §4 and shows the sense.
3. **L1-neutral examples:** no Vietnamese names or places in `content/en` (`Lan`, `Da Nang`,
   `Haiduong`, `Vietnam`); use the varied name list in the standard.

Every later batch uses the full row format from the start (def + gloss + example + example translation).

**Owner review of batch 2 + batch-1 fixes (2026-10-05): REJECTED.**
Accepted: sense order of `book`/`place`/`kind`/`live` and their new examples; definitions in batch 2.
Rejected, because the data is broken or is filler that only looks finished:

1. **Vietnamese text corrupted.** 168 glosses and 215 example translations in `content/i18n/vi.json`
   lost their diacritics (`tiền` → `ti?n`, `bạn` → `b?n`, `chính phủ` → `ch?nh ph?`). Users see this in
   the app now. The source TSVs (`senses_editor_batch1.tsv`, `senses_editor_batch2.tsv`) are already
   corrupted, so the text was written through a non-UTF-8 path (e.g. PowerShell `Set-Content`/`>`).
   Audit: `gloss_encoding_broken`, `example_translation_encoding_broken`.
2. **Template examples.** "The X is important in daily life." (53), "We use X in a simple sentence."
   (25), "The X is useful in daily life." (24), "I X this task today." (20), "This is a X example
   for learners." (17), "This is a X choice for today." (15), "We use X in everyday speech." (13)…
   These do not show the meaning. Audit: `example_template` (a sentence skeleton used > 2 times).
3. **Filler translations.** Example translations are "Câu này minh họa cách dùng từ …" / "Ví dụ này
   minh họa nghĩa của từ …" instead of translating the sentence. Audit: `example_translation_filler`.
4. `interest_s1` still has the bank-interest example for the "feeling of wanting to know" definition;
   3 `editor_label_on_wordnet_text` remain; `Nam` remains in `content/en`.

Redo batches 1–2 examples and glosses:

- Restore every corrupted gloss from the last good value (the pre-batch `vi.json` text or the
  NGSL-1000 table in `dist/reports/task-1A.1.md`), or rewrite it, in correct Vietnamese with diacritics.
- Write a real example for each curated sense: a specific everyday situation that shows the meaning
  (`money`: "I don't have enough money for a taxi."; `friend`: "My best friend lives next door.").
  If you cannot write a good one, leave `example` empty. An empty field is an honest audit error;
  a template is not allowed.
- Translate each example sentence fully and naturally into Vietnamese.
- Batches are now **100 senses**. Before reporting, run the audit and confirm 0 of: `*_encoding_broken`,
  `example_template`, `example_translation_filler`, `editor_label_on_wordnet_text` for the batch.

**Owner fix of batches 1–2 (2026-10-05): DONE — NGSL ranks 1–400 are complete.**
The owner rewrote `senses_editor_batch1.tsv` and `senses_editor_batch2.tsv` (370 senses): UTF-8 glosses,
corrected sense 1 and part of speech, one real example per sense, full Vietnamese translations.
Audit for these senses: 0 errors. `gen_content.py` now loads every `senses_editor_batch<N>.tsv`,
applies the `pos` column, keeps only the editor example for a curated sense, and highlights the real
word form. `tools/check_editor_batch.py` checks a batch file before generation.
Use `senses_editor_batch1.tsv` / `batch2.tsv` as the model for style and quality.

**Remaining work for Task 1A.1: NGSL ranks 401–1000.** Batches 3–4 (401–600) are accepted.
Batch size from batch 5 on: **one batch for ranks 601–1000** (about 350 senses). Owner review of batch 4:
fixed `current` (adjective "hiện tại" first), `vote` (pos), `accord` ("according to"), `wish` (example).
Watch for these: NGSL ranks a word by its most frequent use, which is often a phrase or another part of
speech (`according to`, `current` = now).
For each batch `<N>` (3, 4, 5 …):

1. List the core entries whose lemma has NGSL rank in the batch range (rank order; skip lemmas not in
   the core — Task 1B adds them).
2. Write `tools/authoring/senses_editor_batch<N>.tsv` (UTF-8, LF, header
   `sense_id	pos	def	vi_gloss	example	example_vi	note`). One row per sense 1. If sense 1 is not the
   everyday meaning, put the everyday sense first (choose that sense's existing ID, or add the sense in the
   authoring module) — never repurpose an ID for an unrelated meaning without saying so in the report.
3. `python tools/check_editor_batch.py tools/authoring/senses_editor_batch<N>.tsv` → `problems=0`.
4. `python tools/gen_content.py`, `python tools/validate_content.py`, `python tools/audit_content.py`:
   0 errors on the batch's senses; no new `*_encoding_broken`, `example_template`,
   `example_translation_filler`, `editor_label_on_wordnet_text` anywhere.
5. `gradlew check assembleDebug` passes. Report `dist/reports/task-1A.1-batch<N>.md` (one row per sense:
   word, pos, def, vi gloss, example, example translation). Commit `Task 1A.1 batch <N>: NGSL <from>-<to>`.
   Stop after one batch.

To put a more common sense first, add a row for that sense with `[first]` in the `note` column
(example: `draw_s2` "vẽ" before `draw_s1` "trận hòa"). `gen_content.py` reorders the senses.

Common mistakes found in batch 3 (fixed by the owner) — check every row for them:
- Choose the meaning a beginner meets most: `please` = "làm ơn" (not "làm hài lòng"), `draw` = "vẽ"
  (not "trận hòa").
- `pos` and `def` must match how the word is used in the example: `land` noun def with "The plane will
  land" (verb); `front` marked adv in "the front of the line" (noun); `above`/`behind` used as
  prepositions; `outside` used as an adverb.
- The example must sound natural: not "These strong bags carry heavy books safely." or
  "The train will pass the bridge soon."

Tick Task 1A.1 only after the batch covering rank 1000 is accepted.

**Owner review of batch 5 (NGSL 601–1000, 2026-10-05): ACCEPTED after fixes. Task 1A.1 is done.**
All 880 NGSL top-1000 entries in the core now have an editor sense 1. Fixed by the owner:
- 110 rows kept the old imported Vietnamese gloss unchanged; about 50 of them contradicted the new
  definition (`bank` "đắp bờ", `guy` "sự chuồn", `dog` "gã, thằng cha", `useful` "làm ăn được, cừ",
  `clock` "ghi giờ", `version` "bản dịch", `fail` "không nhớ, quên"…). Rewritten; the correct ones are
  marked `[keep-gloss]`.
- Wrong sense 1: `firm` (company), `pretty` (= quite), `pound` (money), `apply` (for a job),
  `operation` (surgery), `statement`, `degree`.
- pos/def mismatch: `notice`, `press`, `review`, `guess`, `attempt`, `release`.
- A real person and product in an example (`author`: "Ho Ngoc Duc … FVDP"); translations that added
  "Hà Nội" (`season`, `conference`); missing `true`.

`tools/check_editor_batch.py` now also fails on: a gloss copied unchanged from the imported dictionary
(unless the note says `[keep-gloss]` after you checked it), pos that does not match the def form, old
dictionary-style glosses ("(thuộc) …"), and names or places added only in the translation.

### Task 1B: Fill the NGSL gaps

1. Add all 718 missing NGSL lemmas (120 in ranks 1–1000, 272 in 1001–2000, 326 in 2001–2809),
   including function words (`he`, `we`, `would`, `which`, `who`, `no`, `could`…) linked to grammar
   points via `grammarIds`.
2. Batches: **batch 6 = every missing lemma ranked 1–2000 (392)**; **batch 7 = ranks 2001–2809 (326)**.
3. How to add an entry (one source of truth, no duplicated text):
   - `tools/authoring/vocab_ngsl_core.tsv` + `vocab_ngsl_core.py` (same pattern as `vocab_foundation`):
     `lemma|pos|ipa|level|topic|collocations|forms|grammarIds`. Level by NGSL band (1–1000 → 1,
     1001–2000 → 2, 2001+ → 3). `topic` = an existing topic ID that is not a catch-all
     (`open_things`, `open_descriptions`); Task 1D reorganises topics later. IPA: General American,
     standard §3.2. Collocations: ≥ 2 for nouns/verbs/adjectives.
   - The sense text (def, vi gloss, example, example translation) goes **only** in
     `tools/authoring/senses_editor_batch<N>.tsv`, one row per new sense `<id>_s1`, and the new
     module reads its gloss/example from there. Add more senses only when a beginner needs them.
   - Mark new entries `needs_review: true`, `source: "original:ngsl-core-2026-10"`.
4. Same checks as Task 1A.1 batches: `check_editor_batch.py` → `problems=0` (also run it on all earlier
   batch files), gen, validate, audit (0 errors on the new entries), `gradlew check assembleDebug`.
   Report the "NGSL lemmas missing" count before/after. Commit `Task 1B batch <N>: …`. Do not push.
   Apply the batch 3–5 lessons: most frequent meaning first, pos = def = example usage, natural
   examples, no names or places in translations that are not in the English sentence.

**Done when:** "NGSL lemmas missing" = 0 and none of the new entries has an audit error.

**Owner review of Task 1B batch 6 (2026-10-05): ACCEPTED after fixes.** Sense rows (def, gloss,
example, translation) were good. The metadata was filler and was rewritten by the owner for all 392
entries:
- IPA was the spelling with a stress mark for ~110 words (`ˈwould`, `ˈwhite`, `ˈillustrate`) and wrong
  for others (`ˈpakk`, `kat`). Write real General American IPA (`wʊd`, `waɪt`, `ˈɪləˌstreɪt`).
- Collocations were frames ("the X", "a X", "X something", "X together", "very X", "use of X",
  "meaning of X"). Write two collocations people actually say ("turn off", "phone call").
- Forms were imported noise ("WHOs", "musics", "the Combine"). Only irregular forms, as a JSON object:
  `{"past": "began", "pp": "begun"}`, `{"plural": "analyses"}`; otherwise `{}`.
The audit now reports `ipa_is_spelling` and `collocation_template`; both must be 0 for new entries.
(46 older entries also have spelling-as-IPA, e.g. `bed`, `ten`, `desk` — fix them in Task 1C.)

**Owner review of Task 1B batch 7 (2026-10-05): REJECTED and reverted (`6d3179f`); rewritten by the owner.**
The Codex batch was built to pass the checks without being real content:
- examples from one template with a random word swapped in so the repeat check would not match
  ("The report links the bell to abroad during the study.", "The report links the hello to download…");
- WordNet definitions with "a type of …" / "describing something that …" prefixes to dodge the WordNet check;
- translations that only name the word ("…qua từ "bell"", "Người nói phản hồi … khi thảo luận từ…");
- collocation frames swapped for new ones ("public X; social X" ×172, "actively X; carefully X");
- machine IPA with misplaced stress (`hˈoʊldɝ`) and random topics (`tension` → food, `planet` → linking).
The owner wrote all 326 entries (IPA, pos, topic, collocations, def, gloss, example, translation).
Task 1B is done: NGSL lemmas missing = 0; all 718 new entries pass the audit.

**Who does what from here.** Content writing (Tasks 1C–1F) is done by the owner with Claude, because the
checks cannot catch every way of producing text that only looks finished. Codex continues with the code
tasks, starting with **Task 2**, which does not depend on 1C–1F. AGENTS.md "do tasks in order" applies to
Codex's own sequence: Task 2 → 3 → 4 → 5 → 6. Codex must not edit `tools/authoring/*` content files.

### Task 1C: Rewrite Level 1–2 entries to the standard

1. Order: NGSL rank 1–2000 first, then other Level 1–2 entries.
2. For each `bronze` entry: choose the senses learners actually need (standard §3.3), most common first;
   write `def`; replace dictionary fragments with real example sentences (§4); rewrite the vi gloss
   per sense (§6). Drop archaic/specialist senses from lessons (keep them only in the dictionary data).
3. Work in batches of 200 entries; after each batch run the audit and append a before/after sample of
   10 entries to `dist/reports/task-1C.md`.

**Done when:** Level 1–2 has 0 `bronze` entries; the report lists every entry whose first sense changed.

### Task 1D: Topics

1. Create `content/en/topics.json` with the controlled list (standard §5): domain, topic ID, icon.
2. Reassign every entry to 1–3 real topics; remove `open_things`, `open_descriptions`; split `core_verbs`
   by level. Topic names go in `content/i18n/vi.json` (later `topics.json` per locale).
3. Each topic has 20–80 words per level; report topics outside that range.

**Done when:** `topic_catch_all_only` = 0.

### Task 1E: Level 3 rewrite and re-levelling

1. Same process as 1C for Level 3.
2. Re-level every entry by the standard §2 bands; record overrides in `levelReason`. Clear the
   `level_above_band` / `ngsl_top1000_at_level3/4` warnings.
3. Level 4–5 `bronze` entries stay out of lessons for now (searchable only). List them in the report.

**Done when:** Levels 1–3 have 0 `bronze` entries; level warnings are 0 or explained.

### Task 1F: Grammar questions, confusables, families

1. Bring every grammar point to ≥ 20 questions (balanced answer positions, explanations in vi).
2. Confusable sets to ≥ 250 (start with pairs learners of any L1 mix up: affect/effect, borrow/lend,
   say/tell, make/do, look/see/watch, lose/loose, rise/raise, fun/funny, bored/boring…).
3. Word families to ≥ 800 for NGSL words.
4. Switch `audit_content.py` in `gradlew check` to `--strict` for Levels 1–3.

**Done when:** the audit summary meets the targets above; the owner marks a 50-entry random sample
`gold` after checking it.

## Task 2: Per-locale folder layout + review status

1. Write `tools/migrate_locale_layout.py` to convert `content/i18n/vi.json` into `content/i18n/vi/`
   (layout above). Test: counts of every entry type before == after; a round trip loses nothing.
2. Move Vietnamese-specific text into the REWRITE fields (`mistakes`, `tip`, `confusables`).
3. Create `vi/status.json` with every existing entry `approved`, `by: "legacy-vi"`, so current users see
   no change.
4. Delete the old empty scaffolds (`ar.json`, `fr.json`, `hi.json`, `id.json`, `ja.json`, `ko.json`,
   `pt-BR.json`, `th.json`). Task 4's `new` command recreates them in the new layout.
5. Update `ContentRepository.kt` + `Models.kt` to read the folder layout, and update
   `generateContentAssets` in `app/build.gradle.kts`.

**Done when:** the app shows identical Vietnamese content (spot-check 20 words, 5 grammar points,
10 questions in the report); unit test for the loader passes.

**Owner review of Task 2 (2026-10-05): ACCEPTED after a fix.** The layout, loader, tests and round-trip
tool are good. But the committed `content/i18n/vi/words.json` was migrated from a stale copy that still
held the rejected batch-7 text ("…khi thảo luận từ "championship""), and `status.json` hashed that text.
The owner regenerated `vi/` with `tools/gen_content.py` and rebuilt `status.json` from the correct data.
Always regenerate (`python tools/gen_content.py`) before migrating or hashing; never work from an old copy.

**Requirement for Task 3:** `vi` is generated from `tools/authoring/` by `gen_content.py`. When the owner
changes Vietnamese content there, `gen_content.py` must refresh `vi/status.json` for the changed entries
(`s: approved`, `by: owner`, new `src`), so the stale-hash rule does not block the build. Add this to
`gen_content.py` with a test; other locales keep the import/approve workflow.

## Task 3: Validation rules

Extend `tools/validate_content.py` (or `tools/locale.py check`) so these **fail** the build:

- every ID in a locale exists in `content/en`; no orphan or unknown IDs;
- placeholders/markup identical to source (`{name}`, `{1}`, `%1$s`, `<b>`);
- no leftover `<keep>` tags; text that was inside `<keep>` is present unchanged;
- no duplicate comma/semicolon fragments within a gloss;
- locale ≠ `vi` contains no Vietnam/Vietnamese words (any script, case-insensitive);
- `approved` entries whose `src` hash no longer matches English → stale (fail for bundled locales);
- `ui.json` keys == `strings.xml` keys.

These produce **warnings**: TRANSLATE fields identical to the English source; very long or very
short glosses compared with the English definition.

**Done when:** each rule has a small failing fixture in `tools/tests/` and passes on real content.

## Task 4: `tools/locale.py` operations tool

Replace `new_locale.py`, `draft_translate_locale.py`, `apply_locale_batch.py`, `locale_report.py`:

| Command | Behaviour |
|---|---|
| `new <loc>` | create `content/i18n/<loc>/` with every ID, empty values, status `missing` |
| `draft <loc> [--level N] [--only words,grammar,...]` | machine-draft TRANSLATE fields **from English** via `TRANSLATE_PROVIDER` (`deepl` / `google_cloud` / `none`) in `secrets.properties`; wraps KEEP text in `<keep>`; never touches REWRITE fields or non-`missing` entries; sets `draft` |
| `export <loc> [--level N] [--status draft]` | CSV batches to `content/review/<loc>/batch_NNN.csv`, ≤ 300 rows: `key, english, english_example, def, draft, corrected, comment` |
| `import <loc> <file.csv> --reviewer <name>` | accepts known keys only; `corrected` (or `draft` if blank and unchanged) becomes the value; status `reviewed`; records `src` hash |
| `approve <loc> [--level N] [--file words]` | owner promotes `reviewed` → `approved` |
| `check <loc>` / `check --all` | Task 3 rules |
| `report` | `dist/locale-status.md`: % missing/draft/reviewed/approved per locale × level × file, plus stale count |

Add `TRANSLATE_PROVIDER` and `TRANSLATE_API_KEY` to `secrets.properties.example`.

**Done when:** a full cycle `new → draft --provider none → export → import → approve → check` works
on a test locale `xx` in `tools/tests/`.

## Task 5: UI localization

1. Make `res/values/strings.xml` **English** (the fallback); move current Vietnamese text to
   `res/values-vi/strings.xml`. Same for `arrays.xml` if it has visible text.
2. Move the 49 hardcoded Vietnamese strings in Kotlin (33 in `PetItems.kt`, plus `SessionScreen.kt`,
   `SessionBuilder.kt`, `OnboardingScreen.kt`, `WordListScreen.kt`, `Models.kt`, `Exercise.kt`,
   `LearningStore.kt`, `PracticeBuilder.kt`, `BuilderScreen.kt`, …) into resources.
3. `tools/locale.py build-ui` generates `res/values-<android-qualifier>/strings.xml` from approved
   `ui.json` (`es` → `values-es`, `pt-BR` → `values-pt-rBR`).
4. `localeFilters` in `app/build.gradle.kts` = `en` + locales whose UI is 100% approved.
5. Arabic is RTL: confirm `android:supportsRtl="true"` and use start/end, not left/right, in layouts.

**Done when:** a device set to Vietnamese looks exactly as before; a device set to French shows English UI.

## Task 6: Show only approved content; language picker

1. The loader hides any entry not `approved` (or stale). A word is visible when its sense 1 is approved;
   its other unapproved senses are hidden. A grammar point / question is visible only when all its
   TRANSLATE and REWRITE fields are approved. No fallback to Vietnamese for another locale.
2. A locale is **selectable** only when the UI is 100% approved and Level 1 vocabulary + Level 1
   grammar are ≥ 95% approved. Levels with < 95% approved content show "Coming soon" for that locale.
3. Onboarding step "I speak: ___" (list = selectable locales, default = device language if selectable,
   else English UI + the picker). Changeable in Settings. `market_profiles.json` picks the variant.

**Done when:** with a fake locale that has only Level 1 approved, the app shows only Level 1 content and
"Coming soon" elsewhere (report includes screenshots or a test).

## Task 7: Spanish pilot (Level 1)

1. `locale.py new es` → `draft es --level 1` (UI, topics, Level 1 words, Level 1 grammar, Level 1
   questions, pet, tips).
2. Write `es/l1_notes.json` and the REWRITE fields for Level 1, for Spanish speakers:
   - false friends: actually/actualmente, embarrassed/embarazada, library/librería,
     sensible/sensible, assist/asistir, carpet/carpeta, exit/éxito, realize/realizar, attend/atender;
   - pronunciation: b/v, ship/sheep (short i), epenthetic e ("espeak", "estudent"), final consonants,
     the -ed endings, "j/y", h is not silent;
   - grammar: dropped subject ("Is raining"), "I have 20 years", adjective after noun, double
     negatives, "people is", "the life is hard" (article with general nouns), make/do from "hacer".
3. Write es-419 by default; add `regional["es-ES"]` only where Spain differs (ordenador/computadora,
   coche/carro, móvil/celular, vosotros forms in UI are **not** used).
4. `export es --level 1` and stop. Statuses stay `draft`; a native reviewer does the rest.

**Done when:** `check es` passes except for "not approved"; CSV batches exist; the report lists every
REWRITE item written so the reviewer can focus on them.

## Task 8: Store listings per locale

1. `dist/listing/<loc>/`: `title.txt` (≤ 30 chars), `short.txt` (≤ 80), `full.txt` (≤ 4000),
   screenshots captured with that locale. Only for selectable locales.
2. `app_config.json`: `displayName` per locale (vi "EngPet: Học Tiếng Anh", en "EngPet: Learn English",
   es "EngPet: Aprende Inglés", pt-BR "EngPet: Aprenda Inglês"); app name in `strings.xml` per locale.
3. One applicationId for all locales (`com.engpet.learn`). No per-country apps.

---

## Owner workflow after Task 8 (for each new language)

```
python tools/locale.py new pt-BR
python tools/locale.py draft pt-BR --level 1
# agent writes l1_notes + REWRITE fields for pt-BR (follow Task 7 as the template)
python tools/locale.py export pt-BR --level 1      # send CSVs to a native reviewer
python tools/locale.py import pt-BR reviewed.csv --reviewer <name>
python tools/locale.py approve pt-BR --level 1
python tools/locale.py check pt-BR && python tools/locale.py report
gradlew bundleRelease
```

Suggested order: `vi` (fix) → `es` → `pt-BR` → `id` → `hi` → others. Arabic last (RTL QA).
