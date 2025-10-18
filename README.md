# 🛡️ SectorSecured — Mobile Forensics & Security Training App  
SPDX-License-Identifier: MIT

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](./LICENSE)

> Practical, hands-on Android security training packaged as a deliberately insecure app.
> Designed to teach reverse engineering, exploitation techniques, and secure remediation by letting you break things in a sandboxed environment — and then fix them. Because apparently reading slides wasn't enough.

---

## Project Summary 

**SectorSecured** is an educational Android application built to simulate six real-world mobile security mistakes. Each level intentionally contains a specific vulnerability (hardcoded secrets, exported activities, weak encoding, SQL injection, exposed files, broken cryptography). The learner’s goal is to find, exploit, and remediate each issue using standard mobile security tools and techniques: `jadx-gui`, `apktool`, `adb`, `apksigner`, and manual code inspection.

---

## Why this exists (Motivation)

Most mobile-security training is either:

* Too theoretical (boiling the ocean with jargon), or
* A carefully sanitized capture-the-flag that hides how messy real apps are.

SectorSecured chooses the messy, realistic route: it shows how small, sloppy developer choices cascade into exploitable systems. You’ll learn the tools used by security engineers and attackers alike — and you’ll learn how to patch the same mistakes without pretending the bugs were aesthetic choices.

---

## Who this is for

* Students learning mobile forensics and app security.
* Developers who want to stop shipping dangerously naive Android apps.
* Red-team / pentest practitioners learning Android-specific techniques.
* Security instructors who need a predictable, repeatable lab environment.

If you’re here to practice on production systems — go elsewhere. This app is intentionally insecure and must only be used in isolated, controlled environments.

---

## Long-form Walkthrough 

### Level 1 — Password Pursuit (Hardcoded Credentials)

**What’s broken:** The app stores a password directly in the source code (`getHardcodedPassword()` returns `CtrlAltDefe4t!`).
**Why it matters:** Hardcoded secrets are the single easiest vector for lateral compromise — code repositories, decompiled binaries, or accidental screenshots all expose them.
**How to find it:** Decompile with `jadx-gui` and inspect `MainActivity.java` (or equivalent).
**Learning point:** Secrets belong in secure storage or deriving flows; never in code. Use Android Keystore for long-term secrets and dynamic configuration for secrets management.

### Level 2 — Silent Bypass (Exported Activity / ADB Start)

**What’s broken:** An activity (`Level3Activity`) is marked `exported=true` in `AndroidManifest.xml`.
**Why it matters:** Exported components can be triggered by other apps or via ADB — granting unauthorized entry points.
**How to exploit:** From a rooted emulator or device with `adb`:

```bash
adb shell am start -n com.cybertooths.sectorsecured/.Level3Activity
```

**Learning point:** Only export components intentionally, and validate input on entry regardless of origin.

### Level 3 — Obscured Insight (Weak Encoding — Base64)

**What’s broken:** The “secret” passphrase is Base64-encoded (`Q1RSTF80bHRfUHdu`) and decoded client-side for verification.
**Why it matters:** Base64 is encoding, not encryption. It offers zero secrecy.
**How to exploit:** Decode the string with any Base64 tool:

```
Q1RSTF80bHRfUHdu  ->  CTRL_4lt_Pwn
```

**Learning point:** Use proper cryptographic primitives where confidentiality is required. Treat encoded secrets as public.

### Level 4 — Authentication (SQL Injection)

**What’s broken:** Authentication accepts unvalidated input concatenated into SQL.
**Why it matters:** SQL injection allows unauthorized access, data extraction, and full account takeover.
**How to exploit:** Use classic payloads to bypass checks:

```
Username: PhantomHack' OR '1'='1
```

**Learning point:** Use parameterized queries / prepared statements. Always validate and sanitize inputs.

### Level 5 — Echoes of Betrayal (Insecure File Exposure)

**What’s broken:** Sensitive files are left in app cache and can be viewed via `file://` paths presented to an internal WebView (or by directly inspecting app storage on a rooted device).
**Why it matters:** Local files in app storage or world-readable cache can leak secrets if not protected.
**How to find:** Use the app’s URL-loading feature with paths like:

```
file:///etc/hosts
file:///data/data/com.cybertooths.sectorsecured/cache/email
```

**Learning point:** Protect files with correct file permissions, use internal-only storage APIs, and avoid leaving credentials or secrets in logs/cache.

### Level 6 — Decryption Mania (Broken ROT13 Implementation)

**What’s broken:** A buggy ROT13-like function (missing modulo / incorrect mapping) prevents decryption; the remediation requires editing smali to fix logic and re-signing the APK.
**Why it matters:** Broken algorithms or incorrect implementations of cryptography often defeat the intended security. Security is not about inventing “clever” transformations — use vetted libraries and algorithms.
**How to fix:** Disassemble with `apktool`, correct the algorithm (apply correct ROT13 mapping or replace with library call), rebuild, sign, and install:

