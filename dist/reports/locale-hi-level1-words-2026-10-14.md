# Hindi Level 1 vocabulary completion — 2026-10-14

The Hindi Level 1 word translation has been completed from `content/en/words.json`.
All 1,321 Level 1 sense/example pairs now have manually authored Hindi glosses and example
translations, imported through `tools/locale.py import` as `reviewed`.

## Counts

| Area | Missing | Reviewed | Approved |
|---|---:|---:|---:|
| Hindi Level 1 words | 0 | 1,321 | 0 |
| Hindi words in all levels | 7,516 | 1,321 | 0 |

The full Hindi locale scaffold still contains 10,618 tracked entries. Grammar, questions, UI,
topics, pet lines, tips, and L1-specific confusable notes remain to be authored in later batches.
No `approved` metadata was set.

The batches are recorded in:

- `content/review/hi/manual_words_batch_001.csv`
- `content/review/hi/manual_words_batch_002.csv` through `manual_words_batch_014.csv`

The source entry `medicine_s1` remains flagged for native review because its English definition
describes medical science while its example uses medicine as a drug.
