# Follow-up — exam guides and generated exam data

## Scope

This follow-up completes the pending exam-guide UI work and synchronizes the generated
TOEIC bank with its source question order. The guide formatter keeps localized content intact
while presenting method text and tips as separate readable blocks. The reading-bank screen now
shows the Part 5, Part 6 and Part 7 guidance entries. Two Vietnamese THPT explanations were
also clarified so that they state the evidence and conclusion shown by their passages.

The content validator now accepts a concise learner-facing gloss when the English definition is
long; it still reports glosses that are unnecessarily longer than the source definition. A
regression test covers the concise-gloss case.

The unfinished semantic source batch was also restored after generated-data cleanup had
overwritten its working-tree changes. English source senses, their Vietnamese source hashes,
and the synchronized Spanish, Brazilian Portuguese and Vietnamese entries now match again.

## Counts

- TOEIC generated bank: 306 Part 5 items and 40 Part 6 passages, with answer indices rebuilt
  after option ordering.
- THPT Vietnamese overlay: 200 explanations retained; 2 explanations clarified.
- Exam guide formatter: added 2 unit-level behaviors for Vietnamese and English method/tip text,
  plus the three reading-bank guide keys.
- English source: 8,674 senses; audit errors reduced from the restored stale state to 0, with
  8,674 silver entries and 0 bronze entries.

## Validation

- `python -X utf-8 tools/gen_content.py`: passed; generated counts reported successfully.
- `python -X utf-8 tools/validate_content.py`: 0 errors, 0 warnings.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: 73 passed.
- `python -X utf-8 tools/locale.py check --all`: all five locales passed with 0 errors.
- `gradlew.bat testDebugUnitTest assembleDebug --no-daemon`: passed.

## Review notes

- No locale was promoted to `approved`.
- The unrelated pre-existing APK, manual Hindi question CSVs and older reports remain unstaged.
