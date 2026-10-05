from lib import point, C, E, O, T

point("past_perfect", 4, "Quá khứ hoàn thành", "S + had + V3",
      "Hành động xảy ra TRƯỚC một hành động/mốc khác trong quá khứ.",
      "Khi kể hai việc trong quá khứ, việc xảy ra trước dùng had + V3, việc sau dùng quá khứ đơn. Hay đi với: before, after, by the time, when, already. By the time + quá khứ đơn, S + had + V3.",
      ["before", "after", "by the time", "already", "when"],
      [("When we arrived, the film had already started.", "Khi chúng tôi đến, phim đã bắt đầu rồi.", "had already started"),
       ("She had worked there for ten years before she retired.", "Bà ấy đã làm ở đó mười năm trước khi nghỉ hưu.", "had worked"),
       ("By the time I called, he had left.", "Lúc tôi gọi thì anh ấy đã đi rồi.", "had left")],
      [("When I arrived, the train already left.", "When I arrived, the train had already left.", "Việc xảy ra trước → had + V3."),
       ("I had saw it before.", "I had seen it before.", "had + V3: see → seen.")],
      tags=["tense"], exam=True)
C("When I got to the station, the train ___.", ["had already left", "already leaves", "has already left", "is leaving"], "Tàu đi TRƯỚC khi tôi đến → quá khứ hoàn thành.")
C("By the time the manager arrived, we ___ the meeting.", ["had started", "start", "have started", "will start"], "By the time + QKĐ → had + V3.", qtype="tense")
C("She told me she ___ the movie before.", ["had seen", "has seen", "sees", "was seeing"], "Đã xem trước thời điểm kể lại → had seen.")
C("After he ___ his homework, he went out to play.", ["had finished", "has finished", "finishes", "will finish"], "Làm bài xong trước rồi mới đi chơi → had finished.")
C("I didn't recognize her because she ___ so much.", ["had changed", "has changed", "changes", "is changing"], "Thay đổi xảy ra trước lúc gặp → had changed.")
C("The report ___ before the director requested it.", ["had been completed", "has completed", "completes", "is completing"], "Bị động + quá khứ hoàn thành → had been completed.", qtype="tense")
E("When we [got] home, [someone] [has broken] [into] the house.", 2, "had broken", "Việc xảy ra trước một mốc quá khứ → had broken.")
E("She [had] [went] to bed [before] I [called].", 1, "gone", "had + V3: go → gone.")
O("I had never seen snow before I went to Japan.", "Tôi chưa từng thấy tuyết trước khi đến Nhật.")
O("By the time we arrived, the party had ended.", "Lúc chúng tôi đến thì bữa tiệc đã kết thúc.")

point("conditional3", 4, "Câu điều kiện loại 3 và hỗn hợp", "If + had + V3, would have + V3 · Hỗn hợp: If + had + V3, would + V (now)",
      "Giả định trái với quá khứ (tiếc nuối), và giả định quá khứ ảnh hưởng hiện tại.",
      "Loại 3: điều đã không xảy ra trong quá khứ (If I had studied, I would have passed). Hỗn hợp: điều kiện quá khứ → kết quả hiện tại (If I had taken that job, I would be rich now). Đảo ngữ trang trọng: Had I known = If I had known.",
      ["If ... had + V3", "would have + V3", "Had + S + V3", "now"],
      [("If I had left earlier, I wouldn't have missed the bus.", "Nếu tôi đi sớm hơn thì đã không lỡ xe buýt.", "had left ... wouldn't have missed"),
       ("If she had studied medicine, she would be a doctor now.", "Nếu cô ấy đã học y thì giờ đã là bác sĩ.", "had studied ... would be"),
       ("Had I known, I would have helped.", "Nếu tôi biết thì tôi đã giúp rồi.", "Had I known")],
      [("If I would have known, I would have come.", "If I had known, I would have come.", "Mệnh đề if không dùng would."),
       ("If he had asked me, I would help him.", "If he had asked me, I would have helped him.", "Kết quả trong quá khứ → would have + V3.")],
      tags=["conditional"], exam=True)
