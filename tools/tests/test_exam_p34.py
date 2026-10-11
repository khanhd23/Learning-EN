import json
import sys
import unittest
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / "tools"))
import build_toeic_bank  # noqa: E402


class OriginalPart34ExamplesTest(unittest.TestCase):
    def test_each_part_has_three_complete_practice_sets(self):
        formats = json.loads((ROOT / "config/exam_formats.json").read_text(encoding="utf-8"))["formats"]
        for section in ("p3", "p4"):
            bank = json.loads((ROOT / f"content/en/exams/toeic_{section}.json").read_text(encoding="utf-8"))
            groups_per_set, questions_per_set = (13, 39) if section == "p3" else (10, 30)
            self.assertEqual(groups_per_set * 3, len(bank["groups"]))
            self.assertEqual(questions_per_set * 3, sum(len(group["items"]) for group in bank["groups"]))
            self.assertEqual({"1", "2", "3"}, {group["set"] for group in bank["groups"]})
            self.assertEqual(
                {str(set_id): groups_per_set for set_id in range(1, 4)},
                {set_id: sum(group["set"] == set_id for group in bank["groups"]) for set_id in ("1", "2", "3")},
            )
            self.assertTrue(all(item["qtype"] == f"listen_{section}" for group in bank["groups"] for item in group["items"]))
            for group in bank["groups"]:
                self.assertEqual(3, len(group["items"]))
                for item in group["items"]:
                    self.assertTrue(item.get("evidence"))
                    self.assertIn(item["evidence"], group["passage"])
            for suffix, set_id in [("examples", "1"), ("practice_2", "2"), ("practice_3", "3")]:
                fmt = next(item for item in formats if item["id"] == f"toeic_{section}_{suffix}")
                self.assertEqual(questions_per_set, fmt["questionCount"])
                self.assertEqual("toeic_" + section, fmt["sections"][0]["bank"])
                self.assertEqual(questions_per_set, fmt["sections"][0]["count"])
                self.assertEqual(set_id, fmt["sections"][0]["groupSet"])
                self.assertEqual([f"listen_{section}"], fmt["sections"][0]["qtype"])

    def test_toeic_parts_five_to_seven_have_method_and_tip_content(self):
        formats = json.loads((ROOT / "config/exam_formats.json").read_text(encoding="utf-8"))["formats"]
        by_id = {item["id"]: item for item in formats}
        for part in ("toeic_p5", "toeic_p6"):
            self.assertIn("Cách làm", by_id[part]["description"])
        for part in ("toeic_p7_practice_1", "toeic_p7_practice_2", "toeic_p7_practice_3"):
            self.assertIn("Cách làm", by_id[part]["description"])
            self.assertEqual(["p7_single", "p7_double", "p7_triple"], by_id[part]["sections"][0]["qtype"])
        en = json.loads((ROOT / "content/en/tips.json").read_text(encoding="utf-8"))
        vi = json.loads((ROOT / "content/i18n/vi/tips.json").read_text(encoding="utf-8"))
        for key in ("p5_method", "p6_method", "p7_method", "p7_single", "p7_double", "p7_triple"):
            self.assertTrue(en.get(key))
            self.assertTrue(vi.get(key))


if __name__ == "__main__":
    unittest.main()
