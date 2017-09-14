package com.example.android.linkup.network.register;

import com.android.volley.Response;
import com.example.android.linkup.network.Command;

import org.json.JSONObject;

/**
 * Created by diegokim on 9/11/17.
 */

public class RegisterResponseListener implements Response.Listener<JSONObject> {
    private Command command;

    public RegisterResponseListener(Command onSuccessCommand) {
    command = onSuccessCommand;
    }

    @Override
    public void onResponse(JSONObject response) {
        command.excecute();
    }
}
