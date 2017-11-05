package com.example.android.linkup.network.NotificationToken;

import com.android.volley.Response;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.models.Settings;
import com.example.android.linkup.models.Token;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.settings.SaveSettingsResponseListener;
import com.example.android.linkup.utils.JSONParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joaquin on 28/10/17.
 */

public class SaveNotificationTokenResponseListener implements Response.Listener<JSONObject>{
    @Override
    public void onResponse(JSONObject response) {
        Token token = JSONParser.getToken(response);
        if (token != null) {
            EventBus.getDefault().post(new SaveNotificationTokenResponseListener.SaveNotificationTokenSuccessEvent(token));
        } else {
            EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("Ups! Ha ocurrido un error :("));
        }
    }

    public static class SaveNotificationTokenSuccessEvent {
        public Token token;
        public SaveNotificationTokenSuccessEvent(Token token) {
            this.token = token;
        }
    }
}
