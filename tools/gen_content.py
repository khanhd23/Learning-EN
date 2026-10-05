"""Compiles tools/authoring/*.py into content/en/*.json + content/i18n/vi.json.

Usage: python tools/gen_content.py
"""
import hashlib
import importlib
import json
import os
import random
import re
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.insert(0, os.path.join(ROOT, "tools", "authoring"))
import lib  # noqa: E402

DEFS_PATH = os.path.join(ROOT, "tools", "sources", "wordnet_defs.json")
with open(DEFS_PATH, encoding="utf-8") as f:
    WORDNET_DEFS = json.load(f)
def load_editor_rows(filename):
    rows = {}
    with open(os.path.join(ROOT, "tools", "authoring", filename), encoding="utf-8") as f:
        for line in f:
            if not line.strip() or line.startswith("sense_id\t"):
                continue
            sid, definition, gloss, example, example_vi, note = line.rstrip("\n").split("\t", 5)
            rows[sid] = {
                "def": definition, "gloss": gloss, "example": example,
                "example_vi": example_vi, "note": note,
            }
    return rows


EDITOR_BATCH1 = load_editor_rows("senses_editor_batch1.tsv")
EDITOR_BATCH2 = load_editor_rows("senses_editor_batch2.tsv")

VI_CHARS = set("áạảãấầẩẫậắằẳẵặđẹẻẽếềểễệỉĩịọỏõốồổỗộớờởỡợụủũứừửữựỳỷỹỵ")


def has_vi(text):
    return any(c in VI_CHARS for c in (text or "").lower())


def normalize_ipa(value):
    # IPA is stored without UI slashes or syllable dots. Remaining unusual symbols
    # are intentionally preserved for the audit to flag rather than guessed away.
    return (value or "").replace("/", "").replace(".", "").strip()


def normalize_gloss(value):
    value = (value or "").strip()
    value = value.rstrip(".").strip()
    if value and value[0].isupper() and not value.isupper():
        value = value[0].lower() + value[1:]
    return value


def definition_for(word, sense, index):
    existing = (sense.get("def") or "").strip()
    if existing.lower().rstrip(".") in {
        "a word or phrase used in english", "a person, place, thing, or idea",
        "describing a quality or state", "a word showing a relation or place",
        "in a particular way or manner", "a word that joins words or ideas",
        "to do or make something", "how something is done or how it happens",
    }:
        return "", "template"
    if existing:
        return existing, "wordnet-auto"
    lemma = word["lemma"].lower().replace(" ", "_")
    defs = (WORDNET_DEFS.get(lemma) or {}).get(sense.get("pos"), [])
    if defs:
        return defs[min(index, len(defs) - 1)], "wordnet-auto"
    templates = {
        "n": "a person, place, thing, or idea",
        "v": "to do or make something",
        "adj": "describing a quality or state",
        "adv": "in a particular way or manner",
        "prep": "a word showing a relation or place",
        "conj": "a word that joins words or ideas",
    }
    return "", "template"


