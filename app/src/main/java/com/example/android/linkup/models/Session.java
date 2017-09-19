package com.example.android.linkup.models;

public class Session {
    public Profile myProfile;
    private static Session instance;

    private Session () {
        this.myProfile = new Profile();
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
}
