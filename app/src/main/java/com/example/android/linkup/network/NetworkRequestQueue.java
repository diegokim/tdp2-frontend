package com.example.android.linkup.network;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/* Singleton to access the request queue */
public class NetworkRequestQueue {
    private static NetworkRequestQueue instance;
    private RequestQueue requestQueue;
    private static Context context;

    private NetworkRequestQueue(Context context) {
        this.context = context;
        requestQueue = getRequestQueue();
    }

    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public static synchronized NetworkRequestQueue getInstance(Context context) {
        if ( instance == null ) {
            instance = new NetworkRequestQueue(context);
        }
        return instance;
    }

    /* Adding a request automatically sends it */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }
}