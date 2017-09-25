package com.example.android.linkup.network.settings;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.WebServiceManager;

import org.greenrobot.eventbus.EventBus;


public class SaveSettingsErrorListener implements Response.ErrorListener{
    @Override
    public void onErrorResponse(VolleyError error) {
        EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("Ups! Ha ocurrido un error :("));
    }
}
