"""TOEIC Part 6 bank batch 1: original workplace messages."""
from lib import P6


P6("p6_d1_training", "Customer service workshop",
   "To all service representatives,\n\nThe customer service workshop will be held in Room 204 next Tuesday. Please arrive {1} ten minutes early so that the trainer can distribute the materials. {2} The session will cover complaint handling and follow-up calls.\n\nBecause the workshop includes role-play activities, participants should wear {3} clothing. The activities are designed to be practical and {4} for new and experienced staff.\n\nHuman Resources",
   [(["at least", "already", "rather", "almost"], "adverb", "At least ten minutes means ten minutes or more before the start.", "At least expresses a minimum amount of time, so it correctly modifies ten minutes."),
    (["Coffee and water will be available outside the room.", "The company opened this branch last year.", "All invoices are due on Friday.", "The warehouse closes at noon."], "sentence", "Câu này bổ sung thông tin hậu cần phù hợp trước khi chuyển sang nội dung buổi học.", "The refreshment sentence is relevant to workshop participants and connects naturally before the session content."),
    (["comfortable", "comfort", "comfortably", "comforted"], "pos", "Trước danh từ clothing cần tính từ comfortable.", "An adjective is needed before clothing. Comfortable describes clothing that is suitable for role-play."),
    (["useful", "use", "usefully", "usefulness"], "pos", "Sau practical cần tính từ song song useful.", "Useful is an adjective parallel to practical and describes the activities." )],
   topic="hr", level=3)

P6("p6_d1_invoice", "Updated invoice procedure",
   "Dear Finance Team,\n\nStarting in July, all invoices must be uploaded to the shared folder within three business days of {1}. The new procedure will help us track payments more {2}. Please check that each file includes the purchase order number.\n\nIf an invoice is missing information, return it to the sender with a short note. {3}, do not approve the payment until the corrected file arrives. The accounting manager will review the folder {4} to make sure the procedure is being followed.\n\nThank you,\nFinance Operations",
   [(["receipt", "receive", "received", "reception"], "pos", "Sau of cần danh từ; receipt nghĩa là việc nhận hóa đơn.", "The preposition of requires a noun. Receipt means the act of receiving the invoice."),
    (["efficiently", "efficient", "efficiency", "efficiencies"], "pos", "Bổ nghĩa cho động từ track cần trạng từ efficiently.", "Efficiently is an adverb modifying track. The other forms do not fit after more in this sentence."),
    (["Therefore", "Meanwhile", "Otherwise", "Similarly"], "linking", "Therefore nêu kết quả của việc thiếu thông tin: không duyệt thanh toán.", "Therefore introduces the result of the missing information: payment must not be approved yet."),
    (["weekly", "week", "weeks", "weekly basis"], "pos", "Đứng sau động từ review và không có mạo từ cần trạng từ weekly.", "Weekly is the adverb that tells how often the manager will review the folder.")],
   topic="accounting", level=4)

P6("p6_d1_store", "Weekend store promotion",
   "To celebrate the opening of our renovated store, customers will receive a free tote bag with purchases of $50 or more this weekend. The offer is available {1} supplies last. Customers should present the same receipt at the information desk.\n\nThe promotion cannot be combined with other discounts. {2}, loyalty members may use their points on a separate purchase. Staff members have prepared extra bags, but the quantity is still {3}. Please direct questions to the supervisor, who will be happy to provide {4}.\n\nThank you for shopping with us.",
   [(["while", "during", "because", "despite"], "conjunction", "While supplies last là cụm chỉ chương trình kéo dài chừng nào hàng còn.", "While introduces the time condition: the offer continues as long as supplies remain."),
    (["However", "Because", "Unless", "Therefore"], "linking", "However nối ý ngoại lệ tương phản với câu trước.", "However introduces an exception to the rule about combining discounts."),
    (["limited", "limit", "limiting", "limits"], "pos", "Sau is cần tính từ mô tả quantity; limited nghĩa là có hạn.", "Limited is an adjective describing the quantity of bags, which is not unlimited."),
    (["assistance", "assist", "assisted", "assisting"], "pos", "Sau provide cần danh từ assistance.", "Provide assistance is the appropriate noun phrase. The other choices are verb forms.")],
   topic="shopping", level=3)

P6("p6_d1_appointment", "Consultation appointments",
   "Our design studio is offering free twenty-minute consultations to small businesses this month. To reserve a time, complete the online form and choose a slot that is {1} for your team. A consultant will send a confirmation message within one business day.\n\nPlease provide a brief description of your project {2} the appointment. This will allow us to prepare relevant examples. {3} the consultation is free, clients are responsible for any printing or production costs.\n\nAppointments may be changed once, provided that the request is made {4} twenty-four hours in advance.",
   [(["convenient", "convenience", "conveniently", "convening"], "pos", "Sau is cần tính từ convenient để mô tả slot phù hợp.", "The linking verb is needs an adjective. Convenient describes a time that works for the team."),
    (["before", "during", "among", "beside"], "preposition", "Before the appointment chỉ thời điểm gửi mô tả trước buổi hẹn.", "Before is followed by the noun phrase the appointment and gives the required earlier time."),
    (["Although", "Because", "Unless", "Therefore"], "conjunction", "Although thể hiện sự tương phản giữa miễn phí và các chi phí khác.", "Although introduces the contrast: the consultation costs nothing, but printing and production still cost money."),
    (["at least", "almost", "already", "only"], "adverb", "At least twenty-four hours nghĩa là sớm tối thiểu 24 giờ.", "At least expresses the minimum notice required for changing an appointment.")],
   topic="office", level=3)
