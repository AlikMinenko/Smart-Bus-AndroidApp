package com.example.busproject;

import android.app.Application;

public class User extends Application {
    private static String UserName;
    private static String Email;
    private static String Password;

    public User() {

    }

    public static String getUserName() {
        return UserName;
    }

    public static void setUserName(String userName) {
        UserName = userName;
    }

    public static String getEmail() {
        return Email;
    }

    public static void setEmail(String email) {
        Email = email;
    }

    public static String getPassword() {
        return Password;
    }

    public static void setPassword(String password) {
        Password = password;
    }
}

