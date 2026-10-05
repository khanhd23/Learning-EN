from lib import point, C, E, O, T, M

point("noun_phrase", 5, "Cụm danh từ phức", "det + adv + adj + N + N (+ prep phrase)",
      "Đọc hiểu và chọn từ đúng trong các cụm danh từ dài của văn bản công việc.",
      "Trật tự: từ hạn định (the, our) + trạng từ (highly) + tính từ (experienced) + danh từ bổ nghĩa (sales) + DANH TỪ CHÍNH (team). Danh từ đứng trước danh từ đóng vai trò như tính từ và thường ở số ít: a ten-minute break, a customer service desk.",
      ["the + adv + adj + N + N", "N + N", "ten-minute", "five-star"],
      [("our highly experienced sales team", "đội ngũ bán hàng giàu kinh nghiệm của chúng tôi", "highly experienced sales team"),
       ("a two-week training program", "một chương trình đào tạo kéo dài hai tuần", "two-week"),
       ("the company's annual performance review", "đợt đánh giá hiệu suất hằng năm của công ty", "annual performance review")],
      [("a two-weeks holiday", "a two-week holiday", "Tính từ ghép với số không thêm -s."),
       ("a customers service desk", "a customer service desk", "Danh từ bổ nghĩa thường ở số ít.")],
      tags=["noun_phrase"], exam=True)
C("We offer a ___ guarantee on all products.", ["two-year", "two-years", "two years'", "two-yearly"], "Tính từ ghép số + danh từ: không thêm s → two-year.", qtype="noun_phrase")
C("The hotel has a ___ restaurant on the top floor.", ["five-star", "five-stars", "five stars", "fifth-star"], "Tính từ ghép → five-star.", qtype="noun_phrase")
C("Please contact our ___ department for help.", ["customer service", "customers serving", "customer's served", "customerly service"], "Danh từ ghép customer service.", qtype="noun_phrase")
C("The ___ trained staff handled the problem quickly.", ["highly", "high", "height", "higher"], "Bổ nghĩa cho tính từ trained → trạng từ highly.", qtype="pos")
E("We took [a] [fifteen-minutes] [coffee] [break].", 1, "fifteen-minute", "Tính từ ghép số không thêm -s.")
O("Our highly experienced team will help you.", "Đội ngũ giàu kinh nghiệm của chúng tôi sẽ giúp bạn.")

point("subjunctive", 5, "Thể giả định (suggest/recommend that + S + V nguyên mẫu)", "suggest / recommend / insist / require / It is essential that + S + V (nguyên mẫu)",
      "Văn phong trang trọng: đề nghị, yêu cầu, điều cần thiết.",
      "Sau các động từ suggest, recommend, insist, demand, request, require và các cụm It is important/essential/necessary that..., động từ trong mệnh đề that ở dạng NGUYÊN MẪU cho mọi chủ ngữ (không thêm -s, không chia thì). Phủ định: not + V (that he not be late). Anh-Anh có thể dùng should + V.",
      ["suggest that", "recommend that", "It is essential that", "insist that"],
      [("The doctor recommended that he rest for a week.", "Bác sĩ khuyên anh ấy nên nghỉ một tuần.", "he rest"),
       ("It is essential that every employee attend the meeting.", "Mọi nhân viên bắt buộc phải tham dự cuộc họp.", "every employee attend"),
       ("She insisted that we be on time.", "Cô ấy nhất quyết yêu cầu chúng tôi đến đúng giờ.", "we be")],
      [("I suggest that he goes home.", "I suggest that he go home.", "Thể giả định: động từ nguyên mẫu."),
       ("It is important that she is here.", "It is important that she be here.", "Dùng be cho mọi chủ ngữ.")],
      tags=["subjunctive"], exam=True)
