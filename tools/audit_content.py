"""Audit the English core and a learner-language gloss pack against docs/content/CONTENT_STANDARD.md.

Usage: python tools/audit_content.py [--locale vi] [--strict]

Writes dist/content-audit.md (summary) and dist/content-audit-issues.jsonl (one row per entry with
its errors/warnings and the tier it qualifies for). With --strict, exits non-zero when any error remains.
"""
from __future__ import annotations

import argparse
import collections
import csv
import json
import re
import sys
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

IPA_OK = set("abdefhijklmnoprstuvwzæɑɒɔəɚɝɛɜɪʊʌθðʃʒŋɡgɹɾʔɫːˈˌ ")
VI_LETTERS = set("ạảãắằẳẵặấầẩẫậđẹẻẽếềểễệỉĩịọỏốồổỗộớờởỡợụủũứừửữựỳỷỹỵơưăâêô")
SENTENCE = re.compile(r"""^["'(]?[A-Z0-9]""")
WORD = re.compile(r"[A-Za-z']+")
CATCH_ALL_SIZE = 300
# Template text that only says what part of speech a word is. It is not a definition.
PLACEHOLDER_DEFS = {
    "a word or phrase used in english", "a person, place, thing, or idea", "describing a quality or state",
    "a word showing a relation or place", "in a particular way or manner", "a word that joins words or ideas",
    "to do or make something", "how something is done or how it happens",
}


def load(rel):
    with open(ROOT / rel, encoding="utf-8") as f:
        return json.load(f)


def ngsl_ranks():
    with open(ROOT / "tools/sources/ngsl.csv", encoding="utf-8") as f:
        return {row["Lemma"].lower(): i + 1 for i, row in enumerate(csv.DictReader(f))}


def band_level(rank):
    if rank is None:
        return None
    return 1 if rank <= 1000 else 2 if rank <= 2000 else 3


def wordnet_texts():
    """Every definition string in the WordNet source map, lowercased."""
    path = ROOT / "tools/sources/wordnet_defs.json"
    out = set()
    if not path.exists():
        return out
    stack = [load(path.relative_to(ROOT))]
    while stack:
        v = stack.pop()
        if isinstance(v, str):
            out.add(v.strip().lower().rstrip("."))
        elif isinstance(v, list):
            stack.extend(v)
        elif isinstance(v, dict):
            stack.extend(v.values())
    return out


WORDNET = set()
MOJIBAKE = re.compile("\\w\\?\\w|\\?\\w|�|Ã.|Ä.|á»|áº")
# Filler that only says "this example shows the word"; it is not a translation.
FILLER_VI = re.compile(r"minh h.a|v. d. n.y|c.u n.y", re.I)
EX_SKELETON = collections.Counter()
EXVI_SKELETON = collections.Counter()


def skeleton(text, word):
    t = text.lower()
    for part in word.lower().split():
        t = re.sub(r"\b" + re.escape(part) + r"\w*", "X", t)
    return re.sub(r"\s+", " ", t).strip()


def has_vi(text):
    return any(ch in VI_LETTERS for ch in text.lower())


def walk_strings(value, path=""):
    if isinstance(value, dict):
        for k, v in value.items():
            yield from walk_strings(v, f"{path}.{k}" if path else k)
    elif isinstance(value, list):
        for i, v in enumerate(value):
            yield from walk_strings(v, f"{path}[{i}]")
    elif isinstance(value, str):
        yield path, value


def gloss_issues(g):
    out = []
    if not g.strip():
        return ["gloss_empty"]
    if g.rstrip().endswith("."):
        out.append("gloss_final_period")
    if g[:1].isupper():
        out.append("gloss_capitalized")
    parts = [p.strip().lower() for p in re.split(r"[,;]", g) if p.strip()]
    if len(parts) != len(set(parts)):
        out.append("gloss_duplicate_fragment")
    if len(parts) > 3:
        out.append("gloss_too_many_equivalents")
    if len(g) > 40:
        out.append("gloss_too_long")
    return out


