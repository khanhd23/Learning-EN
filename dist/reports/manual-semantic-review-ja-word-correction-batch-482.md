# Japanese manual semantic review — word correction batch 482

- Reviewed 50 Japanese word senses manually against the English definitions and examples.
- Scope: conversational expressions, phrasal verbs, and common idioms.
- Replaced corrupted `?` examples with natural Japanese translations and checked that each phrasal verb or idiom retained its intended figurative meaning.
- Japanese word senses containing `?`: 2,296 before this batch, 2,246 after.
- `python -X utf-8 tools/locale.py check ja`: passed with 0 errors.
- The Japanese word queue remains incomplete; 2,246 corrupted senses still require manual review.
