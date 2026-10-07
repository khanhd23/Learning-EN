"""Owner-written new entries (everyday words missing from the imported dictionary).

The TSV is the editable source (same columns as vocab_foundation.tsv, no topic lines).
Topics and levels come from entry_corrections.tsv; the reviewed English definition, gloss and
example come from the senses_editor_batch files, like every other lesson entry.
"""
from pathlib import Path
import lib

SOURCE = "owner:everyday-2026-10"
for line_number, line in enumerate(Path(__file__).with_suffix(".tsv").read_text(encoding="utf-8").splitlines(), 1):
    if not line or line.startswith("#"):
        continue
    fields = line.split("|")
    if len(fields) != 8:
        raise ValueError(f"vocab_owner.tsv row {line_number}: expected eight columns")
    lemma, pos, ipa, level, gloss, example, translation, collocations = fields
    if any(w["id"] == lib.slug(lemma) for w in lib.WORDS):
        raise ValueError(f"vocab_owner.tsv row {line_number}: {lemma!r} already exists")
    lib.W(lemma, pos, ipa, int(level), gloss, example, translation, coll=[c for c in collocations.split(";") if c])
    lib.WORDS[-1].update(source=SOURCE)
