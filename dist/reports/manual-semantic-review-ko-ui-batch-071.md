# Korean manual semantic review ? UI batch 071

- Reviewed 10 Korean exam-word-list, passage, section, and backup UI strings manually against the English source.
- Preserved the `%1$d`, `%2$d`, and `%3$s` placeholders exactly and retained the ordering meaning in Korean.
- Updated each reviewed entry to `reviewed`; no entries were marked approved.
- Validation: `python -X utf-8 tools/locale.py check ko`.
