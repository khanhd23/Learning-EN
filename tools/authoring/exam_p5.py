"""Original Part 5-style items (incomplete sentences). Correct option first. Never copied from real tests."""
from lib import P5

# ---- Word form / part of speech
P5("Ms. Rivera was praised for her ___ handling of the client's complaint.", ["professional", "profession", "professionally", "professionalism"], "pos", "Trước danh từ handling → tính từ professional.", "office", 3, skills=["pos_transform"], trap=["wrong_pos"])
P5("The new software allows employees to work more ___.", ["efficiently", "efficient", "efficiency", "efficiencies"], "pos", "Bổ nghĩa cho động từ work → trạng từ efficiently.", "office", 3, skills=["pos_transform"], trap=["wrong_pos"])
P5("All visitors must show proper ___ at the front desk.", ["identification", "identify", "identifiable", "identified"], "pos", "Sau tính từ proper → danh từ identification.", "office", 3, skills=["pos_transform"])
P5("The marketing team has ___ improved the company's online presence.", ["significantly", "significant", "significance", "signify"], "pos", "Giữa has và V3 → trạng từ.", "marketing", 3, skills=["pos_transform"])
P5("Our hotel is ___ located near the airport and the city center.", ["conveniently", "convenient", "convenience", "convene"], "pos", "Bổ nghĩa cho V3 located → trạng từ.", "hospitality", 3, skills=["pos_transform"])
P5("The director expressed her ___ to all the volunteers.", ["appreciation", "appreciate", "appreciative", "appreciatively"], "pos", "Sau tính từ sở hữu her → danh từ.", "events", 3, skills=["pos_transform"])
P5("Please read the safety instructions ___ before using the machine.", ["carefully", "careful", "care", "cared"], "pos", "Bổ nghĩa cho động từ read → trạng từ.", "logistics", 2, skills=["pos_transform"])
P5("The training session was so ___ that many staff asked for another one.", ["informative", "inform", "information", "informatively"], "pos", "so + tính từ + that → informative.", "hr", 3, skills=["pos_transform"])
P5("Mr. Ito is ___ for checking all invoices before payment.", ["responsible", "responsibility", "responsibly", "respond"], "pos", "be responsible for = chịu trách nhiệm.", "accounting", 3, skills=["pos_transform", "collocation"])
P5("The company's ___ to quality has earned it many loyal customers.", ["commitment", "commit", "committed", "committing"], "pos", "Sau sở hữu cách company's → danh từ.", "sales", 4, skills=["pos_transform"])
P5("Applicants should have ___ communication skills.", ["excellent", "excel", "excellently", "excellence"], "pos", "Trước danh từ communication skills → tính từ.", "hr", 2, skills=["pos_transform"])
P5("The sales figures were ___ higher than last year.", ["considerably", "considerable", "consider", "consideration"], "pos", "Bổ nghĩa cho tính từ so sánh higher → trạng từ.", "finance", 4, skills=["pos_transform"])
P5("We are ___ to announce the opening of our new branch.", ["pleased", "pleasing", "pleasure", "pleasantly"], "pos", "be pleased to + V = vui mừng.", "events", 3, skills=["pos_transform"])
P5("The museum offers free ___ on the first Sunday of every month.", ["admission", "admit", "admitted", "admissible"], "pos", "Sau tính từ free → danh từ admission (vé vào cửa).", "travel", 4, skills=["pos_transform"])
P5("Our technicians will respond ___ to any service request.", ["promptly", "prompt", "prompted", "promptness"], "pos", "Bổ nghĩa cho động từ respond → trạng từ.", "tech", 3, skills=["pos_transform"])
P5("The proposal was rejected because it was not financially ___.", ["feasible", "feasibly", "feasibility", "feasibleness"], "pos", "Sau trạng từ financially và to be → tính từ.", "finance", 5, skills=["pos_transform"])
P5("Ms. Nguyen is one of the most ___ designers in the company.", ["creative", "create", "creation", "creatively"], "pos", "the most + tính từ + danh từ → creative.", "marketing", 2, skills=["pos_transform"])
P5("The ___ of the new factory will create 500 jobs.", ["construction", "construct", "constructive", "constructed"], "pos", "The ... of → danh từ.", "logistics", 3, skills=["pos_transform"])
P5("Employees must complete the form ___ and return it to HR.", ["accurately", "accurate", "accuracy", "accurateness"], "pos", "Bổ nghĩa cho complete → trạng từ.", "hr", 3, skills=["pos_transform"])
P5("The CEO gave a very ___ speech at the annual dinner.", ["inspiring", "inspired", "inspire", "inspiration"], "pos", "Bài phát biểu gây cảm hứng (tính chất) → inspiring.", "events", 3, skills=["pos_transform"])
P5("We were ___ by the high quality of the samples.", ["impressed", "impressive", "impress", "impression"], "pos", "Cảm xúc của người (We) → impressed.", "commerce", 3, skills=["pos_transform"])
P5("The new parking rules will become ___ on July 1.", ["effective", "effect", "effectively", "effectiveness"], "pos", "become effective = có hiệu lực.", "legal", 4, skills=["pos_transform", "collocation"])

