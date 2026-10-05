from lib import point, C, E, O, T

point("present_perfect", 3, "Hiện tại hoàn thành (so với quá khứ đơn)", "S + have/has + V3/V-ed",
      "Trải nghiệm, việc vừa xong, việc bắt đầu trong quá khứ còn kéo dài đến nay, kết quả liên quan hiện tại.",
      "Hiện tại hoàn thành KHÔNG đi với thời điểm quá khứ cụ thể (yesterday, last year, in 2020) — khi đó dùng quá khứ đơn. Dấu hiệu: just, already, yet, ever, never, since, for, so far, recently, up to now.",
      ["already", "yet", "just", "ever", "never", "since", "for", "so far", "recently"],
      [("I have already finished my homework.", "Tôi đã làm xong bài tập rồi.", "have already finished"),
       ("She has worked here since 2019.", "Cô ấy làm ở đây từ năm 2019.", "has worked"),
       ("Have you ever been to Korea?", "Bạn đã từng đến Hàn Quốc chưa?", "Have ... been")],
      [("I have seen him yesterday.", "I saw him yesterday.", "Có thời điểm quá khứ cụ thể → quá khứ đơn."),
       ("She has went home.", "She has gone home.", "have/has + V3 (go → gone).")],
      tags=["tense"], exam=True)
C("I ___ this movie three times.", ["have seen", "saw", "see", "am seeing"], "Trải nghiệm tính đến hiện tại (three times) → have seen.")
C("She ___ in this company since 2018.", ["has worked", "worked", "works", "is working"], "since 2018 → hiện tại hoàn thành.")
C("Have you finished your report ___?", ["yet", "already", "since", "ago"], "Câu hỏi hoàn thành → yet ở cuối câu.")
C("We ___ to Da Nang last summer.", ["went", "have gone", "have been", "go"], "last summer (thời điểm xác định) → quá khứ đơn.")
C("He has ___ left the office. You just missed him.", ["just", "yet", "ever", "since"], "Vừa mới → just.")
C("___ you ever eaten durian?", ["Have", "Did", "Do", "Are"], "ever → hiện tại hoàn thành: Have you ever...?")
C("Sales ___ by 15 percent so far this year.", ["have increased", "increased", "increase", "are increase"], "so far → hiện tại hoàn thành.", qtype="tense")
C("I haven't seen her ___ we graduated.", ["since", "for", "ago", "from"], "since + mốc (mệnh đề quá khứ).")
C("They have been married ___ ten years.", ["for", "since", "ago", "in"], "for + khoảng thời gian.")
C("Ms. Garcia ___ as our marketing director for the past five years.", ["has served", "serves", "served", "is serving"], "for the past five years → hiện tại hoàn thành.", qtype="tense")
E("I [have] [visited] my grandparents [last] [weekend].", 0, "visited (bỏ have)", "last weekend → quá khứ đơn.")
E("She [has] [went] to [the] [bank].", 1, "gone", "has + V3: go → gone.")
E("We [have] lived [here] [since] [five years].", 3, "for five years", "five years là khoảng thời gian → for.")
T("I started working here in 2020.", ["I have worked here since 2020.", "I worked here for 2020.", "I have worked here for 2020.", "I am working here in 2020."], "Bắt đầu năm 2020 và vẫn đang làm → have worked since 2020.")
T("This is the first time I have eaten sushi.", ["I have never eaten sushi before.", "I ate sushi many times.", "I have eaten sushi before.", "I don't like eating sushi."], "Lần đầu tiên = trước đây chưa từng.")
O("Have you ever been to Japan?", "Bạn đã từng đến Nhật chưa?")
O("I have just finished my homework.", "Tôi vừa làm xong bài tập.")

point("passive", 3, "Câu bị động", "S + be + V3 (+ by O)",
      "Nhấn mạnh đối tượng chịu tác động, hoặc khi không cần/không biết ai làm.",
      "Chia to be theo thì của câu chủ động, động từ chính luôn ở V3: is made, was built, has been sent, will be held, must be signed. Mẹo làm Part 5: nếu sau chỗ trống KHÔNG có tân ngữ và chủ ngữ là vật chịu tác động → thường chọn bị động.",
      ["by", "be + V3", "is held", "was sent", "has been", "will be"],
      [("This bridge was built in 1900.", "Cây cầu này được xây năm 1900.", "was built"),
       ("English is spoken all over the world.", "Tiếng Anh được nói trên khắp thế giới.", "is spoken"),
       ("The meeting will be held on Monday.", "Cuộc họp sẽ được tổ chức vào thứ Hai.", "will be held")],
      [("The letter was wrote by Nam.", "The letter was written by Nam.", "Bị động dùng V3: write → written."),
       ("The report must send today.", "The report must be sent today.", "Vật không tự gửi được → bị động: must be sent.")],
      tags=["passive"], exam=True)
