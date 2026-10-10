"""Build the sectioned TOEIC bank from the generated question corpus.

This is deliberately a separate generator step: authored P5/P6 data stays in
tools/authoring, while content/en/exams/toeic.json and its Vietnamese overlay
are reproducible generated artifacts.
"""
import hashlib
import json
import os
import sys


ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))


def read_json(*parts):
    with open(os.path.join(ROOT, *parts), encoding="utf-8") as handle:
        return json.load(handle)


def read_authoring_p7():
    path = os.path.join(ROOT, "tools", "authoring", "exam_p7.json")
    if not os.path.isfile(path):
        return []
    value = read_json("tools", "authoring", "exam_p7.json")
    if not isinstance(value, list):
        raise ValueError("exam_p7.json must contain a list")
    return value


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


def add_item(item, ids):
    if item["id"] in ids:
        raise ValueError(f"duplicate TOEIC bank id {item['id']}")
    ids.add(item["id"])
    if len(item["opts"]) != 4 or not 0 <= item["ans"] < 4:
        raise ValueError(f"invalid options or answer for {item['id']}")
    if not item["expl"]:
        raise ValueError(f"missing English explanation for {item['id']}")


def build_p7(groups, existing_ids):
    """Build the original Part 7 preview bank from owner-authored source data."""
    output_groups = []
    locale = {}
    ids = set(existing_ids)
    for group in groups:
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
        })
        locale[group_id] = {"title": group.get("title_vi", ""), "passage": group["passage_vi"]}
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
    update_status({**locale, **p7_locale})
    print(f"toeic_p5={len(bank['items'])} toeic_p6_passages={len(bank['groups'])} toeic_p6_items={sum(len(g['items']) for g in bank['groups'])}")
    print(f"toeic_p7_preview_passages={len(p7['groups'])} toeic_p7_preview_items={sum(len(g['items']) for g in p7['groups'])}")


if __name__ == "__main__":
    try:
        main()
    except (KeyError, TypeError, ValueError) as error:
        print(f"ERROR: {error}", file=sys.stderr)
        raise SystemExit(1)
