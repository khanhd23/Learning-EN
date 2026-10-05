from lib import point, C, E, O, T

point("past_simple", 2, "Thì quá khứ đơn", "S + V2/V-ed · S + didn't + V · Did + S + V?",
      "Hành động đã xảy ra và kết thúc tại thời điểm xác định trong quá khứ.",
      "Động từ có quy tắc thêm -ed (worked, studied, stopped). Động từ bất quy tắc phải học thuộc (go → went, buy → bought, see → saw). Phủ định/câu hỏi dùng did + động từ NGUYÊN MẪU: I didn't go, Did you see...? To be: was (I/he/she/it), were (you/we/they).",
      ["yesterday", "last week", "ago", "in 2020", "when I was a child"],
      [("I visited Hue last summer.", "Mùa hè năm ngoái tôi đã đến Huế.", "visited"),
       ("She didn't come to class yesterday.", "Hôm qua cô ấy không đến lớp.", "didn't come"),
       ("Did you see the match?", "Bạn có xem trận đấu không?", "Did ... see")],
      [("I didn't went there.", "I didn't go there.", "Sau didn't dùng động từ nguyên mẫu."),
       ("Yesterday I go to school.", "Yesterday I went to school.", "Có 'yesterday' → quá khứ đơn.")],
      tags=["tense"], exam=True)
C("We ___ a great movie last night.", ["watched", "watch", "have watched", "watching"], "'last night' → quá khứ đơn.")
C("She ___ to Japan two years ago.", ["went", "goes", "has gone", "go"], "'two years ago' → quá khứ đơn: go → went.")
C("I ___ my keys yesterday, but I found them later.", ["lost", "lose", "have lost", "was lose"], "Hành động kết thúc trong quá khứ → lost.")
C("___ you finish the report on time?", ["Did", "Do", "Were", "Have"], "Câu hỏi quá khứ đơn với động từ thường → Did.")
C("They ___ at home last weekend.", ["were", "was", "did", "are"], "They + to be quá khứ → were.")
C("He didn't ___ the email.", ["read", "reads", "readed", "reading"], "didn't + V nguyên mẫu.")
C("When I was a child, I ___ in the countryside.", ["lived", "live", "have lived", "am living"], "Mốc quá khứ 'when I was a child' → quá khứ đơn.")
C("The company ___ a new product in March.", ["launched", "launches", "has launched", "launching"], "'in March' (đã qua) → quá khứ đơn.")
C("I ___ very tired after the trip.", ["was", "were", "am", "did"], "I + to be quá khứ → was.")
C("She ___ her phone at home this morning.", ["left", "leaved", "leaves", "has leave"], "leave → left (bất quy tắc).")
E("I [didn't] [went] to [the] party [last night].", 1, "go", "Sau didn't dùng động từ nguyên mẫu: go.")
E("We [buyed] [a] new car [two months] [ago].", 0, "bought", "buy → bought (bất quy tắc).")
E("[Did] she [called] you [yesterday] [evening]?", 1, "call", "Sau Did dùng động từ nguyên mẫu: call.")
T("I started learning English five years ago.", ["I have learned English for five years.", "I learned English five years.", "I will learn English in five years.", "I haven't learned English for five years."], "Bắt đầu cách đây 5 năm và vẫn đang học ≈ have learned for five years.")
O("We went to the beach last Sunday.", "Chủ nhật tuần trước chúng tôi đi biển.")
O("Did you enjoy the concert?", "Bạn có thích buổi hòa nhạc không?")
O("She didn't have breakfast this morning.", "Sáng nay cô ấy không ăn sáng.")

