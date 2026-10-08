import json
import sys
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import build_toeic_bank  # noqa: E402


class ToeicBankTest(unittest.TestCase):
    def test_generated_bank_has_full_sections_and_locale_explanations(self):
        root = Path(__file__).resolve().parents[2]
        bank = json.loads((root / "content/en/exams/toeic.json").read_text(encoding="utf-8"))
        vi = json.loads((root / "content/i18n/vi/exams/toeic.json").read_text(encoding="utf-8"))
        self.assertGreaterEqual(len(bank["items"]), 300)
        self.assertEqual(len(bank["groups"]), 40)
        self.assertTrue(all(len(item["opts"]) == 4 and 0 <= item["ans"] < 4 for item in bank["items"]))
        self.assertTrue(all(len(group["items"]) == 4 for group in bank["groups"]))
        nested = [item for group in bank["groups"] for item in group["items"]]
        self.assertEqual(len(nested), 160)
        self.assertEqual(len({item["id"] for item in bank["items"] + nested}), len(bank["items"]) + len(nested))
        self.assertTrue(all(value.get("expl") for value in vi.values()))

    def test_builder_rejects_short_or_incomplete_source(self):
        with self.assertRaises(ValueError):
            build_toeic_bank.build_bank({"questions": [], "passages": []}, {"q": {}, "passages": {}})


if __name__ == "__main__":
    unittest.main()
