import json
import sys
import unittest
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / "tools"))
import build_toeic_bank  # noqa: E402


class OriginalPart34ExamplesTest(unittest.TestCase):
    def test_each_part_has_ten_questions_in_five_two_question_groups(self):
        for section in ("p3", "p4"):
            source = build_toeic_bank.read_authoring_part(section)
            bank, locale = build_toeic_bank.build_p34(source, section)
            self.assertEqual(5, len(source))
            self.assertEqual(5, len(bank["groups"]))
            self.assertEqual(10, sum(len(group["items"]) for group in bank["groups"]))
            self.assertEqual(15, len(locale))  # five titles/passages + ten explanations
            ids = [item["id"] for group in bank["groups"] for item in group["items"]]
            self.assertEqual(10, len(set(ids)))
            for group in source:
                self.assertTrue(group["source"].startswith("original:"))
                self.assertEqual(2, len(group["items"]))
                for item in group["items"]:
                    self.assertEqual(4, len(item["opts"]))
                    self.assertIn(item["ans"], range(4))
                    self.assertTrue(item["expl"])
                    self.assertTrue(item["vi_expl"])

    def test_generated_banks_and_config_expose_only_example_sets(self):
        formats = json.loads((ROOT / "config/exam_formats.json").read_text(encoding="utf-8"))["formats"]
        for section in ("p3", "p4"):
            bank = json.loads((ROOT / f"content/en/exams/toeic_{section}.json").read_text(encoding="utf-8"))
            self.assertEqual(5, len(bank["groups"]))
            self.assertEqual(10, sum(len(group["items"]) for group in bank["groups"]))
            self.assertEqual({"examples"}, {group["set"] for group in bank["groups"]})
            self.assertTrue(all(item["qtype"] == f"listen_{section}" for group in bank["groups"] for item in group["items"]))
            fmt = next(item for item in formats if item["id"] == f"toeic_{section}_examples")
            self.assertEqual(10, fmt["questionCount"])
            self.assertEqual("toeic_" + section, fmt["sections"][0]["bank"])
            self.assertEqual(10, fmt["sections"][0]["count"])
            self.assertEqual([f"listen_{section}"], fmt["sections"][0]["qtype"])


if __name__ == "__main__":
    unittest.main()
