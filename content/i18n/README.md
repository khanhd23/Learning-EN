# Locale packs

`vi/` is the only complete, shippable locale currently. Other locale folders are generated
scaffolds: they contain every content ID but blank translations and a `status.json` with
`todo: true`.

A locale may be copied into the Android asset bundle only after:

- UI strings are fully translated;
- at least 95% of word glosses and 100% of question explanations are reviewed by a native speaker;
- examples, register, exam terminology and cultural references are checked;
- script/RTL rendering, store listing, privacy policy and screenshots are reviewed;
- `tools/validate_content.py` and locale completeness checks pass.

Do not fill these files with guessed or unreviewed machine translations and ship them as finished
learning content.

## Country reuse

`market_profiles.json` maps countries to a language pack and a regional variant. The shared pack holds
English-learning meanings and explanations; a market profile adds goals or regional review notes without
duplicating thousands of words. Country-specific claims, exam facts and cultural examples must be added as
separately sourced, reviewed data.
