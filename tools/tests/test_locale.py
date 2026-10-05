import csv
import importlib.util
import json
import tempfile
import unittest
from pathlib import Path


MODULE_PATH = Path(__file__).resolve().parents[1] / "locale.py"
spec = importlib.util.spec_from_file_location("locale_tool", MODULE_PATH)
locale_tool = importlib.util.module_from_spec(spec)
spec.loader.exec_module(locale_tool)


class LocaleCycle(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.root = Path(self.tmp.name)
        (self.root / "content/en").mkdir(parents=True)
        (self.root / "content/i18n/vi").mkdir(parents=True)
        (self.root / "app/src/main/res/values").mkdir(parents=True)
        self.write("content/en/words.json", {"topics": [{"id": "daily"}], "words": [{"id": "teach", "lemma": "teach", "level": 1, "senses": [{"id": "teach_s1", "def": "to teach", "ex": [{"id": "teach_s1", "text": "Teach Sam."}]}]}], "confusables": []})
        self.write("content/en/grammar.json", {"points": []})
        self.write("content/en/questions.json", {"questions": [], "passages": []})
        self.write("content/i18n/vi/pet.json", {})
        self.write("content/i18n/vi/tips.json", {})
        (self.root / "app/src/main/res/values/strings.xml").write_text("<resources><string name=\"hello\">Hello</string></resources>", encoding="utf-8")
        tools = self.root / "tools"
        tools.mkdir()
        source_validator = Path(__file__).resolve().parents[1] / "validate_content.py"
        (tools / "validate_content.py").write_text(source_validator.read_text(encoding="utf-8"), encoding="utf-8")

    def tearDown(self):
        self.tmp.cleanup()

    def write(self, name, value):
        path = self.root / name
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(json.dumps(value, ensure_ascii=False), encoding="utf-8")

    def test_new_none_export_import_approve_check_report(self):
        created = locale_tool.cmd_new(self.root, "xx")
        self.assertEqual(created["status"], "missing")
        drafted = locale_tool.cmd_draft(self.root, "xx", 1, None, "none")
        self.assertEqual(drafted["drafted"], 1)
        pack = json.loads((self.root / "content/i18n/xx/words.json").read_text(encoding="utf-8"))
        self.assertEqual(pack["teach_s1"]["g"], "")
        paths = locale_tool.cmd_export(self.root, "xx", 1, "draft")
        self.assertEqual(len(paths), 1)
        with paths[0].open(encoding="utf-8", newline="") as csv_file:
            rows = list(csv.DictReader(csv_file))
        self.assertEqual(rows[0]["key"], "words:teach_s1")
        rows[0]["corrected"] = json.dumps({"g": "dạy", "ex": {"teach_s1": "Dạy Sam."}}, ensure_ascii=False)
        with paths[0].open("w", encoding="utf-8", newline="") as f:
            writer = csv.DictWriter(f, fieldnames=rows[0].keys())
            writer.writeheader(); writer.writerows(rows)
        imported = locale_tool.cmd_import(self.root, "xx", paths[0], "reviewer")
        self.assertEqual(imported["imported"], 1)
        approved = locale_tool.cmd_approve(self.root, "xx", 1, "words")
        self.assertEqual(approved["approved"], 1)
        self.assertEqual(locale_tool.cmd_check(self.root, "xx"), 0)
        report = locale_tool.cmd_report(self.root)
        self.assertTrue(report.is_file())
        text = report.read_text(encoding="utf-8")
        self.assertIn("xx", text)
        self.assertIn("| 1 | words | 0 (0.0%) | 0 (0.0%) | 0 (0.0%) | 1 (100.0%) | 0 |", text)


if __name__ == "__main__":
    unittest.main()