C("The manager suggested that Mr. Tan ___ the report by Friday.", ["submit", "submits", "submitted", "submitting"], "suggest that + S + V nguyên mẫu.", qtype="subjunctive")
C("It is essential that all passengers ___ their seatbelts.", ["fasten", "fastens", "fastened", "will fasten"], "It is essential that + V nguyên mẫu.", qtype="subjunctive")
C("The landlord requires that the rent ___ paid on time.", ["be", "is", "was", "being"], "require that + be + V3.", qtype="subjunctive")
C("We recommend that she ___ a lawyer before signing.", ["consult", "consults", "consulted", "consulting"], "recommend that + V nguyên mẫu.", qtype="subjunctive")
E("The doctor [recommended] that [she] [drinks] more [water].", 2, "drink", "recommend that + V nguyên mẫu.")
O("I suggest that he apply for the scholarship.", "Tôi đề nghị anh ấy nộp đơn xin học bổng.")

point("cleft", 5, "Câu chẻ (It is ... that / What ... is)", "It is/was + X + that/who... · What + S + V + is/was + X",
      "Nhấn mạnh một thành phần của câu.",
      "It is/was + phần cần nhấn mạnh + that/who + phần còn lại: It was Lan who won the prize (Chính Lan là người đoạt giải). What-cleft: What I need is a holiday (Điều tôi cần là một kỳ nghỉ).",
      ["It is ... that", "It was ... who", "What I need is"],
      [("It was my mother who taught me to cook.", "Chính mẹ là người dạy tôi nấu ăn.", "It was my mother who"),
       ("What I love about Hanoi is the food.", "Điều tôi yêu ở Hà Nội là đồ ăn.", "What I love ... is"),
       ("It is in the morning that I study best.", "Chính vào buổi sáng là lúc tôi học hiệu quả nhất.", "It is in the morning that")],
      [("It was him that he called me.", "It was he who called me. / It was him who called me.", "Không lặp chủ ngữ sau who/that."),
       ("What I need it is sleep.", "What I need is sleep.", "Không thêm it.")],
      tags=["cleft"])
C("It was the manager ___ approved the budget.", ["who", "which", "what", "where"], "Nhấn mạnh người → who (hoặc that).")
C("___ I need right now is a cup of coffee.", ["What", "That", "Which", "It"], "What-cleft: What I need is...")
C("It was in 2010 ___ they opened their first shop.", ["that", "which", "when it", "what"], "It was + thời gian + that.")
E("[What] I [want] [it] [is] a quiet weekend.", 2, "(bỏ it)", "What I want is..., không thêm it.")
O("It was my teacher who encouraged me to read more.", "Chính cô giáo là người khuyến khích tôi đọc nhiều hơn.")

point("inversion_adv", 5, "Đảo ngữ nâng cao (Only after, No sooner, Hardly, Should...)", "No sooner had S V3 than... · Hardly had S V3 when... · Only after/when... + đảo · Should + S + V (= If)",
      "Văn viết trang trọng, câu hỏi khó trong các bài thi.",
      "No sooner had + S + V3 + than + QKĐ (vừa mới... thì). Hardly/Scarcely had + S + V3 + when + QKĐ. Only after/when/if + mệnh đề/cụm + đảo ở mệnh đề chính. Under no circumstances + đảo (tuyệt đối không). Should + S + V = If + S + should + V (điều kiện loại 1 trang trọng, rất hay gặp trong email công việc).",
      ["No sooner ... than", "Hardly ... when", "Only after", "Under no circumstances", "Should you..."],
      [("No sooner had I arrived than it started to rain.", "Tôi vừa đến thì trời bắt đầu mưa.", "No sooner had I arrived than"),
       ("Only after the meeting did I understand the plan.", "Chỉ sau cuộc họp tôi mới hiểu kế hoạch.", "did I understand"),
       ("Should you have any questions, please contact us.", "Nếu bạn có câu hỏi, vui lòng liên hệ chúng tôi.", "Should you have")],
      [("No sooner I had arrived than...", "No sooner had I arrived than...", "Đảo had lên trước chủ ngữ."),
       ("Hardly had she left than...", "Hardly had she left when...", "Hardly ... when (không dùng than).")],
      tags=["inversion"], exam=True)
