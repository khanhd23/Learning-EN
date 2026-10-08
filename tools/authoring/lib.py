"""Authoring helpers. Content is written as compact Python calls and compiled by gen_content.py into
content/en/*.json (language data) + content/i18n/vi.json (Vietnamese layer).

Conventions
- The correct option is ALWAYS written first; the generator shuffles options with a seeded RNG so
  answer positions end up balanced (validator checks 20-30% per position).
- Every item is original. status defaults to "needs_review" (a human must skim before release).
"""
import re
import unicodedata

TOPICS = []      # dicts: id, icon, hue, vi
WORDS = []       # dicts
CONFUSABLES = []
GRAMMAR = []
QUESTIONS = []   # dicts (en part) + "_vi" explanation
PASSAGES = []

_current_topic = None
_current_point = None


def nfc(s):
    return unicodedata.normalize("NFC", s) if isinstance(s, str) else s


def slug(s):
    s = unicodedata.normalize("NFKD", s).encode("ascii", "ignore").decode().lower()
    return re.sub(r"[^a-z0-9]+", "_", s).strip("_")


# ---------------------------------------------------------------- vocabulary

def topic(tid, vi, icon, hue):
    global _current_topic
    TOPICS.append(dict(id=tid, vi=nfc(vi), icon=icon, hue=hue))
    _current_topic = tid


_last_word = None


def _sense(w, pos, gloss, ex, ex_vi):
    n = len(w["senses"]) + 1
    eid = "%s_s%d" % (w["id"], n)
    w["senses"].append(dict(pos=pos, ex=[dict(id=eid, text=nfc(ex))]))
    w["_vi"]["senses"].append(dict(g=nfc(gloss), ex={eid: nfc(ex_vi)}))


def W(lemma, pos, ipa, level, gloss, ex, ex_vi, fam=None, coll=None, tip=None, extra_topics=()):
    """One word (its main sense). pos: n, v, adj, adv, prep, conj, pron, phr, idiom.

    The same lemma written again in another topic merges into one entry: same part of speech ->
    only the topic is added; a new part of speech -> a new sense (e.g. "order" n. / v.).
    Add more meanings of the word with S(...) right after W(...).
    """
    global _last_word
    wid = slug(lemma)
    w = next((x for x in WORDS if x["id"] == wid), None)
    if w is None:
        w = dict(id=wid, lemma=lemma, ipa=ipa, level=level, topics=[], senses=[], family={}, coll=[],
                 _vi=dict(senses=[], tip=None))
        WORDS.append(w)
    for t in (_current_topic, *extra_topics):
        if t not in w["topics"]:
            w["topics"].append(t)
    if not any(s["pos"] == pos for s in w["senses"]):
        _sense(w, pos, gloss, ex, ex_vi)
    w["level"] = min(w["level"], level)
    w["family"].update(fam or {})
    w["coll"] += [c for c in (coll or []) if c not in w["coll"]]
    if tip:
        w["_vi"]["tip"] = nfc(tip)
    _last_word = w


def S(pos, gloss, ex, ex_vi):
    """Another meaning / part of speech of the previous W() word, with its own example in context."""
    _sense(_last_word, pos, gloss, ex, ex_vi)


def CONF(cid, words, tip, notes):
    """Confusable set. notes: {word: vi meaning + usage}."""
    CONFUSABLES.append(dict(id=cid, words=words, _vi=dict(tip=nfc(tip), notes={k: nfc(v) for k, v in notes.items()})))


# ---------------------------------------------------------------- grammar

def point(gid, level, title, formula, when, body, signals, examples, mistakes, tags=(), exam=False):
    """examples: [(en, vi, highlighted_fragment)]; mistakes: [(wrong, right, note_vi)]."""
    global _current_point
    GRAMMAR.append(dict(
        id=gid, level=level, formula=formula, signals=list(signals), tags=list(tags), exam=exam,
        examples=[dict(en=e, hl=h) for e, _, h in examples],
        mistakes=[dict(wrong=w, right=r) for w, r, _ in mistakes],
        _vi=dict(title=nfc(title), when=nfc(when), body=nfc(body),
                 examples=[nfc(v) for _, v, _ in examples], mistakes=[nfc(n) for _, _, n in mistakes]),
    ))
    _current_point = gid


