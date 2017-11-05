package com.example.android.linkup.network.NotificationToken;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.WebServiceManager;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by joaquin on 28/10/17.
 */

public class SaveNotificationTokenErrorListener implements Response.ErrorListener {
    @Override
    public void onErrorResponse(VolleyError error) {
        EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("Token error! :s"));
    }
}
