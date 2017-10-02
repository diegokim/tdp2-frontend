package com.example.android.linkup.models;


public class ActiveChatProfile {
    private static ActiveChatProfile instance;
    private Profile profile;

    private ActiveChatProfile() {
        this.profile = new Profile();
    }

    public static ActiveChatProfile getInstance() {
        if (instance == null) {
            instance = new ActiveChatProfile();
        }
        return instance;
    }

    public void update(Profile p) {
        this.profile.update(p);
    }
}

