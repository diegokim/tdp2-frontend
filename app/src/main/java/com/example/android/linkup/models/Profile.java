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

    public void commitChanges() {
        setChanged();
        notifyObservers();
    }
}
