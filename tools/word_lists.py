"""Shared loaders and matching rules for vocabulary list metadata."""
from __future__ import annotations

import csv
import re
from pathlib import Path

LIST_FILES = {
    "ngsl": "ngsl.csv",
    "nawl": "nawl.txt",
    "bsl": "bsl.txt",
    "tsl": "tsl.txt",
}
CEFR_LEVELS = ("A1", "A2", "B1", "B2", "C1", "C2")
POS_NAMES = {
    "n": "noun", "noun": "noun",
    "v": "verb", "verb": "verb",
    "adj": "adjective", "adjective": "adjective",
    "adv": "adverb", "adverb": "adverb",
    "prep": "preposition", "preposition": "preposition",
    "pron": "pronoun", "pronoun": "pronoun",
    "conj": "conjunction", "conjunction": "conjunction",
    "det": "determiner", "determiner": "determiner", "article": "determiner",
    "modal": "modal", "aux": "auxiliary", "auxiliary": "auxiliary",
    "interj": "interjection", "interjection": "interjection",
}


def _root() -> Path:
    return Path(__file__).resolve().parents[1]


def normalize_lemma(value: str) -> str:
    return re.sub(r"\s+", " ", value.strip().casefold())


def load_list(name: str, root: Path | None = None) -> set[str]:
    """Load one of the four owner-provided list files as normalized lemmas."""
    if name not in LIST_FILES:
        raise ValueError(f"unknown vocabulary list: {name}")
    path = (root or _root()) / "tools/sources" / LIST_FILES[name]
    if path.suffix == ".csv":
        with path.open(encoding="utf-8-sig", newline="") as stream:
            rows = csv.DictReader(stream)
            lemma_key = next((key for key in rows.fieldnames or () if key.casefold() == "lemma"), None)
            return {normalize_lemma(row[lemma_key]) for row in rows if lemma_key and row.get(lemma_key)}

    values: set[str] = set()
    for raw in path.read_text(encoding="utf-8-sig").splitlines():
        line = raw.strip()
        numbered = re.match(r"^\d+\.\s+(.+?)\s*$", line)
        if numbered:
            line = numbered.group(1)
        # NAWL and BSL are one lemma per line; TSL uses numbered lines. Ignore
        # prose, headings, references, affixes, and corpus statistics.
        if re.fullmatch(r"[A-Za-z][A-Za-z' -]*", line) and " " not in line:
            values.add(normalize_lemma(line))
    return values


def load_all_lists(root: Path | None = None) -> dict[str, set[str]]:
    return {name: load_list(name, root) for name in LIST_FILES}


def _headword_variants(value: str) -> set[str]:
    return {normalize_lemma(part) for part in value.split("/") if normalize_lemma(part)}


def load_cefr_rows(root: Path | None = None) -> dict[str, list[tuple[str, str]]]:
    """Load CEFR-J plus Octanove C1/C2 rows keyed by each headword variant."""
    base = (root or _root()) / "tools/sources"
    result: dict[str, list[tuple[str, str]]] = {}
    for filename in ("cefrj-vocabulary-profile-1.5.csv", "octanove-vocabulary-profile-c1c2-1.0.csv"):
        with (base / filename).open(encoding="utf-8-sig", newline="") as stream:
            for row in csv.DictReader(stream):
                level = row.get("CEFR", "").strip().upper()
                if level not in CEFR_LEVELS:
                    continue
                pos = POS_NAMES.get(row.get("pos", "").strip().casefold(), row.get("pos", "").strip().casefold())
                for variant in _headword_variants(row.get("headword", "")):
                    result.setdefault(variant, []).append((pos, level))
    return result


def match_cefr(lemma: str, pos: str, rows: dict[str, list[tuple[str, str]]]) -> str | None:
    """Apply the Task 15 rule: matching POS first, otherwise the lowest level."""
    candidates = rows.get(normalize_lemma(lemma), [])
    if not candidates:
        return None
    wanted = POS_NAMES.get(pos.casefold(), pos.casefold())
    same_pos = [level for row_pos, level in candidates if row_pos == wanted]
    pool = same_pos or [level for _, level in candidates]
    return min(pool, key=CEFR_LEVELS.index)
