# TOEIC-style Part 7 sample

## Change

The Part 7 mini/sample test now selects exactly 10 questions. Each selected
question still resolves to its authored passage, so passage context is kept in
the exercise. The full Part 7 format remains a 54-question practice paper.

## Check

- `./gradlew.bat testDebugUnitTest`: `BUILD SUCCESSFUL`.
- Added a unit test covering the 10-question limit and passage lookup.
