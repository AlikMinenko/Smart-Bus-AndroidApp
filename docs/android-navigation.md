# Android Screens and Navigation

[Back to documentation](README.md)

**Report reference:** PDF pages 123–130, 141–153 and 154–163.

The application was built in **Java**. Its screens were designed with **XML** files. Each main screen has a Java class that controls buttons, text and navigation.

## 1. Splash screen

[Java: SplashActivity.java](../app/src/main/java/com/example/busproject/SplashActivity.java) · [XML: activity_splash.xml](../app/src/main/res/layout/activity_splash.xml)

This is the first screen shown when the application opens. It displays the project animation. After approximately four seconds, the app checks whether the user previously selected the option to remain logged in. It then opens the login screen or the main screen.

## 2. Main screen

[Java: MainActivity.java](../app/src/main/java/com/example/busproject/MainActivity.java) · [XML: activity_main.xml](../app/src/main/res/layout/activity_main.xml)

The main screen has two main tabs:

- **Lines:** Shows the available bus lines.
- **Tickets:** Shows the user's tickets.

The screen also includes a side navigation menu with more options. The original app shows additional menu options for the `admin` username.

## 3. Lines screen

[LinesFragment.java](../app/src/main/java/com/example/busproject/LinesFragment.java) reads line information from Firebase and displays the lines in a scrolling list. [LineAdapter.java](../app/src/main/java/com/example/busproject/LineAdapter.java) is the Java class that displays each item in this list.

Selecting a line opens [LineInfo.java](../app/src/main/java/com/example/busproject/LineInfo.java), where the user can view the line information.

![Lines screen from the project report](../assets/screenshots/03-lines.png)

## 4. Tickets screen

[TicketsFragment.java](../app/src/main/java/com/example/busproject/TicketsFragment.java) reads the current user's tickets from Firebase. [TicketAdapter.java](../app/src/main/java/com/example/busproject/TicketAdapter.java) displays the tickets in a list.

The user can open the details of a ticket and start the QR scanner from this screen.

**Simple explanation:** A `Fragment` is a part of a screen. An `Adapter` helps display the data in a list. These terms are also explained in the Java chapter of the report.
