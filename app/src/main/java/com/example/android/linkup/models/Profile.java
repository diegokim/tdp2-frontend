package com.example.android.linkup.models;


import java.util.Observable;

public class Profile extends Observable {
    public String name;
    public String interests[];
    public String description;
    public short age;
    public String ocupation;
    public String education;

    public void commitChanges() {
        setChanged();
        notifyObservers();
    }
}
