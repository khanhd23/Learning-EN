# Content standard (English core + learner glosses)

Every lesson item in EngPet must meet this standard. `tools/audit_content.py` measures it and writes
`dist/content-audit.md` (summary) and `dist/content-audit-issues.jsonl` (one row per failing entry).
Agents fix content by working through that issues file; the owner reviews the result.

This standard is L1-neutral: it applies to the English core for every learner language. §6 adds the
rules for a learner-language gloss (Vietnamese today, every locale later).

---

## 1. Scope: which words, how many

| Pack | Source | Purpose | Target |
|---|---|---|---|
| Core | **All of NGSL 1.2** (`tools/sources/ngsl.csv`, 2,809 lemmas) | everyday English, the backbone | 100% present |
| Everyday & picture | authored (food, home, body, weather, school, hobbies, devices…) | concrete words a beginner needs that NGSL ranks low | ~700 |
| Work / TOEIC | TSL + BSL (`tsl.csv`, `bsl.txt`) | workplace and exam goal | ~1,200 |
| Academic | NAWL (`nawl.txt`) | academic goal | ~900 |
| Phrases | phrasal verbs, collocations, idioms, chunks | natural speech | ~400 |

Learner-ready target for v1: **~3,500 entries in Levels 1–3** (all NGSL + everyday pack), then Levels 4–5.
Function words (pronouns, modals, determiners, auxiliaries, conjunctions) **are vocabulary entries**
and link to grammar points with `grammarIds`. Missing `he`, `we`, `would`, `which`, `who`… is a blocker.

## 2. Levels

Levels are internal. Never present them as CEFR or exam scores.

| Level | Rule (frequency band) | Roughly | Size |
|---|---|---|---|
| 1 | NGSL rank 1–1000 + everyday/picture words for absolute beginners | starter | ~1,200 |
| 2 | NGSL 1001–2000 + everyday pack | elementary | ~1,100 |
| 3 | NGSL 2001–2809 + TSL | intermediate | ~1,000 |
| 4 | NAWL + BSL + common phrasal verbs/idioms | upper-intermediate | ~1,000 |
| 5 | lower-frequency, formal, nuanced | advanced | ~500 |

- Default level comes from the band. An override must be recorded in `levelReason`.
- A word may move **down** freely when it is concrete and daily (`passport`, `fridge`) and **up**
  by at most one level.
- Audit flags: NGSL top-1000 word above Level 2; non-NGSL, non-pack word at Level 1–2 without a reason.

## 3. Entry schema (`content/en/words.json`)

```json
{
  "id": "bank", "lemma": "bank", "pos": "n",
  "ipa": "bæŋk", "level": 1, "levelReason": "",
  "topics": ["money_banking"],
  "tier": "gold",
  "forms": {"plural": "banks"},
  "grammarIds": [],
  "senses": [
    {"id": "bank_s1", "pos": "n", "def": "a business that keeps and lends money",
     "register": "neutral",
     "ex": [{"id": "bank_s1", "text": "I need to go to the bank before it closes.", "hl": "bank"}],
     "coll": ["bank account", "go to the bank"]},
    {"id": "bank_s2", "pos": "n", "def": "the land along the side of a river",
     "register": "neutral",
     "ex": [{"id": "bank_s2", "text": "We had a picnic on the river bank.", "hl": "bank"}],
     "coll": ["river bank"]}
  ],
  "family": {"banker": "n", "banking": "n"},
  "conf": [], "source": "...", "license": "..."
}
```

### 3.1 Required fields and rules

| Field | Rule |
|---|---|
| `id` | stable, lowercase, `_` for spaces; never renamed |
| `lemma` | dictionary form; multi-word allowed for phrases |
| `ipa` | General American; **no slashes, no syllable dots**; stress marks `ˈ ˌ`; only symbols in §3.2. The UI adds the slashes |
| `level` | 1–5 per §2 |
| `topics` | 1–3 IDs from the controlled list (§5); no catch-all topics |
| `tier` | `gold` / `silver` / `bronze` (§7) |
| `forms` | irregular plural / past / past participle / comparative when they exist |
| `senses` | 1–3 for Levels 1–2, 1–4 for Levels 3–5; **most common sense first** |
| `sense.id` | `<id>_s<n>`, stable when reordered |
| `sense.def` | plain English, ≤ 12 words, uses mostly NGSL-2000 words |
| `sense.register` | `neutral` / `informal` / `formal` / `technical` |
| `sense.ex` | ≥ 1 example (Levels 1–2: ≥ 2 for sense 1), rules in §4 |
| `sense.coll` | nouns/verbs/adjectives at Levels 1–3: ≥ 2 collocations |

### 3.2 IPA symbol set

