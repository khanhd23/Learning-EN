"""Compiles tools/authoring/*.py into content/en/*.json + content/i18n/vi/*.json.

Usage: python tools/gen_content.py
"""
import hashlib
import importlib
import json
import os
import random
import re
import sys
import xml.etree.ElementTree as ET

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
sys.path.insert(0, os.path.join(ROOT, "tools", "authoring"))
import lib  # noqa: E402

DEFS_PATH = os.path.join(ROOT, "tools", "sources", "wordnet_defs.json")
with open(DEFS_PATH, encoding="utf-8") as f:
    WORDNET_DEFS = json.load(f)
def load_editor_rows(filename):
    rows = {}
    with open(os.path.join(ROOT, "tools", "authoring", filename), encoding="utf-8") as f:
        for line in f:
            if not line.strip() or line.startswith("sense_id\t"):
                continue
            cols = line.rstrip("\n").split("\t")
            if len(cols) == 7:
                sid, pos, definition, gloss, example, example_vi, note = cols
            else:
                sid, definition, gloss, example, example_vi, note = cols[:6]
                pos = ""
            rows[sid] = {
                "pos": pos, "def": definition, "gloss": gloss, "example": example,
                "example_vi": example_vi, "note": note,
            }
    return rows


def load_all_editor_rows():
    """Merge every tools/authoring/senses_editor_batch<N>.tsv in batch order."""
    folder = os.path.join(ROOT, "tools", "authoring")
    names = [n for n in os.listdir(folder) if re.fullmatch(r"senses_editor_batch\d+\.tsv", n)]
    rows = {}
    for name in sorted(names, key=lambda n: int(re.search(r"\d+", n).group())):
        rows.update(load_editor_rows(name))
    return rows


def canonical(value):
    return json.dumps(value, ensure_ascii=False, sort_keys=True, separators=(",", ":"))


def source_hash(value):
    return hashlib.sha256(canonical(value).encode("utf-8")).hexdigest()[:12]


def refresh_vi_status(path, old_status, sources):
    """Keep approved metadata for unchanged source; owner-refresh changed vi entries."""
    old_entries = old_status.get("entries", {}) if isinstance(old_status, dict) else {}
    entries = {}
    for kind, values in sources.items():
        previous = old_entries.get(kind, {})
        entries[kind] = {}
        for key, source in values.items():
            digest = source_hash(source)
            prior = previous.get(key, {})
            if prior.get("src") == digest:
                entries[kind][key] = dict(prior)
            else:
                entries[kind][key] = {"s": "approved", "by": "owner", "src": digest}
    status = {"schemaVersion": 1, "locale": "vi", "status": "complete", "todo": False, "entries": entries}
    with open(path, "w", encoding="utf-8") as f:
        json.dump(status, f, ensure_ascii=False, separators=(",", ":"))


EDITOR_ROWS = load_all_editor_rows()

# English source explanations for grammar points (owner-written): when, body, and why per mistake.
with open(os.path.join(os.path.dirname(os.path.abspath(__file__)), "authoring", "grammar_en.json"), encoding="utf-8") as _f:
    GRAMMAR_EN = {k: v for k, v in json.load(_f).items() if not k.startswith("_")}


def load_question_expl_en():
    """id|explanation|stem override|vi override. Passage blanks use passage_id#index."""
    rows = {}
    path = os.path.join(os.path.dirname(os.path.abspath(__file__)), "authoring", "question_expl_en.txt")
    with open(path, encoding="utf-8") as f:
        for line in f:
            line = line.rstrip("\r\n")
            if not line.strip() or line.startswith("#"):
                continue
            cols = line.split("|") + ["", ""]
            if cols[0] in rows:
                raise SystemExit(f"question_expl_en.txt: duplicate id {cols[0]}")
            rows[cols[0]] = {"expl": cols[1].strip(), "stem": cols[2].strip(), "vi": cols[3].strip()}
    return rows


QUESTION_EXPL_EN = load_question_expl_en()


def apply_grammar_en(point):
    src = GRAMMAR_EN[point["id"]]
    for index, fix in src.get("exampleFix", {}).items():
        point["examples"][int(index)] = dict(fix)
    for index, fix in src.get("mistakeFix", {}).items():
        point["mistakes"][int(index)] = dict(fix)
    if len(src["why"]) != len(point["mistakes"]):
        raise SystemExit(f"grammar_en.json: {point['id']} needs one 'why' per mistake")
    point["when"] = src["when"]
    point["body"] = src["body"]
    for mistake, why in zip(point["mistakes"], src["why"]):
        mistake["why"] = why

