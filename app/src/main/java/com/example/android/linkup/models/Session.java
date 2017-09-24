package com.example.android.linkup.models;

public class Session {
    public Profile myProfile;
    private static Session instance;
    public Settings mySettings;

    private Session () {
        this.myProfile = new Profile();
        this.mySettings = new Settings();
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
}
