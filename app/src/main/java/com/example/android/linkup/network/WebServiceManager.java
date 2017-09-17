package com.example.android.linkup.network;


import android.content.Context;

import com.android.volley.Request;
import com.example.android.linkup.network.get_profile.GetProfileRequestGenerator;
import com.example.android.linkup.network.login.LoginRequestGenerator;
import com.example.android.linkup.network.register.RegisterData;
import com.example.android.linkup.network.register.RegisterRequestGenerator;

import org.greenrobot.eventbus.EventBus;

public class WebServiceManager {
    private static WebServiceManager instance;
    private EventBus eventBus;
    private Context context;

    private WebServiceManager() {
        eventBus = EventBus.getDefault();
    }

    public static WebServiceManager getInstance (Context context) {
        if (instance == null) {
            instance = new WebServiceManager();
            instance.context = context;
        }
        return instance;
    }

    public void login () {
        Request request = LoginRequestGenerator.generate();
        NetworkRequestQueue.getInstance(context).addToRequestQueue(request);
    }

    public static class ErrorMessageEvent {
        public String message;
        public ErrorMessageEvent (String message) {
            this.message = message;
        }
    }
    public void getProfile () {
        Request getProfile = GetProfileRequestGenerator.generate();
        NetworkRequestQueue.getInstance(context).addToRequestQueue(getProfile);
    }

    public void register (RegisterData data) {
        Request registerRequest = RegisterRequestGenerator.generate(data);
        NetworkRequestQueue.getInstance(context).addToRequestQueue(registerRequest);
    }
 }
