package com.example.android.linkup.login;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;

import android.widget.Toast;

import com.example.android.linkup.BaseActivity;
import com.example.android.linkup.MainActivity;
import com.example.android.linkup.R;

import com.example.android.linkup.login.register_parameters_selection.SelectPhotosCommand;
import com.example.android.linkup.network.WebServiceManager;
import com.example.android.linkup.network.login.LoginResponseListener;
import com.example.android.linkup.network.register.RegisterRequestGenerator;
import com.example.android.linkup.profile.ProfileFragment;
import com.facebook.FacebookSdk;

import com.facebook.login.widget.LoginButton;

import com.facebook.appevents.AppEventsLogger;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;



public class LoginActivity extends BaseActivity {

    private static final String TAG = "FacebookLogin";
    private AuthManager authManager;
    private LoginButton loginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        authManager = AuthManager.getInstance(this);
        findViews();
        final Activity activity = LoginActivity.this;
        authManager.initializeFacebookLoginButton(loginButton);


    }

    private void findViews() {
        loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        authManager.passActivityResultToFacebookSDK(requestCode, resultCode, data);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        if (authManager.userIsLoggedIn()) {
            Intent intent = new Intent (this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe
    public void onSuccessfulLoginEvent(LoginResponseListener.PhotosEvent photosEvent) {
        hideProgressDialog();
        Photos photos = photosEvent.photos;
        SelectPhotosCommand command = new SelectPhotosCommand(LoginActivity.this, photos);
        command.excecute();
    };

    @Subscribe
    public void onErrorMessageEvent(WebServiceManager.ErrorMessageEvent error) {
        hideProgressDialog();
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(this, error.message, duration);
        toast.show();
    }

    @Subscribe
    public void onRegisterSuccessEvent (RegisterRequestGenerator.RegisterResponseListener.RegisterSuccessEvent event) {
        Intent intent = new Intent(this, ProfileFragment.class);
        startActivity(intent);
        finish();
    }
}
