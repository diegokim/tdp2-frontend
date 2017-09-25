package com.example.android.linkup.network.login;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;

import org.json.JSONObject;


public class LoginRequestGenerator {
    private static final int GET_PROFILE_METHOD = Request.Method.GET;
    private static final String PROFILE_ENDPOINT = "/login";

    public static Request generate() {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += PROFILE_ENDPOINT;
        JSONObject obj = new JSONObject();

        LoginErrorListener errorListener = new LoginErrorListener();
        LoginResponseListener responseListener = new LoginResponseListener();

        CustomJsonObjectRequest request = new CustomJsonObjectRequest(GET_PROFILE_METHOD, url, obj, responseListener, errorListener);

        request.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        return request;
    }
}
