package com.example.android.linkup.login;

import java.util.ArrayList;
import java.util.Observable;


public class Photos extends Observable {
    private ArrayList<String> photos;

    public Photos(){
        photos = new ArrayList<>();
    }

    public void addPhoto(String photo) {
        photos.add(photo);
    }
    public ArrayList<String> getPhotos() {
        return photos;
    }

    public void commitChanges() {
        setChanged();
        notifyObservers();
    }

}
