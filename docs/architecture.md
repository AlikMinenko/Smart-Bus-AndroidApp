# How the Android App Connects to the Smart Bus

[Back to documentation](README.md)

**Report reference:** PDF pages 6, 11–12 and 44.

The goal of the final project was to build a model of a smart bus. The Android application lets the passenger see bus lines, select a route, reserve seats and use a QR code. It also shows information that comes from the bus model.

## Main parts of the project

- **Android application:** The screens and Java code that the passenger uses.
- **Firebase Realtime Database:** Stores information about bus lines, buses, users and tickets. The app reads data from it and writes data to it.
- **ESP32:** Communicates with Firebase and the FPGA in the original bus model.
- **FPGA DE10-Lite:** Controls electronic parts of the model, such as the doors and other indicators.
- **ESP32-CAM:** Provides video that is used by the Android monitoring screen.

![System block diagram from the project report](../assets/architecture/system-block-diagram.png)

## Example from the application

In [LinesFragment.java](../app/src/main/java/com/example/busproject/LinesFragment.java), the app reads the list of lines from Firebase. When the information changes, the list on the phone changes.

In [ServerActivity.java](../app/src/main/java/com/example/busproject/ServerActivity.java), the app receives video from the ESP32-CAM, detects text and writes the detected station text to Firebase.

**In short:** Firebase connects the Android application to the information used by the smart-bus model.

The electronic components and their connections are described in the earlier chapters of the project report. This GitHub repository contains the **Android application code**.
