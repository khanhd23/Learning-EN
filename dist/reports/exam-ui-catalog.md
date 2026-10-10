# Exam catalog UI

## Scope

- Reworked the Exams hub from one long horizontal format strip into a two-level
  catalog: exam-family chips followed by a vertical list of practice sets.
- Groups the available data without changing format IDs or exam content:
  TOEIC variants, IELTS variants, VSTEP, THPT, A2 Key (KET), and B1 Preliminary
  (PET).
- Each selected practice set now shows its question count and time limit when
  available. Sectioned formats also show their sections and counts, including
  Part 5, Part 6, and Part 7 practice sets.
- Added English and Vietnamese UI strings for the catalog labels. The matching
  Vietnamese UI manifest keys were added so localization validation stays in
  sync.

## Verification

- `python tools/validate_content.py` — `errors=0`, `warnings=241` (existing data warnings)
- `python -m unittest discover -s tools/tests` — 67 tests passed
- `./gradlew.bat testDebugUnitTest assembleDebug --console=plain -q` — passed
- New `ExamCatalogTest` covers preferred family order, TOEIC/IELTS collapsing,
  KET/PET mapping, and stable handling of unknown families.

No vocabulary, exam-bank, authoring, or content-generation source data was changed.
