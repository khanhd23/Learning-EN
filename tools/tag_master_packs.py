"""Attach reusable multi-axis tags and emit focused learning-pack manifests."""
import collections
import gzip
import json
from pathlib import Path

from word_lists import load_all_lists

ROOT = Path(__file__).resolve().parents[1]
DATA = ROOT / "content/datasets"
INPUT = DATA / "en_vi_master_15k.jsonl.gz"
OUTPUT = DATA / "en_vi_master_15k_tagged.jsonl.gz"
DOMAIN_HINTS = {
    "travel": "travel transport airport hotel flight passport train bus taxi", "food": "food meal cook kitchen restaurant drink bread rice",
    "health": "health medical medicine doctor hospital body disease", "education": "school student teacher study exam lesson university",
    "technology": "computer software internet digital data network website device", "science": "science biology chemistry physics research experiment",
    "environment": "environment climate energy pollution nature earth", "finance": "money bank finance tax loan payment budget cost price",
    "law": "law legal court crime contract policy right", "sports": "sport football basketball game player match", "arts": "music film art theater painting book",
    "home": "house home room furniture kitchen bathroom bed garden", "people": "person family friend child man woman", "work": "office job work employee manager meeting company project",
    "business": "business market sale customer product service company", "nature": "animal plant tree bird fish weather", "time_numbers": "time day week month year number first second",
}
BAD = {"proper_name", "name", "abbrev", "character", "prefix", "suffix"}


def main():
    lists = load_all_lists(ROOT)
    counts = collections.Counter()
    pack_counts = collections.Counter()
    records = 0
    with gzip.open(INPUT, "rt", encoding="utf-8") as inp, gzip.open(OUTPUT, "wt", encoding="utf-8", newline="\n") as out:
        for line in inp:
            item = json.loads(line)
            word = item["headword"].casefold()
            glosses = [g for s in item.get("senses", []) for g in s.get("glosses", [])]
            text = " ".join([word, *item.get("categories", []), *glosses]).casefold()
            tags = {"review:needs_review"}
            memberships = {name for name, values in lists.items() if word in values}
            tags.update("source:" + x for x in memberships)
            if "ngsl" in memberships or item["pos"] in {"prep", "conj", "pron", "det", "article"}:
                tags.add("goal:communication")
            if memberships.intersection({"bsl", "tsl"}) or any(x in text for x in DOMAIN_HINTS["business"].split()):
                tags.add("goal:workplace")
            if "nawl" in memberships or any(x in text for x in DOMAIN_HINTS["science"].split() + DOMAIN_HINTS["education"].split()):
                tags.add("goal:academic")
            if "tsl" in memberships:
                tags.add("exam:toeic")
            if "ngsl" in memberships:
                tags.add("exam:general_english")
            for domain, hints in DOMAIN_HINTS.items():
                if any(re.search(r"\b" + re.escape(h) + r"\b", text) for h in hints.split()):
                    tags.add("domain:" + domain)
            if item["pos"] in {"prep", "conj", "pron", "det", "article"}:
                tags.add("usage:function_word")
            if item["pos"] in {"v", "n", "adj", "adv"} and item.get("forms"):
                tags.add("learning:word_family_candidate")
            if item.get("related"):
                tags.add("usage:collocation")
                if any("idiom" in x.get("tags", []) for x in item["related"]):
                    tags.add("usage:idiom")
            if len(item.get("senses", [])) > 1:
                tags.add("learning:polysemy_candidate")
            if item.get("ipa"):
                tags.add("learning:pronunciation_candidate")
            if item["pos"] == "n" and not any(x in text for x in "idea concept system process ability".split()):
                tags.add("learning:picture_candidate")
            if any(x in text for x in "formal academic legal official written report".split()):
                tags.add("usage:formal")
            if any(x in text for x in "conversation spoken talk informal".split()):
                tags.add("usage:spoken")
            item["tags"] = sorted(tags)
            out.write(json.dumps(item, ensure_ascii=False, separators=(",", ":")) + "\n")
            records += 1
            counts.update(tags)
            for tag in tags:
                if tag.startswith(("goal:", "exam:", "domain:")):
                    pack_counts[tag] += 1
    manifest = {"schemaVersion": 1, "dataset": "en-vi-master-15k-tagged", "records": records, "distinctHeadwords": 15000, "axes": "content/datasets/TAXONOMY.json", "packs": dict(pack_counts), "tagCounts": dict(counts), "review": "needs_review", "license": "CC-BY-SA-4.0", "files": {"records": "en_vi_master_15k_tagged.jsonl.gz", "taxonomy": "TAXONOMY.json"}}
    (DATA / "en_vi_master_15k_tagged.index.json").write_text(json.dumps(manifest, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({"records": records, "packs": dict(pack_counts)}))


if __name__ == "__main__":
    main()