def _q(kind, expl, **kw):
    base = dict(type=kind, gp=kw.pop("gp", _current_point), level=kw.pop("level", None),
                skills=list(kw.pop("skills", ())), trap=list(kw.pop("trap", ())),
                fmt=kw.pop("fmt", "grammar"), qtype=kw.pop("qtype", None), topic=kw.pop("topic", None))
    base.update(kw)
    base["_vi"] = nfc(expl)
    QUESTIONS.append(base)
    return base


def C(stem, opts, expl, **kw):
    """Cloze (E02): one '___' blank, correct option first."""
    return _q("E02", expl, stem=nfc(stem), opts=list(opts), **kw)


def M(stem, opts, expl, **kw):
    """Plain multiple choice (E01/E12-like) without a blank, correct option first."""
    return _q(kw.pop("kind", "E01"), expl, stem=nfc(stem), opts=list(opts), **kw)


def T(stem, opts, expl, **kw):
    """Sentence transformation (E12): choose the sentence closest in meaning. Correct first."""
    return _q("E12", expl, stem=nfc(stem), opts=list(opts), **kw)


def E(marked, wrong_index, correction, expl, **kw):
    """Find the error (E04). marked: sentence with exactly 4 [bracketed] parts; wrong_index 0-3."""
    parts = re.findall(r"\[([^\]]+)\]", marked)
    assert len(parts) == 4, marked
    return _q("E04", expl, stem=nfc(marked), ans=wrong_index, fix=correction, **kw)


def O(sentence, vi, **kw):
    """Word order (E05): learner rebuilds [sentence] from shuffled chips."""
    return _q("E05", "", stem=nfc(sentence), vi=nfc(vi), **kw)


def P5(stem, opts, qtype, expl, topic=None, level=3, **kw):
    """TOEIC Part 5-style item (original). Correct option first."""
    # New bank entries may carry their English explanation next to the
    # Vietnamese teaching explanation.  Keeping it in authoring data lets the
    # generator produce both locale layers without hand-editing generated JSON.
    expl_en = kw.pop("expl_en", None)
    if expl_en:
        kw["_en_expl"] = expl_en
    return C(stem, opts, expl, fmt="toeic_p5", qtype=qtype, topic=topic, level=level, gp=kw.pop("gp", None), **kw)


def P6(pid, title, text, blanks, topic=None, level=3):
    """Part 6-style passage. text has {1}..{4}; blanks: [(opts_correct_first, qtype, expl_vi)]."""
    assert all("{%d}" % (i + 1) in text for i in range(len(blanks))), pid
    values = []
    for blank in blanks:
        if len(blank) == 3:
            o, q, e = blank
            en = None
        else:
            o, q, e, en = blank
        row = dict(opts=list(o), qtype=q, _vi=nfc(e))
        if en:
            row["_en_expl"] = nfc(en)
        values.append(row)
    PASSAGES.append(dict(id=pid, title=title, text=nfc(text), topic=topic, level=level, blanks=values))


def PR(words, expl, level=2):
    """Pronunciation: 4 words with the focus letters in [brackets]; the ODD one first."""
    return _q("E01", expl, stem="", opts=list(words), fmt="school", qtype="pronunciation", gp=None, level=level,
              skills=["pronunciation"])


def ST(words, expl, level=2):
    """Stress: 4 words, the one with a DIFFERENT stress position first."""
    return _q("E01", expl, stem="", opts=list(words), fmt="school", qtype="stress", gp=None, level=level,
              skills=["stress"])


def SW(lemma, pos, gloss, ex, ex_vi):
    """Extra meaning for a word defined elsewhere (looked up by lemma)."""
    w = next((x for x in WORDS if x["lemma"].lower() == lemma.lower()), None)
    assert w is not None, "SW: unknown word " + lemma
    _sense(w, pos, gloss, ex, ex_vi)
