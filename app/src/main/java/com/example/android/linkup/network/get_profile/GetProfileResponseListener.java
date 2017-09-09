package com.example.android.linkup.network.get_profile;

import com.android.volley.Response;
import com.example.android.linkup.models.Profile;

/**
 * Created by diegokim on 9/9/17.
 */

public class GetProfileResponseListener implements Response.Listener {

    private Profile profileToFill;

    public GetProfileResponseListener (Profile profileToFill) {
        this.profileToFill = profileToFill;
    }

    @Override
    public void onResponse(Object response) {

    }
}
