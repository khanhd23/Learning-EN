import json
import unittest
from pathlib import Path


class ExamFormatConfigTest(unittest.TestCase):
    def test_thpt_format_matches_official_exam_shape(self):
        path = Path(__file__).resolve().parents[2] / "config" / "exam_formats.json"
        data = json.loads(path.read_text(encoding="utf-8"))
        fmt = next(item for item in data["formats"] if item["id"] == "thpt")
        self.assertEqual(fmt["questionCount"], 40)
        self.assertEqual(fmt["timeLimitMinutes"], 50)
        self.assertEqual(fmt["optionsPerQuestion"], 4)
        self.assertEqual(sum(section["count"] for section in fmt["sections"]), 40)
        self.assertEqual(
            [section["id"] for section in fmt["sections"]],
            ["notice", "leaflet", "arrange", "complete", "reading1", "reading2"],
        )
        self.assertTrue(fmt["source"].startswith("https://vqa.moet.gov.vn/"))
        self.assertTrue(all(section["bank"] == "thpt" for section in fmt["sections"]))

    def test_thpt_has_five_disjoint_paper_configs(self):
        path = Path(__file__).resolve().parents[2] / "config" / "exam_formats.json"
        data = json.loads(path.read_text(encoding="utf-8"))
        papers = [
            item for item in data["formats"]
            if item["id"] == "thpt" or item["id"].startswith("thpt_")
        ]
        self.assertEqual(["thpt", "thpt_2", "thpt_3", "thpt_4", "thpt_5"], [item["id"] for item in papers])
        for number, paper in enumerate(papers, start=1):
            self.assertEqual(40, paper["questionCount"])
            self.assertEqual(50, paper["timeLimitMinutes"])
            self.assertEqual([str(number)] * 6, [section["groupSet"] for section in paper["sections"]])

    def test_thpt_bank_contains_five_complete_papers(self):
        root = Path(__file__).resolve().parents[2]
        bank = json.loads((root / "content" / "en" / "exams" / "thpt.json").read_text(encoding="utf-8"))
        for number in range(1, 6):
            prefix = f"p{number}_"
            item_ids = [item["id"] for item in bank.get("items", []) if item["id"].startswith(prefix)]
            group_items = [
                item
                for group in bank.get("groups", [])
                if group["id"].startswith(prefix)
                for item in group["items"]
            ]
            ids = item_ids + [item["id"] for item in group_items]
            self.assertEqual(40, len(ids), f"p{number}")
            self.assertEqual(40, len(set(ids)), f"p{number}")


if __name__ == "__main__":
    unittest.main()
