# Korean manual semantic review ? UI batch 072

- Reviewed 10 Korean backup and restore UI strings manually against the English source.
- Preserved the `%1$s`, `%2$d`, `%3$d`, and `%4$d` placeholders exactly and retained the warning meaning for replacement and version compatibility.
- Updated each reviewed entry to `reviewed`; no entries were marked approved.
- Validation: `python -X utf-8 tools/locale.py check ko`.
