# Manual semantic review — batch 007

## Scope

I read the next 25 finance and marketing senses against their English examples. I rewrote 14
source definitions to remove dictionary-like wording and correct mismatches. The key fixes were
the verb and noun distinction for `launch_s1`/`launch_s2`, the financial-statement meaning of
`balance_sheet_s1`, and the public-label meaning of `brand_s2`.

Hindi received a fresh gloss and example review for all 25 senses. The review also clarified
`advertise`, `promotion`, `competitor`, `logo`, `slogan`, and `audience` so each meaning matches
the example and remains distinct from nearby senses. Source hashes were refreshed for every
changed source sense in all five locales.

## Reviewed senses

`accurate_s1`, `fiscal_year_s1`, `bill_s1`, `balance_sheet_s1`, `expenditure_s1`,
`advertise_s1`, `campaign_s1`, `brand_s1`, `brand_s2`, `target_s1`, `target_s2`, `survey_s1`,
`survey_s2`, `competitor_s1`, `launch_s1`, `launch_s2`, `promotion_s1`, `consumer_s1`,
`market_share_s1`, `strategy_s1`, `logo_s1`, `slogan_s1`, `feedback_s1`, `trend_s1`,
`audience_s1`.

## Counts before/after

- English source senses: 8,837 / 8,837.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 14.
- Locale wording changes: hi 25.
- Status hashes refreshed for all five locales on the changed source senses.

## Checks

- `python -X utf-8 tools/audit_content.py`: 0 errors.
- `python -X utf-8 tools/validate_content.py`: 0 errors, 0 warnings.
- `python -X utf-8 tools/locale.py check --all`: all locales passed.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: 73 passed.
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Remaining review

The complete 8,837-sense corpus still requires additional manual batches. This report covers
only this 25-sense pass.
