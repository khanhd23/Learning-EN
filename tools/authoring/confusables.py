from lib import CONF, C

K = dict(fmt="vocab", gp=None, skills=["confusable"], trap=["similar_meaning"])


def items(*qs):
    for stem, opts, expl, lvl in qs:
        C(stem, opts, expl, level=lvl, qtype="confusable", **K)


CONF("affect_effect", ["affect", "effect"],
     "affect thường là ĐỘNG TỪ (tác động đến), effect thường là DANH TỪ (tác động, hiệu quả). Mẹo: Affect = Action.",
     {"affect": "(v) ảnh hưởng đến: Stress affects sleep.", "effect": "(n) tác động: Stress has an effect on sleep."})
items(("Lack of sleep can ___ your memory.", ["affect", "effect", "effective", "affection"], "Sau 'can' cần động từ nguyên mẫu → affect (ảnh hưởng đến).", 3),
      ("The new rule had a positive ___ on sales.", ["effect", "affect", "effective", "affected"], "Sau 'a positive' cần danh từ → effect (tác động).", 3))

CONF("borrow_lend", ["borrow", "lend"],
     "borrow = MƯỢN (lấy về mình); lend = CHO MƯỢN (đưa cho người khác). borrow sth FROM sb, lend sb sth.",
     {"borrow": "mượn: Can I borrow your pen?", "lend": "cho mượn: Can you lend me your pen?"})
items(("Could you ___ me 50,000 dong until tomorrow?", ["lend", "borrow", "loan of", "owe"], "lend sb sth = cho ai mượn cái gì. 'borrow' không đi với 'me' theo cấu trúc này.", 2),
      ("I ___ a book from the library every week.", ["borrow", "lend", "lent", "rent out"], "borrow sth from = mượn từ đâu đó.", 2))

CONF("say_tell", ["say", "tell"],
     "tell + NGƯỜI (tell me, tell her); say + điều được nói (say hello, say that...). Không dùng 'say me'.",
     {"say": "say something (to sb)", "tell": "tell somebody something"})
items(("Can you ___ me the way to the station?", ["tell", "say", "speak", "talk"], "tell + tân ngữ chỉ người (me) + điều gì.", 1),
      ("She ___ that she was tired.", ["said", "told", "spoke", "talked"], "say that + mệnh đề; 'told' cần tân ngữ chỉ người (told me that...).", 2))

CONF("make_do", ["make", "do"],
     "make = tạo ra cái gì mới (make a cake, make a decision, make a mistake); do = thực hiện việc/hoạt động (do homework, do the dishes, do business).",
     {"make": "make a mistake, make money, make a plan", "do": "do homework, do exercise, do a favor"})
items(("I always ___ my homework after dinner.", ["do", "make", "take", "have"], "do homework là cụm cố định.", 1),
      ("Don't worry if you ___ a mistake.", ["make", "do", "take", "get"], "make a mistake là cụm cố định.", 1),
      ("Could you ___ me a favor?", ["do", "make", "give", "take"], "do sb a favor = giúp ai một việc.", 2),
      ("We need to ___ a decision today.", ["make", "do", "get", "put"], "make a decision = đưa ra quyết định.", 2))

CONF("lose_loose", ["lose", "loose"],
     "lose /luːz/ (v): mất, thua; loose /luːs/ (adj): lỏng, rộng. Nhớ: loose có 2 chữ o như chiếc quần rộng thùng thình.",
     {"lose": "(v) mất, thua", "loose": "(adj) rộng, lỏng"})
items(("Don't ___ your boarding pass.", ["lose", "loose", "lost", "loss"], "Sau don't cần động từ nguyên mẫu → lose.", 2),
      ("These pants are too ___ for me.", ["loose", "lose", "lost", "loss"], "Sau 'too' cần tính từ → loose (rộng).", 2))

CONF("job_work", ["job", "work"],
     "job là danh từ ĐẾM ĐƯỢC (a job, two jobs); work thường KHÔNG đếm được (a lot of work) và còn là động từ.",
     {"job": "(n, đếm được) công việc cụ thể", "work": "(n, không đếm được) công việc nói chung; (v) làm việc"})
items(("She has found a new ___ in a bank.", ["job", "work", "works", "working"], "'a new' + danh từ đếm được số ít → job.", 2),
      ("I have a lot of ___ to do today.", ["work", "job", "jobs", "a work"], "'a lot of' + danh từ không đếm được → work.", 2))

