"""TOEIC Part 6 bank batch 2: original service and office messages."""
from lib import P6


P6("p6_d2_relocation", "Office relocation notice",
   "Dear colleagues,\n\nOur office will move to the building on Oak Avenue during the first week of August. The new location is {1} to the train station than our current office. Moving boxes will be delivered to each department {2} Friday.\n\nPlease label every box clearly and remove personal items that are no longer {3}. The moving company will collect the boxes on Monday morning. {4}, keep essential documents with you until the move is complete.\n\nFacilities Team",
   [(["closer", "close", "closest", "closely"], "comparison", "Than báo hiệu so sánh hơn closer to the train station.", "Than calls for the comparative adjective closer in the phrase closer to the station."),
    (["by", "until", "during", "since"], "preposition", "By Friday nghĩa là không muộn hơn thứ Sáu.", "By gives the deadline for delivery: the boxes will arrive no later than Friday."),
    (["needed", "need", "needing", "needs"], "pos", "Sau are no longer cần phân từ needed để chỉ những món không còn cần.", "Needed is an adjective meaning required. Personal items may be removed if they are no longer required."),
    (["Therefore", "Although", "Because", "Meanwhile"], "linking", "Therefore nêu hướng dẫn là kết quả của việc chuyển văn phòng.", "Therefore introduces the practical instruction that follows from the move.")],
   topic="office", level=3)

P6("p6_d2_supplier", "Supplier quality review",
   "Thank you for attending yesterday's review meeting. We discussed the quality of the latest shipment and agreed on several improvements. The supplier will inspect every unit more {1} before dispatch. In addition, the supplier will send a report {2} each shipment.\n\nThese steps should reduce the number of products returned by customers. {3} the new procedure is effective, our team will continue to sample units on arrival. Please contact me if you notice an unusual {4} in the product quality.",
   [(["carefully", "careful", "care", "cared"], "pos", "Bổ nghĩa cho inspect cần trạng từ carefully.", "Carefully is the adverb modifying inspect and describes how each unit will be checked."),
    (["with", "to", "at", "for"], "preposition", "Send a report with each shipment nghĩa là gửi kèm mỗi lô hàng.", "With means that the report accompanies each shipment."),
    (["Although", "Because of", "During", "Unless"], "conjunction", "Although nối sự tương phản giữa quy trình có hiệu lực và việc vẫn tiếp tục lấy mẫu.", "Although introduces the contrast between the new procedure and the continued sampling."),
    (["change", "changing", "changed", "changes"], "pos", "Sau an unusual cần danh từ số ít change.", "An unusual requires a singular countable noun. Change fits the phrase an unusual change.")],
   topic="commerce", level=4)

P6("p6_d2_library", "Library membership update",
   "Beginning next month, members may borrow up to six books at a time. The new limit will give readers {1} flexibility when planning their reading. Members who have overdue items must return them {2} borrowing additional books.\n\nThe library will also extend its weekend hours. {3}, the study room will remain available until 8 p.m. on Saturdays. Please check the online calendar before visiting, as hours may change {4} special events.",
   [(["greater", "greatly", "greatness", "great"], "pos", "Sau give readers cần tính từ greater bổ nghĩa cho flexibility.", "Greater is an adjective modifying flexibility and means more flexibility than before."),
    (["before", "during", "among", "beside"], "preposition", "Return them before borrowing thêm sách để đáp ứng điều kiện.", "Before introduces the earlier action required before borrowing additional books."),
    (["In addition", "Otherwise", "Instead", "For example"], "linking", "In addition bổ sung thông tin về study room sau giờ mở rộng.", "In addition adds another detail about the extended weekend service."),
    (["for", "at", "on", "from"], "preposition", "Change for special events chỉ sự thay đổi vì các sự kiện đặc biệt.", "For introduces the reason or occasion that may cause the hours to change.")],
   topic="education", level=3)

P6("p6_d2_travel", "Airport shuttle reminder",
   "Guests departing on morning flights may use the complimentary airport shuttle. The first shuttle leaves the hotel at 5:00 a.m., and seats are available on a first-come, first-served basis. Guests should reserve a seat {1} midnight.\n\nThe driver will help with large bags, but passengers are responsible {2} smaller personal items. {3} the shuttle is full, the front desk can call a taxi. Please allow extra time, as traffic near the airport is often {4} before 7:00 a.m.",
   [(["by", "until", "during", "from"], "preposition", "By midnight là hạn chót để đặt chỗ.", "By sets the deadline for reserving a seat: no later than midnight."),
    (["for", "to", "at", "with"], "preposition", "Responsible for là cụm cố định.", "Responsible for is the fixed expression used before the items passengers must look after."),
    (["If", "Despite", "Because of", "Whereas"], "conjunction", "If mở đầu điều kiện shuttle đầy chỗ thì quầy lễ tân có thể gọi taxi.", "If introduces the condition under which the front desk can call a taxi."),
    (["heavy", "heavily", "heaviness", "heavier"], "pos", "Sau is often cần tính từ heavy; traffic is often heavy.", "Heavy is the adjective that describes traffic before the morning rush.")],
   topic="travel_holidays", level=3)
