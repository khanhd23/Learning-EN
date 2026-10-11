"""Author and materialize the three complete TOEIC Part 3 and Part 4 practices.

The scripts are original practice material.  The ETS URL remains a format reference
only; no official scripts or questions are copied.
"""

from __future__ import annotations

import json
from pathlib import Path


ROOT = Path(__file__).resolve().parents[1]


def question(number, stem, options, answer, explanation, evidence):
    return {
        "id_suffix": f"q{number}",
        "stem": stem,
        "opts": options,
        "ans": answer,
        "expl": explanation,
        "evidence": evidence,
    }


def group(section, number, set_id, title, passage, questions):
    prefix = f"{section}_example_{number:03d}"
    return {
        "id": prefix,
        "section": section,
        "level": 3 if number % 3 else 4,
        "set": str(set_id),
        "source": "original:exam-expansion-2026",
        "title": title,
        "passage": passage,
        "items": [
            {
                "id": f"{prefix}_{item['id_suffix']}",
                "stem": item["stem"],
                "opts": item["opts"],
                "ans": item["ans"],
                "expl": item["expl"],
                "evidence": item["evidence"],
            }
            for item in questions
        ],
    }


P3_NEW = [
    group("p3", 14, 2, "A clinic appointment moves",
          "Woman: I am calling about my appointment with Dr. Lee on Thursday. Man: The doctor will be attending a training session that morning. Woman: Could I move my appointment to Thursday afternoon? Man: The first opening is at 3:30, and the nurse can complete the paperwork before the visit. Woman: That works for me. I also need to update my emergency contact. Man: Bring the new information to the front desk, and arrive fifteen minutes early so we can enter it in the system.",
          [
              question(1, "Why is the appointment being changed?", ["The doctor will attend training.", "The clinic will close for repairs.", "The nurse is moving to another office.", "The patient forgot the appointment."], 0, "The doctor is attending a training session that morning.", "attending a training session"),
              question(2, "When is the first afternoon opening?", ["At 1:15", "At 2:00", "At 3:30", "At 4:45"], 2, "The first available afternoon appointment is at 3:30.", "The first opening is at 3:30"),
              question(3, "What should the woman bring?", ["A payment receipt", "New emergency-contact information", "A doctor's note", "A training certificate"], 1, "She needs to bring the updated emergency-contact information.", "update my emergency contact"),
          ]),
    group("p3", 15, 2, "A delivery for the office move",
          "Man: Has the moving company confirmed the delivery time for the new desks? Woman: Yes, the truck will arrive between ten and noon on Friday. Man: That is earlier than I expected. The loading area is still being painted. Woman: The building manager said the crew can use the side entrance instead. Man: Then I will reserve that entrance and ask the movers to call me when they are ten minutes away. Woman: Good. I will label the desk boxes by department before the truck arrives.",
          [
              question(1, "What are the speakers arranging?", ["A delivery of office furniture", "A painting class", "A new department budget", "A building lease"], 0, "They are arranging the delivery of desks for an office move.", "delivery time for the new desks"),
              question(2, "Why cannot the truck use the loading area?", ["It is too narrow.", "It is being painted.", "It is reserved for visitors.", "It has been moved underground."], 1, "The loading area is being painted.", "The loading area is still being painted"),
              question(3, "What task will the woman handle before the truck arrives?", ["Call the building manager", "Reserve the side entrance", "Label the desk boxes", "Drive the moving truck"], 2, "She will label the desk boxes by department.", "label the desk boxes by department"),
          ]),
    group("p3", 16, 2, "Counting a store inventory",
          "Woman: The store manager asked us to finish the inventory count before closing. Man: I can count the kitchen section, but several price tags are missing. Woman: I will print replacement tags from the office. Man: Thanks. Should I record the damaged items separately? Woman: Yes, place them on the blue cart and enter their product numbers on this sheet. Man: The stockroom shelves are almost finished, so we should have the report ready by six. Woman: I will review the numbers before sending it to the manager.",
          [
              question(1, "What task must be completed before closing?", ["A customer survey", "An inventory count", "A staff schedule", "A delivery order"], 1, "The staff must finish the inventory count before the store closes.", "finish the inventory count"),
              question(2, "Which inventory issue does the man report?", ["Some products are damaged.", "The office printer is broken.", "Some price tags are missing.", "The stockroom is locked."], 2, "He says that several price tags are missing.", "several price tags are missing"),
              question(3, "What will the woman probably do before sending the report?", ["Review the numbers", "Move the blue cart", "Close the stockroom", "Call a customer"], 0, "She will review the numbers before sending the report.", "review the numbers"),
          ]),
    group("p3", 17, 2, "A conference speaker changes travel plans",
          "Man: I received a message from our guest speaker. Her flight will arrive two hours late. Woman: Will she still reach the conference before her opening session? Man: Yes, but she will miss the welcome dinner tonight. Woman: I can ask the hotel to keep a meal for her. Man: Good idea. I will move her airport pickup to 9:30 and tell the registration team about the delay. Woman: Please also change the program note that says she will greet attendees at the dinner.",
          [
              question(1, "What problem has occurred?", ["A conference room is unavailable.", "A flight has been delayed.", "A dinner has been cancelled.", "A registration system has failed."], 1, "The guest speaker's flight will arrive two hours late.", "flight will arrive two hours late"),
              question(2, "What will the woman arrange?", ["A meal at the hotel", "A new flight", "A conference badge", "A welcome speech"], 0, "She will ask the hotel to keep a meal for the speaker.", "ask the hotel to keep a meal"),
              question(3, "What will the man change?", ["The conference location", "The speaker's airport pickup", "The hotel reservation", "The dinner menu"], 1, "He will move the airport pickup to 9:30.", "move her airport pickup to 9:30"),
          ]),
    group("p3", 18, 2, "Apartment maintenance request",
          "Woman: The maintenance office returned my request about the leaking kitchen faucet. Man: Did they give you a repair time? Woman: They can send a plumber tomorrow between eight and ten. Man: I will be at work then. Could you leave the spare key with the concierge? Woman: I can, but the plumber also needs access to the cabinet under the sink. Man: That is fine. Please ask the concierge to call me when the work is finished. Woman: I will add that note to the request.",
          [
              question(1, "What problem does the woman report?", ["A broken cabinet door", "A leaking faucet", "A missing spare key", "A blocked elevator"], 1, "She submitted a request about a leaking kitchen faucet.", "leaking kitchen faucet"),
              question(2, "When will the plumber arrive?", ["This afternoon", "Tomorrow between eight and ten", "Tomorrow after lunch", "Next week"], 1, "The plumber can come tomorrow between eight and ten.", "tomorrow between eight and ten"),
              question(3, "What does the man ask the woman to do?", ["Call his employer", "Leave a key with the concierge", "Repair the cabinet", "Meet the plumber at work"], 1, "He asks her to leave the spare key with the concierge.", "leave the spare key with the concierge"),
          ]),
    group("p3", 19, 2, "Replacing staff photo badges",
          "Man: The security office says our staff badges need new photos this month. Woman: I saw the notice. Is the photo session still in the lobby? Man: No, it moved to Room 204 because the lobby is being renovated. Woman: When should our team go? Man: The photographer will be there from noon to four on Wednesday. Woman: I teach a class at noon, so I will go after two. Man: I will reserve the 2:30 slot for you and remind everyone to bring their old badges.",
          [
              question(1, "What is the purpose of the photo session?", ["To make new staff badges", "To advertise a security course", "To inspect the lobby", "To schedule classes"], 0, "Staff members need new photos for replacement badges.", "staff badges need new photos"),
              question(2, "Why has the location changed?", ["Room 204 is larger.", "The lobby is being renovated.", "The photographer requested a window.", "The security office moved."], 1, "The lobby is being renovated.", "the lobby is being renovated"),
              question(3, "What will the man do for the woman?", ["Teach her class", "Bring a new camera", "Reserve a time slot", "Collect her old badge"], 2, "He will reserve the 2:30 slot for her.", "reserve the 2:30 slot"),
          ]),
    group("p3", 20, 2, "Access to a new software system",
          "Woman: I tried to open the new scheduling system, but my account is not active. Man: Did you complete the security course? Woman: Yes, I finished it yesterday and received a confirmation message. Man: The access list is updated every evening, so your account should work tomorrow morning. Woman: I need to prepare next week's schedule tonight. Man: I can ask the IT desk for temporary access. Woman: That would help. I will forward my course confirmation to you now.",
          [
              question(1, "What difficulty is the woman having?", ["She cannot access a software system.", "She lost a training certificate.", "She cannot change a work schedule.", "She forgot her account password."], 0, "Her account for the new scheduling system is not active.", "my account is not active"),
              question(2, "When is the access list normally updated?", ["Every hour", "At noon", "Every evening", "Once a week"], 2, "The man says the access list is updated every evening.", "updated every evening"),
              question(3, "What will the woman send?", ["A weekly schedule", "A course confirmation", "A password list", "An IT invoice"], 1, "She will forward her security-course confirmation.", "forward my course confirmation"),
          ]),
    group("p3", 21, 2, "Volunteers for a community festival",
          "Man: We still need someone to manage the information table at Saturday's festival. Woman: I can work there until noon, but I have a food-stall shift later. Man: That is enough for the morning. Could you prepare a sign with the event map? Woman: Yes, and I can print a list of emergency phone numbers. Man: Great. The organizers will deliver the table and chairs at eight. Woman: I will arrive at 7:45 to set up the signs before visitors arrive.",
          [
              question(1, "What position is the woman willing to fill?", ["Information-table volunteer", "Food-stall manager", "Festival photographer", "Emergency driver"], 0, "She will manage the information table in the morning.", "manage the information table"),
              question(2, "What will she prepare?", ["A food menu", "A sign with the event map", "A list of volunteers' addresses", "A set of chairs"], 1, "She will prepare a sign with the event map.", "prepare a sign with the event map"),
              question(3, "When will she arrive?", ["At 7:15", "At 7:45", "At 8:30", "At noon"], 1, "She plans to arrive at 7:45.", "arrive at 7:45"),
          ]),
    group("p3", 22, 2, "Labels for a factory shipment",
          "Woman: The customer changed the destination on the equipment order. Man: Has the shipping label been printed? Woman: Not yet. The original label says Eastport, but the equipment must go to Lakeview. Man: I will update the address in the system and print a new label. Woman: Please also check the delivery date. The customer wants the crates there by the 18th. Man: The carrier can meet that date if we load the truck on Monday. Woman: I will tell the warehouse team to have everything ready.",
          [
              question(1, "What has the customer changed?", ["The equipment design", "The shipment destination", "The delivery company", "The order quantity"], 1, "The equipment must now be sent to Lakeview instead of Eastport.", "changed the destination"),
              question(2, "Which action will the man take?", ["Load the crates", "Contact the customer", "Print a new shipping label", "Move the delivery date"], 2, "He will update the address and print a new label.", "print a new label"),
              question(3, "When must the crates arrive?", ["By the 8th", "By the 15th", "By the 18th", "By the end of the month"], 2, "The customer wants the crates delivered by the 18th.", "by the 18th"),
          ]),
    group("p3", 23, 2, "Catering a café meeting",
          "Man: I am ordering lunch for tomorrow's planning meeting. Woman: How many people should I prepare for? Man: Eight employees and three visitors. Two visitors have a nut allergy, so please avoid the usual dessert. Woman: We can provide fruit cups instead. Man: That sounds good. The meeting begins at eleven, but lunch will be served at twelve-thirty. Woman: I will deliver the meals to Conference Room A and label the allergy-safe boxes clearly.",
          [
              question(1, "What is the man ordering?", ["Office furniture", "Lunch for a meeting", "Conference equipment", "A travel ticket"], 1, "He is ordering lunch for a planning meeting.", "ordering lunch for tomorrow's planning meeting"),
              question(2, "Why should the usual dessert be avoided?", ["It is too expensive.", "It contains nuts.", "It arrives too late.", "It is not available on Mondays."], 1, "Two visitors have a nut allergy.", "Two visitors have a nut allergy"),
              question(3, "Where will the meals be delivered?", ["The café entrance", "The reception desk", "Conference Room A", "The training kitchen"], 2, "The woman will deliver the meals to Conference Room A.", "deliver the meals to Conference Room A"),
          ]),
    group("p3", 24, 2, "A school career fair",
          "Woman: Have the companies confirmed their tables for Friday's career fair? Man: Most have, but the engineering firm needs an electrical outlet for its demonstration. Woman: I will move its table beside the wall outlet. Man: Thanks. The student registration list also needs updating because two classes will attend in the afternoon. Woman: I will print a second copy for the welcome desk. Man: Please place the company name cards on each table before the first students arrive. Woman: I will ask the volunteers to do that at eight.",
          [
              question(1, "What event are the speakers preparing for?", ["A student career fair", "A science examination", "A school sports day", "A parent meeting"], 0, "They are preparing tables and materials for a career fair.", "Friday's career fair"),
              question(2, "Why will one table be moved?", ["It needs more chairs.", "It must be near an electrical outlet.", "It is blocking the welcome desk.", "It belongs to an afternoon class."], 1, "The engineering company needs an electrical outlet for a demonstration.", "needs an electrical outlet"),
              question(3, "Who will place the company name cards?", ["The engineering firm", "The students", "The volunteers", "The afternoon teachers"], 2, "The man asks the woman to have the volunteers place them.", "ask the volunteers to do that"),
          ]),
    group("p3", 25, 2, "A hotel shuttle schedule",
          "Man: The airport shuttle is running on a different schedule this weekend. Woman: Is that because of the road construction? Man: Yes. The hotel entrance road will be closed from Saturday morning until Sunday evening. Woman: Where should guests wait for the shuttle? Man: At the east parking lot, beside the bicycle rental desk. Woman: I will put a notice at reception and send a message to guests with airport reservations. Man: Good. The first shuttle will leave at 5:30 instead of 5:00.",
          [
              question(1, "Why is the shuttle schedule changing?", ["The hotel is changing owners.", "Road construction will close an entrance.", "The airport has moved terminals.", "The bicycle desk is closed."], 1, "Road construction will close the hotel entrance road.", "road construction"),
              question(2, "Where should guests wait?", ["At the hotel entrance", "At the east parking lot", "Near the airport ticket desk", "Beside the restaurant"], 1, "Guests should wait at the east parking lot.", "At the east parking lot"),
              question(3, "How will the woman inform travelers?", ["Repair the hotel road", "Change the shuttle bus", "Notify guests", "Rent bicycles"], 2, "She will post a notice and message guests with reservations.", "send a message to guests"),
          ]),
    group("p3", 26, 2, "A museum membership renewal",
          "Woman: I would like to renew my museum membership, but the online form keeps rejecting my card. Man: The payment system was updated this morning. You can renew at the desk while we test it. Woman: I also want to change my mailing address. Man: We can do that here, and your new membership card will be mailed within ten days. Woman: Excellent. Does the renewal include the summer lecture series? Man: Yes, but reservations for lectures open next Monday. Woman: I will return then to reserve a seat.",
          [
              question(1, "What problem does the woman have?", ["Her membership has expired.", "An online payment is being rejected.", "Her mailing address is missing.", "A lecture was cancelled."], 1, "The online form is rejecting her card.", "online form keeps rejecting my card"),
              question(2, "When will the new card arrive?", ["Within three days", "Within ten days", "Next Monday", "At the end of summer"], 1, "The new membership card will be mailed within ten days.", "mailed within ten days"),
              question(3, "What will the woman do next Monday?", ["Renew her card online", "Attend a lecture", "Reserve a lecture seat", "Change the payment system"], 2, "She will return to reserve a seat in the lecture series.", "return then to reserve a seat"),
          ]),
    group("p3", 27, 3, "A dentist booking",
          "Man: I need to schedule a cleaning for my daughter. Woman: We have openings on Tuesday morning and Thursday after school. Man: Thursday would be better. Does the dentist need her previous records? Woman: Yes, please ask her former clinic to send them before the appointment. Man: I will do that today. Woman: Also, arrive ten minutes early because this is her first visit here. Man: Thank you. I will bring her insurance card and arrive early.",
          [
              question(1, "What appointment does the man want?", ["A dental cleaning", "An eye examination", "A school interview", "An insurance meeting"], 0, "He wants to schedule a cleaning for his daughter.", "schedule a cleaning"),
              question(2, "When will the appointment probably take place?", ["Tuesday morning", "Thursday after school", "Friday before lunch", "Saturday evening"], 1, "Thursday after school is better for him.", "Thursday would be better"),
              question(3, "What should the man bring?", ["A school report", "A former clinic bill", "An insurance card", "A new appointment form"], 2, "He says he will bring his daughter's insurance card.", "bring her insurance card"),
          ]),
    group("p3", 28, 3, "A design review meeting",
          "Woman: I have reviewed the first design for the new website. Man: What do you think of the navigation menu? Woman: It is clear, but the labels are too small on a phone screen. Man: I can increase the font size. Should we keep the blue background? Woman: Yes, it matches the company's logo. Man: I will make the text change before our client meeting on Friday. Woman: Great. I will prepare two phone screenshots so the client can compare the old and new versions.",
          [
              question(1, "What are the speakers reviewing?", ["A website design", "A company logo", "A client invoice", "A phone contract"], 0, "They are reviewing the first design for a website.", "first design for the new website"),
              question(2, "What problem does the woman identify?", ["The background color is wrong.", "The navigation labels are too small.", "The logo is missing.", "The screenshots are outdated."], 1, "She says the labels are too small on a phone screen.", "labels are too small on a phone screen"),
              question(3, "What will the woman prepare?", ["A new menu", "A client invoice", "Two phone screenshots", "A color chart"], 2, "She will prepare two screenshots for the client.", "prepare two phone screenshots"),
          ]),
    group("p3", 29, 3, "A train platform change",
          "Man: Passengers for the 4:20 train should listen for an announcement. Woman: Has the departure time changed? Man: No, but the train will leave from Platform 6 instead of Platform 2. Woman: I am meeting my sister at Platform 2. Man: Ask her to walk through the main hall; the signs will point to Platform 6. Woman: I will text her now. Man: Good. The station staff will also put a notice beside the ticket machines.",
          [
              question(1, "Which detail about the train is different?", ["The train's departure time", "The train's destination", "The departure platform", "The ticket price"], 2, "The train will leave from Platform 6 instead of Platform 2.", "leave from Platform 6"),
              question(2, "Who is the woman meeting?", ["A station employee", "Her sister", "A train conductor", "A ticket agent"], 1, "She says she is meeting her sister.", "meeting my sister"),
              question(3, "What will the station staff do?", ["Change the ticket machines", "Put up a notice", "Delay the train", "Close the main hall"], 1, "They will put a notice beside the ticket machines.", "put a notice beside the ticket machines"),
          ]),
    group("p3", 30, 3, "A customer invoice correction",
          "Woman: The customer says the invoice lists the wrong quantity of chairs. Man: The order was for twenty, but the invoice shows twelve. Woman: I checked the warehouse record, and twenty chairs were shipped. Man: I will correct the invoice and send it again. Woman: Please include a note explaining that the amount due has not changed because the customer was billed for twenty. Man: I will add that note and copy you on the email. Woman: Thanks. I will call the customer after the corrected invoice is sent.",
          [
              question(1, "What error is on the invoice?", ["The chair quantity is incorrect.", "The delivery address is incomplete.", "The payment date is missing.", "The customer name is misspelled."], 0, "The invoice lists twelve chairs instead of twenty.", "invoice shows twelve"),
              question(2, "What will the man send again?", ["Ship eight more chairs", "Correct and resend the invoice", "Call the warehouse", "Change the amount due"], 1, "He will correct the invoice and send it again.", "correct the invoice and send it again"),
              question(3, "What will the woman do after the invoice is resent?", ["Call the customer", "Count the chairs", "Approve a new order", "Update the warehouse record"], 0, "She will call the customer after the corrected invoice is sent.", "call the customer"),
          ]),
    group("p3", 31, 3, "A library author event",
          "Man: The author speaking at the library tonight requested a table for signing books. Woman: We have one in the storage room, but it needs a tablecloth. Man: I can bring one from the events office. Woman: Please also check the microphone. The last speaker had trouble with it. Man: The technician will test it at five. Woman: Good. I will place the reserved-seat cards near the front row and ask the volunteers to guide visitors to the overflow room.",
          [
              question(1, "What will the author do?", ["Sign books", "Teach a music class", "Repair a microphone", "Guide visitors"], 0, "The author requested a table for signing books.", "table for signing books"),
              question(2, "What will the man bring?", ["Reserved-seat cards", "A tablecloth", "An extra microphone", "A storage key"], 1, "He will bring a tablecloth from the events office.", "bring one from the events office"),
              question(3, "What will the woman ask volunteers to do?", ["Test the microphone", "Sell tablecloths", "Guide visitors", "Move the front row"], 2, "She will ask them to guide visitors to the overflow room.", "guide visitors to the overflow room"),
          ]),
    group("p3", 32, 3, "Warehouse safety training",
          "Woman: The safety trainer moved tomorrow's session to the afternoon. Man: What time should warehouse staff arrive? Woman: The session starts at 1:00 in the break room. Man: We normally load the afternoon truck then. Woman: The loading supervisor agreed to start that truck at 2:30 instead. Man: I will post the new training time by the entrance. Woman: Please also remind staff to wear their safety shoes. The trainer will not allow anyone onto the practice floor without them.",
          [
              question(1, "What part of the training schedule has changed?", ["The safety trainer's topic", "The training time", "The warehouse location", "The afternoon truck route"], 1, "The safety training has moved to the afternoon.", "moved tomorrow's session to the afternoon"),
              question(2, "What will happen to the afternoon truck?", ["It will leave earlier.", "It will be loaded at 2:30.", "It will be cancelled.", "It will use the break room."], 1, "The loading supervisor agreed to start it at 2:30.", "start that truck at 2:30"),
              question(3, "What should staff wear?", ["Work gloves only", "A training badge", "Safety shoes", "A company jacket"], 2, "Staff must wear safety shoes on the practice floor.", "wear their safety shoes"),
          ]),
    group("p3", 33, 3, "The office heating problem",
          "Man: The west side of the office is still cold this morning. Woman: I called the building service desk. They found a problem with the thermostat and will send a technician after lunch. Man: Should we move the afternoon meeting? Woman: The small meeting room is warmer, but it only has six chairs. Man: Our team has five people, so that will work. Woman: I will reserve the room and tell everyone to bring their laptops. Man: I will put a sign on the west-side meeting room so no one waits there.",
          [
              question(1, "What issue does the man report?", ["The west office is too cold.", "The meeting room has no tables.", "The laptops need updates.", "The service desk is closed."], 0, "He says that the west side of the office is cold.", "west side of the office is still cold"),
              question(2, "When will the technician arrive?", ["Before breakfast", "At noon", "After lunch", "Tomorrow morning"], 2, "The building service desk will send a technician after lunch.", "send a technician after lunch"),
              question(3, "What arrangement will the woman make?", ["Repair the thermostat", "Reserve a warmer meeting room", "Buy six chairs", "Move the west office"], 1, "She will reserve the small meeting room.", "reserve the room"),
          ]),
    group("p3", 34, 3, "A restaurant reservation",
          "Woman: I am calling about our reservation for Saturday evening. Man: I have it here for six people at seven. Woman: One guest is now vegetarian. Can the restaurant provide a suitable main course? Man: Yes, the chef can prepare a vegetable pasta, but we need the request before Friday. Woman: Please add it to the reservation. Man: I will. Also, would you like to move to the quieter room? Woman: No, the main dining room is fine. We are bringing a small birthday cake.",
          [
              question(1, "What change does the woman request?", ["A different reservation date", "A vegetarian meal", "A larger dining room", "A new birthday cake"], 1, "She asks the restaurant to provide a vegetarian main course.", "One guest is now vegetarian"),
              question(2, "By when must the request be made?", ["Before Friday", "By Saturday at noon", "At seven on Saturday", "After the meal"], 0, "The chef needs the request before Friday.", "before Friday"),
              question(3, "What will the group bring?", ["A vegetable dish", "A birthday cake", "A quiet-room sign", "Six menus"], 1, "They will bring a small birthday cake.", "bringing a small birthday cake"),
          ]),
    group("p3", 35, 3, "A market survey assignment",
          "Man: Have you finished the customer survey for the farmers market? Woman: I collected responses from the morning shoppers, but we still need evening visitors. Man: The market closes at eight. Could you return at six? Woman: Yes, and I will use the shorter version of the questionnaire. Man: Good. Remember to ask each person how they heard about the market. Woman: I will record those answers separately. Man: Afterward, upload the spreadsheet to the shared folder before tomorrow's meeting.",
          [
              question(1, "What is incomplete?", ["The market schedule", "The customer survey", "The shared folder", "The questionnaire design"], 1, "The survey still needs responses from evening visitors.", "we still need evening visitors"),
              question(2, "What will the woman use?", ["A shorter questionnaire", "A new spreadsheet", "A market map", "A price list"], 0, "She will use the shorter version of the questionnaire.", "shorter version of the questionnaire"),
              question(3, "What should she do after the survey?", ["Close the market", "Upload a spreadsheet", "Print more signs", "Call the morning shoppers"], 1, "She should upload the spreadsheet before the next day's meeting.", "upload the spreadsheet"),
          ]),
    group("p3", 36, 3, "A campus parking permit",
          "Woman: I applied for a campus parking permit, but the office says my vehicle number is missing. Man: Did you enter the number from your registration document? Woman: I entered the license plate, not the vehicle identification number. Man: That is the number they need. You can update the application online. Woman: I will do that tonight. Man: Once it is corrected, the permit office will email you a pickup appointment. Woman: Thanks. I will bring the registration document when I collect the permit.",
          [
              question(1, "Why was the application returned?", ["A payment was late.", "A vehicle number was missing.", "The campus lot was full.", "A pickup appointment expired."], 1, "The application was missing the required vehicle identification number.", "vehicle number is missing"),
              question(2, "What did the woman enter by mistake?", ["A license plate number", "A campus room number", "An insurance code", "A registration date"], 0, "She entered the license plate instead of the vehicle identification number.", "entered the license plate"),
              question(3, "What will she bring to the pickup appointment?", ["A campus map", "The registration document", "A parking receipt", "A new application form"], 1, "She will bring the vehicle registration document.", "bring the registration document"),
          ]),
    group("p3", 37, 3, "A charity donation pickup",
          "Man: The charity can collect the donated computers on Wednesday morning. Woman: That is fine, but the loading dock is reserved for another delivery. Man: Could the charity use the side door? Woman: Yes, if the boxes are placed on the marked pallets. Man: I will move the computers there on Tuesday afternoon. Woman: Please remove the old inventory labels first. Man: I will do that and prepare a list of the serial numbers. Woman: Great. The charity driver said he would call when he is fifteen minutes away.",
          [
              question(1, "What will the charity collect?", ["Office computers", "Food donations", "Delivery pallets", "Inventory labels"], 0, "The charity will collect donated computers.", "donated computers"),
              question(2, "Why cannot the charity use the loading dock?", ["It is being painted.", "It is reserved for another delivery.", "The pallets are missing.", "The driver is late."], 1, "The loading dock is reserved for another delivery.", "loading dock is reserved"),
              question(3, "What will the man prepare?", ["A list of serial numbers", "A new loading dock", "A donation receipt", "A driver schedule"], 0, "He will prepare a list of the computers' serial numbers.", "prepare a list of the serial numbers"),
          ]),
    group("p3", 38, 3, "A travel reimbursement",
          "Woman: I submitted my travel reimbursement, but accounting returned it. Man: Which receipt is missing? Woman: The train ticket is attached, but the hotel receipt is only in my email. Man: The policy requires a PDF from the hotel, not a screenshot. Woman: I will download the PDF and attach it today. Man: Also check that the project code is correct. Woman: It says Project 418, but the trip was for Project 481. Man: Then change that before resubmitting the claim.",
          [
              question(1, "Why was the reimbursement returned?", ["A project ended.", "A required hotel receipt was missing.", "The train ticket was too expensive.", "Accounting changed offices."], 1, "Accounting needs the hotel receipt in the required format.", "hotel receipt is only in my email"),
              question(2, "What format is required?", ["A printed ticket", "A PDF", "A photograph", "A spreadsheet"], 1, "The policy requires a PDF from the hotel.", "requires a PDF"),
              question(3, "What else should the woman correct?", ["The travel date", "The hotel name", "The project code", "The train route"], 2, "She must change the incorrect project code before resubmitting.", "change that before resubmitting the claim"),
          ]),
    group("p3", 39, 3, "Conference registration materials",
          "Man: The conference badges arrived, but the attendee list has three spelling errors. Woman: I can correct the list before the badges are printed. Man: The printer needs the final file by four o'clock. Woman: I will ask the registration team to confirm the three names. Man: Good. The welcome packets also need the updated room map. Woman: I will replace the old map and put the packets in alphabetical order. Man: Please leave them in the supply room; the volunteers will carry them to the registration desk tomorrow morning.",
          [
              question(1, "What problem do the speakers identify?", ["Some badges are too large.", "Some names are misspelled.", "The room map is missing.", "The volunteers are unavailable."], 1, "The attendee list contains three spelling errors.", "three spelling errors"),
              question(2, "What will the woman replace?", ["The printer", "The attendee list", "The room map", "The supply room"], 2, "She will replace the old room map in the welcome packets.", "replace the old map"),
              question(3, "Where should the packets be left?", ["At the hotel entrance", "In the supply room", "On the conference stage", "At the airport desk"], 1, "The man asks her to leave them in the supply room.", "leave them in the supply room"),
          ]),
]


