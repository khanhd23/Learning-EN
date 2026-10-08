"""List the words an exam list still needs.

    python tools/word_gaps.py tsl            # TOEIC Service List words missing / not lesson-ready
    python tools/word_gaps.py cefr A1 A2 B1  # CEFR-J (+ Octanove C1/C2) words missing / not silver

Prints two groups, most useful first: MISSING (not in the dictionary: add with
`words_batch.py new`) and BRONZE (in the dictionary but not lesson-ready: upgrade with M/S lines).
Single words only; multi-word CEFR entries and test-only words are skipped.
"""
import csv
import json
import os
import re
import sys

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
SRC = os.path.join(ROOT, "tools", "sources")
SKIP = {"mister", "e-book", "distractor", "unreal", "illogical", "taker", "mini", "headquarter",
        "sightsee", "explanatory", "businessperson"}


def slug(s):
    return re.sub(r"[^a-z0-9]+", "_", s.lower()).strip("_")


def main():
    if len(sys.argv) < 2:
        raise SystemExit(__doc__)
    words = json.load(open(os.path.join(ROOT, "content", "en", "words.json"), encoding="utf-8"))["words"]
    by_lemma = {w["lemma"].lower(): w for w in words}
    ids = {w["id"] for w in words}
    wanted = []  # (rank, word)
    if sys.argv[1] == "tsl":
        for r in csv.DictReader(open(os.path.join(SRC, "tsl.csv"), encoding="latin-1")):
            wanted.append((int(r["TSL Rank"]), r["Word"].strip().lower()))
    elif sys.argv[1] == "cefr":
        levels = set(sys.argv[2:]) or {"A1", "A2", "B1"}
        order = {"A1": 1, "A2": 2, "B1": 3, "B2": 4, "C1": 5, "C2": 6}
        seen = set()
        for name in ("cefrj-vocabulary-profile-1.5.csv", "octanove-vocabulary-profile-c1c2-1.0.csv"):
            for r in csv.DictReader(open(os.path.join(SRC, name), encoding="utf-8")):
                if r["CEFR"] not in levels:
                    continue
                for h in r["headword"].split("/"):
                    h = h.strip().lower()
                    if h and h not in seen and re.fullmatch(r"[a-z][a-z'-]*", h):
                        seen.add(h)
                        wanted.append((order[r["CEFR"]], h))
    else:
        raise SystemExit(__doc__)
    missing, bronze = [], []
    for rank, w in sorted(wanted):
        if w in SKIP:
            continue
        hit = by_lemma.get(w)
        if hit is None and slug(w) in ids:
            continue
        if hit is None:
            missing.append(w)
        elif hit.get("tier") != "silver":
            bronze.append(w)
    print(f"MISSING {len(missing)}")
    print(" ".join(missing))
    print(f"\nBRONZE {len(bronze)}")
    print(" ".join(bronze))


if __name__ == "__main__":
    main()
