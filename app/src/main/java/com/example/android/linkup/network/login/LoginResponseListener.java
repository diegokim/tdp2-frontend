package com.example.android.linkup.network.login;

import android.util.Log;

import com.android.volley.Response;
import com.example.android.linkup.login.Photos;
import com.example.android.linkup.network.Command;

import org.json.JSONArray;
import org.json.JSONObject;

public class LoginResponseListener implements Response.Listener<JSONObject> {
    private static final String PHOTOS_KEY = "photos";
    private Command command;
    private Photos photos;

    public LoginResponseListener(Command onSuccessCommand, Photos photos) {
        this.command = onSuccessCommand;
        this.photos = photos;
    }

    @Override
    public void onResponse(JSONObject response) {
        try {
            JSONArray photos = response.getJSONArray(PHOTOS_KEY);
            for (int i =0 ; i< photos.length() ; i++) {
                this.photos.addPhoto(photos.getString(i));
            }
            command.excecute();

        } catch (Exception e) {
            // TODO: handle exception
            Log.e("ERROR12345", e.toString());

        }
    }
}
