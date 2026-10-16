# Korean manual semantic review ? UI batch 047

- Reviewed 10 Korean reset, result, retry, and answer-review UI strings manually against the English source.
- Preserved the `%1$d`, `%2$d`, and `%3$02d` placeholders and the middle-dot separator.
- Updated each reviewed entry to `reviewed`; no entries were marked approved.
- Validation: `python -X utf-8 tools/locale.py check ko`.
