from lib import point, C, E, O, T

point("be", 1, "Động từ to be (am / is / are)", "I am · He/She/It is · You/We/They are",
      "Giới thiệu bản thân, mô tả tính chất, nghề nghiệp, vị trí, cảm xúc.",
      "To be nghĩa là 'thì, là, ở'. I đi với am; he, she, it và danh từ số ít đi với is; you, we, they và danh từ số nhiều đi với are. Phủ định thêm not (isn't, aren't). Câu hỏi đảo to be lên trước chủ ngữ: Are you ready?",
      ["I'm", "isn't", "aren't", "Are you...?"],
      [("I am a student.", "Tôi là học sinh.", "am"),
       ("My parents are at home.", "Bố mẹ tôi đang ở nhà.", "are"),
       ("Is she your sister?", "Cô ấy là chị gái bạn à?", "Is")],
      [("She are my friend.", "She is my friend.", "She là số ít → is."),
       ("I very happy.", "I am very happy.", "Tiếng Việt không cần 'là' trước tính từ, nhưng tiếng Anh BẮT BUỘC có to be.")],
      tags=["agreement"])
C("My brother ___ a doctor.", ["is", "are", "am", "be"], "My brother = he → is.")
C("They ___ from Da Nang.", ["are", "is", "am", "be"], "They đi với are.")
C("I ___ not hungry right now.", ["am", "is", "are", "be"], "I đi với am.")
C("___ your parents at home today?", ["Are", "Is", "Am", "Do"], "Câu hỏi với chủ ngữ số nhiều (your parents) → Are.")
C("The coffee ___ too hot.", ["is", "are", "am", "be"], "The coffee là danh từ không đếm được → is.")
C("We ___ very excited about the trip.", ["are", "is", "am", "be"], "We đi với are.")
C("My keys ___ on the table.", ["are", "is", "am", "be"], "My keys là số nhiều → are.")
C("It ___ cold today. Wear a jacket.", ["is", "are", "am", "be"], "It đi với is.")
E("She [are] [a] good [teacher] [at] our school.", 0, "is", "She là số ít → is.")
E("My friends [is] [very] friendly [to] [everyone].", 0, "are", "My friends là số nhiều → are.")
E("I [tired] [today] because I [didn't] [sleep].", 0, "am tired", "Thiếu to be trước tính từ: I am tired.")
O("Are you ready for the test?", "Bạn đã sẵn sàng cho bài kiểm tra chưa?")
O("My sister is a nurse.", "Chị gái tôi là y tá.")
O("They are not at school today.", "Hôm nay họ không ở trường.")

point("present_simple", 1, "Thì hiện tại đơn", "S + V(s/es) · S + do/does + not + V · Do/Does + S + V?",
      "Thói quen, việc lặp lại, sự thật hiển nhiên, lịch trình cố định.",
      "Chủ ngữ he/she/it (số ít): động từ thêm -s/-es (works, watches, goes). Phủ định và câu hỏi dùng do/does + động từ NGUYÊN MẪU (doesn't work, Does she work?). Khi đã có does thì động từ không thêm -s nữa.",
      ["always", "usually", "often", "sometimes", "never", "every day", "on Mondays"],
      [("She works in a bank.", "Cô ấy làm ở ngân hàng.", "works"),
       ("I don't drink coffee.", "Tôi không uống cà phê.", "don't drink"),
       ("Does he play football?", "Anh ấy có chơi bóng đá không?", "Does ... play")],
      [("He work every day.", "He works every day.", "He/She/It → động từ thêm -s."),
       ("She doesn't likes tea.", "She doesn't like tea.", "Sau doesn't dùng động từ nguyên mẫu.")],
      tags=["tense", "agreement"], exam=True)
