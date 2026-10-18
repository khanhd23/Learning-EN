# Japanese manual semantic review — word correction batch 484

- Reviewed 50 Japanese word senses manually against the English definitions and examples.
- Scope: foundational perception/action verbs, common daily actions, and time words.
- Preserved sense distinctions such as `listen`/`hear`, `look`/`see`/`watch`, verb/noun `answer`, and the two senses of `change` and `second`.
- Japanese word senses containing `?`: 2,196 before this batch, 2,146 after.
- `python -X utf-8 tools/locale.py check ja`: passed with 0 errors.
- The Japanese word queue remains incomplete; 2,146 corrupted senses still require manual review.
