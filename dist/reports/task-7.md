# Task 7 — Spanish pilot (Level 1)

## Phạm vi

- Locale: `es`, dùng văn phong tiếng Tây Ban Nha trung tính cho người học Mỹ Latinh (`es-419`); không thêm biến thể `es-ES`.
- Không dịch lại nội dung tiếng Anh đang được học: stem, lựa chọn đáp án, công thức và các token giữ nguyên.
- Không dùng endpoint dịch không chính thức. Các lô trong `content/review/es/manual_*.csv` được viết và rà thủ công trước khi import.
- Trạng thái dừng ở `reviewed`, chưa `approved`; cần native reviewer duyệt trước khi build UI locale để phát hành.

## Phạm vi đã rà thủ công

| Thành phần | Đã rà | Ghi chú |
|---|---:|---|
| Nghĩa từ vựng Level 1 | 1.321/1.321 | Gloss và toàn bộ ví dụ đối chiếu với source ID; không đổi English core |
| Grammar Level 1 | 8/8 | Dịch title/when/body/examples; lỗi được viết lại theo lỗi thường gặp của người nói tiếng Tây Ban Nha |
| Câu hỏi Level 1 | 112/112 | Chỉ dịch `expl`; stem và options vẫn là tiếng Anh |
| Topics | 47/47 | Tên chủ đề nhất quán |
| Confusables | 32/32 | Đã viết lại `tip` và từng note; tên từ tiếng Anh giữ nguyên |
| UI | 728/728 | Placeholder `%1$s`, `%1$d`, `%2$d`, `%3$02d`, `{name}` được giữ nguyên |
| Tips | 36/36 | Đã dịch thủ công, gồm các mẹo TOEIC Part 1–7 |
| Pet lines | 144/144 | 18 trạng thái × 8 câu; giữ `{name}` khi source có |

Passages không thuộc Level 1 (33 level 3, 6 level 4, 1 level 2) nên không được đánh dấu trong pilot này.
English source không có `tip` cho từng sense từ Level 1; không tự bịa thêm nội dung vào field đó.

## Các mục REWRITE đã viết

### `l1_notes.json`

- False friends: `actually/actualmente`, `embarrassed/embarazada`, `library/librería`, `sensible/sensible`, `assist/asistir`, `carpet/carpeta`, `exit/éxito`, `realize/realizar`, `attend/atender`.
- Pronunciation: b/v; `ship/sheep`; nguyên âm đệm đầu từ (`espeak`, `estudent`); phụ âm cuối; đuôi `-ed`; `j/y`; chữ `h` trong tiếng Anh không câm theo quy tắc tiếng Tây Ban Nha.
- Grammar: chủ ngữ bị lược (`Is raining`); nói tuổi (`I have 20 years`); tính từ sau danh từ; phủ định kép; `people is`; mạo từ với danh từ chung (`the life is hard`); phân biệt `make/do` khi đều có thể dịch là “hacer”.

### `confusables.json`

Đã viết lại đầy đủ 32 bộ: `affect_effect`, `borrow_lend`, `say_tell`, `make_do`, `lose_loose`, `job_work`, `fun_funny`, `bored_boring`, `hear_listen`, `look_see_watch`, `rise_raise`, `among_between`, `since_for`, `during_while`, `although_despite`, `because_because_of`, `economic_economical`, `advice_advise`, `remember_remind`, `check_control`, `customer_client_guest`, `salary_wage`, `price_prize`, `job_career`, `travel_trip_journey`, `its_its`, `than_then`, `especially_specially`, `hard_hardly`, `late_lately`, `alone_lonely`, `excited_exciting`.

### Grammar mistakes

Đã viết lại 16 lý do lỗi của 8 điểm Level 1: `be`, `present_simple`, `articles`, `plurals`, `prep_time`, `there_is`, `can_could`, `word_order` (mỗi điểm 2 lỗi), và giữ câu sai/câu đúng tiếng Anh để người học nhận diện mẫu thật.

## Kiểm tra

- `python tools/locale.py check es` → `errors: 0`.
- Đối chiếu thủ công + script: 1.321 gloss có nội dung, mỗi tập ví dụ khớp đúng source ID, 8 grammar/112 questions/47 topics/32 confusables/728 UI đều có `reviewed`.
- `python tools/validate_content.py` → `errors=0`, `warnings=7899`. Cảnh báo còn lại thuộc nội dung ngoài Level 1 chưa dịch trong pilot; không có lỗi cấu trúc locale.
- `python -m unittest discover -s tools/tests` → 72 tests, OK.
- Đã xuất 5 lô cho native reviewer: `content/review/es/batch_001.csv` … `batch_005.csv` với `--level 1 --status reviewed`.

## Chưa làm trong Task 7

Native review/approval chưa được thực hiện. Không chạy `build-ui es` vì công cụ yêu cầu toàn bộ UI ở trạng thái `approved`; đây là chủ ý để không phát hành bản dịch chưa được native reviewer duyệt.
