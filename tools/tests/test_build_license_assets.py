import importlib.util
import tempfile
import unittest
import zipfile
from pathlib import Path


MODULE_PATH = Path(__file__).resolve().parents[1] / "build_license_assets.py"
spec = importlib.util.spec_from_file_location("build_license_assets", MODULE_PATH)
build_license_assets = importlib.util.module_from_spec(spec)
spec.loader.exec_module(build_license_assets)


class LicenseAssetBuilderTest(unittest.TestCase):
    def test_extracts_license_text_and_uses_apache_for_androidx(self):
        with tempfile.TemporaryDirectory() as raw:
            root = Path(raw)
            (root / "app/src/main/assets/licenses").mkdir(parents=True)
            (root / "app/src/main/assets/licenses/apache-2.0.txt").write_text("Apache full text", encoding="utf-8")
            archive = root / "library.aar"
            with zipfile.ZipFile(archive, "w") as z:
                z.writestr("META-INF/LICENSE.txt", "Library license")
            output = root / "generated/licenses/dependencies.txt"
            result = build_license_assets.build(root, output, [f"androidx.test:sample:1={archive}"])
            text = output.read_text(encoding="utf-8")
            self.assertEqual(result["artifacts"], 1)
            self.assertEqual(result["license_texts"], 1)
            self.assertIn("Library license", text)

    def test_apache_fallback_is_deduplicated(self):
        with tempfile.TemporaryDirectory() as raw:
            root = Path(raw)
            (root / "app/src/main/assets/licenses").mkdir(parents=True)
            (root / "app/src/main/assets/licenses/apache-2.0.txt").write_text("Apache full text", encoding="utf-8")
            first = root / "one.jar"
            second = root / "two.jar"
            first.touch(); second.touch()
            output = root / "generated/licenses/dependencies.txt"
            result = build_license_assets.build(root, output, [f"androidx.one:one:1={first}", f"org.jetbrains.kotlin:two:1={second}"])
            self.assertEqual(result["license_texts"], 1)
            self.assertEqual(output.read_text(encoding="utf-8").count("Apache full text"), 1)

    def test_identical_text_lists_all_artifacts_once(self):
        with tempfile.TemporaryDirectory() as raw:
            root = Path(raw)
            (root / "app/src/main/assets/licenses").mkdir(parents=True)
            (root / "app/src/main/assets/licenses/apache-2.0.txt").write_text("Apache full text", encoding="utf-8")
            first = root / "one.aar"
            second = root / "two.aar"
            for archive in (first, second):
                with zipfile.ZipFile(archive, "w") as z:
                    z.writestr("META-INF/LICENSE.txt", "Same license text")
            output = root / "generated/licenses/dependencies.txt"
            result = build_license_assets.build(root, output, [
                f"one:one:1={first}", f"two:two:1={second}",
            ])
            text = output.read_text(encoding="utf-8")
            self.assertEqual(result["license_texts"], 1)
            self.assertEqual(text.count("Same license text"), 1)
            self.assertIn("one:one:1", text)
            self.assertIn("two:two:1", text)


if __name__ == "__main__":
    unittest.main()
