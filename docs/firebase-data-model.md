# Firebase and the Java Data Classes

[Back to documentation](README.md)

**Report reference:** PDF page 44 and pages 203–210.

The Android app uses **Firebase Realtime Database** to store and read information. The Java code uses classes such as `DatabaseReference` and `ValueEventListener` to communicate with Firebase.

## Main information saved in Firebase

The app works mainly with:

- `Users` — user information and tickets.
- `Lines` — bus line information and station lists.
- `Buses` — bus information, such as current station, seats and temperature.

These are the same database sections referenced in the Java explanations in the project report.

## Bus.java

[Open Bus.java](../app/src/main/java/com/example/busproject/Bus.java)

This class stores information about one bus, including:

- Current station (`currentST`)
- Current user (`currentUser`)
- Number of people (`totalpeople`)
- Bus temperature (`temp`)
- Camera address (`live`)
- Seat information (`places`)

The code starts the bus with **15 available seats**, represented by 15 `false` values in a list.

## Line.java

[Open Line.java](../app/src/main/java/com/example/busproject/Line.java)

This class holds information about a bus line: its number, description, related bus, station list, price, departure time and journey duration.

## Ticket.java

[Open Ticket.java](../app/src/main/java/com/example/busproject/Ticket.java)

This class stores the ticket's bus, first station, last station, selected seats, number of seats and line number. It also stores a Boolean value named `allowed`, which is used in the QR boarding process.

## User.java

[Open User.java](../app/src/main/java/com/example/busproject/User.java)

This class keeps the username, email and password values used by the original app.

## What are getters and setters?

The report explains the `get` and `set` methods in these classes:

- A **getter** returns a stored value.
- A **setter** changes a stored value.

For example, `getCurrentST()` returns the current station saved in a `Bus` object, and `setCurrentST(...)` sets it.

**Simple explanation:** The data classes help the app organize information received from Firebase, instead of working only with individual text values.
