# Korean semantic review — 2026-10-19

## Result

- Reviewed the Korean entries sequentially against the English source.
- Corrected 329 flagged entries in `content/i18n/ko/words.json`, including glosses and examples.
- Remaining corruption scan: 0 entries containing the detected placeholder patterns.
- UTF-8 JSON parse: passed.
- `python -X utf-8 tools/locale.py check ko`: retains the pre-existing `ko/words:ex: unknown or orphan ID` warning.

## Notes

- IDs, JSON structure, and English source terms were preserved.
- No non-Korean locale files were changed by this review.
- No additional semantic issues were identified by the sequential pass; human review should still verify tone and terminology in context.
