# Task 1A.1 — owner fix of batches 1–2

Date: 2026-10-05. Done by the owner (with Claude), replacing the rejected batch-2 output.

## What changed

- Rewrote `tools/authoring/senses_editor_batch1.tsv` and `senses_editor_batch2.tsv` (370 senses, NGSL ranks 1–400 present in the core) in UTF-8: part of speech, learner definition, Vietnamese gloss, one real example and its full Vietnamese translation.
- Removed all template examples ("The X is important in daily life.") and filler translations ("Câu này minh họa…"); restored every broken Vietnamese gloss (`ti?n` → `tiền`).
- Fixed sense 1 / part of speech where the old entry pointed at a rare meaning (e.g. `will` modal, `might` modal, `party` = celebration, `level` = amount/stage, `form` = type, `the`/`a` articles, `feel`/`leave`/`turn`/`mean` verbs).
- `tools/gen_content.py`: loads every `senses_editor_batch<N>.tsv`; applies `pos`; a curated sense keeps only the editor example; highlights the real word form (`won`, `studies`).
- `tools/audit_content.py`: `prepare()` shared with `gen_content.py` so stored tiers match the audit; filler rule narrowed to real boilerplate.
- New `tools/check_editor_batch.py` to check a batch file before generation.

## Verification

- `check_editor_batch.py` on batches 1–2: `problems=0`.
- Audit: 0 errors on the 370 curated senses; 0 `*_encoding_broken`, 0 `example_template`/`example_translation_filler` on curated senses, 0 `editor_label_on_wordnet_text`.
- Level 1 lesson-ready (silver) words: 368 (was 296 after the batch-2 rejection).
- `validate_content.py` errors=0; `gradlew check assembleDebug` passed.

## Senses