point("present_continuous", 2, "Thì hiện tại tiếp diễn", "S + am/is/are + V-ing",
      "Việc đang diễn ra lúc nói, việc tạm thời, kế hoạch đã sắp xếp trong tương lai gần.",
      "Dùng to be + V-ing. Dấu hiệu: now, right now, at the moment, Look!, Listen!. Một số động từ chỉ trạng thái (know, like, want, need, believe, understand) thường KHÔNG dùng tiếp diễn: I know (không phải I'm knowing).",
      ["now", "right now", "at the moment", "Look!", "this week"],
      [("I'm studying English now.", "Bây giờ tôi đang học tiếng Anh.", "I'm studying"),
       ("Look! It's raining.", "Nhìn kìa! Trời đang mưa.", "It's raining"),
       ("We're meeting the client tomorrow.", "Ngày mai chúng tôi gặp khách hàng (đã hẹn).", "We're meeting")],
      [("She is cook dinner now.", "She is cooking dinner now.", "Cần V-ing sau to be."),
       ("I am knowing the answer.", "I know the answer.", "know là động từ trạng thái, không dùng tiếp diễn.")],
      tags=["tense"], exam=True)
C("Be quiet! The baby ___.", ["is sleeping", "sleeps", "slept", "sleep"], "Hành động đang diễn ra → is sleeping.")
C("Look! Those people ___ the bus.", ["are getting off", "get off", "got off", "is getting off"], "Look! → đang diễn ra; chủ ngữ số nhiều → are.")
C("I ___ the answer to this question.", ["know", "am knowing", "knowing", "am know"], "know là động từ trạng thái → hiện tại đơn.")
C("She ___ for her exam at the moment.", ["is studying", "studies", "studied", "study"], "'at the moment' → hiện tại tiếp diễn.")
C("We ___ dinner with my parents tonight. We booked a table.", ["are having", "have", "had", "having"], "Kế hoạch đã sắp xếp → hiện tại tiếp diễn.")
C("Prices ___ higher and higher these days.", ["are getting", "get", "got", "gets"], "Xu hướng đang thay đổi → are getting.")
C("Listen! Someone ___ at the door.", ["is knocking", "knocks", "knocked", "knock"], "Listen! → đang diễn ra.")
E("Right now she [is] [cook] dinner [for] [her family].", 1, "cooking", "to be + V-ing → is cooking.")
E("I [am] [wanting] [a] cup of [coffee].", 1, "want (I want)", "want là động từ trạng thái → I want.")
E("Look! The children [plays] [in] [the] [garden].", 0, "are playing", "Look! → hiện tại tiếp diễn: are playing.")
O("What are you doing right now?", "Bây giờ bạn đang làm gì?")
O("My sister is working from home this week.", "Tuần này chị tôi làm việc ở nhà.")

point("comparison", 2, "So sánh hơn & so sánh nhất", "adj-er than · more + adj + than · the adj-est · the most + adj",
      "So sánh hai hay nhiều người/vật.",
      "Tính từ ngắn (1 âm tiết, hoặc 2 âm tiết tận cùng -y): thêm -er/-est (cheap → cheaper → the cheapest; happy → happier → the happiest). Tính từ dài: more/most (more expensive, the most expensive). Bất quy tắc: good → better → the best; bad → worse → the worst; far → farther/further. So sánh bằng: as + adj + as.",
      ["than", "the most", "the -est", "as ... as"],
      [("This phone is cheaper than that one.", "Chiếc điện thoại này rẻ hơn chiếc kia.", "cheaper than"),
       ("She is the best student in the class.", "Cô ấy là học sinh giỏi nhất lớp.", "the best"),
       ("Hanoi is as busy as Saigon.", "Hà Nội đông đúc như Sài Gòn.", "as busy as")],
      [("more cheaper", "cheaper", "Không dùng more với tính từ đã thêm -er."),
       ("the most good", "the best", "good có dạng bất quy tắc.")],
      tags=["comparison"], exam=True)
