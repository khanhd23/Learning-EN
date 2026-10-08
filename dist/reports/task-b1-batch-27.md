# B1 TOEIC content batch 27

Added the final 169 missing TSL words in rank order, from `centimeter` through
`videoconference`. Each entry has a learner-facing definition, IPA, example, Vietnamese
gloss and example translation, plus a controlled topic assignment.

## Batch summary

- Words added: 169
- Levels: L2 = 68, L3 = 97, L4 = 4
- All 169 entries are silver; `python tools/words_batch.py tiers tools/authoring/work/b27.txt`
  reports `not silver: 0`.
- The source lemma `by-law` is intentionally normalized by the generator to the content ID
  `by_law`; it is included in the 169 generated silver entries.
- TSL missing words: 169 before → 0 after
- TSL bronze words: 75 before → 75 after
- Lesson-ready silver words: 4,644 before → 4,813 after
- Total generated word entries: 6,046 before → 6,215 after

## Topic counts in this batch

| Topic | Words |
|---|---:|
| health_illness | 24 |
| academic_words | 18 |
| feelings_personality | 18 |
| describing_things | 15 |
| home_furniture | 15 |
| food_drink | 14 |
| education | 16 |
| it_technology | 20 |
| office | 16 |
| jobs | 13 |
| contracts_law | 9 |
| media_news | 9 |
| transport | 9 |
| travel_holidays | 9 |
| numbers_quantity | 8 |
| arts_entertainment | 7 |
| environment_energy | 7 |
| language_learning | 7 |
| sports_fitness | 7 |
| finance_accounting | 6 |
| time_dates | 6 |
| events | 5 |
| government_society | 5 |
| shopping | 5 |
| nature_landscape | 4 |
| science_basics | 4 |
| weather | 4 |
| city_directions | 3 |
| clothes | 3 |
| core_verbs | 3 |
| hotel_restaurant | 3 |
| logistics | 3 |
| body_appearance | 2 |
| daily_routine | 2 |
| internet_social | 2 |
| linking_words | 2 |
| marketing | 2 |
| money_banking | 2 |
| sales_customer | 2 |
| animals | 1 |
| family | 1 |
| friends_relationships | 1 |
| function_words | 1 |

## Verification

- `python tools/gen_content.py`: pass (`words=6215`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch27.tsv`: pass;
  `rows=169 problems=0`.
- `python tools/validate_content.py`: pass; errors=0 (256 existing warnings).
- `python tools/word_gaps.py tsl`: pass; missing=0 and bronze=75.
- `python tools/audit_content.py`: generated audit (`entries=6215 gold=0 silver=4813
  bronze=1402 errors=3393`). The project-wide audit errors are pre-existing outside this
  batch; all batch-27 entries are silver.
- `python -m unittest discover -s tools/tests`: pass (29 tests).
- `./gradlew testDebugUnitTest assembleDebug`: pass.

The pre-existing batch 1–24 combined-check issues were not rewritten as part of this batch.
