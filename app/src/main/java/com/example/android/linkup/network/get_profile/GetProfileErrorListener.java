package com.example.android.linkup.network.get_profile;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.Command;

public class GetProfileErrorListener implements Response.ErrorListener  {

    private Command onErrorCommand;

    public GetProfileErrorListener(Command onErrorCommand) {
        this.onErrorCommand = onErrorCommand;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        onErrorCommand.excecute();
    }
}
