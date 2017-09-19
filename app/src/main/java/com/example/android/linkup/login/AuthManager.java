package com.example.android.linkup.login;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.network.NetworkConfiguration;


import com.example.android.linkup.network.WebServiceManager;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;


public class AuthManager {

    private static String TAG = "Auth Manager";
    private static AuthManager instance;
    private final BaseActivity activity;
    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;




    private AuthManager(BaseActivity activity) {
        mAuth = FirebaseAuth.getInstance();
        this.activity = activity;
    }

    public static AuthManager getInstance (BaseActivity activity) {
        if (instance == null) {
            instance = new AuthManager(activity);
        }
        return instance;
    }

    public void signOut() {
        Log.d(TAG,"SignOut...");
        mAuth.signOut();
        LoginManager.getInstance().logOut();
    }



    public void initializeFacebookLoginButton(LoginButton loginButton) {
        mCallbackManager = CallbackManager.Factory.create();
        loginButton.setReadPermissions("email",
                "public_profile","user_photos",
                "user_birthday","user_location"
                ,"user_actions.books","user_actions.fitness",
                "user_actions.music","user_actions.news", "user_work_history",
                "user_actions.video", "user_posts");

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                activity.showProgressDialog();
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.d(TAG,loginResult.getAccessToken().getToken());
                NetworkConfiguration.getInstance().accessToken = loginResult.getAccessToken().getToken();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                NetworkConfiguration.getInstance().accessToken = "-1";
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                NetworkConfiguration.getInstance().accessToken = "-1";
            }
        });
    }


    private void handleFacebookAccessToken(AccessToken token) {

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity,  new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        WebServiceManager.getInstance(activity).login();
                    }
                });
    }

    public void passActivityResultToFacebookSDK (int requestCode, int resultCode, Intent data) {
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public boolean userIsLoggedIn() {
        if( AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                return true;
            }
        }
        return false;
    }

    public String getAccessToken() {
        return AccessToken.getCurrentAccessToken().getToken();
    }

}
