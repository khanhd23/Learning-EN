# Dictionary search design

## Product behavior

The search entry is available from Today, Vocabulary, Grammar and Practice. The
same query can be English, Vietnamese, a phrase, a topic or a tag. Results are
offline and never require a network request.

The screen has four states:

1. Empty: recent searches, suggested goals (Giao tiếp, TOEIC, Công việc, Học bằng ảnh).
2. Typing: instant results after two characters, with a 150 ms debounce.
3. Results: grouped as **Từ phù hợp**, **Nghĩa & ví dụ**, **Cụm từ**, **Chủ đề**.
4. No result: spelling suggestion, broader search and a free-text report action.

Each word card shows lemma, IPA, part of speech, the best Vietnamese sense, one
example and small chips such as `Giao tiếp`, `TOEIC`, `Công việc`. Actions are
`Xem toàn bộ nghĩa`, `Luyện từ này`, `Lưu` and `Nghe`.

## Ranking

Search is deliberately meaning-aware:

| Match | Score | Display |
|---|---:|---|
| exact English headword | 1000 | first |
| English headword prefix | 850 | first |
| exact Vietnamese gloss | 800 | first |
| Vietnamese gloss token | 700 | first |
| translated example | 600 | below direct matches |
| topic/tag | 450 | supporting result |
| fuzzy spelling | 300 | only when no direct result |

Scores receive small boosts for NGSL/TSL/BSL/NAWL membership and a learner's
selected goal. Never hide a direct meaning match just because it is not in a
high-frequency list.

## Word detail behavior

Polysemous words are shown as separate sense cards, each with its own gloss,
example, translation, register and topic. The first visit teaches one concrete
sense; `Xem thêm nghĩa` reveals the rest. The screen links to word family,
collocations, idioms and verified pronunciation relations.

Spelling similarity and pronunciation similarity are different relations. A
`confusable` card may say “dễ nhầm về nghĩa”, while a `near_homophone` card is
allowed only after a pronunciation editor verifies it. The system never infers
homophones from spelling.

## Data contract

`en_vi_master_15k_tagged.jsonl.gz` remains the source for the learner dictionary.
`en_vi_dictionary_search.index.json.gz` is a compact inverted index:

```json
{
  "version": 1,
  "token": "meeting",
  "ids": ["envi_..."],
  "fields": ["headword", "gloss", "example", "topic", "tag"]
}
```

The index is an optimization, not the source of truth. If it is deleted, it can
be regenerated from the tagged master pack.

## Learning safety

All imported records remain `needs_review` until a bilingual editor checks IPA,
sense boundaries, naturalness of Vietnamese, register and example quality. The
dictionary can expose broad results, but the recommended “Học” button must only
select records from reviewed/appropriate packs.
