"""Build an offline dependency-license catalog from resolved AAR/JAR artifacts."""
from __future__ import annotations

import argparse
import hashlib
import re
import textwrap
import zipfile
from pathlib import Path


APACHE_GROUPS = ("androidx.", "com.google.guava", "org.jetbrains.kotlin.")
LICENSE_NAMES = ("license", "notice", "third_party_licenses")
SEPARATOR = re.compile(r"\n-{10,}\n")


def _heading(line: str, index: int, lines: list[str]) -> bool:
    """Recognise the component headings used by Google's third_party_licenses.txt."""
    stripped = line.strip()
    if not stripped or line[:1].isspace() or len(stripped) > 120:
        return False
    if index and lines[index - 1] != "":
        return False
    if not stripped.startswith("Files:") and index + 1 < len(lines) and lines[index + 1] != "":
        return False
    if stripped.startswith(('"', "When ")) or re.match(r"^[0-9]", stripped):
        return False
    return stripped.startswith("Files:") or stripped.endswith(":")


def _license_marker(lines: list[str], index: int) -> str | None:
    """Return a stable family label for a well-known license heading."""
    value = lines[index].strip()
    following = lines[index + 1].strip() if index + 1 < len(lines) else ""
    if value == "Apache License" and following.startswith("Version 2.0,"):
        return "Apache-2.0"
    if value in {
        "The MIT License", "MIT License", "MIT License:",
        "GNU General Public License", "GNU LESSER GENERAL PUBLIC LICENSE",
        "MOZILLA PUBLIC LICENSE", "W3C SOFTWARE NOTICE AND LICENSE",
    }:
        return value.rstrip(":")
    if value.startswith((
        "APPLE PUBLIC SOURCE LICENSE", "ICU License -", "Common Public License -",
        "Boost Software License -", "Eclipse Public License,", "Creative Commons CC0",
    )):
        return value
    if value == "This is the Android Software Development Kit License.":
        return "Android SDK License"
    return None


def _chunks(text: str, entry_name: str) -> list[tuple[str, str]]:
    """Split a normal license file or Google's concatenated license catalog."""
    if Path(entry_name).name.casefold() != "third_party_licenses.txt":
        return [(entry_name, text.strip())]

    chunks: list[tuple[str, str]] = []
    for segment in SEPARATOR.split(text):
        lines = segment.strip().splitlines()
        if not lines:
            continue
        headings = [i for i in range(len(lines)) if _heading(lines[i], i, lines)]
        blocks: list[tuple[str, str]] = []
        if headings:
            for position, start in enumerate(headings):
                end = headings[position + 1] if position + 1 < len(headings) else len(lines)
                blocks.append((lines[start].strip().rstrip(":"), "\n".join(lines[start + 1:end]).strip()))
        else:
            blocks.append((entry_name, "\n".join(lines).strip()))

        for title, block in blocks:
            if not block:
                continue
            block_lines = block.splitlines()
            markers = [
                (i, _license_marker(block_lines, i))
                for i in range(len(block_lines))
                if _license_marker(block_lines, i) is not None
            ]
            if not markers:
                chunks.append((title, block))
                continue
            if markers[0][0] > 0:
                notice = "\n".join(block_lines[:markers[0][0]]).strip()
                if notice:
                    chunks.append((f"{title} / notice", notice))
            for position, (start, marker) in enumerate(markers):
                end = markers[position + 1][0] if position + 1 < len(markers) else len(block_lines)
                license_text = "\n".join(block_lines[start:end]).strip()
                if license_text:
                    chunks.append((f"{title} / {marker}", license_text))
    return chunks


def _normalised(text: str) -> str:
    """Hash key only; the first verbatim text is retained for output."""
    text = textwrap.dedent(text).replace("\r\n", "\n").strip()
    return "\n".join(line.rstrip() for line in text.splitlines() if not line.strip().startswith("Files:"))


def _family(label: str, text: str) -> str | None:
    """Group equivalent standard licenses even when upstream adds a different file list."""
    compact = " ".join(_normalised(text).split())
    if "Apache License Version 2.0, January 2004 http://www.apache.org/licenses/" in compact:
        return "Apache-2.0"
    for marker in (
        "GNU LESSER GENERAL PUBLIC LICENSE", "GNU General Public License",
        "APPLE PUBLIC SOURCE LICENSE", "ICU License -", "Common Public License -",
        "Eclipse Public License", "MOZILLA PUBLIC LICENSE", "W3C SOFTWARE NOTICE",
        "Boost Software License", "Creative Commons CC0", "Android Software Development Kit License",
        "The MIT License", "MIT License",
    ):
        if marker in compact or marker in label:
            return marker.rstrip(":")
    return None


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
    grouped: dict[str, dict[str, object]] = {}
    artifact_count = 0
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
            for label, license_text in _chunks(text, entry_name):
                family = _family(label, license_text)
                # Only Apache-2.0 shares one text (identical wording; copyright lives in NOTICE
                # chunks, which are kept). MIT/BSD-style texts carry their own copyright lines,
                # so they merge only when the text is identical.
                key = "family:Apache-2.0" if family == "Apache-2.0" else f"text:{hashlib.sha256(_normalised(license_text).encode('utf-8')).hexdigest()}"
                record = grouped.setdefault(key, {"text": license_text, "labels": set(), "artifacts": set(), "family": family})
                record["labels"].add(label)
                record["artifacts"].add(coordinate)

    if not grouped:
        raise SystemExit("no dependency license text was found")
    chunks: list[str] = []
    for record in sorted(grouped.values(), key=lambda value: str(value["family"] or value["text"])):
        family = record["family"]
        labels = ", ".join(sorted(record["labels"]))
        artifacts_used = ", ".join(sorted(record["artifacts"]))
        if family == "Apache-2.0":
            chunks.append(
                "=== Apache-2.0 (shared full text) ===\n"
                f"Used by: {artifacts_used}\n"
                "Full text: licenses/apache-2.0.txt"
            )
        else:
            chunks.append(
                f"=== {family or 'Dependency license'} ===\n"
                f"Used by: {artifacts_used}\n"
                f"Source labels: {labels}\n\n{record['text']}"
            )
    output.parent.mkdir(parents=True, exist_ok=True)
    output.write_text("\n\n".join(chunks) + "\n", encoding="utf-8")
    return {"artifacts": artifact_count, "license_texts": len(grouped), "bytes": output.stat().st_size}


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
