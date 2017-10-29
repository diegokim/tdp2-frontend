package com.example.android.linkup.notifications;

import android.app.Service;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.models.Settings;
import com.example.android.linkup.models.Token;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.NetworkRequestQueue;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.settings.SaveSettingsResponseListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by joaquin on 28/10/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "Notifications";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        Log.d(TAG,"Enviando Token a servidor -> "+token);

        if (WebServiceManager.getInstance() != null) {
            Log.d(TAG,"Updating token...");
            WebServiceManager.getInstance().updateToken(token);
            Log.d(TAG,"Token updated: "+token);
        } else {
            Log.d(TAG,"Webservice Manager null");
        }
    }
}
