# Original exam-practice content design

This project may teach the skills and patterns needed for common exams, but it
must not reproduce copyrighted exam questions, passages, answer keys or audio.
Every shipped item is an original scenario written for this project and carries
`source: original:<bank>` plus `needs_review: true` until reviewed.

## Reusable item families

### Vocabulary and sentence completion

- Meaning in context: choose the word that fits the situation.
- Collocation: choose the natural partner (`make a decision`, `meet a deadline`).
- Word family: choose noun/verb/adjective/adverb from the sentence role.
- Confusable words: choose by meaning and grammar (`borrow/lend`, `affect/effect`).
- Preposition pattern: choose the preposition required by the verb/adjective.
- Phrasal verb: choose the particle from a practical context.
- Register: choose a natural formal or informal option for an email/dialogue.

### Grammar and structure

- Tense and time signal.
- Subject–verb agreement.
- Articles, determiners and quantifiers.
- Pronoun/reference tracking.
- Modal meaning and advice/obligation.
- Comparatives and superlatives.
- Passive voice.
- Gerund vs infinitive.
- Relative clauses.
- Conditionals.
- Connectors and discourse markers.
- Sentence transformation.
- Sentence ordering.
- Find and repair one error.

### Original exam-shaped contexts

- Notice, sign, schedule and short announcement.
- Email, memo, chat and customer-service exchange.
- Form, invoice, booking and application.
- Short article, report, opinion paragraph and data description.
- Two-line dialogue with one missing turn.

These contexts teach transfer skills without claiming to be official TOEIC, IELTS,
Cambridge, TOEFL or PTE material. The UI must say “bài luyện theo dạng”, not
“đề thi thật”.

## Item contract

```json
{
  "id": "orig_vocab_0001",
  "type": "cloze|word_family|error_fix|ordering|dialogue_gap|notice_gap",
  "skill": "vocabulary|grammar|reading|writing|micro_listening",
  "format": "general|toeic_style|cambridge_style|ielts_style|academic",
  "context": "original scenario",
  "stem": "Original sentence or short text",
  "options": ["..."],
  "answer": 0,
  "explanation": "Why this answer fits",
  "vi": "Vietnamese explanation",
  "wordIds": ["..."],
  "tags": ["collocation", "preposition"],
  "source": "original:exam-patterns-2026",
  "needs_review": true
}
```

The validator must reject copied-source fields, missing explanations, more than
one correct answer, empty Vietnamese feedback and unsupported exam claims.
