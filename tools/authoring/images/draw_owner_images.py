"""Owner-drawn word images (ROADMAP C2) for words with no fitting emoji.

Flat style to sit beside Noto Emoji: 128x128 viewBox, solid fills, no strokes, only <path>
elements (the image builder converts each path to an Android VectorDrawable path).
Run: python tools/authoring/images/draw_owner_images.py  -> writes <name>.svg next to this file.
"""
import math
from pathlib import Path

OUT = Path(__file__).resolve().parent


def f(v):
    return f"{v:.1f}".rstrip("0").rstrip(".")


def circle(cx, cy, r):
    return f"M{f(cx - r)} {f(cy)}a{f(r)} {f(r)} 0 1 0 {f(2 * r)} 0a{f(r)} {f(r)} 0 1 0 {f(-2 * r)} 0Z"


def ellipse(cx, cy, rx, ry):
    return f"M{f(cx - rx)} {f(cy)}a{f(rx)} {f(ry)} 0 1 0 {f(2 * rx)} 0a{f(rx)} {f(ry)} 0 1 0 {f(-2 * rx)} 0Z"


def rect(x, y, w, h, r=0):
    if r <= 0:
        return f"M{f(x)} {f(y)}h{f(w)}v{f(h)}h{f(-w)}Z"
    return (f"M{f(x + r)} {f(y)}h{f(w - 2 * r)}a{f(r)} {f(r)} 0 0 1 {f(r)} {f(r)}v{f(h - 2 * r)}"
            f"a{f(r)} {f(r)} 0 0 1 {f(-r)} {f(r)}h{f(-(w - 2 * r))}a{f(r)} {f(r)} 0 0 1 {f(-r)} {f(-r)}"
            f"v{f(-(h - 2 * r))}a{f(r)} {f(r)} 0 0 1 {f(r)} {f(-r)}Z")


def poly(*pts):
    return "M" + "L".join(f"{f(x)} {f(y)}" for x, y in pts) + "Z"


def half_disc(cx, cy, r, top=True):
    """Half circle; top=True keeps the upper half."""
    sweep = 1 if top else 0
    return f"M{f(cx - r)} {f(cy)}a{f(r)} {f(r)} 0 0 {sweep} {f(2 * r)} 0Z"


def star(cx, cy, ro, ri, n=5, rot=-90):
    pts = []
    for i in range(2 * n):
        r = ro if i % 2 == 0 else ri
        a = math.radians(rot + i * 180 / n)
        pts.append((cx + r * math.cos(a), cy + r * math.sin(a)))
    return poly(*pts)


def leaf(x, y, w, h, angle=-30):
    """Simple leaf: two arcs from base (x,y) to tip, rotated by angle (degrees)."""
    a = math.radians(angle)
    tx, ty = x + w * math.cos(a), y + w * math.sin(a)
    return f"M{f(x)} {f(y)}Q{f((x + tx) / 2 - h * math.sin(a))} {f((y + ty) / 2 + h * math.cos(a) * -1)} {f(tx)} {f(ty)}Q{f((x + tx) / 2 + h * math.sin(a))} {f((y + ty) / 2 + h * math.cos(a))} {f(x)} {f(y)}Z"


def seeds(points, r=2.6):
    return "".join(circle(x, y, r) for x, y in points)


def svg(*layers):
    body = "".join(f'<path fill="{c}" d="{d}"/>' for c, d in layers)
    return f'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 128 128">{body}</svg>\n'


GREEN, DGREEN, BROWN = "#7CB342", "#558B2F", "#8D6E63"

