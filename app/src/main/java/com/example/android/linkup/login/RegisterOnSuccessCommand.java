package com.example.android.linkup.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.android.linkup.network.Command;
import com.example.android.linkup.profile.ProfileActivity;


public class RegisterOnSuccessCommand implements Command {

    private Activity activity;

    public RegisterOnSuccessCommand (Activity activity) {
        this.activity = activity;
    }

    @Override
    public void excecute() {
        Intent intent = new Intent(activity, ProfileActivity.class);
        activity.startActivity(intent);
        activity.finish();
    }
}