C("If I ___ about the meeting, I would have attended.", ["had known", "knew", "would know", "have known"], "Loại 3: If + had + V3.")
C("If we had left earlier, we ___ the flight.", ["wouldn't have missed", "won't miss", "didn't miss", "wouldn't miss"], "Loại 3: would have + V3.")
C("If she had taken the job in Singapore, she ___ there now.", ["would be living", "would have lived", "will live", "lived"], "Hỗn hợp: quá khứ → hiện tại (now) → would + V.")
C("___ the weather been better, the event would have been held outdoors.", ["Had", "If", "Were", "Should"], "Đảo ngữ loại 3: Had + S + V3.", qtype="conditional")
C("You would have passed if you ___ harder.", ["had studied", "studied", "would study", "have studied"], "Loại 3: If + had + V3.")
E("If I [would have] [known], I [would have] [called] you.", 0, "had", "Mệnh đề if loại 3: If I had known.")
E("If he [had] [listened] to me, he [wouldn't] [make] that mistake.", 3, "have made", "Kết quả quá khứ → wouldn't have made.")
T("I didn't know her number, so I didn't call her.", ["If I had known her number, I would have called her.", "If I knew her number, I would call her.", "If I had known her number, I would call her yesterday.", "Unless I knew her number, I called her."], "Giả định trái quá khứ → điều kiện loại 3.")
O("If I had saved more money, I would have bought that laptop.", "Nếu tôi tiết kiệm nhiều hơn thì đã mua được chiếc laptop đó.")

point("reported", 4, "Câu tường thuật", "said (that) + lùi thì · asked if/whether · told sb to V",
      "Kể lại lời người khác nói.",
      "Khi động từ tường thuật ở quá khứ (said, told), lùi một thì: am/is → was, will → would, can → could, hiện tại đơn → quá khứ đơn, quá khứ đơn → quá khứ hoàn thành. Đổi đại từ và trạng từ: now → then, today → that day, tomorrow → the next day, here → there. Câu hỏi tường thuật dùng trật tự câu KHẲNG ĐỊNH: She asked where I lived (không phải where did I live).",
      ["said", "told", "asked", "if/whether"],
      [("He said that he was tired.", "Anh ấy nói rằng anh ấy mệt.", "said that he was"),
       ("She asked me where I lived.", "Cô ấy hỏi tôi sống ở đâu.", "where I lived"),
       ("The teacher told us to be quiet.", "Cô giáo bảo chúng tôi giữ trật tự.", "told us to be")],
      [("She asked where did I live.", "She asked where I lived.", "Câu hỏi tường thuật dùng trật tự khẳng định."),
       ("He said me that...", "He told me that... / He said that...", "said không đi trực tiếp với tân ngữ người.")],
      tags=["reported_speech"], exam=True)
C("She said that she ___ busy that day.", ["was", "is", "will be", "has been"], "Lùi thì: is → was.")
C("He asked me where I ___.", ["worked", "did work", "do I work", "did I work"], "Câu hỏi tường thuật: trật tự khẳng định + lùi thì.")
C("The doctor ___ me to get more rest.", ["told", "said", "spoke", "talked"], "told + sb + to V.")
C("They asked ___ we could join them for dinner.", ["if", "that", "what", "which"], "Câu hỏi Yes/No tường thuật → if/whether.")
C("Mai said she ___ call me the next day.", ["would", "will", "can", "shall"], "Lùi thì: will → would.")
E("She [asked] me [where] [did I] [go] last weekend.", 2, "I had gone", "Câu hỏi tường thuật: where I had gone.")
E("He [said] [me] [that] he [was] tired.", 1, "told me / said (bỏ me)", "said không đi trực tiếp với tân ngữ người.")
T("\"I'm learning Japanese,\" said Lan.", ["Lan said that she was learning Japanese.", "Lan said that I am learning Japanese.", "Lan told that she learns Japanese.", "Lan said she had learned Japanese."], "Lùi thì: am learning → was learning; I → she.")
O("She asked me if I liked spicy food.", "Cô ấy hỏi tôi có thích đồ cay không.")

