import json
import unittest
from collections import Counter
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]


class KetBankTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.bank = json.loads((ROOT / "content/en/exams/ket.json").read_text(encoding="utf-8"))
        cls.vi = json.loads((ROOT / "content/i18n/vi/exams/ket.json").read_text(encoding="utf-8"))
        cls.formats = json.loads((ROOT / "config/exam_formats.json").read_text(encoding="utf-8"))["formats"]

    def test_five_complete_reading_papers(self):
        self.assertEqual(25, len(self.bank["groups"]))
        self.assertEqual(150, sum(len(group["items"]) for group in self.bank["groups"]))
        expected = {"part1": 6, "part2": 7, "part3": 5, "part4": 6, "part5": 6}
        for paper in map(str, range(1, 6)):
            groups = [group for group in self.bank["groups"] if group["set"] == paper]
            self.assertEqual(set(expected), {group["section"] for group in groups})
            self.assertEqual(30, sum(len(group["items"]) for group in groups))
            for group in groups:
                self.assertEqual(expected[group["section"]], len(group["items"]))

    def test_task_types_are_not_collapsed_to_multiple_choice(self):
        for group in self.bank["groups"]:
            for item in group["items"]:
                self.assertIn(item["id"], self.vi)
                self.assertTrue(self.vi[item["id"]]["expl"])
                if group["section"] == "part5":
                    self.assertEqual([], item["opts"])
                    self.assertEqual(-1, item["ans"])
                    self.assertTrue(item["answerText"])
                else:
                    self.assertEqual(3, len(item["opts"]))
                    self.assertIn(item["ans"], range(3))
                    self.assertNotIn("answerText", item)

    def test_ids_and_prompts_are_unique_and_answers_are_balanced(self):
        items = [(group["id"], item) for group in self.bank["groups"] for item in group["items"]]
        self.assertEqual(len(items), len({item["id"] for _, item in items}))
        self.assertEqual(len(items), len({(group_id, item["stem"]) for group_id, item in items}))
        positions = Counter(item["ans"] for _, item in items if item["opts"])
        self.assertEqual(120, sum(positions.values()))
        self.assertGreaterEqual(len(positions), 3)

    def test_formats_select_each_disjoint_paper(self):
        formats = [item for item in self.formats if item["id"].startswith("ket_reading_")]
        self.assertEqual(5, len(formats))
        for number, item in enumerate(formats, 1):
            self.assertEqual(30, item["questionCount"])
            self.assertEqual(60, item["timeLimitMinutes"])
            self.assertEqual(30, sum(section["count"] for section in item["sections"]))
            self.assertEqual({str(number)}, {section["groupSet"] for section in item["sections"]})
            self.assertTrue(all(section["bank"] == "ket" for section in item["sections"]))


if __name__ == "__main__":
    unittest.main()
