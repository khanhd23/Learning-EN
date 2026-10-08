# B1 TOEIC manual QA: batches 25–28

I manually reviewed every authored sense in the B1 TOEIC batches from batch 25
through the current batch 28 output. The review checked the part of speech against
the definition and example, English usage and lemma highlighting, Vietnamese gloss
and example meaning, spelling, and learner-facing naturalness.

## Scope and result

| Batch | Authored senses | Unique words | Result |
|---:|---:|---:|---|
| 25 | 150 | 150 | reviewed |
| 26 | 150 | 150 | reviewed |
| 27 | 169 | 169 | reviewed |
| 28 | 76 | 75 | reviewed |
| **Total** | **545** | **544** | **complete** |

Batch 28 has two senses for `downtown`; all other rows have one sense. No word
definition, topic, level, list membership, or tier was changed by the manual review.

## Corrections made

- `advisory_s1`: changed the example to “The airport issued an advisory notice
  about strong winds.” so the authored adjective sense is used correctly.
- `portable_s1`: improved the Vietnamese example to “Bộ sạc di động này dễ dàng
  nằm gọn trong túi du lịch.”
- `junk_s1`: corrected the standard compound spelling `junkyard`.
- `layoff_s1`: replaced the awkward singular-person example with “The factory
  announced a layoff after orders fell.”
- `asleep_s1`: corrected the authored part of speech from `adv` to `adj`.

The corrections were made in the authoring TSV files and included in generated
content by `python tools/gen_content.py`; generated content files were not edited
by hand.

## TOEIC coverage and tier checks

- Generated word entries: 6,215.
- `python tools/word_gaps.py tsl`: `MISSING 0`, `BRONZE 0`.
- `python tools/words_batch.py tiers tools/authoring/work/b25.txt`: `not silver: 0`.
- `python tools/words_batch.py tiers tools/authoring/work/b26.txt`: `not silver: 0`.
- `python tools/words_batch.py tiers tools/authoring/work/b27.txt`: `not silver: 0`.
- `python tools/words_batch.py tiers tools/authoring/work/b28.txt`: `not silver: 0`.

The generated JSON tier counts are unchanged from the pre-review output:
4,889 silver and 1,326 bronze entries. The bronze entries are outside these
completed B1 TOEIC batches.

## Verification

- `python tools/check_editor_batch.py` on batches 25–28: pass; respectively
  `150/0`, `150/0`, `169/0`, and `76/0` rows/problems.
- `python tools/gen_content.py`: pass; `words=6215`.
- `python tools/validate_content.py`: pass; `errors=0`, 247 existing warnings.
- `python -m unittest discover -s tools/tests`: pass; 30 tests, `OK`.
- `./gradlew.bat testDebugUnitTest assembleDebug`: pass; `BUILD SUCCESSFUL`.
- `python tools/audit_content.py`: completed with
  `entries=6215 gold=0 silver=4888 bronze=1327 ngsl_missing=0 errors=3251`.
  The project-wide audit errors are pre-existing and are outside these four
  batches; the batch checks and content validator pass.
