"""Build an offline, review-pending vocabulary subset from licensed source snapshots.

No network during normal builds. Download paths and hashes are recorded in the output.
Existing authoring modules are reconstructed, so repeated imports cannot erase entries.
"""
import collections
import csv
import gzip
import hashlib
import importlib
import json
from pathlib import Path
import re
import sys
import unicodedata
from urllib.parse import quote

ROOT = Path(__file__).resolve().parents[1]
SOURCES = ROOT / "tools/sources"
sys.path.insert(0, str(ROOT / "tools/authoring"))
import lib

POS = {"noun": "n", "verb": "v", "adj": "adj", "adv": "adv", "prep": "prep", "conj": "conj", "pron": "pron", "det": "det", "num": "num", "intj": "interj", "article": "article"}
BAD_TAGS = {"archaic", "obsolete", "dated", "offensive", "vulgar", "derogatory", "slang", "rare", "form-of", "abbreviation", "historical"}
BAD_TEXT = re.compile(r"từ cổ|nghĩa cổ|lỗi thời|ít dùng|tục tĩu|miệt thị|xem thêm|như trên|viết tắt|số nhiều của|quá khứ của|phân từ của|\{\{|\}\}|\[|\]|someone|something|somebody|one's|sb\.|sth\.|\.\.\.|…", re.I)


def clean(s):
    return unicodedata.normalize("NFC", re.sub(r"\s+", " ", s).strip())


def lists():
    ngsl = {r["Lemma"].lower(): int(r["SFI Rank"]) for r in csv.DictReader((SOURCES / "ngsl.csv").read_text(encoding="utf-8-sig").splitlines()) if r.get("Lemma")}
    others = {}
    for name in ("nawl", "bsl"):
        others[name] = {x.strip().lower() for x in (SOURCES / (name + ".txt")).read_text(encoding="utf-8-sig").splitlines() if re.fullmatch(r"[a-z]+(?:-[a-z]+)*", x.strip())}
    return ngsl, others["nawl"], others["bsl"]


def main():
    for name in ("vocab_work", "vocab_life", "vocab_everyday", "vocab_modern", "vocab_foundation", "senses"):
        importlib.import_module(name)
    original = {w["lemma"].casefold() for w in lib.WORDS}
    original_ids = {w["id"] for w in lib.WORDS}
    ngsl, nawl, bsl = lists()
    target = set(ngsl) | nawl | bsl
    candidates = collections.defaultdict(list)
    rejected = collections.Counter()
    with gzip.open(SOURCES / "viwiktionary-20260901.jsonl.gz", "rt", encoding="utf-8") as stream:
        for line in stream:
            d = json.loads(line)
            lemma = d.get("word", "")
            if d.get("lang_code") != "en" or lemma not in target or lemma in original or lib.slug(lemma) in original_ids:
                continue
            pos = POS.get(d.get("pos"))
            ipas = [clean(x["ipa"]) for x in d.get("sounds", []) if x.get("ipa") and len(x["ipa"]) < 65]
            if not pos or not ipas:
                rejected["missing_pos_or_ipa"] += 1
                continue
            for index, sense in enumerate(d.get("senses", [])):
                if index > 1:
                    continue  # Do not promote an obscure late sense just because it has an example.
                if BAD_TAGS.intersection(d.get("tags", []) + sense.get("tags", [])) or sense.get("form_of"):
                    continue
                gloss = clean("; ".join(sense.get("glosses", [])))
                if not 2 <= len(gloss) <= 160 or BAD_TEXT.search(gloss):
                    continue
                examples = []
                for ex in sense.get("examples", []):
                    en, vi = clean(ex.get("text", "")), clean(ex.get("translation", ""))
                    if not 2 <= len(en.split()) <= 24 or not 3 <= len(vi) <= 220 or len(en) > 180:
                        continue
                    if BAD_TEXT.search(en + " " + vi) or ";" in en or ex.get("ref") or ex.get("roman"):
                        continue
                    # Exact lemma enables the existing offline cloze exercise generator.
                    if not re.search(r"\b" + re.escape(lemma) + r"\b", en, re.I):
                        continue
                    if any(ord(c) > 127 for c in en):
                        continue
                    examples.append({"en": en, "vi": vi})
                if examples:
                    examples.sort(key=lambda e: (not bool(re.search(r"[.!?]$", e["en"])), len(e["en"].split()), e["en"]))
                    candidates[lemma].append({"pos": pos, "ipa": ipas[0], "gloss": gloss, "examples": examples[:2], "sense_index": index})
    rows = []
    for lemma in sorted(candidates, key=lambda w: (0 if w in ngsl else 1 if w in nawl else 2, ngsl.get(w, 9999), w)):
        choices = candidates[lemma]
        choices.sort(key=lambda c: c["sense_index"])
        choice = choices[0]
        memberships = [name for name, words in (("ngsl", ngsl), ("nawl", nawl), ("bsl", bsl)) if lemma in words]
        rank = ngsl.get(lemma)
        level = (1 if rank <= 700 else 2 if rank <= 1500 else 3) if rank else (4 if lemma in bsl else 5)
        topic = "open_general" if rank else "open_academic" if lemma in nawl else "open_business"
        rows.append(dict(lemma=lemma, level=level, topic=topic, lists=memberships,
                         rank=rank, source="https://vi.wiktionary.org/wiki/" + quote(lemma),
                         **choice))
    out = ROOT / "tools/authoring/vocab_open.json"
    out.write_text(json.dumps(rows, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    manifest = {"retrieved": "2026-10-04", "dump": "2026-09-01", "extracted": "2026-10-03", "license": "CC-BY-SA-4.0", "review": "needs_review", "sources": [], "baseline_words": len(original), "added": len(rows), "candidate_union": len(target), "rejected_records": dict(rejected)}
    urls = {"viwiktionary-20260901.jsonl.gz": "https://kaikki.org/viwiktionary/raw-wiktextract-data.jsonl.gz", "ngsl.csv": "https://www.newgeneralservicelist.com/s/NGSL_12_stats.csv", "nawl.txt": "https://www.newgeneralservicelist.com/s/NAWL_12_alphabetized_description.txt", "bsl.txt": "https://www.newgeneralservicelist.com/s/BSL_120_alphabetized_description.txt"}
    for name, url in urls.items():
        raw = (SOURCES / name).read_bytes()
        manifest["sources"].append(dict(file=name, url=url, bytes=len(raw), sha256=hashlib.sha256(raw).hexdigest()))
    shipped = original | {r["lemma"] for r in rows}
    manifest["coverage"] = {name: {"list_entries": len(words), "before_exact_lemmas": len(original & set(words)), "after_exact_lemmas": len(shipped & set(words)), "missing_lemmas": sorted(set(words) - shipped)} for name, words in (("ngsl", ngsl), ("nawl", nawl), ("bsl", bsl))}
    (ROOT / "content/open-vocabulary-manifest.json").write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({"added": len(rows), "total": len(shipped), "topics": dict(collections.Counter(r["topic"] for r in rows)), "rejected": dict(rejected)}))


if __name__ == "__main__":
    main()
