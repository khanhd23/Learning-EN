import json
import unittest
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]


def read_json(path):
    return json.loads(path.read_text(encoding="utf-8"))


class ListeningPaperTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.config = {f["id"]: f for f in read_json(ROOT / "config" / "exam_formats.json")["formats"]}

    def assert_papers(self, bank_name, format_prefix, parts, total, options):
        bank = read_json(ROOT / "content" / "en" / "exams" / bank_name)
        all_ids = [item["id"] for group in bank["groups"] for item in group["items"]]
        self.assertEqual(len(all_ids), len(set(all_ids)), bank_name)
        for set_id in ("1", "2", "3"):
            papers = [f for f in self.config.values() if f["id"].startswith(format_prefix) and f["id"].endswith("_" + set_id)]
            self.assertEqual(1, len(papers), (format_prefix, set_id))
            fmt = papers[0]
            self.assertEqual(total, fmt["questionCount"], fmt["id"])
            self.assertEqual(total, sum(section["count"] for section in fmt["sections"]), fmt["id"])
            selected = []
            for section in fmt["sections"]:
                self.assertEqual(parts[section["id"]], section["count"], (fmt["id"], section["id"]))
                self.assertEqual(bank_name[:-5], section["bank"])
                self.assertEqual([section["qtype"][0]], section["qtype"])
                groups = [g for g in bank["groups"] if g["set"] == set_id and g["section"] == section["id"]]
                self.assertTrue(groups, (fmt["id"], section["id"]))
                items = [item for group in groups for item in group["items"]]
                self.assertEqual(section["count"], len(items), (fmt["id"], section["id"]))
                for group in groups:
                    self.assertTrue(group.get("audioPassage"), group["id"])
                    self.assertEqual(group["passage"], group["audioPassage"], group["id"])
                    for item in group["items"]:
                        self.assertEqual(group["section"], item["section"])
                        self.assertEqual(section["qtype"][0], item["qtype"])
                        self.assertIn(item["ans"], range(len(item["opts"])), item["id"])
                        self.assertEqual(options, len(item["opts"]), item["id"])
                        selected.append(item["id"])
            self.assertEqual(total, len(selected), fmt["id"])

    def test_ielts_three_four_part_papers(self):
        self.assert_papers("ielts_listening.json", "ielts_listening", {
            "listening1": 10, "listening2": 10, "listening3": 10, "listening4": 10,
        }, 40, 4)

    def test_vstep_three_part_papers(self):
        self.assert_papers("vstep_listening.json", "vstep_listening", {
            "listening1": 8, "listening2": 12, "listening3": 15,
        }, 35, 4)

    def test_ket_and_pet_three_papers(self):
        self.assert_papers("ket_listening.json", "ket_listening", {
            "listening1": 5, "listening2": 5, "listening3": 5, "listening4": 5, "listening5": 5,
        }, 25, 3)
        self.assert_papers("pet_listening.json", "pet_listening", {
            "listening1": 7, "listening2": 6, "listening3": 6, "listening4": 6,
        }, 25, 3)

    def test_toeic_three_full_listening_papers_use_existing_disjoint_sets(self):
        for set_id in ("1", "2", "3"):
            fmt = self.config[f"toeic_listening_{set_id}"]
            self.assertEqual(100, fmt["questionCount"])
            self.assertEqual([6, 25, 39, 30], [section["count"] for section in fmt["sections"]])
            self.assertEqual(["toeic_p1", "toeic_p2", "toeic_p3", "toeic_p4"], [section["bank"] for section in fmt["sections"]])
            self.assertTrue(all(section["groupSet"] == set_id for section in fmt["sections"]))


if __name__ == "__main__":
    unittest.main()
