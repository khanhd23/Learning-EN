# Task 1B batch 7: NGSL missing 2001–2809

## Scope

Added all 326 NGSL lemmas ranked 2001–2809 that were still missing from the core. Metadata was appended to `tools/authoring/vocab_ngsl_core.tsv`; `tools/authoring/vocab_ngsl_core.py` remains the metadata-only loader. Sense text is in `tools/authoring/senses_editor_batch7.tsv`.

The new metadata uses level 3, existing non-catch-all topics, General American IPA, natural two-item collocation sets, and `{}` unless an irregular form is required. New words are marked `needs_review: true` with source `original:ngsl-core-2026-10`.

## NGSL coverage

| Measure | Before | After |
|---|---:|---:|
| NGSL lemmas missing from core | 326 | 0 |
| Core word entries | 5010 | 5336 |
| Batch 7 additions | 0 | 326 |

## Verification

| Check | Result |
|---|---|
| `check_editor_batch.py` on batches 1–7 | 1599 rows, 0 problems |
| `gen_content.py` | 5336 words generated |
| `validate_content.py` | 0 errors, 0 warnings |
| `audit_content.py` | NGSL missing 0; 0 errors on all 326 batch-7 entries, including `ipa_is_spelling` and `collocation_template` |
| `gradlew.bat check assembleDebug` | BUILD SUCCESSFUL |

Task 1B is ticked in `docs/localization/PLAN.md`. No push was performed.
