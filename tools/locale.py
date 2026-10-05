"""Create, draft, review, approve, check, and report locale packs.

Examples:
    python tools/locale.py new es
    python tools/locale.py draft es --provider none --level 1
    python tools/locale.py export es --level 1 --status draft
    python tools/locale.py import es content/review/es/batch_001.csv --reviewer maria
    python tools/locale.py approve es --level 1 --file words
    python tools/locale.py check --all
    python tools/locale.py report
"""
from __future__ import annotations

import os as _bootstrap_os
import sys as _bootstrap_sys
import sysconfig as _bootstrap_sysconfig

# ``tools`` is placed first on sys.path when another script is launched from
# this directory. Proxy the standard-library module in that import context;
# otherwise this CLI's filename would break gettext, calendar, and argparse.
if __name__ == "locale":
    _stdlib_locale = _bootstrap_os.path.join(_bootstrap_sysconfig.get_path("stdlib"), "locale.py")
    with open(_stdlib_locale, encoding="utf-8") as _file:
        exec(compile(_file.read(), _stdlib_locale, "exec"), globals())

import argparse
import csv
import hashlib
import html
import json
import os
import re
import sys
from pathlib import Path
import xml.etree.ElementTree as ET

# This file is intentionally named locale.py. Remove its directory from the
# import path while stdlib modules load, so it cannot shadow stdlib locale.
if __name__ != "locale" and sys.path and Path(sys.path[0]).resolve() == Path(__file__).resolve().parent:
    sys.path.pop(0)

ROOT = Path(__file__).resolve().parents[1]
LOCALE_RE = re.compile(r"^[a-z]{2}(?:-[A-Z]{2})?$")
KEEP_RE = re.compile(r"(\{[^{}]+\}|%\d+\$[A-Za-z]|%[A-Za-z]|</?[A-Za-z][^>]*>)")


def load(path: Path):
    return json.loads(path.read_text(encoding="utf-8"))


def dump(path: Path, value) -> None:
    path.parent.mkdir(parents=True, exist_ok=True)
    path.write_text(json.dumps(value, ensure_ascii=False, separators=(",", ":")) + "\n", encoding="utf-8", newline="\n")


def canonical(value) -> str:
    return json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(",", ":"))


def source_hash(value) -> str:
    return hashlib.sha256(canonical(value).encode("utf-8")).hexdigest()[:12]


def read_properties(path: Path) -> dict[str, str]:
    values = {}
    if not path.is_file():
        return values
    for line in path.read_text(encoding="utf-8").splitlines():
        line = line.strip()
        if line and not line.startswith("#") and "=" in line:
            key, value = line.split("=", 1)
            values[key.strip()] = value.strip()
    return values


def english(root: Path) -> dict:
    words = load(root / "content/en/words.json")
    grammar = load(root / "content/en/grammar.json")
    questions = load(root / "content/en/questions.json")
    strings = {node.attrib["name"]: "".join(node.itertext()) for node in ET.parse(root / "app/src/main/res/values/strings.xml").getroot().findall("string")}
    return {"words": words, "grammar": grammar, "questions": questions, "ui": strings}


