# Testing, Reproducibility and Known Limitations

[Documentation index](README.md)

This repository preserves a historical **2023 educational Android/IoT prototype**. The original report includes software and hardware explanations; the GitHub repository contains the Android Java/XML project. A clean end-to-end build **has not been verified as part of this documentation work**.

## What is checked in

- Java Android application source code and XML resources
- `app/build.gradle`, root Gradle config, wrapper scripts and wrapper properties
- Screenshots from the capstone report
- Documentation and conceptual system diagram

## What is needed for a realistic demonstration

1. A compatible Android Studio/SDK/Gradle environment.
2. A fresh **test** Firebase project with Realtime Database and carefully configured security rules.
3. An appropriate `app/google-services.json` generated for that Firebase project (excluded from version control).
4. Test route, bus, and ticket fixtures consistent with the model fields, and a plan to avoid using insecure historical login logic with real accounts.
5. A camera stream and original ESP32/FPGA hardware for end-to-end physical demonstrations.

The original database contents and complete embedded firmware are **not** part of the Android repository.

## Source-level observations (not test results)

| Area | Observation | Suggested follow-up |
|---|---|---|
| Authentication | Raw password values are read and written in `LoginActivity` | Use Firebase Authentication and restrictive rules |
| Authorization | Client username comparisons control admin/server behavior | Verify privileges server-side |
| Seat booking | Seat positions and ticket record are written separately | Use atomic transactions and server validation |
| QR boarding | Matching ticket access may rely on an unchecked index | Handle absent matches before reading a ticket |
| Firebase snapshots | Some `getValue()` results are used without null checks | Validate optional/missing data |
| OCR | `ServerActivity` couples camera, OCR, UI and hardware signaling | Separate components and lifecycle management |
| Build | Google Services plugin is enabled; `google-services.json` is not checked in | Document/setup a test Firebase app and run Gradle |
| UI | Original 2023 XML layouts and navigation APIs | Test on current supported Android versions |

## Local verification checklist

Run these commands from the Android project root **after** installing the Android SDK and adding a test Firebase config as appropriate:

```powershell
.\gradlew.bat --version
.\gradlew.bat :app:assembleDebug
.\gradlew.bat :app:testDebugUnitTest
```

A successful build does **not** by itself verify Firebase permissions, camera stream connectivity, or hardware behavior. The included `ExampleUnitTest` and `ExampleInstrumentedTest` are basic templates, not a comprehensive test suite.

## Prioritized engineering backlog

1. **Security:** migrate login and user-role checks to an authoritative authentication/authorization design; inspect active Realtime Database rules.
2. **Correctness:** handle missing Firebase nodes; reject mismatched QR scans safely; use transactional seat claims.
3. **Reliability:** improve error paths, network timeout behavior, and Android activity lifecycle cleanup.
4. **Testability:** extract UI-independent business logic and add tests for seats, route validation and boarding eligibility.
5. **Maintenance:** update dependencies deliberately and record a reproducible Android Studio/SDK version.

## Source and attribution

The original Hebrew capstone report was consulted to preserve the project intent and component descriptions. Page references appear throughout the [documentation index](README.md). The full report is deliberately **not published** in this repository because it contains personal information.