# ---- Verb tense / voice
P5("The board of directors ___ the merger at next week's meeting.", ["will discuss", "discussed", "has discussed", "discussing"], "tense", "next week → tương lai.", "office", 3, skills=["tense"], trap=["wrong_tense"])
P5("Since joining the company, Mr. Ahmed ___ three major projects.", ["has managed", "manages", "managed", "will manage"], "tense", "Since + V-ing (mốc) → hiện tại hoàn thành.", "hr", 3, skills=["tense"])
P5("The package ___ to the wrong address yesterday.", ["was sent", "sent", "has been sent", "is sending"], "tense", "yesterday + bị động → was sent.", "logistics", 3, skills=["tense", "passive"])
P5("Currently, our engineers ___ a new mobile application.", ["are developing", "developed", "have developed", "develop"], "tense", "Currently → hiện tại tiếp diễn.", "tech", 3, skills=["tense"])
P5("The conference room ___ for the training session tomorrow.", ["has been reserved", "reserves", "reserving", "has reserved"], "tense", "Phòng ĐƯỢC đặt (bị động); việc đặt đã xong → has been reserved.", "events", 4, skills=["passive"])
P5("By the end of this year, the company ___ 20 new stores.", ["will have opened", "opens", "opened", "has opened"], "tense", "By the end of + tương lai → tương lai hoàn thành.", "commerce", 5, skills=["tense"])
P5("Last quarter, profits ___ by nearly 10 percent.", ["rose", "rise", "have risen", "will rise"], "tense", "Last quarter → quá khứ đơn: rise → rose.", "finance", 3, skills=["tense"])
P5("Ms. Lopez usually ___ the weekly sales report on Fridays.", ["prepares", "prepare", "is preparing", "prepared"], "tense", "usually + chủ ngữ số ít → prepares.", "sales", 2, skills=["tense", "agreement"])
P5("All orders ___ within two business days.", ["are processed", "process", "processing", "have processing"], "tense", "Đơn hàng ĐƯỢC xử lý → bị động.", "sales", 3, skills=["passive"])
P5("When the fire alarm rang, the employees ___ a meeting.", ["were having", "have", "are having", "had had"], "tense", "Hành động đang diễn ra thì bị cắt ngang → quá khứ tiếp diễn.", "office", 4, skills=["tense"])
P5("The new policy ___ by the board last Monday.", ["was approved", "approved", "has approved", "approves"], "tense", "Chính sách ĐƯỢC phê duyệt, last Monday → was approved.", "legal", 3, skills=["passive"])
P5("Please make sure that the lights ___ off before you leave.", ["are turned", "turn", "turning", "have turn"], "tense", "Đèn được tắt → bị động are turned off.", "office", 3, skills=["passive"])
P5("Mr. Park ___ for the company for over twenty years when he retired.", ["had worked", "has worked", "works", "is working"], "tense", "Trước một mốc quá khứ (when he retired) → quá khứ hoàn thành.", "hr", 4, skills=["tense"])
P5("If sales ___ to grow, we will hire more staff.", ["continue", "continued", "will continue", "would continue"], "tense", "Điều kiện loại 1: If + hiện tại đơn.", "sales", 3, skills=["conditional"])

