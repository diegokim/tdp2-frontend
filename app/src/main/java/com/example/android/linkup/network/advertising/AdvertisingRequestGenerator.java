package com.example.android.linkup.network.advertising;

import com.android.volley.Request;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;

import org.json.JSONObject;


public class AdvertisingRequestGenerator {

    private static final int ADVERTISING_METHOD = Request.Method.GET;
    private static final String ADVERTISING_ENDPOINT = "/users/advertising";

    public static Request generate() {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += ADVERTISING_ENDPOINT;
        JSONObject obj = new JSONObject();

        AdvertisingResponseListener responseListener = new AdvertisingResponseListener();
        AdvertisingErrorListener errorListener = new AdvertisingErrorListener();
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(ADVERTISING_METHOD, url, obj, responseListener, errorListener);
        return request;
    }
}
