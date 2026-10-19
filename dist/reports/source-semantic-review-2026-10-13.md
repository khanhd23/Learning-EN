# English source semantic review

Date: 2026-10-13

## Scope

The English source was reviewed in learner-facing batches. For each changed sense I
checked the meaning against the part of speech and example context, replaced placeholder
or source-editorial definitions, corrected malformed IPA, assigned an existing topic,
and synchronized the affected Vietnamese, Latin-American Spanish, and Brazilian
Portuguese glosses/examples. Locale entries touched by a source change remain `draft`
for native review.

The audit was used to locate and verify structural problems; a zero audit result was not
treated as proof of semantic correctness. The final pass manually covered the remaining
audit candidates through batches 343-347, including the final IPA, example, topic, and
gloss corrections.

## Result

- Source entries: 8,674.
- Silver: 8,674.
- Bronze: 0.
- Gold: 0.
- NGSL missing: 0.
- Audit errors: 0.
- Validator errors: 0; validator warnings: 0.
- Locale checks: `es`, `pt-BR`, and `vi` each report `errors: 0`.

The final manual cleanup included rewording definitions that still matched legacy
WordNet text, replacing placeholder/fragment examples, correcting invalid IPA such as
parenthesized or partial forms, moving catch-all topics to existing specific topics,
and shortening learner glosses that contained excessive alternatives. No word IDs were
renamed or removed.

## Verification

- `python -m unittest discover -s tools/tests`: 73 tests passed.
- `python tools/validate_content.py`: PASS, `errors=0`, `warnings=0`.
- `python tools/locale.py check --all`: PASS, all three locales `errors=0`.
- `python tools/audit_content.py --locale vi`: PASS, `errors=0`.
- `./gradlew testDebugUnitTest assembleDebug --no-daemon`: PASS.
- `git diff --check`: PASS.

Native review is still recommended for all locale drafts before release. This report
records the data-quality gate as clean, not a replacement for native-language review.