def records(root: Path) -> list[dict]:
    en = english(root)
    result = []
    for word in en["words"].get("words", []):
        for sense in word.get("senses", []):
            result.append({"kind": "words", "key": sense["id"], "source": sense, "lemma": word.get("lemma", ""),
                           "definition": sense.get("def", ""),
                           "example": "\n".join(x.get("text", "") for x in sense.get("ex", [])),
                           "level": word.get("level", 0), "file": "words"})
    for topic in en["words"].get("topics", []):
        result.append({"kind": "topics", "key": topic["id"], "source": topic,
                       "definition": topic["id"], "example": "", "level": 0, "file": "topics"})
    for item in en["words"].get("confusables", []):
        result.append({"kind": "confusables", "key": item["id"], "source": item,
                       "definition": " ".join(item.get("words", [])), "example": "", "level": 0, "file": "confusables"})
    for item in en["grammar"].get("points", []):
        result.append({"kind": "grammar", "key": item["id"], "source": item,
                       "definition": item.get("title", ""),
                       "example": "\n".join(x if isinstance(x, str) else json.dumps(x, ensure_ascii=False) for x in item.get("examples", [])),
                       "level": item.get("level", 0), "file": "grammar"})
    for item in en["questions"].get("questions", []):
        result.append({"kind": "questions", "key": item["id"], "source": item,
                       "definition": item.get("stem", ""),
                       "example": " | ".join(item.get("opts", [])),
                       "level": item.get("level", 0), "file": "questions"})
    for item in en["questions"].get("passages", []):
        result.append({"kind": "passages", "key": item["id"], "source": item,
                       "definition": item.get("title", ""), "example": item.get("text", ""),
                       "level": item.get("level", 0), "file": "questions"})
    for key in sorted(en["ui"]):
        result.append({"kind": "ui", "key": key, "source": en["ui"][key],
                       "definition": en["ui"][key], "example": "", "level": 0, "file": "ui"})
    return result


def status_template(items: list[dict]) -> dict:
    entries = {}
    for item in items:
        entries.setdefault(item["kind"], {})[item["key"]] = {"s": "missing"}
    return {"schemaVersion": 1, "locale": "", "status": "incomplete", "todo": True, "entries": entries}


def empty_files(root: Path, locale: str) -> dict[str, object]:
    en = english(root)
    vi = root / "content/i18n/vi"
    vi_pet = load(vi / "pet.json") if (vi / "pet.json").is_file() else {}
    vi_tips = load(vi / "tips.json") if (vi / "tips.json").is_file() else {}
    words = {}
    for word in en["words"].get("words", []):
        for sense in word.get("senses", []):
            words[sense["id"]] = {"g": "", "ex": {x["id"]: "" for x in sense.get("ex", [])}, "tip": "", "regional": {"es-ES": ""}}
    grammar = {}
    for item in en["grammar"].get("points", []):
        grammar[item["id"]] = {"title": "", "when": "", "body": "", "examples": ["" for _ in item.get("examples", [])], "mistakes": ["" for _ in item.get("mistakes", [])]}
    questions = {"q": {x["id"]: "" for x in en["questions"].get("questions", [])},
                 "q_fix": {x["id"]: "" for x in en["questions"].get("questions", []) if x.get("type") == "E04"},
                 "q_translation": {x["id"]: "" for x in en["questions"].get("questions", []) if x.get("type") == "E05"},
                 "q_notes": {},
                 "passages": {x["id"]: {"blanks": ["" for _ in x.get("blanks", [])]} for x in en["questions"].get("passages", [])},
                 "quarantine": {}}
    return {"topics": {x["id"]: "" for x in en["words"].get("topics", [])}, "words": words,
            "confusables": {x["id"]: {"tip": "", "notes": {w: "" for w in x.get("words", [])}} for x in en["words"].get("confusables", [])},
            "grammar": grammar, "questions": questions, "pet": {k: ["" for _ in v] for k, v in vi_pet.items()},
            "tips": {k: "" for k in vi_tips}, "ui": {k: "" for k in en["ui"]}, "l1_notes": {}}


def locale_dir(root: Path, locale: str) -> Path:
    return root / "content/i18n" / locale


def get_value(pack: dict, item: dict):
    kind, key = item["kind"], item["key"]
    if kind == "questions":
        return pack["questions"].get("q", {}).get(key, "")
    if kind == "passages":
        return pack["questions"].get("passages", {}).get(key, {})
    if kind == "words":
        return pack["words"].get(key, {})
    return pack.get(kind, {}).get(key, "")


def set_value(pack: dict, item: dict, value) -> None:
    kind, key = item["kind"], item["key"]
    if kind == "questions":
        pack["questions"].setdefault("q", {})[key] = value
    elif kind == "passages":
        pack["questions"].setdefault("passages", {})[key] = value
    else:
        pack.setdefault(kind, {})[key] = value


def record_map(root: Path) -> dict[str, dict]:
    return {f"{x['kind']}:{x['key']}": x for x in records(root)}


