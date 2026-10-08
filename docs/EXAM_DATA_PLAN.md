# Exam data plan (owner, C4)

Goal: enough original, licence-clean data to prepare learners for THPT, TOEIC Reading, VSTEP
(B1–B2), Cambridge A2 Key / B1 Preliminary and IELTS academic vocabulary. Research date:
2026-10-08.

## 1. What "enough" means

A learner who studies daily for 3 months should not see the same item twice in timed practice.
That needs about 20 full papers' worth of items per exam, plus the vocabulary list the exam
draws on, at lesson quality (silver).

| Exam | Official format (verified) | Bank target | Word list |
|---|---|---|---|
| THPT (from 2025) | 40 MCQ, 50 min, 0.25 point each. Notice gap fill 6, leaflet gap fill 6, arrange a dialogue/letter/text 5, text completion 5, reading 8 + reading 10 | 20 papers = 800 items (240 groups) | CEFR A1–B1 |
| TOEIC Reading | Part 5: 30, Part 6: 16 (4 texts × 4), Part 7: 54; 100 items / 75 min | P5 600, P6 80 texts, P7 150 texts | TSL 1,250 |
| VSTEP 3–5 Reading | 40 items / 60 min, 4 texts, B1→C1 | 80 texts × 10 | CEFR B1–B2 |
| A2 Key Reading | 5 parts: 6 short messages, 7 matching, 5 long text, 6 MC gap fill, 5 open gap fill | 20 papers | CEFR A1–A2 |
| B1 Preliminary Reading | 6 parts (same family as Key) | 20 papers | CEFR A2–B1 |
| IELTS | Vocabulary and short reading only; full IELTS reading (3 texts × ~900 words) is out of scope for now | — | NAWL + CEFR B2–C1 |

Listening, writing and speaking: listening uses device TTS on owner scripts (TOEIC Part 1–2
style, Key Part 1 style) only after the reading banks are done; writing and speaking stay as
practice tasks with model answers, never scored as an exam band.

## 2. Coverage today (measured)

- TSL: 1,250 words → 366 lesson-ready, 76 search-only, 808 missing.
- CEFR (CEFR-J + Octanove) A1–B2 ∪ TSL: 7,461 words needed → 3,275 single words missing,
  501 search-only. Missing words include basic ones (airport, butter, cinema, December).
- Questions: 641 general items (E01–E12), 8 passages; no exam-specific bank yet.

## 3. Sources we may use

| Source | Use | Licence | Conditions |
|---|---|---|---|
| CEFR-J Wordlist 1.5 (Tono Lab, TUFS) | CEFR level per word | Free incl. commercial | Cite (done in `content/LICENSES.md`) |
| Octanove Vocabulary Profile C1/C2 | C1/C2 levels | CC BY-SA 4.0 | Attribution, share-alike |
| NGSL / NAWL / BSL / TSL | Exam lists, frequency | CC BY-SA 4.0 | Attribution (done) |
| VOA Learning English (VOA-produced only) | Ideas and adapted passages for reading at A2–B2 | Public domain | Credit VOA; never reuse AP/Reuters/AFP text or photos inside VOA pages |
| Simple English Wikipedia | Facts for passages | CC BY-SA 4.0 | Attribution per passage, share-alike |
| Tatoeba | Optional extra example sentences | CC BY 2.0 FR | Credit each sentence's author; audio licences differ per speaker, do not use audio |
| Official format pages (MOET, ETS, VNU-ULIS for VSTEP, Cambridge handbooks) | Format facts only (counts, time, item types) | Facts are not protected | Store the URL in `config/exam_formats.json` `source` |

## 4. Sources we must not copy

- Past or sample official papers (MOET THPT papers, ETS TOEIC tests, Cambridge sample papers,
  VSTEP sample papers). Exam papers are not in the exemptions of Article 15 of the Vietnamese IP
  Law, and they often contain third-party texts. Use them only to learn the format.
- Commercial prep books and their word lists ("600 Essential Words for the TOEIC", Barron's,
  Hackers, ETS official guides, Cambridge Vocabulary in Use).
- English Vocabulary Profile, Oxford 3000/5000, Longman Communication 3000 (proprietary lists).
- Research datasets limited to non-commercial use (e.g. RACE reading comprehension).
- Prep-site "mock tests" (most copy official papers).

## 5. How we write items

- Every passage and question is original. A passage may take facts from a public-domain or
  CC BY-SA source; when it does, `credit` is stored with the group.
- Only lesson-ready words appear in stems and options at or below the exam's level; harder words
  are allowed in reading texts only.
- Each item has an English explanation (`expl`) and a Vietnamese one in the vi bank, approved by
  the owner, like `questions`.
- Distractors are plausible and grammatical; exactly one answer; no "all of the above".
- Each bank goes through `validate_content.py` (Task 14 checks) plus an owner checklist: answer
  key re-checked, level, no near-duplicates, no real people's names except public figures in
  neutral facts, no brand names.

## 6. Order of work

1. Words: add the 808 missing TSL words, then CEFR A1–B1 missing words (THPT/Key/PET), then B2
   (VSTEP), in batches of ~150 at silver quality. Replace archaic group-C words with these.
2. THPT format entry in `config/exam_formats.json` with `sections` (counts above, source URL),
   then the THPT bank: start with 5 papers (200 items), ship, then grow to 20.
3. TOEIC Part 5/6 bank on the new engine (P5 600 items, P6 80 texts), then Part 7.
4. VSTEP B1–B2 reading texts; Key/PET reading parts.

## Sources (format facts)

- THPT 2025 structure: MOET sample papers announced 18 Oct 2024; summaries in
  [luatminhkhue.vn](https://luatminhkhue.vn/de-thi-minh-hoa-mon-tieng-anh-tot-nghiep-thpt-tu-nam-2025-co-dap-an.aspx),
  [prepedu.com](https://prepedu.com/vi/blog/cac-dang-bai-trong-de-thi-tieng-anh-thpt-quoc-gia),
  [hatinh.gov.vn](https://hatinh.gov.vn/vi/bai-viet/doi-moi-ky-thi-tot-nghiep-thpt-2025-thay-doi-cach-hoc-thi-ngoai-ngu-the-nao).
  The exact counts must be re-checked against the MOET sample paper before the format entry ships.
- VSTEP 3–5: Decision 729/QĐ-BGDĐT (2015), [VNU format PDF](https://vstep.vnu.edu.vn/files/uploads/2020/12/Dinh-dang-VSTEP.3-5.pdf).
- A2 Key: [Cambridge exam format](https://www.cambridgeenglish.org/exams-and-tests/key/exam-format),
  [handbook 2020](https://www.cambridgeenglish.org/images/504505-a2-key-handbook-2020.pdf).
- TOEIC: ETS and IIBC format pages already cited in `config/exam_formats.json`.
- VOA public domain: [learningenglish.voanews.com/p/6861.html](https://learningenglish.voanews.com/p/6861.html).
- CEFR-J / Octanove licence: [olp-en-cefrj](https://github.com/openlanguageprofiles/olp-en-cefrj).
