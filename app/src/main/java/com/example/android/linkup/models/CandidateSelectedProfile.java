package com.example.android.linkup.models;

public class CandidateSelectedProfile {
    public Profile profile;
    private static CandidateSelectedProfile instance;

    private CandidateSelectedProfile () {
        this.profile = new Profile();
    }
    public static CandidateSelectedProfile getInstance() {
        if (instance == null) {
            instance = new CandidateSelectedProfile();
        }
        return instance;
    }
}