def protect_keep(text: str, source: dict) -> str:
    source_text = "\n".join(str(v) for v in source.values() if isinstance(v, str))
    for token in KEEP_RE.findall(source_text):
        text = text.replace(token, f"<keep>{token}</keep>")
    return text


VI_CHARS = re.compile(r"[ăâđêôơưạảãấầẩẫậắằẳẵặẹẻẽếềểễệỉĩịọỏõốồổỗộớờởỡợụủũứừửữựỳỷỹỵ]", re.I)


def translate_text(text: str, source: dict, locale: str, provider: str, api_key: str, context: str = "") -> str:
    if not text.strip() or provider == "none":
        return ""
    protected = protect_keep(text, source)
    target = locale.split("-")[0]
    import requests
    if provider == "deepl":
        data = {"text": protected, "target_lang": target.upper()}
        if context:
            data["context"] = context  # DeepL uses it to pick the right sense; it is not translated
        response = requests.post("https://api-free.deepl.com/v2/translate", data=data,
                                 headers={"Authorization": f"DeepL-Auth-Key {api_key}"}, timeout=30)
        response.raise_for_status()
        translated = response.json()["translations"][0]["text"]
    elif provider == "google_cloud":
        response = requests.post("https://translation.googleapis.com/language/translate/v2", params={"key": api_key}, json={"q": [protected], "target": target, "format": "text"}, timeout=30)
        response.raise_for_status()
        translated = response.json()["data"]["translations"][0]["translatedText"]
    else:
        raise ValueError(f"unsupported TRANSLATE_PROVIDER: {provider}")
    return re.sub(r"<keep>(.*?)</keep>", r"\1", translated)


def draft_value(item: dict, locale: str, provider: str, api_key: str):
    if provider == "none":
        return "" if item["kind"] not in ("words", "passages") else ({} if item["kind"] == "words" else {"blanks": []})
    source = item["source"]
    if item["kind"] == "words":
        # The gloss is a short equivalent of the headword in this sense, not a translated definition.
        context = " ".join([source.get("def", "")] + [x.get("text", "") for x in source.get("ex", [])])
        return {"g": translate_text(item.get("lemma", ""), {}, locale, provider, api_key, context=context),
                "ex": {x["id"]: translate_text(x.get("text", ""), {"lemma": item.get("lemma", "")}, locale, provider, api_key)
                       for x in source.get("ex", [])},
                "tip": "", "regional": {"es-ES": ""}}
    if item["kind"] == "grammar":
        # Title, when, body and each mistake's "why" are owner-written English sources
        # (tools/authoring/grammar_en.json). Formulas, signals and the wrong/right sentences are taught
        # English, so they are never translated.
        def tr(text):
            return translate_text(text, {}, locale, provider, api_key) if text else ""
        examples = [x.get("en", "") if isinstance(x, dict) else str(x) for x in source.get("examples", [])]
        return {"title": tr(source.get("title", "")), "when": tr(source.get("when", "")), "body": tr(source.get("body", "")),
                "examples": [tr(x) for x in examples],
                "mistakes": [tr(m.get("why", "")) if isinstance(m, dict) else "" for m in source.get("mistakes", [])]}
    if item["kind"] == "questions":
        # Translate only the English explanation; stems and options are taught English.
        text = source.get("expl", "")
        return translate_text(text, {}, locale, provider, api_key) if text else ""
    if item["kind"] == "passages":
        return {"blanks": [translate_text(b.get("expl", ""), {}, locale, provider, api_key) if b.get("expl") else ""
                           for b in source.get("blanks", [])]}
    if item["kind"] == "topics":
        name = source.get("name", "")
        return translate_text(name, {}, locale, provider, api_key) if name else ""
    if item["kind"] == "ui":
        text = str(source)
        if VI_CHARS.search(text):
            return ""  # the default strings.xml is still Vietnamese until Task 5; never pivot from vi
        return translate_text(text, {"value": text}, locale, provider, api_key)
    return ""


