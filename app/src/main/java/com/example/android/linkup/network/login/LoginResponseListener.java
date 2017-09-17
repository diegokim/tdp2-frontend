package com.example.android.linkup.network.login;

import android.util.Log;

import com.android.volley.Response;
import com.example.android.linkup.login.Photos;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.register.RegisterRequestGenerator;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

public class LoginResponseListener implements Response.Listener<JSONObject> {
    private static final String PHOTOS_KEY = "photos";

    @Override
    public void onResponse(JSONObject response) {
        Log.e("ERR RESPONSE: ", response.toString());
        Photos photos = new Photos();
        try {
            JSONArray photosJSON = response.getJSONArray(PHOTOS_KEY);
            for (int i =0 ; i< photosJSON.length() ; i++) {
                photos.addPhoto(photosJSON.getString(i));
            }
            EventBus.getDefault().post(new PhotosEvent(photos));
        } catch (Exception e) {
            EventBus.getDefault().post(new RegisterRequestGenerator.RegisterResponseListener.RegisterSuccessEvent());
            Log.e("ERROR12345", e.toString());
        }
    }

    public static class PhotosEvent {
        public Photos photos;
        public PhotosEvent (Photos photos) {
            this.photos = photos;
        }
    }

}
