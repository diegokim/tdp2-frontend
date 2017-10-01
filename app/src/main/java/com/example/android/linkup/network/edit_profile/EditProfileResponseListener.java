package com.example.android.linkup.network.edit_profile;

import com.android.volley.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;


public class EditProfileResponseListener implements Response.Listener<JSONObject> {
    @Override
    public void onResponse(JSONObject response) {
        EventBus.getDefault().post(new EditProfileSuccessEvent());
    }

    public class EditProfileSuccessEvent {}
}
