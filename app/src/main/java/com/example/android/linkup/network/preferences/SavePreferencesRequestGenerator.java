package com.example.android.linkup.network.preferences;

import com.android.volley.Request;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;

import org.json.JSONObject;


public class SavePreferencesRequestGenerator {
    private static final int GET_SETTINGS_METHOD = Request.Method.PATCH;
    private static final String SETTINGS_ENDPOINT = "/settings";

    public static Request generate() {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += SETTINGS_ENDPOINT;
        JSONObject obj = new JSONObject();
        SavePreferencesResponseListener responseListener = new SavePreferencesResponseListener();
        SavePreferencesErrorListener errorListener = new SavePreferencesErrorListener();
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(GET_SETTINGS_METHOD, url, obj, responseListener, errorListener);
        return request;
    }

}
