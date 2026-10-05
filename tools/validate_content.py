"""Validates content/en/*.json + content/i18n/vi/*.json. Non-zero exit on error.

Writes content-validation-report.md and content-coverage-report.md into dist/.
"""
import collections
import json
import os
import re
import sys
import unicodedata
import hashlib
import xml.etree.ElementTree as ET

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
errors, warnings = [], []


def load(*p):
    with open(os.path.join(ROOT, *p), encoding="utf-8") as f:
        return json.load(f)


def nfc_ok(s, where):
    if isinstance(s, str) and unicodedata.normalize("NFC", s) != s:
        errors.append(f"{where}: not NFC")


def trigrams(s):
    s = re.sub(r"\s+", " ", s.lower())
    return {s[i:i + 3] for i in range(max(1, len(s) - 2))}


def canonical(value):
    return json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(",", ":"))


def source_hash(value):
    return hashlib.sha256(canonical(value).encode("utf-8")).hexdigest()[:12]


def walk_strings(value):
    if isinstance(value, str):
        yield value
    elif isinstance(value, dict):
        for child in value.values():
            yield from walk_strings(child)
    elif isinstance(value, list):
        for child in value:
            yield from walk_strings(child)


def tokens(value):
    text = "\n".join(walk_strings(value))
    return re.findall(r"\{[^{}]+\}|%\d+\$[a-zA-Z]|%[a-zA-Z]|</?[A-Za-z][^>]*>", text)


def locale_source_maps(root):
    en_words = load_from(root, "content", "en", "words.json")
    en_grammar = load_from(root, "content", "en", "grammar.json")
    en_questions = load_from(root, "content", "en", "questions.json")
    return {
        "topics": {x["id"]: x for x in en_words.get("topics", [])},
        "words": {s["id"]: s for w in en_words.get("words", []) for s in w.get("senses", [])},
        "confusables": {x["id"]: x for x in en_words.get("confusables", [])},
        "grammar": {x["id"]: x for x in en_grammar.get("points", [])},
        "questions": {x["id"]: x for x in en_questions.get("questions", [])},
        "passages": {x["id"]: x for x in en_questions.get("passages", [])},
    }


def load_from(root, *parts):
    with open(os.path.join(root, *parts), encoding="utf-8") as f:
        return json.load(f)


def strings_xml_keys(root):
    path = os.path.join(root, "app", "src", "main", "res", "values", "strings.xml")
    tree = ET.parse(path)
    return {node.attrib["name"] for node in tree.getroot().findall("string")}


