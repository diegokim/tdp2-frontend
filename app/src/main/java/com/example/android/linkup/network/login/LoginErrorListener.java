package com.example.android.linkup.network.login;


import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.Command;

public class LoginErrorListener implements Response.ErrorListener {

    private Command command;

    public LoginErrorListener(Command onErrorCommand) {
        command = onErrorCommand;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        command.excecute();
    }
}
