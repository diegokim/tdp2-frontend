package com.example.android.linkup.network.candidates;

import android.graphics.Bitmap;
import android.util.Log;

import com.android.volley.Response;
import com.example.android.linkup.models.Candidate;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.utils.Base64Converter;
import com.example.android.linkup.utils.JSONParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by diegokim on 10/9/17.
 */

public class GetCandidatesResponseListener implements Response.Listener<JSONObject> {
    @Override
    public void onResponse(JSONObject response) {
        Log.e("Response ", response.toString());
        ArrayList<Candidate> candidates = new ArrayList<>();
        ArrayList<Bitmap> photos = new ArrayList<>();
        Base64Converter converter = new Base64Converter();

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
                    photos.add(converter.Base64ToBitmap(candidate.profile.profilePhoto));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace(System.err);
            Log.e(NetworkErrorMessages.CANDIDATES_TAG, e.getMessage());
        }
        EventBus.getDefault().post(candidates);
    }

    public class OnGetCandidatesSuccessEvent {
        ArrayList<Candidate> candidates;
        ArrayList<Bitmap> profilePhotos;
    }
}
