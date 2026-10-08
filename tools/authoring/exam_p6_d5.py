"""TOEIC Part 6 bank batch 5: original travel, facilities, and technology messages."""
from lib import P6


P6("p6_d5_hotel", "Hotel renovation notice",
   "The hotel lobby will be renovated from March 3 through March 7. During this period, guests should use the side entrance. The front desk will remain open and can answer questions about {1}. We apologize for any {2} the work may cause.\n\nBreakfast will be served in the meeting room instead of the restaurant. {3}, the fitness room will close for one day while its equipment is moved. The hotel expects all facilities to return to normal {4} on March 8.",
   [(["check-in", "check in", "checked-in", "checking in"], "noun", "About cần danh từ check-in; đây là thủ tục nhận phòng.", "The preposition about is followed by the noun check-in, meaning the arrival procedure."),
    (["inconvenience", "inconvenient", "inconveniently", "convenience"], "pos", "Any đi trước danh từ inconvenience.", "Any inconvenience is the noun phrase for problems caused for guests."),
    (["In addition", "Instead", "Otherwise", "For example"], "linking", "In addition bổ sung một thay đổi khác trong thời gian sửa chữa.", "In addition adds another temporary change: the fitness room will close."),
    (["operation", "operate", "operational", "operating"], "pos", "Return to normal operation là cụm danh từ cố định.", "Return to normal operation is the noun phrase meaning resume usual service.")],
   topic="hospitality", level=3)

P6("p6_d5_app", "Mobile app maintenance",
   "Our mobile app will be unavailable for approximately thirty minutes while we install a security update. Users should save their work before the maintenance begins. The update is intended to make the app more {1} and to protect personal information.\n\nWe will post a notice when the service is restored. {2} you experience a problem afterward, please restart the app before contacting support. The support team will be ready to provide {3} assistance. We appreciate your patience during this {4} interruption.",
   [(["reliable", "reliability", "reliably", "rely"], "pos", "More cần tính từ reliable để mô tả app.", "More reliable is an adjective phrase describing an app that works consistently."),
    (["If", "Although", "During", "Because of"], "conjunction", "If mở đầu điều kiện người dùng gặp vấn đề sau cập nhật.", "If introduces the possible problem that triggers the next instruction."),
    (["additional", "add", "additionally", "addition"], "pos", "Provide cần tân ngữ là danh từ assistance, được bổ nghĩa bởi additional.", "Additional modifies the noun assistance and means extra help."),
    (["temporary", "temporarily", "temporariness", "temporize"], "pos", "Interruption là danh từ cần tính từ temporary.", "Temporary is an adjective describing an interruption that will end soon.")],
   topic="tech", level=3)

P6("p6_d5_flight", "Flight change information",
   "Passengers on Flight 248 should note that the departure time has changed. The flight will leave at 6:40 p.m., thirty minutes later than originally planned. Please arrive at the gate {1} and have your passport ready.\n\nThe airline will provide meal vouchers to passengers who are delayed {2} the schedule change. {3}, passengers with connecting flights should speak to an agent as soon as possible. The gate may change, so check the departure screens {4}.",
   [(["early", "earlier", "earliest", "earliness"], "adverb", "Arrive early là cụm diễn tả đến sớm.", "Early is the adverb modifying arrive and tells passengers to come before the flight."),
    (["because of", "although", "during", "unless"], "preposition", "Because of đi trước cụm danh từ the schedule change.", "Because of introduces the noun phrase explaining why passengers are delayed."),
    (["In particular", "Because", "Although", "Meanwhile"], "linking", "In particular nhấn mạnh nhóm hành khách có chuyến bay nối chuyến.", "In particular focuses the next advice on passengers with connecting flights."),
    (["frequently", "frequent", "frequency", "frequented"], "pos", "Check là động từ cần trạng từ frequently.", "Frequently modifies check and tells passengers how often to look at the screens.")],
   topic="transport", level=3)

P6("p6_d5_equipment", "Equipment booking system",
   "Employees can now reserve meeting equipment through the online booking system. The system lists projectors, microphones, and portable speakers that are {1}. To reserve an item, select a date and enter the room number.\n\nPlease return equipment promptly after use so that the next employee can collect it. {2} an item is damaged, report the problem in the system rather than making a repair yourself. The facilities team will inspect the item and decide whether it is safe to use {3}. The new process should make equipment sharing more {4}.",
   [(["available", "availability", "availably", "avail"], "pos", "Sau are cần tính từ available để mô tả thiết bị có thể đặt.", "The linking verb are needs the adjective available."),
    (["If", "Despite", "During", "Because of"], "conjunction", "If mở đầu điều kiện thiết bị bị hỏng.", "If introduces the condition requiring a report in the system."),
    (["again", "already", "rather", "almost"], "adverb", "Use again nghĩa là sử dụng lại; trạng từ again đứng cuối cụm.", "Again is the adverb meaning the item may be used another time."),
    (["efficient", "efficiency", "efficiently", "effect"], "pos", "Make sharing more cần tính từ efficient.", "More efficient is an adjective phrase describing a process that uses time and equipment better.")],
   topic="office", level=3)
