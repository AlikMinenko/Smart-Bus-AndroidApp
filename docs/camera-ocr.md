# ESP32-CAM Video and Text Recognition

[Back to documentation](README.md)

**Report reference:** PDF pages 126 and 194–202.

[Java: ServerActivity.java](../app/src/main/java/com/example/busproject/ServerActivity.java) · [XML: activity_server.xml](../app/src/main/res/layout/activity_server.xml)

The project includes a screen called `ServerActivity`. In the original report, this screen represents the computer inside the bus.

It shows live video from the **ESP32-CAM** and uses text recognition to identify station names.

## 1. Receiving the camera video

The Java code reads the camera IP address from Firebase. It connects to the ESP32-CAM video stream and displays the video inside the app.

## 2. Reading text from the video

The code uses **Google ML Kit** to recognize text in a video frame.

It follows this sequence:

1. Receive frames from the video.
2. Count the received frames.
3. Every **60 frames**, send one frame for text recognition.
4. Read the recognized text.
5. Write the detected text to Firebase as the bus's current station (`currentST`).

The current station is also displayed on the monitoring screen.

![Monitoring screen from the project report](../assets/screenshots/08-ocr-server.png)

## 3. Using station information

The rest of `ServerActivity` uses information from Firebase to help control the bus model.

The original report describes these actions:

- Read the ticket of the passenger who is boarding.
- Use passenger counts to help determine when to close the entrance door.
- Keep information about passengers and the stations where they need to get off.
- Signal before a passenger's destination station.
- Open and close the exit door according to the number of people getting off.
- Update the related information in Firebase.

These actions are part of the original model and are explained in the `ServerActivity` section of the report.

## Important detail

**The ESP32-CAM sends video, but the Android app performs text recognition.**

The original code runs this process in `ServerActivity`. It does not show a separate cloud service that continues reading video when the Android activity is closed.
