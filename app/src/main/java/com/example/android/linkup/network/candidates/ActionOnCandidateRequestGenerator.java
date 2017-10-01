package com.example.android.linkup.network.candidates;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.ServerErrorListener;

import org.json.JSONObject;

public class ActionOnCandidateRequestGenerator {

    private static final String DELETE = "delete";

    private static String getURL (String id, String action) {
        if (action.equals(DELETE)) {
            return "/users/" + id;
        } else {
            return "/users/" + id + "/actions" ;
        }
    }

    private static int getMethod (String action) {
        if (action.equals(DELETE)) {
            return Request.Method.DELETE;
        } else {
            return Request.Method.PUT;
        }
    }

    public static Request baseRequest (String id, String action, JSONObject params) {
        Response.Listener responseListener = new ActionOnCandidateResponseListener();
        Response.ErrorListener errorListener = new ServerErrorListener(NetworkErrorMessages.CANDIDATES_TAG);
        String url = getURL(id,action);
        int method = getMethod(action);
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(method, url, params, responseListener, errorListener);
        return request;
    }

    public static Request generate(String id, String action) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("action",action);
        } catch (Exception e) {
            Log.e(NetworkErrorMessages.CANDIDATES_TAG, e.getMessage());
        }
        return baseRequest(id, action,obj);
    }

    public static Request generate(String id, String action, String reason) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("action",action);
            obj.put("message", reason);
        } catch (Exception e ) {
            //TODO: handle error
            Log.e("ACTIONS", e.toString());
        }
        return baseRequest(id, action, obj);
    }



}
