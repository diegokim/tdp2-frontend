package com.example.android.linkup.network.edit_profile;


import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.get_profile.GetProfileErrorListener;
import com.example.android.linkup.network.get_profile.GetProfileResponseListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileRequestGenerator  {

    private static final String PROFILE_ENDPOINT = "/profile";
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
        Response.ErrorListener errorListener = new EditProfileErrorListener();
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(EDIT_PROFILE_METHOD, url, obj, responseListener, errorListener);
        return request;
    }

    public static class EditProfileResponseListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            EventBus.getDefault().post(new EditProfileSuccessEvent());
        }

        public class EditProfileSuccessEvent {}
    }

    private static class EditProfileErrorListener implements Response.ErrorListener {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.e(NetworkErrorMessages.EDIT_PROFILE_TAG, error.toString());
            EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("Ha ocurrido un error! por favor intenta nuevamente mas tarde"));
        }
    }
}
