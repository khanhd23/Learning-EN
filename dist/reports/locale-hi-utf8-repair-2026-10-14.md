# Hindi locale semantic and UTF-8 repair — 2026-10-14

## Result

The Hindi locale now has reviewed coverage for every tracked entry:

| Kind | Missing | Draft | Reviewed | Approved |
|---|---:|---:|---:|---:|
| Words | 0 | 0 | 8,837 | 0 |
| Topics | 0 | 0 | 47 | 0 |
| Confusables | 0 | 0 | 32 | 0 |
| Grammar | 0 | 0 | 44 | 0 |
| Questions | 0 | 0 | 890 | 0 |
| Passages | 0 | 0 | 40 | 0 |
| UI | 0 | 0 | 728 | 0 |

Total reviewed entries: 10,618. No entries were marked `approved`.

## Work completed

The semantic Hindi translations were restored from the session-authored review material and
written back with explicit UTF-8 encoding. This repaired the word batches and the Hindi
learning packs for grammar, questions, passages, UI, topics, and confusable notes. The
corresponding review CSVs were synchronized with the repaired locale values.

A scan of all Hindi JSON packs found no repeated-question corruption sequences. English terms
being taught, placeholders, symbols, and markup remain in their source form where required.

## Validation

- `python -X utf-8 tools/validate_content.py`: 0 errors, 13 warnings for English-identical Hindi
  glosses that need native reviewer attention.
- `python -X utf-8 tools/locale.py check --all`: all locales passed.
- `python -X utf-8 tools/audit_content.py`: 604 source-content errors remain in the English audit
  output and need a separate source-content task.
