package com.example.android.linkup.network.candidates;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.models.Candidate;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
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
                ArrayList<Candidate> candidates = new ArrayList<>();

                try {
                    JSONArray profilesJSON = response.getJSONArray("profiles");
                    for (int i = 0 ; i < profilesJSON.length() ; i ++) {
                        JSONObject profileJSON = profilesJSON.getJSONObject(i);
                        Profile profile = JSONParser.getProfile(profileJSON);
                        int distance = profileJSON.getInt("distance");
                        if (profile != null ){
                            Candidate candidate = new Candidate();
                            candidate.profile = profile;
                            candidate.distance = distance;
                            candidates.add(candidate);
                        }


                    }
                } catch (JSONException e) {
                    Log.e(NetworkErrorMessages.CANDIDATES_TAG, e.getMessage());
                }
                EventBus.getDefault().post(candidates);
            }
        } ;
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(NetworkErrorMessages.CANDIDATES_TAG, error.toString());
                EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent(NetworkErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER));
            }
        };
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(GET_CANDIDATES_METHOD, url, obj, responseListener, errorListener);
        return request;
    }
}
