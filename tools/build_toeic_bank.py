"""Build the sectioned TOEIC bank from the generated question corpus.

This is deliberately a separate generator step: authored P5/P6 data stays in
tools/authoring, while content/en/exams/toeic.json and its Vietnamese overlay
are reproducible generated artifacts.
"""
import hashlib
import json
import os
import re
import sys


ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


def read_json(*parts):
    with open(os.path.join(ROOT, *parts), encoding="utf-8") as handle:
        return json.load(handle)


def read_authoring_p7():
    folder = os.path.join(ROOT, "tools", "authoring")
    names = sorted(
        name for name in os.listdir(folder)
        if re.fullmatch(r"exam_p7(?:_batch\d+)?\.json", name)
    )
    groups = []
    for name in names:
        value = read_json("tools", "authoring", name)
        if not isinstance(value, list):
            raise ValueError(f"{name} must contain a list")
        groups.extend(value)
    return groups


def read_authoring_p1():
    folder = os.path.join(ROOT, "tools", "authoring")
    names = sorted(
        name for name in os.listdir(folder)
        if re.fullmatch(r"exam_p1(?:_batch\d+)?\.json", name)
    )
    groups = []
    for name in names:
        value = read_json("tools", "authoring", name)
        if not isinstance(value, list):
            raise ValueError(f"{name} must contain a list")
        groups.extend(value)
    return groups


def read_authoring_p2():
    folder = os.path.join(ROOT, "tools", "authoring")
    names = sorted(
        name for name in os.listdir(folder)
        if re.fullmatch(r"exam_p2(?:_batch\d+)?\.json", name)
    )
    groups = []
    for name in names:
        value = read_json("tools", "authoring", name)
        if not isinstance(value, list):
            raise ValueError(f"{name} must contain a list")
        groups.extend(value)
    return groups


def write_json(path, value):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as handle:
        json.dump(value, handle, ensure_ascii=False, indent=2)
        handle.write("\n")


def build_bank(questions, locale_questions):
    p5 = [q for q in questions.get("questions", []) if q.get("fmt") == "toeic_p5"]
    passages = questions.get("passages", [])
    if len(p5) < 300:
        raise ValueError(f"TOEIC bank needs at least 300 Part 5 items, found {len(p5)}")
    if len(passages) != 40:
        raise ValueError(f"TOEIC bank needs exactly 40 Part 6 passages, found {len(passages)}")

    vi_q = locale_questions.get("q", {})
    vi_passages = locale_questions.get("passages", {})
    items = []
    locale = {}
    ids = set()
    for q in p5:
        item = {
            "id": q["id"], "section": "p5", "level": q.get("level", 3),
            "stem": q["stem"], "opts": q["opts"], "ans": q["ans"],
            "expl": q.get("expl", ""), "qtype": q.get("qtype"),
        }
        add_item(item, ids)
        items.append(item)
        explanation = vi_q.get(q["id"])
        if not explanation:
            raise ValueError(f"missing Vietnamese explanation for {q['id']}")
        locale[q["id"]] = {"expl": explanation}

    groups = []
    for passage in passages:
        p_id = passage["id"]
        vi_blanks = vi_passages.get(p_id, {}).get("blanks", [])
        if len(passage.get("blanks", [])) != 4 or len(vi_blanks) != 4:
            raise ValueError(f"Part 6 passage {p_id} must have four English and Vietnamese blanks")
        group_items = []
        for index, blank in enumerate(passage["blanks"]):
            item = {
                "id": f"{p_id}_{index + 1}", "section": "p6",
                "level": passage.get("level", 3), "stem": f"Choose the best answer for blank {index + 1}.",
                "opts": blank["opts"], "ans": blank["ans"],
                "expl": blank.get("expl", ""), "qtype": blank.get("qtype"),
            }
            add_item(item, ids)
            group_items.append(item)
            if not vi_blanks[index]:
                raise ValueError(f"missing Vietnamese explanation for {p_id} blank {index + 1}")
            locale[item["id"]] = {"expl": vi_blanks[index]}
        groups.append({
            "id": p_id, "section": "p6", "title": passage.get("title"),
            "passage": passage["text"], "items": group_items,
        })
    return {"items": items, "groups": groups}, locale


def add_item(item, ids, options_count=4):
    if item["id"] in ids:
        raise ValueError(f"duplicate TOEIC bank id {item['id']}")
    ids.add(item["id"])
    if len(item["opts"]) != options_count or not 0 <= item["ans"] < options_count:
        raise ValueError(f"invalid options or answer for {item['id']}")
    if not item["expl"]:
        raise ValueError(f"missing English explanation for {item['id']}")