def audit_word(w, gl, ranks, easy, topic_size):
    errors, warns = [], []
    level = w.get("level")
    senses = w.get("senses") or []

    ipa = w.get("ipa") or ""
    if not ipa:
        errors.append("ipa_missing")
    else:
        bad = sorted({c for c in ipa if c not in IPA_OK})
        if bad or ipa.startswith(".") or "/" in ipa:
            errors.append("ipa_format:" + "".join(bad))

    max_senses = 3 if (level or 9) <= 2 else 4
    if not senses:
        errors.append("no_senses")
    if len(senses) > max_senses:
        warns.append("too_many_senses")

    for i, s in enumerate(senses):
        tag = f"s{i + 1}"
        if not s.get("id"):
            errors.append(f"{tag}:sense_id_missing")
        if not s.get("def"):
            errors.append(f"{tag}:def_missing")
        elif s["def"].strip().lower().rstrip(".") in PLACEHOLDER_DEFS:
            errors.append(f"{tag}:def_placeholder")
        if s.get("def") and s.get("defSource", "auto") != "editor":
            warns.append(f"{tag}:def_unchecked")
        if s.get("defSource") == "editor" and s.get("def", "").strip().lower().rstrip(".") in WORDNET:
            # An editor definition is written for learners; verbatim WordNet text is auto data.
            errors.append(f"{tag}:editor_label_on_wordnet_text")
        elif len(s["def"].split()) > 12:
            warns.append(f"{tag}:def_long")
        exs = s.get("ex") or []
        if not exs:
            errors.append(f"{tag}:example_missing")
        if i == 0 and (level or 9) <= 2 and len(exs) < 2:
            warns.append("s1:one_example_only")
        for e in exs:
            t = (e.get("text") or "").strip()
            if EX_SKELETON[skeleton(t, w["lemma"])] > 2:
                errors.append(f"{tag}:example_template")
            n = len(t.split())
            if not SENTENCE.match(t) or t[-1:] not in ".!?\"'":
                errors.append(f"{tag}:example_fragment")
                continue
            lo, hi = (5, 12) if (level or 9) <= 2 else (6, 18)
            if n < lo:
                errors.append(f"{tag}:example_too_short")
            elif n > hi:
                warns.append(f"{tag}:example_too_long")
            if (level or 9) <= 2 and easy:
                toks = [x.lower() for x in WORD.findall(t)]
                hard = [x for x in toks if x not in easy and not x.startswith(w["lemma"].split()[0].lower()[:4])]
                if toks and len(hard) / len(toks) > 0.10:
                    warns.append(f"{tag}:example_vocab_hard")
        if (level or 9) <= 3 and s.get("pos", w.get("pos")) in ("n", "v", "adj") \
                and len(s.get("coll") or w.get("coll") or []) < 2:
            warns.append(f"{tag}:few_collocations")

    topics = w.get("topics") or []
    if not topics:
        errors.append("topic_missing")
    elif all(topic_size.get(t, 0) > CATCH_ALL_SIZE for t in topics):
        errors.append("topic_catch_all_only")

    rank = ranks.get(w["lemma"].lower())
    band = band_level(rank)
    if band and level and level > band + 1:
        warns.append(f"level_above_band:rank{rank}")
    if rank and rank <= 1000 and level and level > 2:
        warns.append(f"ngsl_top1000_at_level{level}")
    if not rank and level and level <= 2 and not w.get("levelReason") \
            and "picture" not in (w.get("source") or "") and w.get("source"):
        warns.append("low_frequency_at_low_level")

    if gl is not None:
        gsenses = gl.get("senses") or []
        if len(gsenses) != len(senses):
            errors.append("gloss_sense_count_mismatch")
        for i, gs in enumerate(gsenses):
            for issue in gloss_issues(gs.get("g") or ""):
                errors.append(f"s{i + 1}:{issue}")
            if MOJIBAKE.search(gs.get("g") or ""):
                errors.append(f"s{i + 1}:gloss_encoding_broken")
            for tr in (gs.get("ex") or {}).values():
                if MOJIBAKE.search(tr or ""):
                    errors.append(f"s{i + 1}:example_translation_encoding_broken")
                if FILLER_VI.search(tr or "") or EXVI_SKELETON[skeleton(tr or "", gs.get("g") or "~")] > 3:
                    errors.append(f"s{i + 1}:example_translation_filler")

    tier = "bronze" if errors else ("gold" if w.get("tier") == "gold" else "silver")
    return errors, warns, tier


