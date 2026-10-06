#!/usr/bin/env python3
"""Render owner/Noto SVG word images to small, faithful WebP assets."""
from __future__ import annotations
import argparse, hashlib, json, re, sys
from io import BytesIO
from pathlib import Path

MAX_BYTES = 3 * 1024 * 1024

def safe_name(word_id: str) -> str:
    return "word_" + (re.sub(r"[^a-zA-Z0-9_]", "_", word_id).strip("_").lower() or "image")

def emoji_file(codepoints: str) -> str:
    points = [p.strip().lower().removeprefix("u+") for p in re.split(r"[ _+,\-]+", codepoints) if p.strip()]
    if not points: raise ValueError("empty emoji codepoints")
    return "emoji_u" + "_".join(points) + ".svg"

def render_webp(source: Path, target: Path) -> None:
    try:
        from resvg_py import svg_to_bytes
        from PIL import Image
    except ImportError as exc:
        raise RuntimeError("resvg-py and Pillow are required; install tools/requirements.txt") from exc
    png = svg_to_bytes(svg_path=str(source), width=128, height=128)
    Image.open(BytesIO(png)).convert("RGBA").save(target, "WEBP", lossless=True, method=6)

def build(root: Path, output: Path, noto_root: Path | None = None) -> dict:
    output.mkdir(parents=True, exist_ok=True)
    res = output / "res" / "drawable-nodpi"; res.mkdir(parents=True, exist_ok=True)
    manifest, missing = {}, []
    mapping = root / "tools" / "authoring" / "word_images.tsv"
    noto = noto_root or root / "third_party" / "noto-emoji" / "svg"
    if mapping.is_file():
        for line_no, raw in enumerate(mapping.read_text(encoding="utf-8").splitlines(), 1):
            if not raw.strip() or raw.lstrip().startswith("#"): continue
            fields = [x.strip() for x in raw.split("|", 2)]
            if len(fields) != 3 or not fields[0]: raise ValueError(f"{mapping}:{line_no}: expected word_id | emoji codepoints | svg name")
            word_id, emoji, owner_svg = fields
            owner_name = owner_svg if owner_svg.lower().endswith(".svg") else owner_svg + ".svg"
            source = (noto / emoji_file(emoji)) if emoji else (root / "tools/authoring/images" / owner_name)
            if not source.is_file(): missing.append(f"{word_id}: {source}"); continue
            name = safe_name(word_id); render_webp(source, res / f"{name}.webp"); manifest[word_id] = name
    if missing: raise ValueError("mapped words have no image:\n" + "\n".join(missing[:20]))
    total = sum(p.stat().st_size for p in res.glob("*.webp"))
    if total > MAX_BYTES: raise ValueError(f"generated image resources exceed 3 MiB: {total} bytes")
    (output / "image_manifest.json").write_text(json.dumps(manifest, sort_keys=True), encoding="utf-8")
    third = root / "third_party/noto-emoji"; version = (third / "VERSION").read_text(encoding="utf-8").strip() if (third / "VERSION").is_file() else "unknown"
    payload = sorted((third / "svg").glob("*.svg")) if (third / "svg").exists() else []
    digest = hashlib.sha256(b"".join(p.read_bytes() for p in payload)).hexdigest() if payload else "none"
    (output / "third_party_notices.txt").write_text(f"Noto Emoji\nVersion: {version}\nSHA-256 (vendored SVG payload): {digest}\nLicense: third_party/noto-emoji/LICENSE\n", encoding="utf-8")
    return {"mapped": len(manifest), "missing": missing, "bytes": total}

def main(argv=None) -> int:
    ap = argparse.ArgumentParser(); ap.add_argument("--root", type=Path, default=Path(__file__).resolve().parents[1]); ap.add_argument("--output", type=Path, required=True); ap.add_argument("--noto-root", type=Path)
    args = ap.parse_args(argv)
    try: result = build(args.root, args.output, args.noto_root)
    except (OSError, RuntimeError, ValueError) as exc: print(f"build_images.py: {exc}", file=sys.stderr); return 1
    print(json.dumps(result, sort_keys=True)); return 0

if __name__ == "__main__": raise SystemExit(main())
