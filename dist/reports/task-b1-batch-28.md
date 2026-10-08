# B1 TOEIC content batch 28

Upgraded all 75 remaining bronze TSL entries to silver. The batch contains 75 words and
76 curated senses because `downtown` has both its adverb and noun senses. The two accented
source lemmas `résumé` and `café` are represented by the generator IDs `resume` and `cafe`.

## Batch summary

- Words upgraded: 75
- Curated senses: 76
- Levels: L3 = 41, L4 = 33, L5 = 1
- All 75 entries are silver; `python tools/words_batch.py tiers tools/authoring/work/b28.txt`
  reports `not silver: 0`.
- TSL missing words: 0 before → 0 after
- TSL bronze words: 75 before → 0 after
- Lesson-ready silver words: 4,813 before → 4,888 after
- Total generated word entries: 6,215 before → 6,215 after

## Topic counts in this batch

| Topic | Words |
|---|---:|
| feelings_personality | 10 |
| home_furniture | 9 |
| arts_entertainment | 7 |
| describing_things | 7 |
| office | 7 |
| clothes | 5 |
| friends_relationships | 5 |
| health_illness | 5 |
| daily_routine | 4 |
| education | 4 |
| food_drink | 4 |
| government_society | 4 |
| hotel_restaurant | 4 |
| transport | 4 |
| sports_fitness | 3 |
| time_dates | 3 |
| city_directions | 2 |
| contracts_law | 2 |
| environment_energy | 2 |
| jobs | 2 |
| language_learning | 2 |
| events | 1 |
| function_words | 1 |
| it_technology | 1 |
| linking_words | 1 |
| media_news | 1 |
| numbers_quantity | 1 |
| sales_customer | 1 |
| science_basics | 1 |
| shopping | 1 |
| weather | 1 |

## Pipeline corrections

- `downtown_s2` received an authored definition and full example so its noun sense is
  lesson-ready as well as its adverb sense.
- The imported `the Monument` proper-name collocation was cleared through the owner
  correction marker (`-`), because it is not a useful learner collocation.
- `tools/check_editor_batch.py` now tokenizes Unicode words and folds accents when checking
  that an authored example contains its lemma; this is needed for `résumé` and is a general
  validator fix, not a content change.

## Verification

- `python tools/words_batch.py build tools/authoring/work/b28.txt 28`: pass (`meta=75`,
  `senses=76`).
- `python tools/check_editor_batch.py tools/authoring/senses_editor_batch28.tsv`: pass;
  `rows=76 problems=0`.
- `python tools/gen_content.py`: pass (`words=6215`).
- `python tools/validate_content.py`: pass; errors=0 (247 existing warnings).
- `python tools/word_gaps.py tsl`: pass; missing=0 and bronze=0.
- `python tools/audit_content.py`: generated audit (`entries=6215 gold=0 silver=4888
  bronze=1327 errors=3251`). The remaining project-wide audit errors are pre-existing and
  outside this batch.