# Controlled topic taxonomy (docs/content/CONTENT_STANDARD.md §5). Topics for words come from
# tools/authoring/entry_corrections.tsv; "unsorted" marks entries that are not in lessons yet and is
# deliberately not listed as a topic, so the app never shows it.
CONTROLLED_TOPICS = [
    ('people', 'family', '👪', 'Gia đình'),
    ('people', 'friends_relationships', '🤝', 'Bạn bè & quan hệ'),
    ('people', 'body_appearance', '🧍', 'Cơ thể & ngoại hình'),
    ('people', 'feelings_personality', '😊', 'Cảm xúc & tính cách'),
    ('people', 'health_illness', '🩺', 'Sức khỏe & bệnh tật'),
    ('people', 'age_life_stages', '🌱', 'Tuổi tác & các giai đoạn sống'),
    ('daily_life', 'home_furniture', '🏠', 'Nhà cửa & đồ dùng'),
    ('daily_life', 'daily_routine', '🗓️', 'Sinh hoạt hằng ngày'),
    ('daily_life', 'food_drink', '🍎', 'Đồ ăn & thức uống'),
    ('daily_life', 'cooking', '🍳', 'Nấu ăn'),
    ('daily_life', 'shopping', '🛍️', 'Mua sắm'),
    ('daily_life', 'clothes', '👕', 'Quần áo'),
    ('daily_life', 'money_banking', '💰', 'Tiền bạc & ngân hàng'),
    ('daily_life', 'time_dates', '⏰', 'Thời gian & ngày tháng'),
    ('daily_life', 'numbers_quantity', '🔢', 'Số & số lượng'),
    ('daily_life', 'weather', '🌤️', 'Thời tiết'),
    ('places_travel', 'city_directions', '🗺️', 'Thành phố & chỉ đường'),
    ('places_travel', 'transport', '🚌', 'Phương tiện đi lại'),
    ('places_travel', 'travel_holidays', '✈️', 'Du lịch & kỳ nghỉ'),
    ('places_travel', 'hotel_restaurant', '🍽️', 'Khách sạn & nhà hàng'),
    ('places_travel', 'nature_landscape', '🌳', 'Thiên nhiên & cảnh quan'),
    ('places_travel', 'animals', '🐾', 'Động vật'),
    ('study', 'school', '🏫', 'Trường học'),
    ('study', 'university', '🎓', 'Đại học'),
    ('study', 'language_learning', '📚', 'Học ngôn ngữ'),
    ('study', 'science_basics', '🔬', 'Khoa học cơ bản'),
    ('study', 'academic_words', '🧠', 'Từ vựng học thuật'),
    ('work', 'jobs', '💼', 'Nghề nghiệp'),
    ('work', 'office', '🏢', 'Văn phòng'),
    ('work', 'meetings', '🗣️', 'Cuộc họp'),
    ('work', 'hr_recruiting', '🧑\u200d💼', 'Nhân sự & tuyển dụng'),
    ('work', 'sales_customer', '🤲', 'Bán hàng & khách hàng'),
    ('work', 'marketing', '📣', 'Tiếp thị'),
    ('work', 'finance_accounting', '📊', 'Tài chính & kế toán'),
    ('work', 'contracts_law', '⚖️', 'Hợp đồng & pháp luật'),
    ('work', 'logistics', '📦', 'Sản xuất & kho vận'),
    ('work', 'it_technology', '💻', 'Công nghệ thông tin'),
    ('work', 'events', '📅', 'Sự kiện & hội nghị'),
    ('society', 'media_news', '📰', 'Truyền thông & tin tức'),
    ('society', 'internet_social', '🌐', 'Internet & mạng xã hội'),
    ('society', 'environment_energy', '♻️', 'Môi trường & năng lượng'),
    ('society', 'government_society', '🏛️', 'Chính phủ & xã hội'),
    ('society', 'arts_entertainment', '🎭', 'Nghệ thuật & giải trí'),
    ('society', 'sports_fitness', '⚽', 'Thể thao & vận động'),
    ('language', 'function_words', '🔤', 'Từ chức năng'),
    ('language', 'core_verbs', '▶️', 'Động từ cốt lõi'),
    ('language', 'describing_things', '🎨', 'Miêu tả sự vật'),
    ('language', 'linking_words', '🔗', 'Từ nối'),
    ('language', 'phrasal_verbs', '↗️', 'Cụm động từ'),
    ('language', 'idioms_chunks', '💬', 'Thành ngữ & cụm từ'),
]
TOPIC_NAMES_EN = {'family': 'Family', 'friends_relationships': 'Friends & relationships', 'body_appearance': 'Body & appearance', 'feelings_personality': 'Feelings & personality', 'health_illness': 'Health & illness', 'age_life_stages': 'Age & life stages', 'home_furniture': 'Home & household', 'daily_routine': 'Daily routine', 'food_drink': 'Food & drink', 'cooking': 'Cooking', 'shopping': 'Shopping', 'clothes': 'Clothes', 'money_banking': 'Money & banking', 'time_dates': 'Time & dates', 'numbers_quantity': 'Numbers & quantity', 'weather': 'Weather', 'city_directions': 'City & directions', 'transport': 'Transport', 'travel_holidays': 'Travel & holidays', 'hotel_restaurant': 'Hotels & restaurants', 'nature_landscape': 'Nature & landscape', 'animals': 'Animals', 'school': 'School', 'university': 'University', 'language_learning': 'Language learning', 'science_basics': 'Science basics', 'academic_words': 'Academic words', 'jobs': 'Jobs', 'office': 'Office', 'meetings': 'Meetings', 'hr_recruiting': 'HR & recruiting', 'sales_customer': 'Sales & customers', 'marketing': 'Marketing', 'finance_accounting': 'Finance & accounting', 'contracts_law': 'Contracts & law', 'logistics': 'Production & logistics', 'it_technology': 'IT & technology', 'events': 'Events & conferences', 'media_news': 'Media & news', 'internet_social': 'Internet & social media', 'environment_energy': 'Environment & energy', 'government_society': 'Government & society', 'arts_entertainment': 'Arts & entertainment', 'sports_fitness': 'Sports & fitness', 'function_words': 'Function words', 'core_verbs': 'Core verbs', 'describing_things': 'Describing things', 'linking_words': 'Linking words', 'phrasal_verbs': 'Phrasal verbs', 'idioms_chunks': 'Idioms & expressions'}
GRAMMAR_TITLES_EN = {'be': 'The verb to be (am / is / are)', 'present_simple': 'Present simple', 'articles': 'Articles: a / an / the', 'plurals': 'Plural nouns', 'prep_time': 'Prepositions of time and place: in / on / at', 'there_is': 'There is / There are', 'can_could': 'Can / Could', 'word_order': 'Basic word order', 'past_simple': 'Past simple', 'present_continuous': 'Present continuous', 'comparison': 'Comparatives and superlatives', 'quantifiers': 'Countable and uncountable nouns; quantifiers', 'future': 'Will and be going to', 'must_should': 'Must / Should / Have to', 'pronouns': 'Pronouns and possessives', 'present_perfect': 'Present perfect (vs. past simple)', 'passive': 'The passive voice', 'conditionals': 'Conditionals: zero, first, second', 'relative': 'Relative clauses (who / which / that / whose / where)', 'gerund_infinitive': 'Gerund or infinitive', 'conj_prep': 'Conjunction or preposition (although / despite …)', 'agreement': 'Subject–verb agreement', 'word_forms': 'Word forms (noun / verb / adjective / adverb)', 'past_perfect': 'Past perfect', 'conditional3': 'Third and mixed conditionals', 'reported': 'Reported speech', 'participle': 'Participle clauses', 'wish': 'Wish / If only', 'inversion_basic': 'Basic inversion (So / Neither, Not only …)', 'modal_perfect': 'Modal perfects (should have, must have …)', 'linking': 'Linking words and discourse markers', 'noun_phrase': 'Complex noun phrases', 'subjunctive': 'The subjunctive (suggest that + base verb)', 'cleft': 'Cleft sentences (It is … that / What … is)', 'inversion_adv': 'Advanced inversion (No sooner, Hardly …)', 'ellipsis': 'Ellipsis and substitution', 'hedging': 'Hedging and softening', 'register': 'Formal and informal register'}
CONTROLLED_TOPIC_IDS = {t[1] for t in CONTROLLED_TOPICS}
UNSORTED = "unsorted"
LEGACY_TOPIC_MAP = {"office": "office", "hr": "hr_recruiting", "finance": "money_banking", "accounting": "finance_accounting", "marketing": "marketing", "sales": "sales_customer", "legal": "contracts_law", "logistics": "logistics", "commerce": "shopping", "travel": "travel_holidays", "hospitality": "hotel_restaurant", "events": "events", "tech": "it_technology", "health": "health_illness", "housing": "home_furniture", "education": "school", "environment": "environment_energy", "media": "media_news", "academic": "academic_words", "family": "family", "feelings": "feelings_personality", "food": "food_drink", "weather": "weather", "sports": "sports_fitness", "entertainment": "arts_entertainment", "body": "body_appearance", "animals": "animals", "clothes": "clothes", "city": "city_directions", "social": "internet_social", "phrasal": "phrasal_verbs", "idioms": "idioms_chunks", "home_basics": "home_furniture"}


