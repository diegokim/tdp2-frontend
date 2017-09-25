package com.example.android.linkup.network.get_profile;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.WebServiceManager;

import org.greenrobot.eventbus.EventBus;

public class GetProfileErrorListener implements Response.ErrorListener  {
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(NetworkErrorMessages.GET_PROFILE_TAG, error.toString());
        EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent(NetworkErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER));
    }
}