def locale_rule_errors(root=ROOT):
    """Return Task 3 errors and warnings for all folder-layout locales."""
    maps = locale_source_maps(root)
    errors_out, warnings_out = [], []
    locale_root = os.path.join(root, "content", "i18n")
    if not os.path.isdir(locale_root):
        return ["content/i18n: missing locale directory"], []
    forbidden = re.compile(r"(?:vietnam|viet\s+nam|vietnamese|ti[eế]ng\s+vi[eệ]t|vietnamita|vietnamesisch)", re.I)
    file_kinds = {"topics": "topics", "words": "words", "confusables": "confusables", "grammar": "grammar", "passages": "passages"}
    for locale in sorted(os.listdir(locale_root)):
        path = os.path.join(locale_root, locale)
        if not os.path.isdir(path) or locale.startswith("_"):
            continue
        files = {}
        for filename, kind in file_kinds.items():
            file_path = os.path.join(path, filename + ".json")
            if os.path.isfile(file_path):
                files[kind] = load_from(root, "content", "i18n", locale, filename + ".json")
        q_path = os.path.join(path, "questions.json")
        if os.path.isfile(q_path):
            files["questions_file"] = load_from(root, "content", "i18n", locale, "questions.json")
            files["questions"] = {k: v for name in ("q", "q_fix", "q_translation", "q_notes") for k, v in files["questions_file"].get(name, {}).items()}
            files["passages"] = files["questions_file"].get("passages", {})
        ui_path = os.path.join(path, "ui.json")
        if os.path.isfile(ui_path):
            files["ui"] = load_from(root, "content", "i18n", locale, "ui.json")

        for kind, values in files.items():
            if kind in ("questions_file", "ui"):
                continue
            if not isinstance(values, dict):
                continue
            source_kind = "questions" if kind == "questions" else kind
            known = maps.get(source_kind, {})
            for key, value in values.items():
                if key not in known and kind not in ("pet", "tips"):
                    errors_out.append(f"{locale}/{kind}:{key}: unknown or orphan ID")
                    continue
                if key in known:
                    source = known[key]
                    # Passage locale files contain only translated explanations; the
                    # source passage text and options are intentionally not copied.
                    if kind != "passages":
                        source_tokens = sorted(tokens(source))
                        locale_tokens = sorted(tokens(value))
                        if source_tokens != locale_tokens:
                            errors_out.append(f"{locale}/{kind}:{key}: placeholders/markup differ from English source")
                    kept = re.findall(r"<keep>(.*?)</keep>", "\n".join(walk_strings(value)), re.S)
                    source_text = "\n".join(walk_strings(source))
                    for text in kept:
                        if text not in source_text:
                            errors_out.append(f"{locale}/{kind}:{key}: <keep> text is not in English source")
                if locale != "vi" and forbidden.search("\n".join(walk_strings(value))):
                    errors_out.append(f"{locale}/{kind}:{key}: Vietnam/Vietnamese reference")
                raw = "\n".join(walk_strings(value))
                if "<keep>" in raw or "</keep>" in raw:
                    errors_out.append(f"{locale}/{kind}:{key}: leftover <keep> tag")
                if kind == "words" and isinstance(value, dict) and isinstance(value.get("g"), str):
                    fragments = [re.sub(r"\s+", " ", x.strip()).casefold() for x in re.split(r"[,;]", value["g"]) if x.strip()]
                    if len(fragments) != len(set(fragments)):
                        errors_out.append(f"{locale}/words:{key}: duplicate comma/semicolon gloss fragment")
                    definition = source.get("def", "") if isinstance(source, dict) else ""
                    if value["g"].strip().casefold() == str(definition).strip().casefold():
                        warnings_out.append(f"{locale}/words:{key}: TRANSLATE gloss identical to English definition")
                    if definition and (len(value["g"].split()) > max(8, len(definition.split()) * 2.5) or
                                       len(value["g"].split()) < max(1, len(definition.split()) // 5)):
                        warnings_out.append(f"{locale}/words:{key}: gloss length is unusual compared with English definition")

        if "ui" in files:
            expected = strings_xml_keys(root)
            actual = set(files["ui"])
            if actual != expected:
                missing = sorted(expected - actual)
                extra = sorted(actual - expected)
                errors_out.append(f"{locale}/ui: keys differ from strings.xml (missing={missing[:5]}, extra={extra[:5]})")

        status_path = os.path.join(path, "status.json")
        if os.path.isfile(status_path):
            status = load_from(root, "content", "i18n", locale, "status.json")
            for kind, entries in status.get("entries", {}).items():
                for key, record in entries.items():
                    if record.get("s") != "approved":
                        continue
                    source_kind = "questions" if kind == "questions" else kind
                    source = maps.get(source_kind, {}).get(key)
                    if source is not None and record.get("src") != source_hash(source):
                        errors_out.append(f"{locale}/{kind}:{key}: approved entry is stale")
    return errors_out, warnings_out


def main():
    locale_errors, locale_warnings = locale_rule_errors()
    errors.extend(locale_errors)
    warnings.extend(locale_warnings)
    # UTF-8 text decoded as cp1252 ("Ná»™i dung", "EspaÃ±ol", "â€”") must never reach resources or packs.
    mojibake = re.compile("Ã[\u0080-ÿ]|á»|áº|â€|Ä‘|Æ°")
    root_path = __import__("pathlib").Path(ROOT)
    for path in sorted((root_path / "app/src/main/res").glob("values*/*.xml")) + sorted((root_path / "content/i18n").glob("*/*.json")):
        text = path.read_text(encoding="utf-8")
        if mojibake.search(text):
            errors.append(f"{path.relative_to(root_path).as_posix()}: broken encoding (UTF-8 read as cp1252)")
    words = load("content", "en", "words.json")
    grammar = load("content", "en", "grammar.json")
    qs = load("content", "en", "questions.json")
    vi = {
        "topics": load("content", "i18n", "vi", "topics.json"),
        "words": load("content", "i18n", "vi", "words.json"),
        "conf": load("content", "i18n", "vi", "confusables.json"),
        "grammar": load("content", "i18n", "vi", "grammar.json"),
        **load("content", "i18n", "vi", "questions.json"),
        "pet": load("content", "i18n", "vi", "pet.json"),
        "tips": load("content", "i18n", "vi", "tips.json"),
    }

    topic_ids = {t["id"] for t in words["topics"]}
    for t in topic_ids:
        if t not in vi["topics"]:
            errors.append(f"topic {t}: missing vi name")

    ids = set()
    lemmas = set()
    example_ids = set()
    per_topic = collections.Counter()
    for w in words["words"]:
        if w["id"] in ids:
            errors.append(f"word {w['id']}: duplicate id")
        ids.add(w["id"])
        lemma = unicodedata.normalize("NFC", w["lemma"]).strip().casefold()
        if not lemma or lemma in lemmas:
            errors.append(f"word {w['id']}: empty or duplicate lemma")
        lemmas.add(lemma)
        nfc_ok(w["lemma"], w["id"])
        if not w.get("ipa") or not w.get("senses") or not w.get("topics"):
            errors.append(f"word {w['id']}: missing IPA, senses or topics")
        for t in w["topics"]:
            if t not in topic_ids and t != "unsorted":
                errors.append(f"word {w['id']}: unknown topic {t}")
            per_topic[t] += 1
        for s in w["senses"]:
            sv = vi["words"].get(s["id"])
            if not sv:
                errors.append(f"word {w['id']}: missing vi sense {s['id']}")
                continue
            if not sv.get("g"):
                errors.append(f"word {w['id']}: sense without gloss")
            nfc_ok(sv.get("g"), w["id"])
            if not s.get("ex"):
                errors.append(f"word {w['id']}: sense without examples")
            for ex in s["ex"]:
                nfc_ok(ex["text"], w["id"])
                if ex["id"] in example_ids or not ex["text"].strip():
                    errors.append(f"word {w['id']}: duplicate example id or empty example")
                example_ids.add(ex["id"])
                translation = sv.get("ex", {}).get(ex["id"], "")
                if not translation.strip():
                    errors.append(f"word {w['id']}: example {ex['id']} has no translation")
                nfc_ok(translation, ex["id"])
        if not 1 <= w["level"] <= 5:
            errors.append(f"word {w['id']}: level out of range")
    for t, n in per_topic.items():
        if n < 20:
            warnings.append(f"topic {t}: only {n} words (target ≥ 25)")

    by_id = {w["id"]: w for w in words["words"]}
    conf_ids = set()
    for c in words["confusables"]:
        if c["id"] in conf_ids:
            errors.append(f"confusable {c['id']}: duplicate id")
        conf_ids.add(c["id"])
        if len(c.get("wordIds", [])) != len(c["words"]):
            errors.append(f"confusable {c['id']}: word reference count mismatch")
        for lemma, wid in zip(c["words"], c.get("wordIds", [])):
            word = by_id.get(wid)
            if word is None or word["lemma"].casefold() != lemma.casefold():
                errors.append(f"confusable {c['id']}: unresolved or incorrect reference {lemma}")
            elif c["id"] not in word.get("conf", []):
                errors.append(f"word {wid}: missing reverse confusable reference {c['id']}")
        if c["id"] not in vi["conf"]:
            errors.append(f"confusable {c['id']}: missing Vietnamese notes")

    gp_ids = set()
    for g in grammar["points"]:
        gp_ids.add(g["id"])
        if g["id"] not in vi["grammar"]:
            errors.append(f"grammar {g['id']}: missing vi")

    qids = set()
    pos_counter = collections.Counter()
    per_gp = collections.Counter()
    tri = []
    for q in qs["questions"]:
        qid = q["id"]
        if qid in qids:
            errors.append(f"{qid}: duplicate id")
        qids.add(qid)
        if q.get("gp") and q["gp"] not in gp_ids:
            errors.append(f"{qid}: unknown grammar point {q['gp']}")
        if q.get("gp"):
            per_gp[q["gp"]] += 1
        stem = q.get("stem", "")
        nfc_ok(stem, qid)
        if len(stem) > 200:
            errors.append(f"{qid}: stem longer than 200")
        t = q["type"]
        if t in ("E01", "E02", "E12"):
            opts = q["opts"]
            if len(opts) != 4 or len(set(opts)) != 4:
                errors.append(f"{qid}: needs 4 distinct options")
            if not 0 <= q["ans"] < len(opts):
                errors.append(f"{qid}: answer index out of range")
            if any(len(o) > 60 for o in opts) and t != "E12":
                errors.append(f"{qid}: option longer than 60")
            pos_counter[q["ans"]] += 1
            if t == "E02" and stem.count("___") != 1:
                errors.append(f"{qid}: cloze must have exactly one blank")
            if qid not in vi["q"]:
                errors.append(f"{qid}: missing explanation")
        elif t == "E04":
            if len(re.findall(r"\[([^\]]+)\]", stem)) != 4:
                errors.append(f"{qid}: find-error needs 4 marked parts")
            if qid not in vi["q"]:
                errors.append(f"{qid}: missing explanation")
        elif t == "E05":
            if len(stem.split()) > 12:
                warnings.append(f"{qid}: word-order sentence has more than 12 words")
        if qid in vi["q"] and len(vi["q"][qid]) > 300:
            errors.append(f"{qid}: explanation longer than 300")
        if stem:
            tri.append((qid, trigrams(stem + " " + " ".join(q.get("opts", [])))))

    # Near duplicates (trigram Jaccard > 0.85)
    for i in range(len(tri)):
        for j in range(i + 1, len(tri)):
            a, b = tri[i][1], tri[j][1]
            inter = len(a & b)
            if inter and inter / len(a | b) > 0.85:
                warnings.append(f"near-duplicate: {tri[i][0]} ~ {tri[j][0]}")

    total = sum(pos_counter.values())
    if total:
        for p in range(4):
            share = pos_counter[p] / total
            if not 0.20 <= share <= 0.30:
                errors.append(f"answer position {'ABCD'[p]} = {share:.0%} (must be 20-30%)")

    for p in qs["passages"]:
        for i, b in enumerate(p["blanks"]):
            if "{%d}" % (i + 1) not in p["text"]:
                errors.append(f"passage {p['id']}: blank {i + 1} missing in text")
            if len(b["opts"]) != 4:
                errors.append(f"passage {p['id']}: blank {i + 1} needs 4 options")

    for mood, lines in vi.get("pet", {}).items():
        if len(lines) < 8:
            errors.append(f"pet mood {mood}: fewer than 8 lines")
        for l in lines:
            if len(l.replace("{name}", "Miu")) > 60:
                errors.append(f"pet line too long: {l}")

    for g in gp_ids:
        if per_gp[g] < 5:
            warnings.append(f"grammar {g}: only {per_gp[g]} items")

    raw = json.dumps(qs) + json.dumps(words)
    if "ai_draft" in raw:
        errors.append("content contains ai_draft items (release blocked)")

    out = os.path.join(ROOT, "dist")
    os.makedirs(out, exist_ok=True)
    senses = sum(len(w["senses"]) for w in words["words"])
    with open(os.path.join(out, "content-validation-report.md"), "w", encoding="utf-8") as f:
        f.write("# Content validation report\n\n")
        f.write(f"- Words: {len(words['words'])} ({senses} senses) in {len(topic_ids)} topics\n")
        f.write(f"- Confusable sets: {len(words['confusables'])}\n- Grammar points: {len(gp_ids)}\n")
        f.write(f"- Questions: {len(qs['questions'])} · Passages: {len(qs['passages'])}\n")
        f.write(f"- Answer positions: " + ", ".join(f"{'ABCD'[p]} {pos_counter[p]}" for p in range(4)) + "\n")
        review_words = [w for w in words["words"] if w.get("needs_review")]
        f.write(f"- Words explicitly marked needs_review: {len(review_words)} (human editorial review pending)\n")
        f.write("- Other entries have no review certification; missing flags are not evidence of human approval.\n\n")
        f.write(f"## Errors ({len(errors)})\n" + "".join(f"- {e}\n" for e in errors))
        f.write(f"\n## Warnings ({len(warnings)})\n" + "".join(f"- {w}\n" for w in warnings))
        f.write("\n## Word review queue\n" + "".join(f"- {w['id']}: {w.get('source', 'unspecified')}\n" for w in review_words))
    with open(os.path.join(out, "content-coverage-report.md"), "w", encoding="utf-8") as f:
        word_count = len(words['words'])
        f.write("# Content coverage\n\n")
        f.write(f"- Unique words/phrases: {word_count}; senses: {senses}\n")
        f.write(f"- Skill v1 vocabulary target: 2,500–3,500; gap to lower target: {max(0, 2500 - word_count)}\n")
        f.write("- Levels are internal estimates, not CEFR/exam equivalents. Topic membership may overlap.\n")
        f.write(f"- Distinct collocation strings: {len({c for w in words['words'] for c in w.get('coll', [])})}\n")
        f.write(f"- Words with family information: {sum(bool(w.get('family')) for w in words['words'])}\n")
        f.write("\n## Words per level\n")
        levels = collections.Counter(w['level'] for w in words['words'])
        for level in range(1, 6):
            f.write(f"- Level {level}: {levels[level]}\n")
        f.write("\n## Words per topic\n")
        for t in sorted(topic_ids):
            f.write(f"- {vi['topics'][t]}: {per_topic[t]}\n")
        f.write("\n## Items per grammar point\n")
        for g in grammar["points"]:
            f.write(f"- L{g['level']} {vi['grammar'][g['id']]['title']}: {per_gp[g['id']]}\n")
        qt = collections.Counter((q.get("fmt"), q.get("qtype")) for q in qs["questions"])
        f.write("\n## Exam items by format/type\n")
        for (fm, qtp), n in sorted(qt.items(), key=lambda x: str(x)):
            f.write(f"- {fm}/{qtp}: {n}\n")

    print(f"errors={len(errors)} warnings={len(warnings)}")
    for e in errors[:40]:
        print("ERROR", e)
    for w in warnings[:15]:
        print("warn", w)
    sys.exit(1 if errors else 0)


if __name__ == "__main__":
    main()