C("The new library ___ next year.", ["will be opened", "will open it", "opened", "is opening it"], "Thư viện được mở (bị động, tương lai) → will be opened.")
C("These cars ___ in Japan.", ["are made", "make", "are making", "made it"], "Xe được sản xuất → bị động hiện tại: are made.")
C("The winners ___ at the end of the ceremony.", ["will be announced", "will announce", "announced", "are announcing"], "Người thắng được công bố → bị động.", qtype="passive")
C("All applications must ___ by May 31.", ["be submitted", "submit", "submitted", "submitting"], "Đơn được nộp → must be + V3.", qtype="passive")
C("The package ___ yesterday afternoon.", ["was delivered", "delivered", "is delivered", "has delivered"], "yesterday + bị động → was delivered.")
C("The room ___ every morning by the hotel staff.", ["is cleaned", "cleans", "is cleaning", "cleaned it"], "Phòng được dọn mỗi sáng → is cleaned.")
C("The meeting has ___ until next Thursday.", ["been postponed", "postponed", "being postponed", "postpone"], "has + been + V3 → has been postponed.", qtype="passive")
C("Customers ___ to keep their receipts.", ["are advised", "advise", "are advising", "advised to"], "Khách hàng được khuyên → are advised.", qtype="passive")
C("The museum ___ by thousands of tourists every year.", ["is visited", "visits", "is visiting", "has visited"], "Bảo tàng được tham quan → is visited.")
E("This song [was] [wrote] [by] a [famous] singer.", 1, "written", "write → written.")
E("The documents [must] [send] [to] the [manager] today.", 1, "be sent", "Tài liệu được gửi → must be sent.")
E("The house [is] [painting] [by] my father [right now].", 1, "being painted", "Bị động tiếp diễn: is being painted.")
T("Someone stole my bike last night.", ["My bike was stolen last night.", "My bike stole last night.", "My bike has stolen last night.", "Someone's bike was stolen by me."], "Chuyển bị động: My bike was stolen.")
O("The office is cleaned every evening.", "Văn phòng được dọn dẹp mỗi tối.")
O("The results will be announced tomorrow.", "Kết quả sẽ được công bố vào ngày mai.")

point("conditionals", 3, "Câu điều kiện loại 0, 1, 2", "0: If + HTĐ, HTĐ · 1: If + HTĐ, will + V · 2: If + QKĐ, would + V",
      "Diễn tả sự thật (0), khả năng có thật ở tương lai (1), giả định trái hiện tại (2).",
      "Loại 0: sự thật luôn đúng (If you heat ice, it melts). Loại 1: có thể xảy ra (If it rains, I will stay home) — mệnh đề if KHÔNG dùng will. Loại 2: không có thật ở hiện tại (If I had money, I would travel) — với to be dùng were cho mọi ngôi (If I were you...). unless = if ... not.",
      ["if", "unless", "would", "were"],
      [("If it rains tomorrow, we will cancel the trip.", "Nếu mai trời mưa, chúng tôi sẽ hủy chuyến đi.", "If it rains ... will cancel"),
       ("If I were you, I would apply for that job.", "Nếu tôi là bạn, tôi sẽ nộp đơn vào công việc đó.", "If I were you ... would apply"),
       ("If you heat water to 100°C, it boils.", "Nếu đun nước đến 100°C, nó sôi.", "If you heat ... boils")],
      [("If it will rain, I will stay home.", "If it rains, I will stay home.", "Mệnh đề if loại 1 dùng hiện tại đơn."),
       ("If I am rich, I would buy a big house.", "If I were rich, I would buy a big house.", "Loại 2: If + quá khứ (were).")],
      tags=["conditional"], exam=True)