C("She ___ to work by bus every day.", ["goes", "go", "going", "is go"], "She + động từ thêm -es: go → goes. 'every day' là dấu hiệu hiện tại đơn.")
C("My parents ___ TV in the evening.", ["watch", "watches", "watching", "is watching"], "Chủ ngữ số nhiều → động từ nguyên mẫu.")
C("Water ___ at 100 degrees Celsius.", ["boils", "boil", "is boil", "boiling"], "Sự thật hiển nhiên, chủ ngữ số ít → boils.")
C("He ___ eat meat. He's a vegetarian.", ["doesn't", "don't", "isn't", "not"], "He + doesn't + V.")
C("___ your sister speak English?", ["Does", "Do", "Is", "Are"], "Your sister = she → Does.")
C("The train ___ at 7:30 every morning.", ["leaves", "leave", "is leave", "leaving"], "Lịch trình cố định, chủ ngữ số ít → leaves.")
C("I usually ___ up at six.", ["get", "gets", "getting", "am get"], "I + động từ nguyên mẫu; 'usually' → hiện tại đơn.")
C("Tom ___ his homework after dinner.", ["does", "do", "dos", "doing"], "Tom (he) + do → does.")
C("Cats ___ milk.", ["like", "likes", "is like", "liking"], "Cats số nhiều → like.")
C("She ___ never late for class.", ["is", "are", "be", "does"], "Trạng từ never đứng sau to be; chủ ngữ She → is.")
E("My father [work] [in] a hospital [near] [our] house.", 0, "works", "My father (he) → works.")
E("She [doesn't] [likes] spicy [food] [at all].", 1, "like", "Sau doesn't dùng động từ nguyên mẫu: like.")
E("[Do] your brother [play] [the] guitar [well]?", 0, "Does", "Your brother (he) → Does.")
T("Lan always walks to school.", ["Lan goes to school on foot every day.", "Lan sometimes walks to school.", "Lan never goes to school on foot.", "Lan walked to school yesterday."], "always walks = luôn đi bộ ≈ goes on foot every day.")
O("She usually drinks tea in the morning.", "Cô ấy thường uống trà vào buổi sáng.")
O("Does your brother live in Hanoi?", "Anh trai bạn có sống ở Hà Nội không?")
O("We don't go to school on Sundays.", "Chúng tôi không đi học vào Chủ nhật.")

point("articles", 1, "Mạo từ a / an / the", "a + phụ âm · an + nguyên âm (âm) · the + đã xác định",
      "a/an: nhắc đến lần đầu, một trong nhiều; the: người nghe đã biết, duy nhất, đã nhắc trước.",
      "a/an chỉ dùng với danh từ đếm được số ít. Chọn a hay an theo ÂM đầu chứ không theo chữ cái: an hour (h câm), a university (/juː/). the dùng khi cả hai người đều biết đang nói về cái gì, hoặc vật duy nhất (the sun, the moon).",
      ["a", "an", "the", "zero article"],
      [("I saw a cat. The cat was black.", "Tôi thấy một con mèo. Con mèo đó màu đen.", "a cat ... The cat"),
       ("She is an engineer.", "Cô ấy là kỹ sư.", "an engineer"),
       ("The sun rises in the east.", "Mặt trời mọc ở hướng đông.", "The sun")],
      [("She is a honest person.", "She is an honest person.", "honest có h câm → âm đầu là nguyên âm → an."),
       ("I want to be engineer.", "I want to be an engineer.", "Nghề nghiệp số ít cần a/an.")],
      tags=["article_quantifier"], exam=True)
C("I'll be back in ___ hour.", ["an", "a", "the", "—"], "hour có h câm, âm đầu /aʊ/ là nguyên âm → an.")
C("He studies at ___ university in Hanoi.", ["a", "an", "the", "—"], "university bắt đầu bằng âm /juː/ (phụ âm) → a.")
C("Look at ___ moon! It's so bright.", ["the", "a", "an", "—"], "Vật duy nhất → the moon.")
C("My mother is ___ teacher.", ["a", "an", "the", "—"], "Nghề nghiệp số ít, teacher bắt đầu bằng phụ âm → a.")
C("Can you close ___ door, please?", ["the", "a", "an", "—"], "Cả hai người đều biết cánh cửa nào → the.")
C("She bought ___ umbrella yesterday.", ["an", "a", "the", "—"], "umbrella bắt đầu bằng âm nguyên âm /ʌ/ → an.")
C("I love ___ music.", ["—", "the", "a", "an"], "Nói chung chung về âm nhạc (không đếm được) → không dùng mạo từ.")
C("It's ___ European company.", ["a", "an", "the", "—"], "European bắt đầu bằng âm /j/ (phụ âm) → a.")
E("She is [a] [honest] [and] kind [person].", 0, "an", "honest có h câm → an honest.")
E("I want [to] [be] [doctor] [when] I grow up.", 2, "a doctor", "Nghề nghiệp số ít cần mạo từ: a doctor.")
E("[The] life [is] [too] [short] to worry.", 0, "Life", "Nói chung chung về cuộc sống → không dùng the.")
O("I have an idea.", "Tôi có một ý tưởng.")
O("The book on the table is mine.", "Cuốn sách trên bàn là của tôi.")

