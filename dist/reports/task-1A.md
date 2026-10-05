# Task 1A report — schema upgrade and quarantine

Date: 2026-10-05

## Result

Task 1A was applied through `tools/gen_content.py`, so the generated JSON remains reproducible.
The English core now has 4,618 words, 4,778 senses and 5,480 examples. Every word has
`levelReason`, `forms`, `grammarIds` and `tier`; every sense has a stable `<word>_s<n>` ID,
English `def` and `register`; every example has `hl`.

Vietnamese question notes/translations, legacy Vietnamese collocations/examples and legacy
grammar formulas were moved into `content/i18n/vi.json` quarantine/localization fields. The
English core audit reports zero non-English strings. Vietnamese glosses are normalized only when
safe; meanings were not truncated. IPA slashes and syllable dots are removed, while uncertain
symbols remain visible as audit `ipa_format` issues for later editorial review.

`ContentRepository` parses the new fields. `SessionBuilder`, `PracticeBuilder` and
`ExerciseFactory` use only silver/gold lesson candidates; the full word list remains available
for dictionary/search use. Bronze entries therefore remain dictionary data and are not lesson
candidates.

The report-only `auditContent` task is now part of `gradlew check`. No gold entries were created.

## Audit summary

| metric | Before Task 1A | After Task 1A |
|---|---:|---:|
| entries | 4,618 | 4,618 |
| gold / silver / bronze | 0 / 0 / 4,618 | 0 / 1,058 / 3,560 |
| NGSL lemmas missing | 718 | 718 |
| audit errors (all categories) | 27,121 | 8,910 |
| non-English strings/fields in `content/en` | 114 | 0 |
| `sense_id_missing` | present in baseline | 0 |
| `def_missing` | present in baseline | 0 |
| Level 1 silver/gold lesson words | — | 342 |

The remaining errors are pre-existing content-quality work for later planned tasks: imported
fragment examples, catch-all topics, long/duplicate glosses and invalid IPA. The Task 1A schema
and quarantine criteria are complete; the plan's 600-word Level 1 usability target remains
below target and is intentionally deferred to the planned content rewrite/topic tasks rather than
inventing definitions or topic assignments here.

## Verification

- `python tools/gen_content.py --check` — passed.
- `python tools/validate_content.py` — `errors=0 warnings=0`.
- `python tools/audit_content.py` — passed in report-only mode; summary above.
- `./gradlew check` — passed.
- `./gradlew assembleDebug` — passed.
- Debug APK: [app-debug.apk](/C:/Learning%20EN%20App/app/build/outputs/apk/debug/app-debug.apk), 10,376,388 bytes (~9.90 MiB).

## Files of note

- `tools/gen_content.py` — deterministic schema upgrade, definitions, normalization and quarantine.
- `tools/sources/wordnet_defs.json` — draft definition source map; missing matches remain marked
  `needs_review`.
- `content/LICENSES.md` — WordNet source attribution.
- `app/src/main/java/com/yourbrand/englishlearn/content/Models.kt`
- `app/src/main/java/com/yourbrand/englishlearn/content/ContentRepository.kt`
- `app/build.gradle.kts`

Task 1A is ticked. No later task was started.
