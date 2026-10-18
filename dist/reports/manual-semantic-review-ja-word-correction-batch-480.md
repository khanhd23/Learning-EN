# Japanese manual semantic review — word correction batch 480

- Reviewed 125 Japanese word senses manually against the English definitions and examples.
- Replaced corrupted `?` example text with natural Japanese translations; stable sense IDs and glosses were preserved.
- Removed a pre-existing orphan top-level `ex` block from `content/i18n/ja/words.json`; its shape caused `tools/locale.py check ja` to fail.
- Japanese word senses containing `?`: 2,521 before this correction batch, 2,396 after.
- `python -X utf-8 tools/locale.py check ja`: passed with 0 errors.
- The Japanese word queue is not complete; 2,396 corrupted word senses remain for later manual review.
