# Task 1A.1 batch 5: NGSL 601-1000

## Scope

Curated the sense 1 row for every NGSL rank 601?1000 lemma present in the English core. The batch contains 333 rows, in NGSL rank order, in `tools/authoring/senses_editor_batch5.tsv`. Definitions are learner-facing and no longer copied from WordNet. Examples are complete English sentences with Vietnamese translations; POS follows the example. The batch does not tick Task 1A.1.

## Quality decisions

- Ran `python tools/check_editor_batch.py tools/authoring/senses_editor_batch5.tsv` to `rows=333 problems=0`.
- Used `[first]` notes where the existing first object needed the everyday learner sense: bank, laugh, attack, miss, park, inside, clock, original, smile and ring. Stable sense IDs were retained.
- Corrected common POS/sense traps including bank as a noun, laugh/attack/miss/park as verbs, inside as an adverb, shall as a modal-like verb label accepted by the repository schema, and nor as a conjunction.

## Audit summary

| metric | before batch 5 | after batch 5 |
|---|---:|---:|
| entries | 4,618 | 4,618 |
| gold | 0 | 0 |
| silver | 988 | 1,033 |
| bronze | 3,630 | 3,585 |
| NGSL missing | 718 | 718 |
| audit errors | 8,904 | 8,476 |

Batch-specific audit checks found zero `gloss_encoding_broken`, `example_translation_encoding_broken`, `example_template`, `example_translation_filler`, or `editor_label_on_wordnet_text` issues, and no audit issue was attached to a batch-5 sense ID.

## Verification

- `python tools/gen_content.py`: passed (`topics=46 words=4618 confusables=32 grammar=38 questions=641 passages=8 petMoods=9`).
- `python tools/validate_content.py`: passed (`errors=0 warnings=0`).
- `python tools/audit_content.py`: passed as a report-only audit; remaining errors are pre-existing outside this batch.
- `gradlew.bat check assembleDebug`: passed; Gradle reported `BUILD SUCCESSFUL` (58 actionable tasks).

## Curated rows

