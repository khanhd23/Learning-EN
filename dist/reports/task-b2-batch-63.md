# Content B2 batch 63

## Scope and manual review

Added 26 CEFR A1-B1 entries through the authoring workflow:
cd, dr, dvd, mommie, mr, mrs, olympics, pm, smith, tee-shirt, tv, id, ms,
pc, aborigine, anti, connexion, cv, dj, doc, easygoing, encyclopedia,
englishman, instal, mediterranean, monk.

I manually reviewed every English definition, part of speech, example,
Vietnamese gloss, Vietnamese example, level, and topic. I corrected two
checker issues: short glosses for mrs/olympics and the tee-shirt sense ID
normalization. All 26 entries are silver.

## Counts

| Measure | Before | After |
| --- | ---: | ---: |
| Dictionary entries | 7,182 | 7,208 |
| Silver entries | 5,954 | 5,980 |
| CEFR A1-B1 entries still missing | 47 | 21 |
| Bronze entries | 1,228 | 1,228 |

## Checks

- check_editor_batch: rows=26 problems=0
- words_batch tiers: not silver: 0
- validate_content: errors=0 warnings=247
- audit_content: entries=7208 gold=0 silver=5980 bronze=1228 ngsl_missing=0 errors=3012
- unittest discover: 32 tests passed; locale xx errors=0
- Final cumulative Gradle check: BUILD SUCCESSFUL
