import json
import sys
import unittest
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / "tools"))
import build_toeic_bank  # noqa: E402


class OriginalPart7BankTest(unittest.TestCase):
    def test_preview_is_original_and_complete(self):
        source = build_toeic_bank.read_authoring_p7()
        bank, locale = build_toeic_bank.build_p7(source, set())
        self.assertGreaterEqual(len(source), 54)
        self.assertEqual(len(source), len(bank["groups"]))
        self.assertEqual(sum(len(group["items"]) for group in source), sum(len(group["items"]) for group in bank["groups"]))
        ids = [item["id"] for group in bank["groups"] for item in group["items"]]
        self.assertEqual(len(ids), len(set(ids)))
        self.assertEqual(len(source) + len(ids), len(locale))
        for group in source:
            self.assertTrue(group["source"].startswith("original:"))
            for item in group["items"]:
                self.assertEqual(4, len(item["opts"]))
                self.assertIn(item["ans"], range(4))
                self.assertTrue(item["expl"])
                self.assertTrue(item["vi_expl"])

    def test_three_practice_sets_are_complete_and_disjoint(self):
        bank, _ = build_toeic_bank.build_p7(build_toeic_bank.read_authoring_p7(), set())
        by_set = {}
        for group in bank["groups"]:
            by_set.setdefault(group["set"], []).append(group)
        self.assertEqual({"1", "2", "3"}, set(by_set))
        item_sets = []
        for set_id in ("1", "2", "3"):
            groups = by_set[set_id]
            self.assertEqual(18, len(groups))
            items = [item["id"] for group in groups for item in group["items"]]
            self.assertEqual(54, len(items))
            self.assertEqual(len(items), len(set(items)))
            item_sets.append(set(items))
        self.assertEqual(set(), item_sets[0] & item_sets[1])
        self.assertEqual(set(), item_sets[0] & item_sets[2])
        self.assertEqual(set(), item_sets[1] & item_sets[2])

    def test_generated_preview_matches_source(self):
        path = ROOT / "content/en/exams/toeic_p7.json"
        data = json.loads(path.read_text(encoding="utf-8"))
        self.assertEqual(len(build_toeic_bank.read_authoring_p7()), len(data["groups"]))
        self.assertEqual(sum(len(group["items"]) for group in build_toeic_bank.read_authoring_p7()), sum(len(group["items"]) for group in data["groups"]))
        self.assertTrue(all(group["section"] == "p7" for group in data["groups"]))
        self.assertEqual({"1", "2", "3"}, {group["set"] for group in data["groups"]})

    def test_full_practice_section_can_select_54_questions_by_group(self):
        data = json.loads((ROOT / "content/en/exams/toeic_p7.json").read_text(encoding="utf-8"))
        sizes = [len(group["items"]) for group in data["groups"]]
        reachable = {0}
        for size in sizes:
            reachable |= {total + size for total in tuple(reachable) if total + size <= 54}
        self.assertIn(54, reachable)
        self.assertGreaterEqual(sum(sizes), 54)


if __name__ == "__main__":
    unittest.main()
