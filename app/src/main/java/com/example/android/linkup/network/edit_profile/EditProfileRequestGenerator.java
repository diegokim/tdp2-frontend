package com.example.android.linkup.network.edit_profile;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.ServerErrorListener;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileRequestGenerator  {

    private static final String PROFILE_ENDPOINT = "/users/me/profile";
    private static final int EDIT_PROFILE_METHOD = Request.Method.PATCH;

    public static Request generate(String description, String photo) {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += PROFILE_ENDPOINT;
        JSONObject obj = new JSONObject();
        try {
            obj.put("description",description);
            obj.put("photo", photo);
        } catch (JSONException e) {
            Log.e(NetworkErrorMessages.EDIT_PROFILE_TAG, e.getMessage());
        }

        Response.Listener responseListener = new EditProfileResponseListener();
        Response.ErrorListener errorListener = new ServerErrorListener(NetworkErrorMessages.EDIT_PROFILE_TAG);
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(EDIT_PROFILE_METHOD, url, obj, responseListener, errorListener);
        return request;
    }
}
