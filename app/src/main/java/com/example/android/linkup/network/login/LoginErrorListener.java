package com.example.android.linkup.network.login;


import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.WebServiceManager;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

public class LoginErrorListener implements Response.ErrorListener {


    public LoginErrorListener() {
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(NetworkErrorMessages.LOGIN_TAG, error.toString());
        String message = NetworkErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER;

        NetworkResponse response = error.networkResponse;
        if(response != null && response.data != null ) {
            try {
                // TODO: handle other type of error
                Log.e(NetworkErrorMessages.LOGIN_TAG,response.data.toString());
                String body = new String(error.networkResponse.data,"UTF-8");
                Log.d("ServerResponse",body);
            } catch (UnsupportedEncodingException e) {
                Log.e(NetworkErrorMessages.LOGIN_TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent(message));
    }


}
