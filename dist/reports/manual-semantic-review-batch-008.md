# Manual semantic review — batch 008

## Scope

I read the next 25 marketing and customer service senses against their English examples. I
rewrote 12 source definitions where the wording was too broad, overly dictionary-like, or did
not fit the example. The main corrections distinguish the two meanings of `commercial`, make
`client_s1` cover professional services rather than only lawyers, and clarify the noun meaning
of `warranty_s1`.

Hindi received a fresh gloss and example review for all 25 senses. The review also corrected
the Hindi distinctions for `image_s1`/`image_s2`, `research_s1`/`research_s2`, and
`customer_s1`/`client_s1`, while preserving the English target words in learner examples.
Source hashes were refreshed for every changed source sense in all five locales.

## Reviewed senses

`commercial_s1`, `commercial_s2`, `flyer_s1`, `demand_s1`, `demand_s2`, `attract_s1`,
`loyal_s1`, `sponsor_s1`, `publicity_s1`, `innovative_s1`, `potential_s1`, `research_s1`,
`research_s2`, `image_s1`, `image_s2`, `billboard_s1`, `exclusive_s1`, `boost_s1`,
`customer_s1`, `client_s1`, `discount_s1`, `complaint_s1`, `warranty_s1`, `purchase_s1`,
`deliver_s1`.

## Counts before/after

- English source senses: 8,837 / 8,837.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 12.
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
