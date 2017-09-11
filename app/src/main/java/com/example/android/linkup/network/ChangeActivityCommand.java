package com.example.android.linkup.network;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.android.linkup.profile.ProfileActivity;

public class ChangeActivityCommand implements Command {

    Context context;

    public ChangeActivityCommand (Context context) {
        this.context = context;
    }

    @Override
    public void excecute() {
        Log.d("Debug: ", "Pasa activity de login a principal");
        Intent intent = new Intent(context, ProfileActivity.class);
        context.startActivity(intent);
    }
}
