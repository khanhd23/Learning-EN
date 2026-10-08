"""Check a curated sense batch (tools/authoring/senses_editor_batch*.tsv) before generating content.

Usage: python tools/check_editor_batch.py tools/authoring/senses_editor_batch3.tsv [more.tsv ...]

Each row: sense_id, pos, def, vi_gloss, example, example_vi, note (tab-separated, UTF-8).
Exits non-zero and lists every problem; fix them all before running tools/gen_content.py.
"""
from __future__ import annotations

import collections
import json
import re
import sys
import unicodedata
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "tools"))
import audit_content as audit  # noqa: E402
from gen_content import IRREGULAR_FORMS  # noqa: E402

POS = {"n", "v", "adj", "adv", "prep", "conj", "pron", "det", "article", "interj", "phr", "idiom"}
TOKEN = re.compile(r"[^\W_]+(?:['-][^\W_]+)*", re.UNICODE)
VI_NAMES = re.compile(r"\b(Lan|Nam|Hoa|Minh|Mai|Tuấn|Hùng|Hà Nội|Hanoi|Da Nang|Đà Nẵng|Saigon|Sài Gòn|"
                      r"Vietnam|Việt Nam|Hue|Huế|Haiduong)\b")


def norm(text):
    return re.sub(r"[\s.;,]+", " ", (text or "").lower()).strip()


def fold(text):
    """Compare learner words without letting accents break form detection."""
    return unicodedata.normalize("NFKD", text).encode("ascii", "ignore").decode().lower()


def imported_glosses():
    """sense_id -> gloss as it came from the vi.wiktionary import (before any editor work)."""
    import importlib
    import gen_content
    sys.path.insert(0, str(ROOT / "tools" / "authoring"))
    import lib
    for name in gen_content.MODULES:
        try:
            importlib.import_module(name)
        except ModuleNotFoundError:
            pass
    out = {}
    for w in lib.WORDS:
        if "wiktionary" not in (w.get("source") or ""):
            continue
        senses = (w.get("_vi") or {}).get("senses") or []
        for i, s in enumerate(w.get("senses") or []):
            sid = s.get("id") or ((s.get("ex") or [{}])[0].get("id"))
            if sid and i < len(senses):
                out[sid] = senses[i].get("g") or ""
    return out


