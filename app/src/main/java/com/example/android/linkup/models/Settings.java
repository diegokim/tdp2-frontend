package com.example.android.linkup.models;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.NetworkRequestQueue;
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
            CustomJsonObjectRequest objectRequest = new CustomJsonObjectRequest(Request.Method.PATCH, NetworkConfiguration.getInstance().serverAddr + "/users/me/settings", params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    SaveSettingsResponseListener.SaveSettingsSuccessEvent event = new SaveSettingsResponseListener.SaveSettingsSuccessEvent(settings);
                   EventBus.getDefault().post(event);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(NetworkErrorMessages.SETTINGS_TAG,error.toString());
                    WebServiceManager.ErrorMessageEvent event = new WebServiceManager.ErrorMessageEvent(NetworkErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER);
                    EventBus.getDefault().post(event);
                }
            });

            objectRequest.setRetryPolicy(new DefaultRetryPolicy(10000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

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
        this.hombres = s.hombres;
        this.mujeres = s.mujeres;
        this.pareja = s.pareja;
        this.just_friends = s.just_friends;
        this.solo_amigos =s.solo_amigos;
    }
}