def has_text(value) -> bool:
    if isinstance(value, str):
        return bool(value.strip())
    if isinstance(value, dict):
        return any(has_text(v) for v in value.values())
    if isinstance(value, list):
        return any(has_text(v) for v in value)
    return False


def selected(item: dict, level: int | None, only: set[str] | None) -> bool:
    return (level is None or item["level"] == level) and (not only or item["file"] in only or item["kind"] in only)


def cmd_new(root: Path, locale: str) -> dict:
    target = locale_dir(root, locale)
    if target.exists():
        raise SystemExit(f"locale already exists: {locale}")
    files = empty_files(root, locale)
    for name, value in files.items():
        dump(target / ("questions.json" if name == "questions" else name + ".json"), value)
    status = status_template(records(root)); status["locale"] = locale
    dump(target / "status.json", status)
    return {"locale": locale, "status": "missing", "entries": sum(len(x) for x in status["entries"].values())}


def cmd_draft(root: Path, locale: str, level: int | None, only: set[str] | None, provider: str | None) -> dict:
    target = locale_dir(root, locale)
    if not target.is_dir():
        raise SystemExit(f"locale does not exist: {locale}; run new first")
    props = read_properties(root / "secrets.properties")
    provider = provider or props.get("TRANSLATE_PROVIDER", "none")
    api_key = props.get("TRANSLATE_API_KEY", "")
    if provider != "none" and not api_key:
        raise SystemExit("TRANSLATE_API_KEY is required for a translation provider")
    files = {"words": load(target / "words.json"), "topics": load(target / "topics.json"), "confusables": load(target / "confusables.json"), "grammar": load(target / "grammar.json"), "questions": load(target / "questions.json"), "ui": load(target / "ui.json")}
    status = load(target / "status.json"); changed = 0
    for item in records(root):
        if item["kind"] == "confusables":
            continue  # REWRITE: never machine-draft L1-specific confusable advice.
        if not selected(item, level, only):
            continue
        rec = status.setdefault("entries", {}).setdefault(item["kind"], {}).setdefault(item["key"], {"s": "missing"})
        if rec.get("s") != "missing":
            continue
        # The no-provider workflow must never replace the structured empty
        # scaffold with a different shape or make a network request.
        value = get_value(files, item) if provider == "none" else draft_value(item, locale, provider, api_key)
        if provider != "none" and not has_text(value):
            continue  # nothing could be drafted from English; keep the entry "missing"
        if item["kind"] == "questions": files["questions"].setdefault("q", {})[item["key"]] = value
        elif item["kind"] == "passages": files["questions"].setdefault("passages", {})[item["key"]] = value
        elif item["kind"] in files: files[item["kind"]][item["key"]] = value
        rec["s"] = "draft"; changed += 1
    for name, value in files.items():
        dump(target / ("questions.json" if name == "questions" else name + ".json"), value)
    dump(target / "status.json", status)
    return {"locale": locale, "provider": provider, "drafted": changed}


def cmd_export(root: Path, locale: str, level: int | None, wanted_status: str) -> list[Path]:
    target = locale_dir(root, locale); status = load(target / "status.json"); pack = {"words": load(target / "words.json"), "topics": load(target / "topics.json"), "confusables": load(target / "confusables.json"), "grammar": load(target / "grammar.json"), "questions": load(target / "questions.json"), "ui": load(target / "ui.json")}
    by_key = record_map(root); rows = []
    for key, item in by_key.items():
        rec = status.get("entries", {}).get(item["kind"], {}).get(item["key"], {})
        if rec.get("s") != wanted_status or (level is not None and item["level"] != level): continue
        rows.append({"key": key, "english": item["definition"], "english_example": item["example"], "def": item["definition"], "draft": json.dumps(get_value(pack, item), ensure_ascii=False) if isinstance(get_value(pack, item), (dict, list)) else str(get_value(pack, item)), "corrected": "", "comment": ""})
    out_paths = []
    for n in range(0, len(rows), 300):
        path = root / "content/review" / locale / f"batch_{n // 300 + 1:03d}.csv"; path.parent.mkdir(parents=True, exist_ok=True)
        with path.open("w", encoding="utf-8", newline="") as f:
            writer = csv.DictWriter(f, fieldnames=["key", "english", "english_example", "def", "draft", "corrected", "comment"]); writer.writeheader(); writer.writerows(rows[n:n + 300])
        out_paths.append(path)
    return out_paths


