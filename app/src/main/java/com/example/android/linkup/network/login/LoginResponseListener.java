package com.example.android.linkup.network.login;

import com.android.volley.Response;
import com.example.android.linkup.network.Command;

/**
 * Created by diegokim on 9/10/17.
 */

public class LoginResponseListener implements Response.Listener {
    private Command command;

    public LoginResponseListener(Command onSuccessCommand) {
    this.command = onSuccessCommand;
    }

    @Override
    public void onResponse(Object response) {
        command.excecute();
    }
}
