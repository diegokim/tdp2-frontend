package com.example.android.linkup.network.deleteAccount;

import android.util.Log;

import com.android.volley.Request;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkErrorMessages;
import com.example.android.linkup.network.messages.SendMessageErrorListener;
import com.example.android.linkup.network.messages.SendMessageResponseListener;

import org.json.JSONObject;

/**
 * Created by joaquin on 13/11/17.
 */

public class DeleteAccountRequestGenerator {

    private static final int DELETE_METHOD = Request.Method.DELETE;
    private static final String DELETE_ACCOUNT_ENDPOINT = "/users/me/account";

    public static Request generate() {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += DELETE_ACCOUNT_ENDPOINT;
        JSONObject obj = new JSONObject();

        DeleteAccountResponseListener responseListener = new DeleteAccountResponseListener();
        DeleteAccountErrorListener errorListener = new DeleteAccountErrorListener();
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(DELETE_METHOD, url, obj, responseListener, errorListener);
        return request;
    }
}
