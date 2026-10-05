"""Original Part 6-style passages: 4 blanks each, one of them a sentence-insertion blank."""
from lib import P6

P6("p6_renovation", "Notice to all tenants",
   "Dear Tenants,\n\nPlease be advised that the lobby of Green Tower {1} from Monday, May 6, to Friday, May 10. During this time, residents should use the side entrance on Lake Street. {2}\n\nWe understand that this work may cause some {3}. The new lobby will include a larger mail area and improved lighting. {4}, a small coffee corner will be added for residents.\n\nThank you for your patience.\nBuilding Management",
   [(["will be renovated", "renovated", "was renovating", "has renovated"], "tense", "Thời gian trong tương lai và lobby được sửa (bị động) → will be renovated."),
    (["The elevators will continue to operate as usual.", "The building was built in 1995.", "Rent must be paid by the first of the month.", "Our company also manages several hotels."], "sentence", "Câu tiếp tục thông tin thực tế cho cư dân trong thời gian sửa chữa — thang máy vẫn hoạt động bình thường."),
    (["inconvenience", "inconvenient", "inconveniently", "convenient"], "pos", "Sau some cần danh từ → inconvenience."),
    (["In addition", "However", "Otherwise", "Instead"], "linking", "Bổ sung thêm một tiện ích → In addition.")],
   topic="housing", level=3)

P6("p6_order", "Re: Your order #4471",
   "Dear Ms. Patel,\n\nThank you for your recent order. Unfortunately, the blue office chairs you selected are currently out of stock. We expect to receive more {1} two weeks.\n\n{2} If you would prefer, we can send the same model in black, which is available now. {3} customers who choose this option, we will include free delivery.\n\nPlease let us know which option you prefer by replying {4} this email.\n\nBest regards,\nDaniel Moore, Customer Service",
   [(["within", "since", "until", "among"], "preposition", "within + khoảng thời gian = trong vòng hai tuần."),
    (["We apologize for this delay.", "Our chairs are made in Italy.", "The meeting has been moved to Friday.", "Please send us your résumé."], "sentence", "Sau khi báo hết hàng, câu xin lỗi vì sự chậm trễ là hợp lý nhất."),
    (["For", "Of", "By", "At"], "preposition", "For customers who... = Đối với những khách hàng..."),
    (["to", "for", "at", "on"], "preposition", "reply to an email.")],
   topic="sales", level=3)

P6("p6_hiring", "Job Opening: Marketing Assistant",
   "Bright Media is looking for an {1} marketing assistant to join our growing team in Da Nang. The successful candidate will help plan social media campaigns and {2} customer data.\n\nApplicants should have at least one year of experience in marketing. {3} Knowledge of graphic design software is a plus.\n\nTo apply, please send your résumé and a short cover letter to jobs@brightmedia.example by June 30. Only candidates who {4} for an interview will be contacted.",
   [(["enthusiastic", "enthusiasm", "enthusiastically", "enthuse"], "pos", "Trước cụm danh từ marketing assistant → tính từ enthusiastic."),
    (["analyze", "analysis", "analytical", "analyzing"], "pos", "Song song với plan (help plan ... and analyze) → động từ nguyên mẫu."),
    (["Strong English communication skills are also required.", "Our office closes at 6 p.m. on Fridays.", "The company was founded by three friends.", "Thank you for your recent purchase."], "sentence", "Đoạn đang liệt kê yêu cầu ứng viên → thêm một yêu cầu nữa."),
    (["are selected", "select", "selecting", "have selected"], "tense", "Ứng viên ĐƯỢC chọn → bị động are selected.")],
   topic="hr", level=3)

P6("p6_conference", "Annual Tech Forum — Update",
   "Dear Participants,\n\nWe are pleased to announce that registration for the Annual Tech Forum has been extended {1} October 15. Because of the high number of requests, we have also moved the event to a {2} venue, the City Convention Center.\n\n{3} All sessions will take place in Hall A, and lunch will be served on the second floor.\n\nIf you have already registered, you do not need to do anything. Your ticket {4} valid.\n\nWe look forward to seeing you there.",
   [(["until", "during", "since", "while"], "preposition", "extended until = gia hạn đến."),
    (["larger", "largely", "largest of", "large than"], "comparison", "Dời sang địa điểm lớn hơn (do nhiều người đăng ký) → larger."),
    (["The center is a five-minute walk from Central Station.", "Our company sells computer parts.", "Please submit your expense report.", "The weather was very cold last year."], "sentence", "Sau khi báo địa điểm mới, câu hướng dẫn cách đến là phù hợp nhất."),
    (["remains", "remain", "remaining", "is remained"], "vocab", "Your ticket (số ít) remains valid = vẫn còn hiệu lực.")],
   topic="events", level=3)

