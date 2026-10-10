import json
import unittest
from collections import Counter
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]


class IeltsShortReadingTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.bank = json.loads((ROOT / "content/en/exams/ielts.json").read_text(encoding="utf-8"))
        cls.vi = json.loads((ROOT / "content/i18n/vi/exams/ielts.json").read_text(encoding="utf-8"))
        cls.formats = json.loads((ROOT / "config/exam_formats.json").read_text(encoding="utf-8"))["formats"]

    def test_five_mini_papers_have_two_passages_and_ten_items(self):
        self.assertEqual(10, len(self.bank["groups"]))
        self.assertEqual(50, sum(len(group["items"]) for group in self.bank["groups"]))
        for paper in map(str, range(1, 6)):
            groups = [group for group in self.bank["groups"] if group["set"] == paper]
            self.assertEqual({"reading1", "reading2"}, {group["section"] for group in groups})
            self.assertEqual(10, sum(len(group["items"]) for group in groups))
            self.assertTrue(all(len(group["items"]) == 5 for group in groups))

    def test_all_items_have_four_choices_and_locale_explanations(self):
        for group in self.bank["groups"]:
            self.assertIn(group["id"], self.vi)
            self.assertTrue(self.vi[group["id"]]["passage"])
            for item in group["items"]:
                self.assertEqual(4, len(item["opts"]))
                self.assertIn(item["ans"], range(4))
                self.assertEqual("ielts_short_reading", item["qtype"])
                self.assertIn(item["id"], self.vi)
                self.assertTrue(self.vi[item["id"]]["expl"])

    def test_ids_prompts_and_answer_positions(self):
        items = [(group["id"], item) for group in self.bank["groups"] for item in group["items"]]
        self.assertEqual(len(items), len({item["id"] for _, item in items}))
        self.assertEqual(len(items), len({(group_id, item["stem"]) for group_id, item in items}))
        positions = Counter(item["ans"] for _, item in items)
        self.assertEqual(50, sum(positions.values()))
        self.assertGreaterEqual(len(positions), 4)

    def test_formats_select_each_paper(self):
        formats = [item for item in self.formats if item["id"].startswith("ielts_short_reading_")]
        self.assertEqual(5, len(formats))
        for number, item in enumerate(formats, 1):
            self.assertEqual(10, item["questionCount"])
            self.assertEqual(30, item["timeLimitMinutes"])
            self.assertEqual(10, sum(section["count"] for section in item["sections"]))
            self.assertEqual({str(number)}, {section["groupSet"] for section in item["sections"]})
            self.assertTrue(all(section["bank"] == "ielts" for section in item["sections"]))


if __name__ == "__main__":
    unittest.main()