point("plurals", 1, "Danh từ số nhiều", "N + s / es / ies · bất quy tắc: man → men, child → children",
      "Nói về từ hai người/vật trở lên.",
      "Phần lớn thêm -s. Tận cùng -s, -sh, -ch, -x, -o thường thêm -es (buses, watches, boxes, tomatoes). Phụ âm + y → -ies (city → cities). Bất quy tắc: man → men, woman → women, child → children, foot → feet, tooth → teeth, person → people, mouse → mice.",
      ["two", "many", "a lot of", "some"],
      [("There are three boxes on the floor.", "Có ba cái hộp trên sàn.", "boxes"),
       ("Many children like cartoons.", "Nhiều trẻ em thích phim hoạt hình.", "children"),
       ("We visited two cities.", "Chúng tôi đã thăm hai thành phố.", "cities")],
      [("two childs", "two children", "child có số nhiều bất quy tắc."),
       ("many informations", "much information", "information là danh từ không đếm được, không thêm -s.")],
      tags=["agreement"])
C("There are five ___ in my family.", ["people", "peoples", "persons", "person"], "Số nhiều thông dụng của person là people.")
C("Brush your ___ before bed.", ["teeth", "tooths", "teeths", "tooth"], "tooth → teeth (bất quy tắc).")
C("I bought two ___ of milk.", ["boxes", "boxs", "boxies", "box"], "box tận cùng -x → boxes.")
C("Ho Chi Minh City and Hanoi are big ___.", ["cities", "citys", "cityes", "city"], "Phụ âm + y → -ies: cities.")
C("The ___ are playing in the garden.", ["children", "childs", "childrens", "child"], "child → children.")
C("Can you give me some ___?", ["advice", "advices", "an advice", "advises"], "advice không đếm được → không thêm -s.")
C("There are three ___ in the box.", ["mice", "mouses", "mices", "mouse"], "mouse (con chuột) → mice.")
E("I have [two] [childs] and [a] [dog].", 1, "children", "child → children.")
E("She gave me [many] [useful] [informations] [about] the job.", 2, "information", "information không đếm được → much information / không thêm s.")
O("There are many tourists in the city.", "Có nhiều khách du lịch trong thành phố.")

point("prep_time", 1, "Giới từ thời gian & nơi chốn: in / on / at", "at + giờ, địa điểm cụ thể · on + ngày, bề mặt · in + tháng/năm/mùa, không gian bên trong",
      "Chỉ thời điểm và vị trí — xuất hiện rất nhiều trong mọi bài thi.",
      "Thời gian: at + giờ (at 7 o'clock), at night, at the weekend (Anh-Anh); on + thứ/ngày (on Monday, on May 5th); in + tháng, năm, mùa, buổi (in May, in 2025, in summer, in the morning). Nơi chốn: at + điểm cụ thể (at the bus stop); on + bề mặt (on the table); in + bên trong (in the box, in Hanoi).",
      ["at 7 a.m.", "on Monday", "in July", "in the morning"],
      [("The meeting is at 9 a.m. on Monday.", "Cuộc họp lúc 9 giờ sáng thứ Hai.", "at 9 a.m. on Monday"),
       ("I was born in 2005.", "Tôi sinh năm 2005.", "in 2005"),
       ("Your phone is on the desk.", "Điện thoại của bạn ở trên bàn.", "on the desk")],
      [("in Monday", "on Monday", "Thứ trong tuần dùng on."),
       ("at the morning", "in the morning", "Buổi trong ngày dùng in (trừ at night).")],
      tags=["preposition"], exam=True)
