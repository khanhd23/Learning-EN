"""Build non-TOEIC original exam banks from reviewed authoring JSON."""

import hashlib
import json
import os
import random


ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
VSTEP_SECTIONS = {"reading1", "reading2", "reading3", "reading4"}


def read_json(*parts):
    with open(os.path.join(ROOT, *parts), encoding="utf-8") as handle:
        return json.load(handle)


def write_json(path, value):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8") as handle:
        json.dump(value, handle, ensure_ascii=False, indent=2)
        handle.write("\n")


def build_vstep():
    raw_groups = read_json("tools", "authoring", "exam_vstep.json")
    groups = []
    locale = {}
    seen = set()
    by_paper = {}
    for raw_group in raw_groups:
        group_id = raw_group.get("id", "")
        section = raw_group.get("section", "")
        paper = str(raw_group.get("paper", ""))
        if not group_id or group_id in seen:
            raise ValueError(f"VSTEP duplicate or empty group id: {group_id!r}")
        if section not in VSTEP_SECTIONS or paper not in {"1", "2", "3"}:
            raise ValueError(f"VSTEP group {group_id} has an invalid section or paper")
        if raw_group.get("source") != "original:exam-expansion-2026":
            raise ValueError(f"VSTEP group {group_id} must be original")
        if not raw_group.get("passage") or not raw_group.get("passage_vi"):
            raise ValueError(f"VSTEP group {group_id} needs English and Vietnamese passages")
        items = raw_group.get("items", [])
        if len(items) != 10:
            raise ValueError(f"VSTEP group {group_id} needs exactly 10 questions")
        seen.add(group_id)
        by_paper.setdefault(paper, set()).add(section)
        output_items = []
        for raw in items:
            item_id = raw.get("id", "")
            if not item_id or item_id in seen:
                raise ValueError(f"VSTEP duplicate or empty item id: {item_id!r}")
            options = raw.get("opts", [])
            answer = raw.get("ans")
            if len(options) != 4 or not isinstance(answer, int) or not 0 <= answer < 4:
                raise ValueError(f"VSTEP item {item_id} needs four options and a valid answer")
            if not raw.get("stem") or not raw.get("expl") or not raw.get("vi_expl"):
                raise ValueError(f"VSTEP item {item_id} is missing stem or explanation")
            seen.add(item_id)
            order = list(range(4))
            random.Random(item_id).shuffle(order)
            output_items.append({
                "id": item_id,
                "section": section,
                "level": raw.get("level", 3),
                "stem": raw["stem"],
                "opts": [options[index] for index in order],
                "ans": order.index(answer),
                "expl": raw["expl"],
                "qtype": "vstep_reading",
            })
            locale[item_id] = {"expl": raw["vi_expl"]}
        groups.append({
            "id": group_id,
            "section": section,
            "title": raw_group.get("title"),
            "passage": raw_group["passage"],
            "items": output_items,
            "set": paper,
        })
        locale[group_id] = {
            "title": raw_group.get("title_vi", ""),
            "passage": raw_group["passage_vi"],
        }
    expected = {str(paper): VSTEP_SECTIONS for paper in range(1, 4)}
    if by_paper != expected:
        raise ValueError(f"VSTEP needs four sections in each of three papers, found {by_paper}")
    return {"items": [], "groups": groups}, locale


def update_status(locale):
    path = os.path.join(ROOT, "content", "i18n", "vi", "status.json")
    status = read_json("content", "i18n", "vi", "status.json")
    entries = status.setdefault("entries", {})
    exams = entries.setdefault("exams", {})
    exams.update({
        key: {
            "s": "approved", "by": "owner",
            "src": hashlib.sha256(json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(",", ":")).encode("utf-8")).hexdigest()[:12],
        }
        for key, value in locale.items()
    })
    with open(path, "w", encoding="utf-8") as handle:
        json.dump(status, handle, ensure_ascii=False, separators=(",", ":"))


def main():
    bank, locale = build_vstep()
    write_json(os.path.join(ROOT, "content", "en", "exams", "vstep.json"), bank)
    write_json(os.path.join(ROOT, "content", "i18n", "vi", "exams", "vstep.json"), locale)
    update_status(locale)
    print(f"vstep_passages={len(bank['groups'])} vstep_items={sum(len(g['items']) for g in bank['groups'])}")


if __name__ == "__main__":
    main()
