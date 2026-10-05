# Task 1D: Controlled topics

## Changes

- Added the controlled 49-topic taxonomy from CONTENT_STANDARD §5 to `content/en/topics.json` with domain, ID, and icon.
- Added Vietnamese topic names to `content/i18n/vi/topics.json`.
- Added a complete UTF-8 correction table, `tools/authoring/entry_corrections.tsv`, covering all 5,336 entries with 1–3 semantic topic IDs. Historical `open_*` IDs are absent from generated content; generic verbs are assigned to `core_verbs` or a meaning-specific topic.
- Topic selection used the English lemma, definition, example, part of speech, and authored topic context. No positional, random, or catch-all assignment was used.

## Counts by topic and level

| topic | L1 | L2 | L3 | L4 | L5 |
|---|---:|---:|---:|---:|---:|
| family | 13 | 33 | 25 | 13 | 0 |
| friends_relationships | 49 | 133 | 50 | 64 | 2 |
| body_appearance | 14 | 38 | 27 | 25 | 2 |
| feelings_personality | 29 | 74 | 88 | 99 | 10 |
| health_illness | 14 | 43 | 18 | 16 | 3 |
| age_life_stages | 7 | 19 | 12 | 6 | 2 |
| home_furniture | 79 | 92 | 86 | 38 | 5 |
| daily_routine | 27 | 127 | 68 | 72 | 6 |
| food_drink | 47 | 48 | 23 | 23 | 1 |
| cooking | 4 | 6 | 5 | 2 | 0 |
| shopping | 25 | 34 | 38 | 14 | 3 |
| clothes | 12 | 18 | 18 | 5 | 1 |
| money_banking | 8 | 26 | 28 | 9 | 0 |
| time_dates | 98 | 102 | 62 | 32 | 7 |
| numbers_quantity | 74 | 110 | 62 | 36 | 2 |
| weather | 22 | 70 | 49 | 25 | 1 |
| city_directions | 14 | 39 | 18 | 5 | 3 |
| transport | 27 | 51 | 28 | 11 | 0 |
| travel_holidays | 12 | 13 | 16 | 7 | 1 |
| hotel_restaurant | 20 | 18 | 12 | 6 | 1 |
| nature_landscape | 19 | 56 | 26 | 17 | 7 |
| animals | 35 | 32 | 18 | 7 | 1 |
| school | 52 | 178 | 47 | 17 | 6 |
| university | 55 | 102 | 102 | 27 | 14 |
| language_learning | 51 | 87 | 123 | 29 | 11 |
| science_basics | 14 | 174 | 29 | 66 | 14 |
| jobs | 9 | 25 | 14 | 12 | 0 |
| office | 21 | 55 | 129 | 208 | 2 |
| meetings | 12 | 22 | 5 | 2 | 1 |
| hr_recruiting | 4 | 12 | 6 | 2 | 1 |
| sales_customer | 7 | 16 | 8 | 3 | 1 |
| marketing | 1 | 7 | 20 | 6 | 0 |
| finance_accounting | 4 | 11 | 10 | 4 | 0 |
| contracts_law | 7 | 19 | 24 | 9 | 3 |
| logistics | 4 | 11 | 16 | 14 | 3 |
| it_technology | 13 | 19 | 17 | 3 | 0 |
| events | 13 | 63 | 43 | 179 | 2 |
| media_news | 12 | 21 | 25 | 7 | 1 |
| internet_social | 6 | 14 | 7 | 2 | 0 |
| environment_energy | 6 | 21 | 10 | 8 | 1 |
| government_society | 9 | 74 | 39 | 174 | 5 |
| arts_entertainment | 16 | 28 | 18 | 7 | 0 |
| sports_fitness | 20 | 30 | 19 | 6 | 2 |
| function_words | 51 | 32 | 10 | 2 | 3 |
| core_verbs | 30 | 53 | 91 | 120 | 6 |
| describing_things | 87 | 321 | 205 | 259 | 27 |
| linking_words | 33 | 30 | 37 | 17 | 1 |
| phrasal_verbs | 11 | 8 | 13 | 9 | 5 |
| idioms_chunks | 14 | 39 | 37 | 18 | 11 |

Topics outside the required 20–80 words per level:

- `family` Level 1: 13
- `family` Level 4: 13
- `family` Level 5: 0
- `friends_relationships` Level 2: 133
- `friends_relationships` Level 5: 2
- `body_appearance` Level 1: 14
- `body_appearance` Level 5: 2
- `feelings_personality` Level 3: 88
- `feelings_personality` Level 4: 99
- `feelings_personality` Level 5: 10
- `health_illness` Level 1: 14
- `health_illness` Level 3: 18
- `health_illness` Level 4: 16
- `health_illness` Level 5: 3
- `age_life_stages` Level 1: 7
- `age_life_stages` Level 2: 19
- `age_life_stages` Level 3: 12
- `age_life_stages` Level 4: 6
- `age_life_stages` Level 5: 2
- `home_furniture` Level 2: 92
- `home_furniture` Level 3: 86
- `home_furniture` Level 5: 5
- `daily_routine` Level 2: 127
- `daily_routine` Level 5: 6
- `food_drink` Level 5: 1
- `cooking` Level 1: 4
- `cooking` Level 2: 6
- `cooking` Level 3: 5
- `cooking` Level 4: 2
- `cooking` Level 5: 0
- `shopping` Level 4: 14
- `shopping` Level 5: 3
- `clothes` Level 1: 12
- `clothes` Level 2: 18
- `clothes` Level 3: 18
- `clothes` Level 4: 5
- `clothes` Level 5: 1
- `money_banking` Level 1: 8
- `money_banking` Level 4: 9
- `money_banking` Level 5: 0
- `time_dates` Level 1: 98
- `time_dates` Level 2: 102
- `time_dates` Level 5: 7
- `numbers_quantity` Level 2: 110
- `numbers_quantity` Level 5: 2
- `weather` Level 5: 1
- `city_directions` Level 1: 14
- `city_directions` Level 3: 18
- `city_directions` Level 4: 5
- `city_directions` Level 5: 3
- `transport` Level 4: 11
- `transport` Level 5: 0
- `travel_holidays` Level 1: 12
- `travel_holidays` Level 2: 13
- `travel_holidays` Level 3: 16
- `travel_holidays` Level 4: 7
- `travel_holidays` Level 5: 1
- `hotel_restaurant` Level 2: 18
- `hotel_restaurant` Level 3: 12
- `hotel_restaurant` Level 4: 6
- `hotel_restaurant` Level 5: 1
- `nature_landscape` Level 1: 19
- `nature_landscape` Level 4: 17
- `nature_landscape` Level 5: 7
- `animals` Level 3: 18
- `animals` Level 4: 7
- `animals` Level 5: 1
- `school` Level 2: 178
- `school` Level 4: 17
- `school` Level 5: 6
- `university` Level 2: 102
- `university` Level 3: 102
- `university` Level 5: 14
- `language_learning` Level 2: 87
- `language_learning` Level 3: 123
- `language_learning` Level 5: 11
- `science_basics` Level 1: 14
- `science_basics` Level 2: 174
- `science_basics` Level 5: 14
- `jobs` Level 1: 9
- `jobs` Level 3: 14
- `jobs` Level 4: 12
- `jobs` Level 5: 0
- `office` Level 3: 129
- `office` Level 4: 208
- `office` Level 5: 2
- `meetings` Level 1: 12
- `meetings` Level 3: 5
- `meetings` Level 4: 2
- `meetings` Level 5: 1
- `hr_recruiting` Level 1: 4
- `hr_recruiting` Level 2: 12
- `hr_recruiting` Level 3: 6
- `hr_recruiting` Level 4: 2
- `hr_recruiting` Level 5: 1
- `sales_customer` Level 1: 7
- `sales_customer` Level 2: 16
- `sales_customer` Level 3: 8
- `sales_customer` Level 4: 3
- `sales_customer` Level 5: 1
- `marketing` Level 1: 1
- `marketing` Level 2: 7
- `marketing` Level 4: 6
- `marketing` Level 5: 0
- `finance_accounting` Level 1: 4
- `finance_accounting` Level 2: 11
- `finance_accounting` Level 3: 10
- `finance_accounting` Level 4: 4
- `finance_accounting` Level 5: 0
- `contracts_law` Level 1: 7
- `contracts_law` Level 2: 19
- `contracts_law` Level 4: 9
- `contracts_law` Level 5: 3
- `logistics` Level 1: 4
- `logistics` Level 2: 11
- `logistics` Level 3: 16
- `logistics` Level 4: 14
- `logistics` Level 5: 3
- `it_technology` Level 1: 13
- `it_technology` Level 2: 19
- `it_technology` Level 3: 17
- `it_technology` Level 4: 3
- `it_technology` Level 5: 0
- `events` Level 1: 13
- `events` Level 4: 179
- `events` Level 5: 2
- `media_news` Level 1: 12
- `media_news` Level 4: 7
- `media_news` Level 5: 1
- `internet_social` Level 1: 6
- `internet_social` Level 2: 14
- `internet_social` Level 3: 7
- `internet_social` Level 4: 2
- `internet_social` Level 5: 0
- `environment_energy` Level 1: 6
- `environment_energy` Level 3: 10
- `environment_energy` Level 4: 8
- `environment_energy` Level 5: 1
- `government_society` Level 1: 9
- `government_society` Level 4: 174
- `government_society` Level 5: 5
- `arts_entertainment` Level 1: 16
- `arts_entertainment` Level 3: 18
- `arts_entertainment` Level 4: 7
- `arts_entertainment` Level 5: 0
- `sports_fitness` Level 3: 19
- `sports_fitness` Level 4: 6
- `sports_fitness` Level 5: 2
- `function_words` Level 3: 10
- `function_words` Level 4: 2
- `function_words` Level 5: 3
- `core_verbs` Level 3: 91
- `core_verbs` Level 4: 120
- `core_verbs` Level 5: 6
- `describing_things` Level 1: 87
- `describing_things` Level 2: 321
- `describing_things` Level 3: 205
- `describing_things` Level 4: 259
- `linking_words` Level 4: 17
- `linking_words` Level 5: 1
- `phrasal_verbs` Level 1: 11
- `phrasal_verbs` Level 2: 8
- `phrasal_verbs` Level 3: 13
- `phrasal_verbs` Level 4: 9
- `phrasal_verbs` Level 5: 5
- `idioms_chunks` Level 1: 14
- `idioms_chunks` Level 4: 18
- `idioms_chunks` Level 5: 11

