"""Curated expansion from the tagged offline English-Vietnamese source pack.

This module deliberately promotes a bounded, useful slice into the learner bank. Every promoted
record remains ``needs_review``: the source is licensed, but machine selection is not editorial
approval. The full archive stays dictionary/search material and is never shipped as a lesson bank.
"""
import collections
import gzip
import json
import re
from pathlib import Path

import lib

ROOT = Path(__file__).resolve().parents[2]
SOURCE = ROOT / "content/datasets/en_vi_master_15k_tagged.jsonl.gz"
TARGET_ADDITIONS = 1700

SUPPORTED_POS = {"n", "v", "adj", "adv", "prep", "conj", "pron", "det", "article", "intj", "phrase"}
BAD = re.compile(r"\{\{|\}\}|\[\[|\]\]|^The |proper name|archaic|obsolete|vulgar|offensive|slang", re.I)


def _clean(text):
    return re.sub(r"\s+", " ", str(text or "")).strip()


def _topic(tags, pos, text):
    tags = set(tags)
    domain = next((t.split(":", 1)[1] for t in tags if t.startswith("domain:")), "")
    direct = {
        "animals": "animals", "nature": "weather", "food": "food", "home": "home_basics",
        "travel": "travel", "health": "health", "education": "education", "work": "office",
        "business": "commerce", "finance": "finance", "technology": "tech", "science": "academic",
        "environment": "environment", "law": "legal", "sports": "sports", "arts": "entertainment",
        "time_numbers": "time_numbers", "people": "family", "daily_life": "daily",
    }
    if domain == "nature" and any(x in text for x in ("animal", "bird", "fish", "mammal", "insect", "pet", "wildlife")):
        return "animals"
    if domain in direct:
        return direct[domain]
    if "learning:picture_candidate" in tags:
        if any(x in text for x in ("fruit", "apple", "banana", "orange", "grape", "berry")):
            return "food"
        return "open_things" if pos == "n" else "daily"
    if "exam:toeic" in tags or "goal:workplace" in tags:
        return "office"
    if "goal:academic" in tags:
        return "academic"
    if "usage:phrasal_verb" in tags or pos == "v":
        return "core_verbs"
    if "usage:collocation" in tags or "goal:communication" in tags:
        return "expressions"
    return "open_things" if pos == "n" else "open_descriptions"


def _score(item):
    tags = set(item.get("tags", []))
    word = item["headword"].casefold()
    score = 0
    score += 18 if "goal:communication" in tags else 0
    score += 18 if "exam:toeic" in tags else 0
    score += 15 if "goal:academic" in tags else 0
    score += 15 if "goal:workplace" in tags else 0
    score += 16 if "learning:picture_candidate" in tags else 0
    score += 8 if "usage:collocation" in tags else 0
    score += 8 if "usage:phrasal_verb" in tags else 0
    score += 6 if "learning:polysemy_candidate" in tags else 0
    score += 5 if "learning:word_family_candidate" in tags else 0
    score += 4 if any(t.startswith("source:") for t in tags) else 0
    score += min(8, sum(len(s.get("examples", [])) for s in item.get("senses", [])))
    score += min(6, len(item.get("senses", [])))
    if len(word) > 28:
        score -= 8
    if " " in word:
        score += 2  # useful phrases are intentionally retained
    return score


def _best_sense(item):
    senses = []
    for sense in item.get("senses", []):
        gloss = _clean("; ".join(sense.get("glosses", [])))
        examples = [
            {"en": _clean(e.get("en")), "vi": _clean(e.get("vi"))}
            for e in sense.get("examples", [])
            if _clean(e.get("en")) and _clean(e.get("vi"))
        ]
        if gloss and examples:
            senses.append((len(examples), len(gloss), gloss, examples))
    return max(senses, key=lambda x: (x[0], x[1]), default=None)


existing = {w["lemma"].casefold() for w in lib.WORDS}
existing_ids = {w["id"] for w in lib.WORDS}
by_headword = {}
with gzip.open(SOURCE, "rt", encoding="utf-8") as stream:
    for line in stream:
        item = json.loads(line)
        lemma = _clean(item.get("headword"))
        pos = item.get("pos")
        if not lemma or lemma.casefold() in existing or lib.slug(lemma) in existing_ids:
            continue
        if pos not in SUPPORTED_POS or BAD.search(lemma):
            continue
        sense = _best_sense(item)
        if not sense:
            continue
        if not item.get("ipa") or not _clean(item["ipa"][0]):
            continue
        if len(lemma) > 45 or any(len(e["en"]) > 180 or len(e["vi"]) > 240 for e in sense[3]):
            continue
        old = by_headword.get(lemma.casefold())
        if old is None or _score(item) > _score(old):
            by_headword[lemma.casefold()] = item

ranked = sorted(by_headword.values(), key=lambda x: (-_score(x), x["headword"].casefold()))
selected = ranked[:TARGET_ADDITIONS]

for item in selected:
    lemma = _clean(item["headword"])
    wid = lib.slug(lemma)
    if wid in existing_ids:
        continue
    pos = item["pos"]
    sense = _best_sense(item)
    _, _, gloss, examples = sense
    example = examples[0]
    tags = set(item.get("tags", []))
    text = " ".join([lemma, gloss, *item.get("topics", [])]).casefold()
    topic_id = _topic(tags, pos, text)
    if topic_id not in {t["id"] for t in lib.TOPICS}:
        topic_id = "daily"
    # Existing authoring modules already registered all topics. Select one without adding
    # duplicate topic metadata to the generated locale file.
    lib._current_topic = topic_id
    lib.W(
        lemma, pos, (item.get("ipa") or [""])[0],
        2 if "learning:picture_candidate" in tags or "goal:communication" in tags
        else 3 if "exam:toeic" in tags or "goal:workplace" in tags else 4,
        gloss, example["en"], example["vi"],
        coll=[_clean(r.get("expression")) for r in item.get("related", []) if _clean(r.get("expression"))][:4],
    )
    word = next(w for w in lib.WORDS if w["id"] == wid)
    existing_ids.add(wid)
    word.update(
        needs_review=True,
        source=item.get("source", "https://vi.wiktionary.org/"),
        license="CC-BY-SA-4.0",
        sourceLists=sorted(t.split(":", 1)[1] for t in tags if t.startswith("source:")) or ["tagged-master"],
        sourceTags=sorted(tags),
        editorialCorrection=False,
        usageKind="picture_or_phrase_candidate" if "learning:picture_candidate" in tags else "sentence_or_phrase",
    )