# ---- Prepositions
P5("The quarterly meeting will be held ___ the third floor.", ["on", "in", "at", "to"], "preposition", "Tầng (floor) → on the third floor.", "office", 2, skills=["preposition"], trap=["wrong_preposition"])
P5("Payment is due ___ receipt of the invoice.", ["upon", "into", "among", "toward"], "preposition", "upon receipt = ngay khi nhận được.", "accounting", 5, skills=["preposition", "collocation"])
P5("The store will be closed ___ the holiday period.", ["throughout", "between", "along", "beneath"], "preposition", "throughout + khoảng thời gian = suốt.", "commerce", 4, skills=["preposition"])
P5("Ms. Lee has worked in sales ___ 2015.", ["since", "for", "during", "from"], "preposition", "Mốc thời gian → since.", "sales", 2, skills=["preposition"])
P5("The report must be submitted ___ Friday at 5 p.m.", ["by", "until", "in", "within"], "preposition", "Hạn chót (không muộn hơn) → by.", "office", 3, skills=["preposition"], trap=["wrong_preposition"])
P5("The bus stop is located directly ___ the hotel entrance.", ["opposite", "among", "through", "above of"], "preposition", "đối diện → opposite.", "travel", 3, skills=["preposition"])
P5("The museum is open daily ___ 9 a.m. to 6 p.m.", ["from", "since", "between", "at"], "preposition", "from ... to ...", "travel", 2, skills=["preposition"])
P5("Refunds will be issued ___ 14 days of the return.", ["within", "until", "since", "among"], "preposition", "within + khoảng thời gian = trong vòng.", "sales", 3, skills=["preposition"])
P5("The training is mandatory for all staff, ___ managers.", ["including", "include", "included", "inclusion"], "preposition", "including = bao gồm (giới từ).", "hr", 3, skills=["preposition"])
P5("The meeting was postponed ___ the director's illness.", ["because of", "because", "although", "unless"], "preposition", "Sau chỗ trống là cụm danh từ → because of.", "office", 3, skills=["conjunction"])
P5("The lecture was interesting, ___ a bit too long.", ["albeit", "despite", "because", "unless"], "preposition", "albeit = mặc dù (+ tính từ/cụm), văn trang trọng.", "education", 5, skills=["conjunction"])
P5("Customers can pay ___ cash or by credit card.", ["in", "at", "on", "to"], "preposition", "pay in cash (cụm cố định), pay by card.", "commerce", 3, skills=["preposition", "collocation"])
P5("Ms. Tran is responsible ___ training new employees.", ["for", "of", "to", "with"], "preposition", "responsible for.", "hr", 2, skills=["preposition", "collocation"])
P5("The price of the tour includes meals and accommodation, but not ___ insurance.", ["travel", "traveling", "traveled", "travels"], "pos", "travel insurance là danh từ ghép.", "travel", 3, skills=["collocation"])
P5("We apologize ___ any inconvenience this may cause.", ["for", "about", "of", "with"], "preposition", "apologize for.", "sales", 2, skills=["preposition", "collocation"])
P5("Please reply ___ this email by Thursday.", ["to", "for", "at", "on"], "preposition", "reply to.", "office", 2, skills=["preposition", "collocation"])