def build_p7(groups, existing_ids):
    """Build the original Part 7 bank and stable, non-overlapping practice sets."""
    output_groups = []
    locale = {}
    ids = set(existing_ids)
    for group_index, group in enumerate(groups):
        group_id = group.get("id", "")
        if not group_id or group_id in ids:
            raise ValueError(f"duplicate or empty Part 7 group id: {group_id!r}")
        if group.get("section") != "p7" or not group.get("source", "").startswith("original:"):
            raise ValueError(f"Part 7 group {group_id} must be original and use section p7")
        if not group.get("passage") or not group.get("passage_vi"):
            raise ValueError(f"Part 7 group {group_id} needs English and Vietnamese passages")
        ids.add(group_id)
        group_items = []
        for raw in group.get("items", []):
            item = {
                "id": raw["id"], "section": "p7", "level": raw.get("level", group.get("level", 3)),
                "stem": raw["stem"], "opts": raw["opts"], "ans": raw["ans"],
                "expl": raw.get("expl", ""), "qtype": f"p7_{group.get('kind', 'single')}",
            }
            add_item(item, ids)
            group_items.append(item)
            if not raw.get("vi_expl"):
                raise ValueError(f"missing Vietnamese explanation for {raw['id']}")
            locale[item["id"]] = {"expl": raw["vi_expl"]}
        if not group_items:
            raise ValueError(f"Part 7 group {group_id} is empty")
        output_groups.append({
            "id": group_id, "section": "p7", "title": group.get("title"),
            "passage": group["passage"], "items": group_items,
            # Three fixed sets of 18 three-question groups make three complete
            # 54-question practices without sharing an item between sets.
            "set": group.get("set") or str(group_index // 18 + 1),
        })
        locale[group_id] = {"title": group.get("title_vi", ""), "passage": group["passage_vi"]}
    return {"items": [], "groups": output_groups}, locale


def build_p1(groups):
    """Build original, image-free Part 1-style listening practice sets."""
    output_groups = []
    locale = {}
    ids = set()
    by_set = {}
    for group in groups:
        group_id = group.get("id", "")
        if not group_id or group_id in ids:
            raise ValueError(f"duplicate or empty Part 1 group id: {group_id!r}")
        if group.get("section") != "p1" or not group.get("source", "").startswith("original:"):
            raise ValueError(f"Part 1 group {group_id} must be original and use section p1")
        if not group.get("passage") or not group.get("passage_vi"):
            raise ValueError(f"Part 1 group {group_id} needs English and Vietnamese scene prompts")
        set_id = str(group.get("set") or "")
        if set_id not in {"1", "2", "3", "4", "5"}:
            raise ValueError(f"Part 1 group {group_id} must use set 1-5")
        ids.add(group_id)
        by_set[set_id] = by_set.get(set_id, 0) + 1
        raw_items = group.get("items", [])
        if len(raw_items) != 1:
            raise ValueError(f"Part 1 group {group_id} must contain exactly one item")
        raw = raw_items[0]
        item = {
            "id": raw["id"], "section": "p1", "level": raw.get("level", group.get("level", 3)),
            "stem": raw["stem"], "opts": raw["opts"], "ans": raw["ans"],
            "expl": raw.get("expl", ""), "qtype": "listen_p1",
        }
        add_item(item, ids)
        if not raw.get("vi_expl"):
            raise ValueError(f"missing Vietnamese explanation for {raw['id']}")
        locale[raw["id"]] = {"expl": raw["vi_expl"]}
        output_groups.append({
            "id": group_id, "section": "p1", "title": group.get("title"),
            "passage": group["passage"], "items": [item], "set": set_id,
        })
        locale[group_id] = {"title": group.get("title_vi", ""), "passage": group["passage_vi"]}
    if set(by_set) != {"1", "2", "3", "4", "5"} or any(count != 6 for count in by_set.values()):
        raise ValueError(f"Part 1 needs five sets of six groups, found {by_set}")
    return {"items": [], "groups": output_groups}, locale


def build_p2(groups):
    """Build original, image-free Part 2-style question-response sets."""
    output_groups = []
    locale = {}
    ids = set()
    by_set = {}
    for group in groups:
        group_id = group.get("id", "")
        if not group_id or group_id in ids:
            raise ValueError(f"duplicate or empty Part 2 group id: {group_id!r}")
        if group.get("section") != "p2" or not group.get("source", "").startswith("original:"):
            raise ValueError(f"Part 2 group {group_id} must be original and use section p2")
        if not group.get("passage") or not group.get("passage_vi"):
            raise ValueError(f"Part 2 group {group_id} needs English and Vietnamese questions")
        set_id = str(group.get("set") or "")
        if set_id not in {"1", "2", "3", "4", "5"}:
            raise ValueError(f"Part 2 group {group_id} must use set 1-5")
        ids.add(group_id)
        by_set[set_id] = by_set.get(set_id, 0) + 1
        raw_items = group.get("items", [])
        if len(raw_items) != 1:
            raise ValueError(f"Part 2 group {group_id} must contain exactly one item")
        raw = raw_items[0]
        item = {
            "id": raw["id"], "section": "p2", "level": raw.get("level", group.get("level", 3)),
            "stem": raw["stem"], "opts": raw["opts"], "ans": raw["ans"],
            "expl": raw.get("expl", ""), "qtype": "listen_p2",
        }
        add_item(item, ids, options_count=3)
        if not raw.get("vi_expl"):
            raise ValueError(f"missing Vietnamese explanation for {raw['id']}")
        locale[raw["id"]] = {"expl": raw["vi_expl"]}
        output_groups.append({
            "id": group_id, "section": "p2", "title": group.get("title"),
            "passage": group["passage"], "items": [item], "set": set_id,
        })
        locale[group_id] = {"title": group.get("title_vi", ""), "passage": group["passage_vi"]}
    if set(by_set) != {"1", "2", "3", "4", "5"} or any(count != 6 for count in by_set.values()):
        raise ValueError(f"Part 2 needs five sets of six groups, found {by_set}")
    return {"items": [], "groups": output_groups}, locale


def update_status(locale):
    path = os.path.join(ROOT, "content", "i18n", "vi", "status.json")
    status = read_json("content", "i18n", "vi", "status.json")
    entries = status.setdefault("entries", {})
    entries["exams"] = {
        key: {
            "s": "approved", "by": "owner",
            "src": hashlib.sha256(json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(",", ":")).encode("utf-8")).hexdigest()[:12],
        }
        for key, value in locale.items()
    }
    # Keep the same compact generated form used by gen_content.py; status is
    # large, so pretty-printing it would create needless repository churn.
    with open(path, "w", encoding="utf-8") as handle:
        json.dump(status, handle, ensure_ascii=False, separators=(",", ":"))