C("If you ___ hard, you will pass the exam.", ["study", "will study", "studied", "would study"], "Loại 1: If + hiện tại đơn.")
C("If I ___ you, I would talk to her.", ["were", "am", "will be", "be"], "Loại 2: If I were you.")
C("If I had more time, I ___ learn another language.", ["would", "will", "can", "am going to"], "Loại 2: would + V.")
C("If you mix red and white, you ___ pink.", ["get", "would get", "got", "had got"], "Loại 0 (sự thật) → hiện tại đơn.")
C("___ you hurry, you'll miss the bus.", ["Unless", "If", "When", "Because"], "Unless = nếu không: Nếu bạn không nhanh lên thì sẽ lỡ xe.")
C("If the shipment ___ late, please contact our office.", ["arrives", "will arrive", "arrived", "would arrive"], "Loại 1: If + hiện tại đơn.", qtype="conditional")
C("We would hire more staff if our budget ___ larger.", ["were", "is", "will be", "has been"], "Loại 2 → were.", qtype="conditional")
C("If she ___ the answer, she would tell us.", ["knew", "knows", "will know", "has known"], "Loại 2: If + quá khứ đơn.")
E("If it [will] [rain] tomorrow, we [will] [stay] at home.", 0, "rains (bỏ will)", "Mệnh đề if loại 1 không dùng will.")
E("If I [have] [a car], I [would] [drive] to work.", 0, "had", "Loại 2: If + quá khứ đơn → had.")
E("[Unless] you [don't] [study], you [will] fail.", 1, "(bỏ don't)", "Unless đã mang nghĩa phủ định: Unless you study...")
T("I don't have a car, so I can't drive you home.", ["If I had a car, I could drive you home.", "If I have a car, I can drive you home.", "If I had a car, I can't drive you home.", "Unless I have a car, I could drive you home."], "Giả định trái hiện tại → điều kiện loại 2.")
O("If you need help, just call me.", "Nếu bạn cần giúp đỡ, cứ gọi tôi.")
O("What would you do if you won the lottery?", "Bạn sẽ làm gì nếu trúng xổ số?")

point("relative", 3, "Mệnh đề quan hệ (who / which / that / whose / where)", "N (người) + who · N (vật) + which · that: cả hai · whose + N · where: nơi chốn",
      "Bổ sung thông tin cho danh từ đứng trước.",
      "who thay cho người, which thay cho vật, that thay cho cả hai (trong mệnh đề xác định). whose + danh từ chỉ sở hữu. where = nơi mà (= in/at which). Trong mệnh đề có dấu phẩy (không xác định) KHÔNG dùng that. Có thể lược bỏ đại từ quan hệ khi nó làm tân ngữ.",
      ["who", "which", "that", "whose", "where"],
      [("The woman who lives next door is a doctor.", "Người phụ nữ sống cạnh nhà là bác sĩ.", "who lives"),
       ("This is the phone which I bought yesterday.", "Đây là chiếc điện thoại tôi mua hôm qua.", "which I bought"),
       ("That's the man whose car was stolen.", "Đó là người đàn ông bị mất trộm xe.", "whose car")],
      [("The man which called you...", "The man who called you...", "Người → who."),
       ("Hanoi, that is the capital, ...", "Hanoi, which is the capital, ...", "Sau dấu phẩy không dùng that.")],
      tags=["relative_clause"], exam=True)
C("The girl ___ is sitting next to Nam is my cousin.", ["who", "which", "whose", "where"], "Người làm chủ ngữ → who.")
C("I lost the book ___ you lent me.", ["which", "who", "whose", "where"], "Vật → which.")
C("This is the restaurant ___ we had our first date.", ["where", "which", "who", "whose"], "Nơi chốn (= at which) → where.")
C("He's the student ___ father is a famous singer.", ["whose", "who", "which", "that"], "Sở hữu (cha của cậu ấy) → whose.")
C("Applicants ___ have experience will be preferred.", ["who", "which", "whose", "whom they"], "Người làm chủ ngữ → who.", qtype="relative")
C("The laptop, ___ I bought last month, has already broken.", ["which", "that", "who", "what"], "Có dấu phẩy, thay cho vật → which (không dùng that).")
C("The company ___ products we use is based in Korea.", ["whose", "which", "who", "where"], "Sản phẩm CỦA công ty → whose.", qtype="relative")
C("Employees ___ wish to attend the seminar should register by Friday.", ["who", "which", "whose", "whom"], "Thay cho người, làm chủ ngữ → who.", qtype="relative")
E("The man [which] [is] talking [to] my father [is] my teacher.", 0, "who", "Người → who.")
E("Da Lat, [that] [is] famous [for] flowers, [is] beautiful.", 0, "which", "Sau dấu phẩy dùng which.")
E("That is the house [which] my grandfather [built] [it] [in 1970].", 2, "(bỏ it)", "which đã thay cho 'the house' → không lặp lại tân ngữ it.")
O("The book that I am reading is very interesting.", "Cuốn sách tôi đang đọc rất hay.")
O("She is the teacher who helped me most.", "Cô ấy là giáo viên đã giúp tôi nhiều nhất.")

