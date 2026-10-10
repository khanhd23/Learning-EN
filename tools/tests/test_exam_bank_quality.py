import json
import re
import unittest
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]


def read_json(path):
    return json.loads(path.read_text(encoding="utf-8"))


def normalized(value):
    return re.sub(r"\s+", " ", value).strip().lower()


class ExamBankQualityTest(unittest.TestCase):
    def test_every_exam_bank_has_unique_valid_items_and_groups(self):
        for path in sorted((ROOT / "content" / "en" / "exams").glob("*.json")):
            bank = read_json(path)
            ids = set()
            passages = set()
            for group in bank.get("groups", []):
                self.assertTrue(group.get("id"), path.name)
                self.assertNotIn(group["id"], ids, f"{path.name}: duplicate group {group['id']}")
                ids.add(group["id"])
                passage = normalized(group.get("passage", ""))
                self.assertTrue(passage, f"{path.name}: empty passage {group['id']}")
                self.assertNotIn(passage, passages, f"{path.name}: duplicate passage {group['id']}")
                passages.add(passage)
                group_stems = set()
                for item in group.get("items", []):
                    self._check_item(path.name, item, ids)
                    stem = normalized(item["stem"])
                    self.assertNotIn(stem, group_stems, f"{path.name}: duplicate stem in {group['id']}")
                    group_stems.add(stem)
            for item in bank.get("items", []):
                self._check_item(path.name, item, ids)

    def _check_item(self, bank_name, item, ids):
        item_id = item.get("id", "")
        self.assertTrue(item_id, bank_name)
        self.assertNotIn(item_id, ids, f"{bank_name}: duplicate item {item_id}")
        ids.add(item_id)
        self.assertTrue(item.get("stem"), f"{bank_name}: empty stem {item_id}")
        options = item.get("opts", [])
        answer = item.get("ans")
        if item.get("answerText"):
            self.assertEqual([], options, f"{bank_name}: typed item has options {item_id}")
            self.assertEqual(-1, answer, f"{bank_name}: typed item answer index {item_id}")
        else:
            self.assertIn(len(options), (3, 4), f"{bank_name}: option count {item_id}")
            self.assertIsInstance(answer, int, f"{bank_name}: non-integer answer {item_id}")
            self.assertGreaterEqual(answer, 0, f"{bank_name}: answer below range {item_id}")
            self.assertLess(answer, len(options), f"{bank_name}: answer above range {item_id}")
            self.assertEqual(len(options), len({normalized(option) for option in options}), f"{bank_name}: duplicate options {item_id}")
        self.assertTrue(item.get("expl"), f"{bank_name}: empty explanation {item_id}")

    def test_three_full_toeic_reading_papers_are_exact_and_disjoint(self):
        config = read_json(ROOT / "config" / "exam_formats.json")
        toeic = read_json(ROOT / "content" / "en" / "exams" / "toeic.json")
        p7 = read_json(ROOT / "content" / "en" / "exams" / "toeic_p7.json")
        banks = {"toeic": toeic, "toeic_p7": p7}
        selected = {}
        for fmt in (f for f in config["formats"] if f["id"].startswith("toeic_reading_")):
            item_ids = []
            for section in fmt["sections"]:
                bank = banks[section["bank"]]
                set_id = section.get("groupSet")
                if section["itemType"] == "mcq":
                    items = [
                        item for item in bank.get("items", [])
                        if item.get("section") == section["id"] and (set_id is None or item.get("set") == set_id)
                    ]
                else:
                    groups = [
                        group for group in bank.get("groups", [])
                        if group.get("section") == section["id"] and (set_id is None or group.get("set") == set_id)
                    ]
                    items = [item for group in groups for item in group.get("items", [])]
                self.assertEqual(section["count"], len(items), f"{fmt['id']} {section['id']}")
                item_ids.extend(item["id"] for item in items)
            self.assertEqual(100, len(item_ids), fmt["id"])
            self.assertEqual(100, len(set(item_ids)), fmt["id"])
            selected[fmt["id"]] = set(item_ids)
        ids = list(selected)
        for index, left in enumerate(ids):
            for right in ids[index + 1:]:
                self.assertFalse(selected[left] & selected[right], f"{left}/{right} share items")


if __name__ == "__main__":
    unittest.main()
