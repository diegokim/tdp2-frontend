package com.example.android.linkup.network;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.greenrobot.eventbus.EventBus;


public class ServerErrorListener implements Response.ErrorListener {

    private String tag;

    public ServerErrorListener(String tag) {
        this.tag = tag;
    }
    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e(tag, error.toString());
        if (error.getMessage() != null) {
            Log.e(tag, error.getMessage());
        }
        if (error.getStackTrace() != null) {
            for (StackTraceElement element: error.getStackTrace())
                Log.e(tag,element.toString());
        }
        error.printStackTrace(System.err);
        EventBus.getDefault().post(new WebServiceManager.ErrorMessageEvent(NetworkErrorMessages.ERROR_COMMUNICATING_WITH_THE_SERVER));
    }
}
