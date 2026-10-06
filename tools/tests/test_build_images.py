import importlib.util
import json
import tempfile
import unittest
from pathlib import Path

MODULE_PATH = Path(__file__).resolve().parents[1] / "build_images.py"
spec = importlib.util.spec_from_file_location("build_images", MODULE_PATH)
build_images = importlib.util.module_from_spec(spec)
spec.loader.exec_module(build_images)


class BuildImagesTest(unittest.TestCase):
    def fixture_root(self):
        root = Path(__file__).resolve().parent / "fixtures" / "picture_images"
        temp = Path(tempfile.mkdtemp())
        (temp / "tools/authoring/images").mkdir(parents=True)
        (temp / "tools/authoring/word_images.tsv").write_text((root / "word_images.tsv").read_text(encoding="utf-8"), encoding="utf-8")
        (temp / "third_party/noto-emoji/svg").mkdir(parents=True)
        (temp / "third_party/noto-emoji/svg/emoji_u1f431.svg").write_text((root / "noto-emoji/emoji_u1f431.svg").read_text(encoding="utf-8"), encoding="utf-8")
        (temp / "third_party/noto-emoji/svg/emoji_u1f436.svg").write_text((root / "noto-emoji/emoji_u1f436.svg").read_text(encoding="utf-8"), encoding="utf-8")
        (temp / "third_party/noto-emoji/svg/emoji_u2600_fe0f.svg").write_text((root / "noto-emoji/emoji_u2600_fe0f.svg").read_text(encoding="utf-8"), encoding="utf-8")
        (temp / "tools/authoring/images/tree.svg").write_text((root / "owner/tree.svg").read_text(encoding="utf-8"), encoding="utf-8")
        (temp / "tools/authoring/images/book.svg").write_text((root / "owner/book.svg").read_text(encoding="utf-8"), encoding="utf-8")
        return temp

    def test_builds_emoji_and_owner_svg(self):
        root = self.fixture_root(); out = root / "build"
        result = build_images.build(root, out)
        self.assertEqual(result["mapped"], 5)
        manifest = json.loads((out / "image_manifest.json").read_text(encoding="utf-8"))
        self.assertEqual(set(manifest), {"cat", "dog", "sun", "tree", "book"})
        self.assertTrue((out / "res/drawable-nodpi/word_cat.webp").is_file())

    def test_missing_mapping_fails(self):
        root = Path(tempfile.mkdtemp()); (root / "tools/authoring").mkdir(parents=True)
        (root / "tools/authoring/word_images.tsv").write_text("missing | 1F431 |\n", encoding="utf-8")
        with self.assertRaises((ValueError, RuntimeError)): build_images.build(root, root / "build")

    def test_unsupported_svg_fails(self):
        root = self.fixture_root(); bad = root / "tools/authoring/images/bad.svg"
        bad.write_text("<svg><path", encoding="utf-8")
        (root / "tools/authoring/word_images.tsv").write_text("bad |  | bad.svg\n", encoding="utf-8")
        with self.assertRaises((ValueError, RuntimeError)): build_images.build(root, root / "build")

    def test_size_budget_fails(self):
        root = Path(tempfile.mkdtemp()); image_dir = root / "tools/authoring/images"
        image_dir.mkdir(parents=True)
        (root / "tools/authoring/word_images.tsv").write_text("large |  | large.svg\n", encoding="utf-8")
        (image_dir / "large.svg").write_text("<svg viewBox='0 0 1 1'><path d='M0 0'/></svg>", encoding="utf-8")
        original = build_images.render_webp
        build_images.render_webp = lambda _source, target: target.write_bytes(b"x" * (build_images.MAX_BYTES + 1))
        try:
            with self.assertRaises(ValueError): build_images.build(root, root / "build")
        finally:
            build_images.render_webp = original


if __name__ == "__main__": unittest.main()