```bash
apktool d sectorsecured.apk
# edit smali
apktool b sectorsecured -o sectorsecured_fixed.apk
zipalign -v 4 sectorsecured_fixed.apk sectorsecured_fixed_aligned.apk
keytool -genkey -v -keystore sectorsecured_fixed_keystore -alias key1 -keyalg RSA -keysize 2048 -validity 10000
apksigner sign --ks sectorsecured_fixed_keystore --v1-signing-enabled true --v2-signing-enabled true sectorsecured_fixed_aligned.apk
adb install -r sectorsecured_fixed_aligned.apk
```

Decrypted final message:

```
Initiate protocol 'Zero Trust' — no one is safe, even those closest.
```

**Learning point:** Cryptographic code must be correct, tested, and preferably provided by trusted libraries. Reimplementing crypto is a safe route to failure.

---

## Setup & Reproduction 

### Prerequisites

* Android SDK (platform-tools: `adb`, `apksigner`)
* Java JDK 8+ (`keytool`)
* `jadx-gui` (decompilation)
* `apktool` (disassembly/reassembly)
* Rooted emulator (recommended) or an isolated physical device
* Optional: Visual Studio Code for reading & editing smali

### Quick install (recommended for labs)

1. Start a rooted Android emulator (e.g., Android Studio AVD with root modifications).
2. From repo root:

```bash
adb install apk/sectorsecured.apk
```

3. Open the app and proceed through levels. Use `adb` shell and the tools above for inspection.

### Rebuilding / Patching

Follow the Level 6 example above to disassemble and rebuild an APK. Always re-sign with a valid keystore after rebuilding; Android will refuse to install unsigned or mismatched-signed APKs.

---

## Tools & Commands Cheat Sheet

* Decompile: `jadx-gui sectorsecured.apk`
* Disassemble: `apktool d sectorsecured.apk`
* Rebuild: `apktool b sectorsecured -o sectorsecured_fixed.apk`
* Align: `zipalign -v 4 sectorsecured_fixed.apk sectorsecured_fixed_aligned.apk`
* Sign: `apksigner sign --ks <keystore> sectorsecured_fixed_aligned.apk`
* ADB start activity: `adb shell am start -n com.cybertooths.sectorsecured/.Level3Activity`
* View files on device: `adb shell ls -la /data/data/com.cybertooths.sectorsecured/` (requires root)

---

## Security Considerations and Responsible Use

Yes, the repo contains vulnerable code. This is intentional. Use it only in isolated lab environments — never against live services. If you clone and run this project:

* Use a sandboxed or emulator environment.
* Do not deploy to public app stores.
* Do not attempt these techniques on systems you do not own or have explicit permission to test.

If you’re an instructor, feel free to adapt levels, add telemetry for grading, or lock certain hints depending on your class format.

---

## Remediation Guidance (what you would do in a real app)

* Replace hardcoded secrets with secure key management (Android Keystore or remote secret manager).
* Avoid exporting activities unless necessary; require authentication and validate intents.
* Treat Base64 as encoding; use authenticated encryption (AES-GCM) for confidentiality.
* Use parameterized SQL queries or ORMs that escape inputs properly.
* Store sensitive files using internal-only storage and proper file permissions; avoid caching secrets.
* Use vetted cryptographic libraries; never design your own encryption routine.

If you do one thing from this project: fix input validation and secrets handling across the entire codebase.

---

## Troubleshooting & Common Issues

* **APK won’t install after rebuilding:** Make sure you signed the APK with `apksigner` and used `zipalign`.
* **Cannot access `/data/data/...` on device:** You need root or an emulator with root enabled.
* **`am start` fails:** Verify the package and activity name with `adb shell pm list packages -f` and `aapt dump badging sectorsecured.apk` (or inspect `AndroidManifest.xml`).
* **Decompiled code missing context:** Decompiled Java can be incomplete; open the smali if logic is unclear.

---

## Contribution & Extensions

This project is intentionally opinionated. If you want to contribute:

* Add new levels that represent modern mobile threats (e.g., OAuth mistakes, insecure IPC).
* Add automated grading scripts for instructors.
* Add unit tests that validate secure fixes (e.g., input sanitizer test).
* Provide Dockerized lab environment for consistent student setup.

When you send a PR: describe the learning objective, provide steps to reproduce, and include a mitigation writeup.

---

## License

This project is licensed under the **MIT License** — see the [LICENSE](./LICENSE) file for details.

Copyright (c) 2024-2025 Kunal Rajour

Permission is hereby granted, free of charge, to any person obtaining a copy of this software
and associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions: attribution is appreciated but not required.
