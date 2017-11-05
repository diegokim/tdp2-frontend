package com.example.android.linkup.network.NotificationToken;

import com.android.volley.Request;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.settings.SaveSettingsErrorListener;
import com.example.android.linkup.network.settings.SaveSettingsResponseListener;

import org.json.JSONObject;

/**
 * Created by joaquin on 28/10/17.
 */

public class SaveNotificationTokenRequestGenerator {
    private static final int GET_SETTINGS_METHOD = Request.Method.PATCH;
    private static final String SETTINGS_ENDPOINT = "/settings";

    public static Request generate() {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += SETTINGS_ENDPOINT;
        JSONObject obj = new JSONObject();
        SaveNotificationTokenResponseListener responseListener = new SaveNotificationTokenResponseListener();
        SaveNotificationTokenErrorListener errorListener = new SaveNotificationTokenErrorListener();
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(GET_SETTINGS_METHOD, url, obj, responseListener, errorListener);
        return request;
    }
}
