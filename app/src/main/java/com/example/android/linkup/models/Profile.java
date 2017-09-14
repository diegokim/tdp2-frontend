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
        name = "Diego Kim";
        interests = new String[0];
        description = "";
        education = "FIUBA";
        gender = "";
        work = "Estudiante";
        photos = new String[0];
        profilePhoto = "";
        age = 18;
    }

    public void commitChanges() {
        setChanged();
        notifyObservers();
    }
}
