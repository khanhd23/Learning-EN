# B1 TOEIC content batch 26

Added the next 150 missing TSL words in rank order, from `indicator` (rank 826) through `butter`
(rank 1033). Each entry has a learner-facing definition, IPA, example, Vietnamese gloss and
example translation, plus a controlled topic assignment.

## Batch summary

- Words added: 150
- Levels: L2 = 52, L3 = 90, L4 = 8
- All 150 entries are silver; `python tools/words_batch.py tiers tools/authoring/work/b26.txt`
  reports `not silver: 0`.
- TSL missing words: 319 before → 169 after
- TSL bronze words: 75 before → 75 after
- Lesson-ready silver words: 4,494 before → 4,644 after
- Total generated word entries: 5,896 before → 6,046 after

## Topic counts in this batch

| Topic | Words |
|---|---:|
| office | 26 |
| academic_words | 20 |
| feelings_personality | 18 |
| jobs | 18 |
| describing_things | 15 |
| it_technology | 14 |
| education | 12 |
| home_furniture | 12 |
| food_drink | 11 |
| arts_entertainment | 10 |
| government_society | 9 |
| health_illness | 8 |
| sports_fitness | 7 |
| contracts_law | 6 |
| finance_accounting | 6 |
| shopping | 6 |
| transport | 6 |
| time_dates | 5 |
| core_verbs | 4 |
| events | 4 |
| language_learning | 4 |
| linking_words | 4 |
| money_banking | 4 |
| sales_customer | 4 |
| environment_energy | 3 |
| logistics | 3 |
| science_basics | 3 |
| travel_holidays | 3 |
| city_directions | 2 |
| clothes | 2 |
| hotel_restaurant | 2 |
| marketing | 2 |
| media_news | 2 |
| weather | 2 |
| age_life_stages | 1 |
| animals | 1 |
| daily_routine | 1 |
| family | 1 |
| internet_social | 1 |
| nature_landscape | 1 |

## Verification

- `python tools/gen_content.py`: pass (`words=6046`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch26.tsv`: pass;
  `rows=150 problems=0`.
- `python tools/validate_content.py`: pass; errors=0.
- `python tools/audit_content.py`: generated audit; all batch-26 entries are silver. Existing
  project-wide audit issues remain outside this batch.
- `python -m unittest discover -s tools/tests`: pass (29 tests).
- `./gradlew testDebugUnitTest assembleDebug`: pass.

The pre-existing batch 1–24 combined-check issues were not rewritten as part of this batch.