# ---- Conjunctions / connectors
P5("___ the weather was bad, the outdoor concert went ahead as planned.", ["Although", "Despite", "Because", "Unless"], "conj_prep", "Mệnh đề + nghĩa đối lập → Although.", "events", 3, skills=["conjunction"])
P5("You will not be able to enter the building ___ you show your ID card.", ["unless", "if", "because", "so that"], "conj_prep", "unless = nếu không.", "office", 3, skills=["conjunction"])
P5("Ms. Brown will lead the meeting ___ Mr. Davis is on vacation.", ["while", "during", "despite", "so"], "conj_prep", "Sau chỗ trống là mệnh đề → while.", "office", 3, skills=["conjunction"])
P5("Both the manager ___ her assistant attended the conference.", ["and", "or", "nor", "but"], "conj_prep", "both ... and.", "events", 2, skills=["conjunction"])
P5("Please arrive early ___ we can start the meeting on time.", ["so that", "because of", "unless", "despite"], "conj_prep", "so that = để mà (chỉ mục đích).", "office", 3, skills=["conjunction"])
P5("Neither the printer ___ the scanner is working today.", ["nor", "or", "and", "but"], "conj_prep", "neither ... nor.", "tech", 3, skills=["conjunction"])
P5("The shop offers free delivery ___ the order is over 500,000 dong.", ["if", "unless", "although", "whereas"], "conj_prep", "Điều kiện → if.", "commerce", 2, skills=["conjunction"])
P5("___ submitting the form, check that all information is correct.", ["Before", "Until", "Unless", "Whereas"], "conj_prep", "Before + V-ing = trước khi.", "office", 3, skills=["conjunction"])
P5("The first design was simple, ___ the second one was more detailed.", ["whereas", "despite", "because of", "so that"], "conj_prep", "whereas = trong khi đó (đối lập hai mệnh đề).", "marketing", 4, skills=["conjunction"])
P5("___ you have any questions, please contact our support team.", ["If", "Unless", "Although", "Whether"], "conj_prep", "Điều kiện → If.", "sales", 2, skills=["conjunction"])
P5("The project was completed on time ___ several unexpected problems.", ["in spite of", "although", "even though", "so"], "conj_prep", "Sau chỗ trống là cụm danh từ → in spite of.", "office", 3, skills=["conjunction"])
P5("Not only did the hotel upgrade our room, ___ it also gave us free breakfast.", ["but", "and", "so", "or"], "conj_prep", "Not only ... but (also).", "hospitality", 4, skills=["conjunction"])
P5("We will not start the meeting ___ everyone has arrived.", ["until", "by", "since", "during"], "conj_prep", "not ... until = mãi đến khi.", "office", 3, skills=["conjunction"])
P5("___ the hotel nor the restaurant accepts credit cards.", ["Neither", "Either", "Both", "Not"], "conj_prep", "Neither ... nor.", "hospitality", 3, skills=["conjunction"])

# ---- Pronouns
P5("Ms. Kim asked that all reports be sent directly to ___.", ["her", "she", "hers", "herself"], "pronoun", "Sau giới từ to → tân ngữ her.", "office", 2, skills=["pronoun"])
P5("The employees organized the farewell party by ___.", ["themselves", "them", "their", "theirs"], "pronoun", "by themselves = tự họ.", "hr", 3, skills=["pronoun"])
P5("Mr. Chen forgot ___ laptop in the meeting room.", ["his", "him", "he", "himself"], "pronoun", "Trước danh từ laptop → tính từ sở hữu his.", "office", 2, skills=["pronoun"])
P5("Our products are cheaper than ___ of our competitors.", ["those", "that", "them", "these"], "pronoun", "those thay cho danh từ số nhiều products đã nhắc.", "sales", 4, skills=["pronoun", "comparison"])
P5("___ who wish to join the tour should sign up at the front desk.", ["Those", "They", "Them", "Their"], "pronoun", "Those who = những người mà.", "travel", 4, skills=["pronoun"])
P5("The company celebrated ___ 50th anniversary last month.", ["its", "it's", "their", "it"], "pronoun", "Sở hữu của company (số ít) → its.", "events", 2, skills=["pronoun"])
P5("The guests said they enjoyed ___ stay at our hotel.", ["their", "them", "they", "theirs"], "pronoun", "Trước danh từ stay → tính từ sở hữu their.", "hospitality", 3, skills=["pronoun"])
P5("Mr. Diaz prefers to review the contracts ___ before signing them.", ["himself", "he", "his", "him"], "pronoun", "Nhấn mạnh tự mình → himself.", "legal", 3, skills=["pronoun"])

