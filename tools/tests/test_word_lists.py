import sys
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
from word_lists import load_all_lists, match_cefr  # noqa: E402


class WordListMetadataTest(unittest.TestCase):
    def test_cefr_prefers_matching_pos_before_lowest_level(self):
        rows = {"record": [("noun", "A1"), ("verb", "B2"), ("adverb", "C1")]}
        self.assertEqual(match_cefr("record", "v", rows), "B2")

    def test_cefr_uses_lowest_level_when_pos_is_absent(self):
        rows = {"record": [("noun", "B2"), ("adverb", "A2")]}
        self.assertEqual(match_cefr("record", "verb", rows), "A2")

    def test_list_loader_ignores_source_prose(self):
        lists = load_all_lists()
        self.assertIn("ngsl", lists)
        self.assertIn("abide", lists["tsl"])
        self.assertNotIn("toeic service list ver 1.2", lists["tsl"])


if __name__ == "__main__":
    unittest.main()
