package com.example.android.linkup.network.login;


import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.Command;

import java.io.UnsupportedEncodingException;

public class LoginErrorListener implements Response.ErrorListener {

    private Command command;
    private Context context;

    public LoginErrorListener(Command onErrorCommand, Context c) {
        command = onErrorCommand;
        context = c;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("ServerResponse",String.valueOf(error.networkResponse.statusCode));
        String body = "Nada por aqui";
        //get response body and parse with appropriate encoding
        if(error.networkResponse.data != null) {
            try {
                body = new String(error.networkResponse.data,"UTF-8");
                Log.d("ServerResponse",body);
                String[] body_parts = body.split(":");
                if (body_parts[1] != null && body_parts[1].length() > 0 && body_parts[1].charAt(body_parts[1].length() - 1) == '}') {
                    body_parts[1] = body_parts[1].substring(0, body_parts[1].length() - 1);
                }
                int duration = Toast.LENGTH_LONG;
                Toast toast = Toast.makeText(context, body_parts[1], duration);
                toast.show();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }




        command.excecute();
    }
}
