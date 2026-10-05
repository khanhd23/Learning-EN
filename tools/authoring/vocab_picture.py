"""Small, human-readable picture vocabulary pack for children and visual lessons.

These are concrete nouns and a few visible actions. They are intentionally separate from the
large dictionary import so editors can review the picture choice and translation as a unit.
"""
import gzip
import json
from pathlib import Path

import lib

ROOT = Path(__file__).resolve().parents[2]
SOURCE = ROOT / "content/datasets/en_vi_master_15k_tagged.jsonl.gz"

# lemma, Vietnamese gloss, simple English example, Vietnamese example
FRUITS = [
    ("apple", "quả táo", "The apple is red.", "Quả táo màu đỏ."),
    ("orange", "quả cam; màu cam", "I eat an orange.", "Tôi ăn một quả cam."),
    ("mango", "quả xoài", "This mango is sweet.", "Quả xoài này ngọt."),
    ("grape", "quả nho", "She washed the grapes.", "Cô ấy rửa nho."),
    ("pineapple", "quả dứa/thơm", "The pineapple has a rough skin.", "Quả dứa có vỏ sần."),
    ("watermelon", "dưa hấu", "We shared a watermelon.", "Chúng tôi cùng ăn một quả dưa hấu."),
    ("melon", "dưa", "The melon is cool and juicy.", "Quả dưa mát và mọng nước."),
    ("peach", "quả đào", "The peach is soft.", "Quả đào mềm."),
    ("pear", "quả lê", "He cut a pear in half.", "Cậu ấy cắt đôi quả lê."),
    ("plum", "quả mận", "The plum is purple.", "Quả mận màu tím."),
    ("cherry", "quả anh đào", "There is a cherry on the cake.", "Có một quả anh đào trên bánh."),
    ("coconut", "quả dừa", "We drank coconut water.", "Chúng tôi uống nước dừa."),
    ("avocado", "quả bơ", "The avocado is green inside.", "Quả bơ bên trong màu xanh."),
    ("papaya", "quả đu đủ", "The papaya is ripe.", "Quả đu đủ đã chín."),
    ("guava", "quả ổi", "This guava smells fresh.", "Quả ổi này có mùi tươi."),
    ("kiwi", "quả kiwi", "The kiwi has brown skin.", "Quả kiwi có vỏ màu nâu."),
    ("lemon", "quả chanh vàng", "Add a slice of lemon.", "Thêm một lát chanh vàng."),
    ("lime", "quả chanh xanh", "She squeezed a lime.", "Cô ấy vắt một quả chanh xanh."),
    ("grapefruit", "bưởi chùm", "The grapefruit tastes bitter.", "Quả bưởi chùm có vị đắng."),
    ("pomegranate", "quả lựu", "The pomegranate has many seeds.", "Quả lựu có nhiều hạt."),
    ("fig", "quả sung", "A fig is a soft fruit.", "Quả sung là một loại quả mềm."),
    ("date", "quả chà là", "He added dates to the bowl.", "Cậu ấy cho chà là vào bát."),
    ("apricot", "quả mơ", "The apricot is orange.", "Quả mơ có màu cam."),
    ("raspberry", "quả mâm xôi đỏ", "The rabbit likes raspberries.", "Con thỏ thích quả mâm xôi đỏ."),
    ("blueberry", "quả việt quất", "Blueberries are small and round.", "Quả việt quất nhỏ và tròn."),
    ("cranberry", "quả nam việt quất", "Cranberries are red berries.", "Nam việt quất là những quả mọng màu đỏ."),
    ("mulberry", "quả dâu tằm", "The mulberry is dark purple.", "Quả dâu tằm màu tím sẫm."),
    ("passion fruit", "quả chanh dây", "Passion fruit has many seeds.", "Quả chanh dây có nhiều hạt."),
    ("star fruit", "quả khế", "The star fruit has a star shape.", "Quả khế có hình ngôi sao."),
    ("dragon fruit", "quả thanh long", "Dragon fruit has pink skin.", "Quả thanh long có vỏ màu hồng."),
]

