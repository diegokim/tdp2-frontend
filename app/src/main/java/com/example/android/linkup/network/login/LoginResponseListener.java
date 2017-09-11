package com.example.android.linkup.network.login;

import android.util.Log;

import com.android.volley.Response;
import com.example.android.linkup.network.Command;
import com.example.android.linkup.network.DisplayLoginErrorCommand;

import org.json.JSONObject;

public class LoginResponseListener implements Response.Listener <JSONObject> {
    private Command command;

    public LoginResponseListener(Command onSuccessCommand) {
    this.command = onSuccessCommand;
    }

    @Override
    public void onResponse(JSONObject response) {
        Log.d("ServerResponse","GET /login request success");
        command.excecute();
    }
}
