# Korean manual semantic review ? UI batch 065

- Reviewed 10 Korean weak-spot, lesson, usage, pet, word, and known-vocabulary UI strings manually against the English source.
- Preserved the `%1$d` and `%2$d` placeholders exactly, including their meaning in the reordered Korean sentence.
- Updated each reviewed entry to `reviewed`; no entries were marked approved.
- Validation: `python -X utf-8 tools/locale.py check ko`.
