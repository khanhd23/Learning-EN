# Korean manual semantic review ? UI batch 063

- Reviewed 10 Korean semantic-trap, answer-type, passage, and listening UI strings manually against the English source.
- Preserved the `%1$d`, `%2$s`, and `%3$d` placeholders and the middle-dot separators in `type_meta`.
- Updated each reviewed entry to `reviewed`; no entries were marked approved.
- Validation: `python -X utf-8 tools/locale.py check ko`.
