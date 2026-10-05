# Task 1A.1 — batch 1 report

Date: 2026-10-05

## Scope

This is batch 1 of the rejected redo of Task 1A.1 items 3–4. The bulk NGSL top-1000
relabel loop was removed from `tools/gen_content.py`. Only the 184 NGSL entries ranked
1–200 that already exist in the core were curated. No missing NGSL entries were added,
and Task 1A.1 was not ticked.

The per-entry source is `tools/authoring/senses_editor_batch1.json`. Each row is an
entry-specific definition written for this batch, not copied from WordNet. Untouched entries
remain `wordnet-auto`; only these 184 sense-1 rows receive `defSource: editor`.

## Audit before / after

| metric | before batch 1 | after batch 1 |
|---|---:|---:|
| entries | 4,618 | 4,618 |
| gold / silver / bronze | 0 / 898 / 3,720 | 0 / 908 / 3,710 |
| audit errors | 9,219 | 9,158 |
| def_missing | 304 | 276 |
| def_placeholder | 5 | 0 |
| def_unchecked | 3,644 | 4,318 |
| Level 1 silver/gold | 318 | 318 |
| NGSL entries rank 1–200 present / curated | — | 184 / 184 |

Audit was run with `python tools/audit_content.py` before and after. The remaining errors
are outside this batch (examples, topics, IPA and definitions intentionally left for later
curation).

## Changed senses

