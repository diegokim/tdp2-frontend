package com.example.android.linkup.models;

public class Session {
    public Profile myProfile;
    private static Session instance;
    public Settings mySettings;
    public Token myToken;

    private Session () {
        this.myProfile = new Profile();
        this.mySettings = new Settings();
        this.myToken = new Token();
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }
}
