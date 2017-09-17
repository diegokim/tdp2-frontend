package com.example.android.linkup.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.MainActivity;
import com.example.android.linkup.R;
import com.example.android.linkup.network.ChangeActivityCommand;
import com.example.android.linkup.login.photo_selection.SelectPhotosCommand;
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
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import com.facebook.appevents.AppEventsLogger;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class LoginActivity extends BaseActivity implements View.OnClickListener, Observer{

    private static final String TAG = "FacebookLogin";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private TextView mStatusTextView;
    private TextView mDetailTextView;

    private CallbackManager mCallbackManager;

    private Photos photosToSelectFrom;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
/*
        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        if ( sharedPref.getBoolean(ProfileActivity.LOGGED_IN, false)) {
            NetworkConfiguration.getInstance().accessToken = sharedPref.getString(ProfileActivity.ACCESS_TOKEN,"");
            Intent intent = new Intent(this, ProfileActivity.class);
            startActivity(intent);
            finish();
        }

        photosToSelectFrom = new Photos();
        photosToSelectFrom.addObserver(this);
*/

        initializeViews();
        initializeFirebaseAuth();
        initializeFacebookLoginButton();

    }


    private void initializeFacebookLoginButton() {
        mCallbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email",
                "public_profile","user_photos",
                "user_birthday","user_location"
                ,"user_actions.books","user_actions.fitness",
                "user_actions.music","user_actions.news", "user_work_history",
                "user_actions.video", "user_posts");

        final Context context = LoginActivity.this;
        final LayoutInflater inflater = getLayoutInflater();
        final Activity activity = LoginActivity.this;

        loginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
                Log.d(TAG,loginResult.getAccessToken().getToken());
                NetworkConfiguration.getInstance().accessToken = loginResult.getAccessToken().getToken();

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
    }

    private void initializeFirebaseAuth() {
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
    }

    private void initializeViews () {
        // Views
        //mStatusTextView = (TextView) findViewById(R.id.status);
        //mDetailTextView = (TextView) findViewById(R.id.detail);
        findViewById(R.id.button_facebook_signout).setOnClickListener(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        updateUI(mAuth.getCurrentUser());
        //Si estoy loggeado en facebook
        if( AccessToken.getCurrentAccessToken() != null && Profile.getCurrentProfile() != null) {
            Log.d(TAG,"Estoy loggeado con facebook!");
            //Si estoy loggeado en firebase
            if (FirebaseAuth.getInstance().getCurrentUser() != null) {
                Log.d(TAG,"Estoy loggeado con firebase!");
                Intent intent = new Intent (this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                Log.d(TAG,"No estoy loggeado con Firebase");
            }
        } else {
            Log.d(TAG, "No estoy Loggeado con Facebook!");
        }
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
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                        //sendLoginRequest();
                    }
                });
    }
    // [END auth_with_facebook]


    private void sendLoginRequest () {

        Command onErrorCommand = new Command() {
            @Override
            public void excecute() {
                signOut();
            }
        };
        Command onSuccessCommand = new SelectPhotosCommand(this, getLayoutInflater(), photosToSelectFrom, LoginActivity.this);
        Command hideProgressBarCommand = new Command() {
            @Override
            public void excecute() {
                hideProgressDialog();
            }
        };

        Request request = LoginRequestGenerator.generate(onSuccessCommand, onErrorCommand, hideProgressBarCommand, this, photosToSelectFrom);
        NetworkRequestQueue.getInstance(this).addToRequestQueue(request);
    }

    public void signOut() {
        Log.d(TAG,"SignOut...");
        mAuth.signOut();
        LoginManager.getInstance().logOut();
        updateUI(null);
    }

    private void updateUI(FirebaseUser user) {
        hideProgressDialog();
        /*
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
        */
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.button_facebook_signout) {
            signOut();
        }
    }

    @Override
    public void update(Observable o, Object arg) {

    }
}