def main():
    ap = argparse.ArgumentParser(description=__doc__.splitlines()[0])
    ap.add_argument("--locale", default="vi")
    ap.add_argument("--strict", action="store_true")
    args = ap.parse_args()

    core = load("content/en/words.json")
    words = core["words"]
    gloss_path = ROOT / "content/i18n" / args.locale / "words.json"
    legacy_path = ROOT / "content/i18n" / f"{args.locale}.json"
    if gloss_path.exists():
        glosses = load(gloss_path.relative_to(ROOT))
        get_gloss = lambda w: {"senses": [glosses.get(s.get("id"), {}) for s in w["senses"]]}  # noqa: E731
    elif legacy_path.exists():
        glosses = load(legacy_path.relative_to(ROOT)).get("words", {})
        get_gloss = lambda w: glosses.get(w["id"])  # noqa: E731
    else:
        get_gloss = lambda w: None  # noqa: E731

    WORDNET.update(wordnet_texts())
    ranks = ngsl_ranks()
    easy = {k for k, r in ranks.items() if r <= 2000} | {
        "i", "you", "he", "she", "it", "we", "they", "a", "an", "the", "is", "am", "are", "was", "were",
        "my", "your", "his", "her", "our", "their", "me", "him", "us", "them", "don't", "can't", "it's", "i'm"}
    topic_size = collections.Counter(t for w in words for t in w.get("topics") or [])

    for w in words:
        for s_ in w.get("senses") or []:
            for e in s_.get("ex") or []:
                EX_SKELETON[skeleton(e.get("text") or "", w["lemma"])] += 1
        gl = get_gloss(w)
        for gs in (gl or {}).get("senses") or []:
            for tr in (gs.get("ex") or {}).values():
                EXVI_SKELETON[skeleton(tr or "", gs.get("g") or "~")] += 1

    rows, err_count, warn_count = [], collections.Counter(), collections.Counter()
    tiers, tier_by_level = collections.Counter(), collections.Counter()
    for w in words:
        errors, warns, tier = audit_word(w, get_gloss(w), ranks, easy, topic_size)
        tiers[tier] += 1
        tier_by_level[(w.get("level"), tier)] += 1
        for e in errors:
            err_count[e.split(":")[-1] if e.startswith("s") and ":" in e else e.split(":")[0]] += 1
        for x in warns:
            warn_count[x.split(":")[-1] if x.startswith("s") and ":" in x else x.split(":")[0]] += 1
        if errors or warns:
            rows.append({"id": w["id"], "lemma": w["lemma"], "level": w.get("level"),
                         "source": w.get("source"), "tier": tier, "errors": errors, "warnings": warns})

    lemmas = {w["lemma"].lower() for w in words}
    missing = sorted((r, k) for k, r in ranks.items() if k not in lemmas)

    vi_in_core = []
    for rel in ("content/en/words.json", "content/en/grammar.json", "content/en/questions.json",
                "content/en/relations.json"):
        for path, text in walk_strings(load(rel)):
            # IPA is a phonetic transcription field, not learner-language prose;
            # malformed symbols are reported separately as ipa_format.
            if path.endswith(".ipa"):
                continue
            if has_vi(text) or re.search(r"(^|\.)vi(\.|$|\[)", path):
                vi_in_core.append(f"{rel}:{path}")

    grammar = load("content/en/grammar.json")["points"]
    questions = load("content/en/questions.json")["questions"]
    per_gp = collections.Counter(q.get("gp") for q in questions if q.get("gp"))
    thin_gp = sorted((per_gp.get(g["id"], 0), g["id"]) for g in grammar if per_gp.get(g["id"], 0) < 20)
    families = sum(1 for w in words if w.get("family"))
    confusables = len(core.get("confusables") or [])
    multi_sense = sum(1 for w in words if len(w.get("senses") or []) > 1)

    out = ROOT / "dist"
    out.mkdir(exist_ok=True)
    with open(out / "content-audit-issues.jsonl", "w", encoding="utf-8") as f:
        for r in rows:
            f.write(json.dumps(r, ensure_ascii=False) + "\n")

    def table(counter, title):
        lines = [f"### {title}", "", "| issue | entries |", "|---|---|"]
        lines += [f"| {k} | {v} |" for k, v in counter.most_common()]
        return lines + [""]

    md = ["# Content audit", "",
          f"Standard: `docs/content/CONTENT_STANDARD.md` · locale glosses: `{args.locale}`", "",
          "## Summary", "",
          "| metric | value | target |", "|---|---|---|",
          f"| entries | {len(words)} | ~3,500 at Levels 1–3, then 4–5 |",
          f"| tier gold / silver / bronze | {tiers['gold']} / {tiers['silver']} / {tiers['bronze']} | bronze = 0 in lessons |",
          f"| NGSL lemmas missing | {len(missing)} / {len(ranks)} | 0 |",
          f"| entries with > 1 sense | {multi_sense} | most NGSL top-2000 polysemous words |",
          f"| grammar points | {len(grammar)} | 40–60 |",
          f"| grammar points with < 20 questions | {len(thin_gp)} | 0 |",
          f"| confusable sets | {confusables} | 250–400 |",
          f"| word families | {families} | 800–1,200 |",
          f"| non-English strings/fields in content/en | {len(vi_in_core)} | 0 |", "",
          "## Tier by level", "", "| level | gold | silver | bronze |", "|---|---|---|---|"]
    for lv in sorted({k[0] for k in tier_by_level if k[0] is not None}):
        md.append(f"| {lv} | {tier_by_level[(lv, 'gold')]} | {tier_by_level[(lv, 'silver')]} | {tier_by_level[(lv, 'bronze')]} |")
    md += ["", "## Issues", ""]
    md += table(err_count, "Errors (block silver)")
    md += table(warn_count, "Warnings")
    md += ["## Missing NGSL lemmas (by rank)", "",
           ", ".join(f"{k} ({r})" for r, k in missing[:400]) + (" …" if len(missing) > 400 else ""), "",
           "## Catch-all topics (> %d words)" % CATCH_ALL_SIZE, ""]
    md += [f"- `{t}`: {n}" for t, n in topic_size.most_common() if n > CATCH_ALL_SIZE] or ["- none"]
    md += ["", "## Grammar points with < 20 questions", ""]
    md += [f"- `{g}`: {n}" for n, g in thin_gp] or ["- none"]
    md += ["", "## Non-English text in content/en (first 30)", ""]
    md += [f"- `{p}`" for p in vi_in_core[:30]] or ["- none"]
    md += ["", "Per-entry details: `dist/content-audit-issues.jsonl`."]
    (out / "content-audit.md").write_text("\n".join(md) + "\n", encoding="utf-8")

    total_errors = sum(err_count.values()) + len(missing) + len(vi_in_core)
    print(f"entries={len(words)} gold={tiers['gold']} silver={tiers['silver']} bronze={tiers['bronze']} "
          f"ngsl_missing={len(missing)} errors={total_errors} -> dist/content-audit.md")
    return 1 if args.strict and total_errors else 0


if __name__ == "__main__":
    sys.exit(main())