point("gerund_infinitive", 3, "V-ing hay to V", "enjoy/avoid/finish/mind/suggest + V-ing · want/decide/plan/hope/agree + to V",
      "Chọn đúng dạng của động từ theo sau một động từ khác.",
      "Sau giới từ luôn dùng V-ing (interested in learning, before leaving, look forward to meeting). Nhóm + V-ing: enjoy, avoid, finish, mind, suggest, consider, keep, practice, recommend. Nhóm + to V: want, decide, plan, hope, agree, refuse, promise, afford, manage, need, would like. Một số đổi nghĩa: stop doing (ngừng làm) / stop to do (dừng lại để làm); remember doing (nhớ đã làm) / remember to do (nhớ phải làm).",
      ["enjoy + V-ing", "want + to V", "look forward to + V-ing", "stop", "remember"],
      [("I enjoy reading in the evening.", "Tôi thích đọc sách vào buổi tối.", "enjoy reading"),
       ("She decided to study abroad.", "Cô ấy quyết định đi du học.", "decided to study"),
       ("We look forward to meeting you.", "Chúng tôi mong được gặp bạn.", "look forward to meeting")],
      [("I look forward to hear from you.", "I look forward to hearing from you.", "'to' ở đây là giới từ → V-ing."),
       ("She avoided to answer.", "She avoided answering.", "avoid + V-ing.")],
      tags=["gerund_infinitive"], exam=True)
C("I really enjoy ___ to music.", ["listening", "to listen", "listen", "listened"], "enjoy + V-ing.")
C("She decided ___ a new car.", ["to buy", "buying", "buy", "bought"], "decide + to V.")
C("We are looking forward to ___ you next week.", ["seeing", "see", "seen", "be seeing"], "look forward to + V-ing.")
C("Would you mind ___ the window?", ["opening", "to open", "open", "opened"], "mind + V-ing.")
C("He promised ___ me with my homework.", ["to help", "helping", "help", "helped"], "promise + to V.")
C("Thank you for ___ me.", ["helping", "help", "to help", "helped"], "Sau giới từ for → V-ing.")
C("Please remember ___ the lights when you leave.", ["to turn off", "turning off", "turn off", "turned off"], "remember to do = nhớ phải làm (việc chưa làm).")
C("The manager suggested ___ the meeting to Friday.", ["moving", "to move", "move", "moved"], "suggest + V-ing.", qtype="gerund")
C("We cannot afford ___ any more delays.", ["to have", "having", "have", "had"], "afford + to V.", qtype="gerund")
C("Before ___ the office, please turn off your computer.", ["leaving", "leave", "to leave", "left"], "Sau giới từ before → V-ing.", qtype="gerund")
E("I [am] interested [in] [learn] [Japanese].", 2, "learning", "Sau giới từ in → V-ing.")
E("She [avoided] [to answer] [my] [question].", 1, "answering", "avoid + V-ing.")
E("They [agreed] [helping] us [move] [house].", 1, "to help", "agree + to V.")
O("I'm thinking about changing my job.", "Tôi đang nghĩ về việc đổi việc.")
O("She wants to become a doctor.", "Cô ấy muốn trở thành bác sĩ.")

point("conj_prep", 3, "Liên từ hay giới từ (although/despite, because/because of...)", "Liên từ + MỆNH ĐỀ (S + V) · Giới từ + DANH TỪ / V-ing",
      "Dạng câu hỏi xuất hiện nhiều nhất trong Part 5: nhìn sau chỗ trống là mệnh đề hay cụm danh từ.",
      "Cặp nghĩa tương đương: although/though/even though (liên từ) ↔ despite/in spite of (giới từ): mặc dù. because/since/as (liên từ) ↔ because of/due to/owing to (giới từ): vì. while (liên từ) ↔ during (giới từ): trong khi/trong suốt. Mẹo: nhìn ngay sau chỗ trống — có động từ chia → liên từ; chỉ có danh từ → giới từ.",
      ["although", "despite", "because", "because of", "due to", "while", "during"],
      [("Although it was late, she kept working.", "Mặc dù đã muộn, cô ấy vẫn tiếp tục làm việc.", "Although it was late"),
       ("Despite the rain, the match continued.", "Mặc dù trời mưa, trận đấu vẫn tiếp tục.", "Despite the rain"),
       ("The flight was canceled due to bad weather.", "Chuyến bay bị hủy do thời tiết xấu.", "due to bad weather")],
      [("Despite it rained, we went out.", "Although it rained, we went out.", "Sau chỗ trống là mệnh đề → although."),
       ("because the traffic", "because of the traffic", "Sau chỗ trống là cụm danh từ → because of.")],
      tags=["conjunction"], exam=True)
