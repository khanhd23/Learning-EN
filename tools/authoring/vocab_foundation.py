"""Original foundation expansion; Vietnamese-first, pending human editorial review.

Levels are internal teaching estimates, not CEFR or exam-score equivalents.
The TSV is the editable source; gen_content.py compiles it with the existing banks.
"""
from pathlib import Path
import lib

SOURCE = "original:foundation-expansion-2026-10-04"
added = []
for line_number, line in enumerate(Path(__file__).with_suffix(".tsv").read_text(encoding="utf-8").splitlines(), 1):
    if not line or line.startswith("#"):
        continue
    fields = line.split("|")
    if line.startswith("@"):
        tid, title, hue = fields
        lib.topic(tid[1:], title, "📘", int(hue))
        continue
    if len(fields) != 8:
        raise ValueError(f"Foundation row {line_number}: expected eight columns")
    lemma, pos, ipa, level, gloss, example, translation, collocations = fields
    wid = lib.slug(lemma)
    if any(w["id"] == wid for w in lib.WORDS):
        raise ValueError(f"Foundation row {line_number}: duplicate word {wid}")
    lib.W(lemma, pos, ipa, int(level), gloss, example, translation, coll=collocations.split(";"))
    word = lib.WORDS[-1]
    word.update(needs_review=True, source=SOURCE)
    added.append(word)

# Add genuinely useful derived forms, not inflection lists counted as extra words.
families = {
    "decision": {"verb": "decide", "adj": "decisive"},
    "describe": {"noun": "description", "adj": "descriptive"},
    "explain": {"noun": "explanation"},
    "develop": {"noun": "development"},
    "achieve": {"noun": "achievement"},
    "responsibility": {"adj": "responsible", "adv": "responsibly"},
    "requirement": {"verb": "require"},
    "solution": {"verb": "solve"},
    "information": {"verb": "inform", "adj": "informative"},
    "discuss": {"noun": "discussion"},
    "manager": {"verb": "manage", "noun": "management"},
    "assistant": {"verb": "assist", "noun": "assistance"},
    "teacher": {"verb": "teach"},
    "learn": {"noun": "learner"},
    "read": {"noun": "reader"},
    "write": {"noun": "writer"},
    "choose": {"noun": "choice"},
    "understand": {"noun": "understanding"},
    "remember": {"noun": "remembrance"},
    "advice": {"verb": "advise", "noun": "adviser"},
    "advise": {"noun": "advice"},
    "economic": {"noun": "economy", "adv": "economically"},
    "economical": {"verb": "economize", "adv": "economically"},
    "different": {"noun": "difference", "verb": "differ", "adv": "differently"},
    "important": {"noun": "importance"},
    "possible": {"noun": "possibility", "adv": "possibly"},
    "impossible": {"noun": "impossibility"},
    "careful": {"adv": "carefully", "noun": "care"},
    "dangerous": {"noun": "danger", "adv": "dangerously"},
    "safe": {"noun": "safety", "adv": "safely"},
    "strong": {"noun": "strength", "verb": "strengthen"},
    "weak": {"noun": "weakness", "verb": "weaken"},
    "polite": {"adv": "politely", "noun": "politeness"},
    "rude": {"adv": "rudely", "noun": "rudeness"},
    "beautiful": {"noun": "beauty", "adv": "beautifully"},
    "kind": {"noun": "kindness", "adv": "kindly"},
    "easy": {"adv": "easily"},
    "difficult": {"noun": "difficulty"},
    "heavy": {"adv": "heavily"},
    "slow": {"adv": "slowly"},
    "help": {"adj": "helpful", "noun": "helper"},
    "use": {"adj": "useful", "noun": "user"},
}
for word in added:
    word["family"].update(families.get(word["id"], {}))

