# Login, Registration and Remembered Sessions

[Documentation index](README.md)

**Security warning:** This page documents the historical academic implementation, **not a recommended authentication architecture**. The original project report describes login and registration in the app chapter (PDF pp. 131–139); the current [LoginActivity.java](../app/src/main/java/com/example/busproject/LoginActivity.java) confirms the mechanism.

## What the user sees

The original XML login screen provides username, password, a remember-me checkbox, and a registration dialog. A password visibility icon switches between masked and visible input. After a successful login, the app opens the main UI; a special `server` username opens the monitoring activity.

Screenshots: [login](../assets/screenshots/02-login.png).

## Original implementation flow

1. Read username and password entered into the login form.
2. Query the Firebase Realtime Database `Users` node.
3. Check whether a child for the entered username exists.
4. Read its `Password` field and compare it with the submitted password.
5. If requested, store `loggedin`, `username` and `email` in `SharedPreferences`.
6. Open `MainActivity`, or `ServerActivity` for the special server username.

Registration also reads existing users to check names and email addresses, then writes fields to `Users/{username}`.

In [SplashActivity.java](../app/src/main/java/com/example/busproject/SplashActivity.java), the remembered `loggedin` flag is sufficient to skip the login page. In [MainActivity.java](../app/src/main/java/com/example/busproject/MainActivity.java), the username `admin` controls visibility of administrative UI items.

## Why this requires redesign before deployment

| Original prototype behavior | Risk |
|---|---|
| Passwords stored as plain values in `Users` | Exposure if database reads are permitted or account data is leaked |
| Client-side comparison of passwords | No authoritative authenticated identity |
| Admin/server access based on username comparisons | Not a reliable server-side authorization mechanism |
| Remember-me boolean in local preferences | May bypass reauthentication after credentials change |
| Password change writes directly to the database | Needs identity verification and protected update rules |
| Some Firebase failure handlers are empty | Users may receive no actionable error feedback |

The source also includes a Firebase database **URL**. A database URL by itself is generally not a secret; whether data is protected depends on access control and Firebase Security Rules.

## Suggested modernization (not implemented)

Use **Firebase Authentication** for account creation and login. Use the signed-in user's Firebase UID as the stable identity. Define roles in a trusted system, such as custom claims or server-protected records, and enforce role-based access in Firebase Security Rules. Never use visibility of an admin menu as the permission check.

A migration must also address any **real historic passwords** stored in the original database; simply removing the field from code does not erase existing data or access paths.

## Code links

- [LoginActivity.java](../app/src/main/java/com/example/busproject/LoginActivity.java)
- [SplashActivity.java](../app/src/main/java/com/example/busproject/SplashActivity.java)
- [MainActivity.java](../app/src/main/java/com/example/busproject/MainActivity.java)
- [User.java](../app/src/main/java/com/example/busproject/User.java)
- [register_dialog.xml](../app/src/main/res/layout/register_dialog.xml)

This documentation keeps the security shortcomings visible so reviewers can distinguish the original proof of concept from a production design.
