"""Report translation completeness for one locale without changing the pack.

Usage: python tools/locale_report.py es

This is deliberately a gate, not an auto-translator. Empty or machine-draft values stay
visible so a native reviewer can work through the pack in batches.
"""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def load(path: Path):
    return json.loads(path.read_text(encoding="utf-8"))


def filled(value) -> bool:
    if isinstance(value, str):
        return bool(value.strip()) and not value.strip().lower().startswith("needs_review")
    if isinstance(value, list):
        return bool(value) and all(filled(v) for v in value)
    if isinstance(value, dict):
        return all(filled(v) for k, v in value.items() if k != "_meta")
    return value is not None


def main() -> int:
    if len(sys.argv) != 2 or not re.fullmatch(r"[a-z]{2}(?:-[A-Z]{2})?", sys.argv[1]):
        print("usage: python tools/locale_report.py <locale>", file=sys.stderr)
        return 2
    locale = sys.argv[1]
    path = ROOT / "content/i18n" / f"{locale}.json"
    if not path.exists():
        print(json.dumps({"locale": locale, "error": "missing_pack"}))
        return 1

    pack = load(path)
    en = load(ROOT / "content/en/words.json")
    total_words = sum(len(w.get("senses", [])) for w in en["words"])
    translated_words = sum(
        1
        for w in pack.get("words", {}).values()
        for sense in w.get("senses", [])
        if filled(sense.get("g"))
    )
    total_questions = len(load(ROOT / "content/en/questions.json")["questions"])
    translated_questions = sum(1 for value in pack.get("q", {}).values() if filled(value))
    total_ui = len(load(ROOT / "content/i18n/vi.json").get("tips", {})) + len(pack.get("pet", {}))
    meta = pack.get("_meta", {})
    result = {
        "locale": locale,
        "status": meta.get("status", "unknown"),
        "todo": bool(meta.get("todo", True)),
        "wordSenseGlosses": {"filled": translated_words, "total": total_words,
                             "percent": round(translated_words * 100 / total_words, 2) if total_words else 0},
        "questionExplanations": {"filled": translated_questions, "total": total_questions,
                                 "percent": round(translated_questions * 100 / total_questions, 2) if total_questions else 0},
        "shipGate": "blocked" if meta.get("status") != "complete" or meta.get("todo", True) else "review_required",
        "next": "Translate and native-review in batches; set _meta.status=complete only after all release checks pass.",
    }
    print(json.dumps(result, ensure_ascii=False, indent=2))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
