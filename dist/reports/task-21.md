# Task 21: Pet speech bubble never hides content

Implemented the pet bubble safety rules from `docs/ROADMAP_V1.md`:

- The Today weekly-quest card is marked as `petAvoid`, so the bubble cannot cover it.
- Bubble placement is calculated in a small pure Kotlin helper. It stays inside the visible
  content bounds, prefers the side beside the pet, then the position above it, and hides when no
  safe position remains.
- The bubble auto-hides after exactly 3 seconds.
- The bubble is visual/announced content rather than a touch target; touches pass through it while
  the pet disc keeps its existing tap, long-press, drag, and menu behavior.
- Existing praise/pet-line selection logic was not changed.

## Verification

- `PetBubblePlacementTest`: pass (safe placement and no-safe-placement cases).
- `python tools/validate_content.py`: pass; errors=0.
- `python -m unittest discover -s tools/tests`: pass.
- `./gradlew testDebugUnitTest assembleDebug`: pass.

No new UI strings were needed.