def cmd_import(root: Path, locale: str, csv_path: Path, reviewer: str) -> dict:
    target = locale_dir(root, locale); pack = {"words": load(target / "words.json"), "topics": load(target / "topics.json"), "confusables": load(target / "confusables.json"), "grammar": load(target / "grammar.json"), "questions": load(target / "questions.json"), "ui": load(target / "ui.json")}; status = load(target / "status.json"); by_key = record_map(root); errors = []; applied = 0
    with csv_path.open(encoding="utf-8", newline="") as f:
        for number, row in enumerate(csv.DictReader(f), 2):
            item = by_key.get(row.get("key", "")); corrected = row.get("corrected", "").strip(); draft = row.get("draft", "")
            if not item: errors.append(f"row {number}: unknown key {row.get('key')!r}"); continue
            if not corrected: corrected = draft
            if not corrected.strip(): errors.append(f"row {number}: corrected or unchanged draft is required"); continue
            try: value = json.loads(corrected) if corrected[:1] in "[{\"" else corrected
            except json.JSONDecodeError: errors.append(f"row {number}: corrected value is not valid JSON"); continue
            if item["kind"] == "words" and isinstance(value, str): value = {"g": value, "ex": {}}
            set_value(pack, item, value); status.setdefault("entries", {}).setdefault(item["kind"], {})[item["key"]] = {"s": "reviewed", "by": reviewer, "src": source_hash(item["source"])}; applied += 1
    if errors: raise SystemExit("\n".join(errors))
    for name, value in pack.items(): dump(target / ("questions.json" if name == "questions" else name + ".json"), value)
    dump(target / "status.json", status)
    return {"locale": locale, "reviewer": reviewer, "imported": applied}


def cmd_approve(root: Path, locale: str, level: int | None, file_name: str | None) -> dict:
    target = locale_dir(root, locale); status = load(target / "status.json"); count = 0
    for item in records(root):
        if level is not None and item["level"] != level or file_name and item["file"] != file_name: continue
        rec = status.get("entries", {}).get(item["kind"], {}).get(item["key"])
        if rec and rec.get("s") == "reviewed": rec["s"] = "approved"; count += 1
    dump(target / "status.json", status); return {"locale": locale, "approved": count}


def cmd_check(root: Path, locale: str) -> int:
    sys.path.insert(0, str(root / "tools"))
    import validate_content
    errors, _ = validate_content.locale_rule_errors(str(root))
    errors = [x for x in errors if x.startswith(locale + "/")]
    if errors: print("\n".join(errors), file=sys.stderr); return 1
    print(json.dumps({"locale": locale, "errors": 0})); return 0


def cmd_report(root: Path) -> Path:
    out = root / "dist/locale-status.md"; lines = ["# Locale status", "", "| Locale | Level | File | Missing | Draft | Reviewed | Approved | Stale |", "|---|---:|---|---:|---:|---:|---:|---:|"]
    for directory in sorted((root / "content/i18n").iterdir()):
        if not directory.is_dir() or directory.name.startswith("_") or not (directory / "status.json").is_file(): continue
        status = load(directory / "status.json"); counts = {}
        for item in records(root):
            rec = status.get("entries", {}).get(item["kind"], {}).get(item["key"], {"s": "missing"}); bucket = (item["level"], item["file"]); counts.setdefault(bucket, {x: 0 for x in ("missing", "draft", "reviewed", "approved", "stale")}); state = rec.get("s", "missing");
            if state == "approved" and rec.get("src") != source_hash(item["source"]): state = "stale"
            counts[bucket][state] = counts[bucket].get(state, 0) + 1
        for (level, file_name), values in sorted(counts.items(), key=lambda x: (x[0][0], x[0][1])):
            total = sum(values.values()) or 1
            display = {key: f"{values[key]} ({values[key] * 100 / total:.1f}%)" for key in ("missing", "draft", "reviewed", "approved")}
            lines.append(f"| {directory.name} | {level or '—'} | {file_name} | {display['missing']} | {display['draft']} | {display['reviewed']} | {display['approved']} | {values['stale']} |")
    out.parent.mkdir(parents=True, exist_ok=True); out.write_text("\n".join(lines) + "\n", encoding="utf-8", newline="\n"); return out


