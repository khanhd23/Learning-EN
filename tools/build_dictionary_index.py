"""Build the offline inverted index used by the dictionary search design."""
import gzip
import json
import re
import unicodedata
from collections import defaultdict
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "content/datasets/en_vi_master_15k_tagged.jsonl.gz"
TARGET = ROOT / "content/datasets/en_vi_dictionary_search.index.json.gz"


def norm(value):
    value = unicodedata.normalize("NFD", value.casefold())
    return re.sub(r"\p{M}+", "", value) if False else re.sub(r"[\u0300-\u036f]+", "", value).replace("đ", "d")


def tokens(value):
    return {x for x in re.findall(r"[a-z0-9]+", norm(value)) if len(x) > 1}


def main():
    postings = defaultdict(lambda: {"ids": set(), "fields": set()})
    records = 0
    with gzip.open(SOURCE, "rt", encoding="utf-8") as stream:
        for line in stream:
            item = json.loads(line)
            wid = item["id"]
            records += 1
            fields = {
                "headword": [item["headword"]],
                "gloss": [g for s in item.get("senses", []) for g in s.get("glosses", [])],
                "example": [e for s in item.get("senses", []) for x in s.get("examples", []) for e in (x.get("en", ""), x.get("vi", ""))],
                "topic": item.get("topics", []),
                "tag": item.get("tags", []),
            }
            for field, values in fields.items():
                for value in values:
                    for token in tokens(value):
                        postings[token]["ids"].add(wid)
                        postings[token]["fields"].add(field)
    index = {
        "version": 1,
        "dataset": "en-vi-master-15k-tagged",
        "records": records,
        "normalization": "NFD lowercase accent-insensitive; Vietnamese đ→d",
        "postings": {token: {"ids": sorted(value["ids"]), "fields": sorted(value["fields"])} for token, value in sorted(postings.items())},
    }
    with gzip.open(TARGET, "wt", encoding="utf-8", newline="\n") as out:
        json.dump(index, out, ensure_ascii=False, separators=(",", ":"))
    print(json.dumps({"records": records, "tokens": len(postings), "bytes": TARGET.stat().st_size}))


if __name__ == "__main__":
    main()
