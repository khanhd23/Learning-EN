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
                self.assertEqual(db.execute("select value from meta where key='schema_version'").fetchone()[0], "4")
                self.assertEqual(db.execute("select value from meta where key='content_hash'").fetchone()[0], digest)
                self.assertEqual(db.execute("select count(*) from word").fetchone()[0], 1)
                self.assertEqual(db.execute("select word_id from word_fts where word_fts match 'di*'").fetchone()[0], "go")
            finally:
                db.close()

    @staticmethod
    def write(path, value):
        path.write_text(json.dumps(value, ensure_ascii=False), encoding="utf-8")


    def test_hash_sidecar_is_written(self):
        with tempfile.TemporaryDirectory() as raw:
            root = Path(raw)
            self.fixture(root, [{"id": "one", "lemma": "one", "senses": []}])
            output = root / "build/content.db"
            sidecar = root / "build/content.db.sha256"
            digest = build_content_db.build(root, output, sidecar)
            self.assertEqual(sidecar.read_text(encoding="ascii").strip(), digest)

    def test_blocked_sense_is_marked_adult(self):
        with tempfile.TemporaryDirectory() as raw:
            root = Path(raw)
            self.fixture(root, [{"id": "adult_word", "lemma": "adultword", "senses": [{"id": "adult_s1"}]}])
            (root / "config/blocked_senses.txt").write_text("adult_s1\n", encoding="utf-8")
            output = root / "build/content.db"
            build_content_db.build(root, output)
            db = sqlite3.connect(output)
            try:
                self.assertEqual(db.execute("select adult from word where id='adult_word'").fetchone()[0], 1)
            finally:
                db.close()

    def test_sql_ranks_exact_match_before_limit_with_more_than_200_hits(self):
        with tempfile.TemporaryDirectory() as raw:
            root = Path(raw)
            words = [{"id": f"filler_{i}", "lemma": f"gopher{i}", "level": 1, "tier": "bronze",
                      "senses": [{"id": f"filler_{i}_s1", "pos": "n", "def": "go", "ex": []}]}
                     for i in range(250)]
            words.append({"id": "exact_go", "lemma": "go", "level": 1, "tier": "gold",
                          "senses": [{"id": "exact_go_s1", "pos": "v", "def": "move", "ex": []}]})
            self.fixture(root, words)
            output = root / "build/content.db"
            build_content_db.build(root, output)
            db = sqlite3.connect(output)
            try:
                rows = db.execute(
                    "SELECT w.id FROM word_fts f JOIN word w ON w.id=f.word_id "
                    "WHERE f.locale='en' AND word_fts MATCH ? "
                    "ORDER BY CASE WHEN w.lemma_norm=? THEN 0 WHEN w.lemma_norm LIKE ? || '%' THEN 1 "
                    "WHEN instr(f.gloss, ?) > 0 THEN 2 ELSE 3 END, w.level, "
                    "CASE w.tier WHEN 'gold' THEN 0 WHEN 'silver' THEN 1 ELSE 2 END, "
                    "CASE WHEN w.ngsl_rank IS NULL THEN 2147483647 ELSE w.ngsl_rank END, w.lemma_norm LIMIT ?",
                    ("go*", "go", "go", "go", 30),
                ).fetchall()
                self.assertEqual(len(rows), 30)
                self.assertEqual(rows[0][0], "exact_go")
            finally:
                db.close()

    def test_preserves_raw_gloss_for_accent_exact_matching(self):
        with tempfile.TemporaryDirectory() as raw:
            root = Path(raw)
            self.fixture(root, [
                {"id": "fish", "lemma": "fish", "level": 1, "tier": "gold",
                 "senses": [{"id": "fish_s1", "pos": "n", "def": "cá", "ex": []}]},
                {"id": "coffee", "lemma": "coffee", "level": 1, "tier": "gold",
                 "senses": [{"id": "coffee_s1", "pos": "n", "def": "cà phê", "ex": []}]},
            ])
            output = root / "build/content.db"
            build_content_db.build(root, output)
            db = sqlite3.connect(output)
            try:
                self.assertEqual(db.execute("select gloss_raw from word_fts where word_id='fish'").fetchone()[0], "cá")
                rows = db.execute(
                    "SELECT word_id FROM word_fts WHERE locale='en' AND word_fts MATCH ? "
                    "ORDER BY CASE WHEN lower(gloss_raw)=? THEN 0 ELSE 1 END",
                    ("ca*", "cá"),
                ).fetchall()
                self.assertEqual(rows[0][0], "fish")
            finally:
                db.close()

    @staticmethod
    def fixture(root, words):
        (root / "content/en").mkdir(parents=True, exist_ok=True)
        (root / "config").mkdir(exist_ok=True)
        ContentDbBuilderTest.write(root / "content/en/words.json", {"topics": [], "words": words})
        ContentDbBuilderTest.write(root / "content/en/grammar.json", {"points": []})
        ContentDbBuilderTest.write(root / "content/en/questions.json", {"questions": [], "passages": []})
        ContentDbBuilderTest.write(root / "content/en/relations.json", {})
        ContentDbBuilderTest.write(root / "config/exam_formats.json", {"formats": []})

if __name__ == "__main__":
    unittest.main()
