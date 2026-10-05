# Task 1B batch 7 — owner rewrite

Date: 2026-10-05. Codex commit `8283c57` was reverted (`6d3179f`): template examples with swapped
words, WordNet definitions with "a type of" prefixes, filler translations, frame collocations
("public X; social X" ×172), machine IPA with misplaced stress, random topics.

The owner wrote all 326 entries for NGSL ranks 2001–2809 (pos, General American IPA, topic, two real
collocations, irregular forms, learner definition, Vietnamese gloss, example, full translation):
`tools/authoring/vocab_ngsl_core.tsv` (rows 393–718) and `tools/authoring/senses_editor_batch7.tsv`.

Also fixed: 8 batch-5 examples shorter than 6 words at Level 3+; `check_editor_batch.py` now uses the
same minimum length as the audit (5 words for Levels 1–2, 6 for Level 3+); audit filler rule narrowed
to real boilerplate; `ski` allowed as IPA = spelling.

## Verification
- NGSL lemmas missing: 326 → 0.
- `check_editor_batch.py` on batches 1–7: rows=1599, problems=0.
- Audit: all 718 Task 1B entries have 0 errors. Silver entries: 1,729 (Level 1: 493, Level 2: 529, Level 3: 567).
- `validate_content.py` errors=0; `gradlew check assembleDebug` passed.
