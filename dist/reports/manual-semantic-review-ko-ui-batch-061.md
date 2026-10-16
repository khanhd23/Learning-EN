# Korean manual semantic review ? UI batch 061

- Reviewed 10 Korean wish, word-order, flashcard, audio-replay, error-tap, test, and theme UI strings manually against the English source.
- Preserved the `%1$d`, `%2$s`, `%3$s`, and `%4$s` placeholders and all middle-dot separators in `test_meta`.
- Updated each reviewed entry to `reviewed`; no entries were marked approved.
- Validation: `python -X utf-8 tools/locale.py check ko`.
