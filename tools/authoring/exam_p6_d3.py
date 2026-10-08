"""TOEIC Part 6 bank batch 3: original human-resources and operations notices."""
from lib import P6


P6("p6_d3_wellness", "Employee wellness program",
   "The company is introducing a wellness program for all employees. Participants may attend a short exercise class twice a week and receive advice from a health coach. Registration is {1}, but places in each class are limited.\n\nEmployees should bring water and wear {2} shoes. The program is designed to support healthy habits, not to replace medical care. {3} you have a health concern, consult a doctor before joining. The first class will take place {4} Wednesday morning.",
   [(["free", "freely", "freedom", "freeing"], "pos", "Sau is cần tính từ free để nói đăng ký không mất phí.", "The linking verb is requires an adjective. Free means participants do not pay to register."),
    (["comfortable", "comfort", "comfortably", "comforted"], "pos", "Tính từ comfortable đứng trước shoes.", "Comfortable describes shoes suitable for exercise and is needed before the noun shoes."),
    (["If", "Despite", "During", "Because of"], "conjunction", "If mở đầu điều kiện có vấn đề sức khỏe.", "If introduces the condition under which employees should consult a doctor first."),
    (["on", "in", "at", "by"], "preposition", "Dùng on với ngày cụ thể Wednesday.", "On is used with a day of the week: on Wednesday morning.")],
   topic="health_illness", level=3)

P6("p6_d3_contract", "Contract renewal reminder",
   "Your service contract will expire at the end of September. We are writing to ask whether you would like to renew it for another year. If you choose to continue, please return the signed form {1} September 15.\n\nThe renewal includes routine inspections and telephone support. It does not include replacement parts, which are charged {2}. {3}, we can prepare a separate quotation for a parts plan. Please contact our office if you need any {4} about the options.",
   [(["by", "until", "during", "from"], "preposition", "By September 15 là hạn chót gửi biểu mẫu.", "By sets the deadline for returning the form: no later than September 15."),
    (["separately", "separate", "separation", "separating"], "pos", "Charged là động từ nên cần trạng từ separately.", "Separately is the adverb that explains the replacement parts are charged on their own."),
    (["Alternatively", "Although", "Because", "Meanwhile"], "linking", "Alternatively đưa ra lựa chọn khác là lập báo giá riêng.", "Alternatively introduces another option: preparing a separate quotation."),
    (["information", "inform", "informative", "informing"], "pos", "Sau any cần danh từ không đếm được information.", "Any information is the noun phrase needed after need. The other forms are not nouns here.")],
   topic="commerce", level=4)

P6("p6_d3_delivery", "Delivery schedule change",
   "Due to road construction, deliveries to the west district may arrive later than usual this week. Customers who require a morning delivery should place their orders {1} noon on the previous day. Our dispatch team will confirm the expected time {2} email.\n\nWe apologize for the inconvenience. {3} the delay continues beyond this week, we will contact affected customers directly. Drivers have been given an alternate route, but the route is {4} longer than the normal one.",
   [(["before", "during", "among", "toward"], "preposition", "Before noon chỉ hạn chót đặt hàng trước buổi trưa.", "Before introduces the deadline: orders must be placed earlier than noon."),
    (["by", "with", "at", "from"], "preposition", "Confirm by email là cụm chỉ phương thức liên lạc.", "By email identifies the method used to confirm the expected time."),
    (["If", "Despite", "Because of", "Whereas"], "conjunction", "If mở đầu điều kiện sự chậm trễ kéo dài.", "If introduces the possible condition that the delay continues beyond the week."),
    (["slightly", "slight", "slightness", "slighted"], "pos", "Bổ nghĩa cho tính từ longer cần trạng từ slightly.", "Slightly modifies the comparative adjective longer and means by a small amount.")],
   topic="transport", level=3)

P6("p6_d3_feedback", "Product feedback request",
   "Thank you for purchasing our new planner. We would appreciate it if you would complete a short survey about your experience. The survey should take {1} five minutes. Your answers will help us identify features that customers find {2}.\n\nPlease use the link in the email to begin. {3} you have already completed the survey, no further action is needed. As a thank-you, we will send a discount code to everyone who responds {4} Friday.",
   [(["approximately", "approximate", "approximation", "approximated"], "pos", "Sau take cần trạng từ approximately để ước lượng thời gian.", "Approximately is an adverb modifying take and gives an estimated duration."),
    (["useful", "use", "usefully", "usefulness"], "pos", "Sau find + tân ngữ cần tính từ useful mô tả features.", "Find takes an object complement here. Useful describes the features customers value."),
    (["If", "Although", "During", "Because of"], "conjunction", "If tạo điều kiện survey đã được hoàn thành.", "If introduces the condition that the reader has already completed the survey."),
    (["by", "until", "during", "since"], "preposition", "Respond by Friday nghĩa là phản hồi không muộn hơn thứ Sáu.", "By gives the deadline for responding and receiving the discount code.")],
   topic="shopping", level=3)
