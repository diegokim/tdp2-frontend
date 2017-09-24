package com.example.android.linkup.network.login;

import android.util.Log;

import com.android.volley.Response;
import com.example.android.linkup.login.Photos;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.register.RegisterRequestGenerator;
import com.example.android.linkup.utils.JSONParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginResponseListener implements Response.Listener<JSONObject> {
    private static final String PHOTOS_KEY = "photos";

    @Override
    public void onResponse(JSONObject response) {
        Photos photos = new Photos();
        try {
            JSONArray photosJSON = response.getJSONArray(PHOTOS_KEY);
            for (int i =0 ; i< photosJSON.length() ; i++) {
                photos.addPhoto(photosJSON.getString(i));
            }
            EventBus.getDefault().post(new PhotosEvent(photos));
        } catch (Exception e) {
            RegisterRequestGenerator.RegisterResponseListener.OnLoginSuccessEvent event;
            event = new RegisterRequestGenerator.RegisterResponseListener.OnLoginSuccessEvent();
            JSONObject profileJSON = null;
            try {
                profileJSON = response.getJSONObject("profile");
                Profile profile = JSONParser.getProfile(profileJSON);
                event.profile = profile;
                EventBus.getDefault().post(event);
            } catch (JSONException e1) {
                Log.e(NetworkErrorMessages.LOGIN_TAG, e1.getMessage());
                e1.printStackTrace();
                EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent(NetworkConfiguration.SERVER_REQUEST_ERROR));
            }

        }
    }

    public static class PhotosEvent {
        public Photos photos;
        public PhotosEvent (Photos photos) {
            this.photos = photos;
        }
    }

}
