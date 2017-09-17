package com.example.android.linkup.network.get_profile;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.WebServiceManager;

import org.greenrobot.eventbus.EventBus;

public class GetProfileErrorListener implements Response.ErrorListener  {
    @Override
    public void onErrorResponse(VolleyError error) {
        EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("GET PROFILE ERROR"));
    }
}
