package com.example.android.linkup.network;

import android.content.Context;
import android.widget.Toast;


public class DisplayLoginErrorCommand implements Command {

    private Context context;
    private String message;

    public DisplayLoginErrorCommand (Context context, String message) {
        this.context = context;
        this.message = message;
    }


    public void setMessage(String m) {
        this.message = m;
    }

    public String getMessage() {
        return this.message;
    }

    @Override
    public void excecute() {
        int duration = Toast.LENGTH_LONG;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
