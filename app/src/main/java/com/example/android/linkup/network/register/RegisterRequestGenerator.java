package com.example.android.linkup.network.register;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.WebServiceManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegisterRequestGenerator {

    private static final String REGISTER_ENDPOINT = "/profile";
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
        } catch (Exception e) {

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
            EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("error"));
        }
    }

    public static class RegisterResponseListener implements Response.Listener<JSONObject> {

        @Override
        public void onResponse(JSONObject response) {
            EventBus.getDefault().post(new RegisterSuccessEvent());
        }

        public static class RegisterSuccessEvent {}
    }

}