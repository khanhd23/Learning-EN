# Original exam-practice expansion roadmap

This roadmap expands LingoMori with original practice data for TOEIC-style
listening and Part 7, VSTEP, Cambridge A2 Key/B1 Preliminary, and limited IELTS
academic reading. It does not reproduce official tests and it does not claim
affiliation with ETS, IELTS owners, Cambridge, VNU, MOET, or any other exam
organisation.

## Product and licensing rules

1. Use official pages only to verify format facts: sections, timing, question
   types, and public scoring descriptions.
2. Write every passage, script, question, option, answer, and explanation from
   scratch. Do not copy, translate, lightly paraphrase, or mine official
   papers, sample papers, prep books, mock-test websites, answer keys, or
   official audio.
3. Prefer original scenarios and facts. If an external source is used, it
   must have a commercial-compatible licence, and the item must retain its
   URL, licence, attribution, and adaptation note. No source is included just
   because it is publicly visible on the web.
4. Every item records provenance such as
   `source: original:exam-expansion-2026`, author date, and review status.
5. Use descriptive wording such as “TOEIC-style practice” and
   “Cambridge-style reading practice”. Do not use logos, official seals, real
   test screenshots, or claims that scores are official. The trademark
   disclaimer remains visible in the app and store listing.
6. A bank is hidden until its format, content, answer key, Vietnamese
   explanation, duplicate check, licence audit, and manual educational review
   all pass.

## Ordered tasks

### E1 — TOEIC-style Part 7

- Add the bank schema and provenance checks first.
- Write original single, double, and multiple-passage groups in small batches.
- Target for the expanded release: 150 original passage groups, with enough
  questions to model the 54-question Part 7 share of a 100-question reading
  session. The first batch remains a practice preview until coverage is met.
- Manually verify every answer, distractor, reference, date, and Vietnamese
  explanation.
- Keep it separate from the current P5/P6 bank until the full section can be
  selected without an incomplete session.

### E2 — TOEIC-style listening with device TTS

- Start with original Part 1–2-style scripts and image-free text prompts,
  using device TTS only; no ETS audio or copied scripts.
- Add replay limits, speed control, transcript reveal after answering, and
  offline fallback.
- Add a separate provenance and pronunciation QA pass before considering later
  Part 3–4-style dialogues.

### E3 — VSTEP-style B1–B2 reading

- Verify the public format from the VNU source recorded in
  `config/exam_formats.json`.
- Write original passages with 10 questions per text, B1–B2 vocabulary, and
  Vietnamese explanations.
- Ship an internally reviewed starter bank first; expand toward the long-term
  plan of 80 texts only after the starter bank has been used and reviewed.

### E4 — Cambridge A2 Key-style reading

- Implement the five public reading-part shapes as practice modes.
- Write original notices, matching tasks, longer texts, multiple-choice cloze,
  and open cloze items.
- Start with a small reviewed set, then grow toward 20 practice papers.
- Never copy Cambridge sample material or reuse its answer keys.

### E5 — Cambridge B1 Preliminary-style reading

- Reuse only the generic engine, not Cambridge content.
- Write original six-part reading practice at A2–B1 difficulty.
- Apply the same staged bank, provenance, and manual-QA gates as E4.

### E6 — IELTS-style short academic reading

- Keep the scope to short original academic passages and vocabulary practice;
  full IELTS Reading is out of scope for this release.
- Do not reproduce IELTS passages, task text, band descriptors, or official
  answer keys.
- Use “academic reading practice” in learner-facing copy unless a trademark
  reference is necessary and the disclaimer is shown.

### E7 — Engine, UI, and policy integration

- Add format cards only when a bank meets its coverage gate.
- Show “practice in this format”, not “official exam” or “official score”.
- Add sources and disclaimers to the About/licences/store materials without
  adding exam logos or implying endorsement.
- Keep incomplete banks out of onboarding and selectable exam lists.

### E8 — Release audit

- Run content validation, duplicate/near-duplicate checks, locale checks, and
  manual answer-key review for every item.
- Review provenance and commercial licence evidence item by item.
- Run debug, benchmark, and release smoke tests; test offline, TTS failure,
  replay, resume, and result screens on a device.
- Update the store listing only after the corresponding bank is actually in
  the shipped build.

## Commit and report policy

Each batch or code task gets one commit and one report in `dist/reports/`.
Reports must state counts, sources and licences, manual-review coverage,
validation output, unresolved risks, and whether the bank is enabled or still
held behind its coverage gate. No task pushes to a remote.

## Official format references

- ETS TOEIC format and licensing: <https://www.ets.org/toeic/about/listening-reading.html>
  and <https://www.ets.org/legal/permissions/licensing.html>
- VSTEP format: <https://vstep.vnu.edu.vn/files/uploads/2020/12/Dinh-dang-VSTEP.3-5.pdf>
- Cambridge A2 Key format: <https://www.cambridgeenglish.org/exams-and-tests/key/exam-format>
- Cambridge copyright and permissions: <https://www.cambridge.org/us/legal/copyright>
- IELTS official information: <https://ielts.org/take-a-test/test-types>

These links document format and rights boundaries; they are not content sources
for questions or passages.