C("This hotel is ___ than the one we stayed in last year.", ["more comfortable", "comfortabler", "most comfortable", "comfortable"], "Tính từ dài + than → more comfortable.")
C("Today is ___ day of the year.", ["the hottest", "the hotter", "hottest", "the most hot"], "So sánh nhất, tính từ ngắn: hot → the hottest (gấp đôi t).")
C("My English is getting ___.", ["better", "gooder", "more good", "best"], "good → better.")
C("This is ___ restaurant in town.", ["the most expensive", "the expensivest", "more expensive", "most expensive than"], "So sánh nhất tính từ dài: the most expensive.")
C("Her new car is ___ as mine.", ["as fast", "faster", "so fast than", "fastest"], "So sánh bằng: as + adj + as.")
C("The weather today is ___ than yesterday.", ["worse", "badder", "more bad", "worst"], "bad → worse.")
C("Mai is ___ than her sister.", ["taller", "more tall", "tallest", "the taller"], "Tính từ ngắn + -er + than.")
C("Of all the candidates, Mr. Kim is ___ qualified.", ["the most", "more", "most than", "the more"], "Of all... → so sánh nhất: the most qualified.", qtype="comparison")
E("This book [is] [more] [interesting] [then] the film.", 3, "than", "So sánh dùng than, không phải then.")
E("He [is] [more] [taller] [than] his brother.", 1, "(bỏ more)", "taller đã là so sánh hơn, không thêm more.")
E("This is [the] [most] [cheap] [phone] in the shop.", 2, "cheapest (the cheapest)", "Tính từ ngắn: the cheapest.")
O("Mount Everest is the highest mountain in the world.", "Everest là ngọn núi cao nhất thế giới.")
O("My room is bigger than yours.", "Phòng tôi to hơn phòng bạn.")

point("quantifiers", 2, "Danh từ đếm được / không đếm được & lượng từ", "many/few + N đếm được · much/little + N không đếm được · some/any/a lot of: cả hai",
      "Nói về số lượng: nhiều, ít, một vài.",
      "Đếm được: many, a few, few, a number of. Không đếm được (water, money, information, advice, furniture, luggage): much, a little, little, an amount of. a lot of / lots of / some / any dùng được cho cả hai. some thường ở câu khẳng định và lời mời; any ở câu phủ định và câu hỏi. a few/a little = một vài (tích cực); few/little = rất ít (tiêu cực).",
      ["many", "much", "a few", "a little", "some", "any"],
      [("How much money do you need?", "Bạn cần bao nhiêu tiền?", "much money"),
       ("I have a few friends in Canada.", "Tôi có vài người bạn ở Canada.", "a few friends"),
       ("Would you like some tea?", "Bạn uống chút trà nhé?", "some tea")],
      [("How many money...?", "How much money...?", "money không đếm được → much."),
       ("a lot of luggages", "a lot of luggage", "luggage không đếm được.")],
      tags=["article_quantifier"], exam=True)
C("How ___ people came to the meeting?", ["many", "much", "little", "a little"], "people đếm được → many.")
C("We don't have ___ time. Let's hurry.", ["much", "many", "a few", "few"], "time (thời gian) không đếm được → much.")
C("There are only ___ seats left. Book now!", ["a few", "a little", "much", "little"], "seats đếm được, 'một vài' → a few.")
C("Could I have ___ water, please?", ["some", "any", "many", "a few"], "Lời đề nghị lịch sự → some.")
C("I didn't buy ___ bread.", ["any", "some", "many", "a few"], "Câu phủ định → any.")
C("She has ___ furniture in her new apartment.", ["very little", "very few", "many", "a few"], "furniture không đếm được → (very) little.")
C("___ of the information in this report is out of date.", ["Much", "Many", "A few", "Several"], "information không đếm được → much.", qtype="quantifier")
C("___ employees have asked to work from home.", ["Several", "Much", "A little", "Little"], "employees đếm được số nhiều → several.", qtype="quantifier")
E("How [many] [money] [do] you [need]?", 0, "much", "money không đếm được → How much.")
E("We [have] [a lot of] [homeworks] [today].", 2, "homework", "homework không đếm được, không thêm -s.")
E("There [isn't] [many] [milk] [left].", 1, "much", "milk không đếm được → much.")
O("There are a few apples in the basket.", "Có vài quả táo trong giỏ.")
O("I need a little help with this.", "Tôi cần một chút giúp đỡ việc này.")

