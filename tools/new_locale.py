"""Scaffold a complete localized content layer without inventing translations.

Usage: python tools/new_locale.py <locale>
The generated file contains every current content ID, empty translation values and a `todo`
manifest. It is intentionally not copied into the APK until a native reviewer completes it.
"""
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def empty_pack(words, grammar, questions, vi, locale):
    out = {
        "_meta": {
            "locale": locale,
            "status": "scaffold",
            "todo": True,
            "review": "native_speaker_required",
            "sourceLocale": "vi",
            "note": "Do not ship until UI is translated, content is reviewed and completeness checks pass.",
        },
        "topics": {t["id"]: "" for t in words["topics"]},
        "words": {},
        "conf": {},
        "grammar": {},
        "q": {},
        "passages": {},
        "pet": {m: ["" for _ in lines] for m, lines in vi.get("pet", {}).items()},
        "tips": {k: "" for k in vi.get("tips", {})},
    }
    for word in words["words"]:
        senses = []
        for sense in word["senses"]:
            senses.append({"g": "", "ex": {e["id"]: "" for e in sense["ex"]}})
        out["words"][word["id"]] = {"senses": senses, "tip": ""}
    for c in words["confusables"]:
        out["conf"][c["id"]] = {"tip": "", "notes": {w: "" for w in c["words"]}}
    for g in grammar["points"]:
        out["grammar"][g["id"]] = {
            "title": "", "when": "", "body": "",
            "examples": ["" for _ in g["examples"]],
            "mistakes": ["" for _ in g["mistakes"]],
        }
    for q in questions["questions"]:
        out["q"][q["id"]] = ""
    for p in questions["passages"]:
        out["passages"][p["id"]] = {"blanks": ["" for _ in p["blanks"]]}
    return out


def main():
    if len(sys.argv) != 2 or not re.fullmatch(r"[a-z]{2}(?:-[A-Z]{2})?", sys.argv[1]):
        raise SystemExit("usage: python tools/new_locale.py <locale>, e.g. es or pt-BR")
    locale = sys.argv[1]
    out_path = ROOT / "content/i18n" / f"{locale}.json"
    if out_path.exists():
        raise SystemExit(f"already exists: {out_path}")
    load = lambda p: json.loads((ROOT / p).read_text(encoding="utf-8"))
    pack = empty_pack(load("content/en/words.json"), load("content/en/grammar.json"), load("content/en/questions.json"), load("content/i18n/vi.json"), locale)
    out_path.write_text(json.dumps(pack, ensure_ascii=False, separators=(",", ":")) + "\n", encoding="utf-8")
    print(json.dumps({"locale": locale, "path": str(out_path), "status": "scaffold", "todo": True}))


if __name__ == "__main__":
    main()
