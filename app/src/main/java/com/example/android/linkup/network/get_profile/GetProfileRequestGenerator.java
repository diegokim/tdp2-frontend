package com.example.android.linkup.network.get_profile;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.ServerErrorListener;

import org.json.JSONObject;

public class GetProfileRequestGenerator {
    private static final int GET_PROFILE_METHOD = Request.Method.GET;
    private static final String PROFILE_ENDPOINT = "/users/me/profile";

    public static Request generate() {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += PROFILE_ENDPOINT;
        JSONObject obj = new JSONObject();
        GetProfileResponseListener responseListener = new GetProfileResponseListener();
        Response.ErrorListener errorListener = new ServerErrorListener(NetworkErrorMessages.GET_PROFILE_TAG);
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(GET_PROFILE_METHOD, url, obj, responseListener, errorListener);
        return request;
    }

}
