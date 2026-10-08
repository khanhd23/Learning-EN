"""Owner workflow for word batches (moved from the owner's scratch tools, 2026-10-08).

Two kinds of input, both plain UTF-8 text in tools/authoring/work/ (git-ignored):

1. NEW words (not in the dictionary yet) — a master file, one word per line:
       lemma|pos|ipa|level|topics|definition|vi_gloss|example|example_vi
   `python tools/words_batch.py new <master.tsv> <N>` appends the word to
   tools/authoring/vocab_owner.tsv and entry_corrections.tsv, skips words that already exist,
   and writes the working file work/b<N>.txt (glosses marked [keep]).

2. Existing words (upgrade, fix, topic changes) — a working file with two kinds of lines:
       M|word_id|topics|ipa              (ipa may be empty to keep it)
       S|sense_id|pos|definition|vi_gloss|example|example_vi
   sense_id without "_s" gets "_s1"; a gloss ending in " [keep]" keeps the imported gloss.

`python tools/words_batch.py build <work/bN.txt> <N>` writes
tools/authoring/senses_editor_batch<N>.tsv and updates entry_corrections.tsv.
`python tools/words_batch.py tiers <work/bN.txt>` lists the batch's words that are not silver.

Full cycle for a batch:
    words_batch.py new|build ...  →  python tools/gen_content.py  →
    python tools/check_editor_batch.py tools/authoring/senses_editor_batch<N>.tsv  (fix until 0)  →
    python tools/gen_content.py  →  python tools/validate_content.py  (errors=0)  →
    python tools/audit_content.py  →  words_batch.py tiers ...  (fix until empty)
"""
import collections
import io
import json
import os
import re
import sys
import unicodedata

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
AUTH = os.path.join(ROOT, "tools", "authoring")
WORK = os.path.join(AUTH, "work")


def slug(s):
    s = unicodedata.normalize("NFKD", s).encode("ascii", "ignore").decode().lower()
    return re.sub(r"[^a-z0-9]+", "_", s).strip("_")


def cmd_new(master, num):
    words = json.load(open(os.path.join(ROOT, "content", "en", "words.json"), encoding="utf-8"))["words"]
    have = {w["id"] for w in words}
    rows = []
    for n, line in enumerate(io.open(master, encoding="utf-8").read().splitlines(), 1):
        if not line.strip() or line.startswith("#"):
            continue
        f = [x.strip() for x in line.split("|")]
        if len(f) != 9:
            raise SystemExit(f"{master}:{n}: expected 9 fields, got {len(f)}")
        lemma, pos, ipa, level, topics, d, gloss, ex, exvi = f
        wid = slug(lemma)
        if wid in have:
            print("skip (exists, upgrade it with an M/S line instead):", lemma)
            continue
        rows.append((wid, lemma, pos, ipa, level, topics, d, gloss, ex, exvi))
    ids = [r[0] for r in rows]
    if len(ids) != len(set(ids)):
        raise SystemExit("duplicate words in the master file")
    with open(os.path.join(AUTH, "vocab_owner.tsv"), "a", encoding="utf-8", newline="\n") as f:
        for wid, lemma, pos, ipa, level, topics, d, gloss, ex, exvi in rows:
            f.write(f"{lemma}|{pos}|{ipa}|{level}|{gloss}|{ex}|{exvi}|\n")
    with open(os.path.join(AUTH, "entry_corrections.tsv"), "a", encoding="utf-8", newline="\n") as f:
        for wid, lemma, pos, ipa, level, topics, d, gloss, ex, exvi in rows:
            f.write(f"{wid}|{topics}|{ipa}|{level}\n")
    os.makedirs(WORK, exist_ok=True)
    out = os.path.join(WORK, f"b{num}.txt")
    with open(out, "w", encoding="utf-8", newline="\n") as f:
        for wid, lemma, pos, ipa, level, topics, d, gloss, ex, exvi in rows:
            f.write(f"M|{wid}|{topics}|{ipa}\nS|{wid}|{pos}|{d}|{gloss} [keep]|{ex}|{exvi}\n")
    print("new words", len(rows), "->", out)
    cmd_build(out, num)


def cmd_build(src, num):
    meta, senses = {}, []
    for n, raw in enumerate(io.open(src, encoding="utf-8").read().splitlines(), 1):
        line = raw.replace("\t", "|").strip()
        if not line:
            continue
        f = line.split("|")
        if f[0] == "M":
            assert len(f) == 4, (n, line)
            meta[f[1]] = (f[2], f[3])
        elif f[0] == "S":
            assert len(f) == 7, (n, len(f), line)
            sid = f[1] if "_s" in f[1] else f[1] + "_s1"
            note = f"owner 1C batch{num}"
            if f[4].endswith(" [keep]"):
                f[4] = f[4][: -len(" [keep]")]
                note += " [keep-gloss]"
            senses.append([sid] + f[2:] + [note])
        else:
            raise SystemExit(f"{src}:{n}: unknown line kind {line!r}")
    if senses:
        out = ["sense_id\tpos\tdef\tvi_gloss\texample\texample_vi\tnote"] + ["\t".join(r) for r in senses]
        io.open(os.path.join(AUTH, f"senses_editor_batch{num}.tsv"), "w", encoding="utf-8", newline="\n").write("\n".join(out) + "\n")
    path = os.path.join(AUTH, "entry_corrections.tsv")
    lines = io.open(path, encoding="utf-8").read().splitlines()
    seen = set()
    for i, line in enumerate(lines):
        if line.startswith("#") or not line.strip():
            continue
        parts = line.split("|")
        if parts[0] in meta:
            topics, ipa = meta[parts[0]]
            while len(parts) < 4:
                parts.append("")
            parts[1] = topics
            if ipa:
                parts[2] = ipa
            lines[i] = "|".join(parts).rstrip("|")
            seen.add(parts[0])
    missing = set(meta) - seen
    if missing:
        raise SystemExit(f"not in entry_corrections.tsv: {sorted(missing)}")
    io.open(path, "w", encoding="utf-8", newline="\n").write("\n".join(lines) + "\n")
    print("batch", num, "meta", len(meta), "senses", len(senses))


def cmd_tiers(src):
    sys.path.insert(0, os.path.join(ROOT, "tools"))
    os.chdir(ROOT)
    import audit_content as a
    ids = {l.split("|")[1] for l in open(src, encoding="utf-8") if l.startswith("M|")}
    words = a.load("content/en/words.json")["words"]
    gl = a.load("content/i18n/vi/words.json")
    g = lambda w: {"senses": [gl.get(s.get("id"), {}) for s in w["senses"]]}  # noqa: E731
    ranks = a.ngsl_ranks()
    easy = {k for k, r in ranks.items() if r <= 2000}
    ts = collections.Counter(t for w in words for t in w.get("topics") or [])
    a.prepare(words, g)
    bad = 0
    for w in words:
        if w["id"] in ids:
            e, _, t = a.audit_word(w, g(w), ranks, easy, ts)
            if t != "silver":
                bad += 1
                print(w["id"], t, e)
    print("not silver:", bad)


if __name__ == "__main__":
    if len(sys.argv) < 3:
        raise SystemExit(__doc__)
    kind = sys.argv[1]
    if kind == "new":
        cmd_new(sys.argv[2], sys.argv[3])
    elif kind == "build":
        cmd_build(sys.argv[2], sys.argv[3])
    elif kind == "tiers":
        cmd_tiers(sys.argv[2])
    else:
        raise SystemExit(__doc__)
