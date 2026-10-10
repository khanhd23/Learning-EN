# Content cleanup batch 129

## Scope

Manually upgraded the 43 remaining internal bronze entries that were not from the
`tagged-master` archive. Each row received an owner-written English learner definition,
Vietnamese gloss, natural example pair, and a controlled topic correction where the old
topic was `unsorted`. No audit or validation rule was changed.

The remaining 1,026 bronze entries are all Level 4 rows with `sourceLists=["tagged-master"]`.
They are archive/search records without TSL, NAWL, BSL or NGSL list membership. They remain
search-only under `docs/PLAN_FULL.md` §4 B4 until a separate editorial promotion batch is
approved; no source-archive row was mass-promoted into lessons.

## Before / after

| Measure | Before | After |
|---|---:|---:|
| Lesson-ready silver entries | 7,605 | 7,648 |
| Bronze entries | 1,069 | 1,026 |
| Audit error instances | 2,644 | 2,573 |
| Internal bronze entries without `sourceLists` | 43 | 0 |
| Validate errors | 0 | 0 |
| Validate warnings | 242 | 241 |

## Verification

- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch129.tsv` — `rows=43 problems=0`
- `python tools/gen_content.py` — passed and rebuilt all generated packs
- `python tools/validate_content.py` — `errors=0 warnings=241`
- `python tools/audit_content.py` — `silver=7648 bronze=1026 errors=2573`
- Remaining audit errors — all 1,026 `tagged-master` Level 4 archive rows; no lesson-list rows

No UI strings, exam content, application code or audit rules changed.