C("The shop opens ___ 8 o'clock.", ["at", "on", "in", "by"], "Giờ cụ thể → at.")
C("My birthday is ___ June 12th.", ["on", "in", "at", "by"], "Ngày cụ thể → on.")
C("We often go to the beach ___ summer.", ["in", "on", "at", "during to"], "Mùa → in.")
C("I'll call you ___ Friday.", ["on", "in", "at", "to"], "Thứ trong tuần → on.")
C("She was born ___ 1999.", ["in", "on", "at", "since"], "Năm → in.")
C("There's a cat ___ the roof.", ["on", "in", "at", "into"], "Trên bề mặt → on.")
C("I'll wait for you ___ the bus stop.", ["at", "in", "on", "to"], "Điểm cụ thể → at.")
C("I usually study ___ night.", ["at", "in", "on", "by"], "Cụm cố định: at night.")
C("He lives ___ Da Lat.", ["in", "at", "on", "to"], "Thành phố → in.")
C("We have a meeting ___ the afternoon.", ["in", "on", "at", "by"], "Buổi trong ngày → in the afternoon.")
E("The concert [starts] [in] Saturday [at] [8 p.m.]", 1, "on", "Thứ trong tuần → on Saturday.")
E("I [usually] [drink] coffee [at] the [morning].", 2, "in", "in the morning.")
E("She [has] lived [at] Hanoi [since] [2020].", 1, "in", "Thành phố → in Hanoi.")
O("The class starts at seven in the morning.", "Lớp học bắt đầu lúc bảy giờ sáng.")
O("We met on a rainy day in October.", "Chúng tôi gặp nhau vào một ngày mưa tháng Mười.")

point("there_is", 1, "There is / There are", "There is + N số ít / không đếm được · There are + N số nhiều",
      "Giới thiệu sự tồn tại của người/vật ở đâu đó ('có').",
      "Người Việt hay nói 'có' bằng have, nhưng để nói 'ở đâu đó có cái gì' tiếng Anh dùng there is/are. Động từ to be chia theo danh từ ĐI SAU: There is a book · There are two books. Với danh sách, chia theo danh từ đầu tiên: There is a pen and two books.",
      ["There is", "There are", "Is there...?", "There isn't any..."],
      [("There is a bank near my house.", "Có một ngân hàng gần nhà tôi.", "There is"),
       ("There are 30 students in my class.", "Lớp tôi có 30 học sinh.", "There are"),
       ("Is there any milk in the fridge?", "Trong tủ lạnh còn sữa không?", "Is there")],
      [("It has a park near here.", "There is a park near here.", "Diễn tả 'có' sự tồn tại ở đâu đó dùng there is, không dùng it has."),
       ("There is many people.", "There are many people.", "people là số nhiều → are.")],
      tags=["agreement"])
C("There ___ a lot of people at the party.", ["were", "was", "is", "be"], "people số nhiều, ngữ cảnh quá khứ (party đã qua) → were.")
C("There ___ some water in the bottle.", ["is", "are", "be", "have"], "water không đếm được → is.")
C("___ there any eggs in the fridge?", ["Are", "Is", "Do", "Have"], "eggs số nhiều → Are there.")
C("There ___ two banks on this street.", ["are", "is", "has", "have"], "two banks số nhiều → are.")
C("There ___ a pen and two notebooks on my desk.", ["is", "are", "be", "has"], "Chia theo danh từ đầu tiên (a pen) → is.")
C("There isn't ___ sugar left.", ["any", "some", "a", "many"], "Câu phủ định → any.")
E("[It] [has] a [beautiful] park [near] my house.", 0, "There is", "Diễn tả sự tồn tại → There is a beautiful park...")
E("There [is] [many] [interesting] [places] in Hue.", 0, "are", "places số nhiều → There are.")
O("There is a cat under the table.", "Có một con mèo dưới gầm bàn.")
O("Are there any good restaurants near here?", "Gần đây có nhà hàng nào ngon không?")

