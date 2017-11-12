package com.example.android.linkup.network;


import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.android.linkup.models.Token;
import com.example.android.linkup.network.NotificationToken.SaveNotificationTokenResponseListener;
import com.example.android.linkup.network.candidates.GetCandidatesRequestGenerator;
import com.example.android.linkup.network.candidates.ActionOnCandidateRequestGenerator;
import com.example.android.linkup.network.candidates.GetLinksRequestGenerator;
import com.example.android.linkup.network.edit_profile.EditProfileRequestGenerator;
import com.example.android.linkup.network.get_profile.GetProfileRequestGenerator;
import com.example.android.linkup.network.location_update.UpdateLocationRequestGenerator;
import com.example.android.linkup.network.login.LoginRequestGenerator;
import com.example.android.linkup.network.messages.SendMessageResquestGenerator;
import com.example.android.linkup.network.register.RegisterData;
import com.example.android.linkup.network.register.RegisterRequestGenerator;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

public class WebServiceManager {
    private static WebServiceManager instance;
    private Context context;

    public static WebServiceManager getInstance (Context context) {
        if (instance == null) {
            instance = new WebServiceManager();
            instance.context = context;
        }
        return instance;
    }

    public static WebServiceManager getInstance() {
        return instance;
    }

    public void sendRequest (Request request) {
        NetworkRequestQueue.getInstance(context).addToRequestQueue(request);
    }

    public void login() {
        sendRequest(LoginRequestGenerator.generate());
    }

    public void updateProfile(String profilePhotoSelected, String description) {
        Request request = EditProfileRequestGenerator.generate(description, profilePhotoSelected);
        sendRequest(request);
    }

    public void sendChatMessage(String message,String idUser_to) {
        Request request = SendMessageResquestGenerator.generate(message,idUser_to);
        sendRequest(request);
    }

    public void updateLocation(Location location) {
        Request request = UpdateLocationRequestGenerator.generate(location);
        sendRequest(request);
    }

    public void getCandidates() {
        Request request = GetCandidatesRequestGenerator.generate();
        sendRequest(request);
    }

    public void reject(String id) {
        Request request = ActionOnCandidateRequestGenerator.generate(id, "reject");
        sendRequest(request);
    }

    public void link(String id) {
        Request request = ActionOnCandidateRequestGenerator.generate(id, "link");
        sendRequest(request);
    }

    public void superLink(String id) {
        Request request = ActionOnCandidateRequestGenerator.generate(id, "super-link");
        sendRequest(request);
    }

    public void getLinks() {
        Request request = GetLinksRequestGenerator.generate();
        sendRequest(request);
    }

    public void deleteLink(String id) {
        Request deleteLink = ActionOnCandidateRequestGenerator.generate(id, "delete");
        sendRequest(deleteLink);
    }

    public void blockUser(String id) {
        Request blockUser = ActionOnCandidateRequestGenerator.generate(id,"block");
        sendRequest(blockUser);
    }

    public void reportUser(String id, String reason) {
        Request request = ActionOnCandidateRequestGenerator.generate(id,"report",reason);
        sendRequest(request);
    }

    public static class ErrorMessageEvent {
        public String message;
        public ErrorMessageEvent (String message) {
            this.message = message;
        }
    }
    public void getProfile () {
        Request getProfile = GetProfileRequestGenerator.generate();
        sendRequest(getProfile);
    }

    public void register (RegisterData data) {
        Request registerRequest = RegisterRequestGenerator.generate(data);
        sendRequest(registerRequest);
    }

    public void updateToken(String token) {
        Log.d("Notification","Updating token from WebService Manager");

        JSONObject params = new JSONObject();

        try {
            params.put("registrationToken",token);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final Token new_token = new Token();
        new_token.token = token;

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
 }
