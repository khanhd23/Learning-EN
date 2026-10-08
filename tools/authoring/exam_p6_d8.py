"""TOEIC Part 6 bank batch 8: final original business messages."""
from lib import P6


P6("p6_d8_feedback", "Service feedback meeting",
   "The service team will meet next Wednesday to review customer feedback from the spring campaign. Team members should read the summary and bring one suggestion for improving response times. The meeting will begin {1} at 9:00 a.m.\n\nThe manager will first discuss the most common complaints. {2}, the group will choose two changes to test next month. Please send any additional data to the coordinator {3} Tuesday so that it can be included in the presentation. The final action list will be shared with the team {4} the meeting.",
   [(["promptly", "prompt", "promptness", "prompted"], "adverb", "Begin promptly at 9:00 a.m. cần trạng từ promptly.", "Promptly modifies begin and means at the stated time without delay."),
    (["Afterward", "Because", "Although", "Unless"], "linking", "Afterward nêu bước tiếp theo sau phần thảo luận khiếu nại.", "Afterward introduces the next stage after the complaints have been discussed."),
    (["by", "until", "during", "among"], "preposition", "By Tuesday là hạn chót gửi dữ liệu.", "By sets the deadline for sending the data before Tuesday ends."),
    (["after", "because", "despite", "among"], "preposition", "After the meeting chỉ thời điểm chia sẻ danh sách hành động.", "After introduces the time when the action list will be shared.")],
   topic="sales", level=3)

P6("p6_d8_shipping", "Shipping label change",
   "The shipping department has introduced a new label format. All packages must include the order number and the recipient's phone number. Please print labels {1} so that the information is easy to read. Packages without complete labels may be delayed.\n\nThe new format will be used for all orders starting Monday. {2} a package has already been prepared, replace its label before handing it to the courier. The system will save a copy of each label {3}. Contact the logistics desk if you need {4} help.",
   [(["clearly", "clear", "clarity", "cleared"], "adverb", "Print labels là động từ cần trạng từ clearly.", "Clearly describes how the labels should be printed so the information can be read."),
    (["If", "During", "Despite", "Because of"], "conjunction", "If mở đầu điều kiện gói hàng đã được chuẩn bị.", "If introduces the condition requiring the existing label to be replaced."),
    (["automatically", "automatic", "automation", "automate"], "pos", "Save là động từ cần trạng từ automatically.", "Automatically modifies save and describes what the system does without manual action."),
    (["additional", "add", "additionally", "addition"], "pos", "Need help cần danh từ help, được bổ nghĩa bởi additional.", "Additional modifies the noun help and means extra assistance.")],
   topic="logistics", level=3)

P6("p6_d8_training", "Training room reservation",
   "The training department has reserved Room 12 for next month's workshops. Employees who need the room should submit a request at least one week {1}. The room contains a projector, but presenters should bring any {2} cables or adapters.\n\nPlease leave the room clean after each session. {3}, return the chairs to their original arrangement. The facilities team will inspect the room {4} to ensure that the equipment is ready for the next group.",
   [(["in advance", "advance", "advanced", "advancing"], "adverb", "One week in advance là cụm chỉ việc đăng ký trước một tuần.", "In advance is the adverbial phrase meaning before the workshop date."),
    (["necessary", "necessarily", "necessity", "necessitate"], "pos", "Any cần tính từ necessary đứng trước cables or adapters.", "Necessary is an adjective describing cables or adapters that may be needed."),
    (["In addition", "However", "Because", "Unless"], "linking", "In addition bổ sung hướng dẫn xếp ghế sau yêu cầu vệ sinh phòng.", "In addition adds another cleanup instruction about the chairs."),
    (["regularly", "regular", "regularity", "regulate"], "pos", "Inspect là động từ cần trạng từ regularly.", "Regularly tells how often the facilities team checks the room.")],
   topic="education", level=3)

P6("p6_d8_vendor", "Vendor contact update",
   "Our primary vendor has changed its customer-service email address. Please use the new address for all purchase orders and delivery questions. Messages sent to the old address may not be answered {1}.\n\nThe vendor has also assigned a new account representative. {2}, the representative will contact each department this week to introduce herself. Please update your contact list {3} you receive her message. The purchasing team will remain available to provide {4} during the transition.",
   [(["promptly", "prompt", "promptness", "prompted"], "adverb", "Answered promptly cần trạng từ promptly bổ nghĩa cho answered.", "Promptly modifies answered and means without delay."),
    (["In addition", "Otherwise", "Although", "Because of"], "linking", "In addition bổ sung thông tin về đại diện tài khoản mới.", "In addition adds the information about the new account representative."),
    (["after", "during", "among", "because"], "conjunction", "After nối mệnh đề receive her message và chỉ thời điểm cập nhật sau đó.", "After introduces the event that should happen before updating the contact list."),
    (["assistance", "assist", "assisted", "assisting"], "pos", "Provide assistance là cụm danh từ đúng.", "Provide assistance is the noun phrase meaning offer help during the transition.")],
   topic="commerce", level=3)
