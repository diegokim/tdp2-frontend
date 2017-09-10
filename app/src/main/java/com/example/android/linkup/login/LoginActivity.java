package com.example.android.linkup.login;


import android.content.Context;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.content.Intent;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.R;
import com.example.android.linkup.network.Command;
import com.example.android.linkup.network.NetworkConfiguration;
import com.example.android.linkup.network.NetworkRequestQueue;
import com.example.android.linkup.network.ToastErrorCommand;
import com.example.android.linkup.network.login.LoginRequestGenerator;
import com.example.android.linkup.profile.ProfileActivity;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;

import com.facebook.appevents.AppEventsLogger;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONObject;

public class LoginActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "FacebookLogin";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView mStatusTextView;
    private TextView mDetailTextView;

    private CallbackManager mCallbackManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);

        initializeViews();

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
                // ...
            }
        };
        // [END initialize_auth]

        // [START initialize_fblogin]
        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email",
                "public_profile","user_photos",
                "user_birthday","user_location"
                ,"user_actions.books","user_actions.fitness",
                "user_actions.music","user_actions.news",
                "user_actions.video", "user_posts");
        //,"publish_actions", "user_about_me","user_education_history","user_friends","user_games_activity","user_hometown","user_likes","user_posts","user_relationship_details","user_relationships","user_religion_politics","user_status","user_tagged_places","user_videos","user_website","user_work_history","user_events","read_custom_friendlists"
        final Context context = this.getApplicationContext();

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.d(TAG,loginResult.getAccessToken().getToken());
                NetworkConfiguration.getInstance().accessToken = loginResult.getAccessToken().getToken();

                Command onErrorCommand = new ToastErrorCommand(context, NetworkConfiguration.SERVER_REQUEST_ERROR);
                Command onSuccessCommand = new ToastErrorCommand(context, "SUCCESS");

                Request request = LoginRequestGenerator.generate(onSuccessCommand, onErrorCommand);
                NetworkRequestQueue.getInstance(context).addToRequestQueue(request);
                //request.setRetryPolicy(new DefaultRetryPolicy(10000,1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                //NetworkRequestQueue.getInstance(context).addToRequestQueue(request);
//                Intent intent = new Intent(context, ProfileActivity.class);
//                startActivity(intent);

            }

            @Override
            public void onCancel() {
                Log.d(TAG, "facebook:onCancel");
                NetworkConfiguration.getInstance().accessToken = "-1";
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "facebook:onError", error);
                NetworkConfiguration.getInstance().accessToken = "-1";
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        });
        // [END initialize_fblogin]
    }

    private void initializeViews () {
        // Views
        mStatusTextView = (TextView) findViewById(R.id.status);
        mDetailTextView = (TextView) findViewById(R.id.detail);
        findViewById(R.id.button_facebook_signout).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        updateUI(mAuth.getCurrentUser());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    // [START auth_with_facebook]
    private void handleFacebookAccessToken(AccessToken token) {
        Log.d(TAG, "handleFacebookAccessToken:" + token);
        // [START_EXCLUDE silent]
        showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        hideProgressDialog();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_facebook]

    public void signOut() {
        mAuth.signOut();
        LoginManager.getInstance().logOut();

        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        if (user != null) {
            mStatusTextView.setText(getString(R.string.facebook_status_fmt, user.getDisplayName()));
            mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));

            findViewById(R.id.button_facebook_login).setVisibility(View.GONE);
            findViewById(R.id.button_facebook_signout).setVisibility(View.VISIBLE);
        } else {
            mStatusTextView.setText(R.string.signed_out);
            mDetailTextView.setText(null);

            findViewById(R.id.button_facebook_login).setVisibility(View.VISIBLE);
            findViewById(R.id.button_facebook_signout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_facebook_signout) {
            signOut();
        }
    }



}
