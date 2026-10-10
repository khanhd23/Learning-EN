import json
import unittest
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]


class IeltsFullReadingTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.bank = json.loads((ROOT / "content/en/exams/ielts_full.json").read_text(encoding="utf-8"))
        cls.locale = json.loads((ROOT / "content/i18n/vi/exams/ielts_full.json").read_text(encoding="utf-8"))
        cls.formats = json.loads((ROOT / "config/exam_formats.json").read_text(encoding="utf-8"))["formats"]

    def test_three_papers_have_three_passages_and_forty_items(self):
        self.assertEqual(9, len(self.bank["groups"]))
        self.assertEqual(120, sum(len(group["items"]) for group in self.bank["groups"]))
        for paper in map(str, range(1, 4)):
            groups = [group for group in self.bank["groups"] if group["set"] == paper]
            self.assertEqual({"reading1", "reading2", "reading3"}, {group["section"] for group in groups})
            self.assertEqual(40, sum(len(group["items"]) for group in groups))
            self.assertEqual({13, 14}, {len(group["items"]) for group in groups})

    def test_items_are_originally_distinct_and_localised(self):
        groups = self.bank["groups"]
        item_ids = [item["id"] for group in groups for item in group["items"]]
        self.assertEqual(120, len(set(item_ids)))
        self.assertEqual(9, len({group["passage"] for group in groups}))
        for group in groups:
            self.assertIn(group["id"], self.locale)
            self.assertTrue(self.locale[group["id"]]["title"])
            self.assertTrue(self.locale[group["id"]]["passage"])
            stems = [item["stem"] for item in group["items"]]
            self.assertEqual(len(stems), len(set(stems)))
            for item in group["items"]:
                self.assertEqual("ielts_full_reading", item["qtype"])
                self.assertEqual(4, len(item["opts"]))
                self.assertEqual(4, len(set(item["opts"])))
                self.assertIn(item["ans"], range(4))
                self.assertIn(item["id"], self.locale)
                self.assertTrue(self.locale[item["id"]]["expl"])

    def test_formats_select_one_disjoint_paper_each(self):
        formats = [item for item in self.formats if item["id"].startswith("ielts_reading_full_")]
        self.assertEqual(3, len(formats))
        for number, item in enumerate(formats, 1):
            self.assertEqual(40, item["questionCount"])
            self.assertEqual(60, item["timeLimitMinutes"])
            self.assertEqual(40, sum(section["count"] for section in item["sections"]))
            self.assertEqual({str(number)}, {section["groupSet"] for section in item["sections"]})
            self.assertEqual({"ielts_full"}, {section["bank"] for section in item["sections"]})
            self.assertEqual({13, 14}, {section["count"] for section in item["sections"]})


if __name__ == "__main__":
    unittest.main()