ANIMALS = [
    ("ant", "con kiến", "The ant is carrying food.", "Con kiến đang mang thức ăn."),
    ("bee", "con ong", "The bee is on the flower.", "Con ong đang ở trên bông hoa."),
    ("butterfly", "con bướm", "A butterfly has colorful wings.", "Con bướm có đôi cánh nhiều màu."),
    ("spider", "con nhện", "The spider is on the wall.", "Con nhện ở trên tường."),
    ("snake", "con rắn", "The snake is long.", "Con rắn dài."),
    ("lizard", "con thằn lằn", "The lizard is on a rock.", "Con thằn lằn ở trên tảng đá."),
    ("frog", "con ếch", "The frog can jump.", "Con ếch có thể nhảy."),
    ("toad", "con cóc", "The toad is near the pond.", "Con cóc ở gần ao."),
    ("turtle", "con rùa", "The turtle moves slowly.", "Con rùa di chuyển chậm."),
    ("crocodile", "con cá sấu", "The crocodile lives near water.", "Cá sấu sống gần nước."),
    ("lizard", "con thằn lằn", "The lizard is small.", "Con thằn lằn nhỏ."),
    ("whale", "cá voi", "The whale is very large.", "Cá voi rất lớn."),
    ("shark", "cá mập", "The shark swims in the sea.", "Cá mập bơi trong biển."),
    ("dolphin", "cá heo", "The dolphin jumps out of the water.", "Cá heo nhảy lên khỏi mặt nước."),
    ("octopus", "bạch tuộc", "The octopus has eight arms.", "Bạch tuộc có tám xúc tu."),
    ("crab", "con cua", "The crab walks sideways.", "Con cua đi ngang."),
    ("shrimp", "con tôm", "The shrimp is in the net.", "Con tôm ở trong lưới."),
    ("seal", "hải cẩu", "The seal is resting on the ice.", "Hải cẩu đang nghỉ trên băng."),
    ("penguin", "chim cánh cụt", "The penguin cannot fly.", "Chim cánh cụt không thể bay."),
    ("eagle", "đại bàng", "The eagle has wide wings.", "Đại bàng có đôi cánh rộng."),
    ("owl", "con cú", "The owl can see at night.", "Con cú có thể nhìn ban đêm."),
    ("parrot", "con vẹt", "The parrot can copy sounds.", "Con vẹt có thể bắt chước âm thanh."),
    ("swan", "thiên nga", "The swan is on the lake.", "Thiên nga ở trên hồ."),
    ("peacock", "con công", "The peacock has a beautiful tail.", "Con công có chiếc đuôi đẹp."),
    ("rooster", "gà trống", "The rooster crows in the morning.", "Gà trống gáy vào buổi sáng."),
    ("duckling", "vịt con", "The duckling follows its mother.", "Vịt con đi theo mẹ."),
    ("goat", "con dê", "The goat eats grass.", "Con dê ăn cỏ."),
    ("sheep", "con cừu", "The sheep has thick wool.", "Con cừu có bộ lông dày."),
    ("pig", "con lợn/heo", "The pig is in the field.", "Con lợn ở ngoài đồng."),
    ("donkey", "con lừa", "The donkey carries a bag.", "Con lừa mang một chiếc túi."),
    ("deer", "con hươu", "The deer is in the forest.", "Con hươu ở trong rừng."),
    ("bear", "con gấu", "The bear is looking for food.", "Con gấu đang tìm thức ăn."),
    ("lion", "sư tử", "The lion has a long mane.", "Sư tử có bờm dài."),
    ("zebra", "ngựa vằn", "The zebra has black and white stripes.", "Ngựa vằn có sọc đen trắng."),
    ("giraffe", "hươu cao cổ", "The giraffe has a long neck.", "Hươu cao cổ có cổ dài."),
    ("elephant", "con voi", "The elephant has a long trunk.", "Con voi có vòi dài."),
    ("rhinoceros", "tê giác", "The rhinoceros has thick skin.", "Tê giác có da dày."),
    ("hippopotamus", "hà mã", "The hippopotamus is in the river.", "Hà mã ở dưới sông."),
    ("kangaroo", "chuột túi", "The kangaroo can jump far.", "Chuột túi có thể nhảy xa."),
    ("panda", "gấu trúc", "The panda eats bamboo.", "Gấu trúc ăn tre."),
    ("monkey", "con khỉ", "The monkey climbs a tree.", "Con khỉ trèo cây."),
    ("wolf", "con sói", "The wolf lives in a group.", "Con sói sống theo bầy."),
]

