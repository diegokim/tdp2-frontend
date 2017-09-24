package com.example.android.linkup.network.preferences;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.WebServiceManager;

import org.greenrobot.eventbus.EventBus;


public class SavePreferencesErrorListener implements Response.ErrorListener{
    @Override
    public void onErrorResponse(VolleyError error) {
        EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("Ups! Ha ocurrido un error :("));
    }
}
