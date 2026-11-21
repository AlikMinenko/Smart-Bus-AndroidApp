# QR Code Scanning

[Back to documentation](README.md)

**Report reference:** PDF pages 126, 157–163 and 191–193.

[Java: QrActivity.java](../app/src/main/java/com/example/busproject/QrActivity.java) · [XML: activity_qr.xml](../app/src/main/res/layout/activity_qr.xml)

The app has a QR scanning screen. The user opens it from the tickets screen to scan the QR code of a bus.

## How the scan works

1. `QrActivity` opens the phone camera using the `CodeScanner` library.
2. The scanner reads the QR code.
3. The scanned text is returned to the tickets screen using an `Intent` value named `BusID`.
4. [TicketsFragment.java](../app/src/main/java/com/example/busproject/TicketsFragment.java) checks the scanned bus against the user's tickets.

## Checking the ticket

The code checks information such as:

- Whether the user has a ticket for the scanned bus.
- Whether the bus is at the pickup station.
- Whether the ticket is marked as allowed.
- Whether the bus is currently in the passenger drop-off process.

If the checks succeed, the app updates values in Firebase that are used by the smart-bus model for boarding.

The tickets screen shows a visual result for a successful or unsuccessful scan.

![QR scanning screen from the original report](../assets/screenshots/07-qr-scanner.png)

**Simple explanation:** The QR code helps the app identify the bus. The ticket and current station are then checked using the information saved in Firebase.

This document describes the original project behavior, not a new QR system.
