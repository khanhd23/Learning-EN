"""Build an offline dependency-license catalog from resolved AAR/JAR artifacts."""
from __future__ import annotations

import argparse
import hashlib
import zipfile
from pathlib import Path


APACHE_GROUPS = ("androidx.", "com.google.guava", "org.jetbrains.kotlin.")
LICENSE_NAMES = ("license", "notice", "third_party_licenses")


def archive_entries(path: Path) -> list[tuple[str, str]]:
    if not zipfile.is_zipfile(path):
        return []
    found = []
    with zipfile.ZipFile(path) as archive:
        for name in sorted(archive.namelist()):
            lowered = name.casefold()
            if not lowered.endswith((".txt", ".md")):
                continue
            basename = Path(name).name.casefold()
            if any(token in basename for token in LICENSE_NAMES):
                text = archive.read(name).decode("utf-8", errors="strict").strip()
                if text:
                    found.append((name, text))
    return found


def build(root: Path, output: Path, artifacts: list[str]) -> dict[str, int]:
    apache = (root / "app/src/main/assets/licenses/apache-2.0.txt").read_text(encoding="utf-8").strip()
    chunks: list[str] = []
    seen: set[str] = set()
    artifact_count = 0
    entry_count = 0
    for item in sorted(artifacts):
        coordinate, raw_path = item.split("=", 1)
        path = Path(raw_path)
        if not path.is_file():
            continue
        artifact_count += 1
        entries = archive_entries(path)
        if not entries and any(coordinate.startswith(group) for group in APACHE_GROUPS):
            entries = [("Apache License 2.0 (shared full text)", apache)]
        for entry_name, text in entries:
            digest = hashlib.sha256(text.encode("utf-8")).hexdigest()
            if digest in seen:
                continue
            seen.add(digest)
            entry_count += 1
            chunks.append(f"=== {coordinate} - {entry_name} ===\n\n{text}")

    if not chunks:
        raise SystemExit("no dependency license text was found")
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text("\n\n".join(chunks) + "\n", encoding="utf-8")
    return {"artifacts": artifact_count, "license_texts": entry_count, "bytes": output.stat().st_size}


def main() -> int:
    parser = argparse.ArgumentParser()
    parser.add_argument("--root", type=Path, required=True)
    parser.add_argument("--output", type=Path, required=True)
    parser.add_argument("artifacts", nargs="*")
    args = parser.parse_args()
    print(build(args.root.resolve(), args.output.resolve(), args.artifacts))
    return 0


if __name__ == "__main__":
    raise SystemExit(main())
