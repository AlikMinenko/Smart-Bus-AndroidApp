# Original Project Files and Running the App

[Back to documentation](README.md)

This repository contains the **Android application** from the Smart Bus final project. The Java and XML code is explained in Chapter 6 of the original project report.

## Where the files are

| Folder or file | What it contains |
|---|---|
| [Java classes](../app/src/main/java/com/example/busproject) | Code for the screens, buttons and program operations |
| [XML layouts](../app/src/main/res/layout) | Design of the application screens |
| [Images](../app/src/main/res/drawable-v24) | Images used by the application |
| [AndroidManifest.xml](../app/src/main/AndroidManifest.xml) | Application screens and required permissions |
| [app/build.gradle](../app/build.gradle) | Libraries used by the Android project |

## Running the original application

The project was built using Android Studio and Java. It uses **Firebase Realtime Database**, so screens that read and write data need a suitable Firebase setup.

The complete smart-bus demonstration also depends on the original ESP32, ESP32-CAM and FPGA setup.

## What this GitHub repository shows

The code includes the following functions described in the project report:

- Login and registration
- Bus line information
- Ticket reservation and seat selection
- QR code scanning
- Camera video and text recognition
- Adding buses and bus lines

The repository includes the original Android source. This documentation **does not claim** that the app has been rebuilt and tested on every current Android version.

For detailed explanations, return to the [documentation index](README.md).
