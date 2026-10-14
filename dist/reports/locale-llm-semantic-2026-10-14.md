# Locale semantic conversion report — 2026-10-14

## Scope

This pass used the English core as the source for every correction. The repaired entries were
written and reviewed as semantic content for learners; no locale was translated through another
learner language, and no entry was marked approved.

## Changes

- Indonesian words: completed the final 57 missing senses, bringing the locale to 8,837/8,837
  reviewed word senses.
- Indonesian Level 1 learning support: reviewed 47 topic names, 8 grammar points, and 32
  confusable sets. Added Indonesian L1 notes for false friends, pronunciation, and grammar.
- Indonesian support copy: translated 10 exam strategy tips and 144 pet lines from the English
  source into natural Indonesian, preserving {name} placeholders.
- Indonesian pilot UI: translated all 728 UI strings, preserving Android placeholders and symbols.
- Indonesian Level 1 questions: translated all 112 Level 1 answer explanations, preserving the
  English answer words and grammar forms being taught.
- Spanish: repaired 11 damaged or semantically wrong word entries found during the focused sample
  review, including grind_s1, wink_s1, trifle_s1, aka_s1, henry_s1, rum_s1, slumber_s1,
  justified_s1, nationwide_s1, chum_s1, and whereon_s1.
- Portuguese (Brazil): repaired 30 damaged or semantically wrong word entries, including the
  fan, glass, reply, naked, proxy, and formal verb examples whose diacritics or verb forms had
  been corrupted.

All new review CSV rows use reviewer codex-llm-semantic-2026-10-14 and remain reviewed.

## Current completion state

| Locale | Reviewed | Missing | Approved |
|---|---:|---:|---:|
| Spanish | 10,618 tracked entries | 0 | 0 |
| Portuguese (Brazil) | 10,618 tracked entries | 0 | 0 |
| Indonesian pilot content | 9,764 reviewed entries | 40 passages and 36 higher-level grammar points | 0 |
| Vietnamese | existing complete pack | 0 | existing owner state |

Indonesian now has the pilot's words, topics, UI, Level 1 grammar, Level 1 questions, pet lines,
tips, and confusable guidance. It is still not release-ready: 40 passage explanations and 36
higher-level grammar points still need semantic translation. Indonesian is not selectable or
bundled.

Spanish and Portuguese packs also remain unapproved and are not selectable or bundled. Spanish
Level 4 includes a previously identified group of fragmentary examples that needs a separate
content rewrite; this pass did not manufacture sentences merely to satisfy punctuation checks.

## Checks

- python tools/locale.py check --all — passed, 0 errors for es, id, pt-BR, and vi.
- python tools/validate_content.py — passed, 0 errors and 0 warnings.
- python tools/audit_content.py — passed, 8,674 silver entries, 0 errors.
- python -m unittest discover -s tools/tests — passed, 73 tests.
- gradlew.bat check assembleDebug --no-daemon — passed.

## Human review still required

The owner must review these semantic batches before changing any status to approved. A native
Indonesian reviewer is still needed for the pilot, and the Spanish/Portuguese packs need native
review before release.