def main() -> int:
    paths = [Path(p) for p in sys.argv[1:]]
    if not paths or any(p.name in ("-h", "--help") for p in paths):
        print(__doc__)
        return 2
    words = audit.load("content/en/words.json")["words"]
    sense_ids = {s.get("id") for w in words for s in w.get("senses") or []}
    lemma_of = {s.get("id"): w["lemma"] for w in words for s in w.get("senses") or []}
    level_of = {s.get("id"): w.get("level") or 1 for w in words for s in w.get("senses") or []}
    wordnet = audit.wordnet_texts()
    imported = imported_glosses()
    problems, seen, rows = [], set(), []

    for path in paths:
        raw = path.read_bytes()
        try:
            text = raw.decode("utf-8")
        except UnicodeDecodeError as e:
            problems.append(f"{path}: not UTF-8 ({e})")
            continue
        if "\r" in text:
            problems.append(f"{path}: use LF line endings")
        lines = text.split("\n")
        if not lines or not lines[0].startswith("sense_id\tpos\tdef\tvi_gloss\texample\texample_vi\tnote"):
            problems.append(f"{path}: header must be sense_id, pos, def, vi_gloss, example, example_vi, note")
        for n, line in enumerate(lines[1:], start=2):
            if not line.strip():
                continue
            cols = line.split("\t")
            where = f"{path.name}:{n}"
            if len(cols) != 7:
                problems.append(f"{where}: expected 7 columns, got {len(cols)}")
                continue
            sid, pos, definition, gloss, example, example_vi, _note = cols
            rows.append((where, sid, example, example_vi, gloss))
            if sid in seen:
                problems.append(f"{where}: duplicate sense_id {sid}")
            seen.add(sid)
            if sid not in sense_ids:
                problems.append(f"{where}: unknown sense_id {sid}")
            if pos not in POS:
                problems.append(f"{where}: pos '{pos}' not in {sorted(POS)}")
            if not definition or len(definition.split()) > 12:
                problems.append(f"{where}: def must be 1-12 words")
            if definition.strip().lower().rstrip(".") in wordnet:
                problems.append(f"{where}: def is copied from WordNet; write your own")
            for issue in audit.gloss_issues(gloss):
                problems.append(f"{where}: vi_gloss {issue}")
            for field, value in (("vi_gloss", gloss), ("example_vi", example_vi)):
                if audit.MOJIBAKE.search(value):
                    problems.append(f"{where}: {field} has broken encoding: {value!r}")
            if not example_vi.strip():
                problems.append(f"{where}: example_vi is empty")
            if audit.FILLER_VI.search(example_vi):
                problems.append(f"{where}: example_vi is filler, translate the sentence")
            if not audit.SENTENCE.match(example) or example[-1:] not in ".!?":
                problems.append(f"{where}: example must be a full sentence with end punctuation")
            n_words = len(example.split())
            low = 5 if level_of.get(sid, 1) <= 2 else 6
            if not low <= n_words <= 14:
                problems.append(f"{where}: example has {n_words} words ({low}-14 for this level)")
            lemma = lemma_of.get(sid, sid.rsplit("_s", 1)[0])
            head = lemma.split()[0].lower()
            tokens = [t.lower() for t in TOKEN.findall(example)]
            forms = {fold(f) for f in IRREGULAR_FORMS.get(head, [])}
            head_prefix = fold(head[:max(2, len(head) - 2)])
            if not any(fold(t).startswith(head_prefix) or fold(t) in forms for t in tokens):
                problems.append(f"{where}: example does not seem to contain '{lemma}' (check irregular forms)")
            if VI_NAMES.search(example):
                problems.append(f"{where}: example uses a Vietnamese name or place; content/en is L1-neutral")
            if VI_NAMES.search(example_vi) and not VI_NAMES.search(example):
                problems.append(f"{where}: example_vi adds a name or place that is not in the English example")
            if pos in ("n", "adj", "adv") and definition.startswith("to ") \
                    and not definition.startswith(("to a ", "to some ", "to such ", "to the ", "to an ")):
                problems.append(f"{where}: pos '{pos}' but def reads like a verb ('to …'); make them match")
            if pos == "v" and not definition.startswith(("to ", "used ")):
                problems.append(f"{where}: pos 'v' but def does not start with 'to …'; make them match")
            if re.search(r"\((thuộc|Thuộc|nghĩa đen|nghĩa bóng)", gloss) or gloss[:1].isupper():
                problems.append(f"{where}: vi_gloss is in old dictionary style: {gloss!r}")
            old = imported.get(sid)
            if old and norm(old) == norm(gloss) and "[keep-gloss]" not in _note:
                problems.append(f"{where}: vi_gloss is copied unchanged from the imported dictionary "
                                f"({gloss!r}); rewrite it to match def, or add [keep-gloss] to note if it already matches")

    skel = collections.Counter(audit.skeleton(ex, lemma_of.get(sid, sid)) for _, sid, ex, _, _ in rows)
    for where, sid, ex, _, _ in rows:
        if skel[audit.skeleton(ex, lemma_of.get(sid, sid))] > 1:
            problems.append(f"{where}: example frame repeats another row: {ex!r}")
    tr = collections.Counter(vi for *_, vi, _ in rows)
    for where, _, _, vi, _ in rows:
        if tr[vi] > 1:
            problems.append(f"{where}: same example_vi used twice: {vi!r}")

    for p in problems:
        print(p)
    print(f"rows={len(rows)} problems={len(problems)}")
    return 1 if problems else 0


if __name__ == "__main__":
    sys.exit(main())
