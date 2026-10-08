"""Build the read-only content database bundled in assets/content/content.db."""
from __future__ import annotations

import argparse
import hashlib
import json
import sqlite3
import unicodedata
from pathlib import Path

SCHEMA_VERSION = "4"


def load(path: Path):
    return json.loads(path.read_text(encoding="utf-8"))


def canonical(value) -> str:
    return json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(",", ":"))


def source_files(root: Path) -> list[Path]:
    paths = [root / "config/exam_formats.json"]
    word_lists_path = root / "config/word_lists.json"
    if word_lists_path.is_file():
        paths.append(word_lists_path)
    blocked_path = root / "config/blocked_senses.txt"
    if blocked_path.is_file():
        paths.append(blocked_path)
    paths += sorted((root / "content/en").glob("*.json"))
    locale_root = root / "content/i18n"
    for folder in sorted(locale_root.iterdir() if locale_root.is_dir() else []):
        if not folder.is_dir() or folder.name.startswith("_"):
            continue
        status = folder / "status.json"
        if not status.is_file():
            continue
        meta = load(status)
        if meta.get("status") == "complete" and not meta.get("todo") or folder.name == "vi":
            paths += sorted(folder.glob("*.json"))
    return paths


def content_hash(root: Path) -> str:
    digest = hashlib.sha256()
    for path in source_files(root):
        digest.update(path.relative_to(root).as_posix().encode("utf-8"))
        digest.update(b"\0")
        digest.update(path.read_bytes())
        digest.update(b"\0")
    return digest.hexdigest()


def json_text(value) -> str:
    return canonical(value)


def normalize(value: str) -> str:
    value = unicodedata.normalize("NFD", value.casefold())
    return "".join(ch for ch in value if unicodedata.category(ch) != "Mn").replace("đ", "d")


def insert_locale_rows(db, locale: str, folder: Path, status: dict) -> None:
    files = {
        "topics": "topics.json", "words": "words.json", "grammar": "grammar.json",
        "questions": "questions.json", "confusables": "confusables.json",
        "pet": "pet.json", "tips": "tips.json", "ui": "ui.json",
    }
    entries = status.get("entries", {})
    for kind, filename in files.items():
        path = folder / filename
        if not path.is_file():
            continue
        value = load(path)
        if kind == "questions":
            for section, values in value.items():
                if not isinstance(values, dict):
                    continue
                for key, item in values.items():
                    row_kind = f"questions:{section}"
                    row_status = entries.get("questions", {}).get(key, {}).get("s", "missing")
                    db.execute("INSERT OR REPLACE INTO loc VALUES(?,?,?,?,?)",
                               (locale, row_kind, key, json_text(item), row_status))
        elif isinstance(value, dict):
            for key, item in value.items():
                row_status = entries.get(kind, {}).get(key, {}).get("s", "missing")
                db.execute("INSERT OR REPLACE INTO loc VALUES(?,?,?,?,?)",
                           (locale, kind, key, json_text(item), row_status))