# ---- Vocabulary in context
P5("Please ___ the attached file for the meeting agenda.", ["refer to", "look", "tell", "speak"], "vocab", "refer to = tham khảo, xem.", "office", 3, skills=["vocab_workplace", "collocation"])
P5("The hotel offers a free airport ___ service for all guests.", ["shuttle", "flight", "ticket", "passenger"], "vocab", "airport shuttle = xe đưa đón sân bay.", "hospitality", 3, skills=["vocab_workplace"])
P5("Due to high ___, the new phone sold out in two days.", ["demand", "supply", "request", "order"], "vocab", "high demand = nhu cầu cao.", "commerce", 3, skills=["vocab_workplace", "collocation"])
P5("All employees must ___ the new safety regulations.", ["comply with", "agree to", "follow up", "deal"], "vocab", "comply with regulations = tuân thủ quy định.", "legal", 4, skills=["vocab_workplace", "collocation"])
P5("The company will ___ all travel expenses for the conference.", ["reimburse", "refund to", "repay back", "borrow"], "vocab", "reimburse expenses = hoàn trả chi phí.", "accounting", 4, skills=["vocab_workplace"])
P5("The job ___ was posted on the company website last week.", ["opening", "open", "openly", "opener"], "vocab", "job opening = vị trí tuyển dụng.", "hr", 3, skills=["vocab_workplace", "collocation"])
P5("We need to ___ the deadline because the materials arrived late.", ["extend", "expand", "expire", "exceed"], "vocab", "extend a deadline = gia hạn.", "office", 4, skills=["vocab_workplace", "collocation"])
P5("The warranty ___ all repairs for two years.", ["covers", "pays out", "keeps", "holds up"], "vocab", "warranty covers = bảo hành bao gồm.", "sales", 3, skills=["vocab_workplace"])
P5("Ms. Ortiz was ___ to regional manager after five years.", ["promoted", "advanced to", "raised", "lifted"], "vocab", "be promoted to = được thăng chức lên.", "hr", 3, skills=["vocab_workplace", "collocation"])
P5("Our customer service team is ___ 24 hours a day.", ["available", "possible", "capable", "able"], "vocab", "available = sẵn sàng phục vụ.", "sales", 2, skills=["vocab_workplace"])
P5("Please ___ a table for six at 7 p.m.", ["reserve", "keep up", "hold on", "save up"], "vocab", "reserve a table = đặt bàn.", "hospitality", 2, skills=["vocab_workplace", "collocation"])
P5("The flight was ___ because of heavy fog.", ["delayed", "lately", "slowed down", "postponed to"], "vocab", "The flight was delayed = bị hoãn/trễ.", "travel", 2, skills=["vocab_workplace"])
P5("The manager asked us to ___ our ideas at the next meeting.", ["present", "presence", "presently", "presenter"], "pos", "asked us to + V → present.", "office", 3, skills=["pos_transform"])
P5("To ___ for the discount, customers must spend at least $50.", ["qualify", "quality", "qualified", "qualification"], "vocab", "qualify for = đủ điều kiện nhận.", "commerce", 4, skills=["vocab_workplace", "collocation"])
P5("The new manager quickly ___ a good relationship with her team.", ["established", "estimated", "examined", "excused"], "vocab", "establish a relationship = xây dựng mối quan hệ.", "hr", 4, skills=["vocab_workplace", "collocation"])
P5("Interested candidates should ___ their résumés by email.", ["submit", "admit", "commit", "permit"], "vocab", "submit a résumé = nộp hồ sơ.", "hr", 3, skills=["vocab_workplace"], trap=["spelling_lookalike"])
P5("The factory is running at full ___ to meet the holiday orders.", ["capacity", "ability", "capability", "volume up"], "vocab", "at full capacity = hết công suất.", "logistics", 4, skills=["vocab_workplace", "collocation"])
P5("We will ___ you as soon as your order is ready.", ["notify", "notice", "note", "notation"], "vocab", "notify sb = thông báo cho ai.", "sales", 3, skills=["vocab_workplace"], trap=["spelling_lookalike"])
P5("Please keep your receipt as ___ of purchase.", ["proof", "prove", "proven", "proofing"], "vocab", "proof of purchase = bằng chứng mua hàng.", "commerce", 3, skills=["vocab_workplace", "collocation"])
P5("The company plans to ___ its business into three new markets.", ["expand", "extend to", "expend", "explain"], "vocab", "expand into = mở rộng sang.", "marketing", 4, skills=["vocab_workplace"], trap=["spelling_lookalike"])
P5("Staff are ___ to wear their ID badges at all times.", ["required", "requested of", "requiring", "requirement"], "vocab", "be required to = bắt buộc phải.", "office", 3, skills=["vocab_workplace"])
P5("The concert was canceled, and all ticket holders will receive a full ___.", ["refund", "return", "repay", "reward"], "vocab", "a full refund = hoàn tiền toàn bộ.", "events", 3, skills=["vocab_workplace", "collocation"])
P5("Our sales ___ have increased by 20 percent.", ["figures", "numbers of", "amounts of", "counts"], "vocab", "sales figures = số liệu doanh số.", "finance", 3, skills=["vocab_workplace", "collocation"])
P5("The hotel was fully ___, so we had to stay somewhere else.", ["booked", "reserved for", "occupied by", "filled in"], "vocab", "fully booked = kín phòng.", "hospitality", 3, skills=["vocab_workplace", "collocation"])
P5("The two companies have agreed to ___ on the new project.", ["collaborate", "collect", "collapse", "collide"], "vocab", "collaborate on = hợp tác.", "office", 4, skills=["vocab_workplace"], trap=["spelling_lookalike"])
P5("The CEO will ___ the opening speech at the conference.", ["deliver", "say", "talk", "tell"], "vocab", "deliver a speech = có bài phát biểu.", "events", 4, skills=["vocab_workplace", "collocation"])
P5("Please ___ from smoking inside the building.", ["refrain", "avoid", "stop to", "prevent"], "vocab", "refrain from + V-ing = kiềm chế, không làm.", "office", 5, skills=["vocab_workplace", "collocation"])
P5("The bank will ___ a fee for international transfers.", ["charge", "pay", "cost", "spend"], "vocab", "charge a fee = tính phí.", "finance", 3, skills=["vocab_workplace", "collocation"])