# Common additional senses keep one stable word ID and aligned Vietnamese examples.
extra_senses = [
    ("work", "v", "làm việc", "I work in a small office near my home.", "Tôi làm việc trong văn phòng nhỏ gần nhà."),
    ("travel", "n", "việc đi lại; việc du lịch (không đếm được)", "Air travel can be expensive during holidays.", "Đi lại bằng máy bay có thể tốn kém vào dịp lễ."),
    ("fun", "n", "niềm vui; sự vui vẻ (không đếm được)", "We had a lot of fun at the picnic.", "Chúng tôi rất vui trong buổi dã ngoại."),
    ("hard", "adv", "chăm chỉ; hết sức", "She works hard to improve her English.", "Cô ấy chăm chỉ học để cải thiện tiếng Anh."),
    ("hard", "adj", "cứng", "This bread is too hard to eat.", "Bánh mì này cứng quá, không ăn được."),
    ("late", "adj", "muộn; trễ", "I'm sorry I'm late for class.", "Em xin lỗi vì đến lớp muộn."),
    ("since", "conj", "kể từ khi (đi với mệnh đề)", "I've known her since we were children.", "Tôi biết cô ấy từ khi chúng tôi còn nhỏ."),
    ("for", "prep", "dành cho", "This gift is for my sister.", "Món quà này dành cho chị tôi."),
    ("light", "n", "ánh sáng", "There isn't enough light to read here.", "Ở đây không đủ ánh sáng để đọc."),
    ("letter", "n", "chữ cái", "The word 'cat' has three letters.", "Từ 'cat' có ba chữ cái."),
    ("glass", "n", "thủy tinh (vật liệu)", "This bowl is made of glass.", "Chiếc bát này làm bằng thủy tinh."),
    ("fish", "n", "thịt cá (thực phẩm)", "We had grilled fish with rice.", "Chúng tôi ăn cá nướng với cơm."),
    ("chicken", "n", "con gà", "The chickens are walking around the garden.", "Những con gà đang đi quanh vườn."),
    ("free", "adj", "miễn phí", "Entry to the museum is free on Sunday.", "Vào bảo tàng miễn phí vào Chủ nhật."),
    ("second", "adj", "thứ hai (thứ tự)", "Take the second street on the right.", "Rẽ vào con đường thứ hai bên phải."),
    ("change", "n", "sự thay đổi", "A small change can make a big difference.", "Một thay đổi nhỏ có thể tạo ra khác biệt lớn."),
    ("answer", "n", "câu trả lời; đáp án", "Write your answer below the question.", "Viết câu trả lời bên dưới câu hỏi."),
    ("plan", "v", "lên kế hoạch; dự định", "We plan to visit our grandparents this weekend.", "Chúng tôi dự định thăm ông bà cuối tuần này."),
]
for args in extra_senses:
    lib.SW(*args)

tips = {
    "say": "say + điều được nói; tell + người nghe. Dùng say hello nhưng tell me a story.",
    "hear": "hear là nghe thấy; listen to nhấn mạnh việc chủ động lắng nghe.",
    "look": "look at = hướng mắt nhìn; see = nhìn thấy; watch = theo dõi diễn biến.",
    "make": "make thường tạo ra kết quả: make a cake. do thường chỉ thực hiện việc: do homework.",
    "advice": "advice không đếm được: some advice hoặc a piece of advice; không dùng an advice.",
    "work": "work (công việc) không đếm được; job đếm được: much work nhưng many jobs.",
    "hardly": "hardly = hầu như không, khác hard = chăm chỉ trong work hard.",
    "its": "its là từ sở hữu đứng trước danh từ. it's là dạng rút gọn của it is hoặc it has.",
    "during": "during + cụm danh từ; while + mệnh đề: during class / while I study.",
    "although": "although + mệnh đề; despite + danh từ hoặc V-ing.",
    "because": "because + mệnh đề; because of + cụm danh từ.",
    "economic": "economic = thuộc kinh tế; economical = tiết kiệm chi phí hoặc tài nguyên.",
    "lately": "lately = gần đây; late = muộn. lately thường đi với thì hiện tại hoàn thành.",
    "alone": "alone nói về việc không có ai bên cạnh; lonely nói về cảm giác cô đơn.",
    "rise": "rise không có tân ngữ: Prices rise. raise có tân ngữ: They raise prices.",
    "specially": "specially nhấn mạnh mục đích riêng; especially nhấn mạnh một trường hợp nổi bật.",
}
for word in added:
    if word["id"] in tips:
        word["_vi"]["tip"] = tips[word["id"]]
