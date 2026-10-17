# Korean manual semantic review batch 2

- Scope: 1540 Korean word senses and example sentences manually reviewed against the English source.
- Method: each gloss and example was written in natural Korean while preserving the taught English item, stable IDs, and required placeholders.
- Locale validation: `python tools/locale.py check ko` passed with 0 errors.
- Overall Korean word status after this batch: 8117 reviewed, 720 missing.
- Human review note: entries marked reviewed in this batch were checked for meaning, part-of-speech fit, and example-level correspondence; remaining missing entries require the same treatment.
