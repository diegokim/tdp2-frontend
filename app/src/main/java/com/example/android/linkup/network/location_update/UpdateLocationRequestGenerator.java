package com.example.android.linkup.network.location_update;


import android.location.Location;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.WebServiceManager;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UpdateLocationRequestGenerator {
    private static final String PROFILE_ENDPOINT = "/profile";
    private static final int EDIT_PROFILE_METHOD = Request.Method.PATCH;

    public static Request generate(Location location) {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += PROFILE_ENDPOINT;
        JSONObject obj = new JSONObject();
        try {
            JSONArray mLocation = new JSONArray();
            mLocation.put( location.getLatitude());
            mLocation.put(location.getLongitude());
            obj.put("location", location);
        } catch (JSONException e) {
            Log.e(NetworkErrorMessages.UPDATE_LOCATION_TAG, e.getMessage());
            e.printStackTrace();
        }

        Response.Listener responseListener = new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                EventBus.getDefault().post(new OnUpdateLocationSuccessEvent());
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(NetworkErrorMessages.UPDATE_LOCATION_TAG,error.toString());
                EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("Error"));
            }
        };
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(EDIT_PROFILE_METHOD, url, obj, responseListener, errorListener);
        return request;
    }
    public static class OnUpdateLocationSuccessEvent {
    }
}
