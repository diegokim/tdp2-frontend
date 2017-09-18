package com.example.android.linkup.network.edit_profile;


import com.android.volley.Request;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.get_profile.GetProfileErrorListener;
import com.example.android.linkup.network.get_profile.GetProfileResponseListener;

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
            e.printStackTrace();
        }

        GetProfileResponseListener responseListener = new GetProfileResponseListener();
        GetProfileErrorListener errorListener = new GetProfileErrorListener();
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(EDIT_PROFILE_METHOD, url, obj, responseListener, errorListener);
        return request;
    }
}
