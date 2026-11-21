# Firebase Realtime Database and Data Models

[Documentation index](README.md)

The project report describes Firebase as the link between the Android screens and the smart-bus hardware (report PDF pp. 44, 151–163 and 203–210). The Java source uses Realtime Database `DatabaseReference`, `DataSnapshot`, and `ValueEventListener` throughout the app.

## Main database paths used by the code

The hierarchy below is **schematic**. It shows named fields visible in the Java source; it is not an exported production dataset or a complete guaranteed schema.

```text
Realtime Database
├── Users
│   └── {username}
│       ├── UserName
│       ├── Email
│       ├── Password               # Historical insecure plaintext field
│       ├── Mode                   # Used by the historical admin flow
│       └── Tickets
│           └── {lineNumber}
│               ├── bus
│               ├── firststation
│               ├── laststation
│               ├── totalsits
│               ├── linenum
│               ├── sitpositions
│               └── allowed
├── Lines
│   └── {lineNumber}
│       ├── number
│       ├── description
│       ├── bus
│       ├── stations
│       ├── prices
│       ├── deptime
│       └── totaltime
├── Buses
│   └── {busId}
│       ├── currentST
│       ├── currentUser
│       ├── totalpeople
│       ├── functions
│       ├── places                 # 15 Boolean seat flags
│       ├── available
│       ├── live                   # Camera stream host/IP
│       ├── temp
│       └── goingdown
└── Project
    └── SendToFPGA                 # Seen in DevTest.java
```

**Case matters** in Firebase keys. The `Bus`, `Line` and `Ticket` model properties are serialized from the Java models; some user fields are written manually using uppercase names. Do not assume every user node contains every field listed here.

## Classes and their responsibilities

| Model | Important properties | Used by |
|---|---|---|
| [Bus.java](../app/src/main/java/com/example/busproject/Bus.java) | `currentST`, `currentUser`, `totalpeople`, `functions`, `places`, `live`, `goingdown` | Admin screens, server monitoring, reservation and boarding |
| [Line.java](../app/src/main/java/com/example/busproject/Line.java) | Route number, description, bus ID, stations, price, departure and ride duration | Route list and detail screens |
| [Ticket.java](../app/src/main/java/com/example/busproject/Ticket.java) | Bus, pickup/drop-off station, seat count/positions, route, `allowed` | Ticket list and QR boarding |
| [User.java](../app/src/main/java/com/example/busproject/User.java) | Static username, email and password fields | Global application user state |

The original report includes separate explanations of these models and the `RecyclerView` adapters (PDF pp. 203–210).

## A typical Firebase listener

[LinesFragment.java](../app/src/main/java/com/example/busproject/LinesFragment.java) reads `Lines` as snapshots, transforms them into `Line` objects and updates the UI. [LineInfo.java](../app/src/main/java/com/example/busproject/LineInfo.java) watches `Buses/{busId}/places` to update available and occupied seat images.

The same database is used for write operations. For example, confirming a ticket updates both a bus's seat flags and the current user's ticket record.

## Why the schema is prototype-specific

- `places` is an array-like collection with **15** Boolean entries; the index corresponds to a seat.
- Several flows reference the example bus ID `1bus1` directly rather than supporting arbitrary buses.
- `functions` holds numeric control values consumed by the connected hardware prototype.
- There is no checked-in database export or centrally versioned schema migration.
- Multiple separate database writes do not guarantee an **atomic** seat reservation.

## Security and robustness concerns

The source contains an original Firebase database URL, but a database URL is not proof of public access. **Security Rules have not been audited from this repository.** The account code stores a raw password field, so the original database must not be exposed to real users.

A safer approach would use Firebase Authentication, UID-scoped paths, security rules, transaction-based seat allocation, explicit hardware command types, and null-safe parsing of database snapshots.

Related: [Authentication](authentication.md) · [Ticket booking](ticket-booking.md) · [OCR and monitoring](camera-ocr.md).
