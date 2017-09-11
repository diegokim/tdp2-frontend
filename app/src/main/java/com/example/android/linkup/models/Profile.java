package com.example.android.linkup.models;


import java.util.ArrayList;
import java.util.Observable;

public class Profile extends Observable {
    public String name;
    public String interests[];
    public String description;
    public String gender;
    public int age;
    public String ocupation;
    public String education;
    public String photos[];
    public String profilePhoto;

    public Profile () {
        name = "";
        interests = new String[0];
        description = "";
        gender = "";
        ocupation = "";
        photos = new String[0];
        profilePhoto = "";
        age = 18;
    }

    public void commitChanges() {
        setChanged();
        notifyObservers();
    }
}
