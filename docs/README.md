# YourBUS — Android Code Explanations

This folder explains the **Android application I developed for my Smart Bus final project**.

The explanations are written in simple English. They are based on **Chapter 6 (Java and XML)** of the original Hebrew project report and the Java/XML files in this repository. They describe what the code does; they do not describe new features or planned changes.

**How to use these pages:** Read the short explanation, open the linked Java file, and compare it with the original report.

| Explanation | Main Java files | Where it appears in the report |
|---|---|---|
| [Project and system connection](architecture.md) | Android app and Firebase | PDF pages 6, 11–12, 44 |
| [App screens and navigation](android-navigation.md) | SplashActivity, MainActivity, LinesFragment, TicketsFragment | PDF pages 123–126, 127–130, 141–153 |
| [Login and registration](authentication.md) | LoginActivity, User, SplashActivity | PDF pages 131–140, 205 |
| [Firebase and app classes](firebase-data-model.md) | Bus, Line, Ticket, User | PDF pages 203–210 |
| [Choosing stations and seats](ticket-booking.md) | LineInfo, TicketsFragment | PDF pages 154–163, 177–190 |
| [Scanning a QR code](qr-boarding.md) | QrActivity, TicketsFragment | PDF pages 157–163, 191–193 |
| [ESP32-CAM and text recognition](camera-ocr.md) | ServerActivity | PDF pages 194–202 |
| [Adding buses and lines](admin-workflows.md) | AddBus, AddLine | PDF pages 123–125, 164–176 |
| [Files and project setup](testing-and-limitations.md) | Android Studio project | Android source and report Chapter 6 |

> Page numbers above refer to **PDF pages**, not the printed numbers at the bottom of the report.

## The main Android source folder

[Open the Java classes](../app/src/main/java/com/example/busproject) · [Open the XML layouts](../app/src/main/res/layout)

## About the work

The **Android application** in this repository was independently developed by **Alik Minenko** as part of the wider Smart Bus academic project. The hardware is described only to explain how the app communicates with the project system.

The original report is not uploaded here because it contains personal information.
