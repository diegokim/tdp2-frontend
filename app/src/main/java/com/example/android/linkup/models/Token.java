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
import com.example.android.linkup.network.NotificationToken.SaveNotificationTokenResponseListener;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.settings.SaveSettingsResponseListener;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joaquin on 28/10/17.
 */

public class Token {

    public String token;

    public Token() {

    }

    public void updateToken(final Context context) {
        JSONObject params = new JSONObject();

        try {
            params.put("registrationToken",token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Token new_token = new Token();
        new_token.update(this);

        CustomJsonObjectRequest objectRequest = new CustomJsonObjectRequest(Request.Method.PATCH, NetworkConfiguration.getInstance().serverAddr + "/users/me/settings", params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                SaveNotificationTokenResponseListener.SaveNotificationTokenSuccessEvent event = new SaveNotificationTokenResponseListener.SaveNotificationTokenSuccessEvent(new_token);
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

    }

    public void update(Token t) {
        this.token = t.token;
    }
}