C("___ the high price, the tickets sold out quickly.", ["Despite", "Although", "Even though", "However"], "Sau chỗ trống là cụm danh từ → Despite.", qtype="conj_prep")
C("___ the store was busy, the staff were very patient.", ["Although", "Despite", "In spite of", "Due to"], "Sau chỗ trống là mệnh đề → Although.", qtype="conj_prep")
C("The event was postponed ___ the storm.", ["due to", "because", "although", "while"], "Sau chỗ trống là danh từ → due to.", qtype="conj_prep")
C("Please do not use your phone ___ the presentation.", ["during", "while", "when", "as"], "Sau chỗ trống là danh từ → during.", qtype="conj_prep")
C("Mr. Chen called ___ you were at lunch.", ["while", "during", "despite", "because of"], "Sau chỗ trống là mệnh đề → while.", qtype="conj_prep")
C("She got the job ___ she had little experience.", ["even though", "in spite of", "despite", "due to"], "Sau chỗ trống là mệnh đề → even though.", qtype="conj_prep")
C("Sales increased ___ the successful advertising campaign.", ["because of", "because", "although", "so that"], "Sau chỗ trống là cụm danh từ → because of.", qtype="conj_prep")
C("The road is closed ___ repairs are being made.", ["because", "because of", "due to", "despite"], "Sau chỗ trống là mệnh đề → because.", qtype="conj_prep")
E("[Despite] [it] was raining, [we] [played] football.", 0, "Although", "Sau chỗ trống là mệnh đề → Although.")
E("We [stayed] inside [because] [the heavy] [rain].", 1, "because of", "Sau là cụm danh từ → because of.")
E("[During] I [was] [in Hanoi], I [visited] many museums.", 0, "While", "Sau là mệnh đề → While.")
T("Although he was tired, he finished the work.", ["Despite being tired, he finished the work.", "Because he was tired, he finished the work.", "He was tired, so he didn't finish the work.", "Despite he was tired, he finished the work."], "Although + mệnh đề = Despite + V-ing.")
O("Despite the traffic, we arrived on time.", "Mặc dù kẹt xe, chúng tôi vẫn đến đúng giờ.")

point("agreement", 3, "Hòa hợp chủ ngữ – động từ", "Chủ ngữ số ít → V(s/es) · Chủ ngữ số nhiều → V nguyên mẫu",
      "Chia động từ đúng theo chủ ngữ THẬT của câu.",
      "Bẫy: cụm giới từ chen giữa chủ ngữ và động từ (The list of names IS...). each/every/everyone/nobody → số ít. Danh từ không đếm được → số ít. 'A number of + N số nhiều' → số nhiều; 'The number of...' → số ít. either/neither ... or/nor → chia theo chủ ngữ gần nhất.",
      ["each", "every", "the number of", "a number of", "either...or"],
      [("The list of candidates is on the desk.", "Danh sách ứng viên ở trên bàn.", "is"),
       ("Every student has a laptop.", "Mỗi học sinh đều có laptop.", "has"),
       ("A number of employees are working from home.", "Một số nhân viên đang làm việc tại nhà.", "are")],
      [("The quality of these products are high.", "The quality of these products is high.", "Chủ ngữ thật là 'The quality' (số ít)."),
       ("Everyone have arrived.", "Everyone has arrived.", "everyone → số ít.")],
      tags=["agreement"], exam=True)
