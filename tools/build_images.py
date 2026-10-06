#!/usr/bin/env python3
"""Build optional word-image VectorDrawables from the owner mapping."""
from __future__ import annotations
import argparse, hashlib, json, re, sys
import xml.etree.ElementTree as ET
from pathlib import Path

MAX_BYTES = 3 * 1024 * 1024
ALLOWED = {"svg", "g", "path"}

def local(tag: str) -> str:
    return tag.rsplit("}", 1)[-1]

def safe_name(word_id: str) -> str:
    return "word_" + (re.sub(r"[^a-zA-Z0-9_]", "_", word_id).strip("_").lower() or "image")

def emoji_file(codepoints: str) -> str:
    points = [p.strip().lower().removeprefix("u+") for p in re.split(r"[ _+,\-]+", codepoints) if p.strip()]
    if not points: raise ValueError("empty emoji codepoints")
    return "emoji_u" + "_".join(points) + ".svg"

def colour(value: str | None) -> str:
    if not value or value == "none": return "#00000000"
    if value.startswith("#") and len(value) == 7: return value + "ff"
    if value.startswith("#") and len(value) == 9: return value
    raise ValueError(f"unsupported SVG colour: {value}")

def vector_xml(svg_path: Path) -> bytes:
    root = ET.parse(svg_path).getroot()
    if local(root.tag) != "svg": raise ValueError("root element is not svg")
    viewbox = root.attrib.get("viewBox", "0 0 128 128").split()
    if len(viewbox) != 4: raise ValueError("SVG viewBox must have four values")
    out = ET.Element("vector", {"xmlns:android": "http://schemas.android.com/apk/res/android",
        "android:width": "48dp", "android:height": "48dp", "android:viewportWidth": viewbox[2],
        "android:viewportHeight": viewbox[3]})
    for node in root.iter():
        tag = local(node.tag)
        if tag not in ALLOWED: raise ValueError(f"unsupported SVG element: {tag}")
        if tag == "path":
            if not node.attrib.get("d"): raise ValueError("SVG path has no d attribute")
            ET.SubElement(out, "path", {"android:pathData": node.attrib["d"],
                "android:fillColor": colour(node.attrib.get("fill", "#000000"))})
    return ET.tostring(out, encoding="utf-8", xml_declaration=True)

def build(root: Path, output: Path, noto_root: Path | None = None) -> dict:
    output.mkdir(parents=True, exist_ok=True)
    res = output / "res" / "drawable"; res.mkdir(parents=True, exist_ok=True)
    manifest, missing = {}, []
    mapping = root / "tools" / "authoring" / "word_images.tsv"
    if mapping.is_file():
        for line_no, raw in enumerate(mapping.read_text(encoding="utf-8").splitlines(), 1):
            if not raw.strip() or raw.lstrip().startswith("#"): continue
            fields = [x.strip() for x in raw.split("|", 2)]
            if len(fields) != 3 or not fields[0]: raise ValueError(f"{mapping}:{line_no}: expected word_id | emoji codepoints | svg name")
            word_id, emoji, owner_svg = fields
            source = ((noto_root or root / "third_party" / "noto-emoji") / emoji_file(emoji)) if emoji else (root / "tools/authoring/images" / owner_svg)
            if not source.is_file(): missing.append(word_id); continue
            name = safe_name(word_id); (res / f"{name}.xml").write_bytes(vector_xml(source)); manifest[word_id] = name
    total = sum(p.stat().st_size for p in res.glob("*.xml"))
    if total > MAX_BYTES: raise ValueError(f"generated image resources exceed 3 MiB: {total} bytes")
    (output / "image_manifest.json").write_text(json.dumps(manifest, sort_keys=True), encoding="utf-8")
    third = root / "third_party/noto-emoji"; version = (third / "VERSION").read_text(encoding="utf-8").strip() if (third / "VERSION").is_file() else "not vendored"
    payload = sorted(p for p in third.rglob("*") if p.is_file() and p.name not in {"LICENSE", "VERSION"}) if third.exists() else []
    digest = hashlib.sha256(b"".join(p.read_bytes() for p in payload)).hexdigest() if payload else "none"
    (output / "third_party_notices.txt").write_text(f"Noto Emoji\nVersion: {version}\nSHA-256 (vendored SVG payload): {digest}\nLicense: third_party/noto-emoji/LICENSE\n", encoding="utf-8")
    return {"mapped": len(manifest), "missing": missing, "bytes": total}

def main(argv=None) -> int:
    ap = argparse.ArgumentParser(); ap.add_argument("--root", type=Path, default=Path(__file__).resolve().parents[1]); ap.add_argument("--output", type=Path, required=True); ap.add_argument("--noto-root", type=Path)
    args = ap.parse_args(argv)
    try: result = build(args.root, args.output, args.noto_root)
    except (OSError, ValueError, ET.ParseError) as exc: print(f"build_images.py: {exc}", file=sys.stderr); return 1
    print(json.dumps(result, sort_keys=True)); return 0

if __name__ == "__main__": raise SystemExit(main())
