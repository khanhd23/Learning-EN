"""Create a small voice-ready manifest; no audio is downloaded or generated."""
import gzip
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
SOURCE = ROOT / "content/datasets/en_vi_master_15k_tagged.jsonl.gz"
TARGET = ROOT / "content/datasets/micro_listening_1500.json"


def main():
    chosen = {}
    with gzip.open(SOURCE, "rt", encoding="utf-8") as stream:
        for line in stream:
            item = json.loads(line)
            word = item["headword"]
            if item["pos"] not in {"n", "v", "adj", "adv", "prep", "conj"} or not item.get("ipa"):
                continue
            tags = set(item.get("tags", []))
            score = (100 if "source:ngsl" in tags else 0) + (70 if "source:tsl" in tags else 0) + (50 if "source:bsl" in tags else 0) + (20 if "source:nawl" in tags else 0)
            score += max(0, 10 - len(word.split()))
            candidate = {"id": item["id"], "headword": word, "pos": item["pos"], "ipa": item["ipa"], "ttsText": word, "audioStatus": "tts_or_pending_clip", "voice": ["en-US", "en-GB"], "review": "needs_review", "source": "original:micro-listening-manifest-2026", "selectionScore": score}
            if word not in chosen or score > chosen[word]["selectionScore"]:
                chosen[word] = candidate
    rows = sorted(chosen.values(), key=lambda x: (-x["selectionScore"], x["headword"]))[:1500]
    for row in rows:
        row.pop("selectionScore", None)
    TARGET.write_text(json.dumps({"version": 1, "count": len(rows), "items": rows, "license": "voice-generation pending review"}, ensure_ascii=False, indent=2) + "\n", encoding="utf-8")
    print(json.dumps({"count": len(rows), "output": str(TARGET)}))


if __name__ == "__main__":
    main()
