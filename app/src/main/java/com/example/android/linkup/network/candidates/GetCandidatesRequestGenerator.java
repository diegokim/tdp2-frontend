package com.example.android.linkup.network.candidates;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.utils.JSONParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GetCandidatesRequestGenerator {

    private static final int GET_CANDIDATES_METHOD = Request.Method.GET;
    private static final String CANDIDATES_ENDPOINT = "/user/candidates";

    public static Request generate() {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += CANDIDATES_ENDPOINT;
        JSONObject obj = new JSONObject();
        Response.Listener responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.e("Response ", response.toString());
                ArrayList<Profile> profiles = new ArrayList<>();
                try {
                    JSONArray profilesJSON = response.getJSONArray("profiles");
                    for (int i = 0 ; i < profilesJSON.length() ; i ++) {
                        JSONObject profileJSON = profilesJSON.getJSONObject(i);
                        Profile profile = JSONParser.getProfileWithoutPhotos(profileJSON);
                        if (profile != null )
                            profiles.add(profile);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                EventBus.getDefault().post(profiles);
            }
        } ;
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("Error"));
            }
        };
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(GET_CANDIDATES_METHOD, url, obj, responseListener, errorListener);
        return request;
    }
}
