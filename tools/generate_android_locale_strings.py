import html
import json
import argparse
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
parser = argparse.ArgumentParser(description='Generate Android UI resources from locale UI JSON packs.')
parser.add_argument('--output', type=Path, default=ROOT / 'app/src/main/res', help='Android res directory to receive generated values folders.')
args = parser.parse_args()
OUTPUT = args.output if args.output.is_absolute() else ROOT / args.output
MAPPING = {
    'es': 'values-es',
    'hi': 'values-hi',
    # Android's resource qualifier for Indonesian uses the legacy ISO code "in".
    'id': 'values-in',
    'ja': 'values-ja',
    'ko': 'values-ko',
    'pt-BR': 'values-pt-rBR',
}

for locale, folder in MAPPING.items():
    source = ROOT / 'content/i18n' / locale / 'ui.json'
    values = json.loads(source.read_text(encoding='utf-8'))
    lines = ['<?xml version="1.0" encoding="utf-8"?>', '<resources>']
    for name, value in values.items():
        if not isinstance(value, str):
            raise ValueError(f'{locale}/{name}: UI value is not a string')
        escaped = html.escape(value, quote=False)
        lines.append(f'    <string name="{name}">{escaped}</string>')
    lines.append('</resources>')
    target = OUTPUT / folder / 'strings.xml'
    target.parent.mkdir(parents=True, exist_ok=True)
    target.write_text('\n'.join(lines) + '\n', encoding='utf-8')
    print(locale, len(values), target)
