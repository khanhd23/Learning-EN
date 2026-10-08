# Task E — stories to 30

Date: 2026-10-09

## Delivered

- Expanded the authored story set from 16 to 30 stories in `tools/authoring/stories.json`.
- Generated the English and Vietnamese story packs through `python tools/gen_content.py`.
- Every story has a Vietnamese title and text, 2 explained comprehension questions, and 3 or more options per question.
- Every story is 70–130 English words and uses 5–8 silver lesson-word targets.
- No new UI string keys were needed.

## Coverage

| Level | Stories |
|---|---:|
| Level 1 | 14 |
| Level 2 | 15 |
| Level 3 | 1 |
| Total | 30 |

The authored set covers 21 topics, including family, food, education, shopping, weather, travel,
home, animals, health, city directions, jobs, restaurant, sports, clothes, transport, feelings,
banking, office, nature, technology, and shopping.

## Manual educational QA

- Read all 14 newly authored stories in English and Vietnamese after generation.
- Checked that each target word is used in the story's meaning, not merely inserted as an isolated word.
- Re-solved every new question from the final option order and checked the answer, English explanation,
  and Vietnamese explanation against the story.
- Checked Vietnamese wording for natural meaning and consistency with the English event sequence.
- Reviewed target counts across all 30 stories and reduced older over-targeted rows to the required 5–8 range.
- No ambiguous answer, unsupported target, missing translation, or malformed question remained.

## Checks

- `python tools/gen_content.py` — passed (`topics=47 words=6532 confusables=32 grammar=38 questions=830 passages=40 petMoods=18`).
- Manual story structure check — passed (30 stories; 70–130 words; 5–8 silver targets; 2 questions each; Vietnamese explanations present).
- `python tools/validate_content.py` — passed (`errors=0`; existing warnings: 245).
- `python -m unittest discover -s tools/tests` — passed (32 tests; locale `xx` errors=0).
- `./gradlew.bat testDebugUnitTest assembleDebug` — passed.