P4_NEW = [
    group("p4", 11, 2, "A weather advisory",
          "Good afternoon. The coastal weather service has issued a wind advisory for tonight. Residents near the harbor should secure outdoor furniture and avoid the waterfront after eight o'clock. The ferry company has not cancelled service, but passengers should check the departure board before leaving home. Tomorrow morning, winds are expected to weaken. The city will post another update at six a.m. If the advisory continues, the public works department may close the pier temporarily.",
          [
              question(1, "What is the announcement mainly about?", ["A coastal wind advisory", "A ferry ticket sale", "A harbor festival", "A new furniture store"], 0, "The speaker announces a wind advisory for the coast.", "issued a wind advisory"),
              question(2, "What should residents near the harbor do?", ["Visit the pier after eight", "Secure outdoor furniture", "Cancel all ferry trips", "Call the weather service"], 1, "They should secure outdoor furniture.", "secure outdoor furniture"),
              question(3, "When will the city post another update?", ["At eight tonight", "At midnight", "At six a.m.", "At noon tomorrow"], 2, "The next update will be posted at six a.m.", "post another update at six a.m."),
          ]),
    group("p4", 12, 2, "A radio traffic report",
          "Here is the morning traffic report. An accident is blocking two lanes on the northbound expressway near Exit 14. Drivers heading to the airport should use the river road instead, although that route is moving slowly. The transportation department expects the expressway lanes to reopen before ten o'clock. A shuttle bus from the downtown station is operating normally. We will provide another report after the eight-thirty news.",
          [
              question(1, "What is causing a delay?", ["Road construction at the airport", "An accident on the expressway", "A broken shuttle bus", "A river road closure"], 1, "An accident is blocking two lanes on the expressway.", "An accident is blocking two lanes"),
              question(2, "What route is recommended for airport drivers?", ["The northbound expressway", "The river road", "The downtown station", "Exit 14 only"], 1, "Drivers should use the river road instead.", "use the river road instead"),
              question(3, "When will another report be provided?", ["Before sunrise", "After the eight-thirty news", "At ten o'clock", "At lunchtime"], 1, "The station will provide another report after the eight-thirty news.", "after the eight-thirty news"),
          ]),
    group("p4", 13, 2, "A company town hall",
          "Welcome to this month's company town hall. Our chief operations officer will discuss the new flexible-work policy at two p.m. in the auditorium. Employees who work remotely can join through the internal video link sent this morning. Please submit questions through the online form before the presentation begins. After the policy discussion, the benefits team will explain changes to the health plan. A recording and written summary will be posted on the employee portal tomorrow afternoon.",
          [
              question(1, "What will the chief operations officer discuss?", ["A health-plan claim", "A flexible-work policy", "A video-production plan", "A new employee portal"], 1, "The main presentation concerns the new flexible-work policy.", "new flexible-work policy"),
              question(2, "How can remote employees join?", ["By visiting the benefits desk", "Through an internal video link", "By calling the auditorium", "Through a public website"], 1, "Remote employees can join through the internal video link.", "join through the internal video link"),
              question(3, "When will a written summary be posted?", ["This morning", "At two p.m.", "Tomorrow afternoon", "Next month"], 2, "The summary will be posted tomorrow afternoon.", "posted on the employee portal tomorrow afternoon"),
          ]),
    group("p4", 14, 2, "An airport baggage notice",
          "Passengers arriving on Flight 642 should collect their luggage at Carousel 5. One suitcase from an earlier flight was placed at Carousel 4 by mistake, so travelers should check the baggage screens carefully. If your bag does not appear, visit the service counter beside Door B before leaving the arrivals area. The airport has also announced that the elevator near Carousel 5 is closed for maintenance. Passengers who need assistance may ask an employee to direct them to the elevator at the opposite end of the hall.",
          [
              question(1, "Where should passengers from Flight 642 collect luggage?", ["At Carousel 4", "At Carousel 5", "Beside Door B", "At the opposite hall"], 1, "Their luggage should be collected at Carousel 5.", "collect their luggage at Carousel 5"),
              question(2, "What should passengers do if a bag is missing?", ["Leave the arrivals area", "Visit the service counter", "Use the closed elevator", "Call the earlier flight"], 1, "They should visit the service counter beside Door B.", "visit the service counter beside Door B"),
              question(3, "Why is one elevator unavailable?", ["It is being cleaned.", "It is under maintenance.", "It is reserved for staff.", "It is near the wrong carousel."], 1, "The elevator near Carousel 5 is closed for maintenance.", "closed for maintenance"),
          ]),
    group("p4", 15, 2, "A fitness center announcement",
          "The Riverside Fitness Center will close the indoor pool next Monday for annual cleaning. Members may use the outdoor pool from seven in the morning until sunset, weather permitting. The swimming instructor has moved all Monday lessons to Court 2, where members should meet five minutes early. The weight room and locker rooms will remain open as usual. Pool members can check the center's app on Tuesday to see when the indoor pool will reopen.",
          [
              question(1, "What will happen next Monday?", ["The entire center will close.", "The indoor pool will close.", "The outdoor pool will be cleaned.", "The weight room will move."], 1, "The indoor pool will close for annual cleaning.", "close the indoor pool next Monday"),
              question(2, "Where will Monday lessons take place?", ["In the indoor pool", "At the outdoor pool", "On Court 2", "In the weight room"], 2, "Monday lessons have been moved to Court 2.", "moved all Monday lessons to Court 2"),
              question(3, "When can members check the reopening information?", ["On Monday morning", "At sunset", "On Tuesday", "Next month"], 2, "Members can check the app on Tuesday.", "check the center's app on Tuesday"),
          ]),
    group("p4", 16, 2, "A software update",
          "This is a reminder for all staff using the customer database. The system will be unavailable from 9 p.m. until midnight while the technology team installs an update. Please save open records before nine and do not begin a new customer entry during the maintenance period. The update will add a faster search tool and a new report filter. A short guide will appear on the support portal tomorrow. If you have an urgent request tonight, send it to the technology desk by email.",
          [
              question(1, "Why will the database be unavailable?", ["A customer audit is scheduled.", "An update will be installed.", "A report filter was removed.", "The support portal is closed."], 1, "The technology team will install an update.", "installs an update"),
              question(2, "What should staff do before nine?", ["Start a new entry", "Save open records", "Delete old reports", "Print the support guide"], 1, "Staff should save open records before the system becomes unavailable.", "save open records before nine"),
              question(3, "What will the update add?", ["A new customer service desk", "A faster search tool", "A second database", "An email schedule"], 1, "The update will add a faster search tool and a report filter.", "add a faster search tool"),
          ]),
    group("p4", 17, 2, "A hotel breakfast notice",
          "Guests staying at the Maple Hotel can now enter the breakfast room from 6:30 to 10:00 on weekdays. On Saturday, breakfast will begin at 7:00 because the kitchen staff will attend a safety meeting. Guests with early airport departures may request a takeaway box at the front desk before nine the previous evening. The restaurant is on the ground floor beside the garden entrance. Please remember that hot drinks are available only inside the restaurant, not in the lobby.",
          [
              question(1, "When does weekday breakfast begin?", ["At 6:00", "At 6:30", "At 7:00", "At 10:00"], 1, "Weekday breakfast begins at 6:30.", "from 6:30 to 10:00 on weekdays"),
              question(2, "Why will Saturday breakfast begin later?", ["The kitchen staff has a safety meeting.", "The restaurant is being painted.", "Guests requested a later meal.", "The garden entrance is closed."], 0, "The kitchen staff will attend a safety meeting.", "kitchen staff will attend a safety meeting"),
              question(3, "Where can early travelers request a takeaway box?", ["In the garden", "At the front desk", "In the lobby restaurant", "At the airport"], 1, "They can request one at the front desk.", "request a takeaway box at the front desk"),
          ]),
    group("p4", 18, 2, "Warehouse safety reminder",
          "Before today's loading shift begins, please remember the warehouse safety procedure. Drivers must stop their vehicles completely at the yellow line before entering the loading zone. Staff moving boxes should wear gloves and use the marked walking lane. The north loading door is closed because its warning light is being repaired, so use the central door instead. Supervisors will inspect the area at 3 p.m. and record any blocked lanes. Report damaged equipment to the floor manager immediately.",
          [
              question(1, "What should drivers do at the yellow line?", ["Park for the day", "Stop completely", "Move to the north door", "Call the floor manager"], 1, "Drivers must stop completely at the yellow line.", "stop their vehicles completely"),
              question(2, "Which door should staff use?", ["The north loading door", "The central door", "The garden door", "The staff entrance"], 1, "The north loading door is closed, so staff should use the central door.", "use the central door instead"),
              question(3, "What will supervisors do at 3 p.m.?", ["Repair the warning light", "Inspect the area", "Load the vehicles", "Train new drivers"], 1, "Supervisors will inspect the area and record blocked lanes.", "inspect the area at 3 p.m."),
          ]),
    group("p4", 19, 2, "A community lecture",
          "This evening's community lecture on urban gardening will begin at 7 p.m. in the East Hall. The speaker has asked attendees to bring a notebook because there will be a short planning activity after the presentation. Seats are available without reservation, but the hall can hold only eighty people. The community center will provide water near the entrance. After the lecture, participants may sign up for Saturday's garden tour at the information table. The tour is free, but registration is required.",
          [
              question(1, "What is the lecture about?", ["Urban gardening", "Water safety", "Community transportation", "Hall reservations"], 0, "The lecture concerns urban gardening.", "lecture on urban gardening"),
              question(2, "What should attendees bring?", ["A gardening tool", "A notebook", "A reservation card", "A water bottle from home"], 1, "The speaker asks attendees to bring a notebook.", "bring a notebook"),
              question(3, "Where can participants register for the garden tour?", ["At the East Hall stage", "At the information table", "Near the water station", "At the Saturday garden"], 1, "Registration will be available at the information table.", "sign up for Saturday's garden tour at the information table"),
          ]),
    group("p4", 20, 2, "A restaurant opening",
          "The Harbor Table restaurant will open for dinner service this Friday at 5:30. The lunch menu will not be available until next week while the kitchen staff completes training. Customers may make reservations through the restaurant website or by calling the host stand after noon. A parking area behind the building is available to diners, but spaces are limited. The restaurant will offer a complimentary dessert to the first twenty reservations on Friday evening. Please note that the entrance is on Bay Street, not on the waterfront walkway.",
          [
              question(1, "When will the restaurant open for dinner?", ["This Friday at 5:30", "This Friday at noon", "Next week at 5:30", "Next month"], 0, "Dinner service will begin this Friday at 5:30.", "open for dinner service this Friday at 5:30"),
              question(2, "How can customers make reservations?", ["Only at the parking area", "Through the website or by phone", "At the waterfront walkway", "Only during lunch"], 1, "Reservations can be made through the website or by calling the host stand.", "through the restaurant website or by calling"),
              question(3, "What will the first twenty Friday reservations receive?", ["Free parking for a month", "A complimentary dessert", "A lunch menu", "A waterfront table"], 1, "The first twenty reservations receive a complimentary dessert.", "complimentary dessert to the first twenty reservations"),
          ]),
    group("p4", 21, 3, "A museum audio guide",
          "Visitors to the West Gallery can borrow an audio guide at the desk beside the main entrance. The guide is available in four languages and includes a route for the new sculpture exhibit. Please return the device before visiting the café because it may not be taken into the outdoor garden. The final guided tour leaves at 4:30, but visitors using the audio guide may remain until the gallery closes at six. A staff member will explain how to select a language when the device is issued.",
          [
              question(1, "Where can visitors borrow an audio guide?", ["At the café", "Beside the main entrance", "In the outdoor garden", "At the final tour"], 1, "The audio guide desk is beside the main entrance.", "desk beside the main entrance"),
              question(2, "How many languages does the guide include?", ["Two", "Three", "Four", "Six"], 2, "The guide is available in four languages.", "available in four languages"),
              question(3, "When must visitors return the device?", ["Before visiting the café", "At 4:30 only", "Before entering the gallery", "At noon"], 0, "The device must be returned before visitors go to the café.", "return the device before visiting the café"),
          ]),
    group("p4", 22, 3, "A clinic instruction",
          "Patients visiting the Riverside Clinic for a blood test should check in at the laboratory counter on the second floor. Please avoid eating for eight hours before the appointment, although water is permitted. Bring your identification card and the test request from your doctor. The laboratory opens at 7 a.m., but appointments after 9:30 may experience a longer wait. If you need to change your appointment, call the scheduling office at least one day in advance. Results will be sent to the requesting doctor within three business days.",
          [
              question(1, "Where should blood-test patients check in?", ["At the main pharmacy", "At the laboratory counter", "In the doctor's office", "On the first floor"], 1, "They should check in at the laboratory counter on the second floor.", "laboratory counter on the second floor"),
              question(2, "What may patients drink before the appointment?", ["Coffee", "Juice", "Water", "Milk"], 2, "Water is permitted during the fasting period.", "although water is permitted"),
              question(3, "When will results be sent?", ["Within three hours", "Within one business day", "Within three business days", "At the next appointment"], 2, "Results will be sent within three business days.", "within three business days"),
          ]),
    group("p4", 23, 3, "A transit service notice",
          "Passengers on the Green Line should use buses between Central Station and Park Street this weekend. Track maintenance will close that section from Saturday morning through Sunday night. Trains will continue to run between Park Street and the northern stations. Replacement buses will stop outside each closed station, but they may take up to fifteen minutes longer than the train. Passengers traveling to the museum should exit at Park Street and follow the signs to the museum district. Regular train service is expected to resume Monday.",
          [
              question(1, "Why will buses replace trains on part of the Green Line?", ["A station is being expanded.", "Track maintenance is scheduled.", "The museum is closed.", "A bus driver is unavailable."], 1, "Track maintenance will close the section.", "Track maintenance will close that section"),
              question(2, "Where should museum visitors exit?", ["At Central Station", "At Park Street", "At the northern stations", "Outside the bus garage"], 1, "They should exit at Park Street.", "exit at Park Street"),
              question(3, "When is regular train service expected to resume?", ["Saturday morning", "Sunday afternoon", "Monday", "Next month"], 2, "Regular service is expected to resume Monday.", "resume Monday"),
          ]),
    group("p4", 24, 3, "A school announcement",
          "Students who registered for the science field trip should meet outside the library at 8:15 on Thursday. The buses will leave at 8:30, and late students will need to arrange their own transportation. Bring a packed lunch, a water bottle, and the permission form signed by a parent or guardian. The weather forecast calls for rain, so a light jacket is recommended. The group will return to school at approximately 4 p.m. Students with questions about the trip should speak with Ms. Ortega before Wednesday afternoon.",
          [
              question(1, "Where should students meet?", ["Outside the library", "At the science museum", "Beside the buses at four", "In Ms. Ortega's office"], 0, "Students should meet outside the library.", "meet outside the library"),
              question(2, "What must students bring?", ["A camera only", "A signed permission form", "A school uniform", "A train ticket"], 1, "They must bring a permission form signed by a parent or guardian.", "permission form signed by a parent or guardian"),
              question(3, "Why is a light jacket recommended?", ["The buses are cold.", "Rain is expected.", "The trip ends late.", "The library is being repaired."], 1, "The forecast calls for rain.", "weather forecast calls for rain"),
          ]),
    group("p4", 25, 3, "A retail promotion",
          "Customers at Greenway Home Goods can receive a ten-dollar discount on purchases of kitchen equipment this weekend. The offer applies only to items priced above fifty dollars and cannot be combined with another coupon. Members of the store's loyalty program may use the discount on Saturday or Sunday, while other customers may use it on Sunday only. To receive the discount, present the promotion code at the checkout counter before payment. The code is printed in this week's store newsletter and is not available at the customer-service desk.",
          [
              question(1, "What does the promotion offer?", ["A free kitchen item", "A ten-dollar discount", "A loyalty membership", "A newsletter subscription"], 1, "The promotion offers a ten-dollar discount.", "receive a ten-dollar discount"),
              question(2, "Which customers may use the discount on Saturday?", ["All customers", "Only loyalty-program members", "Only new customers", "Customers with another coupon"], 1, "Only loyalty-program members may use it on Saturday.", "Members of the store's loyalty program may use"),
              question(3, "Where is the promotion code printed?", ["At the customer-service desk", "In the store newsletter", "On every kitchen item", "At the checkout counter"], 1, "The code is printed in the weekly store newsletter.", "printed in this week's store newsletter"),
          ]),
    group("p4", 26, 3, "A construction notice",
          "The east entrance of the office complex will be closed from Monday through Wednesday while workers replace the front steps. Employees should enter through the west entrance and allow extra time to walk to the elevators. Visitors should use the temporary reception desk in the parking-lot building. Deliveries will be accepted at the service entrance on Oak Street between 9 and 11 each morning. The construction crew will remove the safety barriers after an inspection on Wednesday evening. The east entrance is expected to reopen Thursday.",
          [
              question(1, "Why will the east entrance close?", ["The elevators are being replaced.", "The front steps are being replaced.", "The parking lot is being expanded.", "The reception desk is moving permanently."], 1, "Workers will replace the front steps.", "replace the front steps"),
              question(2, "Where should visitors go?", ["The east entrance", "The west elevators", "The temporary reception desk", "Oak Street after noon"], 2, "Visitors should use the temporary reception desk in the parking-lot building.", "temporary reception desk"),
              question(3, "When is the east entrance expected to reopen?", ["Monday", "Wednesday morning", "Thursday", "Next month"], 2, "The entrance is expected to reopen Thursday.", "expected to reopen Thursday"),
          ]),
    group("p4", 27, 3, "A city park closure",
          "The rose garden in Central Park will be closed for maintenance from Tuesday morning until Friday afternoon. Workers will replace several damaged paths and install new signs. The playground and tennis courts will remain open, but visitors should use the north gate because the south gate is part of the work area. A free gardening demonstration planned for Wednesday has been moved to the community greenhouse on Lake Road. The park office will publish a new map online this evening. Visitors may still walk around the lake as usual.",
          [
              question(1, "What will workers do in the rose garden?", ["Replace paths and signs", "Build a tennis court", "Close the playground", "Move the park office"], 0, "Workers will replace damaged paths and install new signs.", "replace several damaged paths and install new signs"),
              question(2, "Which gate should visitors use?", ["The south gate", "The north gate", "The greenhouse gate", "The lake gate"], 1, "Visitors should use the north gate.", "use the north gate"),
              question(3, "Where will the gardening demonstration take place?", ["In the rose garden", "At the community greenhouse", "Beside the tennis courts", "At the park office"], 1, "The demonstration has moved to the community greenhouse.", "moved to the community greenhouse"),
          ]),
    group("p4", 28, 3, "A conference keynote",
          "The keynote address at the Innovation Forum will begin at 9:15 in the Grand Ballroom. Attendees should enter through the north doors because the south doors will be used for equipment delivery. The speaker will discuss small-business technology and will answer questions for fifteen minutes afterward. Headsets for simultaneous translation are available at the information table. Please return headsets there after the session. The next session begins at 11:00 in Room 3, and signs will direct attendees from the ballroom.",
          [
              question(1, "Where will the keynote address take place?", ["In Room 3", "At the information table", "In the Grand Ballroom", "At the south doors"], 2, "The keynote will take place in the Grand Ballroom.", "begin at 9:15 in the Grand Ballroom"),
              question(2, "Why should attendees use the north doors?", ["The south doors need repairs.", "The south doors are for equipment delivery.", "The north doors lead to Room 3.", "The speaker will enter there."], 1, "The south doors will be used for equipment delivery.", "south doors will be used for equipment delivery"),
              question(3, "Where should translation headsets be returned?", ["To Room 3", "To the north doors", "To the information table", "To the ballroom stage"], 2, "Attendees should return them to the information table.", "return headsets there"),
          ]),
    group("p4", 29, 3, "A utility bill reminder",
          "Customers who receive paper utility bills can now sign up for automatic payment through the City Utilities website. The service will withdraw the amount due on the date printed on each bill. Customers must enter their account number and bank information, and enrollment takes up to two business days. Those who prefer to pay in person can visit the service center on Pine Street from 8 a.m. to 4 p.m. Monday through Friday. The service center will be closed this Thursday for staff training, but online payments will remain available.",
          [
              question(1, "What new service is available?", ["Automatic bill payment", "A new utility line", "A paper-bill discount", "A staff training course"], 0, "Customers can sign up for automatic payment online.", "sign up for automatic payment"),
              question(2, "How long can enrollment take?", ["One hour", "One business day", "Two business days", "One week"], 2, "Enrollment takes up to two business days.", "takes up to two business days"),
              question(3, "Why will the service center close Thursday?", ["For a public holiday", "For staff training", "For a website update", "For a payment inspection"], 1, "The center will close for staff training.", "closed this Thursday for staff training"),
          ]),
    group("p4", 30, 3, "A theater performance",
          "Tonight's performance of The River Path will begin at 7:30 in the Main Theater. The doors will open at 7:00, and late arrivals may be seated only during a suitable pause. Patrons who purchased tickets online should collect them at the box office using the confirmation number in their email. The theater café will remain open during intermission, but food and drinks cannot be taken into the auditorium. After the performance, the director will speak with the audience for twenty minutes in the lobby. Please keep your ticket if you plan to attend the discussion.",
          [
              question(1, "When will the performance begin?", ["At 6:30", "At 7:00", "At 7:30", "At 8:00"], 2, "The performance will begin at 7:30.", "begin at 7:30"),
              question(2, "Where should online ticket buyers collect tickets?", ["At the theater café", "At the lobby discussion", "At the box office", "Inside the auditorium"], 2, "They should collect tickets at the box office.", "collect them at the box office"),
              question(3, "What will happen after the performance?", ["The café will close permanently.", "The director will speak with the audience.", "Late arrivals will be seated.", "The box office will print new tickets."], 1, "The director will speak with the audience in the lobby.", "director will speak with the audience"),
          ]),
]


