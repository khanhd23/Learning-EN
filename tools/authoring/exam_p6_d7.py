"""TOEIC Part 6 bank batch 7: final original business messages."""
from lib import P6


P6("p6_d7_inventory", "Inventory count reminder",
   "The annual inventory count will begin at 8 a.m. on Saturday. Team leaders should print the count sheets and assign each area {1}. Employees must record quantities carefully and report any damaged stock {2} the count begins. The warehouse will be closed to deliveries during the count.\n\n{3}, please wear safety shoes and follow the supervisor's instructions. The results will be entered into the system {4} the count is finished.",
   [(["in advance", "advance", "advanced", "advancing"], "adverb", "Assign each area in advance nghĩa là phân công trước.", "In advance is the adverbial phrase meaning before the count begins."),
    (["before", "during", "among", "toward"], "preposition", "Report damaged stock before the count begins để nhân viên biết tình trạng trước khi đếm.", "Before introduces the earlier time when damaged stock should be reported."),
    (["In addition", "Instead", "Otherwise", "For example"], "linking", "In addition bổ sung hướng dẫn an toàn sau thông tin về kết quả.", "In addition adds another instruction about safety shoes and supervisor directions."),
    (["after", "because", "despite", "among"], "preposition", "After the count is finished chỉ thời điểm nhập kết quả.", "After introduces the time when the results will be entered.")],
   topic="logistics", level=3)

P6("p6_d7_membership", "Membership renewal offer",
   "Members whose annual plan expires this month may renew it at a reduced rate. The offer is available {1} September 30. To renew, members should sign in to their account and select the plan that is most {2} for their needs.\n\nA confirmation email will be sent after payment is complete. {3} you do not receive the email within an hour, contact support. The new membership period will begin {4} the current plan ends.",
   [(["until", "during", "among", "toward"], "preposition", "Available until September 30 nghĩa là ưu đãi kéo dài đến hết ngày đó.", "Until marks the final date when the offer is available."),
    (["suitable", "suitably", "suitability", "suit"], "pos", "Most cần tính từ suitable để mô tả plan phù hợp nhất.", "Most suitable is the adjective phrase describing the best plan for each member."),
    (["If", "Although", "During", "Because of"], "conjunction", "If mở đầu điều kiện không nhận được email.", "If introduces the condition that the confirmation email does not arrive."),
    (["when", "during", "among", "because"], "conjunction", "When nối mệnh đề current plan ends và chỉ thời điểm bắt đầu gói mới.", "When introduces the time at which the new membership period begins.")],
   topic="money_banking", level=3)

P6("p6_d7_recruitment", "Recruitment event details",
   "Our company will hold a recruitment event for recent graduates next Saturday. Visitors can meet hiring managers and learn more about available positions. Please bring a résumé and arrive {1} fifteen minutes before your chosen session.\n\nThe event is free, but advance registration is required. {2} you have registered online, you will receive a confirmation message with the room number. Staff members will be available to answer questions and provide {3} information about the application process. We look forward to {4} you at the event.",
   [(["at least", "almost", "already", "only"], "adverb", "At least fifteen minutes nghĩa là sớm tối thiểu 15 phút.", "At least expresses the minimum amount of time visitors should arrive early."),
    (["Once", "Despite", "Because of", "Whereas"], "conjunction", "Once chỉ việc gửi tin nhắn ngay sau khi đăng ký.", "Once introduces the time after online registration is complete."),
    (["additional", "add", "additionally", "addition"], "pos", "Provide cần danh từ information, được bổ nghĩa bởi additional.", "Additional modifies the noun information and means extra details."),
    (["meeting", "meet", "met", "meets"], "gerund", "Look forward to có to là giới từ nên dùng V-ing meeting.", "Look forward to is followed by a gerund, so meeting is correct.")],
   topic="hr", level=3)

P6("p6_d7_maintenance", "Building maintenance schedule",
   "The building's water system will be inspected on Tuesday morning. Restrooms on the second floor may be unavailable for up to two hours. Employees should use the facilities on the first floor {1}. The maintenance team will place signs near any area that is temporarily {2}.\n\nWe apologize for the inconvenience. {3}, the inspection is necessary to keep the system operating safely. Please report leaks to Facilities {4} so that they can be addressed quickly.",
   [(["instead", "instead of", "rather", "substitute"], "adverb", "Use instead ở cuối câu để chỉ lựa chọn thay thế.", "Instead is an adverb showing that employees should use the first-floor facilities as an alternative."),
    (["closed", "close", "closely", "closure"], "pos", "Sau is cần phân từ closed để mô tả khu vực bị đóng.", "Closed is an adjective describing an area that cannot be used temporarily."),
    (["However", "Because", "During", "Unless"], "linking", "However nối lời xin lỗi với lý do việc kiểm tra vẫn cần thiết.", "However introduces the contrast between the inconvenience and the necessity of the inspection."),
    (["immediately", "immediate", "immediacy", "immediateness"], "pos", "Report là động từ cần trạng từ immediately.", "Immediately modifies report and tells employees to report leaks without delay.")],
   topic="home_furniture", level=3)
