package com.example.android.linkup.network.settings;

import com.android.volley.Request;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;

import org.json.JSONObject;


public class SaveSettingsRequestGenerator {
    private static final int GET_SETTINGS_METHOD = Request.Method.PATCH;
    private static final String SETTINGS_ENDPOINT = "/settings";

    public static Request generate() {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += SETTINGS_ENDPOINT;
        JSONObject obj = new JSONObject();
        SaveSettingsResponseListener responseListener = new SaveSettingsResponseListener();
        SaveSettingsErrorListener errorListener = new SaveSettingsErrorListener();
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(GET_SETTINGS_METHOD, url, obj, responseListener, errorListener);
        return request;
    }

}