Allowed: `a b d e f h i j k l m n o p r s t u v w z æ ɑ ɒ ɔ ə ɚ ɝ ɛ ɜ ɪ ʊ ʌ θ ð ʃ ʒ ŋ ɡ g ɹ ɾ ʔ ɫ ː ˈ ˌ` plus a space
for phrases. Anything else (`ɳ`, `ɑɪ`, `.`, `/`, a leading fragment such as `.sənt`) is an error.
Diphthongs use `aɪ aʊ ɔɪ eɪ oʊ`; affricates `tʃ dʒ`.

### 3.3 Which senses to include

Include a sense only if a learner is likely to meet it in everyday, work or study English.
**Exclude** archaic, literary, specialist-legal, regional slang and rare technical senses unless the
word's pack needs them (then tag the register). Examples of senses to drop or demote:
`silk` = "a senior British barrister", `put` = "to go, to move towards", `follow` = "to practise a trade".

## 4. Example sentences

- A complete, natural, modern sentence: starts with a capital letter, ends with `.`, `!` or `?`.
  No dictionary fragments (`to take silk`, `of no value`, `the scheme of colour`).
- Length: Levels 1–2 → 5–12 words; Levels 3–5 → 6–18 words.
- Contains the headword or an inflected form; `hl` holds the exact form to highlight.
- The other words are easier than the target word (Levels 1–2: ≥ 90% of tokens in NGSL top 2000).
- Shows the sense it belongs to, unambiguously.
- L1-neutral and globally safe: no real brands, celebrities or politics; no religion, alcohol or
  violence; names drawn from a varied list (Anna, Ben, Maria, Kenji, Aisha, Leo…); no country-
  specific facts that would be wrong elsewhere.
- No near-duplicate sentence patterns across the pack (trigram similarity ≤ 0.85).

## 5. Topics (controlled list)

Two levels: domain → topic. Every topic ID is English; its name lives in each locale's `topics.json`.
A topic has 20–80 words per level. Catch-all topics are not allowed. The current
`Con người, sự vật & khái niệm` (1,511 words) and `Miêu tả & sắc thái mở rộng` (673) must be split
across real topics.

| Domain | Topics |
|---|---|
| people | family, friends_relationships, body_appearance, feelings_personality, health_illness, age_life_stages |
| daily_life | home_furniture, daily_routine, food_drink (food & cooking), shopping, clothes, money_banking, time_dates, numbers_quantity, weather |
| places_travel | city_directions, transport, travel_holidays, hotel_restaurant, nature_landscape, animals |
| study | education, language_learning, science_basics |
| work | jobs, office (office & meetings), hr_recruiting, sales_customer, marketing, finance_accounting, contracts_law, logistics, it_technology, events |
| society | media_news, internet_social, environment_energy, government_society, arts_entertainment, sports_fitness |
| language | function_words, core_verbs, describing_things, linking_words, phrasal_verbs, idioms_chunks |

## 6. Learner-language gloss (`content/i18n/<loc>/words.json`)

- Translates **one sense** (its `def` + example), not the headword in general.
- Lowercase start (unless a proper noun), **no final period**.
- 1–3 equivalents separated by `, `; ≤ 40 characters. Put extra nuance in `note`, not in the gloss.
- Modern, everyday wording; no archaic or Sino-Vietnamese padding when a common word exists.
- No duplicate fragments (`horario, horario`).
- Example translation: natural, same meaning, same register.

## 7. Quality tiers and what the app shows

| Tier | Meaning | Shown in lessons? | Shown in dictionary search? |
|---|---|---|---|
| `gold` | meets the standard + human-checked | yes | yes |
| `silver` | meets the standard automatically (audit passes), not yet checked | yes, in beta builds only | yes |
| `bronze` | imported, fails the standard (most vi.wiktionary imports) | **no** | yes, labelled "dictionary" |

A word moves `bronze → silver` only when `tools/audit_content.py` reports zero errors for it.
Release builds use only `gold` for Levels 1–2.

## 8. Other content

| Item | Rule | v1 target |
|---|---|---|
| Grammar points | 40–60 points; each has formula, signals, ≥ 3 examples, ≥ 3 common mistakes | 40–60 |
| Questions per grammar point | ≥ 20, balanced across levels; answer positions 20–30% each | ≥ 800 grammar |
| Vocabulary questions | every Level 1–2 word appears in ≥ 1 question (generated exercises count) | — |
| Confusable sets | words learners mix up (affect/effect, borrow/lend, look/see/watch…) | 250–400 |
| Word families | NGSL words with common derivations | 800–1,200 |
| Collocations | distinct strings | 1,500+ |
| Language of `content/en` | English only; learner-language text lives in `content/i18n` | 0 non-English fields |

## 9. Audit severities

- **error** (blocks `silver`): missing sense id/def/example, fragment example, bad IPA, gloss style
  violations, catch-all topic only, NGSL word missing, Vietnamese text in `content/en`.
- **warn**: level/band mismatch, few collocations, long example, example vocabulary too hard,
  single example at Levels 1–2.
