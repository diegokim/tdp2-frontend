package com.example.android.linkup.network.settings;

import com.android.volley.Response;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.models.Settings;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.utils.JSONParser;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

public class SaveSettingsResponseListener implements Response.Listener<JSONObject> {
    @Override
    public void onResponse(JSONObject response) {
        Settings settings = JSONParser.getSettings(response);
        if (settings != null) {
            Session.getInstance().mySettings.update(settings);
            EventBus.getDefault().post(new SaveSettingsSuccessEvent(settings));
        } else {
            EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent("Ups! Ha ocurrido un error :("));
        }
    }

    public static class SaveSettingsSuccessEvent {
        public Settings settings;
        public SaveSettingsSuccessEvent(Settings settings) {
            this.settings = settings;
        }
    }
}
