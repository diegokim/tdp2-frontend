package com.example.android.linkup.models;


import java.util.Observable;

public class Profile extends Observable {
    public String name;
    public String interests[];
    public String description;
    public String gender;
    public int age;
    public String work;
    public String education;
    public String photos[];
    public String profilePhoto;


    public Profile () {
        name = "";
        interests = new String[0];
        description = "";
        education = "";
        gender = "";
        work = "";
        photos = new String[0];
        profilePhoto = "";
        age = 18;
    }

    public void commitChanges() {
        setChanged();
        notifyObservers();
    }

    public void update(Profile profile) {
        this.name = profile.name;
        this.interests = profile.interests;
        this.description = profile.description;
        this.education = profile.education;
        this.gender = profile.gender;
        this.work = profile.work;
        this.photos = profile.photos;
        this.profilePhoto = profile.profilePhoto;
        this.age = profile.age;

    }
}