def upgrade_schema(words, vi):
    """Upgrade authored output to the English-core/content-standard schema."""
    quarantine = vi.setdefault("quarantine", {"collocations": {}, "examples": {}})
    for word in words:
        added = {
            "place": ("n", "place_s2"),
            "kind": ("n", "kind_s2"),
            "live": ("v", "live_s2"),
        }.get(word["id"])
        if added and not any(s.get("pos") == added[0] for s in word.get("senses", [])):
            word["senses"].append({"pos": added[0], "ex": [{"id": added[1], "text": ""}]})
            locale_word = vi.get("words", {}).get(word["id"])
            if locale_word:
                locale_word.setdefault("senses", []).append({"g": "", "ex": {}})
        if word["id"] in {"book", "place", "kind", "live"}:
            preferred = {"book": "n", "place": "n", "kind": "n", "live": "v"}[word["id"]]
            locale_word = vi.get("words", {}).get(word["id"])
            word["senses"].sort(key=lambda s: 0 if s.get("pos") == preferred else 1)
            if locale_word:
                reordered = []
                for sense in word["senses"]:
                    example_id = (sense.get("ex") or [{}])[0].get("id")
                    reordered.append(next((s for s in locale_word["senses"] if example_id in s.get("ex", {})), {"g": "", "ex": {}}))
                locale_word["senses"] = reordered
        word["ipa"] = normalize_ipa(word.get("ipa"))
        word.setdefault("levelReason", "")
        word.setdefault("forms", {})
        word.setdefault("grammarIds", [])
        word["tier"] = "bronze"
        for index, sense in enumerate(word.get("senses", [])):
            sid = sense.get("id") or ((sense.get("ex") or [{}])[0].get("id")) or f"{word['id']}_s{index + 1}"
            sense["id"] = sid
            sense["def"], def_source = definition_for(word, sense, index)
            if sense["def"].strip().lower().rstrip(".") in {
                "a word or phrase used in english", "a person, place, thing, or idea",
                "describing a quality or state", "a word showing a relation or place",
                "in a particular way or manner", "a word that joins words or ideas",
                "to do or make something", "how something is done or how it happens",
            }:
                sense["def"] = ""
                def_source = "template"
            curated = EDITOR_BATCH1.get(sid) if index == 0 else None
            if index == 0 and sid in EDITOR_BATCH2:
                curated = EDITOR_BATCH2[sid]
            if curated:
                sense["def"] = curated["def"]
                def_source = "editor"
                locale_word = vi.get("words", {}).get(word["id"])
                if locale_word and len(locale_word.get("senses", [])) > index:
                    locale_sense = locale_word["senses"][index]
                    locale_sense["g"] = curated["gloss"]
                    locale_sense.setdefault("ex", {})[sense["id"]] = curated["example_vi"]
                if sense.get("ex"):
                    sense["ex"][0]["text"] = curated["example"]
                    sense["ex"][0]["hl"] = word["lemma"]
            sense["defSource"] = def_source
            sense.setdefault("register", "neutral")
            if def_source != "editor":
                word["needs_review"] = True
            for example in sense.get("ex", []):
                example["hl"] = example.get("hl") or word["lemma"].split()[0]
                if has_vi(example.get("text", "")):
                    quarantine["examples"][example["id"]] = example.pop("text")
                    example["text"] = "Example sentence needs review."
            if has_vi(" ".join(sense.get("coll") or [])):
                quarantine["collocations"][sid] = sense.get("coll") or []
                sense["coll"] = []
        if has_vi(" ".join(word.get("coll") or [])):
            quarantine["collocations"][word["id"]] = word.get("coll") or []
            word["coll"] = []

        locale_word = vi.get("words", {}).get(word["id"])
        if locale_word:
            for locale_sense in locale_word.get("senses", []):
                locale_sense["g"] = normalize_gloss(locale_sense.get("g"))
                if not locale_sense.get("g") and word["id"] == "should":
                    locale_sense["g"] = "nên, phải"

    # Normalize the handful of legacy Vietnamese formula strings while preserving
    # their learner-language source in the locale pack.
    formula_en = {
        "articles": "a + consonant sound · an + vowel sound · the + specific or known noun",
        "plurals": "N + s / es / ies · irregular: man → men, child → children",
        "prep_time": "at + clock time or exact place · on + day or surface · in + month, year, season, or inside space",
        "there_is": "There is + singular or uncountable N · There are + plural N",
        "can_could": "S + can/could + base verb",
        "word_order": "S + V + O + Place + Time · Adjectives come before nouns",
        "quantifiers": "many/few + countable N · much/little + uncountable N · some/any/a lot of: both",
        "conditionals": "0: If + present, present · 1: If + present, will + V · 2: If + past, would + V",
        "relative": "N (person) + who · N (thing) + which · that: both · whose + N · where: place",
        "conj_prep": "Conjunction + CLAUSE (S + V) · Preposition + NOUN / V-ing",
        "agreement": "Singular subject → V(s/es) · plural subject → base verb",
        "conditional3": "If + had + V3, would have + V3 · mixed: If + had + V3, would + V (now)",
        "participle": "V-ing (active) · V3 (passive) · Having + V3 (earlier action)",
        "wish": "wish + past (present) · wish + had V3 (past) · wish + would (want change)",
        "inversion_basic": "So/Neither + auxiliary + S · Not only + auxiliary + S + V, but also...",
        "subjunctive": "suggest / recommend / insist / require / It is essential that + S + base verb",
        "inversion_adv": "No sooner had S V3 than... · Hardly had S V3 when... · Only after/when... + inversion · Should + S + V (= If)",
        "ellipsis": "I think so / I hope not · one/ones · do so · auxiliary replaces a whole phrase",
    }
    for point in vi.get("grammar", {}).values():
        if "formula" in point and has_vi(point["formula"]):
            point.setdefault("formulaVi", point["formula"])
    return formula_en

MODULES = [
    "vocab_work", "vocab_life", "vocab_everyday", "vocab_modern", "vocab_foundation", "senses", "vocab_open", "vocab_expansion", "vocab_picture", "confusables",
    "grammar_l1", "grammar_l2", "grammar_l3", "grammar_l4", "grammar_l5",
    "exam_p5", "exam_p6", "school", "pet_lines",
]