def main():
    bank, locale = build_bank(
        read_json("content", "en", "questions.json"),
        read_json("content", "i18n", "vi", "questions.json"),
    )
    write_json(os.path.join(ROOT, "content", "en", "exams", "toeic.json"), bank)
    write_json(os.path.join(ROOT, "content", "i18n", "vi", "exams", "toeic.json"), locale)

    p7, p7_locale = build_p7(read_authoring_p7(), set())
    write_json(os.path.join(ROOT, "content", "en", "exams", "toeic_p7.json"), p7)
    write_json(os.path.join(ROOT, "content", "i18n", "vi", "exams", "toeic_p7.json"), p7_locale)
    p1, p1_locale = build_p1(read_authoring_p1())
    write_json(os.path.join(ROOT, "content", "en", "exams", "toeic_p1.json"), p1)
    write_json(os.path.join(ROOT, "content", "i18n", "vi", "exams", "toeic_p1.json"), p1_locale)
    p2, p2_locale = build_p2(read_authoring_p2())
    write_json(os.path.join(ROOT, "content", "en", "exams", "toeic_p2.json"), p2)
    write_json(os.path.join(ROOT, "content", "i18n", "vi", "exams", "toeic_p2.json"), p2_locale)
    update_status({**locale, **p7_locale, **p1_locale, **p2_locale})
    print(f"toeic_p5={len(bank['items'])} toeic_p6_passages={len(bank['groups'])} toeic_p6_items={sum(len(g['items']) for g in bank['groups'])}")
    print(f"toeic_p7_passages={len(p7['groups'])} toeic_p7_items={sum(len(g['items']) for g in p7['groups'])}")
    print(f"toeic_p1_passages={len(p1['groups'])} toeic_p1_items={sum(len(g['items']) for g in p1['groups'])}")
    print(f"toeic_p2_passages={len(p2['groups'])} toeic_p2_items={sum(len(g['items']) for g in p2['groups'])}")


if __name__ == "__main__":
    try:
        main()
    except (KeyError, TypeError, ValueError) as error:
        print(f"ERROR: {error}", file=sys.stderr)
        raise SystemExit(1)
