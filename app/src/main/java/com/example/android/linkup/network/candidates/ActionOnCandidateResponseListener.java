package com.example.android.linkup.network.candidates;

import com.android.volley.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;


public class ActionOnCandidateResponseListener implements Response.Listener<JSONObject>{
    @Override
    public void onResponse(JSONObject response) {
        // TODO: handle response
        EventBus.getDefault().post(new OnActionSuccessEvent());
    }

    public static class OnActionSuccessEvent {
    }
}
