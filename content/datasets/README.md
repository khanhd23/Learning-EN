# English–Vietnamese vocabulary data

The project keeps two layers:

- `en_vi_lexicon.jsonl.gz`: the unrestricted source archive (all available English
  headword/POS records, senses, forms and related expressions).
- `en_vi_master_15k.jsonl.gz`: the practical master pack of 15,000 distinct
  headwords, selected for broad daily, academic, business, technology, science,
  travel and language coverage. It retains multiple POS records for a headword.

The Android app should generate smaller learner packs from the 15k master pack. It
does not need to load the unrestricted archive into memory. Packs can later be
created by age, topic, exam, frequency, IPA availability, register or review state.

Every record is marked `needs_review`. Missing IPA or weak examples are retained in
the master pack so an editor can repair them instead of silently losing useful
coverage. The full source record remains available for comparison.

For polysemy, teach one sense at a time in a concrete context and then connect the
senses. For similar-sounding words, keep verified pronunciation relations separate
from spelling similarity. Never infer a homophone only from spelling.

The exact source snapshot, hashes, counts and license are recorded in each `.index.json`.
