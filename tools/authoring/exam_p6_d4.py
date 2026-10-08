"""TOEIC Part 6 bank batch 4: original finance and customer-service messages."""
from lib import P6


P6("p6_d4_refund", "Refund processing update",
   "We have received your return request and will inspect the item within two business days. If the return meets our policy, the refund will be issued {1} to your original payment method. Please note that your bank may need additional time to {2} the funds.\n\nWe will email you when the refund is processed. {3}, keep the tracking number until the money appears in your account. If you have any questions, our support team is available to provide further {4}.",
   [(["directly", "direct", "direction", "directing"], "pos", "Issued là động từ nên cần trạng từ directly.", "Directly modifies issued and explains that the refund goes to the original method."),
    (["release", "released", "releasing", "releases"], "pos", "Sau to cần động từ nguyên mẫu release.", "The infinitive to release is required after need additional time to."),
    (["Meanwhile", "Because", "Although", "Unless"], "linking", "Meanwhile đưa ra hướng dẫn trong thời gian chờ tiền xuất hiện.", "Meanwhile introduces an action to take during the waiting period."),
    (["assistance", "assist", "assisted", "assisting"], "pos", "Provide further cần danh từ assistance.", "Provide assistance is the correct noun phrase for help from the support team.")],
   topic="shopping", level=3)

P6("p6_d4_budget", "Department budget review",
   "The annual budget review will begin next week. Each department should submit a forecast of expected expenses and revenue. Please use the template provided by Finance and make sure that all figures are {1}.\n\nThe review committee will compare the forecasts with last year's results. {2} a department expects a major change, it should include a short explanation. The committee may request {3} information before approving the budget. Final decisions will be sent to managers {4} the end of the month.",
   [(["accurate", "accuracy", "accurately", "accumulate"], "pos", "Sau are cần tính từ accurate để mô tả figures.", "Accurate is an adjective describing figures that are correct."),
    (["If", "Despite", "During", "Because of"], "conjunction", "If mở đầu điều kiện department expects a major change.", "If introduces the condition requiring an explanation."),
    (["additional", "add", "additionally", "addition"], "pos", "Information không đếm được cần tính từ additional.", "Additional is an adjective modifying the uncountable noun information."),
    (["by", "until", "during", "since"], "preposition", "By the end of the month là hạn chót.", "By sets the deadline for sending final decisions.")],
   topic="finance", level=3)

P6("p6_d4_support", "Support center hours",
   "To improve response times, our support center will open one hour earlier on weekdays starting May 1. Customers may reach an agent by phone or chat during the new hours. The updated schedule is available {1} our website.\n\nOur team will answer technical questions and help customers reset passwords. {2} some requests require specialist assistance, the first agent may create a ticket for another team. Please have your account number ready so that we can identify you {3}. We appreciate your patience while the new schedule is being {4}.",
   [(["on", "at", "in", "to"], "preposition", "Available on our website là cụm dùng on với nền tảng/trang web.", "Available on is the usual phrase for information published on a website."),
    (["Although", "Because of", "During", "Unless"], "conjunction", "Although thể hiện tương phản giữa hỗ trợ ngay và trường hợp cần chuyên gia.", "Although introduces the contrast that some requests need another team."),
    (["quickly", "quick", "quickness", "quicken"], "pos", "Identify là động từ cần trạng từ quickly.", "Quickly is an adverb describing how the team can identify the customer."),
    (["implemented", "implement", "implementation", "implementing"], "passive", "Sau is being cần phân từ implemented trong bị động tiếp diễn.", "Is being implemented is the present progressive passive form for the schedule change.")],
   topic="tech", level=4)

P6("p6_d4_event", "Conference registration reminder",
   "Registration for the regional sales conference closes on June 10. Employees who plan to attend should complete the online form as soon as {1}. The fee includes lunch and conference materials.\n\nBecause seating is limited, registrations will be accepted in the order {2} are received. If you need to cancel, notify the events team {3} possible. This will allow another employee to use the place. A detailed agenda will be sent to all {4} participants next week.",
   [(["possible", "possibly", "possibility", "possess"], "phrase", "As soon as possible là cụm cố định.", "The fixed expression is as soon as possible, meaning without unnecessary delay."),
    (["in which they", "which they", "where they", "they"], "relative", "Trong order in which they are received, they thay cho registrations.", "In which they are received is a relative clause modifying order; they refers to registrations."),
    (["as soon as", "as well as", "as long as", "as far as"], "phrase", "Notify as soon as possible phù hợp ý báo sớm.", "As soon as possible means at the earliest practical time and fits the request to cancel promptly."),
    (["registered", "registering", "registration", "register"], "pos", "Participants đã đăng ký được mô tả bằng registered.", "Registered is a past participle adjective describing participants who completed registration.")],
   topic="events", level=3)