CONF("fun_funny", ["fun", "funny"],
     "fun = vui (trải nghiệm thú vị); funny = buồn cười, hài hước (gây cười).",
     {"fun": "The trip was fun. (vui)", "funny": "The movie was funny. (buồn cười)"})
items(("The party was really ___. We danced all night.", ["fun", "funny", "funnily", "fan"], "Vui vẻ, thú vị → fun. 'funny' là buồn cười.", 2),
      ("He told a ___ joke and everyone laughed.", ["funny", "fun", "funnily", "fan"], "Chuyện cười gây cười → funny.", 2))

CONF("bored_boring", ["bored", "boring"],
     "-ed tả CẢM XÚC của người (I'm bored = tôi thấy chán); -ing tả TÍNH CHẤT của vật/người gây ra cảm xúc (The film is boring = phim chán).",
     {"bored": "cảm thấy chán", "boring": "gây chán, nhàm chán"})
items(("I'm ___. There's nothing to do.", ["bored", "boring", "bore", "boredom"], "Cảm xúc của người → bored.", 2),
      ("The lecture was so ___ that I fell asleep.", ["boring", "bored", "bore", "boredom"], "Tính chất của bài giảng → boring.", 2),
      ("We were ___ by the amazing news.", ["excited", "exciting", "excite", "excitement"], "Cảm xúc của người (we) → excited.", 2))

CONF("hear_listen", ["hear", "listen"],
     "hear = nghe thấy (tự nhiên, không chủ ý); listen (to) = lắng nghe (có chủ ý). listen luôn cần 'to' trước tân ngữ.",
     {"hear": "nghe thấy", "listen": "lắng nghe (listen to)"})
items(("I like to ___ to music while I study.", ["listen", "hear", "sound", "ear"], "listen to music = nghe nhạc (chủ động).", 1),
      ("Did you ___ that noise?", ["hear", "listen", "listen to", "sound"], "Nghe thấy (không chủ ý) → hear.", 2))

CONF("look_see_watch", ["look", "see", "watch"],
     "see = nhìn thấy; look (at) = nhìn vào (có chủ ý); watch = xem, theo dõi (thứ chuyển động: TV, phim, trận đấu).",
     {"look": "look at the board", "see": "see a bird", "watch": "watch a movie"})
items(("Let's ___ a movie tonight.", ["watch", "see at", "look", "look at"], "Xem phim → watch a movie.", 1),
      ("___ at this photo! Isn't it cute?", ["Look", "See", "Watch", "View"], "look at = nhìn vào.", 1))

CONF("rise_raise", ["rise", "raise"],
     "rise (nội động từ, không có tân ngữ): tăng, mọc lên — prices rise; raise (ngoại động từ, cần tân ngữ): nâng, tăng cái gì — raise prices.",
     {"rise": "rise - rose - risen (tự tăng)", "raise": "raise - raised - raised (làm tăng cái gì)"})
items(("The company will ___ prices next month.", ["raise", "rise", "arise", "rising"], "Có tân ngữ 'prices' → raise.", 3),
      ("The sun ___ in the east.", ["rises", "raises", "arises", "is raised"], "Không có tân ngữ → rises.", 3))

CONF("among_between", ["among", "between"],
     "between: giữa HAI người/vật (hoặc các đối tượng tách biệt rõ); among: giữa một nhóm (từ ba trở lên).",
     {"between": "between you and me", "among": "among the students"})
items(("The house is ___ the bank and the school.", ["between", "among", "within", "across"], "Giữa hai địa điểm cụ thể → between.", 2),
      ("She is popular ___ her classmates.", ["among", "between", "along", "beside"], "Giữa một nhóm người → among.", 3))

CONF("since_for", ["since", "for"],
     "for + KHOẢNG thời gian (for 3 years); since + MỐC thời gian (since 2020, since Monday).",
     {"for": "for two hours", "since": "since last week"})
items(("I have lived here ___ 2018.", ["since", "for", "from", "during"], "2018 là mốc thời gian → since.", 2),
      ("We have known each other ___ ten years.", ["for", "since", "in", "ago"], "ten years là khoảng thời gian → for.", 2))

CONF("during_while", ["during", "while"],
     "during + DANH TỪ (during the meeting); while + MỆNH ĐỀ (while I was sleeping).",
     {"during": "giới từ + danh từ", "while": "liên từ + mệnh đề"})
items(("Please turn off your phone ___ the meeting.", ["during", "while", "when", "as long as"], "Sau chỗ trống là danh từ 'the meeting' → during.", 3),
      ("Someone called ___ you were out.", ["while", "during", "for", "in"], "Sau chỗ trống là mệnh đề 'you were out' → while.", 3))