def balance_answers(groups):
    """Spread answer positions without changing the keyed answer text."""
    for set_id in ("1", "2", "3"):
        items = [
            question_item
            for item in groups
            if item.get("set") == set_id
            for question_item in item["items"]
        ]
        for index, question_item in enumerate(items):
            target = index % 4
            current = question_item["ans"]
            if current != target:
                question_item["opts"][current], question_item["opts"][target] = (
                    question_item["opts"][target],
                    question_item["opts"][current],
                )
                question_item["ans"] = target


def materialize(section, new_groups):
    path = ROOT / "content" / "en" / "exams" / f"toeic_{section}.json"
    current = json.loads(path.read_text(encoding="utf-8"))
    existing = current["groups"]
    expected_existing = 13 if section == "p3" else 10
    expected_total = 39 if section == "p3" else 30
    if len(existing) == expected_total:
        existing = existing[:expected_existing]
    elif len(existing) != expected_existing:
        raise ValueError(f"{path} must contain the existing complete Practice 1 before expansion")
    for item in existing:
        item["set"] = "1"
    all_groups = existing + new_groups
    seen = set()
    for item in all_groups:
        if item["id"] in seen:
            raise ValueError(f"duplicate group id {item['id']}")
        seen.add(item["id"])
        if len(item["items"]) != 3:
            raise ValueError(f"{item['id']} must contain three questions")
        for question_item in item["items"]:
            question_item["section"] = section
            question_item["qtype"] = f"listen_{section}"
            if len(question_item.get("opts", [])) != 4:
                raise ValueError(f"{question_item['id']} must contain four options")
            if question_item["ans"] not in range(4):
                raise ValueError(f"{question_item['id']} has an invalid answer")
            if not question_item.get("evidence") or question_item["evidence"] not in item["passage"]:
                raise ValueError(f"{question_item['id']} has no evidence in its passage")
    balance_answers(all_groups)
    if len(all_groups) != expected_total:
        raise ValueError(f"wrong {section} group count")
    output = {"items": [], "groups": all_groups}
    with path.open("w", encoding="utf-8", newline="") as handle:
        json.dump(output, handle, ensure_ascii=False, indent=2)
        handle.write("\n")
    return output


def main():
    p3 = materialize("p3", P3_NEW)
    p4 = materialize("p4", P4_NEW)
    print(f"p3_groups={len(p3['groups'])} p3_questions={sum(len(x['items']) for x in p3['groups'])}")
    print(f"p4_groups={len(p4['groups'])} p4_questions={sum(len(x['items']) for x in p4['groups'])}")


if __name__ == "__main__":
    main()
