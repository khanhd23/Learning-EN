"""Migrate the legacy content/i18n/<locale>.json pack to the folder layout.

The migration is deliberately lossless: every legacy section is written to a
named file, and --round-trip compares the reconstructed legacy object with the
source before any legacy file is removed.
"""

from __future__ import annotations

import argparse
import hashlib
import json
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def canonical(value: object) -> str:
    return json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(",", ":"))


def short_hash(value: object) -> str:
    return hashlib.sha256(canonical(value).encode("utf-8")).hexdigest()[:12]


def dump(path: Path, value: object) -> None:
    path.write_text(json.dumps(value, ensure_ascii=False, separators=(",", ":")) + "\n", encoding="utf-8", newline="\n")


def load(path: Path) -> object:
    return json.loads(path.read_text(encoding="utf-8"))


def status_record(source: object, value: object) -> dict[str, str]:
    return {"s": "approved", "by": "legacy-vi", "src": short_hash(source if source is not None else value)}


def flatten_words(words: dict) -> dict:
    flat = {}
    for word_id, word in words.items():
        for index, sense in enumerate(word.get("senses", []), 1):
            value = dict(sense)
            example_ids = list(value.get("ex", {}).keys())
            sense_id = value.pop("id", None) or (example_ids[0] if example_ids else f"{word_id}_s{index}")
            if word.get("tip"):
                value["tip"] = word["tip"]
            flat[sense_id] = value
    return flat


def unflatten_words(flat: dict) -> dict:
    words = {}
    for sense_id, value in flat.items():
        word_id = sense_id.rsplit("_s", 1)[0]
        sense = dict(value)
        sense.pop("tip", None)
        word = words.setdefault(word_id, {"senses": []})
        if value.get("tip"):
            word["tip"] = value["tip"]
        word["senses"].append(sense)
    return words


def migrate(locale: str, remove_legacy: bool, round_trip: bool) -> dict[str, int]:
    legacy_path = ROOT / "content" / "i18n" / f"{locale}.json"
    out = ROOT / "content" / "i18n" / locale
    if not legacy_path.is_file():
        raise SystemExit(f"legacy pack not found: {legacy_path}")
    legacy = load(legacy_path)
    if not isinstance(legacy, dict):
        raise SystemExit("legacy locale must be a JSON object")

    files = {
        "topics.json": legacy.get("topics", {}),
        "words.json": flatten_words(legacy.get("words", {})),
        "confusables.json": legacy.get("conf", {}),
        "grammar.json": legacy.get("grammar", {}),
        "questions.json": {
            "q": legacy.get("q", {}),
            "q_fix": legacy.get("q_fix", {}),
            "q_translation": legacy.get("q_translation", {}),
            "q_notes": legacy.get("q_notes", {}),
            "passages": legacy.get("passages", {}),
            "quarantine": legacy.get("quarantine", {}),
        },
        "pet.json": legacy.get("pet", {}),
        "tips.json": legacy.get("tips", {}),
        "ui.json": {},
        "l1_notes.json": {},
    }
    out.mkdir(parents=True, exist_ok=True)
    for name, value in files.items():
        dump(out / name, value)

    english = {
        "topics": load(ROOT / "content/en/words.json").get("topics", []),
        "words": load(ROOT / "content/en/words.json").get("words", []),
        "confusables": load(ROOT / "content/en/words.json").get("confusables", []),
        "grammar": load(ROOT / "content/en/grammar.json").get("points", []),
        "questions": load(ROOT / "content/en/questions.json").get("questions", []),
        "passages": load(ROOT / "content/en/questions.json").get("passages", []),
    }
    by_id = {kind: {item.get("id"): item for item in items if item.get("id")} for kind, items in english.items()}
    by_id["senses"] = {sense.get("id"): sense for word in english["words"] for sense in word.get("senses", [])}
    status_entries: dict[str, dict[str, dict[str, str]]] = {}

    def add_status(kind: str, values: dict, source_kind: str | None = None) -> None:
        source_kind = source_kind or kind
        status_entries[kind] = {
            key: status_record(by_id.get(source_kind, {}).get(key), value)
            for key, value in values.items()
        }

    add_status("topics", files["topics.json"])
    add_status("words", files["words.json"], "senses")
    add_status("confusables", files["confusables.json"])
    add_status("grammar", files["grammar.json"])
    q_file = files["questions.json"]
    q_keys = {key for name in ("q", "q_fix", "q_translation", "q_notes") for key in q_file[name]}
    status_entries["questions"] = {key: status_record(by_id["questions"].get(key), q_file["q"].get(key, q_file["q_fix"].get(key, q_file["q_translation"].get(key, q_file["q_notes"].get(key))))) for key in sorted(q_keys)}
    add_status("passages", q_file["passages"])
    add_status("pet", files["pet.json"])
    add_status("tips", files["tips.json"])
    status_entries["ui"] = {}
    status_entries["l1_notes"] = {}
    status = {"schemaVersion": 1, "locale": locale, "status": "complete", "todo": False, "entries": status_entries}
    dump(out / "status.json", status)

    reconstructed = {
        "topics": files["topics.json"], "words": unflatten_words(files["words.json"]), "conf": files["confusables.json"],
        "grammar": files["grammar.json"], "q": q_file["q"], "passages": q_file["passages"],
        "pet": files["pet.json"], "tips": files["tips.json"], "quarantine": q_file["quarantine"],
        "q_fix": q_file["q_fix"], "q_translation": q_file["q_translation"], "q_notes": q_file["q_notes"],
    }
    if round_trip and reconstructed != legacy:
        raise SystemExit("round-trip mismatch: legacy data would be lost")
    if remove_legacy:
        legacy_path.unlink()
    return {name: len(value) for name, value in files.items()}


def main() -> int:
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("locale", nargs="?", default="vi")
    parser.add_argument("--round-trip", action="store_true", help="compare reconstructed data with the legacy pack")
    parser.add_argument("--remove-legacy", action="store_true", help="remove the legacy JSON after a successful migration")
    args = parser.parse_args()
    counts = migrate(args.locale, args.remove_legacy, args.round_trip)
    print(json.dumps(counts, ensure_ascii=False, sort_keys=True))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