def load_topic_corrections(words):
    path = os.path.join(ROOT, "tools", "authoring", "entry_corrections.tsv")
    rows = {}
    with open(path, encoding="utf-8") as f:
        for n, raw in enumerate(f, 1):
            line = raw.strip()
            if not line or line.startswith("#"):
                continue
            parts = line.split("|")
            wid, topic_text = parts[0], parts[1] if len(parts) > 1 else ""
            ipa = parts[2].strip() if len(parts) > 2 else ""
            level = parts[3].strip() if len(parts) > 3 else ""
            if level and level not in {"1", "2", "3", "4", "5"}:
                raise SystemExit(f"entry_corrections.tsv line {n}: bad level {level!r}")
            topics = [t.strip() for t in topic_text.split(",") if t.strip()]
            ok = topics == [UNSORTED] or (1 <= len(topics) <= 3 and all(t in CONTROLLED_TOPIC_IDS for t in topics))
            if not ok or wid in rows:
                raise SystemExit(f"entry_corrections.tsv line {n}: bad row {line!r}")
            rows[wid] = (topics, ipa, int(level) if level else None)
    missing = [w["id"] for w in words if w["id"] not in rows]
    if missing:
        raise SystemExit(f"entry_corrections.tsv has no topic for: {missing[:10]} (+{max(0, len(missing) - 10)})")
    return rows


