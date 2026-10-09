# Final content data review

Date: 2026-10-09

## Result

The release-oriented content set is complete and was regenerated from the authoring sources. The
latest manual additions were batches 96–98: 75 B2 entries. I checked each new row for the intended
sense, part of speech, General American IPA, difficulty, controlled topics, natural Vietnamese
gloss, and an original English/Vietnamese example pair.

The project does **not** yet contain every CEFR B2/C1/C2 source candidate as lesson-ready content.
That is intentional: the roadmap places CEFR B2 and C1/C2 expansion after release, and the
remaining source candidates include rare spellings, compounds, offensive senses, and records that
still need bilingual editorial review. I did not promote those records into lessons merely to make
the gap count zero.

## Counts

| Measure | Result |
|---|---:|
| Lesson dictionary entries | 7,951 |
| Silver entries | 6,839 |
| Bronze entries | 1,112 |
| Normalized duplicate lemmas | 0 |
| TSL missing / bronze | 0 / 0 |
| CEFR A1–B1 missing / bronze | 2 / 0 |
| CEFR B2 missing | 724 |
| CEFR C1–C2 missing | 1,598 |
| Audit errors | 2,709 (unchanged by batches 96–98) |
| Content validation errors | 0 |
| Content validation warnings | 240 existing warnings |

The two A1–B1 source gaps are deliberate exclusions: `handicapped` is outdated and potentially
ableist in learner-facing use, while `hearted` is not useful as a standalone teaching lemma and is
normally encountered inside compounds such as `kind-hearted`.

## Existing release data verified

- THPT: 5 papers / 200 items (25 top-level items plus 175 items inside groups).
- TOEIC: 306 Part 5 items, 40 Part 6 passages, 160 Part 6 items.
- Stories: 30.
- Picture mappings: 500.
- Shared dictionary model: one `content/en/words.json`; exam lists and CEFR are metadata, not
  separate duplicate dictionaries.
- Full source-derived search archive: 133,447 records, 119,207 distinct headwords, 190,805 senses,
  and 27,403 examples. Its manifest explicitly marks every record `needs_review`; it is not used as
  reviewed lesson content.

## Checks

- `python tools/validate_content.py`: 0 errors, 240 warnings.
- `python tools/audit_content.py`: 2,709 existing errors; no increase from batches 96–98.
- `python tools/locale.py check --all`: Vietnamese locale 0 errors.
- `python -m unittest discover -s tools/tests`: 32 tests passed; the locale `xx` check returned 0
  errors.
- `gradlew.bat check testDebugUnitTest assembleDebug`: BUILD SUCCESSFUL.
- New editor files `senses_editor_batch96.tsv`, `senses_editor_batch97.tsv`, and
  `senses_editor_batch98.tsv`: 25 rows each, 0 problems; each batch has 0 entries below silver.

An aggregate run over all historical editor files reports 105 cross-batch duplicate/example-frame
findings in older authoring files. They predate batches 96–98; the per-batch checks for the new work
are clean, and no generated lemma or sense ID was duplicated.

## Commits

- `eda5af9` — `Content: batches 96-97, B2 vocabulary`
- `b3dd593` — `Content: batch 98, B2 vocabulary`

No push was performed.
