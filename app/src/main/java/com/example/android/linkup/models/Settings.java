package com.example.android.linkup.models;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.NetworkRequestQueue;
import com.example.android.linkup.network.ServerErrorListener;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.settings.SaveSettingsResponseListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class Settings {
    public int age_from;
    public int age_to;
    public int range;
    public boolean just_friends;
    public boolean pareja;
    public boolean hombres;
    public boolean mujeres;
    public boolean solo_amigos;
    public boolean invisible;
    public boolean notifications;
    public String accountType;
    public int superLinksCount;


    public Settings (){

    }

    public void updateSettings(final Context context) {
        JSONObject params = new JSONObject();
        JSONObject distRange = new JSONObject();
        JSONObject ageRange = new JSONObject();
        try {
            distRange.put("min",0);
            distRange.put("max",range);

            ageRange.put("min",age_from);
            ageRange.put("max",age_to);

            params.put("distRange",distRange);
            params.put("ageRange",ageRange);

            params.put("invisible",invisible);

            params.put("notifications",notifications);


            params.put("accountType", accountType);

            if (solo_amigos) {
                params.put("interestType","friends");
            } else {
                if (hombres && mujeres) {
                    params.put("interestType","both");
                } else {
                    if (hombres) {
                        params.put("interestType","male");
                    } else {
                        params.put("interestType","female");
                    }
                }
            }
            final Settings settings = new Settings();
            settings.update(this);
            CustomJsonObjectRequest objectRequest = new CustomJsonObjectRequest(Request.Method.PATCH, NetworkConfiguration.getInstance().serverAddr + "/users/me/settings", params, new SaveSettingsResponseListener()
            , new ServerErrorListener(NetworkErrorMessages.SETTINGS_TAG));

            NetworkRequestQueue.getInstance(context).addToRequestQueue(objectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void update(Settings s) {
        this.age_from = s.age_from;
        this.age_to = s.age_to;
        this.range = s.range;
        this.invisible = s.invisible;
        this.notifications = s.notifications;
        this.accountType = s.accountType;
        this.hombres = s.hombres;
        this.mujeres = s.mujeres;
        this.pareja = s.pareja;
        this.just_friends = s.just_friends;
        this.solo_amigos =s.solo_amigos;
        this.superLinksCount = s.superLinksCount;
    }
}