# ---- Comparison / quantifiers / relative / gerund
P5("This year's conference attracted ___ participants than last year's.", ["more", "most", "much", "many"], "comparison", "So sánh hơn với than → more.", "events", 2, skills=["comparison"])
P5("This is the ___ sales result in the company's history.", ["best", "better", "good", "well"], "comparison", "the + so sánh nhất → best.", "sales", 2, skills=["comparison"])
P5("The new model is ___ lighter than the old one.", ["much", "very", "more", "most"], "comparison", "Nhấn mạnh so sánh hơn → much lighter.", "tech", 3, skills=["comparison"])
P5("___ employee will receive a bonus this year.", ["Every", "All", "Many", "Several"], "quantifier", "Every + danh từ số ít.", "hr", 2, skills=["article_quantifier"])
P5("There were ___ complaints about the new menu.", ["few", "little", "much", "a little"], "quantifier", "complaints đếm được → few.", "hospitality", 3, skills=["article_quantifier"])
P5("The candidate ___ we interviewed yesterday has accepted the offer.", ["whom", "whose", "which", "what"], "relative", "Người làm tân ngữ → whom (hoặc who/that).", "hr", 4, skills=["relative_clause"])
P5("The building ___ the conference will be held has a large parking lot.", ["where", "which", "who", "whose"], "relative", "Nơi chốn (= in which) → where.", "events", 3, skills=["relative_clause"])
P5("Thank you for ___ our survey.", ["completing", "complete", "completed", "to complete"], "gerund", "Sau giới từ for → V-ing.", "marketing", 2, skills=["gerund_infinitive"])
P5("We look forward to ___ with you again.", ["working", "work", "worked", "be working"], "gerund", "look forward to + V-ing.", "office", 3, skills=["gerund_infinitive"])
P5("The manager decided ___ the event until next month.", ["to postpone", "postponing", "postpone", "postponed"], "gerund", "decide + to V.", "events", 3, skills=["gerund_infinitive"])
P5("Customers who wish ___ their orders should call before noon.", ["to change", "changing", "change", "changed"], "gerund", "wish + to V.", "sales", 3, skills=["gerund_infinitive"])
P5("___ the end of the year, all employees will receive a gift card.", ["At", "In", "On", "By the"], "preposition", "At the end of = vào cuối.", "hr", 2, skills=["preposition"])
P5("The marketing manager, ___ ideas helped increase sales, will speak first.", ["whose", "who", "which", "whom"], "relative", "Ý tưởng CỦA cô ấy → whose.", "marketing", 4, skills=["relative_clause"])
P5("Please inform us ___ any changes to your address.", ["of", "for", "at", "with"], "preposition", "inform sb of sth.", "office", 3, skills=["preposition", "collocation"])
P5("The software update is ___ to fix the login problem.", ["expected", "expecting", "expectation", "expectant"], "vocab", "be expected to = được kỳ vọng sẽ.", "tech", 3, skills=["pos_transform"])

