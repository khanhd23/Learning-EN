"""Select a practical 15k-headword master pack from the unrestricted archive."""
import collections
import gzip
import json
from pathlib import Path
import re

ROOT = Path(__file__).resolve().parents[1]
ARCHIVE = ROOT / "content/datasets/en_vi_lexicon.jsonl.gz"
SOURCE_DIR = ROOT / "tools/sources"
OUT = ROOT / "content/datasets"
TARGET = 15000
BAD = re.compile(r"archaic|obsolete|offensive|vulgar|derogatory|slang|proper", re.I)
COMMON_POS = {"n": 8, "v": 8, "adj": 7, "adv": 6, "prep": 5, "conj": 5, "pron": 5, "det": 5, "phrase": 3}


def source_sets():
    ngsl = {r["Lemma"].lower(): int(r["SFI Rank"]) for r in __import__("csv").DictReader((SOURCE_DIR / "ngsl.csv").read_text(encoding="utf-8-sig").splitlines()) if r.get("Lemma")}
    nawl = {x.strip().lower() for x in (SOURCE_DIR / "nawl.txt").read_text(encoding="utf-8-sig").splitlines() if re.fullmatch(r"[a-z]+(?:-[a-z]+)*", x.strip())}
    bsl = {x.strip().lower() for x in (SOURCE_DIR / "bsl.txt").read_text(encoding="utf-8-sig").splitlines() if re.fullmatch(r"[a-z]+(?:-[a-z]+)*", x.strip())}
    return ngsl, nawl, bsl


def score(items, ngsl, nawl, bsl):
    word = items[0]["headword"]
    low = word.casefold()
    best = max(items, key=lambda x: (len(x["ipa"]), len(x["senses"]), len(x["forms"])))
    joined = " ".join(best.get("categories", [])) + " " + " ".join(tag for s in best.get("senses", []) for tag in s.get("tags", []))
    examples = sum(len(s["examples"]) for x in items for s in x["senses"])
    glosses = sum(len(s["glosses"]) for x in items for s in x["senses"])
    value = 0
    if low in ngsl:
        value += 10000 - min(ngsl[low], 10000)
    if low in nawl:
        value += 2600
    if low in bsl:
        value += 2200
    value += min(examples, 4) * 90 + min(glosses, 8) * 12
    value += max((COMMON_POS.get(x["pos"], 0) for x in items), default=0)
    if len(low) <= 14:
        value += 30
    if " " not in low and re.fullmatch(r"[a-z][a-z'’-]*", low):
        value += 25
    if not best.get("ipa"):
        value -= 300
    if BAD.search(joined):
        value -= 500
    if len(low) > 24:
        value -= 400
    return value


def main():
    ngsl, nawl, bsl = source_sets()
    grouped = collections.defaultdict(list)
    with gzip.open(ARCHIVE, "rt", encoding="utf-8") as stream:
        for line in stream:
            item = json.loads(line)
            if not item["headword"] or not any(s["glosses"] for s in item["senses"]):
                continue
            # Keep learner-relevant lexical items; phrases remain available in the full archive.
            if item["pos"] == "proper_name" or BAD.search(" ".join(item.get("categories", []))):
                continue
            grouped[item["headword"]].append(item)
    ranked = sorted(grouped, key=lambda word: score(grouped[word], ngsl, nawl, bsl), reverse=True)
    selected = set(ranked[:TARGET])
    output = OUT / "en_vi_master_15k.jsonl.gz"
    records = 0
    pos = collections.Counter()
    topics = collections.Counter()
    with gzip.open(output, "wt", encoding="utf-8", newline="\n") as out:
        for word in ranked:
            if word not in selected:
                continue
            for item in grouped[word]:
                out.write(json.dumps(item, ensure_ascii=False, separators=(",", ":")) + "\n")
                records += 1
                pos[item["pos"]] += 1
                topics.update(item.get("topics", []))
    index = {
        "schemaVersion": 1,
        "dataset": "en-vi-master-15k",
        "distinctHeadwords": len(selected),
        "records": records,
        "selection": "Top 15,000 distinct headwords scored by NGSL/NAWL/BSL membership, IPA, Vietnamese glosses, translated examples, common parts of speech and topic breadth.",
        "fullArchive": "en_vi_lexicon.jsonl.gz",
        "license": "CC-BY-SA-4.0",
        "review": "needs_review",
        "posCounts": dict(pos),
        "topicCounts": dict(topics),
        "source": "https://kaikki.org/viwiktionary/",
        "files": {"records": "en_vi_master_15k.jsonl.gz", "index": "en_vi_master_15k.index.json"},
    }
    (OUT / "en_vi_master_15k.index.json").write_text(json.dumps(index, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({"headwords": len(selected), "records": records, "pos": dict(pos)}))


if __name__ == "__main__":
    main()
