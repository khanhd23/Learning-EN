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

        ui_dir = self.root / "content/i18n/xx"
        ui_dir.joinpath("ui.json").write_text(json.dumps({"hello": "Hola"}, ensure_ascii=False), encoding="utf-8")
        status = json.loads(ui_dir.joinpath("status.json").read_text(encoding="utf-8"))
        status.setdefault("entries", {})["ui"] = {"hello": {"s": "approved"}}
        ui_dir.joinpath("status.json").write_text(json.dumps(status), encoding="utf-8")
        built = locale_tool.cmd_build_ui(self.root, "xx")
        self.assertEqual(built.name, "strings.xml")
        self.assertIn('name="hello">Hola</string>', built.read_text(encoding="utf-8"))


if __name__ == "__main__":
    unittest.main()


class DraftSources(unittest.TestCase):
    """Drafts come from English only; nothing is drafted where no English source exists."""

    def setUp(self):
        self.calls = []
        self.original = locale_tool.translate_text

        def fake(text, source, locale, provider, api_key, context=""):
            self.calls.append((text, context))
            return f"<{text}>" if text.strip() else ""

        locale_tool.translate_text = fake

    def tearDown(self):
        locale_tool.translate_text = self.original

    def test_word_gloss_translates_headword_with_context(self):
        item = {"kind": "words", "lemma": "bank",
                "source": {"def": "a business that keeps money", "ex": [{"id": "bank_s1", "text": "I went to the bank."}]}}
        value = locale_tool.draft_value(item, "es", "deepl", "key")
        self.assertEqual(value["g"], "<bank>")
        self.assertIn(("bank", "a business that keeps money I went to the bank."), self.calls)
        self.assertEqual(value["ex"]["bank_s1"], "<I went to the bank.>")

    def test_grammar_draft_translates_english_source_fields_only(self):
        item = {"kind": "grammar", "source": {
            "title": "The title", "when": "When to use it", "body": "How it works",
            "formula": "S + V", "signals": ["always"],
            "examples": [{"en": "I am a student.", "hl": "am"}],
            "mistakes": [{"wrong": "She are here.", "right": "She is here.", "why": "The subject is singular."}]
        }}
        value = locale_tool.draft_value(item, "es", "deepl", "key")
        self.assertEqual(value["examples"], ["<I am a student.>"])
        self.assertEqual(value["title"], "<The title>")
        self.assertEqual(value["when"], "<When to use it>")
        self.assertEqual(value["body"], "<How it works>")
        self.assertEqual(value["mistakes"], ["<The subject is singular.>"])
        sent = [text for text, _ in self.calls]
        self.assertNotIn("S + V", sent)
        self.assertNotIn("always", sent)
        self.assertNotIn("She are here.", sent)
        self.assertNotIn("She is here.", sent)

    def test_question_passage_and_topic_drafts_use_only_english_explanations(self):
        question = {"kind": "questions", "source": {
            "stem": "Choose ___", "opts": ["one", "two"], "expl": "The first option fits."
        }}
        self.assertEqual(locale_tool.draft_value(question, "es", "deepl", "key"), "<The first option fits.>")
        self.assertEqual(self.calls[-1][0], "The first option fits.")

        passage = {"kind": "passages", "source": {"blanks": [
            {"expl": "First blank."}, {"expl": ""}
        ]}}
        self.assertEqual(locale_tool.draft_value(passage, "es", "deepl", "key"),
                         {"blanks": ["<First blank.>", ""]})
        topic = {"kind": "topics", "source": {"name": "Daily life"}}
        self.assertEqual(locale_tool.draft_value(topic, "es", "deepl", "key"), "<Daily life>")

    def test_no_english_source_means_no_draft(self):
        self.assertEqual(locale_tool.draft_value({"kind": "questions", "source": {"stem": "Lack of sleep can ___."}}, "es", "deepl", "k"), "")
        self.assertEqual(locale_tool.draft_value({"kind": "ui", "source": "Nội dung học"}, "es", "deepl", "k"), "")
        self.assertFalse(locale_tool.has_text({"g": "", "ex": {"a": ""}}))