def cmd_build_ui(root: Path, locale: str) -> Path:
    """Build an Android UI resource file from a fully approved locale UI pack."""
    target = locale_dir(root, locale)
    ui = load(target / "ui.json")
    status = load(target / "status.json")
    missing = sorted(key for key in ui if status.get("entries", {}).get("ui", {}).get(key, {}).get("s") != "approved")
    if missing:
        raise SystemExit(f"UI is not fully approved for {locale}; first missing/unapproved key: {missing[0]}")
    qualifier = {"es": "es", "pt-BR": "pt-rBR"}.get(locale, locale)
    out = root / "app/src/main/res" / f"values-{qualifier}" / "strings.xml"
    lines = ['<?xml version="1.0" encoding="utf-8"?>', "<resources>"]
    for key in sorted(ui):
        lines.append(f'    <string name="{key}">{html.escape(str(ui[key]), quote=False)}</string>')
    lines += ["</resources>", ""]
    out.parent.mkdir(parents=True, exist_ok=True)
    out.write_text("\n".join(lines), encoding="utf-8", newline="\n")
    return out


def parser() -> argparse.ArgumentParser:
    p = argparse.ArgumentParser(description=__doc__); sub = p.add_subparsers(dest="command", required=True)
    n = sub.add_parser("new"); n.add_argument("locale")
    d = sub.add_parser("draft"); d.add_argument("locale"); d.add_argument("--level", type=int); d.add_argument("--only", help="comma-separated file kinds"); d.add_argument("--provider", choices=("none", "deepl", "google_cloud"))
    e = sub.add_parser("export"); e.add_argument("locale"); e.add_argument("--level", type=int); e.add_argument("--status", default="draft", choices=("draft", "reviewed", "approved", "missing"))
    i = sub.add_parser("import"); i.add_argument("locale"); i.add_argument("csv_file"); i.add_argument("--reviewer", required=True)
    a = sub.add_parser("approve"); a.add_argument("locale"); a.add_argument("--level", type=int); a.add_argument("--file")
    b = sub.add_parser("build-ui"); b.add_argument("locale")
    c = sub.add_parser("check"); c.add_argument("locale", nargs="?"); c.add_argument("--all", action="store_true")
    sub.add_parser("report")
    return p


def main(argv=None, root: Path = ROOT) -> int:
    args = parser().parse_args(argv)
    if args.command not in ("report", "check") and not LOCALE_RE.fullmatch(args.locale): raise SystemExit("invalid locale; use e.g. es or pt-BR")
    if args.command == "new": result = cmd_new(root, args.locale)
    elif args.command == "draft": result = cmd_draft(root, args.locale, args.level, set(args.only.split(",")) if args.only else None, args.provider)
    elif args.command == "export": result = {"files": [str(x) for x in cmd_export(root, args.locale, args.level, args.status)]}
    elif args.command == "import": result = cmd_import(root, args.locale, Path(args.csv_file), args.reviewer)
    elif args.command == "approve": result = cmd_approve(root, args.locale, args.level, args.file)
    elif args.command == "build-ui": result = {"locale": args.locale, "path": str(cmd_build_ui(root, args.locale))}
    elif args.command == "check":
        locales = [args.locale] if args.locale else [x.name for x in (root / "content/i18n").iterdir() if x.is_dir() and not x.name.startswith("_")]
        if not args.all and not args.locale: raise SystemExit("usage: check <locale> or check --all")
        return max(cmd_check(root, x) for x in locales)
    else: print(str(cmd_report(root))); return 0
    print(json.dumps(result, ensure_ascii=False, indent=2)); return 0


if __name__ == "__main__":
    raise SystemExit(main())
