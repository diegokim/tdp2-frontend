package com.example.android.linkup.login;

import java.util.ArrayList;
import java.util.Observable;
import java.util.function.Consumer;


public class Photos extends Observable {
    private ArrayList<String> photos;

    public Photos(){
        photos = new ArrayList<>();
    }

    public Photos(ArrayList<String> photos) {
        this.photos = new ArrayList<>();
        for (int i =0 ; i < photos.size() ; i ++) {
            addPhoto(photos.get(i));
        }
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
