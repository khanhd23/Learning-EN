# TOEIC Reading full-practice pass

Date: 2026-10-10

## Scope

The app now exposes three original 100-question TOEIC Reading practice papers. Each paper
uses the existing generated banks without copying question or passage text:

| Paper | Part 5 | Part 6 | Part 7 | Total | Time |
|---|---:|---:|---:|---:|---:|
| Practice 1 | 30 | 16 | 54 | 100 | 75 min |
| Practice 2 | 30 | 16 | 54 | 100 | 75 min |
| Practice 3 | 30 | 16 | 54 | 100 | 75 min |

P5 and P6 are drawn from `toeic.json`; P7 is drawn from the existing three disjoint P7 sets.
The engine now supports sectioned formats that use more than one bank. Deterministic set metadata
keeps the three full papers disjoint while leaving the remaining P5/P6 pool available for
question-type practice.

The labels and descriptions explicitly identify these as original practice, not official TOEIC
papers and not official scores.

## Manual data review

- Every paper contains exactly 100 selected items and 22 complete passages (4 Part 6 + 18 Part 7).
- The three papers have 0 overlapping item IDs.
- All selected items have 3 or 4 options, an in-range answer, and no duplicate option within the
  same item.
- P6 uses a repeated structural stem such as “Choose the best answer for blank 1” across
  different passages. This is intentional: the passage and blank ID provide the unique context;
  no passage or item ID is reused between the three papers.
- No new English question, passage, answer, translation, or explanation was generated in this
  change. Only deterministic paper-set metadata was generated from existing reviewed content.

## Verification

- `python tools/validate_content.py`: `errors=0`, `warnings=244`
- `python -m unittest discover -s tools/tests`: 62 passed
- `python tools/audit_content.py`: `entries=8674`, `silver=7562`, `bronze=1112`, `errors=2709`
  (same pre-existing audit baseline; no vocabulary content was changed)
- `./gradlew.bat check testDebugUnitTest assembleDebug --console=plain -q`: passed

The new unit test covers mixed-bank selection, paper-set filtering, `MockTest.build`, and the
round-tripped `exam:<bank>:<item>` keys.
