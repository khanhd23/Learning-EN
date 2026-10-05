import json
import sys
import tempfile
import unittest
from pathlib import Path

sys.path.insert(0, str(Path(__file__).resolve().parents[1]))
import validate_content  # noqa: E402


class Task3ValidationFixtures(unittest.TestCase):
    def setUp(self):
        self.tmp = tempfile.TemporaryDirectory()
        self.root = Path(self.tmp.name)
        (self.root / "content/en").mkdir(parents=True)
        (self.root / "content/i18n/xx").mkdir(parents=True)
        (self.root / "app/src/main/res/values").mkdir(parents=True)
        self.write("content/en/words.json", {
            "topics": [{"id": "daily"}],
            "words": [{"id": "teach", "lemma": "teach", "senses": [{
                "id": "teach_s1", "def": "teach {name}",
                "ex": [{"id": "teach_s1", "text": "Teach {name}."}]
            }]}],
            "confusables": []
        })
        self.write("content/en/grammar.json", {"points": []})
        self.write("content/en/questions.json", {"questions": [], "passages": []})
        (self.root / "app/src/main/res/values/strings.xml").write_text(
            "<resources><string name=\"hello\">Hello</string></resources>", encoding="utf-8"
        )
        self.write("content/i18n/xx/status.json", {"entries": {}})

    def tearDown(self):
        self.tmp.cleanup()

    def write(self, relative, value):
        path = self.root / relative
        path.parent.mkdir(parents=True, exist_ok=True)
        path.write_text(json.dumps(value, ensure_ascii=False), encoding="utf-8")

    def check(self, files):
        for name, value in files.items():
            self.write("content/i18n/xx/" + name, value)
        return "\n".join(validate_content.locale_rule_errors(str(self.root))[0])

    def test_unknown_id_fixture(self):
        self.assertIn("unknown or orphan ID", self.check({"words.json": {"missing_s1": {"g": "x"}}}))

    def test_placeholder_fixture(self):
        self.assertIn("placeholders/markup differ", self.check({"words.json": {"teach_s1": {"g": "enseñar", "ex": {"teach_s1": "Teach {wrong}."}}}}))

    def test_keep_fixture(self):
        message = self.check({"words.json": {"teach_s1": {"g": "<keep>Teach</keep>"}}})
        self.assertIn("leftover <keep> tag", message)

    def test_duplicate_gloss_fixture(self):
        self.assertIn("duplicate comma/semicolon", self.check({"words.json": {"teach_s1": {"g": "a, a"}}}))

    def test_vietnamese_fixture(self):
        self.assertIn("Vietnam/Vietnamese", self.check({"words.json": {"teach_s1": {"g": "Vietnamese"}}}))

    def test_stale_fixture(self):
        source = {"id": "teach_s1", "def": "teach {name}", "ex": [{"id": "teach_s1", "text": "Teach {name}."}]}
        self.write("content/i18n/xx/words.json", {"teach_s1": {"g": "enseñar"}})
        self.write("content/i18n/xx/status.json", {"entries": {"words": {"teach_s1": {"s": "approved", "src": "old"}}}})
        self.assertIn("approved entry is stale", "\n".join(validate_content.locale_rule_errors(str(self.root))[0]))

    def test_ui_key_fixture(self):
        self.assertIn("keys differ from strings.xml", self.check({"ui.json": {}}))

    def test_generator_refreshes_changed_vi_status(self):
        status_path = self.root / "status.json"
        validate_content_status = {"entries": {"words": {"teach_s1": {"s": "approved", "by": "legacy-vi", "src": "old"}}}}
        status_path.write_text(json.dumps(validate_content_status), encoding="utf-8")
        # Import the generator helper without executing its main routine.
        import gen_content
        gen_content.refresh_vi_status(str(status_path), validate_content_status, {"words": {"teach_s1": {"def": "new"}}})
        result = json.loads(status_path.read_text(encoding="utf-8"))["entries"]["words"]["teach_s1"]
        self.assertEqual(result["s"], "approved")
        self.assertEqual(result["by"], "owner")
        self.assertNotEqual(result["src"], "old")


if __name__ == "__main__":
    unittest.main()