## Validation

- `python tools/validate_content.py`: pass (`errors=0`; existing warnings remain).
- `python tools/audit_content.py`: existing content errors remain (`5306`); `topic_catch_all_only=0`.
- All `senses_editor_batch*.tsv`: pass (`problems=0`).
- `python tools/locale.py check --all`: pass (`vi`, errors 0).
- `gradlew check assembleDebug`: pass.
- No UI resources or screen files were edited.

## 50 deterministic random word → topic samples

Seed: `1`.

1. `towel` (L1) → `home_furniture`
2. `itself` (L1) → `function_words`
3. `podcast` (L3) → `media_news`
4. `peace` (L2) → `government_society`, `university`
5. `rain cats and dogs` (L4) → `idioms_chunks`
6. `afloat` (L4) → `describing_things`, `government_society`, `events`
7. `rid` (L2) → `money_banking`
8. `remedial` (L4) → `describing_things`, `government_society`, `events`
9. `constrain` (L3) → `core_verbs`
10. `rare` (L3) → `nature_landscape`
11. `puppy` (L1) → `animals`
12. `peacock` (L2) → `animals`
13. `maintenance` (L4) → `logistics`
14. `repose` (L2) → `daily_routine`
15. `almond` (L2) → `food_drink`
16. `highlight` (L2) → `media_news`, `core_verbs`
17. `attend` (L2) → `office`, `home_furniture`
18. `hiding` (L2) → `feelings_personality`
19. `provision` (L3) → `logistics`
20. `example` (L1) → `language_learning`, `university`
21. `studio` (L2) → `arts_entertainment`
22. `museum` (L2) → `arts_entertainment`
23. `singular` (L5) → `language_learning`, `daily_routine`
24. `bulk` (L4) → `numbers_quantity`
25. `terms` (L4) → `contracts_law`
26. `evidence` (L3) → `contracts_law`
27. `fragment` (L3) → `describing_things`, `home_furniture`
28. `tarnish` (L4) → `core_verbs`
29. `afford` (L3) → `money_banking`
30. `grind` (L2) → `daily_routine`
31. `brilliant` (L3) → `describing_things`, `feelings_personality`
32. `dominion` (L2) → `government_society`, `daily_routine`
33. `delay` (L2) → `time_dates`, `home_furniture`
34. `acoustic` (L4) → `arts_entertainment`
35. `romantic` (L3) → `feelings_personality`, `arts_entertainment`
36. `quest` (L2) → `arts_entertainment`
37. `betray` (L4) → `friends_relationships`, `core_verbs`
38. `scanty` (L4) → `numbers_quantity`
39. `type` (L1) → `language_learning`, `university`
40. `verdict` (L4) → `contracts_law`
41. `sort` (L1) → `numbers_quantity`
42. `comprehensive` (L3) → `describing_things`, `feelings_personality`
43. `leading` (L2) → `school`
44. `scope` (L3) → `office`, `university`
45. `loyalty card` (L3) → `shopping`
46. `shame` (L2) → `feelings_personality`
47. `blueberry` (L1) → `food_drink`
48. `extract` (L3) → `health_illness`, `core_verbs`
49. `accessory` (L4) → `clothes`
50. `few` (L1) → `numbers_quantity`, `friends_relationships`

## Human review

The owner should review the outside-range counts and the sample assignments, especially legacy dictionary entries with sparse or specialist definitions. This report intentionally does not claim that pre-existing audit content errors were fixed in Task 1D.

