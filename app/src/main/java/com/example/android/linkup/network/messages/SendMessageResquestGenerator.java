package com.example.android.linkup.network.messages;

import com.android.volley.Request;
import com.example.android.linkup.models.ActiveChatProfile;
import com.example.android.linkup.models.Session;
import com.example.android.linkup.network.CustomJsonObjectRequest;
import com.example.android.linkup.network.NetworkConfiguration;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by joaquin on 12/11/17.
 */

public class SendMessageResquestGenerator {
    private static final int SEND_MESSAGE_METHOD = Request.Method.POST;
    private static final String SEND_MESSAGE_ENDPOINT_init = "/users/";
    private static final String SEND_MESSAGE_ENDPOINT_end = "/chats/message";

    public static Request generate(String message,String idUser_to) {
        String url = NetworkConfiguration.getInstance().serverAddr;
        url += SEND_MESSAGE_ENDPOINT_init+idUser_to+SEND_MESSAGE_ENDPOINT_end;
        JSONObject obj = new JSONObject();
        try {
            obj.put("message",message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SendMessageResponseListener responseListener = new SendMessageResponseListener();
        SendMessageErrorListener errorListener = new SendMessageErrorListener();
        CustomJsonObjectRequest request = new CustomJsonObjectRequest(SEND_MESSAGE_METHOD, url, obj, responseListener, errorListener);
        return request;
    }
}
