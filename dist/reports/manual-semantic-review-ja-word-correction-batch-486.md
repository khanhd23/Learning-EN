# Japanese manual semantic review — word correction batch 486

- Reviewed 50 Japanese word senses manually against the English definitions and examples.
- Scope: household objects, tableware, food and drink, meals, and basic adjectives.
- Checked countable expressions, word-sense distinctions such as `glass` material versus drinking container, `fish` animal versus food, and `chicken` food versus bird.
- Japanese word senses containing `?`: 2,096 before this batch, 2,046 after.
- `python -X utf-8 tools/locale.py check ja`: passed with 0 errors.
- The Japanese word queue remains incomplete; 2,046 corrupted senses still require manual review.
