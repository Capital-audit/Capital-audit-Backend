package com.example.capitalauditbackend.model;

public class user {
    private static String username;
    private static int user_id;

    public static void setUser_id(int user_id) {
        user.user_id = user_id;
    }

    public static int getUser_id() {
        return user_id;
    }
    public static void setUsername(String username)
    {
        user.username = username;
    }
    public static String getUsername() {
        return username;
    }
}