P6("p6_policy", "New Expense Policy",
   "To all staff:\n\nStarting next month, all travel expenses must be approved by a department manager {1} the trip. Employees who travel without approval may not be {2}.\n\nTo make the process easier, we have created a new online form. {3} Simply fill it in, attach your estimated costs, and click 'Submit.'\n\nAfter your trip, keep all receipts and upload them within ten days. Please contact Ms. Hoang in Accounting if you have {4} questions.\n\nThank you.",
   [(["before", "during", "until", "since"], "preposition", "Phải được duyệt TRƯỚC chuyến đi → before."),
    (["reimbursed", "reimbursing", "reimburse", "reimbursement"], "pos", "be + V3 (bị động): được hoàn tiền → reimbursed."),
    (["You can find it on the company intranet.", "Our sales grew by 10 percent last year.", "The cafeteria will close early on Friday.", "Several employees retired this year."], "sentence", "Sau khi giới thiệu mẫu đơn mới, câu chỉ nơi tìm mẫu đơn là hợp lý."),
    (["any", "much", "every", "a"], "quantifier", "Câu điều kiện/nghi vấn + danh từ số nhiều → any questions.")],
   topic="accounting", level=4)

P6("p6_hotel", "Welcome to Sunrise Hotel",
   "Dear Guest,\n\nWelcome to the Sunrise Hotel! We hope you enjoy your stay. Breakfast is served daily from 6:30 to 10:00 a.m. in the Garden Restaurant, {1} is located on the ground floor.\n\nOur swimming pool and fitness center are open to all guests free of charge. {2} Towels are provided at the pool.\n\nIf you need anything during your stay, please {3} hesitate to call the front desk by dialing 0. Our staff are available 24 hours a day and will be happy to {4} you.",
   [(["which", "who", "where", "what"], "relative", "Thay cho the Garden Restaurant (vật), sau dấu phẩy → which."),
    (["Children under 12 must be with an adult at the pool.", "The hotel was sold to a new owner.", "Please pay your phone bill online.", "Our factory produces furniture."], "sentence", "Đoạn nói về hồ bơi → quy định an toàn ở hồ bơi phù hợp."),
    (["do not", "not", "don't to", "no"], "vocab", "Cụm cố định: Please do not hesitate to..."),
    (["assist", "assistance", "assistant", "assisted"], "pos", "be happy to + V → assist.")],
   topic="hospitality", level=3)

P6("p6_launch", "Press Release: FreshBox App",
   "HANOI — FreshBox, a local food delivery company, {1} a new mobile app next Monday. The app will let customers order groceries from more than 200 stores and receive them in under an hour.\n\n\"Our customers told us they wanted a faster and {2} way to shop,\" said CEO Linh Pham. {3}\n\nTo celebrate the launch, FreshBox will offer a 20 percent discount on all orders placed {4} the first week.",
   [(["will release", "released", "has released", "releasing"], "tense", "next Monday → tương lai."),
    (["easier", "easiest", "more easy", "easily"], "comparison", "So sánh song song với faster → easier."),
    (["The company plans to expand to Da Nang next year.", "Mr. Brown will retire in June.", "The museum is closed on Mondays.", "Please wear a uniform at work."], "sentence", "Thông cáo báo chí thường tiếp nối bằng kế hoạch phát triển của công ty."),
    (["during", "while", "when", "at"], "preposition", "Trước danh từ the first week → during.")],
   topic="tech", level=3)

P6("p6_library", "City Library — Summer Reading Program",
   "This summer, the City Library invites readers of all ages to take part in our Summer Reading Program. Participants who read ten books {1} July and August will receive a free tote bag.\n\nTo join, simply register at the front desk or on our website. {2}\n\nWe will also hold weekly storytelling sessions for children every Saturday morning. Parents are {3} encouraged to attend with their kids. For more information, please {4} our website or call 555-0182.",
   [(["between", "among", "within of", "along"], "preposition", "between July and August = giữa (hai mốc)."),
    (["Registration is free for all library members.", "The library was built in 1960.", "Our café sells sandwiches and drinks.", "Some books are very expensive."], "sentence", "Sau hướng dẫn đăng ký, câu thông tin về phí đăng ký là hợp lý."),
    (["strongly", "strong", "strength", "strengthen"], "pos", "Bổ nghĩa cho V3 encouraged → trạng từ strongly."),
    (["visit", "visiting", "visitor", "visited"], "pos", "please + V nguyên mẫu → visit.")],
   topic="education", level=2)
