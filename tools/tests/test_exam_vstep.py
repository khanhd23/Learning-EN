import json
import unittest
from collections import Counter
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]


class VstepBankTest(unittest.TestCase):
    def test_three_complete_papers_and_locale_explanations(self):
        bank = json.loads((ROOT / "content/en/exams/vstep.json").read_text(encoding="utf-8"))
        vi = json.loads((ROOT / "content/i18n/vi/exams/vstep.json").read_text(encoding="utf-8"))
        groups = bank["groups"]
        self.assertEqual(12, len(groups))
        self.assertEqual({"1", "2", "3"}, {group["set"] for group in groups})
        for paper in ("1", "2", "3"):
            paper_groups = [group for group in groups if group["set"] == paper]
            self.assertEqual({"reading1", "reading2", "reading3", "reading4"}, {group["section"] for group in paper_groups})
            self.assertEqual(40, sum(len(group["items"]) for group in paper_groups))
            for group in paper_groups:
                self.assertEqual(10, len(group["items"]))
                self.assertIn(group["id"], vi)
                self.assertTrue(vi[group["id"]]["passage"])
                for item in group["items"]:
                    self.assertEqual(4, len(item["opts"]))
                    self.assertIn(item["id"], vi)
                    self.assertTrue(vi[item["id"]]["expl"])

    def test_answer_positions_are_deterministic_and_not_all_first(self):
        bank = json.loads((ROOT / "content/en/exams/vstep.json").read_text(encoding="utf-8"))
        positions = Counter(item["ans"] for group in bank["groups"] for item in group["items"])
        self.assertEqual(120, sum(positions.values()))
        self.assertGreaterEqual(len(positions), 3)
        self.assertLess(positions[0], 60)

    def test_formats_expose_three_40_question_papers(self):
        formats = json.loads((ROOT / "config/exam_formats.json").read_text(encoding="utf-8"))["formats"]
        selected = [item for item in formats if item["id"].startswith("vstep_practice_")]
        self.assertEqual(3, len(selected))
        for item in selected:
            self.assertEqual(40, item["questionCount"])
            self.assertEqual(60, item["timeLimitMinutes"])
            self.assertEqual(40, sum(section["count"] for section in item["sections"]))
            self.assertTrue(all(section["bank"] == "vstep" for section in item["sections"]))


if __name__ == "__main__":
    unittest.main()
