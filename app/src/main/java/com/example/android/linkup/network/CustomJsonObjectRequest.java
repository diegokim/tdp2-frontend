package com.example.android.linkup.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomJsonObjectRequest extends JsonObjectRequest {

    public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);

    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map headers = new HashMap<>();
        String accessToken = NetworkConfiguration.getInstance().accessToken;
        headers.put("Content-Type", "application/json");
        if (accessToken != null) {
            headers.put("Authorization", accessToken);
        }
        return headers;
    }
}
