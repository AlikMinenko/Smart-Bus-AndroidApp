# Adding Buses and Bus Lines

[Back to documentation](README.md)

**Report reference:** PDF pages 124–125 and 164–176.

The original app has additional menu options for the username `admin`. These screens allow the user to add buses and create bus lines.

## Adding a bus

[Java: AddBus.java](../app/src/main/java/com/example/busproject/AddBus.java) · [XML: activity_add_bus.xml](../app/src/main/res/layout/activity_add_bus.xml)

The user enters a bus identifier and the IP address for the camera stream.

The code checks Firebase and creates a new `Bus` object. The new bus is saved under the `Buses` section.

## Adding a bus line

[Java: AddLine.java](../app/src/main/java/com/example/busproject/AddLine.java) · [XML: activity_add_line.xml](../app/src/main/res/layout/activity_add_line.xml)

The user enters:

- Bus line number
- Line description
- Bus to use for the line
- Bus stops
- Departure time
- Travel time
- Ticket price

A separate dialog allows stations to be added to or removed from the list.

After the line is saved, the app writes it into the `Lines` section of Firebase and marks the selected bus as unavailable for another line.

![Adding a line in the original project](../assets/screenshots/09-add-route.png)

## Removing a line

[LineInfo.java](../app/src/main/java/com/example/busproject/LineInfo.java) also contains an option that removes a bus line from Firebase and marks the related bus as available again.

**Simple explanation:** These screens were created to manage the bus lines used in the original project model.
