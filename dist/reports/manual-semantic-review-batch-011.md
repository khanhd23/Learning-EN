# Manual semantic review — batch 011

## Scope

I read the next 25 legal, manufacturing, and logistics senses against their English examples.
I rewrote five source definitions: `liable_s1` now expresses legal responsibility,
`obligation_s1` removes corrupt dictionary text, and `factory_s1`, `manufacture_s1`, and
`shipment_s1` now match the manufacturing and shipping examples.

Hindi received a fresh gloss and example review for all 25 senses. The review clarified the
legal distinctions among `liable`, `obligation`, `binding`, and `evidence`, and aligned the
factory, manufacture, warehouse, shipment, inventory, supplier, and assemble meanings. Source
hashes were refreshed for every changed source sense in all five locales.

## Reviewed senses

`breach_s1`, `dispute_s1`, `court_s1`, `court_s2`, `confidential_s1`, `liable_s1`,
`obligation_s1`, `draft_s1`, `draft_s2`, `binding_s1`, `terminate_s1`, `notice_s1`,
`notice_s2`, `property_s1`, `license_s1`, `license_s2`, `evidence_s1`, `authorize_s1`,
`factory_s1`, `manufacture_s1`, `warehouse_s1`, `shipment_s1`, `inventory_s1`,
`supplier_s1`, `assemble_s1`.

## Counts before/after

- English source senses: 8,837 / 8,837.
- Source audit errors: 0 / 0.
- Locale validator errors: 0 / 0.
- Source definitions rewritten: 5.
- Locale wording changes: hi 25.
- Status hashes refreshed for all five locales on the changed source senses.

## Checks

- `python -X utf-8 tools/audit_content.py`: 0 errors.
- `python -X utf-8 tools/validate_content.py`: 0 errors, 0 warnings.
- `python -X utf-8 tools/locale.py check --all`: all locales passed.
- `python -m unittest discover -s tools/tests -p "test_*.py"`: 73 passed.
- `gradlew.bat check assembleDebug --no-daemon`: passed.

## Remaining review

The complete 8,837-sense corpus still requires additional manual batches. This report covers
only this 25-sense pass.
