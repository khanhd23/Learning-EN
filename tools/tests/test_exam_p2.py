import json
import sys
import unittest
from pathlib import Path


ROOT = Path(__file__).resolve().parents[2]
sys.path.insert(0, str(ROOT / "tools"))
import build_toeic_bank  # noqa: E402


class OriginalPart2BankTest(unittest.TestCase):
    def test_five_papers_are_complete_and_disjoint(self):
        bank, locale = build_toeic_bank.build_p2(build_toeic_bank.read_authoring_p2())
        by_set = {}
        for group in bank["groups"]:
            by_set.setdefault(group["set"], []).append(group)
        self.assertEqual({"1", "2", "3", "4", "5"}, set(by_set))
        paper_items = []
        for set_id in ("1", "2", "3", "4", "5"):
            groups = by_set[set_id]
            self.assertEqual(6, len(groups))
            ids = [item["id"] for group in groups for item in group["items"]]
            self.assertEqual(6, len(ids))
            self.assertEqual(len(ids), len(set(ids)))
            self.assertTrue(all(item["qtype"] == "listen_p2" for group in groups for item in group["items"]))
            paper_items.append(set(ids))
        for i, left in enumerate(paper_items):
            for right in paper_items[i + 1:]:
                self.assertEqual(set(), left & right)
        self.assertEqual(60, len(locale))

    def test_every_item_has_three_responses_and_original_provenance(self):
        source = build_toeic_bank.read_authoring_p2()
        bank, _ = build_toeic_bank.build_p2(source)
        self.assertEqual(30, len(source))
        self.assertEqual(30, len(bank["groups"]))
        for group in source:
            self.assertTrue(group["source"].startswith("original:"))
            self.assertTrue(group["passage"].endswith("?"))
            self.assertEqual(1, len(group["items"]))
            item = group["items"][0]
            self.assertEqual(3, len(item["opts"]))
            self.assertIn(item["ans"], range(3))
            self.assertTrue(item["expl"])
            self.assertTrue(item["vi_expl"])

    def test_generated_bank_matches_source(self):
        data = json.loads((ROOT / "content/en/exams/toeic_p2.json").read_text(encoding="utf-8"))
        self.assertEqual(30, len(data["groups"]))
        self.assertEqual({"1", "2", "3", "4", "5"}, {group["set"] for group in data["groups"]})
        self.assertTrue(all(len(group["items"]) == 1 for group in data["groups"]))

    def test_config_exposes_each_generated_paper(self):
        formats = json.loads((ROOT / "config/exam_formats.json").read_text(encoding="utf-8"))["formats"]
        papers = [item for item in formats if item["id"].startswith("toeic_p2_practice_")]
        self.assertEqual(5, len(papers))
        self.assertEqual({"1", "2", "3", "4", "5"}, {item["id"][-1] for item in papers})
        for item in papers:
            self.assertEqual(6, item["questionCount"])
            self.assertEqual(3, item["optionsPerQuestion"])
            self.assertEqual("toeic_p2", item["sections"][0]["bank"])
            self.assertEqual("listen_p2", item["sections"][0]["qtype"][0])


if __name__ == "__main__":
    unittest.main()