| rank | word | pos | definition | Vietnamese gloss | English example | Vietnamese example | note |
|---:|---|---|---|---|---|---|---|
| 601 | particularly | adv | to a greater degree than usual | đặc biệt, cá biệt, riêng biệt | I enjoy winter, particularly on quiet mornings. | Tôi thích mùa đông, đặc biệt là những buổi sáng yên tĩnh. | editor 2026-10-05 |
| 602 | wife | n | a married woman | vợ | His wife works at a hospital. | Vợ anh ấy làm ở bệnh viện. | editor 2026-10-05 |
| 603 | sport | n | an activity involving exercise or competition | thể thao | Swimming is a healthy sport for many people. | Bơi lội là môn thể thao lành mạnh với nhiều người. | editor 2026-10-05 |
| 604 | prepare | v | to make something ready | chuẩn bị | I prepare my lunch before leaving home. | Tôi chuẩn bị bữa trưa trước khi rời nhà. | editor 2026-10-05 |
| 605 | discuss | v | to talk about something carefully | thảo luận | Let's discuss our ideas after class. | Hãy thảo luận ý tưởng sau giờ học. | editor 2026-10-05 |
| 606 | response | n | an answer or reaction | phản hồi, câu trả lời | We received a quick response to our request. | Chúng tôi nhận được phản hồi nhanh cho yêu cầu của mình. | editor 2026-10-05 |
| 607 | voice | n | the sound made when a person speaks | giọng nói | Her voice sounded tired after the long speech. | Giọng cô ấy nghe mệt sau bài phát biểu dài. | editor 2026-10-05 |
| 608 | piece | n | a separate part of something | mảnh, phần | Please give me one piece of paper. | Vui lòng đưa tôi một mảnh giấy. | editor 2026-10-05 |
| 609 | finish | v | to complete something | hoàn thành, kết thúc | I finish work at five today. | Hôm nay tôi làm việc xong lúc năm giờ. | editor 2026-10-05 |
| 610 | suppose | v | to think or believe something is true | cho rằng, tin, nghĩ rằng | I suppose the meeting starts at nine. | Tôi cho rằng cuộc họp bắt đầu lúc chín giờ. | editor 2026-10-05 |
| 611 | apply | v | to use something for a purpose | áp dụng | Apply the cream gently to the dry skin. | Thoa kem nhẹ nhàng lên vùng da khô. | editor 2026-10-05 |
| 613 | fire | n | flames that burn and produce heat | đám cháy | The firefighters put out the kitchen fire. | Lính cứu hỏa dập tắt đám cháy trong bếp. | editor 2026-10-05 |
| 614 | compare | v | to look for similarities and differences | so sánh | Compare prices before you buy. | So sánh giá trước khi mua. | editor 2026-10-05 |
| 615 | court | n | a place where legal cases are heard | tòa án | The case will go to court next month. | Vụ việc sẽ được đưa ra tòa vào tháng sau. | editor 2026-10-05 |
| 617 | store | n | a shop that sells goods | cửa hàng | I bought fresh fruit at the local store. | Tôi mua trái cây tươi ở cửa hàng địa phương. | editor 2026-10-05 |
| 619 | knowledge | n | information and understanding gained through learning | kiến thức | Reading improves your general knowledge. | Đọc sách giúp mở rộng kiến thức chung. | editor 2026-10-05 |
| 620 | laugh | v | to make sounds that show amusement | cười | The children laugh when the puppy jumps. | Bọn trẻ cười khi chú chó con nhảy lên. | editor 2026-10-05 [first] everyday sense |
| 622 | heart | n | the organ that pumps blood | tim | Exercise keeps your heart healthy. | Tập thể dục giúp tim khỏe. | editor 2026-10-05 |
| 623 | source | n | where something comes from | nguồn (tin) | Always check the source of the news. | Luôn kiểm tra nguồn tin. | editor 2026-10-05 |
| 624 | employee | n | a person paid to work for someone | nhân viên | The company has about 300 employees. | Công ty có khoảng 300 nhân viên. | editor 2026-10-05 |
| 625 | manage | v | to succeed in doing something | quản lý, trông nom | She managed to finish the report before noon. | Cô ấy đã xoay xở hoàn thành báo cáo trước buổi trưa. | editor 2026-10-05 |
| 627 | bank | n | a business that keeps and lends money | đắp bờ (để ngăn) | I deposited my paycheck at the bank. | Tôi gửi tiền lương vào ngân hàng. | editor 2026-10-05 [first] everyday sense |
| 628 | firm | adj | strong and not soft | chắc, rắn chắc | Please give the rope a firm pull. | Hãy kéo sợi dây thật mạnh. | editor 2026-10-05 |
| 630 | article | n | a piece of writing in a newspaper or magazine | bài báo | I read an interesting article about AI. | Tôi đọc một bài báo thú vị về AI. | editor 2026-10-05 |
| 631 | fast | adj | moving at high speed | nhanh | We took a fast train to the city. | Chúng tôi đi tàu nhanh đến thành phố. | editor 2026-10-05 |
| 632 | attack | v | to try to hurt or damage someone | tấn công | The dog may attack if it feels threatened. | Con chó có thể tấn công nếu cảm thấy bị đe dọa. | editor 2026-10-05 [first] everyday sense |
| 633 | foreign | adj | from or belonging to another country | nước ngoài, ngoại quốc | She enjoys meeting foreign students at college. | Cô ấy thích gặp sinh viên nước ngoài ở trường. | editor 2026-10-05 |
| 636 | factor | n | something that helps cause a result | yếu tố | Price is an important factor for buyers. | Giá là yếu tố quan trọng với người mua. | editor 2026-10-05 |
| 637 | pretty | adj | attractive in a delicate way | xinh đẹp | The garden looks pretty after the spring rain. | Khu vườn trông xinh đẹp sau cơn mưa xuân. | editor 2026-10-05 |
| 639 | affect | v | to change or influence something | ảnh hưởng đến | Lack of sleep affects your memory. | Thiếu ngủ ảnh hưởng đến trí nhớ của bạn. | editor 2026-10-05 |
| 640 | drop | v | to let something fall | làm rơi | Be careful not to drop the glass. | Hãy cẩn thận đừng làm rơi chiếc cốc. | editor 2026-10-05 |
| 641 | recent | adj | happening or existing not long ago | gần đây | Have you read any recent news about space? | Bạn đã đọc tin tức gần đây về vũ trụ chưa? | editor 2026-10-05 |
| 642 | relate | v | to show a connection between things | kể lại, thuật lại | This example relates the rule to daily life. | Ví dụ này liên hệ quy tắc với đời sống hằng ngày. | editor 2026-10-05 |
| 643 | official | adj | approved or provided by an authority | chính thức | Please wait for the official announcement. | Vui lòng chờ thông báo chính thức. | editor 2026-10-05 |
| 644 | financial | adj | connected with money or finance | (thuộc) tài chính | The company is in a strong financial position. | Công ty đang có tình hình tài chính vững mạnh. | editor 2026-10-05 |
| 645 | miss | v | to feel sad because someone is absent | nhớ, thấy thiếu | I miss my family when I travel for work. | Tôi nhớ gia đình khi đi công tác. | editor 2026-10-05 [first] everyday sense |
| 646 | art | n | creative work such as painting or music | nghệ thuật | The gallery displays modern art from many artists. | Phòng tranh trưng bày nghệ thuật hiện đại của nhiều nghệ sĩ. | editor 2026-10-05 |
| 647 | campaign | n | an organized effort to achieve a goal | chiến dịch | The new advertising campaign starts in May. | Chiến dịch quảng cáo mới bắt đầu vào tháng Năm. | editor 2026-10-05 |
| 648 | private | adj | belonging to or for one person or group | riêng tư | The doctor spoke with me in a private room. | Bác sĩ nói chuyện với tôi trong một phòng riêng. | editor 2026-10-05 |
| 649 | pause | n | a short stop in an activity | sự tạm dừng | She took a short pause before answering. | Cô ấy dừng lại một lát trước khi trả lời. | editor 2026-10-05 |
| 651 | forget | v | to fail to remember something | quên | Please do not forget your passport tomorrow. | Ngày mai bạn đừng quên hộ chiếu nhé. | editor 2026-10-05 |
| 653 | worry | v | to think about problems with fear | lo, lo nghĩ | Try not to worry about tomorrow's appointment. | Cố gắng đừng lo lắng về cuộc hẹn ngày mai. | editor 2026-10-05 |
| 654 | summer | n | the warmest season of the year | (thuộc) mùa hè | We usually travel during the summer holiday. | Chúng tôi thường đi du lịch trong kỳ nghỉ hè. | editor 2026-10-05 |
| 655 | drink | v | to take liquid into your mouth | uống | I drink water after exercise. | Tôi uống nước sau khi tập thể dục. | editor 2026-10-05 |
| 656 | opinion | n | what someone thinks or believes | ý kiến | In my opinion, this plan is more practical. | Theo ý kiến của tôi, kế hoạch này thực tế hơn. | editor 2026-10-05 |
| 657 | park | v | to leave a vehicle in a place | đỗ xe | Please park your car beside the blue gate. | Vui lòng đỗ xe cạnh cổng màu xanh. | editor 2026-10-05 [first] everyday sense |
| 658 | represent | v | to speak or act for someone | thay mặt, đại diện | This symbol represents peace in the story. | Biểu tượng này đại diện cho hòa bình trong câu chuyện. | editor 2026-10-05 |
| 659 | key | n | a tool used to open a lock | chìa khóa | I keep my key in my jacket pocket. | Tôi để chìa khóa trong túi áo khoác. | editor 2026-10-05 |
| 660 | inside | adv | in or into the inner part | bên trong | The children are playing inside because it is raining. | Bọn trẻ đang chơi bên trong vì trời đang mưa. | editor 2026-10-05 [first] everyday sense |
| 661 | manager | n | a person who controls a business or team | người quản lý | Ask the manager about the new schedule. | Hỏi quản lý về lịch làm việc mới. | editor 2026-10-05 |
| 663 | contain | v | to have something inside | chứa | The box contains three small glass bottles. | Chiếc hộp chứa ba chai thủy tinh nhỏ. | editor 2026-10-05 |
| 664 | notice | n | to see or become aware of something | thông báo (trước) | The rules may change without notice. | Quy định có thể thay đổi mà không báo trước. | editor 2026-10-05 |
| 665 | wonder | v | to think about something with curiosity | tự hỏi | I wonder whether the store is open today. | Tôi tự hỏi hôm nay cửa hàng có mở không. | editor 2026-10-05 |
| 666 | nature | n | the physical world and its living things | thiên nhiên | I love being close to nature. | Tôi thích gần gũi thiên nhiên. | editor 2026-10-05 |
| 667 | structure | n | the way parts are organized together | cấu trúc | The bridge has a strong steel structure. | Cây cầu có kết cấu thép chắc chắn. | editor 2026-10-05 |
| 668 | section | n | one part of a larger thing | mặt cắt, tiết diện | Read the final section before answering. | Hãy đọc phần cuối trước khi trả lời. | editor 2026-10-05 |
| 672 | paint | v | to cover a surface with colored liquid | sơn, quét sơn | We will paint the kitchen walls next week. | Tuần tới chúng tôi sẽ sơn tường bếp. | editor 2026-10-05 |
| 674 | press | n | to push something firmly | báo chí | The minister spoke to the press. | Bộ trưởng trả lời báo chí. | editor 2026-10-05 |
| 676 | necessary | adj | needed for a particular purpose | cần, cần thiết, thiết yếu | Bring only the necessary documents to the interview. | Chỉ mang những giấy tờ cần thiết đến buổi phỏng vấn. | editor 2026-10-05 |
| 677 | region | n | a large area of land | khu vực | This region is known for its quiet beaches. | Khu vực này nổi tiếng với những bãi biển yên tĩnh. | editor 2026-10-05 |
| 678 | growth | n | the process of becoming bigger | sự tăng trưởng | The company reported steady growth this year. | Công ty báo cáo mức tăng trưởng ổn định trong năm nay. | editor 2026-10-05 |
| 679 | evening | n | the part of the day before night | buổi tối | My family eats together in the evening. | Gia đình tôi ăn cùng nhau vào buổi tối. | editor 2026-10-05 |
| 680 | influence | n | the power to change someone or something | ảnh hưởng | Parents have a strong influence on children. | Cha mẹ có ảnh hưởng lớn đến con cái. | editor 2026-10-05 |
| 682 | various | adj | several different kinds | khác nhau, nhiều loại | The library offers books on various subjects. | Thư viện có sách về nhiều chủ đề khác nhau. | editor 2026-10-05 |
| 683 | catch | v | to stop and hold something moving | bắt | Can you catch the ball with one hand? | Bạn có thể bắt quả bóng bằng một tay không? | editor 2026-10-05 |
| 684 | thus | adv | as a result | vậy, như vậy, như thế | The road was closed; thus, we took a bus. | Con đường bị đóng; vì vậy, chúng tôi đi xe buýt. | editor 2026-10-05 |
| 685 | skill | n | an ability learned through practice | kỹ năng | Communication is an important skill. | Giao tiếp là một kỹ năng quan trọng. | editor 2026-10-05 |
| 686 | attempt | v | to try to do something | cố gắng, thử | He made an attempt to repair the lamp. | Anh ấy đã cố gắng sửa chiếc đèn. | editor 2026-10-05 |
| 687 | son | n | a male child | con trai | Their son starts school next September. | Con trai của họ bắt đầu đi học vào tháng Chín tới. | editor 2026-10-05 |
| 688 | simple | adj | easy to understand or do | đơn, đơn giản | This recipe uses simple ingredients from the market. | Công thức này dùng những nguyên liệu đơn giản từ chợ. | editor 2026-10-05 |
| 689 | medium | adj | a middle size or level | trung bình | I would like a medium coffee with milk. | Tôi muốn một cốc cà phê cỡ vừa có sữa. | editor 2026-10-05 |
| 690 | average | n | a usual amount or level | trung bình | The average of these three numbers is ten. | Trung bình của ba số này là mười. | editor 2026-10-05 |
| 691 | stock | n | shares representing ownership in a company | cổ phiếu, hàng tồn kho | The price of the company's stock rose sharply. | Giá cổ phiếu của công ty tăng mạnh. | editor 2026-10-05 |
| 693 | character | n | a person in a story or film | nhân vật, tính cách | The main character is a young doctor. | Nhân vật chính là một bác sĩ trẻ. | editor 2026-10-05 |
| 694 | bed | n | furniture used for sleeping | giường | I read for ten minutes before going to bed. | Tôi đọc sách mười phút trước khi đi ngủ. | editor 2026-10-05 |
| 695 | hit | v | to strike someone or something | đánh, đập | The ball hit the window during the game. | Quả bóng đập vào cửa sổ trong trận đấu. | editor 2026-10-05 |
| 696 | establish | v | to start or create something | thành lập | The family established a small business together. | Gia đình đã thành lập một doanh nghiệp nhỏ cùng nhau. | editor 2026-10-05 |
| 697 | indeed | adv | used to emphasize that something is true | thực sự, quả thực | The task is indeed harder than it looks. | Nhiệm vụ thực sự khó hơn vẻ ngoài. | editor 2026-10-05 |
| 698 | final | adj | last or happening at the end | cuối cùng | The final decision will arrive next week. | Quyết định cuối cùng sẽ đến vào tuần sau. | editor 2026-10-05 |
| 699 | economy | n | the system of money and trade | nền kinh tế | The economy grew by 6 percent this year. | Nền kinh tế tăng trưởng 6% trong năm nay. | editor 2026-10-05 |
| 700 | fit | v | to be the right size or shape | vừa (kích cỡ) | These jeans don't fit me. | Chiếc quần jean này không vừa với tôi. | editor 2026-10-05 |
| 701 | guy | n | an informal word for a man | sự chuồn | That guy helped me carry the heavy box. | Anh chàng đó giúp tôi mang chiếc hộp nặng. | editor 2026-10-05 |
| 702 | function | n | the purpose or job of something | chức năng | The main function of this button is simple. | Chức năng chính của nút này rất đơn giản. | editor 2026-10-05 |
| 703 | yesterday | adv | the day before today | hôm qua | I left my umbrella at school yesterday. | Hôm qua tôi để quên ô ở trường. | editor 2026-10-05 |
| 704 | image | n | a picture or mental idea | hình ảnh (thương hiệu) | The company wants to improve its public image. | Công ty muốn cải thiện hình ảnh trước công chúng. | editor 2026-10-05 |
| 705 | size | n | how large or small something is | cỡ, kích cỡ | This shirt is available in every size. | Chiếc áo này có đủ mọi kích cỡ. | editor 2026-10-05 |
| 707 | addition | n | something added to something else | sự thêm vào | The new addition makes the room more useful. | Phần bổ sung mới làm căn phòng hữu ích hơn. | editor 2026-10-05 |
| 708 | determine | v | to find out something exactly | định, xác định, định rõ | The test will determine your current reading level. | Bài kiểm tra sẽ xác định trình độ đọc hiện tại của bạn. | editor 2026-10-05 |
| 709 | station | n | a place for a particular service | trạm, nhà ga | The bus station is near the city center. | Bến xe buýt gần trung tâm thành phố. | editor 2026-10-05 |
| 710 | population | n | all the people living in an area | dân số | The city's population has grown quickly. | Dân số thành phố đã tăng nhanh. | editor 2026-10-05 |
| 711 | fail | v | to not succeed in doing something | không nhớ, quên | Do not fail to lock the door tonight. | Tối nay đừng quên khóa cửa. | editor 2026-10-05 |
| 712 | environment | n | the natural world around us | môi trường | We must protect the environment. | Chúng ta phải bảo vệ môi trường. | editor 2026-10-05 |
| 714 | contract | n | a legal agreement between people or organizations | hợp đồng | Both sides signed the contract yesterday. | Hai bên đã ký hợp đồng hôm qua. | editor 2026-10-05 |
| 716 | comment | n | a statement giving an opinion | bình luận | Her comment helped us improve the plan. | Nhận xét của cô ấy giúp chúng tôi cải thiện kế hoạch. | editor 2026-10-05 |
| 718 | occur | v | to happen | xuất hiện, tìm thấy | Most accidents occur when roads are wet. | Hầu hết tai nạn xảy ra khi đường ướt. | editor 2026-10-05 |
| 719 | alone | adj | without other people | một mình (không có ai khác) | She was alone in the classroom. | Cô ấy ở một mình trong lớp. | editor 2026-10-05 |
| 720 | significant | adj | important or large enough to notice | đáng kể, quan trọng | There was a significant increase in sales. | Doanh số tăng đáng kể. | editor 2026-10-05 |
| 722 | wall | n | a vertical structure that encloses or divides | tường | She put a clock on the wall. | Cô ấy treo đồng hồ lên tường. | editor 2026-10-05 |
| 723 | series | n | a group of related things in order | loạt, chuỗi | This series has five seasons. | Bộ phim này có năm mùa. | editor 2026-10-05 |
| 724 | direct | v | to control or guide something | thẳng, ngay, lập tức | A guide will direct visitors to the museum. | Một hướng dẫn viên sẽ chỉ đường cho khách đến bảo tàng. | editor 2026-10-05 |
| 725 | success | n | the achievement of a desired result | thành công | Hard work was the key to her success. | Làm việc chăm chỉ là chìa khóa thành công của cô ấy. | editor 2026-10-05 |
| 726 | tomorrow | adv | the day after today | ngày mai | We are visiting our grandparents tomorrow. | Ngày mai chúng tôi sẽ thăm ông bà. | editor 2026-10-05 |
| 727 | director | n | a person who controls a film or organization | đạo diễn, giám đốc | The director won an award. | Vị đạo diễn đã giành giải thưởng. | editor 2026-10-05 |
| 728 | clearly | adv | in a way that is easy to understand | rõ ràng, sáng sủa, sáng tỏ | Please explain the instructions clearly to everyone. | Vui lòng giải thích hướng dẫn rõ ràng cho mọi người. | editor 2026-10-05 |
| 729 | lack | v | to not have enough of something | thiếu | The project may fail because it lacks funding. | Dự án có thể thất bại vì thiếu kinh phí. | editor 2026-10-05 |
| 730 | review | n | to examine something again | bài đánh giá | The film got great reviews. | Bộ phim nhận được đánh giá rất tốt. | editor 2026-10-05 |
| 731 | depend | v | to need someone or something | phụ thuộc | Success can depend on careful preparation. | Thành công có thể phụ thuộc vào sự chuẩn bị kỹ lưỡng. | editor 2026-10-05 |
| 732 | race | n | a competition to move fastest | cuộc đua | She won the race by two seconds. | Cô ấy thắng cuộc đua với cách biệt hai giây. | editor 2026-10-05 |
| 733 | recognize | v | to know someone or something from before | công nhận, thừa nhận, chấp nhận | I recognized her voice on the phone. | Tôi nhận ra giọng cô ấy qua điện thoại. | editor 2026-10-05 |
| 734 | window | n | an opening with glass in a wall | cửa sổ | The cat is sitting by the window. | Con mèo đang ngồi cạnh cửa sổ. | editor 2026-10-05 |
| 735 | purpose | n | the reason something exists or is done | mục đích | The purpose of this meeting is to plan. | Mục đích của cuộc họp này là lập kế hoạch. | editor 2026-10-05 |
| 736 | department | n | a section of an organization | phòng ban, bộ phận | She works in the sales department. | Cô ấy làm việc ở phòng kinh doanh. | editor 2026-10-05 |
| 737 | gain | v | to get more of something | đạt được, thu được | The plants gain strength from regular sunlight. | Cây cối có thêm sức mạnh nhờ ánh nắng đều đặn. | editor 2026-10-05 |
| 738 | tree | n | a tall plant with a trunk | biểu đồ hình cây, cây | A tall tree shades the garden in summer. | Một cây cao che bóng cho khu vườn vào mùa hè. | editor 2026-10-05 |
| 739 | college | n | a place for higher education | trường cao đẳng, đại học | My sister starts college in September. | Chị gái tôi bắt đầu học đại học vào tháng Chín. | editor 2026-10-05 |
| 740 | argue | v | to disagree and give reasons | cãi nhau | My brothers argue about everything. | Các anh tôi cãi nhau về mọi thứ. | editor 2026-10-05 |
| 741 | board | n | a flat piece of wood or other material | bảng | Write your answer on the white board. | Hãy viết câu trả lời lên bảng trắng. | editor 2026-10-05 |
| 742 | holiday | n | a period when people rest or travel | kỳ nghỉ | We visited a quiet village during the holiday. | Chúng tôi thăm một ngôi làng yên tĩnh trong kỳ nghỉ. | editor 2026-10-05 |
| 743 | mark | n | a visible sign or written symbol | điểm số | She got full marks on the test. | Cô ấy đạt điểm tuyệt đối bài kiểm tra. | editor 2026-10-05 |
| 744 | church | n | a building where Christians worship | nhà thờ | The old church stands beside the river. | Nhà thờ cổ nằm bên cạnh con sông. | editor 2026-10-05 |
| 746 | achieve | v | to succeed in reaching a goal | đạt được | You can achieve your goals with regular practice. | Bạn có thể đạt mục tiêu bằng luyện tập đều đặn. | editor 2026-10-05 |
| 747 | item | n | one thing in a group | món đồ, mặt hàng | Each item is checked before it is shipped. | Mỗi mặt hàng đều được kiểm tra trước khi gửi. | editor 2026-10-05 |
| 748 | prove | v | to show that something is true | chứng tỏ, chứng minh | The receipt can prove that we paid. | Biên lai có thể chứng minh chúng tôi đã trả tiền. | editor 2026-10-05 |
| 749 | cent | n | a coin worth one hundredth of a dollar | xu | This chocolate bar costs only fifty cents. | Thanh sô-cô-la này chỉ có giá năm mươi xu. | editor 2026-10-05 |
| 750 | season | n | one part of the year | mùa | Autumn is the best season in the capital city. | Mùa thu là mùa đẹp nhất ở Hà Nội. | editor 2026-10-05 |
| 751 | floor | n | the flat surface you walk on | sàn nhà | There is a blue rug on the floor. | Có một tấm thảm xanh trên sàn. | editor 2026-10-05 |
| 752 | stuff | n | things or material of an unspecified kind | đồ đạc, thứ | Please put your stuff in this cupboard. | Vui lòng để đồ đạc của bạn vào tủ này. | editor 2026-10-05 |
| 753 | wide | adj | measuring a large distance from side to side | rộng, rộng lớn | The river is too wide to cross here. | Con sông quá rộng để băng qua ở đây. | editor 2026-10-05 |
| 755 | method | n | a way of doing something | phương pháp | This is a simple method for learning words. | Đây là một phương pháp học từ đơn giản. | editor 2026-10-05 |
| 757 | election | n | a process for choosing political leaders | cuộc bầu cử | The election will take place in November. | Cuộc bầu cử sẽ diễn ra vào tháng Mười Một. | editor 2026-10-05 |
| 760 | club | n | an organization for people with a shared interest | hội, câu lạc bộ | She joined the school chess club this year. | Cô ấy tham gia câu lạc bộ cờ của trường năm nay. | editor 2026-10-05 |
| 761 | below | prep | in or to a lower place | ở dưới | The temperature fell below zero overnight. | Nhiệt độ giảm xuống dưới không độ qua đêm. | editor 2026-10-05 |
| 762 | movie | n | a story shown as moving pictures | bộ phim | Let's watch a movie tonight. | Tối nay xem phim đi. | editor 2026-10-05 |
| 763 | doctor | n | a person trained to treat illness | bác sĩ | You should see a doctor. | Bạn nên đi khám bác sĩ. | editor 2026-10-05 |
| 764 | discussion | n | a talk about a subject | cuộc thảo luận | Our discussion lasted nearly an hour. | Cuộc thảo luận của chúng tôi kéo dài gần một giờ. | editor 2026-10-05 |
| 765 | sorry | adj | feeling bad about something | xin lỗi, tiếc | I am sorry that your train was delayed. | Tôi rất tiếc vì chuyến tàu của bạn bị trễ. | editor 2026-10-05 |
| 766 | challenge | n | a difficult task or situation | sự thách thức | Learning new verbs can be a real challenge. | Học động từ mới có thể là một thử thách thực sự. | editor 2026-10-05 |
| 768 | nearly | adv | almost but not completely | gần, sắp, suýt | We are nearly ready to leave the house. | Chúng tôi gần như sẵn sàng rời khỏi nhà. | editor 2026-10-05 |
| 769 | statement | n | something said or written clearly | lời tuyên bố, bản phát biểu | Your bank statement arrives at the start of each month. | Sao kê ngân hàng đến vào đầu mỗi tháng. | editor 2026-10-05 |
| 771 | despite | prep | without being affected by something | mặc dù, bất chấp | We enjoyed the trip despite the rain. | Chúng tôi vẫn thích chuyến đi dù trời mưa. | editor 2026-10-05 |
| 772 | introduce | v | to tell people who someone is | giới thiệu | Let me introduce our new teacher to you. | Để tôi giới thiệu giáo viên mới với bạn. | editor 2026-10-05 |
| 773 | advantage | n | something that helps you succeed | lợi thế | Speaking two languages gives her a clear advantage. | Nói được hai ngôn ngữ mang lại cho cô ấy lợi thế rõ ràng. | editor 2026-10-05 |
| 774 | ready | adj | prepared for use or action | sẵn sàng | Dinner will be ready in ten minutes. | Bữa tối sẽ sẵn sàng trong mười phút. | editor 2026-10-05 |
| 775 | marry | v | to become someone's husband or wife | kết hôn | They plan to marry after finishing college. | Họ dự định kết hôn sau khi học xong đại học. | editor 2026-10-05 |
| 776 | strike | v | to hit something or someone | đánh, đập | Lightning can strike the tallest tree nearby. | Sét có thể đánh vào cây cao nhất gần đó. | editor 2026-10-05 |
| 777 | mile | n | a unit of distance equal to 1.609 kilometers | dặm, lý | The village is only one mile from here. | Ngôi làng chỉ cách đây một dặm. | editor 2026-10-05 |
| 778 | seek | v | to try to find or get something | tìm kiếm | Many students seek advice before the exam. | Nhiều học sinh tìm lời khuyên trước kỳ thi. | editor 2026-10-05 |
| 779 | ability | n | the power or skill to do something | khả năng | Her ability to explain ideas is impressive. | Khả năng giải thích ý tưởng của cô ấy rất ấn tượng. | editor 2026-10-05 |
| 780 | unit | n | a single thing used as part of a whole | đơn vị | Each lesson includes one unit of new words. | Mỗi bài học gồm một đơn vị từ mới. | editor 2026-10-05 |
| 781 | card | n | a small piece of stiff paper | thẻ | He used a card to pay for lunch. | Anh ấy dùng thẻ để trả tiền trưa. | editor 2026-10-05 |
| 782 | hospital | n | a place where sick people receive care | bệnh viện | She works at the city hospital. | Cô ấy làm ở bệnh viện thành phố. | editor 2026-10-05 |
| 784 | interview | n | a meeting where someone is asked questions | buổi phỏng vấn | I have a job interview tomorrow morning. | Tôi có một buổi phỏng vấn xin việc vào sáng mai. | editor 2026-10-05 |
| 785 | agreement | n | a decision accepted by two or more sides | thỏa thuận | We reached an agreement after long talks. | Chúng tôi đạt được thỏa thuận sau các cuộc đàm phán dài. | editor 2026-10-05 |
| 786 | release | v | to let someone or something go free | sự thả, sự phóng thích | The company will release a new app soon. | Công ty sẽ phát hành một ứng dụng mới. | editor 2026-10-05 |
| 787 | tax | n | money paid to the government | thuế | Prices include a 10 percent sales tax. | Giá đã bao gồm 10% thuế bán hàng. | editor 2026-10-05 |
| 788 | solution | n | a way to solve a problem | giải pháp, lời giải | We found a simple solution to the problem. | Chúng tôi tìm ra giải pháp đơn giản cho vấn đề. | editor 2026-10-05 |
| 789 | capital | n | money used to start or grow a business | vốn | The company needs capital to open a factory. | Công ty cần vốn để mở một nhà máy. | editor 2026-10-05 |
| 790 | popular | adj | liked or enjoyed by many people | phổ biến, được yêu thích | This restaurant is popular with local families. | Nhà hàng này được các gia đình địa phương yêu thích. | editor 2026-10-05 |
| 791 | specific | adj | clearly defined and not general | cụ thể | Could you give a specific example of the problem? | Bạn có thể cho một ví dụ cụ thể về vấn đề không? | editor 2026-10-05 |
| 792 | beautiful | adj | very attractive or pleasing | đẹp | The garden looks beautiful in spring. | Khu vườn trông đẹp vào mùa xuân. | editor 2026-10-05 |
| 793 | fear | n | an unpleasant feeling caused by danger | sự sợ, sự sợ hãi | Her fear of dogs began after an accident. | Nỗi sợ chó của cô ấy bắt đầu sau một tai nạn. | editor 2026-10-05 |
| 794 | aim | n | a goal or purpose | sự nhắm | Our main aim is to improve reading speed. | Mục tiêu chính của chúng tôi là cải thiện tốc độ đọc. | editor 2026-10-05 |
| 796 | serious | adj | important and not for joking | đứng đắn, nghiêm trang, nghiêm nghị | The doctor found no serious health problem. | Bác sĩ không phát hiện vấn đề sức khỏe nghiêm trọng. | editor 2026-10-05 |
| 797 | target | n | a person or thing aimed at | mục tiêu, đối tượng | Young people are our main target market. | Người trẻ là thị trường mục tiêu chính của chúng tôi. | editor 2026-10-05 |
| 798 | degree | n | a unit or level of measurement | bằng cấp, mức độ | She has a degree in economics. | Cô ấy có bằng kinh tế. | editor 2026-10-05 |
| 799 | pull | v | to move something toward you | kéo | Pull the door gently toward you. | Kéo cánh cửa nhẹ nhàng về phía bạn. | editor 2026-10-05 |
| 800 | red | adj | having the color of blood | đỏ | She bought a red scarf for winter. | Cô ấy mua một chiếc khăn quàng đỏ cho mùa đông. | editor 2026-10-05 |
| 801 | husband | n | a married man | chồng | Her husband is a teacher. | Chồng cô ấy là giáo viên. | editor 2026-10-05 |
| 802 | access | n | the right or way to use something | quyền truy cập | Only managers have access to this file. | Chỉ quản lý có quyền truy cập tệp này. | editor 2026-10-05 |
| 803 | movement | n | the act of changing position | sự chuyển động | The sensor detects movement near the door. | Cảm biến phát hiện chuyển động gần cửa. | editor 2026-10-05 |
| 804 | treat | v | to behave toward someone in a certain way | điều đình, thương lượng | Please treat every customer with respect. | Vui lòng đối xử tôn trọng với mọi khách hàng. | editor 2026-10-05 |
| 805 | identify | v | to recognize and name someone or something | đồng nhất với, đồng cảm với | The nurse identified the correct patient quickly. | Y tá nhanh chóng xác định đúng bệnh nhân. | editor 2026-10-05 |
| 806 | loss | n | the fact of no longer having something | khoản lỗ, sự mất mát | The shop made a loss in its first year. | Cửa hàng bị lỗ trong năm đầu tiên. | editor 2026-10-05 |
| 807 | shall | v | used to talk about a future action | sẽ | We shall meet outside the library at noon. | Chúng ta sẽ gặp nhau bên ngoài thư viện vào buổi trưa. | editor 2026-10-05 |
| 808 | modern | adj | belonging to the present time | hiện đại | The museum has a modern design and bright rooms. | Bảo tàng có thiết kế hiện đại và các phòng sáng. | editor 2026-10-05 |
| 809 | pressure | n | a force that pushes against something | áp lực, áp suất | High pressure can damage the water pipe. | Áp suất cao có thể làm hỏng ống nước. | editor 2026-10-05 |
| 810 | bus | n | a large road vehicle for passengers | xe buýt | The last bus leaves at eleven tonight. | Chuyến xe buýt cuối rời đi lúc mười một giờ tối nay. | editor 2026-10-05 |
| 811 | treatment | n | medical care for an illness | sự điều trị | The treatment takes six weeks. | Việc điều trị kéo dài sáu tuần. | editor 2026-10-05 |
| 812 | conference | n | a formal meeting for discussion | hội nghị | The conference will be held in the capital city. | Hội nghị sẽ được tổ chức tại Hà Nội. | editor 2026-10-05 |
| 814 | supply | n | an amount available for use | sự cung cấp, sự tiếp tế | The town has a safe supply of drinking water. | Thị trấn có nguồn nước uống an toàn. | editor 2026-10-05 |
| 816 | worth | adj | having a particular value | giá, đáng giá | This old camera is still worth repairing. | Chiếc máy ảnh cũ này vẫn đáng để sửa. | editor 2026-10-05 |
| 817 | natural | adj | existing in nature or not made by people | tự nhiên, (thuộc) thiên nhiên | The park protects the area's natural beauty. | Công viên bảo vệ vẻ đẹp tự nhiên của khu vực. | editor 2026-10-05 |
| 818 | express | v | to show or say something | nói rõ, rõ ràng | He expressed his thanks after the meeting. | Anh ấy bày tỏ lời cảm ơn sau cuộc họp. | editor 2026-10-05 |
| 819 | indicate | v | to show or point out something | chỉ ra, cho thấy | The results indicate a clear trend. | Kết quả cho thấy một xu hướng rõ ràng. | editor 2026-10-05 |
| 820 | attend | v | to go to an event or place | tham dự | All managers must attend the training session. | Tất cả quản lý phải tham dự buổi đào tạo. | editor 2026-10-05 |
| 821 | brother | n | a boy or man with the same parents | anh, em trai | My younger brother plays the guitar. | Em trai tôi chơi đàn ghi-ta. | editor 2026-10-05 |
| 823 | score | n | the number of points in a game | ghi bàn | The final score was three to two. | Tỉ số cuối cùng là ba- hai. | editor 2026-10-05 |
| 824 | organize | v | to arrange things in an orderly way | tổ chức | We need to organize the files by date. | Chúng ta cần sắp xếp các tệp theo ngày. | editor 2026-10-05 |
| 825 | trip | n | a journey to a place | chuyến đi | We took a short trip to the coast. | Chúng tôi có chuyến đi ngắn đến bờ biển. | editor 2026-10-05 |
| 826 | beyond | prep | farther away than something | ở bên kia | The small village lies beyond the mountain. | Ngôi làng nhỏ nằm phía bên kia ngọn núi. | editor 2026-10-05 |
| 827 | sleep | v | to rest with your eyes closed | ngủ | The baby is sleeping in the bedroom. | Em bé đang ngủ trong phòng ngủ. | editor 2026-10-05 |
| 828 | fish | n | an animal that lives in water | cá | We saw tiny fish in the stream. | Chúng tôi nhìn thấy những con cá nhỏ trong suối. | editor 2026-10-05 |
| 829 | promise | v | to say that you will do something | lời hứa | I promise to call you after work. | Tôi hứa sẽ gọi cho bạn sau giờ làm. | editor 2026-10-05 |
| 830 | potential | adj | possible or likely in the future | tiềm năng | We met several potential customers at the fair. | Chúng tôi gặp vài khách hàng tiềm năng ở hội chợ. | editor 2026-10-05 |
| 831 | energy | n | the power needed to do work | năng lượng | Turn off the lights to save energy. | Tắt đèn để tiết kiệm năng lượng. | editor 2026-10-05 |
| 832 | trouble | n | a problem or difficulty | điều lo lắng, điều phiền muộn | The old printer is causing trouble again. | Chiếc máy in cũ lại gây rắc rối. | editor 2026-10-05 |
| 833 | relation | n | a connection between people or things | mối quan hệ, mối liên hệ | There is a close relation between sleep and health. | Có mối liên hệ chặt chẽ giữa giấc ngủ và sức khỏe. | editor 2026-10-05 |
| 834 | touch | v | to put your hand on something | chạm | Please do not touch the wet paint. | Vui lòng đừng chạm vào sơn ướt. | editor 2026-10-05 |
| 836 | middle | n | the central part of something | giữa | The pharmacy is in the middle of town. | Nhà thuốc nằm ở giữa thị trấn. | editor 2026-10-05 |
| 837 | bar | n | a place where drinks are served | thanh, thỏi | We ordered juice at a quiet hotel bar. | Chúng tôi gọi nước ép tại quầy bar yên tĩnh của khách sạn. | editor 2026-10-05 |
| 838 | suffer | v | to feel pain or difficulty | chịu, bị | Many plants suffer during a long drought. | Nhiều cây cối chịu ảnh hưởng trong đợt hạn hán dài. | editor 2026-10-05 |
| 839 | strategy | n | a plan for achieving a goal | chiến lược | We need a new marketing strategy. | Chúng ta cần một chiến lược marketing mới. | editor 2026-10-05 |
| 840 | deep | adj | going far down from the surface | sâu | The water is too deep for small children. | Nước quá sâu đối với trẻ nhỏ. | editor 2026-10-05 |
| 841 | except | prep | not including someone or something | trừ, ngoại trừ | Everyone came except the team captain. | Mọi người đều đến trừ đội trưởng. | editor 2026-10-05 |
| 842 | clean | adj | free from dirt | sạch | Please use a clean spoon. | Hãy dùng một chiếc thìa sạch. | editor 2026-10-05 |
| 843 | tend | v | to usually do or be something | có xu hướng | Young children tend to ask many questions. | Trẻ nhỏ thường có xu hướng hỏi nhiều câu. | editor 2026-10-05 |
| 844 | advance | v | to move forward | tiến lên, tiến tới, tiến bộ | The soldiers advanced slowly through the valley. | Những người lính tiến chậm qua thung lũng. | editor 2026-10-05 |
| 845 | fill | v | to make something full | làm đầy | Please fill the bottle with clean water. | Vui lòng đổ đầy chai bằng nước sạch. | editor 2026-10-05 |
| 846 | star | n | a bright object in the night sky | sao, ngôi sao, tinh tú | We saw a bright star above the trees. | Chúng tôi thấy một ngôi sao sáng trên những cái cây. | editor 2026-10-05 |
| 847 | network | n | a system of connected people or things | mạng (lưới) | The office network is down. | Mạng văn phòng đang bị sập. | editor 2026-10-05 |
| 848 | generally | adv | in most cases | nói chung, đại thể | People generally feel better after enough sleep. | Mọi người thường cảm thấy khỏe hơn sau khi ngủ đủ. | editor 2026-10-05 |
| 849 | operation | n | an organized activity or process | hoạt động | The machine stops during a power operation check. | Máy dừng trong khi kiểm tra hoạt động điện. | editor 2026-10-05 |
| 850 | match | v | to be the same or look good together | trận đấu | These curtains match the color of the sofa. | Những chiếc rèm này hợp với màu của ghế sofa. | editor 2026-10-05 |
| 851 | avoid | v | to stay away from something | tránh, tránh xa | Wear a hat to avoid getting sunburned. | Hãy đội mũ để tránh bị cháy nắng. | editor 2026-10-05 |
| 852 | seat | n | a place where someone can sit | chỗ ngồi | Please take a seat near the window. | Vui lòng ngồi vào chỗ gần cửa sổ. | editor 2026-10-05 |
| 853 | throw | v | to send something through the air | ném | Please throw the empty bottle in the bin. | Vui lòng ném chai rỗng vào thùng rác. | editor 2026-10-05 |
| 854 | task | n | a piece of work to do | nhiệm vụ, việc cần làm | Finish one task before starting another. | Hoàn thành một việc trước khi bắt đầu việc khác. | editor 2026-10-05 |
| 855 | normal | adj | usual or expected | thường, thông thường, bình thường | It is normal to feel nervous before an exam. | Cảm thấy lo lắng trước kỳ thi là bình thường. | editor 2026-10-05 |
| 856 | goal | n | something you want to achieve | mục tiêu | My goal is to read a book in English. | Mục tiêu của tôi là đọc một cuốn sách tiếng Anh. | editor 2026-10-05 |
| 857 | associate | v | to connect one thing with another | liên kết, liên tưởng | Many people associate this smell with childhood. | Nhiều người liên tưởng mùi này với tuổi thơ. | editor 2026-10-05 |
| 858 | blue | adj | having the color of a clear sky | xanh, lam | The child chose a blue backpack for school. | Đứa trẻ chọn một chiếc ba lô xanh cho trường học. | editor 2026-10-05 |
| 859 | positive | adj | good or certain rather than negative | xác thực, rõ ràng | The doctor gave us a positive update today. | Hôm nay bác sĩ đưa cho chúng tôi một thông tin tích cực. | editor 2026-10-05 |
| 861 | box | n | a container with flat sides | hộp | The gift came in a small wooden box. | Món quà được đựng trong một chiếc hộp gỗ nhỏ. | editor 2026-10-05 |
| 862 | huge | adj | extremely large | to lớn, đồ sộ, khổng lồ | A huge tree fell across the road. | Một cây lớn đổ chắn ngang con đường. | editor 2026-10-05 |
| 863 | message | n | information sent to someone | tin nhắn, thông điệp | Please leave a message after the tone. | Vui lòng để lại lời nhắn sau tiếng báo. | editor 2026-10-05 |
| 864 | instance | n | an example of something | thí dụ, ví dụ (chứng minh, minh hoạ) | For instance, you can use this simple method. | Ví dụ, bạn có thể dùng phương pháp đơn giản này. | editor 2026-10-05 |
| 865 | style | n | a particular way of doing or appearing | phong cách | He has his own style. | Anh ấy có phong cách riêng. | editor 2026-10-05 |
| 867 | cold | adj | having a low temperature | lạnh | I need a jacket because it's cold outside. | Tôi cần áo khoác vì bên ngoài lạnh. | editor 2026-10-05 |
| 868 | push | v | to move something away from you | đẩy | Push the button to open the gate. | Nhấn nút để mở cổng. | editor 2026-10-05 |
| 869 | quarter | n | one part of four equal parts | quý (3 tháng) | Sales rose in the second quarter. | Doanh số tăng trong quý hai. | editor 2026-10-05 |
| 870 | assume | v | to accept something as true without proof | cho rằng, giả định | I assume you've read the report. | Tôi cho rằng bạn đã đọc báo cáo. | editor 2026-10-05 |
| 872 | successful | adj | achieving the result you wanted | thành công | The community project was successful last year. | Dự án cộng đồng đã thành công vào năm ngoái. | editor 2026-10-05 |
| 873 | sing | v | to make music with your voice | hát, ca hát | They sing together at the community center. | Họ hát cùng nhau tại trung tâm cộng đồng. | editor 2026-10-05 |
| 874 | doubt | v | to feel unsure about something | nghi ngờ | I doubt that the shop is open now. | Tôi nghi ngờ cửa hàng đang mở lúc này. | editor 2026-10-05 |
| 875 | competition | n | a situation where people try to win | sự cạnh tranh | The school held a cooking competition yesterday. | Hôm qua trường tổ chức một cuộc thi nấu ăn. | editor 2026-10-05 |
| 876 | theory | n | an explanation based on ideas and evidence | lý thuyết | In theory, the plan should work. | Về lý thuyết, kế hoạch sẽ hiệu quả. | editor 2026-10-05 |
| 877 | propose | v | to suggest a plan or idea | đề nghị, đề xuất, đưa ra | The team proposed a better solution yesterday. | Hôm qua nhóm đã đề xuất một giải pháp tốt hơn. | editor 2026-10-05 |
| 878 | reference | n | a source of information or a recommendation | tài liệu tham khảo, người giới thiệu | Please provide two references from former employers. | Vui lòng cung cấp hai người giới thiệu từ nơi làm việc cũ. | editor 2026-10-05 |
| 879 | argument | n | a reason given to support an idea | lập luận, cuộc tranh cãi | Her argument is clear and logical. | Lập luận của cô ấy rõ ràng và logic. | editor 2026-10-05 |
| 881 | fly | v | to move through the air | bay | Birds fly south when winter begins. | Chim bay về phía nam khi mùa đông bắt đầu. | editor 2026-10-05 |
| 882 | document | n | a written or electronic record | tài liệu, văn bản | Save the document before you close it. | Hãy lưu tài liệu trước khi đóng. | editor 2026-10-05 |
| 884 | application | n | a formal request for something | đơn đăng ký | She submitted an application for the job. | Cô ấy nộp đơn xin việc. | editor 2026-10-05 |
| 885 | hot | adj | having a high temperature | nóng | Be careful: the soup is hot. | Cẩn thận: súp nóng đấy. | editor 2026-10-05 |
| 888 | bill | n | a paper showing how much you must pay | hóa đơn (điện, nước, nhà hàng) | Could we have the bill before we leave? | Cho chúng tôi xin hóa đơn trước khi đi được không? | editor 2026-10-05 |
| 889 | search | n | an attempt to find something | sự tìm kiếm | The police began a search of the building. | Cảnh sát bắt đầu khám xét tòa nhà. | editor 2026-10-05 |
| 891 | central | adj | in the middle or most important | trung tâm, chủ yếu | The hotel is in a central part of town. | Khách sạn nằm ở khu vực trung tâm thị trấn. | editor 2026-10-05 |
| 892 | career | n | a person's working life | sự nghiệp | He wants a career in education. | Anh ấy muốn có sự nghiệp trong ngành giáo dục. | editor 2026-10-05 |
| 893 | anyway | adv | used to return to a subject or continue | thế nào cũng được, cách nào cũng được | It was raining, but we went anyway. | Trời đang mưa, nhưng dù sao chúng tôi vẫn đi. | editor 2026-10-05 |
| 894 | speech | n | a talk given to an audience | khả năng nói, năng lực nói | The mayor gave a short speech at noon. | Thị trưởng có bài phát biểu ngắn vào buổi trưa. | editor 2026-10-05 |
| 895 | dog | n | a common domesticated animal | gã, thằng cha | Their dog sleeps beside the front door. | Con chó của họ ngủ cạnh cửa trước. | editor 2026-10-05 |
| 896 | officer | n | a person with authority in an organization | sĩ quan | The officer checked our tickets at the entrance. | Nhân viên kiểm tra vé của chúng tôi ở lối vào. | editor 2026-10-05 |
| 898 | oil | n | a thick liquid used for fuel or cooking | dầu | Add a little oil before frying the vegetables. | Thêm một ít dầu trước khi xào rau. | editor 2026-10-05 |
| 899 | dress | n | a one-piece garment worn by a woman | váy liền | She wore a red dress to the party. | Cô ấy mặc váy đỏ đến bữa tiệc. | editor 2026-10-05 |
| 900 | profit | n | money left after costs are paid | lợi nhuận | The company made a large profit last year. | Công ty thu được lợi nhuận lớn năm ngoái. | editor 2026-10-05 |
| 901 | guess | v | an answer based on limited information | sự đoán, sự ước chừng | I guess the meeting will finish soon. | Tôi đoán cuộc họp sẽ sớm kết thúc. | editor 2026-10-05 |
| 902 | fun | adj | enjoyable and amusing | vui, thú vị | The cooking class was fun. | Buổi học nấu ăn rất vui. | editor 2026-10-05 |
| 903 | protect | v | to keep someone or something safe | bảo vệ | Sunscreen helps protect your skin from burns. | Kem chống nắng giúp bảo vệ da bạn khỏi bị bỏng. | editor 2026-10-05 |
| 904 | resource | n | a useful supply of something | tài nguyên, nguồn lực | Water is a precious resource. | Nước là tài nguyên quý giá. | editor 2026-10-05 |
| 905 | science | n | the study of the natural world | khoa học | She studies science at school every morning. | Cô ấy học khoa học ở trường mỗi sáng. | editor 2026-10-05 |
| 907 | balance | n | the amount left in an account | số dư | Please check your account balance online. | Vui lòng kiểm tra số dư tài khoản trực tuyến. | editor 2026-10-05 |
| 908 | damage | n | harm that makes something worse | sự thiệt hại | The storm caused serious damage to the roof. | Cơn bão gây thiệt hại nghiêm trọng cho mái nhà. | editor 2026-10-05 |
| 910 | author | n | a person who writes a book or text | tác giả | Ho Ngoc Duc is the author of the FVDP software. | Hồ Ngọc Đức là tác giả của phần mềm FVDP | editor 2026-10-05 |
| 911 | basic | adj | simple and most important | cơ bản, cơ sở | The course teaches basic computer skills. | Khóa học dạy các kỹ năng máy tính cơ bản. | editor 2026-10-05 |
| 913 | hair | n | thin strands growing on the body | tóc | She has long black hair. | Cô ấy có mái tóc đen dài. | editor 2026-10-05 |
| 914 | male | adj | being a man or boy or a male animal | trai, đực, trống | The farmer separated the male and female birds. | Người nông dân tách những con chim đực và cái. | editor 2026-10-05 |
| 915 | operate | v | to use or control a machine | vận hành | Only trained staff may operate this machine. | Chỉ nhân viên đã qua đào tạo mới được vận hành máy này. | editor 2026-10-05 |
| 916 | reflect | v | to send back light, heat, or sound | phản chiếu, phản xạ, dội lại | The lake reflects the mountains at sunrise. | Hồ phản chiếu những ngọn núi lúc bình minh. | editor 2026-10-05 |
| 917 | exercise | n | physical activity to keep healthy | sự tập thể dục, bài tập | Regular exercise is good for your heart. | Tập thể dục đều đặn tốt cho tim. | editor 2026-10-05 |
| 918 | useful | adj | helpful for a purpose | làm ăn được, cừ | This dictionary is useful for new learners. | Từ điển này hữu ích cho người học mới. | editor 2026-10-05 |
| 920 | income | n | money received regularly | thu nhập, doanh thu, lợi tức | His monthly income covers the rent and food. | Thu nhập hằng tháng của anh ấy đủ trả tiền thuê và thức ăn. | editor 2026-10-05 |
| 921 | property | n | something owned by a person or group | tài sản | Do not damage public property. | Không được làm hư hại tài sản công. | editor 2026-10-05 |
| 922 | previous | adj | happening before another time | trước đó | The previous owner painted the house blue. | Chủ cũ đã sơn ngôi nhà màu xanh. | editor 2026-10-05 |
| 923 | dark | adj | having little or no light | tối, u ám | The room became dark after the lights failed. | Căn phòng trở nên tối sau khi đèn hỏng. | editor 2026-10-05 |
| 924 | imagine | v | to form a picture in your mind | tưởng rằng, nghĩ rằng, cho rằng | Imagine living in a city without cars. | Hãy tưởng tượng sống trong một thành phố không có ô tô. | editor 2026-10-05 |
| 926 | earn | v | to receive money for work | kiếm được | She earns enough money to support her family. | Cô ấy kiếm đủ tiền để nuôi gia đình. | editor 2026-10-05 |
| 928 | post | n | a message published online | bài đăng | Your post got 500 likes! | Bài đăng của bạn được 500 lượt thích! | editor 2026-10-05 |
| 929 | newspaper | n | a publication that reports news | báo, báo chí | My grandfather reads the newspaper every morning. | Ông tôi đọc báo mỗi sáng. | editor 2026-10-05 |
| 930 | define | v | to explain the meaning of a word | định nghĩa | Can you define this word in simple English? | Bạn có thể định nghĩa từ này bằng tiếng Anh đơn giản không? | editor 2026-10-05 |
| 931 | conclusion | n | an opinion reached after thinking | kết luận | The evidence supports her conclusion about the accident. | Bằng chứng ủng hộ kết luận của cô ấy về vụ tai nạn. | editor 2026-10-05 |
| 932 | clock | n | a device that shows the time | ghi giờ | The clock shows the correct time now. | Chiếc đồng hồ đang chỉ đúng giờ. | editor 2026-10-05 [first] everyday sense |
| 933 | everybody | pron | every person | mọi người | Everybody enjoyed the picnic by the lake. | Mọi người đều thích buổi dã ngoại bên hồ. | editor 2026-10-05 |
| 934 | weekend | n | Saturday and Sunday | cuối tuần | We plan to visit our grandparents this weekend. | Cuối tuần này chúng tôi định thăm ông bà. | editor 2026-10-05 |
| 935 | perform | v | to do an action or activity | biểu diễn | The band performed three new songs. | Ban nhạc biểu diễn ba bài mới. | editor 2026-10-05 |
| 936 | professional | adj | trained and paid for a particular job | (Thuộc) nghề, (thuộc) nghề nghiệp | She gave a professional answer to the client. | Cô ấy đưa ra câu trả lời chuyên nghiệp cho khách hàng. | editor 2026-10-05 |
| 938 | debate | v | to discuss opposing views formally | suy nghĩ, cân nhắc | Students debate the issue during class. | Học sinh tranh luận về vấn đề trong giờ học. | editor 2026-10-05 |
| 939 | memory | n | the ability to remember something | sự nhớ, trí nhớ, ký ức | That song brings back a happy memory. | Bài hát đó gợi lại một ký ức vui. | editor 2026-10-05 |
| 940 | green | adj | having the color of grass | xanh lá, tươi | The gardener planted green beans in spring. | Người làm vườn trồng đậu xanh vào mùa xuân. | editor 2026-10-05 |
| 941 | song | n | a piece of music with words | bài hát | This song is so catchy. | Bài hát này dễ nghe quá. | editor 2026-10-05 |
| 943 | maintain | v | to keep something in good condition | duy trì, bảo dưỡng | Regular checks maintain the machine in good condition. | Kiểm tra thường xuyên giữ cho máy hoạt động tốt. | editor 2026-10-05 |
| 944 | credit | n | praise or approval for an achievement | sự cho vay, cho thiếu, cho chịu | She received credit for solving the problem. | Cô ấy được ghi nhận vì đã giải quyết vấn đề. | editor 2026-10-05 |
| 945 | ring | v | to make a clear sound like a bell | reo, rung chuông | The phone may ring during the meeting. | Điện thoại có thể reo trong cuộc họp. | editor 2026-10-05 [first] everyday sense |
| 947 | dead | adj | not alive or no longer working | đúng vào, ngay vào, thẳng vào | The battery is dead after years of use. | Pin đã hỏng sau nhiều năm sử dụng. | editor 2026-10-05 |
| 948 | afternoon | n | the part of the day after noon | buổi chiều | We play football in the afternoon. | Chúng tôi chơi bóng đá vào buổi chiều. | editor 2026-10-05 |
| 949 | prefer | v | to like one thing better | thích hơn, ưa hơn | I prefer tea when the weather is cold. | Tôi thích trà hơn khi thời tiết lạnh. | editor 2026-10-05 |
| 951 | possibility | n | something that may happen | sự có thể, tình trạng có thể, khả năng | There is a possibility of rain tonight. | Có khả năng tối nay sẽ mưa. | editor 2026-10-05 |
| 952 | direction | n | the way to a place | hướng, chỉ dẫn | The sign points in the direction of the station. | Biển báo chỉ về hướng nhà ga. | editor 2026-10-05 |
| 954 | variety | n | a range of different things | sự đa dạng | The market sells a wide variety of fruit. | Chợ bán nhiều loại trái cây đa dạng. | editor 2026-10-05 |
| 955 | daily | adv | happening every day | hằng ngày | He checks the weather forecast daily. | Anh ấy kiểm tra dự báo thời tiết hằng ngày. | editor 2026-10-05 |
| 957 | screen | n | a surface where images are shown | màn hình | The screen is too small to read. | Màn hình quá nhỏ để đọc. | editor 2026-10-05 |
| 958 | track | v | to follow the progress or location of something | theo dõi (đơn hàng) | You can track your package online. | Bạn có thể theo dõi gói hàng trực tuyến. | editor 2026-10-05 |
| 959 | dance | v | to move to music | nhảy | The children dance to music after school. | Bọn trẻ nhảy theo nhạc sau giờ học. | editor 2026-10-05 |
| 961 | female | adj | being a woman or girl or female animal | cái, mái | The female bird built a nest in the tree. | Con chim mái làm tổ trên cây. | editor 2026-10-05 |
| 962 | responsibility | n | a duty that you are expected to handle | trách nhiệm | It's my responsibility to lock the office. | Khóa văn phòng là trách nhiệm của tôi. | editor 2026-10-05 |
| 963 | original | adj | the first form of something | nguyên bản | Please keep the original document safe. | Vui lòng giữ tài liệu gốc an toàn. | editor 2026-10-05 [first] everyday sense |
| 964 | sister | n | a girl or woman with the same parents | chị, em gái | My sister is studying medicine at university. | Chị gái tôi đang học y khoa ở đại học. | editor 2026-10-05 |
| 965 | rock | n | a hard natural piece of stone | đá | A large rock blocked the narrow path. | Một tảng đá lớn chắn lối đi hẹp. | editor 2026-10-05 |
| 966 | dream | n | a series of images during sleep | giấc mơ, giấc mộng | She had a strange dream last night. | Tối qua cô ấy có một giấc mơ lạ. | editor 2026-10-05 |
| 967 | nor | conj | used after neither to add another negative choice | mà... cũng không, và... không | Neither answer is correct, nor is it complete. | Cả hai câu trả lời đều không đúng và cũng không đầy đủ. | editor 2026-10-05 |
| 968 | university | n | a place for advanced education and research | trường đại học | She studies history at university. | Cô ấy học lịch sử ở đại học. | editor 2026-10-05 |
| 970 | agency | n | an organization that provides a service | cơ quan, đại lý | The agency helps students find language courses. | Cơ quan này giúp sinh viên tìm các khóa học ngôn ngữ. | editor 2026-10-05 |
| 972 | garden | n | a place where plants are grown | vườn | The children planted tomatoes in the garden. | Bọn trẻ trồng cà chua trong vườn. | editor 2026-10-05 |
| 973 | fix | v | to repair something that is broken | sửa chữa | Can you fix the loose handle on this door? | Bạn có thể sửa tay nắm lỏng của cánh cửa này không? | editor 2026-10-05 |
| 974 | ahead | adv | in front or in the future | phía trước | A long road stretches ahead of us. | Một con đường dài trải ra phía trước chúng tôi. | editor 2026-10-05 |
| 975 | cross | v | to go from one side to the other | băng qua | Look both ways before you cross the street. | Hãy nhìn cả hai phía trước khi băng qua đường. | editor 2026-10-05 |
| 977 | candidate | n | a person who seeks a position | ứng viên | The best candidate has ten years of experience. | Ứng viên tốt nhất có mười năm kinh nghiệm. | editor 2026-10-05 |
| 978 | weight | n | how heavy someone or something is | cân nặng | He lost a lot of weight. | Anh ấy giảm nhiều cân. | editor 2026-10-05 |
| 979 | legal | adj | allowed by law | hợp pháp, thuộc pháp luật | The company is facing legal problems. | Công ty đang gặp các vấn đề pháp lý. | editor 2026-10-05 |
| 981 | version | n | a particular form of something | bản dịch | Please download the latest version of the app. | Vui lòng tải phiên bản mới nhất của ứng dụng. | editor 2026-10-05 |
| 982 | conversation | n | an informal talk between people | cuộc trò chuyện | We had a pleasant conversation after dinner. | Chúng tôi có một cuộc trò chuyện dễ chịu sau bữa tối. | editor 2026-10-05 |
| 983 | somebody | pron | some person | một người nào đó | Somebody left an umbrella by the door. | Ai đó đã để một chiếc ô cạnh cửa. | editor 2026-10-05 |
| 984 | pound | v | to hit something hard repeatedly | đập mạnh | The rain pounded against the bedroom window. | Mưa đập mạnh vào cửa sổ phòng ngủ. | editor 2026-10-05 |
| 985 | magazine | n | a publication with articles and pictures | tạp chí | I bought a fashion magazine. | Tôi mua một cuốn tạp chí thời trang. | editor 2026-10-05 |
| 986 | shape | n | the form or outline of something | hình, hình dạng, hình thù | The table has a round shape. | Chiếc bàn có hình tròn. | editor 2026-10-05 |
| 987 | sea | n | a large area of salt water | biển | The fishing boat disappeared beyond the sea mist. | Chiếc thuyền đánh cá biến mất sau màn sương biển. | editor 2026-10-05 |
| 989 | welcome | adj | received gladly | được tiếp đi ân cần, được hoan nghênh | Your advice is always welcome here. | Lời khuyên của bạn luôn được hoan nghênh ở đây. | editor 2026-10-05 |
| 990 | smile | v | to show happiness with your mouth | mỉm cười | She smiled when she saw the birthday cake. | Cô ấy mỉm cười khi nhìn thấy bánh sinh nhật. | editor 2026-10-05 [first] everyday sense |
| 991 | communication | n | the sharing of information | sự giao tiếp | Good communication helps teams solve problems. | Giao tiếp tốt giúp các nhóm giải quyết vấn đề. | editor 2026-10-05 |
| 992 | agent | n | a person who acts for another | tác nhân | The travel agent found us a cheap flight. | Đại lý du lịch tìm cho chúng tôi một chuyến bay rẻ. | editor 2026-10-05 |
| 994 | replace | v | to put one thing in place of another | thay thế, đổi | We will replace the broken item for free. | Chúng tôi sẽ đổi miễn phí món hàng bị hỏng. | editor 2026-10-05 |
| 995 | judge | v | to form an opinion about someone or something | đánh giá | It is unfair to judge people by clothing. | Đánh giá mọi người qua quần áo là không công bằng. | editor 2026-10-05 |
| 997 | suddenly | adv | quickly and unexpectedly | đột nhiên | They've suddenly decided to sell the house. | Họ đột nhiên quyết định bán nhà. | editor 2026-10-05 |
| 998 | generation | n | people born and living at about the same time | thế hệ | Three generations live in this house. | Ba thế hệ sống trong ngôi nhà này. | editor 2026-10-05 |
| 999 | estimate | n | a rough calculation of amount or cost | bản ước tính, báo giá sơ bộ | The builder gave us an estimate for the repairs. | Thợ xây đưa cho chúng tôi bản ước tính chi phí sửa chữa. | editor 2026-10-05 |

Task 1A.1 remains unticked pending owner review of this batch and the final acceptance of ranks through 1000.
