"""Apply a reviewed locale batch without allowing unknown IDs or silent overwrites.

Usage: python tools/apply_locale_batch.py es path/to/reviewed.jsonl
Each row is {"path":"words.apple.senses.0.g", "value":"manzana", "reviewer":"..."}.
"""
from __future__ import annotations

import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]


def get_path(root, path):
    value = root
    for part in path.split("."):
        if isinstance(value, list):
            value = value[int(part)]
        elif isinstance(value, dict) and part in value:
            value = value[part]
        else:
            raise KeyError(path)
    return value


def set_path(root, path, new_value):
    bits = path.split(".")
    parent = get_path(root, ".".join(bits[:-1])) if len(bits) > 1 else root
    last = bits[-1]
    if isinstance(parent, list):
        parent[int(last)] = new_value
    elif isinstance(parent, dict) and last in parent:
        parent[last] = new_value
    else:
        raise KeyError(path)


def main() -> int:
    if len(sys.argv) != 3 or not re.fullmatch(r"[a-z]{2}(?:-[A-Z]{2})?", sys.argv[1]):
        print("usage: python tools/apply_locale_batch.py <locale> <reviewed.jsonl>", file=sys.stderr)
        return 2
    locale, batch_name = sys.argv[1:]
    pack_path = ROOT / "content/i18n" / f"{locale}.json"
    batch_path = Path(batch_name)
    if not pack_path.exists() or not batch_path.exists():
        print("missing locale pack or batch", file=sys.stderr)
        return 2
    pack = json.loads(pack_path.read_text(encoding="utf-8"))
    rows = [json.loads(line) for line in batch_path.read_text(encoding="utf-8").splitlines() if line.strip()]
    errors = []
    for index, row in enumerate(rows, 1):
        if not row.get("reviewer") or not isinstance(row.get("value"), str) or not row["value"].strip():
            errors.append(f"row {index}: reviewer and non-empty value are required")
            continue
        try:
            get_path(pack, row["path"])
            set_path(pack, row["path"], row["value"])
        except (KeyError, ValueError, IndexError):
            errors.append(f"row {index}: unknown path {row.get('path')!r}")
    if errors:
        print("\n".join(errors), file=sys.stderr)
        return 1
    meta = pack.setdefault("_meta", {})
    meta["status"] = "draft"
    meta["todo"] = True
    meta["lastBatch"] = batch_path.name
    meta["review"] = "native_speaker_required"
    pack_path.write_text(json.dumps(pack, ensure_ascii=False, separators=(",", ":")) + "\n", encoding="utf-8")
    print(json.dumps({"locale": locale, "applied": len(rows), "status": "draft", "todo": True}))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
