"""Licensed dictionary subset; all imported and corrected entries require human review."""
import json
from pathlib import Path
import lib

HERE = Path(__file__).resolve().parent
rows = json.loads((HERE / "vocab_open.json").read_text(encoding="utf-8"))
corrections = {}
for line in (HERE / "vocab_open_corrections.tsv").read_text(encoding="utf-8").splitlines():
    if not line or line.startswith("#"):
        continue
    lemma, pos, ipa, gloss, en, vi = line.split("|")
    corrections[lemma] = dict(pos=pos, ipa=ipa, gloss=gloss, examples=[dict(en=en, vi=vi)])

topics = {
    "open_actions": ("Động từ thông dụng mở rộng", "🏃", 160),
    "open_descriptions": ("Miêu tả & sắc thái mở rộng", "🎨", 280),
    "open_things": ("Con người, sự vật & khái niệm", "🌍", 210),
    "open_connectors": ("Từ chức năng & diễn đạt", "💬", 30),
    "open_academic": ("Từ vựng học thuật mở rộng", "🎓", 240),
    "open_business": ("Từ vựng công việc mở rộng", "💼", 190),
}
seen = {w["id"] for w in lib.WORDS}
for tid, (title, icon, hue) in topics.items():
    lib.topic(tid, title, icon, hue)
    for original in rows:
        row = {**original, **corrections.get(original["lemma"], {})}
        topic = row["topic"]
        if topic == "open_general":
            topic = {"v": "open_actions", "n": "open_things", "adj": "open_descriptions", "adv": "open_descriptions"}.get(row["pos"], "open_connectors")
        if topic != tid:
            continue
        wid = lib.slug(row["lemma"])
        if wid in seen:
            raise ValueError(f"Open vocabulary ID collision: {wid}")
        seen.add(wid)
        example = row["examples"][0]
        lib.W(row["lemma"], row["pos"], row["ipa"], row["level"], row["gloss"], example["en"], example["vi"])
        word = lib.WORDS[-1]
        for i, ex in enumerate(row["examples"][1:], 2):
            eid = f"{wid}_s1_ex{i}"
            word["senses"][0]["ex"].append(dict(id=eid, text=lib.nfc(ex["en"])))
            word["_vi"]["senses"][0]["ex"][eid] = lib.nfc(ex["vi"])
        word.update(needs_review=True, source=row["source"], license="CC-BY-SA-4.0",
                    sourceLists=row["lists"], editorialCorrection=row["lemma"] in corrections)
        # Short source examples remain usage phrases, never mislabeled as full sentences.
        word["usageKind"] = "sentence_or_phrase"
