package com.example.android.linkup.network.preferences;

import com.android.volley.Response;
import com.example.android.linkup.models.Settings;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.utils.JSONParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

public class SavePreferencesResponseListener implements Response.Listener<JSONObject> {
    @Override
    public void onResponse(JSONObject response) {
        Settings settings = JSONParser.getSettings(response);
        if (settings != null) {
            EventBus.getDefault().post(new SavePreferencesSuccessEvent(settings));
        } else {
            EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("Ups! Ha ocurrido un error :("));
        }
    }

    public static class SavePreferencesSuccessEvent {
        public Settings settings;
        public SavePreferencesSuccessEvent( Settings settings ) {
            this.settings = settings;
        }
    }
}
