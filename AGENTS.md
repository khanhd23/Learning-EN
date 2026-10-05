# AGENTS.md — rules for every coding agent working in this repo

This app (EngPet) teaches English to learners whose native language (L1) varies: Vietnamese
today, then Spanish, Portuguese (Brazil), Indonesian, Hindi and more. One app, one codebase;
each learner language is a data folder, never a fork.

The step-by-step work plan is `docs/localization/PLAN.md`. Do the tasks in order, one task per
session, and do not start a task until the previous one is ticked in that file.

## 1. Content rules (non-negotiable)

1. **English is the only source of truth.** `content/en/` holds the English core. Every learner
   language, including Vietnamese, is a translation/adaptation of the English core.
   Never translate one learner language from another (no vi→es, no es→pt-BR).
2. **Never translate what is being taught.** English words, phrases and example sentences that
   the learner is studying stay in English inside every locale string. In machine drafts, wrap
   them as `<keep>…</keep>` before sending and unwrap afterwards. Placeholders (`{name}`, `{n}`,
   `{1}`), symbols (`→`, `·`, `/`) and HTML/markup also stay byte-identical.
3. **Three kinds of locale fields** (full table in PLAN.md §Field classes):
   - `TRANSLATE`: glosses, example translations, grammar explanations, UI, pet lines, answer explanations.
   - `KEEP`: English being taught, placeholders, IDs. Never changed.
   - `REWRITE`: content that depends on the learner's L1 (`mistakes`, `tip`, confusable tips,
     `l1_notes`). Written fresh for that L1. Never machine-translated from another locale.
4. **No Vietnamese leakage.** A locale other than `vi` must not mention Vietnam, Vietnamese,
   "tiếng Việt", "vietnamita", etc.
5. **Machine output is always `draft`.** Only a human reviewer, through
   `tools/locale.py import`, may set `reviewed`; only the owner sets `approved`.
   The app shows nothing that is not `approved`.
6. **Stable IDs.** Never rename or renumber a word, sense, example, grammar point or question ID.
   When you reorder senses, move the objects and keep their IDs.
7. **Spanish = `es` written as Latin American Spanish (es-419).** Spain-specific wording goes into
   `regional["es-ES"]`, never into the main value. One `es` pack serves every Spanish market.
8. **Countries are not content.** `content/i18n/market_profiles.json` maps a country to a locale.
   Never create a pack per country.
9. **Content standard.** Every word, sense, example and gloss must meet
   `docs/content/CONTENT_STANDARD.md`. Check with `python tools/audit_content.py`; work from
   `dist/content-audit-issues.jsonl`. Never edit the audit tool or the standard to make a check pass;
   propose changes in the task report instead.

10. **UTF-8 only.** Read and write every content/data file with Python `encoding="utf-8"` (or
    another explicit UTF-8 writer). Never write content through PowerShell `>`, `Out-File`,
    `Set-Content` or `Add-Content`. Vietnamese must keep all diacritics; `?` inside a word is corruption.
11. **No filler.** Never generate examples, definitions or translations from a template
    ("The X is important in daily life.", "Câu này minh họa…"). If you cannot write real content for an
    entry, leave the field empty so the audit reports it.

## 2. Forbidden

- `translate.googleapis.com/translate_a/single` or any other unofficial/free scraping endpoint.
  Machine drafts use only the provider set in `secrets.properties` (`TRANSLATE_PROVIDER`).
- Guessed or unsourced facts about countries, exams or culture.
- Shipping (bundling into the APK) any locale entry whose status is not `approved`.
- Hardcoded user-visible strings in Kotlin. All UI text goes through `strings.xml`.
- Deleting learner data, IDs or reviewed translations to make a check pass.
- Setting review metadata in bulk by code (`defSource: editor`, `tier: gold`, `reviewed`,
  `approved`, `needs_review: false`). These mark work a person or you did for that specific entry;
  set them only per entry, together with the change that justifies them.
- Editing `tools/sources/*` (third-party source lists) or `tools/audit_content.py` /
  `docs/content/CONTENT_STANDARD.md`. Propose changes in the task report.
- Claiming a task is done when a "Done when" item is not met. Report it as not met instead.

## 3. Definition of done for every task

1. `python tools/validate_content.py` passes.
2. `python tools/audit_content.py` shows no new errors; `python tools/locale.py check --all` passes (once Task 4 exists).
3. `gradlew check` and `gradlew assembleDebug` succeed.
4. Existing behaviour for Vietnamese users is unchanged unless the task says otherwise.
5. Write `dist/reports/task-<N>.md`: what changed, counts before/after, anything unsure,
   anything a human must review.
6. Tick the task in `docs/localization/PLAN.md` (only when every "Done when" item is met).
7. Commit all changes of the task as one git commit on `main`, message `Task <N>: <short summary>`
   (batches: `Task <N> batch <k>: …`). Never rewrite history (`reset --hard`, `rebase`, `push --force`,
   `commit --amend` on earlier tasks). If the owner rejects a task, fix it in a new commit.
   **Never `git push`.** The owner reviews every commit first and pushes only what passes review.
8. Stop. Do not start the next task.

## 4. Style

- App code, comments, tool output and docs in English. Vietnamese only inside `vi` content.
- Python tools: standard library + `requests` only; every tool has `--help` and a non-zero
  exit code on failure.
- Match existing Kotlin style (views, no Compose); keep the APK and content size budgets in
  `config/app_config.json`.