VI_CHARS = set("áạảãấầẩẫậắằẳẵặđẹẻẽếềểễệỉĩịọỏõốồổỗộớờởỡợụủũứừửữựỳỷỹỵ")


def has_vi(text):
    return any(c in VI_CHARS for c in (text or "").lower())


IRREGULAR_FORMS = {
    "be": ["am", "is", "are", "was", "were", "been", "being"], "have": ["has", "had"],
    "do": ["does", "did", "done"], "go": ["goes", "went", "gone"], "say": ["said"],
    "make": ["made"], "take": ["took", "taken"], "come": ["came"], "see": ["saw", "seen"],
    "get": ["got", "gotten"], "give": ["gave", "given"], "find": ["found"], "think": ["thought"],
    "tell": ["told"], "become": ["became"], "leave": ["left"], "feel": ["felt"], "bring": ["brought"],
    "begin": ["began", "begun"], "keep": ["kept"], "hold": ["held"], "write": ["wrote", "written"],
    "stand": ["stood"], "hear": ["heard"], "let": ["let"], "mean": ["meant"], "set": ["set"],
    "meet": ["met"], "run": ["ran"], "pay": ["paid"], "sit": ["sat"], "speak": ["spoke", "spoken"],
    "lose": ["lost"], "fall": ["fell", "fallen"], "send": ["sent"], "build": ["built"],
    "understand": ["understood"], "spend": ["spent"], "grow": ["grew", "grown"], "win": ["won"],
    "buy": ["bought"], "break": ["broke", "broken"], "drive": ["drove", "driven"],
    "read": ["read"], "put": ["put"], "learn": ["learned", "learnt"], "i": ["I"],
}


def highlight_form(example, lemma):
    """Return the exact word form of `lemma` used in `example`, for highlighting."""
    head = lemma.split()[0]
    tokens = re.findall(r"[A-Za-z']+", example)
    candidates = [head] + IRREGULAR_FORMS.get(head.lower(), [])
    for tok in tokens:
        low = tok.lower()
        if any(low == c.lower() for c in candidates):
            return tok
    stem = head.lower().rstrip("e")
    for tok in tokens:
        low = tok.lower()
        if low.startswith(stem) and len(low) - len(head) <= 4:
            return tok
    return head


def normalize_ipa(value):
    # IPA is stored without UI slashes or syllable dots. Remaining unusual symbols
    # are intentionally preserved for the audit to flag rather than guessed away.
    return (value or "").replace("/", "").replace(".", "").strip()


def normalize_gloss(value):
    value = (value or "").strip()
    value = value.rstrip(".").strip()
    if value and value[0].isupper() and not value.isupper():
        value = value[0].lower() + value[1:]
    fragments = []
    seen = set()
    for fragment in re.split(r"([,;])", value):
        if fragment in ",;":
            if fragments and fragments[-1] != fragment:
                fragments.append(fragment)
            continue
        cleaned = fragment.strip()
        if not cleaned:
            continue
        key = cleaned.casefold()
        if key in seen:
            if fragments and fragments[-1] in ",;":
                fragments.pop()
            continue
        seen.add(key)
        fragments.append(cleaned)
    return " ".join(fragments).replace(" ,", ",").replace(" ;", ";")


def definition_for(word, sense, index):
    existing = (sense.get("def") or "").strip()
    if existing.lower().rstrip(".") in {
        "a word or phrase used in english", "a person, place, thing, or idea",
        "describing a quality or state", "a word showing a relation or place",
        "in a particular way or manner", "a word that joins words or ideas",
        "to do or make something", "how something is done or how it happens",
    }:
        return "", "template"
    if existing:
        return existing, "wordnet-auto"
    lemma = word["lemma"].lower().replace(" ", "_")
    defs = (WORDNET_DEFS.get(lemma) or {}).get(sense.get("pos"), [])
    if defs:
        return defs[min(index, len(defs) - 1)], "wordnet-auto"
    templates = {
        "n": "a person, place, thing, or idea",
        "v": "to do or make something",
        "adj": "describing a quality or state",
        "adv": "in a particular way or manner",
        "prep": "a word showing a relation or place",
        "conj": "a word that joins words or ideas",
    }
    return "", "template"


