# B2 CEFR A1-B1 - batch 52

Date: 2026-10-09

## Scope

Added 25 learner-relevant CEFR B1 entries:

`stuck`, `stuffed`, `stun`, `stunning`, `stylish`, `subconscious`, `submarine`,
`suffix`, `sundial`, `superstition`, `supportive`, `supposedly`, `surgeon`,
`surrounding`, `survivor`, `swarm`, `swell`, `swollen`, `syndrome`, `takeoff`,
`talented`, `talkative`, `tasteless`, `teaching`, `technological`.

## Manual educational review

- Read all 25 generated English entries and Vietnamese locale entries after the final generation.
- Checked every definition, part of speech, General American IPA, level, and controlled topic assignment.
- Checked every English example for a complete sentence, the intended sense, and the target word or inflected form.
- Checked every Vietnamese gloss and example for accurate meaning, natural wording, and preserved diacritics.
- Checked the related adjective pairs `stuck`/`stuffed`, `stunning`/`stylish`, and `swollen`/`supportive` for distinct meanings.
- Checked the academic/science entries `subconscious`, `suffix`, `sundial`, `syndrome`, and `technological` in clear learner contexts.
- Improved the Vietnamese wording for `stuffed` ("được nhồi đầy"), `subconscious` ("thuộc tiềm thức"), and `supportive` ("biết hỗ trợ, động viên") after the manual review.
- Confirmed all 25 generated entries are silver after the final regeneration.

## Counts

| Measure | Before | After |
|---|---:|---:|
| Dictionary entries | 6,907 | 6,932 |
| Audit silver entries | 5,679 | 5,704 |
| CEFR A1-B1 missing | 322 | 297 |
| CEFR A1-B1 bronze | 116 | 116 |
| TOEIC missing | 0 | 0 |
| TOEIC bronze | 0 | 0 |

## Checks

- `python tools/words_batch.py new tools/authoring/work/b52.master.tsv 52` - pass (`25` new words).
- `python tools/words_batch.py build tools/authoring/work/b52.txt 52` - pass (`25` senses).
- `python tools/gen_content.py` - pass (`words=6932`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch52.tsv` - pass (`rows=25 problems=0`).
- `python tools/words_batch.py tiers tools/authoring/work/b52.txt` - pass (`not silver: 0`).
- Manual generated-entry QA script - pass (`25/25` definitions, examples, glosses, topics, and silver tiers checked).
- `python tools/validate_content.py` - pass (`errors=0`; `247` existing warnings).
- `python tools/audit_content.py` - no new audit errors from this batch; project total is `silver=5704 bronze=1228 errors=3012`.
- `python tools/word_gaps.py cefr A1 A2 B1` - pass; remaining `MISSING 297`, `BRONZE 116`.
- `python tools/word_gaps.py tsl` - pass; `MISSING 0`, `BRONZE 0`.
- `python -m unittest discover -s tools/tests` - pass (`32` tests; locale `xx` errors `0`).
- `./gradlew.bat check assembleDebug` - pass (`BUILD SUCCESSFUL`).
- `git diff --check` - pass.

## Remaining work

The CEFR A1-B1 source still contains 297 missing entries and 116 bronze entries before
intentional duplicate/variant skips. Future batches must continue to select learner-relevant
words and skip duplicates, abbreviations, proper nouns, and source-only variants.
