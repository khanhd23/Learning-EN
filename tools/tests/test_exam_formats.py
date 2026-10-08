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


if __name__ == "__main__":
    unittest.main()
