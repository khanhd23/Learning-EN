# Semantic review: Spanish and Brazilian Portuguese

Date: 2026-10-13

## Result

Both locale packs now have every tracked locale entry populated and marked
`reviewed`:

| Locale | Tracked entries | Reviewed | Draft/missing | Approved |
|---|---:|---:|---:|---:|
| `es` (Latin-American Spanish) | 10,618 | 10,618 | 0 | 0 |
| `pt-BR` (Brazilian Portuguese) | 10,618 | 10,618 | 0 | 0 |

Tracked content includes 8,837 word senses, 47 topics, 32 confusable sets,
44 grammar points, 890 question explanations, 40 passage explanations, and
728 UI strings per locale.

## Manual review evidence

- Spanish used the repository's 357 manually authored word batches (8,837
  unique word-sense IDs), plus the manually authored question, grammar, topic,
  confusable, and UI batches. Duplicate word rows were applied in numeric batch
  order so later correction batches win.
- The Brazilian Portuguese phase-1 report records the manual pass over all
  8,837 words, 47 topics, 32 confusables, 44 grammar points, 890 question
  explanations, 40 passage explanations, and 728 UI strings. This pass was
  retained and the status hashes were synchronized.
- The current pass also corrected the clearly mismatched Spanish glosses for
  `grind`, `trifle`, `dressing`, `lee`, `tom`, and related long glosses (23
  glosses total), and retained the earlier Brazilian Portuguese corrections for
  context-sensitive senses such as `fan`, `lose`, `glass`, `answer`, `move`,
  `reply`, `coach`, `update`, `taste`, and `since`.
- Every `reviewed` entry has a current English-source hash; every locale value
  is non-empty.

## Release gate

`reviewed` means the content pass is complete. It is deliberately not
`approved`: a native Spanish reviewer and a native Brazilian-Portuguese
reviewer must still approve the packs before the locale can be bundled for
release. The locale status remains incomplete for that reason.

## Verification

- `python tools/validate_content.py`: PASS — errors 0, warnings 0.
- `python tools/locale.py check --all`: PASS — `es`, `pt-BR`, and `vi` all 0
  errors.
- `python tools/audit_content.py`: PASS — 8,674 silver, 0 errors.
- `python -m unittest discover -s tools/tests`: PASS — 73 tests.
- `gradlew.bat testDebugUnitTest assembleDebug --no-daemon`: PASS.
