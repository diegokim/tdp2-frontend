package com.example.android.linkup.network.register;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.models.Settings;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.utils.JSONParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterRequestGenerator {

    private static final String REGISTER_ENDPOINT = "/users/me/profile";
    private static final int REGISTER_METHOD = Request.Method.PATCH;
    private static final String PROFILE_PHOTO_KEY = "photo";
    private static final String PHOTOS_KEY = "photos" ;
    private static final String DESCRIPTION_KEY = "description";

    public static Request generate(RegisterData data) {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += REGISTER_ENDPOINT;
        JSONObject obj = new JSONObject();
        try {
            obj.put(PROFILE_PHOTO_KEY, data.profilePhoto);
            JSONArray photosArr = new JSONArray();
            ArrayList<String> photos = data.photos;
            for (int i =0; i < photos.size() ; i ++ ) {
                photosArr.put(photos.get(i));
            }
            obj.put(PHOTOS_KEY, photosArr);
            obj.put(DESCRIPTION_KEY, data.description);
            JSONArray location = new JSONArray();
            location.put(data.location.getLongitude());
            location.put( data.location.getLatitude());
            obj.put("location", location);
        } catch (Exception e) {
            Log.e(NetworkErrorMessages.REGISTER_TAG, e.getMessage() );
        }
        RegisterErrorListener errorListener = new RegisterErrorListener();
        RegisterResponseListener responseListener = new RegisterResponseListener();
        Request request = new CustomJsonObjectRequest(REGISTER_METHOD, url, obj,responseListener, errorListener);
        return request;
    }

    public static class RegisterErrorListener implements Response.ErrorListener {

        @Override
        public void onErrorResponse(VolleyError error) {
            //TODO: handle error message
            Log.e(NetworkErrorMessages.REGISTER_TAG, error.toString());
            EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent(NetworkErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER));
        }
    }

    public static class RegisterResponseListener implements Response.Listener<JSONObject> {

        @Override
        public void onResponse(JSONObject response) {

            try {
                OnRegisterSuccessEvent event = new OnRegisterSuccessEvent();
                EventBus.getDefault().post(event);
            } catch (Exception e) {
                Log.e(NetworkErrorMessages.REGISTER_TAG, e.getMessage());
                e.printStackTrace();
            }
        }

        public static class OnLoginSuccessEvent {
            public Profile profile;
            public Settings settings;
        }

        public class OnRegisterSuccessEvent {
        }
    }

}
