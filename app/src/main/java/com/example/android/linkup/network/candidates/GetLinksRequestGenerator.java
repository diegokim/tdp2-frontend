package com.example.android.linkup.network.candidates;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.models.Link;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.ServerErrorListener;
import com.example.android.linkup.utils.JSONParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GetLinksRequestGenerator {
    private static final int GET_LINKS_METHOD = Request.Method.GET;
    private static final String GET_LINKS_ENDPOINT = "/users/me/links";

    public static Request generate() {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += GET_LINKS_ENDPOINT;
        JSONObject obj = new JSONObject();

        Response.Listener<JSONObject> responseListener = new GetLinksResponseListener();

        Response.ErrorListener errorListener = new ServerErrorListener(NetworkErrorMessages.CANDIDATES_TAG);
        return new CustomJsonObjectRequest(GET_LINKS_METHOD,url,obj,responseListener,errorListener);
    }
}