def upgrade_schema(words, vi):
    """Upgrade authored output to the English-core/content-standard schema."""
    quarantine = vi.setdefault("quarantine", {"collocations": {}, "examples": {}})
    for word in words:
        added = {
            "place": ("n", "place_s2"),
            "kind": ("n", "kind_s2"),
            "live": ("v", "live_s2"),
        }.get(word["id"])
        if added and not any(s.get("pos") == added[0] for s in word.get("senses", [])):
            word["senses"].append({"pos": added[0], "ex": [{"id": added[1], "text": ""}]})
            locale_word = vi.get("words", {}).get(word["id"])
            if locale_word:
                locale_word.setdefault("senses", []).append({"g": "", "ex": {}})
        first_ids = {sid for sid, row in EDITOR_ROWS.items() if "[first]" in row.get("note", "")}
        legacy_first = {"book": "n", "place": "n", "kind": "n", "live": "v"}
        sense_ids = [((s.get("ex") or [{}])[0].get("id")) or s.get("id") for s in word.get("senses", [])]
        if word["id"] in legacy_first or first_ids.intersection(sense_ids):
            preferred = legacy_first.get(word["id"])
            locale_word = vi.get("words", {}).get(word["id"])
            word["senses"].sort(key=lambda s: 0 if (
                (((s.get("ex") or [{}])[0].get("id")) or s.get("id")) in first_ids
                or (preferred and s.get("pos") == preferred)) else 1)
            if locale_word:
                reordered = []
                for sense in word["senses"]:
                    example_id = (sense.get("ex") or [{}])[0].get("id")
                    reordered.append(next((s for s in locale_word["senses"] if example_id in s.get("ex", {})), {"g": "", "ex": {}}))
                locale_word["senses"] = reordered
        word["ipa"] = normalize_ipa(word.get("ipa"))
        word.setdefault("levelReason", "")
        word.setdefault("forms", {})
        word.setdefault("grammarIds", [])
        word["tier"] = "bronze"
        for index, sense in enumerate(word.get("senses", [])):
            sid = sense.get("id") or ((sense.get("ex") or [{}])[0].get("id")) or f"{word['id']}_s{index + 1}"
            sense["id"] = sid
            sense["def"], def_source = definition_for(word, sense, index)
            if sense["def"].strip().lower().rstrip(".") in {
                "a word or phrase used in english", "a person, place, thing, or idea",
                "describing a quality or state", "a word showing a relation or place",
                "in a particular way or manner", "a word that joins words or ideas",
                "to do or make something", "how something is done or how it happens",
            }:
                sense["def"] = ""
                def_source = "template"
            curated = EDITOR_ROWS.get(sid)
            if curated:
                sense["def"] = curated["def"]
                def_source = "editor"
                if curated.get("pos"):
                    sense["pos"] = curated["pos"]
                # A curated sense keeps exactly one example: the editor's sentence.
                # Older dictionary fragments attached to the same sense are dropped.
                ex_id = ((sense.get("ex") or [{}])[0].get("id")) or sense["id"]
                sense["ex"] = [{"id": ex_id, "text": curated["example"],
                                "hl": highlight_form(curated["example"], word["lemma"])}]
                locale_word = vi.get("words", {}).get(word["id"])
                if locale_word and len(locale_word.get("senses", [])) > index:
                    locale_sense = locale_word["senses"][index]
                    locale_sense["g"] = curated["gloss"]
                    locale_sense["ex"] = {ex_id: curated["example_vi"]}
            sense["defSource"] = def_source
            sense.setdefault("register", "neutral")
            if def_source != "editor":
                word["needs_review"] = True
            for example in sense.get("ex", []):
                example["hl"] = example.get("hl") or word["lemma"].split()[0]
                if has_vi(example.get("text", "")):
                    quarantine["examples"][example["id"]] = example.pop("text")
                    example["text"] = "Example sentence needs review."
            if has_vi(" ".join(sense.get("coll") or [])):
                quarantine["collocations"][sid] = sense.get("coll") or []
                sense["coll"] = []
        if has_vi(" ".join(word.get("coll") or [])):
            quarantine["collocations"][word["id"]] = word.get("coll") or []
            word["coll"] = []

        locale_word = vi.get("words", {}).get(word["id"])
        if locale_word:
            for locale_sense in locale_word.get("senses", []):
                locale_sense["g"] = normalize_gloss(locale_sense.get("g"))
                if not locale_sense.get("g") and word["id"] == "should":
                    locale_sense["g"] = "nên, phải"

    # Normalize the handful of legacy Vietnamese formula strings while preserving
    # their learner-language source in the locale pack.
    formula_en = {
        "articles": "a + consonant sound · an + vowel sound · the + specific or known noun",
        "plurals": "N + s / es / ies · irregular: man → men, child → children",
        "prep_time": "at + clock time or exact place · on + day or surface · in + month, year, season, or inside space",
        "there_is": "There is + singular or uncountable N · There are + plural N",
        "can_could": "S + can/could + base verb",
        "word_order": "S + V + O + Place + Time · Adjectives come before nouns",
        "quantifiers": "many/few + countable N · much/little + uncountable N · some/any/a lot of: both",
        "conditionals": "0: If + present, present · 1: If + present, will + V · 2: If + past, would + V",
        "relative": "N (person) + who · N (thing) + which · that: both · whose + N · where: place",
        "conj_prep": "Conjunction + CLAUSE (S + V) · Preposition + NOUN / V-ing",
        "agreement": "Singular subject → V(s/es) · plural subject → base verb",
        "conditional3": "If + had + V3, would have + V3 · mixed: If + had + V3, would + V (now)",
        "participle": "V-ing (active) · V3 (passive) · Having + V3 (earlier action)",
        "wish": "wish + past (present) · wish + had V3 (past) · wish + would (want change)",
        "inversion_basic": "So/Neither + auxiliary + S · Not only + auxiliary + S + V, but also...",
        "subjunctive": "suggest / recommend / insist / require / It is essential that + S + base verb",
        "inversion_adv": "No sooner had S V3 than... · Hardly had S V3 when... · Only after/when... + inversion · Should + S + V (= If)",
        "ellipsis": "I think so / I hope not · one/ones · do so · auxiliary replaces a whole phrase",
    }
    for point in vi.get("grammar", {}).values():
        if "formula" in point and has_vi(point["formula"]):
            point.setdefault("formulaVi", point["formula"])
    return formula_en