C("___ you need further information, please call our office.", ["Should", "Had", "Were", "Would"], "Should + S + V = If you need...", qtype="inversion")
C("No sooner had we sat down ___ the phone rang.", ["than", "when", "then", "that"], "No sooner ... than.", qtype="inversion")
C("Hardly had the show started ___ the power went out.", ["when", "than", "then", "so"], "Hardly ... when.", qtype="inversion")
C("Under no circumstances ___ leave the door unlocked.", ["should you", "you should", "you shouldn't", "shouldn't you"], "Under no circumstances + đảo ngữ (đã mang nghĩa phủ định).", qtype="inversion")
C("Only after checking all the figures ___ the report.", ["did she send", "she sent", "she did send", "sent she"], "Only after + cụm → đảo ở mệnh đề chính.", qtype="inversion")
E("No sooner [I had] [finished] my meal [than] the bill [arrived].", 0, "had I", "No sooner had I finished...")
O("Should you need any help, please ask our staff.", "Nếu cần giúp đỡ, vui lòng hỏi nhân viên của chúng tôi.")

point("ellipsis", 5, "Tỉnh lược & thay thế (so, not, one, do so)", "I think so / I hope not · one/ones · do so · trợ động từ thay cho cả cụm",
      "Tránh lặp từ, nói tự nhiên như người bản xứ.",
      "so/not thay cho mệnh đề sau think, hope, expect, guess, be afraid: I think so / I hope not. one/ones thay cho danh từ đếm được đã nhắc: I like the red one. do so thay cho cụm động từ (văn trang trọng). Trợ động từ thay cho cụm động từ: She can swim, but I can't (swim).",
      ["I think so", "I hope not", "the blue one", "do so", "I can't"],
      [("Will it rain tomorrow? — I hope not.", "Mai có mưa không? — Mong là không.", "I hope not"),
       ("I don't like this shirt. Can I see the blue one?", "Tôi không thích cái áo này. Cho tôi xem cái màu xanh?", "the blue one"),
       ("She said she would call, and she did.", "Cô ấy bảo sẽ gọi, và cô ấy đã gọi.", "she did")],
      [("I think yes.", "I think so.", "Dùng so sau think."),
       ("I don't hope so.", "I hope not.", "Với hope dùng hope not.")],
      tags=["ellipsis"])
C("Is the shop open on Sunday? — I think ___.", ["so", "yes", "it", "that"], "I think so.")
C("Will we have to work this weekend? — I hope ___.", ["not", "no", "don't", "so not"], "Mong là không → I hope not.")
C("These shoes are too small. Do you have bigger ___?", ["ones", "one", "it", "them"], "Thay cho danh từ số nhiều đếm được → ones.")
C("Tom can play the piano, but his sister ___.", ["can't", "doesn't", "isn't", "can't play it"], "Trợ động từ thay cho cả cụm: can't (play the piano).")
E("Do you think she'll come? — I [think] [yes], [but] [I'm] not sure.", 1, "so", "I think so.")
O("I wanted to call you, but I forgot to.", "Tôi định gọi cho bạn nhưng quên mất.")