point("participle", 4, "Mệnh đề phân từ (rút gọn)", "V-ing (chủ động) · V3 (bị động) · Having + V3 (xảy ra trước)",
      "Rút gọn mệnh đề quan hệ hoặc trạng ngữ cho câu gọn hơn — rất hay gặp ở Part 5, 6.",
      "Rút gọn mệnh đề quan hệ: chủ động → V-ing (the man sitting there = who is sitting); bị động → V3 (the products made in Vietnam = which are made). Rút gọn trạng ngữ khi hai mệnh đề cùng chủ ngữ: Walking home, I met an old friend. Having finished the work, she went home (việc xảy ra trước).",
      ["V-ing", "V3", "Having + V3", "when/while + V-ing"],
      [("The woman sitting next to me is a lawyer.", "Người phụ nữ ngồi cạnh tôi là luật sư.", "sitting"),
       ("Products made in Vietnam are popular.", "Hàng sản xuất tại Việt Nam rất được ưa chuộng.", "made"),
       ("Having finished her work, she went home.", "Làm xong việc, cô ấy về nhà.", "Having finished")],
      [("The report writing by Mr. Kim...", "The report written by Mr. Kim...", "Báo cáo được viết → V3."),
       ("Walking home, the rain started.", "While I was walking home, the rain started.", "Hai vế phải cùng chủ ngữ.")],
      tags=["participle"], exam=True)
C("The documents ___ by the lawyer are on your desk.", ["prepared", "preparing", "prepare", "to prepare"], "Tài liệu được chuẩn bị (bị động) → prepared.", qtype="participle")
C("Employees ___ to attend the training should register online.", ["wishing", "wished", "wish", "are wishing"], "Nhân viên mong muốn (chủ động) → wishing (= who wish).", qtype="participle")
C("___ the instructions, he assembled the desk easily.", ["Having read", "Read", "Being read", "To have read"], "Đọc xong rồi mới lắp → Having read.", qtype="participle")
C("Any items ___ in the room will be kept at reception.", ["left", "leaving", "leave", "to leave"], "Đồ bị bỏ lại (bị động) → left.", qtype="participle")
C("The man ___ to the manager is our new client.", ["talking", "talked", "talks", "is talking"], "Đang nói (chủ động) → talking.")
C("When ___ abroad, always carry a copy of your passport.", ["traveling", "traveled", "travel", "to travel"], "When + V-ing (chủ ngữ ẩn là you, chủ động).", qtype="participle")
E("The [letter] [writing] by my grandfather [is] [very] old.", 1, "written", "Bức thư được viết → written.")
E("[Looking] out of the window, [the] [mountains] [were] beautiful.", 0, "(chủ ngữ phải là người nhìn) When I looked", "Hai vế khác chủ ngữ: mountains không thể 'look'.")
O("The man standing at the door is my uncle.", "Người đàn ông đứng ở cửa là chú tôi.")

point("wish", 4, "Wish / If only", "wish + QKĐ (hiện tại) · wish + had V3 (quá khứ) · wish + would (muốn người khác thay đổi)",
      "Bày tỏ mong ước trái với thực tế.",
      "Ước điều trái hiện tại: I wish I had more time (thực tế: không có). Với to be dùng were: I wish I were taller. Ước điều trái quá khứ: I wish I had studied harder. Muốn ai/cái gì thay đổi: I wish it would stop raining. If only mang nghĩa giống wish nhưng mạnh hơn.",
      ["wish", "if only", "would"],
      [("I wish I spoke English fluently.", "Ước gì tôi nói tiếng Anh trôi chảy.", "wish I spoke"),
       ("She wishes she hadn't said that.", "Cô ấy ước mình đã không nói điều đó.", "hadn't said"),
       ("If only it would stop raining!", "Giá mà trời tạnh mưa!", "would stop")],
      [("I wish I am rich.", "I wish I were rich.", "Ước trái hiện tại → lùi về quá khứ (were)."),
       ("I wish I didn't eat so much last night.", "I wish I hadn't eaten so much last night.", "Ước về quá khứ → had + V3.")],
      tags=["wish"])
