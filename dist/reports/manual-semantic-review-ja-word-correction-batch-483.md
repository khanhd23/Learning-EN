# Japanese manual semantic review — word correction batch 483

- Reviewed 50 Japanese word senses manually against the English definitions and examples.
- Scope: figurative idioms and foundational English verbs.
- Replaced corrupted `?` examples with natural Japanese; idioms were rendered by meaning rather than literal word substitution.
- Japanese word senses containing `?`: 2,246 before this batch, 2,196 after.
- `python -X utf-8 tools/locale.py check ja`: passed with 0 errors.
- The Japanese word queue remains incomplete; 2,196 corrupted senses still require manual review.