C("The price of these shoes ___ too high.", ["is", "are", "were", "be"], "Chủ ngữ thật là 'The price' (số ít) → is.", qtype="agreement")
C("Each of the rooms ___ a private bathroom.", ["has", "have", "having", "are having"], "Each of + N → động từ số ít.", qtype="agreement")
C("The number of tourists ___ increased this year.", ["has", "have", "are", "were"], "The number of → số ít.", qtype="agreement")
C("A number of customers ___ complained about the noise.", ["have", "has", "is", "was"], "A number of + N số nhiều → số nhiều.", qtype="agreement")
C("Neither the manager nor the employees ___ aware of the change.", ["were", "was", "is", "has been"], "Chia theo chủ ngữ gần nhất (the employees) → were.", qtype="agreement")
C("Everyone in the office ___ the new coffee machine.", ["likes", "like", "are liking", "have liked"], "Everyone → số ít.", qtype="agreement")
C("The information you gave me ___ very useful.", ["was", "were", "are", "have been"], "information không đếm được → số ít.", qtype="agreement")
E("The boxes [on] the shelf [is] [full] [of] books.", 1, "are", "Chủ ngữ thật 'The boxes' số nhiều → are.")
E("Every [one] of the students [have] [a] [book].", 1, "has", "Every one of → số ít.")
E("The news [are] [very] [good] [today].", 0, "is", "news là danh từ không đếm được → is.")
O("Each of the students has a different idea.", "Mỗi học sinh có một ý tưởng khác nhau.")

point("word_forms", 3, "Từ loại (danh / động / tính / trạng từ)", "N: -tion, -ment, -ness, -ity · V: -ize, -en, -ify · Adj: -ful, -ous, -ive, -able · Adv: adj + -ly",
      "Dạng phổ biến nhất của TOEIC Part 5: chọn đúng từ loại cho chỗ trống dựa vào vị trí.",
      "Quy tắc vị trí: sau mạo từ/tính từ sở hữu → DANH TỪ (the decision). Trước danh từ → TÍNH TỪ (a successful plan). Sau to be/become/seem → TÍNH TỪ (is available). Bổ nghĩa cho động từ, tính từ, cả câu → TRẠNG TỪ (work efficiently, highly qualified). Giữa have/be và V3 → trạng từ (has recently opened).",
      ["the + N", "adj + N", "be + adj", "V + adv", "have + adv + V3"],
      [("We made a final decision.", "Chúng tôi đã đưa ra quyết định cuối cùng.", "decision"),
       ("The new system works efficiently.", "Hệ thống mới hoạt động hiệu quả.", "efficiently"),
       ("The room is available now.", "Phòng hiện đang trống.", "available")],
      [("She speaks English fluent.", "She speaks English fluently.", "Bổ nghĩa cho động từ speaks → trạng từ."),
       ("This is a success plan.", "This is a successful plan.", "Trước danh từ plan → tính từ.")],
      tags=["word_family", "pos_transform"], exam=True)
C("The manager made an important ___ yesterday.", ["decision", "decide", "decisive", "decisively"], "Sau tính từ important → danh từ.", qtype="pos")
C("Please drive ___ in the school zone.", ["carefully", "careful", "care", "carefulness"], "Bổ nghĩa cho động từ drive → trạng từ.", qtype="pos")
C("Our new product has been very ___.", ["successful", "success", "succeed", "successfully"], "Sau to be (has been very) → tính từ.", qtype="pos")
C("She is a highly ___ engineer.", ["qualified", "qualify", "qualification", "qualifying it"], "Trước danh từ engineer (sau trạng từ highly) → tính từ.", qtype="pos")
C("The company has ___ expanded into Asia.", ["recently", "recent", "recency", "more recent"], "Giữa has và V3 → trạng từ.", qtype="pos")
C("We appreciate your ___ in this matter.", ["cooperation", "cooperate", "cooperative", "cooperatively"], "Sau tính từ sở hữu your → danh từ.", qtype="pos")
C("The instructions are not very ___.", ["clear", "clearly", "clarity", "clarify"], "Sau to be → tính từ.", qtype="pos")
C("Mr. Park will ___ the new staff members on Monday.", ["train", "training", "trainee", "trained"], "Sau will → động từ nguyên mẫu.", qtype="pos")
C("Customer ___ is our top priority.", ["satisfaction", "satisfy", "satisfied", "satisfying"], "Customer satisfaction là danh từ ghép làm chủ ngữ.", qtype="pos")
C("The team worked ___ to meet the deadline.", ["hard", "hardly", "hardness", "harden"], "work hard (hard vừa là adj vừa là adv; hardly = hầu như không).", qtype="pos")
E("She [sings] [beautiful] [at] every [party].", 1, "beautifully", "Bổ nghĩa cho động từ sings → trạng từ.")
E("This [is] a [very] [danger] [road].", 3, "dangerous", "Trước danh từ road → tính từ.")
E("She [thanked] us [for] [our] help and [patient].", 3, "patience", "Song song với danh từ help → cần danh từ patience.")
O("She answered all the questions correctly.", "Cô ấy trả lời đúng tất cả các câu hỏi.")
