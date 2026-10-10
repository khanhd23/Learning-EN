"""Build non-TOEIC original exam banks from reviewed authoring JSON."""

import hashlib
import json
import os
import random


ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
VSTEP_SECTIONS = {"reading1", "reading2", "reading3", "reading4"}
KET_SECTIONS = {"part1", "part2", "part3", "part4", "part5"}
PET_SECTIONS = {"part1", "part2", "part3", "part4", "part5", "part6"}


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


def build_ket():
    """Build five original A2 Key Reading practice papers.

    Cambridge's official format page is used only for the part counts and task types;
    every passage, option, answer and explanation lives in our authoring file.
    """
    raw_groups = read_json("tools", "authoring", "exam_ket.json")
    groups = []
    locale = {}
    seen = set()
    by_paper = {}
    for raw_group in raw_groups:
        group_id = raw_group.get("id", "")
        section = raw_group.get("section", "")
        paper = str(raw_group.get("paper", ""))
        if not group_id or group_id in seen:
            raise ValueError(f"KET duplicate or empty group id: {group_id!r}")
        if section not in KET_SECTIONS or paper not in {"1", "2", "3", "4", "5"}:
            raise ValueError(f"KET group {group_id} has an invalid section or paper")
        if raw_group.get("source") != "original:exam-expansion-2026":
            raise ValueError(f"KET group {group_id} must be original")
        if not raw_group.get("passage") or not raw_group.get("passage_vi"):
            raise ValueError(f"KET group {group_id} needs English and Vietnamese passages")
        items = raw_group.get("items", [])
        expected = {"part1": 6, "part2": 7, "part3": 5, "part4": 6, "part5": 6}[section]
        if len(items) != expected:
            raise ValueError(f"KET group {group_id} needs {expected} questions")
        seen.add(group_id)
        by_paper.setdefault(paper, set()).add(section)
        output_items = []
        for item_raw in items:
            item_id = item_raw.get("id", "")
            if not item_id or item_id in seen:
                raise ValueError(f"KET duplicate or empty item id: {item_id!r}")
            if not item_raw.get("stem") or not item_raw.get("expl") or not item_raw.get("vi_expl"):
                raise ValueError(f"KET item {item_id} is missing stem or explanation")
            answer_text = item_raw.get("answerText")
            options = item_raw.get("opts", [])
            answer = item_raw.get("ans")
            if answer_text:
                if options or answer is not None:
                    raise ValueError(f"KET open cloze {item_id} must have answerText only")
            else:
                if len(options) != 3 or not isinstance(answer, int) or not 0 <= answer < 3:
                    raise ValueError(f"KET item {item_id} needs three options and a valid answer")
            seen.add(item_id)
            out = {
                "id": item_id,
                "section": section,
                "level": item_raw.get("level", 2),
                "stem": item_raw["stem"],
                "expl": item_raw["expl"],
                "qtype": f"a2_{section}",
            }
            if answer_text:
                out["opts"] = []
                out["ans"] = -1
                out["answerText"] = answer_text
            else:
                order = list(range(3))
                random.Random(item_id).shuffle(order)
                out["opts"] = [options[index] for index in order]
                out["ans"] = order.index(answer)
            output_items.append(out)
            locale[item_id] = {"expl": item_raw["vi_expl"]}
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
    expected = {str(paper): KET_SECTIONS for paper in range(1, 6)}
    if by_paper != expected:
        raise ValueError(f"KET needs five sections in each of five papers, found {by_paper}")
    return {"items": [], "groups": groups}, locale


