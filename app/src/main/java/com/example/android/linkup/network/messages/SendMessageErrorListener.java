package com.example.android.linkup.network.messages;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.WebServiceManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by joaquin on 12/11/17.
 */

public class SendMessageErrorListener implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("Ups! Ha ocurrido un error :("));
    }
}
