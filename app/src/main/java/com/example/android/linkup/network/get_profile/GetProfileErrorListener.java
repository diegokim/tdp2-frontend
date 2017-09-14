package com.example.android.linkup.network.get_profile;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.network.Command;

public class GetProfileErrorListener implements Response.ErrorListener  {

    private Command onErrorCommand;
    private Profile profile;

    public GetProfileErrorListener(Command onErrorCommand, Profile profile) {
        this.onErrorCommand = onErrorCommand;
        this.profile = profile;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        onErrorCommand.excecute();
        profile.commitChanges();
    }
}
