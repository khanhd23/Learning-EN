"""Regression checks for preserving earlier learning IDs and bilingual content.

Run: python -m unittest discover -s tools -p test_vocab_expansion.py
"""
import importlib
import json
from pathlib import Path
import sys
import unittest

ROOT = Path(__file__).resolve().parents[1]
sys.path.insert(0, str(ROOT / "tools" / "authoring"))


class VocabularyExpansionTest(unittest.TestCase):
    @classmethod
    def setUpClass(cls):
        cls.bank = json.loads((ROOT / "content/en/words.json").read_text(encoding="utf-8"))
        cls.vi = {
            "topics": json.loads((ROOT / "content/i18n/vi/topics.json").read_text(encoding="utf-8")),
            "words": json.loads((ROOT / "content/i18n/vi/words.json").read_text(encoding="utf-8")),
        }
        cls.words = {w["id"]: w for w in cls.bank["words"]}
        # Reconstruct the earlier word sources without importing the expansion.
        cls.lib = importlib.import_module("lib")
        for module in ("vocab_work", "vocab_life", "vocab_everyday", "vocab_modern", "senses"):
            importlib.import_module(module)

    @classmethod
    def localized_word(cls, word):
        senses = []
        tip = None
        for sense in word["senses"]:
            examples = sense.get("ex", [])
            if isinstance(examples, dict):
                sense_id = sense.get("id") or next(iter(examples), "")
            else:
                sense_id = sense.get("id") or (examples[0].get("id", "") if examples else "")
            value = dict(cls.vi["words"][sense_id])
            if value.get("tip"):
                tip = value.pop("tip")
            senses.append(value)
        result = {"senses": senses}
        if tip:
            result["tip"] = tip
        return result

    def test_earlier_ids_meanings_examples_and_levels_are_preserved(self):
        for old in self.lib.WORDS:
            with self.subTest(word=old["id"]):
                current = self.words[old["id"]]
                for field in ("lemma", "ipa", "level", "topics", "family", "coll"):
                    self.assertEqual(old[field], current[field])
                old_sense_ids = []
                for sense in old["senses"]:
                    examples = sense.get("ex", [])
                    if isinstance(examples, dict):
                        old_sense_ids.append(sense.get("id") or next(iter(examples), ""))
                    else:
                        old_sense_ids.append(sense.get("id") or (examples[0].get("id", "") if examples else ""))
                current_sense_ids = [s["id"] for s in current["senses"]]
                self.assertTrue(set(old_sense_ids).issubset(current_sense_ids))
                for sense_id in old_sense_ids:
                    with self.subTest(sense=sense_id):
                        localized = self.vi["words"][sense_id]
                        self.assertTrue(localized.get("g", "").strip())
                        self.assertIn(sense_id, localized.get("ex", {}))

    def test_foundation_entries_have_complete_bilingual_examples_and_review_flags(self):
        foundation = [w for w in self.words.values() if w.get("source", "").startswith("original:foundation-expansion")]
        self.assertTrue(foundation)
        for word in foundation:
            with self.subTest(word=word["id"]):
                self.assertTrue(word["needs_review"])
                self.assertTrue(word["ipa"])
                self.assertTrue(word["coll"])
                localized = [self.vi["words"][sense["id"]] for sense in word["senses"]]
                self.assertEqual(len(word["senses"]), len(localized))
                for sense, translated in zip(word["senses"], localized):
                    self.assertTrue(translated["g"].strip())
                    self.assertTrue(sense["ex"])
                    for example in sense["ex"]:
                        self.assertTrue(example["text"].strip())
                        self.assertTrue(translated["ex"][example["id"]].strip())

    def test_confusable_links_resolve_to_correct_words_in_both_directions(self):
        for group in self.bank["confusables"]:
            with self.subTest(group=group["id"]):
                self.assertEqual(len(group["words"]), len(group["wordIds"]))
                for lemma, wid in zip(group["words"], group["wordIds"]):
                    self.assertIn(wid, self.words)
                    self.assertEqual(lemma.casefold(), self.words[wid]["lemma"].casefold())
                    self.assertIn(group["id"], self.words[wid]["conf"])

    def test_open_vocabulary_has_provenance_and_relation_index(self):
        imported = [w for w in self.words.values() if w.get("license") == "CC-BY-SA-4.0"]
        self.assertGreaterEqual(len(imported), 1500)
        for word in imported:
            with self.subTest(word=word["id"]):
                self.assertTrue(word.get("needs_review"))
                self.assertTrue(word.get("source", "").startswith("https://vi.wiktionary.org/wiki/"))
                self.assertTrue(word.get("sourceLists"))
                self.assertTrue(word.get("editorialCorrection") in (True, False))
        relations = json.loads((ROOT / "content/en/relations.json").read_text(encoding="utf-8"))
        self.assertIn("families", relations)
        self.assertIn("polysemy", relations)
        self.assertEqual(relations["pronunciation"]["homophones"], [])


if __name__ == "__main__":
    unittest.main()