IMAGES = {
    # fruit
    "plum": svg(("#6A1B9A", circle(64, 70, 42)), ("#8E24AA", circle(56, 62, 30)), ("#4A148C", "M64 30Q60 70 64 110Q68 70 64 30Z"),
                (BROWN, rect(61, 14, 6, 20, 3)), (GREEN, leaf(66, 24, 30, 9, -25))),
    "papaya": svg((GREEN, ellipse(64, 64, 34, 52)), ("#FF8A3D", ellipse(64, 64, 26, 44)), ("#FFB74D", ellipse(64, 64, 14, 30)),
                  ("#3E2723", seeds([(64, 44), (60, 52), (68, 52), (64, 60), (60, 68), (68, 68), (64, 76), (64, 84)], 3.4))),
    "guava": svg(("#9CCC65", circle(64, 66, 44)), ("#F48FB1", circle(64, 66, 34)), ("#F8BBD0", circle(64, 66, 18)),
                 ("#FFF3E0", seeds([(48, 58), (80, 58), (52, 80), (76, 80), (64, 48), (64, 86), (44, 70), (84, 70)], 2.4)),
                 (DGREEN, leaf(64, 22, 28, 8, -40))),
    "lime": svg(("#558B2F", circle(64, 64, 46)), ("#C5E1A5", circle(64, 64, 40)), ("#9CCC65", "".join(
        poly((64, 64), (64 + 34 * math.cos(math.radians(a)), 64 + 34 * math.sin(math.radians(a))),
             (64 + 34 * math.cos(math.radians(a + 50)), 64 + 34 * math.sin(math.radians(a + 50)))) for a in range(0, 360, 60))),
                ("#F1F8E9", circle(64, 64, 5))),
    "grapefruit": svg(("#FF9800", circle(64, 64, 46)), ("#FFE0B2", circle(64, 64, 41)), ("#F06292", "".join(
        poly((64, 64), (64 + 36 * math.cos(math.radians(a)), 64 + 36 * math.sin(math.radians(a))),
             (64 + 36 * math.cos(math.radians(a + 40)), 64 + 36 * math.sin(math.radians(a + 40)))) for a in range(0, 360, 45))),
                      ("#FFE0B2", circle(64, 64, 4))),
    "pomegranate": svg(("#C62828", circle(64, 70, 42)), ("#E53935", circle(56, 62, 28)),
                       ("#B71C1C", poly((50, 34), (54, 18), (60, 30), (64, 14), (68, 30), (74, 18), (78, 34))),
                       ("#FFCDD2", circle(48, 56, 5))),
    "apricot": svg(("#FB8C00", circle(64, 70, 40)), ("#FFB74D", circle(56, 62, 26)), ("#EF6C00", "M64 32Q58 70 64 108Q70 70 64 32Z"),
                   (BROWN, rect(61, 18, 6, 18, 3)), (GREEN, leaf(66, 28, 28, 8, -20))),
    "raspberry": svg((GREEN, poly((44, 30), (64, 20), (84, 30), (64, 36))), ("#C2185B", "".join(circle(x, y, 9) for x, y in
                     [(50, 46), (64, 44), (78, 46), (44, 60), (58, 58), (72, 58), (84, 60), (50, 74), (64, 72), (78, 74), (56, 88), (72, 88), (64, 100)])),
                     ("#F06292", "".join(circle(x - 3, y - 3, 3) for x, y in [(50, 46), (64, 44), (78, 46), (58, 58), (72, 58), (64, 72)]))),
    "cranberry": svg(("#B71C1C", circle(44, 72, 22) + circle(84, 72, 22) + circle(64, 44, 22)),
                     ("#E57373", circle(38, 66, 5) + circle(78, 66, 5) + circle(58, 38, 5)), (DGREEN, leaf(64, 22, 30, 8, -60))),
    "mulberry": svg((DGREEN, rect(61, 10, 6, 20, 3)), ("#4A148C", "".join(circle(x, y, 9) for x, y in
                    [(56, 36), (72, 36), (52, 50), (68, 50), (80, 52), (56, 64), (72, 64), (60, 78), (74, 78), (64, 92), (66, 104)])),
                    ("#7B1FA2", "".join(circle(x - 3, y - 3, 3) for x, y in [(56, 36), (68, 50), (56, 64), (74, 78)]))),
    "passion_fruit": svg(("#4527A0", circle(64, 64, 46)), ("#FFD54F", circle(64, 64, 38)), ("#FFB300", circle(64, 64, 28)),
                         ("#212121", seeds([(52, 54), (64, 50), (76, 54), (48, 66), (60, 64), (72, 66), (80, 68), (54, 78), (66, 78), (76, 80)], 3))),
    "star_fruit": svg(("#C0CA33", star(64, 68, 50, 27)), ("#FFEE58", star(64, 68, 40, 21)), ("#FFF9C4", star(64, 68, 13, 7)),
                      ("#8D6E63", ellipse(64, 50, 3, 5) + ellipse(80, 64, 5, 3) + ellipse(48, 64, 5, 3)), (DGREEN, rect(61, 6, 6, 16, 3))),
    "dragon_fruit": svg(("#D81B60", ellipse(64, 64, 42, 52)), ("#FFFFFF", ellipse(64, 64, 34, 44)),
                        ("#212121", seeds([(52, 40), (66, 36), (76, 46), (48, 56), (62, 54), (78, 60), (52, 72), (66, 70), (80, 76), (56, 88), (70, 90), (60, 102)], 2.2)),
                        ("#8BC34A", poly((30, 30), (40, 34), (34, 40)) + poly((98, 30), (88, 34), (94, 40)))),
    # home
    "table": svg(("#8D6E63", rect(14, 40, 100, 14, 4)), ("#6D4C41", rect(22, 54, 10, 52, 3) + rect(96, 54, 10, 52, 3)),
                 ("#A1887F", rect(14, 40, 100, 5, 2))),
    "pillow": svg(("#90CAF9", "M20 40Q64 26 108 40Q118 64 108 88Q64 102 20 88Q10 64 20 40Z"), ("#BBDEFB", "M30 48Q64 38 98 48Q104 64 98 80Q64 90 30 80Q24 64 30 48Z")),
    "blanket": svg(("#EF5350", rect(16, 28, 96, 72, 8)), ("#FFCDD2", rect(16, 44, 96, 8) + rect(16, 76, 96, 8)), ("#C62828", rect(16, 92, 96, 8, 4))),
    "towel": svg(("#9E9E9E", rect(14, 22, 100, 8, 4)), ("#26A69A", rect(30, 26, 68, 84, 6)), ("#B2DFDB", rect(30, 90, 68, 8)), ("#00897B", rect(30, 26, 68, 10))),
    "toothpaste": svg(("#ECEFF1", poly((22, 52), (98, 40), (98, 88), (22, 76))), ("#1E88E5", rect(98, 50, 18, 28, 4)), ("#E53935", poly((40, 56), (80, 50), (80, 78), (40, 72))),
                      ("#B0BEC5", rect(14, 50, 10, 28, 2))),
    "wallet": svg(("#6D4C41", rect(16, 34, 96, 64, 10)), ("#8D6E63", rect(60, 50, 56, 32, 8)), ("#FFCA28", circle(96, 66, 6))),
    "cup": svg(("#1E88E5", "M86 46a18 18 0 1 1 0 36v-10a8 8 0 1 0 0-16Z"), ("#42A5F5", rect(24, 36, 64, 62, 10)), ("#90CAF9", rect(30, 42, 10, 46, 5))),
    "plate": svg(("#B0BEC5", ellipse(64, 70, 52, 48)), ("#90A4AE", circle(64, 64, 50)), ("#FFFFFF", circle(64, 64, 44)), ("#E3E8EB", circle(64, 64, 30))),
    "bowl": svg(("#1565C0", "M14 56h100a50 46 0 0 1-100 0Z"), ("#42A5F5", "M22 56h84a42 36 0 0 1-84 0Z"), ("#0D47A1", rect(46, 98, 36, 8, 4))),
    "bottle": svg(("#1E88E5", rect(52, 10, 24, 14, 4)), ("#90CAF9", "M50 24h28v14q14 8 14 24v48q0 8-8 8h-40q-8 0-8-8v-48q0-16 14-24Z"),
                  ("#42A5F5", rect(36, 66, 56, 28)), ("#E3F2FD", rect(42, 48, 6, 54, 3))),
    "lamp": svg(("#FFCA28", poly((38, 18), (90, 18), (106, 62), (22, 62))), ("#FFE082", poly((46, 24), (82, 24), (94, 56), (34, 56))),
                ("#757575", rect(60, 62, 8, 36)), ("#616161", rect(36, 98, 56, 12, 6))),
    "fridge": svg(("#B0BEC5", rect(28, 8, 72, 112, 10)), ("#ECEFF1", rect(32, 12, 64, 38, 6) + rect(32, 54, 64, 62, 6)),
                  ("#78909C", rect(84, 22, 6, 18, 3) + rect(84, 64, 6, 28, 3))),
    "shelf": svg(("#8D6E63", rect(10, 50, 108, 10, 3) + rect(10, 100, 108, 10, 3)), ("#6D4C41", rect(20, 60, 6, 12) + rect(102, 60, 6, 12)),
                 ("#E53935", rect(22, 14, 12, 36, 2)), ("#1E88E5", rect(36, 20, 12, 30, 2)), ("#43A047", rect(50, 10, 12, 40, 2)),
                 ("#FFB300", rect(76, 70, 30, 30, 4))),
    "drawer": svg(("#8D6E63", rect(18, 14, 92, 100, 8)), ("#BCAAA4", rect(26, 22, 76, 26, 4) + rect(26, 52, 76, 26, 4) + rect(26, 82, 76, 26, 4)),
                  ("#5D4037", rect(56, 32, 16, 6, 3) + rect(56, 62, 16, 6, 3) + rect(56, 92, 16, 6, 3))),
    "curtain": svg(("#B3E5FC", rect(24, 22, 80, 94)), ("#757575", rect(10, 12, 108, 8, 4)), ("#D32F2F", "M16 20h30q-8 48 4 96h-34Z"),
                   ("#D32F2F", "M112 20h-30q8 48-4 96h34Z"), ("#B71C1C", rect(16, 20, 6, 96) + rect(106, 20, 6, 96))),
    "stool": svg(("#8D6E63", ellipse(64, 34, 40, 12)), ("#A1887F", ellipse(64, 30, 40, 10)),
                 ("#6D4C41", poly((36, 40), (44, 40), (34, 116), (26, 116)) + poly((84, 40), (92, 40), (102, 116), (94, 116)) + rect(60, 42, 8, 70)),
                 ("#5D4037", rect(34, 82, 60, 6))),
    "socket": svg(("#CFD8DC", rect(20, 20, 88, 88, 14)), ("#FFFFFF", rect(28, 28, 72, 72, 10)), ("#37474F", circle(50, 64, 7) + circle(78, 64, 7))),
    "iron": svg(("#1E88E5", "M16 92Q22 50 66 40h40q8 0 8 10v42Z"), ("#ECEFF1", rect(16, 92, 98, 10, 4)), ("#455A64", "M54 40q0-18 18-18h26q8 0 8 8v10h-12v-6h-22q-6 0-6 6Z"),
                ("#FF7043", circle(84, 70, 6))),
    "tray": svg(("#8D6E63", "M14 58h100l-10 34h-80Z"), ("#A1887F", rect(14, 54, 100, 8, 4)), ("#6D4C41", rect(4, 52, 14, 10, 4) + rect(110, 52, 14, 10, 4)),
                ("#ECEFF1", ellipse(48, 48, 14, 6)), ("#FFB74D", ellipse(80, 48, 12, 6))),
    # clothes
    "skirt": svg(("#EC407A", rect(36, 22, 56, 14, 4)), ("#F06292", poly((38, 36), (90, 36), (114, 106), (14, 106))), ("#D81B60", poly((56, 36), (64, 36), (60, 106), (46, 106)) + poly((72, 36), (80, 36), (90, 106), (76, 106)))),
    "jacket": svg(("#F57C00", "M40 16l-30 20v74h44v-84Zm48 0l30 20v74h-44v-84Z"), ("#E65100", rect(10, 94, 22, 16) + rect(96, 94, 22, 16)),
                  ("#FFB74D", poly((40, 16), (54, 16), (60, 36), (50, 40))), ("#FFB74D", poly((88, 16), (74, 16), (68, 36), (78, 40))),
                  ("#616161", rect(62, 28, 4, 82))),
    "belt": svg(("#6D4C41", rect(6, 52, 116, 24, 6)), ("#FFC107", rect(40, 44, 34, 40, 6)), ("#6D4C41", rect(48, 52, 18, 24, 3)), ("#FFC107", rect(54, 60, 18, 8, 2)),
                ("#3E2723", circle(90, 64, 3) + circle(104, 64, 3))),
    "button": svg(("#5C6BC0", circle(64, 64, 48)), ("#7986CB", circle(64, 64, 38)), ("#283593", circle(52, 52, 7) + circle(76, 52, 7) + circle(52, 76, 7) + circle(76, 76, 7))),
    "zipper": svg(("#90A4AE", rect(56, 8, 16, 112, 4)), ("#546E7A", "".join(rect(48 if i % 2 else 66, 14 + i * 10, 14, 6, 2) for i in range(7))),
                  ("#455A64", rect(50, 82, 28, 16, 6)), ("#78909C", rect(58, 96, 12, 24, 6))),
    "raincoat": svg(("#FDD835", "M64 8q30 0 32 30l18 16v62h-28v-50h-44v50h-28v-62l18-16q2-30 32-30Z"), ("#F9A825", "M64 20q20 0 22 22h-44q2-22 22-22Z"),
                    ("#FFF59D", rect(42, 66, 44, 50)), ("#F57F17", circle(64, 80, 3) + circle(64, 96, 3))),
    # transport
    "helmet": svg(("#E53935", "M14 78a50 50 0 0 1 100 0v8h-100Z"), ("#263238", "M66 52h44a50 50 0 0 1 4 26h-48Z"), ("#B71C1C", rect(14, 86, 100, 10, 4)),
                  ("#EF9A9A", "M30 50q8-20 30-24q-16 10-22 26Z")),
    # nature
    "seed": svg(("#8D6E63", "M64 116q-32-10-32-40q0-20 32-30q32 10 32 30q0 30-32 40Z"), ("#A1887F", "M54 64q6-10 18-12q-10 8-12 20Z"),
                (GREEN, leaf(64, 46, 34, 10, -60) + leaf(64, 46, 30, 9, -125)), (DGREEN, rect(62, 30, 4, 18, 2))),
    # school
    "eraser": svg(("#EC407A", poly((14, 74), (66, 30), (114, 54), (62, 98))), ("#42A5F5", poly((14, 74), (40, 52), (88, 76), (62, 98))),
                  ("#1E88E5", poly((14, 74), (62, 98), (62, 108), (14, 84))), ("#C2185B", poly((62, 98), (114, 54), (114, 64), (62, 108)))),
    "whiteboard": svg(("#90A4AE", rect(10, 14, 108, 76, 6)), ("#FFFFFF", rect(16, 20, 96, 64, 3)), ("#1E88E5", "M28 50q10-20 20 0t20 0t20 0v6q-10-14-20 0t-20 0t-20 0Z"),
                      ("#78909C", poly((40, 90), (48, 90), (36, 120), (28, 120)) + poly((80, 90), (88, 90), (100, 120), (92, 120))), ("#E53935", rect(70, 84, 26, 6, 3))),
    "desk": svg(("#A1887F", rect(10, 40, 108, 12, 4)), ("#8D6E63", rect(18, 52, 10, 62, 3) + rect(100, 52, 10, 62, 3)), ("#6D4C41", rect(64, 52, 36, 28, 3)),
                ("#D7CCC8", rect(76, 62, 12, 5, 2)), ("#1E88E5", rect(26, 26, 30, 14, 2))),
}

if __name__ == "__main__":
    total = 0
    for name, content in IMAGES.items():
        path = OUT / f"{name}.svg"
        path.write_text(content, encoding="utf-8", newline="\n")
        total += len(content.encode("utf-8"))
    print(f"{len(IMAGES)} images, {total} bytes")
