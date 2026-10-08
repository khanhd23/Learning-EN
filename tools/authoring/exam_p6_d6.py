"""TOEIC Part 6 bank batch 6: original meetings, sales, and workplace messages."""
from lib import P6


P6("p6_d6_meeting", "Meeting preparation guide",
   "The quarterly meeting will take place on Thursday afternoon. Please review the attached agenda and send any proposed changes {1} Tuesday. Each department should prepare a short update on its current projects.\n\nTo keep the meeting focused, presenters should limit their remarks to ten minutes. {2} a topic requires more discussion, it can be added to the follow-up list. The chairperson will distribute the final minutes {3} the meeting and record all action items {4}.",
   [(["by", "until", "during", "since"], "preposition", "By Tuesday là hạn chót gửi thay đổi.", "By gives the deadline for sending proposed changes."),
    (["If", "Although", "During", "Because of"], "conjunction", "If mở đầu điều kiện một chủ đề cần thảo luận thêm.", "If introduces the condition for adding a topic to the follow-up list."),
    (["after", "among", "because", "despite"], "preposition", "After the meeting chỉ thời điểm phát biên bản.", "After is followed by the noun phrase the meeting and gives the distribution time."),
    (["separately", "separate", "separation", "separating"], "pos", "Record là động từ cần trạng từ separately.", "Separately modifies record and means each action item is recorded on its own.")],
   topic="office", level=3)

P6("p6_d6_sales", "Sales follow-up procedure",
   "After speaking with a potential customer, sales representatives should enter the contact's details in the customer relationship system. The information should be entered {1} so that other representatives can understand the conversation. A follow-up reminder should be set for a date that is {2} for the customer.\n\nIf the customer does not respond, send one polite reminder. {3}, do not send repeated messages in a short period. The sales manager will review the records {4} to identify opportunities for training.",
   [(["accurately", "accurate", "accuracy", "accurateness"], "pos", "Entered là động từ cần trạng từ accurately.", "Accurately modifies entered and describes how the contact details should be recorded."),
    (["convenient", "convenience", "conveniently", "convening"], "pos", "Sau is cần tính từ convenient để mô tả ngày phù hợp.", "The linking verb is needs the adjective convenient for a suitable date."),
    (["However", "Because", "Unless", "For example"], "linking", "However nêu giới hạn trái với hướng dẫn gửi một lời nhắc.", "However introduces the contrasting instruction not to send repeated messages."),
    (["monthly", "month", "months", "month-long"], "adverb", "Review the records monthly là cụm chỉ tần suất.", "Monthly is the adverb telling how often the manager reviews the records.")],
   topic="sales", level=3)

P6("p6_d6_lease", "Lease renewal options",
   "The office lease will end on December 31. The property manager has offered two renewal options: a one-year lease with a fixed rate or a two-year lease with a small discount. Tenants should notify the manager of their choice {1} November 30.\n\nThe building will undergo minor repairs during the renewal period. {2} the work may cause some noise, it will not affect access to the offices. Tenants who have questions about the options should request a meeting {3}. The manager will send the final documents after all tenants have responded {4}.",
   [(["by", "until", "during", "from"], "preposition", "By November 30 là hạn chót thông báo.", "By sets the deadline for notifying the manager."),
    (["Although", "Because of", "During", "Unless"], "conjunction", "Although nối sự tương phản giữa có tiếng ồn và vẫn ra vào được.", "Although introduces the contrast between noise and continued access."),
    (["in person", "person", "personal", "personally"], "phrase", "Request a meeting in person là cụm chỉ gặp trực tiếp.", "In person is the adverbial phrase meaning face to face."),
    (["in writing", "writing", "written", "write"], "phrase", "Respond in writing là cụm chỉ phản hồi bằng văn bản.", "In writing is the fixed phrase meaning in a written form.")],
   topic="home_furniture", level=4)

P6("p6_d6_training", "Training completion notice",
   "All employees must complete the annual data-security training by August 20. The course consists of three short modules and a final quiz. Employees may complete the modules {1} their regular working hours with their supervisor's approval.\n\nThe quiz can be retaken if the first score is below the required level. {2}, employees should review the relevant module before trying again. The training system will send a reminder to anyone who has not completed the course {3} August 15. Please contact Human Resources if you need technical {4}.",
   [(["during", "between", "among", "toward"], "preposition", "During their regular working hours chỉ khoảng thời gian học.", "During is followed by the noun phrase their regular working hours."),
    (["However", "Because", "Unless", "Similarly"], "linking", "However nối việc được thi lại với lời khuyên nên xem lại bài.", "However introduces advice that contrasts with the possibility of retaking the quiz."),
    (["by", "until", "during", "since"], "preposition", "Before August 15 would also express the intended idea, but the text says reminder to anyone ... by August 15: by is the deadline.", "By August 15 sets the point by which the reminder will have been sent."),
    (["assistance", "assist", "assisted", "assisting"], "pos", "Need technical assistance là cụm danh từ.", "Technical assistance is the noun phrase needed after need.")],
   topic="education", level=3)
