package com.example.android.linkup.network.register;

import android.util.Log;

import com.android.volley.Response;
import com.example.android.linkup.models.Profile;
import com.example.android.linkup.models.Settings;
import com.example.android.linkup.network.NetworkErrorMessages;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;



public class RegisterResponseListener implements Response.Listener<JSONObject> {
    @Override
    public void onResponse(JSONObject response) {

        try {
            OnRegisterSuccessEvent event = new OnRegisterSuccessEvent();
            EventBus.getDefault().post(event);
        } catch (Exception e) {
            Log.e(NetworkErrorMessages.REGISTER_TAG, e.getMessage());
            e.printStackTrace();
        }
    }

    public static class OnLoginSuccessEvent {
        public Profile profile;
        public Settings settings;
    }

    public class OnRegisterSuccessEvent {
    }
}