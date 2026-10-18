# Korean manual semantic review — batch 001

- Reviewed the first 50 Korean word entries whose example text contained corrupted `?` sequences.
- Corrected the Korean example sentences manually against the English meanings and preserved all IDs.
- Remaining entries with corruption-pattern matches: 6670 -> 6620.
- UTF-8 read check: passed.
- `python -X utf-8 tools/locale.py check ko`: currently reports the pre-existing `ko/words:ex: unknown or orphan ID` issue; no other locale error was introduced by this batch.
