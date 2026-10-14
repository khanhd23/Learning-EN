# Hindi locale batch 001 — 2026-10-14

## Scope

Started the Hindi learner-language pack from the English source of truth with `python tools/locale.py new hi`.
The new pack contains the complete locale schema and 10,618 tracked entries, all initially marked `missing`.

This batch manually authored and imported 100 Level 1 word sense/example translations into Hindi. Each gloss
and example was written from the corresponding English definition and example, then imported through
`tools/locale.py import` with reviewer `codex-llm-semantic-hi-2026-10-14`. No other learner locale was used
as a translation source.

The batch also adds Hindi-specific learning notes in `content/i18n/hi/l1_notes.json`:

- 4 transfer/confusion notes (`do/make`, `say/tell`, `borrow/lend`, `since/for`)
- 5 pronunciation notes (Hindi /v/-/w/, English dental fricatives, consonant clusters, /ɪ/-/iː/, and /f/)
- 7 grammar notes (articles, word order, prepositions, age, weather subject, do/make, and plural agreement)

## Counts

| Area | Before | After |
|---|---:|---:|
| Hindi tracked entries | 10,618 missing | 10,518 missing, 100 reviewed |
| Hindi Level 1 word senses | 1,321 missing | 1,221 missing, 100 reviewed |
| Hindi Level 1 grammar points | 8 missing | 8 missing |
| Hindi Level 1 question explanations | 112 missing | 112 missing |
| Hindi approved entries | 0 | 0 |

The remaining entries stay missing until their Hindi wording is authored and semantically reviewed. This batch
does not enable Hindi in the app and does not set any `approved` metadata.

## Checks

- `python -X utf8 tools/locale.py check --all` — passed for es, hi, id, pt-BR, and vi.
- `python -X utf8 tools/validate_content.py` — `errors=0 warnings=0`.
- `python -X utf8 tools/audit_content.py` — `errors=0`.
- `python -X utf8 -m unittest discover -s tools/tests` — 73 tests passed.
- `./gradlew.bat check assembleDebug --no-daemon` — `BUILD SUCCESSFUL`.

## Human review needed

The source entry `medicine_s1` defines medical science while its English example uses medicine as a drug;
the Hindi gloss records both senses and needs a native reviewer to confirm the intended learner treatment.
The Hindi gendered verb forms and loanword choices also need native review before approval.
