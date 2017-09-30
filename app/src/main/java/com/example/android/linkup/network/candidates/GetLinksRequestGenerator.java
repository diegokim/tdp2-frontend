package com.example.android.linkup.network.candidates;


import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
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

        Response.Listener<JSONObject> responseListener = new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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
                    Log.e(NetworkErrorMessages.CANDIDATES_TAG, e.getMessage());
                }
                OnGetLinksSuccessEvent event = new OnGetLinksSuccessEvent();
                event.profiles = profiles;
                EventBus.getDefault().post(event);
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        };
        return new CustomJsonObjectRequest(GET_LINKS_METHOD,url,obj,responseListener,errorListener);
    }


    public static class OnGetLinksSuccessEvent {
        public ArrayList<Profile> profiles;
    }
}