point("can_could", 1, "Can / Could", "S + can/could + V (nguyên mẫu)",
      "Diễn tả khả năng, xin phép, đề nghị lịch sự.",
      "can: có thể (hiện tại). could: có thể (quá khứ) hoặc dùng để hỏi lịch sự hơn (Could you help me?). Sau can/could luôn là động từ nguyên mẫu KHÔNG 'to', không thêm -s.",
      ["can", "can't", "could", "Could you...?"],
      [("She can speak three languages.", "Cô ấy có thể nói ba thứ tiếng.", "can speak"),
       ("I couldn't sleep last night.", "Tối qua tôi không ngủ được.", "couldn't sleep"),
       ("Could you open the window, please?", "Bạn có thể mở cửa sổ giúp tôi không?", "Could you open")],
      [("He can swims.", "He can swim.", "Sau can dùng động từ nguyên mẫu, không thêm -s."),
       ("I can to drive.", "I can drive.", "Không dùng 'to' sau can.")],
      tags=["modal"])
C("My little brother can ___ very fast.", ["run", "runs", "to run", "running"], "can + V nguyên mẫu.")
C("When I was five, I ___ swim.", ["could", "can", "can to", "could to"], "Khả năng trong quá khứ → could.")
C("___ you help me with my homework, please?", ["Could", "Do", "Are", "Must"], "Nhờ vả lịch sự → Could you...?")
C("Sorry, I ___ come to your party. I'm busy.", ["can't", "don't can", "not can", "can't to"], "Phủ định của can → can't.")
C("It's too dark. I ___ see anything.", ["can't", "mustn't", "shouldn't", "won't to"], "Không thể nhìn thấy → can't.")
E("She [can] [speaks] English [very] [well].", 1, "speak", "can + V nguyên mẫu: speak.")
E("I [can] [to] play [the] [piano].", 1, "(bỏ to)", "Không dùng 'to' sau can.")
O("Can you play the guitar?", "Bạn có biết chơi guitar không?")
O("Could you speak more slowly, please?", "Bạn có thể nói chậm hơn được không?")

point("word_order", 1, "Trật tự từ cơ bản (S + V + O + nơi chốn + thời gian)", "S + V + O + Place + Time · Tính từ đứng TRƯỚC danh từ",
      "Sắp xếp câu đúng trật tự tự nhiên của tiếng Anh.",
      "Câu tiếng Anh cơ bản: Chủ ngữ + Động từ + Tân ngữ + Nơi chốn + Thời gian. Khác tiếng Việt, tính từ đứng TRƯỚC danh từ: a red car (một chiếc xe đỏ). Trạng từ tần suất (always, often...) đứng trước động từ thường nhưng sau to be.",
      ["S + V + O", "adj + N", "always + V", "be + always"],
      [("I bought a new phone yesterday.", "Hôm qua tôi mua một chiếc điện thoại mới.", "a new phone"),
       ("She always drinks coffee in the morning.", "Cô ấy luôn uống cà phê vào buổi sáng.", "always drinks"),
       ("He is always late.", "Anh ấy luôn đến muộn.", "is always")],
      [("I have a car red.", "I have a red car.", "Tính từ đứng trước danh từ."),
       ("She drinks always coffee.", "She always drinks coffee.", "Trạng từ tần suất đứng trước động từ thường.")],
      tags=["word_order"])
C("She has ___.", ["a beautiful voice", "a voice beautiful", "voice a beautiful", "beautiful a voice"], "Mạo từ + tính từ + danh từ.")
C("I ___ breakfast at home.", ["usually have", "have usually", "usually having", "am usually have"], "Trạng từ tần suất đứng trước động từ thường.")
C("He is ___ late for work.", ["never", "never is", "is never", "not never"], "Với to be: be + never → He is never late.")
E("We [visited] [a] [museum famous] [in Paris] last year.", 2, "famous museum", "Tính từ đứng trước danh từ: a famous museum.")
E("I [go] [often] [to the gym] [after work].", 1, "often go", "Trạng từ tần suất đứng trước động từ thường: I often go.")
O("I met my best friend at school ten years ago.", "Tôi gặp bạn thân ở trường mười năm trước.")
O("She always wears a black hat.", "Cô ấy luôn đội một chiếc mũ đen.")
O("We are going to visit our grandparents next week.", "Tuần sau chúng tôi sẽ đi thăm ông bà.")
O("He bought an expensive watch for his father.", "Anh ấy mua một chiếc đồng hồ đắt tiền cho bố.")