| word | old def | new def | old gloss | new gloss |
|---|---|---|---|---|
| the | used before a specific person or thing | used before a specific person, thing, or group | cái, người, điều đã xác định | cái, người, điều đã xác định |
| be | have the quality of being; (copula, used with an adjective or a predicate noun) | to exist or have a particular quality or state | là; thì; ở | là, thì, ở |
| and | used to join words or clauses | used to join words, phrases, or clauses | và | và |
| of | used to show belonging or connection | used to show belonging, connection, or description | của | của |
| to | used to show direction or purpose | used to show direction, purpose, or an action | đến, tới, để | đến, tới, để |
| a | used before one non-specific thing | used before one non-specific person or thing | một, một cái nào đó | một, một cái nào đó |
| in | to or toward the inside of | inside or toward the inside of a place | vào | trong, vào trong |
| have | have or possess, either in a concrete or an abstract sense | to own, hold, or experience something | có; sở hữu | có, sở hữu |
| it | the branch of engineering that deals with the use of computers and telecommunications to retrieve and store and transmit information | a thing, animal, or situation already mentioned | tình hình chung, hoàn cảnh, cuộc sống nói chung | nó, điều đó |
| you | the person or people being spoken to | the person or people being spoken to | bạn, các bạn | bạn, các bạn |
| for | during a stated period of time | used to show purpose, benefit, or a period | trong một khoảng thời gian | cho, để, trong khoảng |
| they | two or more people or things | two or more people or things | họ, chúng | họ, chúng |
| not | negation of a word or group of words | used to make a word or statement negative | không | không |
| that | used to point to a specific person or thing | used to point to a specific person or thing | ấy, đó, kia | ấy, đó, kia |
| on | with a forward motion | touching or supported by a surface | trên, ở trên | trên, ở trên |
| with | together with or using | together with someone or something; using something | với, cùng với | với, cùng với |
| this | used to point to a person or thing near you | used to point to a person or thing nearby | điều này, cái này | điều này, cái này |
| i | a nonmetallic element belonging to the halogens; used especially in medicine and photography and in dyes; occurs naturally only in combination in small quantities (as in sea water or rocks) | the person who is speaking or writing | tôi, ta, tao, tớ | tôi, ta |
| do | engage in | to perform an action or activity | làm; thực hiện | làm, thực hiện |
| at | a highly unstable radioactive element (the heaviest of the halogen series); a decay product of uranium and thorium | in or near a particular place or time | ở tại (chỉ vị trí) | ở, tại |
| she | a female person or animal | a female person or animal already mentioned | cô ấy, bà ấy | cô ấy, bà ấy |
| but | and nothing more | used to connect ideas that are different | nhưng | nhưng |
| from | starting at a place, time, or source | starting at a place, time, or source | từ | từ |
| by | so as to pass a given point | near; or before a stated time | gần, cạnh, kề, bên | bởi, bằng, gần |
| will | the capability of conscious choice and decision and intention; - George Meredith | used to talk about a future action | ý chí, chí, ý định, lòng | sẽ |
| or | used to show a choice | used to show a choice or alternative | hoặc, hay | hoặc, hay |
| say | express in words | to express a thought or fact in words | nói (một điều gì) | nói, cho biết |
| go | change location; move, travel, or proceed, also metaphorically | to move or travel to another place | đi | đi |
| so | to a very great extent or degree | to such a degree; therefore | như thế, như vậy | vì vậy, đến mức |
| all | quantifier; used with either mass or count nouns to indicate the whole number or amount of or every one of a class | the whole amount or every person or thing | tất cả, hết thảy, toàn bộ, suốt trọn, mọi | tất cả, toàn bộ |
| if | used to talk about a condition | used to talk about a possible condition | nếu | nếu |
| one | the smallest whole number or a numeral representing this number | the number 1; a single person or thing | số một | một, số một |
| can | preserve in a can or tin | to be able to do something | có thể; có khả năng | có thể, có khả năng |
| there | in or at that place | in, at, or to that place | ở đó, tại đó, chỗ đó, chỗ ấy, đấy | ở đó, tại đó |
| know | be cognizant or aware of a fact or a specific piece of information; possess knowledge or information about | to have information or understanding about something | biết | biết |
| more | (comparative of `much' used with mass nouns) a quantifier meaning greater in size or amount or extent or degree | a greater amount, number, or degree | nhiều hơn, lớn hơn, đông hơn | nhiều hơn |
| get | come into the possession of something concrete or abstract | to receive, obtain, or become something | nhận; lấy được | nhận, lấy được, trở nên |
| like | prefer or wish to do something | to enjoy or prefer something | thích | thích |
| when | at or during the time that | at or during the time that | khi | khi |
| think | judge or regard; look upon; judge | to have an idea or opinion | nghĩ; cho rằng | nghĩ, cho rằng |
| make | engage in | to create, produce, or cause something | làm; tạo ra | làm, tạo ra |
| time | an instance or single occasion for some event | a period or moment when something happens | thời gian, thì giờ | thời gian, lúc |
| see | perceive by sight or have the power to perceive by sight | to notice someone or something with your eyes | nhìn thấy | nhìn thấy |
| what | used to ask about a thing or fact | used to ask about a thing or fact | gì, điều gì | gì, điều gì |
| up | spatially or metaphorically from a lower to a higher position | toward a higher place or position | ở trên, lên trên, lên | lên, ở trên |
| some | quantifier; used with either mass nouns or plural count nouns to indicate an unspecified number or quantity | an amount or number that is not exact | nào đó | một vài, một số |
| other | not the same one or ones already mentioned or implied; - the White Queen | different from the person or thing mentioned | khác | khác |
| out | away from home | away from a place or inside space | ngoài, ở ngoài, ra ngoài | ra ngoài, ở ngoài |
| good | having desirable or positive qualities especially those suitable for a thing specified | having positive qualities or being satisfactory | tốt; hay | tốt, hay |
| people | (plural) any group of human beings (men or women or children) collectively | human beings considered as a group | nhân dân, dân chúng, quần chúng | mọi người, người ta |
| year | a period of time containing 365 (or 366) days | a period of twelve months | năm | năm |
| take | carry out | to carry or move something with you | lấy; mang theo | lấy, mang theo |
| well | (often used as a combining form) in a good or proper or satisfactory manner or to a high standard (`good' is a nonstandard dialectal variant for `well') | in a good, proper, or satisfactory way | tốt, giỏi, hay | tốt, giỏi |
| because | for the reason that | for the reason that | bởi vì | bởi vì |
| very | precisely as stated | to a high degree; extremely | thực, thực sự | rất, thực sự |
| just | and nothing more | only; exactly; or very recently | đúng, chính | chỉ, vừa mới, đúng |
| come | move toward, travel toward something or somebody or approach something or somebody | to move toward or arrive at a place | đến | đến |
| work | activity directed toward making or doing something | an activity done to achieve a result | công việc; việc cần làm (không đếm được) | công việc, làm việc |
| use | put into service; make work or employ for a particular purpose or for its inherent or natural purpose | to employ something for a purpose | sử dụng | sử dụng |
| than | used to compare two things | used to compare two people or things | hơn, dùng khi so sánh | hơn, so với |
| now | in the historical present; at this point in the narration of a series of past events | at the present time | bây giờ, lúc này, giờ đây, hiện nay, ngày nay | bây giờ, hiện nay |
| then | subsequently or soon afterward (often used as sentence connectors) | at that time or after that | sau đó; lúc đó | lúc đó, sau đó |
| also | in addition | in addition; too | cũng; ngoài ra | cũng, ngoài ra |
| into | to the inside of a place or thing | to the inside of a place or thing | vào, vào trong | vào, vào trong |
| only | being the only one; single and isolated from others | and no one or nothing else | chỉ có một, duy nhất | chỉ, duy nhất |
| look | perceive with attention; direct one's gaze towards | to direct your eyes toward something | nhìn | nhìn |
| want | feel or have a desire for; want strongly | to desire or feel a need for something | muốn | muốn |
| give | cause to have, in the abstract sense or physical sense | to provide something to someone | cho; đưa | cho, đưa |
| first | the first or highest in an ordering or series | coming before all others in time or order | người đầu tiên, người thứ nhất; vật đầu tiên, vật thứ nhất | đầu tiên, thứ nhất |
| new | not of long duration; having just (or relatively recently) come into being or been made or acquired or discovered | recently made, found, or experienced | mới | mới |
| way | a method or direction for doing something | a method, path, or direction | cách, đường, lối | cách, đường, lối |
| find | come upon, as if by accident; meet with | to discover or locate someone or something | tìm thấy | tìm thấy |
| over | (cricket) the division of play during which six balls are bowled at the batsman by one player from the other team from the same end of the pitch | above or across something | trên; ở trên | trên, qua |
| any | one or some or every or all without specification | one or some, without a specific choice | một, một (người, vật) nào đó (trong câu hỏi) | bất kỳ, nào |
| after | happening at a time subsequent to a reference time | later than a particular time or event | sau | sau |
| day | time for Earth to make a complete rotation on its axis | a period of twenty-four hours | ngày | ngày |
| where | in or at what place | in or at what place | ở đâu, ở nơi nào | ở đâu, nơi nào |
| thing | a special situation | an object, idea, event, or matter | vấn đề, điều, công việc, sự việc, chuyện | điều, vật, việc |
| should | used to give advice or show duty | used to give advice or show duty | nên, phải | nên, phải |
| need | require as useful, just, or proper | to require something because it is necessary | cần | cần |
| much | (quantifier used with mass nouns) great in quantity or degree or extent | a large amount or degree | nhiều, lắm | nhiều, lắm |
| right | correct or suitable | correct, suitable, or true | đúng, chính xác | đúng, chính xác |
| how | in what way | in what way or manner | như thế nào, bằng cách nào | như thế nào, bằng cách nào |
| back | the posterior part of a human (or animal) body from the neck to the end of the spine | the rear part of a body or thing | lưng | lưng, phía sau |
| mean | approximating the statistical norm or average or expected value | to have a particular meaning or intention | trung bình, vừa, ở giữa | có nghĩa, có ý |
| even | used as an intensive especially to indicate something unexpected | used to emphasize something surprising or unexpected | ngay cả, ngay, thậm chí | ngay cả, thậm chí |
| may | used to show possibility or permission | used to show possibility or permission | có thể, có lẽ | có thể, có lẽ |
| here | in or at this place; where the speaker or writer is | in or at this place | đây, ở đây, ở chỗ này | đây, ở đây |
| many | a quantifier that can be used with count nouns and is often preceded by `as' or `too' or `so' or `that'; amounting to a large but indefinite number | a large but not exact number | nhiều, lắm | nhiều |
| such | of so extreme a degree or extent | of the kind or degree mentioned | như thế, như vậy, như loại đó | như vậy, như thế |
| last | the temporal end; the concluding time | coming after all others in time or order | người cuối cùng, người sau cùng | cuối cùng, sau cùng |
| child | a young person of either sex | a young person who is not an adult | kết quả, hậu quả, sản phẩm | trẻ em, đứa trẻ |
| tell | express in words | to give information or instructions in words | nói cho ai biết; kể | nói cho biết, kể |
| really | in accordance with truth or fact or reality | in fact or to a high degree | thực sự; rất | thực sự, rất |
| call | assign a specified (usually proper) proper name to | to contact someone by phone or voice | gọi; gọi điện | gọi, gọi điện |
| before | earlier in time; previously | earlier than a particular time or event | trước | trước |
| company | an institution created to conduct business | a business organization | sự cùng đi; sự cùng ở; sự có bầu có bạn | công ty |
| through | from beginning to end | from one side or end to another | qua, xuyên qua, suốt | qua, xuyên qua |
| down | spatially or metaphorically from a higher to a lower level or position | toward a lower place or position | xuống | xuống |
| show | give an exhibition of to an interested audience | to let someone see or understand something | cho xem; chỉ cho | cho xem, chỉ cho |
| life | a characteristic state or mode of living | the state of being alive; a person's existence | đời sống, sinh mệnh, tính mệnh | cuộc sống, sự sống |
| man | an adult male person | an adult male person | người đàn ông | người đàn ông |
| change | cause to change; make different; cause a transformation | to make someone or something different | thay đổi | thay đổi |
| place | put into a certain place or abstract location | to put something somewhere | để, đặt | đặt, để |
| long | primarily temporal sense; being or indicating a relatively great or greater than average duration or passage of time or a duration as specified | continuing for a large amount of time or distance | dài | dài, lâu |
| between | in the interval | in the space separating two people or things | ở giữa (hai) | ở giữa hai |
| feel | an intuitive awareness;  or | to experience an emotion or physical sensation | sự sờ mó | cảm thấy |
| too | to a degree exceeding normal or proper limits | more than is wanted or needed; also | quá | quá, cũng |
| still | with reference to action or condition; without change, interruption, or cessation | continuing until a particular time | vẫn; còn | vẫn, còn |
| problem | a state of difficulty that needs to be resolved | a difficulty that needs an answer or solution | vấn đề | vấn đề |
| write | produce a literary work | to form words with a pen or keyboard | viết | viết |
| same | same in identity | not different; exactly alike | giống nhau; cùng một | giống nhau, cùng một |
| lot | (often followed by `of') a large number or amount or extent | a large amount or number | thăm, việc rút thăm; sự chọn bằng cách rút thăm | nhiều, một lượng lớn |
| great | relatively large in size or number or extent; larger than others of its kind | large, important, or very good | lớn, to lớn, vĩ đại | lớn, tuyệt vời |
| try | make an effort or attempt | to make an effort to do something | thử; cố gắng | thử, cố gắng |
| leave | the period of time during which you are absent from work or duty | to go away from a place or person | kỳ nghỉ phép | rời đi, để lại |
| number | the property possessed by a sum or total or indefinite quantity of units or individuals | a symbol or amount used for counting | số | số |
| both | the two people or things together | the two people or things together | cả hai | cả hai |
| own | have ownership or possession of | to have something as your property | sở hữu | sở hữu |
| part | in part; in some degree; not wholly | a piece or section of something | một phần | phần |
| point | to direct toward a place or person | to direct attention toward something | hướng về | chỉ, hướng về |
| little | limited or below average in number or quantity or magnitude or extent | small in amount, size, or degree | nhỏ bé, bé bỏng | ít, nhỏ |
| help | give help or assistance; be of service | to make something easier for someone | giúp đỡ | giúp đỡ |
| ask | inquire about | to request information or something from someone | hỏi; yêu cầu | hỏi, yêu cầu |
| meet | come together | to come together with someone | gặp; gặp gỡ | gặp, gặp gỡ |
| start | take the first step or steps in carrying out an action | to begin doing something | bắt đầu | bắt đầu |
| talk | exchange thoughts; talk with | to speak with someone about something | nói chuyện; trò chuyện | nói chuyện, trò chuyện |
| something | an unspecified thing | an unspecified thing or matter | một điều gì đó | một điều gì đó |
| put | to move something to a place | to move something to a particular place | đặt, để | đặt, để |
| another | any of various alternatives; some other | one more person or thing of the same kind | khác | một người hoặc vật khác |
| become | enter or assume a certain state or condition | to begin to be something | trở nên, trở thành | trở nên, trở thành |
| interest | a sense of concern with and curiosity about someone or something | a feeling of wanting to know or learn more | tiền lãi; sự quan tâm | sự quan tâm, hứng thú |
| country | a politically organized body of people under a single government | a nation with its own government | số ít vùng, xứ, miền; (nghĩa bóng) địa hạt, lĩnh vực | đất nước, quốc gia |
| old | (used especially of persons) having lived for a relatively long time or attained a specific age | having existed or lived for a long time | cũ; già | cũ, già |
| each | (used of count nouns) every one considered individually | every one considered separately | mỗi | mỗi |
| school | an educational institution | a place where people learn | trường học | trường học |
| late | later than usual or than expected | after the expected or usual time | muộn; trễ | muộn, trễ |
| high | greater than normal in degree or intensity or amount | far above the ground or usual level | cao | cao |
| different | unlike in nature or quality or form or degree | not the same as another person or thing | khác; khác nhau | khác, khác nhau |
| next | immediately following in time or order | coming immediately after in time or order | sát, gần, ngay bên, bên cạnh | tiếp theo, kế bên |
| live | actually being performed at the time of hearing or viewing | happening now in front of an audience | trực tiếp | trực tiếp |
| why | for what reason | for what reason | tại sao, vì sao | tại sao, vì sao |
| while | during the time that | during the time that something happens | trong khi | trong khi |
| world | everything that exists anywhere | the earth and all its people and places | thế giới, hoàn cầu, địa cầu | thế giới |
| week | any period of seven consecutive days | a period of seven days | tuần | tuần |
| might | physical strength | used to show possibility | sức mạnh, lực (thân thể hoặc tinh thần) | có thể |
| must | used to show necessity | used to show necessity or strong certainty | phải, cần phải | phải, cần phải |
| home | where you live at a particular time | the place where a person lives | nhà; nơi ở | nhà, nơi ở |
| never | not ever; at no time in the past or future | not at any time | không bao giờ | không bao giờ |
| course | education imparted in a series of lessons or meetings | a series of lessons about a subject | khóa học | khóa học |
| house | a dwelling that serves as living quarters for one or more families | a building where people live | ngôi nhà | ngôi nhà |
| report | a written document describing the findings of some individual or group | a written account of facts or findings | báo cáo; bản tin | báo cáo |
| group | any number of entities (members) considered as a unit | a number of people or things together | nhóm | nhóm |
| case | an occurrence of something | an example or situation of a particular kind | trường hợp, cảnh ngộ, hoàn cảnh, tình thế | trường hợp, tình huống |
| woman | an adult female person (as opposed to a man) | an adult female person | đàn bà, phụ nữ | phụ nữ, người phụ nữ |
| around | in the area or vicinity | in a circle or surrounding an area | xung quanh, vòng quanh | xung quanh, vòng quanh |
| book | engage for a performance | to reserve a place, room, or service | đặt (vé, phòng) | đặt chỗ, đặt phòng |
| family | a social unit living together | a group of related people | gia đình, gia quyến | gia đình |
| seem | give a certain impression or have a certain outward aspect | to give a particular impression | có vẻ như, dường như, coi bộ | có vẻ, dường như |
| let | make it possible through a specific action or lack of action for something to happen | to allow something to happen | để cho, cho phép | để cho, cho phép |
| again | anew | one more time | trở lại | lại, lần nữa |
| kind | having or showing a tender and considerate and helpful nature; used especially of persons and their behavior | friendly, helpful, and considerate | tốt bụng; tử tế | tử tế, tốt bụng |
| keep | keep in a certain state, position, or activity; e.g., | to continue to have or hold something | giữ; giữ lại | giữ, giữ lại |
| hear | perceive (sound) via the auditory sense | to notice a sound with your ears | nghe thấy | nghe thấy |
| system | instrumentality that combines interrelated interacting artifacts designed to work as a coherent entity | a set of connected parts working together | hệ thống; chế độ | hệ thống |
| question | an instance of questioning | a sentence used to ask for information | câu hỏi | câu hỏi |
| during | throughout a period of time | throughout a period of time | trong suốt, trong thời gian | trong suốt, trong thời gian |
| always | at all times; all the time and on every occasion | at every time or on every occasion | luôn luôn | luôn luôn |
| big | above average in size or number or quantity or magnitude or extent | large in size, amount, or importance | to; lớn | to, lớn |
| set | a group of things of the same kind that belong together and are so used | a group of things that belong together | bộ | bộ, tập hợp |
| small | limited or below average in number or quantity or magnitude or extent | limited in size, amount, or degree | nhỏ; bé | nhỏ, bé |
| study | consider in detail and subject to an analysis in order to discover essential features or meaning | to learn about a subject | học; nghiên cứu | học, nghiên cứu |
| follow | to practice a trade or profession | to go or come after someone | theo nghề, làm nghề | đi theo |
| important | of great significance or value | having great value or significance | quan trọng | quan trọng |
| since | from a time in the past until now | from a past time until now | từ, kể từ | từ, kể từ |
| run | move fast by using one's feet, with one foot off the ground at any given time | to move quickly on your feet | chạy | chạy |
| under | down to defeat, death, or ruin | below or lower than something | dưới, ở dưới | dưới, ở dưới |
| turn | a circular segment of a curve | to move around a central point | sự quay; vòng quay | quay, xoay |
| few | a quantifier that can be used with count nouns and is often preceded by `a'; a small but indefinite number | a small number of people or things | ít vài | ít, vài |
| bring | take something or somebody with oneself somewhere | to carry something to a person or place | mang đến | mang đến |
| early | during an early stage | before the usual or expected time | sớm | sớm |
| hand | the (prehensile) extremity of the superior limb | the body part at the end of an arm | tay, bàn tay (người); bàn chân trước (loài vật bốn chân) | bàn tay |
| state | the territory occupied by one of the constituent administrative districts of a nation | the condition someone or something is in | trạng thái, tình trạng | trạng thái, tình trạng |
| move | change location; move, travel, or proceed, also metaphorically | to change position or place | di chuyển; chuyển nhà | di chuyển, chuyển đi |

