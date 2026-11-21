# YourBUS — Technical Documentation

This documentation expands the [project overview](../README.md) into a walkthrough of the **Android implementation**. It is adapted from the original Hebrew capstone report (April 2023), then checked against the Java and XML files currently in this repository.

**How to read:** start with the architecture, follow the user journey, and then examine the Firebase, ticketing, QR, and camera integrations. Links lead to the real implementation.

| Topic | What it covers | Original report (PDF page) |
|---|---|---|
| [System architecture](architecture.md) | Android, Firebase, ESP32, FPGA and communication flow | 6, 11–12, 44 |
| [Android navigation](android-navigation.md) | Splash screen, activities, fragments and RecyclerView | 123–130, 141–153 |
| [Authentication and session](authentication.md) | Registration, login, remembered session and security caveats | 131–139, 144–149 |
| [Firebase data model](firebase-data-model.md) | Routes, buses, users and ticket objects | 151–163, 203–210 |
| [Ticket and seat reservation](ticket-booking.md) | Route information, station and seat choice, reservation writes | 177–190 |
| [QR boarding flow](qr-boarding.md) | Camera scanning and ticket/bus validation | 191–193, 157–163 |
| [ESP32-CAM and OCR](camera-ocr.md) | MJPEG capture, ML Kit, stop recognition and prototype control | 194–202 |
| [Admin workflows](admin-workflows.md) | Adding buses/routes, stop lists and admin UI | 123–125, 164–176 |
| [Testing and limitations](testing-and-limitations.md) | Reproducing a demo and known improvements | Report overview and current code |

> **Documentary scope:** The report explains the original educational prototype. These pages describe the existing implementation, **not** a claim that the code is production-ready, secure, or currently passing a clean build. References to pages use the **PDF's page numbers**; the printed page numbers in the report can differ by one.

## Repository map

- [Android Java source](../app/src/main/java/com/example/busproject)
- [Android XML layouts](../app/src/main/res/layout)
- [Android manifest](../app/src/main/AndroidManifest.xml)
- [Gradle configuration](../app/build.gradle)
- [Prototype screenshots](../assets/screenshots)
- [System diagram](../assets/architecture/system-block-diagram.png)

## Historical context and attribution

The Android app in this repository was **independently developed by Alik Minenko** as part of a broader academic smart-bus capstone project. This repository is maintained as an Android-development portfolio example. The full original report is **not committed**, because it contains personal and academic records. Descriptions here are paraphrased and reorganized for readability, with links back to source files for verification.

## Reading a code walkthrough

Each page distinguishes:

1. **Report description** — behavior explained in the original project report.
2. **Current repository code** — observable Java classes, data fields and event handlers.
3. **Limitations / next steps** — implementation concerns or possible improvements, which should not be mistaken for implemented features.

This is not an API reference or a deployment guide; see [testing and limitations](testing-and-limitations.md) for reproducibility concerns.
