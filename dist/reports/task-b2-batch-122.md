# B2 batch 122

Added and manually reviewed 25 B2 entries:

\`unchanged\`, \`uncharacteristic\`, \`unclearly\`, \`uncomfortably\`,
\`uncommon\`, \`unconcerned\`, \`underestimate\`, \`undergraduate\`,
\`undersea\`, \`understandable\`, \`underwear\`, \`undoubtedly\`,
\`undreamed\`, \`undressed\`, \`unease\`, \`uneasily\`, \`unenthusiastic\`,
\`uneven\`, \`unfashionable\`, \`unforeseen\`, \`ungodly\`, \`unhappiness\`,
\`unhelpful\`, \`unimaginable\`, \`unimaginably\`.

## Counts

- Lesson entries: 8,524 -> 8,549.
- Silver entries: 7,412 -> 7,437.
- Bronze entries: 1,112 -> 1,112.
- B2 missing entries: 151 -> 126.
- A1-B1 missing entries: 2 (the intentional skips \`handicapped\` and \`hearted\`).
- TSL missing entries: 0.
- Audit errors: 2,709 (unchanged historical audit findings).
- Validator warnings: 243.

## Manual review

Each row was checked for lemma, part of speech, definition, Vietnamese gloss,
example meaning, topics, and level. The negative forms were kept distinct
where needed (\`unaware\`, \`unconcerned\`, \`unease\`, and \`uneasily\`) and
the examples avoid personal or medical claims.

## Checks

- \`check_editor_batch.py\`: 25 rows, 0 problems.
- \`words_batch.py tiers\`: 0 not-silver.
- \`validate_content.py\`: 0 errors, 243 warnings.
- \`audit_content.py\`: 8,549 entries, 7,437 silver, 1,112 bronze, 2,709 errors.