C("I wish I ___ how to swim.", ["knew", "know", "will know", "have known"], "Ước trái hiện tại → quá khứ đơn.")
C("She wishes she ___ the bus this morning.", ["hadn't missed", "didn't miss", "doesn't miss", "won't miss"], "Ước về quá khứ (this morning) → had + V3.")
C("If only I ___ taller!", ["were", "am", "will be", "be"], "If only + were.")
C("I wish my neighbors ___ so much noise at night.", ["wouldn't make", "don't make", "won't make", "haven't made"], "Muốn người khác thay đổi → would.")
E("I wish I [can] [speak] [Korean] [well].", 0, "could", "Ước trái hiện tại → could.")
E("He wishes he [didn't] [spend] [all his money] [last month].", 0, "hadn't spent", "Ước về quá khứ → hadn't spent.")
O("I wish I had more free time.", "Ước gì tôi có nhiều thời gian rảnh hơn.")

point("inversion_basic", 4, "Đảo ngữ cơ bản (So/Neither, Not only, Never...)", "So/Neither + trợ động từ + S · Not only + trợ ĐT + S + V, but also...",
      "Nói 'cũng vậy', nhấn mạnh trong văn viết trang trọng.",
      "Đồng tình khẳng định: So + trợ động từ + S (I like tea. — So do I). Đồng tình phủ định: Neither/Nor + trợ động từ + S (I can't swim. — Neither can I). Nhấn mạnh: khi câu bắt đầu bằng Never, Rarely, Seldom, Not only, Only then... → đảo trợ động từ lên trước chủ ngữ.",
      ["So do I", "Neither can I", "Never have I", "Not only ... but also"],
      [("I love durian. — So do I.", "Tôi thích sầu riêng. — Tôi cũng vậy.", "So do I"),
       ("She didn't come. — Neither did he.", "Cô ấy không đến. — Anh ấy cũng không.", "Neither did he"),
       ("Never have I seen such a beautiful sunset.", "Chưa bao giờ tôi thấy hoàng hôn đẹp như vậy.", "Never have I seen")],
      [("I'm tired. — So I am.", "I'm tired. — So am I.", "Đảo trợ động từ lên trước chủ ngữ."),
       ("Never I have seen this.", "Never have I seen this.", "Never đứng đầu câu → đảo ngữ.")],
      tags=["inversion"], exam=True)
C("I can't speak French. — ___ can I.", ["Neither", "So", "Either", "Too"], "Đồng tình phủ định → Neither can I.")
C("My sister likes K-pop, and so ___ I.", ["do", "am", "does", "like"], "Đồng tình khẳng định với like → so do I.")
C("Not only ___ the report on time, but she also presented it brilliantly.", ["did she finish", "she finished", "she did finish", "finished she"], "Not only đứng đầu → đảo trợ động từ: did she finish.", qtype="inversion")
C("Rarely ___ such an excellent candidate.", ["have we interviewed", "we have interviewed", "we interviewed", "interviewed we"], "Rarely đứng đầu → đảo ngữ.", qtype="inversion")
E("I [was] tired, and [so] [my brother] [was].", 2, "was my brother (so was my brother)", "So + trợ động từ + S.")
E("[Never] [I have] [heard] [such] a funny story.", 1, "have I", "Never đứng đầu câu → đảo ngữ: Never have I heard.")
O("She doesn't eat meat, and neither do I.", "Cô ấy không ăn thịt, tôi cũng không.")

point("modal_perfect", 4, "Động từ khuyết thiếu hoàn thành (should have, must have...)", "should/could/might/must/can't + have + V3",
      "Tiếc nuối, phỏng đoán về quá khứ.",
      "should have V3: lẽ ra nên (nhưng đã không). shouldn't have V3: lẽ ra không nên (nhưng đã làm). must have V3: chắc hẳn đã (suy luận chắc chắn). can't/couldn't have V3: không thể nào đã. might/may/could have V3: có lẽ đã.",
      ["should have", "must have", "might have", "can't have"],
      [("You should have told me earlier.", "Lẽ ra bạn nên nói với tôi sớm hơn.", "should have told"),
       ("He must have forgotten the meeting.", "Chắc hẳn anh ấy đã quên cuộc họp.", "must have forgotten"),
       ("She can't have finished already.", "Cô ấy không thể nào đã làm xong rồi.", "can't have finished")],
      [("You should told me.", "You should have told me.", "should have + V3."),
       ("He must has left.", "He must have left.", "Sau must luôn là have (nguyên mẫu).")],
      tags=["modal"])
