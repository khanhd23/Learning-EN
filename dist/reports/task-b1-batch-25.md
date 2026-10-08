# B1 TOEIC content batch 25

Added the first 150 missing TSL words in TSL rank order, from `unspecified` (rank 590) through
`hesitant` (rank 825). The batch contains learner-facing English definitions, IPA, examples,
Vietnamese glosses and example translations, with controlled topic assignments.

## Batch summary

- Words added: 150
- Levels: L2 = 57, L3 = 72, L4 = 21
- All 150 entries are silver; `python tools/words_batch.py tiers tools/authoring/work/b25.txt`
  reports `not silver: 0`.
- TSL missing words: 469 before → 319 after
- TSL bronze words: 75 before → 75 after
- Lesson-ready silver words: 4,344 before → 4,494 after
- Total generated word entries: 5,746 before → 5,896 after

## Topic counts in this batch

| Topic | Words |
|---|---:|
| office | 22 |
| describing_things | 21 |
| academic_words | 16 |
| feelings_personality | 15 |
| education | 13 |
| jobs | 13 |
| health_illness | 13 |
| home_furniture | 13 |
| food_drink | 10 |
| language_learning | 8 |
| time_dates | 8 |
| transport | 8 |
| it_technology | 7 |
| arts_entertainment | 7 |
| sports_fitness | 6 |
| travel_holidays | 6 |
| contracts_law | 6 |
| numbers_quantity | 6 |
| hotel_restaurant | 5 |
| finance_accounting | 5 |
| friends_relationships | 4 |
| media_news | 4 |
| internet_social | 4 |
| sales_customer | 4 |
| government_society | 4 |
| city_directions | 3 |
| shopping | 11 |
| environment_energy | 3 |
| hr_recruiting | 3 |
| linking_words | 3 |
| money_banking | 3 |
| nature_landscape | 3 |
| weather | 2 |
| science_basics | 2 |
| core_verbs | 2 |
| daily_routine | 2 |
| events | 2 |
| body_appearance | 2 |
| clothes | 1 |
| family | 1 |
| logistics | 1 |
| marketing | 2 |

## Verification

- `python tools/gen_content.py`: pass (`words=5896`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch25.tsv`: pass;
  `rows=150 problems=0`.
- `python tools/validate_content.py`: pass; errors=0.
- `python tools/audit_content.py`: generated audit; existing project-wide audit issues remain
  outside this batch, while all batch-25 entries are silver.
- `python -m unittest discover -s tools/tests`: pass (29 tests).
- `./gradlew testDebugUnitTest assembleDebug`: pass.

The existing batch files 1–24 contain pre-existing duplicate sense IDs/repeated example frames
when checked as one combined set; they were not rewritten as part of this batch.
