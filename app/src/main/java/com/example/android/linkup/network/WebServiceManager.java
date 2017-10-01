package com.example.android.linkup.network;


import android.content.Context;
import android.location.Location;

import com.android.volley.Request;
import com.example.android.linkup.network.candidates.GetCandidatesRequestGenerator;
import com.example.android.linkup.network.candidates.ActionOnCandidateRequestGenerator;
import com.example.android.linkup.network.candidates.GetLinksRequestGenerator;
import com.example.android.linkup.network.edit_profile.EditProfileRequestGenerator;
import com.example.android.linkup.network.get_profile.GetProfileRequestGenerator;
import com.example.android.linkup.network.location_update.UpdateLocationRequestGenerator;
import com.example.android.linkup.network.login.LoginRequestGenerator;
import com.example.android.linkup.network.register.RegisterData;
import com.example.android.linkup.network.register.RegisterRequestGenerator;

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
        Request request = ActionOnCandidateRequestGenerator.generate(id,"block",reason);
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
 }
