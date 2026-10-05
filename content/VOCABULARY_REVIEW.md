# Vocabulary audit and expansion — 2026-10-04

## Assessment

The original bank had 969 unique words/phrases across 34 topics. It provided a useful
workplace and everyday starter collection, but omitted basic verbs (say, tell, make,
do, hear, listen), time words and household nouns. Several existing confusable sets
had null word-detail links. Only 155 entries were at internal Level 1.

## This update

- Added 292 independently authored words/phrases, bringing the total to 1,261.
- Added six focused topics: core verbs (60), time/numbers (54), household basics
  (50), descriptions/comparisons (46), connecting/function words (32), and
  essential study/work vocabulary (50).
- Every added entry includes IPA, Vietnamese meaning, an English example with
  Vietnamese translation, and two usage phrases. Selected entries also have
  additional senses, word-family information and Vietnamese contrast notes.
- All 32 existing confusable sets now resolve to word details in both directions.
- Existing word IDs, levels, meanings and examples remain intact, preserving
  saved words and learning progress.
- New entries carry an explicit source and `needs_review: true`. Review IPA,
  register, level, meaning and examples before publication. No external list or
  dictionary example corpus was imported.

## Coverage and remaining gaps

| Internal level | Before | After |
|---|---:|---:|
| 1 | 155 | 370 |
| 2 | 247 | 301 |
| 3 | 375 | 396 |
| 4 | 147 | 149 |
| 5 | 45 | 45 |

This is a stronger beginner collection, not a complete v1 curriculum. The skill's
2,500–3,500-word target still requires at least 1,239 more unique entries. Next
editorial batches should deepen existing topics (family/relationships, food,
transport, common adjectives and verbs), then expand Level 4–5 academic/workplace
usage. Do not meet the target by counting inflections, repeated meanings or example
sentences as new vocabulary entries.

The 641 authored questions, eight passages and 38 grammar points are unchanged.
Runtime vocabulary activities can use the new words through the existing factory.
This task does not claim to complete the skill's broader grammar/exam targets.

## Reproduce

From the project root (paths containing spaces are supported):

```powershell
python tools/gen_content.py
python tools/gen_content.py --check
python tools/validate_content.py
python -m unittest discover -s tools -p test_vocab_expansion.py
.\gradlew.bat assembleDebug validateContent
```

Edit `tools/authoring/vocab_foundation.tsv` and `.py`, then regenerate the JSON.
The validator writes current counts and the pending review list into `dist/`.

## Verification result

- Content generation consistency check passed.
- Validator: zero errors and zero warnings.
- Three regression tests passed (earlier words preserved, new bilingual entries
  complete, confusable links resolve in both directions).
- Gradle generated the updated content assets and passed `validateContent`.
- APK assembly failed at Kotlin compilation because the existing project has no
  `app/src/main/res/values/strings.xml`; UI references such as `tab_today` and
  `tab_vocab` cannot resolve. No new APK or device-level verification is claimed.