MODULES = [
    "vocab_work", "vocab_life", "vocab_everyday", "vocab_modern", "vocab_foundation", "senses", "vocab_open", "vocab_expansion", "vocab_picture", "vocab_ngsl_core", "confusables",
    "grammar_l1", "grammar_l2", "grammar_l3", "grammar_l4", "grammar_l5",
    "exam_p5", "exam_p6", "school", "pet_lines",
]


def qid(q):
    key = "|".join([q["type"], q.get("fmt") or "", q.get("stem") or "", "/".join(q.get("opts") or [])])
    return "q" + hashlib.sha1(key.encode("utf-8")).hexdigest()[:9]


def main():
    loaded = []
    for m in MODULES:
        try:
            loaded.append(importlib.import_module(m))
        except ModuleNotFoundError as e:
            if e.name != m:
                raise
            print(f"(skip missing module {m})")
    pet = next((getattr(m, "PET_LINES") for m in loaded if hasattr(m, "PET_LINES")), {})
    ui_tips = next((getattr(m, "QTYPE_TIPS") for m in loaded if hasattr(m, "QTYPE_TIPS")), {})

    vi = dict(topics={}, words={}, conf={}, grammar={}, q={}, passages={}, pet=pet, tips=ui_tips)

    # Topics: only the controlled taxonomy is emitted.
    topics = []
    for i, (domain, topic_id, icon, vi_name) in enumerate(CONTROLLED_TOPICS):
        vi["topics"][topic_id] = vi_name
        topics.append(dict(id=topic_id, domain=domain, icon=icon, hue=i % 10, name=TOPIC_NAMES_EN[topic_id]))

    # Words
    words = []
    for w in lib.WORDS:
        v = w.pop("_vi")
        vi["words"][w["id"]] = {k: val for k, val in v.items() if val}
        words.append(w)

    corrections = load_topic_corrections(words)
    for w in words:
        w["topics"], ipa, level = corrections[w["id"]]
        if ipa:
            w["ipa"] = ipa
        if level and level != w["level"]:
            w["levelReason"] = f"set by NGSL band (was Level {w['level']})"
            w["level"] = level

    lemma_to_id = {}
    for w in words:
        lemma_to_id.setdefault(w["lemma"].lower(), w["id"])
        w["pos"] = w["senses"][0]["pos"]

    conf = []
    for c in lib.CONFUSABLES:
        v = c.pop("_vi")
        vi["conf"][c["id"]] = v
        c["wordIds"] = [lemma_to_id.get(x.lower()) for x in c["words"]]
        conf.append(c)
        for wid in c["wordIds"]:
            if wid:
                next(w for w in words if w["id"] == wid).setdefault("conf", []).append(c["id"])

    formula_en = upgrade_schema(words, vi)

    # Grammar
    grammar = []
    for g in lib.GRAMMAR:
        vi["grammar"][g["id"]] = g.pop("_vi")
        g["title"] = GRAMMAR_TITLES_EN[g["id"]]
        apply_grammar_en(g)
        if g["id"] in formula_en and has_vi(g.get("formula", "")):
            vi["grammar"][g["id"]]["formulaVi"] = g["formula"]
            g["formula"] = formula_en[g["id"]]
        grammar.append(g)
    gp_level = {g["id"]: g["level"] for g in grammar}

    # Questions: stable ids, balanced answer positions for option-based items.
    questions = []
    seen = set()
    mc_index = 0
    rng = random.Random(20261004)
    block = []
    for q in lib.QUESTIONS:
        q = dict(q)
        if q.get("topic"):
            q["topic"] = LEGACY_TOPIC_MAP.get(q["topic"], q["topic"])
        expl = q.pop("_vi")
        q["id"] = qid(q)
        if q["id"] in seen:
            raise SystemExit(f"duplicate question: {q.get('stem')}")
        seen.add(q["id"])
        # English explanation; a stem override fixes the wording without changing the stable id.
        en_row = QUESTION_EXPL_EN.get(q["id"])
        if en_row:
            q["expl"] = en_row["expl"]
            if en_row["stem"]:
                q["stem"] = en_row["stem"]
            if en_row["vi"]:
                # For translation items (E05) the vi column is the sentence translation.
                if q["type"] == "E05":
                    q["vi"] = en_row["vi"]
                else:
                    expl = en_row["vi"]
        if q.get("level") is None:
            q["level"] = gp_level.get(q.get("gp"), 2)
        if "opts" in q and q["type"] != "E05":
            if not block:
                block = [0, 1, 2, 3]
                rng.shuffle(block)
            target = block.pop() % len(q["opts"])
            opts = q["opts"]
            correct = opts[0]
            others = opts[1:]
            random.Random(q["id"]).shuffle(others)
            others.insert(target, correct)
            q["opts"] = others
            q["ans"] = target
            mc_index += 1
        if expl:
            vi["q"][q["id"]] = expl
        if q.get("vi"):
            vi.setdefault("q_translation", {})[q["id"]] = q.pop("vi")
        if has_vi(q.get("stem", "")):
            vi.setdefault("q_notes", {})[q["id"]] = q["stem"]
            q["stem"] = re.sub(r"^\([^)]*[áạảãấầẩẫậắằẳẵặđẹẻẽếềểễệỉĩịọỏõốồổỗộớờởỡợụủũứừửữựỳỷỹỵ][^)]*\)\s*", "", q["stem"]).strip()
        if q.get("fix"):
            legacy_fix = q["fix"]
            if has_vi(legacy_fix):
                vi.setdefault("q_fix", {})[q["id"]] = legacy_fix
                q["fix"] = re.sub(r"\s*\([^()]*[áạảãấầẩẫậắằẳẵặđẹẻẽếềểễệỉĩịọỏõốồổỗộớờởỡợụủũứừửữựỳỷỹỵ][^()]*\)", "", legacy_fix).strip()
        if q.get("type") == "E04" and q["id"] not in vi.get("q_fix", {}):
            vi.setdefault("q_fix", {})[q["id"]] = q.get("fix", "")
        q = {k: v for k, v in q.items() if v not in (None, [], "")}
        if q["type"] == "E04":
            q["ans"] = q.get("ans", 0)
        questions.append(q)

    # Tier is derived from the exact audit rules at generation time. This keeps
    # the lesson/dictionary boundary reproducible and never creates gold data.
    import audit_content
    ranks = audit_content.ngsl_ranks()
    easy = {k for k, r in ranks.items() if r <= 2000}
    topic_size = {}
    for w in words:
        for topic in w.get("topics") or []:
            topic_size[topic] = topic_size.get(topic, 0) + 1
    audit_content.prepare(words, lambda w: vi["words"].get(w["id"]))
    for w in words:
        errors, _, _ = audit_content.audit_word(w, vi["words"].get(w["id"]), ranks, easy, topic_size)
        w["tier"] = "bronze" if errors else "silver"

    passages = []
    for p in lib.PASSAGES:
        p = dict(p)
        if p.get("topic"):
            p["topic"] = LEGACY_TOPIC_MAP.get(p["topic"], p["topic"])
        vi["passages"][p["id"]] = {"blanks": []}
        blanks = []
        for i, b in enumerate(p["blanks"]):
            b = dict(b)
            vi["passages"][p["id"]]["blanks"].append(b.pop("_vi"))
            b["expl"] = QUESTION_EXPL_EN[f"{p['id']}#{i}"]["expl"]
            correct = b["opts"][0]
            others = b["opts"][1:]
            random.Random(p["id"] + str(i)).shuffle(others)
            target = random.Random(p["id"] + "pos" + str(i)).randrange(4)
            others.insert(target, correct)
            b["opts"] = others
            b["ans"] = target
            blanks.append(b)
        p["blanks"] = blanks
        passages.append(p)

    out_en = os.path.join(ROOT, "content", "en")
    out_vi = os.path.join(ROOT, "content", "i18n", "vi")
    os.makedirs(out_en, exist_ok=True)
    os.makedirs(out_vi, exist_ok=True)

    # Keep the English corpus globally reusable.  Authoring examples may contain
    # local names or places from older exercises; neutralize those labels in the
    # generated English assets while leaving the Vietnamese teaching notes intact.
    def neutralize_local_names(value):
        if isinstance(value, str):
            replacements = (
                (r"\bHo Chi Minh City\b", "a large city"),
                (r"\bDa Nang\b", "a coastal city"),
                (r"\bHaiduong\b", "a city"),
                (r"\bVietnam\b", "the country"),
                (r"\bHanoi\b", "the capital city"),
                (r"\bSaigon\b", "the southern city"),
                (r"\bHue\b", "the old city"),
                (r"\bLan\b", "Alex"),
                (r"\bMai\b", "Sam"),
            )
            for pattern, replacement in replacements:
                value = re.sub(pattern, replacement, value)
            return value
        if isinstance(value, list):
            return [neutralize_local_names(item) for item in value]
        if isinstance(value, dict):
            return {key: neutralize_local_names(item) for key, item in value.items()}
        return value

    topics = neutralize_local_names(topics)
    words = neutralize_local_names(words)
    conf = neutralize_local_names(conf)
    grammar = neutralize_local_names(grammar)
    questions = neutralize_local_names(questions)
    passages = neutralize_local_names(passages)

    def dump(path, obj):
        if "--check" in sys.argv:
            with open(path, encoding="utf-8") as f:
                if json.load(f) != obj:
                    raise SystemExit(f"Generated content differs from {path}")
            return
        with open(path, "w", encoding="utf-8") as f:
            json.dump(obj, f, ensure_ascii=False, separators=(",", ":"))

    dump(os.path.join(out_en, "words.json"), dict(topics=topics, words=words, confusables=conf))
    dump(os.path.join(out_en, "grammar.json"), dict(points=grammar))
    dump(os.path.join(out_en, "questions.json"), dict(questions=questions, passages=passages))
    # A small, reusable relation index keeps learning features independent from the word card UI.
    # It deliberately records only relations supported by authored data; no homophone is inferred.
    relations = {
        "version": 1,
        "families": [
            {"wordId": w["id"], "forms": w.get("family", {})}
            for w in words if w.get("family")
        ],
        "confusableSets": [
            {"id": c["id"], "wordIds": c["wordIds"], "kind": "meaning_or_form", "needs_review": True}
            for c in conf
        ],
        "polysemy": [
            {"wordId": w["id"], "senseCount": len(w.get("senses", [])), "needs_review": bool(w.get("needs_review"))}
            for w in words if len(w.get("senses", [])) > 1
        ],
        "pronunciation": {
            "homophones": [],
            "nearHomophones": [],
            "note": "Empty until a pronunciation editor verifies each pair; do not infer from spelling."
        }
    }
    dump(os.path.join(out_en, "relations.json"), relations)
    dump(os.path.join(out_vi, "topics.json"), vi.get("topics", {}))
    locale_words = {}
    english_by_id = {word["id"]: word for word in words}
    for word_id, word in vi.get("words", {}).items():
        english_senses = english_by_id.get(word_id, {}).get("senses", [])
        for index, sense in enumerate(word.get("senses", [])):
            value = dict(sense)
            example_ids = list(value.get("ex", {}).keys())
            sense_id = example_ids[0] if example_ids else english_senses[index]["id"]
            if word.get("tip"):
                value["tip"] = word["tip"]
            locale_words[sense_id] = value
    dump(os.path.join(out_vi, "words.json"), locale_words)
    dump(os.path.join(out_vi, "confusables.json"), vi.get("conf", {}))
    dump(os.path.join(out_vi, "grammar.json"), vi.get("grammar", {}))
    dump(os.path.join(out_vi, "questions.json"), {
        "q": vi.get("q", {}), "q_fix": vi.get("q_fix", {}),
        "q_translation": vi.get("q_translation", {}), "q_notes": vi.get("q_notes", {}),
        "passages": vi.get("passages", {}), "quarantine": vi.get("quarantine", {}),
    })
    dump(os.path.join(out_vi, "pet.json"), vi.get("pet", {}))
    dump(os.path.join(out_vi, "tips.json"), vi.get("tips", {}))
    ui_path = os.path.join(out_vi, "ui.json")
    old_ui = {}
    if os.path.isfile(ui_path):
        with open(ui_path, encoding="utf-8") as f:
            old_ui = json.load(f)
    strings_path = os.path.join(ROOT, "app", "src", "main", "res", "values", "strings.xml")
    ui = {}
    if os.path.isfile(strings_path):
        for node in ET.parse(strings_path).getroot().findall("string"):
            ui[node.attrib["name"]] = old_ui.get(node.attrib["name"], "")
    dump(ui_path, ui)
    dump(os.path.join(out_vi, "l1_notes.json"), {})
    status_path = os.path.join(out_vi, "status.json")
    old_status = {}
    if os.path.isfile(status_path):
        with open(status_path, encoding="utf-8") as f:
            old_status = json.load(f)
    refresh_vi_status(status_path, old_status, {
        "topics": {x["id"]: x for x in topics},
        "words": {s["id"]: s for w in words for s in w.get("senses", [])},
        "confusables": {x["id"]: x for x in conf},
        "grammar": {x["id"]: x for x in grammar},
        "questions": {x["id"]: x for x in questions},
        "passages": {x["id"]: x for x in passages},
        "ui": {node.attrib["name"]: "".join(node.itertext()) for node in ET.parse(strings_path).getroot().findall("string")} if os.path.isfile(strings_path) else {},
        "pet": vi.get("pet", {}),
        "tips": vi.get("tips", {}),
    })
    print(f"topics={len(topics)} words={len(words)} confusables={len(conf)} grammar={len(grammar)} "
          f"questions={len(questions)} passages={len(passages)} petMoods={len(pet)}")


if __name__ == "__main__":
    main()
