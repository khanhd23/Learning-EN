"""Create the unrestricted, reusable English-Vietnamese lexicon pack.

This is intentionally separate from the small learner pack. It keeps every English
headword/POS record available in the licensed Vietnamese Wiktionary extraction,
including all senses, usage examples, forms, related expressions, IPA and topic
categories. Nothing is promoted to "verified" by this script.
"""
import collections
import gzip
import hashlib
import json
from pathlib import Path
import re
import unicodedata

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "tools/sources/viwiktionary-20260901.jsonl.gz"
OUT = ROOT / "content/datasets"
POS = {"noun": "n", "verb": "v", "adj": "adj", "adv": "adv", "prep": "prep", "conj": "conj", "pron": "pron", "det": "det", "article": "article", "interj": "interj", "phrase": "phrase", "idiom": "idiom", "proper name": "proper_name"}
TOPIC_HINTS = {
    "business": "business", "finance": "finance", "banking": "finance", "account": "finance",
    "comput": "technology", "internet": "technology", "software": "technology", "medical": "health",
    "medicine": "health", "biology": "science", "chemistry": "science", "physics": "science",
    "law": "law", "legal": "law", "sport": "sports", "music": "arts", "film": "arts",
    "food": "food", "cooking": "food", "transport": "travel", "aviation": "travel",
    "school": "education", "education": "education", "grammar": "language", "linguistics": "language",
    "kinh doanh": "business", "thương mại": "business", "tài chính": "finance", "ngân hàng": "finance",
    "kế toán": "finance", "công nghệ": "technology", "máy tính": "technology", "y học": "health",
    "sức khỏe": "health", "sinh học": "science", "hóa học": "science", "vật lý": "science",
    "luật": "law", "pháp lý": "law", "thể thao": "sports", "âm nhạc": "arts", "điện ảnh": "arts",
    "ẩm thực": "food", "nấu ăn": "food", "giao thông": "travel", "hàng không": "travel",
    "giáo dục": "education", "ngôn ngữ": "language", "toán": "mathematics", "hàng hải": "travel",
    "quân sự": "military", "chính trị": "politics", "tôn giáo": "religion", "sinh thái": "environment",
    "môi trường": "environment", "địa lý": "geography", "xã hội": "society", "tâm lý": "psychology",
}


def nfc(value):
    return unicodedata.normalize("NFC", value) if isinstance(value, str) else value


def text(value):
    return nfc(re.sub(r"\s+", " ", value or "").strip())


def topics(categories):
    result = set()
    for category in categories:
        low = category.casefold()
        for needle, topic in TOPIC_HINTS.items():
            if needle in low:
                result.add(topic)
    return sorted(result) or ["general"]


def compact_sense(sense):
    examples = []
    for ex in sense.get("examples", []):
        en = text(ex.get("text"))
        vi = text(ex.get("translation"))
        if en or vi:
            examples.append({"en": en, "vi": vi})
    return {
        "glosses": [text(x) for x in sense.get("glosses", []) if text(x)],
        "examples": examples,
        "tags": sorted(set(sense.get("tags", []))),
        "topics": topics(sense.get("categories", [])),
    }


def compact(record, record_id):
    categories = [text(x) for x in record.get("categories", []) if text(x)]
    senses = [compact_sense(s) for s in record.get("senses", [])]
    return {
        "id": record_id,
        "headword": nfc(record["word"]),
        "pos": POS.get(record.get("pos"), record.get("pos", "unknown")),
        "posSource": record.get("pos"),
        "ipa": sorted({text(s.get("ipa")) for s in record.get("sounds", []) if text(s.get("ipa"))}),
        "forms": [{"form": nfc(f.get("form", "")), "tags": sorted(set(f.get("tags", [])))} for f in record.get("forms", []) if f.get("form")],
        "senses": senses,
        "related": [{"expression": nfc(x.get("word", "")), "tags": sorted(set(x.get("tags", []))), "note": text(x.get("sense", ""))} for x in record.get("related", []) if x.get("word")],
        "topics": topics(categories),
        "categories": categories,
        "source": "https://vi.wiktionary.org/wiki/" + record["word"].replace(" ", "_"),
        "license": "CC-BY-SA-4.0",
        "review": "needs_review",
    }


def main():
    OUT.mkdir(parents=True, exist_ok=True)
    path = OUT / "en_vi_lexicon.jsonl.gz"
    stats = collections.Counter()
    headwords = collections.Counter()
    pos = collections.Counter()
    topic_counts = collections.Counter()
    source_hash = hashlib.sha256(SOURCE.read_bytes()).hexdigest()
    record_id = 0
    with gzip.open(SOURCE, "rt", encoding="utf-8") as inp, gzip.open(path, "wt", encoding="utf-8", newline="\n") as out:
        for line in inp:
            record = json.loads(line)
            if record.get("lang_code") != "en" or not record.get("word"):
                continue
            item = compact(record, f"envi_{record_id:07d}")
            out.write(json.dumps(item, ensure_ascii=False, separators=(",", ":")) + "\n")
            record_id += 1
            stats["records"] += 1
            headwords[item["headword"]] += 1
            pos[item["pos"]] += 1
            for topic in item["topics"]:
                topic_counts[topic] += 1
            stats["senses"] += len(item["senses"])
            stats["examples"] += sum(len(s["examples"]) for s in item["senses"])
            stats["forms"] += len(item["forms"])
            stats["related"] += len(item["related"])
    index = {
        "schemaVersion": 1,
        "dataset": "en-vi-full-lexicon",
        "description": "Unrestricted source-derived English-Vietnamese lexicon; use packs and review layers for learner-facing experiences.",
        "records": stats["records"],
        "distinctHeadwords": len(headwords),
        "senses": stats["senses"],
        "examples": stats["examples"],
        "inflectionAndVariantForms": stats["forms"],
        "relatedExpressions": stats["related"],
        "posCounts": dict(pos),
        "topicCounts": dict(topic_counts),
        "source": "https://kaikki.org/viwiktionary/",
        "sourceSnapshot": "2026-09-01 Wiktionary dump; Kaikki extraction 2026-10-03",
        "sourceSha256": source_hash,
        "license": "CC-BY-SA-4.0",
        "reviewPolicy": "Every record is needs_review. Source-derived data is not a claim of pedagogical suitability.",
        "files": {"records": "en_vi_lexicon.jsonl.gz", "index": "en_vi_lexicon.index.json"},
    }
    (OUT / "en_vi_lexicon.index.json").write_text(json.dumps(index, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({"records": stats["records"], "distinctHeadwords": len(headwords), "senses": stats["senses"], "examples": stats["examples"], "forms": stats["forms"]}))


if __name__ == "__main__":
    main()
