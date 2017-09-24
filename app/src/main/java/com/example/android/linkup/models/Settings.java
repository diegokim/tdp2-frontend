package com.example.android.linkup.models;

import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

public class Settings {
    public int age_from = 18;
    public int age_to = 99;
    public int range = 100;
    public boolean just_friends;
    public boolean visible = true;
    public String[] interested_in = new String[]{"MEN","WOMEN"};
    public boolean pareja = true;
    public boolean hombres = true;
    public boolean mujeres = true;
    public boolean solo_amigos = false;
    public boolean invisible;

    public Settings (){

    }

    public void updateSettings(Context context) {
        Log.d("Settings: ", "Updating.....");
        JSONObject params = new JSONObject();
        JSONObject distRange = new JSONObject();
        JSONObject ageRange = new JSONObject();
        try {
            distRange.put("min",1);
            distRange.put("max",range);

            ageRange.put("min",age_from);
            ageRange.put("max",age_to);

            params.put("disRange",distRange);
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

            CustomJsonObjectRequest objectRequest = new CustomJsonObjectRequest(Request.Method.PATCH, NetworkConfiguration.getInstance().serverAddr + "/settings", params, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.d("Settings: ",response.toString());
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Settings: ","error");
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
    }
}
