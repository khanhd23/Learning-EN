# Manual semantic review — batch 035

Date: 2026-10-14
Scope: source sense indices 850–874, inclusive (25 senses), covering music, entertainment, and body vocabulary.

## Work completed

- Read each English definition and example sentence individually.
- Corrected the music sense of `band_s1`, the recording sense of `album_s1`, the genre sense of `horror_s1`, and the return-direction sense of `back_s2`.
- Rechecked the remaining 21 senses against their Hindi examples and retained accurate wording.
- Updated the affected Hindi glosses/examples and refreshed source fingerprints for all 25 senses in five locale status files.

## Reviewed IDs

`catchy_s1`, `concert_s1`, `band_s1`, `album_s1`, `playlist_s1`, `game_s1`, `addictive_s1`, `comedy_s1`, `horror_s1`, `cartoon_s1`, `ticket_s1`, `trailer_s1`, `review_s1`, `review_s2`, `perform_s1`, `subtitles_s1`, `streaming_s1`, `fan_club_s1`, `masterpiece_s1`, `head_s1`, `head_s2`, `shoulder_s1`, `knee_s1`, `back_s1`, `back_s2`

## Verification

- English source senses reviewed: 875 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
