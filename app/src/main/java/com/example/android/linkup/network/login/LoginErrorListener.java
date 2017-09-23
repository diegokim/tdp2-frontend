package com.example.android.linkup.network.login;


import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.android.linkup.network.WebServiceManager;

import org.greenrobot.eventbus.EventBus;

import java.io.UnsupportedEncodingException;

public class LoginErrorListener implements Response.ErrorListener {


    public LoginErrorListener() {
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        String message = "no hemos podido comunicarnos con el servidor";

        NetworkResponse response = error.networkResponse;
        if(response != null && response.data != null ) {
            try {
                // TODO: handle other type of error
                String body = new String(error.networkResponse.data,"UTF-8");
                Log.d("ServerResponse",body);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent(message));
    }


}
