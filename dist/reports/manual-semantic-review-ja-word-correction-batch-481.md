# Japanese manual semantic review — word correction batch 481

- Reviewed 100 Japanese word senses manually against the English definitions and examples.
- Scope: clothing, city directions, social media, devices, and conversational expressions.
- Replaced corrupted `?` example text with natural Japanese while preserving stable IDs and English terms that remain instructional (`hot`, `cold`, `ATM`).
- Japanese word senses containing `?`: 2,396 before this batch, 2,296 after.
- `python -X utf-8 tools/locale.py check ja`: passed with 0 errors.
- The Japanese word queue remains incomplete; 2,296 corrupted senses still require manual review.