CONF("although_despite", ["although", "despite"],
     "although + MỆNH ĐỀ (S + V); despite / in spite of + DANH TỪ / V-ing.",
     {"although": "Although it rained, we went out.", "despite": "Despite the rain, we went out."})
items(("___ the heavy traffic, she arrived on time.", ["Despite", "Although", "Even though", "Because"], "Sau chỗ trống là cụm danh từ → Despite.", 3),
      ("___ he was tired, he finished the report.", ["Although", "Despite", "In spite of", "Because of"], "Sau chỗ trống là mệnh đề → Although.", 3))

CONF("because_because_of", ["because", "because of"],
     "because + MỆNH ĐỀ; because of + DANH TỪ / V-ing.",
     {"because": "because it rained", "because of": "because of the rain"})
items(("The flight was delayed ___ the storm.", ["because of", "because", "since", "so"], "Sau chỗ trống là danh từ 'the storm' → because of.", 2),
      ("I stayed home ___ I was sick.", ["because", "because of", "due to", "despite"], "Sau chỗ trống là mệnh đề → because.", 2))

CONF("economic_economical", ["economic", "economical"],
     "economic = (thuộc) kinh tế (economic growth); economical = tiết kiệm, ít tốn kém (an economical car).",
     {"economic": "liên quan đến kinh tế", "economical": "tiết kiệm"})
items(("The country has seen strong ___ growth.", ["economic", "economical", "economy", "economist"], "Tăng trưởng kinh tế → economic growth.", 4),
      ("This small car is very ___ to run.", ["economical", "economic", "economy", "economize"], "Tiết kiệm (nhiên liệu, chi phí) → economical.", 4))

CONF("advice_advise", ["advice", "advise"],
     "advice /ədˈvaɪs/ là DANH TỪ không đếm được (some advice, a piece of advice); advise /ədˈvaɪz/ là ĐỘNG TỪ.",
     {"advice": "(n) lời khuyên", "advise": "(v) khuyên"})
items(("Can you give me some ___ about studying abroad?", ["advice", "advise", "advices", "advisor"], "'some' + danh từ không đếm được → advice (không thêm s).", 2),
      ("The doctor ___ me to drink more water.", ["advised", "advice", "adviced", "advisable"], "Cần động từ quá khứ → advised.", 3))

CONF("remember_remind", ["remember", "remind"],
     "remember = (tự mình) nhớ; remind sb = nhắc ai đó nhớ. remind sb to do sth / remind sb of sth.",
     {"remember": "tự nhớ", "remind": "nhắc người khác"})
items(("Please ___ me to buy milk.", ["remind", "remember", "memorize", "recall"], "Nhắc ai đó làm gì → remind sb to do.", 2),
      ("I can't ___ her name.", ["remember", "remind", "remain", "reminder"], "Tự nhớ → remember.", 1))

CONF("check_control", ["check", "control"],
     "Bẫy người Việt: 'kiểm tra' là check, KHÔNG phải control. control = điều khiển, kiểm soát.",
     {"check": "kiểm tra", "control": "điều khiển, kiểm soát"})
items(("Please ___ your answers before you submit.", ["check", "control", "examine at", "test out"], "Kiểm tra lại → check. 'control' là kiểm soát/điều khiển.", 2))

CONF("customer_client_guest", ["customer", "client", "guest"],
     "customer: khách mua hàng; client: khách dùng dịch vụ chuyên môn (luật sư, ngân hàng, agency); guest: khách khách sạn/khách mời.",
     {"customer": "khách mua hàng", "client": "khách dịch vụ chuyên môn", "guest": "khách lưu trú/khách mời"})
items(("The lawyer has a meeting with a new ___ at ten.", ["client", "guest", "customer", "consumer"], "Khách của luật sư (dịch vụ chuyên môn) → client.", 3),
      ("Hotel ___ can use the gym for free.", ["guests", "clients", "customers", "passengers"], "Khách lưu trú khách sạn → guests.", 2))

CONF("salary_wage", ["salary", "wage"],
     "salary: lương cố định theo tháng/năm (nhân viên văn phòng); wage: tiền công theo giờ/ngày/tuần.",
     {"salary": "lương tháng/năm", "wage": "tiền công theo giờ/tuần"})
items(("Factory workers here are paid an hourly ___.", ["wage", "salary", "income", "fee"], "Trả theo giờ → wage.", 3))

