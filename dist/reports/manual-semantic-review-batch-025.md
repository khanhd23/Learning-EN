# Manual semantic review — batch 025

Date: 2026-10-14
Scope: source sense indices 600–624, inclusive (25 senses), covering news and media.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English source definitions where a shorter, clearer definition better represented the sense.
- Rewrote 25 Hindi glosses and examples in context, including separate meanings for article, report, press, issue, and column.
- Refreshed the source fingerprint for these 25 senses in all five locale status files.
- No source word, sense ID, example ID, or question ID was renamed or reordered.

## Reviewed IDs

`temperature_s1`, `news_s1`, `article_s1`, `article_s2`, `journalist_s1`, `headline_s1`, `broadcast_s1`, `editor_s1`, `publish_s1`, `magazine_s1`, `report_s1`, `report_s2`, `press_s1`, `press_s2`, `channel_s1`, `subscribe_s1`, `advertisement_s1`, `source_s1`, `fake_news_s1`, `documentary_s1`, `reporter_s1`, `column_s1`, `issue_s1`, `issue_s2`, `viral_s1`

## Verification

- English source senses reviewed: 625 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
