# Task 1A.1 report — Task 1A regressions

Date: 2026-10-05

## Changes

- `ContentRepository` now reads `q_translation`, `q_fix` and `q_notes` from the Vietnamese
  locale pack. It no longer expects moved Vietnamese fields in the English question object.
- Added a JVM unit test covering all 62 E05 translations and all 85 E04 fixes.
- Added `defSource` to every sense: `editor`, `wordnet-auto`, or `template`.
- Removed all 309 generated definition templates by leaving those definitions empty for later
  editorial work; no placeholder definition is shipped.
- Curated the NGSL top-1000 sense-1 definitions below and aligned their Vietnamese glosses.
- Kept the previously accepted audit-tool change untouched. `tools/audit_content.py` and the
  content standard were not modified in this task.

## NGSL top-1000 sense-1 changes

The following entries changed from the old placeholder/incorrect definition to an editor definition.
For rows marked `—`, the old definition was the removed generic placeholder.

| word | old def | new def | vi gloss |
|---|---|---|---|
| style | — | a way of doing or presenting something | phong cách |
| both | — | the two people or things together | cả hai |
| fun | — | pleasure or enjoyment | vui, thú vị |
| and | — | used to join words or clauses | và |
| or | — | used to show a choice | hoặc, hay |
| because | — | for the reason that | bởi vì |
| although | — | despite the fact that | mặc dù |
| despite | — | without being stopped by something | mặc dù, bất chấp |
| if | — | used to talk about a condition | nếu |
| when | — | at or during the time that | khi |
| while | — | during the time that | trong khi |
| during | — | throughout a period of time | trong suốt, trong thời gian |
| since | — | from a time in the past until now | từ, kể từ |
| for | — | during a stated period of time | trong một khoảng thời gian |
| until | — | up to the time that | cho đến |
| among | — | in or surrounded by a group | giữa, trong số |
| than | — | used to compare two things | hơn, dùng khi so sánh |
| either | — | one or the other of two | một trong hai |
| whether | — | used to show two possible choices | liệu, có hay không |
| may | — | used to show possibility or permission | có thể, có lẽ |
| must | — | used to show necessity | phải, cần phải |
| shall | — | used to talk about future or duty | sẽ |
| the | — | used before a specific person or thing | cái, người, điều đã xác định |
| this | — | used to point to a person or thing near you | điều này, cái này |
| where | — | in or at what place | ở đâu, ở nơi nào |
| how | — | in what way | như thế nào, bằng cách nào |
| why | — | for what reason | tại sao, vì sao |
| yes | — | used to agree or answer positively | vâng, phải, được |
| else | — | in addition or otherwise | khác, nữa |
| front | — | the part or side that faces forward | phía trước |
| she | — | a female person or animal | cô ấy, bà ấy |
| into | — | to the inside of a place or thing | vào, vào trong |
| way | — | a method or direction for doing something | cách, đường, lối |
| anything | — | any thing or object | bất cứ điều gì |
| per | — | for each | mỗi |
| everybody | — | every person | mọi người |
| of | — | used to show belonging or connection | của |
| to | — | used to show direction or purpose | đến, tới, để |
| a | — | used before one non-specific thing | một, một cái nào đó |
| you | — | the person or people being spoken to | bạn, các bạn |
| that | — | used to point to a specific person or thing | ấy, đó, kia |
| with | — | together with or using | với, cùng với |
| from | — | starting at a place, time, or source | từ |
| what | — | used to ask about a thing or fact | gì, điều gì |
| man | — | an adult male person | người đàn ông |
| without | — | not having or using something | không, không có |
| against | — | opposed to or touching something | chống lại, ngược lại |
| toward | — | in the direction of something | về phía, hướng về |
| everything | — | all things or people | mọi thứ |
| upon | — | on or immediately after | trên, ngay sau khi |
| something | — | an unspecified thing | một điều gì đó |
| should | — | used to give advice or show duty | nên, phải |
| they | — | two or more people or things | họ, chúng |
| nor | — | and not; used after neither or not | mà cũng không, và không |
| bank | tip laterally | a business that keeps and lends money | ngân hàng |
| post | the position where someone (as a guard or sentry) stands or is assigned to stand | a message published online | bài đăng |
| fine | an expression of agreement normally occurring at the beginning of a sentence | good or of high quality | hay, khéo |
| right | being or located on or directed toward the side of the body to the east when facing north | correct or suitable | đúng, chính xác |
| put | put into a certain place or abstract location | to move something to a place | đặt, để |
| follow | to travel behind, go after, come after | to practice a trade or profession | theo nghề, làm nghề |
| point | indicate a place, direction, person, or thing; either spatially or figuratively | to direct toward a place or person | hướng về |

The known `silk` and `sensible` mismatches were also corrected, but neither is in the NGSL
top-1000 list; they are included in the generator's reviewed override table for consistency.

## Audit before / after

| metric | before Task 1A.1 | after Task 1A.1 |
|---|---:|---:|
| entries | 4,618 | 4,618 |
| gold / silver / bronze | 0 / 898 / 3,720 | 0 / 917 / 3,701 |
| audit errors (all categories) | 9,219 | 9,151 |
| `def_placeholder` | 5 | 0 |
| `def_missing` | 304 | 255 |
| `defSource` coverage | 0 before Task 1A.1 | 4,778 / 4,778 senses |
| non-English strings in `content/en` | 0 | 0 |
| E05 translations in vi pack | 62 / 62 | 62 / 62 |
| E04 fixes in vi pack | 11 / 85 | 85 / 85 |
| Level 1 silver/gold words | 318 | 326 |

The remaining audit errors are existing content work outside this task, primarily fragment
examples, catch-all topics, gloss quality and IPA. They were not hidden by changing the audit.

## Verification

- `python tools/audit_content.py` before and after — completed; summary above.
- `python tools/validate_content.py` — `errors=0 warnings=0`.
- `python tools/gen_content.py --check` — passed.
- `./gradlew testDebugUnitTest` — passed.
- `./gradlew check assembleDebug` — passed.
- Debug APK: 10,381,612 bytes (~9.90 MiB).

Task 1A.1 is ticked. No later task was started.