point("future", 2, "Will và be going to", "S + will + V · S + am/is/are going to + V",
      "Nói về tương lai: quyết định tức thời, dự đoán, kế hoạch có sẵn.",
      "will: quyết định ngay lúc nói (I'll get it!), lời hứa, đề nghị, dự đoán chung (I think it will rain). be going to: dự định đã có từ trước (I'm going to study abroad), dự đoán có bằng chứng trước mắt (Look at the clouds — it's going to rain).",
      ["tomorrow", "next week", "I think", "Look!", "plan"],
      [("I'm hungry. — I'll make you a sandwich.", "Tôi đói. — Để tôi làm cho bạn cái bánh mì.", "I'll make"),
       ("We're going to visit Japan next month.", "Tháng sau chúng tôi sẽ đi Nhật (đã lên kế hoạch).", "We're going to visit"),
       ("Look at the sky! It's going to rain.", "Nhìn trời kìa! Sắp mưa rồi.", "It's going to rain")],
      [("I will to call you.", "I will call you.", "Sau will dùng V nguyên mẫu, không có to."),
       ("She going to buy a car.", "She is going to buy a car.", "Thiếu to be.")],
      tags=["tense"], exam=True)
C("The phone is ringing. — OK, I ___ it.", ["will answer", "answered", "have answered", "was answering"], "Quyết định ngay lúc nói → will.")
C("We've saved money because we ___ a new house next year.", ["are going to buy", "will buying", "bought", "buy"], "Dự định đã có từ trước → be going to.")
C("Watch out! That glass ___ fall!", ["is going to", "will to", "is", "going to"], "Dự đoán có dấu hiệu rõ ràng ngay trước mắt → is going to.")
C("I think the team ___ the match.", ["will win", "wins", "won", "winning"], "Dự đoán dựa trên ý kiến (I think) → will.")
C("I promise I ___ tell anyone.", ["won't", "am not going", "don't", "not will"], "Lời hứa → will/won't.")
C("The new branch ___ next spring, according to the manager.", ["will open", "opened", "has opened", "opening"], "Sự việc tương lai → will open.", qtype="tense")
E("I [will] [to] [call] you [tomorrow].", 1, "(bỏ to)", "will + V nguyên mẫu.")
E("She [going] [to] [start] a new job [next week].", 0, "is going", "Thiếu to be: She is going to...")
O("I'm going to learn ten new words every day.", "Tôi định học mười từ mới mỗi ngày.")
O("Don't worry, I will help you.", "Đừng lo, tôi sẽ giúp bạn.")

point("must_should", 2, "Must / Should / Have to", "S + must/should + V · S + have/has to + V",
      "Bắt buộc, lời khuyên, cấm đoán.",
      "should: nên (lời khuyên). must: phải (bắt buộc từ người nói, nội quy). have to: phải (bắt buộc từ hoàn cảnh bên ngoài). mustn't = CẤM; don't have to = KHÔNG CẦN (không bắt buộc). Đây là bẫy rất hay gặp!",
      ["should", "must", "mustn't", "have to", "don't have to"],
      [("You should drink more water.", "Bạn nên uống nhiều nước hơn.", "should drink"),
       ("You mustn't smoke here.", "Bạn không được hút thuốc ở đây.", "mustn't"),
       ("You don't have to come if you're busy.", "Nếu bận thì bạn không cần đến.", "don't have to")],
      [("You must to wear a helmet.", "You must wear a helmet.", "must + V nguyên mẫu."),
       ("She have to work on Saturday.", "She has to work on Saturday.", "She → has to.")],
      tags=["modal"])