def build_pet():
    """Build three original B1 Preliminary Reading practice papers."""
    raw_groups = read_json("tools", "authoring", "exam_pet.json")
    groups = []
    locale = {}
    seen = set()
    by_paper = {}
    counts = {"part1": 5, "part2": 5, "part3": 5, "part4": 5, "part5": 6, "part6": 6}
    for raw_group in raw_groups:
        group_id = raw_group.get("id", "")
        section = raw_group.get("section", "")
        paper = str(raw_group.get("paper", ""))
        if not group_id or group_id in seen:
            raise ValueError(f"PET duplicate or empty group id: {group_id!r}")
        if section not in PET_SECTIONS or paper not in {"1", "2", "3"}:
            raise ValueError(f"PET group {group_id} has an invalid section or paper")
        if raw_group.get("source") != "original:exam-expansion-2026":
            raise ValueError(f"PET group {group_id} must be original")
        if not raw_group.get("passage") or not raw_group.get("passage_vi"):
            raise ValueError(f"PET group {group_id} needs English and Vietnamese passages")
        items = raw_group.get("items", [])
        if len(items) != counts[section]:
            raise ValueError(f"PET group {group_id} needs {counts[section]} questions")
        seen.add(group_id)
        by_paper.setdefault(paper, set()).add(section)
        output_items = []
        for item_raw in items:
            item_id = item_raw.get("id", "")
            if not item_id or item_id in seen:
                raise ValueError(f"PET duplicate or empty item id: {item_id!r}")
            if not item_raw.get("stem") or not item_raw.get("expl") or not item_raw.get("vi_expl"):
                raise ValueError(f"PET item {item_id} is missing stem or explanation")
            answer_text = item_raw.get("answerText")
            options = item_raw.get("opts", [])
            answer = item_raw.get("ans")
            if answer_text:
                if options or answer is not None:
                    raise ValueError(f"PET open cloze {item_id} must have answerText only")
            elif len(options) != 3 or not isinstance(answer, int) or not 0 <= answer < 3:
                raise ValueError(f"PET item {item_id} needs three options and a valid answer")
            seen.add(item_id)
            out = {"id": item_id, "section": section, "level": item_raw.get("level", 3),
                   "stem": item_raw["stem"], "expl": item_raw["expl"], "qtype": f"b1_{section}"}
            if answer_text:
                out.update({"opts": [], "ans": -1, "answerText": answer_text})
            else:
                order = list(range(3))
                random.Random(item_id).shuffle(order)
                out.update({"opts": [options[index] for index in order], "ans": order.index(answer)})
            output_items.append(out)
            locale[item_id] = {"expl": item_raw["vi_expl"]}
        groups.append({"id": group_id, "section": section, "title": raw_group.get("title"),
                       "passage": raw_group["passage"], "items": output_items, "set": paper})
        locale[group_id] = {"title": raw_group.get("title_vi", ""), "passage": raw_group["passage_vi"]}
    expected = {str(paper): PET_SECTIONS for paper in range(1, 4)}
    if by_paper != expected:
        raise ValueError(f"PET needs six sections in each of three papers, found {by_paper}")
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
    vstep, vstep_locale = build_vstep()
    ket, ket_locale = build_ket()
    pet, pet_locale = build_pet()
    write_json(os.path.join(ROOT, "content", "en", "exams", "vstep.json"), vstep)
    write_json(os.path.join(ROOT, "content", "i18n", "vi", "exams", "vstep.json"), vstep_locale)
    write_json(os.path.join(ROOT, "content", "en", "exams", "ket.json"), ket)
    write_json(os.path.join(ROOT, "content", "i18n", "vi", "exams", "ket.json"), ket_locale)
    write_json(os.path.join(ROOT, "content", "en", "exams", "pet.json"), pet)
    write_json(os.path.join(ROOT, "content", "i18n", "vi", "exams", "pet.json"), pet_locale)
    update_status(vstep_locale)
    update_status(ket_locale)
    update_status(pet_locale)
    print(f"vstep_passages={len(vstep['groups'])} vstep_items={sum(len(g['items']) for g in vstep['groups'])}")
    print(f"ket_passages={len(ket['groups'])} ket_items={sum(len(g['items']) for g in ket['groups'])}")
    print(f"pet_passages={len(pet['groups'])} pet_items={sum(len(g['items']) for g in pet['groups'])}")


if __name__ == "__main__":
    main()