def build(root: Path, output: Path, hash_output: Path | None = None) -> str:
    output.parent.mkdir(parents=True, exist_ok=True)
    if output.exists():
        output.unlink()
    words = load(root / "content/en/words.json")
    grammar = load(root / "content/en/grammar.json")
    blocked_path = root / "config/blocked_senses.txt"
    blocked = {
        line.split("#", 1)[0].strip().lower()
        for line in blocked_path.read_text(encoding="utf-8").splitlines()
        if line.split("#", 1)[0].strip()
    } if blocked_path.is_file() else set()
    digest = content_hash(root)
    db = sqlite3.connect(output)
    try:
        db.executescript("""
            CREATE TABLE meta(key TEXT PRIMARY KEY, value TEXT NOT NULL);
            CREATE TABLE word(id TEXT PRIMARY KEY, lemma TEXT NOT NULL, ipa TEXT NOT NULL, lemma_norm TEXT NOT NULL,
                              level INTEGER NOT NULL, tier TEXT NOT NULL, ngsl_rank INTEGER, adult INTEGER NOT NULL,
                              pos_list TEXT NOT NULL, topics_json TEXT NOT NULL, family_json TEXT NOT NULL,
                              forms_json TEXT NOT NULL, collocations_json TEXT NOT NULL, grammar_ids_json TEXT NOT NULL,
                              lists_json TEXT NOT NULL, cefr TEXT);
            CREATE TABLE sense(id TEXT PRIMARY KEY, word_id TEXT NOT NULL, ord INTEGER NOT NULL,
                               pos TEXT NOT NULL, def TEXT NOT NULL, register TEXT NOT NULL);
            CREATE TABLE example(id TEXT PRIMARY KEY, sense_id TEXT NOT NULL, ord INTEGER NOT NULL,
                                 text TEXT NOT NULL, hl TEXT NOT NULL);
            CREATE TABLE word_topic(word_id TEXT NOT NULL, topic_id TEXT NOT NULL, PRIMARY KEY(word_id, topic_id));
            CREATE TABLE topic(id TEXT PRIMARY KEY, domain TEXT NOT NULL, icon TEXT NOT NULL,
                               hue INTEGER NOT NULL, name TEXT NOT NULL);
            CREATE TABLE grammar(id TEXT PRIMARY KEY, level INTEGER NOT NULL, title TEXT NOT NULL);
            CREATE TABLE loc(locale TEXT NOT NULL, kind TEXT NOT NULL, key TEXT NOT NULL,
                             value_json TEXT NOT NULL, status TEXT NOT NULL, PRIMARY KEY(locale, kind, key));
            CREATE INDEX word_level ON word(level);
            CREATE INDEX word_cefr ON word(cefr);
            CREATE INDEX word_topic_topic ON word_topic(topic_id);
            CREATE TABLE word_list(word_id TEXT NOT NULL, list_id TEXT NOT NULL, PRIMARY KEY(word_id, list_id));
            CREATE INDEX word_list_list ON word_list(list_id);
            CREATE INDEX loc_lookup ON loc(locale, kind, key);
            CREATE VIRTUAL TABLE word_fts USING fts4(locale, word_id, lemma, gloss, gloss_raw, body, tokenize=unicode61);
        """)
        db.executemany("INSERT INTO meta VALUES(?,?)", [("schema_version", SCHEMA_VERSION), ("content_hash", digest)])
        for topic in words.get("topics", []):
            db.execute("INSERT INTO topic VALUES(?,?,?,?,?)", (topic["id"], topic.get("domain", ""), topic.get("icon", ""), topic.get("hue", 0), topic.get("name", topic["id"])))
        for point in grammar.get("points", []):
            db.execute("INSERT INTO grammar VALUES(?,?,?)", (point["id"], point.get("level", 1), point.get("title", point["id"])))
        for word in words.get("words", []):
            senses = word.get("senses", [])
            pos_list = word.get("pos") if isinstance(word.get("pos"), list) else [s.get("pos", "") for s in senses]
            adult = int(word["id"].lower() in blocked or word.get("lemma", "").lower() in blocked or any(s.get("id", "").lower() in blocked for s in senses))
            db.execute("INSERT INTO word VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", (
                word["id"], word.get("lemma", ""), word.get("ipa", ""), normalize(word.get("lemma", "")), word.get("level", 1),
                word.get("tier", "bronze"), word.get("ngslRank"), adult, json_text(pos_list), json_text(word.get("topics", [])),
                json_text(word.get("family", {})), json_text(word.get("forms", {})), json_text(word.get("coll", [])),
                json_text(word.get("grammarIds", [])), json_text(word.get("lists", [])), word.get("cefr"),
            ))
            for list_id in word.get("lists", []):
                db.execute("INSERT OR IGNORE INTO word_list VALUES(?,?)", (word["id"], list_id))
            for order, sense in enumerate(senses):
                db.execute("INSERT INTO sense VALUES(?,?,?,?,?,?)", (sense["id"], word["id"], order, sense.get("pos", ""), sense.get("def", ""), sense.get("register", "neutral")))
                for ex_order, example in enumerate(sense.get("ex", [])):
                    db.execute("INSERT INTO example VALUES(?,?,?,?,?)", (example["id"], sense["id"], ex_order, example.get("text", ""), example.get("hl", "")))
                for topic_id in word.get("topics", []):
                    db.execute("INSERT OR IGNORE INTO word_topic VALUES(?,?)", (word["id"], topic_id))
        for locale in ("en",):
            for word in words.get("words", []):
                gloss = " ".join(s.get("def", "") for s in word.get("senses", []))
                body = " ".join([gloss] + [e.get("text", "") for s in word.get("senses", []) for e in s.get("ex", [])] + word.get("coll", []))
                db.execute("INSERT INTO word_fts VALUES(?,?,?,?,?,?)", (locale, word["id"], normalize(word.get("lemma", "")), normalize(gloss), gloss, normalize(body)))
        locale_root = root / "content/i18n"
        for folder in sorted(locale_root.iterdir() if locale_root.is_dir() else []):
            if not folder.is_dir() or folder.name.startswith("_") or not (folder / "status.json").is_file():
                continue
            status = load(folder / "status.json")
            if (status.get("status") == "complete" and not status.get("todo")) or folder.name == "vi":
                insert_locale_rows(db, folder.name, folder, status)
                values = load(folder / "words.json") if (folder / "words.json").is_file() else {}
                approved = status.get("entries", {}).get("words", {})
                for word in words.get("words", []):
                    first = word.get("senses", [{}])[0].get("id")
                    if approved.get(first, {}).get("s") != "approved":
                        continue
                    gloss = "; ".join(g for g in (values.get(s["id"], {}).get("g", "") for s in word.get("senses", [])) if g)
                    db.execute("INSERT INTO word_fts VALUES(?,?,?,?,?,?)", (folder.name, word["id"], normalize(word.get("lemma", "")), normalize(gloss), gloss, normalize(gloss)))
        db.commit()
    finally:
        db.close()
    if hash_output is not None:
        hash_output.parent.mkdir(parents=True, exist_ok=True)
        hash_output.write_text(digest + "\n", encoding="ascii")
    return digest


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--root", type=Path, default=Path(__file__).resolve().parents[1])
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("--hash-output", type=Path)
    args = parser.parse_args()
    print(json.dumps({"output": str(args.output), "content_hash": build(args.root.resolve(), args.output.resolve(), args.hash_output.resolve() if args.hash_output else None)}))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