C("You look tired. You ___ go to bed early.", ["should", "mustn't", "don't have to", "has to"], "Lời khuyên → should.")
C("Students ___ use phones during the exam. It's not allowed.", ["mustn't", "don't have to", "should", "can"], "Cấm → mustn't.")
C("Tomorrow is Sunday, so I ___ get up early.", ["don't have to", "mustn't", "must", "have"], "Không cần thiết → don't have to.")
C("All visitors ___ sign in at reception.", ["must", "mustn't", "don't have to", "shouldn't to"], "Quy định bắt buộc → must.")
C("He ___ wear a uniform at work.", ["has to", "have to", "must to", "having to"], "He → has to.")
E("You [must] [to] [finish] this [by Friday].", 1, "(bỏ to)", "must + V nguyên mẫu.")
E("My mother [have] [to] work [on] [weekends].", 0, "has", "My mother (she) → has to.")
T("It isn't necessary to bring food.", ["You don't have to bring food.", "You mustn't bring food.", "You must bring food.", "You should bring food."], "Không cần thiết = don't have to (khác mustn't = cấm).")
O("You should practice speaking every day.", "Bạn nên luyện nói mỗi ngày.")

point("pronouns", 2, "Đại từ & tính từ sở hữu", "I/me/my/mine · he/him/his/his · she/her/her/hers · they/them/their/theirs",
      "Thay thế danh từ, chỉ sở hữu.",
      "Đại từ chủ ngữ (I, he, she, we, they) đứng trước động từ. Đại từ tân ngữ (me, him, her, us, them) đứng sau động từ/giới từ. Tính từ sở hữu (my, his, her, our, their) đứng TRƯỚC danh từ. Đại từ sở hữu (mine, his, hers, ours, theirs) đứng MỘT MÌNH, thay cho cụm 'tính từ sở hữu + danh từ'. Đại từ phản thân (myself, himself...) khi chủ ngữ và tân ngữ là một.",
      ["me", "my", "mine", "myself"],
      [("This is my bag. That one is yours.", "Đây là túi của tôi. Cái kia là của bạn.", "yours"),
       ("Can you help me?", "Bạn giúp tôi được không?", "me"),
       ("She made the cake herself.", "Cô ấy tự làm cái bánh.", "herself")],
      [("Me and him went out.", "He and I went out.", "Làm chủ ngữ dùng đại từ chủ ngữ."),
       ("This book is my.", "This book is mine.", "Đứng một mình dùng đại từ sở hữu.")],
      tags=["pronoun"], exam=True)
C("This isn't my umbrella. It's ___.", ["hers", "her", "she", "herself"], "Đứng một mình thay cho 'her umbrella' → hers.")
C("Please give ___ the report by noon.", ["me", "I", "my", "mine"], "Sau động từ give cần tân ngữ → me.")
C("Ms. Tran asked ___ assistant to book a room.", ["her", "she", "hers", "herself"], "Trước danh từ assistant → tính từ sở hữu her.", qtype="pronoun")
C("The children cleaned the room by ___.", ["themselves", "them", "their", "theirs"], "by + đại từ phản thân = tự mình → themselves.")
C("Our team finished ___ project early.", ["its", "it's", "their's", "it"], "Sở hữu của team (số ít) → its. 'it's' = it is.", qtype="pronoun")
C("Mr. Lee will present the results ___ because his assistant is sick.", ["himself", "him", "his", "he"], "Nhấn mạnh chính anh ấy tự làm → himself.", qtype="pronoun")
E("[Me] and my brother [went] [to the zoo] [yesterday].", 0, "My brother and I", "Làm chủ ngữ phải dùng I.")
E("Is [this] pen [yours] or [hers]? — It's [my].", 3, "mine", "Đứng một mình → mine.")
E("The company [announced] [it's] new [policy] [yesterday].", 1, "its", "Sở hữu → its (it's = it is).")
O("Their house is bigger than ours.", "Nhà họ to hơn nhà chúng tôi.")
O("I taught myself to play the guitar.", "Tôi tự học chơi guitar.")
