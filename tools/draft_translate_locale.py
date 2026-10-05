"""Disabled legacy command kept only as a migration notice."""

import sys


def main() -> int:
    if "--help" in sys.argv:
        print("usage: python tools/draft_translate_locale.py")
        print("disabled: use tools/locale.py for approved translation-provider workflows")
        return 0
    print("This legacy translator is disabled. Use tools/locale.py (Task 4) instead.", file=sys.stderr)
    return 2


if __name__ == "__main__":
    raise SystemExit(main())