def qid(q):
    key = "|".join([q["type"], q.get("fmt") or "", q.get("stem") or "", "/".join(q.get("opts") or [])])
    return "q" + hashlib.sha1(key.encode("utf-8")).hexdigest()[:9]


def main():
    loaded = []
    for m in MODULES:
        try:
            loaded.append(importlib.import_module(m))
        except ModuleNotFoundError as e:
            if e.name != m:
                raise
            print(f"(skip missing module {m})")
    pet = next((getattr(m, "PET_LINES") for m in loaded if hasattr(m, "PET_LINES")), {})
    ui_tips = next((getattr(m, "QTYPE_TIPS") for m in loaded if hasattr(m, "QTYPE_TIPS")), {})

    vi = dict(topics={}, words={}, conf={}, grammar={}, q={}, passages={}, pet=pet, tips=ui_tips)

    # Topics
    topics = []
    for t in lib.TOPICS:
        vi["topics"][t["id"]] = t["vi"]
        topics.append(dict(id=t["id"], icon=t["icon"], hue=t["hue"]))

    # Words
    words = []
    for w in lib.WORDS:
        v = w.pop("_vi")
        vi["words"][w["id"]] = {k: val for k, val in v.items() if val}
        words.append(w)

    lemma_to_id = {}
    for w in words:
        lemma_to_id.setdefault(w["lemma"].lower(), w["id"])
        w["pos"] = w["senses"][0]["pos"]

    conf = []
    for c in lib.CONFUSABLES:
        v = c.pop("_vi")
        vi["conf"][c["id"]] = v
        c["wordIds"] = [lemma_to_id.get(x.lower()) for x in c["words"]]
        conf.append(c)
        for wid in c["wordIds"]:
            if wid:
                next(w for w in words if w["id"] == wid).setdefault("conf", []).append(c["id"])

    formula_en = upgrade_schema(words, vi)

    # Grammar
    grammar = []
    for g in lib.GRAMMAR:
        vi["grammar"][g["id"]] = g.pop("_vi")
        if g["id"] in formula_en and has_vi(g.get("formula", "")):
            vi["grammar"][g["id"]]["formulaVi"] = g["formula"]
            g["formula"] = formula_en[g["id"]]
        grammar.append(g)
    gp_level = {g["id"]: g["level"] for g in grammar}

    # Questions: stable ids, balanced answer positions for option-based items.
    questions = []
    seen = set()
    mc_index = 0
    rng = random.Random(20261004)
    block = []
    for q in lib.QUESTIONS:
        q = dict(q)
        expl = q.pop("_vi")
        q["id"] = qid(q)
        if q["id"] in seen:
            raise SystemExit(f"duplicate question: {q.get('stem')}")
        seen.add(q["id"])
        if q.get("level") is None:
            q["level"] = gp_level.get(q.get("gp"), 2)
        if "opts" in q and q["type"] != "E05":
            if not block:
                block = [0, 1, 2, 3]
                rng.shuffle(block)
            target = block.pop() % len(q["opts"])
            opts = q["opts"]
            correct = opts[0]
            others = opts[1:]
            random.Random(q["id"]).shuffle(others)
            others.insert(target, correct)
            q["opts"] = others
            q["ans"] = target
            mc_index += 1
        if expl:
            vi["q"][q["id"]] = expl
        if q.get("vi"):
            vi.setdefault("q_translation", {})[q["id"]] = q.pop("vi")
        if has_vi(q.get("stem", "")):
            vi.setdefault("q_notes", {})[q["id"]] = q["stem"]
            q["stem"] = re.sub(r"^\([^)]*[áạảãấầẩẫậắằẳẵặđẹẻẽếềểễệỉĩịọỏõốồổỗộớờởỡợụủũứừửữựỳỷỹỵ][^)]*\)\s*", "", q["stem"]).strip()
        if q.get("fix"):
            legacy_fix = q["fix"]
            if has_vi(legacy_fix):
                vi.setdefault("q_fix", {})[q["id"]] = legacy_fix
                q["fix"] = re.sub(r"\s*\([^()]*[áạảãấầẩẫậắằẳẵặđẹẻẽếềểễệỉĩịọỏõốồổỗộớờởỡợụủũứừửữựỳỷỹỵ][^()]*\)", "", legacy_fix).strip()
        if q.get("type") == "E04" and q["id"] not in vi.get("q_fix", {}):
            vi.setdefault("q_fix", {})[q["id"]] = q.get("fix", "")
        q = {k: v for k, v in q.items() if v not in (None, [], "")}
        if q["type"] == "E04":
            q["ans"] = q.get("ans", 0)
        questions.append(q)

    # Tier is derived from the exact audit rules at generation time. This keeps
    # the lesson/dictionary boundary reproducible and never creates gold data.
    import audit_content
    ranks = audit_content.ngsl_ranks()
    easy = {k for k, r in ranks.items() if r <= 2000}
    topic_size = {}
    for w in words:
        for topic in w.get("topics") or []:
            topic_size[topic] = topic_size.get(topic, 0) + 1
    for w in words:
        errors, _, _ = audit_content.audit_word(w, vi["words"].get(w["id"]), ranks, easy, topic_size)
        w["tier"] = "bronze" if errors else "silver"

    passages = []
    for p in lib.PASSAGES:
        p = dict(p)
        vi["passages"][p["id"]] = {"blanks": []}
        blanks = []
        for i, b in enumerate(p["blanks"]):
            b = dict(b)
            vi["passages"][p["id"]]["blanks"].append(b.pop("_vi"))
            correct = b["opts"][0]
            others = b["opts"][1:]
            random.Random(p["id"] + str(i)).shuffle(others)
            target = random.Random(p["id"] + "pos" + str(i)).randrange(4)
            others.insert(target, correct)
            b["opts"] = others
            b["ans"] = target
            blanks.append(b)
        p["blanks"] = blanks
        passages.append(p)

    out_en = os.path.join(ROOT, "content", "en")
    out_vi = os.path.join(ROOT, "content", "i18n")
    os.makedirs(out_en, exist_ok=True)
    os.makedirs(out_vi, exist_ok=True)

    # Keep the English corpus globally reusable.  Authoring examples may contain
    # local names or places from older exercises; neutralize those labels in the
    # generated English assets while leaving the Vietnamese teaching notes intact.
    def neutralize_local_names(value):
        if isinstance(value, str):
            replacements = (
                (r"\bHo Chi Minh City\b", "a large city"),
                (r"\bDa Nang\b", "a coastal city"),
                (r"\bHaiduong\b", "a city"),
                (r"\bVietnam\b", "the country"),
                (r"\bHanoi\b", "the capital city"),
                (r"\bSaigon\b", "the southern city"),
                (r"\bHue\b", "the old city"),
                (r"\bLan\b", "Alex"),
                (r"\bMai\b", "Sam"),
            )
            for pattern, replacement in replacements:
                value = re.sub(pattern, replacement, value)
            return value
        if isinstance(value, list):
            return [neutralize_local_names(item) for item in value]
        if isinstance(value, dict):
            return {key: neutralize_local_names(item) for key, item in value.items()}
        return value

    topics = neutralize_local_names(topics)
    words = neutralize_local_names(words)
    conf = neutralize_local_names(conf)
    grammar = neutralize_local_names(grammar)
    questions = neutralize_local_names(questions)
    passages = neutralize_local_names(passages)

    def dump(path, obj):
        if "--check" in sys.argv:
            with open(path, encoding="utf-8") as f:
                if json.load(f) != obj:
                    raise SystemExit(f"Generated content differs from {path}")
            return
        with open(path, "w", encoding="utf-8") as f:
            json.dump(obj, f, ensure_ascii=False, separators=(",", ":"))

    dump(os.path.join(out_en, "words.json"), dict(topics=topics, words=words, confusables=conf))
    dump(os.path.join(out_en, "grammar.json"), dict(points=grammar))
    dump(os.path.join(out_en, "questions.json"), dict(questions=questions, passages=passages))
    # A small, reusable relation index keeps learning features independent from the word card UI.
    # It deliberately records only relations supported by authored data; no homophone is inferred.
    relations = {
        "version": 1,
        "families": [
            {"wordId": w["id"], "forms": w.get("family", {})}
            for w in words if w.get("family")
        ],
        "confusableSets": [
            {"id": c["id"], "wordIds": c["wordIds"], "kind": "meaning_or_form", "needs_review": True}
            for c in conf
        ],
        "polysemy": [
            {"wordId": w["id"], "senseCount": len(w.get("senses", [])), "needs_review": bool(w.get("needs_review"))}
            for w in words if len(w.get("senses", [])) > 1
        ],
        "pronunciation": {
            "homophones": [],
            "nearHomophones": [],
            "note": "Empty until a pronunciation editor verifies each pair; do not infer from spelling."
        }
    }
    dump(os.path.join(out_en, "relations.json"), relations)
    dump(os.path.join(out_vi, "vi.json"), vi)
    print(f"topics={len(topics)} words={len(words)} confusables={len(conf)} grammar={len(grammar)} "
          f"questions={len(questions)} passages={len(passages)} petMoods={len(pet)}")


if __name__ == "__main__":
    main()