QTYPE_TIPS = {
    "pos": "Nhìn VỊ TRÍ chỗ trống: sau mạo từ/sở hữu → danh từ; trước danh từ → tính từ; bổ nghĩa động từ/tính từ → trạng từ. Không cần dịch cả câu!",
    "tense": "Tìm dấu hiệu thời gian (since, by the time, last, next, currently) và kiểm tra chủ ngữ có tự làm hành động không (chủ động hay bị động).",
    "preposition": "Học giới từ theo CỤM (responsible for, reply to, by Friday). Với thời gian: at + giờ, on + ngày, in + tháng/năm.",
    "conj_prep": "Sau chỗ trống có động từ chia → LIÊN TỪ (although, because, while). Chỉ có cụm danh từ → GIỚI TỪ (despite, because of, during).",
    "pronoun": "Trước danh từ → tính từ sở hữu (his, its); sau động từ/giới từ → tân ngữ (him); đứng một mình → đại từ sở hữu (his, hers); tự mình → -self.",
    "vocab": "Đọc cả câu, tìm cụm từ đi kèm (collocation): meet a deadline, comply with, fully booked. Cẩn thận từ có hình thức giống nhau.",
    "comparison": "Có than → so sánh hơn; có the + nhóm/phạm vi (in, of) → so sánh nhất. much/far + so sánh hơn để nhấn mạnh.",
    "quantifier": "Danh từ đếm được: many/few; không đếm được: much/little. every/each + danh từ số ít.",
    "relative": "Người → who/whom, vật → which, sở hữu → whose, nơi chốn → where. Có dấu phẩy thì không dùng that.",
    "gerund": "Sau giới từ → V-ing. Nhớ nhóm động từ + to V (decide, plan, want) và + V-ing (enjoy, avoid, suggest).",
    "passive": "Chủ ngữ là vật chịu tác động và sau chỗ trống không có tân ngữ → nghĩ ngay đến bị động (be + V3).",
    "agreement": "Tìm chủ ngữ THẬT, bỏ qua cụm giới từ chen giữa. every/each/everyone → số ít.",
    "conditional": "Loại 1: If + hiện tại, will; loại 2: If + quá khứ, would; loại 3: If + had V3, would have V3. Should you... = If you...",
    "participle": "V-ing: chủ động; V3: bị động; Having V3: xảy ra trước.",
    "subjunctive": "suggest/recommend/require/It is essential that + S + V NGUYÊN MẪU.",
    "inversion": "Never/Rarely/Not only/Only after đứng đầu câu → đảo trợ động từ. Should you... = If you...",
    "linking": "Xác định quan hệ hai câu: đối lập (However), kết quả (Therefore), bổ sung (Moreover), điều kiện (Otherwise).",
    "noun_phrase": "Tính từ ghép số + danh từ không thêm -s (a two-week trip). Danh từ bổ nghĩa thường ở số ít.",
    "confusable": "Hai từ dễ nhầm: xem từ loại và cấu trúc đi kèm trước, rồi mới xét nghĩa.",
    "sentence": "Part 6: đọc câu trước VÀ câu sau chỗ trống; câu đúng phải nối mạch ý và dùng đúng từ tham chiếu (this, these, it).",
    "pronunciation": "Đuôi -ed: /ɪd/ sau t, d; /t/ sau âm vô thanh (p, k, f, s, ʃ, tʃ); /d/ còn lại. Đuôi -s/es: /ɪz/ sau s, z, ʃ, tʃ, dʒ; /s/ sau p, t, k, f, θ; /z/ còn lại.",
    "stress": "Danh từ/tính từ 2 âm tiết thường nhấn âm 1; động từ 2 âm tiết thường nhấn âm 2. Từ đuôi -tion, -ic, -ity: nhấn ngay trước đuôi.",
    "synonym": "Đọc cả câu để hiểu ngữ cảnh, rồi chọn từ có nghĩa GẦN NHẤT với từ gạch chân.",
    "antonym": "Tìm nghĩa của từ gạch chân trong ngữ cảnh, rồi chọn từ TRÁI nghĩa. Cẩn thận phương án là từ đồng nghĩa (bẫy).",
    "error": "Kiểm tra lần lượt: chia động từ, từ loại, giới từ, đại từ, so sánh. Lỗi thường nằm ở chỗ 'nghe quen tai' theo tiếng Việt.",
    "transform": "Câu viết lại phải giữ nguyên nghĩa, kể cả thì và mức độ chắc chắn. Loại các câu đổi nghĩa phủ định/khẳng định.",
}
