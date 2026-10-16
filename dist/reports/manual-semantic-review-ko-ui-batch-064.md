# Korean manual semantic review ? UI batch 064

- Reviewed 10 Korean answer-state, unlock, saved-item, timer, version, vocabulary, comparison, and wake-up UI strings manually against the English source.
- Preserved the `%1$s`, `%2$d`, and `%1$d` placeholders, percentage marker, and middle-dot separator.
- Updated each reviewed entry to `reviewed`; no entries were marked approved.
- Validation: `python -X utf-8 tools/locale.py check ko`.
