# Current content data review

This review covers the current lesson-ready target in \`docs/ROADMAP_V1.md\`:
CEFR A1-B2. Batches 106-127 added 523 manually authored and reviewed rows.

## Lesson data

- Lesson dictionary entries: 8,674.
- Silver: 7,562.
- Bronze: 1,112.
- NGSL-rank missing: 0.
- TSL gaps: 0.
- Audit errors: 2,709, unchanged historical findings present before these
  batches.

## Remaining CEFR gaps

- A1: 0.
- A2: \`handicapped\` — intentionally excluded as outdated and potentially
  offensive terminology.
- B1: \`hearted\` — intentionally excluded because it is not useful as a
  standalone lesson word; it normally appears in compounds such as
  \`kind-hearted\`.
- B2: \`gook\` — intentionally excluded because it is an ethnic slur.

The remaining three words are documented exclusions, not silently missing
lesson content.

C1 and C2 source gaps remain outside the current A1-B2 lesson-ready target:
757 C1 and 843 C2 source headwords still need editorial review. They should
not be mass-promoted from the source archive into lessons.

## Manual and automated checks

- Every row in batches 106-127 was checked for lemma, part of speech,
  definition, Vietnamese gloss, example meaning, topic, level, and IPA.
- Every batch editor check: 0 problems.
- Every batch tier check: 0 not-silver after fixes.
- \`validate_content.py\`: 0 errors, 244 warnings.
- \`locale.py check --all\`: Vietnamese 0 errors; test locale \`xx\` 0 errors.
- \`python -m unittest discover -s tools/tests\`: 32 tests passed.
- \`./gradlew.bat check testDebugUnitTest assembleDebug\`: BUILD SUCCESSFUL.

The 244 validator warnings are existing glossary-length warnings and no new
warning matches the latest manually reviewed batches.
