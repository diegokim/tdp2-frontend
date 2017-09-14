package com.example.android.linkup.network;

import android.content.Context;
import android.widget.Toast;

public class ToastErrorCommand implements Command {

    Context context;
    String message;

    public ToastErrorCommand (Context context, String message) {
        this.context = context;
        this.message = message;
    }

    @Override
    public void excecute() {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, message, duration);
        toast.show();
    }
}
