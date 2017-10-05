package com.example.android.linkup.network.candidates;

import android.util.Log;

import com.android.volley.Response;
import com.example.android.linkup.models.Link;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.utils.JSONParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class GetLinksResponseListener implements Response.Listener<JSONObject> {
        @Override
        public void onResponse(JSONObject response) {
            ArrayList<Link> links = new ArrayList<>();
            Link link;
            try {
                JSONArray profilesJSON = response.getJSONArray("profiles");
                for (int i = 0 ; i < profilesJSON.length() ; i ++) {
                    JSONObject profileJSON = profilesJSON.getJSONObject(i);
                    Log.e("PROFILE", profileJSON.toString());
                    Profile profile = JSONParser.getProfileWithoutPhotos(profileJSON);
                    String type = profileJSON.getString("type");
                    link = new Link();
                    if (profile != null )
                        link.profile = profile;
                        link.type = type;
                        links.add(link);
                }
            } catch (JSONException e) {
                Log.e(NetworkErrorMessages.CANDIDATES_TAG, e.toString());
                e.printStackTrace(System.err);
            }
            OnGetLinksSuccessEvent event = new OnGetLinksSuccessEvent();
            event.links = links;
            EventBus.getDefault().post(event);
        }

    public static class OnGetLinksSuccessEvent {
        public ArrayList<Link> links;
    }
}
