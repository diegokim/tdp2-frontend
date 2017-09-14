package com.example.android.linkup.network.register;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.Command;

/**
 * Created by diegokim on 9/11/17.
 */

public class RegisterErrorListener implements Response.ErrorListener {
    Command command;

    public RegisterErrorListener(Command onErrCommand) {
        command = onErrCommand;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        command.excecute();
    }
}
