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
        self.assertGreaterEqual(len(source), 15)
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

    def test_generated_preview_matches_source(self):
        path = ROOT / "content/en/exams/toeic_p7.json"
        data = json.loads(path.read_text(encoding="utf-8"))
        self.assertEqual(len(build_toeic_bank.read_authoring_p7()), len(data["groups"]))
        self.assertEqual(sum(len(group["items"]) for group in build_toeic_bank.read_authoring_p7()), sum(len(group["items"]) for group in data["groups"]))
        self.assertTrue(all(group["section"] == "p7" for group in data["groups"]))


if __name__ == "__main__":
    unittest.main()
