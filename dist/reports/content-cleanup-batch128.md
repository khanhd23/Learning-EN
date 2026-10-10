# Content cleanup batch 128

## Scope

Reviewed the 43 Level 3 entries that had real audit failures in the existing dictionary pack.
Added owner-written English definitions, Vietnamese glosses and natural example sentences in
`tools/authoring/senses_editor_batch128.tsv`. Reassigned affected catch-all entries to
controlled topics in `entry_corrections.tsv`.

No audit or validation rule was changed. The editor batch was checked before regeneration.

## Before / after

| Measure | Before | After |
|---|---:|---:|
| Lesson-ready silver entries | 7,562 | 7,605 |
| Bronze entries | 1,112 | 1,069 |
| Audit error instances | 2,709 | 2,644 |
| Level 3 entries with audit errors | 43 | 0 |
| Validate errors | 0 | 0 |
| Validate warnings | 244 | 242 |

At the time of this report, 1,026 of the remaining bronze rows came from the `tagged-master`
source archive and 43 were internal entries later handled by cleanup batch 129. The archive
rows remain search-only long-tail entries under the roadmap's B4 rule; they were not deleted or
forced into lessons. The remaining validate warnings are gloss-length heuristics, not invalid
translations; they need individual language review rather than mechanical shortening.

## Verification

- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch128.tsv` — `rows=43 problems=0`
- `python tools/gen_content.py` — passed and rebuilt all generated packs
- `python tools/validate_content.py` — `errors=0 warnings=242`
- `python tools/audit_content.py` — `silver=7605 bronze=1069 errors=2644`
- `python -m unittest discover -s tools/tests` — 67 tests passed
- `./gradlew.bat check testDebugUnitTest assembleDebug --console=plain -q` — passed

No UI strings, exam content or application code changed.
