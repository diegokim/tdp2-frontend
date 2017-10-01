package com.example.android.linkup.network.candidates;

import android.util.Log;

import com.android.volley.Response;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.utils.JSONParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


class GetLinksResponseListener implements Response.Listener<JSONObject> {
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
                Log.e(NetworkErrorMessages.CANDIDATES_TAG, e.toString());
            }
            GetLinksRequestGenerator.OnGetLinksSuccessEvent event = new GetLinksRequestGenerator.OnGetLinksSuccessEvent();
            event.profiles = profiles;
            EventBus.getDefault().post(event);
        }
}