| sense | pos | def | vi gloss | example | example (vi) |
|---|---|---|---|---|---|
| the_s1 | article | used before a specific person, thing, or group | cái, này, đó (đã xác định) | Please close the door behind you. | Làm ơn đóng cửa lại sau lưng bạn. |
| be_s1 | v | to exist or have a particular quality or state | là, thì, ở | My sister wants to be a teacher. | Em gái tôi muốn làm giáo viên. |
| and_s1 | conj | used to join words, phrases, or clauses | và | I bought bread and milk. | Tôi đã mua bánh mì và sữa. |
| of_s1 | prep | used to show belonging, connection, or description | của | The roof of the house is red. | Mái của ngôi nhà màu đỏ. |
| to_s1 | prep | used to show direction, purpose, or an action | đến, tới, để | I walk to school every morning. | Sáng nào tôi cũng đi bộ đến trường. |
| a_s1 | article | used before one non-specific person or thing | một | A bird is sitting in the tree. | Một con chim đang đậu trên cây. |
| in_s1 | prep | inside or toward the inside of a place | trong, ở trong | The keys are in my bag. | Chìa khóa ở trong túi của tôi. |
| have_s1 | v | to own, hold, or experience something | có, sở hữu | They have a small garden behind the house. | Họ có một khu vườn nhỏ phía sau nhà. |
| it_s1 | pron | a thing, animal, or situation already mentioned | nó, điều đó | I lost my phone, but I found it later. | Tôi bị mất điện thoại, nhưng sau đó đã tìm thấy nó. |
| you_s1 | pron | the person or people being spoken to | bạn, các bạn | Can you help me with this bag? | Bạn giúp tôi cái túi này được không? |
| for_s1 | prep | used to show purpose, benefit, or a period | cho, để, trong (khoảng) | We waited for twenty minutes. | Chúng tôi đã đợi trong hai mươi phút. |
| they_s1 | pron | two or more people or things | họ, chúng | They study together after class. | Họ cùng nhau học sau giờ học. |
| not_s1 | adv | used to make a word or statement negative | không | I am not hungry right now. | Bây giờ tôi không đói. |
| that_s1 | det | used to point to a specific person or thing | đó, kia | Who is that man by the door? | Người đàn ông ở cạnh cửa kia là ai? |
| on_s1 | prep | touching or supported by a surface | trên, ở trên | There is a book on the table. | Có một quyển sách ở trên bàn. |
| with_s1 | prep | together with someone or something; using something | với, cùng với | I live with my parents. | Tôi sống cùng với bố mẹ. |
| this_s1 | det | used to point to a person or thing nearby | này, cái này | This chair is very comfortable. | Cái ghế này rất thoải mái. |
| i_s1 | pron | the person who is speaking or writing | tôi | I usually get up at six. | Tôi thường dậy lúc sáu giờ. |
| do_s1 | v | to perform an action or activity | làm, thực hiện | I do my homework after dinner. | Tôi làm bài tập về nhà sau bữa tối. |
| at_s1 | prep | in or near a particular place or time | ở, tại, lúc | Let's meet at the bus station. | Chúng ta gặp nhau ở bến xe buýt nhé. |
| she_s1 | pron | a female person or animal already mentioned | cô ấy, bà ấy, chị ấy | She works at a hospital near here. | Cô ấy làm việc ở một bệnh viện gần đây. |
| but_s1 | conj | used to connect ideas that are different | nhưng | I like this shirt, but it's too small. | Tôi thích cái áo này, nhưng nó chật quá. |
| from_s1 | prep | starting at a place, time, or source | từ | The train from London arrives at noon. | Chuyến tàu từ London đến vào buổi trưa. |
| by_s1 | prep | near something, or not later than a time | gần, cạnh, trước (thời hạn) | Please finish the report by Friday. | Vui lòng hoàn thành báo cáo trước thứ Sáu. |
| will_s1 | v | used to talk about a future action | sẽ | It will rain tomorrow afternoon. | Chiều mai trời sẽ mưa. |
| or_s1 | conj | used to show a choice or alternative | hoặc, hay | Would you like tea or coffee? | Bạn muốn uống trà hay cà phê? |
| say_s1 | v | to express a thought or fact in words | nói | Please say your name slowly. | Bạn hãy nói tên mình thật chậm. |
| go_s1 | v | to move or travel to another place | đi | We go to school by bus. | Chúng tôi đi học bằng xe buýt. |
| so_s1 | conj | for that reason; therefore | vì vậy, nên | It was raining, so we stayed inside. | Trời mưa nên chúng tôi ở trong nhà. |
| all_s1 | det | the whole amount or every person or thing | tất cả, toàn bộ | All students have a notebook. | Tất cả học sinh đều có một quyển vở. |
| if_s1 | conj | used to talk about a possible condition | nếu | Call me if you need help. | Hãy gọi cho tôi nếu bạn cần giúp đỡ. |
| one_s1 | n | the number 1; a single person or thing | một, số một | Write the number one in the first box. | Hãy viết số một vào ô đầu tiên. |
| can_s1 | v | to be able to do something | có thể | I can help you carry that bag. | Tôi có thể giúp bạn xách cái túi đó. |
| there_s1 | adv | in, at, or to that place | ở đó, đằng kia | Can you put the box over there? | Bạn có thể đặt cái hộp ở đằng kia không? |
| know_s1 | v | to have information or understanding about something | biết | Do you know the answer to this question? | Bạn có biết câu trả lời cho câu hỏi này không? |
| more_s1 | det | a greater amount, number, or degree | nhiều hơn, thêm | Can I have more rice, please? | Cho tôi thêm cơm được không? |
| get_s1 | v | to receive, obtain, or become something | nhận, lấy, trở nên | I get a message from my sister every morning. | Sáng nào tôi cũng nhận được tin nhắn từ chị gái. |
| like_s1 | v | to enjoy or prefer something | thích | I like reading before bed. | Tôi thích đọc sách trước khi đi ngủ. |
| when_s1 | conj | at or during the time that | khi | Text me when you get home. | Hãy nhắn tin cho tôi khi bạn về đến nhà. |
| think_s1 | v | to have an idea or opinion | nghĩ, cho rằng | I think this bag is yours. | Tôi nghĩ cái túi này là của bạn. |
| make_s1 | v | to create, produce, or cause something | làm, tạo ra | We make breakfast together on Sundays. | Chủ nhật nào chúng tôi cũng cùng nhau làm bữa sáng. |
| time_s1 | n | a period or moment when something happens | thời gian, lúc | I don't have time to cook today. | Hôm nay tôi không có thời gian nấu ăn. |
| see_s1 | v | to notice someone or something with your eyes | nhìn thấy | Can you see the bridge from here? | Từ đây bạn có nhìn thấy cây cầu không? |
| what_s1 | pron | used to ask about a thing or fact | gì, cái gì | What is your favorite color? | Màu yêu thích của bạn là gì? |
| up_s1 | adv | toward a higher place or position | lên, ở trên | We walked up the hill slowly. | Chúng tôi chậm rãi đi bộ lên đồi. |
| some_s1 | det | an amount or number that is not exact | một vài, một ít | I bought some apples at the market. | Tôi đã mua một ít táo ở chợ. |
| other_s1 | adj | different from the person or thing mentioned | khác | The other students are already in class. | Những học sinh khác đã ở trong lớp rồi. |
| out_s1 | adv | away from a place or inside space | ra ngoài, ra khỏi | The cat ran out of the kitchen. | Con mèo chạy ra khỏi bếp. |
| good_s1 | adj | having positive qualities or being satisfactory | tốt, hay | That is a really good question. | Đó là một câu hỏi rất hay. |
| people_s1 | n | human beings considered as a group | người, mọi người | Many people visit this park on weekends. | Nhiều người đến công viên này vào cuối tuần. |
| year_s1 | n | a period of twelve months | năm | They moved to this city last year. | Năm ngoái họ đã chuyển đến thành phố này. |
| take_s1 | v | to carry or move something with you | lấy, mang theo | Take an umbrella when you go out. | Hãy mang theo ô khi bạn ra ngoài. |
| well_s1 | adv | in a good, proper, or satisfactory way | tốt, giỏi | She speaks English very well. | Cô ấy nói tiếng Anh rất giỏi. |
| because_s1 | conj | for the reason that | bởi vì | I stayed home because I was tired. | Tôi ở nhà vì tôi mệt. |
| very_s1 | adv | to a high degree; extremely | rất | The soup is very hot. | Món súp rất nóng. |
| just_s1 | adv | a very short time ago; only | vừa mới, chỉ | I have just finished my lunch. | Tôi vừa mới ăn xong bữa trưa. |
| come_s1 | v | to move toward or arrive at a place | đến | Can you come to my house after class? | Bạn có thể đến nhà tôi sau giờ học không? |
| work_s1 | n | an activity done to achieve a result | công việc | I have a lot of work to do today. | Hôm nay tôi có rất nhiều việc phải làm. |
| use_s1 | v | to do something with a thing for a purpose | dùng, sử dụng | You can use my phone to call home. | Bạn có thể dùng điện thoại của tôi để gọi về nhà. |
| than_s1 | conj | used to compare two people or things | hơn, so với | This bag is lighter than that one. | Cái túi này nhẹ hơn cái kia. |
| now_s1 | adv | at the present time | bây giờ, hiện nay | I'm busy now, so call me later. | Bây giờ tôi đang bận, lát nữa gọi lại cho tôi nhé. |
| then_s1 | adv | at that time or after that | lúc đó, sau đó | Read the text, then answer the questions. | Hãy đọc đoạn văn, sau đó trả lời các câu hỏi. |
| also_s1 | adv | in addition; too | cũng, ngoài ra | She speaks English and also studies Korean. | Cô ấy nói tiếng Anh và cũng học tiếng Hàn. |
| into_s1 | prep | to the inside of a place or thing | vào, vào trong | She walked into the room quietly. | Cô ấy lặng lẽ bước vào phòng. |
| only_s1 | adv | no more than; just | chỉ | I only have five dollars left. | Tôi chỉ còn lại năm đô la. |
| look_s1 | v | to direct your eyes toward something | nhìn | Look at the picture on page ten. | Hãy nhìn bức tranh ở trang mười. |
| want_s1 | v | to desire or feel a need for something | muốn | I want a glass of water. | Tôi muốn một cốc nước. |
| give_s1 | v | to provide something to someone | cho, đưa | Please give this book to Anna. | Vui lòng đưa quyển sách này cho Anna. |
| first_s1 | adj | coming before all others in time or order | đầu tiên, thứ nhất | This is my first visit to London. | Đây là lần đầu tiên tôi đến London. |
| new_s1 | adj | recently made, found, or experienced | mới | She bought a new pair of shoes. | Cô ấy đã mua một đôi giày mới. |
| way_s1 | n | a method, path, or direction | cách, đường đi | What is the best way to learn English? | Cách tốt nhất để học tiếng Anh là gì? |
| find_s1 | v | to discover or locate someone or something | tìm thấy | I can't find my notebook. | Tôi không tìm thấy quyển vở của mình. |
| over_s1 | prep | above or across something | phía trên, qua | A lamp hangs over the kitchen table. | Một chiếc đèn treo phía trên bàn bếp. |
| any_s1 | det | one or some, without a specific choice | nào, bất kỳ | Do you have any questions? | Bạn có câu hỏi nào không? |
| after_s1 | prep | later than a particular time or event | sau, sau khi | We went for a walk after breakfast. | Chúng tôi đã đi dạo sau bữa sáng. |
| day_s1 | n | a period of twenty-four hours | ngày | I read a little English every day. | Ngày nào tôi cũng đọc một chút tiếng Anh. |
| where_s1 | adv | in or at what place | ở đâu | Where do you live now? | Bây giờ bạn sống ở đâu? |
| thing_s1 | n | an object, idea, event, or matter | thứ, điều, việc | There is one more thing to buy. | Còn một thứ nữa cần mua. |
| should_s1 | v | used to give advice or show duty | nên | You should drink more water every day. | Bạn nên uống nhiều nước hơn mỗi ngày. |
| need_s1 | v | to require something because it is necessary | cần | You need a pen for this exercise. | Bạn cần một cây bút cho bài tập này. |
| much_s1 | det | a large amount or degree | nhiều | I don't have much money with me. | Tôi không mang theo nhiều tiền. |
| right_s1 | adj | correct, suitable, or true | đúng, chính xác | Check that you have the right address. | Hãy kiểm tra xem bạn có đúng địa chỉ không. |
| how_s1 | adv | in what way or manner | như thế nào, bằng cách nào | How do you get to school? | Bạn đến trường bằng cách nào? |
| back_s1 | n | the rear part of a body or thing | lưng, phía sau | Sitting too long is bad for your back. | Ngồi quá lâu không tốt cho lưng của bạn. |
| mean_s1 | v | to have a particular meaning or intention | có nghĩa là | What does this word mean? | Từ này có nghĩa là gì? |
| even_s1 | adv | used to emphasize something surprising or unexpected | ngay cả, thậm chí | Even my little brother can swim. | Ngay cả em trai nhỏ của tôi cũng biết bơi. |
| may_s1 | v | used to show possibility or permission | có thể, được phép | May I open the window? | Tôi mở cửa sổ được không? |
| here_s1 | adv | in or at this place | ở đây | Please wait here for a moment. | Vui lòng đợi ở đây một lát. |
| many_s1 | det | a large but not exact number | nhiều | Many people work from home now. | Bây giờ nhiều người làm việc tại nhà. |
| such_s1 | det | of the kind or degree mentioned | như vậy, như thế | I have never seen such a big dog. | Tôi chưa bao giờ thấy con chó nào to như vậy. |
| last_s1 | adj | coming after all others in time or order | cuối cùng | December is the last month of the year. | Tháng Mười Hai là tháng cuối cùng của năm. |
| child_s1 | n | a young person who is not an adult | trẻ em, đứa trẻ | The child is playing outside. | Đứa trẻ đang chơi ở bên ngoài. |
| tell_s1 | v | to give information or instructions in words | nói cho biết, kể | Tell me about your first day at school. | Hãy kể cho tôi nghe về ngày đầu tiên đi học của bạn. |
| really_s1 | adv | in fact or to a high degree | thực sự, rất | I really enjoyed the concert. | Tôi thực sự rất thích buổi hòa nhạc. |
| call_s1 | v | to contact someone by phone or voice | gọi, gọi điện | I'll call you after lunch. | Tôi sẽ gọi cho bạn sau bữa trưa. |
| before_s1 | prep | earlier than a particular time or event | trước, trước khi | Wash your hands before dinner. | Hãy rửa tay trước bữa tối. |
| company_s1 | n | a business organization | công ty | Our company opened a new office. | Công ty chúng tôi đã mở một văn phòng mới. |
| through_s1 | prep | from one side or end to another | qua, xuyên qua | The train goes through a long tunnel. | Đoàn tàu chạy xuyên qua một đường hầm dài. |
| down_s1 | adv | toward a lower place or position | xuống | Be careful when you walk down the stairs. | Hãy cẩn thận khi bạn đi xuống cầu thang. |
| show_s1 | v | to let someone see or understand something | cho xem, chỉ cho | Please show me the next page. | Vui lòng cho tôi xem trang tiếp theo. |
| life_s1 | n | the state of being alive; a person's existence | cuộc sống, cuộc đời | My grandmother had a long and happy life. | Bà tôi đã có một cuộc đời dài và hạnh phúc. |
| man_s1 | n | an adult male person | người đàn ông | A man is waiting at the door. | Một người đàn ông đang đợi ở cửa. |
| change_s1 | v | to make someone or something different | thay đổi | We need to change our travel plans. | Chúng tôi cần thay đổi kế hoạch du lịch. |
| place_s2 | n | a particular area or position | nơi, chỗ | This is a quiet place to study. | Đây là một nơi yên tĩnh để học bài. |
| long_s1 | adj | continuing for a large amount of time or distance | dài, lâu | It was a long journey home. | Đó là một chuyến đi dài về nhà. |
| between_s1 | prep | in the space separating two people or things | ở giữa | The pharmacy is between the bank and the school. | Hiệu thuốc nằm giữa ngân hàng và trường học. |
| feel_s1 | v | to experience an emotion or physical sensation | cảm thấy | I feel happy after a long walk. | Tôi cảm thấy vui sau một cuộc đi bộ dài. |
| too_s1 | adv | more than is needed or wanted | quá | This coffee is too hot to drink. | Cà phê này nóng quá, không uống được. |
| still_s1 | adv | continuing until a particular time | vẫn, vẫn còn | She is still waiting for the bus. | Cô ấy vẫn đang đợi xe buýt. |
| problem_s1 | n | a difficulty that needs an answer or solution | vấn đề | We need to discuss this problem together. | Chúng ta cần cùng nhau thảo luận vấn đề này. |
| write_s1 | v | to form words with a pen or keyboard | viết | Write your address on the envelope. | Hãy viết địa chỉ của bạn lên phong bì. |
| same_s1 | adj | not different; exactly alike | giống nhau, cùng một | We take the same bus to school. | Chúng tôi đi cùng một chuyến xe buýt đến trường. |
| lot_s1 | n | a large amount or number | nhiều, rất nhiều | We bought a lot of fruit. | Chúng tôi đã mua rất nhiều trái cây. |
| great_s1 | adj | very good; or very large | tuyệt vời, lớn | We had a great time at the beach. | Chúng tôi đã có khoảng thời gian tuyệt vời ở bãi biển. |
| try_s1 | v | to make an effort to do something | thử, cố gắng | Try to read the sentence without a dictionary. | Hãy thử đọc câu này mà không dùng từ điển. |
| leave_s1 | v | to go away from a place or person | rời đi, rời khỏi | I leave home at seven each morning. | Mỗi sáng tôi rời nhà lúc bảy giờ. |
| number_s1 | n | a symbol or amount used for counting | số | Please write your phone number here. | Vui lòng viết số điện thoại của bạn vào đây. |
| both_s1 | pron | the two people or things together | cả hai | Both of my sisters speak English. | Cả hai chị gái của tôi đều nói tiếng Anh. |
| own_s1 | v | to have something as your property | sở hữu | They own two houses by the sea. | Họ sở hữu hai ngôi nhà gần biển. |
| part_s1 | n | a piece or section of something | phần | This is my favorite part of the film. | Đây là phần tôi thích nhất trong bộ phim. |
| point_s1 | v | to show something with your finger | chỉ, chỉ tay | The boy pointed at the big red bus. | Cậu bé chỉ tay vào chiếc xe buýt to màu đỏ. |
| little_s1 | adj | small in amount, size, or degree | nhỏ, ít | We live in a little house near the park. | Chúng tôi sống trong một ngôi nhà nhỏ gần công viên. |
| help_s1 | v | to make something easier for someone | giúp, giúp đỡ | Can you help me carry this box? | Bạn có thể giúp tôi bê cái hộp này không? |
| ask_s1 | v | to request information or something from someone | hỏi, yêu cầu | Ask your teacher if you need help. | Hãy hỏi giáo viên nếu bạn cần giúp đỡ. |
| meet_s1 | v | to come together with someone | gặp, gặp gỡ | Let's meet outside the library. | Chúng ta gặp nhau bên ngoài thư viện nhé. |
| start_s1 | v | to begin doing something | bắt đầu | Our English class starts at six. | Lớp tiếng Anh của chúng tôi bắt đầu lúc sáu giờ. |
| talk_s1 | v | to speak with someone about something | nói chuyện, trò chuyện | We talk about our day over dinner. | Chúng tôi kể cho nhau nghe về một ngày của mình trong bữa tối. |
| something_s1 | pron | an unspecified thing or matter | cái gì đó, điều gì đó | I want something cold to drink. | Tôi muốn uống thứ gì đó lạnh. |
| put_s1 | v | to move something to a particular place | đặt, để | Put your bag under the chair, please. | Bạn hãy để túi xuống dưới ghế nhé. |
| another_s1 | det | one more person or thing of the same kind | khác, thêm một | Can I have another cup of tea? | Cho tôi thêm một tách trà nữa được không? |
| become_s1 | v | to begin to be something | trở nên, trở thành | She wants to become a doctor one day. | Cô ấy muốn một ngày nào đó trở thành bác sĩ. |
| interest_s1 | n | a feeling of wanting to know or learn more | sự quan tâm, hứng thú | He has a strong interest in music. | Anh ấy rất quan tâm đến âm nhạc. |
| country_s1 | n | a nation with its own government | đất nước, quốc gia | Canada is a very large country. | Canada là một đất nước rất rộng lớn. |
| old_s1 | adj | having existed or lived for a long time | cũ, già | This old photo shows our first house. | Bức ảnh cũ này chụp ngôi nhà đầu tiên của chúng tôi. |
| each_s1 | det | every one considered separately | mỗi | Each student gets a free book. | Mỗi học sinh được nhận một quyển sách miễn phí. |
| school_s1 | n | a place where people learn | trường học | My school is close to the park. | Trường của tôi ở gần công viên. |
| late_s1 | adv | after the expected or usual time | muộn, trễ | He arrived late because he missed the bus. | Anh ấy đến muộn vì bị lỡ xe buýt. |
| high_s1 | adj | far above the ground or usual level | cao | The shelf is too high for me. | Cái kệ quá cao đối với tôi. |
| different_s1 | adj | not the same as another person or thing | khác, khác nhau | These two words have different meanings. | Hai từ này có nghĩa khác nhau. |
| next_s1 | adj | coming immediately after in time or order | tiếp theo, kế tiếp | We will meet again next week. | Tuần sau chúng ta sẽ gặp lại nhau. |
| live_s2 | v | to have your home somewhere | sống, ở | I live in a small apartment. | Tôi sống trong một căn hộ nhỏ. |
| why_s1 | adv | for what reason | tại sao, vì sao | Why are you late today? | Sao hôm nay bạn đến muộn? |
| while_s1 | conj | during the time that something happens | trong khi | I listened to music while I cooked. | Tôi nghe nhạc trong khi nấu ăn. |
| world_s1 | n | the earth and all its people and places | thế giới | She wants to travel around the world. | Cô ấy muốn đi du lịch vòng quanh thế giới. |
| week_s1 | n | a period of seven days | tuần | The library is closed this week. | Tuần này thư viện đóng cửa. |
| might_s1 | v | used to show possibility | có thể, có lẽ | Take a coat; it might rain later. | Hãy mang theo áo khoác; lát nữa trời có thể mưa. |
| must_s1 | v | used to show necessity or strong certainty | phải | You must wear a seat belt. | Bạn phải thắt dây an toàn. |
| home_s1 | n | the place where a person lives | nhà, nơi ở | My home is near the river. | Nhà tôi ở gần con sông. |
| never_s1 | adv | not at any time | không bao giờ | I never leave home without my keys. | Tôi không bao giờ ra khỏi nhà mà không mang chìa khóa. |
| course_s1 | n | a series of lessons about a subject | khóa học | I'm taking an online English course. | Tôi đang học một khóa tiếng Anh trực tuyến. |
| house_s1 | n | a building where people live | ngôi nhà | Their house has a red door. | Ngôi nhà của họ có cửa màu đỏ. |
| report_s1 | n | a written account of facts or findings | báo cáo | The report was published yesterday. | Bản báo cáo đã được công bố hôm qua. |
| group_s1 | n | a number of people or things together | nhóm | A small group of students waited outside. | Một nhóm nhỏ học sinh đã đợi ở bên ngoài. |
| case_s1 | n | an example or situation of a particular kind | trường hợp | In that case, we will meet tomorrow. | Trong trường hợp đó, chúng ta sẽ gặp nhau vào ngày mai. |
| woman_s1 | n | an adult female person | người phụ nữ | The woman at the desk can help you. | Người phụ nữ ở bàn kia có thể giúp bạn. |
| around_s1 | prep | in a circle or surrounding an area | xung quanh, vòng quanh | We walked around the lake after lunch. | Chúng tôi đi dạo quanh hồ sau bữa trưa. |
| book_s2 | n | a set of printed pages you read | quyển sách | I read a book before bed. | Tôi đọc sách trước khi đi ngủ. |
| family_s1 | n | a group of related people | gia đình | My family eats dinner together every night. | Tối nào gia đình tôi cũng ăn tối cùng nhau. |
| seem_s1 | v | to give a particular impression | có vẻ, dường như | You seem very tired this morning. | Sáng nay trông bạn có vẻ rất mệt. |
| let_s1 | v | to allow something to happen | để cho, cho phép | My parents let me stay up late. | Bố mẹ cho phép tôi thức khuya. |
| again_s1 | adv | one more time | lại, lần nữa | Can you say that again, please? | Bạn có thể nói lại điều đó được không? |
| kind_s2 | n | a type or sort of person or thing | loại, kiểu | What kind of music do you like? | Bạn thích loại nhạc nào? |
| keep_s1 | v | to continue to have or hold something | giữ, giữ lại | Keep your ticket until the end of the journey. | Hãy giữ vé cho đến khi kết thúc chuyến đi. |
| hear_s1 | v | to notice a sound with your ears | nghe thấy | I can hear birds outside my window. | Tôi có thể nghe thấy tiếng chim bên ngoài cửa sổ. |
| system_s1 | n | a set of connected parts working together | hệ thống | The school has a new computer system. | Trường học có một hệ thống máy tính mới. |
| question_s1 | n | a sentence used to ask for information | câu hỏi | Do you have a question about the lesson? | Bạn có câu hỏi nào về bài học không? |
| during_s1 | prep | throughout a period of time | trong suốt, trong lúc | Please turn off your phone during the lesson. | Vui lòng tắt điện thoại trong suốt giờ học. |
| always_s1 | adv | at every time or on every occasion | luôn luôn | She always checks her answers. | Cô ấy luôn kiểm tra lại câu trả lời của mình. |
| big_s1 | adj | large in size, amount, or importance | to, lớn | They live in a big house near the beach. | Họ sống trong một ngôi nhà lớn gần bãi biển. |
| set_s1 | n | a group of things that belong together | bộ | I bought a new set of pencils. | Tôi đã mua một bộ bút chì mới. |
| small_s1 | adj | limited in size, amount, or degree | nhỏ, bé | I carry a small notebook in my bag. | Tôi mang một quyển sổ nhỏ trong túi. |
| study_s1 | v | to learn about a subject | học, nghiên cứu | She studies English at the library. | Cô ấy học tiếng Anh ở thư viện. |
| follow_s1 | v | to go or come after someone | đi theo | Please follow me to the meeting room. | Vui lòng đi theo tôi đến phòng họp. |
| important_s1 | adj | having great value or significance | quan trọng | It's important to check your work. | Kiểm tra lại bài làm là điều quan trọng. |
| since_s1 | prep | from a past time until now | từ, kể từ | I've lived here since 2020. | Tôi đã sống ở đây từ năm 2020. |
| run_s1 | v | to move quickly on your feet | chạy | The children run across the playground. | Bọn trẻ chạy băng qua sân chơi. |
| under_s1 | prep | below or lower than something | dưới, ở dưới | The cat is sleeping under the bed. | Con mèo đang ngủ dưới gầm giường. |
| turn_s1 | v | to move around a central point | quay, xoay | Turn the handle slowly to open it. | Xoay tay nắm từ từ để mở ra. |
| few_s1 | det | a small number of people or things | vài, ít | Only a few people came to the meeting. | Chỉ có vài người đến dự cuộc họp. |
| bring_s1 | v | to carry something to a person or place | mang đến, mang theo | Please bring your notebook to class. | Vui lòng mang vở đến lớp. |
| early_s1 | adv | before the usual or expected time | sớm | We arrived early and found good seats. | Chúng tôi đến sớm và tìm được chỗ ngồi tốt. |
| hand_s1 | n | the body part at the end of an arm | bàn tay | Raise your hand if you know the answer. | Hãy giơ tay nếu bạn biết câu trả lời. |
| state_s1 | n | the condition someone or something is in | trạng thái, tình trạng | The old house is in a bad state. | Ngôi nhà cũ đang ở trong tình trạng tồi tệ. |
| move_s1 | v | to change position or place | di chuyển, chuyển (nhà) | We moved to a new city in spring. | Mùa xuân chúng tôi đã chuyển đến một thành phố mới. |
| money_s1 | n | what people use to buy goods and services | tiền | I don't have enough money for a taxi. | Tôi không có đủ tiền để đi taxi. |
| fact_s1 | n | something known to be true | sự thật, sự việc | It is a fact that water boils at 100 degrees. | Nước sôi ở 100 độ là một sự thật. |
| however_s1 | adv | used to introduce a contrast | tuy nhiên | The room is small. However, it has a lovely view. | Căn phòng nhỏ. Tuy nhiên, nó có tầm nhìn rất đẹp. |
| area_s1 | n | a part of a place or surface | khu vực, vùng | There are many cafés in this area. | Có nhiều quán cà phê ở khu vực này. |
| provide_s1 | v | to give someone something useful | cung cấp | The school provides books for students. | Nhà trường cung cấp sách cho học sinh. |
| name_s1 | n | a word used to identify a person or thing | tên | What is the name of your dog? | Con chó của bạn tên là gì? |
| read_s1 | v | to understand written words | đọc | I read a short book every night. | Tối nào tôi cũng đọc một quyển sách ngắn. |
| friend_s1 | n | a person you know and like | bạn, bạn bè | My best friend lives next door. | Bạn thân nhất của tôi sống ở nhà bên cạnh. |
| month_s1 | n | one of twelve parts of a year | tháng | My birthday is next month. | Tháng sau là sinh nhật của tôi. |
| large_s1 | adj | big in size or amount | lớn, rộng | We need a large box for these books. | Chúng tôi cần một cái hộp lớn để đựng những quyển sách này. |
| business_s1 | n | an organization that sells goods or services | doanh nghiệp, việc kinh doanh | Her parents run a small business. | Bố mẹ cô ấy điều hành một doanh nghiệp nhỏ. |
| without_s1 | prep | not having or using something | không có | I drink my coffee without sugar. | Tôi uống cà phê không đường. |
| information_s1 | n | facts or details about something | thông tin | The website has useful information about the course. | Trang web có thông tin hữu ích về khóa học. |
| open_s1 | v | to move something so its inside can be seen | mở | Please open the window for fresh air. | Vui lòng mở cửa sổ cho thoáng khí. |
| order_s1 | n | a request for goods or food | đơn hàng, món gọi | We placed an order for lunch. | Chúng tôi đã đặt một đơn đồ ăn trưa. |
| government_s1 | n | the group that runs a country or area | chính phủ | The government plans to build a new hospital. | Chính phủ có kế hoạch xây một bệnh viện mới. |
| word_s1 | n | a unit of language with meaning | từ | This word has two common meanings. | Từ này có hai nghĩa phổ biến. |
| issue_s1 | n | a problem or subject people discuss | vấn đề | The article discusses several important issues. | Bài báo bàn về một số vấn đề quan trọng. |
| market_s1 | n | a place where people buy and sell goods | chợ, thị trường | We buy fresh fish at the market. | Chúng tôi mua cá tươi ở chợ. |
| pay_s1 | v | to give money for something | trả tiền | I pay for my meal at the counter. | Tôi trả tiền bữa ăn ở quầy. |
| build_s1 | v | to make something by putting parts together | xây, xây dựng | They are building a new bridge over the river. | Họ đang xây một cây cầu mới bắc qua sông. |
| hold_s1 | v | to keep something in your hands | cầm, giữ | Please hold my bag for a minute. | Bạn cầm giúp tôi cái túi một lát nhé. |
| service_s1 | n | work done to help a person or group | dịch vụ, sự phục vụ | The service at this hotel is excellent. | Dịch vụ ở khách sạn này rất tuyệt. |
| against_s1 | prep | opposed to someone or something | chống lại, phản đối | Most people voted against the plan. | Hầu hết mọi người đã bỏ phiếu phản đối kế hoạch. |
| believe_s1 | v | to think something is true | tin, tin rằng | I believe you are telling the truth. | Tôi tin rằng bạn đang nói thật. |
| second_s1 | n | a short unit of time; one sixtieth of a minute | giây | Wait a few seconds before opening the door. | Hãy đợi vài giây trước khi mở cửa. |
| though_s1 | conj | used to introduce a contrast | dù, mặc dù | Though it was late, we kept working. | Dù đã muộn, chúng tôi vẫn tiếp tục làm việc. |
| yes_s1 | adv | used to agree or answer positively | vâng, có, ừ | Yes, I would love some tea. | Vâng, tôi rất muốn uống một chút trà. |
| love_s1 | v | to like someone or something very much | yêu, rất thích | My parents love walking in the park. | Bố mẹ tôi rất thích đi dạo trong công viên. |
| increase_s1 | v | to become greater in amount or size | tăng | Prices increased by 3 percent last month. | Giá cả đã tăng 3% vào tháng trước. |
| job_s1 | n | work done to earn money | việc làm, công việc | She found a job at a bookshop. | Cô ấy đã tìm được việc ở một hiệu sách. |
| plan_s1 | n | a set of steps for reaching a goal | kế hoạch | We need a plan for the weekend. | Chúng ta cần một kế hoạch cho cuối tuần. |
| result_s1 | n | something that happens because of an action | kết quả | We will receive the results tomorrow. | Ngày mai chúng tôi sẽ nhận được kết quả. |
| away_s1 | adv | at a distance from a place | xa, cách xa | The beach is two kilometers away from here. | Bãi biển cách đây hai cây số. |
| example_s1 | n | something that shows what a group is like | ví dụ | Can you give me an example? | Bạn có thể cho tôi một ví dụ không? |
| happen_s1 | v | to take place | xảy ra | What happened at school today? | Hôm nay ở trường đã xảy ra chuyện gì? |
| offer_s1 | v | to say that you will give or do something | đề nghị, mời | She offered to drive me home. | Cô ấy đề nghị lái xe đưa tôi về nhà. |
| young_s1 | adj | having lived for a short time | trẻ, nhỏ tuổi | The young musician plays the piano very well. | Nhạc công trẻ chơi piano rất hay. |
| close_s1 | adj | near in space or time | gần, thân thiết | We are very close friends. | Chúng tôi là bạn rất thân. |
| buy_s1 | v | to get something by paying money | mua | I want to buy a new phone. | Tôi muốn mua một chiếc điện thoại mới. |
| understand_s1 | v | to know the meaning of something | hiểu | I understand the words but not the whole sentence. | Tôi hiểu các từ nhưng không hiểu cả câu. |
| thank_s1 | v | to tell someone you are grateful | cảm ơn | I want to thank you for your help. | Tôi muốn cảm ơn bạn vì đã giúp đỡ. |
| far_s1 | adj | a long distance away | xa | Is the station far from here? | Nhà ga có xa đây không? |
| today_s1 | adv | on this day | hôm nay | I have two classes today. | Hôm nay tôi có hai tiết học. |
| hour_s1 | n | a period of sixty minutes | giờ, tiếng | The journey takes one hour. | Chuyến đi mất một tiếng. |
| student_s1 | n | a person who studies at a school | học sinh, sinh viên | She is a student at a large university. | Cô ấy là sinh viên của một trường đại học lớn. |
| face_s1 | n | the front part of a person's head | mặt, khuôn mặt | Wash your face with cold water. | Hãy rửa mặt bằng nước lạnh. |
| hope_s1 | n | a feeling that something good may happen | hy vọng | There is still hope that he will win. | Vẫn còn hy vọng rằng anh ấy sẽ thắng. |
| idea_s1 | n | a thought or plan in your mind | ý tưởng, ý kiến | That is a great idea for a party. | Đó là một ý tưởng tuyệt vời cho bữa tiệc. |
| cost_s1 | n | the amount of money needed for something | chi phí, giá | The cost of living is rising. | Chi phí sinh hoạt đang tăng lên. |
| less_s1 | det | a smaller amount or degree | ít hơn | I want to spend less time on my phone. | Tôi muốn dành ít thời gian hơn cho điện thoại. |
| room_s1 | n | a space inside a building | phòng | There are two windows in this room. | Căn phòng này có hai cửa sổ. |
| until_s1 | prep | up to a particular time | cho đến | The library is open until eight. | Thư viện mở cửa đến tám giờ. |
| reason_s1 | n | a cause or explanation for something | lý do | What was the reason for the delay? | Lý do của sự chậm trễ là gì? |
| form_s1 | n | a type or kind of something | dạng, hình thức | Walking is a good form of exercise. | Đi bộ là một hình thức tập thể dục tốt. |
| spend_s1 | v | to use time or money | dành (thời gian), tiêu (tiền) | I spend an hour reading every evening. | Tối nào tôi cũng dành một tiếng để đọc sách. |
| head_s1 | n | the part of the body above the neck | đầu | He shook his head and smiled. | Anh ấy lắc đầu và mỉm cười. |
| car_s1 | n | a road vehicle with four wheels | xe ô tô, xe hơi | My father drives an old blue car. | Bố tôi lái một chiếc ô tô cũ màu xanh. |
| learn_s1 | v | to gain knowledge or skill | học, học được | We learn five new words each day. | Mỗi ngày chúng tôi học năm từ mới. |
| level_s1 | n | a particular amount or stage of something | mức độ, trình độ | This book is for students at a basic level. | Quyển sách này dành cho học sinh ở trình độ cơ bản. |
| person_s1 | n | a man, woman, or child | người | She is a very kind person. | Cô ấy là một người rất tốt bụng. |
| experience_s1 | n | knowledge gained by doing something | kinh nghiệm, trải nghiệm | Do you have any experience in sales? | Bạn có kinh nghiệm bán hàng không? |
| once_s1 | adv | one time in the past or future | một lần | We visit our cousins once a month. | Chúng tôi đến thăm anh chị em họ mỗi tháng một lần. |
| member_s1 | n | a person belonging to a group | thành viên | Every member of the team was at the meeting. | Mọi thành viên của đội đều có mặt ở cuộc họp. |
| enough_s1 | det | as much as needed | đủ | We have enough chairs for everyone. | Chúng tôi có đủ ghế cho mọi người. |
| bad_s1 | adj | not good or pleasant | xấu, tệ | We stayed home because of the bad weather. | Chúng tôi ở nhà vì thời tiết xấu. |
| night_s1 | n | the dark time between evening and morning | đêm, ban đêm | The street is quiet at night. | Đường phố yên tĩnh vào ban đêm. |
| able_s1 | adj | having the skill or power to do something | có thể, có khả năng | Will you be able to come tomorrow? | Ngày mai bạn có thể đến được không? |
| support_s1 | v | to help someone emotionally or practically | ủng hộ, hỗ trợ | Friends support each other in hard times. | Bạn bè hỗ trợ nhau trong lúc khó khăn. |
| whether_s1 | conj | used to introduce two possible choices | liệu, có ... hay không | I don't know whether the shop is open. | Tôi không biết liệu cửa hàng có mở cửa không. |
| line_s1 | n | a row of people or things | hàng, dòng | We waited in a long line for tickets. | Chúng tôi xếp hàng dài để mua vé. |
| present_s1 | adj | being in a place now | có mặt | All students are present in class today. | Hôm nay tất cả học sinh đều có mặt trong lớp. |
| side_s1 | n | one of two parts beside a center | bên, mặt, phía | Please write on one side of the paper. | Vui lòng chỉ viết trên một mặt của tờ giấy. |
| quite_s1 | adv | to a fairly high degree | khá | The test was quite easy for me. | Bài kiểm tra khá dễ đối với tôi. |
| although_s1 | conj | despite the fact that | mặc dù | Although it was cold, we went for a walk. | Mặc dù trời lạnh, chúng tôi vẫn đi dạo. |
| sure_s1 | adj | certain and not having doubts | chắc chắn | Are you sure the shop is open? | Bạn có chắc là cửa hàng đang mở không? |
| term_s1 | n | a word or phrase for something | thuật ngữ | Do you know the meaning of this term? | Bạn có biết nghĩa của thuật ngữ này không? |
| least_s1 | adj | the smallest amount or degree | ít nhất, kém nhất | This is the least expensive hotel in town. | Đây là khách sạn rẻ nhất trong thị trấn. |
| age_s1 | n | how long someone or something has lived | tuổi | Children start school at the age of six. | Trẻ em bắt đầu đi học khi sáu tuổi. |
| low_s1 | adj | not high or great | thấp | The table is too low for adults. | Cái bàn quá thấp đối với người lớn. |
| speak_s1 | v | to talk using words | nói | Does your brother speak English? | Anh trai bạn có nói tiếng Anh không? |
| within_s1 | prep | inside or not beyond a limit | trong vòng, trong phạm vi | You will get an answer within two days. | Bạn sẽ nhận được câu trả lời trong vòng hai ngày. |
| process_s1 | n | a series of actions for a result | quy trình, quá trình | The hiring process takes about a month. | Quy trình tuyển dụng mất khoảng một tháng. |
| public_s1 | adj | open to or shared by everyone | công cộng | You can't smoke in public places. | Bạn không được hút thuốc ở nơi công cộng. |
| often_s1 | adv | many times or frequently | thường, thường xuyên | We often cook dinner together. | Chúng tôi thường cùng nhau nấu bữa tối. |
| train_s1 | n | a vehicle that travels on a railway | tàu hỏa | The train to the airport leaves at nine. | Chuyến tàu đến sân bay khởi hành lúc chín giờ. |
| possible_s1 | adj | able to happen or exist | có thể, khả thi | Is it possible to change the date? | Có thể đổi ngày được không? |
| actually_s1 | adv | in fact or really | thật ra, thực sự | I thought he was French, but actually he's Swiss. | Tôi tưởng anh ấy là người Pháp, nhưng thật ra anh ấy là người Thụy Sĩ. |
| rather_s1 | adv | to some degree; preferably | đúng hơn, khá | I'm a doctor, or rather, a surgeon. | Tôi là bác sĩ, hay đúng hơn là bác sĩ phẫu thuật. |
| view_s1 | n | an opinion or way of seeing something | quan điểm, ý kiến | In my view, the plan is too expensive. | Theo quan điểm của tôi, kế hoạch này quá tốn kém. |
| together_s1 | adv | with each other or in one place | cùng nhau | Let's walk home together after the game. | Hãy cùng nhau đi bộ về nhà sau trận đấu nhé. |
| consider_s1 | v | to think carefully about something | cân nhắc, xem xét | We are considering a move to the city. | Chúng tôi đang cân nhắc chuyển lên thành phố. |
| price_s1 | n | the amount paid for something | giá | The room price includes a free breakfast. | Giá phòng đã bao gồm bữa sáng miễn phí. |
| parent_s1 | n | a mother or father | cha, mẹ, phụ huynh | Every parent wants the best for their child. | Cha mẹ nào cũng muốn điều tốt nhất cho con mình. |
| hard_s1 | adj | not easy; needing effort | khó, vất vả | The last question was hard. | Câu hỏi cuối cùng rất khó. |
| party_s1 | n | a social event where people celebrate together | bữa tiệc | We are having a party for her birthday. | Chúng tôi sẽ tổ chức một bữa tiệc mừng sinh nhật cô ấy. |
| local_s1 | adj | connected with a particular area | địa phương | We buy bread from a local bakery. | Chúng tôi mua bánh mì ở một tiệm bánh địa phương. |
| control_s1 | v | to direct or manage something | kiểm soát, điều khiển | This small button controls the volume. | Nút nhỏ này điều chỉnh âm lượng. |
| already_s1 | adv | before now or before a stated time | đã, rồi | I've already finished this lesson. | Tôi đã học xong bài này rồi. |
| concern_s1 | n | something that worries or interests someone | mối lo ngại, mối quan tâm | Safety is our main concern. | An toàn là mối quan tâm hàng đầu của chúng tôi. |
| product_s1 | n | something made to be sold | sản phẩm | This shop sells only local products. | Cửa hàng này chỉ bán sản phẩm địa phương. |
| lose_s1 | v | to no longer have something | làm mất, thua | I often lose my keys at home. | Ở nhà tôi hay làm mất chìa khóa. |
| story_s1 | n | a description of events, real or imagined | câu chuyện | My grandfather tells us a story every night. | Tối nào ông tôi cũng kể cho chúng tôi nghe một câu chuyện. |
| continue_s1 | v | to keep happening or doing something | tiếp tục | Please continue reading from page five. | Vui lòng đọc tiếp từ trang năm. |
| stand_s1 | v | to be upright on your feet | đứng | We stand near the door while we wait. | Chúng tôi đứng gần cửa trong lúc chờ. |
| yet_s1 | adv | until now; used in questions and negatives | chưa, vẫn chưa | I haven't read that book yet. | Tôi vẫn chưa đọc quyển sách đó. |
| rate_s1 | n | a measurement of speed, amount, or frequency | tỷ lệ, mức, tốc độ | The bank offers a good interest rate. | Ngân hàng đưa ra mức lãi suất tốt. |
| care_s1 | n | help or attention given to someone or something | sự chăm sóc | Babies need a lot of care. | Em bé cần được chăm sóc nhiều. |
| expect_s1 | v | to think something will happen | mong đợi, dự kiến | We expect the package to arrive tomorrow. | Chúng tôi dự kiến gói hàng sẽ đến vào ngày mai. |
| effect_s1 | n | a change caused by something | tác động, tác dụng | The medicine had no effect on my headache. | Thuốc không có tác dụng gì với cơn đau đầu của tôi. |
| sort_s1 | n | a group or type of similar things | loại, kiểu | This sort of problem is easy to fix. | Loại vấn đề này rất dễ khắc phục. |
| ever_s1 | adv | at any time in your life | từng, bao giờ | Have you ever been to Japan? | Bạn đã bao giờ đến Nhật Bản chưa? |
| anything_s1 | pron | any thing or object | bất cứ thứ gì | Do you need anything from the shop? | Bạn có cần gì ở cửa hàng không? |
| cause_s1 | n | a person or thing that makes something happen | nguyên nhân | Bad weather was the cause of the delay. | Thời tiết xấu là nguyên nhân gây ra sự chậm trễ. |
| fall_s1 | v | to move downward through the air | rơi, ngã | Be careful not to fall on the ice. | Cẩn thận kẻo ngã trên băng. |
| deal_s1 | n | an agreement or act of buying and selling | thỏa thuận, giao dịch | We made a deal with a new partner. | Chúng tôi đã ký thỏa thuận với một đối tác mới. |
| water_s1 | n | the clear liquid people drink and use | nước | There is some water in the bottle. | Có một ít nước trong chai. |
| send_s1 | v | to cause something to go somewhere | gửi | I will send you the photos tonight. | Tối nay tôi sẽ gửi ảnh cho bạn. |
| allow_s1 | v | to let someone do something | cho phép | Dogs are not allowed in this shop. | Không được mang chó vào cửa hàng này. |
| soon_s1 | adv | in a short time | sớm, sắp | Dinner will be ready soon. | Bữa tối sắp xong rồi. |
| watch_s1 | v | to look at something carefully | xem, theo dõi | We watch a film on Friday nights. | Tối thứ Sáu nào chúng tôi cũng xem phim. |
| base_s1 | v | to use something as the main support | dựa trên, dựa vào | The film is based on a true story. | Bộ phim dựa trên một câu chuyện có thật. |
| suggest_s1 | v | to mention an idea for consideration | đề nghị, gợi ý | I suggest we take a short break. | Tôi đề nghị chúng ta nghỉ giải lao một chút. |
| past_s1 | n | the time before the present | quá khứ, trước đây | In the past, this town was very small. | Trước đây, thị trấn này rất nhỏ. |
| power_s1 | n | the ability to control or do something | quyền lực, sức mạnh | The president has a lot of power. | Tổng thống có rất nhiều quyền lực. |
| test_s1 | n | an activity used to check knowledge or ability | bài kiểm tra | We have a short test on Friday. | Chúng tôi có một bài kiểm tra ngắn vào thứ Sáu. |
| visit_s1 | v | to go to a person or place for a time | thăm, đến thăm | We visit our grandparents every summer. | Mùa hè nào chúng tôi cũng về thăm ông bà. |
| grow_s1 | v | to become bigger or develop | lớn lên, phát triển | Children grow very fast at this age. | Trẻ em lớn rất nhanh ở độ tuổi này. |
| nothing_s1 | pron | not anything | không có gì | There is nothing in the fridge. | Trong tủ lạnh không có gì cả. |
| return_s1 | v | to come or go back | trở lại, trở về | She will return from her trip on Monday. | Cô ấy sẽ trở về sau chuyến đi vào thứ Hai. |
| mother_s1 | n | a woman who has a child | mẹ | My mother works at a school. | Mẹ tôi làm việc ở một trường học. |
| walk_s1 | v | to move on foot | đi bộ | I walk to the station every morning. | Sáng nào tôi cũng đi bộ đến nhà ga. |
| matter_s1 | n | a subject or situation that concerns people | vấn đề, chuyện | This is an important matter for the family. | Đây là một vấn đề quan trọng đối với gia đình. |
| mind_s1 | n | the part of you that thinks and feels | tâm trí, trí óc | Reading is good for your mind. | Đọc sách tốt cho trí óc của bạn. |
| value_s1 | n | how useful or important something is | giá trị | The value of the house has gone up. | Giá trị của ngôi nhà đã tăng lên. |
| office_s1 | n | a place where people work | văn phòng | Our office is on the third floor. | Văn phòng của chúng tôi ở tầng ba. |
| record_s1 | n | stored information about an event or person | hồ sơ, bản ghi | Keep a record of all your expenses. | Hãy ghi lại tất cả các khoản chi tiêu của bạn. |
| stay_s1 | v | to remain in a place or state | ở lại, lưu lại | We stayed at a small hotel by the sea. | Chúng tôi ở tại một khách sạn nhỏ gần biển. |
| force_s1 | n | strength or power that causes movement | lực, sức mạnh | The force of the wind broke the window. | Sức gió đã làm vỡ cửa sổ. |
| stop_s1 | v | to end movement or an activity | dừng, dừng lại | The bus stops outside the school. | Xe buýt dừng ở bên ngoài trường học. |
| several_s1 | det | more than two but not many | vài, một số | Several students stayed after class. | Vài học sinh đã ở lại sau giờ học. |
| light_s1 | adj | not heavy | nhẹ | This bag is light enough to carry all day. | Cái túi này đủ nhẹ để mang cả ngày. |
| develop_s1 | v | to grow or make something grow | phát triển | This course helps you develop writing skills. | Khóa học này giúp bạn phát triển kỹ năng viết. |
| remember_s1 | v | to keep or bring something to mind | nhớ | Remember to lock the door before you leave. | Nhớ khóa cửa trước khi ra ngoài. |
| bit_s1 | n | a small piece or amount | một chút, một ít | Can you wait a bit longer? | Bạn có thể đợi thêm một chút không? |
| share_s1 | v | to give part of something to others | chia sẻ | Share this video with your friends. | Hãy chia sẻ video này với bạn bè của bạn. |
| answer_s1 | v | to reply to a question | trả lời | Please answer the question in English. | Vui lòng trả lời câu hỏi bằng tiếng Anh. |
| sit_s1 | v | to rest on a chair or seat | ngồi | Please sit next to me. | Hãy ngồi cạnh tôi. |
| figure_s1 | n | a number or amount | con số, số liệu | The sales figures for May look good. | Số liệu bán hàng của tháng Năm rất khả quan. |
| letter_s1 | n | a written message sent to someone | lá thư, thư | I wrote a letter to my grandmother. | Tôi đã viết một lá thư cho bà. |
| decide_s1 | v | to choose after thinking | quyết định | We decided to stay home tonight. | Chúng tôi quyết định tối nay ở nhà. |
| language_s1 | n | a system used for communication | ngôn ngữ | Learning a language takes regular practice. | Học một ngôn ngữ cần luyện tập thường xuyên. |
| subject_s1 | n | a topic or area of study | môn học, chủ đề | Science is my favorite subject. | Khoa học là môn học yêu thích của tôi. |
| class_s1 | n | a group of students learning together | lớp học, buổi học | There are twenty students in my class. | Lớp tôi có hai mươi học sinh. |
| town_s1 | n | a place larger than a village, smaller than a city | thị trấn | She grew up in a small town by the sea. | Cô ấy lớn lên ở một thị trấn nhỏ ven biển. |
| half_s1 | n | one of two equal parts | một nửa | We ate half of the cake. | Chúng tôi đã ăn một nửa cái bánh. |
| minute_s1 | n | a unit of time equal to sixty seconds | phút | The next bus arrives in ten minutes. | Chuyến xe buýt tiếp theo sẽ đến sau mười phút. |
| food_s1 | n | things people or animals eat | thức ăn, đồ ăn | This restaurant serves very good food. | Nhà hàng này phục vụ đồ ăn rất ngon. |
| break_s1 | v | to damage or separate something by force | làm vỡ, làm gãy | Be careful not to break the glass. | Cẩn thận kẻo làm vỡ cái ly. |
| clear_s1 | adj | easy to see, hear, or understand | rõ ràng | The teacher gave us clear instructions. | Giáo viên đã hướng dẫn chúng tôi rất rõ ràng. |
| future_s1 | n | the time that has not happened yet | tương lai | What do you want to do in the future? | Trong tương lai bạn muốn làm gì? |
| either_s1 | pron | one or the other of two | một trong hai | Both seats are empty; you can take either. | Cả hai ghế đều trống; bạn ngồi ghế nào cũng được. |
| ago_s1 | adv | before the present time | cách đây, trước đây | I started learning English two years ago. | Tôi bắt đầu học tiếng Anh cách đây hai năm. |
| per_s1 | prep | for each | mỗi | The tickets cost ten dollars per person. | Vé có giá mười đô la mỗi người. |
| remain_s1 | v | to stay in the same place or state | vẫn, ở lại | Please remain in your seats until the bus stops. | Vui lòng ngồi yên tại chỗ cho đến khi xe buýt dừng hẳn. |
| among_s1 | prep | in or surrounded by a group | giữa, trong số | I found my pen among the books. | Tôi tìm thấy cây bút của mình giữa đống sách. |
| win_s1 | v | to succeed in a competition | thắng, chiến thắng | Our team won the game last night. | Đội chúng tôi đã thắng trận đấu tối qua. |
| reach_s1 | v | to arrive at or get to a place | đến, đạt tới | We reached the station before noon. | Chúng tôi đến nhà ga trước buổi trưa. |
| social_s1 | adj | relating to society or groups of people | xã hội, giao lưu | She enjoys social events with her coworkers. | Cô ấy thích các sự kiện giao lưu với đồng nghiệp. |
| period_s1 | n | a length or part of time | giai đoạn, khoảng thời gian | The shop is closed for a short period. | Cửa hàng đóng cửa trong một thời gian ngắn. |
| across_s1 | prep | from one side to the other | qua, ngang qua | We swam across the river. | Chúng tôi bơi qua sông. |
| note_s1 | v | to notice or pay attention to something | lưu ý, ghi chú | Please note that the office closes at five. | Xin lưu ý rằng văn phòng đóng cửa lúc năm giờ. |
| history_s1 | n | events that happened in the past | lịch sử | I am reading a book about world history. | Tôi đang đọc một quyển sách về lịch sử thế giới. |
| create_s1 | v | to make something new | tạo ra | The students created a short video together. | Các học sinh đã cùng nhau làm một video ngắn. |
| drive_s1 | v | to control and move a vehicle | lái xe | I drive to work every morning. | Sáng nào tôi cũng lái xe đi làm. |
| along_s1 | prep | moving beside or through a place | dọc theo | We walked along the beach at sunset. | Chúng tôi đi dạo dọc bãi biển lúc hoàng hôn. |
| type_s1 | n | a group of things with shared features | loại, kiểu | What type of phone do you have? | Bạn dùng loại điện thoại nào? |
| sound_s1 | n | something that can be heard | âm thanh, tiếng động | I heard a strange sound outside. | Tôi nghe thấy một âm thanh lạ bên ngoài. |
| game_s1 | n | an activity with rules and a winner | trò chơi, trận đấu | The children are playing a card game. | Bọn trẻ đang chơi một trò chơi bài. |
| political_s1 | adj | related to government or politics | chính trị | The two political parties disagree on taxes. | Hai đảng chính trị bất đồng về thuế. |
| free_s1 | adj | not busy; or not costing money | rảnh, miễn phí | Are you free after lunch? | Bạn có rảnh sau bữa trưa không? |
| receive_s1 | v | to get something that someone sends | nhận, nhận được | You will receive a letter next week. | Tuần sau bạn sẽ nhận được một lá thư. |
| moment_s1 | n | a very short period of time | lát, khoảnh khắc | Please wait a moment, I'm almost ready. | Vui lòng đợi một lát, tôi sắp xong rồi. |
| sale_s1 | n | an occasion when goods are sold | việc bán, đợt giảm giá | These shoes are on sale this week. | Đôi giày này đang giảm giá trong tuần này. |
| policy_s1 | n | a plan or rule used by an organization | chính sách | Our return policy allows refunds within 30 days. | Chính sách đổi trả của chúng tôi cho phép hoàn tiền trong vòng 30 ngày. |
| further_s1 | adj | more; additional | thêm, xa hơn | Call us if you need further information. | Hãy gọi cho chúng tôi nếu bạn cần thêm thông tin. |
