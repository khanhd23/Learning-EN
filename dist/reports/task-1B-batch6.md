# Task 1B batch 6: NGSL missing 1–2000

## Scope

Added every NGSL lemma ranked 1–2000 that was missing from the English core. The batch contains 392 new entries. Metadata is authored in `tools/authoring/vocab_ngsl_core.tsv` and loaded by `tools/authoring/vocab_ngsl_core.py`; sense text is authored only in `tools/authoring/senses_editor_batch6.tsv`.

All new entries are marked `needs_review: true` with source `original:ngsl-core-2026-10`. No state-specific facts or invented rules were added.

## NGSL coverage

| Measure | Before | After |
|---|---:|---:|
| NGSL lemmas missing from core (rank 1–2000) | 718 | 326 |
| Core word entries | 4618 | 5010 |
| Batch 6 additions | 0 | 392 |

The remaining 326 missing lemmas are outside this batch's 392-entry addition set and remain for the planned follow-up work.

## Verification

| Check | Result |
|---|---|
| `check_editor_batch.py` on batches 1–6 | 1273 rows, 0 problems |
| `gen_content.py` | 5010 words generated |
| `validate_content.py` | 0 errors, 0 warnings |
| `audit_content.py` | 392 new entries, 0 new-entry errors; 326 NGSL missing overall |
| `gradlew.bat check assembleDebug` | BUILD SUCCESSFUL |

Task 1B remains unticked because the planned batch 7 work (NGSL ranks 2001–2809) is still outstanding. No push was performed.