OBJECTS = [
    ("backpack", "ba lô", "My books are in my backpack.", "Sách của tôi ở trong ba lô."),
    ("eraser", "cục tẩy", "Use an eraser to remove the mark.", "Dùng cục tẩy để xóa nét đánh dấu."),
    ("ruler", "thước kẻ", "The ruler is thirty centimeters long.", "Cái thước dài ba mươi xen-ti-mét."),
    ("crayon", "bút sáp màu", "The child draws with a crayon.", "Đứa trẻ vẽ bằng bút sáp màu."),
    ("marker", "bút dạ", "Write the title with a marker.", "Viết tiêu đề bằng bút dạ."),
    ("scissors", "cái kéo", "The scissors are on the desk.", "Cái kéo ở trên bàn học."),
    ("glue", "keo dán", "Put some glue on the paper.", "Bôi một ít keo lên tờ giấy."),
    ("schoolbag", "cặp sách", "Her schoolbag is blue.", "Cặp sách của cô bé màu xanh."),
    ("blackboard", "bảng đen", "The teacher writes on the blackboard.", "Giáo viên viết lên bảng đen."),
    ("whiteboard", "bảng trắng", "The answer is on the whiteboard.", "Đáp án ở trên bảng trắng."),
    ("keyboard", "bàn phím", "Type your name on the keyboard.", "Gõ tên của bạn trên bàn phím."),
    ("screen", "màn hình", "The picture is on the screen.", "Bức tranh ở trên màn hình."),
    ("headphones", "tai nghe", "He wears headphones to listen.", "Cậu ấy đeo tai nghe để nghe."),
    ("camera", "máy ảnh", "This camera takes clear pictures.", "Máy ảnh này chụp ảnh rõ."),
    ("remote", "điều khiển từ xa", "The remote is beside the television.", "Điều khiển ở cạnh tivi."),
    ("clock", "đồng hồ", "The clock says ten o'clock.", "Đồng hồ chỉ mười giờ."),
    ("calendar", "lịch", "The calendar is on the wall.", "Lịch ở trên tường."),
    ("umbrella", "ô/dù", "Take an umbrella because it may rain.", "Mang ô vì có thể trời sẽ mưa."),
    ("raincoat", "áo mưa", "His raincoat is yellow.", "Áo mưa của cậu ấy màu vàng."),
    ("helmet", "mũ bảo hiểm", "Always wear a helmet on a bike.", "Luôn đội mũ bảo hiểm khi đi xe đạp."),
    ("bicycle", "xe đạp", "She rides a bicycle to school.", "Cô ấy đạp xe đến trường."),
    ("scooter", "xe tay ga/xe trượt", "The scooter is parked outside.", "Chiếc xe được đỗ bên ngoài."),
    ("bus", "xe buýt", "The bus stops near the school.", "Xe buýt dừng gần trường."),
    ("train", "tàu hỏa", "The train arrives at noon.", "Tàu hỏa đến vào buổi trưa."),
    ("airplane", "máy bay", "The airplane is above the clouds.", "Máy bay ở phía trên những đám mây."),
    ("boat", "thuyền", "The boat is on the river.", "Con thuyền ở trên sông."),
    ("bridge", "cây cầu", "We walked across the bridge.", "Chúng tôi đi bộ qua cầu."),
    ("traffic light", "đèn giao thông", "The traffic light is red.", "Đèn giao thông đang màu đỏ."),
    ("sign", "biển báo; dấu hiệu", "Read the sign before entering.", "Đọc biển báo trước khi vào."),
    ("map", "bản đồ", "The map shows the way to the park.", "Bản đồ chỉ đường đến công viên."),
    ("ticket", "vé", "Keep your train ticket safe.", "Giữ vé tàu cẩn thận."),
    ("key", "chìa khóa", "The key is in my pocket.", "Chìa khóa ở trong túi tôi."),
    ("lock", "ổ khóa; khóa", "The lock is on the door.", "Ổ khóa ở trên cửa."),
    ("shelf", "kệ", "The books are on the shelf.", "Sách ở trên kệ."),
    ("drawer", "ngăn kéo", "The pencils are in the drawer.", "Bút chì ở trong ngăn kéo."),
    ("basket", "cái giỏ", "Put the apples in the basket.", "Đặt táo vào giỏ."),
    ("bucket", "cái xô", "The bucket is full of water.", "Cái xô đầy nước."),
    ("broom", "cái chổi", "The broom is behind the door.", "Cái chổi ở sau cánh cửa."),
    ("tissue", "khăn giấy", "Use a tissue to wipe your hands.", "Dùng khăn giấy lau tay."),
    ("comb", "cái lược", "The comb is next to the mirror.", "Cái lược ở cạnh gương."),
    ("brush", "bàn chải; cọ", "Use a brush to paint the box.", "Dùng cọ để sơn cái hộp."),
    ("button", "cái nút áo/nút bấm", "The button is missing from my shirt.", "Áo của tôi bị mất một cái nút."),
    ("zipper", "khóa kéo", "The zipper on my bag is broken.", "Khóa kéo trên túi của tôi bị hỏng."),
    ("glove", "găng tay", "Wear a glove to protect your hand.", "Đeo găng tay để bảo vệ tay."),
    ("scarf", "khăn quàng", "The scarf keeps her warm.", "Khăn quàng giữ ấm cho cô ấy."),
    ("sneaker", "giày thể thao", "My sneakers are under the bed.", "Giày thể thao của tôi ở dưới giường."),
    ("bowl", "cái bát", "The soup is in the bowl.", "Canh ở trong bát."),
    ("pan", "cái chảo", "The pan is on the stove.", "Cái chảo ở trên bếp."),
    ("pot", "cái nồi", "The pot is full of rice.", "Cái nồi đầy cơm."),
    ("tray", "cái khay", "Put the cups on the tray.", "Đặt những cái cốc lên khay."),
    ("napkin", "khăn ăn", "There is a napkin beside the plate.", "Có một chiếc khăn ăn cạnh đĩa."),
    ("bottle", "cái chai", "The bottle is made of glass.", "Cái chai làm bằng thủy tinh."),
    ("box", "cái hộp", "The toy is in the box.", "Đồ chơi ở trong hộp."),
    ("ball", "quả bóng", "The children play with a ball.", "Bọn trẻ chơi với một quả bóng."),
    ("doll", "búp bê", "The doll has a red dress.", "Búp bê mặc váy đỏ."),
    ("puzzle", "trò ghép hình", "This puzzle has many pieces.", "Bộ ghép hình này có nhiều mảnh."),
    ("toy", "đồ chơi", "The toy is under the chair.", "Đồ chơi ở dưới ghế."),
]


def ipa_for(lemma):
    with gzip.open(SOURCE, "rt", encoding="utf-8") as stream:
        for line in stream:
            row = json.loads(line)
            if row.get("headword", "").casefold() == lemma.casefold() and row.get("ipa"):
                return row["ipa"][0]
    return "/" + lemma.replace(" ", "·") + "/"


existing = {w["lemma"].casefold() for w in lib.WORDS}
for topic_id, rows in (("food", FRUITS), ("animals", ANIMALS), ("home_basics", OBJECTS)):
    lib._current_topic = topic_id
    for lemma, gloss, en, vi in rows:
        if lemma.casefold() in existing:
            continue
        lib.W(lemma, "n", ipa_for(lemma), 1, gloss, en, vi)
        word = next(w for w in lib.WORDS if w["id"] == lib.slug(lemma))
        word.update(
            needs_review=True,
            source="authorial-picture-pack-2026-10",
            sourceLists=["authorial-picture-pack"],
            sourceTags=["learning:picture_candidate", "goal:communication"],
            license="Original authored examples; source list not imported",
            usageKind="picture_candidate",
            editorialCorrection=False,
        )
        existing.add(lemma.casefold())

