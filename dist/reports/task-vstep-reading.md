# VSTEP-style reading starter bank

## Scope

- Added three complete original practice papers: 40 questions per paper.
- Each paper has four reading passages with 10 multiple-choice questions each.
- Total: 12 passages and 120 questions.
- The app exposes the papers as `vstep_practice_1`, `vstep_practice_2`, and
  `vstep_practice_3`, each timed at 60 minutes.
- The VSTEP PDF in `config/exam_formats.json` is used only for format facts.
  Passages, questions, options, answers, and explanations were written from
  scratch; no official paper or sample text was copied.

## Review

- Reviewed all 120 answer keys against their passage evidence.
- Reviewed all 120 distractor sets for four unique options and one supported
  answer, including vocabulary and reference-word questions.
- Reviewed all 120 English explanations and Vietnamese explanations.
- Correct-option positions are deterministically shuffled in the generated bank
  so answer position is not a cue.
- No external content licence is used; the bank is marked
  `original:exam-expansion-2026` and is not an official VSTEP test.

## Checks

- `python tools/gen_content.py`: passed; generated `vstep.json` with 12 groups
  and 120 items.
- `python tools/validate_content.py`: `errors=0`, `warnings=244` (existing
  Vietnamese gloss warnings).
- `python tools/audit_content.py`: `entries=8674`, `silver=7562`,
  `bronze=1112`, `errors=2709` (unchanged vocabulary baseline).
- `python tools/locale.py check --all`: passed for `vi`.
- `python -m unittest discover -s tools/tests`: 50 tests passed.
- `./gradlew.bat check assembleDebug`: `BUILD SUCCESSFUL`.

## Remaining scope

This is the requested three-paper starter set. It does not claim the long-term
80-text VSTEP target; further papers should be added only after the same answer,
duplicate, provenance, and manual-review checks.
