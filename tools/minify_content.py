#!/usr/bin/env python3
"""Compact generated runtime JSON without changing the authoring content.

Usage: python tools/minify_content.py <generated assets/content directory>
"""
from __future__ import annotations

import json
import pathlib
import sys

WORD_KEYS = {
    "id", "lemma", "ipa", "level", "topics", "senses", "family", "coll", "conf",
    "tier", "forms", "grammarIds", "lists", "cefr",
}
SENSE_KEYS = {"id", "pos", "def", "register", "ex"}


def dump(path: pathlib.Path, value: object) -> None:
    path.write_text(json.dumps(value, ensure_ascii=False, separators=(",", ":")), encoding="utf-8")


def main(root: pathlib.Path) -> None:
    if not root.is_dir():
        raise SystemExit(f"content directory does not exist: {root}")
    before = after = 0
    for path in sorted(root.rglob("*.json")):
        if path.name == "status.json":
            continue
        before += path.stat().st_size
        value = json.loads(path.read_text(encoding="utf-8"))
        if path.relative_to(root).as_posix() == "en/words.json":
            if "topics" in value:
                dump(root / "en" / "words_meta.json", {k: v for k, v in value.items() if k != "words"})
            value = {
                "words": [
                    {
                        **{k: v for k, v in word.items() if k in WORD_KEYS and k != "senses"},
                        "senses": [
                            {k: v for k, v in sense.items() if k in SENSE_KEYS}
                            for sense in word["senses"]
                        ],
                    }
                    for word in value["words"]
                ]
            }
        dump(path, value)
        after += path.stat().st_size
    print(f"minify_content: {before / 1e6:.1f} MB -> {after / 1e6:.1f} MB")


if __name__ == "__main__":
    if len(sys.argv) != 2 or sys.argv[1] in {"-h", "--help"}:
        print(__doc__.strip())
        raise SystemExit(0 if len(sys.argv) == 2 else 2)
    main(pathlib.Path(sys.argv[1]))
