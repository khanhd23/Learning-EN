# Release checklist — Google Play with ads

Owner-maintained. Every item must be checked before a production release.

## Copyright and licenses

- [x] All lessons, explanations, questions, grammar notes, pet art, sounds and UI text are original.
- [x] Licenses screen (`content/LICENSES.md`) credits: Vietnamese Wiktionary (CC BY-SA 4.0, adapted
      dictionary data shared under the same license), NGSL/NAWL/BSL/TSL (CC BY-SA 4.0), WordNet 3.0
      (full license text), Noto Emoji (Apache-2.0), AndroidX, Google Mobile Ads/UMP.
- [x] Trademark notice for TOEIC/IELTS in the About text (`disclaimer_full`) and Licenses screen.
- [ ] Full license texts for bundled libraries and Noto (Apache-2.0 §4 requires a copy): add the
      Google `oss-licenses` plugin screen or bundle the texts (Codex, with Task 9.1).
- [ ] Never copy real exam questions from ETS, British Council, IDP or Cambridge books or websites;
      formats are described from official public pages only (`config/exam_formats.json` sources).
- [ ] App name, icon and store graphics: original; must not include exam logos or names
      (no "TOEIC" or "IELTS" in the app title or icon). Store description may say
      "practice for TOEIC-style questions" with the trademark notice.
- [ ] Before any new image, sound, font or text source: check its license first; record it here and
      in `content/LICENSES.md`. No images from Google search, no AI images of real brands/people.

## Ads and Google Play policy

- [ ] Real AdMob IDs only in release builds (the build already refuses test IDs for release).
- [ ] Consent (UMP) shown before ads in the EEA/UK (already wired); test with the UMP debug geography.
- [ ] Ad formats: banner on hub screens and interstitials between sessions only; never during a
      question, never auto-expanding or collapsible banners; rewarded ads always optional.
- [ ] Target audience in Play Console: 13+ (students and adults). The pet is cute, so Play may ask
      whether the app appeals to children — if children are included, the Families policy applies
      (only Families-certified ad settings, `tagForChildDirectedTreatment`, no personalised ads).
      Set `setMaxAdContentRating(G)` in any case (Codex).
- [ ] Privacy policy hosted at a public URL (required for apps with ads) and the same text in the
      app; Data safety form: AdMob collects device identifiers and approximate location for ads;
      the app itself stores learning data only on the device.
- [ ] Microphone (Task 11): the app requests RECORD_AUDIO only for speaking practice, after an
      in-app explanation. Data safety: audio is passed to the device's speech recognition service
      (often Google) to turn speech into text; the app does not record, store or upload audio.
      Say this in the privacy policy and the Data safety form ("Audio — not collected by the app,
      processed by the system speech service").
- [ ] Content rating questionnaire completed; dictionary entries with adult meanings are not shown
      in lessons (bronze entries are search-only; check `sex`, `horny`, `naughty` senses).
- [ ] Release build tested on a device (`assembleBenchmark` for speed; real release signed AAB).