point("hedging", 5, "Sắc thái & nói giảm (hedging)", "may/might/could · seem/appear · tend to · likely · It is possible that",
      "Diễn đạt mức độ chắc chắn — quan trọng trong viết học thuật và giao tiếp lịch sự.",
      "Người bản xứ hiếm khi khẳng định tuyệt đối. Dùng may/might/could (có thể), seem/appear to (dường như), tend to (có xu hướng), be likely/unlikely to (có khả năng/ít khả năng), It is possible that... để câu nói mềm và chính xác hơn. Lịch sự: I was wondering if you could... nhẹ nhàng hơn Can you...?",
      ["may", "might", "seem to", "tend to", "likely", "I was wondering if"],
      [("The results seem to suggest a link.", "Kết quả dường như gợi ý có mối liên hệ.", "seem to suggest"),
       ("Prices are likely to rise next year.", "Giá có khả năng sẽ tăng vào năm sau.", "are likely to"),
       ("I was wondering if you could help me.", "Không biết bạn có thể giúp tôi được không.", "I was wondering if")],
      [("Prices are likely rise.", "Prices are likely to rise.", "be likely + to V."),
       ("Students tend studying at night.", "Students tend to study at night.", "tend + to V.")],
      tags=["hedging"])
C("Young people ___ to spend more time online.", ["tend", "tends", "tending", "are tend"], "Young people (số nhiều) + tend to V.")
C("The project is ___ to be finished by June.", ["likely", "like", "likelihood", "liking"], "be likely to V = có khả năng.")
C("I was ___ if you could send me the file.", ["wondering", "wonder", "wondered", "to wonder"], "Cách hỏi lịch sự: I was wondering if...")
C("The new policy ___ to have improved staff morale.", ["appears", "appear", "appearing", "is appear"], "The new policy (số ít) + appears to have V3.")
E("Sales [are] [likely] [increase] [next] quarter.", 2, "to increase", "be likely to + V.")
O("It seems that the meeting has been canceled.", "Có vẻ như cuộc họp đã bị hủy.")

point("register", 5, "Văn phong trang trọng & thân mật", "formal: request, assist, purchase, inform · informal: ask, help, buy, tell",
      "Chọn từ phù hợp hoàn cảnh: email công việc, thư xin việc hay tin nhắn bạn bè.",
      "Văn phong trang trọng dùng từ gốc Latin dài hơn (purchase thay buy, assist thay help, require thay need, inform thay tell), không viết tắt (I am thay I'm), dùng bị động và câu đầy đủ. Thân mật dùng cụm động từ (find out, get), viết tắt, câu ngắn. Email công việc: Dear Mr./Ms. + họ; kết thư: Best regards / Sincerely.",
      ["purchase", "assist", "require", "inform", "Dear Ms.", "Best regards"],
      [("Please do not hesitate to contact us.", "Xin đừng ngần ngại liên hệ với chúng tôi.", "do not hesitate"),
       ("We regret to inform you that the position has been filled.", "Chúng tôi rất tiếc phải thông báo rằng vị trí đã có người.", "regret to inform"),
       ("Hey, can you send me that file?", "Ê, gửi mình cái file đó nhé?", "Hey")],
      [("Hi boss, gimme the report pls.", "Dear Mr. Lee, could you please send me the report?", "Email công việc cần văn phong lịch sự, đầy đủ."),
       ("We are sorry to tell you...", "We regret to inform you...", "Thư trang trọng dùng regret to inform.")],
      tags=["register"])
C("(Email trang trọng) We would like to ___ you that your order has been shipped.", ["inform", "tell to", "say", "speak"], "Văn phong trang trọng: inform sb that...")
C("(Email trang trọng) Please do not ___ to contact me if you have any questions.", ["hesitate", "wait", "stop", "forget"], "Cụm cố định: do not hesitate to contact.")
C("(Thư xin việc) I am writing to ___ for the position of sales assistant.", ["apply", "ask", "get", "want"], "apply for a position = ứng tuyển.")
M("Which phrase is the most formal way to end a business email?", ["Sincerely,", "See ya!", "Cheers mate,", "Bye for now!"], "Sincerely / Best regards là cách kết thư trang trọng.")
E("(Email công việc) [Dear] Mr. Brown, I [wanna] [ask] about [the meeting].", 1, "would like to", "Email trang trọng không dùng wanna.")
O("We look forward to hearing from you.", "Chúng tôi mong nhận được phản hồi từ bạn.")
