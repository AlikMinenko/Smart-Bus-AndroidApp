# Choosing a Bus Line and Reserving Seats

[Back to documentation](README.md)

**Report reference:** PDF pages 125, 154–163 and 177–190.

[Main Java file: LineInfo.java](../app/src/main/java/com/example/busproject/LineInfo.java)

The user starts by selecting a bus line from the list. The app opens a new screen showing information about that line.

## Line information

The `LineInfo` screen displays information such as the line number, description, price, travel time, stations, number of passengers and bus temperature.

Some of the information is read from **Firebase**.

## Choosing stations

When the user opens the ticket dialog:

1. The user chooses a **pickup station**.
2. The user chooses a **drop-off station** that comes after the pickup station.
3. The user chooses seats on the bus.

The list of drop-off stations changes depending on the selected pickup station.

## Choosing seats

The bus seat diagram contains **15 positions**.

- **Blue seats:** Available.
- **Red seats:** Already occupied.
- **Green seats:** Selected by the user.

The code uses the `places` information stored in Firebase to decide which seats are occupied. The user can select **up to four seats** for one reservation.

![Seat selection dialog from the original report](../assets/screenshots/06-seat-selection.png)

## Saving the ticket

After the user confirms the selection, the code:

1. Marks the selected seats as occupied in Firebase.
2. Creates a `Ticket` object with the bus, stations and selected seats.
3. Saves the ticket under the current user's tickets in Firebase.

The user can then see the ticket in [TicketsFragment.java](../app/src/main/java/com/example/busproject/TicketsFragment.java).

**Important:** The word **Buy** appears in the original app. The Java code shown in the report performs a ticket reservation; it does not show an online credit-card payment.