C("The streets are wet. It ___ rained last night.", ["must have", "should have", "can't have", "mustn't"], "Suy luận chắc chắn về quá khứ → must have.")
C("I failed the test. I ___ studied harder.", ["should have", "must have", "can't have", "would"], "Tiếc nuối → should have.")
C("She ___ seen you. She was in Hanoi all week.", ["can't have", "must have", "should have", "needn't"], "Không thể nào → can't have.")
C("I'm not sure where my phone is. I ___ left it in the taxi.", ["might have", "must", "should have", "can't have"], "Phỏng đoán không chắc → might have.")
E("You [should] [have] [came] [earlier].", 2, "come", "should have + V3: come.")
E("He [must] [has] [taken] [the wrong bus].", 1, "have", "must + have (nguyên mẫu).")
O("You should have brought an umbrella.", "Lẽ ra bạn nên mang ô.")

point("linking", 4, "Từ nối & dấu hiệu diễn ngôn", "however · therefore · moreover · otherwise · as a result · in addition · for example",
      "Nối ý giữa các câu — trọng tâm của Part 6 (hoàn thành đoạn văn).",
      "Đối lập: however, nevertheless, on the other hand. Kết quả: therefore, as a result, consequently. Bổ sung: moreover, furthermore, in addition, also. Điều kiện: otherwise (nếu không thì). Ví dụ: for example, for instance. Những từ này thường đứng đầu câu, theo sau là dấu phẩy, và nối HAI CÂU (không nối hai mệnh đề như liên từ).",
      ["However,", "Therefore,", "Moreover,", "Otherwise,", "As a result,"],
      [("The hotel was expensive. However, the service was excellent.", "Khách sạn đắt. Tuy nhiên, dịch vụ rất tuyệt.", "However"),
       ("It rained heavily. As a result, the match was canceled.", "Trời mưa to. Kết quả là trận đấu bị hủy.", "As a result"),
       ("Please reply by Friday. Otherwise, we will give the seat to someone else.", "Vui lòng trả lời trước thứ Sáu. Nếu không, chúng tôi sẽ nhường chỗ cho người khác.", "Otherwise")],
      [("The plan is good, however it is expensive.", "The plan is good. However, it is expensive.", "however không nối hai mệnh đề bằng dấu phẩy."),
       ("He was sick. Therefore he came to work.", "He was sick. However, he came to work.", "Ý đối lập → However.")],
      tags=["discourse"], exam=True)
C("The flight was delayed. ___, we missed our connection.", ["As a result", "However", "For example", "Otherwise"], "Kết quả của việc trễ chuyến → As a result.", qtype="linking")
C("The software is easy to use. ___, it is very affordable.", ["Moreover", "However", "Otherwise", "Instead"], "Bổ sung ý tích cực → Moreover.", qtype="linking")
C("Please save your work often. ___, you may lose your data.", ["Otherwise", "Therefore", "Moreover", "For instance"], "Nếu không thì → Otherwise.", qtype="linking")
C("The product is popular in Asia. ___, sales in Europe remain low.", ["However", "Therefore", "In addition", "As a result"], "Đối lập → However.", qtype="linking")
C("Many companies now allow remote work. ___, our firm lets staff work from home twice a week.", ["For example", "However", "Otherwise", "Nevertheless"], "Đưa ví dụ → For example.", qtype="linking")
C("Our costs have risen sharply. ___, we must increase our prices.", ["Therefore", "However", "Otherwise", "For example"], "Hệ quả tất yếu → Therefore.", qtype="linking")
E("The room [was] small. [Therefore], it was [very] [comfortable].", 1, "However", "Ý đối lập → However.")
O("The food was cheap. However, it was delicious.", "Đồ ăn rẻ. Tuy vậy, nó rất ngon.")