CONF("price_prize", ["price", "prize"],
     "price = giá tiền; prize = giải thưởng.",
     {"price": "giá", "prize": "giải thưởng"})
items(("She won first ___ in the speech contest.", ["prize", "price", "praise", "priced"], "Giải nhất → first prize.", 2))

CONF("job_career", ["job", "career"],
     "job: công việc cụ thể hiện tại; career: sự nghiệp (con đường nghề nghiệp lâu dài).",
     {"job": "việc làm", "career": "sự nghiệp"})
items(("She has had a long and successful ___ as a doctor.", ["career", "job", "work", "employment"], "Sự nghiệp lâu dài, thành công → career.", 3))

CONF("travel_trip_journey", ["travel", "trip", "journey"],
     "travel thường là động từ hoặc danh từ không đếm được; trip: chuyến đi (đi và về); journey: hành trình di chuyển từ A đến B.",
     {"travel": "(v) đi du lịch, di chuyển", "trip": "chuyến đi", "journey": "hành trình"})
items(("We went on a business ___ to Singapore.", ["trip", "travel", "journey", "tour guide"], "Chuyến công tác → business trip.", 2),
      ("The ___ from Hanoi to Saigon takes 30 hours by train.", ["journey", "travel", "trip to", "tour"], "Hành trình di chuyển từ A đến B → journey.", 3))

CONF("its_its", ["its", "it's"],
     "its = của nó (tính từ sở hữu); it's = it is / it has.",
     {"its": "của nó", "it's": "it is / it has"})
items(("The dog is wagging ___ tail.", ["its", "it's", "it", "their"], "Đuôi của nó → its (sở hữu).", 2),
      ("___ raining. Take an umbrella.", ["It's", "Its", "It", "There's"], "It is raining → It's.", 1))

CONF("than_then", ["than", "then"],
     "than dùng trong SO SÁNH (bigger than); then = sau đó, lúc đó.",
     {"than": "hơn (so sánh)", "then": "sau đó"})
items(("This phone is cheaper ___ that one.", ["than", "then", "that", "as"], "So sánh hơn → than.", 1))

CONF("especially_specially", ["especially", "specially"],
     "especially = đặc biệt là, nhất là (nhấn mạnh); specially = (làm) riêng cho một mục đích.",
     {"especially": "nhất là", "specially": "dành riêng"})
items(("I love fruit, ___ mangoes.", ["especially", "specially", "special", "specialty"], "Nhấn mạnh 'nhất là' → especially.", 3),
      ("This cake was made ___ for your birthday.", ["specially", "especially", "special", "specialize"], "Làm riêng cho mục đích → specially.", 4))

CONF("hard_hardly", ["hard", "hardly"],
     "hard vừa là tính từ vừa là trạng từ (work hard = làm chăm chỉ); hardly = hầu như không.",
     {"hard": "chăm chỉ, vất vả", "hardly": "hầu như không"})
items(("She works very ___ to support her family.", ["hard", "hardly", "harder than", "hardness"], "work hard = làm việc chăm chỉ (hard là trạng từ).", 3),
      ("I could ___ hear him because of the noise.", ["hardly", "hard", "harden", "hardship"], "Hầu như không nghe thấy → hardly.", 3))

CONF("late_lately", ["late", "lately"],
     "late = muộn (adj/adv); lately = gần đây (thường dùng với thì hiện tại hoàn thành).",
     {"late": "muộn", "lately": "gần đây"})
items(("Have you seen any good movies ___?", ["lately", "late", "later", "latest"], "Gần đây (đi với hiện tại hoàn thành) → lately.", 3))

CONF("alone_lonely", ["alone", "lonely"],
     "alone = một mình (trạng thái, không nhất thiết buồn); lonely = cô đơn (cảm xúc buồn).",
     {"alone": "một mình", "lonely": "cô đơn"})
items(("She likes to travel ___.", ["alone", "lonely", "lone", "only"], "Một mình (trạng thái) → alone.", 2),
      ("He felt ___ in the big city without friends.", ["lonely", "alone", "lone", "loneliness"], "Cảm giác cô đơn → lonely.", 2))

CONF("excited_exciting", ["excited", "exciting"],
     "excited: người cảm thấy hào hứng; exciting: điều gì đó thú vị, gây hào hứng.",
     {"excited": "cảm thấy hào hứng", "exciting": "gây hào hứng"})
items(("The final match was really ___.", ["exciting", "excited", "excite", "excitedly"], "Tính chất trận đấu → exciting.", 2))
