import importlib.util
import json
import sqlite3
import tempfile
import unittest
from pathlib import Path


MODULE_PATH = Path(__file__).resolve().parents[1] / "build_content_db.py"
spec = importlib.util.spec_from_file_location("build_content_db", MODULE_PATH)
build_content_db = importlib.util.module_from_spec(spec)
spec.loader.exec_module(build_content_db)


class ContentDbBuilderTest(unittest.TestCase):
    def test_builds_hash_schema_and_normalized_fts(self):
        with tempfile.TemporaryDirectory() as raw:
            root = Path(raw)
            (root / "content/en").mkdir(parents=True)
            (root / "config").mkdir()
            self.write(root / "content/en/words.json", {
                "topics": [{"id": "daily", "domain": "daily", "icon": "x", "hue": 1, "name": "Daily"}],
                "words": [{"id": "go", "lemma": "đi", "level": 1, "tier": "silver", "topics": ["daily"],
                            "senses": [{"id": "go_s1", "pos": "v", "def": "move", "ex": [{"id": "go_e1", "text": "Go now."}]}]}],
                "confusables": []
            })
            self.write(root / "content/en/grammar.json", {"points": []})
            self.write(root / "content/en/questions.json", {"questions": [], "passages": []})
            self.write(root / "content/en/relations.json", {})
            self.write(root / "config/exam_formats.json", {"formats": []})
            output = root / "build/content.db"
            digest = build_content_db.build(root, output)

            db = sqlite3.connect(output)
            try:
                self.assertEqual(db.execute("select value from meta where key='schema_version'").fetchone()[0], "1")
                self.assertEqual(db.execute("select value from meta where key='content_hash'").fetchone()[0], digest)
                self.assertEqual(db.execute("select count(*) from word").fetchone()[0], 1)
                self.assertEqual(db.execute("select word_id from word_fts where word_fts match 'di*'").fetchone()[0], "go")
            finally:
                db.close()

    @staticmethod
    def write(path, value):
        path.write_text(json.dumps(value, ensure_ascii=False), encoding="utf-8")


if __name__ == "__main__":
    unittest.main()
