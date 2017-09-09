package com.example.android.linkup.network.get_profile;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.Command;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkRequestQueue;

import org.json.JSONObject;

public class GetProfileRequestGenerator {
    private static final int GET_PROFILE_METHOD = Request.Method.GET;
    private static final String PROFILE_ENDPOINT = "/profile";
    private CustomJsonObjectRequest request;

    public GetProfileRequestGenerator(Profile profileToFill, Command onErrorCommand) {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += PROFILE_ENDPOINT;
        JSONObject obj = new JSONObject();
        GetProfileResponseListener responseListener = new GetProfileResponseListener(profileToFill);
        GetProfileErrorListener errorListener = new GetProfileErrorListener(onErrorCommand);
        request = new CustomJsonObjectRequest(GET_PROFILE_METHOD, url, obj, responseListener, errorListener);
    }

    public Request getRequest() {
        return request;
    }

}
