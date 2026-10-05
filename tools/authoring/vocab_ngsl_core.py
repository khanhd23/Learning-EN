"""Metadata-only authoring module for the NGSL 1-2000 gap batch.

Sense definitions, Vietnamese glosses, and examples live only in
``senses_editor_batch6.tsv``. This module owns the reusable vocabulary metadata.
"""

import json
from pathlib import Path

import lib

SOURCE = "original:ngsl-core-2026-10"
ROOT = Path(__file__).resolve().parent

for line_number, line in enumerate((ROOT / "vocab_ngsl_core.tsv").read_text(encoding="utf-8").splitlines(), 1):
    if not line or line.startswith("#"):
        continue
    fields = line.split("|")
    if len(fields) != 8:
        raise ValueError(f"NGSL core row {line_number}: expected eight columns")
    lemma, pos, ipa, level, topic_id, collocations, forms_text, grammar_text = fields
    if any(w["id"] == lib.slug(lemma) for w in lib.WORDS):
        raise ValueError(f"NGSL core ID collision: {lemma}")
    lib._current_topic = topic_id
    lib.W(lemma, pos, ipa, int(level), "", "", "", coll=collocations.split(";"))
    word = lib.WORDS[-1]
    try:
        source_forms = json.loads(forms_text)
    except json.JSONDecodeError as exc:
        raise ValueError(f"NGSL core row {line_number}: invalid forms JSON") from exc
    try:
        grammar_ids = json.loads(grammar_text)
    except json.JSONDecodeError as exc:
        raise ValueError(f"NGSL core row {line_number}: invalid grammarIds JSON") from exc
    word["forms"] = {"source": source_forms} if source_forms else {}
    word["grammarIds"] = grammar_ids
    word.update(needs_review=True, source=SOURCE)
