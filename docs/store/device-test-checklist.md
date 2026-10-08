# Device test checklist (Publisher, every build)

Takes about 15 minutes. For anything that looks wrong, take a screenshot and write one line: which
screen, what you did, what you expected. Send it to Claude.

Build: ________  Phone: ________  Android: ____  Date: ________

## First start (uninstall first to test this)

- [ ] Onboarding: language, goal and level screens look right; no text cut off.
- [ ] Consent form (only in some regions) appears before any ad.
- [ ] Today screen opens; the pet sits on the right and does not cover buttons.

## Learning

- [ ] Start a lesson; finish it. Questions, options and explanations are fully visible.
- [ ] Picture exercise: the picture matches the word; no answer is printed next to the picture.
- [ ] Sound exercise and dictation play audio; replay works.
- [ ] Speaking: the app asks for the microphone only when you tap it; refusing does not break the
      lesson.
- [ ] The pet praises after a streak of correct answers and does not jump around.
- [ ] Back from a lesson returns to the same place without lag.

## Words tab

- [ ] Open three topics, scroll to the end, go back. No stutter longer than a blink.
- [ ] Search "airport", "go", "con chó" (Vietnamese): the right word is first.
- [ ] Exam word lists (after Task 15): counts look sensible; "Learn this list" starts a lesson.

## Exams

- [ ] Open the exam tab; start a 10-question mini test; finish; review screen shows explanations.
- [ ] Sectioned exam (after Task 14.1 and the THPT bank): passage shows above questions, can be
      hidden; section scores appear on the result screen.
- [ ] Leave a mock test halfway, reopen the app: "continue" works.

## Stories, quests, pet

- [ ] Read a story, answer its questions; answers cannot be changed after tapping.
- [ ] Daily challenge and weekly quests show progress; a reward appears once.
- [ ] Pet room and shop open; buying an item spends coins once.

## Ads (release or internal-track build only)

- [ ] Banner only on hub screens, never during a question.
- [ ] Interstitial only between sessions, not more often than every 3 minutes.
- [ ] Rewarded ad always optional; closing it early gives no reward and no crash.
- [ ] Settings → privacy options opens the consent form (where required).

## Settings and system

- [ ] Dark mode: every screen readable.
- [ ] Vietnamese and English UI: no missing or English-only text in Vietnamese mode.
- [ ] Turn on the daily reminder; it arrives at the chosen time.
- [ ] Privacy policy and licences screens open; the policy link opens in the browser.
- [ ] Rotate the phone or split screen (if your phone allows): nothing crashes.
- [ ] Airplane mode: lessons, dictionary and tests still work.
