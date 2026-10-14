# Manual semantic review — batch 034

Date: 2026-10-14
Scope: source sense indices 825–849, inclusive (25 senses), covering exercise, sports, and film vocabulary.

## Work completed

- Read each English definition and example sentence individually.
- Rewrote 25 English definitions to align with the sense shown by each example.
- Clarified the paired meanings of `work_out` and `draw`, and simplified the definitions for `episode`, `series`, and `spoiler`.
- Rewrote 25 Hindi glosses and examples in context, preserving stable IDs and English learning terms.
- Refreshed source fingerprints for these 25 senses in all five locale status files.

## Reviewed IDs

`work_out_s1`, `work_out_s2`, `jog_s1`, `gym_s1`, `muscle_s1`, `compete_s1`, `athlete_s1`, `medal_s1`, `draw_s2`, `draw_s1`, `injured_s1`, `cheer_s1`, `warm_up_s1`, `badminton_s1`, `movie_s1`, `actor_s1`, `director_s1`, `plot_s1`, `character_s1`, `episode_s1`, `series_s1`, `spoiler_s1`, `binge_watch_s1`, `song_s1`, `lyrics_s1`

## Verification

- English source senses reviewed: 850 / 8,837.
- `python -X utf-8 tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python -X utf-8 tools/audit_content.py`: passed (`errors=0`).
- `python -X utf-8 tools/locale.py check --all`: passed for es, hi, id, pt-BR, and vi.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: passed (73 tests).
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Follow-up

The corpus still requires additional manual batches. This report does not claim full completion.
