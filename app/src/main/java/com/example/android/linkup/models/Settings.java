package com.example.android.linkup.models;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;

import org.json.JSONObject;

public class Settings {
    public int age_from = 18;
    public int age_to = 99;
    public int range = 100;
    public boolean just_friends;
    public String[] interested_in = new String[]{"MEN","WOMEN"};
    public boolean pareja = true;
    public boolean hombres = true;
    public boolean mujeres = true;
    public boolean solo_amigos = false;

    public Settings (){
    }

    public void updateSettings() {
        CustomJsonObjectRequest objectRequest = new CustomJsonObjectRequest(Request.Method.GET, NetworkConfiguration.getInstance().serverAddr + "/settings", new JSONObject(), new Response.Listener<JSONObject>() {
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
    }
}
