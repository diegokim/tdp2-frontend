package com.example.android.linkup.network;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class CustomJsonObjectRequest extends JsonObjectRequest {

    public CustomJsonObjectRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        Log.d("Req URL",url);
        Log.d("Req Body", jsonRequest.toString());
        this.setRetryPolicy(new DefaultRetryPolicy((int) TimeUnit.SECONDS.toMillis(20),
                0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        Map headers = new HashMap<>();
        String accessToken = NetworkConfiguration.getInstance().accessToken;
        Log.i("ACCESS TOKEN", accessToken);
        headers.put("Content-Type", "application/json");
        if (accessToken != null) {
            headers.put("Authorization", accessToken);
        }
        return headers;
    }
}
