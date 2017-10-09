package com.example.android.linkup.network.candidates;

import com.android.volley.Response;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;


public class ActionOnCandidateResponseListener implements Response.Listener<JSONObject>{

    private String action;

    public ActionOnCandidateResponseListener(String action) {
        this.action = action;
    }

    @Override
    public void onResponse(JSONObject response) {
        // TODO: handle response
        EventBus.getDefault().post(new OnActionSuccessEvent(action));
    }

    public static class OnActionSuccessEvent {
        public String action;
        public OnActionSuccessEvent(String action) {
            this.action = action;
        }
    }
}
