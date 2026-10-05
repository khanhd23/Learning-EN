# Lightweight micro-listening

Listening is deliberately limited to short, reusable items so the base APK stays
small and offline-friendly.

## Initial pack

- 1,500 headwords selected from NGSL/TSL/BSL with IPA when available.
- One word, short phrase or short original sentence per item.
- Exercises: hear → choose meaning, hear → type the word, hear → choose spelling,
  and minimal-pair discrimination when pronunciation is human-verified.
- No long dialogues, lectures or passages in the base pack.

## Audio strategy

The data pack stores `audioStatus`, `voice`, `ipa` and `ttsText`; it does not force
large audio files into the vocabulary JSON. The app may use Android TTS offline
as the default and add compressed, license-safe clips only for the highest-priority
words. Audio is cached on demand and capped by the user's selected pack.

Every audio item records accent (`en-US` or `en-GB`), speed and review state. Do not
label a synthetic voice as a native recording. Do not download audio at runtime
without a clear network/consent policy.
